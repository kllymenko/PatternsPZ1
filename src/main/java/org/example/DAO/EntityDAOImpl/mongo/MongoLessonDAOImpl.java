package org.example.DAO.EntityDAOImpl.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.DAO.EntityDAO.LessonDAO;
import org.example.entities.Homework;
import org.example.entities.Lesson;
import org.example.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MongoLessonDAOImpl implements LessonDAO {

    private final MongoDatabase connection;
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "lesson";

    public MongoLessonDAOImpl(MongoDatabase connection) {
        this.connection = connection;
        this.collection = connection.getCollection(COLLECTION_NAME);
    }

    @Override
    public String insert(Lesson entity) {
        Document userDocument = mapLessonToDocument(entity);
        collection.insertOne(userDocument);
        return userDocument.getString("_id");
    }

    @Override
    public boolean update(Lesson entity) {
        Document query = new Document("_id", entity.getLessonId());
        Document update = new Document("$set", mapLessonToDocument(entity));
        return collection.updateOne(query, update).getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String id) {
        Document query = new Document("_id", id);
        return collection.deleteOne(query).getDeletedCount() > 0;
    }

    @Override
    public List<Lesson> findAll() {
        List<Lesson> lessonList = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document lessonDocument = cursor.next();
                lessonList.add(mapDocumentToLesson(lessonDocument));
            }
        }
        return lessonList;
    }

    @Override
    public Lesson findById(String id) {
        Document query = new Document("_id", id);
        Document lesssonDocument = collection.find(query).first();
        return mapDocumentToLesson(lesssonDocument);
    }

    @Override
    public boolean updateName(Lesson entity, String newName) {
        Document query = new Document("_id", entity.getLessonId());
        Document update = new Document("$set", new Document("name", newName));
        return collection.updateOne(query, update).getModifiedCount() > 0;
    }

    private Document mapLessonToDocument(Lesson lesson) {
        return new Document("_id", lesson.getLessonId())
                .append("name", lesson.getName())
                .append("topic", lesson.getTopic())
                .append("date", lesson.getDate().toString())
                .append("timeStart", lesson.getTimeStart().toString())
                .append("timeEnd", lesson.getTimeEnd().toString())
                .append("cabNum", lesson.getCabNum())
                .append("homework", mapHomeworkToDocument(lesson.getHomework()));
    }

    private Document mapHomeworkToDocument(Homework homework) {
        return new Document("_id", homework.getHomeworkId())
                .append("description", homework.getDescription())
                .append("due_datetime", homework.getDueDateTime().toString());
    }

    private Lesson mapDocumentToLesson(Document lessonDocument) {
        return new Lesson.Builder()
                .setLessonId(lessonDocument.getString("_id"))
                .setName(lessonDocument.getString("name"))
                .setTopic(lessonDocument.getString("topic"))
                .setDate(LocalDate.parse(lessonDocument.getString("date")))
                .setTimeStart(LocalTime.parse(lessonDocument.getString("timeStart")))
                .setTimeEnd(LocalTime.parse(lessonDocument.getString("timeEnd")))
                .setCabNum(lessonDocument.getInteger("cabNum"))
                .setHomework(mapDocumentToHomework(lessonDocument.get("homework", Document.class)))
                .build();
    }

    private Homework mapDocumentToHomework(Document homeworkDocument) {
        return new Homework.Builder()
                .setHomeworkId(homeworkDocument.getString("_id"))
                .setDescription(homeworkDocument.getString("description"))
                .setDueDateTime(LocalDateTime.parse(homeworkDocument.getString("due_datetime")))
                .build();
    }
}

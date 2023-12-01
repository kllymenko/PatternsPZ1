package org.example.DAO.EntityDAOImpl.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.example.DAO.EntityDAO.UserDAO;
import org.example.entities.Homework;
import org.example.entities.Lesson;
import org.example.entities.User;
import org.example.entities.enums.Role;
import org.example.entities.enums.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoUserDAOImpl implements UserDAO {

    private final MongoDatabase connection;
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "user";

    public MongoUserDAOImpl(MongoDatabase connection) {
        this.connection = connection;
        this.collection = connection.getCollection(COLLECTION_NAME);
    }


    @Override
    public String insert(User entity) {
        Document userDocument = mapUserToDocument(entity);
        collection.insertOne(userDocument);
        return userDocument.getString("_id");
    }

    @Override
    public boolean update(User entity) {
        Document query = new Document("_id", entity.getUserId());
        Document update = new Document("$set", mapUserToDocument(entity));
        return collection.updateOne(query, update).getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String id) {
        Document query = new Document("_id", id);
        return collection.deleteOne(query).getDeletedCount() > 0;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document userDocument = cursor.next();
                userList.add(mapDocumentToUser(userDocument));
            }
        }
        return userList;
    }

    @Override
    public User findById(String id) {
        Document query = new Document("_id", id);
        Document userDocument = collection.find(query).first();
        return mapDocumentToUser(userDocument);
    }

    @Override
    public boolean updatePassword(User entity, String newPassword) {
        Document query = new Document("_id", entity.getUserId());
        Document update = new Document("$set", new Document("password", newPassword));
        return collection.updateOne(query, update).getModifiedCount() > 0;
    }

    private Document mapUserToDocument(User user) {
        Document userDocument = new Document("_id", user.getUserId());
        userDocument.put("name", user.getName());
        userDocument.put("surname", user.getSurname());
        userDocument.put("phone", user.getPhone());
        userDocument.put("email", user.getEmail());
        userDocument.put("password", user.getPassword());
        userDocument.put("sex", user.getSex().toString());
        userDocument.put("role", user.getRole().toString());
        List<Document> lessons = new ArrayList<>();
        for (Map.Entry<Lesson, Integer> entry : user.getLessonsWithGrade().entrySet()) {
            Lesson lesson = entry.getKey();
            int grade = entry.getValue();

            Document lessonDocument = new Document("_id", lesson.getLessonId())
                    .append("name", lesson.getName())
                    .append("topic", lesson.getTopic())
                    .append("date", lesson.getDate().toString())
                    .append("timeStart", lesson.getTimeStart().toString())
                    .append("timeEnd", lesson.getTimeEnd().toString())
                    .append("cabNum", lesson.getCabNum())
                    .append("homework", mapHomeworkToDocument(lesson.getHomework()))
                    .append("grade", grade);

            lessons.add(lessonDocument);
        }

        userDocument.put("lessons", lessons);

        return userDocument;
    }

    private Document mapHomeworkToDocument(Homework homework) {
        return new Document("_id", homework.getHomeworkId())
                .append("description", homework.getDescription())
                .append("due_datetime", homework.getDueDateTime().toString());
    }

    private User mapDocumentToUser(Document userDocument) {
        if (userDocument == null) {
            return null;
        }

        User.Builder userBuilder = new User.Builder()
                .setUserId(userDocument.getString("_id"))
                .setName(userDocument.getString("name"))
                .setSurname(userDocument.getString("surname"))
                .setPhone(userDocument.getString("phone"))
                .setEmail(userDocument.getString("email"))
                .setPassword(userDocument.getString("password"))
                .setSex(Sex.valueOf(userDocument.getString("sex")))
                .setRole(Role.valueOf(userDocument.getString("role")));

        // Process lessons
        List<Document> lessonsDocuments = userDocument.getList("lessons", Document.class);
        Map<Lesson, Integer> lessonsWithGrade = new HashMap<>();
        for (Document lessonDocument : lessonsDocuments) {
            Lesson lesson = mapDocumentToLesson(lessonDocument);
            int grade = lessonDocument.getInteger("grade");
            lessonsWithGrade.put(lesson, grade);
        }
        userBuilder.setLessonsWithGrade(lessonsWithGrade);

        return userBuilder.build();
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

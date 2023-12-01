package org.example.DAO.EntityDAOImpl.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.DAO.EntityDAO.HomeworkDAO;
import org.example.entities.Homework;
import org.example.entities.Lesson;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MongoHomeworkDAOImpl implements HomeworkDAO {

    private final MongoDatabase connection;
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "homework";

    public MongoHomeworkDAOImpl(MongoDatabase connection) {
        this.connection = connection;
        this.collection = connection.getCollection(COLLECTION_NAME);
    }

    @Override
    public String insert(Homework entity) {
        Document userDocument = mapHomeworkToDocument(entity);
        collection.insertOne(userDocument);
        return userDocument.getString("_id");
    }

    @Override
    public boolean update(Homework entity) {
        Document query = new Document("_id", entity.getHomeworkId());
        Document update = new Document("$set", mapHomeworkToDocument(entity));
        return collection.updateOne(query, update).getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String id) {
        Document query = new Document("_id", id);
        return collection.deleteOne(query).getDeletedCount() > 0;
    }

    @Override
    public List<Homework> findAll() {
        List<Homework> homeworkList = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document homeworkDocument = cursor.next();
                homeworkList.add(mapDocumentToHomework(homeworkDocument));
            }
        }
        return homeworkList;
    }

    @Override
    public Homework findById(String id) {
        Document query = new Document("_id", id);
        Document homeworkDocument = collection.find(query).first();
        return mapDocumentToHomework(homeworkDocument);
    }

    @Override
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime) {
        Document query = new Document("_id", entity.getHomeworkId());
        Document update = new Document("$set", new Document("due_datetime", dueDateTime));
        return collection.updateOne(query, update).getModifiedCount() > 0;
    }

    private Homework mapDocumentToHomework(Document document) {
        return new Homework.Builder()
                .setHomeworkId(document.getString("_id"))
                .setDescription(document.getString("description"))
                .setDueDateTime(LocalDateTime.parse(document.getString("due_datetime")))
                .build();
    }
    private Document mapHomeworkToDocument(Homework homework) {
        return new Document("_id", homework.getHomeworkId())
                .append("description", homework.getDescription())
                .append("due_datetime", homework.getDueDateTime().toString());
    }
}

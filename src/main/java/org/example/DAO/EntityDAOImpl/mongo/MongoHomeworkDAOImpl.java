package org.example.DAO.EntityDAOImpl.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.DAO.EntityDAO.HomeworkDAO;
import org.example.entities.Homework;
import org.example.entities.Lesson;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

    //Aggregate by Due Date
    public List<Document> aggregateByDueDate() {
        List<Document> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", "$due_datetime").append("count", new Document("$sum", 1))),
                new Document("$sort", new Document("_id", 1))
        );

        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    //Aggregate by Description Length
    public List<Document> aggregateByDescriptionLength() {
        List<Document> pipeline = Arrays.asList(
                new Document("$project", new Document("descriptionLength", new Document("$strLenCP", "$description"))),
                new Document("$group", new Document("_id", "$descriptionLength").append("count", new Document("$sum", 1))),
                new Document("$sort", new Document("_id", 1))
        );

        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    //Find Overdue Homework
    public List<Document> findOverdueHomework() {
        LocalDateTime now = LocalDateTime.now();

        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("due_datetime", new Document("$lt", now.toString())))
        );

        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    public List<Document> aggregateByDueDateWithoutAggregationFramework() {
        Map<LocalDateTime, Integer> countByDueDate = new TreeMap<>();

        for (Document document : collection.find()) {
            LocalDateTime dueDate = LocalDateTime.parse(document.getString("due_datetime"));
            countByDueDate.put(dueDate, countByDueDate.getOrDefault(dueDate, 0) + 1);
        }

        return countByDueDate.entrySet()
                .stream()
                .map(entry -> new Document("_id", entry.getKey()).append("count", entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<Document> aggregateByDescriptionLengthWithoutAggregationFramework() {
        Map<Integer, Integer> countByDescriptionLength = new TreeMap<>();

        for (Document document : collection.find()) {
            String description = document.getString("description");
            int descriptionLength = description.length();
            countByDescriptionLength.put(descriptionLength, countByDescriptionLength.getOrDefault(descriptionLength, 0) + 1);
        }

        return countByDescriptionLength.entrySet()
                .stream()
                .map(entry -> new Document("_id", entry.getKey()).append("count", entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<Document> findOverdueHomeworkWithoutAggregationFramework() {
        LocalDateTime now = LocalDateTime.now();

        BasicDBObject query = new BasicDBObject();
        query.put("due_datetime", new BasicDBObject("$lt", now.toString()));

        FindIterable<Document> result = collection.find(query);

        List<Document> overdueHomework = new ArrayList<>();
        for (Document document : result) {
            overdueHomework.add(document);
        }

        return overdueHomework;
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

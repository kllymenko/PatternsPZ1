package org.example.DAO.EntityDAO;

import org.bson.Document;
import org.example.DAO.CRUDRepository;
import org.example.entities.Homework;

import java.sql.Timestamp;
import java.util.List;

public interface HomeworkDAO extends CRUDRepository<Homework> {
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime);
    List<Document> aggregateByDueDate();

    List<Document> aggregateByDescriptionLength();

    List<Document> findOverdueHomework();

    public List<Document> findOverdueHomeworkWithoutAggregationFramework();

    public List<Document> aggregateByDescriptionLengthWithoutAggregationFramework();

    public List<Document> aggregateByDueDateWithoutAggregationFramework();
}

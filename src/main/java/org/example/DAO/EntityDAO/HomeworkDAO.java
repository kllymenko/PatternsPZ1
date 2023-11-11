package org.example.DAO.EntityDAO;

import org.example.DAO.CRUDRepository;
import org.example.entities.Homework;

import java.sql.Timestamp;

public interface HomeworkDAO extends CRUDRepository<Homework> {
    public boolean updateDueDateTime(Homework entity, Timestamp dueDateTime);
}

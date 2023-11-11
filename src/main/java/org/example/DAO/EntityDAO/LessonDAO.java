package org.example.DAO.EntityDAO;

import org.example.DAO.CRUDRepository;
import org.example.entities.Lesson;

public interface LessonDAO extends CRUDRepository<Lesson> {
    public boolean updateName(Lesson entity, String newName);
}

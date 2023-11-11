package org.example.DAO.Factory;

import org.example.DAO.EntityDAO.HomeworkDAO;
import org.example.DAO.EntityDAO.LessonDAO;
import org.example.DAO.EntityDAO.UserDAO;

public interface DAOFactory {
    UserDAO getUserDAO();

    LessonDAO getLessonDAO();


    HomeworkDAO getHomeworkDAO();
}

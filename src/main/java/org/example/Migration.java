package org.example;

import org.example.DAO.EntityDAO.HomeworkDAO;
import org.example.DAO.EntityDAO.LessonDAO;
import org.example.DAO.EntityDAO.UserDAO;
import org.example.DAO.Factory.DAOFactory;
import org.example.DAO.Factory.DAOFactoryImpl;
import org.example.entities.Homework;
import org.example.entities.Lesson;
import org.example.entities.User;

import java.util.List;

public class Migration {
    DAOFactory factoryMySQL = new DAOFactoryImpl("mysql");
    DAOFactory factoryMongo = new DAOFactoryImpl("mongodb");

    public int userFromMongoToMysql() {
        UserDAO userDAOMongo = factoryMongo.getUserDAO();
        UserDAO userDAOMysql = factoryMySQL.getUserDAO();

        List<User> users = userDAOMongo.findAll();
        for (User u : users) {
            userDAOMysql.insert(u);
        }
        users = userDAOMongo.findAll();
        for (User u : users) {
            userDAOMongo.delete(u.getUserId());
        }
        users = userDAOMysql.findAll();
        return users.size();
    }

    public int userFromMySQLToMongo() {
        UserDAO userDAOMySQL = factoryMySQL.getUserDAO();
        UserDAO userDAOMongo = factoryMongo.getUserDAO();

        List<User> users = userDAOMySQL.findAll();
        for (User u : users) {
            userDAOMongo.insert(u);
        }
        users = userDAOMySQL.findAll();
        for (User u : users) {
            userDAOMySQL.delete(u.getUserId());
        }
        users = userDAOMongo.findAll();
        return users.size();
    }

    public int lessonFromMongoToMysql() {
        LessonDAO lessonDAOMongo = factoryMongo.getLessonDAO();
        LessonDAO lessonDAOMySQL = factoryMySQL.getLessonDAO();

        List<Lesson> lessons = lessonDAOMongo.findAll();
        for (Lesson l : lessons) {
            lessonDAOMySQL.insert(l);
        }
        lessons = lessonDAOMongo.findAll();
        for (Lesson l : lessons) {
            lessonDAOMongo.delete(l.getLessonId());
        }
        lessons = lessonDAOMySQL.findAll();
        return lessons.size();
    }

    public int lessonFromMySQLToMongo() {
        LessonDAO lessonDAOMySQL = factoryMySQL.getLessonDAO();
        LessonDAO lessonDAOMongo = factoryMongo.getLessonDAO();

        List<Lesson> lessons = lessonDAOMySQL.findAll();
        for (Lesson l : lessons) {
            lessonDAOMongo.insert(l);
        }
        lessons = lessonDAOMongo.findAll();
        for (Lesson l : lessons) {
            lessonDAOMySQL.delete(l.getLessonId());
        }
        lessons = lessonDAOMongo.findAll();
        return lessons.size();
    }

    public int homeworkFromMongoToMysql() {
        HomeworkDAO homeworkDAOMongo = factoryMongo.getHomeworkDAO();
        HomeworkDAO homeworkDAOMySQL = factoryMySQL.getHomeworkDAO();

        List<Homework> homeworks = homeworkDAOMongo.findAll();
        for (Homework h : homeworks) {
            homeworkDAOMySQL.insert(h);
        }
        homeworks = homeworkDAOMongo.findAll();
        for (Homework h : homeworks) {
            homeworkDAOMongo.delete(h.getHomeworkId());
        }
        homeworks = homeworkDAOMySQL.findAll();
        return homeworks.size();
    }
    public int homeworkFromMySQLToMongo() {
        HomeworkDAO homeworkDAOMySQL = factoryMySQL.getHomeworkDAO();
        HomeworkDAO homeworkDAOMongo = factoryMongo.getHomeworkDAO();

        List<Homework> homeworks = homeworkDAOMySQL.findAll();
        for (Homework h : homeworks) {
            homeworkDAOMongo.insert(h);
        }
        homeworks = homeworkDAOMongo.findAll();
        for (Homework h : homeworks) {
            homeworkDAOMySQL.delete(h.getHomeworkId());
        }
        homeworks = homeworkDAOMongo.findAll();
        return homeworks.size();
    }
}

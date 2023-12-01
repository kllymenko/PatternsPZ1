package org.example.DAO.Factory;

import com.mongodb.client.MongoDatabase;
import org.example.DAO.ConnectionManager;
import org.example.DAO.ConnectionProperties;
import org.example.DAO.EntityDAO.*;
import org.example.DAO.EntityDAOImpl.mongo.MongoHomeworkDAOImpl;
import org.example.DAO.EntityDAOImpl.mongo.MongoLessonDAOImpl;
import org.example.DAO.EntityDAOImpl.mongo.MongoUserDAOImpl;
import org.example.DAO.EntityDAOImpl.mysql.MySQLHomeworkDAOImpl;
import org.example.DAO.EntityDAOImpl.mysql.MySQLLessonDAOImpl;
import org.example.DAO.EntityDAOImpl.mysql.MySQLUserDAOImpl;

import java.sql.Connection;

public class DAOFactoryImpl implements DAOFactory{

    private ConnectionManager connectionManager;
    private ConnectionProperties connectionProperties;

    public DAOFactoryImpl(String dbType) {
        connectionProperties = new ConnectionProperties(dbType);
        connectionManager = ConnectionManager.getInstance(connectionProperties);
    }

    @Override
    public UserDAO getUserDAO() {
        if ("mysql".equalsIgnoreCase(connectionProperties.getType())) {
            Connection connection = connectionManager.getMySQLConnection();
            return new MySQLUserDAOImpl(connection);
        } else if ("mongodb".equalsIgnoreCase(connectionProperties.getType())) {
            MongoDatabase connection = connectionManager.getMongoConnection();
            return new MongoUserDAOImpl(connection);
        } else {
            throw new UnsupportedOperationException("Unsupported database type");
        }
    }

    @Override
    public LessonDAO getLessonDAO() {
        if ("mysql".equalsIgnoreCase(connectionProperties.getType())) {
            Connection connection = connectionManager.getMySQLConnection();
            return new MySQLLessonDAOImpl(connection);
        } else if ("mongodb".equalsIgnoreCase(connectionProperties.getType())) {
            MongoDatabase connection = connectionManager.getMongoConnection();
            return new MongoLessonDAOImpl(connection);
        } else {
            throw new UnsupportedOperationException("Unsupported database type");
        }
    }


    @Override
    public HomeworkDAO getHomeworkDAO() {
        if ("mysql".equalsIgnoreCase(connectionProperties.getType())) {
            Connection connection = connectionManager.getMySQLConnection();
            return new MySQLHomeworkDAOImpl(connection);
        } else if ("mongodb".equalsIgnoreCase(connectionProperties.getType())) {
            MongoDatabase connection = connectionManager.getMongoConnection();
            return new MongoHomeworkDAOImpl(connection);
        } else {
            throw new UnsupportedOperationException("Unsupported database type");
        }
    }
}

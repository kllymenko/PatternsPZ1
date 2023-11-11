package org.example.DAO.Factory;

import org.example.DAO.ConnectionManager;
import org.example.DAO.ConnectionProperties;
import org.example.DAO.EntityDAO.*;
import org.example.DAO.EntityDAOImpl.*;

import java.sql.Connection;

public class DAOFactoryImpl implements DAOFactory{

    private ConnectionManager connectionManager;

    public DAOFactoryImpl() {
        ConnectionProperties connectionProperties = new ConnectionProperties();
        connectionManager = ConnectionManager.getInstance(connectionProperties);
    }

    @Override
    public UserDAO getUserDAO() {
        Connection connection = connectionManager.getConnection();
        return new UserDAOImpl(connection);
    }

    @Override
    public LessonDAO getLessonDAO() {
        Connection connection = connectionManager.getConnection();
        return new LessonDAOImpl(connection);
    }


    @Override
    public HomeworkDAO getHomeworkDAO() {
        Connection connection = connectionManager.getConnection();
        return new HomeworkDAOImpl(connection);
    }
}

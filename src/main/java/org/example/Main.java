package org.example;

import org.example.DAO.EntityDAO.HomeworkDAO;
import org.example.DAO.EntityDAO.LessonDAO;
import org.example.DAO.EntityDAO.UserDAO;
import org.example.DAO.EntityDAOImpl.UserDAOImpl;
import org.example.DAO.Factory.DAOFactory;
import org.example.DAO.Factory.DAOFactoryImpl;
import org.example.entities.Homework;
import org.example.entities.Lesson;
import org.example.entities.Schedule;
import org.example.entities.User;
import org.example.entities.enums.Role;
import org.example.entities.enums.Sex;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DAOFactory daoFactory = new DAOFactoryImpl();
        UserDAO userDAO= daoFactory.getUserDAO();
        LessonDAO lessonDAO = daoFactory.getLessonDAO();
        HomeworkDAO homeworkDAO = daoFactory.getHomeworkDAO();

        for (int i = 1; i <= 10; i++) {
            User user = new User.Builder("Name" + i, "Surname" + i, "Phone" + i, "Email" + i, "Password" + i, Sex.MALE)
                    .setRole("STUDENT")
                    .build();

            // Вставка користувача в базу даних
            int userId = userDAO.insert(user);
            System.out.println("User inserted with ID: " + userId);
        }

        // Отримання всіх користувачів та виведення їх на екран
        List<User> allUsers = userDAO.findAll();
        System.out.println("All Users:");
        for (User user : allUsers) {
            System.out.println(user.toString());
        }

        // Оновлення паролю користувача
        User userToUpdate = allUsers.get(0);
        String newPassword = "NewPassword";
        boolean passwordUpdated = userDAO.updatePassword(userToUpdate, newPassword);
        System.out.println("Password updated: " + passwordUpdated);

        // Видалення користувача
        int userIdToDelete = userToUpdate.getUserId();
        boolean userDeleted = userDAO.delete(userIdToDelete);
        System.out.println("User deleted: " + userDeleted);


        Lesson lesson = new Lesson.Builder()
                .setName("Test Lesson")
                .setDate(LocalDate.now())
                .setTimeStart(Time.valueOf(LocalTime.now()))
                .setTimeEnd(Time.valueOf(LocalTime.now().plusHours(1)))
                .setCabName(101)
                .setUsersInLesson(Role.STUDENT, new Schedule.Builder(userDAO.findAll().get(1).getUserId()).setGrade(5).setPresent(true).build())
                .setUsersInLesson(Role.STUDENT, new Schedule.Builder(userDAO.findAll().get(2).getUserId()).setGrade(5).setPresent(true).build())
                .build();
        System.out.println(lessonDAO.insert(lesson));
    }
}
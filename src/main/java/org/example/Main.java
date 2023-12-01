package org.example;


import lombok.Data;
import org.example.DAO.EntityDAO.HomeworkDAO;
import org.example.DAO.EntityDAO.LessonDAO;
import org.example.DAO.EntityDAO.UserDAO;
import org.example.DAO.Factory.DAOFactory;
import org.example.DAO.Factory.DAOFactoryImpl;
import org.example.entities.Homework;
import org.example.entities.Lesson;
import org.example.entities.User;
import org.example.entities.enums.Role;
import org.example.entities.enums.Sex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        DAOFactory factory = new DAOFactoryImpl("mysql");
        UserDAO userDAO = factory.getUserDAO();
        LessonDAO lessonDAO = factory.getLessonDAO();
        HomeworkDAO homeworkDAO = factory.getHomeworkDAO();

        User user = new User.Builder()
                .setName("John")
                .setSurname("Doe")
                .setPhone("123456789")
                .setEmail("john.doe@example.com")
                .setPassword("password")
                .setSex(Sex.MALE)
                .setRole(Role.STUDENT)
                .build();
        for (int i = 1; i <= 5; i++) {
            Lesson lesson = new Lesson.Builder()
                    .setName("Lesson " + i)
                    .setTopic("Topic " + i)
                    .setDate(java.time.LocalDate.now())
                    .setTimeStart(java.time.LocalTime.now())
                    .setTimeEnd(java.time.LocalTime.now().plusHours(1))
                    .setCabNum(i)
                    .setHomework(new Homework.Builder()
                            .setDescription("Homework for Lesson " + i)
                            .setDueDateTime(java.time.LocalDateTime.now().plusDays(i))
                            .build())
                    .build();

            user.setLessonsWithGrade(lesson, i * 10);
        }

        // Insert the User into the database
        String userId = userDAO.insert(user);

        // Test findById
        User fetchedUser = userDAO.findById(userId);
        System.out.println("Fetched User by ID: " + fetchedUser);

        // Test update
        fetchedUser.setName("UpdatedName");
        boolean isUpdated = userDAO.update(fetchedUser);
        System.out.println("Is User Updated: " + isUpdated);

        // Test findAll
        List<User> userList = userDAO.findAll();
        System.out.println("All Users:");
        for (User u : userList) {
            writeUser(u);
        }

        // Test updatePassword
        String newPassword = "newPassword";
        boolean isPasswordUpdated = userDAO.updatePassword(fetchedUser, newPassword);
        System.out.println("Is Password Updated: " + isPasswordUpdated);

        // Test delete
        boolean isDeleted = userDAO.delete(userId);
        System.out.println("Is User Deleted: " + isDeleted);

        // Test findAll after deletion
        List<User> updatedUserList = userDAO.findAll();
        System.out.println("All Users after Deletion:");
        for (User u : updatedUserList) {
            System.out.println(u);
        }

        List<Lesson> lessons = lessonDAO.findAll();
        for (Lesson l : lessons) {
            boolean res = lessonDAO.delete(l.getLessonId());
            System.out.println("DELETION LESSON WITH ID =" + l.getLessonId() + ":" + res);
        }

        List<Homework> homeworks = homeworkDAO.findAll();
        for (Homework h : homeworks) {
            boolean res = homeworkDAO.delete(h.getHomeworkId());
            System.out.println("DELETION HOMEWORK WITH ID =" + h.getHomeworkId() + ":" + res);
        }

        // Test LessonDAO
        Lesson lesson = new Lesson.Builder()
                .setName("New Lesson")
                .setDate(java.time.LocalDate.now())
                .setTimeStart(java.time.LocalTime.now())
                .setTimeEnd(java.time.LocalTime.now().plusHours(1))
                .setTopic("Circle")
                .setCabNum(10)
                .setHomework(new Homework.Builder()
                        .setDescription("Homework for New Lesson")
                        .setDueDateTime(java.time.LocalDateTime.now().plusDays(1))
                        .build())
                .build();

        // Insert the Lesson into the database
        String lessonId = lessonDAO.insert(lesson);
        System.out.println("Inserted Lesson with ID: " + lessonId);

        // Test findById for Lesson
        Lesson fetchedLesson = lessonDAO.findById(lessonId);
        System.out.println("Fetched Lesson by ID: " + fetchedLesson);

        // Test updateName for Lesson
        String newLessonName = "Updated Lesson Name";
        boolean isLessonNameUpdated = lessonDAO.updateName(fetchedLesson, newLessonName);
        System.out.println("Is Lesson Name Updated: " + isLessonNameUpdated);

        // Test delete for Lesson
        boolean isLessonDeleted = lessonDAO.delete(lessonId);
        System.out.println("Is Lesson Deleted: " + isLessonDeleted);

        // Test findAll for Lesson
        List<Lesson> lessonList = lessonDAO.findAll();
        System.out.println("All Lessons:");
        for (Lesson l : lessonList) {
            System.out.println(l);
        }

        homeworks = homeworkDAO.findAll();
        for (Homework h : homeworks) {
            boolean res = homeworkDAO.delete(h.getHomeworkId());
            System.out.println("DELETION HOMEWORK WITH ID =" + h.getHomeworkId() + ":" + res);
        }

        // Test HomeworkDAO
        Homework homework = new Homework.Builder()
                .setDescription("New Homework")
                .setDueDateTime(java.time.LocalDateTime.now().plusDays(2))
                .build();

        // Insert the Homework into the database
        String homeworkId = homeworkDAO.insert(homework);
        System.out.println("Inserted Homework with ID: " + homeworkId);

        // Test findById for Homework
        Homework fetchedHomework = homeworkDAO.findById(homeworkId);
        System.out.println("Fetched Homework by ID: " + fetchedHomework);

        // Test update for Homework
        fetchedHomework.setDescription("Updated Homework");
        boolean isHomeworkUpdated = homeworkDAO.update(fetchedHomework);
        System.out.println("Is Homework Updated: " + isHomeworkUpdated);

        // Test delete for Homework
        boolean isHomeworkDeleted = homeworkDAO.delete(homeworkId);
        System.out.println("Is Homework Deleted: " + isHomeworkDeleted);

        // Test findAll for Homework
        List<Homework> homeworkList = homeworkDAO.findAll();
        System.out.println("All Homeworks:");
        for (Homework h : homeworkList) {
            System.out.println(h);
        }

        DAOFactory factoryMongo = new DAOFactoryImpl("mongodb");
        UserDAO userDAOMongo = factoryMongo.getUserDAO();
        LessonDAO lessonDAOMongo = factoryMongo.getLessonDAO();
        HomeworkDAO homeworkDAOMongo = factoryMongo.getHomeworkDAO();

        User userMongo = new User.Builder()
                .setUserId("1")
                .setName("John")
                .setSurname("Doe")
                .setPhone("123456789")
                .setEmail("john.doe@example.com")
                .setPassword("password")
                .setSex(Sex.MALE)
                .setRole(Role.STUDENT)
                .build();
        for (int i = 1; i <= 5; i++) {
            Lesson lessonMongo = new Lesson.Builder()
                    .setLessonId(String.valueOf(i))
                    .setName("Lesson " + i)
                    .setTopic("Topic " + i)
                    .setDate(java.time.LocalDate.now())
                    .setTimeStart(java.time.LocalTime.now())
                    .setTimeEnd(java.time.LocalTime.now().plusHours(1))
                    .setCabNum(i)
                    .setHomework(new Homework.Builder()
                            .setHomeworkId(String.valueOf(i))
                            .setDescription("Homework for Lesson " + i)
                            .setDueDateTime(java.time.LocalDateTime.now().plusDays(i))
                            .build())
                    .build();

            userMongo.setLessonsWithGrade(lessonMongo, i * 10);
        }

        // Insert the User into the database
        String userIdMongo = userDAOMongo.insert(userMongo);

        // Test findById
        User fetchedUserMongo = userDAOMongo.findById(userIdMongo);
        System.out.println("Fetched User by ID: " + fetchedUserMongo);

        // Test update
        fetchedUserMongo.setName("UpdatedName");
        boolean isUpdatedMongo = userDAOMongo.update(fetchedUserMongo);
        System.out.println("Is User Updated: " + isUpdatedMongo);

        // Test findAll
        List<User> userListMongo = userDAOMongo.findAll();
        System.out.println("All Users:");
        for (User u : userListMongo) {
            writeUser(u);
        }

        // Test updatePassword
        String newPasswordMongo = "newPassword";
        boolean isPasswordUpdatedMongo = userDAOMongo.updatePassword(fetchedUserMongo, newPasswordMongo);
        System.out.println("Is Password Updated: " + isPasswordUpdatedMongo);

        // Test delete
        boolean isDeletedMongo = userDAOMongo.delete(userIdMongo);
        System.out.println("Is User Deleted: " + isDeletedMongo);

        // Test findAll after deletion
        List<User> updatedUserListMongo = userDAOMongo.findAll();
        System.out.println("All Users after Deletion:");
        for (User u : updatedUserListMongo) {
            System.out.println(u);
        }

        // Test LessonDAO
        Lesson lessonMongo = new Lesson.Builder()
                .setLessonId("1")
                .setName("New Lesson")
                .setDate(java.time.LocalDate.now())
                .setTimeStart(java.time.LocalTime.now())
                .setTimeEnd(java.time.LocalTime.now().plusHours(1))
                .setTopic("Circle")
                .setCabNum(10)
                .setHomework(new Homework.Builder()
                        .setHomeworkId("1")
                        .setDescription("Homework for New Lesson")
                        .setDueDateTime(java.time.LocalDateTime.now().plusDays(1))
                        .build())
                .build();

        // Insert the Lesson into the database
        String lessonIdMongo = lessonDAOMongo.insert(lessonMongo);
        System.out.println("Inserted Lesson with ID: " + lessonIdMongo);

        // Test findById for Lesson
        Lesson fetchedLessonMongo = lessonDAOMongo.findById(lessonIdMongo);
        System.out.println("Fetched Lesson by ID: " + fetchedLessonMongo);

        // Test updateName for Lesson
        String newLessonNameMongo = "Updated Lesson Name";
        boolean isLessonNameUpdatedMongo = lessonDAOMongo.updateName(fetchedLessonMongo, newLessonNameMongo);
        System.out.println("Is Lesson Name Updated: " + isLessonNameUpdatedMongo);

        // Test delete for Lesson
        boolean isLessonDeletedMongo = lessonDAOMongo.delete(lessonIdMongo);
        System.out.println("Is Lesson Deleted: " + isLessonDeletedMongo);

        // Test findAll for Lesson
        List<Lesson> lessonListMongo = lessonDAOMongo.findAll();
        System.out.println("All Lessons:");
        for (Lesson l : lessonListMongo) {
            System.out.println(l);
        }

        // Test HomeworkDAO
        Homework homeworkMongo = new Homework.Builder()
                .setHomeworkId("1")
                .setDescription("New Homework")
                .setDueDateTime(java.time.LocalDateTime.now().plusDays(2))
                .build();

        // Insert the Homework into the database
        String homeworkIdMongo = homeworkDAOMongo.insert(homeworkMongo);
        System.out.println("Inserted Homework with ID: " + homeworkIdMongo);

        // Test findById for Homework
        Homework fetchedHomeworkMongo = homeworkDAOMongo.findById(homeworkIdMongo);
        System.out.println("Fetched Homework by ID: " + fetchedHomeworkMongo);

        // Test update for Homework
        fetchedHomeworkMongo.setDescription("Updated Homework");
        boolean isHomeworkUpdatedMongo = homeworkDAOMongo.update(fetchedHomeworkMongo);
        System.out.println("Is Homework Updated: " + isHomeworkUpdatedMongo);

        // Test delete for Homework
        boolean isHomeworkDeletedMongo = homeworkDAOMongo.delete(homeworkIdMongo);
        System.out.println("Is Homework Deleted: " + isHomeworkDeletedMongo);
    }

    public static void writeUser(User user) {
        // Виведення інформації у консоль
        System.out.println("User 1:");
        System.out.println("    Name: " + user.getName());
        System.out.println("    Surname: " + user.getSurname());
        System.out.println("    Phone: " + user.getPhone());
        System.out.println("    Email: " + user.getEmail());
        System.out.println("    Sex: " + user.getSex());
        System.out.println("    Role: " + user.getRole());
        System.out.println("    Password: " + user.getPassword());
        System.out.println("\n    Lessons with Grades:");

        for (Map.Entry<Lesson, Integer> entry : user.getLessonsWithGrade().entrySet()) {
            Lesson lesson = entry.getKey();
            int grade = entry.getValue();

            System.out.println("        " + lesson.getName() + ":");
            System.out.println("            Name: " + lesson.getName());
            System.out.println("            Topic: " + lesson.getTopic());
            System.out.println("            Date: " + lesson.getDate());
            System.out.println("            Time Start: " + lesson.getTimeStart());
            System.out.println("            Time End: " + lesson.getTimeEnd());
            System.out.println("            CabNum: " + lesson.getCabNum());
            System.out.println("            Homework:");
            System.out.println("                Description: " + lesson.getHomework().getDescription());
            System.out.println("                Due Date Time: " + lesson.getHomework().getDueDateTime());
            System.out.println("            Grade: " + grade);
        }
    }
}
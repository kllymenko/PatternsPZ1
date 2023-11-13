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
        UserDAO userDAO = daoFactory.getUserDAO();
        LessonDAO lessonDAO = daoFactory.getLessonDAO();
        HomeworkDAO homeworkDAO = daoFactory.getHomeworkDAO();

        System.out.println("Testing UserDAOImpl methods:");

        // Insert users
        for (int i = 1; i <= 10; i++) {
            User user = new User.Builder("Name" + i, "Surname" + i, "Phone" + i, "Email" + i, "Password" + i, Sex.MALE)
                    .setRole("STUDENT")
                    .build();

            // Вставка користувача в базу даних
            int userId = userDAO.insert(user);
            System.out.println("User inserted with ID: " + userId);
        }


        // Find all users
        List<User> allUsers = userDAO.findAll();
        System.out.println("All Users:");
        for (User user : allUsers) {
            System.out.println(user);
        }

        // Оновлення паролю користувача
        User userToUpdate = allUsers.get(0);
        String newPassword = "NewPassword";
        boolean passwordUpdated = userDAO.updatePassword(userToUpdate, newPassword);
        System.out.println("Password updated: " + passwordUpdated);
        System.out.println("\nTesting LessonDAOImpl methods:");



        // Insert lesson
        Lesson lesson = new Lesson.Builder()
                .setName("Test Lesson")
                .setDate(LocalDate.now())
                .setTimeStart(Time.valueOf(LocalTime.now()))
                .setTimeEnd(Time.valueOf(LocalTime.now().plusHours(1)))
                .setCabName(101)
                .setUsersInLesson(new Schedule.Builder(userDAO.findAll().get(1).getUserId()).setGrade(10).setPresent(true).build())
                .setUsersInLesson(new Schedule.Builder(userDAO.findAll().get(2).getUserId()).setGrade(2).setPresent(true).build())
                .build();

        int lessonId = lessonDAO.insert(lesson);
        System.out.println("Inserted lesson with ID: " + lessonId);

        // Update lesson name
        boolean nameUpdated = lessonDAO.updateName(lesson, "Updated Lesson");
        System.out.println("Lesson name updated: " + nameUpdated);

        // Find lesson by ID
        Lesson foundLesson = lessonDAO.findById(lessonId);
        System.out.println("Found lesson by ID: " + foundLesson);

        // Find all lessons
        List<Lesson> allLessons = lessonDAO.findAll();
        System.out.println("All lessons:");
        for (Lesson l : allLessons) {
            System.out.println(l);
        }

        // Get lessons by user ID
        List<Lesson> lessonsByUserId = lessonDAO.getLessonsByUserId(userDAO.findAll().get(1).getUserId());
        System.out.println("Lessons for user ID " + userDAO.findAll().get(1).getUserId() + ": " + lessonsByUserId);

        System.out.println("\nTesting HomeworkDAOImpl methods:");

        // Insert homework
        System.out.println(lessonDAO.findAll());
        Homework homework = new Homework.Builder()
                .setLessonId(lessonDAO.findAll().get(0).getLessonId())
                .setDescription("Complete exercises")
                .build();

        int homeworkId = homeworkDAO.insert(homework);
        System.out.println("Inserted homework with ID: " + homeworkId);

        // Update due date time
        boolean dueDateTimeUpdated = homeworkDAO.updateDueDateTime(homework, java.sql.Timestamp.valueOf("2023-11-22 23:59:59"));
        System.out.println("Homework due date and time updated: " + dueDateTimeUpdated);

        // Find homework by ID
        Homework foundHomework = homeworkDAO.findById(homeworkId);
        System.out.println("Found homework by ID: " + foundHomework);

        // Find all homework
        List<Homework> allHomeworks = homeworkDAO.findAll();
        System.out.println("All homeworks:");
        for (Homework hw : allHomeworks) {
            System.out.println(hw);
        }

        //видалення
        int userIdToDelete = userToUpdate.getUserId();
        boolean userDeleted = userDAO.delete(userIdToDelete);
        System.out.println("User deleted: " + userDeleted);

        // Delete homework
        boolean homeworkDeleted = homeworkDAO.delete(homeworkId);
        System.out.println("Homework deleted: " + homeworkDeleted);

        // Delete lesson
        boolean lessonDeleted = lessonDAO.delete(lessonId);
        System.out.println("Lesson deleted: " + lessonDeleted);

    }
}
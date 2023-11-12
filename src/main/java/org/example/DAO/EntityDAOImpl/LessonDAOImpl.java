package org.example.DAO.EntityDAOImpl;

import org.example.DAO.EntityDAO.LessonDAO;
import org.example.entities.Lesson;
import org.example.entities.Schedule;
import org.example.entities.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LessonDAOImpl implements LessonDAO {
    private static final String ADD_LESSON = "INSERT INTO school.lesson (name, date, time_start, time_end, cab_num) VALUES (?, ?, ?, ?, ?)";
    private static final String ADD_SCHEDULE = "INSERT INTO school.schedule (user_id, lesson_id, grade, is_present) VALUES (?, ?, ?, ?)";
    private static final String GET_LESSON_BY_ID = "SELECT * from school.lesson WHERE lesson_id=?";
    private static final String DELETE_LESSON_BY_ID = "DELETE FROM school.lesson WHERE lesson_id=?";
    private static final String GET_ALL_LESSONS = "SELECT * FROM school.lesson";
    private static final String UPDATE_NAME = "UPDATE school.lesson SET name=? WHERE lesson_id=?";
    private static final String GET_SCHEDULE_BY_USER_ID = "SELECT * from school.schedule WHERE user_id=?";
    private static final String DELETE_SCHEDULE_BY_USER_ID = "DELETE FROM school.schedule WHERE user_id = ?";
    private static final String GET_ALL_SCHEDULE = "SELECT * FROM school.schedule";
    private static final String UPDATE_GRADE_BY_USER_AND_LESSON = "UPDATE school.schedule SET grade=? WHERE user_id=? AND lesson_id=?";
    private final Connection con;

    public LessonDAOImpl(Connection connection) {
        con = connection;
    }

    @Override
    public int insert(Lesson entity) {
        try (PreparedStatement psLesson = con.prepareStatement(ADD_LESSON, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            psLesson.setString(++k, entity.getName());
            psLesson.setDate(++k, java.sql.Date.valueOf(entity.getDate()));
            psLesson.setTime(++k, entity.getTimeStart());
            psLesson.setTime(++k, entity.getTimeEnd());
            psLesson.setInt(++k, entity.getCabName());
            psLesson.executeUpdate();
            try (ResultSet keys = psLesson.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setLessonId(keys.getInt(1));
                }
                for (Map.Entry<Role, Schedule> entry : entity.getUsersInLesson().entrySet()) {
                    try (PreparedStatement psSchedule = con.prepareStatement(ADD_SCHEDULE)) {
                        k = 0;
                        psSchedule.setInt(++k, entry.getValue().getUserId());
                        psSchedule.setInt(++k, entity.getLessonId());
                        psSchedule.setInt(++k, entry.getValue().getGrade());
                        psSchedule.setBoolean(++k, entry.getValue().isPresent());
                        psSchedule.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return entity.getLessonId();
    }

    @Override
    public boolean update(Lesson entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Lesson> findAll() {
        return null;
    }

    @Override
    public Lesson findById(int id) {
        return null;
    }

    @Override
    public boolean updateName(Lesson entity, String newName) {
        return false;
    }

    @Override
    public List<Lesson> getLessonsByUserId(int userId) {
        return null;
    }

    private Lesson mapLessons(ResultSet rs) throws SQLException {
        Lesson l = new Lesson();
        l.setLessonId(rs.getInt("lesson_id"));
        l.setName(rs.getString("name"));
        l.setDate(rs.getDate("date").toLocalDate());
        l.setTimeStart(rs.getTime("time_start"));
        l.setTimeEnd(rs.getTime("time_end"));
        return l;
    }
}

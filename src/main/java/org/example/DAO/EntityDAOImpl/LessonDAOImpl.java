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
    private static final String GET_USER_LESSON = "SELECT * FROM `schedule` WHERE lesson_id=?";
    private static final String GET_ALL_LESSONS = "SELECT * FROM lesson";
    private static final String DELETE_LESSON_BY_ID = "DELETE FROM lesson WHERE lesson_id=?";
    private static final String GET_LESSON_FROM_SCHEDULE = "SELECT lesson_id FROM `schedule` WHERE user_id=?";
    private static final String UPDATE_NAME = "UPDATE school.lesson SET name=? WHERE lesson_id=?";
    private static final String DELETE_SCHEDULE_BY_LESSON_ID = "DELETE FROM school.schedule WHERE lesson_id = ?";
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
                for (Schedule schedule : entity.getUsersInLesson()) {
                    try (PreparedStatement psSchedule = con.prepareStatement(ADD_SCHEDULE)) {
                        k = 0;
                        psSchedule.setInt(++k, schedule.getUserId());
                        psSchedule.setInt(++k, entity.getLessonId());
                        psSchedule.setInt(++k, schedule.getGrade());
                        psSchedule.setBoolean(++k, schedule.isPresent());
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
        try (PreparedStatement ps = con.prepareStatement(DELETE_SCHEDULE_BY_LESSON_ID)) {
            ps.setInt(1, id);
            try (PreparedStatement ps1 = con.prepareStatement(DELETE_LESSON_BY_ID)) {
                ps1.setInt(1, id);
                int rowsDeleted = ps.executeUpdate();
                return rowsDeleted > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Lesson> findAll() {
        List<Lesson> lessons = new ArrayList<>();
        try (Statement ps = con.createStatement()) {
            try (ResultSet rs = ps.executeQuery(GET_ALL_LESSONS)) {
                while (rs.next()) {
                    Lesson.Builder lessonBuilder = new Lesson.Builder();
                    lessonBuilder.setLessonId(rs.getInt("lesson_id"))
                            .setName(rs.getString("name"))
                            .setDate(rs.getDate("date").toLocalDate())
                            .setTimeStart(rs.getTime("time_start"))
                            .setTimeEnd(rs.getTime("time_end"))
                            .setCabName(rs.getInt("cab_num"));
                    insertUsersToLesson(lessonBuilder, con);
                    lessons.add(lessonBuilder.build());
                }
                return lessons;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Lesson findById(int id) {
        Lesson.Builder lessonBuilder = new Lesson.Builder();
        try (PreparedStatement ps = con.prepareStatement(GET_LESSON_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lessonBuilder.setLessonId(rs.getInt("lesson_id"))
                            .setName(rs.getString("name"))
                            .setDate(rs.getDate("date").toLocalDate())
                            .setTimeStart(rs.getTime("time_start"))
                            .setTimeEnd(rs.getTime("time_end"))
                            .setCabName(rs.getInt("cab_num"));
                    insertUsersToLesson(lessonBuilder, con);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lessonBuilder.build();
    }

    private void insertUsersToLesson(Lesson.Builder lessonBuilder, Connection con) throws SQLException {
        try (PreparedStatement prs = con.prepareStatement(GET_USER_LESSON)) {
            prs.setLong(1, lessonBuilder.getLessonId());
            try (ResultSet resultSet = prs.executeQuery()) {
                while (resultSet.next()) {
                    Schedule schedule = mapSchedule(resultSet);
                    lessonBuilder.setUsersInLesson(schedule);
                }
            }
        }
    }

    @Override
    public boolean updateName(Lesson entity, String newName) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_NAME)) {
            int k = 0;
            ps.setString(++k, newName);
            ps.setInt(++k, entity.getLessonId());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Lesson> getLessonsByUserId(int userId) {
        List<Lesson> lessons = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(GET_LESSON_FROM_SCHEDULE)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lessons.add(findById(rs.getInt(1)));
                }
                return lessons;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Lesson mapLessons(ResultSet rs) throws SQLException {
        Lesson l = new Lesson();
        l.setLessonId(rs.getInt("lesson_id"));
        l.setName(rs.getString("name"));
        l.setDate(rs.getDate("date").toLocalDate());
        l.setTimeStart(rs.getTime("time_start"));
        l.setTimeEnd(rs.getTime("time_end"));
        l.setCabName(rs.getInt("cab_num"));
        return l;
    }

    private Schedule mapSchedule(ResultSet rs) throws SQLException {
        Schedule s = new Schedule();
        s.setUserId(rs.getInt("user_id"));
        s.setGrade(rs.getInt("grade"));
        s.setPresent(rs.getBoolean("is_present"));
        return s;
    }
}

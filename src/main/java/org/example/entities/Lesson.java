package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.enums.Role;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private int lessonId;
    private String name;
    private LocalDate date;
    private Time timeStart;
    private Time timeEnd;
    private int cubName;
    private Map<Role, Schedule> usersInLesson;

    public Lesson(Builder builder) {
        this.lessonId = builder.lessonId;
        this.name = builder.name;
        this.date = builder.date;
        this.timeStart = builder.timeStart;
        this.timeEnd = builder.timeEnd;
        this.cubName = builder.cubName;
        this.usersInLesson = builder.usersInLesson;
    }

    public static class Builder {
        private int lessonId;
        private String name;
        private LocalDate date;
        private Time timeStart;
        private Time timeEnd;
        private int cubName;
        private Map<Role, Schedule> usersInLesson = new EnumMap<>(Role.class);;

        public Builder() {
        }

        public void setLessonId(int lessonId) {
            this.lessonId = lessonId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public void setTimeStart(Time timeStart) {
            this.timeStart = timeStart;
        }

        public void setTimeEnd(Time timeEnd) {
            this.timeEnd = timeEnd;
        }

        public void setCubName(int cubName) {
            this.cubName = cubName;
        }

        public void setUsersInLesson(Role role, Schedule schedule) {
            this.usersInLesson.put(role, schedule);
        }

        public Lesson build(){
            return new Lesson(this);
        }
    }
}

package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private int lessonId;
    private String name;
    private LocalDate date;
    private Time timeStart;
    private Time timeEnd;
    private int cabName;
    private List<Schedule> usersInLesson;

    public Lesson(Builder builder) {
        this.lessonId = builder.lessonId;
        this.name = builder.name;
        this.date = builder.date;
        this.timeStart = builder.timeStart;
        this.timeEnd = builder.timeEnd;
        this.cabName = builder.cabName;
        this.usersInLesson = builder.usersInLesson;
    }

    public static class Builder {
        private int lessonId;
        private String name;
        private LocalDate date;
        private Time timeStart;
        private Time timeEnd;
        private int cabName;
        private List<Schedule> usersInLesson = new ArrayList<>();

        public Builder() {
        }

        public Builder setLessonId(int lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setTimeStart(Time timeStart) {
            this.timeStart = timeStart;
            return this;
        }

        public Builder setTimeEnd(Time timeEnd) {
            this.timeEnd = timeEnd;
            return this;
        }

        public Builder setCabName(int cabName) {
            this.cabName = cabName;
            return this;
        }

        public Builder setUsersInLesson(Schedule schedule) {
            this.usersInLesson.add(schedule);
            return this;
        }

        public Lesson build(){
            return new Lesson(this);
        }

        public long getLessonId() {
            return lessonId;
        }
    }
}

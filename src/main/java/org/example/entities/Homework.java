package org.example.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Homework {
    private int homeworkId;
    private int lessonId;
    private String description;
    private Timestamp dueDateTime;

    public Homework(Builder builder) {
        this.homeworkId = builder.homeworkId;
        this.lessonId = builder.lessonId;
        this.description = builder.description;
        this.dueDateTime = builder.dueDateTime;
    }

    public static class Builder {
        private int homeworkId;
        private int lessonId;
        private String description;
        private Timestamp dueDateTime;

        public Builder(){
        }

        public Builder setHomeworkId(int homeworkId) {
            this.homeworkId = homeworkId;
            return this;
        }

        public Builder setLessonId(int lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDueDateTime(Timestamp dueDateTime) {
            this.dueDateTime = dueDateTime;
            return this;
        }

        public Homework build(){
            return new Homework(this);
        }
    }

}

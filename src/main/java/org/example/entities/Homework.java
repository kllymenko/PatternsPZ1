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

        public void setHomeworkId(int homeworkId) {
            this.homeworkId = homeworkId;
        }

        public void setLessonId(int lessonId) {
            this.lessonId = lessonId;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDueDateTime(Timestamp dueDateTime) {
            this.dueDateTime = dueDateTime;
        }

        public Homework build(){
            return new Homework(this);
        }
    }

}

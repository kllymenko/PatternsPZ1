package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private int userId;
    private int grade;
    private boolean isPresent;

    public Schedule(Builder builder) {
        this.userId = builder.userId;
        this.grade = builder.grade;
        this.isPresent = builder.isPresent;
    }

    public static class Builder {
        private final int userId;
        private int grade;
        private boolean isPresent;

        public Builder(int userId) {
            this.userId = userId;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public void setPresent(boolean present) {
            isPresent = present;
        }

        public Schedule build() {
            if (userId <= 0) {
                throw new IllegalStateException("Can`t create Schedule with invalid userId");
            }
            return new Schedule(this);
        }
    }
}

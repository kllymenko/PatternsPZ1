package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.entities.enums.Role;
import org.example.entities.enums.Sex;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userId;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;
    private Sex sex;
    private Role role;

    public User(Builder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.surname = builder.surname;
        this.phone = builder.phone;
        this.email = builder.email;
        this.password = builder.password;
        this.sex = builder.sex;
        this.role = builder.role;
    }

    public static class Builder {
        private int userId;
        private final String name;
        private final String surname;
        private final String phone;
        private final String email;
        private final String password;
        private final Sex sex;
        private Role role = Role.STUDENT;

        public Builder(String name, String surname, String phone, String email, String password, Sex sex) {
            this.name = name;
            this.surname = surname;
            this.phone = phone;
            this.email = email;
            this.password = password;
            this.sex = sex;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Builder setRole(String role) {
            this.role = Role.valueOf(role);
            return this;
        }

        public User build() {
            if (name == null || surname == null || phone == null || email == null || password == null || sex == null) {
                throw new IllegalStateException("Name, surname, phone, email, password ans sex are required to build a user");
            }
            return new User(this);
        }
    }
}

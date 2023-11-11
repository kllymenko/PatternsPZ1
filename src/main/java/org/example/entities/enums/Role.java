package org.example.entities.enums;

public enum Role {
    STUDENT("student"),
    HEADTEACHER("teacher");
    private String role;

    Role(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}

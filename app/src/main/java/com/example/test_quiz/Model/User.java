package com.example.test_quiz.Model;

public class User {


    public String name,semester,branch,sect;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String semester, String branch, String sect) {
        this.name = name;
        this.semester = semester;
        this.branch = branch;
        this.sect = sect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSect() {
        return sect;
    }

    public void setSect(String secti) {
        this.sect = secti;
    }
}
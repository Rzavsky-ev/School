package ru.hogwarts.school.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Faculty {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String color;

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private final List<Student> students = new ArrayList<>();

    public Faculty(){};

    public Faculty(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Collection<Student> allStudentsFaculty() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public String toString() {
        return "id=" + getId() + " name:" + getName() +
                " color:" + getColor() + " students" + students.toString();
    }
}



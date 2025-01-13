package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findFaculty(@PathVariable("id") Long id) {
        Student studentFind = studentService.findStudent(id);
        if (studentFind == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentFind);
    }

    @GetMapping(path = "/all")
    public Collection<Student> getAll() {
        return studentService.getAll();
    }

    @PostMapping
    public Student addStudents(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student studentEdit = studentService.editStudent(student);
        if (studentEdit == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentEdit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeFaculty(@PathVariable("id") Long id) {
        Student studentRemove = studentService.removeStudent(id);
        if (studentRemove == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}

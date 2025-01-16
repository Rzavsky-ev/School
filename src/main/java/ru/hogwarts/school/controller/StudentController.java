package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
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
    public Student findFaculty(@PathVariable("id") Long id) {
        return studentService.findStudent(id);
    }

    @GetMapping(path = "/find")
    public Collection<Student> findByAgeBetween(@RequestParam int minAge,
                                                @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping(path = "faculty/{id}")
    public Faculty getStudentFaculty(@PathVariable("id") Long id) {
        return studentService.getStudentFaculty(id);
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
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    public Student removeFaculty(@PathVariable("id") Long id) {
        return studentService.removeStudent(id);
    }
}

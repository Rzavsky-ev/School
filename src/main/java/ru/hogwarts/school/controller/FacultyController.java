package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;


@RestController
@RequestMapping(path = "/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public Faculty findFaculty(@PathVariable("id") Long id) {
        return facultyService.findFaculty(id);
    }

    @GetMapping("/name")
    public Faculty findByName(@RequestParam String name) {
        return facultyService.findByName(name);
    }

    @GetMapping("/allStudents/{id}")
    public Collection<Student> getAllStudentsFaculty(@PathVariable("id") Long id) {
        return facultyService.getAllStudentsFaculty(id);
    }

    @GetMapping(path = "/all")
    public Collection<Faculty> getAll() {
        return facultyService.getAll();
    }

    @GetMapping(path = "/LongestName")
    public String getLongestName() {
        return facultyService.getLongestName();
    }

    @GetMapping(path = "/sum")
    public Integer getSum() {
        return facultyService.getSum();
    }

    @PostMapping
    public Faculty addFaculties(@RequestBody Faculty faculty) {
        return facultyService.addFaculties(faculty);
    }

    @PutMapping
    public Faculty editFaculty(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}")
    public Faculty removeFaculty(@PathVariable("id") Long id) {
        return facultyService.removeFaculty(id);
    }


}

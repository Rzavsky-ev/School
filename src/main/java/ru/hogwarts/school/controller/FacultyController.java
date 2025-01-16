package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Faculty> findFaculty(@PathVariable("id") Long id) {
        Faculty facultyFind = facultyService.findFaculty(id);
        if (facultyFind == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyFind);
    }

    @GetMapping("/name")
    public ResponseEntity<Faculty> findByName(@RequestParam String name) {
        Faculty facultyFind = facultyService.findByName(name);
        if (facultyFind == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyFind);
    }

    @GetMapping("/allStudents/{id}")
    public ResponseEntity<Collection<Student>> getAllStudentsFaculty(@PathVariable("id") Long id) {
        Collection<Student> allStudents = facultyService.getAllStudentsFaculty(id);
        if (allStudents.isEmpty()) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allStudents);
    }

    @GetMapping(path = "/all")
    public Collection<Faculty> getAll() {
        return facultyService.getAll();
    }

    @PostMapping
    public Faculty addFaculties(@RequestBody Faculty faculty) {
        return facultyService.addFaculties(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty facultyEdit = facultyService.editFaculty(faculty);
        if (facultyEdit == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyEdit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeFaculty(@PathVariable("id") Long id) {
        Faculty facultyRemove = facultyService.removeFaculty(id);
        if (facultyRemove == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


}

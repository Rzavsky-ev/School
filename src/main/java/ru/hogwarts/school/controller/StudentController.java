package ru.hogwarts.school.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public Student findStudent(@PathVariable("id") Long id) {
        return studentService.findStudent(id);
    }

    @GetMapping(path = "/find")
    public Collection<Student> findByAgeBetween(@RequestParam int minAge,
                                                @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping(path = "/faculty/{id}")
    public Faculty getStudentFaculty(@PathVariable("id") Long id) {
        return studentService.getStudentFaculty(id);
    }

    @GetMapping(path = "/all")
    public Collection<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping(path = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(path = "/{id}/avatar")
    public ResponseEntity<String> downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
        return ResponseEntity.ok("loaded successfully");
    }

    @GetMapping(path = "/quantity")
    public Integer numberOfAllStudents() {
        return studentService.getNumberOfAllStudents();
    }

    @GetMapping(path = "age/average")
    public Integer getAverageAgeStudents() {
        return studentService.getAverageAgeStudents();

    }

    @GetMapping(path = "/last")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping(path = "/startingLetter")
    public List<Student> getStudentsNameStartingLetter(@RequestParam(required = false) String firstLetter) {
        return studentService.getStudentsNameStartingLetter(firstLetter);
    }

    @GetMapping(path = "averageAge")
    public Double getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    @PostMapping
    public Student addStudents(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PostMapping(path = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        try {
            if (avatar.getSize() > 1024 * 300) {
                return ResponseEntity.badRequest().body("File is too big");
            }
            studentService.uploadAvatar(id, avatar);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        }
    }

    @PutMapping
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    public Student removeStudent(@PathVariable("id") Long id) {
        return studentService.removeStudent(id);
    }
}

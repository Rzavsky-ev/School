package ru.hogwarts.school.service;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final AvatarRepository avatarRepository;

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Student added");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        Student studentFind = studentRepository.getReferenceById(id);
        if (studentFind == null) {
            logger.error("There is not student with id = " + id);
            throw new EntityNotFoundException();
        }
        logger.info("Student found");
        return studentFind;
    }

    public Faculty getStudentFaculty(Long id) {
        if (studentRepository.getReferenceById(id) == null) {
            logger.error("There is not student with id = " + id);
            throw new EntityNotFoundException();
        }
        logger.info("Student faculty found");
        return studentRepository.getReferenceById(id).getFaculty();
    }

    public Collection<Student> getAll() {
        logger.info("All students are shown");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentAge(int age) {
        logger.info(" All students are shown with age" + age);
        return studentRepository.findByAge(age);
    }

    public Student editStudent(Student student) {
        if (studentRepository.getReferenceById(student.getId()) == null) {
            logger.error("There is not student");
            throw new EntityNotFoundException();
        }
        logger.info("Student edit");
        return studentRepository.save(student);
    }

    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Showing students aged " + minAge + " to " + maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Student removeStudent(Long id) {
        Student studentRemove = studentRepository.getReferenceById(id);
        if (studentRemove == null) {
            logger.error("There is not student with id = " + id);
            throw new EntityNotFoundException();
        }
        studentRepository.delete(studentRemove);
        logger.info("Student remove");
        return studentRemove;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = findStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).
                orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
        logger.info("Avatar uploaded");
    }

    private String getExtension(String fileName) {
        logger.info("Extension received");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(long studentId) {
        logger.info("Avatar found");
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public Integer getNumberOfAllStudents() {
        Integer studentQuantity = studentRepository.getCountAllStudents();
        if (studentQuantity == null) {
            logger.warn("The database is empty");
            throw new EntityNotFoundException();
        }
        logger.info("");
        return studentQuantity;
    }

    public Integer getAverageAgeStudents() {
        Integer averageAgeStudents = studentRepository.getAverageAgeStudents();
        if (averageAgeStudents == null) {
            logger.warn("The database is empty");
            throw new EntityNotFoundException();
        }
        logger.info("The average age of students is derived");
        return averageAgeStudents;
    }

    public List<Student> getLastFiveStudents() {
        List<Student> students = studentRepository.getLastFiveStudents();
        if (students.isEmpty()) {
            logger.warn("The database is empty");
            throw new EntityNotFoundException();
        }
        logger.info("The last five students were halfway through");
        return students;
    }

    public List<Student> getStudentsNameStartingLetter(String firstLetter) {
        if (firstLetter.isEmpty()) {
            logger.info("Empty string");
            throw new EntityNotFoundException();
        }
        List<Student> students = studentRepository.findAll().stream().
                filter(student -> student.getName().toUpperCase().startsWith(firstLetter.toUpperCase())).
                toList();
        if (students.isEmpty()) {
            logger.warn("The database is empty");
            throw new EntityNotFoundException();
        }
        return students;
    }

    public Double getAverageAgeOfStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            logger.warn("The database is empty");
            throw new EntityNotFoundException();
        }
        return students.stream().collect(Collectors.averagingDouble(Student::getAge));
    }
}

package ru.hogwarts.school.service;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final AvatarRepository avatarRepository;

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        Student studentFind = studentRepository.getReferenceById(id);
        if (studentFind == null) {
            throw new EntityNotFoundException();
        }
        return studentFind;
    }

    public Faculty getStudentFaculty(Long id) {
        if (studentRepository.getReferenceById(id) == null) {
            throw new EntityNotFoundException();
        }
        return studentRepository.getReferenceById(id).getFaculty();
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Student editStudent(Student student) {
        if (studentRepository.getReferenceById(student.getId()) == null) {
            throw new EntityNotFoundException();
        }
        return studentRepository.save(student);
    }

    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Student removeStudent(Long id) {
        Student studentRemove = studentRepository.getReferenceById(id);
        if (studentRemove == null) {
            throw new EntityNotFoundException();
        }
        studentRepository.delete(studentRemove);
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
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public Integer getNumberOfAllStudents() {
        Integer studentQuantity = studentRepository.getCountAllStudents();
        if (studentQuantity == null) {
            throw new EntityNotFoundException();
        }
        return studentQuantity;
    }

    public Integer getAverageAgeStudents() {
        Integer averageAgeStudents = studentRepository.getAverageAgeStudents();
        if (averageAgeStudents == null) {
            throw new EntityNotFoundException();
        }
        return averageAgeStudents;
    }

    public List<Student> getLastFiveStudents() {
        List<Student> students = studentRepository.getLastFiveStudents();
        if (students.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return students;
    }

    public List<Student> getAllPage(Integer numberPage, Integer sizePage) {
        PageRequest pageRequest = PageRequest.of(numberPage, sizePage);
        return studentRepository.findAll(pageRequest).getContent();
    }

}

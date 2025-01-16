package ru.hogwarts.school.service;


import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
        Collection<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
        if (students.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return students;
    }

    public Student removeStudent(Long id) {
        Student studentRemove = studentRepository.getReferenceById(id);
        if (studentRemove == null) {
            throw new EntityNotFoundException();
        }
        studentRepository.delete(studentRemove);
        return studentRemove;
    }
}

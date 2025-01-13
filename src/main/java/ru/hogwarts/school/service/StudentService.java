package ru.hogwarts.school.service;


import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;
import java.util.stream.Collectors;

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
        return studentRepository.getReferenceById(id);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentAge(int age) {
        return studentRepository.findAll().stream().
                filter(student -> student.getAge() == age).collect(Collectors.toList());
    }

    public Student editStudent(Student student) {
        if (studentRepository.getReferenceById(student.getId()) == null) {
            return null;
        }
        return studentRepository.save(student);
    }

    public Student removeStudent(Long id) {
        Student studentRemove = studentRepository.getReferenceById(id);
        studentRepository.delete(studentRemove);
        return studentRemove;
    }
}

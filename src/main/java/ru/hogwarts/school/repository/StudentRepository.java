package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT COUNT(id) FROM student",
            nativeQuery = true)
    Integer getCountAllStudents();

    @Query(value = "SELECT AVG(age) from student", nativeQuery = true)
    Integer getAverageAgeStudents();

    @Query(value = "SELECT* FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudents();

    @Query(value = "SELECT* FROM student LIMIT 6", nativeQuery = true)
    List<Student> getSixStudents();


}

package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;

import java.util.Optional;

public interface AvatarRepositoryTest extends JpaRepository<Avatar, Student> {

    Optional<Avatar> findByStudentId(Long studentId);

}
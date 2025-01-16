package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;


import java.util.Collection;


@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculties(Faculty faculty) {

        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        Faculty facultyFind = facultyRepository.getReferenceById(id);
        if (facultyFind == null) {
            throw new EntityNotFoundException();
        }
        return facultyFind;
    }

    public Faculty findByName(String name) {
        Faculty nameByFind = facultyRepository.findByNameIgnoreCase(name);
        if (nameByFind == null) {
            throw new EntityNotFoundException();
        }
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Student> getAllStudentsFaculty(Long id) {
        Collection<Student> allStudent =
                facultyRepository.getReferenceById(id).allStudentsFaculty();
        if (allStudent.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return allStudent;
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getColorFaculty(String color) {
        return facultyRepository.findByColor(color);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (facultyRepository.getReferenceById(faculty.getId()) == null) {
            throw new EntityNotFoundException();
        }
        return facultyRepository.save(faculty);
    }

    public Faculty removeFaculty(Long id) {
        Faculty facultyRemove = facultyRepository.getReferenceById(id);
        if (facultyRemove == null) {
            throw new EntityNotFoundException();
        }
        facultyRepository.delete(facultyRemove);
        return facultyRemove;
    }
}


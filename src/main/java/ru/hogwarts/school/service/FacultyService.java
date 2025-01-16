package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
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
        return facultyRepository.getReferenceById(id);
    }

    public Faculty findByName(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Student> getAllStudentsFaculty(Long id) {
        return facultyRepository.getReferenceById(id).allStudentsFaculty();
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getColorFaculty(String color) {
        return facultyRepository.findByColor(color);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (facultyRepository.getReferenceById(faculty.getId()) == null) {
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public Faculty removeFaculty(Long id) {
        Faculty facultyRemove = facultyRepository.getReferenceById(id);
        facultyRepository.delete(facultyRemove);
        return facultyRemove;
    }
}


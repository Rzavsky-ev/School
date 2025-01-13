package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;


import java.util.Collection;
import java.util.stream.Collectors;

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

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getColorFaculty(String color) {
        return facultyRepository.findAll().stream().filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
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


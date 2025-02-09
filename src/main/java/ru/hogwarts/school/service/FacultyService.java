package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Faculty addFaculties(Faculty faculty) {

        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        Faculty facultyFind = facultyRepository.getReferenceById(id);
        if (facultyFind == null) {
            logger.error("There is not faculty with id = " + id);
            throw new EntityNotFoundException();
        }
        logger.info("Faculty found");
        return facultyFind;
    }

    public Faculty findByName(String name) {
        Faculty nameByFind = facultyRepository.findByNameIgnoreCase(name);
        if (nameByFind == null) {
            logger.error("There is not faculty with name = " + name);
            throw new EntityNotFoundException();
        }
        logger.info("Faculty found");
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Student> getAllStudentsFaculty(Long id) {
        Collection<Student> allStudent =
                facultyRepository.getReferenceById(id).allStudentsFaculty();
        if (allStudent.isEmpty()) {
            logger.warn("There are no students at the faculty");
            throw new EntityNotFoundException();
        }
        logger.info("All students of the faculty have been shown");
        return allStudent;
    }

    public Collection<Faculty> getAll() {
        logger.info("All faculties are shown");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getColorFaculty(String color) {
        logger.info("Faculty color" + color + "show");
        return facultyRepository.findByColor(color);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (facultyRepository.getReferenceById(faculty.getId()) == null) {
            logger.error("Faculty not found");
            throw new EntityNotFoundException();
        }
        logger.info("Faculty edit");
        return facultyRepository.save(faculty);
    }

    public Faculty removeFaculty(Long id) {
        Faculty facultyRemove = facultyRepository.getReferenceById(id);
        if (facultyRemove == null) {
            logger.error("Faculty not found");
            throw new EntityNotFoundException();
        }
        facultyRepository.delete(facultyRemove);
        logger.info("Faculty remove");
        return facultyRemove;
    }

    public String getLongestName() {
        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            logger.warn("The database is empty");
            throw new EntityNotFoundException();
        }
        return faculties.stream().max(Comparator.comparingInt
                (faculty -> faculty.getName().length())).get().getName();
    }

    public Integer getSum() {
        return Stream.iterate(1, a -> a + 1).
                limit(1_000_000).parallel().reduce(0, Integer::sum);
    }
}


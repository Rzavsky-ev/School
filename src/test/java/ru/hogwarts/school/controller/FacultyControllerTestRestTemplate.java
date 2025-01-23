package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepositoryTest;
import ru.hogwarts.school.repository.StudentRepositoryTest;


import java.util.Collection;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyControllerTest;

    @Autowired
    private FacultyRepositoryTest facultyRepositoryTest;

    @Autowired
    private StudentRepositoryTest studentRepositoryTest;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoadsFacultyController() throws Exception {
        Assertions.assertThat(facultyControllerTest).isNotNull();
    }

    @Test
    public void testFindFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FEMA");
        faculty.setColor("Red");
        facultyRepositoryTest.save(faculty);

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/faculty/" + faculty.getId(), Faculty.class)).isNotNull();
    }


    @Test
    public void testFindByName() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FEMA");
        faculty.setColor("Red");
        facultyRepositoryTest.save(faculty);
        Assertions.assertThat(testRestTemplate.postForEntity("http://localhost:" + port +
                "/faculty/" + faculty.getName(), faculty, Faculty.class)).isNotNull();
    }

    //---------------------------------------------------------------------------//
    //я не понимаю, где тут ошибка (Герман ИИ не смог помочь)
    @Test
    public void testGetAllStudentsFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FEMA");
        faculty.setColor("Red");
        facultyRepositoryTest.save(faculty);

        Student student = new Student();
        student.setId(1L);
        student.setName("Иван");
        student.setAge(20);
        student.setFaculty(faculty);

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/faculty/allStudents/" + faculty.getId(), Collection.class)).isNotNull();
    }
//----------------------------------------------------//

    @Test
    public void testGetAll() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FEMA");
        faculty.setColor("Red");
        facultyRepositoryTest.save(faculty);
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/faculty/all", Collection.class)).isNotNull();
    }

    @Test
    public void testAddFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FEMA");
        faculty.setColor("Red");
        Assertions
                .assertThat(testRestTemplate.postForObject("http://localhost:" + port +
                        "/faculty", faculty, Faculty.class)).isNotNull();
    }

    @Test
    public void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FEMA");
        faculty.setColor("Red");
        facultyRepositoryTest.save(faculty);
        Assertions.assertThat(testRestTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(faculty), Faculty.class).getBody()).isNotNull();
    }

    @Test
    public void testRemoveFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FEMA");
        faculty.setColor("Red");
        facultyRepositoryTest.save(faculty);

        testRestTemplate.delete("http://localhost:" + port + "/faculty/" + faculty.getId());

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/sfaculty/" + faculty.getId(), Faculty.class).getId()).isNull();
    }
}














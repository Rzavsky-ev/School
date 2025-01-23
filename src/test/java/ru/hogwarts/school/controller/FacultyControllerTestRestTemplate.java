package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;


import java.util.Collection;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyControllerTest;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoadsFacultyController() throws Exception {
        Assertions.assertThat(facultyControllerTest).isNotNull();
    }

    @Test
    public void testFindFaculty() throws Exception {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/faculty/1", Faculty.class)).isNotNull();
    }

    @Test
    public void testFindByName() throws Exception {
        Faculty faculty = new Faculty(1L, "AA", "BB");
        Assertions.assertThat(testRestTemplate.postForEntity("http://localhost:" + port +
                "/faculty/" + faculty.getName(), faculty, Faculty.class)).isNotNull();
    }

    //--------------------------------------------------------------------//
    //Подскажите, пожалуйста, что не так в этом тесте? (Герман ИИ не смог помочь)
    @Test
    public void testGetAllStudentsFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "AA", "BB");
        ResponseEntity<Faculty> createResponseFaculty = testRestTemplate.postForEntity("http://localhost:" + port +
                "/faculty", faculty, Faculty.class);
        Assertions.assertThat(createResponseFaculty).isNotNull();

        Student student = new Student();
        student.setId(1L);
        student.setName("Иван");
        student.setAge(20);
        student.setFaculty(faculty);
        ResponseEntity<Student> createResponseStudent = testRestTemplate.postForEntity("http://localhost:" + port +
                "/student", student, Student.class);
        Assertions.assertThat(createResponseStudent).isNotNull();

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/faculty/allStudents/" + student.getFaculty().getId(), Collection.class)).isNotNull();
    }
    //--------------------------------------------------------------------//


    @Test
    public void testGetAll() throws Exception {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/faculty/all", Collection.class)).isNotNull();
    }

    @Test
    public void testAddFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "AA", "BB");
        Assertions
                .assertThat(testRestTemplate.postForObject("http://localhost:" + port +
                        "/faculty", faculty, Faculty.class)).isNotNull();
    }

    @Test
    public void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "AA", "BB");
        Assertions.assertThat(testRestTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(faculty), Faculty.class).getBody()).isNotNull();
    }

    @Test
    public void testRemoveFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "AA", "BB");

        ResponseEntity<Faculty> createResponse = testRestTemplate.postForEntity("http://localhost:" + port +
                "/faculty", faculty, Faculty.class);
        Assertions.assertThat(createResponse).isNotNull();
        testRestTemplate.delete("http://localhost:" + port + "/faculty/" + faculty.getId());

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/sfaculty/" + faculty.getId(), Faculty.class).getId()).isNull();
    }

}

package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentControllerTest;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoadsStudentController() throws Exception {
        Assertions.assertThat(studentControllerTest).isNotNull();
    }

    @Test
    public void testFindStudent() throws Exception {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/1", Student.class)).isNotNull();
    }

    @Test
    public void testFindByAgeBetween() throws Exception {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/find?minAge=20&maxAge=30", Collection.class)).isNotNull();
    }

    @Test
    public void testGetStudentFaculty() throws Exception {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/faculty/1)", Faculty.class)).isNotNull();
    }

    @Test
    public void testGetAll() throws Exception {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/all", Collection.class)).isNotNull();
    }

    @Test
    public void testDownloadAvatarPreview() throws Exception {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/1/avatar/preview", byte[].class)).isNotNull();
    }

    //--------------------------------------------------------------------//
    //Здесь ошибка 500, разобраться сам не могу
    @Test
    public void downloadAvatar() {

        Student student = new Student();
        student.setName("Иван");
        student.setAge(20);
        student = studentRepository.save(student);

        byte[] avatarData = "Test avatar data".getBytes();
        String mediaType = "image/png";

        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setData(avatarData);
        avatar.setMediaType(mediaType);

        studentRepository.save(student);

        Long studentId = student.getId();


        ResponseEntity<byte[]> response = testRestTemplate.getForEntity
                ("/student/{id}/avatar/preview", byte[].class, studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mediaType, response.getHeaders().getContentType().toString());
        assertEquals(avatarData.length, response.getHeaders().getContentLength());
        assertArrayEquals(avatarData, response.getBody());
    }


    @Test
    public void testAddStudents() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Иван");
        student.setAge(20);
        Assertions
                .assertThat(testRestTemplate.postForObject("http://localhost:" + port +
                        "/student", student, Student.class)).isNotNull();
    }

    @Test
    public void testUploadAvatar() throws Exception {
        Avatar avatar = new Avatar();
        Assertions
                .assertThat(testRestTemplate.postForObject("http://localhost:" + port +
                        "/student/1/avatar", avatar, String.class)).isNotNull();
    }

    @Test
    public void testEditStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Иван");
        student.setAge(20);
        Assertions.assertThat(testRestTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(student), Student.class).getBody()).isNotNull();
    }

    @Test
    public void testRemoveStudent() throws Exception {

        Student student = new Student();
        student.setName("Иван");
        student.setAge(20);
        student.setId(1L);
        ResponseEntity<Student> createResponse = testRestTemplate.postForEntity("http://localhost:" + port +
                "/student", student, Student.class);
        Assertions.assertThat(createResponse).isNotNull();
        testRestTemplate.delete("http://localhost:" + port + "/student/" + student.getId());

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/" + student.getId(), Student.class).getId()).isNull();
    }

}



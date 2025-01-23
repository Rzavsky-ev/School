package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import ru.hogwarts.school.repository.AvatarRepositoryTest;
import ru.hogwarts.school.repository.StudentRepositoryTest;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentControllerTest;

    @Autowired
    private StudentRepositoryTest studentRepositoryTest;

    @Autowired
    private AvatarRepositoryTest avatarRepositoryTest;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoadsStudentController() throws Exception {
        Assertions.assertThat(studentControllerTest).isNotNull();
    }

    @Test
    public void testFindStudent() throws Exception {
        Student student = new Student();
        student.setName("Test");
        student.setAge(25);
        student = studentRepositoryTest.save(student);
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/" + student.getId(), Student.class)).isNotNull();
    }

    @Test
    public void testFindByAgeBetween() throws Exception {
        Student studentA = new Student();
        studentA.setName("TestA");
        studentA.setAge(24);
        Student studentB = new Student();
        studentB.setName("TestB");
        studentB.setAge(22);
        studentRepositoryTest.save(studentA);
        studentRepositoryTest.save(studentB);
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/find?minAge=" + studentA.getAge() +
                "&maxAge=" + studentB.getAge(), Collection.class)).isNotNull();
    }

    @Test
    public void testGetStudentFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FEMA", "Красный");
        Student student = new Student();
        student.setName("Test");
        student.setAge(24);
        student.setFaculty(faculty);
        studentRepositoryTest.save(student);

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/faculty/)" + student.getId(), Faculty.class)).isNotNull();
    }

    @Test
    public void testGetAll() throws Exception {
        Student studentA = new Student();
        studentA.setName("TestA");
        studentA.setAge(24);
        Student studentB = new Student();
        studentB.setName("TestB");
        studentB.setAge(22);
        studentRepositoryTest.save(studentA);
        studentRepositoryTest.save(studentB);

        List<Student> students = new ArrayList<>(List.of(studentA, studentB));

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/all", Collection.class)).isNotNull();
    }

    @Test
    public void testDownloadAvatarPreview() throws Exception {
        Student student = new Student();
        student.setName("Test");
        student.setAge(24);
        studentRepositoryTest.save(student);

        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        byte[] avatarData = "Test".getBytes();
        avatar.setData(avatarData);
        avatarRepositoryTest.save(avatar);

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/1/avatar/preview", byte[].class)).isNotNull();
    }


    @Test
    public void testDownloadAvatar() {

        Student student = new Student();
        student.setName("Иван");
        student.setAge(20);
        studentRepositoryTest.save(student);

        byte[] avatarData = "Test".getBytes();
        String mediaType = "image/png";

        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setData(avatarData);
        avatar.setMediaType(mediaType);
        avatarRepositoryTest.save(avatar);
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/" + student.getId() + "/avatar", String.class)).isNotNull();
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
        Student student = new Student();
        student.setId(1L);
        student.setName("Иван");
        student.setAge(20);

        Avatar avatar = new Avatar();
        byte[] avatarData = "Test".getBytes();
        String mediaType = "image/png";
        avatar.setStudent(student);
        avatar.setData(avatarData);
        avatar.setMediaType(mediaType);
        avatarRepositoryTest.save(avatar);
        Assertions
                .assertThat(testRestTemplate.postForObject("http://localhost:" + port +
                        "/student/" + student.getId() + "/avatar", avatar, String.class)).isNotNull();
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

        testRestTemplate.delete("http://localhost:" + port + "/student/" + student.getId());

        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:" + port
                + "/student/" + student.getId(), Student.class).getId()).isNull();
    }

}



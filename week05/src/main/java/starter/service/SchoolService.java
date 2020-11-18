package starter.service;

import beans.domain.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SchoolService {
    private List<Student> students;

    public void register(Student student) {
        if (students == null) {
            students = new ArrayList<>();
        }
        if (student != null) {
            students.add(student);
            log.info("student registration: {}", student);
        } else {
            throw new IllegalArgumentException("student null");
        }
    }

}

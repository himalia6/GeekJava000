package beans.config;

import beans.domain.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public Student student() {
        final Student student = new Student();
        student.setName("Jimmy");
        student.setAge(30);
        return student;
    }
}

package starter;

import beans.domain.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import starter.config.SchoolConfigurationProperties;
import starter.service.SchoolService;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnClass(SchoolService.class)
@ConditionalOnProperty(prefix = "school", value = "open")
@EnableConfigurationProperties(SchoolConfigurationProperties.class)
public class SchoolAutoConfiguration {
    @Bean
    public List<Student> students() {
        final Student jimmy = Student.builder().name("Jimmy").age(19).build();
        final Student john = Student.builder().name("John").age(21).build();
        final Student jessica = Student.builder().name("Jessica").age(22).build();
        return Arrays.asList(jimmy, john, jessica);
    }

    @Bean
    @ConditionalOnMissingBean(SchoolService.class)
    public SchoolService schoolService() {
        return new SchoolService();
    }
}

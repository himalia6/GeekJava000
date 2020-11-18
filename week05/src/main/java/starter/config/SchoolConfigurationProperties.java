package starter.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Configuration //schoolConfigurationProperties
@ConfigurationProperties(prefix = "school") //school-starter.config.SchoolConfigurationProperties
@Data
public class SchoolConfigurationProperties {
    private static final String SCHOOL_NAME = "hogwarts";

    // school.name in application.properties/yml
    private String name = SCHOOL_NAME;
}

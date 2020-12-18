package db.sharding.rw02;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"db.sharding.common", "db.sharding.rw02"})
@EnableJpaRepositories("db.sharding.common.dao")
@EntityScan("db.sharding.common.domain")
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class SpringBootDBApplication {
    public static void main(String[] args) {
        final String profile = System.getProperty("spring.boot.profile", "rw02");
        new SpringApplicationBuilder(SpringBootDBApplication.class)
                .profiles(profile)
                .build()
                .run(args);
    }
}

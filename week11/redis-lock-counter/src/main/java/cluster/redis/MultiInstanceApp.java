package cluster.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiInstanceApp {
    public static void main(String[] args) {
        SpringApplication.run(MultiInstanceApp.class, args);
    }
}

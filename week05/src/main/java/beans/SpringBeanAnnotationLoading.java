package beans;

import beans.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class SpringBeanAnnotationLoading {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("beans");
        context.refresh();

        final Object student = context.getBean(Student.class);
        log.info("student {}", student);
    }
}

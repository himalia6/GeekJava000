package beans;

import beans.scan.School;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class SpringBeanScanLoading {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("beans.scan");
        context.refresh();

        final School school = context.getBean(School.class);
        school.run();
    }
}

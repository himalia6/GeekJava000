package beans;

import beans.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * Spring Bean装配方式一： 使用xml方式
 */
@Slf4j
public class SpringBeanXmlLoading {
    public static void main(String[] args) {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
        final Object student = context.getBean("student");
        log.info("student {}", student);

        final Student student1 = context.getBean("student1", Student.class);
        log.info("student {}", student1);

        final Map<String, Student> students = context.getBeansOfType(Student.class);
        log.info("student {}", students);
    }
}

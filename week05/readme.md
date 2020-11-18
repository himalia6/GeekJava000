极客时间Java训练营第五周作业
=========================
#### 目录
1. [Spring Bean装配](#p01)
2. [自动配置和starter](#p02)
3. [JDBC和连接池](#p03)

<a id="p01"></a>
#### 1. 写代码实现Spring Bean的装配，方式越多越好（XML、Annotation都可以）,提 交到Github。

-1) 基于XML定义装配

在xml中定义bean:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="student" class="beans.domain.Student">
        <property name="name" value="John"/>
        <property name="age" value="20"/>
    </bean>

    <bean id="student1" class="beans.domain.Student">
        <property name="name" value="Emy"/>
        <property name="age" value="23"/>
    </bean>
</beans>
```
在java中加载xml定义的bean：
```java
    final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
    final Object student = context.getBean("student");
    log.info("student {}", student);
    
    final Student student1 = context.getBean("student1", Student.class);
    log.info("student {}", student1);
    
    final Map<String, Student> students = context.getBeansOfType(Student.class);
    log.info("student {}", students);
```

-2) 基于@Bean和@Configuration注解

定义Configuration和Bean:
```java
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
```
获取Bean：
```java
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("beans");
    context.refresh();
    
    final Object student = context.getBean(Student.class);
    log.info("student {}", student);
```

-3) 基于@Component、@Autowire、@ComponentScan等的隐式发现和装配

定义一个@Service类：
```java
@Service
public class DinningHall {
    public void provide() {
        log.info("dinning hall provides breakfast, dinner, supper...");
    }
}
```
使用@Autowired加载定义的Service的Bean:
```java
@Component
public class School {

    @Autowired
    private DinningHall dinningHall;

    public void run() {
        dinningHall.provide();
        log.info("school running smoothly.");
    }
}
```
通过ComponentScan获取加载的Bean:
```java
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("beans.scan");
    context.refresh();
    
    final School school = context.getBean(School.class);
    school.run();
```

<a id="p02"></a>
#### 2. 给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。
通过此starter，期望可以实现的目标：
1. 管理配置文件相关属性，调用输出新配置的值
2. 自动装配Service Bean，调用方法并输出结果

创建属性类保存配置信息：
[SchoolConfigurationProperties.java](src/main/java/starter/config/SchoolConfigurationProperties.java)
```java
//@Configuration //schoolConfigurationProperties
@ConfigurationProperties(prefix = "school") //school-starter.config.SchoolConfigurationProperties
@Data
public class SchoolConfigurationProperties {
    private static final String SCHOOL_NAME = "hogwarts";

    // school.name in application.properties/yml
    private String name = SCHOOL_NAME;
}
```

提供服务的Service类：
[SchoolService.java](src/main/java/starter/service/SchoolService.java)
```java
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
```
自动化配置类以及Bean装配：
[SchoolAutoConfiguration.java](src/main/java/starter/SchoolAutoConfiguration.java)
```java
@Configuration
@ConditionalOnClass(SchoolService.class)
// 需要设定school.open属性值才能正确加载
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
```
最后需要在[resources/META-INF/spring.factories](src/main/resources/META-INF/spring.factories)注册AutoConfiguration类：
```text
org.springframework.boot.autoconfigure.EnableAutoConfiguration=starter.SchoolAutoConfiguration
```
以及配置文件参数元数据[additional-spring-configuration-metadata.json](src/main/resources/META-INF/additional-spring-configuration-metadata.json)：
```json
{
  "properties": [
    {
      "name": "school.open",
      "type": "java.lang.String",
      "description": "Description for school.open."
  },
    {
      "name": "school.name",
      "type": "java.lang.String",
      "description": "Description for school.open."
    }
]
}
```

**调用定义的starter**
1. 首先需要在pom.xml引入依赖，如下：
```xml
    <dependencies>
        <dependency>
            <groupId>com.example.java</groupId>
            <artifactId>week05</artifactId>
            <version>0.1-SNAPSHOT</version>
        </dependency>
        ...
    </dependencies>
```
2. 添加配置项到application.yml/properties:
```yaml
school.open: yes  # 必须有，否则如定义中无法正确装配
school.name: "magic_school" # name字段属性会覆盖默认值
```
3. 加载并使用配置的Bean：
```java
@Component
@Slf4j
public class SchoolController {
    @Autowired
    private SchoolConfigurationProperties schoolConfigProperties;

    @Autowired
    private List<Student> students;

    @Autowired
    private SchoolService schoolService;

    public void run() {
        log.info("school properties: {}", schoolConfigProperties.getName());
        for (Student student: students) {
            schoolService.register(student);
        }
    }
}
```
最终我们可以做到：自动装配starter中配置的Bean，读取到相关配置项的值，调用服务输出结果。


<a id="p03"></a>
#### 3. 研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：
* 1）使用 JDBC 原生接口，实现数据库的增删改查操作。 
* 2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。 
* 3）配置 Hikari 连接池，改进上述操作。提交代码到 Github。



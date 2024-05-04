import com.student.entity.Student;
import com.student.service.IStudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  PACKAGE_NAME
 * ClassName:    StudentTestPlus
 *
 * @Author chnpngwng
 * @Date 2024 05 04 16 19
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application.xml")
public class StudentTestPlus {

    @Autowired
    private IStudentService studentService;

    // 根据ID查询
    @Test
    public void testSelectStudentById() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        IStudentService studentService = applicationContext.getBean("studentService", IStudentService.class);
        Student student = studentService.getById(2);
        System.out.println(student);
    }
}

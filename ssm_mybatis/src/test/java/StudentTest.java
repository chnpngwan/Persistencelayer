import com.student.entity.Student;
import com.student.mapper.StudentMapper;
import com.student.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  PACKAGE_NAME
 * ClassName:    StudentTest
 *
 * @Author chnpngwng
 * @Date 2024 05 02 20 24
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application.xml")
public class StudentTest {

    @Autowired
    @Qualifier("studentService")
    private StudentService studentService;

    // 根据ID查询
    @Test
    public void testSelectStudentById() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        Student student = studentService.selectByPrimaryKey(1);
        System.out.println(student);
    }

    // 全表查询
    @Test
    public void testSelectStudentAll() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        List<Student> studentList = studentService.selectAll();
        for (Student studentAll : studentList) {
            System.out.println(studentAll);
        }
    }

    // 添加
    @Test
    public void testInsertStudent() throws ParseException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        Student studentInsert = new Student();
        studentInsert.setUsername("student0");
        studentInsert.setPasswordHash("123456");
        studentInsert.setFirstName("Samantha");
        studentInsert.setLastName("Hernandez");
        // 时间转换
        String ordDateOfBirth = "2001-02-14";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = dateFormat.parse(ordDateOfBirth);
        studentInsert.setDateOfBirth(dateOfBirth);
        studentInsert.setGender("Female");
        studentInsert.setAddress("234 Strawberry St, Valley");
        studentInsert.setEmail("samantha@example.com");
        studentInsert.setPhoneNumber("2345678901");
        studentInsert.setGuardianName("Samuel Hernandez");
        studentInsert.setGuardianPhoneNumber("1234567890");
        String ordAdmissionDate = "2019-09-01";
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date admissionDate = dateFormat.parse(ordAdmissionDate);
        studentInsert.setAdmissionDate(admissionDate);
        String ordGraduationDate = "2023-06-30";
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date graduationDate = dateFormat.parse(ordGraduationDate);
        studentInsert.setAdmissionDate(graduationDate);
        studentInsert.setDepartment("Electrical Engineering");
        studentInsert.setMajor("Power Systems");
        studentInsert.setCurrentYear(5);
        int i = studentService.insert(studentInsert);
        if (i == 1) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
    }

    // 修改
    @Test
    public void testUpdateStudent() throws ParseException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        Student studentInsert = new Student();
        studentInsert.setStudentId(26);
        studentInsert.setUsername("student");
        studentInsert.setPasswordHash("456789");
        studentInsert.setFirstName("Samantha");
        studentInsert.setLastName("Hernandez");
        // 时间转换
        String ordDateOfBirth = "2001-02-14";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = dateFormat.parse(ordDateOfBirth);
        studentInsert.setDateOfBirth(dateOfBirth);
        studentInsert.setGender("Female");
        studentInsert.setAddress("234 Strawberry St, Valley");
        studentInsert.setEmail("samantha@example.com");
        studentInsert.setPhoneNumber("2345678901");
        studentInsert.setGuardianName("Samuel Hernandez");
        studentInsert.setGuardianPhoneNumber("1234567890");
        String ordAdmissionDate = "2019-09-01";
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date admissionDate = dateFormat.parse(ordAdmissionDate);
        studentInsert.setAdmissionDate(admissionDate);
        String ordGraduationDate = "2023-06-30";
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date graduationDate = dateFormat.parse(ordGraduationDate);
        studentInsert.setAdmissionDate(graduationDate);
        studentInsert.setDepartment("Electrical Engineering");
        studentInsert.setMajor("Power Systems");
        studentInsert.setCurrentYear(5);
        int i = studentService.updateByPrimaryKey(studentInsert);
        if (i == 1) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
    }
    // 根据ID删除
    @Test
    public void testDeleteStudentById() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        int i = studentService.deleteByPrimaryKey(26);
        if (i == 1) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
    }
    // 根据username查询
    @Test
    public void testSelectByUsername() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        Student studentByUsername = studentService.selectByUsername("student10");
        System.out.println(studentByUsername);
    }

    // 根据username进行模糊查询
    @Test
    public void testQueryByUserName() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        List<Student> studentQueryByUserName = studentService.queryByUserName("student");
        for (Student student: studentQueryByUserName) {
            System.out.println(student);
        }
    }

    @Test
    public void test7_foreach() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        // 准备测试数据
        List<Integer> currentYearIds = Arrays.asList(2, 3);
        List<Student> studentList = studentService.getStudentListByClassIds_foreach_array(currentYearIds);
        for (Student student : studentList) {
            System.out.println(student.toString());
        }
    }

    @Test
    public void test7_2_foreach() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        StudentService studentService = applicationContext.getBean("studentService", StudentService.class);
        ArrayList<String> usernameList = new ArrayList<String>();
        usernameList.add("student1");
        usernameList.add("student10");
        List<Student> list = this.studentService.getStudentListByClassIds_foreach_list(usernameList);
        for (Student e : list) {
            System.out.println(e.toString());
        }
    }
}

package cn.org.fluent.mybatis.demo.rich.test;


import cn.org.fluent.mybatis.demo.rich.AppMain;
import cn.org.fluent.mybatis.demo.rich.entity.StudentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AppMain.class)
public class RichEntityDemo {
    @Test
    public void testEntity() {
        StudentEntity student1 = new StudentEntity()
            .setUserName("张三")
            .setAge(25)
            .setEnv("test");
        student1.save();
        long id = student1.getId();
        StudentEntity student2 = new StudentEntity().setId(id).findById();
        System.out.println(student2.getUserName());

        student2.deleteById();
        StudentEntity student3 = new StudentEntity().setId(id).findById();
        System.out.println(student3);
    }

    @Test
    public void queryByForm() {

    }
}

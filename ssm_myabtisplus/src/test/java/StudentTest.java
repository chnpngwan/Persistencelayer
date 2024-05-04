import com.student.controller.StudentController;
import com.student.entity.Student;
import com.student.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
/**
 * ProjectName:  Persistencelayer
 * PackageName:  PACKAGE_NAME
 * ClassName:    StudentTest
 *
 * @Author chnpngwng
 * @Date 2024 05 04 08 39
 **/
@Slf4j
public class StudentTest {
    @Mock
    private IStudentService iStudentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllUser() {
        // Arrange
        List<Student> mockStudentList = Arrays.asList();
        System.out.println(mockStudentList);
        when(iStudentService.list()).thenReturn(mockStudentList);

        // Act
        ModelAndView modelAndView = studentController.findAllUser();
        System.out.println(mockStudentList);

        // Assert
        assertEquals("index", modelAndView.getViewName());
        assertEquals(mockStudentList, modelAndView.getModelMap().getAttribute("studentList"));
    }
}

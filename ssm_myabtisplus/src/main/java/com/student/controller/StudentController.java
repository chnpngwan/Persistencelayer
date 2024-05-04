package com.student.controller;

import com.student.entity.Student;
import com.student.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chnpngwng
 * @since 2024-05-03
 */
@Controller
public class StudentController {

    @Autowired
    public IStudentService iStudentService;

    /**
     * 查全部用户
     * @return
     */
    @RequestMapping
    public ModelAndView findAllUser(){
        List<Student> studentList = iStudentService.list();
        System.out.println(studentList);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("studentList",studentList);
        return mav;
    }
}

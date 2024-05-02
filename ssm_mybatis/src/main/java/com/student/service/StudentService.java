package com.student.service;

import com.student.entity.Student;

import java.util.List;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  com.student.service
 * ClassName:    StudentService
 *
 * @Author chnpngwng
 * @Date 2024 05 02 12 45
 **/
public interface StudentService {
    int deleteByPrimaryKey(Integer studentId);

    int insert(Student record);

    Student selectByPrimaryKey(Integer studentId);

    List<Student> selectAll();

    int updateByPrimaryKey(Student record);

    Student selectByUsername(String username);
}

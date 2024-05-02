package com.student.mapper;

import com.student.entity.Student;
import java.util.List;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  com.student
 * ClassName:    StudentMapper
 *
 * @Author chnpngwng
 * @Date 2024 05 02 20 24
 **/

public interface StudentMapper {
    int deleteByPrimaryKey(Integer studentId);

    int insert(Student record);

    Student selectByPrimaryKey(Integer studentId);

    List<Student> selectAll();

    int updateByPrimaryKey(Student record);

    Student selectByUsername(String username);
}
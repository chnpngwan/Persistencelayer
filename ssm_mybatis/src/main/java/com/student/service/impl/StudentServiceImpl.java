package com.student.service.impl;

import com.student.entity.Student;
import com.student.mapper.StudentMapper;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  com.student.service.impl
 * ClassName:    StudentServiceImpl
 *
 * @Author chnpngwng
 * @Date 2024 05 02 12 45
 **/
@Transactional
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public int deleteByPrimaryKey(Integer studentId) {
        return studentMapper.deleteByPrimaryKey(studentId);
    }

    @Override
    public int insert(Student record) {
        return studentMapper.insert(record);
    }

    @Override
    public Student selectByPrimaryKey(Integer studentId) {
        return studentMapper.selectByPrimaryKey(studentId);
    }

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Student record) {
        return studentMapper.updateByPrimaryKey(record);
    }

    @Override
    public Student selectByUsername(String username) {
        return studentMapper.selectByUsername(username);
    }
}

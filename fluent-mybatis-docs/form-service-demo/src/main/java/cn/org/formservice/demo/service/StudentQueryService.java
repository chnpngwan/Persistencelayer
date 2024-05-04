package cn.org.formservice.demo.service;

import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.formservice.demo.model.StdPagedQuery;
import cn.org.formservice.demo.model.Student;
import cn.org.formservice.demo.model.StudentQuery;
import cn.org.formservice.demo.model.TagPagedQuery;
import cn.org.formservice.demo.shared.entity.StudentEntity;

import java.util.List;

@SuppressWarnings("all")
@FormService(entity = StudentEntity.class)
public interface StudentQueryService {
    Student findStudent(StudentQuery student);

    long countStudentBy(StudentQuery student);

    List<Student> listStudentBy(StudentQuery student);

    StdPagedList<Student> stdPagedStudent(StdPagedQuery student);

    TagPagedList<Student> tagPagedStudent(TagPagedQuery hangzhou);
}
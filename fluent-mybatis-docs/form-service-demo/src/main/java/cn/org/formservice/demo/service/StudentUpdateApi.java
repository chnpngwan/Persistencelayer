package cn.org.formservice.demo.service;

import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.formservice.demo.model.Student;
import cn.org.formservice.demo.model.StudentUpdater;
import cn.org.formservice.demo.shared.entity.StudentEntity;

@FormService(entity = StudentEntity.class)
public interface StudentUpdateApi {
    @FormMethod(entity = StudentEntity.class, type = MethodType.Save)
    Student saveStudent(Student student);

    @FormMethod(type = MethodType.Update)
    int updateStudent(StudentUpdater student);
}
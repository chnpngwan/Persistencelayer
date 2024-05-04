package cn.org.formservice.demo.model;


import cn.org.atool.fluent.form.annotation.Entry;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Student {
    private Long id;

    private String userName;

    private String status;

    private String phone;

    @Entry("email")
    private String hisEmail;

    private Integer age;

    private String address;
}
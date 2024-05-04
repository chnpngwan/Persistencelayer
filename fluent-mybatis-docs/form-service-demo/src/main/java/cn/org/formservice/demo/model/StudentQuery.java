package cn.org.formservice.demo.model;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class StudentQuery {
    @NotNull
    @Entry(ignoreNull = false)
    private String userName;

    @Entry(type = EntryType.StartWith)
    private String address;

    @Size(min = 2, max = 2)
    @Entry(type = EntryType.Between)
    private int[] age;

    private Integer gender;

    /**
     * 默认正序
     */
    @Entry(type = EntryType.OrderBy, value = "userName")
    private boolean byUserName = true;
    /**
     * 默认倒序
     */
    @Entry(type = EntryType.OrderBy, value = "age")
    private boolean byAge;
}
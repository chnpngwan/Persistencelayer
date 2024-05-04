package cn.org.atool.fluent.mybatis.demo3.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * HelloWorldEntity: 简单演示实体类
 *
 * @author darui.wu@163.com
 */
@Data
@Accessors(chain = true)
@FluentMybatis(table = "hello_world")
public class HelloWorld3Entity implements IEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 根据@TableId, MyBatis在生成Mapper接口insert方法时, 会自动设置相关属性
     */
    @TableId
    private Long id;

    private String sayHello;

    private String yourName;

    @TableField(insert = "now()")
    private Date gmtCreated;

    @TableField(insert = "now()", update = "now()")
    private Date gmtModified;

    @TableField(insert = "0")
    private Boolean isDeleted;

    @Override
    public Class<? extends IEntity> entityClass() {
        return HelloWorld3Entity.class;
    }
}
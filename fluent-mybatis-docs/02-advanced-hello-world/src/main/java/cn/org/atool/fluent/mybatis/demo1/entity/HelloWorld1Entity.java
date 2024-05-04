package cn.org.atool.fluent.mybatis.demo1.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;

import java.util.Date;

/**
 * HelloWorldEntity: 简单演示实体类
 *
 * @author darui.wu@163.com
 */
@FluentMybatis(table = "hello_world")
public class HelloWorld1Entity implements IEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 根据@TableId, MyBatis在生成Mapper接口insert方法时, 会自动设置相关属性
     */
    @TableId
    private Long id;

    private String sayHello;

    private String yourName;

    private Date gmtCreated;

    private Date gmtModified;

    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSayHello() {
        return sayHello;
    }

    public void setSayHello(String sayHello) {
        this.sayHello = sayHello;
    }

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "HelloWorldEntity{" +
            "id=" + id +
            ", sayHello='" + sayHello + '\'' +
            ", yourName='" + yourName + '\'' +
            ", gmtCreate=" + gmtCreated +
            ", gmtModified=" + gmtModified +
            ", isDeleted=" + isDeleted +
            '}';
    }

    @Override
    public Class<? extends IEntity> entityClass() {
        return HelloWorld1Entity.class;
    }
}
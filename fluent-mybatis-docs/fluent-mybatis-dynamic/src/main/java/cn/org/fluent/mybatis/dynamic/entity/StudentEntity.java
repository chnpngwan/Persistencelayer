package cn.org.fluent.mybatis.dynamic.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.GmtCreate;
import cn.org.atool.fluent.mybatis.annotation.GmtModified;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * StudentEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
    table = "student",
    schema = "fluent_mybatis",
    useDao = false
)
public class StudentEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "编号"
  )
  private Long id;

  @TableField(
      value = "email",
      desc = "邮箱"
  )
  private String email;

  @TableField(
      value = "gender",
      desc = "性别"
  )
  private Integer gender;

  @TableField(
      value = "locked",
      desc = "状态(0:正常,1:锁定)"
  )
  private Integer locked;

  @TableField(
      value = "name",
      desc = "姓名"
  )
  private String name;

  @TableField(
      value = "phone",
      desc = "电话"
  )
  private String phone;

  @TableField(
      value = "gmt_created",
      insert = "now()",
      desc = "存入数据库的时间"
  )
  @GmtCreate
  private Date gmtCreated;

  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()",
      desc = "修改的时间"
  )
  @GmtModified
  private Date gmtModified;

  @TableField(
      value = "is_deleted",
      insert = "0"
  )
  @LogicDelete
  private Boolean isDeleted;

  @Override
  public final Class entityClass() {
    return StudentEntity.class;
  }
}

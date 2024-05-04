package cn.org.fluent.mybatis.springboot.demo.entity;

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
    schema = "fluent_mybatis"
)
public class StudentEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键id"
  )
  private Long id;

  @TableField(
      value = "address",
      desc = "家庭详细住址"
  )
  private String address;

  @TableField(
      value = "age",
      desc = "年龄"
  )
  private Integer age;

  @TableField(
      value = "birthday",
      desc = "生日"
  )
  private Date birthday;

  @TableField(
      value = "bonus_points",
      desc = "积分"
  )
  private Long bonusPoints;

  @TableField(
      value = "gender_man",
      desc = "性别, 0:女; 1:男"
  )
  private Boolean genderMan;

  @TableField(
      value = "grade",
      desc = "年级"
  )
  private Integer grade;

  @TableField(
      value = "home_county_id",
      desc = "家庭所在区县"
  )
  private Long homeCountyId;

  @TableField(
      value = "phone",
      desc = "电话"
  )
  private String phone;

  @TableField(
      value = "status",
      desc = "状态(字典)"
  )
  private String status;

  @TableField(
      value = "user_name",
      desc = "名字"
  )
  private String userName;

  @TableField(
      value = "gmt_created",
      insert = "now()",
      desc = "创建时间"
  )
  @GmtCreate
  private Date gmtCreated;

  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()",
      desc = "更新时间"
  )
  @GmtModified
  private Date gmtModified;

  @TableField(
      value = "is_deleted",
      insert = "0",
      desc = "是否逻辑删除"
  )
  @LogicDelete
  private Boolean isDeleted;

  @Override
  public final Class entityClass() {
    return StudentEntity.class;
  }
}

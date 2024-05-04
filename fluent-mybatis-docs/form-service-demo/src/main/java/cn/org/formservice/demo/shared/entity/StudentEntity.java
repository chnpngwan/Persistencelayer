package cn.org.formservice.demo.shared.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.formservice.demo.config.AppDefaultSetting;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@FluentMybatis(
    table = "student",
    schema = "fluent_mybatis",
    defaults = AppDefaultSetting.class
)
public class StudentEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId("id")
  private Long id;

  /**
   * 创建时间
   */
  @TableField(
      value = "gmt_created",
      insert = "now()"
  )
  private Date gmtCreated;

  /**
   * 更新时间
   */
  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()"
  )
  private Date gmtModified;

  /**
   * 是否逻辑删除
   */
  @TableField(
      value = "is_deleted",
      insert = "0"
  )
  @LogicDelete
  private Boolean isDeleted;

  /**
   * 家庭详细住址
   */
  @TableField("address")
  private String address;

  /**
   * 年龄
   */
  @TableField("age")
  private Integer age;

  /**
   * 生日
   */
  @TableField("birthday")
  private Date birthday;

  /**
   * 积分
   */
  @TableField("bonus_points")
  private Long bonusPoints;

  /**
   * 同桌
   */
  @TableField("desk_mate_id")
  private Long deskMateId;

  /**
   * 邮箱
   */
  @TableField("email")
  private String email;

  /**
   * 数据隔离环境
   */
  @TableField("env")
  private String env;

  /**
   * 性别, 0:女; 1:男
   */
  @TableField("gender")
  private Integer gender;

  /**
   * 年级
   */
  @TableField("grade")
  private Integer grade;

  /**
   * home_address外键
   */
  @TableField("home_address_id")
  private Long homeAddressId;

  /**
   * 家庭所在区县
   */
  @TableField("home_county_id")
  private Long homeCountyId;

  /**
   * 电话
   */
  @TableField("phone")
  private String phone;

  /**
   * 状态(字典)
   */
  @TableField("status")
  private String status;

  /**
   * 租户标识
   */
  @TableField("tenant")
  private Long tenant;

  /**
   * 名字
   */
  @TableField("user_name")
  private String userName;

  /**
   * 版本号
   */
  @TableField("version")
  private String version;

  @Override
  public final Class entityClass() {
    return StudentEntity.class;
  }
}
package cn.org.atool.fluent.mybatis.demo4.entity;

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
 * UserEntity: 数据映射实体定义
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
    table = "user",
    schema = "fluent_mybatis"
)
public class UserEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键id"
  )
  private Long id;

  @TableField(
      value = "account",
      desc = "账号"
  )
  private String account;

  @TableField(
      value = "address_id",
      desc = "外键，收货地址id"
  )
  private Long addressId;

  @TableField(
      value = "age",
      desc = "年龄"
  )
  private Long age;

  @TableField(
      value = "avatar",
      desc = "头像"
  )
  private String avatar;

  @TableField(
      value = "birthday",
      desc = "生日"
  )
  private Date birthday;

  @TableField(
      value = "bonus_points",
      desc = "会员积分"
  )
  private Long bonusPoints;

  @TableField(
      value = "e_mail",
      desc = "电子邮件"
  )
  private String eMail;

  @TableField(
      value = "password",
      desc = "密码"
  )
  private String password;

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
    return UserEntity.class;
  }
}

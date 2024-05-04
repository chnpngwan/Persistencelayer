package cn.org.atool.fluent.mybatis.demo4.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
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
 * ReceivingAddressEntity: 数据映射实体定义
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
    table = "receiving_address",
    schema = "fluent_mybatis"
)
public class ReceivingAddressEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键id"
  )
  private Long id;

  @TableField(
      value = "city",
      desc = "城市"
  )
  private String city;

  @TableField(
      value = "detail_address",
      desc = "详细住址"
  )
  private String detailAddress;

  @TableField(
      value = "district",
      desc = "区"
  )
  private String district;

  @TableField(
      value = "gmt_create",
      desc = "创建时间"
  )
  private Date gmtCreate;

  @TableField(
      value = "province",
      desc = "省份"
  )
  private String province;

  @TableField(
      value = "user_id",
      desc = "用户id"
  )
  private Long userId;

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
    return ReceivingAddressEntity.class;
  }
}

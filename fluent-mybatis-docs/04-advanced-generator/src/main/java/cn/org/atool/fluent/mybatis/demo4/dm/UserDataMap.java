package cn.org.atool.fluent.mybatis.demo4.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * UserDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("user")
@SuppressWarnings({"unused"})
public class UserDataMap extends TableDataMap<UserDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<UserDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "account",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<UserDataMap> account = new KeyValue<>(this, "account", "account", supplier);

  @ColumnDef(
      value = "address_id",
      type = "BIGINT"
  )
  public final transient KeyValue<UserDataMap> addressId = new KeyValue<>(this, "address_id", "addressId", supplier);

  @ColumnDef(
      value = "age",
      type = "BIGINT"
  )
  public final transient KeyValue<UserDataMap> age = new KeyValue<>(this, "age", "age", supplier);

  @ColumnDef(
      value = "avatar",
      type = "VARCHAR(255)"
  )
  public final transient KeyValue<UserDataMap> avatar = new KeyValue<>(this, "avatar", "avatar", supplier);

  @ColumnDef(
      value = "birthday",
      type = "DATETIME"
  )
  public final transient KeyValue<UserDataMap> birthday = new KeyValue<>(this, "birthday", "birthday", supplier);

  @ColumnDef(
      value = "bonus_points",
      type = "BIGINT",
      defaultValue = "0"
  )
  public final transient KeyValue<UserDataMap> bonusPoints = new KeyValue<>(this, "bonus_points", "bonusPoints", supplier);

  @ColumnDef(
      value = "e_mail",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<UserDataMap> eMail = new KeyValue<>(this, "e_mail", "eMail", supplier);

  @ColumnDef(
      value = "password",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<UserDataMap> password = new KeyValue<>(this, "password", "password", supplier);

  @ColumnDef(
      value = "phone",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<UserDataMap> phone = new KeyValue<>(this, "phone", "phone", supplier);

  @ColumnDef(
      value = "status",
      type = "VARCHAR(32)"
  )
  public final transient KeyValue<UserDataMap> status = new KeyValue<>(this, "status", "status", supplier);

  @ColumnDef(
      value = "user_name",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<UserDataMap> userName = new KeyValue<>(this, "user_name", "userName", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<UserDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<UserDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<UserDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  UserDataMap(boolean isTable) {
    super("user", isTable);
  }

  UserDataMap(boolean isTable, int size) {
    super("user", isTable, size);
  }

  /**
   * 创建UserDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public UserDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public static UserDataMap table() {
    return new UserDataMap(true, 1);
  }

  public static UserDataMap table(int size) {
    return new UserDataMap(true, size);
  }

  public static UserDataMap entity() {
    return new UserDataMap(false, 1);
  }

  public static UserDataMap entity(int size) {
    return new UserDataMap(false, size);
  }

  public static class Factory extends BaseFactory<UserDataMap> {
    public Factory() {
      super(UserDataMap.class);
    }
  }
}

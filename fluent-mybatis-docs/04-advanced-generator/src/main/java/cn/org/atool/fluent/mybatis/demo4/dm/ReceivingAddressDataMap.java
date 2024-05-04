package cn.org.atool.fluent.mybatis.demo4.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * ReceivingAddressDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("receiving_address")
@SuppressWarnings({"unused"})
public class ReceivingAddressDataMap extends TableDataMap<ReceivingAddressDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<ReceivingAddressDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "city",
      type = "VARCHAR(50)"
  )
  public final transient KeyValue<ReceivingAddressDataMap> city = new KeyValue<>(this, "city", "city", supplier);

  @ColumnDef(
      value = "detail_address",
      type = "VARCHAR(100)"
  )
  public final transient KeyValue<ReceivingAddressDataMap> detailAddress = new KeyValue<>(this, "detail_address", "detailAddress", supplier);

  @ColumnDef(
      value = "district",
      type = "VARCHAR(50)"
  )
  public final transient KeyValue<ReceivingAddressDataMap> district = new KeyValue<>(this, "district", "district", supplier);

  @ColumnDef(
      value = "gmt_create",
      type = "DATETIME"
  )
  public final transient KeyValue<ReceivingAddressDataMap> gmtCreate = new KeyValue<>(this, "gmt_create", "gmtCreate", supplier);

  @ColumnDef(
      value = "province",
      type = "VARCHAR(50)"
  )
  public final transient KeyValue<ReceivingAddressDataMap> province = new KeyValue<>(this, "province", "province", supplier);

  @ColumnDef(
      value = "user_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<ReceivingAddressDataMap> userId = new KeyValue<>(this, "user_id", "userId", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<ReceivingAddressDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<ReceivingAddressDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  ReceivingAddressDataMap(boolean isTable) {
    super("receiving_address", isTable);
  }

  ReceivingAddressDataMap(boolean isTable, int size) {
    super("receiving_address", isTable, size);
  }

  /**
   * 创建ReceivingAddressDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public ReceivingAddressDataMap init() {
    this.id.autoIncrease();
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public static ReceivingAddressDataMap table() {
    return new ReceivingAddressDataMap(true, 1);
  }

  public static ReceivingAddressDataMap table(int size) {
    return new ReceivingAddressDataMap(true, size);
  }

  public static ReceivingAddressDataMap entity() {
    return new ReceivingAddressDataMap(false, 1);
  }

  public static ReceivingAddressDataMap entity(int size) {
    return new ReceivingAddressDataMap(false, size);
  }

  public static class Factory extends BaseFactory<ReceivingAddressDataMap> {
    public Factory() {
      super(ReceivingAddressDataMap.class);
    }
  }
}

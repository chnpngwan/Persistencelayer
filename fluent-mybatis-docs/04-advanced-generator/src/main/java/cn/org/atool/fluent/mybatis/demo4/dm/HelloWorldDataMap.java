package cn.org.atool.fluent.mybatis.demo4.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * HelloWorldDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("hello_world")
@SuppressWarnings({"unused"})
public class HelloWorldDataMap extends TableDataMap<HelloWorldDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<HelloWorldDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "say_hello",
      type = "VARCHAR(100)"
  )
  public final transient KeyValue<HelloWorldDataMap> sayHello = new KeyValue<>(this, "say_hello", "sayHello", supplier);

  @ColumnDef(
      value = "your_name",
      type = "VARCHAR(100)"
  )
  public final transient KeyValue<HelloWorldDataMap> yourName = new KeyValue<>(this, "your_name", "yourName", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<HelloWorldDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<HelloWorldDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<HelloWorldDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  HelloWorldDataMap(boolean isTable) {
    super("hello_world", isTable);
  }

  HelloWorldDataMap(boolean isTable, int size) {
    super("hello_world", isTable, size);
  }

  /**
   * 创建HelloWorldDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public HelloWorldDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public static HelloWorldDataMap table() {
    return new HelloWorldDataMap(true, 1);
  }

  public static HelloWorldDataMap table(int size) {
    return new HelloWorldDataMap(true, size);
  }

  public static HelloWorldDataMap entity() {
    return new HelloWorldDataMap(false, 1);
  }

  public static HelloWorldDataMap entity(int size) {
    return new HelloWorldDataMap(false, size);
  }

  public static class Factory extends BaseFactory<HelloWorldDataMap> {
    public Factory() {
      super(HelloWorldDataMap.class);
    }
  }
}

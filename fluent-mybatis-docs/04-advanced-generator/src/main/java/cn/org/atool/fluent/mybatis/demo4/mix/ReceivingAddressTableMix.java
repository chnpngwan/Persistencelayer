package cn.org.atool.fluent.mybatis.demo4.mix;

import cn.org.atool.fluent.mybatis.demo4.dm.ReceivingAddressDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[receiving_address]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class ReceivingAddressTableMix extends BaseMix<ReceivingAddressTableMix, ReceivingAddressDataMap> implements IMix {
  public ReceivingAddressTableMix() {
    super("receiving_address");
  }

  @Step("清空表[receiving_address]数据")
  public ReceivingAddressTableMix cleanReceivingAddressTable() {
    return super.cleanTable();
  }

  @Step("准备表[receiving_address]数据{1}")
  public ReceivingAddressTableMix readyReceivingAddressTable(ReceivingAddressDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[receiving_address]有全表数据{1}")
  public ReceivingAddressTableMix checkReceivingAddressTable(ReceivingAddressDataMap data,
      EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[receiving_address]有符合条件{1}的数据{2}")
  public ReceivingAddressTableMix checkReceivingAddressTable(String where,
      ReceivingAddressDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[receiving_address]有符合条件{1}的数据{2}")
  public ReceivingAddressTableMix checkReceivingAddressTable(ReceivingAddressDataMap where,
      ReceivingAddressDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[receiving_address]有{1}条符合条件{2}的数据")
  public ReceivingAddressTableMix countReceivingAddressTable(int count,
      ReceivingAddressDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[receiving_address]有{1}条符合条件{2}的数据")
  public ReceivingAddressTableMix countReceivingAddressTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[receiving_address]有{1}条数据")
  public ReceivingAddressTableMix countReceivingAddressTable(int count) {
    return super.countTable(count);
  }
}

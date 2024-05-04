package cn.org.atool.fluent.mybatis.demo4.mix;

import cn.org.atool.fluent.mybatis.demo4.dm.UserDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[user]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class UserTableMix extends BaseMix<UserTableMix, UserDataMap> implements IMix {
  public UserTableMix() {
    super("user");
  }

  @Step("清空表[user]数据")
  public UserTableMix cleanUserTable() {
    return super.cleanTable();
  }

  @Step("准备表[user]数据{1}")
  public UserTableMix readyUserTable(UserDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[user]有全表数据{1}")
  public UserTableMix checkUserTable(UserDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[user]有符合条件{1}的数据{2}")
  public UserTableMix checkUserTable(String where, UserDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[user]有符合条件{1}的数据{2}")
  public UserTableMix checkUserTable(UserDataMap where, UserDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[user]有{1}条符合条件{2}的数据")
  public UserTableMix countUserTable(int count, UserDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[user]有{1}条符合条件{2}的数据")
  public UserTableMix countUserTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[user]有{1}条数据")
  public UserTableMix countUserTable(int count) {
    return super.countTable(count);
  }
}

package cn.org.atool.fluent.mybatis.demo4.mix;

import cn.org.atool.fluent.mybatis.demo4.dm.HelloWorldDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[hello_world]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class HelloWorldTableMix extends BaseMix<HelloWorldTableMix, HelloWorldDataMap> implements IMix {
  public HelloWorldTableMix() {
    super("hello_world");
  }

  @Step("清空表[hello_world]数据")
  public HelloWorldTableMix cleanHelloWorldTable() {
    return super.cleanTable();
  }

  @Step("准备表[hello_world]数据{1}")
  public HelloWorldTableMix readyHelloWorldTable(HelloWorldDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[hello_world]有全表数据{1}")
  public HelloWorldTableMix checkHelloWorldTable(HelloWorldDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[hello_world]有符合条件{1}的数据{2}")
  public HelloWorldTableMix checkHelloWorldTable(String where, HelloWorldDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[hello_world]有符合条件{1}的数据{2}")
  public HelloWorldTableMix checkHelloWorldTable(HelloWorldDataMap where, HelloWorldDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[hello_world]有{1}条符合条件{2}的数据")
  public HelloWorldTableMix countHelloWorldTable(int count, HelloWorldDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[hello_world]有{1}条符合条件{2}的数据")
  public HelloWorldTableMix countHelloWorldTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[hello_world]有{1}条数据")
  public HelloWorldTableMix countHelloWorldTable(int count) {
    return super.countTable(count);
  }
}

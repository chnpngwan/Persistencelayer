package cn.org.atool.fluent.mybatis.demo4;

import cn.org.atool.fluent.mybatis.demo4.dm.HelloWorldDataMap;
import cn.org.atool.fluent.mybatis.demo4.dm.ReceivingAddressDataMap;
import cn.org.atool.fluent.mybatis.demo4.dm.UserDataMap;
import cn.org.atool.fluent.mybatis.demo4.mix.HelloWorldTableMix;
import cn.org.atool.fluent.mybatis.demo4.mix.ReceivingAddressTableMix;
import cn.org.atool.fluent.mybatis.demo4.mix.UserTableMix;
import java.util.List;
import org.test4j.module.database.IDataSourceScript;
import org.test4j.module.spec.internal.MixProxy;

/**
 * ATM: Application Table Manager
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface ATM {
  DataMap dataMap = new DataMap();

  Table table = new Table();

  Mixes mixes = new Mixes();

  /**
   * 应用表名
   */
  class Table {
    public final String receivingAddress = "receiving_address";

    public final String helloWorld = "hello_world";

    public final String user = "user";
  }

  /**
   * table or entity data构造器
   */
  class DataMap {
    public final ReceivingAddressDataMap.Factory receivingAddress = new ReceivingAddressDataMap.Factory();

    public final HelloWorldDataMap.Factory helloWorld = new HelloWorldDataMap.Factory();

    public final UserDataMap.Factory user = new UserDataMap.Factory();
  }

  /**
   * 应用表数据操作
   */
  class Mixes {
    public final ReceivingAddressTableMix receivingAddressTableMix = MixProxy.proxy(ReceivingAddressTableMix.class);

    public final HelloWorldTableMix helloWorldTableMix = MixProxy.proxy(HelloWorldTableMix.class);

    public final UserTableMix userTableMix = MixProxy.proxy(UserTableMix.class);

    public void cleanAllTable() {
      this.receivingAddressTableMix.cleanReceivingAddressTable();
      this.helloWorldTableMix.cleanHelloWorldTable();
      this.userTableMix.cleanUserTable();
    }
  }

  /**
   * 应用数据库创建脚本构造
   */
  class Script implements IDataSourceScript {
    @Override
    public List<Class> getTableKlass() {
      return list(
      	ReceivingAddressDataMap.class,
      	HelloWorldDataMap.class,
      	UserDataMap.class
      );
    }

    @Override
    public IndexList getIndexList() {
      return new IndexList();
    }
  }
}

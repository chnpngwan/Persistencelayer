package cn.org.formservice.demo.shared;

import cn.org.formservice.demo.shared.dm.StudentDataMap;
import cn.org.formservice.demo.shared.mix.StudentTableMix;
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
    public final String student = "student";
  }

  /**
   * table or entity data构造器
   */
  class DataMap {
    public final StudentDataMap.Factory student = new StudentDataMap.Factory();
  }

  /**
   * 应用表数据操作
   */
  class Mixes {
    public final StudentTableMix studentTableMix = MixProxy.proxy(StudentTableMix.class);

    public void cleanAllTable() {
      this.studentTableMix.cleanStudentTable();
    }
  }

  /**
   * 应用数据库创建脚本构造
   */
  class Script implements IDataSourceScript {
    @Override
    public List<Class> getTableKlass() {
      return list(
      	StudentDataMap.class
      );
    }

    @Override
    public IndexList getIndexList() {
      return new IndexList();
    }
  }
}
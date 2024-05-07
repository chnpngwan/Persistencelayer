package com.extract;

import java.sql.*;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  com.extract
 * ClassName:    MySQLDataExtractor
 *
 * @Author chnpngwng
 * @Date 2024 05 06 18 10
 **/
public class MySQLDataExtractor {
    public static void main(String[] args) {
        String remoteServerIp = "172.20.35.220:3306";
        String remoteDatabaseName = "ds_pub";
        String remoteUsername = "root";
        String remotePassword = "Qq_656535";

        String localJdbcUrl = "jdbc:mysql://127.0.0.1:3307/db_test?useSSL=false&serverTimezone=UTC";
        String localDatabaseName = "db_test";
        String localUsername = "root";
        String localPassword = "123456";

        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接到远程数据库
            try (Connection remoteConnection = DriverManager.getConnection(
                    "jdbc:mysql://" + remoteServerIp + "/" + remoteDatabaseName,
                    remoteUsername, remotePassword);
                 // 连接到本地数据库
                 Connection localConnection = DriverManager.getConnection(localJdbcUrl, localUsername, localPassword)) {

                // 从远程数据库查询数据
                String querySql = "SELECT id, region_name FROM base_region";
                try (PreparedStatement remoteStatement = remoteConnection.prepareStatement(querySql);
                     ResultSet resultSet = remoteStatement.executeQuery()) {

                    // 准备本地数据库的插入语句
                    String insertSql = "INSERT INTO tb_test (id, region_name) VALUES (?, ?)";
                    try (PreparedStatement localStatement = localConnection.prepareStatement(insertSql)) {

                        // 遍历结果集并插入到本地数据库
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String regionName = resultSet.getString("region_name");

                            localStatement.setInt(1, id);
                            localStatement.setString(2, regionName);
                            localStatement.addBatch();

                            // 建议批量处理，提高效率
                            if (resultSet.getRow() % 100 == 0) {
                                localStatement.executeBatch(); // 执行批量插入
                                localConnection.commit(); // 提交事务
                            }
                        }
                        // 插入剩余的数据
                        localStatement.executeBatch();
                        localConnection.commit();
                    }
                }
            }
            System.out.println("数据迁移完成！");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // 异常处理，可能需要回滚事务
        }
    }
}

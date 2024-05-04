package cn.org.fluent.mybatis.env.demo.utils;

/**
 * 应用部署环境工具类
 */
public class EnvUtils {
    public static String currEnv() {
        // 应用启动时, 读取的环境变量, 这里简化为返回固定值演示
        return "test1";
    }
}

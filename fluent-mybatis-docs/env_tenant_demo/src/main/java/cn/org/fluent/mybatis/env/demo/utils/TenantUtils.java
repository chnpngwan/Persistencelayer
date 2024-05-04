package cn.org.fluent.mybatis.env.demo.utils;

/**
 * 获取用户所属租户信息工具类
 */
public class TenantUtils {
    /**
     * 租户A
     */
    static final long A_TENANT = 111111L;
    /**
     * 租户B
     */
    static final long B_TENANT = 222222L;

    /**
     * 租户信息一般根据登录用户身份来判断, 这里简化为偶数用户属于租户A, 奇数用户属于租户B
     *
     * @return
     */
    public static long findUserTenant() {
        long userId = loginUserId();
        if (userId % 2 == 0) {
            return A_TENANT;
        } else {
            return B_TENANT;
        }
    }

    /**
     * 当前登录的用户id, 一般从Session中获取
     *
     * @return
     */
    public static long loginUserId() {
        return 1L;
    }
}

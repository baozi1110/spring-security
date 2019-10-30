package cn.qp.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

/**
 * 在无法通过{@link Connection}映射用户ID的情况下，用于注册新用户的命令。
 * 在用户第三方登录完成后，根据拿到了唯一标识查询用户，当用户不存在时不进行跳转登录页，而是拿用户信息直接进行注册
 * @author BaoZi
 */
// @Component
public class DemoConnectionSignUp implements ConnectionSignUp {
    /**
     * Sign up a new user of the application from the connection.
     *
     * @param connection the connection
     * @return the new user id. May be null to indicate that an implicit local user profile could not be created.
     */
    @Override
    public String execute(Connection<?> connection) {
        // 根据社交用户信息默认创建用户并返回用户唯一标识,根据自己实际情况设定
        return connection.getDisplayName();
    }
}

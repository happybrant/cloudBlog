package com.kongfu.backend.model.vo;

import org.springframework.stereotype.Component;

/**
 * @author 付聪 持有用户信息(多线程)，用于代替 session 对象
 */
@Component
public class HostHolder {

    private ThreadLocal<LoginToken> users = new ThreadLocal<>();

    /**
     * 存储 User
     */
    public void setUser(LoginToken token) {
        users.set(token);
    }

    /**
     * 获取User
     *
     * @return
     */
    public LoginToken getUser() {
        return users.get();
    }

    /**
     * 清理
     */
    public void clear() {
        users.remove();
    }
}

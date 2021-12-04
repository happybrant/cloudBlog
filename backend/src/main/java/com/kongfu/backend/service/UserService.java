package com.kongfu.backend.service;
import com.kongfu.backend.dao.UserMapper;
import com.kongfu.backend.entity.LoginTicket;
import com.kongfu.backend.entity.User;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import com.kongfu.backend.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 付聪
 */
@Service
public class UserService implements BlogConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>(16);

        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        //验证账号
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
        //验证状态
        if(user.getStatus() == 0){
            map.put("usernameMsg", "该账号未激活");
            return map;
        }
        //验证密码
        if(!user.getPassword().equals(BlogUtil.md5(password + user.getSalt()))){
            map.put("passwordMsg", "密码错误");
            return map;
        }
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        //随机凭证
        loginTicket.setTicket(BlogUtil.generateUUID());
        //设置凭证状态为有效（当用户登出的时候，设置凭证状态为无效）
        loginTicket.setStatus(1);
        //设置凭证到期时间
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        // 将登录凭证存入 redis
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
     * 根据 ticket 查询 LoginTicket 信息
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket){
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    public User findUserById(int userId){
        User user = getUserCache(userId);
        if(user == null){
            user = initUserCache(userId);
        }
        return user;
    }

    /**
     * 将用户信息初始化在缓存中，默认3600秒
     * @param userId
     * @return
     */
    public User initUserCache(int userId){
        User user = userMapper.selectByUserId(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        //第4个参数：时间单位，第3个参数：后面时间的倍数
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     *优先从缓存中取值
     * @param userId
     * @return
     */
    public User getUserCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 获取某个用户的权限
     * @param userId
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
/*                    case 2:
                        return AUTHORITY_MODERATOR;*/
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}

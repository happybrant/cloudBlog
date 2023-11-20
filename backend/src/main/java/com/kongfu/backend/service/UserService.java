package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.dao.NoteCategoryMapper;
import com.kongfu.backend.dao.UserMapper;
import com.kongfu.backend.model.dto.UserQuery;
import com.kongfu.backend.model.entity.NoteCategory;
import com.kongfu.backend.model.entity.User;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** @author 付聪 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements BlogConstant {

  @Resource private UserMapper userMapper;

  @Autowired private RedisTemplate<String, Serializable> redisTemplate;

  @Resource private NoteCategoryMapper noteCategoryMapper;
  @Resource private NoteCategoryService noteCategoryService;

  public ResponseResult<String> login(String username, String password) {

    // 空值处理
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "用户名或密码为空");
    }
    // 验证账号
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("username", username);
    queryWrapper.ne("status", 0);
    User user = userMapper.selectOne(queryWrapper);
    if (user == null) {
      return new ResponseResult<>(ResponseResultCode.Error, "该账号不存在");
    }
    // 验证状态
    if (user.getStatus() == 2) {
      return new ResponseResult<>(ResponseResultCode.Error, "该账号已停用");
    }
    // 验证密码
    if (!user.getPassword().equals(BlogUtil.md5(password + user.getSalt()))) {
      return new ResponseResult<>(ResponseResultCode.Error, "密码错误");
    }
    LoginToken loginToken = new LoginToken(user.getId(), user.getUsername(), user.getType());
    if (StringUtils.isEmpty(loginToken.getTicket())) {
      return new ResponseResult<>(ResponseResultCode.Error, "生成Ticket失败");
    } else {
      return new ResponseResult<>(ResponseResultCode.Success, "认证成功", loginToken.getTicket());
    }
  }

  /**
   * 分页查找用户
   *
   * @param userQuery
   * @return
   */
  public Page<User> getUserListPager(UserQuery userQuery) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", DELETE_STATUS);

    if (!StringUtils.isEmpty(userQuery.getName())) {
      queryWrapper
          .like("username", userQuery.getName())
          .or()
          .like("displayName", userQuery.getName());
    }
    if (userQuery.getStatus() != null) {
      queryWrapper.eq("status", userQuery.getStatus());
    }
    if (userQuery.getType() != null) {
      queryWrapper.eq("type", userQuery.getType());
    }
    Page<User> userPage = new Page<>(userQuery.getPageIndex(), userQuery.getPageSize());

    return userMapper.selectPage(userPage, queryWrapper);
  }

  /**
   * 用户退出（将用户加入到黑名单中并设置redis缓存过期时间为token过期时间）
   *
   * @param ticket
   */
  public ResponseResult<String> logout(String ticket) {
    // 解析token
    Claims claims = LoginToken.resolveTicket(ticket.substring(7));
    if (claims != null) {
      // 获取过期时间
      Date expiration = claims.getExpiration();
      if (new Date().after(expiration)) {
        return new ResponseResult<>(ResponseResultCode.Error, "token超时");
      }
      // 获取用户名
      String userName = claims.getIssuer();
      // 获取token唯一标识
      String jti = claims.getId();
      long exp = expiration.getTime() / 1000;
      long currentTimeSeconds = System.currentTimeMillis() / 1000;

      // 将该用户加入到黑名单中并设置token过期时间
      redisTemplate
          .opsForValue()
          .set(
              BlogConstant.TOKEN_BLACKLIST_PREFIX + jti,
              userName,
              (exp - currentTimeSeconds),
              TimeUnit.SECONDS);
      return new ResponseResult<>(ResponseResultCode.Success, "退出成功");
    } else {
      return new ResponseResult<>(ResponseResultCode.Error, "token格式异常");
    }
  }

  /**
   * 管理员新增用户，密码为初始密码123456
   *
   * @param user
   * @return
   */
  public int addUser(User user) {
    user.setSalt(BlogUtil.generateUUID().substring(0, 5));
    user.setPassword(BlogUtil.md5(BlogConstant.INITIAL_PASSWORD + user.getSalt()));
    int result = userMapper.insert(user);
    if (result > 0) {
      NoteCategory noteCategory = new NoteCategory();
      noteCategory.setName("默认分类");
      noteCategory.setCode("default");
      noteCategory.setDescription("笔记的默认分类，不允许删除");
      noteCategory.setOrder(noteCategoryService.getMaxOrder());
      noteCategory.setStatus(BlogConstant.PUBLISH_STATUS);
      noteCategory.setCreateUser(user.getId());
      noteCategory.setLastUpdateUser(user.getId());
      // 给用户创建默认笔记分类
      noteCategoryMapper.insert(noteCategory);
    }
    return result;
  }

  /**
   * 更新用户信息
   *
   * @param user
   * @return
   */
  public int updateUser(User user) {
    return userMapper.updateById(user);
  }

  /**
   * 根据id查找用户
   *
   * @param userId
   * @return
   */
  public User findUserById(int userId) {
    return userMapper.selectById(userId);
  }

  /**
   * 根据id删除用户,逻辑删除
   *
   * @param ids
   * @return
   */
  public int deleteUser(List<Integer> ids) {
    int count = 0;
    for (int id : ids) {
      User user = userMapper.selectById(id);
      if (user != null) {
        // 将状态修改为0
        user.setStatus(BlogConstant.DELETE_STATUS);
        count += userMapper.updateById(user);
      }
    }
    return count;
  }
}

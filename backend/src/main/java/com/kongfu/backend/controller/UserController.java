package com.kongfu.backend.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.annotation.Authentication;
import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.UserQuery;
import com.kongfu.backend.model.entity.User;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.model.vo.UserInfo;
import com.kongfu.backend.service.UserService;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/** @author 付聪 */
@RestController
@RequestMapping("/user")
public class UserController implements BlogConstant {

  @Autowired private UserService userService;

  /**
   * 用户列表
   *
   * @return
   */
  @PostMapping("/list")
  @Authentication(role = 1, menu = "获取用户列表")
  @Log(menu = "用户管理", description = "获取用户列表")
  public ResponseResult<Page<User>> getUserList(@RequestBody Map<String, Object> map) {
    Page<User> notePage = userService.getUserListPager(getUserQuery(map));
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", notePage);
  }

  /**
   * 接口查询条件
   *
   * @param map
   * @return
   */
  private UserQuery getUserQuery(Map<String, Object> map) {
    UserQuery query = new UserQuery();
    query.setPageIndex(0);
    query.setPageSize(Integer.MAX_VALUE);
    if (map != null && map.size() > 0) {
      Object pageIndex = map.get("pageIndex");
      Object pageSize = map.get("pageSize");
      if (pageSize != null && pageIndex != null) {
        if (StringUtils.isNumber(pageIndex.toString())
            && StringUtils.isNumber(pageSize.toString())) {
          query.setPageSize((Integer) pageSize);
          query.setPageIndex((Integer) pageIndex);
        }
      }
      Object name = map.get("name");
      if (name != null) {
        query.setName(name.toString());
      }
      Object type = map.get("type");
      if (type != null && StringUtils.isNumber(type.toString())) {
        query.setType((Integer) type);
      }
      Object status = map.get("status");
      if (status != null && StringUtils.isNumber(status.toString())) {
        query.setStatus((Integer) status);
      }
    }

    return query;
  }

  /**
   * 用户登陆
   *
   * @return
   */
  @PostMapping("/login")
  @Log(menu = "用户管理", description = "登录")
  public ResponseResult<String> login(@RequestBody Map<String, String> map) {
    if (map == null || map.size() == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空");
    }
    String username = map.get("username");
    String password = map.get("password");

    // 验证用户名和密码
    return userService.login(username, password);
  }

  /**
   * 获取用户信息
   *
   * @return
   */
  @GetMapping("/getUserInfo")
  @Log(menu = "用户管理", description = "获取用户信息")
  public ResponseResult<UserInfo> getUserInfo() {
    HttpServletRequest request;
    // 得到 HttpServletRequest
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      request = attributes.getRequest();
      String authorization = request.getHeader("authorization");
      LoginToken token = LoginToken.checkTicket(authorization.substring(7));
      if (token != null) {
        User user = userService.findUserById(token.getId());
        UserInfo userInfo =
            new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getType(),
                user.getHeaderUrl());

        return new ResponseResult<>(ResponseResultCode.Success, "操作成功", userInfo);
      }
    }
    return new ResponseResult<>(ResponseResultCode.AuthFailed, "权限不足，操作失败");
  }

  /**
   * 用户登出
   *
   * @param request
   * @return
   */
  @PostMapping("/logout")
  @Log(menu = "用户管理", description = "登出")
  public ResponseResult<String> logout(HttpServletRequest request) {
    String ticket = request.getHeader("authorization");
    return userService.logout(ticket);
  }

  /**
   * 新增用户
   *
   * @param user
   * @return
   */
  @PostMapping("/add")
  @Authentication(role = 1, menu = "新增用户")
  @Log(menu = "用户管理", description = "新增用户")
  public ResponseResult<String> addUser(@RequestBody User user) {
    ResponseResult<String> result;
    if (user == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = userService.addUser(user);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 修改用户
   *
   * @param user
   * @return
   */
  @PostMapping("/update")
  @Log(menu = "用户管理", description = "修改用户信息")
  public ResponseResult<String> updateUser(@RequestBody User user) {
    ResponseResult<String> result;
    if (user == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = userService.updateUser(user);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 修改用户密码
   *
   * @param map
   * @return
   */
  @PostMapping("/updatePwd")
  @Log(menu = "用户管理", description = "修改用户密码")
  public ResponseResult<String> updatePwd(@RequestBody Map<String, Object> map) {
    ResponseResult<String> result;
    if (map == null || map.size() == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    Object id = map.get("id");
    Object oldPwdObject = map.get("oldPwd");
    Object newPwdObject = map.get("newPwd");
    if (id == null || oldPwdObject == null || newPwdObject == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数异常，操作失败");
    }

    User currentUser = userService.findUserById((Integer) id);
    if (currentUser == null) {
      return new ResponseResult<>(ResponseResultCode.Error, "未找到对应用户");
    }
    if (!currentUser
        .getPassword()
        .equals(BlogUtil.md5(oldPwdObject.toString() + currentUser.getSalt()))) {
      return new ResponseResult<>(ResponseResultCode.Error, "原密码输入错误");
    }
    if (currentUser
        .getPassword()
        .equals(BlogUtil.md5(newPwdObject.toString() + currentUser.getSalt()))) {
      return new ResponseResult<>(ResponseResultCode.Error, "新密码不能和原密码保持一致");
    }
    currentUser.setPassword(BlogUtil.md5(newPwdObject.toString() + currentUser.getSalt()));
    int i = userService.updateUser(currentUser);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 重置密码
   *
   * @param id
   * @return
   */
  @GetMapping("/resetPwd")
  @Authentication(role = 1, menu = "重置密码")
  @Log(menu = "用户管理", description = "重置密码")
  public ResponseResult<String> resetPwd(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    User user = userService.findUserById(id);
    if (user == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "未找到对应用户");
    }
    user.setPassword(BlogConstant.INITIAL_PASSWORD);
    int i = userService.updateUser(user);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 删除用户
   *
   * @param params
   * @return
   */
  @PostMapping("/delete")
  @Authentication(role = 1, menu = "删除用户")
  @Log(menu = "用户管理", description = "删除用户")
  public ResponseResult<String> deleteUser(@RequestBody List<Integer> params) {
    ResponseResult<String> result;
    if (params == null || params.size() == 0) {
      result = new ResponseResult<>(ResponseResultCode.ParameterEmpty, "传入参数为空");
    } else {
      int i = userService.deleteUser(params);
      if (i > 0) {
        result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
      } else {
        result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
      }
    }
    return result;
  }
}

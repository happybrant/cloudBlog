package com.kongfu.backend.controller;

import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.SettingDto;
import com.kongfu.backend.model.entity.Setting;
import com.kongfu.backend.service.SettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author 付聪 */
@RestController
@RequestMapping("/setting")
public class SettingController {
  @Resource public SettingService settingService;

  /**
   * 分类列表
   *
   * @return
   */
  @GetMapping("/list")
  public ResponseResult<List<Setting>> getSettingList() {
    List<Setting> settingList = settingService.getSettingList();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", settingList);
  }

  /**
   * 查找当前用户设置
   *
   * @return
   */
  @GetMapping("/getSettingByCurrentUser")
  public ResponseResult<SettingDto> getSettingByUserId() {
    SettingDto setting = settingService.getSettingByCurrentUser();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", setting);
  }
  /**
   * 新增设置
   *
   * @param setting
   * @return
   */
  @PostMapping("/add")
  public ResponseResult<String> addSetting(@RequestBody SettingDto setting) {
    ResponseResult<String> result;
    if (setting == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    Setting existSetting = settingService.selectSettingByRouting(setting.getRouting());
    if (existSetting != null) {
      return new ResponseResult<>(ResponseResultCode.Error, "该路由设置已存在,请更换路由");
    }
    int i = settingService.addSetting(setting);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }
  /**
   * 更新设置
   *
   * @param setting
   * @return
   */
  @PostMapping("/update")
  public ResponseResult<String> updateSetting(@RequestBody SettingDto setting) {
    ResponseResult<String> result;
    if (setting == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int count = settingService.updateSetting(setting);
    if (count > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + count + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }
  /**
   * 根据id删除标签
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  public ResponseResult<String> deleteSetting(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    if (id == null || id == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = settingService.deleteSetting(id);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }
}

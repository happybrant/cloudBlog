package com.framework.backend.controller;

import com.framework.backend.annotation.Log;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.dto.SettingDto;
import com.framework.backend.model.entity.Setting;
import com.framework.backend.service.SettingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 付聪
 */
@RestController
@RequestMapping("/setting")
@ResponseResult
public class SettingController {
  @Autowired public SettingService settingService;

  /**
   * 设置列表
   *
   * @return
   */
  @GetMapping("/list")
  public List<Setting> getSettingList() {
    return settingService.getSettingList();
  }

  /**
   * 查找当前用户设置
   *
   * @return
   */
  @GetMapping("/getSettingByCurrentUser")
  public SettingDto getSettingByUserId() {
    return settingService.getSettingByCurrentUser();
  }

  /**
   * 新增设置
   *
   * @param setting
   * @return
   */
  @PostMapping("/add")
  @Log(module = "个人中心/博客设置", value = "新增用户设置")
  public String addSetting(@RequestBody SettingDto setting) {
    Setting existSetting = settingService.getSettingByRouting(setting.getRouting());
    if (existSetting != null) {
      throw new BusinessException("该路由设置已存在，请更换路由");
    }
    settingService.addSetting(setting);
    return setting.getId();
  }

  /**
   * 更新设置
   *
   * @param setting
   * @return
   */
  @PostMapping("/update")
  @Log(module = "个人中心/博客设置", value = "更新用户设置")
  public void updateSetting(@RequestBody SettingDto setting) {
    settingService.updateSetting(setting);
  }

  /**
   * 根据id删除设置
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(module = "个人中心/博客设置", value = "删除用户设置")
  public void deleteSetting(@RequestParam("id") String id) {
    settingService.deleteSetting(id);
  }
}

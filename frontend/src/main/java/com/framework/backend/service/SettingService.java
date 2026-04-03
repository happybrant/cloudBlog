package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.dao.SettingMapper;
import com.framework.backend.entity.Setting;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 付聪
 */
@Service
public class SettingService {

  @Autowired public SettingMapper settingMapper;

  /**
   * 查找当前用户设置
   *
   * @param
   * @return
   */
  public Setting getSettingByRouter(String router) {
    Setting setting = null;

    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", 1);
    queryWrapper.eq("routing", router);
    List<Setting> settings = settingMapper.selectList(queryWrapper);
    if (!settings.isEmpty()) {
      setting = settings.get(0);
    }
    return setting;
  }

  public String getDefaultRouting() {
    Setting setting = null;

    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("create_user", 1);
    List<Setting> settings = settingMapper.selectList(queryWrapper);
    if (!settings.isEmpty()) {
      setting = settings.get(0);
    }
    if (setting != null) {
      return setting.getRouting();
    }
    return "";
  }
}

package com.kongfu.frontend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.frontend.dao.SettingMapper;
import com.kongfu.frontend.entity.Setting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** @author 付聪 */
@Service
public class SettingService {

  @Resource public SettingMapper settingMapper;

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
    if (settings.size() > 0) {
      setting = settings.get(0);
    }
    return setting;
  }

  public String getDefaultRouting() {
    Setting setting = null;

    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("create_user", 1);
    List<Setting> settings = settingMapper.selectList(queryWrapper);
    if (settings.size() > 0) {
      setting = settings.get(0);
    }
    return setting.getRouting();
  }
}

package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.dao.SettingMapper;
import com.kongfu.backend.model.dto.SettingDto;
import com.kongfu.backend.model.entity.Setting;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/** @author 付聪 */
@Service
public class SettingService {

  @Resource public SettingMapper settingMapper;
  @Resource public HostHolder holder;

  /**
   * 查找设置列表
   *
   * @return
   */
  public List<Setting> getSettingList() {
    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    queryWrapper.orderByAsc("create_time");
    return settingMapper.selectList(queryWrapper);
  }

  /**
   * 查找当前用户设置
   *
   * @param
   * @return
   */
  public SettingDto getSettingByCurrentUser() {
    LoginToken user = holder.getUser();
    SettingDto settingDto = null;
    int currentUserId = 0;
    if (user != null) {
      currentUserId = user.getId();
    }
    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    queryWrapper.eq("create_user", currentUserId);
    List<Setting> settings = settingMapper.selectList(queryWrapper);
    if (settings.size() > 0) {
      Setting setting = settings.get(0);
      settingDto =
          new SettingDto(
              setting.getId(),
              setting.getName(),
              setting.getTitle(),
              setting.getLocation(),
              setting.getAvatarUrl(),
              setting.getRouting());
      List<String> controls = new ArrayList<>();
      if (setting.getCategory() == 1) {
        controls.add("分类");
      }
      if (setting.getTag() == 1) {
        controls.add("标签");
      }
      if (setting.getArchive() == 1) {
        controls.add("归档");
      }
      if (setting.getRecentPost() == 1) {
        controls.add("最近文章");
      }
      if (setting.getLink() == 1) {
        controls.add("链接");
      }
      settingDto.setControls(controls);
    }
    return settingDto;
  }

  /**
   * 根据路由查找设置，防止重复路由
   *
   * @param routing
   * @return
   */
  public Setting selectSettingByRouting(String routing) {
    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    queryWrapper.eq("routing", routing);
    List<Setting> settings = settingMapper.selectList(queryWrapper);
    if (settings.size() > 0) {
      return settings.get(0);
    }
    return null;
  }
  /**
   * 新增设置
   *
   * @param settingDto
   * @return
   */
  public int addSetting(SettingDto settingDto) {
    Setting setting =
        new Setting(
            settingDto.getId(),
            settingDto.getName(),
            settingDto.getTitle(),
            settingDto.getLocation(),
            settingDto.getAvatarUrl(),
            settingDto.getRouting());
    List<String> controls = settingDto.getControls();
    for (String control : controls) {
      if ("分类".equals(control)) {
        setting.setCategory(1);
      }
      if ("标签".equals(control)) {
        setting.setTag(1);
      }
      if ("归档".equals(control)) {
        setting.setArchive(1);
      }
      if ("最近文章".equals(control)) {
        setting.setRecentPost(1);
      }
      if ("链接".equals(control)) {
        setting.setLink(1);
      }
    }
    setting.setStatus(BlogConstant.PUBLISH_STATUS);
    return settingMapper.insert(setting);
  }
  /**
   * 根据id更新分类
   *
   * @param settingDto
   * @return
   */
  public int updateSetting(SettingDto settingDto) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Setting setting =
        new Setting(
            settingDto.getId(),
            settingDto.getName(),
            settingDto.getTitle(),
            settingDto.getLocation(),
            settingDto.getAvatarUrl(),
            settingDto.getRouting());
    List<String> controls = settingDto.getControls();
    for (String control : controls) {
      if ("分类".equals(control)) {
        setting.setCategory(1);
      }
      if ("标签".equals(control)) {
        setting.setTag(1);
      }
      if ("归档".equals(control)) {
        setting.setArchive(1);
      }
      if ("最近文章".equals(control)) {
        setting.setRecentPost(1);
      }
      if ("链接".equals(control)) {
        setting.setLink(1);
      }
    }
    return settingMapper.updateById(setting);
  }
  /**
   * 根据id删除标签,逻辑删除
   *
   * @param id
   * @return
   */
  public int deleteSetting(int id) {
    Setting setting = settingMapper.selectById(id);
    if (setting != null) {
      // 将状态修改为0
      setting.setStatus(BlogConstant.DELETE_STATUS);
      return settingMapper.updateById(setting);
    }
    return 0;
  }
}

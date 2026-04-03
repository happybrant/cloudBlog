package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.common.ResponseData;
import com.framework.backend.mapper.SettingMapper;
import com.framework.backend.model.dto.SettingDto;
import com.framework.backend.model.entity.Setting;
import com.framework.backend.util.BlogConstant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author 付聪
 */
@Service
@Slf4j
public class SettingService {

  @Autowired public SettingMapper settingMapper;

  @Autowired private RestTemplate restTemplate;

  @Value("${restTemplate.url}")
  private String restTemplateUrl;

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
    // LoginToken user = holder.getUser();
    SettingDto settingDto = null;
    int currentUserId = 0;
    //    if (user != null) {
    //      currentUserId = user.getId();
    //    }
    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    queryWrapper.eq("create_user", currentUserId);
    List<Setting> settings = settingMapper.selectList(queryWrapper);
    if (!settings.isEmpty()) {
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
  public Setting getSettingByRouting(String routing) {
    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    queryWrapper.eq("routing", routing);
    List<Setting> settings = settingMapper.selectList(queryWrapper);
    if (!settings.isEmpty()) {
      return settings.get(0);
    }
    return null;
  }

  /**
   * 新增设置
   *
   * @param settingDto
   */
  public void addSetting(SettingDto settingDto) {
    Setting setting = new Setting();
    setting.setName(settingDto.getName());
    setting.setTitle(settingDto.getTitle());
    setting.setLocation(settingDto.getLocation());
    setting.setAvatarUrl(settingDto.getAvatarUrl());
    setting.setRouting(settingDto.getRouting());
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
    refreshStatisticCache(settingDto.getRouting());
    settingMapper.insert(setting);
  }

  /**
   * 根据id更新分类
   *
   * @param settingDto
   */
  public void updateSetting(SettingDto settingDto) {
    Setting setting = new Setting();
    if (settingDto.getId() == null) {
      return;
    }
    // 获取原路由
    Setting originSetting = settingMapper.selectById(settingDto.getId());
    if (originSetting == null) {
      return;
    }
    // 如果router不为空，表示单独修改路由，忽略其余信息
    if (StringUtils.isNotBlank(settingDto.getRouting())) {
      String oldRouter = originSetting.getRouting();
      String newRouter = settingDto.getRouting();
      setting.setId(settingDto.getId());
      setting.setRouting(settingDto.getRouting());
      settingMapper.updateById(setting);
      // 重新设置缓存
      resetCache(newRouter, oldRouter);
    } else {
      setting =
          new Setting(
              settingDto.getId(),
              settingDto.getName(),
              settingDto.getTitle(),
              settingDto.getLocation(),
              settingDto.getAvatarUrl());
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
      settingMapper.updateById(setting);
      refreshStatisticCache(originSetting.getRouting());
    }
  }

  /**
   * 根据id删除设置，逻辑删除
   *
   * @param id
   */
  public void deleteSetting(String id) {
    Setting setting = settingMapper.selectById(id);
    if (setting != null) {
      // 将状态修改为0
      setting.setStatus(BlogConstant.DELETE_STATUS);
      settingMapper.updateById(setting);
    }
  }

  /** 刷新博客前端设置数据缓存 */
  public void refreshStatisticCache(String routing) {
    String url = restTemplateUrl + "/home/refreshSettingCache?router=" + routing;
    // 发起请求,直接返回对象
    //    ResponseData responseData = restTemplate.getForObject(url, ResponseData.class);
    //    if (responseData != null) {
    //      log.info(responseData.getMessage());
    //    } else {
    //      log.info("缓存更新失败");
    //    }
  }

  /**
   * 重设所有缓存
   *
   * @param newRouter
   * @param oldRouter
   */
  public void resetCache(String newRouter, String oldRouter) {
    String url =
        restTemplateUrl
            + "/home/refreshSettingCache?newRouter="
            + newRouter
            + "&oldRouter="
            + oldRouter;
    // 发起请求,直接返回对象
    ResponseData responseData = restTemplate.getForObject(url, ResponseData.class);
    if (responseData != null) {
      log.info(responseData.getMessage());
    } else {
      log.info("缓存更新失败");
    }
  }

  /**
   * 根据用户id获取路由
   *
   * @param userId
   * @return
   */
  public String getRoutingByUserId(String userId) {
    QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    queryWrapper.eq("create_user", userId);
    Setting setting = settingMapper.selectOne(queryWrapper);
    if (setting != null) {
      return setting.getRouting();
    }
    return "";
  }
}

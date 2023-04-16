package com.kongfu.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/** @Author fuCong @Date 2023/2/8 17:18 */
@Data
@AllArgsConstructor
public class SettingDto {

  private int id;
  private String name;
  private String title;
  private String location;
  private String avatarUrl;

  /** 个人路由 */
  private String routing;

  private List<String> controls;

  public SettingDto(
      int id, String name, String title, String location, String avatarUrl, String routing) {
    this.id = id;
    this.name = name;
    this.title = title;
    this.location = location;
    this.avatarUrl = avatarUrl;
    this.routing = routing;
  }
}

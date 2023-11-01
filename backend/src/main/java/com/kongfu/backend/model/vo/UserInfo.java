package com.kongfu.backend.model.vo;

import com.kongfu.backend.model.entity.NoteCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-05-25 17:52:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

  private int id;
  private String username;
  /** 显示名 */
  private String displayName;
  /** 邮箱地址 */
  private String email;
  /** 用户类型，1-管理员;2-普通用户 */
  private Integer type;
  /** 头像地址 */
  private String headerUrl;
  /** 用户的笔记分类，映射成前端菜单 */
  private List<NoteCategory> categoryList;
}

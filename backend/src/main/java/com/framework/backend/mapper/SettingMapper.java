package com.framework.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.backend.model.entity.Setting;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fuCong
 * @version 1.0.0 @Description 博客前台设置
 * @createTime 2022-06-13 12:23:00
 */
@Mapper
public interface SettingMapper extends BaseMapper<Setting> {}

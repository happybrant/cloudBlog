package com.kongfu.backend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author fuCong
 * @version 1.0.0 @Description 首页折线图数据
 * @createTime 2022-05-31 12:17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticData {
    /**
     * 已发布博客数量
     */
    private Long publishedBlogCount;
    /**
     * 草稿数量
     */
    private Long draftBlogCount;
    /**
     * 待办任务数量
     */
    private Long todoTaskCount;
    /**
     * 执行中任务数量
     */
    private Long workingTaskCount;
    /**
     * 已完成任务数量
     */
    private Long doneTaskCount;
    /**
     * 笔记数量
     */
    private Long noteCount;
    /**
     * 相册数量
     */
    private Long albumCount;
    /**
     * 照片数量
     */
    private Long photoCount;

    private Map<String, Long> todoTaskYearMap;
    private Map<String, Long> workingTaskYearMap;
    private Map<String, Long> doneTaskYearMap;
    private Map<String, Long> noteMap;
    private Map<String, Long> articleMap;
    private Map<String, Long> photoMap;
    private Map<String, Long> articleCategoryMap;
    private Map<String, Long> articleTagMap;
    private Map<String, Long> todoTaskWeekMap;
    private Map<String, Long> workingTaskWeekMap;
    private Map<String, Long> doneTaskWeekMap;
}

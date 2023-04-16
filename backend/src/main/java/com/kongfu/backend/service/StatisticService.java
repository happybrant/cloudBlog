package com.kongfu.backend.service;

import com.kongfu.backend.model.entity.Article;
import com.kongfu.backend.model.entity.Task;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.model.vo.StatisticData;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 付聪
 */
@Service
public class StatisticService {

    @Resource
    public ArticleService articleService;
    @Resource
    public TaskService taskService;
    @Resource
    public NoteService noteService;
    @Resource
    public PhotoService photoService;
    @Resource
    public AlbumService albumService;
    @Resource
    public HostHolder holder;

    public StatisticData getStatisticData() {
        List<Article> articleList = articleService.getArticleList();
        long publishedBlogCount =
                articleList.stream().filter(r -> r.getStatus() == BlogConstant.PUBLISH_STATUS).count();
        long draftBlogCount =
                articleList.stream().filter(r -> r.getStatus() == BlogConstant.UN_PUBLISH_STATUS).count();
        List<Task> taskList = taskService.getTaskList();
        long todoTaskCount =
                taskList.stream().filter(r -> r.getStatus() == BlogConstant.TASK_TODO_STATUS).count();
        long workingTaskCount =
                taskList.stream().filter(r -> r.getStatus() == BlogConstant.TASK_WORKING_STATUS).count();
        long doneTaskCount =
                taskList.stream().filter(r -> r.getStatus() == BlogConstant.TASK_DONE_STATUS).count();
        long noteCount = noteService.getNoteCount();
        long albumCount = albumService.getAlbumCount();
        long photoCount = photoService.getPhotoCount();

        // 统计近一年每个月的创建任务数量和完成任务数量变化趋势
        Map<String, Long> todoTaskYearMap = taskService.getCreatedTaskByMonth();
        Map<String, Long> workingTaskYearMap = taskService.getStartedTaskByMonth();
        Map<String, Long> doneTaskYearMap = taskService.getFinishedTaskByMonth();

        // 统计近一年每个月的笔记创建数量变化趋势
        Map<String, Long> noteMap = noteService.getNoteByMonth();
        // 统计近一年每个月的博客（包括已发布和暂存为草稿的）创建数量变化趋势
        Map<String, Long> articleMap = articleService.getArticleByMonth();
        // 统计近一年每个月的照片上传数量变化趋势
        Map<String, Long> photoMap = photoService.getPhotoByMonth();
        // 统计每个分类下博客的数量
        Map<String, Long> articleCategoryMap = articleService.getArticleByCategory();
        // 统计每个标签下博客的数量占比
        Map<String, Long> articleTagMap = articleService.getArticleByTag();
        // 统计近一周每天的创建任务数、开始任务数和完成任务数
        Map<String, Long> todoTaskWeekMap = taskService.getCreatedTaskByDay();
        Map<String, Long> workingTaskWeekMap = taskService.getStartedTaskByDay();
        Map<String, Long> doneTaskWeekMap = taskService.getFinishedTaskByDay();

        StatisticData statisticData = new StatisticData();

        statisticData.setPublishedBlogCount(publishedBlogCount);
        statisticData.setDraftBlogCount(draftBlogCount);
        statisticData.setTodoTaskCount(todoTaskCount);
        statisticData.setWorkingTaskCount(workingTaskCount);
        statisticData.setDoneTaskCount(doneTaskCount);
        statisticData.setNoteCount(noteCount);
        statisticData.setAlbumCount(albumCount);
        statisticData.setPhotoCount(photoCount);

        statisticData.setTodoTaskYearMap(todoTaskYearMap);
        statisticData.setWorkingTaskYearMap(workingTaskYearMap);
        statisticData.setDoneTaskYearMap(doneTaskYearMap);
        statisticData.setNoteMap(noteMap);
        statisticData.setArticleMap(articleMap);
        statisticData.setPhotoMap(photoMap);

        statisticData.setArticleCategoryMap(articleCategoryMap);
        statisticData.setArticleTagMap(articleTagMap);

        statisticData.setTodoTaskWeekMap(todoTaskWeekMap);
        statisticData.setWorkingTaskWeekMap(workingTaskWeekMap);
        statisticData.setDoneTaskWeekMap(doneTaskWeekMap);

        return statisticData;
    }
}

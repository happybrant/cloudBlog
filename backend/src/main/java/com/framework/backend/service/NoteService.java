package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.common.MyPage;
import com.framework.backend.mapper.NoteMapper;
import com.framework.backend.model.dto.NoteQuery;
import com.framework.backend.model.entity.Note;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.util.BlogUtil;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:38:00
 */
@Service
public class NoteService {
  @Autowired private NoteMapper noteMapper;

  // @Autowired private RestHighLevelClient client;

  public int addNote(Note note) {
    return noteMapper.insert(note);
  }

  /**
   * 获取笔记列表
   *
   * @param query
   * @return
   */
  public List<Note> getNoteList(NoteQuery query) {
    QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Note::getStatus, BlogConstant.PUBLISH_STATUS);
    if (StringUtils.isNotBlank(query.getTitle())) {
      queryWrapper.lambda().like(Note::getTitle, query.getTitle());
    }
    if (StringUtils.isNotBlank(query.getCategoryId())) {
      queryWrapper.lambda().eq(Note::getCategoryId, query.getCategoryId());
    }
    queryWrapper.lambda().orderByDesc(MyBaseEntity::getCreateTime);
    return noteMapper.selectList(queryWrapper);
  }

  /**
   * 分页获取笔记列表
   *
   * @return
   */
  public MyPage<Note> getNoteListPager(NoteQuery query) {
    QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Note::getStatus, BlogConstant.PUBLISH_STATUS);
    queryWrapper.lambda().like(Note::getTitle, query.getTitle());
    if (StringUtils.isNotBlank(query.getCategoryId())) {
      queryWrapper.lambda().eq(Note::getCategoryId, query.getCategoryId());
    }
    queryWrapper.lambda().orderByDesc(Note::getTopFlag);
    queryWrapper.lambda().orderByDesc(MyBaseEntity::getUpdateTime);
    MyPage<Note> notePage = new MyPage<>(query.getPageIndex(), query.getPageSize());
    return noteMapper.selectPage(notePage, queryWrapper);
  }

  /**
   * 获取笔记数量
   *
   * @return
   */
  public long getNoteCount() {
    QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    return noteMapper.selectCount(queryWrapper);
  }

  /**
   * 根据笔记id获取笔记
   *
   * @param id
   * @return
   */
  public Note getNoteById(int id) {
    return noteMapper.selectById(id);
  }

  /**
   * 根据id更新任务
   *
   * @param note
   * @return
   */
  public int updateNote(Note note) {
    return noteMapper.updateById(note);
  }

  /**
   * 根据id删除任务，逻辑删除
   *
   * @param id
   * @return
   */
  public void deleteNote(String id) {
    Note note = noteMapper.selectById(id);
    if (note != null) {
      note.setStatus(BlogConstant.DELETE_STATUS);
      noteMapper.updateById(note);
    }
  }

  /**
   * 根据创建时间的月份对笔记进行分组
   *
   * @return
   */
  public Map<String, Long> getNoteByMonth() {
    QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.DELETE_STATUS);
    queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m')");
    queryWrapper.select(" DATE_FORMAT(create_time,'%Y-%m') AS Month,COUNT(*) AS Count");
    List<Map<String, Object>> noteList = noteMapper.selectMaps(queryWrapper);

    List<String> monthList = BlogUtil.getLatest12Month();
    Map<String, Long> noteMap = new LinkedHashMap<>(16);
    for (String month : monthList) {
      Map<String, Object> map =
          noteList.stream().filter(r -> r.get("Month").equals(month)).findAny().orElse(null);
      if (map == null) {
        noteMap.put(month, (long) 0);
      } else {
        noteMap.put(month, (Long) map.get("Count"));
      }
    }
    return noteMap;
  }

  //  public MyPage<Note> searchNoteListPager(NoteQuery query) throws IOException {
  //    MyPage<Note> notePage = new MyPage<>(query.getPageIndex(), query.getPageSize());
  //
  //    // 搜索请求对象
  //    SearchRequest searchRequest = new SearchRequest("note");
  //    // 搜索源构建对象
  //    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
  //    // 搜索条件不为空，按照条件进行搜索
  //    if (!StringUtils.isEmpty(query.getTitle())) {
  //      // 构建高亮查询
  //      HighlightBuilder highlightBuilder = new HighlightBuilder();
  //      // 多个高亮显示
  //      highlightBuilder.requireFieldMatch(true);
  //      highlightBuilder.field("title");
  //      highlightBuilder.preTags("<span style='color:red'>").postTags("</span>");
  //      searchSourceBuilder.highlighter(highlightBuilder);
  //      // 构建查询对象
  //      searchSourceBuilder.query(QueryBuilders.matchQuery("title", query.getTitle()));
  //    } else {
  //      // 如果搜索条件为空，则查询全部
  //      searchSourceBuilder.query(QueryBuilders.matchAllQuery());
  //    }
  //    // 设置分页信息
  //    searchSourceBuilder.from((query.getPageIndex() - 1) * query.getPageSize());
  //    searchSourceBuilder.size(query.getPageSize());
  //    searchRequest.source(searchSourceBuilder);
  //    // 执行搜索,向ES发起http请求
  //    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
  //
  //    // 搜索结果
  //    SearchHits hits = searchResponse.getHits();
  //    TotalHits totalHits = hits.getTotalHits();
  //    notePage.setTotal(totalHits.value);
  //    // 得到匹配度高的文档
  //    SearchHit[] searchHits = hits.getHits();
  //    List<Note> notes = new ArrayList<>();
  //    for (SearchHit searchHit : searchHits) {
  //      // 获取完整数据
  //      Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
  //      String highLightValue = (String) sourceAsMap.get("title");
  //      if (!StringUtils.isEmpty(query.getTitle())) {
  //        // 获取高亮数据
  //        Map<String, HighlightField> fields = searchHit.getHighlightFields();
  //        HighlightField highlightField = fields.get("title");
  //        highLightValue = highlightField.getFragments()[0].toString();
  //      }
  //      Note note = new Note();
  //      note.setTitle(highLightValue);
  //      note.setContent((String) sourceAsMap.get("content"));
  //      note.setOriginContent((String) sourceAsMap.get("originContent"));
  //      note.setCreateTime((Date) sourceAsMap.get("createTime"));
  //      notes.add(note);
  //    }
  //    notePage.setRecords(notes);
  //    return notePage;
  //  }
}

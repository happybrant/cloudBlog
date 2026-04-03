package com.framework.backend.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author fuCong @Date 2023/2/3 10:03
 */
@Service
@Slf4j
public class ElasticSearchService {

  @Autowired private RestHighLevelClient client;

  /** 判断索引是否存在 */
  public boolean existIndex(String index) {
    try {
      GetIndexRequest request = new GetIndexRequest(index);
      return client.indices().exists(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("es 持久层异常！index={}", index, e);
    }
    return false;
  }

  /** 创建索引 */
  public boolean createIndex(String index, String mappingSource) {
    if (existIndex(index)) {
      return false;
    }
      
    CreateIndexRequest request = new CreateIndexRequest(index);
    Settings settings = Settings.builder()
        .put("number_of_shards", 1)
        .put("number_of_replicas", 0)
        .build();
    request.settings(settings);
    
    try {
      request.mapping(mappingSource, XContentType.JSON);
    } catch (Exception e) {
      log.error("es 解析映射失败！index={}", index, e);
      return false;
    }
      
    try {
      var response = client.indices().create(request, RequestOptions.DEFAULT);
      boolean acknowledged = response.isAcknowledged();
      log.info("es Index: {} 创建{}", index, acknowledged ? "成功" : "失败");
      return acknowledged;
    } catch (IOException e) {
      log.error("es 持久层异常！index={}, columnMap={}", index, e);
    }
    return false;
  }

  /** 删除索引 */
  public boolean deleteIndex(String index) {
    try {
      if (existIndex(index)) {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        var response = client.indices().delete(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
      }
    } catch (Exception e) {
      log.error("es 持久层异常！index={}", index, e);
    }
    return false;
  }

  /**
   * 新增数据
   *
   * @param jsonString
   * @return
   */
  public boolean insert(String index, String jsonString, String id) {
    IndexRequest request = new IndexRequest(index);
    request.id(id);
    
    try {
      request.source(jsonString, XContentType.JSON);
    } catch (Exception e) {
      log.error("es 解析 JSON 失败！index={}, jsonString={}", index, jsonString, e);
      return false;
    }
  
    try {
      log.info("indexRequest={}", request);
      var response = client.index(request, RequestOptions.DEFAULT);
      log.info("indexResponse={}", response);
      return response.getResult().name().equals("created") || response.getResult().name().equals("updated");
    } catch (IOException e) {
      log.error("es 持久层异常！index={}, jsonString={}", index, jsonString, e);
    }
    return false;
  }

  /** 数据更新 */
  public boolean update(String index, String jsonString, String id) {
    UpdateRequest request = new UpdateRequest(index, id);
    
    try {
      request.doc(jsonString, XContentType.JSON);
    } catch (Exception e) {
      log.error("es 解析 JSON 失败！index={}, jsonString={}", index, jsonString, e);
      return false;
    }
      
    try {
      log.info("updateRequest={}", request);
      var response = client.update(request, RequestOptions.DEFAULT);
      log.info("updateResponse={}", response);
      return response.getResult().name().equals("updated");
    } catch (IOException e) {
      log.error("es 持久层异常！index={}, jsonString={}", index, jsonString, e);
      return false;
    }
  }

  /** 删除数据 */
  public boolean delete(String index, String id) {
    var request = new org.elasticsearch.action.delete.DeleteRequest(index, id);
    try {
      var response = client.delete(request, RequestOptions.DEFAULT);
      return response.getResult().name().equals("deleted");
    } catch (IOException e) {
      log.error("es 持久层异常！index={}, id={}", index, id, e);
      return false;
    }
  }
}

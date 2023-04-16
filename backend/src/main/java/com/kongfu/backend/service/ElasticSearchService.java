package com.kongfu.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/** @Author fuCong @Date 2023/2/3 10:03 */
@Service
@Slf4j
public class ElasticSearchService {

  @Resource private RestHighLevelClient client;

  /** 判断索引是否存在 */
  public boolean existIndex(String index) {
    try {
      return client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("es持久层异常！index={}", index, e);
    }
    return Boolean.FALSE;
  }

  /** 创建索引 */
  public boolean createIndex(String index, String mappingSource) {
    if (existIndex(index)) {
      return Boolean.FALSE;
    }
    // 指定映射
    CreateIndexRequest request = new CreateIndexRequest(index);
    request.settings(Settings.builder().put("number_of_shards", 1).put("number_of_replicas", 0));
    // 设置映射
    request.mapping(mappingSource, XContentType.JSON);
    try {
      // 创建索引操作客户端
      IndicesClient indices = client.indices();
      // 创建响应对象
      CreateIndexResponse createIndexResponse = indices.create(request, RequestOptions.DEFAULT);
      // 得到响应结果
      boolean acknowledged = createIndexResponse.isAcknowledged();
      log.info("es Index: " + index + "创建" + (acknowledged ? "成功" : "失败"));
      return Boolean.TRUE;
    } catch (IOException e) {
      log.error("es持久层异常！index={}, columnMap={}", index, e);
    }
    return Boolean.FALSE;
  }

  /** 删除索引 */
  public boolean deleteIndex(String index) {
    try {
      if (existIndex(index)) {
        AcknowledgedResponse response =
            client.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        return response.isAcknowledged();
      }
    } catch (Exception e) {
      log.error("es持久层异常！index={}", index, e);
    }
    return Boolean.FALSE;
  }

  /**
   * 新增数据
   *
   * @param jsonString
   * @return
   */
  public boolean insert(String index, String jsonString, String id) {
    IndexRequest indexRequest = new IndexRequest(index);
    // 创建唯一id
    indexRequest.id(id);
    indexRequest.source(jsonString, XContentType.JSON);

    try {
      log.info("indexRequest={}", indexRequest);
      IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
      log.info("indexResponse={}", indexResponse);
      return Boolean.TRUE;
    } catch (IOException e) {
      log.error("es持久层异常！index={}, jsonString={}", index, jsonString, e);
    }
    return Boolean.FALSE;
  }

  /** 数据更新 */
  public boolean update(String index, String jsonString, String id) {
    UpdateRequest updateRequest = new UpdateRequest();
    updateRequest.index(index).id(id);
    updateRequest.doc(jsonString, XContentType.JSON);
    try {
      log.info("updateRequest={}", updateRequest);
      UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
      log.info("updateResponse={}", updateResponse);
    } catch (IOException e) {
      log.error("es持久层异常！index={}, jsonString={}", index, jsonString, e);
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  /** 删除数据 */
  public boolean delete(String index, String id) {
    DeleteRequest deleteRequest = new DeleteRequest(index, id);
    try {
      client.delete(deleteRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("es持久层异常！index={}, id={}", index, id, e);
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }
}

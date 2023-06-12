package com.fiberhome.jy.dataexchange.common;

import java.util.Arrays;
import java.util.List;

/**
 * @className: StatisticIndexEnum
 * @description: TODO
 * @author: Garen-chen
 * @date: 2023/5/30
 */
public enum StatisticIndexEnum {
  INTERFACENUM("interfaceNum", "接口数量"),

  GROUPNUM("groupNum", "接口分组数量"),
  TABLENUM("tableNum", "结构化表数量"),
  SCHEMANUM("schemaNum", "模式数量"),
  PLANNUM("planNum", "计划任务数量"),
  ACCESSUSERNUM("accessUserNum", "接入用户数量"),
  UNSTRUCTUREDDATANUM("unStructuredDataNum", "非结构化数据量"),
  UNSTRUCTUREDDATASIZE("unStructuredDataSize", "非结构化数据大小"),
  STRUCTUREDDATANUM("structuredDataNum", "结构化数据量"),
  STRUCTUREDDATASIZE("structuredDataSize", "结构化数据大小"),
  TABLEINFOS("tableInfos", "结构化表情况");

  public String redisKey;

  public String redisMean;

  StatisticIndexEnum(String redisKey, String redisMean) {
    this.redisKey = redisKey;
    this.redisMean = redisMean;
  }

  public static List<String> IndexAll() {
    return Arrays.asList(
        INTERFACENUM.redisKey,
        GROUPNUM.redisKey,
        TABLENUM.redisKey,
        SCHEMANUM.redisKey,
        PLANNUM.redisKey,
        ACCESSUSERNUM.redisKey,
        UNSTRUCTUREDDATANUM.redisKey,
        UNSTRUCTUREDDATASIZE.redisKey,
        STRUCTUREDDATANUM.redisKey,
        STRUCTUREDDATASIZE.redisKey);
  }
}

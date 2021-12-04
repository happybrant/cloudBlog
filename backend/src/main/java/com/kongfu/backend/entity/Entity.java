package com.kongfu.backend.entity;

/**
 *基础实体类
 * @author 付聪
 */
public class Entity {
    private Integer id;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 最后修改时间
     */
    private String lastUpdateTime;
    /**
     * 状态
     */
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

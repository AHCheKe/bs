package com.assist.dao.model;

import javax.persistence.*;

public class Tag {
    /**
     * 主键
     */
    @Id
    @Column(name = "tag_id")
    private Integer tagId;

    /**
     * 标签名称
     */
    @Column(name = "tag_name")
    private String tagName;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 获取主键
     *
     * @return tag_id - 主键
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * 设置主键
     *
     * @param tagId 主键
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    /**
     * 获取标签名称
     *
     * @return tag_name - 标签名称
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * 设置标签名称
     *
     * @param tagName 标签名称
     */
    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    /**
     * 获取备注说明
     *
     * @return remark - 备注说明
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注说明
     *
     * @param remark 备注说明
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
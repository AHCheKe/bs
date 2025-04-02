package com.assist.dao.model;

import javax.persistence.*;

@Table(name = "user_tag")
public class UserTag {
    /**
     * 主键
     */
    @Id
    @Column(name = "user_tag_id")
    private Integer userTagId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 标签ID
     */
    @Column(name = "tag_id")
    private Integer tagId;

    @Transient
    private Tag tag;

    /**
     * 审核结果
     */
    private Integer validate;

    /**
     * 获取主键
     *
     * @return user_tag_id - 主键
     */
    public Integer getUserTagId() {
        return userTagId;
    }

    /**
     * 设置主键
     *
     * @param userTagId 主键
     */
    public void setUserTagId(Integer userTagId) {
        this.userTagId = userTagId;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取标签ID
     *
     * @return tag_id - 标签ID
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * 设置标签ID
     *
     * @param tagId 标签ID
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    /**
     * 获取审核结果
     *
     * @return validate - 审核结果
     */
    public Integer getValidate() {
        return validate;
    }

    /**
     * 设置审核结果
     *
     * @param validate 审核结果
     */
    public void setValidate(Integer validate) {
        this.validate = validate;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
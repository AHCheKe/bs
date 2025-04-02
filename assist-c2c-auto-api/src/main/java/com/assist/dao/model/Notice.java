package com.assist.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class Notice {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "notice_id")
    private Integer noticeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private Date publishTime;

    /**
     * 题图
     */
    @Column(name = "headPic")
    private String headpic;

    /**
     * 标签，以分号;隔开
     */
    private String tags;

    @Transient
    private List<String> tagList;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 获取物理主键
     *
     * @return notice_id - 物理主键
     */
    public Integer getNoticeId() {
        return noticeId;
    }

    /**
     * 设置物理主键
     *
     * @param noticeId 物理主键
     */
    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取发布时间
     *
     * @return publish_time - 发布时间
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 设置发布时间
     *
     * @param publishTime 发布时间
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 获取题图
     *
     * @return headPic - 题图
     */
    public String getHeadpic() {
        return headpic;
    }

    /**
     * 设置题图
     *
     * @param headpic 题图
     */
    public void setHeadpic(String headpic) {
        this.headpic = headpic == null ? null : headpic.trim();
    }

    /**
     * 获取标签，以分号;隔开
     *
     * @return tags - 标签，以分号;隔开
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置标签，以分号;隔开
     *
     * @param tags 标签，以分号;隔开
     */
    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    /**
     * 获取正文内容
     *
     * @return content - 正文内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置正文内容
     *
     * @param content 正文内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
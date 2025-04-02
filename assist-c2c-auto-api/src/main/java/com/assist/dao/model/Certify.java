package com.assist.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class Certify {
    /**
     * 主键
     */
    @Id
    @Column(name = "certify_id")
    private Integer certifyId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 申请时间
     */
    @Column(name = "app_time")
    private Date appTime;

    /**
     * 申请理由
     */
    @Column(name = "app_content")
    private String appContent;

    /**
     * 证明材料附件
     */
    private String attachment;

    @Transient
    private List<String> attachmentList;

    /**
     * 申请状态：0待审核；1已通过；2申请失败
     */
    @Column(name = "certify_status")
    private Integer certifyStatus;

    /**
     * 审核时间
     */
    @Column(name = "certify_time")
    private Date certifyTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取主键
     *
     * @return certify_id - 主键
     */
    public Integer getCertifyId() {
        return certifyId;
    }

    /**
     * 设置主键
     *
     * @param certifyId 主键
     */
    public void setCertifyId(Integer certifyId) {
        this.certifyId = certifyId;
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
     * 获取申请时间
     *
     * @return app_time - 申请时间
     */
    public Date getAppTime() {
        return appTime;
    }

    /**
     * 设置申请时间
     *
     * @param appTime 申请时间
     */
    public void setAppTime(Date appTime) {
        this.appTime = appTime;
    }

    /**
     * 获取申请理由
     *
     * @return app_content - 申请理由
     */
    public String getAppContent() {
        return appContent;
    }

    /**
     * 设置申请理由
     *
     * @param appContent 申请理由
     */
    public void setAppContent(String appContent) {
        this.appContent = appContent == null ? null : appContent.trim();
    }

    /**
     * 获取证明材料附件
     *
     * @return attachment - 证明材料附件
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * 设置证明材料附件
     *
     * @param attachment 证明材料附件
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    /**
     * 获取申请状态：0待审核；1已通过；2申请失败
     *
     * @return certify_status - 申请状态：0待审核；1已通过；2申请失败
     */
    public Integer getCertifyStatus() {
        return certifyStatus;
    }

    /**
     * 设置申请状态：0待审核；1已通过；2申请失败
     *
     * @param certifyStatus 申请状态：0待审核；1已通过；2申请失败
     */
    public void setCertifyStatus(Integer certifyStatus) {
        this.certifyStatus = certifyStatus;
    }

    /**
     * 获取审核时间
     *
     * @return certify_time - 审核时间
     */
    public Date getCertifyTime() {
        return certifyTime;
    }

    /**
     * 设置审核时间
     *
     * @param certifyTime 审核时间
     */
    public void setCertifyTime(Date certifyTime) {
        this.certifyTime = certifyTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public List<String> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<String> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
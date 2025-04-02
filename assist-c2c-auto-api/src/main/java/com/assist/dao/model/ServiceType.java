package com.assist.dao.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "service_type")
public class ServiceType {
    /**
     * 主键
     */
    @Id
    @Column(name = "service_id")
    private Integer serviceId;

    /**
     * 服务类型：全程陪诊、院内陪护、代办问诊、诊前约号、跑腿买药、送取结果
     */
    @Column(name = "service_name")
    private String serviceName;

    /**
     * 价格
     */
    private Double price;

    /**
     * 服务描述
     */
    @Column(name = "service_desc")
    private String serviceDesc;

    /**
     * 注意事项
     */
    @Column(name = "service_notice")
    private String serviceNotice;

    /**
     * 获取主键
     *
     * @return service_id - 主键
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     * 设置主键
     *
     * @param serviceId 主键
     */
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * 获取服务类型：全程陪诊、院内陪护、代办问诊、诊前约号、跑腿买药、送取结果
     *
     * @return service_name - 服务类型：全程陪诊、院内陪护、代办问诊、诊前约号、跑腿买药、送取结果
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * 设置服务类型：全程陪诊、院内陪护、代办问诊、诊前约号、跑腿买药、送取结果
     *
     * @param serviceName 服务类型：全程陪诊、院内陪护、代办问诊、诊前约号、跑腿买药、送取结果
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    /**
     * 获取价格
     *
     * @return price - 价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取服务描述
     *
     * @return service_desc - 服务描述
     */
    public String getServiceDesc() {
        return serviceDesc;
    }

    /**
     * 设置服务描述
     *
     * @param serviceDesc 服务描述
     */
    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc == null ? null : serviceDesc.trim();
    }

    /**
     * 获取注意事项
     *
     * @return service_notice - 注意事项
     */
    public String getServiceNotice() {
        return serviceNotice;
    }

    /**
     * 设置注意事项
     *
     * @param serviceNotice 注意事项
     */
    public void setServiceNotice(String serviceNotice) {
        this.serviceNotice = serviceNotice == null ? null : serviceNotice.trim();
    }
}
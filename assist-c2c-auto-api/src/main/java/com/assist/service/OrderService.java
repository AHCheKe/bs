package com.assist.service;

import com.alibaba.fastjson.JSONObject;
import com.assist.dao.model.ServiceOrder;
import com.assist.utils.page.PageView;

public interface OrderService {

    Integer ORDER_STATUS_WAIT_CONFIRM = 0;
    Integer ORDER_STATUS_CONFIRM = 1;
    Integer ORDER_STATUS_CANCEL = -1;
    Integer ORDER_STATUS_WAIT_PAY = 2;
    Integer ORDER_STATUS_FINISH = 3;
    Integer ORDER_STATUS_REJECT = 4;

    Integer SERVICE_STATUS_WAIT_CONFIRM = 0;
    Integer SERVICE_STATUS_CONFIRM = 1;
    Integer SERVICE_STATUS_FINISH = 2;
    Integer SERVICE_STATUS_REJECT = -1;

    Integer SERVICE_STATUS_CANCEL = -2;

    ServiceOrder saveOrder(ServiceOrder order);

    void payOrder(Integer orderId);

    void cancelOrder(Integer orderId);

    void finishOrder(Integer orderId);

    Integer checkOrders(Integer userId);

    Integer checkPatientOrders(Integer userId);

    /**
     * 查找病人的陪诊订单
     * @param reqMap
     * @param userId
     * @return
     */
    PageView<ServiceOrder>  findMyServiceOrderList(JSONObject reqMap, Integer userId);

    /**
     * 查询陪诊师的陪诊订单
     * @param reqMap
     * @param userId
     * @return
     */
    PageView<ServiceOrder>  findAssistantServiceOrderList(JSONObject reqMap, Integer userId);

    void deleteServiceInfo(Integer serviceId);

    /**
     * 陪诊师接受定向预约
     * @param orderId
     */
    void assistantConfirm(Integer orderId);

    /**
     * 陪诊师拒绝定向预约
     * @param orderId
     */
    void assistantReject(Integer orderId);
}

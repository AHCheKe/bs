package com.assist.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.assist.dao.mapper.ServiceNeedMapper;
import com.assist.dao.mapper.ServiceOrderMapper;
import com.assist.dao.mapper.UserMapper;
import com.assist.dao.model.ServiceNeed;
import com.assist.dao.model.ServiceOrder;
import com.assist.dao.model.User;
import com.assist.service.OrderService;
import com.assist.utils.page.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    @Autowired
    private ServiceNeedMapper serviceNeedMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceOrder saveOrder(ServiceOrder order) {
        return null;
    }

    @Override
    public void payOrder(Integer orderId) {

    }

    @Override
    public Integer checkOrders(Integer userId) {
        //1.待确认的定向预约订单
        ServiceOrder record = new ServiceOrder();
        record.setAssistantId(userId);
        record.setOrderStatus(OrderService.ORDER_STATUS_WAIT_CONFIRM);
        Integer count = serviceOrderMapper.selectCount(record);

        //2.待就诊的订单
        ServiceOrder record2 = new ServiceOrder();
        record2.setAssistantId(userId);
        record2.setOrderStatus(OrderService.ORDER_STATUS_CONFIRM);
        Integer count2 = serviceOrderMapper.selectCount(record2);

        return count+count2;
    }

    public Integer checkPatientOrders(Integer userId) {
        //待就诊的订单
        ServiceOrder record2 = new ServiceOrder();
        record2.setUserId(userId);
        record2.setOrderStatus(OrderService.ORDER_STATUS_CONFIRM);
        return serviceOrderMapper.selectCount(record2);
    }

    @Override
    public void cancelOrder(Integer orderId) {
        //1.修改订单状态
        ServiceOrder serviceOrder = serviceOrderMapper.selectByPrimaryKey(orderId);
        serviceOrder.setOrderStatus(OrderService.ORDER_STATUS_CANCEL);
        serviceOrderMapper.updateByPrimaryKeySelective(serviceOrder);

        //2.修改陪诊需求状态
        ServiceNeed serviceNeedInfo = serviceNeedMapper.selectByPrimaryKey(serviceOrder.getServiceId());
        serviceNeedInfo.setStatus(OrderService.SERVICE_STATUS_CANCEL);
        serviceNeedMapper.updateByPrimaryKeySelective(serviceNeedInfo);
    }

    @Override
    public void finishOrder(Integer orderId) {
        ServiceOrder order = serviceOrderMapper.selectByPrimaryKey(orderId);
        order.setOrderStatus(OrderService.ORDER_STATUS_FINISH);
        order.setFinishTime(new Date());
        ServiceNeed serviceNeed = serviceNeedMapper.selectByPrimaryKey(order.getServiceId());
        serviceNeed.setStatus(2);
        serviceOrderMapper.updateByPrimaryKeySelective(order);
        serviceNeedMapper.updateByPrimaryKeySelective(serviceNeed);
        //陪诊师服务次数加1
        User user = userMapper.selectByPrimaryKey(order.getAssistantId());
        user.setTotalService(user.getTotalService()+1);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public PageView<ServiceOrder> findMyServiceOrderList(@RequestBody JSONObject reqMap, Integer userId) {
        PageView<ServiceOrder> pageView = new PageView<>();
        pageView.startPage(reqMap);
        List<ServiceOrder> serviceOrderList = serviceOrderMapper.selectMyOrderList(userId, pageView.getFirstResult(), pageView.getMaxresult());
        pageView.setRecords(serviceOrderList);
        pageView.setTotalrecord(serviceOrderMapper.selectMyOrderCount(userId));
        return pageView;
    }

    @Override
    public PageView<ServiceOrder> findAssistantServiceOrderList(@RequestBody JSONObject reqMap, Integer userId) {
        PageView<ServiceOrder> pageView = new PageView<>();
        pageView.startPage(reqMap);
        List<ServiceOrder> serviceOrderList = serviceOrderMapper.selectAssistantOrderList(userId, pageView.getFirstResult(), pageView.getMaxresult());
        pageView.setRecords(serviceOrderList);
        pageView.setTotalrecord(serviceOrderMapper.selectAssistantOrderCount(userId));
        return pageView;
    }

    @Override
    public void deleteServiceInfo(Integer serviceId) {
        //删除订单记录
        ServiceOrder so = new ServiceOrder();
        so.setServiceId(serviceId);
        serviceOrderMapper.delete(so);

        //删除服务
        serviceNeedMapper.deleteByPrimaryKey(serviceId);
    }

    /**
     * 陪诊师接受定向预约
     * @param orderId
     */
    @Override
    public void assistantConfirm(Integer orderId) {
        ServiceOrder order = serviceOrderMapper.selectByPrimaryKey(orderId);
        order.setOrderStatus(OrderService.ORDER_STATUS_CONFIRM);
        order.setAcceptTime(new Date());
        ServiceNeed serviceNeed = serviceNeedMapper.selectByPrimaryKey(order.getServiceId());
        serviceNeed.setStatus(OrderService.SERVICE_STATUS_CONFIRM);
        serviceOrderMapper.updateByPrimaryKeySelective(order);
        serviceNeedMapper.updateByPrimaryKeySelective(serviceNeed);
    }

    /**
     * 陪诊师拒绝定向预约
     * @param orderId
     */
    @Override
    public void assistantReject(Integer orderId) {
        ServiceOrder order = serviceOrderMapper.selectByPrimaryKey(orderId);
        order.setOrderStatus(OrderService.ORDER_STATUS_REJECT);
        order.setAcceptTime(new Date());
        ServiceNeed serviceNeed = serviceNeedMapper.selectByPrimaryKey(order.getServiceId());
        serviceNeed.setStatus(OrderService.SERVICE_STATUS_REJECT);
        serviceOrderMapper.updateByPrimaryKeySelective(order);
        serviceNeedMapper.updateByPrimaryKeySelective(serviceNeed);
    }
}

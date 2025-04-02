package com.assist.dao.mapper;

import com.assist.dao.model.ServiceOrder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface ServiceOrderMapper extends Mapper<ServiceOrder> {

    List<ServiceOrder> selectOrderList(@Param("mobile") String mobile, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectOrderCount(@Param("mobile") String mobile);

    List<ServiceOrder> selectMyOrderList(@Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectMyOrderCount(@Param("userId") Integer userId);

    List<ServiceOrder> selectAssistantOrderList(@Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectAssistantOrderCount(@Param("userId") Integer userId);

    List<ServiceOrder> selectOrderByAssistantTime(@Param("userId") Integer userId, @Param("serviceDate") Date serviceDate);

    Integer selectOrderCountByAssistantTime(@Param("userId") Integer userId, @Param("serviceDate") Date serviceDate);

    Integer selectOrderCountByCode(@Param("orderCode") String orderCode);

    List<ServiceOrder> selectNotFinishOrderByCode(@Param("orderCode") String orderCode);
}
package com.assist.dao.mapper;

import com.assist.dao.model.ServiceNeed;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ServiceNeedMapper extends Mapper<ServiceNeed> {

    ServiceNeed selectServiceById(@Param("serviceId") Integer serviceId);
}
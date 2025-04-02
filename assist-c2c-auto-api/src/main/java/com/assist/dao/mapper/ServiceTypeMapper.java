package com.assist.dao.mapper;

import com.assist.dao.model.ServiceType;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ServiceTypeMapper extends Mapper<ServiceType> {

    ServiceType selectServiceTypeById(@Param("serviceId") Integer serviceId);
}
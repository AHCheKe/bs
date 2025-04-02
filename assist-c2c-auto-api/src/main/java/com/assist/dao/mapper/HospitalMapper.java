package com.assist.dao.mapper;

import com.assist.dao.model.Hospital;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface HospitalMapper extends Mapper<Hospital> {

    Hospital selectHospitalById(@Param("hospitalId") Integer hospitalId);
}
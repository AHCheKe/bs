package com.assist.service;

import com.assist.dao.model.ServiceNeed;
import com.assist.dao.model.ServiceOrder;

/**
 * 陪诊师匹配逻辑
 */
public interface AssistantMatchService {

    /**
     * 根据陪诊需求匹配陪诊师
     * @param serviceNeed 需求记录
     * @return 返回生成的订单
     */
    ServiceOrder matchPractitioner(ServiceNeed serviceNeed) throws Exception;
}

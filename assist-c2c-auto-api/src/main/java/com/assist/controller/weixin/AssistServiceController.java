package com.assist.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.assist.author.AuthRequired;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.*;
import com.assist.dao.model.*;
import com.assist.service.AssistantMatchService;
import com.assist.service.OrderService;
import com.assist.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 陪诊服务需求接口
 */
@RestController
@RequestMapping("api/service")
public class AssistServiceController extends ApiBaseController {

	private final Logger logger = LoggerFactory.getLogger(AssistServiceController.class);

	@Autowired
	private ServiceNeedMapper serviceNeedMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ServiceOrderMapper serviceOrderMapper;
	@Autowired
	private ServiceTypeMapper serviceTypeMapper;
	@Autowired
	private TagMapper tagMapper;
	@Autowired
	private AssistantMatchService matchService;
	@Autowired
	private HospitalMapper hospitalMapper;

	/**
	 * 陪诊需求详情
	 * @param pid
	 * @return
	 */
	@RequestMapping("detail")
	public JsonResult detail(@RequestParam Integer pid) {
		JsonResult result = new JsonResult(true, 200, "查询成功");
		ServiceNeed serviceNeed = serviceNeedMapper.selectByPrimaryKey(pid);
		// 切割图片、标签
		if(StringUtils.isNotBlank(serviceNeed.getPics())) {
			String[] url = serviceNeed.getPics().split(",");
			List<String> urls = new ArrayList<>();
			for (int i=0; i<url.length; i++) {
				urls.add(url[i]);
			}
			serviceNeed.setPicList(urls);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("service", serviceNeed);
		result.setData(jsonObject);
		return result;
	}

	/**
	 * 就诊者：我的陪诊需求
	 * @param reqMap
	 * @param request
	 * @return
	 */
	@RequestMapping("my/list")
	@AuthRequired
	public JsonResult myServiceList(@RequestBody JSONObject reqMap, HttpServletRequest request) {
		PageView<ServiceNeed> pageView = new PageView<>();
		User user = getCurrUser(request);
		pageView = pageView.startPage(reqMap);
		Example example = new Example(ServiceNeed.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", user.getUserId());
		example.orderBy("publishTime").desc();
		List<ServiceNeed> list = serviceNeedMapper.selectByExampleAndRowBounds(example, new RowBounds(pageView.getFirstResult(), pageView.getMaxresult()));
		pageView.setRecords(list);
		pageView.setTotalrecord(serviceNeedMapper.selectCountByExample(example));
		return JsonResult.success(pageView);
	}

	/**
	 * 提交陪诊需求，系统自动匹配合适的陪诊师并生成陪诊订单
	 * @param req
	 * @param request
	 * @return
	 */
	@RequestMapping("save")
	@AuthRequired
	public JsonResult saveService(@RequestBody JSONObject req, HttpServletRequest request){
		User user = getCurrUser(request);
		ServiceNeed serviceNeed = JSONObject.parseObject(req.toJSONString(), ServiceNeed.class);
		serviceNeed.setUserId(user.getUserId());
		serviceNeed.setPublishTime(new Date());
//		ServiceType typeRecord = new ServiceType();
//		typeRecord.setServiceName(serviceNeed.getServiceType());
//		ServiceType st = serviceTypeMapper.select(typeRecord).get(0);
//		serviceNeed.setPrice(st.getPrice());
		//生成服务需求单时，不设置价格，由陪诊师上传
		JsonResult jsonResult = new JsonResult();
		try {
			//保存陪诊需求记录
			serviceNeedMapper.insertSelective(serviceNeed);
			if(serviceNeed.getSelectModel()==0){
				//根据系统算法匹配陪诊师，并生成陪诊订单
				matchService.matchPractitioner(serviceNeed);
				jsonResult.setMsg("陪诊需求已提交，系统将为你分配陪诊师，请在订单详情页面查看");
			}else if(serviceNeed.getSelectModel()==1){
				//定向预约
				ServiceOrder order = new ServiceOrder();
				order.setServiceId(serviceNeed.getServiceId());
				order.setOrderStatus(OrderService.ORDER_STATUS_WAIT_CONFIRM);
				order.setOrderType(2);//定向预约
				order.setAssistantId(serviceNeed.getAppointAssist());//定向预约的陪诊师
				order.setPrice(serviceNeed.getPrice());
				order.setUserId(user.getUserId());
				order.setCreateTime(new Date());
				jsonResult.setMsg("陪诊订单已提交，请等待陪诊师确认接单");
				serviceOrderMapper.insertSelective(order);
			}
			jsonResult.setData(serviceNeed);
		} catch (Exception e) {
			logger.error("生成陪诊订单发生异常", e);
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}

	/**
	 * 服务类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping("type/list")
	public JsonResult serviceTypeList(HttpServletRequest request) {
		Example example = new Example(ServiceType.class);
		List<ServiceType> list = serviceTypeMapper.selectByExample(example);
		JSONObject data = new JSONObject();
		data.put("serviceList", list);
		List<Tag> tagList = tagMapper.selectAll();
		//查询医院列表
		List<Hospital> hospitalList = hospitalMapper.selectAll();
		data.put("tagList", tagList);
		data.put("hospitalList", hospitalList);
		return JsonResult.success(data);
	}
}

package com.assist.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.CertifyMapper;
import com.assist.dao.mapper.ServiceNeedMapper;
import com.assist.dao.mapper.UserMapper;
import com.assist.dao.model.Certify;
import com.assist.dao.model.User;
import com.assist.dao.model.UserTag;
import com.assist.service.TagService;
import com.assist.utils.page.PageView;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 陪诊师接口
 */
@RestController
@RequestMapping("api/assistant")
public class AssistantController extends ApiBaseController {
	
	@Autowired
	private ServiceNeedMapper serviceNeedMapper;
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TagService tagService;
	@Autowired
	private CertifyMapper certifyMapper;
	
	/**
	 * 查询陪诊师列表
	 * @param reqMap
	 * @param request
	 * @return
	 */
	@RequestMapping("list")
	public JsonResult serviceList(@RequestBody JSONObject reqMap, HttpServletRequest request) {
		PageView<User> pageView = new PageView<>();
		pageView = pageView.startPageWxmp(reqMap);
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		// 如果要筛选城市
		String currCity = null;
		if(reqMap.get("currCity")!=null){
			//小程序端可以通过serviceType参数过滤需求与服务
			currCity = reqMap.get("currCity").toString();
		}
		if(reqMap.get("kw")!=null){
			// 按关键字查询
			String kw = (String) reqMap.get("kw");
			criteria.andLike("title", "%"+kw+"%");
		}
		if(StringUtils.isNotBlank(currCity)){
			criteria.andEqualTo("location", currCity);
		}
		criteria.andEqualTo("role", 2);
		List<User> list = userMapper.selectByExampleAndRowBounds(example, new RowBounds(pageView.getFirstResult(), pageView.getMaxresult()));
		// 切割图片、标签
		for (User item : list) {
			if(StringUtils.isNotBlank(item.getTags())) {
				String[] tagArray = item.getTags().split(";");
				List<String> tags = new ArrayList<>();
				for (int i=0; i<tagArray.length; i++) {
					tags.add(tagArray[i]);
				}
				item.setTagList(tags);
			}
		}
		pageView.setRecords(list);
		pageView.setTotalrecord(userMapper.selectCountByExample(example));//总记录数
		return JsonResult.success(pageView);
	}

	/**
	 * 查看陪诊师详情
	 * @param uid
	 * @return
	 */
	@RequestMapping("detail")
	public JsonResult detail(@RequestParam Integer uid) {
		JsonResult result = new JsonResult(true, 200, "查询成功");
		User user = userMapper.selectByPrimaryKey(uid);
		List<UserTag> userTagList = tagService.findUserTags(user);
		List<String> tags = new ArrayList<>();
		for (UserTag tag:userTagList) {
			tags.add(tag.getTag().getTagName());
		}
		//证明材料
		Certify certify = new Certify();
		certify.setUserId(uid);
		if(certifyMapper.selectCount(certify)>0){
			Certify info = certifyMapper.select(certify).get(0);
			if(StringUtils.isNotBlank(info.getAttachment())){
				user.setPicList(Arrays.asList(info.getAttachment().split(",")));
			}
		}
		user.setTagList(tags);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("assistant", user);
		result.setData(jsonObject);
		return result;
	}

}

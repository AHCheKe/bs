package com.assist.controller.weixin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.assist.author.AuthRequired;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.*;
import com.assist.dao.model.Notice;
import com.assist.dao.model.Tag;
import com.assist.dao.model.User;
import com.assist.dao.model.UserTag;
import com.assist.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 小程序端标签相关接口
 */
@RestController
@RequestMapping("api/tag")
public class TagController extends ApiBaseController {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TagMapper tagMapper;
	@Autowired
	private ServiceTypeMapper serviceTypeMapper;
	@Autowired
	private TagService tagService;

	/**
	 * 标签数据，包含所有标签与当前登录陪诊师的标签
	 * @param request
	 * @return
	 */
	@RequestMapping("data")
	@AuthRequired
	public JsonResult serviceList(HttpServletRequest request) {
		User user = getCurrUser(request);
		List<UserTag> userTagList = tagService.findUserTags(user);
		List<Tag> allTags = tagMapper.selectAll();
		JSONObject data = new JSONObject();
		data.put("userTags", userTagList);
		data.put("serviceTagList", allTags);
		return JsonResult.success(data);
	}

	/**
	 * 更新标签
	 * @param reqJson
	 * @param request
	 * @return
	 */
	@RequestMapping("updateTags")
	@AuthRequired
	public JsonResult updateUser(@RequestBody JSONObject reqJson, HttpServletRequest request) {
		User user = getCurrUser(request);
		JSONArray ids = reqJson.getJSONArray("newTagIds");
		List<Integer> tagIds = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			tagIds.add(ids.getInteger(i));
		}
		tagService.updateUserTags(user, tagIds);
        return JsonResult.success();
	}

//	@RequestMapping("notice/detail/{id}")
//	public JsonResult noticeInfo(@PathVariable("id") Integer configId) {
//		JsonResult result = new JsonResult(true, 200, "查询成功");
//		Notice notice = noticeMapper.selectByPrimaryKey(configId);
//		result.setData(notice);
//		return result;
//	}
}

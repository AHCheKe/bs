package com.assist.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.assist.author.AuthRequired;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.*;
import com.assist.dao.model.*;
import com.assist.utils.page.PageView;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 小程序端医院信息接口
 */
@RestController
@RequestMapping("api/index")
public class HomeController extends ApiBaseController {
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ServiceOrderMapper serviceOrderMapper;
	@Autowired
	private SlidePicMapper slidePicMapper;
	@Autowired
	private ServiceTypeMapper serviceTypeMapper;
	@Autowired
	private NoticeMapper noticeMapper;
	@Autowired
	private TagMapper tagMapper;

	/**
	 * 获取轮播图
	 * @return
	 */
	@RequestMapping("slide/picture")
	public JsonResult slidePics() {
		Example example = new Example(SlidePic.class);
		example.orderBy("seq").asc();
		List<SlidePic> list = slidePicMapper.selectByExample(example);
		return JsonResult.success(list);
	}

	/**
	 * 服务类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping("service/list")
	public JsonResult serviceList(HttpServletRequest request) {
		Example example = new Example(ServiceType.class);
//		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("userId", user.getUserId());
//		example.orderBy("publishTime").desc();
		List<ServiceType> list = serviceTypeMapper.selectByExample(example);
		Example example1 = new Example(Notice.class);
		example1.orderBy("publishTime").desc();
		List<Notice> noticeList = noticeMapper.selectByExample(example1);
		for (Notice notice : noticeList) {
			if(StringUtils.isNotBlank(notice.getTags())){
				notice.setTagList(Arrays.asList(notice.getTags().split(";")));
			}
		}
		JSONObject data = new JSONObject();
		data.put("serviceList", list);
		data.put("noticeList", noticeList);
		return JsonResult.success(data);
	}

	@RequestMapping("notice/detail/{id}")
	public JsonResult noticeInfo(@PathVariable("id") Integer configId) {
		JsonResult result = new JsonResult(true, 200, "查询成功");
		Notice notice = noticeMapper.selectByPrimaryKey(configId);
		result.setData(notice);
		return result;
	}

	@RequestMapping("tag/list")
	public JsonResult getAllTags() {
		JsonResult result = new JsonResult(true, 200, "查询成功");
		List<Tag> tagList = tagMapper.selectAll();
		result.setData(tagList);
		return result;
	}
}

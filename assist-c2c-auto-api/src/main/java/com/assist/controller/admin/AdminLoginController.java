package com.assist.controller.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.assist.dao.mapper.AdminMapper;
import com.assist.dao.mapper.UserMapper;
import com.assist.dao.model.Admin;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.assist.author.JwtConfig;
import com.assist.controller.vo.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/admin")
public class AdminLoginController {
	
	@Autowired
	private UserMapper userMapper;
	@Resource
	private JwtConfig jwtConfig ;
	@Autowired
	private AdminMapper adminMapper;

	@RequestMapping("logout")
	public String logOut(HttpSession session) {
		session.removeAttribute("currAdmin");
		return "admin/login";
	}
	
	/**
	 * 管理员登录表单
	 * @return
	 */
	@RequestMapping("login")
	public JsonResult loginForm(@RequestBody Admin admin) {
		JsonResult jsonResult = new JsonResult();
		List<Admin> list = adminMapper.select(admin);
		if(list!=null&&list.size()>0) {
			Admin admin3 = list.get(0);
			admin3.setLastLogin(new Date());
			adminMapper.updateByPrimaryKeySelective(admin3);
			//使用ID来签发token
			String token = jwtConfig.createToken("ADMIN:"+admin3.getAdminId());
			JSONObject json = new JSONObject();
			json.put("admin", admin3);
			json.put("status", "OK");
			if (!StringUtils.isEmpty(token)) {
				json.put("token",token);
			}
			jsonResult.setData(json);
			return  jsonResult;
		}else {
			JsonResult ret = new JsonResult();
			ret.setCode(500);
			ret.setMsg("手机号或密码错误");
			ret.setSuccess(false);
			return ret;
		}
	}

	/**
	 * 修改个人信息
	 * @param session
	 * @param admin
	 * @return
	 */
	@RequestMapping("updateProfile")
	public JsonResult updateProfile(HttpSession session, Admin admin) {
		JsonResult result = new JsonResult();
		try {
			adminMapper.updateByPrimaryKeySelective(admin);
			admin = adminMapper.selectByPrimaryKey(admin.getAdminId());
			result.setData(admin);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.error("-1", "更新失败");
		}
		return result;
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}

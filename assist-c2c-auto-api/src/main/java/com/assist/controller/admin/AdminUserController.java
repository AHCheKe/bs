package com.assist.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.AdminMapper;
import com.assist.dao.mapper.CertifyMapper;
import com.assist.dao.mapper.ConfigMapper;
import com.assist.dao.mapper.UserMapper;
import com.assist.dao.model.*;
import com.assist.service.TagService;
import com.assist.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 后台接口：用户管理
 */
@RequestMapping("/admin/user")
@RestController
public class AdminUserController {
    
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private CertifyMapper certifyMapper;
    @Autowired
    private TagService tagService;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询小程序用户列表
     * @param request
     * @return
     */
    @RequestMapping("list")
    public JsonResult userList(@RequestBody JSONObject request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        PageView<User> page = new PageView<>();
        page = page.startPage(request);
        String kw = request.getString("kw");
        Integer role = request.getInteger("role");
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("mobile", "%"+kw+"%");
        }
        criteria.andEqualTo("role", role);
//        example.orderBy("userId").desc();
        List<User> list = userMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        //如果是陪诊师审核列表，则关联申请信息与标签
        if(role > 1){
            for(User user : list){
                Certify record = new Certify();
                record.setUserId(user.getUserId());
                if(certifyMapper.selectCount(record)>0){
                    Certify certify = certifyMapper.select(record).get(0);
                    if(StringUtils.isNotBlank(certify.getAttachment())){
                        certify.setAttachmentList(Arrays.asList(certify.getAttachment().split(",")));
                    }
                    user.setCertify(certify);
                }
                //标签
                List<UserTag> userTagList = tagService.findUserTags(user);
                user.setUserTagList(userTagList);
            }
        }
        page.setRecords(list);
        page.setTotalrecord(userMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 标签申请审核列表
     * @param request
     * @return
     */
    @RequestMapping("tag/app")
    public JsonResult tagAppUserList(@RequestBody JSONObject request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        PageView<User> page = new PageView<>();
        page = page.startPage(request);
        String kw = request.getString("kw");
        List<User> list = userMapper.selectTagModifyUsers(page.getFirstResult(), page.getMaxresult());
        //关联申请信息与标签
        for(User user : list){
            Certify record = new Certify();
            record.setUserId(user.getUserId());
            if(certifyMapper.selectCount(record)>0){
                Certify certify = certifyMapper.select(record).get(0);
                if(StringUtils.isNotBlank(certify.getAttachment())){
                    certify.setAttachmentList(Arrays.asList(certify.getAttachment().split(",")));
                }
                user.setCertify(certify);
            }
            //标签
            List<UserTag> userTagList = tagService.findUserTags(user);
            user.setUserTagList(userTagList);
        }
        page.setRecords(list);
        page.setTotalrecord(userMapper.selectTagModifyUsersCount());
        result.setData(page);
        return result;
    }

    /**
     * 标签审核通过
     * @param assistId
     * @return
     */
    @RequestMapping("tag/validate/{id}")
    public JsonResult assistTagValidate(@PathVariable("id") Integer assistId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        User user = userMapper.selectByPrimaryKey(assistId);
        tagService.validateUserTag(user);//通过标签审核
        result.setData(user);
        return result;
    }

    /**
     * 标签审核拒绝
     * @param assistId
     * @return
     */
    @RequestMapping("tag/reject/{id}")
    public JsonResult assistTagReject(@PathVariable("id") Integer assistId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        User user = userMapper.selectByPrimaryKey(assistId);
        tagService.rejectUserTag(user);//拒绝标签审核
        result.setData(user);
        return result;
    }

    @RequestMapping("detail")
    public JsonResult detail(Integer userId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        User user = userMapper.selectByPrimaryKey(userId);
        result.setData(user);
        return result;
    }

    @RequestMapping("save")
    public JsonResult saveOrUpdateApartment(User user) {
        JsonResult result = new JsonResult(true, 200, "更新成功");
        try {
            if(user.getUserId()==null){
                userMapper.insertSelective(user);
            }else{
                userMapper.updateByPrimaryKeySelective(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 审核通过
     * @param assistId
     * @return
     */
    @RequestMapping("assist/validate/{id}")
    public JsonResult assistValidate(@PathVariable("id") Integer assistId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        User user = userMapper.selectByPrimaryKey(assistId);
        user.setRole(2);
        user.setStatus(1);
        user.setRate(4.5);//初始评分为4.5
        user.setTotalService(0);//总服务次数为0
        userMapper.updateByPrimaryKeySelective(user);
        tagService.validateUserTag(user);//通过标签审核
        Certify record = new Certify();
        record.setUserId(user.getUserId());
        Certify certify = certifyMapper.select(record).get(0);
        certify.setCertifyStatus(1);//审核通过状态
        certify.setAppTime(new Date());
        certifyMapper.updateByPrimaryKeySelective(certify);//更新数据库
        result.setData(user);
        return result;
    }

    /**
     * 审核拒绝
     * @param assistId
     * @return
     */
    @RequestMapping("assist/reject/{id}")
    public JsonResult assistReject(@PathVariable("id") Integer assistId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        User user = userMapper.selectByPrimaryKey(assistId);
        user.setStatus(2);
        userMapper.updateByPrimaryKeySelective(user);
        tagService.validateUserTag(user);//通过标签审核
        Certify record = new Certify();
        record.setUserId(user.getUserId());
        Certify certify = certifyMapper.select(record).get(0);
        certify.setCertifyStatus(2);//审核拒绝状态
        certify.setAppTime(new Date());
        certifyMapper.updateByPrimaryKeySelective(certify);//更新数据库
        result.setData(user);
        return result;
    }

    /**
     * @param userId
     * @param status
     * @return
     */
    @RequestMapping("update2")
    public JsonResult delete(Integer userId, Integer status) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            user.setStatus(status);
            userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查询管理员列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("manager/list")
    public JsonResult apartmentManagerList(PageView<Admin> page, String kw) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("mobile", "%"+kw+"%");
        }
        criteria.andEqualTo("role", 2);
        example.orderBy("adminId").desc();
        List<Admin> list = adminMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(list);
        page.setTotalrecord(adminMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 管理员详情
     * @param adminId
     * @return
     */
    @RequestMapping("manager/detail/{id}")
    public JsonResult apartmentManagerDetail(@PathVariable("id") Integer adminId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Admin user = adminMapper.selectByPrimaryKey(adminId);
        result.setData(user);
        return result;
    }

    /**
     * 管理员信息更新
     * @param user
     * @param user
     * @return
     */
    @RequestMapping("manager/save")
    public JsonResult saveApartmentManager(@RequestBody Admin user) {
        JsonResult result = new JsonResult(true, 200, "个人信息已更新");
        try {
            adminMapper.updateByPrimaryKey(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("注册失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 更新管理员状态
     * @param managerId
     * @param status
     * @return
     */
    @RequestMapping("manager/update")
    public JsonResult updateApartmentManagerStatus(Integer managerId, Integer status) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            Admin user = adminMapper.selectByPrimaryKey(managerId);
            user.setStatus(status);
            adminMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 将该用户设置为小程序端的授权登录用户
     * @param userId
     * @return
     */
    @RequestMapping("author/login/{userId}")
    public JsonResult authorLogin(@PathVariable("userId") Integer userId) {
        JsonResult result = new JsonResult(true, 200, "更新成功");
        try {
            //先从系统参数表读取openid
            Config t = new Config();
            t.setConfKey("wx_user_openid");
            if(configMapper.selectCount(t)==0){
                return  JsonResult.error("500", "未设置openid");
            }
            //将之前的用户对应的openid去掉
            User user1 = new User();
            List<User> oldUserList = userMapper.select(user1);
            for(User u:oldUserList){
                u.setOpenId("temp");
                userMapper.updateByPrimaryKeySelective(u);
            }
            Config config = configMapper.select(t).get(0);
            User user = userMapper.selectByPrimaryKey(userId);
            user.setOpenId(config.getValue());
            userMapper.updateByPrimaryKeySelective(user);//更新这个用户的openid
        } catch (Exception e) {
            result.setMsg("失败");
            result.setSuccess(false);
        }
        return result;
    }
}

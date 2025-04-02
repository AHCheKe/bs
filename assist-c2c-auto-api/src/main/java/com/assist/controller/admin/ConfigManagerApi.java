package com.assist.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.ConfigMapper;
import com.assist.dao.mapper.NoticeMapper;
import com.assist.dao.model.Config;
import com.assist.dao.model.Notice;
import com.assist.dao.model.User;
import com.assist.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 系统参数的api
 */
@RestController
@RequestMapping("/admin/config")
public class ConfigManagerApi {

    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private NoticeMapper noticeMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询参数列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    public JsonResult userList(PageView<Config> page, String kw) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(Config.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("remark", kw);
        }
        example.orderBy("lastUpdate").desc();
        List<Config> list = configMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(list);
        page.setTotalrecord(configMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 删除参数
     * @param configId
     * @return
     */
    @RequestMapping("delete")
    public JsonResult delete(Integer configId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        configMapper.deleteByPrimaryKey(configId);
        return result;
    }

    @RequestMapping("info/{id}")
    public JsonResult info(@PathVariable("id") Integer configId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Config config = configMapper.selectByPrimaryKey(configId);
        result.setData(config);
        return result;
    }

    @RequestMapping("save")
    public JsonResult saveOrUpdateApartment(@RequestBody Config config) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        try {
            if(config.getConfigId()==null){
                configMapper.insertSelective(config);
            }else{
                configMapper.updateByPrimaryKeySelective(config);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /* ******通知公告管理********/
    /**
     * 查询列表
     * @param request
     * @return
     */
    @RequestMapping("notice/list")
    public JsonResult noticeList(@RequestBody JSONObject request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        PageView<Notice> page = new PageView<>();
        page = page.startPage(request);
        Example example = new Example(Notice.class);
        Example.Criteria criteria = example.createCriteria();
        String kw = request.getString("kw");
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("remark", kw);
        }
        example.orderBy("publishTime").desc();
        List<Notice> list = noticeMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for (Notice notice : list) {
            if(StringUtils.isNotBlank(notice.getTags())){
                notice.setTagList(Arrays.asList(notice.getTags().split(";")));
            }
        }
        page.setRecords(list);
        page.setTotalrecord(noticeMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 删除内容
     * @param configId
     * @return
     */
    @RequestMapping("notice/delete")
    public JsonResult deleteNotice(Integer configId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        noticeMapper.deleteByPrimaryKey(configId);
        return result;
    }

    @RequestMapping("notice/info/{id}")
    public JsonResult noticeInfo(@PathVariable("id") Integer configId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Notice notice = noticeMapper.selectByPrimaryKey(configId);
        result.setData(notice);
        return result;
    }

    @RequestMapping("notice/save")
    public JsonResult saveOrUpdateNotice(@RequestBody Notice notice) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        try {
            if(notice.getNoticeId()==null){
                notice.setPublishTime(new Date());
                noticeMapper.insertSelective(notice);
            }else{
                noticeMapper.updateByPrimaryKeySelective(notice);
            }
        } catch (Exception e) {
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
}

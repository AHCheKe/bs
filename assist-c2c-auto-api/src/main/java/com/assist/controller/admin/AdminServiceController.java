package com.assist.controller.admin;

import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.*;
import com.assist.dao.model.ServiceNeed;
import com.assist.dao.model.ServiceType;
import com.assist.dao.model.Tag;
import com.assist.dao.model.UserTag;
import com.assist.service.OrderService;
import com.assist.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 陪诊服务需求管理
 */
@RequestMapping("/admin/service")
@RestController
public class AdminServiceController {

    @Autowired
    private ServiceNeedMapper serviceNeedMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ServiceTypeMapper serviceTypeMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private UserTagMapper userTagMapper;

    /**
     * 查询列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    public JsonResult toyList(PageView<ServiceNeed> page, String kw) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(ServiceNeed.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
//            criteria.orLike("content", "%"+kw+"%");
            criteria.orLike("hospital", "%"+kw+"%");
        }
        example.orderBy("serviceId").desc();
        List<ServiceNeed> list = serviceNeedMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for(ServiceNeed serviceNeed :list){
            serviceNeed.setUser(userMapper.selectByPrimaryKey(serviceNeed.getUserId()));
        }
        page.setRecords(list);
        page.setTotalrecord(serviceNeedMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 下架违规信息
     * @param serviceId
     * @return
     */
    @RequestMapping("delete/{serviceId}")
    public JsonResult updateStatus(@PathVariable("serviceId") Integer serviceId) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            orderService.deleteServiceInfo(serviceId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }


    /* *************医院的陪诊服务管理***********/
    /**
     * 查询列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("type/list")
    public JsonResult serviceDataList(PageView<ServiceType> page, String kw) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(ServiceType.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("hospitalName", kw);
        }
//        example.orderBy("lastUpdate").desc();
        List<ServiceType> serviceTypeList = serviceTypeMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(serviceTypeList);
        page.setTotalrecord(serviceTypeMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 删除服务
     * @param id
     * @return
     */
    @RequestMapping("/type/delete/{id}")
    public JsonResult deleteService(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "删除成功");
        serviceTypeMapper.deleteByPrimaryKey(id);
        return result;
    }

    @RequestMapping("type/info/{id}")
    public JsonResult serviceInfo(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        ServiceType serviceType = serviceTypeMapper.selectByPrimaryKey(id);
        result.setData(serviceType);
        return result;
    }

    @RequestMapping("type/save")
    public JsonResult saveOrUpdateService(@RequestBody ServiceType serviceType) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            if(serviceType.getServiceId()==null){
                serviceTypeMapper.insertSelective(serviceType);
            }else{
                serviceTypeMapper.updateByPrimaryKeySelective(serviceType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /* *************陪诊师标签管理***********/
    /**
     * 查询列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("tag/list")
    public JsonResult tagDataList(PageView<Tag> page, String kw) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(Tag.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("hospitalName", kw);
        }
//        example.orderBy("lastUpdate").desc();
        List<Tag> serviceTypeList = tagMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(serviceTypeList);
        page.setTotalrecord(serviceTypeMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 删除标签，同时要删除陪诊师对应的标签
     * @param id
     * @return
     */
    @RequestMapping("/tag/delete/{id}")
    public JsonResult deleteTag(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "删除成功");
        tagMapper.deleteByPrimaryKey(id);
        UserTag userTag = new UserTag();
        userTag.setTagId(id);
        userTagMapper.delete(userTag);//解除标注了该标签的陪诊师关联
        return result;
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    @RequestMapping("tag/info/{id}")
    public JsonResult tagInfo(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Tag tag = tagMapper.selectByPrimaryKey(id);
        result.setData(tag);
        return result;
    }

    /**
     * 保存标签
     * @param tag
     * @return
     */
    @RequestMapping("tag/save")
    public JsonResult saveOrUpdateTag(@RequestBody Tag tag) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            if(tag.getTagId()==null){
                tagMapper.insertSelective(tag);
            }else{
                tagMapper.updateByPrimaryKeySelective(tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
}

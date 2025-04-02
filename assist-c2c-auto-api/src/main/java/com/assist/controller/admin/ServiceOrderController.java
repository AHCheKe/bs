package com.assist.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.assist.author.AuthRequired;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.ServiceNeedMapper;
import com.assist.dao.mapper.ServiceOrderMapper;
import com.assist.dao.mapper.UserMapper;
import com.assist.dao.model.Admin;
import com.assist.dao.model.ServiceOrder;
import com.assist.service.OrderService;
import com.assist.utils.page.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/admin/order")
@RestController
public class ServiceOrderController {

    @Autowired
    private ServiceOrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ServiceNeedMapper serviceNeedMapper;
    @Autowired
    private OrderService orderService;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询订单列表
     * @param page
     * @param kw
     * @param request
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult couponOrderList(@RequestBody JSONObject reqMap, HttpServletRequest request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        PageView<ServiceOrder> page = new PageView<>();
        page=page.startPage(reqMap);
        String kw = reqMap.getString("kw");
        Admin manager = (Admin) request.getAttribute("loginAdmin");
        List<ServiceOrder> list = orderMapper.selectOrderList(kw, page.getFirstResult(), page.getMaxresult());
        for (ServiceOrder order : list) {}
        page.setRecords(list);
        page.setTotalrecord(orderMapper.selectOrderCount(kw));
        result.setData(page);
        return result;
    }

    /**
     * 取消陪诊订单
     * @param orderId
     * @return
     */
    @RequestMapping("cancel")
    public JsonResult cancel(Integer orderId) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            ServiceOrder orderInfo = orderMapper.selectByPrimaryKey(orderId);
            orderInfo.setOrderStatus(-1);//取消状态
            orderMapper.updateByPrimaryKeySelective(orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }

}

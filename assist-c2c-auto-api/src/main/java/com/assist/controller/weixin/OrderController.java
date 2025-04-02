package com.assist.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.assist.author.AuthRequired;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.*;
import com.assist.dao.model.Feedback;
import com.assist.dao.model.ServiceOrder;
import com.assist.dao.model.User;
import com.assist.service.OrderService;
import com.assist.utils.NumberUtils;
import com.assist.utils.page.PageView;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 微信小程序接口：预约订单相关
 */
@RestController
@RequestMapping("api/order")
public class OrderController extends ApiBaseController{

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private ServiceOrderMapper serviceOrderMapper;
    @Autowired
    private ServiceNeedMapper serviceNeedMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FeedbackMapper feedbackMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 提交预约订单
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveOrder(@RequestBody JSONObject req, HttpServletRequest request){
        User user = getCurrUser(request);
        ServiceOrder orderInfo = JSONObject.parseObject(req.toJSONString(), ServiceOrder.class);
        orderInfo.setUserId(user.getUserId());
        String orderCode = NumberUtils.genOrderNo(6);
        while (serviceOrderMapper.selectOrderCountByCode(orderCode) > 0) {
            orderCode = NumberUtils.genOrderNo(6);
        }
        orderInfo.setOrderCode(orderCode);//6位数字
        orderService.saveOrder(orderInfo);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(orderInfo);
        return jsonResult;
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @RequestMapping("detail")
    public JsonResult detail(@RequestParam Integer orderId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        ServiceOrder orderInfo = serviceOrderMapper.selectByPrimaryKey(orderId);
        orderInfo.setServiceNeed(serviceNeedMapper.selectByPrimaryKey(orderInfo.getServiceId()));
        orderInfo.setAssistant(userMapper.selectByPrimaryKey(orderInfo.getAssistantId()));
        orderInfo.setUser(userMapper.selectByPrimaryKey(orderInfo.getUserId()));
        Feedback feedback = new Feedback();
        feedback.setOrderId(orderId);
        if(feedbackMapper.selectCount(feedback)>0){
            orderInfo.setFeedback(feedbackMapper.select(feedback).get(0));
        }
        result.setData(orderInfo);
        return result;
    }

    /**
     * 取消预约订单
     * @param orderId
     * @return
     */
    @RequestMapping("cancel")
    public JsonResult cancel(@RequestParam Integer orderId) {
        JsonResult result = new JsonResult(true, 200, "取消成功");
        orderService.cancelOrder(orderId);
        return result;
    }

    /**
     * 陪诊师查看新订单
     * @param request
     * @return
     */
    @RequestMapping("assistant/neworders")
    @AuthRequired
    public JsonResult newOrders(HttpServletRequest request) {
        JsonResult result = new JsonResult(true, 200, "");
        User user = getCurrUser(request);
        int count = 0;
        if(user.getRole()==2){
            count = orderService.checkOrders(user.getUserId());
        }else if(user.getRole()==1) {
            count = orderService.checkPatientOrders(user.getUserId());
        }
        result.setData(count);
        return result;
    }

    /**
     * 陪诊师确认接受陪诊订单
     * @param orderId
     * @return
     */
    @RequestMapping("assistant/confirm")
    public JsonResult assistantConfirm(@RequestParam Integer orderId) {
        JsonResult result = new JsonResult(true, 200, "取消成功");
        orderService.assistantConfirm(orderId);
        return result;
    }

    /**
     * 陪诊师拒绝接受陪诊订单
     * @param orderId
     * @return
     */
    @RequestMapping("assistant/reject")
    public JsonResult assistantReject(@RequestParam Integer orderId) {
        JsonResult result = new JsonResult(true, 200, "取消成功");
        orderService.assistantReject(orderId);
        return result;
    }

    /**
     * 完成订单
     * @param orderId
     * @return
     */
    @RequestMapping("finish")
    public JsonResult finish(@RequestParam Integer orderId) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        orderService.finishOrder(orderId);
        return result;
    }

    /**
     * 我的订单列表
     * @param reqMap
     * @param request
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult myOrderList(@RequestBody JSONObject reqMap, HttpServletRequest request) {
        User user = getCurrUser(request);
        PageView<ServiceOrder> pageView;
        if(user.getRole()==2){
            //陪诊师
            pageView = orderService.findAssistantServiceOrderList(reqMap, user.getUserId());
        }else{
            //病人
            pageView = orderService.findMyServiceOrderList(reqMap, user.getUserId());
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(pageView);
        return jsonResult;
    }

    @RequestMapping("load/user")
    @AuthRequired
    public JsonResult loadUser(HttpServletRequest request) {
        JsonResult result = new JsonResult(true, 200, "读取成功");
        User user = getCurrUser(request);
        result.setData(user);
        return result;
    }

    /**
     * 提交反馈信息
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("feedback")
    @AuthRequired
    public JsonResult feedback(@RequestBody JSONObject req, HttpServletRequest request){
//        User user = getCurrUser(request);
        ServiceOrder orderInfo = serviceOrderMapper.selectByPrimaryKey(req.getInteger("orderId"));
        Feedback fb = new Feedback();
        fb.setOrderId(req.getInteger("orderId"));
        fb.setContent(req.getString("content"));
        fb.setRate(req.getInteger("rate"));
        fb.setServiceId(orderInfo.getServiceId());
        fb.setUserId(orderInfo.getUserId());
        fb.setAssistantId(orderInfo.getAssistantId());
        fb.setFeedbackTime(new Date());
        feedbackMapper.insertSelective(fb);//向数据库插入反馈记录
        //加权更新陪诊师的评分
        User assistant = userMapper.selectByPrimaryKey(orderInfo.getAssistantId());
        double orgRate = assistant.getRate();
        logger.info("陪诊师{}原评分为：{}", assistant.getRealName(), orgRate);
        double newRate = (fb.getRate()*assistant.getTotalService() + orgRate)/(assistant.getTotalService()+1);//新的评分
        newRate = Math.round(newRate * 10) / 10.0;
        logger.info("陪诊师{}新评分为：{}", assistant.getRealName(), newRate);
        assistant.setRate(newRate);
        assistant.setTotalService(assistant.getTotalService()+1);//服务次数加1
        userMapper.updateByPrimaryKeySelective(assistant); //更新到数据库
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(orderInfo);
        return jsonResult;
    }

    /**
     * 陪诊师上传费用
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("update/fee")
    @AuthRequired
    public JsonResult updateFee(@RequestBody JSONObject req, HttpServletRequest request){
        ServiceOrder orderInfo = serviceOrderMapper.selectByPrimaryKey(req.getInteger("orderId"));
        double amount = req.getDouble("amount");
        String remark = req.getString("remark");
        orderInfo.setPrice(amount);
        orderInfo.setRemark(remark);
        orderInfo.setOrderStatus(OrderService.ORDER_STATUS_WAIT_PAY);//待支付状态
        serviceOrderMapper.updateByPrimaryKeySelective(orderInfo);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(orderInfo);
        return jsonResult;
    }

    /**
     * 验证服务码
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("validate/code")
    @AuthRequired
    public JsonResult validateCode(@RequestBody JSONObject req, HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        ServiceOrder orderInfo = serviceOrderMapper.selectByPrimaryKey(req.getInteger("orderId"));
        String orderCode = req.getString("orderCode");
        if(orderCode.equalsIgnoreCase(orderInfo.getOrderCode())){
            jsonResult.setSuccess(true);
            jsonResult.setData(orderInfo);
        }else{
            jsonResult.setSuccess(false);
        }
        return jsonResult;
    }

    /**
     * 我的反馈列表
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("my/feedback/list")
    @AuthRequired
    public JsonResult getMyFeedback(@RequestBody JSONObject req, HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        User user = getCurrUser(request);
        PageView<Feedback> pageView = new PageView<>();
        pageView = pageView.startPage(req);
        Example example = new Example(Feedback.class);
        Example.Criteria criteria = example.createCriteria();
        if(user.getRole()==1){
            //病人
            criteria.andEqualTo("userId", user.getUserId());//assistantId
        }else{
            //陪诊师
            criteria.andEqualTo("assistantId", user.getUserId());//assistantId
        }
        example.orderBy("feedbackTime");
        List<Feedback> list = feedbackMapper.selectByExampleAndRowBounds(example, new RowBounds(pageView.getFirstResult(), pageView.getMaxresult()));
        for(Feedback fb:list){
            fb.setAssistant(userMapper.selectByPrimaryKey(fb.getAssistantId()));
            fb.setUser(userMapper.selectByPrimaryKey(fb.getUserId()));
        }
        pageView.setRecords(list);
        pageView.setTotalrecord(feedbackMapper.selectCountByExample(example));
        jsonResult.setData(pageView);
        return jsonResult;
    }

}

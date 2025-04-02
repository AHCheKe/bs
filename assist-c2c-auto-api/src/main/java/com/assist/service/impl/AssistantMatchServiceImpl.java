package com.assist.service.impl;

import com.assist.dao.mapper.ServiceOrderMapper;
import com.assist.dao.mapper.TagMapper;
import com.assist.dao.mapper.UserMapper;
import com.assist.dao.mapper.UserTagMapper;
import com.assist.dao.model.*;
import com.assist.service.AssistantMatchService;
import com.assist.service.TagService;
import com.assist.utils.DistanceUtils;
import com.assist.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssistantMatchServiceImpl implements AssistantMatchService {

    private final Logger logger = LoggerFactory.getLogger(AssistantMatchServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private UserTagMapper userTagMapper;
    @Autowired
    private ServiceOrderMapper orderMapper;

    /**
     * 综合推荐匹配算法
     * @param serviceNeed 需求记录
     * @return
     */
    @Override
    public ServiceOrder matchPractitioner(ServiceNeed serviceNeed) {
        logger.info("开始匹配陪诊师，需求标签为：{}", serviceNeed.getServiceTags());

        //权重：标签30%，距离20%，性别30%，年龄20%
        //加权平均值 = (权重1 × 值1 + 权重2 × 值2 + … + 权重n × 值n) ÷ (权重1 + 权重2 + … + 权重n)
        Map<Integer, List<User>> userWeightMap = new HashMap<>();//以权重为key，保存候选陪诊师

        if("无特殊要求".equalsIgnoreCase(serviceNeed.getServiceTags())){
            logger.info("该病人无特殊要求，不计算标签权重！");
            List<User> weightUsers = userMapper.selectAllValidateAssistant();
            userWeightMap.put(100, weightUsers);
        }else{
            //1.根据需求标签匹配
            String needTag = serviceNeed.getServiceTags();//病人的需求标签
            Tag tagCondition = new Tag();
            tagCondition.setTagName(needTag);
            //根据标签找到关联的陪诊师列表
            List<UserTag> userTagList = userTagMapper.selectUserTagByName(needTag);
            for(UserTag ut:userTagList){
                logger.info("当前用户ID{},{}",ut.getUserId(),ut.getTagId());
                User hit = userMapper.selectByPrimaryKey(ut.getUserId());

                if(hit.getRole()!=2){
                    //剔除非认证陪诊师
                    continue;
                }
                //计算权重：如果有合适的标签，获得80%权重，否则为20%权重
                int tagWeight = 80; //权重值乘以100，防止出现小数被截取损失精度
                List<User> weightUsers;
                if(userWeightMap.containsKey(tagWeight)){
                    weightUsers = userWeightMap.get(tagWeight);
                }else{
                    weightUsers = new ArrayList<>();
                }
                weightUsers.add(hit);
                userWeightMap.put(tagWeight, weightUsers);
                logger.info("计算符合标准的陪诊师：{}，权重80", hit.getRealName());
            }

            //不符合标签的陪诊师，权重20
            List<UserTag> userNotTagList = userTagMapper.selectUserWithoutTagByName(needTag);
            for(UserTag ut:userNotTagList){
                User hit = userMapper.selectByPrimaryKey(ut.getUserId());
                if(hit.getRole()!=2){
                    //剔除非认证陪诊师
                    continue;
                }
                int tagWeight = 20; //权重值乘以100，防止出现小数被截取损失精度
                List<User> weightUsers;
                if(userWeightMap.containsKey(tagWeight)){
                    weightUsers = userWeightMap.get(tagWeight);
                }else{
                    weightUsers = new ArrayList<>();
                }
                weightUsers.add(hit);
                userWeightMap.put(tagWeight, weightUsers);
                logger.info("计算不符合标准的陪诊师：{}，权重20", hit.getRealName());
            }
        }

        //2.根据距离计算权重
        User patient = userMapper.selectByPrimaryKey(serviceNeed.getUserId());//病人记录，获取他的位置坐标
        if(StringUtils.isNotBlank(serviceNeed.getLat()) || StringUtils.isNotBlank(serviceNeed.getLng())){
            patient.setLat(serviceNeed.getLat());
            patient.setLng(serviceNeed.getLng());
        }
        //计算所有陪诊师与病人的距离权重值
        Map<Integer, List<User>> setp2WeightMap = new HashMap<>();
        for(Map.Entry<Integer, List<User>> entry:userWeightMap.entrySet()){
            //计算权重：距离为20%
            Iterator<User> iterator = entry.getValue().iterator();
            while (iterator.hasNext()){
                int distanceWeight = 0;
                //计算各个候选陪诊师与病人的距离
                User u = iterator.next();
                double currDistance = DistanceUtils.getDistance(patient.getLng(), patient.getLat(), u.getLng(), u.getLat(), 2);
                logger.info("正在计算陪诊师与病人的距离，{}：{}千米", u.getRealName(), currDistance);
                u.setDistance(currDistance);
                //为了让距离越远的权重越低，使用1/距离的方式转换
                distanceWeight = (int) Math.round((1/currDistance) * 100);
                //新的权重：标签权重加上距离权重值
                Integer newWeight = entry.getKey() + Math.round(distanceWeight * 0.2f);
                List<User> weightUsers;
                if(setp2WeightMap.containsKey(newWeight)){
                    weightUsers = setp2WeightMap.get(newWeight);
                }else{
                    weightUsers = new ArrayList<>();
                }
                weightUsers.add(u);
                setp2WeightMap.put(newWeight, weightUsers);//新的权重对应的集合，相同权重的陪诊师放在同一个列表中
            }
        }

        //计算性别权重
        Map<Integer, List<User>> setp3WeightMap = new HashMap<>();
        if(serviceNeed.getGenderNeed()!=0){
            for(Map.Entry<Integer, List<User>> entry:setp2WeightMap.entrySet()){
                //计算权重：性别为30%
                Iterator<User> iterator = entry.getValue().iterator();
                while (iterator.hasNext()){
                    int genderWeight = 0;
                    User u = iterator.next();
                    logger.info("正在计算陪诊师性别权重，{}：{}", u.getRealName(), u.getGender());
                    if(serviceNeed.getGenderNeed()==1 && "男".equals(u.getGender())){
                        genderWeight = 100;
                    }else if(serviceNeed.getGenderNeed()==2 && "女".equals(u.getGender())){
                        genderWeight = 100;
                    }
                    //新的权重：上一步得出的权重值加上这一步的性别权重值
                    Integer newWeight = entry.getKey() + Math.round(genderWeight * 0.3f);
                    List<User> weightUsers;
                    if(setp3WeightMap.containsKey(newWeight)){
                        weightUsers = setp3WeightMap.get(newWeight);
                    }else{
                        weightUsers = new ArrayList<>();
                    }
                    weightUsers.add(u);
                    setp3WeightMap.put(newWeight, weightUsers);//新的权重对应的集合，相同权重的陪诊师放在同一个列表中
                }
            }
        }

        //计算年龄权重
        Map<Integer, List<User>> setp4WeightMap = new HashMap<>();
        if(!serviceNeed.getAgeRange().equals("0")){
            for(Map.Entry<Integer, List<User>> entry:userWeightMap.entrySet()){
                //计算权重：年龄为10%
                Iterator<User> iterator = entry.getValue().iterator();
                while (iterator.hasNext()){
                    int ageWeight = 0;
                    User u = iterator.next();
                    logger.info("正在计算陪诊师年龄权重，{}：{}", u.getRealName(), u.getAge());
                    if(serviceNeed.getAgeRange().equals("1")){
                        //25岁以下
                        if(u.getAge()<25){
                            ageWeight = 100;
                        }
                    }else if(serviceNeed.getAgeRange().equals("2")){
                        //25~30岁
                        if(u.getAge()>=25 && u.getAge()<30){
                            ageWeight = 100;
                        }
                    }else if(serviceNeed.getAgeRange().equals("3")){
                        //30~40岁
                        if(u.getAge()>=30 && u.getAge()<40){
                            ageWeight = 100;
                        }
                    }
                    //新的权重：上一步计算出来的权重，加上年龄权重值
                    Integer newWeight = entry.getKey() + Math.round(ageWeight * 0.1f);
                    List<User> weightUsers;
                    if(setp4WeightMap.containsKey(newWeight)){
                        weightUsers = setp4WeightMap.get(newWeight);
                    }else{
                        weightUsers = new ArrayList<>();
                    }
                    weightUsers.add(u);
                    setp4WeightMap.put(newWeight, weightUsers);//新的权重对应的集合，相同权重的陪诊师放在同一个列表中
                }
            }
        }

        //整合最终的权重计算结果
        Map<Integer, List<User>> finalWeightMap = setp4WeightMap;
        if(setp4WeightMap.isEmpty()){
            //用户没有指定年龄偏好
            finalWeightMap = setp3WeightMap;
        }
        if(setp3WeightMap.isEmpty()){
            //用户没有指定性别偏好
            finalWeightMap = setp2WeightMap;
        }
        for(Map.Entry<Integer, List<User>> entry:finalWeightMap.entrySet()){
            Iterator<User> iterator = entry.getValue().iterator();
            logger.info("权重计算完成，权重为{}的陪诊师：", entry.getKey());
            for(User u:entry.getValue()){
                logger.info(u.getRealName());
            }
        }
        return this.finalStep(serviceNeed, finalWeightMap);
    }

    /**
     * 最后一步生成订单
     * @param serviceNeed
     * @param userWeightMap
     * @return
     */
    private ServiceOrder finalStep(ServiceNeed serviceNeed, Map<Integer, List<User>> userWeightMap){
        //取出权重最大的陪诊师
        List<Integer> allWeights = new ArrayList<>(userWeightMap.keySet());
        allWeights.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
//        int maxWeight = 0;
//        for(Map.Entry<Integer, List<User>> entry:userWeightMap.entrySet()){
//            if(entry.getKey() > maxWeight){
//                maxWeight = entry.getKey();//循环查找最大权重的陪诊师列表
//            }
//            logger.info("循环权重：{}", entry.getKey());
//        }
//        logger.info("找到最大权重：{}", maxWeight);

        ServiceOrder serviceOrder = null;
        //继续顺位判断这些候选陪诊师是否有档期为病人提供服务
        for(Integer weight:allWeights){
            List<User> candidateAssistants = userWeightMap.get(weight);
            Iterator<User> itr = candidateAssistants.iterator();
            while(itr.hasNext()){
                User u = itr.next();
                int count = orderMapper.selectOrderCountByAssistantTime(u.getUserId(), serviceNeed.getServiceDate());//当天是否有订单
                if(count > 0){
                    logger.info("发现陪诊师{}在当天已经被预约，从候选列表中剔除……", u.getRealName());
                    itr.remove();
                }
            }
            //确认是否所有候选陪诊师都没档期
            if(candidateAssistants.isEmpty()){
                //无法生成订单！
                logger.error("权重为{}的陪诊师都没有档期，继续顺位匹配下一权重的陪诊师！", weight);
            }else{
                User matchUser = candidateAssistants.get(0);
                logger.info("最符合权重匹配算法的陪诊师为{}", matchUser.getRealName());
                return generateOrder(matchUser, serviceNeed);//生成订单
            }
        }
        logger.error("无法分配陪诊师，订单生成失败！");
        return null;
    }

    /**
     * 生成订单
     * @param assistant
     * @param need
     * @return
     */
    private ServiceOrder generateOrder(User assistant, ServiceNeed need){
        logger.info("正在生成订单……");
        ServiceOrder order = new ServiceOrder();
        order.setUserId(need.getUserId());
        order.setAssistantId(assistant.getUserId());
        //将订单状态设为“等待陪诊师确认”，不能强买强卖，要让陪诊师看到这个订单是否值得接，再决定是否接单
        order.setOrderStatus(OrderServiceImpl.ORDER_STATUS_WAIT_CONFIRM);
        order.setCreateTime(new Date());
        order.setServiceId(need.getServiceId());
        order.setServiceTime(need.getServiceDate());
        order.setPrice(need.getPrice());//订单价格
        String orderCode = NumberUtils.genOrderNo(6);
        int existCount = orderMapper.selectOrderCountByCode(orderCode);
        while (existCount > 0) {
            orderCode = NumberUtils.genOrderNo(6);
            existCount = orderMapper.selectOrderCountByCode(orderCode);
        }
        order.setOrderCode(orderCode);//6位数字
        orderMapper.insertSelective(order);//插入数据库
        logger.info("新订单已插入数据库");
        return order;
    }
}

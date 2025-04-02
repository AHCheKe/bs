package com.assist.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.HospitalMapper;
import com.assist.dao.mapper.ServiceTypeMapper;
import com.assist.dao.model.Hospital;
import com.assist.dao.model.ServiceType;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 医院信息管理接口
 */
@RestController
@RequestMapping("/admin/hospital")
public class HospitalManagerApi {

    @Autowired
    private HospitalMapper hospitalMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询列表
     * @param request
     * @return
     */
    @RequestMapping("list")
    public JsonResult dataList(@RequestBody JSONObject request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        PageView<Hospital> page = new PageView<>();
        page = page.startPage(request);
        String kw = request.getString("kw");
        Example example = new Example(Hospital.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("hospitalName", "%"+kw+"%");
        }
//        example.orderBy("lastUpdate").desc();
        List<Hospital> list = hospitalMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for(Hospital hospital : list){
            if(StringUtils.isNotBlank(hospital.getHospitalTags())){
                String[] tags = hospital.getHospitalTags().split(";");
                List<String> hTags = new ArrayList<>(Arrays.asList(tags));
                hospital.setTagsList(hTags);
            }
        }
        page.setRecords(list);
        page.setTotalrecord(hospitalMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 删除参数
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "删除成功");
        hospitalMapper.deleteByPrimaryKey(id);
        return result;
    }

    @RequestMapping("info/{id}")
    public JsonResult info(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Hospital config = hospitalMapper.selectByPrimaryKey(id);
        result.setData(config);
        return result;
    }

    @RequestMapping("save")
    public JsonResult saveOrUpdateHospital(@RequestBody Hospital hospital) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            if(hospital.getHospitalId()==null){
                hospitalMapper.insertSelective(hospital);
            }else{
                hospitalMapper.updateByPrimaryKeySelective(hospital);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
}

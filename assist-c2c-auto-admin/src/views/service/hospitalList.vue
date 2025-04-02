<template>
    <div>
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="医院名称">
          <el-input v-model="queryForm.kw" placeholder="医院名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
  
      <el-table v-loading="loading" :data="list">
        <!-- <el-table-column label="序号" type="index" width="100" /> -->
        <el-table-column align="center" label="" width="80">
            <template #default="scope">
            <el-avatar shape="square" size="medium" :src="scope.row.mainPic"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="hospitalName" label="医院名称">
        </el-table-column>
        <el-table-column align="center" label="所在城市" width="100">
          <template #default="scope">
            <span>{{ scope.row.city }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="tel" label="联系电话" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" prop="address" label="详细地址" show-overflow-tooltip>
        </el-table-column>
        <!-- <el-table-column align="center" prop="openTime" label="类别" show-overflow-tooltip>
        </el-table-column> -->
        <!-- <el-table-column label="创建时间" align="center" prop="createTime" >
            <template #default="scope">
                <div>{{timestampToDayHM(scope.row.createTime)}}</div>
            </template>
        </el-table-column> -->
        <el-table-column align="center" prop="status" label="标签">
          <template #default="scope">
            <el-tag v-for="(tag,i) in scope.row.tagsList" :key="i" type="success" style="margin-right: 2px;">{{ tag }}</el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" sortable prop="floor" label="最后更新">
          <template slot-scope="scope">
            <div>{{scope.row.lastUpdate|timestampToTime}}</div>
          </template>
        </el-table-column> -->
        <el-table-column align="center" label="操作" width="220">
          <template #default="scope">
            <!-- <el-button link type="primary" icon="Document" @click="showDetail(scope.row.hospitalId)">详情</el-button> -->
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row.hospitalId)">修改</el-button>
            <el-button link type="danger" icon="Close" @click="handleDelete(scope.row.hospitalId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />
  
      <!-- 添加或修改对话框 -->
      <el-dialog v-model="open" :title="title" width="800px" append-to-body>
        <el-form label-width="120px" :model="editForm" :rules="rules" ref="formRef">
          <el-form-item label="医院名称" prop="hospitalName">
            <el-input
              v-model="editForm.hospitalName"
              auto-complete="off"
              placeholder=''
            ></el-input>
          </el-form-item>
          <el-form-item label="所在地区" prop="city">
            <!-- <el-cascader
              :options="options"
              v-model="selectedOptions"
              @change="handleChangeArea">
            </el-cascader> -->
            <elui-china-area-dht isall :leave="2" @change="handleChangeArea" v-model="selectedCityOptions"></elui-china-area-dht>
            <span v-if="isEdit==1">当前：{{ editForm.city+'-'+editForm.area }}（如不需修改城市可不选）</span>
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input
              v-model="editForm.address"
              auto-complete="off"
              placeholder=''
            ></el-input>
          </el-form-item>
          <!-- <div style="padding-left: 120px;color: #999;">请使用地图工具拾取坐标并填入：<a href="https://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank">去拾取</a></div>
          <el-form-item label="经纬度" prop="posLat">
            <el-input
              v-model="editForm.posLat"
              auto-complete="off"
              placeholder="格式：经度,纬度"
            ></el-input>
          </el-form-item> -->
          <el-form-item label="联系电话" prop="tel">
            <el-input
              v-model="editForm.tel"
              auto-complete="off"
              placeholder=''
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="营业时间" prop="openTime">
            <el-input
              v-model="editForm.openTime"
              auto-complete="off"
              placeholder="如：7:30~22:30"
            ></el-input>
          </el-form-item> -->
          <el-form-item label="标签" prop="hospitalTags">
            <el-input
              v-model="editForm.hospitalTags"
              auto-complete="off"
              placeholder="如：三甲;综合，用分号;隔开"
            ></el-input>
          </el-form-item>
          <el-form-item label="详细介绍" prop="detail" id='quillEditorQiniu'>
            <el-input
              v-model="editForm.detail"
              type="textarea"
              :rows="5"
              placeholder="请输入医院详细描述信息"
            ></el-input>
          </el-form-item>
          <el-form-item label="服务须知" prop="detail" id=''>
            <el-input
              v-model="editForm.notice1"
              type="textarea"
              :rows="3"
              placeholder="请输入陪诊服务须知"
            ></el-input>
          </el-form-item>
          <el-form-item label="注意事项" prop="detail" id=''>
            <el-input
              v-model="editForm.notice2"
              type="textarea"
              :rows="3"
              placeholder="请输入医院就诊注意事项"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="状态" prop="status">
            <el-radio v-model="editForm.status" :label="1">正常</el-radio>
            <el-radio v-model="editForm.status" :label="2">维修中</el-radio>
          </el-form-item> -->
          <!-- <el-form-item label="医院主图" prop="role">
            <el-upload 
              class="avatar-uploader showUploader"
              ref="businessLicense"
              action
              :auto-upload="false"
              :show-file-list="false"
              :on-remove="handleRemoveLogo"
              :on-change="uploadLogoFile"
            >
            <ul class="img-list">
              <li v-if="editForm.mainPic==''" class="addpic"><el-icon><Plus/></el-icon></li>
            </ul>
            </el-upload>
            <ul class="img-list" v-if="editForm.mainPic!=''">
              <li class="img-li">
                <img :src="editForm.mainPic" class="avatar aspect-fill" style="width: 250px;height: 180px;" />
                <i class="logo-remove el-icon-delete" @click="handleRemoveLogo"><el-icon><Delete /></el-icon></i>
              </li>
            </ul>
          </el-form-item> -->
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="open = false">取 消</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </template>
  <script setup lang="ts">
  import { ref, reactive, nextTick, toRef } from "vue";
  import baseService from "@/service/baseService";
  import { ElMessage, ElMessageBox } from "element-plus";
  import { timestampToDayHM, timestampToHM } from "@/utils/utils"
  import { regionData, codeToText, TextToCode } from 'element-china-area-data'
  import { fileUploader } from '@/utils/upload.js'
  import { useRouter, useRoute } from 'vue-router';
  
  import { EluiChinaAreaDht } from 'elui-china-area-dht'
  const chinaData = new EluiChinaAreaDht.ChinaArea().chinaAreaflat

  const router = useRouter();
  const route = useRoute();
  // 查询参数
  const queryForm = reactive({
    kw: '',
    pageNo: 1,
    pageSize: 10
  });
  // 列表内容数量
  const total = ref(0);
  // 列表是否加载
  const loading = ref(true);
  // 列表返回值
  const list = ref<any[]>([]);
  const selectedCityOptions = ref<any[]>([]);
  
  // 表单实例
  const formRef = ref();
  // 是否打开弹出框
  const open = ref(false);
  const isEdit = ref(0);
  const openDetail = ref(false);
  // 弹出框标题
  const title = ref('');
  const detailData = ref();
  // 提交表单数据
  let editForm = toRef(
    reactive({
      hospitalId: '',
      hospitalName: '',
      tel: '',
      hospitalTags: '',
      pics: '',
      mainPic: '',
      status: 1,
      posLng: '',
      posLat: '',
      detail: '',
      province: '',
      city: '',
      area: '',
      address: '',
      notice1: '',
      notice2: '',
    })
  );
  const options = ref(regionData)
  const selectedOptions = ref<any>([])
  //多图上传的返回图片url数组
  const pics = ref<any>([])
  const uploadUrlPath = ref();

  // 表单验证
  const rules = ref({
    deptName: [{ required: true, message: "规则名称不能为空", trigger: "blur" }],
    leader: [{ required: true, message: "负责人不能为空", trigger: "blur" }],
    phone: [{ required: true, message: "手机号不能为空", trigger: "blur" }],
    email: [{ required: true, message: "邮箱不能为空", trigger: "blur" }]
  });
  
  /**
   * 查询列表
   */
  const getList = () => {
    loading.value = true;
    baseService
      .post("/admin/hospital/list", queryForm)
      .then((res) => {
        loading.value = false;
        if (res.code === 200) {
          list.value = res.data.records;
          total.value = res.data.totalrecord;
        } else {
          list.value = [];
        }
      })
      .catch(() => {
        loading.value = false;
      });
  };
  
  /**
   * 搜索按钮操作
   */
  function handleQuery() {
    queryForm.pageNo = 1;
    getList();
  }
  
  /**
   * 表单重置
   */
  function reset() {
    editForm.value = {
      hospitalId: '',
      hospitalName: '',
      tel: '',
      hospitalTags: '',
      pics: '',
      mainPic: '',
      status: 1,
      posLng: '',
      posLat: '',
      detail: '',
      province: '',
      city: '',
      area: '',
      address: '',
      notice1: '',
      notice2: '',
    };
    selectedCityOptions.value = []
    pics.value = []
  
    nextTick(() => {
      formRef.value?.resetFields();
    });
  }
  
  /**
   * 新增按钮操作
   */
  function handleAdd() {
    reset();
    open.value = true;
    isEdit.value = 0;
    console.log('图片:',pics.value)
    title.value = "添加";
  }

  function showDetail(data: any){
    console.log('进入详情页:', data)
    router.push({path:'/admin/hospital/info', query: {shopid:data}})
  }
  
  /**
   * 修改按钮操作
   * @param id 主键
   */
  function handleUpdate(id: string) {
    open.value = true;
    title.value = "修改";
    isEdit.value = 1;
    reset();
    baseService.get(`/admin/hospital/info/${id}`).then((res) => {
      if (res.code === 200) {
        editForm.value = res.data;
        pics.value = res.data.pics.split(',')
      }
    });
  }
  
  /**
   * 提交按钮
   */
  function submitForm() {
    formRef.value.validate((valid: boolean) => {
      if (!valid) return;
      console.log('图片字段', pics.value)
      editForm.value.pics = pics.value.join(',')
      baseService.post(`/admin/hospital/save`, editForm.value).then((res) => {
        if (res.code === 200) {
          ElMessage.success(res.msg);
          open.value = false;
          getList();
        } else {
          ElMessage.error(res.msg);
        }
      });
    });
  }
  
  /**
   * 删除按钮操作
   * @param id 主键
   */
  function handleDelete(id: any) {
    ElMessageBox.confirm("确认要删除当前项吗?", "提示").then(() => {
      baseService.get(`/admin/hospital/delete/${id}`).then((res) => {
        if (res.code === 200) {
          ElMessage.success(res.msg);
          getList();
        } else {
          ElMessage.error(res.msg);
        }
      });
    });
  }

  /**
   * 更新状态
   * @param id 
   */
  function handleStatus(id: any, status: any) {
    let msg = '确定要将门店更新为营业状态吗？'
    if(status == -1){
      msg = '确定将门店更新为关店状态吗？'
    }
    ElMessageBox.confirm(msg, "提示").then(() => {
      baseService.delete(`/manager/update/status`, {hospitalId: id, status: status}).then((res) => {
        if (res.code === 200) {
          ElMessage.success(res.msg);
          getList();
        } else {
          ElMessage.error(res.msg);
        }
      });
    });
  }

  function handlePreview(file: any) {
      console.log(file);
  }
  function handleRemove(file: any, fileList: any) {
    alert('移除')
      //return this.$confirm(`确定移除 ${file.name}？`);
  }
  function beforeRemove(file: any, fileList: any) {
    alert('移除')
      // return this.$confirm(`确定移除 ${file.name}？`);
  }

    // 图片上传
  function uploadSectionFile(file: any) {
    console.log('选定图片！',file)
    const typeArr = ["image/png", "image/gif", "image/jpeg", "image/jpg"];
    const isJPG = typeArr.indexOf(file.raw.type) !== -1;
    const isLt3M = file.size / 1024 / 1024 < 3;
    if (!isJPG) {
      // ElMessage.error("只能是图片!");
      // this.$refs.upload.clearFiles();
      // this.files = null;
      return;
    }
    if (!isLt3M) {
      // ElMessage.error("上传图片大小不能超过 3MB!");
      // this.$refs.upload.clearFiles();
      // this.files = null;
      return;
    }
    // FormData 对象
    var formData = new FormData();
    // 文件对象
    formData.append("myFile", file.raw)
    console.log('上传图片表单：', formData)
    fileUploader(formData).then((res) => {
      if (res.data.errno==0) {
        //this.photoObj = res.data.data.objectId;
        console.log('图片上传成功！', res.data)
        pics.value.push(res.data.data);
      } else {
        // ElMessage.error(res.data.message);
      }
    })
  }
    // 上传LOGO图片
  function uploadLogoFile(file: any){
    const typeArr = ["image/png", "image/gif", "image/jpeg", "image/jpg"];
    const isJPG = typeArr.indexOf(file.raw.type) !== -1;
    const isLt3M = file.size / 1024 / 1024 < 3;
    if (!isJPG) {
      // ElMessage.error("只能是图片!");
      // this.$refs.upload.clearFiles();
      // this.files = null;
      return;
    }
    if (!isLt3M) {
      // ElMessage.error("上传图片大小不能超过 3MB!");
      // this.$refs.upload.clearFiles();
      // this.files = null;
      return;
    }
    var formData = new FormData();
    formData.append("myFile", file.raw)
    fileUploader(formData).then((res) => {
      console.log('LOGO上传成功', res)
      if (res.data.errno==0) {
        editForm.value.mainPic=res.data.data[0]
      } else {
        // ElMessage.error(res.data.message);
      }
    })
  }
  // 移除LOGO图片
  function handleRemoveLogo(file: any){
    editForm.value.mainPic=''
    console.log('移除LOGO图片！',editForm.value.mainPic)
  }

  // 移除多图中的其中一张
  function handleRemovePics(idx: number){
    console.log('移除图片下标为：', idx)
    pics.value.splice(idx, 1);
  }
  
  // 富文本编辑器：上传图片成功
  function uploadEditorSuccess(res: any, file:any) {
    // console.log("上传成功")
    // // this.$emit('upload',res, file)
    // console.log(res, file);
    // //拼接出上传的图片在服务器的完整地址
    // let imgUrl=res.data[0];
    // let type=imgUrl.substring(imgUrl.lastIndexOf(".")+1);
    // console.log(type);
    // // 获取富文本组件实例
    // let quill = this.$refs.customQuillEditor.quill;
    // // 获取光标所在位置
    // let length = quill.getSelection().index;
    // // 插入图片 res.info为服务器返回的图片地址
    // quill.insertEmbed(length, 'image', imgUrl)
    // // 调整光标到最后
    // quill.setSelection(length + 1)
    // //取消上传动画
    // this.quillUpdateImg = false;
  }
  // 上传(文件)图片失败
  function uploadEditorError(res: any, file: any) {
    console.log(res);
    console.log(file);
    //页面提示
    ElMessage.error('上传图片失败')
    //取消上传动画
    // this.quillUpdateImg = false;
  }
  //上传图片之前async
  function beforeEditorUpload(res: any, file: any){
    //显示上传动画
    // this.quillUpdateImg = true;
    console.log(res);
  }
  //上传组件返回的结果
  function uploadResult (res: any){
    uploadUrlPath.value=res;
  }

  // function handleChangeArea(value) {
  //   console.log('选择城市控件',value)
  //   getCodeToText(null, value)
  // }
  function handleChangeArea(e) {
    const one = chinaData[e[1]]
    const two = chinaData[e[2]]
    console.log(one, two)
    editForm.value.city = one.label
    editForm.value.area = two.label
  }

  function getCodeToText(codeStr: any, codeArray: any) {
    if (null === codeStr && null === codeArray) {
      return null;
    } else if (null === codeArray) {
      codeArray = codeStr.split(",");
    }
    let area = '';
    switch (codeArray.length) {
      case 1:
        area += codeToText[codeArray[0]];
        break;
      case 2:
        area += codeToText[codeArray[0]] + "/" + codeToText[codeArray[1]];
        break;
      case 3:
        area +=
          codeToText[codeArray[0]] +
          "/" +
          codeToText[codeArray[1]] +
          "/" +
          codeToText[codeArray[2]];
        break;
      default:
        break;
    }
    console.log('省市区',area)
    editForm.value.province = codeToText[codeArray[0]]
    editForm.value.city = codeToText[codeArray[1]]
    editForm.value.area = codeToText[codeArray[2]]
    return area;
  }
  
  getList();
  </script>
  
<style scoped>
.pointer{
    cursor: pointer;
}
.pointer:HOVER{
    color: #409eff;
}
.introduction{
  width: 80%;
  margin: 15px auto;
  white-space: pre-line;
}
.addpic{
  font-size: 22px;
  padding: 20px 25px 15px 25px;
  border: 2px dashed #ccc;
  color: #888;
  height: 68px;
}
.img-list{
  display: flex;
  flex-wrap: wrap;
}
.img-list .img-li{
  width: 260px;
  position: relative;
  margin-right: 10px;
}
.logo-remove{
  font-size: 22px;
  position: absolute;
  top: 10px;
  right: 10px;
  color: #f00;
  cursor: pointer;
  text-shadow: #000 4px 4px 5px;
}
.logo-remove .el-icon{
  box-shadow: #fff 3px 5px 6px;
  background-color: #fff;
}
.logo-remove:HOVER{
  color: #F56C6C;
}
</style>
  
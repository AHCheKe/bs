<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <!-- <el-form-item label="系统参数管理">
        <el-input v-model="queryForm.kw" placeholder="参数Key" clearable />
      </el-form-item> -->
      <el-form-item>
        <!-- <el-button type="primary" @click="handleQuery">查询</el-button> -->
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list">
      <!-- <el-table-column label="序号" type="index" width="100" /> -->
      <el-table-column align="center" label="图片">
        <template #default="scope">
          <a :href="scope.row.picUrl" target="_blank">
            <el-avatar shape="square" size="large" :src="scope.row.picUrl"></el-avatar>
          </a>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="picUrl" label="url" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="seq" label="排序">
      </el-table-column>
      <el-table-column align="center" prop="remark" label="备注" show-overflow-tooltip>
      </el-table-column>
      <!-- <el-table-column align="center" prop="lastUpdate" label="更新时间">
        <template #default="scope">
            <div>{{timestampToDayHM(scope.row.lastUpdate)}}</div>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="操作" width="220">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row.slidePicId)">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row.slidePicId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog v-model="open" :title="title" width="800px" append-to-body>
      <el-form label-width="120px" :model="editForm" :rules="rules" ref="formRef">
        <!-- <el-form-item label="超链接" prop="link">
          <el-input
            v-model="editForm.link"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item> -->
        <el-form-item label="排序" prop="seq">
          <el-input
            v-model="editForm.seq"
            type="number"
            auto-complete="off"
            placeholder="数值越小越靠前"
          ></el-input>
        </el-form-item>
        <el-form-item label="图片" prop="role">
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
            <li v-if="editForm.picUrl==''" class="addpic"><el-icon><Plus/></el-icon></li>
          </ul>
          </el-upload>
          <ul class="img-list" v-if="editForm.picUrl!=''">
            <li class="img-li">
              <img :src="editForm.picUrl" class="avatar aspect-fill" style="width: 250px;height: 180px;" />
              <i class="logo-remove el-icon-delete" @click="handleRemoveLogo"><el-icon><Delete /></el-icon></i>
            </li>
          </ul>
        </el-form-item>
        <el-form-item label="说明" prop="remark">
          <el-input
            v-model="editForm.remark"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
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

const router = useRouter();
const route = useRoute();
// 查询参数
const queryForm = reactive({
  kw: "",
  pageNo: 1,
  pageSize: 10
});
// 列表内容数量
const total = ref(0);
// 列表是否加载
const loading = ref(true);
// 列表返回值
const list = ref<any[]>([]);

// 表单实例
const formRef = ref();
// 是否打开弹出框
const open = ref(false);
const openDetail = ref(false);
// 弹出框标题
const title = ref("");
const detailData = ref();
// 提交表单数据
let editForm = toRef(
  reactive({
    slidePicId: '',
    picUrl: "",
    remark: "",
    link: "",
    seq: '',
  })
);
const options = ref(regionData)
const selectedOptions = ref<any>([])
const pics = ref<any>([])
const uploadUrlPath = ref();

// 表单验证
const rules = ref({
  picUrl: [{ required: true, message: "不能为空", trigger: "blur" }],
  // value: [{ required: true, message: "不能为空", trigger: "blur" }],
});

/**
 * 查询列表
 */
const getList = () => {
  loading.value = true;
  baseService
    .get("/admin/slide/list", queryForm).then((res) => {
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
    slidePicId: '',
    picUrl: "",
    remark: "",
    link: "",
    seq: ''
  };

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
  title.value = "添加";
}

/**
 * 修改按钮操作
 * @param id 主键
 */
function handleUpdate(id: string) {
  open.value = true;
  title.value = "修改";
  reset();
  baseService.get(`/admin/slide/info/${id}`).then((res) => {
    if (res.code === 200) {
      editForm.value = res.data;
    }
  });
}

/**
 * 提交按钮
 */
function submitForm() {
  formRef.value.validate((valid: boolean) => {
    if (!valid) return;
    // editForm.value.pics = pics.join(',')
    if(editForm.value.seq==''){editForm.value.seq='10'}
    baseService.post(`/admin/slide/save`, editForm.value).then((res) => {
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
    baseService.delete(`/rules/delete`, id).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        getList();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
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
        editForm.value.picUrl=res.data.data[0]
      } else {
        // ElMessage.error(res.data.message);
      }
    })
  }

// 移除LOGO图片
function handleRemoveLogo(file: any){
  editForm.value.picUrl=''
  console.log('移除LOGO图片！',editForm.value.picUrl)
}

// 移除多图中的其中一张
function handleRemovePics(idx: number){
  console.log('移除图片下标为：', idx)
  pics.value.splice(idx, 1);
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

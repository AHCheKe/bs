<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="系统参数管理">
        <el-input v-model="queryForm.kw" placeholder="参数Key" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="序号" type="index" width="100" />
      <el-table-column align="center" prop="confKey" label="参数Key">
      </el-table-column>
      <el-table-column align="center" prop="value" label="参数值" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="remark" label="参数说明" show-overflow-tooltip>
      </el-table-column>
      <!-- <el-table-column align="center" prop="status" label="状态" width="80">
        <template #default="scope">
          <span v-if="scope.row.status == 1" style="color:#67C23A;">正常</span>
          <span v-else-if="scope.row.status == -1" style="color:#666666;">已关店</span>
        </template>
      </el-table-column> -->
      <el-table-column align="center" prop="lastUpdate" label="更新时间">
        <template #default="scope">
            <div>{{timestampToDayHM(scope.row.lastUpdate)}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="220">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row.configId)">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleStatus(scope.row.configId, -1)" v-if="scope.row.status == 1">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog v-model="open" :title="title" width="800px" append-to-body>
      <el-form label-width="120px" :model="editForm" :rules="rules" ref="formRef">
        <el-form-item label="参数Key" prop="confKey">
          <el-input
            v-model="editForm.confKey"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="参数值" prop="value">
          <el-input
            v-model="editForm.value"
            auto-complete="off"
            placeholder=""
          ></el-input>
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

    <!--详情对话框-->
    <el-dialog v-model="openDetail" :title="title" width="60%" append-to-body v-if="detailData">
      <el-descriptions class="margin-top" :column="2" border>
          <el-descriptions-item label="模板名称" labelStyle="width: 160px;">{{ detailData.ruleName }}</el-descriptions-item>
          <el-descriptions-item label="模板编号" labelStyle="width: 160px;">{{ detailData.ruleCode }}</el-descriptions-item>
          <el-descriptions-item label="版本号" labelStyle="width: 160px;">{{ detailData.version }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" labelStyle="width: 160px;">{{ timestampToDayHM(detailData.createTime) }}</el-descriptions-item>
          <!-- <el-descriptions-item label="标签">
          <el-tag >{{ detailData.tags }}</el-tag>
          </el-descriptions-item> -->
          <!-- <el-descriptions-item label="模板正文" labelStyle="width: 160px;">{{ detailData.ruleContent }}</el-descriptions-item> -->
      </el-descriptions>
        <!-- <div v-html="detailData.ruleContent" class="introduction"></div> -->
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
    configId: '',
    confKey: "",
    remark: "",
    value: ""
  })
);
const options = ref(regionData)
const selectedOptions = ref<any>([])
const pics = ref<any>([])
const uploadUrlPath = ref();

// 表单验证
const rules = ref({
  confKey: [{ required: true, message: "不能为空", trigger: "blur" }],
  value: [{ required: true, message: "不能为空", trigger: "blur" }],
});

/**
 * 查询列表
 */
const getList = () => {
  loading.value = true;
  baseService
    .get("/admin/config/list", queryForm).then((res) => {
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
    configId: '',
    confKey: "",
    remark: "",
    value: ""
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
  baseService.get(`/admin/config/info/${id}`).then((res) => {
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
    baseService.post(`/admin/config/save`, editForm.value).then((res) => {
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
    baseService.delete(`/manager/update/status`, {configId: id, status: status}).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        getList();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
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

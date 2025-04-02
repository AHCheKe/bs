<template>
    <div>
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="陪诊订单">
          <el-input v-model="queryForm.kw" placeholder="手机号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="search">查询</el-button>
        </el-form-item>
      </el-form>
  
      <el-table v-loading="loading" :data="list">
        <!-- <el-table-column label="序号" type="index" width="100" /> -->
        <el-table-column label="创建时间" align="center" prop="createTime" width="120">
            <template #default="scope">
                <div>{{timestampToDayHM(scope.row.createTime)}}</div>
            </template>
        </el-table-column>
        <el-table-column label="服务类型" align="center" prop="serviceNeed.serviceType">
        </el-table-column>
        <el-table-column label="城市" align="center" prop="serviceNeed.city" />
        <el-table-column label="医院" align="center" prop="serviceNeed.hospital" width="200" show-overflow-tooltip/>
        <el-table-column label="就诊时间" align="center" prop="serviceNeed.createTime" width="120">
            <template #default="scope">
                <div>{{timestampToDayHM(scope.row.serviceNeed.serviceDate)}}</div>
            </template>
        </el-table-column>
        <el-table-column label="就诊者" align="center" prop="createTime">
            <template #default="scope">
                <div>{{scope.row.user.realName}}</div>
            </template>
        </el-table-column>
        <el-table-column label="陪诊师" align="center" prop="createTime">
            <template #default="scope">
                <div>{{scope.row.assistant.realName}}</div>
            </template>
        </el-table-column>
        <el-table-column label="服务费用" align="center" prop="price">
            <template #default="scope">
              <span class="price">{{formatAmount(scope.row.price)}}元</span>
            </template>
        </el-table-column>
        <!-- <el-table-column label="服务费用" align="center" prop="price">
            <template #default="scope">
              <span class="price">￥{{formatAmount(scope.row.price)}}</span>
            </template>
        </el-table-column> -->
        <el-table-column label="状态" align="center" prop="orderStatus">
          <template #default="scope">
            <el-tag v-if="scope.row.orderStatus==0">待接单</el-tag>
            <el-tag type="warning" v-if="scope.row.orderStatus==1">待就诊</el-tag>
            <el-tag type="danger" v-if="scope.row.orderStatus==2">待支付</el-tag>
            <el-tag type="success" v-if="scope.row.orderStatus==3">已完成</el-tag>
            <el-tag type="info" v-if="scope.row.orderStatus==-1">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button link type="primary" icon="Document" @click="showDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

      <!--详情对话框-->
      <el-dialog v-model="openDetail" :title="title" width="60%" append-to-body>
        <el-descriptions class="margin-top" :column="2" border v-if="detailData">
            <el-descriptions-item label="订单提交时间" labelStyle="width: 160px;">{{ timestampToDayHM(detailData.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="城市/医院" labelStyle="width: 160px;">{{ detailData.serviceNeed.city }}/{{ detailData.serviceNeed.hospital }}</el-descriptions-item>
            <el-descriptions-item label="服务类型" labelStyle="width: 160px;">{{ detailData.serviceNeed.serviceType }}</el-descriptions-item>
            <el-descriptions-item label="需求标签" labelStyle="width: 160px;">
              <el-tag type="primary">{{ detailData.serviceNeed.serviceTags }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="是否接送" labelStyle="width: 160px;">
                <el-tag type="info" v-if="detailData.serviceNeed.needPickup==0">否</el-tag>
                <el-tag type="primary" v-if="detailData.serviceNeed.needPickup==1">是</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="详细需求" labelStyle="width: 160px;" :span="2">{{ detailData.serviceNeed.content }}</el-descriptions-item>
            <el-descriptions-item label="就诊人" labelStyle="width: 160px;">{{ detailData.user.realName }}</el-descriptions-item>
            <el-descriptions-item label="陪诊师" labelStyle="width: 160px;">
              <span v-if="detailData.assistant">{{ detailData.assistant.realName }}</span>
              <span v-else>订单未确认</span>
            </el-descriptions-item>
            <el-descriptions-item label="订单状态" labelStyle="width: 160px;">
                <el-tag v-if="detailData.orderStatus==0">待接单</el-tag>
                <el-tag type="primary" v-if="detailData.orderStatus==1">待就诊</el-tag>
                <el-tag type="danger" v-if="detailData.orderStatus==2">待支付</el-tag>
                <el-tag type="success" v-if="detailData.orderStatus==3">已完成</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="接单时间" labelStyle="width: 160px;">
              <span v-if="detailData.orderStatus==1">{{ timestampToDayHM(detailData.acceptTime) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="完成时间" labelStyle="width: 160px;" v-if="detailData.orderStatus==2">
              <span>{{ timestampToDayHM(detailData.finishTime) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="评价" labelStyle="width: 160px;" v-if="detailData.orderStatus==2">
              <span></span>
            </el-descriptions-item>
            
        </el-descriptions>
      </el-dialog>
    </div>
  </template>
  <script setup lang="ts">
  import { ref, reactive, nextTick, toRef } from "vue";
  import baseService from "@/service/baseService";
  import { ElMessage, ElMessageBox } from "element-plus";
  import { timestampToDayHM, timestampToHM, formatAmount } from "@/utils/utils"
  
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
  let form = toRef(
    reactive({
      deptId: "",
      deptName: "",
      leader: "",
      phone: "",
      email: ""
    })
  );
  // 表单验证
  const rules = ref({
    deptName: [{ required: true, message: "部门名称不能为空", trigger: "blur" }],
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
      .post("/admin/order/list", queryForm)
      .then((res) => {
        loading.value = false;
        if (res.code === 200) {
          list.value = res.data.records;
          total.value = res.data.totalrecord;
        } else {
          list.value = [];
        }
      }).catch(() => {
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
    form.value = {
      deptId: "",
      deptName: "",
      leader: "",
      phone: "",
      email: ""
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

  function showDetail(data){
    openDetail.value = true;
    title.value = "订单详情";
    detailData.value = data
  }
  
  /**
   * 修改按钮操作
   * @param id 主键
   */
  function handleUpdate(id: string) {
    open.value = true;
    title.value = "修改";
    reset();
    baseService.get(`/sysDept/info/${id}`).then((res) => {
      if (res.code === 200) {
        form.value = res.data;
      }
    });
  }
  
  /**
   * 提交按钮
   */
  function submitForm() {
    formRef.value.validate((valid: boolean) => {
      if (!valid) return;
      baseService.post(`/sysDept/save`, form.value).then((res) => {
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
      baseService.delete(`/sysDept/delete`, id).then((res) => {
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
.price{
  color: #f56c6c;
  font-weight: bold;
}
</style>
  
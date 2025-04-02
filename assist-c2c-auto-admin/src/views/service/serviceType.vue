<template>
    <div>
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="服务类型">
          <el-input v-model="queryForm.kw" placeholder="名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="list">
        <!-- <el-table-column label="序号" type="index" width="100" /> -->
        <!-- <el-table-column label="发布时间" align="center" prop="createTime">
            <template #default="scope">
                <div>{{timestampToDayHM(scope.row.publishTime)}}</div>
            </template>
        </el-table-column> -->
        <el-table-column label="服务名称" align="center" prop="serviceName" />
        <el-table-column label="详细描述" align="center" prop="serviceDesc" show-overflow-tooltip/>
        <el-table-column label="费用说明" align="center" prop="serviceNotice" show-overflow-tooltip/>
        <el-table-column label="起价费用" align="center" prop="price">
            <template #default="scope">
                <div>￥{{formatAmount(scope.row.price)}}</div>
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row.serviceId)">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row.serviceId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />
  
      <!-- 添加或修改参数配置对话框 -->
      <el-dialog v-model="open" :title="title" width="700px" append-to-body>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="服务名称" prop="serviceName">
            <el-input v-model="form.serviceName" placeholder="请输入服务名称" />
          </el-form-item>
          <el-form-item label="价格" prop="price">
            <el-input v-model="form.price" placeholder="请输入服务起价" />
          </el-form-item>
          <el-form-item label="服务描述" prop="serviceDesc">
            <el-input v-model="form.serviceDesc" placeholder="请输入服务描述" />
          </el-form-item>
          <el-form-item label="费用说明" prop="serviceNotice">
            <el-input v-model="form.serviceNotice" placeholder="请输入费用说明" />
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
  // 弹出框标题
  const title = ref("");
  // 提交表单数据
  let form = toRef(
    reactive({
      serviceId: "",
      serviceName: "",
      price: "",
      serviceDesc: "",
      serviceNotice: ""
    })
  );
  // 表单验证
  const rules = ref({
    serviceName: [{ required: true, message: "服务名称不能为空", trigger: "blur" }],
    price: [{ required: true, message: "价格不能为空", trigger: "blur" }],
    serviceNotice: [{ required: true, message: "费用说明不能为空", trigger: "blur" }],
  });
  
  /**
   * 查询列表
   */
  const getList = () => {
    loading.value = true;
    baseService
      .get("/admin/service/type/list", queryForm)
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
      serviceId: "",
      serviceName: "",
      price: "",
      serviceDesc: "",
      serviceNotice: ""
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
    baseService.get(`/admin/service/type/info/${id}`).then((res) => {
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
      baseService.post(`/admin/service/type/save`, form.value).then((res) => {
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
      baseService.get(`/admin/service/type/delete/${id}`, id).then((res) => {
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
  
  <style scoped></style>
  
<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="小程序用户管理">
        <el-input v-model="queryForm.kw" placeholder="手机号" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="昵称" align="center" prop="nickName">
          <template #default="scope">
              <div>{{scope.row.nickName}}</div>
          </template>
      </el-table-column>
      <el-table-column label="真实姓名" align="center" prop="realName"/>
      <el-table-column label="性别" align="center" prop="gender"/>
      <el-table-column label="年龄" align="center" prop="age"/>
      <el-table-column label="城市" align="center" prop="location" />
      <el-table-column label="手机号" align="center" prop="mobile" />
      <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
              <el-tag type="success" v-if="scope.row.status==1">正常</el-tag>
              <el-tag type="info" v-if="scope.row.status==-1">已禁用</el-tag>
          </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template #default="scope">
          <el-button link type="primary" icon="Unlock" @click="handleAuth(scope.row.userId)">设为授权用户</el-button>
          <!--<el-button link type="danger" icon="Close" @click="handleDelete(scope.row.userId)" v-if="scope.row.status>0">禁用</el-button>
          <el-button link type="success" icon="Check" @click="handleDelete(scope.row.userId)" v-if="scope.row.status==-1">启用</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog v-model="open" :title="title" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="form.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
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
  role: 1,
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
    deptId: "",
    deptName: "",
    leader: "",
    phone: "",
    email: ""
  })
);
// 表单验证
const rules = ref({
  phone: [{ required: true, message: "手机号不能为空", trigger: "blur" }],
});

/**
 * 查询列表
 */
const getList = () => {
  loading.value = true;
  baseService
    .post("/admin/user/list", queryForm).then((res) => {
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

/**
 * 修改按钮操作
 * @param id 主键
 */
function handleAuth(id: string) {
  ElMessageBox.confirm("确认要将该用户修改为小程序授权登录用户吗?", "提示").then(() => {
    baseService.get(`/admin/user/author/login/${id}`).then((res) => {
      if (res.code === 200) {
        form.value = res.data;
      }
    });
  })
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

<style scoped></style>

<template>
  <div>
    <div class="profile">
      <el-descriptions
        class="margin-top"
        title="管理员个人信息"
        :column="2"
        border
      >
    <template #extra>
      <el-button type="primary" icon="edit" @click="handleUpdate()">修改</el-button>
    </template>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <User />
          </el-icon>
          姓名
        </div>
      </template>
      {{ user.admin.realName }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <iphone />
          </el-icon>
          手机号
        </div>
      </template>
      {{ user.admin.mobile }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <location />
          </el-icon>
          性别
        </div>
      </template>
      {{ user.admin.gender }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <clock />
          </el-icon>
          上次登录时间
        </div>
      </template>
      {{ timestampToDayHM(user.admin.lastLogin) }}
    </el-descriptions-item>
  </el-descriptions>
    </div>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog v-model="open" :title="title" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="登录密码" prop="pwd">
          <el-input v-model="form.pwd" type="password" placeholder="请输入新密码" />
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
import { getCache, setCache, removeCache } from "@/utils/cache";
import { CacheToken } from "@/constants/cacheKey";
import { timestampToDayHM, timestampToHM, formatAmount } from "@/utils/utils"

// 查询参数
const queryForm = reactive({
  kw: "",
  currentpage: 1,
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
let user = reactive({admin: {}});
// 提交表单数据
let form = toRef(
  reactive({
    adminId: "",
    mobile: "",
    pwd: "",
    gender: "",
    realName: "",
  })
);
// 表单验证
const rules = ref({
  phone: [{ required: true, message: "手机号不能为空", trigger: "blur" }],
});

/**
 * 初始化
 */
 const onInit = () => {
  const userInfo = getCache(CacheToken);
  user = userInfo;
  console.log('登录用户：', userInfo)
};

/**
 * 查询列表
 */
const getList = () => {
  loading.value = true;
  baseService
    .get("/admin/user/manager/detail", queryForm)
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
  queryForm.currentpage = 1;
  // getList();
}

/**
 * 表单重置
 */
function reset() {
  form.value = {
    adminId: "",
    mobile: "",
    pwd: "",
    gender: "",
    realName: "",
  };

  nextTick(() => {
    formRef.value?.resetFields();
  });
}

/**
 * 提交按钮
 */
function submitForm() {
  formRef.value.validate((valid: boolean) => {
    if (!valid) return;
    baseService.post(`/admin/user/manager/save`, form.value).then((res) => {
      if (res.code === 200) {
        if(res.success){ElMessage.success(res.msg);}
        else{ElMessage.error(res.msg);}
        open.value = false;
        baseService.get(`/admin/user/manager/detail/${form.value.adminId}`).then((res) => {
        if (res.code === 200) {
            form.value = res.data
            user.admin = res.data
            const userInfo = getCache(CacheToken);
            userInfo.admin = res.data
            setCache(CacheToken, userInfo);
            location.reload();
          }
        });
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

/**
 * 修改按钮操作
 * @param id 主键
 */
 function handleUpdate() {
  open.value = true;
  title.value = "修改"
  form.value = user.admin
}

onInit();

</script>

<style scoped>
.profile{
  width: 680px;
}
</style>

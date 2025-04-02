<template>
    <div>
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="陪诊师标签变更审核">
          <!-- <el-input v-model="queryForm.kw" placeholder="手机号" clearable /> -->
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="RefreshRight">刷新</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="list">
        <!-- <el-table-column label="昵称" align="center" prop="nickName">
            <template #default="scope">
                <div>{{scope.row.nickName}}</div>
            </template>
        </el-table-column> -->
        <el-table-column label="真实姓名" align="center" prop="realName" width="100"/>
        <el-table-column label="性别" align="center" prop="gender"/>
        <el-table-column label="年龄" align="center" prop="age"/>
        <el-table-column label="身份证号" align="center" prop="idNo" show-overflow-tooltip/>
        <el-table-column label="地址" align="center" prop="location" show-overflow-tooltip/>
        <!-- <el-table-column label="申请理由" align="center" prop="certify.appContent" show-overflow-tooltip/> -->
        <el-table-column label="手机号" align="center" prop="mobile" />
        <el-table-column label="新申请的标签" align="center" prop="role" min-width="200">
          <template #default="scope">
            <el-tag type="primary" style="margin-right: 5px;" v-for="(item, idx) in scope.row.userTagList" :key="idx">{{ item.tag.tagName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="250">
          <template #default="scope">
            <!-- <el-button link type="primary" icon="Document" @click="showDetail(scope.row)">详情</el-button> -->
            <el-button link type="success" icon="Check" @click="handelCheck(scope.row.userId)">通过</el-button>
            <el-button link type="danger" icon="Close" @click="handelCheck2(scope.row.userId)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

      <!--详情对话框-->
      <el-dialog v-model="openDetail" :title="title" width="60%" append-to-body>
        <el-descriptions class="margin-top" :column="1" border v-if="detailData">
          <el-descriptions-item label="申请时间" labelStyle="width: 120px;">{{ timestampToDayHM(detailData.certify.appTime) }}</el-descriptions-item>
          <el-descriptions-item label="姓名" labelStyle="width: 120px;">{{ detailData.realName }}</el-descriptions-item>
          <el-descriptions-item label="身份证号" labelStyle="width: 120px;">{{ detailData.idNo }}</el-descriptions-item>
          <el-descriptions-item label="地址" labelStyle="width: 120px;">{{ detailData.location }}</el-descriptions-item>
          <el-descriptions-item label="申请理由">{{ detailData.certify.appContent }}</el-descriptions-item>
          <el-descriptions-item label="申请标签">
            <el-tag type="primary" v-for="(item, index) in detailData.userTagList" :key="index" style="margin-right: 5px;">{{ item.tag.tagName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核状态">
              <el-tag type="warning" v-if="detailData.certify.certifyStatus==0">待审核</el-tag>
              <el-tag type="success" v-if="detailData.certify.certifyStatus==1">审核通过</el-tag>
              <el-tag type="danger" v-if="detailData.certify.certifyStatus==2">已拒绝</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核时间" v-if="detailData.certify.certifyStatus>0">
            <span>{{ timestampToDayHM(detailData.detailData.certifyTime) }}</span>
          </el-descriptions-item>
        </el-descriptions>
        <div class="attach">
          <p>附件信息</p>
          <ul class="img-ul">
            <li v-for="(item, index) in detailData.certify.attachmentList" :key="index">
              <a :href="item" target="_blank"><img :src="item"/></a>
            </li>
          </ul>
        </div>
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
      role: 3,
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
    const detailData = ref();
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
        .post("/admin/user/tag/app", queryForm)
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
      console.log('详情',data)
      openDetail.value = true;
      title.value = "陪诊师申请信息";
      detailData.value = data
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

    function handelCheck(id: string) {
      ElMessageBox.confirm("确认通过该陪诊师的新标签申请吗?", "提示").then(() => {
        baseService.get(`/admin/user/tag/validate/${id}`).then((res) => {
          if (res.code === 200) {
            getList();
            ElMessage({
              message: '已通过陪诊师的标签申请',
              type: 'success',
            })
          }
        });
      })
    }

    function handelCheck2(id: string) {
      ElMessageBox.confirm("确认拒绝该陪诊师的标签申请吗?", "提示").then(() => {
        baseService.get(`/admin/user/tag/reject/${id}`).then((res) => {
          if (res.code === 200) {
            getList();
            ElMessage({
              message: '已拒绝陪诊师的标签申请',
              type: 'warning',
            })
          }
        });
      })
    }
    
    getList();
  </script>
  
<style scoped>
.attach{
  padding: 5px;
}
.attach .img-ul{
  display: flex;
  justify-content: start;
}
.img-ul li{
  width: 250px;
  height: 180px;
  overflow: hidden; /* 防止内容溢出 */
  position: relative;
  margin-right: 10px;
  border: 1px solid #ccc;
}
.img-ul li a{
  cursor: zoom-in;
}
.img-ul img{
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}
</style>
  
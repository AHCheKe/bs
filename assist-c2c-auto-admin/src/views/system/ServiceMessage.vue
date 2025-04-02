<template>
  <div class="main-contain" style="width: 820px;">
    <p class="page-title">在线客服</p>
    <!-- 聊天消息区域 -->
    <div class="user-list">
      <ul>
        <li 
          v-for="(item, idx) in senderList" :key="idx"
          :class="{ active: selectedIndex === idx }"
          @click="selectItem(idx)"
        >
          {{item.realName}}
        </li>
      </ul>
    </div>
    <el-scrollbar class="chat-messages" ref="scrollbar">
      <div v-for="(message, index) in messagesList" :key="index" 
          :class="['message', message.adminId]">
        <div class="message-content">
          <p class="message-time">{{ timestampToDayHM(message.sendTime) }}</p>
          {{ message.content }}
        </div>
      </div>
      <!-- 输入表单 -->
      <el-form :model="form" class="chat-form">
        <el-form-item>
          <el-input
            v-model="form.message"
            placeholder="请输入消息..."
          >
            <template #append>
              <el-button type="primary" @click="sendMessage">发送</el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, nextTick, toRef, onMounted } from "vue";
import baseService from "@/service/baseService";
import { getCache, setCache, removeCache } from "@/utils/cache";
import { ElMessage, ElMessageBox } from "element-plus";
import { timestampToDayHM, timestampToHM } from "@/utils/utils"
import { fileUploader } from '@/utils/upload.js'
import { useRouter, useRoute } from 'vue-router';
import { CacheToken } from "@/constants/cacheKey";

const router = useRouter();
let userData = reactive({});

/**
 * 初始化读取用户列表
 */
const onInit = () => {
  baseService.post("/admin/message/user/list", queryForm)
    .then((res) => {
      if (res.code === 200) {
        senderList.value = res.data
        console.log(senderList)
      } else {

      }
    }).catch(() => {
      loading.value = false;
    });
};


// 查询参数
const queryForm = reactive({
  senderId: "",
  pageNo: 1,
  pageSize: 10
});
const route = useRoute();

// 列表内容数量
const total = ref(0);
// 列表是否加载
const loading = ref(true);
// 列表返回值
const list = ref<any[]>([]);

// 表单实例
const formRef = ref();
const senderList = ref<any[]>([]);
const selectedIndex = ref(-1); // 初始状态为未选中任何项

function goBack(){
  router.back()
}

// 提交表单数据
// let form = toRef(
//   reactive({
//     userId: '',
//     nickName: '',
//     mobile: '',
//     pic: '',
//     pwd: '',
//     gender: '',
//   })
// );
// 表单验证
const rules = ref({
  phone: [{ required: true, message: "手机号不能为空", trigger: "blur" }],
});

const getInfo = () => {
  
}

const selectItem = (index: number) => {
  selectedIndex.value = index;
  console.log('当前选中：', senderList.value[index].userId)
  queryForm.senderId = senderList.value[index].userId
  form.senderId = senderList.value[index].userId
  getList()
};

  /**
   * 查询列表
   */
  const getList = () => {
    loading.value = true;
    baseService
      .post("/admin/message/list", queryForm)
      .then((res) => {
        loading.value = false;
        if (res.code === 200) {
          messagesList.value=res.data
          console.log('消息列表：',messagesList.value)
        } else {
        }
      })
      .catch(() => {
        loading.value = false;
      });
  };

  const messagesList = ref<any[]>([]);
  messagesList.value.push([
    { sender: 'bot', content: '您好！请问有什么可以帮助您？', time: '12:00' }
  ]);
  
  const form = reactive({
    message: '',
    senderId: ''
  });
  const scrollbar = ref(null);

  const sendMessage = () => {
    if (form.message.trim()) {
      console.log('发送消息EEEEE', form)
      // 发送消息
      baseService.post(`/admin/message/send`, form).then((res) => {
        if (res.code === 200) {
          // messagesList.value.push({
            //   sender: 'bot',
            //   content: form.value.message,
            //   time: new Date().toLocaleTimeString()
            // });
            console.log('messagesList.value:', messagesList.value)
            messagesList.value.push({
              adminId: 'admin',
              content: form.message,
              sendTime: new Date()
            });
            form.message = '';
        } else {
          ElMessage.error(res.msg);
        }
      }).catch((err) => {
        console.error('发送消息异常',err)
      });

      //   // 滚动到底部
      //   scrollbar.value.setScrollTop(scrollbar.value.wrap$.scrollHeight);
      // }, 1000);
    }
  };

  // getList();
  onInit();
</script>

<style scoped>
.page-title{
  font-size: 16px;
  font-weight: bold;
  color: #1ea7d7;
  padding-left: 5px;
  border-left: 3px solid #1ea7d7;
}
.chat-container {
  width: 400px;
  height: 500px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  border: 1px solid #ccc;
  height: 580px;
  position: relative;
  padding-bottom: 80px;
}

.message {
  margin-bottom: 15px;
  clear: both;
}

.message-content {
  padding: 8px 12px;
  border-radius: 4px;
  display: inline-block;
  word-wrap: break-word;
  
}

.user .message-content {
  background-color: #409eff;
  color: white;
  margin-left: auto;
}

.admin .message-content {
  background-color: #3a9641;
  color: #fff;
  /* margin-right: auto; */
  float: right;
  margin-top: 10px;
}

.message-time {
  font-size: 12px;
  color: #fff;
  margin: 4px 0;
}

.user .message-time {
  
}

.chat-form {
  padding: 10px;
  border-top: 1px solid #ebeef5;
  position: absolute;
  bottom: 5px;
  left: 0;
  right: 0;
  background-color: #fff;
}
.user-list{
  margin-bottom: 10px;
}
.user-list li{
  display: inline-block;
  margin-right: 8px;
  background-color: #409eff;
  color: #fff;
  padding: 8px 15px;
  cursor: pointer;
}
.user-list .active{
  background-color: #3a9641;
}
</style>
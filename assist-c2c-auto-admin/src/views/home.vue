<template>
  <div class="root">
    <div class="left-container">
      <div class="title-container">
        <img src="@/assets/images/icon.png" width="35" height="35" />
        <span class="title-text">陪诊小程序管理后台</span>
      </div>
      <el-menu background-color="#343844" class="el-menu-container" :default-active="defaultActive" text-color="#fff" @select="handleSelect" unique-opened>
        <!-- <el-menu-item index="/dashboard">
          <el-icon>
            <Odometer />
          </el-icon>
          <span>控制台</span>
        </el-menu-item> -->
        <el-sub-menu index="/service">
          <template #title>
            <el-icon>
              <Notebook />
            </el-icon>
            <span>陪诊业务管理</span>
          </template>
          <!-- <el-menu-item index="/service/list">
            <el-icon>
              <Help />
            </el-icon>
            <span>陪诊需求</span>
          </el-menu-item> -->
          <el-menu-item index="/service/order/list">
            <el-icon>
              <Tickets />
            </el-icon>
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="/service/hospital/list">
            <el-icon>
              <OfficeBuilding />
            </el-icon>
            <span>医院管理</span>
          </el-menu-item>
          <el-menu-item index="/service/type/list">
            <el-icon>
              <Connection />
            </el-icon>
            <span>陪诊类别管理</span>
          </el-menu-item>
          <el-menu-item index="/service/tag/list">
            <el-icon>
              <PriceTag />
            </el-icon>
            <span>标签管理</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/user">
          <template #title>
            <el-icon>
              <User />
            </el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/user/list">
            <el-icon>
              <Files />
            </el-icon>
            <span>小程序用户</span>
          </el-menu-item>
          <el-menu-item index="/user/assistant">
            <el-icon>
              <Notification />
            </el-icon>
            <span>陪诊师管理</span>
          </el-menu-item>
          <!-- <el-menu-item index="/user/assistant/validate">
            <el-icon>
              <Finished />
            </el-icon>
            <span>新陪诊师审核</span>
          </el-menu-item> -->
          <el-menu-item index="/user/assistant/tag/validate">
            <el-icon>
              <TakeawayBox />
            </el-icon>
            <span>标签变更审核</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/content">
          <template #title>
            <el-icon>
              <MessageBox />
            </el-icon>
            <span>内容管理</span>
          </template>
          <el-menu-item index="/content/config/slide">
            <el-icon>
              <Picture></Picture>
            </el-icon>
            <span>小程序轮播图</span>
          </el-menu-item>
          <el-menu-item index="/content/config/notice">
            <el-icon>
              <Paperclip />
            </el-icon>
            <span>资讯管理</span>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="/sys/service">
          <template #title>
            <el-icon>
              <ChatLineRound />
            </el-icon>
            <span>在线客服</span>
          </template>
          <el-menu-item index="/sys/service/message">
            <el-icon>
              <ChatDotRound />
            </el-icon>
            <span>回复消息</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/sys">
          <template #title>
            <el-icon>
              <SetUp />
            </el-icon>
            <span>系统设置</span>
          </template>
          <el-menu-item index="/sys/profile">
            <el-icon>
              <User />
            </el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <!-- <el-menu-item index="/sys/config/slide">
            <el-icon>
              <Picture></Picture>
            </el-icon>
            <span>小程序轮播图</span>
          </el-menu-item> -->
          <el-menu-item index="/sys/config/list">
            <el-icon>
              <Setting />
            </el-icon>
            <span>系统参数</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>

    <div class="rigth-container">
      <div class="head-container">
        <span class="menu-name">欢迎登录！今天是{{ dateVal }}</span>
        <div class="head-user-container">
          <div>系统管理员 <strong v-if="user.admin">{{ user.admin.realName }}</strong></div>
          <img class="head-user-close" src="../assets/images/close.png" @click="logout" />
        </div>
      </div>

      <div class="page">
        <div class="router-container">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getCache, removeCache } from "@/utils/cache";
import { CacheToken } from "@/constants/cacheKey";

const router = useRouter();
let defaultActive = ref("/workflow/definition");
let user = reactive({});

let dateVal = ref<string>();

/**
 * 初始化
 */
const onInit = () => {
  const userInfo = getCache(CacheToken);
  user = userInfo;
  console.log('登录用户：', userInfo)
  defaultActive.value = router.currentRoute.value.path;
};

/**
 * 退出登录
 */
const logout = () => {
  removeCache(CacheToken);
  router.replace("/login");
};

/**
 * 菜单选择
 * @param key 路由
 */
const handleSelect = (key: string) => {
  router.push(key);
};

/**
 * 获取时间
 */
const getDate = () => {
  var date = new Date(); // 获取时间
  var year = date.getFullYear(); // 获取年
  var month = date.getMonth() + 1; // 获取月
  var day = date.getDate(); // 获取日
  // 周一返回的是1，周六是6，但是周日是0
  var week = "日一二三四五六".charAt(new Date().getDay());
  dateVal.value = year + "年" + getNum(month) + "月" + getNum(day) + "日" + " 星期" + week;
};

// 如果一个数字则在前面添加0
const getNum = (num: number) => {
  return num < 10 ? "0" + num : num;
};

onMounted(() => {
  getDate();
  onInit();
});
</script>

<style scoped>
.root {
  display: flex;
  height: 100vh;
  width: 100%;
}

.left-container {
  width: 250px !important;
  background: #343844;
  min-width: 250px !important;
}

.rigth-container {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

.title-container {
  background: #343844;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-right: solid 1px white;
}

.title-text {
  margin-left: 10px;
  font-family: "幼圆";
  font-size: 20px;
  font-weight: 300;
  color: white;
}

.el-menu-container {
  height: calc(100vh - 150px);
}

.el-sub-menu .is-active {
  background: #fcbc02;
  /* border-radius: 0 80px 80px 0; */
  color: #532f00;
}

/* .el-menu-container :deep() .is-active > ul > .is-active {
  margin-right: 40px;
} */

.head-container {
  height: 60px;
  display: flex;
  width: 100%;
  align-items: center;
  background: white;
  border: solid 1px #e8e8e8;
}

.menu-name {
  font-size: 16px;
  color: #000000;
  margin-left: 20px;
}

.head-user-container {
  position: absolute;
  right: 20px;
  font-size: 14px;
  font-weight: 300;
  color: #000000;
  margin-left: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.head-user-close {
  width: 25px;
  height: 25px;
  margin-left: 10px;
  cursor: pointer;
}

.page {
  padding: 10px;
  height: calc(100vh - 80px);
  overflow-y: auto;
  background: #f0f2f5;
}

.router-container {
  background-color: white;
  padding: 20px;
}
/* 在全局样式文件中 */
.el-menu-item {
  height: 40px;
  line-height: 40px; /* 确保文本垂直居中 */
}
</style>

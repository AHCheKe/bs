import Error from "@/views/error.vue";
import Login from "@/views/login.vue";
import Home from "@/views/home.vue";
import { getToken } from "@/utils/cache";
import { createWebHistory, createRouter, RouteRecordRaw } from "vue-router";
import NProgress from "nprogress";
import "nprogress/nprogress.css";

/**
 * 框架基础路由
 */
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: Home,
    redirect: "/service/list",
    meta: { title: "主页" },
    children: [
      {
        path: "user/list",
        name: "sysUser", // 用户信息
        component: () => import("@/views/system/user/index.vue")
      },{
        path: "user/assistant",
        name: "assistant", // 陪诊师管理
        component: () => import("@/views/system/user/assistant.vue")
      },{
        path: "user/assistant/validate",
        name: "assistantValidate", // 陪诊师审核管理
        component: () => import("@/views/system/user/assistantValidate.vue")
      },{
        path: "user/assistant/tag/validate",
        name: "assistantTagValidate", // 陪诊师标签审核管理
        component: () => import("@/views/system/user/assistantTagsValidate.vue")
      }, {
        path: "service/list",
        name: "serviceList", // 陪诊需求列表
        component: () => import("@/views/service/index.vue")
      }, {
        path: "service/type/list",
        name: "serviceTypeList", // 陪诊类别列表
        component: () => import("@/views/service/serviceType.vue")
      }, {
        path: "service/hospital/list",
        name: "hospitalList", // 医院列表
        component: () => import("@/views/service/hospitalList.vue")
      }, {
        path: "service/tag/list",
        name: "tagList", // 标签管理
        component: () => import("@/views/service/tag.vue")
      },{
        path: "service/order/list",
        name: "orderList", // 陪诊订单列表
        component: () => import("@/views/service/order/index.vue")
      },
      {
        path: "sys/config/list",
        name: "configManage", // 系统参数管理
        component: () => import("@/views/system/config/index.vue")
      },{
        path: "sys/profile",
        name: "adminProfile", // 管理员个人信息
        component: () => import("@/views/system/user/admin.vue")
      },{
        path: "content/config/slide",
        name: "slideManage", // 小程序轮播图管理
        component: () => import("@/views/system/slidePic/index.vue")
      },{
        path: "content/config/notice",
        name: "contentList", // 通知公告管理
        component: () => import("@/views/system/notice/list.vue")
      },{
        path: "sys/service/message",
        name: "serviceMessage", // 在线客服
        component: () => import("@/views/system/ServiceMessage.vue")
      }
    ]
  },
  {
    path: "/login",
    component: Login,
    meta: { title: "登录" }
  },
  {
    path: "/error",
    name: "error",
    component: Error,
    meta: { title: "错误页面" }
  },
  {
    path: "/:path(.*)*",
    redirect: { path: "/error", query: { to: 404 }, replace: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  }
});

// 设置路由守卫
router.beforeEach((to, from, next) => {
  // 执行进度条开始加载
  NProgress.start();
  const token = getToken();
  if (to.path === "/login") {
    if (token) next({ path: "/" });
  } else {
    if (!token) next({ path: "/login" });
  }
  next();
});

// 路由跳转后钩子函数中 - 执行进度条加载结束
router.afterEach(() => {
  NProgress.done();
});

export default router;

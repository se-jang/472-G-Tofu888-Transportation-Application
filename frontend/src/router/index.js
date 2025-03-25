import { createRouter, createWebHistory } from "vue-router";
import store from "@/stores/store.js";
import LoginView from "@/views/LoginView.vue";
import UnauthorizedView from "@/views/UnauthorizedView.vue";
import NotFound from "@/views/NotFound.vue";
import MainViews from "@/views/MainView.vue";
import OrderView from "@/views/OrderView.vue";
import OrderDetailView from "@/views/OrderDetailView.vue";
import CreateUserView from "@/views/CreateUserView.vue";
import WorkerListView from "@/views/WorkerListView.vue";
import WorkerDetailView from "@/views/WorkerDetailView.vue";
import AddOrderView from "@/views/AddOrderView.vue";
import UserListView from "@/views/UserListView.vue";
import UserDetailView from "@/views/UserDetailView.vue";
import CreateOrderView from "@/views/CreateOrderView.vue";
import FailView from "@/views/FailView.vue";
import SuccessView from "@/views/SuccessView.vue";
import ReceiptView from "@/views/ReceiptView.vue";
import EditProfileView from "@/views/EditProfileView.vue";
import EditOrderView from "@/views/EditOrderView.vue";

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: { requiresAuth: false },
  },
  {
    path: "/unauthorized",
    name: "unauthorized",
    component: UnauthorizedView,
    meta: { requiresAuth: true },
  },
  {
    path: "/main",
    name: "main",
    component: MainViews,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN", "USER", "WORKER"] },
  },
  {
    path: "/orders/:id?",
    name: "orders",
    component: OrderView,
    props: true,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN", "USER", "WORKER"] },
  },
  {
    path: "/orders/order-detail/:orderId",
    name: "order-detail",
    component: OrderDetailView,
    props: true,
    meta: { requiresAuth: false, allowedRoles: ["ADMIN", "USER", "WORKER"] },
  },
  {
    path: "/create-user",
    name: "createUser",
    component: CreateUserView,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN"] },
  },
  {
    path: "/worker-list",
    name: "worker-list",
    component: WorkerListView,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN"] },
  },
  {
    path: "/worker-detail/:workerId",
    name: "worker-detail",
    component: WorkerDetailView,
    props: true,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN"] },
  },
  {
    path: "/worker/worker-detail/:workerId/add-order",
    name: "add-order",
    component: AddOrderView,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN"] },
  },
  {
    path: "/user-list",
    name: "user-list",
    component: UserListView,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN"] },
  },
  {
    path: "/user-detail/:userId",
    name: "user-detail",
    component: UserDetailView,
    props: true,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN"] },
  },
  {
    path: "/create-order",
    name: "create-order",
    component: CreateOrderView,
    meta: { requiresAuth: true, allowedRoles: ["USER"] },
  },
  {
    path: "/:pathMatch(.*)*",
    component: NotFound,
  },
  {
    path: "/payment/success",
    name: "success",
    component: SuccessView,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN", "USER", "WORKER"] },
  },
  {
    path: "/receipt/:id",
    name: "receipt",
    component: ReceiptView,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN", "USER"] },
  },
  {
    path: "/payment/fail",
    name: "fail",
    component: FailView,
    meta: { requiresAuth: true, allowedRoles: ["ADMIN", "USER", "WORKER"] },
  },
  {
    path: "/payment/history",
    name: "history",
    component: () => import("@/views/HistoryView.vue"),
    meta: { requiresAuth: true, allowedRoles: ["ADMIN", "USER", "WORKER"] },
  },
  {
    path: "/edit-profile",
    name: "edit-profile",
    component: () => import("@/views/EditProfileView.vue"),
    meta: { requiresAuth: true, allowedRoles: ["ADMIN", "USER", "WORKER"] },
  },
  {
    path: "/orders/edit-order/:orderId",
    name: "edit-order",
    component: () => import("@/views/EditOrderView.vue"),
    meta: { requiresAuth: true, allowedRoles: ["USER"] },
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("jwt");
  const userRole = store.getters.userRole;

  if (to.meta.requiresAuth && !token) {
    next({ path: "/login" });
  } else if (to.meta.allowedRoles && !to.meta.allowedRoles.includes(userRole)) {
    next({ path: "/unauthorized" });
  } else {
    next();
  }
});

export default router;

<template>
  <header class="top-bar">
    <!-- 左侧 Logo 和标题 -->
    <div class="left">
      <img src="@/assets/logo.png" alt="Logo" class="logo" />
      <div>
        <span class="title">三花比价——寻找全网低价好物</span>
      </div>
    </div>

    <!-- 右侧 用户登录状态 -->
    <div class="right">
      <div v-if="isLoggedIn" class="user-info">
        <span class="username">欢迎您, {{ username }}</span>
        <button @click="logout" class="logout-button">退出登录</button>
      </div>
      <div v-else>
        <span>未登录</span>
        <button @click="redirectToLogin" class="login-button">登录</button>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

// 获取用户登录状态和用户名
const userStore = useUserStore();
const isLoggedIn = computed(() => userStore.isLoggedIn);
const username = computed(() => userStore.getUserId());

const router = useRouter();

// 退出登录
const logout = () => {
  userStore.logout();
};

// 跳转到登录页面
const redirectToLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.top-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #FF7243;
  padding: 10px 20px;
  color: white;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left {
  display: flex;
  align-items: center;
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.title {
  font-size: 16px;
  font-weight: bold;
}

.right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.username {
  margin-right: 10px;
  font-size: 16px;
}

.logout-button,
.login-button {
  background-color: white;
  color: #FF7243;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
  margin-left: 10px;
}

.logout-button:hover,
.login-button:hover {
  background-color: #ffe6e1;
}
</style>

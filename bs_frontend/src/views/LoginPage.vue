<template>
  <!-- 顶栏 -->
  <TopBar />

  <!-- 登录框 -->
  <div class="login-container">
    <div class="form-container">
      <h2 class="title">用户登录</h2>

      <form @submit.prevent="handleLogin" class="form">
        <!-- 邮箱 -->
        <div class="form-item">
          <label for="email">邮箱</label>
          <input
              type="email"
              id="email"
              v-model="form.email"
              placeholder="请输入邮箱"
              required
          />
        </div>

        <!-- 密码 -->
        <div class="form-item">
          <label for="password">密码</label>
          <input
              type="password"
              id="password"
              v-model="form.password"
              placeholder="请输入密码"
              required
          />
        </div>

        <!-- 按钮容器 -->
        <div class="button-container">
          <button type="submit" class="auth-btn login-btn">登录</button>
          <button type="button" @click="goToRegister" class="auth-btn register-btn">注册</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import TopBar from '@/components/TopBar.vue';

// 定义表单数据
const form = ref({
  email: '',
  password: ''
});

// 获取 router
const router = useRouter();

// 模拟登录请求
const handleLogin = async () => {
  if (form.value.email && form.value.password) {
    console.log('登录请求:', form.value);

    // 获取当前用户输入的 email 和 password
    const email = form.value.email;
    const password = form.value.password;
    console.log('Email:', email);
    console.log('Password:', password);

    // 发送登录请求到后端
    try {
      const response = await fetch('http://localhost:8000/api/users/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: email,
          password: password,
        }),
      });

      const data = await response.text();
      console.log('后端返回:', data);

      if (response.ok) {
        // 如果登录成功，跳转到首页
        router.push('/');
      } else {
        // 如果登录失败，弹出提示
        alert('登录失败，请检查邮箱和密码');
      }
    } catch (error) {
      console.error('登录请求出错:', error);
      alert('请求失败，请稍后再试');
    }
  } else {
    alert('请输入有效的邮箱和密码');
  }
};

// 跳转到注册页
const goToRegister = () => {
  router.push('/register');
};
</script>





<style scoped>
/* 顶栏样式 */
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

/* 登录框样式 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  padding-top: 60px; /* 顶栏下移 */
}

.form-container {
  background-color: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-item {
  margin-bottom: 15px;
}

label {
  font-size: 14px;
  color: #333;
}

input {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.button-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.auth-btn {
  padding: 10px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  width: 48%;
}

.login-btn {
  background-color: #FF7243;
  color: white;
}

.login-btn:hover {
  background-color: #e55b2e;
}

.register-btn {
  background-color: #FF7243;
  color: white;
}

.register-btn:hover {
  background-color: #e55b2e;
}
</style>

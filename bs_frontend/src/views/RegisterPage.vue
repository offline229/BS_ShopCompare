<template>
  <div class="register-page">
    <!-- 顶栏 -->
    <TopBar />

    <!-- 注册表单容器 -->
    <div class="form-container">
      <h2 class="title">注册账户</h2>

      <form @submit.prevent="handleSubmit" class="form">
        <!-- 用户名 -->
        <div class="form-item">
          <label for="username">用户名</label>
          <input
              type="text"
              id="username"
              v-model="form.username"
              placeholder="请输入用户名"
              required
          />
        </div>

        <!-- 新密码 -->
        <div class="form-item">
          <label for="password">新密码</label>
          <input
              type="password"
              id="password"
              v-model="form.password"
              placeholder="请输入新密码"
              required
          />
        </div>

        <!-- 重复密码 -->
        <div class="form-item">
          <label for="confirmPassword">重复密码</label>
          <input
              type="password"
              id="confirmPassword"
              v-model="form.confirmPassword"
              placeholder="请确认密码"
              required
          />
        </div>

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

        <!-- 验证码 -->
        <div class="form-item">
          <label for="captcha">验证码</label>
          <div class="captcha-container">
            <input
                type="text"
                id="captcha"
                v-model="form.captcha"
                placeholder="请输入验证码"
                required
            />
            <button type="button" class="send-captcha" @click="sendCaptcha">
              发送验证码
            </button>
          </div>
        </div>

        <!-- 确认注册按钮 -->
        <button type="submit" class="submit-btn">确认注册</button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import TopBar from '@/components/TopBar.vue';

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  captcha: ''
});

const router = useRouter();

// 错误提示
const usernameError = ref(false);
const passwordError = ref(false);
const confirmPasswordError = ref(false);

// 用户名和密码校验
const validateForm = () => {
  usernameError.value = form.value.username.length < 6;
  passwordError.value = form.value.password.length < 6;
  confirmPasswordError.value = form.value.password !== form.value.confirmPassword;

  return !(usernameError.value || passwordError.value || confirmPasswordError.value);
};

// 发送验证码
const sendCaptcha = () => {
  // 校验邮箱格式
  if (!form.value.email) {
    alert('请输入有效的邮箱');
    return;
  }

  // 请求后端发送验证码
  console.log('验证码请求发送至邮箱:', form.value.email);
  // 这里可以加入实际的后端请求逻辑，发送验证码到用户邮箱
  // 例如：axios.post('/api/send-captcha', { email: form.value.email });
  alert('验证码已发送到邮箱');
};

// 处理表单提交
const handleSubmit = () => {
  if (!validateForm()) {
    alert('请修正表单中的错误');
    return;
  }

  // 模拟注册请求
  console.log('注册请求数据:', form.value);

  // 假设注册成功，跳转到首页
  const isSuccess = true; // 模拟成功
  if (isSuccess) {
    alert('注册成功');
    router.push('/');  // 注册成功，跳转到首页
  } else {
    alert('注册失败，请稍后再试');
  }
};
</script>

<style scoped>
.register-page {
  background-color: #eeeeee;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.form-container {
  background-color: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
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

.captcha-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.send-captcha {
  background-color: #ff7243;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.send-captcha:hover {
  background-color: #e55b2e;
}

.submit-btn {
  background-color: #ff7243;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
}

.submit-btn:hover {
  background-color: #e55b2e;
}
</style>

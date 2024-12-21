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
import axios from 'axios';

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  captcha: ''
});

const router = useRouter();

// 错误提示
const usernameError = ref('');
const passwordError = ref('');
const confirmPasswordError = ref('');
const captchaError = ref('');

// 用户名和密码校验
const validateForm = () => {
  // 清空错误信息
  usernameError.value = '';
  passwordError.value = '';
  confirmPasswordError.value = '';
  captchaError.value = '';

  // 用户名校验
  if (form.value.username.length < 6) {
    usernameError.value = '用户名必须至少6个字符';
  }

  // 密码校验
  if (form.value.password.length < 6) {
    passwordError.value = '密码必须至少6个字符';
  }

  // 确认密码校验
  if (form.value.password !== form.value.confirmPassword) {
    confirmPasswordError.value = '密码和确认密码不一致';
  }

  // 返回是否有错误
  return !(usernameError.value || passwordError.value || confirmPasswordError.value);
};

// 发送验证码
const sendCaptcha = async () => {
  // 校验邮箱格式
  if (!form.value.email) {
    alert('请输入有效的邮箱');
    return;
  }

  try {
    // 发送请求到后端，传递邮箱地址
    const response = await axios.post('/api/users/send-captcha', {
      email: form.value.email,
      username: form.value.username
    });

    if (response.status === 200) {  // 200 OK 表示请求成功
      alert('验证码已发送到邮箱');
    } else {
      // 其他情况，使用返回的 message 显示错误信息
      alert('验证码发送失败：' + response.data);
    }
  } catch (error) {
    if (error.response) {
      // 如果后端返回了响应数据，且状态码为400等
      alert('请求失败: ' + error.response.data);  // 显示后端返回的错误信息
    } else if (error.request) {
      // 如果请求没有响应，表示没有收到服务器的回应
      alert('请求失败: 网络错误');
    } else {
      // 其他错误
      alert('请求失败: ' + error.message);
    }
    console.error(error);
  }
};

// 处理表单提交
const handleSubmit = async () => {
  // 校验表单
  if (!validateForm()) {
    // 逐个输出具体错误信息
    if (usernameError.value) {
      alert(usernameError.value);  // 提示用户名错误
      return;
    }
    if (passwordError.value) {
      alert(passwordError.value);  // 提示密码错误
      return;
    }
    if (confirmPasswordError.value) {
      alert(confirmPasswordError.value);  // 提示确认密码错误
      return;
    }
    if (!form.value.captcha) {
      alert('请输入验证码');
      return;
    }
  }

  try {
    const response = await axios.post('/api/users/register', {
      email: form.value.email,
      username: form.value.username,
      password: form.value.password,
      captcha: form.value.captcha,
    });

    alert(response.data);  // 如果成功，显示 "注册成功"
    router.push('/');  // 跳转到登录页面或首页
  } catch (error) {
    alert('注册失败: ' + error.response.data);
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

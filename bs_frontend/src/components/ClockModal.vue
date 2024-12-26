<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <h2>定时消息</h2>
      <input v-model="scheduledMessage" type="text" placeholder="请输入定时消息" />
      <input v-model="scheduledTime" type="datetime-local" />
      <button @click="scheduleMessage">确定</button>
      <button @click="closeModal">关闭</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';

const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
  onClose: {
    type: Function,
    required: true,
  },
});

// 定时消息数据
const scheduledMessage = ref('');
const scheduledTime = ref('');

// 定时消息处理
const scheduleMessage = async () => {
  try {
    await axios.post('/api/schedule-message', {
      userId: 123, // 示例用户 ID，实际情况根据需求传递
      message: scheduledMessage.value,
      time: scheduledTime.value,
    });
    alert('定时消息设置成功');
    closeModal();
  } catch (error) {
    console.error('定时消息请求失败:', error);
  }
};

// 关闭弹窗
const closeModal = () => {
  props.onClose(); // 调用父组件的关闭函数
};
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 60%;
  max-width: 600px;
  padding: 30px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.modal-content h2 {
  margin-bottom: 20px;
}

.modal-content input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-content button {
  padding: 10px 20px;
  background-color: #FF7243;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

.modal-content button:hover {
  background-color: #e66237;
}
</style>

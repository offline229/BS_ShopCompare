<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <h2>提醒消息</h2>
      <p>您确定要对此商品特别关注吗，设置后，将定时查询，如有降价，会给您的邮箱发送通知。</p>
      <div class="button-container">
        <button @click="setAlarm">确定</button>
        <button @click="closeModal">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/userStore';  // 引入Pinia UserStore
import { useProductStore} from "@/stores/productStore.ts";
import axios from 'axios';

// 通过Pinia store获取用户ID
const userStore = useUserStore();
const userId = computed(() => userStore.userId); // 假设在UserStore中有userId
const productStore = useProductStore();
const currentProductId = computed(() => productStore.currentProductId);
// Props传递show的状态
const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
  closeChartModal: {
    type: Function,
    required: false,
  },
});

// 设置提醒的函数
const setAlarm = async () => {
  try {
    if (userId.value) {
      // 向后端发送请求，设置商品的定时提醒
      await axios.post('/api/users/set_alarm', {
        username: userId.value,  // 使用computed从Pinia中获取的userId
        productId: currentProductId.value,
      });

      // alert('定时提醒设置成功');
      closeModal();  // 关闭弹窗
    } else {
      alert('用户ID无效');
    }
  } catch (error) {
    console.error('设置定时提醒失败:', error);
  }
};
const emit = defineEmits(['close']);
// 关闭弹窗
const closeModal = () => {
  emit('close');  // 触发父组件传递的 close 事件
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
  font-weight: bold;  /* 设置加粗 */
  color: black;       /* 设置为黑色 */
}

.modal-content p {
  margin-bottom: 20px;
  text-align: center;
  font-size: 16px;
  color: #555;
}

.modal-content button {
  padding: 10px 20px;
  background-color: #FF7243;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
  width: 150px;  /* 设置按钮的宽度 */
}

.modal-content button:hover {
  background-color: #e66237;
}

.modal-content .button-container {
  display: flex;
  justify-content: space-between;  /* 按钮之间水平间隔 */
  width: 100%;
}

.modal-content .button-container button {
  width: 48%;  /* 按钮宽度设置为父容器的 48%，保证并列且有间距 */
}
</style>

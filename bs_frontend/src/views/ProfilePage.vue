<template>
  <div class="profile-page">
    <!-- 顶栏组件 -->
    <TopBar />

    <!-- 个人主页内容 -->
    <div class="content">
      <div class="profile-header">
        <h2>降价提醒列</h2>
      </div>

      <!-- 提醒记录表格 -->
      <div class="alerts-table">
        <table>
          <thead>
          <tr>
            <th>商品名</th>
            <th>提醒价格</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="alert in alerts" :key="alert.id">
            <td>{{ alert.product.name }}</td> <!-- 显示产品名称 -->
            <td>￥{{ alert.priceAlert.priceThreshold }}</td> <!-- 显示提醒价格 -->
            <td>
              <button @click="cancelAlert(alert.priceAlert.id)" class="cancel-alert-button">取消提醒</button>
            </td>
          </tr>
          </tbody>
        </table>

        <!-- 分页 -->
        <div class="pagination">
          <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
          <span>第 {{ currentPage }} 页</span>
          <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';
import TopBar from '@/components/TopBar.vue';
import axios from 'axios';

const userStore = useUserStore();
const router = useRouter();

const alerts = ref<any[]>([]); // 提醒记录数据
const currentPage = ref(1); // 当前页
const totalPages = ref(1); // 总页数

// 获取当前用户ID
const userId = userStore.getUserId();

// 获取提醒记录
const fetchAlerts = async () => {
  try {
    const response = await axios.post('/api/users/personal_alert', {
      username: userId //Id实际上是名字
    });
    console.log(response.data);
    if (response.data) {
      alerts.value = response.data; // 设置提醒记录
    } else {
      alerts.value = ([]);
      console.error("没有找到提醒记录");
    }
  } catch (error) {
    alerts.value = ([]);
  }
};

// 取消提醒
const cancelAlert = async (alertId: number) => {
  try {
    console.log("check"+alertId);
    const response = await axios.delete(`/api/users/delete_alert`, {
      data: { id: alertId  }
    });


    fetchAlerts();

  } catch (error) {
    console.error("删除提醒失败", error);
  }
};

// 分页功能
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    fetchAlerts();
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    fetchAlerts();
  }
};

// 页面加载时获取提醒记录
onMounted(() => {
  fetchAlerts();
});
</script>

<style scoped>
.profile-page {
  margin-top: 60px;
  padding: 20px;
}

.profile-header {
  text-align: center;
  margin-bottom: 20px;
}

.profile-header h2 {
  font-size: 28px;
  font-weight: bold;
  color: #333; /* 标题颜色 */
}

.alerts-table {
  margin-top: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  width: 800px; /* 设置表格最大宽度 */
  margin-left: auto; /* 居中 */
  margin-right: auto; /* 居中 */
}

.alerts-table table {
  width: 100%;
  border-collapse: collapse;
}

.alerts-table th, .alerts-table td {
  padding: 12px;
  text-align: left;
  color: #333; /* 字体颜色为黑色 */
}

.alerts-table th {
  background-color: #f4f4f4; /* 表头背景色 */
  font-weight: bold;
}

.cancel-alert-button {
  background-color: #FF7243;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.cancel-alert-button:hover {
  background-color: #e65c3e;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination span {
  color: #333; /* 设置分页的文本颜色为黑色 */
  font-size: 16px;
  margin: 0 30px;
}

.pagination button {
  background-color: #FF7243;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
  margin: 0 10px;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed; /* 禁用按钮时，禁用鼠标点击 */
}
</style>


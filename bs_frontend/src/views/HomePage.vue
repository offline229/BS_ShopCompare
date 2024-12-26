<template>
  <main class="homepage">
    <!-- 顶栏组件 -->
    <TopBar />

    <div class="content">
      <!-- 侧栏组件 -->
      <Sidebar />

      <!-- 右侧主内容区域 -->
      <div class="main-content">
        <!-- 搜索框区域，在右上角 -->
        <div class="search-bar-container">
          <SearchBar />
        </div>

        <!-- 商品展示区域，在右下角 -->
        <div class="shop-display-container">
          <ShopDisplay />
        </div>

        <!-- 图表展示按钮 -->
        <button @click="handleDetail">查看图表</button>

        <!-- 定时消息弹窗 -->
        <div v-if="productStore.showClockModal" class="modal">
          <div class="modal-content">
            <h2>定时消息</h2>
            <input v-model="scheduledMessage" type="text" placeholder="请输入定时消息" />
            <input v-model="scheduledTime" type="datetime-local" />
            <button @click="scheduleMessage">确定</button>
            <button @click="closeClockModal">关闭</button>
          </div>
        </div>

        <!-- 图表展示弹窗 -->
        <div v-if="productStore.showChartModal" class="modal">
          <div class="modal-content" >
            <h2>价格走势图</h2>
            <!-- 图表容器 -->
            <div ref="chartContainer" style="width: 300px;height: 300px"></div>
            <button @click="closeChartModal">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, watch } from 'vue';
import { useProductStore } from '@/stores/productStore';
import TopBar from '@/components/TopBar.vue';
import Sidebar from '@/components/Sidebar.vue';
import SearchBar from '@/components/SearchBar.vue';
import ShopDisplay from '@/components/ShopDisplay.vue';
import axios from 'axios';
import * as echarts from 'echarts';

// 使用 Pinia store
const productStore = useProductStore();

// 定时消息数据
const scheduledMessage = ref('');
const scheduledTime = ref('');

// 通过 ref 引用 chartContainer
const chartContainer = ref<HTMLDivElement | null>(null);
let chartInstance: echarts.ECharts | null = null; // 保存图表实例

// 定时消息处理
const scheduleMessage = async () => {
  try {
    await axios.post('/api/schedule-message', {
      userId: 123,  // 示例用户 ID，实际情况根据需求传递
      message: scheduledMessage.value,
      time: scheduledTime.value,
    });
    alert('定时消息设置成功');
    closeClockModal();
  } catch (error) {
    console.error('定时消息请求失败:', error);
  }
};

// 关闭定时消息弹窗
const closeClockModal = () => {
  productStore.updateShowClockModal(false);
};

// 关闭图表展示弹窗
const closeChartModal = () => {
  productStore.updateShowChartModal(false);
  if (chartInstance) {
    chartInstance.dispose(); // 销毁图表实例，防止内存泄漏
    chartInstance = null; // 清空实例
  }
};

// 获取图表数据
// 图表初始化方法
const initChart = () => {
  const mockData = [
    { date: '2023-01-01', price: 300 },
    { date: '2023-02-01', price: 290 },
    { date: '2023-03-01', price: 280 },
    { date: '2023-04-01', price: 270 },
    { date: '2023-05-01', price: 260 },
  ];

  if (chartContainer.value) {
    if (chartInstance) {
      chartInstance.dispose();  // 销毁旧的图表实例，防止内存泄漏
    }

    chartInstance = echarts.init(chartContainer.value); // 初始化图表实例
    const option = {
      title: { text: '商品价格走势图' },
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: mockData.map(item => item.date) },
      yAxis: { type: 'value', name: '价格' },
      series: [{ data: mockData.map(item => item.price), type: 'line' }],
    };
    chartInstance.setOption(option); // 设置图表选项
  }
};

// handleDetail 方法
const handleDetail = async (data) => {
  productStore.updateShowChartModal(true); // 打开弹窗

  // 等待 DOM 更新后初始化图表
  await nextTick(() => {
    initChart(); // 封装的图表初始化方法
  });
};

// 监听弹窗状态变化，每次弹窗打开时重新初始化图表
watch(
    () => productStore.showChartModal,
    (newVal) => {
      if (newVal) {
        // 弹窗打开时重新初始化图表
        nextTick(() => {
          initChart();
        });
      }
    }
);

onMounted(() => {
  nextTick(() => {
    initChart();
  });
});
</script>





<style scoped>
.homepage {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 确保整个页面的高度占满 */
}

.content {
  display: flex;
  flex-grow: 1;
  margin-top: 60px; /* 为了避免与 TopBar 重叠，留出顶部空间 */
  width: 100%;
}

.sidebar {
  width: 250px;
  background-color: #fff;
  padding: 20px;
  border-right: 1px solid #ddd;
  position: fixed;
  top: 60px; /* 侧栏下方留出与顶栏的间隔 */
  bottom: 0;
}

.main-content {
  margin-left: 250px; /* 留出与侧栏的间距 */
  flex-grow: 1;
  padding: 20px;
  background-color: #f4f4f4;
  overflow: auto;
}

.search-bar-container {
  margin-top: 20px;
}

.shop-display-container {
  margin-top: 20px;
  flex-grow: 1;
}

/* 定义弹窗的通用样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 确保弹窗在最上层 */
  padding: 20px;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 60%; /* 设置弹窗宽度 */
  max-width: 600px; /* 最大宽度 */
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

/* 顶栏样式 */
.top-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #FF7243;
  color: white;
  padding: 10px;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

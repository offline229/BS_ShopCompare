<template>
  <div class="product-card">
    <!-- 商品图片 -->
    <img :src="productImageUrl" alt="Product Image" class="product-image" />

    <div class="product-details">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-latestPrice">￥{{ product.latestPrice }}</p>

      <div class="product-meta">
        <!-- 平台信息和按钮同一行 -->
        <p class="product-platform">{{ product.platform }}</p>

        <!-- 时钟按钮 -->
        <button class="icon-button" @click="showClockModal = true">
          <img src="@/assets/clock.png" alt="Clock Icon" />
        </button>

        <!-- 图表按钮 -->
        <button class="icon-button" @click="fetchChartData">
          <img src="@/assets/chart.png" alt="Chart Icon" />
        </button>
      </div>
    </div>

    <!-- 弹窗：定时消息输入 -->
    <div v-if="showClockModal" class="modal">
      <div class="modal-content">
        <h2>定时消息</h2>
        <input v-model="scheduledMessage" type="text" placeholder="请输入定时消息" />
        <input v-model="scheduledTime" type="datetime-local" />
        <button @click="scheduleMessage">确定</button>
        <button @click="showClockModal = false">关闭</button>
      </div>
    </div>

    <!-- 图表展示弹窗 -->
    <div v-if="showChartModal" class="modal">
      <div class="modal-content">
        <h2>价格走势图</h2>
        <div ref="chartContainer" style="height: 300px;"></div>
        <button @click="showChartModal = false">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, watch } from 'vue';
import { useUserStore } from '@/stores/userStore';
import axios from 'axios';
import * as echarts from 'echarts'; // 引入 echarts

const userStore = useUserStore();

// 接收商品数据作为 prop
const props = defineProps({
  product: {
    type: Object,
    required: true,
    default: () => ({
      id: null,              // 商品ID
      name: '蓝牙音响',       // 商品名称
      category: '音响设备',    // 商品分类
      latestPrice: 299,      // 商品价格
      platform: '苏宁易购',  // 商品平台
      img: null,           // 商品图片，可能是 Blob 或 null
      shopUrl: '',           // 商品的购买链接
    }),
  },
});

// 控制弹窗显示
const showClockModal = ref(false);
const showChartModal = ref(false);

// 定时消息相关数据
const scheduledMessage = ref('');
const scheduledTime = ref('');

// 商品图片的 Blob 数据 URL
const productImageUrl = ref<string | null>(null);
const defaultImage = '@/assets/seperate.png';  // 默认图片路径

// 监听商品数据变化，重新处理图片
watch(() => props.product, (newProduct) => {
  if (newProduct.img) {
    const byteCharacters = atob(newProduct.img);  // 解码 Base64 字符串
    const byteArrays = [];
    for (let offset = 0; offset < byteCharacters.length; offset += 512) {
      const slice = byteCharacters.slice(offset, offset + 512);
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }
    const blob = new Blob(byteArrays, { type: 'image/jpeg' });
    productImageUrl.value = URL.createObjectURL(blob);
  } else {
    productImageUrl.value = defaultImage;
  }
}, { immediate: true });

// 定时消息处理
const scheduleMessage = async () => {
  try {
    await axios.post('/api/schedule-message', {
      userId: userStore.userId,    // 从 userStore 中获取用户 ID
      message: scheduledMessage.value,
      time: scheduledTime.value,
    });
    alert('定时消息设置成功');
    showClockModal.value = false;  // 关闭弹窗
  } catch (error) {
    console.error('定时消息请求失败:', error);
  }
};

// 获取图表数据
const fetchChartData = async () => {
  try {
    showChartModal.value = true;  // 显示图表弹窗
    const productId = props.product.id;
    const mockData = [
      { date: '2023-01-01', price: 300 },
      { date: '2023-02-01', price: 290 },
      { date: '2023-03-01', price: 280 },
      { date: '2023-04-01', price: 270 },
      { date: '2023-05-01', price: 260 },
    ];

    // 创建 ECharts 实例
    const chart = echarts.init($refs.chartContainer);

    const option = {
      title: { text: '商品价格走势图' },
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: mockData.map(item => item.date) },
      yAxis: { type: 'value', name: '价格' },
      series: [{ data: mockData.map(item => item.price), type: 'line' }],
    };
    chart.setOption(option);
  } catch (error) {
    console.error('图表数据获取失败:', error);
  }
};
</script>

<style scoped>
.product-card {
  width: 250px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin: 10px;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-details {
  padding: 10px;
}

.product-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.product-latestPrice {
  font-size: 16px;
  color: #ff7243;
  margin: 5px 0;
}

.product-platform {
  font-size: 14px;
  color: #666;
  margin-right: auto;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
}

.icon-button img {
  width: 20px;  /* 调整按钮图标大小 */
  height: 20px;
}

/* 弹窗整体样式 */
.modal {
  position: fixed;  /* 使弹窗固定在视口上 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;  /* 确保弹窗在最上层 */
}

/* 弹窗内容 */
.modal-content {
  position: relative;  /* 使弹窗内容根据父级居中 */
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  width: 80%;
  height: 70%;
  max-width: 500px;  /* 设置最大宽度 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);  /* 添加阴影 */
}
.modal-content input {
  margin: 10px 0;
  width: 100%;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.modal-content button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal-content button:hover {
  background-color: #0056b3;
}
</style>

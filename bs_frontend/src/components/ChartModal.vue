<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <h2>价格走势图</h2>
      <div ref="chartContainer" style="width: 300px; height: 300px"></div>
      <button @click="closeModal">关闭</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, watch, computed } from 'vue';
import * as echarts from 'echarts';
import { useProductStore } from '@/stores/productStore';
import axios from "axios";  // 引入 Pinia store
import dayjs from 'dayjs';  // 引入 dayjs 用于格式化日期


// 使用 Pinia store
const productStore = useProductStore();

// Props传递show的状态
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

// 通过 ref 引用 chartContainer
const chartContainer = ref<HTMLDivElement | null>(null);
let chartInstance: echarts.ECharts | null = null; // 保存图表实例

// 获取当前商品 ID
const currentProductId = computed(() => productStore.currentProductId);

// 图表初始化方法
const initChart = () => {
  if (chartContainer.value) {
    if (chartInstance) {
      chartInstance.dispose();  // 销毁旧的图表实例，防止内存泄漏
    }

    chartInstance = echarts.init(chartContainer.value); // 初始化图表实例
    // @ts-nocheck
    const option = {
      title: { text: '商品价格走势图' },
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category' as 'category', data: [] as string[] },  // 数据初始化为空
      yAxis: { type: 'value', name: '价格' },
      series: [{ data: [] as number[], type: 'line' as 'line' }],  // 数据初始化为空
    };
    chartInstance.setOption(option); // 设置图表选项
  }
};

const emit = defineEmits(['close']);
// 关闭弹窗
const closeModal = () => {
  emit('close');  // 触发父组件传递的 close 事件
};

// 监听 show 的变化，当弹窗打开时重新初始化图表
watch(
    () => props.show,
    (newVal) => {
      if (newVal) {
        nextTick(() => {
          initChart(); // 确保 DOM 渲染后初始化图表
          fetchChartData();  // 新增：调用函数获取图表数据
        });
      }
    }
);

// 通过商品 ID 向后端请求数据
const fetchChartData = async () => {
  try {
    if (currentProductId.value) {
      const response = await axios.post('/api/products/history_price', {
        productId: currentProductId.value,  // 将商品 ID 放在请求体中
      });

      // 打印返回的图表数据
      console.log('图表数据:', response.data);

      // 获取图表数据并更新图表
      const chartData = response.data.historyPrices.map((item: any) => ({
        date: dayjs(item.createdAt).format('YYYY-MM-DD'), // 使用 dayjs 格式化日期
        price: item.price,
      }));

      // 将返回的数据传递给图表
      updateChartData(chartData);
    }
  } catch (error) {
    console.error('请求图表数据失败:', error);
  }
};

// 更新图表数据的方法
const updateChartData = (data: { date: string, price: number }[]) => {
  if (chartInstance) {
    const option = chartInstance.getOption(); // 获取当前的图表配置

    // 更新 xAxis.data 和 series.data
    // @ts-ignore
    option.xAxis[0].data = data.map(item => item.date);  // data.map 生成字符串数组
    // @ts-ignore
    option.series[0].data = data.map(item => item.price);

    chartInstance.setOption(option); // 更新图表
  }
};

onMounted(() => {
  nextTick(() => {
    if (props.show) {
      initChart(); // 如果页面加载时弹窗已经显示，则初始化图表
      fetchChartData(); // 请求图表数据
    }
  });
});
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

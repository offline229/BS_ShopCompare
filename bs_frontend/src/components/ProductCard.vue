<template>
  <div class="product-card">
    <!-- 商品图片 -->
    <a :href="product.shopUrl" target="_blank">
      <img :src="productImageUrl" alt="Product Image" class="product-image" />
    </a>
    <p class="product-link-text">点击跳转到对应商品页</p>
    <div class="product-details">
      <h3 class="product-name" :title="product.name">{{ product.name }}</h3>
      <p class="product-latestPrice">￥{{ product.latestPrice }}</p>

      <div class="product-meta">
        <!-- 平台信息和按钮同一行 -->
        <span class="product-platform">{{ product.platform }}</span> |
        <span class="product-category">{{ product.category }}</span>

        <!-- 时钟按钮 -->
        <button class="icon-button" @click="openClockModal">
          <img src="@/assets/clock.png" alt="Clock Icon" />
        </button>

        <!-- 图表按钮 -->
        <button class="icon-button" @click="openChartModal">
          <img src="@/assets/chart.png" alt="Chart Icon" />
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, watch } from 'vue';
import { useProductStore } from '@/stores/productStore';
import axios from 'axios';

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

// 使用 Pinia store
const productStore = useProductStore();

// 打开定时消息弹窗
const openClockModal = () => {
  // 设置当前商品 ID
  productStore.updateCurrentProductId(props.product.id);
  // 显示定时消息弹窗
  productStore.updateShowClockModal(true);

  // 输出调试信息

};

// 打开图表展示弹窗
const openChartModal = () => {
  // 设置当前商品 ID
  productStore.updateCurrentProductId(props.product.id);
  // 显示图表展示弹窗
  productStore.updateShowChartModal(true);

  // 输出调试信息
  console.log('打开图表展示弹窗 - 当前商品 ID:', props.product.id);
  console.log('图表展示弹窗显示状态:', productStore.showChartModal);
};

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
.product-link-text {
  font-size: 12px;         /* 设置小字大小 */
  color: #888888;          /* 设置淡灰色 */
  margin-top: 5px;         /* 与图片之间的间距 */
  text-align: center;      /* 居中显示 */
}


.product-details {
  padding: 10px;
}

.product-name {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin: 0;

  /* 允许换行 */
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;  /* 设置显示2行 */
  overflow: hidden;
  text-overflow: ellipsis;

  width: 100%; /* 占满父容器的宽度 */
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
.product-category {
  font-size: 14px;
  color: #666;
  margin-right: auto;
  transform: translateX(-10px);
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

/* 禁用链接的默认样式 */
a {
  text-decoration: none;
  outline: none;
  background: none;
}



</style>

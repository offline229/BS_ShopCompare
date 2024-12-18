<template>
  <div class="shop-display">
    <!-- 顶栏 -->
    <div class="header">
      <h2 class="title">商品展示</h2>
      <img src="@/assets/seperate.png" alt="logo" class="logo" />
    </div>

    <!-- 商品列表 -->
    <div class="product-list">
      <ProductCard v-for="(product, index) in products" :key="index" :product="product" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import ProductCard from '@/components/ProductCard.vue';

// 默认商品数据
const defaultProducts = [
  { image: '@/assets/seperate.png', name: '商品A', price: 100, platform: '平台A', sales: 500 },
  { image: '@/assets/seperate.png', name: '商品B', price: 150, platform: '平台B', sales: 300 },
  { image: '@/assets/seperate.png', name: '商品C', price: 200, platform: '平台C', sales: 150 },
  { image: '@/assets/seperate.png', name: '商品D', price: 50, platform: '平台D', sales: 800 },
];

const products = ref(defaultProducts); // 商品数据

// 向后端请求数据的函数
const fetchProductData = async () => {
  try {
    // 模拟请求商品展示数据 API（替换为实际的后端接口）
    const response = await axios.get('/api/products');

    // 如果后端返回数据，使用后端数据更新商品列表
    if (response.data && response.data.products) {
      products.value = response.data.products;
    } else {
      console.error('未找到商品数据');
    }
  } catch (error) {
    console.error('请求失败:', error);
    // 如果请求失败，保持默认商品数据
    products.value = defaultProducts;
  }
};

// 组件挂载时调用
onMounted(() => {
  fetchProductData(); // 加载商品数据
});
</script>

<style scoped>
.shop-display {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ff7243; /* 橙色背景 */
  padding: 10px;
}

.title {
  color: white;
  font-size: 24px;
}

.logo {
  width: 40px;
  height: 40px;
  object-fit: cover;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  margin-top: 20px;
}
</style>

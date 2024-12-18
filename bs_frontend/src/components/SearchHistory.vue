<template>
  <div class="search-history">
    <!-- 顶栏 -->
    <div class="header">
      <h2 class="title">检索记录</h2>
      <img src="@/assets/seperate.png" alt="logo" class="logo" />
    </div>

    <!-- 筛选和排序操作 -->
    <div class="filters">
      <button @click="handleSort('sales')" class="filter-btn">按销量排序</button>
      <button @click="handleSort('price')" class="filter-btn">按价格排序</button>
      <div class="price-range">
        <input v-model="priceMin" type="number" placeholder="最低价格" class="filter-input" />
        <input v-model="priceMax" type="number" placeholder="最高价格" class="filter-input" />
        <button @click="handlePriceRangeFilter" class="filter-btn">筛选价格</button>
      </div>
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

// 筛选条件
const priceMin = ref(0);
const priceMax = ref(1000);

// 向后端请求数据的函数
const fetchProductHistory = async () => {
  try {
    // 模拟请求用户历史记录 API（替换为实际的后端接口）
    const response = await axios.get('/api/products', {
      params: {
        priceMin: priceMin.value,
        priceMax: priceMax.value,
      },
    });

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

// 排序功能
const handleSort = (sortBy: string) => {
  // 按照销量或价格排序
  if (sortBy === 'sales') {
    products.value.sort((a, b) => b.sales - a.sales);
  } else if (sortBy === 'price') {
    products.value.sort((a, b) => a.price - b.price);
  }
  // 如果有后端请求需求，重新发起请求
  fetchProductHistory();
};

// 根据价格范围筛选
const handlePriceRangeFilter = () => {
  // 进行价格区间筛选
  fetchProductHistory();
};

// 组件挂载时调用
onMounted(() => {
  fetchProductHistory(); // 加载商品数据
});
</script>

<style scoped>
.search-history {
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

.filters {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.filter-btn {
  background-color: #ff7243;
  color: white;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 4px;
}

.filter-input {
  padding: 5px;
  margin-right: 10px;
  width: 100px;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  margin-top: 20px;
}
</style>

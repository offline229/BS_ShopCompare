<template>
  <div class="shop-display">
    <!-- 顶栏 -->
    <div class="header">
      <h2 class="title">商品展示</h2>
      <img src="@/assets/seperate.png" alt="logo" class="logo" />
    </div>

    <!-- 商品列表 -->
    <div class="product-list">
      <ProductCard v-for="(product, index) in products" :key="product.id" :product="product" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import ProductCard from '@/components/ProductCard.vue';

// 默认商品数据
const defaultProducts = [
  {
    id: 1,
    name: "智能手表",
    category: "电子产品",
    latestPrice: 399,
    platform: "京东",
    img: null,  // 示例图片链接
    shopUrl: "https://jd.com/product/1"
  },
  {
    id: 2,
    name: "无线耳机",
    category: "音响设备",
    latestPrice: 199,
    platform: "淘宝",
    img: null,  // 示例图片链接
    shopUrl: "https://taobao.com/product/2"
  },
  {
    id: 3,
    name: "游戏鼠标",
    category: "电脑配件",
    latestPrice: 149,
    platform: "天猫",
    img: null,  // 示例图片链接
    shopUrl: "https://tmall.com/product/3"
  },
  {
    id: 4,
    name: "蓝牙音响",
    category: "音响设备",
    latestPrice: 299,
    platform: "苏宁易购",
    img: null,  // 示例图片链接
    shopUrl: "https://suning.com/product/4"
  },
];

const products = ref(defaultProducts); // 商品数据

// 向后端请求数据的函数
const fetchProductData = async () => {
  try {
    console.log("发送请求到后端");

    // 向后端请求数据，假设后端接口是 `/api/products/latest`
    const response = await axios.get('/api/products/latest');

    // 查看后端返回的整个响应数据
    /*
    console.log("response", response); // 这里会显示整个响应对象，包含 headers, data, status 等信息
    console.log("response.data", response.data); // 查看返回的数据部分
    // response.data 已经是商品数据的数组
    console.log("商品数据数组:", response.data); // 打印商品数据

     */

    // 直接将 response.data 赋值给 products
    products.value = response.data;
    console.log("接收到的商品数据:", products.value); // 打印赋值后的 products
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



.product-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  margin-top: 20px;
}
.logo {
  width: 150px;   /* 设置图片宽度 */
  height: auto;   /* 高度自适应 */
  object-fit: contain; /* 确保图片不被裁剪 */
}

</style>

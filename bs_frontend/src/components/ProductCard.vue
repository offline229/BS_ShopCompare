<template>
  <div class="product-card">
    <img :src="product.image" alt="Product Image" class="product-image" />
    <div class="product-details">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-price">￥{{ product.price }}</p>
      <p class="product-platform">平台：{{ product.platform }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';

// 接收商品数据作为 prop
const props = defineProps({
  product: {
    type: Object,
    required: true,
    default: () => ({
      image: '@/assets/seperate.png', // 默认商品图片
      name: '商品A',  // 默认商品名称
      price: 99.99,  // 默认商品价格
      platform: '平台A',  // 默认平台
    }),
  },
});
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

.product-price {
  font-size: 16px;
  color: #ff7243;
  margin: 5px 0;
}

.product-platform {
  font-size: 14px;
  color: #666;
}
</style>

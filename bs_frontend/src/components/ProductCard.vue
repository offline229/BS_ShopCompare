<template>
  <div class="product-card">
    <!-- 显示商品图片 -->
    <img v-if="productImageUrl" :src="productImageUrl" alt="Product Image" class="product-image" />
    <!-- 默认图片 -->
    <img v-else :src="defaultImage" alt="Product Image" class="product-image" />

    <div class="product-details">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-latestPrice">￥{{ product.latestPrice }}</p>
      <p class="product-platform">平台：{{ product.platform }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, onMounted } from 'vue';

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
      image: null,           // 商品图片，建议使用图片链接或者 Blob 数据
      shopUrl: '',           // 商品的购买链接
    }),
  },
});

// 存储图片的 Blob 数据的 URL
const productImageUrl = ref<string | null>(null);

// 默认图片（如果没有图片或加载失败时使用）
const defaultImage = '@/assets/seperate.png';

// 当商品数据变化时处理图片
onMounted(() => {
  if (props.product.image) {
    // 如果是 base64 编码的图片
    if (props.product.image.startsWith('data:image')) {
      productImageUrl.value = props.product.image; // 直接使用 base64 字符串
    } else if (props.product.image instanceof Blob) {
      console.log("receive blob");
      productImageUrl.value = URL.createObjectURL(props.product.image); // 处理 Blob 数据
    }
  } else {
    // 如果没有图片，使用默认图片
    productImageUrl.value = defaultImage;
  }
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

.product-latestPrice {
  font-size: 16px;
  color: #ff7243;
  margin: 5px 0;
}

.product-platform {
  font-size: 14px;
  color: #666;
}
</style>

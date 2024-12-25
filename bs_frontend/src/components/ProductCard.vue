<template>
  <div class="product-card">
    <!-- 直接绑定图片的 src -->
    <img :src="productImageUrl" alt="Product Image" class="product-image" />
    <div class="product-details">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-latestPrice">￥{{ product.latestPrice }}</p>
      <p class="product-platform">平台：{{ product.platform }}</p>
    </div>

  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, onMounted, watch } from 'vue';

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

// 存储图片的 Blob 数据的 URL
const productImageUrl = ref<string | null>(null);

// 默认图片路径
const defaultImage = '@/assets/seperate.png';  // 如果没有图片，使用默认图片路径

// 监听商品数据变化，重新处理图片
watch(() => props.product, (newProduct) => {
  // console.log("Product data updated:", newProduct);

  if (newProduct.img) {
    // console.log("Received image data:", newProduct.img);

    // 将收到的 img 字符串直接转换为 Blob 对象
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

    const blob = new Blob(byteArrays, { type: 'image/jpeg' }); // 假设图片格式为 'image/jpeg'
    productImageUrl.value = URL.createObjectURL(blob); // 使用 Blob 创建一个 URL 来展示
    // console.log("Converted image URL:", productImageUrl.value);

  } else {
    // console.log("No product img found, using default img.");
    productImageUrl.value = defaultImage;  // 如果没有图片，使用默认图片
  }

}, { immediate: true });  // immediate: true 确保初始时也会执行一次

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

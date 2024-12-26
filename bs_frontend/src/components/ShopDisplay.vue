<template>
  <div class="shop-display">
    <!-- 顶栏 -->
    <div class="header">
      <h2 class="title">商品库存 共有 {{ totalCount }} 个</h2>
      <img src="@/assets/seperate.png" alt="logo" class="logo" />
    </div>
    <!-- 商品列表 -->
    <div class="product-list">
      <ProductCard v-for="(product, index) in products" :key="product.id" :product="product" />
    </div>

    <!-- 分页按钮 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>

      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>

    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import ProductCard from '@/components/ProductCard.vue';
import { useProductStore } from '@/stores/productStore';
import { computed } from 'vue';

// 获取 Pinia store
const productStore = useProductStore();

// 通过 computed 直接获取 store 中的值
const currentPage = computed(() => productStore.currentPage);
const itemsPerPage = computed(() => productStore.itemsPerPage);


// 分页相关的状态
const totalPages = ref(1);    // 总页数
const totalCount = ref(0);    // 总商品数量
const products = ref([]); // 商品数据

// 向后端请求数据的函数（支持分页）
const fetchProductData = async () => {
  try {
    console.log("发送请求到后端");
    console.log("itemsPerPage: ", itemsPerPage);
    console.log("currentPage: ", currentPage);
    // 向后端请求数据，带上分页参数
    const response = await axios.post('/api/products/shop_display', {
      page: currentPage.value,
      limit: itemsPerPage.value
    });

    // 假设后端返回的数据结构类似：
    // { products: [...], totalCount: 100 }
    const productsData = response.data.products;  // 获取商品列表
    const totalItems = response.data.totalCount;  // 获取总商品数量

    // 更新商品数据和总条目数
    products.value = productsData;
    totalCount.value = totalItems;

    // 计算总页数
    totalPages.value = Math.ceil(totalItems / itemsPerPage.value);

    // 输出调试信息
    console.log("Products: ", products.value);
    console.log("Total count: ", totalCount.value);
    console.log("Total pages: ", totalPages.value);
  } catch (error) {
    console.error('请求失败:', error);
    // 如果请求失败，保持默认商品数据
    products.value = [];
    totalCount.value = 0;
    totalPages.value = 1;
  }
};

// 切换页面的函数
const changePage = (page: number) => {
  if (page < 1 || page > totalPages.value) return; // 防止无效页码

  // 更新页码（确保传递 limit 参数）
  productStore.updatePagination(page, itemsPerPage.value);

  // 获取新的页码的数据
  fetchProductData();
};

// 组件挂载时调用
onMounted(() => {
  fetchProductData(); // 加载第一页商品数据
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
  border-radius: 10px;
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

/* 分页按钮 */
/* 分页按钮 */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  font-size: 16px;
  color: #3b3b3b;
}

/* 单个分页按钮样式 */
.pagination button {
  padding: 10px 20px;  /* 按钮内边距 */
  margin: 0 5px;       /* 按钮间距 */
  cursor: pointer;     /* 鼠标悬停时显示手指样式 */
  border: none;        /* 去除边框 */
  background-color: #ff7243; /* 按钮背景色 */
  color: white;        /* 字体颜色为白色 */
  border-radius: 20px; /* 按钮倒角，圆角效果 */
  font-size: 16px;     /* 字体大小 */
  transition: background-color 0.3s; /* 背景色过渡效果 */
}

/* 鼠标悬停时改变背景色 */
.pagination button:hover {
  background-color: #e65c2f; /* 悬停时按钮背景颜色 */
}

/* 禁用按钮的样式 */
.pagination button:disabled {
  background-color: #ccc; /* 禁用时按钮背景色 */
  cursor: not-allowed;    /* 禁用时鼠标不可点击 */
  color: #777;            /* 禁用时文字颜色 */
}
.pagination span {
  display: inline-block;
  vertical-align: middle;  /* 调整span垂直对齐方式 */
  line-height: 1.5;         /* 使得span内容向下对齐 */
  margin-top: 7px;          /* 可以适当调整top间距，增加垂直偏移量 */
}

</style>

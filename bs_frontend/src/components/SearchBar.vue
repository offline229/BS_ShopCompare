<template>
  <div class="search-bar">
    <input
        v-model="searchQuery"
        type="text"
        placeholder="请输入商品名称"
        class="search-input"
        @input="onInput"
    />
    <button @click="search" class="search-button">搜索</button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useProductStore } from '@/stores/productStore';
import { useRouter } from 'vue-router';
import axios from 'axios';

// 获取 Pinia store
const productStore = useProductStore();

// 响应式的 searchQuery，从 Pinia store 中获取
const searchQuery = computed({
  get: () => productStore.searchQuery,
  set: (value: string) => productStore.updateSearchQuery(value),
});

// 路由实例，用于路由跳转（如果需要）
const router = useRouter();

// 搜索操作
const search = async () => {
  if (searchQuery.value.trim()) {
    console.log('搜索内容:', searchQuery.value);

    // 获取当前页码和每页数量
    const currentPage = productStore.currentPageValue;
    const itemsPerPage = productStore.itemsPerPageValue;
    console.log("check "+ currentPage+" "+itemsPerPage+"value")

    try {
      // 发送带有搜索参数、页码和每页数量的 API 请求
      const response = await axios.post('/api/products/search', {
        keyword: searchQuery.value,
        page: currentPage,
        limit: itemsPerPage,
      });

      // 根据响应处理数据（如商品列表）
      const { products, totalCount } = response.data;

      console.log('商品列表:', products);
      console.log('商品总数:', totalCount);

      // 跳转到搜索结果页面
      router.push('/search');

    } catch (error) {
      console.error('搜索失败:', error);
    }
  } else {
    alert('请输入有效的商品名称');
  }
};

// 输入框内容变化时的操作
const onInput = () => {
  console.log('输入的内容:', searchQuery.value);  // 可以做一些实时搜索或验证等
};
</script>


<style scoped>
.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  background-color: #f8f8f8;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.search-input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
}

.search-button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #FF7243;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #e75d3f;
}
</style>

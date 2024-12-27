<template>
  <div class="search-bar">
    <input
        v-model="localSearchQuery"
        type="text"
        placeholder="请输入商品名称，若使用爬虫请稍作等待"
        class="search-input"
        @input="onInput"
    />
    <button @click="search" class="search-button">搜索</button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useProductStore } from '@/stores/productStore';
import { useRouter } from 'vue-router';
import axios from 'axios';

// 获取 Pinia store
const productStore = useProductStore();

// 本地的 searchQuery
const localSearchQuery = ref(productStore.searchQuery);

// 路由实例，用于路由跳转（如果需要）
const router = useRouter();

// 搜索操作
const search = async () => {
  if (localSearchQuery.value.trim()) {
    console.log('搜索内容:', localSearchQuery.value);

    // 将本地的搜索内容同步到 Pinia store 中
    productStore.updateSearchQuery(localSearchQuery.value);

    // 获取当前页码和每页数量
    const currentPage = productStore.currentPageValue;

    const itemsPerPage = productStore.itemsPerPageValue;
    console.log("check " + currentPage + " " + itemsPerPage + "value");
    productStore.updatePagination(1,itemsPerPage);
  } else {
    productStore.updateSearchQuery(null);
  }
};

// 输入框内容变化时的操作
const onInput = () => {
  console.log('输入的内容:', localSearchQuery.value);  // 仅更新本地的查询内容
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
  white-space: nowrap;
  /* 使用 flex 排列图标和文字 */
  display: flex;
  align-items: center;  /* 垂直居中对齐 */
  justify-content: center; /* 水平居中对齐 */
}

.search-button img {
  margin-right: 8px;  /* 如果有图标，文字和图标之间添加一些间距 */
}


.search-button:hover {
  background-color: #e75d3f;
}
</style>

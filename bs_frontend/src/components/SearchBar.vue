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
import { ref } from 'vue';

const searchQuery = ref('');  // 绑定输入框的值

// 搜索操作
const search = () => {
  if (searchQuery.value.trim()) {
    console.log('搜索内容:', searchQuery.value);  // 这里你可以调用搜索API，或路由跳转等
    // 例如：router.push({ name: 'searchResult', query: { q: searchQuery.value } });
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

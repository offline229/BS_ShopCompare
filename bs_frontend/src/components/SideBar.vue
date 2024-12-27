<template>
  <aside class="sidebar">
    <!-- 价格区间 -->
    <div class="category">
      <h3 class="category-title">价格筛选</h3>
      <div class="price-filter">
        <input type="number" v-model="localPriceMin" placeholder="最低价格" />
        <input type="number" v-model="localPriceMax" placeholder="最高价格" />
      </div>
    </div>

    <!-- 价格排序 -->
    <div class="category">
      <h3 class="category-title">价格排序</h3>
      <button @click="togglePriceSort">{{ localPriceSort === 'asc' ? '升序' : '降序' }}</button>
    </div>

    <!-- 平台选择 -->
    <div class="category">
      <h3 class="category-title">平台选择</h3>
      <select v-model="localPlatform">
        <option value="苏宁易购">苏宁易购</option>
        <option value="尚兴">尚兴</option>
        <!-- 可以根据需求继续添加其他平台 -->
      </select>
    </div>

    <!-- 确定筛选按钮 -->
    <div class="category">
      <button @click="applyFilters">确定筛选</button>
    </div>

    <!-- Debug信息显示 -->
    <div class="debug-info">
      <h3 class="category-title">Debug 信息</h3>
      <pre>{{ debugInfo }}</pre>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useProductStore } from '@/stores/productStore';

const productStore = useProductStore();

// 本地状态变量
const localPriceMin = ref<number | null>(null);
const localPriceMax = ref<number | null>(null);
const localPriceSort = ref<'asc' | 'desc'>('asc'); // 默认升序
const localPlatform = ref<string>('苏宁易购');

// Debug信息
const debugInfo = computed(() => {
  return {
    searchQuery: productStore.searchQuery,
    currentPage: productStore.currentPage,
    itemsPerPage: productStore.itemsPerPage,
    priceMin: productStore.priceMin,
    priceMax: productStore.priceMax,
    platform: productStore.platform,
    priceSort: productStore.priceSort,
  };
});

// 切换价格排序
const togglePriceSort = () => {
  localPriceSort.value = localPriceSort.value === 'asc' ? 'desc' : 'asc';
};

// 更新筛选条件到 store
const applyFilters = () => {
  // 将本地变量更新到 Pinia store 中
  if (localPriceMin.value !== null) productStore.updatePriceMin(localPriceMin.value);
  if (localPriceMax.value !== null) productStore.updatePriceMax(localPriceMax.value);
  productStore.updatePriceSort(localPriceSort.value);
  productStore.updatePlatform(localPlatform.value);

  // 你可以根据需要，也可以更新搜索查询（如果需要更新）
  // productStore.updateSearchQuery(newSearchQuery);

  // 如果需要更新分页或其他筛选项，可以在这里进行
  // productStore.updateCurrentPage(1);  // 默认从第一页开始
};
</script>


<style scoped>
.sidebar {
  width: 250px;
  background-color: white;
  padding: 15px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  height: 100vh;
  position: fixed;
  top: 60px;
  left: 0;
}

.category {
  margin-bottom: 20px;
}

.category-title {
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  color: #333;
}

.subcategory-list {
  list-style: none;
  padding-left: 20px;
  font-size: 16px;
  color: #666;
}

.subcategory-list li {
  padding: 5px 0;
  cursor: pointer;
}

.subcategory-list li:hover {
  background-color: #f0f0f0;
  border-radius: 5px;
}

.price-filter {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.price-filter input {
  padding: 5px;
  font-size: 14px;
  width: 80px;
}

button {
  padding: 5px 10px;
  margin-top: 10px;
  font-size: 14px;
  background-color: #4caf50;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

button:hover {
  background-color: #45a049;
}

select {
  padding: 5px;
  margin-top: 10px;
  width: 100%;
}

.debug-info {
  margin-top: 30px;
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 4px;
}

.debug-info pre {
  font-size: 14px;
  color: #333;
}
</style>

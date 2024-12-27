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
      </select>
    </div>

    <!-- 启用本地搜索模式切换按钮 -->
    <div class="category">
      <button @click="toggleLocalSearch">{{ localSearchButtonText }}</button>
    </div>

    <!-- 确定筛选按钮 -->
    <div class="category">
      <button @click="applyFilters">点击筛选</button>
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

// 切换价格排序
const togglePriceSort = () => {
  localPriceSort.value = localPriceSort.value === 'asc' ? 'desc' : 'asc';
};

// 切换本地搜索模式
const toggleLocalSearch = () => {
  productStore.toggleLocalSearch(!productStore.isLocalSearchEnabled);
};

// 获取本地搜索按钮的文本
const localSearchButtonText = computed(() => {
  return productStore.isLocalSearchEnabled ? '爬虫搜索模式' : '本地搜索模式';
});

// 更新筛选条件到 store
const applyFilters = () => {
  // 将本地变量更新到 Pinia store 中
  if (localPriceMin.value !== null) productStore.updatePriceMin(localPriceMin.value);
  if (localPriceMax.value !== null) productStore.updatePriceMax(localPriceMax.value);
  productStore.updatePriceSort(localPriceSort.value);
  productStore.updatePlatform(localPlatform.value);

  // 如果需要更新分页或其他筛选项，可以在这里进行
  // productStore.updateCurrentPage(1);  // 默认从第一页开始
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  background-color: white;
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  height: 100vh;
  position: fixed;
  top: 60px;
  left: 0;
  border-radius: 8px;
}

.category {
  margin-bottom: 20px;
}

.category-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.price-filter {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.price-filter input {
  padding: 8px;
  font-size: 14px;
  width: 90px;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #f7f7f7;
}

button {
  padding: 8px 15px;
  margin-top: 10px;
  font-size: 14px;
  background-color: #FF7243;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 6px;
  width: 100%;
}

button:hover {
  background-color: #e85c33;
}

select {
  padding: 8px;
  margin-top: 10px;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #f7f7f7;
  color: #333;
}

select:focus {
  border-color: #FF7243;
  outline: none;
}

select option {
  padding: 8px;
}

.category button {
  width: 100%;
}

@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: auto;
    position: relative;
  }
}
</style>

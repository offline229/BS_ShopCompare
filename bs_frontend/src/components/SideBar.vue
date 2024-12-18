<template>
  <aside class="sidebar">
    <!-- 家具类目 -->
    <div class="category">
      <h3 @click="toggleCategory('furniture')" class="category-title">家具</h3>
      <ul v-show="activeCategory === 'furniture'" class="subcategory-list">
        <li>沙发</li>
        <li>床</li>
        <li>桌子</li>
        <li>椅子</li>
      </ul>
    </div>

    <!-- 食物类目 -->
    <div class="category">
      <h3 @click="toggleCategory('food')" class="category-title">食物</h3>
      <ul v-show="activeCategory === 'food'" class="subcategory-list">
        <li>水果</li>
        <li>蔬菜</li>
        <li>零食</li>
        <li>饮料</li>
      </ul>
    </div>

    <!-- 其他类目 (可以继续添加) -->
    <div class="category">
      <h3 @click="toggleCategory('clothing')" class="category-title">衣物</h3>
      <ul v-show="activeCategory === 'clothing'" class="subcategory-list">
        <li>男装</li>
        <li>女装</li>
        <li>鞋子</li>
      </ul>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// 存储当前选中的类目
const activeCategory = ref<string | null>(null);

// 切换显示子类目的函数
const toggleCategory = (category: string) => {
  if (activeCategory.value === category) {
    activeCategory.value = null;  // 点击相同类目时收起
  } else {
    activeCategory.value = category;  // 点击时展开
  }
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  background-color: white;
  padding: 15px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  height: 100vh;  /* 高度占满整个视窗 */
  position: fixed;
  top: 60px; /* 这里设定 Sidebar 从 TopBar 的底部开始 */
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
</style>

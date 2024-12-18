<template>
  <main class="homepage">
    <!-- 顶栏组件 -->
    <TopBar />

    <div class="content">
      <!-- 侧栏组件 -->
      <Sidebar />

      <!-- 右侧主内容区域 -->
      <div class="main-content">
        <!-- 搜索框区域，在右上角 -->
        <div class="search-bar-container">
          <SearchBar />
        </div>

        <!-- 商品展示区域，在右下角 -->
        <div class="shop-display-container">
          <ShopDisplay />
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import TopBar from '@/components/TopBar.vue';
import Sidebar from '@/components/Sidebar.vue';
import SearchBar from '@/components/SearchBar.vue';
import ShopDisplay from '@/components/ShopDisplay.vue';
</script>

<style scoped>
.homepage {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 确保整个页面的高度占满 */
}

.content {
  display: flex;
  flex-grow: 1;
  margin-top: 60px; /* 为了避免与 TopBar 重叠，留出顶部空间 */
  width: 100%;
}

.sidebar {
  width: 250px;
  background-color: #fff;
  padding: 20px;
  border-right: 1px solid #ddd;
  position: fixed;
  top: 60px; /* 侧栏下方留出与顶栏的间隔 */
  bottom: 0;
  /* 侧栏高度占满剩余屏幕高度 */
}

.main-content {
  margin-left: 250px; /* 留出与侧栏的间距 */
  flex-grow: 1;
  padding: 20px;
  background-color: #f4f4f4;
  overflow: auto;
  /* 设置最大宽度，避免内容过宽 */
}

.search-bar-container {
  margin-top: 20px;
  /* 搜索栏在右上角 */
}

.shop-display-container {
  margin-top: 20px;
  flex-grow: 1;
  /* 商品展示区域 */
}

.search-history-container {
  margin-top: 20px;
  /* 搜索历史区域 */
}

/* 顶栏样式 */
.top-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #FF7243;
  color: white;
  padding: 10px;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

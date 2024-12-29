<template>
  <div class="test-view">
    <!-- 顶栏组件 -->
    <TopBar />

    <div class="content">
      <!-- 侧栏组件 -->
      <Sidebar />

      <!-- 主内容区域 -->
      <div class="main-content">
        <h2>欢迎来到商品比价网站</h2>
        <p>这是一个测试页面，侧栏和顶栏已经被成功集成。</p>


        <!-- 展示商品组件 -->
        <SearchHistory /> <!-- 在这里展示商品组件 -->
      </div>

      <div>
        <SearchBar />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import TopBar from '@/components/TopBar.vue';
import Sidebar from '@/components/SideBar.vue';
import SearchBar from '@/components/SearchBar.vue';
import SearchHistory from "@/components/SearchHistory.vue"; // 引入商品展示组件
</script>

<style scoped>
/* 确保 body 和 html 的宽度和高度都为 100% */
html, body {
  width: 100%;
  height: 100%;
  margin: 0; /* 清除默认的 margin */
}

.test-view {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 确保页面高度占满视窗 */
  width: 100%; /* 确保宽度占满视窗 */
}

.content {
  display: flex;
  flex-grow: 1; /* 主内容区域填满剩余空间 */
  width: 100%; /* 确保 content 区域宽度为 100% */
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  background-color: #f4f4f4;
  overflow: auto; /* 防止内容溢出时出现滚动条 */
}

h2 {
  color: #333;
  font-size: 24px;
}

p {
  font-size: 16px;
  color: #666;
}

/* 如果需要，你可以为 SearchHistory 添加特定样式 */
.shop-display {
  margin-top: 20px;
}
</style>

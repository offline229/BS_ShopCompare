<script setup lang="ts">
import { ref, nextTick, watch } from 'vue';
import { useProductStore } from '@/stores/productStore';
import TopBar from '@/components/TopBar.vue';
import Sidebar from '@/components/SideBar.vue';
import SearchBar from '@/components/SearchBar.vue';
import ShopDisplay from '@/components/ShopDisplay.vue';
import ChartModal from '@/components/ChartModal.vue';  // 引入图表弹窗组件
import ClockModal from '@/components/ClockModal.vue';  // 引入定时消息弹窗组件

const productStore = useProductStore();

// 处理打开图表弹窗
const handleDetail = () => {
  productStore.updateShowChartModal(true);
};

// 监听弹窗状态变化
watch(
    () => productStore.showChartModal,
    (newVal) => {
      if (newVal) {
        nextTick(() => {
          // 可以在这里添加需要的图表初始化逻辑
        });
      }
    }
);

// 处理定时消息弹窗关闭
const closeClockModal = () => {
  productStore.updateShowClockModal(false);
};

// 处理图表弹窗关闭
const closeChartModal = () => {
  productStore.updateShowChartModal(false);
};
</script>

<template>
  <main class="homepage">
    <!-- 顶栏组件 -->
    <TopBar />

    <div class="content">
      <!-- 侧栏组件 -->
      <Sidebar />

      <!-- 右侧主内容区域 -->
      <div class="main-content">
        <div class="search-bar-container">
          <SearchBar />
        </div>

        <div class="shop-display-container">
          <ShopDisplay />
        </div>


        <!-- 使用 ChartModal 组件 -->
        <ChartModal
            :show="productStore.showChartModal"
            @close="closeChartModal"
        />

        <!-- 使用 ClockModal 组件 -->
        <ClockModal
            :show="productStore.showClockModal"
            @close="closeClockModal"
        />
      </div>
    </div>
  </main>
</template>

<style scoped>
.homepage {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 确保整个页面的高度占满 */
}

.content {
  display: flex;
  flex-grow: 1;
  margin-top: 40px; /* 为了避免与 TopBar 重叠，留出顶部空间 */
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
}

.main-content {
  margin-left: 250px; /* 留出与侧栏的间距 */
  flex-grow: 1;
  padding: 20px;
  background-color: #f4f4f4;
  overflow: auto;
}

.search-bar-container {
  margin-top: 20px;
}

.shop-display-container {
  margin-top: 20px;
  flex-grow: 1;
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

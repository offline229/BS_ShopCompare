import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useProductStore = defineStore('product', () => {
    // 定义响应式状态
    const searchQuery = ref('');
    const categoryFilter = ref('');
    const currentPage = ref(1);
    const itemsPerPage = ref(8);
    // 新增状态：当前商品的 ID 和弹窗的显示状态
    const currentProductId = ref<number | null>(null);  // 当前请求商品的 ID
    const showClockModal = ref(false);  // 定时消息弹窗是否可见
    const showChartModal = ref(false);  // 图表展示弹窗是否可见

    // 更新当前商品 ID 的方法
    const updateCurrentProductId = (id: number) => {
        currentProductId.value = id;
    };


    // 更新弹窗可见性的方法
    const updateShowClockModal = (isVisible: boolean) => {
        showClockModal.value = isVisible;
    };

    const updateShowChartModal = (isVisible: boolean) => {
        showChartModal.value = isVisible;
    };

    // 使用 computed 来创建 getter 和 setter（如果需要的话）
    const currentProductIdValue = computed({
        get: () => currentProductId.value,  // 获取当前商品 ID
        set: (value: number | null) => {
            currentProductId.value = value;  // 设置新的商品 ID
        }
    });


    // 更新 searchQuery 的方法
    const updateSearchQuery = (query: string) => {
        searchQuery.value = query;
    };

    // 更新 categoryFilter 的方法
    const updateCategoryFilter = (category: string) => {
        categoryFilter.value = category;
    };

    // 更新分页的方法
    const updatePagination = (page: number, limit: number) => {
        currentPage.value = page;
        itemsPerPage.value = limit;
    };

    // 使用 computed 来创建 getter 和 setter
    const searchQueryValue = computed({
        get: () => searchQuery.value,  // 获取 searchQuery 的值
        set: (value: string) => {
            searchQuery.value = value;  // 设置新的值
        }
    });

    const categoryFilterValue = computed({
        get: () => categoryFilter.value,  // 获取 categoryFilter 的值
        set: (value: string) => {
            categoryFilter.value = value;  // 设置新的值
        }
    });

    const currentPageValue = computed({
        get: () => currentPage.value,  // 获取 currentPage 的值
        set: (value: number) => {
            currentPage.value = value;  // 设置新的值
        }
    });

    const itemsPerPageValue = computed({
        get: () => itemsPerPage.value,  // 获取 itemsPerPage 的值
        set: (value: number) => {
            itemsPerPage.value = value;  // 设置新的值
        }
    });

    // 返回 store 中的值和方法
    return {
        searchQuery,
        categoryFilter,
        currentPage,
        itemsPerPage,
        updateSearchQuery,
        updateCategoryFilter,
        updatePagination,
        searchQueryValue,
        categoryFilterValue,
        currentPageValue,
        itemsPerPageValue,

        // 新增的部分
        currentProductId,
        showClockModal,
        showChartModal,
        updateCurrentProductId,
        updateShowClockModal,
        updateShowChartModal
    };
});

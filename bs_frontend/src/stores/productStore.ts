import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useProductStore = defineStore('product', () => {
    // 定义响应式状态
    const searchQuery = ref('');
    const categoryFilter = ref('');
    const currentPage = ref(1);
    const itemsPerPage = ref(8);

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
        searchQueryValue,  // 返回计算属性
        categoryFilterValue,  // 返回计算属性
        currentPageValue,  // 返回计算属性
        itemsPerPageValue  // 返回计算属性
    };
});

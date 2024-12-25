// store/productStore.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useProductStore = defineStore('product', () => {
    const searchQuery = ref('');  // 搜索框的查询条件
    const categoryFilter = ref('');  // 边栏选择的分类
    const currentPage = ref(1);  // 当前页
    const itemsPerPage = ref(8);  // 每页商品数量

    const updateSearchQuery = (query: string) => {
        searchQuery.value = query;
    };

    const updateCategoryFilter = (category: string) => {
        categoryFilter.value = category;
    };

    const updatePagination = (page: number, limit: number) => {
        currentPage.value = page;
        itemsPerPage.value = limit;
    };

    return {
        searchQuery,
        categoryFilter,
        currentPage,
        itemsPerPage,
        updateSearchQuery,
        updateCategoryFilter,
        updatePagination
    };
});

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useProductStore = defineStore('product', () => {
    // 定义响应式状态
    const searchQuery = ref('');
    const currentPage = ref(1);
    const itemsPerPage = ref(8);

    // 新增状态：当前商品的 ID 和弹窗的显示状态
    const currentProductId = ref<number | null>(null);  // 当前请求商品的 ID
    const showClockModal = ref(false);  // 定时消息弹窗是否可见
    const showChartModal = ref(false);  // 图表展示弹窗是否可见

    // 新增状态：价格范围、平台名、价格排序
    const priceMin = ref<number | null>(null);  // 价格下限
    const priceMax = ref<number | null>(null);  // 价格上限
    const platform = ref<string>('');  // 平台名
    const priceSort = ref<'asc' | 'desc'>('asc');  // 价格排序，默认为升序（'asc'）

    // 新增状态：是否启动本地搜索的 flag
    const isLocalSearchEnabled = ref<boolean>(false);  // 默认为 false，表示不启动本地搜索

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

    // 更新搜索查询的值
    const updateSearchQuery = (query: string) => {
        searchQuery.value = query;
    };

    // 更新分页
    const updatePagination = (page: number, limit: number) => {
        currentPage.value = page;
        itemsPerPage.value = limit;
    };

    // 更新价格下限
    const updatePriceMin = (min: number | null) => {
        priceMin.value = min;
    };

    // 更新价格上限
    const updatePriceMax = (max: number | null) => {
        priceMax.value = max;
    };

    // 更新平台名
    const updatePlatform = (platformName: string) => {
        platform.value = platformName;
    };

    // 更新价格排序
    const updatePriceSort = (sortOrder: 'asc' | 'desc') => {
        priceSort.value = sortOrder;
    };

    // 启动/关闭本地搜索
    const toggleLocalSearch = (isEnabled: boolean) => {
        isLocalSearchEnabled.value = isEnabled;
    };

    // 使用 computed 来创建 getter 和 setter
    const searchQueryValue = computed({
        get: () => searchQuery.value,  // 获取 searchQuery 的值
        set: (value: string) => {
            searchQuery.value = value;  // 设置新的值
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

    // 使用 computed 来创建 getter 和 setter
    const priceMinValue = computed({
        get: () => priceMin.value,  // 获取 priceMin 的值
        set: (value: number | null) => {
            priceMin.value = value;  // 设置新的价格下限
        }
    });

    const priceMaxValue = computed({
        get: () => priceMax.value,  // 获取 priceMax 的值
        set: (value: number | null) => {
            priceMax.value = value;  // 设置新的价格上限
        }
    });

    const platformValue = computed({
        get: () => platform.value,  // 获取 platform 的值
        set: (value: string) => {
            platform.value = value;  // 设置新的平台名
        }
    });

    const priceSortValue = computed({
        get: () => priceSort.value,  // 获取 priceSort 的值
        set: (value: 'asc' | 'desc') => {
            priceSort.value = value;  // 设置新的价格排序
        }
    });

    // 返回 store 中的值和方法
    return {
        searchQuery,
        currentPage,
        itemsPerPage,
        priceMin,
        priceMax,
        platform,
        priceSort,
        isLocalSearchEnabled,  // 返回本地搜索 flag

        // 更新的方法
        updateSearchQuery,
        updatePagination,
        updatePriceMin,
        updatePriceMax,
        updatePlatform,
        updatePriceSort,
        toggleLocalSearch,  // 启动/关闭本地搜索的方法

        // computed getter 和 setter
        searchQueryValue,
        currentPageValue,
        itemsPerPageValue,
        priceMinValue,
        priceMaxValue,
        platformValue,
        priceSortValue,

        // 弹窗控制
        currentProductId,
        showClockModal,
        showChartModal,
        updateCurrentProductId,
        updateShowClockModal,
        updateShowChartModal
    };
});

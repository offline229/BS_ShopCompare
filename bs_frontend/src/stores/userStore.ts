// src/stores/userStore.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
    // 用户 ID 和登录状态的响应式数据
    const userId = ref<string | null>(null);  // 可以存储用户 ID，如果未登录则为 null
    const isLoggedIn = ref<boolean>(false);   // 登录状态，初始值为 false

    // 设置用户信息
    const setUser = (id: string) => {
        userId.value = id;
        isLoggedIn.value = true;
    };

    // 清除用户信息
    const logout = () => {
        userId.value = null;
        isLoggedIn.value = false;
    };

    // 获取用户信息
    const getUserId = () => userId.value;
    const getLoginStatus = () => isLoggedIn.value;

    // 返回 store 中的数据和方法
    return {
        userId,
        isLoggedIn,
        setUser,
        logout,
        getUserId,
        getLoginStatus
    };
});

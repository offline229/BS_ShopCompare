// src/stores/userStore.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
    // 用户 ID 和登录状态的响应式数据
    const userName = ref<string | null>(null);  // 存储用户名或用户 ID
    const isLoggedIn = ref<boolean>(false);   // 登录状态，初始值为 false

    // 设置用户信息
    const setUser = (username: string) => {
        userName.value = username;  // 使用用户名作为用户 ID
        isLoggedIn.value = true;   // 设置为已登录状态
    };

    // 清除用户信息
    const logout = () => {
        userName.value = null;
        isLoggedIn.value = false;
    };

    // 获取用户信息
    const getUserId = () => userName.value;
    const getLoginStatus = () => isLoggedIn.value;

    // 返回 store 中的数据和方法
    return {
        userId: userName,
        isLoggedIn,
        setUser,
        logout,
        getUserId,
        getLoginStatus
    };
});

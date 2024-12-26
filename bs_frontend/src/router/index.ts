import { createRouter, createWebHistory } from 'vue-router';

// 页面导入
import HomePage from '@/views/HomePage.vue';
import RegisterPage from '@/views/RegisterPage.vue';
import PasswordChangePage from '@/views/PasswordChangePage.vue';
import SearchPage from '@/views/SearchPage.vue';
import TestView from  '@/views/TestView.vue'
import LoginPage from '@/views/LoginPage.vue';
import ProfilePage from "@/views/ProfilePage.vue";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
  },
  {
    path: '/password-change',
    name: 'PasswordChange',
    component: PasswordChangePage,
  },
  {
    path: '/search',
    name: 'Search',
    component: SearchPage,
  },
  {
    path: '/test',
    name: 'Test',
    component: TestView,
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfilePage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

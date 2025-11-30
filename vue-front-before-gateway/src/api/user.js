// src/api/user.js
import request from '@/utils/request' // 导入封装好的axios实例

// 用户登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data: data, // 请求体数据
  })
}

// 获取用户信息
export function getInfo() {
  return request({
    url: '/user/user',
    method: 'get',
  })
}

// 更新用户信息
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data: data,
  })
}

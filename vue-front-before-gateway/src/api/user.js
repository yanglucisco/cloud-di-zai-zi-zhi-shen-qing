// src/api/user.js
import request from '@/utils/request' // 导入封装好的axios实例

// 获取用户信息
export function getInfo() {
  // 
  return request({
    url: '/user/current',
    method: 'get',
  })
}
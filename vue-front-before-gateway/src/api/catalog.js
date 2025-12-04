// src/api/user.js
import request from '@/utils/request' // 导入封装好的axios实例

export function getCatalog() {
  return request({
    url: '/catalog/user/user',
    method: 'get',
  })
}

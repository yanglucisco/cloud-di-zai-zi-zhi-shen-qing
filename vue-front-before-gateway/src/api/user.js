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
/**
 * 更新用户密码
 * @param {Object} data - 包含更新密码所需信息的对象
 * @returns {Promise} 返回一个 Promise 对象，解析后包含服务器响应结果
 */
export function updatePassword(password) {
  return request.post('/account/sysUser/updatePassword', {password: password});
}
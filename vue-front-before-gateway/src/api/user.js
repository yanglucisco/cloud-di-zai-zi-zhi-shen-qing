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

/**
 * 更新用户基本信息
 * @param {Object} data - 用户信息对象
 * @returns {Promise} 返回一个 Promise 对象
 */
export function updateUser(data) {
  return request.post('/account/sysUser/update', data);
}

/**
 * 获取用户分页列表
 * @param {Object} params - 查询参数 { page, pageSize, keyword, status, orgId }
 * @returns {Promise} 返回一个 Promise 对象
 */
export function getUserPage(params) {
  return request.get('/account/sysUser/page', { params });
}

/**
 * 新增用户
 * @param {Object} data - 用户信息对象
 * @returns {Promise} 返回一个 Promise 对象
 */
export function addUser(data) {
  return request.post('/account/sysUser/add', data);
}

/**
 * 批量删除用户
 * @param {Array} ids - 用户ID数组
 * @returns {Promise} 返回一个 Promise 对象
 */
export function deleteUserByIds(ids) {
  return request.post('/account/sysUser/delete', { ids });
}

/**
 * 更新用户状态（启用/禁用）
 * @param {Object} data - { id, status }
 * @returns {Promise} 返回一个 Promise 对象
 */
export function updateUserStatus(data) {
  return request.post('/account/sysUser/updateStatus', data);
}
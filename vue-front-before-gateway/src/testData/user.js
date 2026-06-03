// 生成模拟用户数据
function generateUsers() {
  const orgNames = ['技术部', '产品部', '市场部', '人事部', '财务部', '运营部', '研发中心', '销售部']
  const names = ['张三', '李四', '王五', '赵六', '孙七', '周八', '吴九', '郑十',
    '陈小明', '林小红', '黄大伟', '刘美丽', '杨志强', '王芳', '李刚', '赵敏']
  const accounts = ['zhangsan', 'lisi', 'wangwu', 'zhaoliu', 'sunqi', 'zhouba', 'wujiu', 'zhengshi',
    'chenxm', 'linxh', 'huangdw', 'liuml', 'yangzq', 'wangf', 'ligang', 'zhaomin']

  return Array.from({ length: 56 }, (_, i) => {
    const idx = i % names.length
    const gender = i % 3 === 0 ? 'M' : (i % 3 === 1 ? 'F' : '')
    return {
      id: i + 1,
      account: accounts[idx] + (i >= names.length ? (i + 1) : ''),
      name: names[idx] + (i >= names.length ? (Math.floor(i / names.length) + 1) : ''),
      gender,
      phone: `138${String(10000000 + i).slice(0, 8)}`,
      email: `${accounts[idx]}@company.com`,
      avatar: '',
      orgName: orgNames[i % orgNames.length],
      status: i % 5 === 0 ? 'DISABLE' : 'ENABLE',
    }
  })
}

export const allUsers = generateUsers()

/**
 * 获取用户测试分页数据
 */
export function getTestUserPage(params = {}) {
  const { page = 1, pageSize = 10, keyword = '', status, orgId } = params
  let filtered = [...allUsers]

  if (keyword) {
    const kw = keyword.toLowerCase()
    filtered = filtered.filter(u =>
      u.name.includes(kw) || u.account.toLowerCase().includes(kw) || u.phone.includes(kw)
    )
  }
  if (status) {
    filtered = filtered.filter(u => u.status === status)
  }
  if (orgId) {
    // 模拟按机构筛选：用 orgId 数字取模匹配
    filtered = filtered.filter(u => u.id % 8 + 1 === Number(orgId) || u.orgName.includes(String(orgId)))
  }

  const total = filtered.length
  const start = (page - 1) * pageSize
  const records = filtered.slice(start, start + pageSize)

  return { records, total, page, pageSize }
}

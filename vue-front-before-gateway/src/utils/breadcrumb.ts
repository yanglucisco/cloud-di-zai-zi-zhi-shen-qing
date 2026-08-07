/**
 * 根据路径生成面包屑数组
 * @param inputPath 输入路径，如 "/a/b/c"
 * @param pathTitleMap 路径到标题的映射表（模拟数据库查询结果）
 * @returns 面包屑数组
 */
export function generateBreadcrumbs(
  inputPath: string,
  pathTitleMap: Record<string, string>
): Array<{ title: string; path: string }> {
  // 1. 分割路径，过滤空字符串
  const segments = inputPath.split('/').filter(segment => segment !== '')
  
  // 2. 逐级构建路径并查找标题
  const breadcrumbs: Array<{ title: string; path: string }> = []
  
  // 构建当前路径
  let currentPath = ''
  
  for (const segment of segments) {
    // 累加路径
    currentPath += `/${segment}`
    
    // 从映射表中查找标题
    const title = pathTitleMap[currentPath]
    
    // 如果找不到标题，可以跳过或使用默认标题
    if (title) {
      breadcrumbs.push({
        title,
        path: currentPath
      })
    }
  }
  return breadcrumbs
}
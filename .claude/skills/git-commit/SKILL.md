---
name: git-commit
description: 生成git commit message
---

# 生成规范

1、只查看在git中暂存的文件；
2、只生成git commit message，不要执行提交动作，我需要手动复制message然后手动提交
3、message生成规范：

| type     | 含义                             |
|----------|----------------------------------|
| feat     | 新功能（feature）                |
| fix      | 修复 bug                         |
| docs     | 文档变更                         |
| style    | 代码格式（不影响逻辑）           |
| refactor | 重构（既不修 bug 也不加功能）    |
| perf     | 性能优化                         |
| test     | 测试相关                         |
| build    | 构建系统或依赖调整               |
| ci       | CI 配置                          |
| chore    | 其他杂项（如工具配置）           |
---
name: commit
description: Generate a commit message for the current changes
invokable: true
---

# git commit message 生成规范

通过git中暂存的更改文件，帮我生成列一个git commit message，并遵循以下规范：

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

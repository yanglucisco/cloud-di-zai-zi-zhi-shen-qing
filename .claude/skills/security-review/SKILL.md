---
name: security-review
description: 对代码进行深度安全审查，检查常见漏洞和硬编码密钥。
allowed-tools: Read, Grep, Bash
# disable-model-invocation: true  # 如果设为 true，则仅允许手动触发
---

# 代码安全审查专家

## 任务目标
分析提供的代码变更，识别潜在的安全风险。

## 审查清单 (SOP)
请严格按照以下 OWASP Top 10 标准进行检查：
1. **注入攻击**：检查 SQL 拼接、命令注入。
2. **敏感数据**：查找硬编码的密码、API Key、Secret。
3. **身份验证**：检查不安全的鉴权逻辑。
4. **错误处理**：确保没有向用户泄露堆栈信息。

## 输出格式
请按严重程度（高、中、低）列出问题，并给出修复建议代码。
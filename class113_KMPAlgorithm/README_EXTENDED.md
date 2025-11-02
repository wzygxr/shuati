# Class 100: KMP算法与子树匹配 - 扩展题目集合

## 概述

本目录包含KMP算法和子树匹配算法的完整实现，涵盖了来自各大算法平台的题目，包括LeetCode、HackerRank、Codeforces、洛谷、牛客网、SPOJ、USACO、AtCoder等。

## 文件结构

### 核心算法文件
- `Code01_KMP.java/cpp/py` - KMP算法基础实现
- `Code02_SubtreeOfAnotherTree.java/cpp/py` - 子树匹配算法
- `Code03_Oulipo.java/cpp/py` - POJ 3461 Oulipo题目
- `Code04_PowerStrings.java/cpp/py` - POJ 2406 Power Strings题目

### 扩展题目文件
- `Code05_ExtendedKMPProblems.java/cpp/py` - KMP算法扩展题目集合
- `Code06_ExtendedSubtreeProblems.java/cpp/py` - 子树匹配扩展题目集合
- `Code07_AdvancedKMPApplications.java` - KMP算法高级应用

### 文档文件
- `README.md` - 原始说明文档
- `README_EXTENDED.md` - 扩展说明文档（本文件）
- `AdditionalProblems.md` - 附加题目列表
- `ProblemLinks.md` - 题目链接汇总
- `COMPLETION_REPORT.md` - 完成情况报告

## 算法覆盖范围

### KMP算法相关题目
1. **基础KMP实现**
   - 字符串匹配
   - 重叠匹配处理
   - Next数组构建

2. **扩展应用**
   - 多模式匹配（AC自动机）
   - 字符串周期检测
   - 最长回文子串（Manacher算法）
   - 生物信息学应用
   - 文本编辑器实现

3. **平台题目**
   - HackerRank: Knuth-Morris-Pratt Algorithm
   - Codeforces 126B: Password
   - 洛谷 P3375: 【模板】KMP
   - SPOJ: NAJPF - Pattern Find
   - USACO: String Transformation
   - AtCoder: String Algorithms

### 子树匹配相关题目
1. **基础二叉树操作**
   - LeetCode 100: 相同的树
   - LeetCode 101: 对称二叉树
   - LeetCode 104: 二叉树的最大深度
   - LeetCode 110: 平衡二叉树

2. **高级树操作**
   - LeetCode 226: 翻转二叉树
   - LeetCode 543: 二叉树的直径
   - LeetCode 687: 最长同值路径
   - Codeforces: Tree Matching

## 工程化特性

### 代码质量
- **多语言实现**: 每个算法都提供Java、C++、Python三种语言的实现
- **详细注释**: 包含算法原理、复杂度分析、边界条件处理
- **测试用例**: 每个算法都有完整的测试用例
- **异常处理**: 完善的边界条件和错误处理

### 性能优化
- **时间复杂度分析**: 每个算法都有详细的时间复杂度分析
- **空间复杂度分析**: 考虑内存使用效率
- **工程化考量**: 包含性能测试、内存测试等

### 实际应用
- **生物信息学**: DNA序列匹配、模糊匹配
- **文本处理**: 查找替换、模式匹配
- **数据压缩**: 字符串周期检测和压缩
- **系统工具**: 文本编辑器功能实现

## 复杂度分析总结

### KMP算法复杂度
| 操作 | 时间复杂度 | 空间复杂度 | 说明 |
|------|------------|------------|------|
| Next数组构建 | O(m) | O(m) | m为模式串长度 |
| 字符串匹配 | O(n+m) | O(m) | n为文本串长度 |
| AC自动机构建 | O(∑m_i) | O(∑m_i) | 多模式匹配 |
| Manacher算法 | O(n) | O(n) | 最长回文子串 |

### 树算法复杂度
| 操作 | 时间复杂度 | 空间复杂度 | 说明 |
|------|------------|------------|------|
| 树遍历 | O(n) | O(h) | h为树高度 |
| 子树匹配 | O(n*m) | O(h) | 最坏情况 |
| 树直径计算 | O(n) | O(h) | 递归实现 |
| 平衡检查 | O(n) | O(h) | 高度平衡 |

## 使用指南

### 编译运行
```bash
# Java
javac class100/*.java
java class100.Code05_ExtendedKMPProblems

# C++
g++ -std=c++11 class100/Code05_ExtendedKMPProblems.cpp -o kmp_test
./kmp_test

# Python
python class100/Code05_ExtendedKMPProblems.py
```

### 测试验证
每个文件都包含完整的测试用例，可以直接运行验证算法正确性。

## 扩展建议

### 进一步学习方向
1. **算法优化**
   - KMP算法的常数优化
   - 树算法的非递归实现
   - 并行化处理

2. **应用扩展**
   - 正则表达式引擎
   - 基因组序列分析
   - 分布式模式匹配

3. **理论研究**
   - 字符串算法的数学基础
   - 树同构问题的复杂性
   - 模式匹配的极限分析

### 实践项目
1. 实现一个简单的文本搜索引擎
2. 开发DNA序列分析工具
3. 构建代码相似度检测系统
4. 设计高效的日志分析工具

## 贡献说明

本代码库持续更新，欢迎贡献：
- 新的算法题目实现
- 性能优化建议
- 测试用例补充
- 文档改进

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 更新日志

- 2024-01-01: 初始版本发布
- 2024-01-02: 添加扩展题目和工程化实现
- 2024-01-03: 完善测试用例和文档

---

*Algorithm Journey - 让算法学习更系统、更深入*
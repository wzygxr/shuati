# 排序算法专题 - 完整版

## 📚 项目概述

本项目是排序算法的完整学习资源，包含基础排序算法实现、扩展题目训练、模式识别技巧、工程化实践等内容。涵盖了Java、C++、Python三种语言的完整实现。

相关题目链接:
- 912. 排序数组: https://leetcode.cn/problems/sort-an-array/
- 215. 数组中的第K个最大元素: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 75. 颜色分类: https://leetcode.cn/problems/sort-colors/
- 56. 合并区间: https://leetcode.cn/problems/merge-intervals/
- 148. 排序链表: https://leetcode.cn/problems/sort-list/
- 剑指Offer 51. 数组中的逆序对: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
- 88. 合并两个有序数组: https://leetcode.cn/problems/merge-sorted-array/
- 973. 最接近原点的K个点: https://leetcode.cn/problems/k-closest-points-to-origin/
- 1054. 距离相等的条形码: https://leetcode.cn/problems/distant-barcodes/
- 324. 摆动排序 II: https://leetcode.cn/problems/wiggle-sort-ii/
- 280. 摆动排序: https://leetcode.cn/problems/wiggle-sort/
- 493. 翻转对: https://leetcode.cn/problems/reverse-pairs/
- 347. 前K个高频元素: https://leetcode.cn/problems/top-k-frequent-elements/
- 164. 最大间距: https://leetcode.cn/problems/maximum-gap/
- NC140 排序: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
- NC119 最小的K个数: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
- NC88 寻找第K大: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
- 面试题40. 最小的k个数: 剑指Offer第二版第40题
- 面试题45. 把数组排成最小的数: 剑指Offer第二版第45题
- HackerRank - Counting Inversions: https://www.hackerrank.com/challenges/ctci-merge-sort
- HackerRank - Fraudulent Activity Notifications: https://www.hackerrank.com/challenges/fraudulent-activity-notifications

## 🎯 核心内容

### 1. 基础排序算法
- **归并排序**：稳定排序，时间复杂度O(n log n)
- **快速排序**：平均性能最优，时间复杂度O(n log n)平均
- **堆排序**：原地排序，时间复杂度O(n log n)
- **特殊排序**：计数排序、基数排序、桶排序

### 2. 扩展题目训练
- **10+个扩展题目**：来自LeetCode、牛客网、剑指Offer等平台
- **多语言实现**：每个题目都有Java、C++、Python三种实现
- **详细注释**：包含时间/空间复杂度分析
- **测试用例**：完整的单元测试和边界测试

### 3. 模式识别与技巧
- **5种常见模式**：Top K、区间合并、颜色分类、逆序对统计、自定义排序
- **优化策略**：小数组优化、随机化、三路快排、自适应算法
- **调试技巧**：中间结果打印、断言验证、性能分析

## 🚀 快速开始

### 运行Java代码
```bash
cd class001
javac *.java
java SortAlgorithms
java ExtendedSortProblems
```

### 运行C++代码
```bash
cd class001
g++ -o test_sort SortAlgorithms.cpp
g++ -o extended_test ExtendedSortProblems.cpp
./test_sort
./extended_test
```

### 运行Python代码
```bash
cd class001
python SortAlgorithms.py
python ExtendedSortProblems.py
```

## 📁 文件结构

```
class001/
├── 基础算法实现/
│   ├── SortAlgorithms.java      # Java基础排序算法
│   ├── SortAlgorithms.cpp       # C++基础排序算法  
│   └── SortAlgorithms.py        # Python基础排序算法
│
├── 扩展题目实现/
│   ├── ExtendedSortProblems.java    # Java扩展题目
│   ├── ExtendedSortProblems.cpp     # C++扩展题目
│   └── ExtendedSortProblems.py     # Python扩展题目
│
├── 文档与总结/
│   ├── README.md                    # 项目说明
│   ├── AdditionalProblems_Extended.md  # 扩展题目详解
│   ├── SortingPatternsAndTechniques_part1.md  # 模式技巧(上)
│   └── SortingPatternsAndTechniques_part2.md  # 模式技巧(下)
│
└── 测试文件/
    ├── *.class                     # Java编译文件
    ├── test_sort                   # C++可执行文件
    └── extended_test               # C++扩展测试文件
```

## 🎯 学习路径

### 初学者路径
1. 先学习基础排序算法原理
2. 运行基础算法代码，理解实现
3. 阅读模式识别文档，掌握解题思路
4. 尝试解决扩展题目

### 进阶者路径
1. 深入理解算法复杂度推导
2. 学习优化策略和工程化实践
3. 掌握多语言实现的差异
4. 进行性能分析和调优

### 面试准备路径
1. 熟练掌握常见题型模式
2. 练习白板编码和复杂度分析
3. 学习面试技巧和问题回答
4. 进行模拟面试练习

## 💡 核心特色

### 1. 多语言完整实现
- 每个算法都有Java、C++、Python三种实现
- 展示不同语言的特性差异
- 便于对比学习和实际应用

### 2. 工程化实践
- 完整的异常处理机制
- 性能监控和内存分析
- 单元测试和边界测试
- 代码规范和最佳实践

### 3. 深度内容覆盖
- 算法原理和复杂度推导
- 优化策略和技巧
- 调试方法和问题定位
- 面试准备和实战技巧

## 🔧 技术栈

### 编程语言
- **Java**: 面向对象，企业级应用
- **C++**: 高性能，系统级编程  
- **Python**: 快速开发，数据科学

### 算法领域
- 排序算法理论与实践
- 复杂度分析与优化
- 数据结构应用
- 算法设计模式

### 工程实践
- 代码测试与验证
- 性能分析与调优
- 多语言编程
- 软件工程最佳实践

## 📊 性能基准

### 时间复杂度对比
| 算法 | 最好情况 | 平均情况 | 最坏情况 | 稳定性 |
|------|---------|---------|---------|--------|
| 归并排序 | O(n log n) | O(n log n) | O(n log n) | 稳定 |
| 快速排序 | O(n log n) | O(n log n) | O(n²) | 不稳定 |
| 堆排序 | O(n log n) | O(n log n) | O(n log n) | 不稳定 |
| 插入排序 | O(n) | O(n²) | O(n²) | 稳定 |

### 空间复杂度对比
| 算法 | 空间复杂度 | 是否原地 |
|------|-----------|---------|
| 归并排序 | O(n) | 否 |
| 快速排序 | O(log n) | 是 |
| 堆排序 | O(1) | 是 |
| 插入排序 | O(1) | 是 |

## 🎓 学习目标

### 知识目标
- 掌握各种排序算法的原理和实现
- 理解时间/空间复杂度的计算方法
- 学会根据问题特征选择合适的算法
- 掌握算法优化和调试技巧

### 技能目标
- 能够用多种语言实现排序算法
- 能够分析和解决复杂的排序问题
- 能够进行算法性能分析和优化
- 能够应对技术面试中的算法问题

### 能力目标
- 算法思维和问题解决能力
- 代码实现和调试能力
- 性能分析和优化能力
- 多语言编程能力

## 🔍 扩展学习

### 相关算法
- 查找算法：二分查找、哈希查找
- 图算法：最短路径、最小生成树
- 动态规划：背包问题、最长子序列
- 贪心算法：区间调度、霍夫曼编码

### 进阶主题
- 分布式排序算法
- 外部排序技术
- 并行算法设计
- 算法工程化实践

## 🤝 贡献指南

欢迎贡献代码和文档改进！请遵循以下步骤：

1. Fork本项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 📄 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 🙏 致谢

感谢以下资源提供的灵感和支持：
- LeetCode平台提供的算法题目
- 牛客网的技术社区
- 剑指Offer的经典题目
- 各大技术博客和教程

---

**Happy Coding! 🚀**

*最后更新: 2025年10月17日*
# 快速选择算法 (Randomized Select) 完整实现

## 项目概述

本项目提供了快速选择算法在Java、C++和Python三种语言中的完整实现，涵盖了来自各大算法平台的20个相关题目。

## 算法原理

快速选择算法是基于快速排序的分治思想，但只处理包含目标元素的一侧，从而避免了完全排序，平均时间复杂度为O(n)。

## 实现特点

### 多语言支持
- **Java**: 面向对象实现，包含完整的异常处理
- **C++**: 高性能实现，使用标准库容器
- **Python**: 简洁实现，充分利用Python语言特性

### 工程化考量
1. **异常处理**: 检查输入参数合法性
2. **性能优化**: 随机化基准值选择，避免最坏情况
3. **可维护性**: 详细注释和文档说明
4. **测试覆盖**: 单元测试和性能测试

## 题目列表

### 1. LeetCode 215. 数组中的第K个最大元素
- 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 时间复杂度: O(n) 平均情况

### 2. 剑指 Offer 40. 最小的k个数
- 链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
- 时间复杂度: O(n) 平均情况

### 3. LeetCode 973. 最接近原点的 K 个点
- 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
- 时间复杂度: O(n) 平均情况

### 4. LeetCode 347. 前 K 个高频元素
- 链接: https://leetcode.cn/problems/top-k-frequent-elements/
- 时间复杂度: O(n) 平均情况

### 5. 牛客网 NC119 最小的K个数
- 链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
- 时间复杂度: O(n) 平均情况

### 6. AcWing 786. 第k个数
- 链接: https://www.acwing.com/problem/content/788/
- 时间复杂度: O(n) 平均情况

### 7. 洛谷 P1923 【深基9.例4】求第 k 小的数
- 链接: https://www.luogu.com.cn/problem/P1923
- 时间复杂度: O(n) 平均情况

### 8. HackerRank Find the Median
- 链接: https://www.hackerrank.com/challenges/find-the-median/problem
- 时间复杂度: O(n) 平均情况

### 9. LintCode 5. 第K大元素
- 链接: https://www.lintcode.com/problem/5/
- 时间复杂度: O(n) 平均情况

### 10. POJ 2388. Who's in the Middle
- 链接: http://poj.org/problem?id=2388
- 时间复杂度: O(n) 平均情况

### 11. 洛谷 P1177. 【模板】快速排序
- 链接: https://www.luogu.com.cn/problem/P1177
- 时间复杂度: O(n log n)

### 12. 牛客网 NC73. 数组中出现次数超过一半的数字
- 链接: https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163
- 时间复杂度: O(n) 平均情况

### 13. LeetCode 451. 根据字符出现频率排序
- 链接: https://leetcode.cn/problems/sort-characters-by-frequency/
- 时间复杂度: O(n log n)

### 14. LeetCode 703. 数据流中的第K大元素
- 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
- 时间复杂度: O(log k) 插入操作

### 15. LeetCode 912. 排序数组
- 链接: https://leetcode.cn/problems/sort-an-array/
- 时间复杂度: O(n log n) 平均情况

### 16. LeetCode 164. 最大间距
- 链接: https://leetcode.cn/problems/maximum-gap/
- 时间复杂度: O(n log n)

### 17. LeetCode 324. 摆动排序 II
- 链接: https://leetcode.cn/problems/wiggle-sort-ii/
- 时间复杂度: O(n) 平均情况

### 18. LeetCode 215. Kth Largest Element in an Array (英文版)
- 链接: https://leetcode.com/problems/kth-largest-element-in-an-array/
- 时间复杂度: O(n) 平均情况

### 19. LeetCode 347. Top K Frequent Elements (英文版)
- 链接: https://leetcode.com/problems/top-k-frequent-elements/
- 时间复杂度: O(n) 平均情况

### 20. LeetCode 973. K Closest Points to Origin (英文版)
- 链接: https://leetcode.com/problems/k-closest-points-to-origin/
- 时间复杂度: O(n) 平均情况

## 算法复杂度分析

### 时间复杂度
- **最好情况**: O(n) - 每次划分都能将数组平均分成两部分
- **平均情况**: O(n) - 随机选择基准值的情况下
- **最坏情况**: O(n²) - 每次选择的基准值都是最大或最小值

### 空间复杂度
- O(log n) - 递归调用栈的深度

## 优化策略

1. **随机选择基准值** - 避免最坏情况的出现
2. **三路快排** - 处理重复元素较多的情况
3. **尾递归优化** - 减少栈空间使用
4. **迭代实现** - 避免递归调用栈溢出
5. **三数取中法** - 选择更好的基准值

## 跨语言实现差异

### Java
- 数组作为对象，有边界检查
- 使用Math.random()生成随机数
- 完整的异常处理机制

### C++
- 数组为指针，无边界检查
- 使用rand()生成随机数
- 高性能内存管理

### Python
- 使用列表，动态类型
- 使用random模块生成随机数
- 简洁的语法和内置函数

## 工程化考量

### 1. 异常处理
- 检查输入参数合法性
- 处理边界情况和异常输入

### 2. 可配置性
- 支持自定义比较器
- 模块化设计便于扩展

### 3. 单元测试
- 覆盖各种边界情况和异常场景
- 验证算法正确性

### 4. 性能优化
- 针对不同数据规模选择合适的算法
- 内存优化和缓存友好设计

### 5. 调试能力
- 添加调试信息输出
- 便于问题定位和性能分析

## 使用说明

### Java
```java
// 编译
javac RandomizedSelect.java

// 运行
java RandomizedSelect
```

### C++
```bash
# 编译
g++ -o RandomizedSelect_test RandomizedSelect.cpp

# 运行
./RandomizedSelect_test
```

### Python
```bash
# 运行
python RandomizedSelect.py
```

## 测试结果

所有三种语言的实现都已通过测试，能够正确处理各种边界情况和异常输入。代码具有良好的可读性和可维护性，符合工程化标准。

## 总结

本项目全面实现了快速选择算法及其相关应用，涵盖了各大算法平台的经典题目。通过多语言实现和工程化考量，展示了算法在实际应用中的完整解决方案。
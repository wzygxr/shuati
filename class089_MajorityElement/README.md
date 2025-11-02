# 水王数（Majority Element）相关算法题解

## 题目概览

本目录包含与"水王数"相关的经典算法题目，这些题目在各大算法平台如LeetCode、LintCode等都有出现。

### 1. 基础水王数问题 (Code01_WaterKing)
- **题目**: 出现次数大于n/2的数
- **描述**: 给定一个大小为n的数组nums，水王数是指在数组中出现次数大于n/2的数，返回其中的水王数，如果数组不存在水王数返回-1
- **测试链接**: 
  - https://leetcode.cn/problems/majority-element/
  - https://leetcode.com/problems/majority-element/
  - https://www.spoj.com/problems/MAJOR/
  - https://www.geeksforgeeks.org/problems/majority-element-1587115620/1
  - https://www.lintcode.com/problem/46/
  - https://www.nowcoder.com/practice/38802713414c4852b6982410c4187dd2
  - https://www.luogu.com.cn/problem/P1496
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 2. 多数元素 II (Code02_MajorityElementII)
- **题目**: 多数元素 II
- **描述**: 给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素
- **测试链接**: 
  - https://leetcode.cn/problems/majority-element-ii/
  - https://leetcode.com/problems/majority-element-ii/
  - https://www.geeksforgeeks.org/dsa/find-all-array-elements-occurring-more-than-floor-of-n-divided-by-3-times/
  - https://www.lintcode.com/problem/47/
  - https://www.nowcoder.com/practice/79165152ac2b4a28804947ed1981e0c2
- **最优解法**: 扩展的Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 3. 合法分割的最小下标 (Code03_MinimumIndexValidSplit)
- **题目**: 合法分割的最小下标
- **描述**: 给定一个数组，找到一个分割点，使得分割后的两部分都有支配元素且等于原数组的支配元素
- **测试链接**: 
  - https://leetcode.cn/problems/minimum-index-of-a-valid-split/
  - https://leetcode.com/problems/minimum-index-of-a-valid-split/
  - https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
- **最优解法**: Boyer-Moore投票算法 + 遍历验证
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 4. 出现次数大于n/k的数 (Code05_MoreThanNK)
- **题目**: 出现次数大于n/k的数
- **描述**: 给定一个大小为n的数组nums，给定一个较小的正数k，水王数是指在数组中出现次数大于n/k的数，返回所有的水王数，如果没有水王数返回空列表
- **测试链接**: 
  - https://leetcode.cn/problems/majority-element-ii/
  - https://leetcode.com/problems/majority-element-ii/
  - https://www.nowcoder.com/practice/79165152ac2b4a28804947ed1981e0c2
- **最优解法**: 扩展的Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(k)

### 5. 子数组中占绝大多数的元素 (Code06_FindSeaKing / Code07_MajorityChecker)
- **题目**: 子数组中占绝大多数的元素
- **描述**: 设计一个数据结构，有效地找到给定子数组的多数元素
- **测试链接**: 
  - https://leetcode.cn/problems/online-majority-element-in-subarray/
  - https://leetcode.com/problems/online-majority-element-in-subarray/
  - https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
- **最优解法**: 线段树 + 二分查找 或 随机化算法
- **时间复杂度**: 初始化O(nlogn)，查询O(logn) 或 初始化O(n)，查询期望O(logn)
- **空间复杂度**: O(n)

### 6. USACO 2024 January Contest, Bronze Problem 1. Majority Opinion
- **题目**: Majority Opinion
- **描述**: Farmer John有N头牛，每头牛喜欢一种干草。他可以通过举办焦点小组会议来改变牛对干草的喜好。如果一个焦点小组中某种干草的喜好数量超过一半，那么所有牛都会喜欢这种干草。目标是找出哪些干草类型可以成为所有牛都喜欢的类型。
- **测试链接**: https://usaco.org/index.php?page=viewproblem2&cpid=1371
- **最优解法**: 前缀和 + 枚举 或 贪心算法
- **时间复杂度**: O(n²) 或 O(n)
- **空间复杂度**: O(n)

## 算法核心思想总结

### Boyer-Moore投票算法
Boyer-Moore投票算法是解决水王数问题的核心算法，其基本思想是：
1. 维护一个候选元素和一个计数器
2. 遍历数组：
   - 如果计数器为0，将当前元素设为候选元素，计数器设为1
   - 如果当前元素等于候选元素，计数器加1
   - 如果当前元素不等于候选元素，计数器减1
3. 最后验证候选元素是否真的是水王数

该算法的正确性基于以下观察：如果数组中存在水王数，那么它与其他所有元素"抵消"后仍会剩余。

### 扩展的Boyer-Moore投票算法
对于寻找出现次数超过n/k的元素问题，可以使用扩展的Boyer-Moore投票算法：
1. 维护k-1个候选元素和对应的计数器
2. 遍历数组，按规则更新候选元素和计数器
3. 最后验证候选元素是否满足条件

### 线段树方法
对于支持区间查询的水王数问题，可以使用线段树：
1. 构建线段树，每个节点维护该区间的一个候选元素和对应的"血量"
2. 查询时合并区间信息得到候选元素
3. 使用二分查找验证候选元素在区间内的出现次数是否满足条件

### 随机化方法
对于在线查询的水王数问题，可以使用随机化方法：
1. 预处理每个元素出现的所有位置
2. 查询时随机选择区间内的元素进行验证
3. 由于多数元素出现次数超过阈值，随机选择命中多数元素的概率较高

## 工程化考量

### 异常处理
- 输入为空或长度为0的数组
- 不存在水王数的情况
- 查询区间不合法的情况
- 阈值参数不合法的情况

### 性能优化
- 预处理数据结构以加速查询
- 使用位运算优化常数时间
- 减少不必要的内存分配
- 对于大数据量情况，考虑使用分块处理

### 单元测试
- 边界情况测试（空数组、单元素数组等）
- 极端输入测试（所有元素相同、所有元素都不同等）
- 性能测试（大数据量情况下的表现）
- 随机化算法的稳定性测试

## 语言特性差异

### Java
- 使用类和对象组织代码
- 自动内存管理
- 丰富的集合类库
- 泛型支持类型安全

### C++
- 手动内存管理（需要注意内存泄漏）
- 模板支持泛型编程
- 性能更接近底层
- STL提供丰富的数据结构和算法

### Python
- 动态类型，代码简洁
- 丰富的内置函数和库
- 性能相对较低但开发效率高
- 列表推导式和生成器表达式提高代码可读性

## 应用场景

水王数相关算法在以下场景中有应用：
1. 数据分析中的众数查找
2. 机器学习中的投票算法
3. 分布式系统中的一致性协议
4. 数据库查询优化
5. 网络安全中的异常检测
6. 生物信息学中的序列分析

## 总结

水王数问题是算法面试中的经典题目，涉及多种算法思想和数据结构。掌握这些问题不仅有助于通过面试，更重要的是理解算法设计的核心思想，如：
1. 投票算法的抵消思想
2. 分治法的区间处理
3. 数据结构的预处理优化查询
4. 随机化算法的概率分析
5. 贪心算法的局部最优选择

在实际工程中，需要根据具体场景选择合适的算法实现，并考虑性能、内存、可维护性等因素。
# 水王数（Majority Element）算法题解大全

## 目录
1. [基础概念](#基础概念)
2. [核心算法](#核心算法)
3. [题目分类](#题目分类)
4. [平台题目汇总](#平台题目汇总)
5. [算法复杂度分析](#算法复杂度分析)
6. [工程化考量](#工程化考量)
7. [语言特性差异](#语言特性差异)
8. [应用场景](#应用场景)

## 基础概念

水王数（Majority Element）是指在数组中出现次数超过一定比例的元素：
- 基础水王数：出现次数 > n/2
- 扩展水王数：出现次数 > n/k（k为给定参数）
- 支配元素：在特定子数组中出现次数超过一半的元素

## 核心算法

### 1. Boyer-Moore投票算法
**适用场景**：寻找出现次数大于n/2的元素
**时间复杂度**：O(n)
**空间复杂度**：O(1)

**算法思路**：
1. 维护一个候选元素和一个计数器
2. 遍历数组：
   - 如果计数器为0，将当前元素设为候选元素，计数器设为1
   - 如果当前元素等于候选元素，计数器加1
   - 如果当前元素不等于候选元素，计数器减1
3. 最后验证候选元素是否真的是水王数

### 2. 扩展Boyer-Moore投票算法
**适用场景**：寻找出现次数大于n/k的元素
**时间复杂度**：O(n)
**空间复杂度**：O(k)

**算法思路**：
1. 维护k-1个候选元素和对应的计数器
2. 遍历数组，按规则更新候选元素和计数器
3. 最后验证候选元素是否满足条件

### 3. 线段树方法
**适用场景**：支持区间查询的水王数问题
**时间复杂度**：初始化O(nlogn)，查询O(logn)
**空间复杂度**：O(n)

**算法思路**：
1. 构建线段树，每个节点维护该区间的一个候选元素和对应的"血量"
2. 查询时合并区间信息得到候选元素
3. 使用二分查找验证候选元素在区间内的出现次数是否满足条件

### 4. 随机化方法
**适用场景**：在线查询的水王数问题
**时间复杂度**：初始化O(n)，查询期望O(logn)
**空间复杂度**：O(n)

**算法思路**：
1. 预处理每个元素出现的所有位置
2. 查询时随机选择区间内的元素进行验证
3. 由于多数元素出现次数超过阈值，随机选择命中多数元素的概率较高

## 题目分类

### 类型1：基础水王数问题
**题目描述**：找出数组中出现次数大于n/2的元素
**典型题目**：
- LeetCode 169. Majority Element
- SPOJ MAJOR
- 牛客网 NC143 - 数组中的水王数
- 洛谷 P1496 - 火烧赤壁

### 类型2：多数元素扩展问题
**题目描述**：找出数组中出现次数大于n/k的元素
**典型题目**：
- LeetCode 229. Majority Element II（k=3）
- 牛客网 NC144 - 多数元素 II

### 类型3：分割问题
**题目描述**：找到一个分割点，使得分割后的两部分都有支配元素
**典型题目**：
- LeetCode 2780. Minimum Index of a Valid Split
- 牛客网 NC145 - 合法分割的最小下标

### 类型4：在线查询问题
**题目描述**：设计数据结构支持快速查询任意子数组中的多数元素
**典型题目**：
- LeetCode 1157. Online Majority Element In Subarray
- 牛客网 NC146 - 子数组中占绝大多数的元素

### 类型5：USACO竞赛题
**题目描述**：通过焦点小组改变牛对干草的喜好，找出可以成为所有牛都喜欢的干草类型
**典型题目**：
- USACO 2024 January Contest, Bronze Problem 1. Majority Opinion

## 平台题目汇总

### LeetCode
1. [169. Majority Element](https://leetcode.com/problems/majority-element/)
2. [229. Majority Element II](https://leetcode.com/problems/majority-element-ii/)
3. [2780. Minimum Index of a Valid Split](https://leetcode.com/problems/minimum-index-of-a-valid-split/)
4. [1157. Online Majority Element In Subarray](https://leetcode.com/problems/online-majority-element-in-subarray/)

### SPOJ
1. [MAJOR](https://www.spoj.com/problems/MAJOR/)

### GeeksforGeeks
1. [Majority Element](https://www.geeksforgeeks.org/problems/majority-element-1587115620/1)
2. [Find all array elements occurring more than ⌊N/3⌋ times](https://www.geeksforgeeks.org/dsa/find-all-array-elements-occurring-more-than-floor-of-n-divided-by-3-times/)

### 牛客网
1. [NC143 - 数组中的水王数](https://www.nowcoder.com/practice/38802713414c4852b6982410c4187dd2)
2. [NC144 - 多数元素 II](https://www.nowcoder.com/practice/79165152ac2b4a28804947ed1981e0c2)
3. [NC145 - 合法分割的最小下标](https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611)
4. [NC146 - 子数组中占绝大多数的元素](https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611)

### 洛谷
1. [P1496 - 火烧赤壁](https://www.luogu.com.cn/problem/P1496)

### USACO
1. [2024 January Contest, Bronze Problem 1. Majority Opinion](https://usaco.org/index.php?page=viewproblem2&cpid=1371)

### LintCode
1. [46. Majority Element](https://www.lintcode.com/problem/46/)
2. [47. Majority Element II](https://www.lintcode.com/problem/47/)

### HackerRank
1. [Most Frequent Element](https://www.hackerrank.com/contests/bits-hyderabad-practice-test-1/challenges/most-frequent-element)
2. [Majority Element II](https://www.hackerrank.com/contests/bits-hyderabad-practice-test-1/challenges/majority-element-ii)

### CodeChef
1. [Find the majority element](https://www.codechef.com/practice/arrays)

### AtCoder
1. [ABC155 C - Poll](https://atcoder.jp/contests/abc155/tasks/abc155_c)

### Codeforces
1. [Round #662 (Div. 2) B - Applejack and Storages](https://codeforces.com/contest/1579/problem/E2)

## 算法复杂度分析

| 算法类型 | 时间复杂度 | 空间复杂度 | 适用场景 |
|---------|-----------|-----------|----------|
| Boyer-Moore投票算法 | O(n) | O(1) | 基础水王数问题 |
| 扩展Boyer-Moore投票算法 | O(n) | O(k) | 多数元素扩展问题 |
| 线段树方法 | 初始化O(nlogn)，查询O(logn) | O(n) | 在线查询问题 |
| 随机化方法 | 初始化O(n)，查询期望O(logn) | O(n) | 在线查询问题 |

## 工程化考量

### 异常处理
1. **输入验证**：检查数组是否为空或长度为0
2. **边界条件**：处理不存在水王数的情况
3. **参数验证**：检查查询区间和阈值参数的合法性

### 性能优化
1. **预处理**：对数据结构进行预处理以加速查询
2. **位运算**：使用位运算优化常数时间
3. **内存管理**：减少不必要的内存分配
4. **分块处理**：对于大数据量情况，考虑使用分块处理

### 单元测试
1. **边界测试**：空数组、单元素数组等
2. **极端输入**：所有元素相同、所有元素都不同等
3. **性能测试**：大数据量情况下的表现
4. **稳定性测试**：随机化算法的稳定性

## 语言特性差异

### Java
- **优势**：自动内存管理、丰富的集合类库、泛型支持
- **特点**：使用类和对象组织代码，类型安全

### C++
- **优势**：性能接近底层、模板支持泛型编程
- **特点**：手动内存管理，需要关注内存泄漏

### Python
- **优势**：动态类型、代码简洁、丰富的内置函数和库
- **特点**：开发效率高，但性能相对较低

## 应用场景

1. **数据分析**：众数查找
2. **机器学习**：投票算法
3. **分布式系统**：一致性协议
4. **数据库**：查询优化
5. **网络安全**：异常检测
6. **生物信息学**：序列分析

## 总结

水王数问题是算法面试中的经典题目，涉及多种算法思想和数据结构。掌握这些问题不仅有助于通过面试，更重要的是理解算法设计的核心思想：

1. **投票算法的抵消思想**
2. **分治法的区间处理**
3. **数据结构的预处理优化查询**
4. **随机化算法的概率分析**
5. **贪心算法的局部最优选择**

在实际工程中，需要根据具体场景选择合适的算法实现，并考虑性能、内存、可维护性等因素。
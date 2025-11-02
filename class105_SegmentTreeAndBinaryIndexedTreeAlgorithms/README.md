# 线段树与树状数组专题 (class132)

## 概述

线段树(Segment Tree)和树状数组(Fenwick Tree/Binary Indexed Tree)是两种重要的数据结构，主要用于解决区间查询和更新问题。

### 线段树 (Segment Tree)
- 适用场景：动态区间操作，支持区间修改和区间查询
- 时间复杂度：构建O(n)，单点/区间更新O(log n)，单点/区间查询O(log n)
- 空间复杂度：O(4n)
- 特点：功能强大，可以处理复杂的区间操作，支持懒惰传播(Lazy Propagation)

### 树状数组 (Fenwick Tree/Binary Indexed Tree)
- 适用场景：前缀和查询，单点更新
- 时间复杂度：构建O(n log n)，单点更新O(log n)，前缀和查询O(log n)
- 空间复杂度：O(n)
- 特点：代码简洁，常数小，效率高，适合简单的前缀和问题

## 已实现题目

### 1. 休息k分钟最大会议和 (Code01_MeetingRestK.java)
- 问题描述：给定会议时长数组和休息时间k，选择会议使得总时长最大
- 算法：动态规划优化
- 时间复杂度：O(n)
- 空间复杂度：O(n)

### 2. 炮兵阵地 (Code02_SoldierPosition1.java, Code02_SoldierPosition2.java)
- 问题描述：在网格中放置炮兵使得互不攻击且数量最多
- 算法：状态压缩动态规划
- 时间复杂度：O(n * 3^m * 3^m)
- 空间复杂度：O(3^m * 3^m)

### 3. 还原数组的方法数 (Code03_WaysOfRevert1.java, Code03_WaysOfRevert2.java)
- 问题描述：还原满足特定条件的数组的方法数
- 算法：动态规划优化
- 时间复杂度：O(n * m)
- 空间复杂度：O(m)

### 4. 粉刷房子III (Code04_PaintHouseIII.java)
- 问题描述：给房子涂色形成指定街区数的最小花费
- 算法：动态规划优化
- 时间复杂度：O(n * t * c)
- 空间复杂度：O(t * c)

### 5. 从上到下挖砖块 (Code05_DiggingBricks1.java, Code05_DiggingBricks2.java)
- 问题描述：在倒三角砖块中挖砖获得最大收益
- 算法：动态规划优化
- 时间复杂度：O(n² * m)
- 空间复杂度：O(n * m)

### 6. 区域和检索 - 数组可修改 (Code06_RangeSumQueryMutable_SegmentTree.java, Code06_RangeSumQueryMutable_FenwickTree.py)
- 问题描述：支持单点更新和区间查询的数组操作
- 算法：线段树和树状数组
- 时间复杂度：更新O(log n)，查询O(log n)
- 空间复杂度：线段树O(4n)，树状数组O(n)

### 7. 计算右侧小于当前元素的个数 (Code07_CountSmallerNumbersAfterSelf.java, Code07_CountSmallerNumbersAfterSelf.py)
- 问题描述：计算数组中每个元素右侧小于它的元素个数
- 算法：树状数组+离散化
- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

### 8. 天际线问题 (Code08_TheSkylineProblem.java, Code08_TheSkylineProblem.py)
- 问题描述：计算建筑物形成的天际线轮廓
- 算法：扫描线+线段树/有序数据结构
- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

### 9. 掉落的方块 (Code09_FallingSquares.java, Code09_FallingSquares.py)
- 问题描述：模拟方块掉落堆叠过程，计算实时最大高度
- 算法：线段树/区间处理
- 时间复杂度：O(n²) 或 O(n log n)
- 空间复杂度：O(n)

### 10. 翻转对 (Code10_ReversePairs.java, Code10_ReversePairs.py, Code10_ReversePairs.cpp)
- 问题描述：计算数组中满足 i < j 且 nums[i] > 2*nums[j] 的重要翻转对数量
- 算法：树状数组+离散化
- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

### 11. DQUERY - 区间不同元素个数 (Code11_DQuery.java, Code11_DQuery.py)
- 问题描述：查询区间[l,r]内不同元素的个数
- 算法：树状数组+离线处理
- 时间复杂度：O((n+q) log n + q log q)
- 空间复杂度：O(n+q)

### 12. 二维区域和检索 - 可变 (Code21_RangeSumQuery2DMutable.java, Code21_RangeSumQuery2DMutable.cpp, Code21_RangeSumQuery2DMutable.py)
- 问题描述：设计数据结构支持二维矩阵的单点更新和子矩阵查询
- 算法：二维树状数组
- 时间复杂度：更新O(log m * log n)，查询O(log m * log n)
- 空间复杂度：O(m * n)

### 13. 区间和的个数 (Code22_CountOfRangeSum.java, Code22_CountOfRangeSum.cpp, Code22_CountOfRangeSum.py)
- 问题描述：统计区间和的值在区间 [lower, upper] 之间的区间个数
- 算法：树状数组+离散化
- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

### 14. 计算右侧小于当前元素的个数 (Code23_CountOfSmallerNumbersAfterSelf.java, Code23_CountOfSmallerNumbersAfterSelf.cpp, Code23_CountOfSmallerNumbersAfterSelf.py)
- 问题描述：计算数组中每个元素右侧小于它的元素个数
- 算法：树状数组+离散化
- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

### 15. 翻转对 (Code24_ReversePairs.java, Code24_ReversePairs.cpp, Code24_ReversePairs.py)
- 问题描述：计算数组中满足 i < j 且 nums[i] > 2*nums[j] 的重要翻转对数量
- 算法：树状数组+离散化
- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

## 经典题目列表

### 线段树题目
1. **区域和检索 - 数组可修改** - LeetCode 307
2. **区域和检索 - 二维可修改** - LeetCode 308
3. **掉落的方块** - LeetCode 699
4. **天际线问题** - LeetCode 218
5. **区间模块** - LeetCode 715
6. **我的日程安排表 III** - LeetCode 732
7. **统计区间中的整数数目** - LeetCode 2276
8. **贴海报** - 洛谷 P3740
9. **最大数** - 洛谷 P1198
10. **敌兵布阵** - HDU 1166
11. **贴纸** - LeetCode 691
12. **矩形区域不超过 K 的最大数值和** - LeetCode 363
13. **最大波动的子字符串** - LeetCode 1157
14. **花括号展开** - LeetCode 1088
15. **范围模块** - LeetCode 715
16. **CPU 监控** - 洛谷 P4314
17. **扶苏的问题** - 洛谷 P1253
18. **XOR的艺术** - 洛谷 P2574
19. **开关** - 洛谷 P3870
20. **The Child and Sequence** - Codeforces 438D

### 树状数组题目
1. **区域和检索 - 数组可修改** - LeetCode 307
2. **区间和的个数** - LeetCode 327
3. **翻转对** - LeetCode 493
4. **计算右侧小于当前元素的个数** - LeetCode 315
5. **逆序对** - 洛谷 P1908
6. **火柴排队** - 洛谷 P1966
7. **HH的项链** - 洛谷 P1972
8. **冒泡排序** - 洛谷 P6186
9. **列队** - 洛谷 P3960
10. **Promotion Counting** - USACO 2017 JAN
11. **树状数组基础操作** - 洛谷 P3374
12. **区间修改区间查询** - 洛谷 P3368
13. **矩阵填数** - Codeforces 755D
14. **树状数组套权值线段树** - Codeforces 940F
15. **区间众数** - SPOJ DQUERY
16. **Restore Permutation** - Codeforces 1208D
17. **Multiset** - Codeforces 1354D
18. **Physical Education Lessons** - Codeforces 915E

## 算法技巧总结

### 线段树适用场景
1. 区间最值查询
2. 区间和查询
3. 区间修改（配合懒惰传播）
4. 区间最值修改
5. 区间历史最值查询

### 树状数组适用场景
1. 前缀和查询
2. 单点更新
3. 区间加法操作（差分数组）
4. 逆序对统计
5. 离散化处理

### 优化技巧
1. **预处理优化**：通过预处理减少重复计算
2. **空间压缩**：滚动数组等技术优化空间复杂度
3. **懒惰传播**：延迟更新以提高区间修改效率
4. **离散化**：处理大数值范围问题
5. **差分数组**：将区间修改转化为单点修改

## 工程化考量
1. **异常处理**：处理非法输入和边界条件
2. **单元测试**：确保算法正确性
3. **性能优化**：针对大规模数据优化
4. **可配置性**：参数化设计提高复用性
5. **调试能力**：中间过程打印和断言验证

## 扩展内容
本专题在原有基础上扩展了更多线段树和树状数组的经典题目实现，包括：
- LeetCode 493. 翻转对（Reverse Pairs）
- SPOJ DQUERY. 区间不同元素个数查询

每个题目都提供了Java、Python、C++三种语言的实现，并包含详细的注释说明设计思路、时间空间复杂度分析，以及工程化考量。代码经过测试验证，确保正确性和鲁棒性。

通过这些扩展题目的练习，可以更深入地理解和掌握线段树与树状数组这两种重要数据结构的应用场景和实现技巧。
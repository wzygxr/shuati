# 线段树补充题目大全 - Additional Segment Tree Problems

## 概述

线段树是一种非常重要的数据结构，广泛应用于各种算法竞赛和工程实践中。它能够高效地处理区间查询和区间更新操作，在时间复杂度上通常能达到O(log n)的效率。本文件收集了来自各大算法平台的线段树相关题目，帮助学习者全面掌握线段树的应用。

## LeetCode题目

### 1. LeetCode 307. Range Sum Query - Mutable (区域和检索 - 数组可修改)
- **类型**: 单点更新 + 区间求和
- **难度**: Medium
- **题目链接**: https://leetcode.com/problems/range-sum-query-mutable/
- **核心思想**: 经典线段树应用，支持单点更新和区间求和查询

### 2. LeetCode 308. Range Sum Query 2D - Mutable (二维区域和检索 - 可变)
- **类型**: 二维线段树 + 区间求和
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/range-sum-query-2d-mutable/
- **核心思想**: 二维线段树或树状数组维护二维区间和

### 3. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
- **核心思想**: 使用线段树维护值域信息，结合离散化处理

### 4. LeetCode 327. Count of Range Sum (区间和的个数)
- **类型**: 前缀和 + 离散化 + 区间查询
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/count-of-range-sum/
- **核心思想**: 前缀和转换 + 线段树维护区间信息

### 5. LeetCode 493. Reverse Pairs (翻转对)
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/reverse-pairs/
- **核心思想**: 计算满足条件的逆序对，使用线段树优化

### 6. LeetCode 699. Falling Squares (掉落的方块)
- **类型**: 区间最值查询 + 离散化
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/falling-squares/
- **核心思想**: 坐标离散化 + 线段树维护区间最大值

### 7. LeetCode 218. The Skyline Problem (天际线问题)
- **类型**: 扫描线 + 线段树
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/the-skyline-problem/
- **核心思想**: 扫描线算法 + 线段树维护区间最大值

### 8. LeetCode 715. Range Module (Range 模块)
- **类型**: 区间合并 + 线段树
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/range-module/
- **核心思想**: 维护区间覆盖状态，支持区间添加、查询、删除

### 9. LeetCode 732. My Calendar III (我的日程安排表 III)
- **类型**: 区间最大重叠次数 + 线段树
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/my-calendar-iii/
- **核心思想**: 维护区间最大值，计算最大重叠次数

### 10. LeetCode 850. Rectangle Area II (矩形面积 II)
- **类型**: 扫描线 + 线段树
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/rectangle-area-ii/
- **核心思想**: 扫描线算法 + 线段树维护区间长度

### 11. LeetCode 1157. Online Majority Element In Subarray (子数组中占绝大多数的元素)
- **类型**: 区间查询 + 二分查找
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/online-majority-element-in-subarray/
- **核心思想**: 线段树维护区间众数信息

### 12. LeetCode 1526. Minimum Number of Increments on Subarrays to Form a Target Array (形成目标数组的子数组最少增加次数)
- **类型**: 差分 + 贪心 + 线段树
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/minimum-number-of-increments-on-subarrays-to-form-a-target-array/
- **核心思想**: 差分数组 + 线段树维护

### 13. LeetCode 1649. Create Sorted Array through Instructions (通过指令创建有序数组)
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Hard
- **题目链接**: https://leetcode.com/problems/create-sorted-array-through-instructions/
- **核心思想**: 使用线段树维护插入代价

## Codeforces题目

### 1. Codeforces 339D. Xenia and Bit Operations (Xenia和位运算)
- **类型**: 位运算 + 线段树
- **难度**: Medium
- **题目链接**: https://codeforces.com/contest/339/problem/D
- **核心思想**: 线段树维护位运算结果

### 2. Codeforces 459D. Pashmak and Parmida's problem (Pashmak和Parmida的问题)
- **类型**: 离散化 + 线段树
- **难度**: Hard
- **题目链接**: https://codeforces.com/contest/459/problem/D
- **核心思想**: 前缀统计 + 线段树查询

### 3. Codeforces 52C. Circular RMQ (循环RMQ)
- **类型**: 循环数组 + 线段树
- **难度**: Hard
- **题目链接**: https://codeforces.com/contest/52/problem/C
- **核心思想**: 处理循环数组的区间操作

### 4. Codeforces 369E. Valera and Queries (Valera和查询)
- **类型**: 离线处理 + 线段树
- **难度**: Hard
- **题目链接**: https://codeforces.com/contest/369/problem/E
- **核心思想**: 离线处理 + 线段树维护

### 5. Codeforces 121E. Lucky Array (幸运数组)
- **类型**: 区间更新 + 区间查询
- **难度**: Hard
- **题目链接**: https://codeforces.com/contest/121/problem/E
- **核心思想**: 带懒惰传播的线段树

### 6. Codeforces 292E. Copying Data (复制数据)
- **类型**: 区间覆盖 + 线段树
- **难度**: Hard
- **题目链接**: https://codeforces.com/contest/292/problem/E
- **核心思想**: 线段树维护区间覆盖操作

## SPOJ题目

### 1. SPOJ GSS1 - Can you answer these queries I (你能回答这些问题吗 I)
- **类型**: 区间最大子段和 + 线段树
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/GSS1/
- **核心思想**: 线段树维护区间最大子段和信息

### 2. SPOJ GSS3 - Can you answer these queries III (你能回答这些问题吗 III)
- **类型**: 区间最大子段和 + 单点更新
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/GSS3/
- **核心思想**: 支持单点更新的区间最大子段和

### 3. SPOJ GSS4 - Can you answer these queries IV (你能回答这些问题吗 IV)
- **类型**: 区间开方 + 线段树
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/GSS4/
- **核心思想**: 利用开方操作收敛性进行优化

### 4. SPOJ GSS5 - Can you answer these queries V (你能回答这些问题吗 V)
- **类型**: 区间最大子段和 + 复杂查询
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/GSS5/
- **核心思想**: 复杂区间查询的线段树实现

### 5. SPOJ GSS6 - Can you answer these queries VI (你能回答这些问题吗 VI)
- **类型**: 区间插入删除 + 线段树
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/GSS6/
- **核心思想**: 支持动态插入删除的线段树

### 6. SPOJ GSS7 - Can you answer these queries VII (你能回答这些问题吗 VII)
- **类型**: 树链剖分 + 线段树
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/GSS7/
- **核心思想**: 树链剖分 + 线段树维护路径信息

### 7. SPOJ HORRIBLE - Horrible Queries (可怕的查询)
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/HORRIBLE/
- **核心思想**: 带懒惰传播的线段树

### 8. SPOJ BRCKTS - Brackets (括号)
- **类型**: 括号匹配 + 线段树
- **难度**: Medium
- **题目链接**: https://www.spoj.com/problems/BRCKTS/
- **核心思想**: 线段树维护括号匹配信息

### 9. SPOJ FREQUENT - Frequent values (频繁出现的值)
- **类型**: 区间众数 + 线段树
- **难度**: Hard
- **题目链接**: https://www.spoj.com/problems/FREQUENT/
- **核心思想**: 线段树维护区间众数信息

### 10. SPOJ KGSS - Maximum Sum (最大和)
- **类型**: 区间最大值 + 线段树
- **难度**: Medium
- **题目链接**: https://www.spoj.com/problems/KGSS/
- **核心思想**: 维护区间最大值和次大值

## 洛谷(Luogu)题目

### 1. P3372 【模板】线段树 1
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: 模板
- **题目链接**: https://www.luogu.com.cn/problem/P3372
- **核心思想**: 线段树模板题

### 2. P3373 【模板】线段树 2
- **类型**: 区间乘法更新 + 区间加法更新 + 区间求和
- **难度**: 模板
- **题目链接**: https://www.luogu.com.cn/problem/P3373
- **核心思想**: 支持乘法和加法的线段树

### 3. P4198 楼房重建
- **类型**: 区间最值 + 二分查找
- **难度**: Hard
- **题目链接**: https://www.luogu.com.cn/problem/P4198
- **核心思想**: 线段树维护区间斜率信息

### 4. P1558 色板游戏
- **类型**: 区间颜色更新 + 位运算
- **难度**: Hard
- **题目链接**: https://www.luogu.com.cn/problem/P1558
- **核心思想**: 位运算表示颜色状态 + 线段树维护

### 5. P2184 贪婪大陆
- **类型**: 区间更新 + 区间查询
- **难度**: Hard
- **题目链接**: https://www.luogu.com.cn/problem/P2184
- **核心思想**: 使用两个线段树分别维护区间起始点和终止点

### 6. P3870 开关
- **类型**: 区间翻转 + 区间求和
- **难度**: Medium
- **题目链接**: https://www.luogu.com.cn/problem/P3870
- **核心思想**: 懒惰标记实现区间翻转操作

### 7. P1438 无聊的数列
- **类型**: 差分数组 + 线段树
- **难度**: Hard
- **题目链接**: https://www.luogu.com.cn/problem/P1438
- **核心思想**: 差分数组 + 线段树维护区间更新

### 8. P1471 方差
- **类型**: 区间更新 + 区间查询
- **难度**: Hard
- **题目链接**: https://www.luogu.com.cn/problem/P1471
- **核心思想**: 维护区间和与区间平方和计算方差

## HDU题目

### 1. HDU 1166. 敌兵布阵
- **类型**: 单点更新 + 区间求和
- **难度**: Medium
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=1166
- **核心思想**: 经典线段树应用

### 2. HDU 1754. I Hate It
- **类型**: 单点更新 + 区间最值
- **难度**: Medium
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=1754
- **核心思想**: 线段树维护区间最大值

### 3. HDU 1698. Just a Hook
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: Medium
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=1698
- **核心思想**: 带懒惰传播的线段树

### 4. HDU 4521. 小明系列故事——吃蜜糖
- **类型**: 区间更新 + 区间查询
- **难度**: Hard
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=4521
- **核心思想**: 线段树维护区间信息

## POJ题目

### 1. POJ 3468. A Simple Problem with Integers (简单的整数问题)
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: Hard
- **题目链接**: http://poj.org/problem?id=3468
- **核心思想**: 经典的带懒惰传播线段树

### 2. POJ 2528. Mayor's posters (市长的海报)
- **类型**: 离散化 + 区间覆盖
- **难度**: Hard
- **题目链接**: http://poj.org/problem?id=2528
- **核心思想**: 坐标离散化 + 线段树维护区间覆盖

### 3. POJ 3264. Balanced Lineup (平衡的阵容)
- **类型**: 区间最值查询
- **难度**: Medium
- **题目链接**: http://poj.org/problem?id=3264
- **核心思想**: RMQ问题的线段树解法

### 4. POJ 3667. Hotel (旅馆)
- **类型**: 区间分配 + 线段树
- **难度**: Hard
- **题目链接**: http://poj.org/problem?id=3667
- **核心思想**: 线段树维护区间连续空闲长度

## AtCoder题目

### 1. AtCoder ABC351 Practice J - Segment Tree
- **类型**: 基础线段树操作
- **难度**: Easy
- **题目链接**: https://atcoder.jp/contests/practice2/tasks/practice2_j
- **核心思想**: 线段树基础应用

### 2. AtCoder ABC185 F - Range Xor Query
- **类型**: 区间异或查询
- **难度**: Medium
- **题目链接**: https://atcoder.jp/contests/abc185/tasks/abc185_f
- **核心思想**: 线段树维护区间异或值

## LintCode题目

### 1. LintCode 206. Interval Sum (区间求和)
- **类型**: 区间求和
- **难度**: Easy
- **题目链接**: https://www.lintcode.com/problem/206/
- **核心思想**: 基础线段树应用

### 2. LintCode 249. Count of Smaller Number before itself (统计前面比自己小的数的个数)
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Medium
- **题目链接**: https://www.lintcode.com/problem/249/
- **核心思想**: 线段树维护值域信息

### 3. LintCode 439. Segment Tree Build II (线段树构造 II)
- **类型**: 线段树构造
- **难度**: Medium
- **题目链接**: https://www.lintcode.com/problem/439/
- **核心思想**: 线段树的构建过程

### 4. LintCode 247. Segment Tree Query II (线段树查询 II)
- **类型**: 线段树查询
- **难度**: Medium
- **题目链接**: https://www.lintcode.com/problem/247/
- **核心思想**: 线段树区间查询操作

## HackerRank题目

### 1. Range Minimum Query (区间最小值查询)
- **类型**: 区间最小值查询
- **难度**: Easy
- **题目链接**: https://www.hackerrank.com/challenges/range-minimum-query/problem
- **核心思想**: 线段树维护区间最小值

### 2. Array Manipulation (数组操作)
- **类型**: 差分数组 + 线段树
- **难度**: Hard
- **题目链接**: https://www.hackerrank.com/challenges/crush/problem
- **核心思想**: 差分思想优化区间更新

## USACO题目

### 1. USACO 2017 January Gold - Balanced Photo (平衡的照片)
- **类型**: 区间查询 + 线段树
- **难度**: Hard
- **题目链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=693
- **核心思想**: 线段树优化计数查询

## 其他平台题目

### 1. Timus 1846. GCD 2010
- **类型**: 区间GCD查询 + 线段树
- **难度**: Hard
- **题目链接**: https://acm.timus.ru/problem.aspx?space=1&num=1846
- **核心思想**: 线段树维护区间GCD

### 2. UVa 12086. Potentiometers (电位器)
- **类型**: 单点更新 + 区间求和
- **难度**: Medium
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3238
- **核心思想**: 经典线段树应用

## 线段树的典型应用场景

### 1. 区间最值查询 (RMQ)
- 查询区间内的最大值或最小值
- 应用：股票价格分析、性能监控等

### 2. 区间求和
- 计算区间内所有元素的和
- 应用：数据统计、积分计算等

### 3. 区间更新
- 对区间内所有元素进行统一操作
- 应用：批量数据修改、区域设置等

### 4. 离散化处理
- 处理大数据范围但实际数据稀疏的情况
- 应用：坐标压缩、排名计算等

### 5. 逆序对计算
- 计算数组中逆序对的个数
- 应用：排序算法分析、相似度计算等

### 6. 扫描线算法
- 处理几何问题中的区间覆盖
- 应用：矩形面积计算、天际线问题等

## 线段树的时间复杂度分析

| 操作类型 | 时间复杂度 | 说明 |
|---------|-----------|------|
| 构建 | O(n) | 从底向上构建整棵树 |
| 单点更新 | O(log n) | 从根到叶子节点的路径 |
| 区间更新 | O(log n) | 带懒惰传播的区间更新 |
| 单点查询 | O(log n) | 从根到叶子节点的路径 |
| 区间查询 | O(log n) | 最多访问两层节点 |

## 线段树的空间复杂度分析

线段树需要4倍原数组大小的空间，即O(4n) = O(n)。

## 工程化考虑

### 1. 异常处理
- 输入验证：检查数组边界、操作合法性等
- 错误恢复：在出现异常时能够恢复到一致状态

### 2. 性能优化
- 懒惰传播：避免不必要的更新操作
- 剪枝优化：利用问题特性减少计算量
- 内存优化：动态开点、压缩存储等

### 3. 可维护性
- 代码模块化：将线段树封装成独立类
- 接口清晰：提供简洁易用的API
- 注释完整：详细说明算法原理和实现细节

## 语言特性差异

### Java
- 面向对象封装良好
- 自动内存管理
- 丰富的集合类库

### Python
- 语法简洁易读
- 动态类型
- 列表推导式等高级特性

### C++
- 性能优秀
- 手动内存管理
- 模板支持泛型编程

## 线段树与其他数据结构的对比

| 数据结构 | 构建 | 更新 | 查询 | 适用场景 |
|---------|------|------|------|---------|
| 线段树 | O(n) | O(log n) | O(log n) | 区间操作频繁 |
| 树状数组 | O(n) | O(log n) | O(log n) | 区间求和、前缀和 |
| 平衡树 | O(n log n) | O(log n) | O(log n) | 动态维护有序序列 |
| 分块 | O(n) | O(√n) | O(√n) | 简单实现、在线算法 |

## 常见问题和解决方案

### 1. 懒惰传播标记错误
- **问题**: 更新操作后查询结果不正确
- **解决方案**: 确保在所有访问节点前都正确下推懒惰标记

### 2. 数组越界
- **问题**: 访问线段树数组时出现越界
- **解决方案**: 确保线段树数组大小足够(通常为4n)

### 3. 离散化错误
- **问题**: 离散化后无法正确映射原值
- **解决方案**: 使用二分查找确保正确映射

## 扩展应用

### 1. 二维线段树
- 处理二维区间查询问题
- 应用：图像处理、地理信息系统等

### 2. 可持久化线段树(主席树)
- 支持历史版本查询
- 应用：版本控制、回滚操作等

### 3. 动态开点线段树
- 节省空间，适用于稀疏数据
- 应用：大数据范围但实际数据稀疏的场景

### 4. 树链剖分 + 线段树
- 处理树上路径查询问题
- 应用：树上区间操作、路径最值等

## 总结

线段树作为一种重要的数据结构，在各种算法竞赛平台都有大量相关题目。掌握线段树的基本操作和各种变种，对于解决区间查询和更新问题具有重要意义。通过系统地练习这些题目，可以深入理解线段树的应用场景和优化技巧。
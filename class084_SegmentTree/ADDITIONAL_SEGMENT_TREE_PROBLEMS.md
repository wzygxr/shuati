# 更多线段树题目 - Additional Segment Tree Problems

## LeetCode题目

### 1. LeetCode 307. Range Sum Query - Mutable (区间求和 - 可变)
- **类型**: 单点更新 + 区间求和
- **难度**: Medium
- **测试链接**: https://leetcode.com/problems/range-sum-query-mutable/
- **核心思想**: 经典线段树应用
- **已实现**: Code18_RangeSumQueryMutable.java/.py/.cpp

### 2. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
- **核心思想**: 使用线段树维护值域信息

### 3. LeetCode 493. Reverse Pairs (翻转对)
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/reverse-pairs/
- **核心思想**: 计算满足条件的逆序对

### 4. LeetCode 327. Count of Range Sum (区间和的个数)
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/count-of-range-sum/
- **核心思想**: 前缀和 + 线段树

### 5. LeetCode 1157. Online Majority Element In Subarray (子数组中占绝大多数的元素)
- **类型**: 区间查询 + 二分查找
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/online-majority-element-in-subarray/
- **核心思想**: 线段树维护区间众数信息

### 6. LeetCode 1526. Minimum Number of Increments on Subarrays to Form a Target Array (形成目标数组的子数组最少增加次数)
- **类型**: 差分 + 贪心 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/minimum-number-of-increments-on-subarrays-to-form-a-target-array/
- **核心思想**: 差分数组 + 线段树维护

### 7. LeetCode 715. Range Module (Range 模块)
- **类型**: 区间合并 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/range-module/
- **核心思想**: 维护区间覆盖状态

### 8. LeetCode 732. My Calendar III (我的日程安排表 III)
- **类型**: 区间最大重叠次数 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/my-calendar-iii/
- **核心思想**: 维护区间最大值

### 9. LeetCode 699. Falling Squares (掉落的方块)
- **类型**: 区间最值查询 + 离散化
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/falling-squares/
- **核心思想**: 坐标离散化 + 线段树维护区间最大值

### 10. LeetCode 850. Rectangle Area II (矩形面积 II)
- **类型**: 扫描线 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/rectangle-area-ii/
- **核心思想**: 扫描线算法 + 线段树维护区间长度

### 11. LeetCode 1099. Two Sum Less Than K (两数之和小于 K)
- **类型**: 排序 + 双指针 + 线段树
- **难度**: Easy
- **测试链接**: https://leetcode.com/problems/two-sum-less-than-k/
- **核心思想**: 可以用线段树优化查询

### 12. LeetCode 1155. Number of Dice Rolls With Target Sum (骰子的不同方法数)
- **类型**: 动态规划 + 线段树优化
- **难度**: Medium
- **测试链接**: https://leetcode.com/problems/number-of-dice-rolls-with-target-sum/
- **核心思想**: 线段树优化区间查询

### 13. LeetCode 1483. Kth Ancestor of a Tree Node (树节点的第 K 个祖先)
- **类型**: 倍增 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/kth-ancestor-of-a-tree-node/
- **核心思想**: 线段树维护祖先信息

### 14. LeetCode 1569. Number of Ways to Reorder Array to Get Same BST (重新排序得到相同二叉搜索树的方案数)
- **类型**: 组合数学 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/number-of-ways-to-reorder-array-to-get-same-bst/
- **核心思想**: 线段树维护区间信息

### 15. LeetCode 1649. Create Sorted Array through Instructions (通过指令创建有序数组)
- **类型**: 离散化 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/create-sorted-array-through-instructions/
- **核心思想**: 线段树维护计数信息

### 16. LeetCode 1744. Can You Eat Your Favorite Candy on Your Favorite Day? (你能在你最喜欢的那天吃到你最喜欢的糖果吗？)
- **类型**: 前缀和 + 线段树
- **难度**: Medium
- **测试链接**: https://leetcode.com/problems/can-you-eat-your-favorite-candy-on-your-favorite-day/
- **核心思想**: 线段树维护前缀和

### 17. LeetCode 1844. Replace All Digits with Characters (将所有数字用字符替换)
- **类型**: 字符串处理 + 线段树
- **难度**: Easy
- **测试链接**: https://leetcode.com/problems/replace-all-digits-with-characters/
- **核心思想**: 可以用线段树优化多模式匹配

### 18. LeetCode 1906. Minimum Absolute Difference Queries (查询的最小绝对差)
- **类型**: 前缀统计 + 线段树
- **难度**: Medium
- **测试链接**: https://leetcode.com/problems/minimum-absolute-difference-queries/
- **核心思想**: 线段树维护区间计数信息

### 19. LeetCode 1944. Number of Visible People in a Queue (队列中可以看到的人数)
- **类型**: 单调栈 + 线段树
- **难度**: Hard
- **测试链接**: https://leetcode.com/problems/number-of-visible-people-in-a-queue/
- **核心思想**: 线段树维护可见性信息

### 20. LeetCode 2013. Detect Squares (检测正方形)
- **类型**: 哈希表 + 线段树
- **难度**: Medium
- **测试链接**: https://leetcode.com/problems/detect-squares/
- **核心思想**: 线段树维护坐标信息

## Codeforces题目

### 1. Codeforces 339D. Xenia and Bit Operations
- **类型**: 位运算 + 线段树
- **难度**: Medium
- **测试链接**: https://codeforces.com/contest/339/problem/D
- **核心思想**: 线段树维护位运算结果

### 2. Codeforces 459D. Pashmak and Parmida's problem
- **类型**: 离散化 + 线段树
- **难度**: Hard
- **测试链接**: https://codeforces.com/contest/459/problem/D
- **核心思想**: 前缀统计 + 线段树查询

### 3. Codeforces 52C. Circular RMQ
- **类型**: 循环数组 + 线段树
- **难度**: Hard
- **测试链接**: https://codeforces.com/contest/52/problem/C
- **核心思想**: 处理循环数组的区间操作

### 4. Codeforces 369E. Valera and Queries
- **类型**: 离线处理 + 线段树
- **难度**: Hard
- **测试链接**: https://codeforces.com/contest/369/problem/E
- **核心思想**: 离线处理 + 线段树维护

### 5. Codeforces 620E. New Year Tree
- **类型**: 树链剖分 + 线段树
- **难度**: Hard
- **测试链接**: https://codeforces.com/contest/620/problem/E
- **核心思想**: 线段树维护子树信息

### 6. Codeforces 1110F. Nearest Leaf
- **类型**: 树链剖分 + 线段树
- **难度**: Hard
- **测试链接**: https://codeforces.com/contest/1110/problem/F
- **核心思想**: 线段树维护路径信息

### 7. Codeforces 1093E. Intersection of Permutations
- **类型**: 线段树套线段树
- **难度**: Hard
- **测试链接**: https://codeforces.com/contest/1093/problem/E
- **核心思想**: 二维线段树维护排列信息

### 8. Codeforces 1194F. Crossword Expert
- **类型**: 二分 + 线段树
- **难度**: Hard
- **测试链接**: https://codeforces.com/contest/1194/problem/F
- **核心思想**: 线段树维护时间信息

## SPOJ题目

### 1. SPOJ GSS1 - Can you answer these queries I
- **类型**: 区间最大子段和 + 线段树
- **难度**: Hard
- **测试链接**: https://www.spoj.com/problems/GSS1/
- **核心思想**: 线段树维护区间最大子段和信息
- **已实现**: Code19_GSS1.java/.py/.cpp

### 2. SPOJ GSS3 - Can you answer these queries III
- **类型**: 区间最大子段和 + 单点更新
- **难度**: Hard
- **测试链接**: https://www.spoj.com/problems/GSS3/
- **核心思想**: 支持单点更新的区间最大子段和

### 3. SPOJ GSS4 - Can you answer these queries IV
- **类型**: 区间开方 + 线段树
- **难度**: Hard
- **测试链接**: https://www.spoj.com/problems/GSS4/
- **核心思想**: 利用开方操作收敛性进行优化

### 4. SPOJ HORRIBLE - Horrible Queries
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: Hard
- **测试链接**: https://www.spoj.com/problems/HORRIBLE/
- **核心思想**: 带懒惰传播的线段树
- **已实现**: Code11_HorribleQueries.java/.py/.cpp

### 5. SPOJ BRCKTS - Brackets
- **类型**: 括号匹配 + 线段树
- **难度**: Medium
- **测试链接**: https://www.spoj.com/problems/BRCKTS/
- **核心思想**: 线段树维护括号匹配信息

### 6. SPOJ FREQUENT - Frequent values
- **类型**: 区间众数 + 线段树
- **难度**: Hard
- **测试链接**: https://www.spoj.com/problems/FREQUENT/
- **核心思想**: 线段树维护区间众数信息

### 7. SPOJ MKTHNUM - K-th number
- **类型**: 可持久化线段树（主席树）
- **难度**: Hard
- **测试链接**: https://www.spoj.com/problems/MKTHNUM/
- **核心思想**: 主席树求区间第K大

### 8. SPOJ COT - Count on a tree
- **类型**: 树链剖分 + 可持久化线段树
- **难度**: Hard
- **测试链接**: https://www.spoj.com/problems/COT/
- **核心思想**: 树上路径第K大

### 9. SPOJ RMQSQ - Range Minimum Query
- **类型**: 区间最小值查询
- **难度**: Easy
- **测试链接**: https://www.spoj.com/problems/RMQSQ/
- **核心思想**: 线段树维护区间最小值

## AtCoder题目

### 1. AtCoder ABC351 Practice J - Segment Tree
- **类型**: 基础线段树操作
- **难度**: Easy
- **测试链接**: https://atcoder.jp/contests/practice2/tasks/practice2_j
- **核心思想**: 线段树基础应用

### 2. AtCoder ARC159D - Yet Another ABC String
- **类型**: 线段树优化DP
- **难度**: Hard
- **测试链接**: https://atcoder.jp/contests/arc159/tasks/arc159_d
- **核心思想**: 线段树优化动态规划

### 3. AtCoder ABC294F - Sugar Water 2
- **类型**: 二分答案 + 线段树
- **难度**: Medium
- **测试链接**: https://atcoder.jp/contests/abc294/tasks/abc294_f
- **核心思想**: 线段树维护计数信息

### 4. AtCoder ABC253F - Operations on a Matrix
- **类型**: 二维线段树
- **难度**: Hard
- **测试链接**: https://atcoder.jp/contests/abc253/tasks/abc253_f
- **核心思想**: 二维线段树维护矩阵信息

## HDU题目

### 1. HDU 1166. 敌兵布阵
- **类型**: 单点更新 + 区间求和
- **难度**: Medium
- **测试链接**: https://acm.hdu.edu.cn/showproblem.php?pid=1166
- **核心思想**: 经典线段树应用
- **已实现**: Code20_HDU1166.java/.py/.cpp

### 2. HDU 1754. I Hate It
- **类型**: 单点更新 + 区间最值
- **难度**: Medium
- **测试链接**: https://acm.hdu.edu.cn/showproblem.php?pid=1754
- **核心思想**: 线段树维护区间最大值

### 3. HDU 1698. Just a Hook
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: Medium
- **测试链接**: https://acm.hdu.edu.cn/showproblem.php?pid=1698
- **核心思想**: 带懒惰传播的线段树

### 4. HDU 2795. Billboard
- **类型**: 线段树优化二分
- **难度**: Medium
- **测试链接**: https://acm.hdu.edu.cn/showproblem.php?pid=2795
- **核心思想**: 线段树维护最大剩余空间

### 5. HDU 3308. LCIS
- **类型**: 线段树维护最长连续递增子序列
- **难度**: Hard
- **测试链接**: https://acm.hdu.edu.cn/showproblem.php?pid=3308
- **核心思想**: 线段树维护区间LCIS信息

### 6. HDU 4578. Transformation
- **类型**: 多种区间更新 + 线段树
- **难度**: Hard
- **测试链接**: https://acm.hdu.edu.cn/showproblem.php?pid=4578
- **核心思想**: 处理多种懒惰标记的优先级

### 7. HDU 5690. All X
- **类型**: 数学计算 + 线段树
- **难度**: Medium
- **测试链接**: https://acm.hdu.edu.cn/showproblem.php?pid=5690
- **核心思想**: 线段树维护数学表达式结果

## POJ题目

### 1. POJ 3468. A Simple Problem with Integers
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: Hard
- **测试链接**: http://poj.org/problem?id=3468
- **核心思想**: 经典的带懒惰传播线段树
- **已实现**: Code21_POJ3468.java/.py/.cpp

### 2. POJ 2528. Mayor's posters
- **类型**: 离散化 + 区间覆盖
- **难度**: Hard
- **测试链接**: http://poj.org/problem?id=2528
- **核心思想**: 坐标离散化 + 线段树维护区间覆盖

### 3. POJ 3264. Balanced Lineup
- **类型**: 区间最大最小值查询
- **难度**: Medium
- **测试链接**: http://poj.org/problem?id=3264
- **核心思想**: 线段树维护区间极值

### 4. POJ 2104. K-th Number
- **类型**: 可持久化线段树（主席树）
- **难度**: Hard
- **测试链接**: http://poj.org/problem?id=2104
- **核心思想**: 主席树求区间第K大

### 5. POJ 2828. Buy Tickets
- **类型**: 线段树逆序处理
- **难度**: Medium
- **测试链接**: http://poj.org/problem?id=2828
- **核心思想**: 线段树维护剩余位置

### 6. POJ 3250. Bad Hair Day
- **类型**: 单调栈 + 线段树
- **难度**: Medium
- **测试链接**: http://poj.org/problem?id=3250
- **核心思想**: 线段树维护可见性信息

## 洛谷(Luogu)题目

### 1. P3372 【模板】线段树 1
- **类型**: 区间更新 + 区间求和 + 懒惰传播
- **难度**: 模板
- **测试链接**: https://www.luogu.com.cn/problem/P3372
- **核心思想**: 线段树模板题
- **已实现**: Code22_LuoguP3372.java/.py/.cpp

### 2. P3373 【模板】线段树 2
- **类型**: 区间乘法更新 + 区间加法更新 + 区间求和
- **难度**: 模板
- **测试链接**: https://www.luogu.com.cn/problem/P3373
- **核心思想**: 支持乘法和加法的线段树

### 3. P4198 楼房重建
- **类型**: 区间最值 + 二分查找
- **难度**: Hard
- **测试链接**: https://www.luogu.com.cn/problem/P4198
- **核心思想**: 线段树维护区间斜率信息

### 4. P4145 上帝造题的七分钟2 / 花神游历各国
- **类型**: 区间开方 + 线段树
- **难度**: Hard
- **测试链接**: https://www.luogu.com.cn/problem/P4145
- **核心思想**: 利用开方操作的收敛性优化

### 5. P3835 【模板】可持久化线段树 1（主席树）
- **类型**: 可持久化线段树
- **难度**: 模板
- **测试链接**: https://www.luogu.com.cn/problem/P3835
- **核心思想**: 主席树模板

### 6. P3953 逛公园
- **类型**: 动态规划 + 线段树优化
- **难度**: Hard
- **测试链接**: https://www.luogu.com.cn/problem/P3953
- **核心思想**: 线段树优化DP转移

### 7. P5357 【模板】AC自动机（二次加强版）
- **类型**: AC自动机 + 线段树合并
- **难度**: Hard
- **测试链接**: https://www.luogu.com.cn/problem/P5357
- **核心思想**: 线段树合并维护fail树信息

### 8. P5057 [CQOI2006] 简单题
- **类型**: 位运算 + 线段树
- **难度**: Medium
- **测试链接**: https://www.luogu.com.cn/problem/P5057
- **核心思想**: 线段树维护异或操作

## LintCode题目

### 1. LintCode 206. Interval Sum
- **类型**: 区间求和
- **难度**: Easy
- **测试链接**: https://www.lintcode.com/problem/206/
- **核心思想**: 基础线段树应用

### 2. LintCode 249. Count of Smaller Number before itself
- **类型**: 离散化 + 单点更新 + 区间求和
- **难度**: Medium
- **测试链接**: https://www.lintcode.com/problem/249/
- **核心思想**: 线段树维护值域信息

### 3. LintCode 207. Interval Sum II
- **类型**: 区间更新 + 区间求和
- **难度**: Medium
- **测试链接**: https://www.lintcode.com/problem/207/
- **核心思想**: 带懒惰传播的线段树

### 4. LintCode 439. Segment Tree Build II
- **类型**: 线段树构建（最大值）
- **难度**: Medium
- **测试链接**: https://www.lintcode.com/problem/439/
- **核心思想**: 构建维护最大值的线段树

### 5. LintCode 440. Backpack III
- **类型**: 动态规划 + 线段树优化
- **难度**: Hard
- **测试链接**: https://www.lintcode.com/problem/440/
- **核心思想**: 线段树优化多重背包

### 6. LintCode 548. Intersection of Two Arrays II
- **类型**: 哈希表 + 线段树
- **难度**: Easy
- **测试链接**: https://www.lintcode.com/problem/548/
- **核心思想**: 线段树维护元素计数

## HackerRank题目

### 1. Range Minimum Query
- **类型**: 区间最小值查询
- **难度**: Easy
- **测试链接**: https://www.hackerrank.com/challenges/range-minimum-query/problem
- **核心思想**: 线段树维护区间最小值

### 2. Persistent Segment Tree
- **类型**: 可持久化线段树
- **难度**: Hard
- **测试链接**: https://www.hackerrank.com/contests/world-codesprint-12/challenges/persistent-segment-tree-sum
- **核心思想**: 主席树维护历史版本

### 3. Square-Ten Tree
- **类型**: 分块线段树
- **难度**: Hard
- **测试链接**: https://www.hackerrank.com/challenges/square-ten-tree/problem
- **核心思想**: 分块线段树优化大范围操作

## 其他平台题目

### 1. UVa 11235 - Frequent values
- **类型**: 区间众数
- **难度**: Medium
- **测试链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2176
- **核心思想**: 线段树维护区间众数信息

### 2. UVa 12299 - RMQ with Shifts
- **类型**: 区间最小值查询（带循环移位）
- **难度**: Hard
- **测试链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3451
- **核心思想**: 线段树维护循环数组信息

### 3. CodeChef - CHEFEXQ
- **类型**: 区间异或查询
- **难度**: Medium
- **测试链接**: https://www.codechef.com/problems/CHEFEXQ
- **核心思想**: 线段树维护区间异或值

### 4. CodeChef - KFSTB
- **类型**: 线段树优化动态规划
- **难度**: Hard
- **测试链接**: https://www.codechef.com/problems/KFSTB
- **核心思想**: 线段树优化DP转移

### 5. Timus OJ 1470 - Strong Defence
- **类型**: 区间更新 + 线段树
- **难度**: Hard
- **测试链接**: https://acm.timus.ru/problem.aspx?space=1&num=1470
- **核心思想**: 带懒惰传播的线段树

### 6. Aizu OJ ALDS1_9_C - Segment Tree
- **类型**: 线段树基础操作
- **难度**: Easy
- **测试链接**: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_9_C
- **核心思想**: 线段树基础应用

### 7. Comet OJ C0146 - 区间修改区间查询
- **类型**: 线段树模板
- **难度**: Medium
- **测试链接**: https://cometoj.com/contest/32/problem/C
- **核心思想**: 带懒惰传播的线段树

### 8. 牛客网 NC14526 - 区间不同的数
- **类型**: 离线处理 + 线段树
- **难度**: Hard
- **测试链接**: https://ac.nowcoder.com/acm/problem/14526
- **核心思想**: 离线查询 + 线段树维护

## 总结

线段树作为一种重要的数据结构，在各种算法竞赛平台都有大量相关题目。掌握线段树的基本操作和各种变种，对于解决区间查询和更新问题具有重要意义。通过系统地练习这些题目，可以深入理解线段树的应用场景和优化技巧。
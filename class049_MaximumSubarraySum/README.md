# Class071 - 最大子数组和相关算法专题

## 算法主题概述
本专题专注于最大子数组和及其各种变种问题的算法实现，包括经典Kadane算法、环形数组、乘积最大子数组、删除操作、多次串联等高级变种。

## 核心算法思想

### 1. Kadane算法（最大子数组和）
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **核心思想**: 动态规划，维护以当前元素结尾的最大子数组和
- **状态转移**: dp[i] = max(nums[i], dp[i-1] + nums[i])

### 2. 乘积最大子数组
- **特殊处理**: 需要同时维护最大值和最小值（处理负数）
- **关键点**: 负数乘以负数会变成正数

### 3. 环形数组最大和
- **两种情况**: 不跨越边界（直接Kadane）和跨越边界（总和-最小子数组和）
- **边界处理**: 全负数数组的特殊情况

## 现有题目列表

### 基础题目
1. **Code01_MaximumProductSubarray** - 乘积最大子数组
2. **Code02_MaxSumDividedBy7** - 被7整除的最大子数组和
3. **Code03_MagicScrollProblem** - 魔法卷轴问题
4. **Code04_MaximumSum3UnoverlappingSubarrays** - 三个非重叠子数组最大和
5. **Code05_ReverseArraySubarrayMaxSum** - 反转子数组最大和
6. **Code06_DeleteOneNumberLengthKMaxSum** - 删除一个数后长度为K的最大子数组和

### 高级变种
7. **Code07_MaximumSubarraySumWithOneDeletion** - 删除一次得到子数组最大和
8. **Code08_MaximumSubarray** - 经典最大子数组和
9. **Code09_MaximumSumCircularSubarray** - 环形子数组最大和
10. **Code10_KConcatenationMaximumSum** - K次串联后最大子数组和
11. **Code11_MaximumSumTwoNonOverlappingSubarrays** - 两个非重叠子数组最大和
12. **Code12_HackerRankMaximumSubarraySum** - HackerRank最大子数组和
13. **Code13_LuoguMaximumSubarraySum** - 洛谷最大子段和

## 补充题目列表（新增）

### LeetCode题目
14. **LeetCode 209. 长度最小的子数组** - 滑动窗口应用 - https://leetcode.cn/problems/minimum-size-subarray-sum/
15. **LeetCode 862. 和至少为 K 的最短子数组** - 单调队列应用 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
16. **LeetCode 1004. 最大连续1的个数 III** - 滑动窗口变种 - https://leetcode.cn/problems/max-consecutive-ones-iii/
17. **LeetCode 1423. 可获得的最大点数** - 前缀后缀和 - https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
18. **LeetCode 1425. 带限制的子序列和** - 单调队列优化DP - https://leetcode.cn/problems/constrained-subsequence-sum/
19. **LeetCode 1658. 将 x 减到 0 的最小操作数** - 滑动窗口逆向思维 - https://leetcode.cn/problems/minimum-operations-to-reduce-x-to-zero/
20. **LeetCode 198. 打家劫舍** - 动态规划基础 - https://leetcode.cn/problems/house-robber/
21. **LeetCode 213. 打家劫舍 II** - 环形数组变种 - https://leetcode.cn/problems/house-robber-ii/
22. **LeetCode 628. 三个数的最大乘积** - 数学思维 - https://leetcode.cn/problems/maximum-product-of-three-numbers/
23. **LeetCode 53. 最大子数组和** - 经典Kadane算法 - https://leetcode.cn/problems/maximum-subarray/
24. **LeetCode 152. 乘积最大子数组** - 乘积变种 - https://leetcode.cn/problems/maximum-product-subarray/
25. **LeetCode 918. 环形子数组的最大和** - 环形变种 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
26. **LeetCode 1186. 删除一次得到子数组最大和** - 删除操作变种 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
27. **LeetCode 1191. K 次串联后最大子数组之和** - 串联变种 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
28. **LeetCode 1031. 两个非重叠子数组的最大和** - 多子数组变种 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
29. **LeetCode 337. 打家劫舍 III** - 树形DP变种 - https://leetcode.cn/problems/house-robber-iii/
30. **LeetCode 740. 删除并获得点数** - DP变种 - https://leetcode.cn/problems/delete-and-earn/
31. **LeetCode 1388. 3n 块披萨** - 环形DP变种 - https://leetcode.cn/problems/pizza-with-3n-slices/

### 其他平台题目
32. **HDU 1003. Max Sum** - 经典最大子段和 - http://acm.hdu.edu.cn/showproblem.php?pid=1003
33. **POJ 2479. Maximum sum** - 两个不重叠子数组最大和 - http://poj.org/problem?id=2479
34. **牛客 NC19. 子数组的最大累加和问题** - 基础训练 - https://www.nowcoder.com/practice/554aa508dd5d4fefbf0f86e56e7dc785
35. **剑指 Offer 42. 连续子数组的最大和** - 面试经典 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
36. **CodeForces 961B. Lecture Sleep** - 滑动窗口应用 - https://codeforces.com/problemset/problem/961/B
37. **洛谷 P1115 最大子段和** - 基础训练 - https://www.luogu.com.cn/problem/P1115
38. **LintCode 41. 最大子数组** - 基础训练 - https://www.lintcode.com/problem/41/
39. **HackerRank Maximum Subarray Sum** - 在线评测 - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
40. **HackerRank The Maximum Subarray** - 在线评测 - https://www.hackerrank.com/challenges/maxsubarray/problem
41. **CodeChef MAXSUM** - 竞赛题目 - https://www.codechef.com/problems/MAXSUM
42. **SPOJ MAXSUM** - 竞赛题目 - https://www.spoj.com/problems/MAXSUM/
43. **UVa OJ 108. Maximum Sum** - 经典题目 - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&problem=44
44. **TimusOJ 1146. Maximum Sum** - 经典题目 - https://acm.timus.ru/problem.aspx?space=1&num=1146
45. **AizuOJ ALDS1_1_D. Maximum Profit** - 经典题目 - https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/1/ALDS1_1_D
46. **ZOJ 1074. To the Max** - 经典题目 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364593
47. **51Nod 1049. 最大子段和** - 基础训练 - https://www.51nod.com/Challenge/Problem.html#!#problemId=1049
48. **Project Euler Problem 1** - 数学思维 - https://projecteuler.net/problem=1
49. **HackerEarth Maximum Subarray** - 在线评测 - https://www.hackerearth.com/practice/basic-programming/implementation/basics-of-implementation/practice-problems/algorithm/maxsubarray/
50. **计蒜客 最大子数组和** - 基础训练 - https://nanti.jisuanke.com/t/T1234
51. **LOJ #10000. 最大子数组和** - 基础训练 - https://loj.ac/p/10000
52. **AcWing 101. 最高的牛** - 基础训练 - https://www.acwing.com/problem/content/103/
53. **USACO 2023 January Contest, Platinum Problem 1. Min Max Subarrays** - 竞赛题目 - https://usaco.org/index.php?page=viewproblem2&cpid=1500
54. **AtCoder ABC123 D. Cake 123** - 竞赛题目 - https://atcoder.jp/contests/abc123/tasks/abc123_d
55. **Comet OJ 最大子数组和** - 在线评测 - https://cometoj.com/problem/1234
56. **杭电 OJ 1003. Max Sum** - 经典题目 - http://acm.hdu.edu.cn/showproblem.php?pid=1003

## 算法技巧总结

### 1. 动态规划技巧
- **状态定义**: 明确dp[i]的含义
- **状态转移**: 分析所有可能的选择
- **空间优化**: 使用滚动数组或变量优化

### 2. 滑动窗口技巧
- **适用场景**: 连续子数组问题
- **关键点**: 窗口的扩张和收缩条件
- **优化**: 使用双指针减少重复计算

### 3. 前缀和技巧
- **应用**: 快速计算子数组和
- **变种**: 前缀最大值、后缀最大值
- **环形处理**: 两次前缀和计算

### 4. 数学思维
- **乘积问题**: 考虑正负数的特性
- **模运算**: 处理大数取模
- **极值选择**: 最大三个数或最小两个数

## 工程化考量

### 1. 异常处理
- 空数组检查
- 单元素数组处理
- 边界值验证

### 2. 性能优化
- 避免不必要的计算
- 使用合适的数据类型
- 考虑缓存友好性

### 3. 代码可读性
- 清晰的变量命名
- 适当的注释
- 模块化设计

## 复杂度分析指南

### 时间复杂度计算
- **遍历数组**: O(n)
- **嵌套循环**: O(n²)
- **滑动窗口**: O(n)
- **动态规划**: 状态数 × 转移代价

### 空间复杂度计算
- **变量存储**: O(1)
- **数组存储**: O(n)
- **递归调用**: 栈空间

## 测试策略

### 1. 边界测试
- 空数组
- 单元素数组
- 全正数/全负数数组
- 包含零的数组

### 2. 功能测试
- 正常情况
- 极端情况
- 随机测试

### 3. 性能测试
- 大规模数据
- 最坏情况输入
- 内存使用监控

## 学习路径建议

1. **基础掌握**: 先理解经典Kadane算法
2. **变种练习**: 逐步尝试各种变种问题
3. **综合应用**: 结合其他算法技巧
4. **工程实践**: 在实际项目中应用

## 相关资源

### 在线评测平台
- LeetCode: https://leetcode.cn/
- LintCode: https://www.lintcode.com/
- HackerRank: https://www.hackerrank.com/
- 洛谷: https://www.luogu.com.cn/
- CodeForces: https://codeforces.com/
- HDU OJ: http://acm.hdu.edu.cn/
- POJ: http://poj.org/
- 牛客网: https://www.nowcoder.com/
- AtCoder: https://atcoder.jp/
- CodeChef: https://www.codechef.com/
- SPOJ: https://www.spoj.com/
- Project Euler: https://projecteuler.net/
- HackerEarth: https://www.hackerearth.com/
- 计蒜客: https://nanti.jisuanke.com/
- ZOJ: https://zoj.pintia.cn/
- UVa OJ: https://onlinejudge.org/
- TimusOJ: https://acm.timus.ru/
- AizuOJ: https://onlinejudge.u-aizu.ac.jp/
- Comet OJ: https://cometoj.com/
- 杭电 OJ: http://acm.hdu.edu.cn/
- LOJ: https://loj.ac/
- AcWing: https://www.acwing.com/
- 51Nod: https://www.51nod.com/

### 学习资料
- 《算法导论》动态规划章节
- 《编程珠玑》算法设计技巧
- 各大高校算法课程讲义

---

*本专题将持续更新，添加更多相关题目和优化解法*
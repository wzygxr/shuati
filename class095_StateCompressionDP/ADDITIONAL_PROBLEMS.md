# 状态压缩动态规划题目扩展

## 题目列表

### 1. 最短超级串 (Shortest Superstring)
- **题目链接**: https://leetcode.cn/problems/find-the-shortest-superstring/
- **难度**: 困难
- **标签**: 状态压缩DP, 字符串, TSP变种
- **时间复杂度**: O(n^2 * 2^n + n * sum(len))
- **空间复杂度**: O(n * 2^n)
- **文件**: Code05_ShortestSuperstring.java, Code05_ShortestSuperstring.py

### 2. 并行课程 II (Parallel Courses II)
- **题目链接**: https://leetcode.cn/problems/parallel-courses-ii/
- **难度**: 困难
- **标签**: 状态压缩DP, 拓扑排序, 枚举子集
- **时间复杂度**: O(3^n + n * 2^n)
- **空间复杂度**: O(2^n)
- **文件**: Code06_MinimumNumberOfSemesters.java, Code06_MinimumNumberOfSemesters.py

### 3. 最小的必要团队 (Smallest Sufficient Team)
- **题目链接**: https://leetcode.cn/problems/smallest-sufficient-team/
- **难度**: 困难
- **标签**: 状态压缩DP, 集合覆盖
- **时间复杂度**: O(2^m * n)，其中m是技能数，n是人员数
- **空间复杂度**: O(2^m)
- **文件**: Code07_SmallestSufficientTeam.java, Code07_SmallestSufficientTeam.py

### 4. 参加考试的最大学生数 (Maximum Students Taking Exam)
- **题目链接**: https://leetcode.cn/problems/maximum-students-taking-exam/
- **难度**: 困难
- **标签**: 状态压缩DP, 位运算检查
- **时间复杂度**: O(m * 2^n * 2^n)，其中m是行数，n是列数
- **空间复杂度**: O(2^n)
- **文件**: Code08_MaximumStudentsTakingExam.java, Code08_MaximumStudentsTakingExam.py

### 5. 我能赢吗 (Can I Win)
- **题目链接**: https://leetcode.cn/problems/can-i-win/
- **难度**: 中等
- **标签**: 状态压缩DP, 博弈论, 记忆化搜索
- **时间复杂度**: O(2^n)
- **空间复杂度**: O(2^n)
- **文件**: Code01_CanIWin.java

### 6. 火柴拼正方形 (Matchsticks to Square)
- **题目链接**: https://leetcode.cn/problems/matchsticks-to-square/
- **难度**: 中等
- **标签**: 状态压缩DP, 回溯
- **时间复杂度**: O(n * 2^n)
- **空间复杂度**: O(2^n)
- **文件**: Code02_MatchsticksToSquare.java

### 7. 划分为k个相等的子集 (Partition to K Equal Sum Subsets)
- **题目链接**: https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
- **难度**: 中等
- **标签**: 状态压缩DP, 回溯
- **时间复杂度**: O(n * 2^n)
- **空间复杂度**: O(2^n)
- **文件**: Code03_PartitionToKEqualSumSubsets.java

### 8. 售货员的难题 (TSP问题)
- **题目链接**: https://www.luogu.com.cn/problem/P1171
- **难度**: 困难
- **标签**: 状态压缩DP, TSP
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(n * 2^n)
- **文件**: Code04_TSP1.java, Code04_TSP2.java

### 9. 优美的排列 (Beautiful Arrangement)
- **题目链接**: https://leetcode.cn/problems/beautiful-arrangement/
- **难度**: 中等
- **标签**: 状态压缩DP, 排列
- **时间复杂度**: O(n * 2^n)
- **空间复杂度**: O(2^n)
- **文件**: Code09_BeautifulArrangement.java, Code09_BeautifulArrangement.py

### 10. 贴纸拼词 (Stickers to Spell Word)
- **题目链接**: https://leetcode.cn/problems/stickers-to-spell-word/
- **难度**: 困难
- **标签**: 状态压缩DP, 字符串匹配
- **时间复杂度**: O(2^m * n * L)，其中m是target长度，n是贴纸数量，L是贴纸平均长度
- **空间复杂度**: O(2^m)
- **文件**: Code10_StickersToSpellWord.java, Code10_StickersToSpellWord.py

### 11. 访问所有节点的最短路径 (Shortest Path Visiting All Nodes)
- **题目链接**: https://leetcode.cn/problems/shortest-path-visiting-all-nodes/
- **难度**: 困难
- **标签**: 状态压缩DP, BFS
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(n * 2^n)
- **文件**: Code11_ShortestPathVisitingAllNodes.java, Code11_ShortestPathVisitingAllNodes.py

### 12. 按位与为零的三元组 (Triples with Bitwise AND Equal To Zero)
- **题目链接**: https://leetcode.cn/problems/triples-with-bitwise-and-equal-to-zero/
- **难度**: 困难
- **标签**: 状态压缩DP, 位运算, 子集枚举
- **时间复杂度**: O(3^m + n^2)，其中m是最大数的位数(本题中为16)
- **空间复杂度**: O(2^m)
- **文件**: Code12_TriplesWithBitwiseAndEqualToZero.java, Code12_TriplesWithBitwiseAndEqualToZero.py

### 13. 得分最高的单词集合 (Maximum Score Words Formed by Letters)
- **题目链接**: https://leetcode.cn/problems/maximum-score-words-formed-by-letters/
- **难度**: 困难
- **标签**: 状态压缩DP, 背包问题
- **时间复杂度**: O(2^n * L)，其中n是单词数量，L是单词平均长度
- **空间复杂度**: O(2^n)
- **文件**: Code13_MaximumScoreWordsFormedbyLetters.java, Code13_MaximumScoreWordsFormedbyLetters.py

### 14. 子集 II (Subsets II)
- **题目链接**: https://leetcode.cn/problems/subsets-ii/
- **难度**: 中等
- **标签**: 状态压缩, 位运算, 回溯
- **时间复杂度**: O(n * 2^n)
- **空间复杂度**: O(n)
- **文件**: Code14_SubsetsII.java, Code14_SubsetsII.cpp, Code14_SubsetsII.py

### 15. 目标和 (Target Sum)
- **题目链接**: https://leetcode.cn/problems/target-sum/
- **难度**: 中等
- **标签**: 状态压缩, 动态规划, 背包问题
- **时间复杂度**: O(n * target)
- **空间复杂度**: O(target)
- **文件**: Code15_TargetSum.java, Code15_TargetSum.cpp, Code15_TargetSum.py

### 16. 最小XOR值路径 (Minimum XOR Sum of Two Arrays)
- **题目链接**: https://leetcode.cn/problems/minimum-xor-sum-of-two-arrays/
- **难度**: 困难
- **标签**: 状态压缩DP, 位运算, 匹配
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(2^n)
- **文件**: Code16_MinimumXORSumTwoArrays.java, Code16_MinimumXORSumTwoArrays.cpp, Code16_MinimumXORSumTwoArrays.py

### 17. 完全平方数 (Perfect Squares)
- **题目链接**: https://leetcode.cn/problems/perfect-squares/
- **难度**: 中等
- **标签**: 状态压缩, BFS, 动态规划
- **时间复杂度**: O(n * sqrt(n))
- **空间复杂度**: O(n)
- **文件**: Code17_PerfectSquares.java

### 18. 岛屿数量 (Number of Islands)
- **题目链接**: https://leetcode.cn/problems/number-of-islands/
- **难度**: 中等
- **标签**: 状态压缩, BFS, DFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(min(M, N))
- **文件**: Code18_NumberOfIslands.java

### 19. 最长公共子序列 (Longest Common Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-common-subsequence/
- **难度**: 中等
- **标签**: 状态压缩, 动态规划
- **时间复杂度**: O(n * m)
- **空间复杂度**: O(n * m)
- **文件**: Code19_LongestCommonSubsequence.java

### 20. 分割等和子集 (Partition Equal Subset Sum)
- **题目链接**: https://leetcode.cn/problems/partition-equal-subset-sum/
- **难度**: 中等
- **标签**: 状态压缩, 动态规划, 背包问题
- **时间复杂度**: O(n * sum)
- **空间复杂度**: O(sum)
- **文件**: Code08_PartitionEqualSubsetSum.cpp, Code20_PartitionEqualSubsetSum.java, Code20_PartitionEqualSubsetSum.py

### 21. 旅行商问题 (Traveling Salesman Problem)
- **题目链接**: https://leetcode.cn/problems/find-the-shortest-superstring/ (相关变种)
- **难度**: 困难
- **标签**: 状态压缩DP, TSP, 图论
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(n * 2^n)
- **文件**: Code04_TSP1.java, Code04_TSP2.java

### 22. 最短公共超序列 (Shortest Common Supersequence)
- **题目链接**: https://leetcode.cn/problems/shortest-common-supersequence/
- **难度**: 困难
- **标签**: 状态压缩DP, 字符串, 动态规划
- **时间复杂度**: O(n * m * 2^(min(n,m)))
- **空间复杂度**: O(2^(min(n,m)))
- **文件**: Code21_ShortestCommonSupersequence.java, Code21_ShortestCommonSupersequence.py, Code21_ShortestCommonSupersequence.cpp

### 23. 最大兼容性评分和 (Maximum Compatibility Score Sum)
- **题目链接**: https://leetcode.cn/problems/maximum-compatibility-score-sum/
- **难度**: 中等
- **标签**: 状态压缩DP, 二分图匹配, 位运算
- **时间复杂度**: O(2^n * n^2)
- **空间复杂度**: O(2^n)
- **文件**: Code22_MaximumCompatibilityScoreSum.java, Code22_MaximumCompatibilityScoreSum.py, Code22_MaximumCompatibilityScoreSum.cpp

### 24. 最大兼容性评分和 II (Maximum Compatibility Score Sum II)
- **题目链接**: https://leetcode.cn/problems/maximum-compatibility-score-sum/
- **难度**: 中等
- **标签**: 状态压缩DP, 匈牙利算法, 二分图匹配
- **时间复杂度**: O(n^3)
- **空间复杂度**: O(n^2)
- **文件**: 未实现

### 25. 状态压缩背包问题 (Bitmask Knapsack Problem)
- **题目链接**: https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/ (相关变种)
- **难度**: 困难
- **标签**: 状态压缩DP, 背包问题, 位运算
- **时间复杂度**: O(3^n)
- **空间复杂度**: O(2^n)
- **文件**: 未实现

### 26. 按位或能得到最大值的子集数目 (Count Number of Maximum Bitwise-OR Subsets)
- **题目链接**: https://leetcode.cn/problems/count-number-of-maximum-bitwise-or-subsets/
- **难度**: 中等
- **标签**: 状态压缩DP, 位运算, 子集枚举
- **时间复杂度**: O(2^n)
- **空间复杂度**: O(2^n)
- **文件**: 未实现

### 27. 最大化网格幸福感 (Maximize Grid Happiness)
- **题目链接**: https://leetcode.cn/problems/maximize-grid-happiness/
- **难度**: 困难
- **标签**: 状态压缩DP, 轮廓线DP, 位运算
- **时间复杂度**: O(m * n * 3^m)
- **空间复杂度**: O(3^m)
- **文件**: 未实现

### 28. 蒙德里安的梦想 (Mondriaan's Dream)
- **题目链接**: https://www.acwing.com/problem/content/293/
- **难度**: 困难
- **标签**: 状态压缩DP, 轮廓线DP, 位运算
- **时间复杂度**: O(n * m * 2^m)
- **空间复杂度**: O(2^m)
- **文件**: 未实现

### 29. 炮兵阵地 (Artillery Positioning)
- **题目链接**: https://www.acwing.com/problem/content/295/
- **难度**: 困难
- **标签**: 状态压缩DP, 轮廓线DP, 位运算
- **时间复杂度**: O(n * m * 2^m)
- **空间复杂度**: O(2^m)
- **文件**: 未实现

### 30. 方格取数 (Grid Pickup)
- **题目链接**: https://www.acwing.com/problem/content/297/
- **难度**: 困难
- **标签**: 状态压缩DP, 轮廓线DP, 位运算
- **时间复杂度**: O(n * m * 2^m)
- **空间复杂度**: O(2^m)
- **文件**: 未实现

## 解题思路总结

### 状态压缩DP核心思想
状态压缩动态规划是一种利用二进制位来表示状态的动态规划方法，适用于集合相关的问题。核心思想是：
1. 用一个整数的二进制位表示集合状态，第i位为1表示集合包含第i个元素
2. 通过位运算来操作集合（添加元素、删除元素、检查元素是否存在等）
3. 使用动态规划来求解最优解

### 常见技巧
1. **位运算操作**：
   - 设置第i位为1：`mask | (1 << i)`
   - 检查第i位是否为1：`(mask & (1 << i)) != 0`
   - 清除第i位：`mask & ~(1 << i)`
   - 切换第i位：`mask ^ (1 << i)`

2. **枚举子集**：
   ```java
   // 枚举mask的所有子集
   for (int subset = mask; subset > 0; subset = (subset - 1) & mask) {
       // 处理subset
   }
   ```

3. **计算二进制中1的个数**：
   - Java: `Integer.bitCount(mask)`
   - Python: `bin(mask).count('1')`
   - C++: `__builtin_popcount(mask)`

### 典型题目类型

#### 1. TSP变种问题
如最短超级串问题，将每个字符串看作城市，字符串间的重叠部分看作距离，转化为TSP问题。

#### 2. 集合划分问题
如划分为k个相等的子集，通过状态压缩表示已选择的元素集合。

#### 3. 博弈问题
如我能赢吗，通过状态压缩表示已使用的数字集合，结合博弈论思想求解。

#### 4. 调度问题
如并行课程II，通过状态压缩表示已完成的课程集合，结合拓扑排序求解。

#### 5. 集合覆盖问题
如最小的必要团队，通过状态压缩表示已覆盖的技能集合，寻找最小的人员组合。

#### 6. 状态检查问题
如参加考试的最大学生数，通过状态压缩表示每行的座位安排，通过位运算检查约束条件。

#### 7. 字符串匹配问题
如贴纸拼词和得分最高的单词集合，通过状态压缩表示字符串的匹配状态。

#### 8. 图论问题
如访问所有节点的最短路径，通过状态压缩表示已访问的节点集合。

#### 9. 位运算优化问题
如按位与为零的三元组，通过状态压缩和子集枚举优化位运算。

#### 10. 轮廓线DP问题
如蒙德里安的梦想，通过状态压缩表示当前行的状态，逐行处理。

## 工程化考量

### 1. 性能优化
- 由于状态压缩DP的时间复杂度通常是指数级的，应尽量优化状态转移过程
- 预处理可以重复使用的数据，避免重复计算
- 使用合适的数据结构存储中间结果

### 2. 代码可读性
- 给位运算操作添加注释说明其含义
- 使用有意义的变量名表示状态和转移过程
- 将复杂的状态转移逻辑拆分为多个函数

### 3. 边界条件处理
- 注意处理空集和全集等特殊状态
- 检查状态是否可达再进行转移
- 正确初始化DP数组

## 复杂度分析

### 时间复杂度
状态压缩DP的时间复杂度通常由状态数和转移复杂度决定：
- 状态数：O(2^n)，其中n是元素个数
- 转移复杂度：根据具体问题而定，可能是O(1)、O(n)或更高

### 空间复杂度
- DP数组空间：O(2^n)
- 其他辅助数组空间：根据具体问题而定

## 扩展题目列表

### 31. 子集 (Subsets)
- **题目链接**: https://leetcode.cn/problems/subsets/
- **难度**: 中等
- **标签**: 状态压缩, 位运算
- **时间复杂度**: O(n * 2^n)
- **空间复杂度**: O(n)

### 32. 目标和 (Target Sum)
- **题目链接**: https://leetcode.cn/problems/target-sum/
- **难度**: 中等
- **标签**: 状态压缩, 动态规划, 背包问题
- **时间复杂度**: O(n * sum)
- **空间复杂度**: O(sum)

### 33. 最小XOR值路径 (Minimum XOR Sum of Two Arrays)
- **题目链接**: https://leetcode.cn/problems/minimum-xor-sum-of-two-arrays/
- **难度**: 困难
- **标签**: 状态压缩DP, 位运算, 匹配
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(2^n)

### 34. 最小的初始能量击败所有怪物 (Minimum Initial Energy to Finish Tasks)
- **题目链接**: https://leetcode.cn/problems/minimum-initial-energy-to-finish-tasks/
- **难度**: 中等
- **标签**: 状态压缩, 贪心, 排序
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(2^n)

### 35. 数组的最大子集和 (Maximum Subset XOR)
- **题目链接**: https://www.geeksforgeeks.org/find-the-maximum-subarray-xor-in-a-given-array/
- **难度**: 中等
- **标签**: 状态压缩, 位运算, 线性基
- **时间复杂度**: O(n * log(max))
- **空间复杂度**: O(1)

### 36. 最小顶点覆盖 (Minimum Vertex Cover)
- **题目链接**: https://www.luogu.com.cn/problem/P3383
- **难度**: 困难
- **标签**: 状态压缩DP, 图论
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(n * 2^n)

### 37. 最大独立集 (Maximum Independent Set)
- **题目链接**: https://www.luogu.com.cn/problem/P4163
- **难度**: 困难
- **标签**: 状态压缩DP, 图论
- **时间复杂度**: O(n * 2^n)
- **空间复杂度**: O(2^n)

### 38. 旅行商问题 (Traveling Salesman Problem)
- **题目链接**: https://www.hackerrank.com/challenges/traveling-salesman-problem/problem
- **难度**: 困难
- **标签**: 状态压缩DP, TSP
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(n * 2^n)

### 39. 最短路径访问所有节点 (Shortest Path Visiting All Nodes)
- **题目链接**: https://leetcode.cn/problems/shortest-path-visiting-all-nodes/
- **难度**: 困难
- **标签**: 状态压缩, BFS, 图论
- **时间复杂度**: O(n^2 * 2^n)
- **空间复杂度**: O(n * 2^n)

### 40. 单词搜索 II (Word Search II)
- **题目链接**: https://leetcode.cn/problems/word-search-ii/
- **难度**: 困难
- **标签**: 状态压缩, Trie树, 回溯
- **时间复杂度**: O(M * N * 4^L)
- **空间复杂度**: O(K * L)

### 41. 字母异位词分组 (Group Anagrams)
- **题目链接**: https://leetcode.cn/problems/group-anagrams/
- **难度**: 中等
- **标签**: 状态压缩, 哈希表
- **时间复杂度**: O(n * k)
- **空间复杂度**: O(n * k)

### 42. 最小的完全平方数数量 (Perfect Squares)
- **题目链接**: https://leetcode.cn/problems/perfect-squares/
- **难度**: 中等
- **标签**: 状态压缩, BFS, 动态规划
- **时间复杂度**: O(n * sqrt(n))
- **空间复杂度**: O(n)

### 43. 岛屿数量 (Number of Islands)
- **题目链接**: https://leetcode.cn/problems/number-of-islands/
- **难度**: 中等
- **标签**: 状态压缩, BFS, DFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(min(M, N))

### 44. 最长公共子序列 (Longest Common Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-common-subsequence/
- **难度**: 中等
- **标签**: 状态压缩, 动态规划
- **时间复杂度**: O(n * m)
- **空间复杂度**: O(n * m)

### 45. 编辑距离 (Edit Distance)
- **题目链接**: https://leetcode.cn/problems/edit-distance/
- **难度**: 困难
- **标签**: 状态压缩, 动态规划
- **时间复杂度**: O(n * m)
- **空间复杂度**: O(n * m)

### 46. 接雨水 (Trapping Rain Water)
- **题目链接**: https://leetcode.cn/problems/trapping-rain-water/
- **难度**: 困难
- **标签**: 状态压缩, 双指针, 栈
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 1. 算法竞赛
状态压缩DP是算法竞赛中的高频考点，特别是：
- ICPC区域赛
- Codeforces比赛
- AtCoder比赛
- TopCoder比赛
- Google Code Jam
- Facebook Hacker Cup

### 2. 面试准备
在技术面试中，状态压缩DP问题经常出现，特别是：
- Google面试
- Facebook面试
- Amazon面试
- Microsoft面试
- Apple面试
- 字节跳动面试
- 腾讯面试
- 阿里巴巴面试

### 3. 实际应用
虽然状态压缩DP通常用于解决理论问题，但在某些实际场景中也有应用：
- 电路设计中的状态机优化
- 生物信息学中的序列分析
- 人工智能中的状态空间搜索
- 通信网络中的路由优化
- 金融领域的投资组合优化
- 物流配送路径规划
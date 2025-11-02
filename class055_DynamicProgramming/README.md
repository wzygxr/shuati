# Class187 算法专题总结

本目录主要包含了一些高级动态规划和贪心算法的实现，涵盖了多种经典问题和优化技巧。

## 主要内容

### 1. 动态规划融合场景 (DP Fusion)
文件: [DPFusion.java](DPFusion.java)

实现了多种DP与其他算法结合的场景：
- DP+数论（模意义下的动态规划）
- DP+字符串（基于后缀自动机的计数）
- DP+计算几何（凸包上的动态规划）

#### 相关算法
- SMAWK算法：用于在Monge矩阵中快速找出每行的最小值
- Aliens Trick（WQS二分）：解决需要恰好分成k个部分的优化问题
- 分层图最短路径：在带有状态约束的图中寻找最短路径
- 期望DP与高斯消元：解决包含环的期望DP问题
- 插头DP：求解网格图中的回路计数、路径覆盖等问题
- 树上背包优化：使用small-to-large合并优化的树上背包问题

### 2. 四边形不等式优化 (Quadrangle Optimization)
文件: [QuadrangleOptimization.java](QuadrangleOptimization.java)

用于优化满足四边形不等式性质和决策单调性的区间DP问题，可以将时间复杂度从O(n³)降低到O(n²)。

#### 相关问题
- 石子合并问题
- LeetCode 312. 戳气球
- LeetCode 1000. 合并石头的最低成本
- 矩阵链乘法问题

### 3. 概率/期望DP (Probability DP)
文件: [ProbabilityDP.java](ProbabilityDP.java)

处理概率和期望问题的动态规划方法，通常使用逆序DP。

#### 相关问题
- LeetCode 808. 分汤
- LeetCode 688. 骑士在棋盘上的概率
- LeetCode 576. 出界的路径数
- 爬楼梯问题的期望版本

### 4. 子集覆盖问题 (Set Cover)
文件: [SetCover.java](SetCover.java)

经典的子集覆盖问题及其变种，使用贪心策略求解。

#### 相关问题
- LeetCode 1541. 平衡括号字符串的最少插入次数
- LeetCode 1689. 十-二进制数的最少数目
- LeetCode 45. 跳跃游戏 II

### 5. 区间染色算法 (Interval Coloring)
文件: [IntervalColoring.java](IntervalColoring.java)

基于贪心策略的区间问题解决方案。

#### 相关问题
- LeetCode 435. 无重叠区间
- LeetCode 452. 用最少数量的箭引爆气球
- LeetCode 253. 会议室 II

## 多语言实现

本目录中的算法问题大多提供了多种编程语言的实现：
- Java (主要实现语言)
- C++
- Python

## 算法特点

1. **高级优化技巧**：包含了多种DP优化技术，如四边形不等式优化、SMAWK算法、Aliens Trick等
2. **问题融合**：展示了DP与其他算法领域（数论、字符串、计算几何等）的结合
3. **贪心策略**：提供了多种贪心算法的实现和应用
4. **实际应用**：所有算法都结合了LeetCode等平台上的实际问题

## 学习建议

1. 理解每种算法的基本思想和适用场景
2. 掌握算法的时间和空间复杂度分析
3. 熟悉算法在经典问题中的应用
4. 通过LeetCode等平台练习相关题目
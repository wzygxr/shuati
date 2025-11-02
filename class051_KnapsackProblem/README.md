# Class073: 01背包问题深度解析

## 概述

01背包问题是动态规划中的经典问题，也是算法面试和竞赛中的高频考点。本目录收集了来自各大算法平台的01背包相关题目，并提供了Java、C++、Python三种语言的详细实现。

## 题目列表

### 经典01背包模板题
1. **洛谷P1048 [NOIP2005 普及组] 采药** - 经典01背包模板题
   - 文件: Code01_01Knapsack.java/Code01_01Knapsack.py
   - 链接: https://www.luogu.com.cn/problem/P1048

### 新补充题目（来自各大算法平台）

#### LeetCode题目
2. **LeetCode 279. 完全平方数** - 完全背包问题
   - 文件: Code44_PerfectSquares.java/Code44_PerfectSquares.py/Code44_PerfectSquares.cpp
   - 链接: https://leetcode.cn/problems/perfect-squares/
   - 类型: 完全背包问题，求最少完全平方数个数

3. **LeetCode 377. 组合总和 Ⅳ** - 完全背包问题（排列数）
   - 文件: Code45_CombinationSumIV.java/Code45_CombinationSumIV.py/Code45_CombinationSumIV.cpp
   - 链接: https://leetcode.cn/problems/combination-sum-iv/
   - 类型: 完全背包问题，计算排列数

4. **LeetCode 518. 零钱兑换 II** - 完全背包问题（组合数）
   - 文件: Code46_CoinChangeII.java/Code46_CoinChangeII.py/Code46_CoinChangeII.cpp
   - 链接: https://leetcode.cn/problems/coin-change-ii/
   - 类型: 完全背包问题，计算组合数

5. **LeetCode 416. 分割等和子集** - 经典01背包应用
   - 文件: Code01_01Knapsack.java/Code01_01Knapsack.py
   - 链接: https://leetcode.cn/problems/partition-equal-subset-sum/

3. **LeetCode 494. 目标和** - 01背包变形题
   - 文件: Code03_TargetSum.java/Code03_TargetSum.py
   - 链接: https://leetcode.cn/problems/target-sum/

4. **LeetCode 474. 一和零** - 二维费用01背包
   - 文件: Code07_OnesAndZeros.java/Code07_OnesAndZeros.py/Code07_OnesAndZeros.cpp
   - 链接: https://leetcode.cn/problems/ones-and-zeroes/

5. **LeetCode 879. 盈利计划** - 三维费用01背包
   - 文件: Code08_ProfitableSchemes.java/Code08_ProfitableSchemes.py/Code08_ProfitableSchemes.cpp
   - 链接: https://leetcode.cn/problems/profitable-schemes/

6. **LeetCode 322. 零钱兑换** - 完全背包变形题
   - 链接: https://leetcode.cn/problems/coin-change/

7. **LeetCode 518. 零钱兑换 II** - 完全背包变形题
   - 链接: https://leetcode.cn/problems/coin-change-ii/

8. **LeetCode 40. 组合总和 II** - 01背包变形题
   - 链接: https://leetcode.cn/problems/combination-sum-ii/

9. **LeetCode 1049. 最后一块石头的重量 II** - 01背包变形题
   - 文件: Code04_LastStoneWeightII.java/Code04_LastStoneWeightII.py
   - 链接: https://leetcode.cn/problems/last-stone-weight-ii/

### 洛谷题目
10. **洛谷P1049 [NOIP2001 普及组] 装箱问题** - 01背包变形题
    - 文件: Code10_PackingProblem.java/Code10_PackingProblem.py/Code10_PackingProblem.cpp
    - 链接: https://www.luogu.com.cn/problem/P1049

11. **洛谷P1060 [NOIP2006 普及组] 开心的金明** - 经典01背包应用
    - 文件: Code11_HappyJinming.java/Code11_HappyJinming.py/Code11_HappyJinming.cpp
    - 链接: https://www.luogu.com.cn/problem/P1060

12. **洛谷P2347 [NOIP2011 普及组] 砝码称重** - 01背包变形题
    - 链接: https://www.luogu.com.cn/problem/P2347

### AtCoder题目
13. **AtCoder Educational DP Contest D - Knapsack 1** - 经典01背包
    - 文件: Code09_Knapsack1.java/Code09_Knapsack1.py/Code09_Knapsack1.cpp
    - 链接: https://atcoder.jp/contests/dp/tasks/dp_d

24. **AtCoder DP Contest E - Knapsack 2** - 大容量01背包
    - 文件: Code49_Knapsack2.java/Code49_Knapsack2.py/Code49_Knapsack2.cpp
    - 链接: https://atcoder.jp/contests/dp/tasks/dp_e
    - 类型: 价值维度DP，处理大容量背包

### POJ题目
14. **POJ 1837 Balance** - 01背包变形题（力矩平衡）
    - 文件: Code12_Balance.java/Code12_Balance.py/Code12_Balance.cpp
    - 链接: http://poj.org/problem?id=1837

15. **POJ 1276 Cash Machine** - 多重背包转01背包
    - 文件: Code13_CashMachine.java/Code13_CashMachine.py/Code13_CashMachine.cpp
    - 链接: http://poj.org/problem?id=1276

16. **POJ 2184 Cow Exhibition** - 二维费用01背包
    - 文件: Code47_CowExhibition.java/Code47_CowExhibition.py/Code47_CowExhibition.cpp
    - 链接: http://poj.org/problem?id=2184
    - 类型: 二维费用01背包，处理负数坐标

### CodeForces题目
17. **Codeforces 546D Soldier and Number Game** - 数论+背包问题
    - 链接: https://codeforces.com/problemset/problem/546/D

18. **Codeforces 1132E Knapsack** - 混合背包问题
    - 链接: https://codeforces.com/problemset/problem/1132/E

### HackerRank题目
19. **HackerRank The Coin Change Problem** - 完全背包变形题
    - 链接: https://www.hackerrank.com/challenges/coin-change/problem

### 牛客网题目
20. **牛客 NC15411 硬币** - 多重背包问题
    - 链接: https://ac.nowcoder.com/acm/problem/15411

### UVA题目
21. **UVA 10616 Divisible Group Sums** - 分组背包+模数运算
    - 文件: Code50_DivisibleGroupSums.java/Code50_DivisibleGroupSums.py/Code50_DivisibleGroupSums.cpp
    - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1557
    - 类型: 分组背包+模数运算，选择M个数字能被D整除

### HDU题目
22. **HDU 2955 Robberies** - 概率背包问题
    - 文件: Code48_Robberies.java/Code48_Robberies.py/Code48_Robberies.cpp
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=2955
    - 类型: 概率背包问题，安全概率计算

23. **HDU 3535 AreYouBusy** - 分组背包问题
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=3535

## 解题思路总结

### 01背包基本模型
- **问题描述**: 有N个物品和一个容量为W的背包，每个物品有重量w[i]和价值v[i]，每个物品只能使用一次，求能装入背包的最大价值。
- **状态定义**: dp[i][j]表示前i个物品，背包容量为j时能获得的最大价值
- **状态转移方程**: 
  ```
  dp[i][j] = max(dp[i-1][j], dp[i-1][j-w[i]] + v[i])  (当j >= w[i]时)
  dp[i][j] = dp[i-1][j]  (当j < w[i]时)
  ```
- **空间优化**: 使用滚动数组，倒序遍历背包容量

### 新补充题目的解题技巧

#### 完全背包问题（LeetCode 279, 377, 518）
- **特点**: 每个物品可以无限次使用
- **状态转移**: 正序遍历背包容量
- **关键区别**: 01背包倒序遍历，完全背包正序遍历

#### 二维费用背包（POJ 2184, LeetCode 474）
- **特点**: 每个物品有两个限制条件
- **状态定义**: dp[j][k]表示第一个限制为j，第二个限制为k时的最优解
- **遍历顺序**: 双重循环，从大到小遍历

#### 概率背包（HDU 2955）
- **特点**: 将概率转化为安全概率（1-p）
- **状态定义**: dp[j]表示抢劫金额为j时的最大安全概率
- **关键点**: 概率相乘转化为安全概率相乘

#### 模数运算背包（UVA 10616）
- **特点**: 涉及模数运算和组合计数
- **状态定义**: dp[j][k]表示选择j个数字，和对D取模为k的方案数
- **关键技巧**: 正确处理负数取模

#### 大容量背包（AtCoder Knapsack 2）
- **特点**: 背包容量极大（10^9），但物品价值较小
- **解法**: 转换维度，以价值为状态进行DP
- **状态定义**: dp[j]表示获得价值j所需的最小重量

### 常见变形题型

1. **目标和问题**: 将问题转化为01背包，选择一些数使其和为特定值
2. **分割等和子集**: 判断是否能将数组分割成两个和相等的子集
3. **二维费用背包**: 每个物品有两个限制条件（如0和1的个数）
4. **三维费用背包**: 每个物品有三个限制条件（如人数、利润等）
5. **多重背包**: 每个物品有指定数量，可转化为01背包求解
6. **完全背包**: 每个物品可以无限次使用
7. **分组背包**: 物品分组，每组只能选一个
8. **依赖背包**: 物品间存在依赖关系

## 复杂度分析

### 基础01背包
- **时间复杂度**: O(N * W)，其中N是物品数量，W是背包容量
- **空间复杂度**: O(W)，使用滚动数组优化后

### 新补充题目的复杂度
1. **完全背包问题**: O(N * W)
2. **二维费用背包**: O(N * W1 * W2)，其中W1、W2是两个维度的限制
3. **三维费用背包**: O(N * W1 * W2 * W3)
4. **模数运算背包**: O(N * M * D)，其中M是选择数量，D是除数
5. **大容量背包**: O(N * V)，其中V是总价值，适用于W很大但V较小的情况

### 空间优化技巧
1. **滚动数组**: 将二维DP优化为一维
2. **状态压缩**: 使用位运算或模数运算减少状态空间
3. **维度转换**: 当容量过大时，转换为价值维度DP

## 工程化考虑

### 1. 异常处理与边界条件
- **输入验证**: 检查数组为空、容量为负等异常情况
- **边界处理**: 处理M=0、D=0、W=0等特殊情况
- **溢出防护**: 处理大数运算时的溢出问题

### 2. 性能优化策略
- **空间优化**: 优先使用滚动数组减少内存占用
- **时间优化**: 避免不必要的循环和计算
- **缓存友好**: 优化内存访问模式，提高缓存命中率

### 3. 可配置性与扩展性
- **参数化设计**: 将背包容量、物品数量等作为参数
- **模块化实现**: 分离DP逻辑和业务逻辑
- **接口设计**: 提供统一的解题接口，支持多种输入格式

### 4. 测试覆盖与质量保证
- **单元测试**: 覆盖正常情况、边界情况、异常情况
- **性能测试**: 测试大规模数据的处理能力
- **回归测试**: 确保修改不会破坏现有功能

## 面试要点

### 1. 基础理解深度
- **本质理解**: 01背包是选择问题，每个物品选或不选
- **状态转移**: 能够推导状态转移方程
- **空间优化**: 理解滚动数组的原理和实现

### 2. 变种识别能力
- **题型识别**: 快速识别各种背包问题的变种
- **转化技巧**: 将复杂问题转化为标准背包问题
- **维度分析**: 正确分析问题的维度和约束条件

### 3. 复杂度分析能力
- **时间复杂度**: 准确分析算法的时间复杂度
- **空间复杂度**: 分析空间使用情况
- **优化潜力**: 识别算法的优化空间

### 4. 工程实践能力
- **代码质量**: 编写清晰、可读、可维护的代码
- **异常处理**: 正确处理各种边界和异常情况
- **测试思维**: 设计全面的测试用例

## 实战技巧

### 1. 调试与问题定位
- **打印调试**: 使用System.out.println打印关键变量
- **断点思维**: 在关键位置添加调试输出
- **逐步验证**: 从小规模数据开始验证算法正确性

### 2. 性能优化策略
- **常数优化**: 减少不必要的计算和函数调用
- **内存优化**: 合理使用数据结构，减少内存占用
- **算法选择**: 根据数据规模选择合适的算法

### 3. 笔试面试技巧
- **模板准备**: 准备常用算法的代码模板
- **时间管理**: 合理分配解题时间
- **沟通表达**: 清晰表达解题思路和复杂度分析

## 扩展应用

### 1. 机器学习与背包问题
- **特征选择**: 背包问题可用于特征选择优化
- **资源分配**: 在资源受限的机器学习任务中的应用
- **模型压缩**: 神经网络剪枝中的背包问题应用

### 2. 大数据场景优化
- **分布式计算**: 大规模背包问题的分布式求解
- **近似算法**: 处理超大规模数据的近似解法
- **流式处理**: 数据流场景下的背包问题求解

### 3. 跨语言实现差异
- **Java**: 注重面向对象设计和异常处理
- **C++**: 强调性能优化和内存管理
- **Python**: 关注代码简洁性和可读性

通过系统学习本目录的所有题目和代码，您将全面掌握01背包问题及其各种变种，具备解决复杂动态规划问题的能力。
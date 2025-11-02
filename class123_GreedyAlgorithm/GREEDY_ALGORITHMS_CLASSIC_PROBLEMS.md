# 贪心算法经典题目清单

## 概述

贪心算法是一种在每一步选择中都采取在当前状态下最好或最优（即最有利）的选择，从而希望导致结果是最好或最优的算法策略。通过本次任务，我们为class092目录下的所有文件添加了详细注释，并创建了额外的经典贪心算法题目实现。

## 已完成的工作

### 1. 为现有文件添加详细注释
我们为class092目录下的所有Java、Python、C++文件添加了详细注释，确保符合用户的算法学习偏好和工程化要求。

### 2. 验证所有代码能正常编译和运行
我们验证了所有Java、Python、C++文件都能正常编译和运行。

### 3. 搜索并整理更多经典题目
我们搜索并整理了来自LeetCode、洛谷、HDU、Codeforces、AtCoder等平台的贪心算法经典题目。

### 4. 创建新的题目实现
我们为以下经典贪心算法题目创建了Java、Python、C++三种语言的实现：
- LeetCode 455. 分发饼干 (Code25_AssignCookies)
- LeetCode 45. 跳跃游戏 II (Code26_JumpGameII)
- LeetCode 134. 加油站 (Code27_GasStation)

## 经典贪心算法题目清单

### LeetCode题目

1. **LeetCode 455. 分发饼干** - https://leetcode.cn/problems/assign-cookies/
   - 类型：基础贪心
   - 难度：简单
   - 核心思想：优先满足胃口小的孩子，优先使用尺寸小的饼干
   - 文件：Code25_AssignCookies.java/.py/.cpp

2. **LeetCode 45. 跳跃游戏 II** - https://leetcode.cn/problems/jump-game-ii/
   - 类型：区间贪心
   - 难度：中等
   - 核心思想：维护当前能到达的最远位置和下一步能到达的最远位置
   - 文件：Code26_JumpGameII.java/.py/.cpp

3. **LeetCode 55. 跳跃游戏** - https://leetcode.cn/problems/jump-game/
   - 类型：区间贪心
   - 难度：中等
   - 核心思想：维护能到达的最远位置

4. **LeetCode 134. 加油站** - https://leetcode.cn/problems/gas-station/
   - 类型：环形贪心
   - 难度：中等
   - 核心思想：如果总油量减去总消耗量大于等于0，那么一定存在解
   - 文件：Code27_GasStation.java/.py/.cpp

5. **LeetCode 135. 分发糖果** - https://leetcode.cn/problems/candy/
   - 类型：双向约束贪心
   - 难度：困难
   - 核心思想：两次扫描处理双向约束

6. **LeetCode 376. 摆动序列** - https://leetcode.cn/problems/wiggle-subsequence/
   - 类型：序列贪心
   - 难度：中等
   - 核心思想：贪心地选择局部最优解

7. **LeetCode 402. 移掉K位数字** - https://leetcode.cn/problems/remove-k-digits/
   - 类型：单调栈贪心
   - 难度：中等
   - 核心思想：维护单调递增栈，使结果最小

8. **LeetCode 435. 无重叠区间** - https://leetcode.cn/problems/non-overlapping-intervals/
   - 类型：区间调度贪心
   - 难度：中等
   - 核心思想：按右端点排序，移除最少的重叠区间

9. **LeetCode 452. 用最少数量的箭引爆气球** - https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
   - 类型：区间调度贪心
   - 难度：中等
   - 核心思想：按右端点排序，尽可能多地引爆重叠气球

### 洛谷题目

1. **洛谷 P1090 合并果子** - https://www.luogu.com.cn/problem/P1090
   - 类型：优先队列贪心
   - 难度：普及-
   - 核心思想：每次合并最小的两堆果子

2. **洛谷 P1223 排队接水** - https://www.luogu.com.cn/problem/P1223
   - 类型：排序贪心
   - 难度：普及-
   - 核心思想：按接水时间升序排列

3. **洛谷 P1803 凌乱的yyy / 线段覆盖** - https://www.luogu.com.cn/problem/P1803
   - 类型：区间调度贪心
   - 难度：普及-
   - 核心思想：按结束时间排序，尽可能多地选择不重叠区间

### HDU题目

1. **HDU 1166 敌兵布阵** - http://acm.hdu.edu.cn/showproblem.php?pid=1166
   - 类型：线段树/树状数组
   - 难度：中等
   - 核心思想：支持单点更新和区间查询

2. **HDU 1698 Just a Hook** - http://acm.hdu.edu.cn/showproblem.php?pid=1698
   - 类型：线段树区间更新
   - 难度：中等
   - 核心思想：区间更新和区间查询

### Codeforces题目

1. **Codeforces 1360B - Honest Coach** - https://codeforces.com/problemset/problem/1360/B
   - 类型：排序贪心
   - 难度：800
   - 核心思想：排序后找相邻元素的最小差值

2. **Codeforces 1367B - Even Array** - https://codeforces.com/problemset/problem/1367/B
   - 类型：计数贪心
   - 难度：800
   - 核心思想：统计奇偶位置不匹配的元素数量

### AtCoder题目

1. **AtCoder ABC143C - Slimes** - https://atcoder.jp/contests/abc143/tasks/abc143_c
   - 类型：字符串贪心
   - 难度：灰
   - 核心思想：统计连续相同字符的段数

2. **AtCoder ABC153F - Silver Fox vs Monster** - https://atcoder.jp/contests/abc153/tasks/abc153_f
   - 类型：前缀和贪心
   - 难度：蓝
   - 核心思想：按位置排序，使用贪心策略和前缀和计算最小攻击次数

### HackerRank题目

1. **HackerRank - Greedy Florist** - https://www.hackerrank.com/challenges/greedy-florist/problem
   - 类型：排序贪心
   - 难度：中等
   - 核心思想：价格高的花优先分配给购买次数少的人

2. **HackerRank - Mark and Toys** - https://www.hackerrank.com/challenges/mark-and-toys/problem
   - 类型：排序贪心
   - 难度：简单
   - 核心思想：按价格排序，优先购买价格低的玩具

## 算法复杂度分析

所有实现的代码都包含了详细的时间复杂度和空间复杂度分析，确保学习者能够理解算法的效率特征。

## 工程化考量

所有代码都考虑了以下工程化因素：
1. 边界条件处理：空数组、单元素数组等特殊情况
2. 异常处理：输入参数验证
3. 可读性：变量命名清晰，注释详细
4. 算法调试技巧：提供了调试建议和中间结果验证方法

## 与机器学习的联系

部分题目还探讨了贪心算法与机器学习的联系：
1. 贪心策略在机器学习中也有应用，如决策树构建时的信息增益选择
2. 特征选择中也会使用贪心策略选择最优特征子集
3. 自定义排序的思想在机器学习中也有应用，如自定义距离度量

## 总结

通过本次任务，我们不仅为现有的算法实现添加了详细注释，还扩展了经典贪心算法题目的实现，为算法学习者提供了更丰富的学习资源。所有代码都经过验证可以正常编译和运行，并且包含了详细的注释和复杂度分析，符合用户的算法学习偏好和工程化要求。
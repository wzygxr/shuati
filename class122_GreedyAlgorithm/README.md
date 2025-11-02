# 贪心算法专题 (Greedy Algorithm)

## 专题介绍

贪心算法（Greedy Algorithm）是一种在每一步选择中都采取在当前状态下最好或最优（即最有利）的选择，从而希望导致结果是最好或最优的算法。贪心算法在有最优子结构的问题中尤为有效。

### 贪心算法的特点
1. **贪心选择性质**：所求问题的整体最优解可以通过一系列局部最优的选择得到
2. **最优子结构**：问题的最优解包含其子问题的最优解
3. **无后效性**：某个状态以前的过程不会影响以后的状态，只与当前状态有关

### 适用场景
- 活动选择问题
- 最小生成树（Kruskal、Prim算法）
- 单源最短路径（Dijkstra算法）
- 霍夫曼编码
- 分数背包问题

## 已有题目列表

### 1. 最短无序连续子数组
- **题目**: 找出需要排序的最短连续子数组
- **文件**: Code01_ShortestUnsortedContinuousSubarray.java/.cpp/.py
- **来源**: LeetCode 581
- **相关题目链接**:
  - https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/ (LeetCode 581)
  - https://www.lintcode.com/problem/shortest-unsorted-continuous-subarray/ (LintCode 1206)
  - https://practice.geeksforgeeks.org/problems/minimum-subarray-to-sort/ (GeeksforGeeks)
  - https://www.nowcoder.com/practice/2f9264b48cc24799925d48d355094c78 (牛客网)
  - https://codeforces.com/problemset/problem/1139/C (Codeforces)
  - https://atcoder.jp/contests/abc134/tasks/abc134_c (AtCoder)
  - https://www.hackerrank.com/challenges/shortest-unsorted-continuous-subarray/problem (HackerRank)
  - https://www.luogu.com.cn/problem/P1525 (洛谷)
- **难度**: 简单
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 2. 最小区间
- **题目**: 找到包含每个列表至少一个数的最小区间
- **文件**: Code02_SmallestRange.java/.cpp/.py
- **来源**: LeetCode 632
- **难度**: 困难
- **算法**: 滑动窗口 + TreeSet
- **时间复杂度**: O(n*logk)
- **空间复杂度**: O(k)

### 3. 组团买票
- **题目**: 计算组团买票的最少花费
- **文件**: Code03_GroupBuyTickets1.java, Code03_GroupBuyTickets2.java, Code03_GroupBuyTickets.cpp, Code03_GroupBuyTickets.py
- **来源**: 大厂笔试题
- **难度**: 中等
- **算法**: 优先队列 + 贪心
- **时间复杂度**: O(n * logm)
- **空间复杂度**: O(m)

### 4. 平均值最小累加和
- **题目**: 将数组划分成k个集合，使平均值累加和最小
- **文件**: Code04_SplitMinimumAverageSum.java/.cpp/.py
- **来源**: 大厂笔试题
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n * logn)
- **空间复杂度**: O(1)

### 5. 执行所有任务的最少初始电量
- **题目**: 计算执行所有任务的最少初始电量
- **文件**: Code05_MinimalBatteryPower.java/.cpp/.py
- **来源**: LeetCode 1383
- **难度**: 困难
- **算法**: 贪心算法
- **时间复杂度**: O(n * logn)
- **空间复杂度**: O(1)

### 6. 两个0和1数量相等区间的最大长度
- **题目**: 找到两个0和1数量相等区间的最大长度
- **文件**: Code06_LongestSameZerosOnes.java/.cpp/.py
- **来源**: 大厂笔试题
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

## 补充题目列表

### 1. 分发饼干
- **题目**: 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。每个孩子最多只能给一块饼干。对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的最小尺寸。分配饼干使最多孩子满足。
- **文件**: Code07_AssignCookies.java/.cpp/.py
- **来源**: LeetCode 455
- **相关题目链接**:
  - https://leetcode.cn/problems/assign-cookies/ (LeetCode 455)
  - https://www.lintcode.com/problem/assign-cookies/ (LintCode 1104)
  - https://practice.geeksforgeeks.org/problems/assign-cookies/ (GeeksforGeeks)
  - https://www.nowcoder.com/practice/1a83b5d505b54350b80ec63107d234a1 (牛客网)
  - https://codeforces.com/problemset/problem/483/B (Codeforces)
  - https://atcoder.jp/contests/abc153/tasks/abc153_d (AtCoder)
  - https://www.hackerrank.com/challenges/assign-cookies/problem (HackerRank)
  - https://www.luogu.com.cn/problem/P1042 (洛谷)
- **难度**: 简单
- **算法**: 贪心算法
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(1)

### 2. 跳跃游戏
- **题目**: 给定一个非负整数数组，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的最大长度。判断你是否能够到达最后一个位置。
- **文件**: Code08_JumpGame.java/.cpp/.py
- **来源**: LeetCode 55
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 3. 最大子数组和
- **题目**: 给你一个整数数组nums，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
- **文件**: Code09_MaximumSubarray.java/.cpp/.py
- **来源**: LeetCode 53
- **难度**: 简单
- **算法**: 贪心算法/Kadane算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 4. 买卖股票的最佳时机 II
- **题目**: 给定一个数组，它的第i个元素是一支给定股票第i天的价格。设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
- **文件**: Code10_BestTimeToBuyAndSellStockII.java/.cpp/.py
- **来源**: LeetCode 122
- **难度**: 简单
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 5. 无重叠区间
- **题目**: 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
- **文件**: Code11_NonOverlappingIntervals.java/.cpp/.py
- **来源**: LeetCode 435
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(1)

### 6. 加油站
- **题目**: 在一条环路上有N个加油站，其中第i个加油站有汽油gas[i]升。你有一辆油箱容量无限的汽车，从第i个加油站开往第i+1个加油站需要消耗汽油cost[i]升。你从其中的一个加油站出发，开始时油箱为空。如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回-1。
- **文件**: Code12_GasStation.java/.cpp/.py
- **来源**: LeetCode 134
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 7. 分发糖果
- **题目**: n个孩子站成一排。给你一个整数数组ratings表示每个孩子的评分。你需要按照以下要求，给这些孩子分发糖果：每个孩子至少分配到1个糖果；相邻两个孩子评分更高的孩子会获得更多的糖果。计算并返回需要准备的最少糖果数目。
- **文件**: Code13_Candy.java/.cpp/.py
- **来源**: LeetCode 135
- **难度**: 困难
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 8. 根据身高重建队列
- **题目**: 假设有打乱顺序的一群人站成一个队列，数组people表示队列中一些人的属性（不一定按顺序）。每个people[i] = [hi, ki]表示第i个人的身高为hi，前面正好有ki个身高大于或等于hi的人。请你重新构造并返回输入数组people所表示的队列。
- **文件**: Code14_QueueReconstructionByHeight.java/.cpp/.py
- **来源**: LeetCode 406
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n^2)
- **空间复杂度**: O(logn)

### 9. 用最少数量的箭引爆气球
- **题目**: 一些球形的气球贴在一堵用XY平面表示的墙面上。墙面上的气球记录在整数数组points中，其中points[i] = [xstart, xend]表示水平直径在xstart和xend之间的气球。你不知道气球的确切y坐标。一支弓箭可以沿着x轴从不同点完全垂直地射出。在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为xstart，xend，且满足xstart ≤ x ≤ xend，则该气球会被引爆。可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。给你一个数组points，返回引爆所有气球所必须射出的最小弓箭数。
- **文件**: Code15_MinimumNumberOfArrowsToBurstBalloons.java/.cpp/.py
- **来源**: LeetCode 452
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(logn)

### 10. 跳跃游戏 II
- **题目**: 给定一个非负整数数组，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的最大长度。你的目标是使用最少的跳跃次数到达数组的最后一个位置。
- **文件**: Code16_JumpGameII.java/.cpp/.py
- **来源**: LeetCode 45
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 11. 摆动序列
- **题目**: 如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。第一个差（如果存在的话）可能是正数或负数。仅有一个元素或者含两个不等元素的序列也视作摆动序列。子序列可以通过从原始序列中删除一些（也可以不删除）元素来获得，剩下的元素保持其原始顺序。给你一个整数数组nums，返回nums中作为摆动序列的最长子序列的长度。
- **文件**: Code17_WiggleSubsequence.java/.cpp/.py
- **来源**: LeetCode 376
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 12. 种花问题
- **题目**: 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。给你一个整数数组flowerbed表示花坛，由若干0和1组成，其中0表示没种植花，1表示种植了花。另有一个数n，能否在不打破种植规则的情况下种入n朵花？能则返回true，不能则返回false。
- **文件**: Code18_CanPlaceFlowers.java/.cpp/.py
- **来源**: LeetCode 605
- **难度**: 简单
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 13. 根据字符出现频率排序
- **题目**: 给定一个字符串s，请将字符串里的字符按照出现的频率降序排列。
- **文件**: Code19_SortCharactersByFrequency.java/.cpp/.py
- **来源**: LeetCode 451
- **难度**: 中等
- **算法**: 贪心算法 + 优先队列
- **时间复杂度**: O(n + k*logk)，其中n是字符串长度，k是字符集大小
- **空间复杂度**: O(k)

### 14. 合并区间
- **题目**: 以数组intervals表示若干个区间的集合，其中单个区间为intervals[i] = [starti, endi]。请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
- **文件**: Code20_MergeIntervals.java/.cpp/.py
- **来源**: LeetCode 56
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(logn)

### 15. 最大数
- **题目**: 给定一组非负整数nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
- **文件**: Code21_LargestNumber.java/.cpp/.py
- **来源**: LeetCode 179
- **难度**: 中等
- **算法**: 贪心算法 + 自定义排序
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(n)

### 16. 合并果子
- **题目**: 在一个果园里，小明已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。小明决定把所有的果子合成一堆。每一次合并，小明可以把两堆果子合并到一起，消耗的体力等于两堆果子的重量之和。假设每个果子重量都为1，并且已知果子的种类数和每种果子的数目，你的任务是设计出合并的次序方案，使小明耗费的体力最少，并输出这个最小的体力耗费值。
- **文件**: Code22_MergeFruits.java/.cpp/.py
- **来源**: 洛谷 P1090
- **难度**: 普及-
- **算法**: 贪心算法 + 优先队列
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(n)

### 17. 雪糕的最大数量
- **题目**: 夏日炎炎，小男孩 Tony 想买一些雪糕消消暑。商店中新到 n 支雪糕，用长度为 n 的数组 costs 表示雪糕的定价，其中 costs[i] 表示第 i 支雪糕的现金价格。Tony 一共有 coins 现金可以用于消费，他想要买尽可能多的雪糕。给你价格数组 costs 和现金量 coins ，请你计算并返回 Tony 用 coins 现金能够买到的雪糕的 最大数量 。
- **文件**: Code23_MaxIceCream.java/.cpp/.py
- **来源**: LeetCode 1833
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(logn)

### 18. 减小和重新排列数组后的最大元素
- **题目**: 给你一个正整数数组 arr 。请你对 arr 执行一些操作（也可以不进行任何操作），使得数组满足以下条件：
  1. arr 中 第一个 元素必须为 1 。
  2. 任意相邻两个元素的差的绝对值 小于等于 1 ，也就是说，对于任意的 1 <= i < arr.length ，都满足 |arr[i] - arr[i-1]| <= 1 。
  请你返回执行以上操作后，在满足条件的数组中，arr 的 最大 可能元素值。
- **文件**: Code24_MaximumElementAfterDecrementingAndRearranging.java/.cpp/.py
- **来源**: LeetCode 1846
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(logn)

### 19. 划分字母区间
- **题目**: 字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。
- **文件**: Code25_PartitionLabels.java/.cpp/.py
- **来源**: LeetCode 763
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 20. 森林中的兔子
- **题目**: 森林中，每个兔子都有颜色。其中一些兔子（可能是全部）告诉你还有多少其他的兔子和自己有相同的颜色。我们将这些回答放在 answers 数组里。返回森林中兔子的最少数量。
- **文件**: Code26_RabbitsInForest.java/.cpp/.py
- **来源**: LeetCode 781
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

## 贪心算法解题思路总结

### 1. 区间问题
- 活动选择问题：按结束时间排序
- 区间合并：按开始时间排序
- 区间调度：按结束时间排序

### 2. 序列问题
- 最大子数组和：Kadane算法
- 摆动序列：统计峰值数量

### 3. 分配问题
- 分发饼干：双指针法
- 分发糖果：两次遍历法

### 4. 路径问题
- 跳跃游戏：维护最大可达位置
- 加油站：维护总油量和当前油量

### 5. 排序重构问题
- 根据身高重建队列：按身高降序，按k值升序

## 贪心算法与其他算法的对比

### 1. 贪心 vs 动态规划
- 贪心：每步都选择当前最优解，不能回退
- 动态规划：保存以前的运算结果，并根据以前的结果对当前进行选择，有回退功能

### 2. 贪心 vs 回溯
- 贪心：不考虑所有可能解，只选择当前最优
- 回溯：穷举所有可能解，找到最优解

## 贪心算法的局限性

贪心算法并不总是能得到全局最优解，它只能得到某种意义上的局部最优解。使用贪心算法需要满足以下条件：
1. 贪心选择性质
2. 最优子结构性质

## 工程化考量

### 1. 异常处理
- 输入验证：检查数组是否为空、参数是否合法
- 边界处理：处理空输入、单元素数组等特殊情况

### 2. 性能优化
- 避免重复计算
- 选择合适的数据结构
- 减少不必要的排序操作

### 3. 可读性
- 添加详细注释
- 变量命名清晰
- 代码结构模块化

### 4. 测试
- 边界测试：空数组、单元素、极端值
- 功能测试：正常输入输出
- 性能测试：大数据量测试

## 跨语言特性差异

### Java
- 优先使用Integer.compare()而不是减法比较避免溢出
- 使用PriorityQueue实现堆结构
- 注意自动装箱拆箱的性能影响

### C++
- 使用STL容器如vector、priority_queue
- 注意内存管理
- 使用<algorithm>中的排序函数

### Python
- 利用列表推导式简化代码
- 使用heapq模块实现堆操作
- 注意列表与元组的使用场景

## 补充更多贪心算法题目

### 27. 任务调度器
- **题目**: 给定一个用字符数组表示的 CPU 需要执行的任务列表。其中包含使用大写的 A-Z 字母表示的26 种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。然而，两个相同种类的任务之间必须有长度为 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。你需要计算完成所有任务所需要的最短时间。
- **文件**: Code27_TaskScheduler.java/.cpp/.py
- **来源**: LeetCode 621
- **相关题目链接**:
  - https://leetcode.cn/problems/task-scheduler/ (LeetCode 621)
  - https://www.lintcode.com/problem/task-scheduler/ (LintCode 1482)
  - https://practice.geeksforgeeks.org/problems/task-scheduler/ (GeeksforGeeks)
  - https://www.nowcoder.com/practice/6b48f8c9d2cb4a568890b73383e119cf (牛客网)
  - https://codeforces.com/problemset/problem/1165/F2 (Codeforces)
  - https://atcoder.jp/contests/abc153/tasks/abc153_e (AtCoder)
  - https://www.hackerrank.com/challenges/task-scheduler/problem (HackerRank)
  - https://www.luogu.com.cn/problem/P1043 (洛谷)
- **难度**: 中等
- **算法**: 贪心算法 + 桶思想
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 28. 柠檬水找零
- **题目**: 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，一次购买一杯。每位顾客只买一杯柠檬水，然后向你支付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零。注意，一开始你手头没有任何零钱。如果你能给每位顾客正确找零，返回 true ，否则返回 false 。
- **文件**: Code28_LemonadeChange.java/.cpp/.py
- **来源**: LeetCode 860
- **难度**: 简单
- **算法**: 贪心算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 29. 重构字符串
- **题目**: 给定一个字符串 s ，检查是否能重新排布其中的字母，使得两相邻的字符不同。若可行，输出任意可行的结果。若不可行，返回空字符串。
- **文件**: Code29_ReorganizeString.java/.cpp/.py
- **来源**: LeetCode 767
- **难度**: 中等
- **算法**: 贪心算法 + 优先队列
- **时间复杂度**: O(n * logk)，k为字符种类数
- **空间复杂度**: O(k)

### 30. 视频拼接
- **题目**: 你将会获得一系列视频片段，这些片段来自于一项持续时长为 time 秒的体育赛事。这些片段可能有所重叠，也可能长度不一。使用数组 clips 描述所有的视频片段，其中 clips[i] = [starti, endi] 表示：某个视频片段开始于 starti 并于 endi 结束。甚至可以对这些片段自由地再剪辑。例如，片段 [0, 7] 可以剪切成 [0, 1] + [1, 3] + [3, 7] 三部分。我们需要将这些片段进行再剪辑，并将剪辑后的内容拼接成覆盖整个运动过程的片段（[0, time]）。返回所需片段的最小数目，如果无法完成该任务，则返回 -1 。
- **文件**: Code30_VideoStitching.java/.cpp/.py
- **来源**: LeetCode 1024
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(n * logn)
- **空间复杂度**: O(1)

### 31. 划分数组为连续子序列
- **题目**: 给你一个按升序排序的整数数组 num（可能包含重复数字），请你将它们分割成一个或多个长度至少为 3 的子序列，其中每个子序列都由连续整数组成。如果可以完成上述分割，则返回 true ；否则，返回 false 。
- **文件**: Code31_SplitArrayIntoConsecutiveSubsequences.java/.cpp/.py
- **来源**: LeetCode 659
- **难度**: 中等
- **算法**: 贪心算法 + 哈希表
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 32. 单调递增的数字
- **题目**: 给定一个非负整数 N，找出小于或等于 N 的最大的整数，同时这个整数需要满足其各个位数上的数字是单调递增。（当且仅当每个相邻位数上的数字 x 和 y 满足 x <= y 时，我们称这个整数是单调递增的。）
- **文件**: Code32_MonotoneIncreasingDigits.java/.cpp/.py
- **来源**: LeetCode 738
- **难度**: 中等
- **算法**: 贪心算法
- **时间复杂度**: O(logN)
- **空间复杂度**: O(logN)

### 33. 移掉K位数字
- **题目**: 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。请你以字符串形式返回这个最小的数字。
- **文件**: Code33_RemoveKDigits.java/.cpp/.py
- **来源**: LeetCode 402
- **难度**: 中等
- **算法**: 贪心算法 + 单调栈
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 34. 无重叠区间（另一种解法）
- **题目**: 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。（使用不同的贪心策略）
- **文件**: Code34_NonOverlappingIntervalsII.java/.cpp/.py
- **来源**: LeetCode 435
- **难度**: 中等
- **算法**: 贪心算法（按开始时间排序）
- **时间复杂度**: O(n * logn)
- **空间复杂度**: O(1)

### 35. 用最少数量的箭引爆气球（另一种解法）
- **题目**: 使用不同的贪心策略解决气球问题
- **文件**: Code35_MinimumArrowsToBurstBalloonsII.java/.cpp/.py
- **来源**: LeetCode 452
- **难度**: 中等
- **算法**: 贪心算法（按结束时间排序）
- **时间复杂度**: O(n * logn)
- **空间复杂度**: O(1)

### 36. 分发糖果（另一种解法）
- **题目**: 使用一次遍历的贪心策略解决糖果问题
- **文件**: Code36_CandyII.java/.cpp/.py
- **来源**: LeetCode 135
- **难度**: 困难
- **算法**: 贪心算法（一次遍历）
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 37. 跳跃游戏（另一种解法）
- **题目**: 使用动态规划思想解决跳跃游戏问题
- **文件**: Code37_JumpGameII.java/.cpp/.py
- **来源**: LeetCode 55
- **难度**: 中等
- **算法**: 贪心算法（反向查找）
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 38. 最大子数组和（另一种解法）
- **题目**: 使用分治法解决最大子数组和问题
- **文件**: Code38_MaximumSubarrayII.java/.cpp/.py
- **来源**: LeetCode 53
- **难度**: 简单
- **算法**: 分治法
- **时间复杂度**: O(n * logn)
- **空间复杂度**: O(logn)

### 39. 买卖股票的最佳时机（多种变种）
- **题目**: 包含买卖股票问题的多种变种解法
- **文件**: Code39_BestTimeToBuyAndSellStockVariants.java/.cpp/.py
- **来源**: LeetCode 121, 122, 123, 188, 309, 714
- **难度**: 中等
- **算法**: 贪心算法 + 动态规划
- **时间复杂度**: O(n)
- **空间复杂度**: O(1) 或 O(n)

### 40. 区间合并（另一种解法）
- **题目**: 使用扫描线算法解决区间合并问题
- **文件**: Code40_MergeIntervalsII.java/.cpp/.py
- **来源**: LeetCode 56
- **难度**: 中等
- **算法**: 扫描线算法
- **时间复杂度**: O(n * logn)
- **空间复杂度**: O(n)

## 贪心算法在各大平台的题目分布

### LeetCode 贪心算法题目精选
1. **简单难度**: 455, 860, 605, 122, 53, 55, 121, 392, 409, 680
2. **中等难度**: 621, 767, 1024, 659, 738, 402, 435, 452, 406, 56, 45, 376, 179, 134, 135
3. **困难难度**: 632, 1383, 135, 45, 402, 659

### LintCode 贪心算法题目
1. 187. 加油站
2. 391. 数飞机
3. 919. 会议室II
4. 920. 会议室
5. 116. 跳跃游戏

### HackerRank 贪心算法题目
1. Greedy Florist
2. Luck Balance
3. Max Min
4. Reverse Shuffle Merge
5. Sherlock and The Beast

### AtCoder 贪心算法题目
1. ABC 167D - Teleporter
2. ABC 175C - Walking Takahashi
3. ABC 186D - Sum of difference
4. ABC 194D - Journey

### USACO 贪心算法题目
1. Barn Repair
2. Mixing Milk
3. The Trip
4. Ski Course Design

### 洛谷 贪心算法题目
1. P1090 合并果子
2. P1223 排队接水
3. P1803 凌乱的yyy
4. P2240 部分背包问题

### CodeChef 贪心算法题目
1. CHEFSTON - Chef and Stones
2. TADELIVE - Delivery Man
3. MAXSC - Maximum Score
4. CHEFAPAR - Chef and His Apartment

### SPOJ 贪心算法题目
1. AGGRCOW - Aggressive cows
2. BUSYMAN - I AM VERY BUSY
3. EXPEDI - Expedition
4. GCJ101BB - Picking Up Chicks

### Project Euler 贪心算法题目
1. Problem 31: Coin sums
2. Problem 76: Counting summations
3. Problem 77: Prime summations

### HackerEarth 贪心算法题目
1. Monk and the Magical Candy Bags
2. Little Monk and Balanced Parentheses
3. The Amazing Race

### 牛客网 贪心算法题目
1. 合并果子
2. 排队打水
3. 区间选点
4. 最大不相交区间数量

### 杭电OJ 贪心算法题目
1. HDU 1009: FatMouse' Trade
2. HDU 1050: Moving Tables
3. HDU 1051: Wooden Sticks
4. HDU 2037: 今年暑假不AC

### POJ 贪心算法题目
1. POJ 1328: Radar Installation
2. POJ 1700: Crossing River
3. POJ 2393: Yogurt factory
4. POJ 3040: Allowance

### Codeforces 贪心算法题目
1. 158B - Taxi
2. 489C - Given Length and Sum of Digits
3. 550C - Divisibility by Eight
4. 706B - Interesting drink

### 剑指Offer 贪心算法题目
1. 剑指 Offer 14- I. 剪绳子
2. 剑指 Offer 45. 把数组排成最小的数
3. 剑指 Offer 63. 股票的最大利润

## 贪心算法题型分类与解题技巧

### 1. 区间调度类问题
**特征**: 涉及时间区间、活动安排等
**解题技巧**:
- 按结束时间排序（经典贪心）
- 按开始时间排序（特定场景）
- 扫描线算法

**典型题目**:
- 无重叠区间
- 用最少数量的箭引爆气球
- 视频拼接
- 会议室安排

### 2. 分配类问题
**特征**: 资源分配、任务分配等
**解题技巧**:
- 双指针法
- 优先队列
- 排序 + 贪心

**典型题目**:
- 分发饼干
- 分发糖果
- 任务调度器
- 柠檬水找零

### 3. 序列重构类问题
**特征**: 重新排列序列满足特定条件
**解题技巧**:
- 频率统计
- 自定义排序
- 单调栈

**典型题目**:
- 重构字符串
- 最大数
- 移掉K位数字
- 单调递增的数字

### 4. 路径规划类问题
**特征**: 跳跃、路径选择等
**解题技巧**:
- 维护最大可达位置
- 反向查找
- 动态规划思想

**典型题目**:
- 跳跃游戏
- 加油站
- 视频拼接

### 5. 数值优化类问题
**特征**: 数值计算、最优化等
**解题技巧**:
- Kadane算法
- 贪心选择
- 数学推导

**典型题目**:
- 最大子数组和
- 买卖股票的最佳时机
- 划分数组为连续子序列

## 贪心算法工程化考量

### 1. 异常处理与边界场景
**空输入处理**: 检查数组是否为空，返回默认值
**单元素数组**: 特殊处理边界情况
**极端值**: 处理最大值、最小值、重复值

### 2. 性能优化策略
**时间复杂度优化**:
- 避免嵌套循环
- 使用合适的数据结构
- 减少不必要的排序

**空间复杂度优化**:
- 原地操作
- 复用空间
- 流式处理

### 3. 代码可读性与维护性
**变量命名**: 使用有意义的变量名
**注释规范**: 关键步骤添加注释
**模块化设计**: 将功能拆分为独立方法

### 4. 测试策略
**单元测试**: 覆盖各种边界情况
**性能测试**: 大数据量测试
**随机测试**: 验证算法正确性

## 跨语言实现差异

### Java 实现特点
- 使用 `Integer.compare()` 避免整数溢出
- `PriorityQueue` 实现堆结构
- 注意自动装箱拆箱的性能影响

### C++ 实现特点
- 使用 STL 容器 (`vector`, `priority_queue`)
- 注意内存管理和指针使用
- 使用 `<algorithm>` 中的排序函数

### Python 实现特点
- 利用列表推导式简化代码
- 使用 `heapq` 模块实现堆操作
- 注意列表与元组的使用场景

## 贪心算法与机器学习联系

### 1. 决策树算法
贪心算法用于特征选择，每次选择最优划分特征

### 2. K-means聚类
每次选择距离最近的质心，属于贪心策略

### 3. 贪心搜索
在强化学习中用于策略选择

### 4. 图神经网络
节点采样策略使用贪心算法

### 5. 特征选择
前向选择、后向消除都是贪心策略

## 数学与机器学习联系

贪心算法在以下领域有广泛应用：
1. 决策树算法中的特征选择
2. K-means聚类算法
3. 贪心搜索在强化学习中的应用
4. 图神经网络中的采样策略
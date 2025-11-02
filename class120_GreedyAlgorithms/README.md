# Class089 贪心算法专题

## 🎯 概述

Class089专注于贪心算法的学习和应用。贪心算法是一种在每一步选择中都采取在当前状态下最好或最优（即最有利）的选择，从而希望导致结果是最好或最优的算法。本章节通过多个经典题目，帮助深入理解贪心算法的设计思想和应用场景。

## 📚 题目列表

### 1. 最大数 (Largest Number)
- **文件**: 
  - [Code01_LargestNumber.java](Code01_LargestNumber.java)
  - [Code01_LargestNumber.cpp](Code01_LargestNumber.cpp)
  - [Code01_LargestNumber.py](Code01_LargestNumber.py)
- **题目链接**: https://leetcode.cn/problems/largest-number/
- **难度**: 中等
- **描述**: 给定一组非负整数，重新排列每个数的顺序使之组成一个最大的整数
- **解法**: 贪心算法 + 自定义排序
- **时间复杂度**: O(n*logn*m)
- **空间复杂度**: O(n*m)
- **是否最优解**: 是

### 2. 两地调度 (Two City Scheduling)
- **文件**: [Code02_TwoCityScheduling.java](Code02_TwoCityScheduling.java)
- **题目链接**: https://leetcode.cn/problems/two-city-scheduling/
- **难度**: 中等
- **描述**: 公司计划面试2n个人，给定一个数组 costs，其中costs[i]=[aCosti, bCosti]表示第i人飞往a市的费用为aCosti，飞往b市的费用为bCosti。返回将每个人都飞到a、b中某座城市的最低费用，要求每个城市都有n人抵达
- **解法**: 贪心算法
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(n)
- **是否最优解**: 是

### 3. 吃掉N个橘子的最少天数 (Minimum Number of Days to Eat N Oranges)
- **文件**: [Code03_MinimumNumberEatOranges.java](Code03_MinimumNumberEatOranges.java)
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-days-to-eat-n-oranges/
- **难度**: 困难
- **描述**: 厨房里总共有 n 个橘子，你决定每一天选择如下方式之一吃这些橘子：1）吃掉一个橘子；2) 如果剩余橘子数 n 能被 2 整除，那么你可以吃掉 n/2 个橘子；3) 如果剩余橘子数 n 能被 3 整除，那么你可以吃掉 2*(n/3) 个橘子。每天你只能从以上 3 种方案中选择一种方案。请你返回吃掉所有 n 个橘子的最少天数
- **解法**: 贪心算法 + 记忆化搜索
- **时间复杂度**: O(logn)
- **空间复杂度**: O(logn)
- **是否最优解**: 是

### 4. 会议室II (Meeting Rooms II)
- **文件**: [Code04_MeetingRoomsII.java](Code04_MeetingRoomsII.java)
- **题目链接**: https://leetcode.cn/problems/meeting-rooms-ii/
- **难度**: 中等
- **描述**: 给你一个会议时间安排的数组 intervals，每个会议时间都会包括开始和结束的时间intervals[i]=[starti, endi]，返回所需会议室的最小数量
- **解法**: 贪心算法 + 最小堆
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(n)
- **是否最优解**: 是

### 5. 课程表III (Course Schedule III)
- **文件**: [Code05_CourseScheduleIII.java](Code05_CourseScheduleIII.java)
- **题目链接**: https://leetcode.cn/problems/course-schedule-iii/
- **难度**: 困难
- **描述**: 这里有n门不同的在线课程，按从1到n编号。给你一个数组courses，其中courses[i]=[durationi, lastDayi]表示第i门课将会持续上durationi天课，并且必须在不晚于lastDayi的时候完成。你的学期从第 1 天开始，且不能同时修读两门及两门以上的课程。返回你最多可以修读的课程数目
- **解法**: 贪心算法 + 最大堆
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(n)
- **是否最优解**: 是

### 6. 连接棒材的最低费用 (Minimum Cost to Connect Sticks)
- **文件**: 
  - [Code06_MinimumCostToConnectSticks1.java](Code06_MinimumCostToConnectSticks1.java) (LeetCode版本)
  - [Code06_MinimumCostToConnectSticks2.java](Code06_MinimumCostToConnectSticks2.java) (洛谷版本)
- **题目链接**: 
  - LeetCode: https://leetcode.cn/problems/minimum-cost-to-connect-sticks/
  - 洛谷: https://www.luogu.com.cn/problem/P1090
- **难度**: 中等
- **描述**: 你有一些长度为正整数的棍子，这些长度以数组sticks的形式给出。你可以通过支付x+y的成本将任意两个长度为x和y的棍子连接成一个棍子。你必须连接所有的棍子，直到剩下一个棍子。返回以这种方式将所有给定的棍子连接成一个棍子的最小成本
- **解法**: 贪心算法 + 最小堆
- **时间复杂度**: O(n*logn)
- **空间复杂度**: O(n)
- **是否最优解**: 是

### 7. 买卖股票的最佳时机 II (Best Time to Buy and Sell Stock II)
- **文件**: 
  - [Code07_BestTimeBuySellStockII.java](Code07_BestTimeBuySellStockII.java)
  - [Code07_BestTimeBuySellStockII.cpp](Code07_BestTimeBuySellStockII.cpp)
  - [Code07_BestTimeBuySellStockII.py](Code07_BestTimeBuySellStockII.py)
- **题目链接**: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
- **难度**: 中等
- **描述**: 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。你也可以先购买，然后在 同一天 出售。返回 你能获得的最大利润
- **解法**: 贪心算法 + 累加正收益
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **是否最优解**: 是

### 8. 分发饼干 (Assign Cookies)
- **文件**: 
  - [Code08_AssignCookies.java](Code08_AssignCookies.java)
  - [Code08_AssignCookies.cpp](Code08_AssignCookies.cpp)
  - [Code08_AssignCookies.py](Code08_AssignCookies.py)
- **题目链接**: https://leetcode.cn/problems/assign-cookies/
- **难度**: 简单
- **描述**: 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；并且每块饼干j，都有一个尺寸s[j]。如果s[j] >= g[i]，我们可以将这个饼干j分配给孩子i。目标是尽可能满足越多数量的孩子，并输出这个最大数值
- **解法**: 贪心算法 + 双指针
- **时间复杂度**: O(m*logm + n*logn)
- **空间复杂度**: O(1)
- **是否最优解**: 是

### 9. 柠檬水找零 (Lemonade Change)
- **文件**: 
  - [Code09_LemonadeChange.java](Code09_LemonadeChange.java)
  - [Code09_LemonadeChange.cpp](Code09_LemonadeChange.cpp)
  - [Code09_LemonadeChange.py](Code09_LemonadeChange.py)
- **题目链接**: https://leetcode.cn/problems/lemonade-change/
- **难度**: 简单
- **描述**: 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。注意，一开始你手头没有任何零钱。给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。如果你能给每位顾客正确找零，返回 true ，否则返回 false
- **解法**: 贪心算法 + 计数策略
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **是否最优解**: 是

## 🧠 贪心算法核心思想

### 1. 基本概念
贪心算法（greedy algorithm，又称贪婪算法）是指在对问题求解时，总是做出在当前看来是最好的选择。也就是说，不从整体最优上加以考虑，算法得到的是在某种意义上的局部最优解。

### 2. 适用场景
贪心算法适用于具有以下性质的问题：
1. **贪心选择性质**：所求问题的整体最优解可以通过一系列局部最优的选择得到
2. **最优子结构**：问题的最优解包含子问题的最优解

### 3. 解题步骤
1. 将问题分解为若干个子问题
2. 找出适合的贪心策略
3. 求解每一个子问题的最优解
4. 将局部最优解堆叠成全局最优解

### 4. 常见题型
1. **区间调度问题**：活动安排、区间合并等
2. **哈夫曼编码**：合并果子等
3. **分数背包**：性价比最高的选择
4. **股票买卖**：收集所有上升波段
5. **字典序问题**：移掉K位数字等
6. **序列重构**：根据身高重建队列等

## 🛠️ 工程化考量

### 1. 异常处理
- 空输入处理：检查输入是否为空或null
- 边界条件：处理数组为空、单个元素等特殊情况
- 输入验证：检查输入参数的有效性

### 2. 性能优化
- 排序优化：合理选择排序算法和比较器
- 避免重复计算：缓存计算结果
- 数据结构选择：根据场景选择合适的数据结构

### 3. 可配置性
- 比较器定制：通过自定义比较器支持不同的排序需求
- 参数化设计：通过参数控制行为

### 4. 线程安全
- 在多线程环境中使用时，需要考虑同步机制

### 5. 内存管理
- 及时清理不需要的对象，避免内存泄漏

### 6. 代码可读性
- 清晰的变量命名和注释
- 模块化设计，将复杂逻辑分解为独立方法

### 7. 单元测试
- 覆盖各种边界情况和异常输入
- 验证时间和空间复杂度是否符合预期

### 8. 跨语言特性
- Java: PriorityQueue, Arrays.sort
- Python: heapq, sorted
- C++: priority_queue, sort

### 9. 调试技巧
- 打印中间状态用于调试
- 使用断言验证中间结果

### 10. 与标准库对比
- 理解标准库实现的优势和局限性
- 在性能要求极高时考虑自实现数据结构

## 📈 复杂度分析总结

| 问题类型 | 时间复杂度 | 空间复杂度 | 最优解 |
|----------|------------|------------|--------|
| 最大数 | O(n*logn*m) | O(n*m) | 是 |
| 两地调度 | O(n*logn) | O(n) | 是 |
| 吃橘子 | O(logn) | O(logn) | 是 |
| 会议室II | O(n*logn) | O(n) | 是 |
| 课程表III | O(n*logn) | O(n) | 是 |
| 连接棒材 | O(n*logn) | O(n) | 是 |

## 🌐 扩展题目平台

### LeetCode 贪心题目
1. 11. 盛最多水的容器
2. 44. 通配符匹配
3. 45. 跳跃游戏 II
4. 53. 最大子数组和
5. 55. 跳跃游戏
6. 122. 买卖股票的最佳时机 II
7. 134. 加油站
8. 135. 分发糖果
9. 316. 去除重复字母
10. 376. 摆动序列
11. 402. 移掉K位数字
12. 406. 根据身高重建队列
13. 435. 无重叠区间
14. 452. 用最少数量的箭引爆气球
15. 455. 分发饼干
16. 502. IPO
17. 561. 数组拆分 I
18. 621. 任务调度器
19. 649. Dota2 参议院
20. 714. 买卖股票的最佳时机含手续费
21. 738. 单调递增的数字
22. 763. 划分字母区间
23. 860. 柠檬水找零
24. 861. 翻转矩阵后的得分
25. 870. 优势洗牌
26. 948. 令牌放置
27. 968. 监控二叉树
28. 1005. K 次取反后最大化的数组和
29. 1029. 两地调度
30. 1053. 交换一次的先前排列
31. 1094. 拼车
32. 1221. 分割平衡字符串
33. 1247. 交换字符使得字符串相同
34. 1328. 破坏回文串
35. 1405. 最长快乐字符串
36. 1414. 和为 K 的最少斐波那契数字数目
37. 1518. 换酒问题
38. 1529. 灯泡开关 IV
39. 1561. 你可以获得的最大硬币数目
40. 1578. 避免重复字母的最小删除成本
41. 1605. 给定行和列的和求可行矩阵
42. 1647. 字符频次唯一的最小删除次数
43. 1663. 具有给定数值的最小字符串
44. 1702. 修改后的最大二进制字符串
45. 1710. 卡车上的最大单元数
46. 1727. 重新排列后的最大子矩阵
47. 1798. 你能构造出连续值的最大数目
48. 1833. 雪糕的最大数量
49. 1846. 减小和重新排列数组后的最大元素
50. 1877. 数组中最大数对和的最小值

### 洛谷贪心题目
1. P1223 排队接水
2. P1803 凌乱的yyy / 线段覆盖
3. P1090 合并果子
4. P1478 陶陶摘苹果（升级版）
5. P3817 小A的糖果
6. P1106 删数问题
7. P5019 铺设道路
8. P1208 混合牛奶
9. P1094 纪念品分组
10. P4995 跳跳！

### Codeforces 贪心题目
1. 479C - Exams
2. 489B - BerSU Ball
3. 478B - Random Teams
4. 441C - Valera and Tubes
5. 454B - Little Pony and Sort by Shift
6. 476B - Dreamoon and WiFi
7. 437C - The Child and Toy
8. 459B - Pashmak and Flowers
9. 463B - Caisa and Pylons
10. 448B - Suffix Structures

### AtCoder 贪心题目
1. ABC143D - Triangles
2. ABC153D - Caracal vs Monster
3. ABC173D - Chat in a Circle
4. ABC164D - Multiple of 2019
5. ABC121C - Energy Drink Collector

## 🏆 总结

贪心算法是算法设计中的一种重要思想，它通过每一步的局部最优选择来达到全局最优解。掌握贪心算法需要：
1. 熟悉各种经典题型和解法
2. 理解贪心策略的正确性证明
3. 多做练习，积累经验
4. 注意边界条件和特殊情况的处理

通过系统学习和大量练习class089中的题目以及扩展题目，可以全面掌握贪心算法的应用技巧，为解决实际问题和面试打下坚实基础。

更详细的扩展题目分析请参考 [ExtendedProblems.md](ExtendedProblems.md) 文件。
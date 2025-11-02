# 差分数组算法详解与题目实现

## 算法简介

差分数组是一种重要的算法技巧，它是前缀和的逆运算。对于数组`a`，其差分数组`b`定义为：
- `b[0] = a[0]`
- `b[i] = a[i] - a[i-1]` (i > 0)

### 核心思想

差分数组主要用于快速处理区间加减操作：
- 对数组区间`[l, r]`中的每个数加上`x`，可以通过以下操作实现：
  1. `b[l] += x`
  2. `b[r+1] -= x` (如果`r+1`在数组范围内)
  3. 然后通过计算差分数组的前缀和得到更新后的原数组

### 应用场景

1. 区间更新操作
2. 大规模数据处理
3. 需要频繁进行区间加减的场景

## 时间复杂度分析

- 构造差分数组: O(n)
- 每次区间更新操作: O(1)
- 还原原数组(通过前缀和): O(n)
- 总时间复杂度: O(n + m) 其中 m 是操作次数

## 空间复杂度分析

- 需要额外的差分数组空间: O(n)

## 已实现题目列表

### 题目1: 航班预订统计 (Corporate Flight Bookings)
**题目来源**: LeetCode 1109  
**题目链接**: https://leetcode.com/problems/corporate-flight-bookings/  
**题目描述**: 给定航班预订表，计算每个航班预定的座位总数。  
**实现文件**: 
- [Code01_CorporateFlightBookings.java](Code01_CorporateFlightBookings.java)
- [Code01_CorporateFlightBookings.cpp](Code01_CorporateFlightBookings.cpp)
- [Code01_CorporateFlightBookings.py](Code01_CorporateFlightBookings.py)

### 题目2: 等差数列差分
**题目来源**: 洛谷 P4231  
**题目链接**: https://www.luogu.com.cn/problem/P4231  
**题目描述**: 在区间上添加等差数列，使用差分数组处理。  
**实现文件**: 
- [Code02_ArithmeticSequenceDifference.java](Code02_ArithmeticSequenceDifference.java)
- [Code02_ArithmeticSequenceDifference.cpp](Code02_ArithmeticSequenceDifference.cpp)
- [Code02_ArithmeticSequenceDifference.py](Code02_ArithmeticSequenceDifference.py)

### 题目3: 水位高度计算
**题目来源**: 洛谷 P5026  
**题目链接**: https://www.luogu.com.cn/problem/P5026  
**题目描述**: 朋友落水后对水面产生影响，使用偏移量差分数组处理。  
**实现文件**: 
- [Code03_WaterHeight.java](Code03_WaterHeight.java)
- [Code03_WaterHeight.cpp](Code03_WaterHeight.cpp)
- [Code03_WaterHeight.py](Code03_WaterHeight.py)

### 题目4: 人口最多的年份 (Maximum Population Year)
**题目来源**: LeetCode 1854  
**题目链接**: https://leetcode.com/problems/maximum-population-year/  
**题目描述**: 根据人员出生和死亡年份计算人口最多且最早的年份。  
**实现文件**: 
- [Code04_MaximumPopulationYear.java](Code04_MaximumPopulationYear.java)
- [Code04_MaximumPopulationYear.cpp](Code04_MaximumPopulationYear.cpp)
- [Code04_MaximumPopulationYear.py](Code04_MaximumPopulationYear.py)

### 题目5: 数组操作 (Array Manipulation)
**题目来源**: HackerRank  
**题目链接**: https://www.hackerrank.com/challenges/crush/problem  
**题目描述**: 对数组进行区间加法操作，求操作后数组中的最大值。  
**实现文件**: 
- [Code05_HackerRankArrayManipulation.java](Code05_HackerRankArrayManipulation.java)
- [Code05_HackerRankArrayManipulation.cpp](Code05_HackerRankArrayManipulation.cpp)
- [Code05_HackerRankArrayManipulation.py](Code05_HackerRankArrayManipulation.py)

### 题目6: 拼车 (Car Pooling)
**题目来源**: LeetCode 1094  
**题目链接**: https://leetcode.com/problems/car-pooling/  
**题目描述**: 判断是否可以在所有给定的行程中接送所有乘客。  
**实现文件**: 
- [Code06_CarPooling.java](Code06_CarPooling.java)
- [Code06_CarPooling.cpp](Code06_CarPooling.cpp)
- [Code06_CarPooling.py](Code06_CarPooling.py)

### 题目7: 区间加法 (Range Addition)
**题目来源**: LeetCode 370  
**题目链接**: https://leetcode.com/problems/range-addition/  
**题目描述**: 对数组进行多次区间加法操作，返回最终数组。  
**实现文件**: 
- [Code07_RangeAddition.java](Code07_RangeAddition.java)
- [Code07_RangeAddition.cpp](Code07_RangeAddition.cpp)
- [Code07_RangeAddition.py](Code07_RangeAddition.py)

### 题目8: 会议室预定 (Meeting Rooms II)
**题目来源**: LeetCode 253  
**题目链接**: https://leetcode.com/problems/meeting-rooms-ii/  
**题目描述**: 给定一系列会议的开始和结束时间，计算需要的最小会议室数量。  
**实现文件**: 
- [Code08_MeetingRoomsII.java](Code08_MeetingRoomsII.java)
- [Code08_MeetingRoomsII.cpp](Code08_MeetingRoomsII.cpp)
- [Code08_MeetingRoomsII.py](Code08_MeetingRoomsII.py)

### 题目9: 学生出勤记录 III
**题目来源**: LeetCode 1115  
**题目链接**: https://leetcode.com/problems/stamping-the-sequence/  
**题目描述**: 给定一个字符串 s 和一个字符串 stamp，我们希望在 s 中通过盖章操作将其转换为 target 字符串。每次盖章操作可以选择 s 中的任意位置，并将 stamp 的字符覆盖到 s 的对应位置。返回一个可能的盖章序列，使得 s 可以转换为 target。  
**实现文件**: 
- [Code09_StampingTheSequence.java](Code09_StampingTheSequence.java)
- [Code09_StampingTheSequence.cpp](Code09_StampingTheSequence.cpp)
- [Code09_StampingTheSequence.py](Code09_StampingTheSequence.py)

### 题目10: 供暖器 (Heaters)
**题目来源**: LeetCode 475  
**题目链接**: https://leetcode.com/problems/heaters/  
**题目描述**: 冬季已经来临。你的任务是设计一个有固定加热半径的供暖器向所有房屋供暖。现在，给出位于一条水平线上的房屋和供暖器的位置，找到可以覆盖所有房屋的最小加热半径。  
**实现文件**: 
- [Code10_Heaters.java](Code10_Heaters.java)
- [Code10_Heaters.cpp](Code10_Heaters.cpp)
- [Code10_Heaters.py](Code10_Heaters.py)

### 题目11: 区间和的个数
**题目来源**: LeetCode 327  
**题目链接**: https://leetcode.com/problems/count-of-range-sum/  
**题目描述**: 给定一个整数数组 nums 和两个整数 lower 和 upper，返回区间和在 [lower, upper] 范围内的子数组个数。  
**实现文件**: 
- [Code11_CountOfRangeSum.java](Code11_CountOfRangeSum.java)
- [Code11_CountOfRangeSum.cpp](Code11_CountOfRangeSum.cpp)
- [Code11_CountOfRangeSum.py](Code11_CountOfRangeSum.py)

### 题目12: 二维区域和检索 - 矩阵不可变
**题目来源**: LeetCode 304  
**题目链接**: https://leetcode.com/problems/range-sum-query-2d-immutable/  
**题目描述**: 给定一个二维矩阵，计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2) 。  
**实现文件**: 
- [Code12_RangeSumQuery2DImmutable.java](Code12_RangeSumQuery2DImmutable.java)
- [Code12_RangeSumQuery2DImmutable.cpp](Code12_RangeSumQuery2DImmutable.cpp)
- [Code12_RangeSumQuery2DImmutable.py](Code12_RangeSumQuery2DImmutable.py)

### 题目13: 二维区域和检索 - 矩阵可变
**题目来源**: LeetCode 308  
**题目链接**: https://leetcode.com/problems/range-sum-query-2d-mutable/  
**题目描述**: 给定一个二维矩阵，实现一个类 NumMatrix 来计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2) 。该类支持以下两种操作：更新矩阵中的某个元素和计算子矩阵的和。  
**实现文件**: 
- [Code13_RangeSumQuery2DMutable.java](Code13_RangeSumQuery2DMutable.java)
- [Code13_RangeSumQuery2DMutable.cpp](Code13_RangeSumQuery2DMutable.cpp)
- [Code13_RangeSumQuery2DMutable.py](Code13_RangeSumQuery2DMutable.py)

### 题目14: 灯泡开关 IV
**题目来源**: LeetCode 1529  
**题目链接**: https://leetcode.com/problems/bulb-switcher-iv/  
**题目描述**: 房间中有 n 个灯泡，初始状态为关闭。每次操作可以选择一个位置，并将该位置之后的所有灯泡的状态反转。求将灯泡变为目标状态所需的最少操作次数。  
**实现文件**: 
- [Code14_BulbSwitcherIV.java](Code14_BulbSwitcherIV.java)
- [Code14_BulbSwitcherIV.cpp](Code14_BulbSwitcherIV.cpp)
- [Code14_BulbSwitcherIV.py](Code14_BulbSwitcherIV.py)

### 题目15: 墙与门
**题目来源**: LeetCode 286  
**题目链接**: https://leetcode.com/problems/walls-and-gates/  
**题目描述**: 给定一个包含 0 和 -1 的二维网格，其中 0 表示门，-1 表示墙，INF 表示空房间。找出每个空房间到最近的门的距离。如果无法到达门，则保留 INF。  
**实现文件**: 
- [Code15_WallsAndGates.java](Code15_WallsAndGates.java)
- [Code15_WallsAndGates.cpp](Code15_WallsAndGates.cpp)
- [Code15_WallsAndGates.py](Code15_WallsAndGates.py)

### 题目16: 区间子数组个数
**题目来源**: LeetCode 795  
**题目链接**: https://leetcode.com/problems/number-of-subarrays-with-bounded-maximum/  
**题目描述**: 给定一个元素都是正整数的数组 A ，其表示商品价格的数组，以及两个正整数 L 和 R 。求满足条件的子数组的个数：子数组中的最大值在区间 [L, R] 之间。  
**实现文件**: 
- [Code16_NumberOfSubarraysWithBoundedMaximum.java](Code16_NumberOfSubarraysWithBoundedMaximum.java)
- [Code16_NumberOfSubarraysWithBoundedMaximum.cpp](Code16_NumberOfSubarraysWithBoundedMaximum.cpp)
- [Code16_NumberOfSubarraysWithBoundedMaximum.py](Code16_NumberOfSubarraysWithBoundedMaximum.py)

### 题目18: 我的日程安排表 III (My Calendar III)
**题目来源**: LeetCode 732  
**题目链接**: https://leetcode.com/problems/my-calendar-iii/  
**题目描述**: 实现一个 MyCalendarThree 类来存放你的日程安排，你可以一直添加新的日程安排。当 k 个日程安排有一些时间上的交叉时（例如 k 个日程安排都在同一时间内），就会产生 k 次预订。每次调用 book 方法时，返回一个整数 k，表示当前日历中存在的最大交叉预订次数。  
**实现文件**: 
- [Code18_MyCalendarIII.java](Code18_MyCalendarIII.java)
- [Code18_MyCalendarIII.cpp](Code18_MyCalendarIII.cpp)
- [Code18_MyCalendarIII.py](Code18_MyCalendarIII.py)

### 题目19: 分割数组的方案数 (Number of Ways to Split Array)
**题目来源**: LeetCode 2270  
**题目链接**: https://leetcode.com/problems/number-of-ways-to-split-array/  
**题目描述**: 给你一个下标从 0 开始长度为 n 的整数数组 nums。如果前 i + 1 个元素的和大于等于剩下的 n - i - 1 个元素的和，那么 nums 在下标 i 处有一个合法分割。请你返回 nums 中的合法分割方案数。  
**实现文件**: 
- [Code19_NumberOfWaysToSplitArray.java](Code19_NumberOfWaysToSplitArray.java)
- [Code19_NumberOfWaysToSplitArray.cpp](Code19_NumberOfWaysToSplitArray.cpp)
- [Code19_NumberOfWaysToSplitArray.py](Code19_NumberOfWaysToSplitArray.py)

### 题目20: Greg and Array
**题目来源**: Codeforces 296C  
**题目链接**: https://codeforces.com/contest/296/problem/C  
**题目描述**: Greg 有一个长度为 n 的数组 a，初始值都为 0。他还有 m 个操作，每个操作是一个三元组 (l, r, d)，表示将区间 [l, r] 中的每个元素加上 d。然后他有 k 个指令，每个指令是一个二元组 (x, y)，表示执行操作 x 到操作 y 各一次。请输出执行完所有指令后的数组。  
**实现文件**: 
- [Code20_GregAndArray.java](Code20_GregAndArray.java)
- [Code20_GregAndArray.cpp](Code20_GregAndArray.cpp)
- [Code20_GregAndArray.py](Code20_GregAndArray.py)

### 题目21: A Simple Problem with Integers
**题目来源**: POJ 3468  
**题目链接**: http://poj.org/problem?id=3468  
**题目描述**: 给定一个长度为 N 的数列 A，以及 M 条指令，每条指令可能是以下两种之一："C a b c" 表示给 [a, b] 区间中的每一个数加上 c；"Q a b" 表示询问 [a, b] 区间中所有数的和。  
**实现文件**: 
- [Code21_POJ3468.java](Code21_POJ3468.java)
- [Code21_POJ3468.cpp](Code21_POJ3468.cpp)
- [Code21_POJ3468.py](Code21_POJ3468.py)

### 题目22: 牛客网数组操作问题
**题目来源**: 牛客网  
**题目描述**: 给定一个长度为 n 的数组，初始值都为 0。有 m 次操作，每次操作给出三个数 l, r, k，表示将数组下标从 l 到 r 的所有元素都加上 k。求执行完所有操作后数组中的最大值。  
**实现文件**: 
- [Code22_NowCoderArrayManipulation.java](Code22_NowCoderArrayManipulation.java)
- [Code22_NowCoderArrayManipulation.cpp](Code22_NowCoderArrayManipulation.cpp)
- [Code22_NowCoderArrayManipulation.py](Code22_NowCoderArrayManipulation.py)

### 题目23: 差分
**题目来源**: AcWing 797  
**题目链接**: https://www.acwing.com/problem/content/799/  
**题目描述**: 输入一个长度为 n 的整数序列。接下来输入 m 个操作，每个操作包含三个整数 l, r, c，表示将序列中 [l, r] 之间的每个数加上 c。请你输出进行完所有操作后的序列。  
**实现文件**: 
- [Code23_AcWingDifferenceArray.java](Code23_AcWingDifferenceArray.java)
- [Code23_AcWingDifferenceArray.cpp](Code23_AcWingDifferenceArray.cpp)
- [Code23_AcWingDifferenceArray.py](Code23_AcWingDifferenceArray.py)

## 算法技巧总结

### 1. 基础差分数组
适用于简单的区间加减操作，时间复杂度O(1)进行区间更新。核心操作：`diff[l] += x; diff[r+1] -= x;`

### 2. 二维差分数组
适用于二维矩阵的区域更新操作，通过容斥原理进行标记。核心操作涉及四个角的标记。

### 3. 等差数列差分
适用于区间上添加等差数列的操作，需要使用特殊的差分标记方式。核心操作涉及四个标记点。

### 4. 偏移量处理
在处理可能涉及负索引的操作时，通过添加偏移量避免边界讨论。常用于复杂区间更新问题。

### 5. 多层差分操作
适用于需要处理操作的操作（如Codeforces 296C），使用两层差分数组来优化多层区间更新。

### 6. 树状数组结合差分
适用于需要支持区间更新和区间查询的问题（如POJ 3468），使用树状数组维护差分信息。

### 7. 动态区间统计
适用于需要实时统计区间重叠次数的问题（如LeetCode 732），使用有序映射维护差分标记。

## 工程化考量

### 1. 异常处理
- 输入参数合法性验证
- 边界条件处理
- 错误信息提示

### 2. 性能优化
- 大规模数据优化策略
- 内存使用优化
- 算法常数项优化

### 3. 可测试性
- 单元测试方法
- 测试用例设计
- 自动化测试框架

### 4. 可扩展性
- 模块化设计
- 接口抽象
- 配置化参数

## 相关题目平台

### LeetCode
1. [1109. 航班预订统计](https://leetcode.com/problems/corporate-flight-bookings/)
2. [1854. 人口最多的年份](https://leetcode.com/problems/maximum-population-year/)
3. [1094. 拼车](https://leetcode.com/problems/car-pooling/)
4. [370. 区间加法](https://leetcode.com/problems/range-addition/)
5. [732. 我的日程安排表 III](https://leetcode.com/problems/my-calendar-iii/)
6. [2270. 分割数组的方案数](https://leetcode.com/problems/number-of-ways-to-split-array/)

### HackerRank
1. [Array Manipulation](https://www.hackerrank.com/challenges/crush/problem)

### 洛谷
1. [P4231 三步必杀](https://www.luogu.com.cn/problem/P4231)
2. [P5026 Lycanthropy](https://www.luogu.com.cn/problem/P5026)

### Codeforces
1. [296C. Greg and Array](https://codeforces.com/contest/296/problem/C)

### POJ (北京大学在线评测系统)
1. [3468. A Simple Problem with Integers](http://poj.org/problem?id=3468)

### AcWing (算法竞赛进阶指南)
1. [797. 差分](https://www.acwing.com/problem/content/799/)

### 牛客网
1. 数组操作问题（类似HackerRank Array Manipulation）
2. [【模板】差分](https://www.nowcoder.com/practice/4bbc401a5df140309edd6f14debdba42)

### 其他平台
1. 各大高校OJ相关题目
2. 赛码、计蒜客等平台相关题目

## 算法深度分析与总结

### 一、差分数组核心原理

#### 1.1 基本定义
对于数组 `a[1..n]`，其差分数组 `b[1..n]` 定义为：
- `b[1] = a[1]`
- `b[i] = a[i] - a[i-1]` (i > 1)

#### 1.2 区间更新原理
对区间 `[l, r]` 加上值 `x` 的操作：
- `b[l] += x`
- `b[r+1] -= x` (如果 r+1 ≤ n)

#### 1.3 还原原数组
通过前缀和操作还原：`a[i] = b[1] + b[2] + ... + b[i]`

### 二、时间复杂度分析对比

| 方法 | 构造时间 | 单次更新 | 单次查询 | 总复杂度(m次操作) |
|------|----------|----------|----------|------------------|
| 暴力法 | O(1) | O(n) | O(1) | O(nm) |
| 差分数组 | O(n) | O(1) | O(n) | O(n+m) |

### 三、空间复杂度分析
- **基础差分数组**: O(n)
- **二维差分数组**: O(n²)
- **树状数组结合差分**: O(n)
- **动态差分映射**: O(k) - k为不同时间点数量

### 四、工程化深度考量

#### 4.1 异常处理与边界检查
- **输入验证**: 验证数组长度、操作数量、索引范围的合法性
- **边界处理**: 确保索引不越界，处理n=0, m=0等边界情况
- **错误信息**: 提供清晰的错误提示信息

#### 4.2 性能优化策略
- **时间复杂度优化**: 从O(nm)暴力解法优化到O(n+m)差分数组解法
- **空间复杂度优化**: 使用原地操作或最小额外空间
- **常数项优化**: 减少不必要的计算和内存访问

#### 4.3 大数处理与溢出防护
- **数据类型选择**: 根据题目数据范围选择合适的整数类型（int/long）
- **溢出检测**: 在关键计算点检查可能的整数溢出
- **大数测试**: 设计包含边界值的测试用例

#### 4.4 代码可读性与维护性
- **命名规范**: 使用有意义的变量名和函数名
- **注释质量**: 关键算法步骤添加详细注释
- **模块化设计**: 将复杂功能分解为独立的小函数

#### 4.5 测试策略与质量保证
- **单元测试**: 为每个核心函数编写测试用例
- **边界测试**: 测试数组长度为1、操作数量为0等边界情况
- **性能测试**: 验证算法在大规模数据下的表现

#### 4.6 跨语言实现差异
- **Java**: 注意类型安全性和异常处理
- **C++**: 注意内存管理和性能优化
- **Python**: 注意动态类型和性能特点

### 五、算法应用场景识别

#### 5.1 适用场景特征
- 需要频繁进行区间加减操作
- 操作次数远大于数组长度
- 最终只需要一次查询或少量查询
- 数据规模较大，暴力解法不可行

#### 5.2 不适用场景
- 需要频繁的单点查询
- 操作包含复杂的非线性变换
- 需要支持区间乘除等复杂操作

### 六、调试技巧与问题定位

#### 6.1 调试方法
1. **打印中间结果**: 输出差分数组和前缀和验证逻辑
2. **小规模测试**: 使用简单测试用例验证算法正确性
3. **边界测试**: 测试n=1, m=1等边界情况

#### 6.2 常见错误
1. **索引越界**: 忘记检查r+1是否超出数组范围
2. **数据类型溢出**: 使用int导致大数计算溢出
3. **边界条件**: 处理数组长度为0或操作数量为0的情况

### 七、与相关算法的对比

#### 7.1 与线段树对比
| 特性 | 差分数组 | 线段树 |
|------|----------|--------|
| 区间更新 | O(1) | O(log n) |
| 区间查询 | O(n) | O(log n) |
| 空间复杂度 | O(n) | O(4n) |
| 适用场景 | 更新多查询少 | 更新查询均衡 |

#### 7.2 与树状数组对比
| 特性 | 差分数组 | 树状数组 |
|------|----------|-----------|
| 区间更新 | O(1) | O(log n) |
| 区间查询 | O(n) | O(log n) |
| 实现难度 | 简单 | 中等 |
| 扩展性 | 有限 | 较强 |

### 八、进阶技巧与变种

#### 8.1 二维差分数组
用于处理二维矩阵的区域更新，通过四个角的标记实现：
```
diff[x1][y1] += value
diff[x1][y2+1] -= value
diff[x2+1][y1] -= value
diff[x2+1][y2+1] += value
```

#### 8.2 等差数列差分
处理区间上添加等差数列的特殊标记方式，需要四个标记点。

#### 8.3 多层差分操作
处理操作的操作（如Codeforces 296C），使用两层差分数组优化。

### 九、面试考点总结

#### 9.1 基础考点
- 差分数组的原理和实现
- 时间复杂度分析
- 边界条件处理

#### 9.2 进阶考点
- 二维差分数组的应用
- 与其他数据结构的对比
- 工程化实现考量

#### 9.3 实战技巧
- 快速识别适用场景
- 处理复杂边界条件
- 优化代码可读性

## 学习建议

1. **理解原理**: 搞清楚差分数组为什么能优化区间更新操作
2. **掌握模板**: 熟练掌握一维和二维差分数组的实现模板
3. **多做练习**: 从简单到困难，逐步提高
4. **总结变化**: 不同题目的变化点在哪里
5. **代码实践**: 手写实现，不要依赖IDE
6. **工程化思维**: 考虑异常处理、性能优化、可维护性等工程因素
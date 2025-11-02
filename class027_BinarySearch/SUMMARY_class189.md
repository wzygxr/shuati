# 二分查询与自适应查询算法总结

## 核心算法类型

### 1. 二分查询 (Binary Search)
二分查找是一种在有序数组中查找特定元素的搜索算法。搜索过程从数组的中间元素开始，如果中间元素正好是要查找的元素，则搜索过程结束；如果某一特定元素大于或者小于中间元素，则在数组大于或小于中间元素的那一半中查找，而且跟开始一样从中间元素开始比较。如果在某一步骤数组为空，则代表找不到。这种搜索算法每一次比较都使搜索范围缩小一半。

#### 时间复杂度
- 最好情况：O(1)
- 最坏情况：O(log n)
- 平均情况：O(log n)

#### 空间复杂度
- 迭代实现：O(1)
- 递归实现：O(log n)

#### 应用场景
1. 在有序数组中查找元素
2. 查找插入位置
3. 查找边界值（第一个、最后一个）
4. 旋转数组查找
5. 峰值查找

#### 相关题目

##### LeetCode (力扣)
1. [704. 二分查找](https://leetcode.cn/problems/binary-search/)
   - 题目描述：给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target ，写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。
   - 解法：基础二分查找

2. [35. 搜索插入位置](https://leetcode.cn/problems/search-insert-position/)
   - 题目描述：给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
   - 解法：查找第一个大于等于目标值的位置

3. [34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/)
   - 题目描述：给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
   - 解法：查找第一个和最后一个等于目标值的位置

4. [153. 寻找旋转排序数组中的最小值](https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/)
   - 题目描述：假设按照升序排序的数组在预先未知的某个点上进行了旋转。请找出其中最小的元素。
   - 解法：旋转数组中的二分查找

5. [162. 寻找峰值](https://leetcode.cn/problems/find-peak-element/)
   - 题目描述：峰值元素是指其值大于左右相邻值的元素。给定一个输入数组 nums，其中 nums[i] ≠ nums[i+1]，找到峰值元素并返回其索引。
   - 解法：二分查找峰值

6. [278. 第一个错误的版本](https://leetcode.cn/problems/first-bad-version/)
   - 题目描述：假设你有 n 个版本 [1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本。
   - 解法：查找第一个满足条件的元素

7. [300. 最长递增子序列](https://leetcode.cn/problems/longest-increasing-subsequence/)
   - 题目描述：给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
   - 解法：动态规划 + 二分查找优化

8. [374. 猜数字大小](https://leetcode.cn/problems/guess-number-higher-or-lower/)
   - 题目描述：我们正在玩一个猜数字游戏。猜数字游戏的规则如下：我会从 1 到 n 随机选择一个数字。请你猜选出的是哪个数字。
   - 解法：交互式二分查找

9. [852. 山脉数组的峰顶索引](https://leetcode.cn/problems/peak-index-in-a-mountain-array/)
   - 题目描述：我们把符合下列属性的数组 A 称为山脉数组。
   - 解法：二分查找山脉峰值

10. [1095. 山脉数组中查找目标值](https://leetcode.cn/problems/find-in-mountain-array/)
    - 题目描述：给你一个山脉数组 mountainArr，返回能够使得 mountainArr.get(index) == target 的最小的下标 index。
    - 解法：先找峰值，再在两个有序部分分别二分查找

##### Codeforces
1. [Codeforces 448D - Multiplication Table](https://codeforces.com/problemset/problem/448/D)
   - 题目描述：在一个 n×m 的乘法表中，第 i 行第 j 列的数是 i*j。求第 k 小的数。
   - 解法：二分答案 + 计数

2. [Codeforces 460C - Present](https://codeforces.com/problemset/problem/460/C)
   - 题目描述：给一个长度为 n 的数组，每天可以给连续 w 个数加 1，问 m 天后最小值最大是多少。
   - 解法：二分答案 + 贪心

3. [Codeforces 706D - Vasiliy's Multiset](https://codeforces.com/problemset/problem/706/D)
   - 题目描述：维护一个多重集，支持插入、删除、查询与给定数异或的最大值。
   - 解法：01字典树 + 二分思想

4. [Codeforces 817C - Really Big Numbers](https://codeforces.com/problemset/problem/817/C)
   - 题目描述：定义 really big number 为一个正整数，它严格大于其各位数字之和。给定两个正整数 n 和 s，求不大于 n 的 really big number 的个数。
   - 解法：二分查找 + 数位分析

##### AtCoder
1. [AtCoder ABC149D - Prediction and Restriction](https://atcoder.jp/contests/abc149/tasks/abc149_d)
   - 题目描述：猜拳游戏，每次出招有得分，连续出相同招式会被限制，求最大得分。
   - 解法：贪心 + 二分

2. [AtCoder ABC153E - Crested Ibis vs Monster](https://atcoder.jp/contests/abc153/tasks/abc153_e)
   - 题目描述：打怪兽，有多种攻击方式，每种有伤害和魔法消耗，求打败怪兽的最小魔法消耗。
   - 解法：动态规划 + 二分优化

3. [AtCoder ABC165D - Floor Function](https://atcoder.jp/contests/abc165/tasks/abc165_d)
   - 题目描述：给定正整数 A, B, N，求 f(x) = floor(Ax/B) - A*floor(x/B) 在 0≤x≤N 时的最大值。
   - 解法：数学分析 + 二分思想

##### 洛谷 (Luogu)
1. [P1873 [COCI2011/2012#5] EKO](https://www.luogu.com.cn/problem/P1873)
   - 题目描述：伐木工要砍掉一些树，每棵树有一个高度，伐木工有一个锯子高度 H，所有树高度大于 H 的部分都会被砍掉。求锯子最大高度 H，使得砍掉的木材总长度至少为 M。
   - 解法：二分答案

2. [P2249 【深基13.例1】查找](https://www.luogu.com.cn/problem/P2249)
   - 题目描述：输入 n 个整数和 m 个询问，对于每个询问，输出这个数第一次出现的位置。
   - 解法：二分查找第一个等于目标值的位置

3. [P2440 质材分割](https://www.luogu.com.cn/problem/P2440)
   - 题目描述：有 n 根木材，每根木材有一个长度。现在要把这些木材切割成 k 段长度相同的木材，求最大长度。
   - 解法：二分答案

4. [P1102 A-B数对](https://www.luogu.com.cn/problem/P1102)
   - 题目描述：给出一串数以及一个数字 C，要求找到数对 A、B，使得 A-B=C。
   - 解法：排序 + 二分查找

5. [P1059 [NOIP2006 普及组] 明明的随机数](https://www.luogu.com.cn/problem/P1059)
   - 题目描述：明明想随机生成一些不同的随机数，然后进行升序排列。
   - 解法：排序 + 去重 + 二分查找

##### 牛客网
1. [NC15074 二分查找](https://ac.nowcoder.com/acm/problem/15074)
   - 题目描述：实现二分查找算法
   - 解法：基础二分查找

2. [NC16533 二分答案](https://ac.nowcoder.com/acm/problem/16533)
   - 题目描述：通过二分答案解决最优化问题
   - 解法：二分答案模板

##### HackerRank
1. [Binary Search Tree : Insertion](https://www.hackerrank.com/challenges/binary-search-tree-insertion/problem)
   - 题目描述：在二叉搜索树中插入节点
   - 解法：BST插入操作

2. [Pairs](https://www.hackerrank.com/challenges/pairs/problem)
   - 题目描述：在数组中找到差值为 K 的数对个数
   - 解法：排序 + 二分查找

##### 其他平台
1. [SPOJ AGGRCOW - Aggressive cows](https://www.spoj.com/problems/AGGRCOW/)
   - 题目描述：农夫有 N 个牛棚，要把 C 头牛安排到牛棚里，使得相邻两头牛之间的最小距离最大。
   - 解法：二分答案 + 贪心

2. [USACO Monthly Gold February 2006 - Crossing the Desert](http://www.usaco.org/index.php?page=viewproblem2&cpid=622)
   - 题目描述：穿越沙漠问题，需要携带足够的水和食物
   - 解法：动态规划 + 二分答案

3. [Project Euler Problem 209](https://projecteuler.net/problem=209)
   - 题目描述：圆形逻辑电路族
   - 解法：图论 + 二分图匹配

### 2. 自适应查询 (Adaptive Query)
自适应查询是一种根据历史查询结果动态调整查询策略的算法。它通过分析已有的查询结果，预测最优的下一步查询位置，从而减少总的查询次数。

#### 核心思想
1. 初始查询：从某个位置开始查询
2. 结果分析：根据查询结果分析数据分布
3. 策略调整：根据分析结果调整下一次查询的位置
4. 迭代优化：重复步骤2-3直到找到目标

#### 应用场景
1. 交互式问题求解
2. 智能搜索系统
3. 推荐系统
4. 自适应测试系统

#### 相关题目
1. [Codeforces 850B - Arpa and a list of numbers](https://codeforces.com/problemset/problem/850/B)
   - 题目描述：给定一个数组，每次操作可以选择删除一个数或花费 x 的代价将一个数加 1。求使数组中不存在两个数的 gcd 大于 1 的最小代价。
   - 解法：枚举质数 + 自适应策略

2. [Codeforces 922D - Robot Vacuum Cleaner](https://codeforces.com/problemset/problem/922/D)
   - 题目描述：机器人吸尘器，有 n 个字符串，每个字符串由 's' 和 'h' 组成，求连接后 'sh' 子序列的最大个数。
   - 解法：贪心排序 + 自适应比较策略

### 3. 信息论下界优化 (Information Theory Lower Bound)
信息论下界优化是通过计算信息熵来确定最优查询策略，使得每次查询都能获得最大的信息增益，从而最小化总的查询次数。

#### 核心思想
1. 熵值计算：计算当前状态的不确定性
2. 信息增益：计算不同查询策略的信息增益
3. 最优选择：选择信息增益最大的查询策略
4. 迭代优化：重复步骤1-3直到找到目标

#### 应用场景
1. 最优查询策略设计
2. 信息检索系统
3. 决策树构建
4. 机器学习特征选择

## 高频场景

### 1. 交互+二分
在交互式问题中，我们不能直接访问数据，而是需要通过查询接口获取信息。二分查找在这种场景下非常有用，因为它能以最少的查询次数找到目标。

#### 相关题目
1. [Codeforces 1208C - Magic Grid](https://codeforces.com/problemset/problem/1208/C)
   - 题目描述：构造一个 n×n 的矩阵，使得每行每列的异或和都相等。
   - 解法：构造性算法 + 交互式验证

2. [Codeforces 1036D - Vasya and Arrays](https://codeforces.com/problemset/problem/1036/D)
   - 题目描述：给两个数组，每次可以选择一个数组的前缀加到另一个数组的末尾，求最大操作次数。
   - 解法：双指针 + 交互式贪心

### 2. 交互+图论
在图论问题中，有时我们需要通过查询来确定图的结构，比如找树的根、找桥边等。

#### 相关题目
1. [Codeforces 1209D - Cow and Snacks](https://codeforces.com/problemset/problem/1209/D)
   - 题目描述：n 个零食和 k 个牛，每个牛有两个喜欢的零食，求最多能满足多少个牛。
   - 解法：并查集 + 交互式贪心

2. [Codeforces 1139D - Steps to One](https://codeforces.com/problemset/problem/1139/D)
   - 题目描述：从 1 到 m 中随机选数，直到所有数的 gcd 为 1，求期望步数。
   - 解法：莫比乌斯反演 + 动态规划

### 3. 交互+数论
在数论问题中，我们可能需要通过查询来确定数字的性质，比如是否为质数、因数分解等。

#### 相关题目
1. [Codeforces 1149C - Tree Generator](https://codeforces.com/problemset/problem/1149/C)
   - 题目描述：给一个括号序列表示的树，支持修改操作，查询直径。
   - 解法：线段树 + 交互式维护

2. [Codeforces 1208E - Let Them Slide](https://codeforces.com/problemset/problem/1208/E)
   - 题目描述：给 n 个滑块，每个滑块有一个数组，求每列的最大值。
   - 解法：单调队列 + 交互式优化

## 技巧总结

### 1. 剪枝查询次数
在实际应用中，我们可以通过以下方式减少查询次数：
1. 提前终止条件：当确定答案时立即返回
2. 查询顺序优化：优先查询信息量大的位置
3. 缓存机制：避免重复查询相同内容
4. 并行查询：在可能的情况下同时进行多个查询

### 2. 容错处理
在交互式查询中，用户输入可能有误，我们需要进行容错处理：
1. 输入验证：检查输入是否合法
2. 异常处理：处理查询失败的情况
3. 回退机制：当发现错误时能够回退到正确状态
4. 用户提示：给出清晰的错误提示和操作指导

## 工程化考量

### 1. 异常处理
1. 输入验证：检查数组是否为空、是否有序等
2. 边界条件：处理数组长度为0、1等特殊情况
3. 错误恢复：当查询失败时能够恢复到正确状态

### 2. 性能优化
1. 减少不必要的计算：避免重复计算相同结果
2. 使用合适的数据结构：选择最适合的数据结构提高效率
3. 内存管理优化：避免内存泄漏和不必要的内存分配

### 3. 可维护性
1. 代码结构清晰：使用清晰的函数和类结构
2. 注释完整：为关键算法和复杂逻辑添加详细注释
3. 接口设计合理：设计简洁明了的接口

### 4. 可扩展性
1. 模块化设计：将不同功能拆分为独立模块
2. 策略模式应用：使用策略模式支持不同的查询策略
3. 配置化参数：通过配置文件或参数控制算法行为

## 算法思路总结

### 二分查找
二分查找的核心在于每次都能将搜索范围缩小一半，这要求数据具有单调性。在实际应用中，我们需要识别问题中的单调性，并将其转化为二分查找的形式。

### 自适应查询
自适应查询的关键在于如何根据历史信息调整查询策略。这需要我们设计合适的反馈机制和策略调整算法。

### 信息论下界优化
信息论下界优化的核心是计算信息熵和信息增益。我们需要理解信息论的基本概念，并将其应用到查询策略优化中。

## 总结

二分查询、自适应查询和信息论下界优化是三种重要的算法思想，它们在不同的场景下发挥着重要作用。掌握这些算法不仅能够帮助我们解决具体的编程问题，还能提高我们分析和解决复杂问题的能力。在实际应用中，我们需要根据具体问题的特点选择合适的算法，并结合工程化考量进行优化。
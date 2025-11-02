# 线段树 (Segment Tree) 专题详解

## 🧠 核心概念

线段树是一种基于分治思想的二叉树数据结构，主要用于解决区间查询和区间更新问题。每个节点代表一个区间，可以高效地支持区间操作。

### 特点
- 时间复杂度：构建 O(n)，单次查询/更新 O(log n)
- 空间复杂度：O(4n)
- 适用于需要频繁进行区间操作的场景

## 📚 本目录题目详解

### 1. Code01_SequenceOperation.java - 序列操作
- **题目来源**：洛谷 P2572 [SDOI2010] 序列操作
- **题目链接**：https://www.luogu.com.cn/problem/P2572
- **题目大意**：
  给定一个长度为n的01序列，支持5种操作：
  1. 操作 0 l r：将区间[l,r]全部置为0
  2. 操作 1 l r：将区间[l,r]全部置为1
  3. 操作 2 l r：将区间[l,r]全部取反
  4. 操作 3 l r：查询区间[l,r]中1的个数
  5. 操作 4 l r：查询区间[l,r]中连续1的最长长度

- **解题思路**：
  使用线段树维护每个区间的信息：
  - sum[i]：区间内1的个数
  - len0[i]/len1[i]：区间内连续0/1的最长子串长度
  - pre0[i]/pre1[i]：区间内连续0/1的最长前缀长度
  - suf0[i]/suf1[i]：区间内连续0/1的最长后缀长度
  - change[i]：懒标记，记录区间被置为的值
  - update[i]：懒标记，记录区间是否有更新操作
  - reverse[i]：懒标记，记录区间是否有翻转操作

- **关键技术点**：
  - 懒标记的下传顺序：先处理update再处理reverse
  - 区间合并操作：需要考虑左右子区间连接处的情况
  - 多重懒标记的处理

- **时间复杂度**：O(m log n)，其中m为操作次数
- **空间复杂度**：O(n)

### 2. Code02_LongestAlternateSubstring.java - 最长LR交替子串
- **题目来源**：洛谷 P6492 [COCI2010-2011#6] STEP
- **题目链接**：https://www.luogu.com.cn/problem/P6492
- **题目大意**：
  给定一个长度为n的字符串，初始全为'L'，每次操作翻转一个位置的字符，求每次操作后最长的LR交替子串长度（如LRLR或RLRL）。

- **解题思路**：
  使用线段树维护每个区间的最长交替子串长度，以及前缀和后缀的最长交替长度。
  - len[i]：区间内最长交替子串长度
  - pre[i]：区间内最长交替前缀长度
  - suf[i]：区间内最长交替后缀长度
  - arr[i]：记录位置i的字符（0表示L，1表示R）

- **关键技术点**：
  - 区间合并时需要判断中间连接处是否可以连接（相邻字符不同）
  - 单点更新时需要重新计算区间信息

- **时间复杂度**：O(q log n)，其中q为操作次数
- **空间复杂度**：O(n)

### 3. Code03_TunnelWarfare.java - 地道相连的房子
- **题目来源**：洛谷 P1503 [SHOI2008] 洞穴勘测
- **题目链接**：https://www.luogu.com.cn/problem/P1503
- **题目大意**：
  有n个房子排成一排，相邻房子有地道连接。支持三种操作：
  1. D x：摧毁x号房子及其相邻地道
  2. R：恢复上次摧毁的房子及其相邻地道
  3. Q x：查询x号房子能到达的连续房子数量

- **解题思路**：
  使用线段树维护每个区间的连续1的前缀和后缀长度，其中1表示房子未被摧毁。
  - pre[i]：区间内连续1的前缀长度
  - suf[i]：区间内连续1的后缀长度
  - 使用栈记录摧毁的房子，支持恢复操作

- **关键技术点**：
  - 查询操作需要根据位置在区间中的位置进行不同处理
  - 区间合并时考虑跨区间的情况

- **时间复杂度**：O(m log n)，其中m为操作次数
- **空间复杂度**：O(n)

### 4. Code04_Hotel.java - 旅馆
- **题目来源**：洛谷 P2894 [USACO08FEB] Hotel G
- **题目链接**：https://www.luogu.com.cn/problem/P2894
- **题目大意**：
  有n个房间，初始都为空房。支持两种操作：
  1. 1 x：找到最左边的长度至少为x的连续空房间，返回起始位置并入住
  2. 2 x y：将从x号房间开始的y个房间清空

- **解题思路**：
  使用线段树维护每个区间的连续空房间信息：
  - len[i]：区间内最长连续空房间长度
  - pre[i]：区间内最长连续空房间前缀长度
  - suf[i]：区间内最长连续空房间后缀长度
  - change[i]：懒标记，记录区间被设置的值（0表示空房，1表示有人）
  - update[i]：懒标记，记录区间是否有更新操作

- **关键技术点**：
  - 查询最左边满足条件的区间需要特殊处理
  - 区间合并时需要考虑左右子区间的连接情况

- **时间复杂度**：O(m log n)，其中m为操作次数
- **空间复杂度**：O(n)

### 5. Code05_TheSkylineProblem.java - 天际线问题
- **题目来源**：LeetCode 218. 天际线问题
- **题目链接**：https://leetcode.cn/problems/the-skyline-problem/
- **题目大意**：
  城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
  给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。

- **解题思路**：
  使用扫描线算法结合优先队列来解决这个问题：
  - 将所有建筑物的左右边界作为事件点处理
  - 对事件点按x坐标排序
  - 使用优先队列维护当前活跃建筑物的高度
  - 遍历事件点，更新天际线关键点

- **关键技术点**：
  - 扫描线算法：从左到右扫描所有建筑物的边界
  - 优先队列：维护当前活跃建筑物的高度信息
  - 事件处理：处理建筑物的进入和离开事件

- **时间复杂度**：O(n log n)，其中n是建筑物数量
- **空间复杂度**：O(n)

### 6. Code06_RangeSumQueryMutable.java - 区域和检索 - 数组可修改
- **题目来源**：LeetCode 307. 区域和检索 - 数组可修改
- **题目链接**：https://leetcode.cn/problems/range-sum-query-mutable/
- **题目大意**：
  实现支持更新和区间求和操作的数据结构。

- **解题思路**：
  使用线段树来维护区间和，支持单点更新和区间查询操作：
  - 线段树构建：构建支持区间求和的线段树
  - 单点更新：支持更新数组中某个位置的值
  - 区间查询：支持查询任意区间的元素和

- **关键技术点**：
  - 线段树构建与维护
  - 单点更新与区间查询的实现
  - 递归与边界条件处理

- **时间复杂度**：
  - 构建线段树：O(n)
  - 单点更新：O(log n)
  - 区间查询：O(log n)
- **空间复杂度**：O(n)

### 7. Code07_CountSmallerNumbersAfterSelf.java - 计算右侧小于当前元素的个数
- **题目来源**：LeetCode 315. 计算右侧小于当前元素的个数
- **题目链接**：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
- **题目大意**：
  给你一个整数数组 nums ，按要求返回一个新数组 counts 。
  数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

- **解题思路**：
  使用离散化+线段树的方法：
  - 离散化：将数组中的值映射到连续的整数范围
  - 线段树：维护每个值的出现次数，支持单点更新和前缀和查询
  - 逆序遍历：从右往左处理数组元素，确保查询的是右侧元素

- **关键技术点**：
  - 离散化处理
  - 线段树维护元素出现次数
  - 逆序遍历与前缀和查询

- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n)

### 8. Code08_SegmentTreeTemplate1.java - 线段树模板1
- **题目来源**：洛谷 P3372 【模板】线段树 1
- **题目链接**：https://www.luogu.com.cn/problem/P3372
- **题目大意**：
  支持区间加法和区间求和操作。

- **解题思路**：
  使用带懒标记的线段树来实现区间更新和区间查询：
  - 线段树构建：构建支持区间求和的线段树
  - 懒标记：使用懒标记优化区间更新操作
  - 区间更新：支持区间加法操作
  - 区间查询：支持区间求和操作

- **关键技术点**：
  - 懒标记的实现与下传
  - 区间更新与查询的处理
  - IO优化与大数据处理

- **时间复杂度**：
  - 构建线段树：O(n)
  - 区间更新：O(log n)
  - 区间查询：O(log n)
- **空间复杂度**：O(n)

## 🎯 线段树经典应用场景

### 1. 区间最值查询（RMQ）
- 维护区间最大值/最小值
- 典型题目：HDU 1754 I Hate It

### 2. 区间求和
- 维护区间元素和
- 典型题目：LeetCode 307. Range Sum Query - Mutable

### 3. 区间修改
- 支持区间加法、区间赋值等操作
- 典型题目：HDU 1166 敌兵布阵

### 4. 区间统计
- 统计区间满足特定条件的元素个数
- 典型题目：POJ 2777 Count Color

## 🔧 线段树实现要点

### 1. 节点信息设计
- 根据题目需求设计节点存储的信息
- 考虑区间合并的逻辑

### 2. 懒标记处理
- 懒标记的下传顺序很重要
- 需要正确处理多种懒标记的相互影响

### 3. 区间合并
- 正确实现push_up函数
- 考虑跨区间的情况

### 4. 边界处理
- 注意数组大小的开法（通常为4n）
- 注意下标从0还是1开始

## 📈 复杂度分析

| 操作 | 时间复杂度 | 空间复杂度 |
|------|------------|------------|
| 建树 | O(n) | O(n) |
| 单点更新 | O(log n) | O(1) |
| 区间更新 | O(log n) | O(log n) |
| 单点查询 | O(log n) | O(1) |
| 区间查询 | O(log n) | O(log n) |

## 🔄 与其他数据结构的比较

| 数据结构 | 优点 | 缺点 | 适用场景 |
|----------|------|------|----------|
| 线段树 | 支持区间操作，功能强大 | 实现复杂，常数较大 | 频繁区间操作 |
| 树状数组 | 实现简单，常数小 | 只支持前缀操作 | 前缀统计问题 |
| 平衡树 | 动态维护有序序列 | 实现复杂 | 动态集合操作 |

## 🌟 工程化应用

### 1. 数据库索引
- 在数据库系统中用于范围查询优化

### 2. 图形学
- 用于空间分割和碰撞检测

### 3. 网络路由
- 在网络路由算法中维护路径信息

### 4. 金融系统
- 实时维护股票价格的区间统计信息

## 📚 相关题目推荐

### LeetCode (力扣)
1. **LeetCode 218. 天际线问题** - https://leetcode.cn/problems/the-skyline-problem/
   - 类型：线段树+扫描线
   - 难度：困难

2. **LeetCode 307. 区域和检索 - 数组可修改** - https://leetcode.cn/problems/range-sum-query-mutable/
   - 类型：线段树基础
   - 难度：中等

3. **LeetCode 308. 二维区域和检索 - 可变** - https://leetcode.cn/problems/range-sum-query-2d-mutable/
   - 类型：二维线段树
   - 难度：困难

4. **LeetCode 315. 计算右侧小于当前元素的个数** - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 类型：线段树+离散化
   - 难度：困难

5. **LeetCode 327. 区间和的个数** - https://leetcode.cn/problems/count-of-range-sum/
   - 类型：线段树+前缀和
   - 难度：困难

6. **LeetCode 493. 翻转对** - https://leetcode.cn/problems/reverse-pairs/
   - 类型：线段树+离散化
   - 难度：困难

7. **LeetCode 699. 掉落的方块** - https://leetcode.cn/problems/falling-squares/
   - 类型：线段树+坐标离散化
   - 难度：困难

8. **LeetCode 715. Range 模块** - https://leetcode.cn/problems/range-module/
   - 类型：线段树+区间合并
   - 难度：困难

9. **LeetCode 729. 我的日程安排表 I** - https://leetcode.cn/problems/my-calendar-i/
   - 类型：线段树区间查询
   - 难度：中等

10. **LeetCode 731. 我的日程安排表 II** - https://leetcode.cn/problems/my-calendar-ii/
    - 类型：线段树+最大值维护
    - 难度：中等

11. **LeetCode 732. 我的日程安排表 III** - https://leetcode.cn/problems/my-calendar-iii/
    - 类型：线段树+最大值维护
    - 难度：困难

12. **LeetCode 850. 矩形面积 II** - https://leetcode.cn/problems/rectangle-area-ii/
    - 类型：线段树+扫描线
    - 难度：困难

13. **LeetCode 1157. 子数组中占绝大多数的元素** - https://leetcode.cn/problems/online-majority-element-in-subarray/
    - 类型：线段树+二分查找
    - 难度：困难

14. **LeetCode 1649. 通过指令创建有序数组** - https://leetcode.cn/problems/create-sorted-array-through-instructions/
    - 类型：线段树+离散化
    - 难度：困难

### 洛谷 (Luogu)
1. **P3372 【模板】线段树 1** - https://www.luogu.com.cn/problem/P3372
   - 类型：线段树基础模板
   - 难度：普及+/提高-

2. **P3373 【模板】线段树 2** - https://www.luogu.com.cn/problem/P3373
   - 类型：线段树+多种操作
   - 难度：提高+/省选-

3. **P2572 [SDOI2010] 序列操作** - https://www.luogu.com.cn/problem/P2572
   - 类型：线段树+多种操作
   - 难度：省选/NOI-

4. **P1503 [SHOI2008] 洞穴勘测** - https://www.luogu.com.cn/problem/P1503
   - 类型：线段树维护连通性
   - 难度：提高+/省选-

5. **P2894 [USACO08FEB] Hotel G** - https://www.luogu.com.cn/problem/P2894
   - 类型：线段树维护连续区间
   - 难度：提高+/省选-

6. **P4514 上帝造题的七分钟** - https://www.luogu.com.cn/problem/P4514
   - 类型：二维线段树
   - 难度：省选/NOI-

7. **P4198 楼房重建** - https://www.luogu.com.cn/problem/P4198
   - 类型：线段树维护斜率
   - 难度：省选/NOI-

8. **P2429 制杖题** - https://www.luogu.com.cn/problem/P2429
   - 类型：线段树+数学
   - 难度：省选/NOI-

9. **P4588 [TJOI2018]数学计算** - https://www.luogu.com.cn/problem/P4588
   - 类型：线段树维护乘积
   - 难度：提高+/省选-

10. **P2184 贪婪大陆** - https://www.luogu.com.cn/problem/P2184
    - 类型：线段树+前缀和
    - 难度：提高+/省选-

### Codeforces
1. **339D - Xenia and Bit Operations** - https://codeforces.com/problemset/problem/339/D
   - 类型：线段树基础
   - 难度：1900

2. **380C - Sereja and Brackets** - https://codeforces.com/problemset/problem/380/C
   - 类型：线段树维护括号匹配
   - 难度：1700

3. **52C - Circular RMQ** - https://codeforces.com/problemset/problem/52/C
   - 类型：线段树+区间更新
   - 难度：2200

4. **145E - Lucky Queries** - https://codeforces.com/problemset/problem/145/E
   - 类型：线段树+懒标记
   - 难度：2200

5. **242E - XOR on Segment** - https://codeforces.com/problemset/problem/242/E
   - 类型：线段树+位运算
   - 难度：2000

6. **438D - The Child and Sequence** - https://codeforces.com/problemset/problem/438/D
   - 类型：线段树+区间取模
   - 难度：2400

7. **446C - DZY Loves Fibonacci Numbers** - https://codeforces.com/problemset/problem/446/C
   - 类型：线段树+斐波那契数列
   - 难度：2700

8. **558E - A Simple Task** - https://codeforces.com/problemset/problem/558/E
   - 类型：线段树+字符排序
   - 难度：2300

9. **610E - Alphabet Permutations** - https://codeforces.com/problemset/problem/610/E
   - 类型：线段树+字符串处理
   - 难度：2400

10. **718C - Sasha and Array** - https://codeforces.com/problemset/problem/718/C
    - 类型：线段树+矩阵乘法
    - 难度：2400

### 洛谷 (Luogu)
1. **P3372 【模板】线段树 1** - https://www.luogu.com.cn/problem/P3372
   - 类型：线段树基础模板
   - 难度：普及+/提高-

2. **P3373 【模板】线段树 2** - https://www.luogu.com.cn/problem/P3373
   - 类型：线段树+多种操作
   - 难度：提高+/省选-

3. **P2572 [SDOI2010] 序列操作** - https://www.luogu.com.cn/problem/P2572
   - 类型：线段树+多种操作
   - 难度：省选/NOI-

4. **P1503 [SHOI2008] 洞穴勘测** - https://www.luogu.com.cn/problem/P1503
   - 类型：线段树维护连通性
   - 难度：提高+/省选-

5. **P2894 [USACO08FEB] Hotel G** - https://www.luogu.com.cn/problem/P2894
   - 类型：线段树维护连续区间
   - 难度：提高+/省选-

6. **P4514 上帝造题的七分钟** - https://www.luogu.com.cn/problem/P4514
   - 类型：二维线段树
   - 难度：省选/NOI-

7. **P4198 楼房重建** - https://www.luogu.com.cn/problem/P4198
   - 类型：线段树维护斜率
   - 难度：省选/NOI-

8. **P2429 制杖题** - https://www.luogu.com.cn/problem/P2429
   - 类型：线段树+数学
   - 难度：省选/NOI-

9. **P4588 [TJOI2018]数学计算** - https://www.luogu.com.cn/problem/P4588
   - 类型：线段树维护乘积
   - 难度：提高+/省选-

10. **P2184 贪婪大陆** - https://www.luogu.com.cn/problem/P2184
    - 类型：线段树+前缀和
    - 难度：提高+/省选-

### HackerRank
1. **Array and simple queries** - https://www.hackerrank.com/challenges/array-and-simple-queries/problem
   - 类型：线段树基础
   - 难度：Advanced

2. **Roy and Coin Boxes** - https://www.hackerrank.com/challenges/roy-and-coin-boxes/problem
   - 类型：线段树+差分
   - 难度：Advanced

3. **Polynomial Division** - https://www.hackerrank.com/challenges/polynomial-divison/problem
   - 类型：线段树+多项式
   - 难度：Expert

### LintCode (炼码)
1. **201. 线段树的构造** - https://www.lintcode.com/problem/segment-tree-build/description
   - 类型：线段树基础
   - 难度：中等

2. **202. 线段树查询** - https://www.lintcode.com/problem/segment-tree-query/description
   - 类型：线段树查询
   - 难度：中等

3. **203. 线段树修改** - https://www.lintcode.com/problem/segment-tree-modify/description
   - 类型：线段树更新
   - 难度：中等

4. **206. 区间求和 I** - https://www.lintcode.com/problem/interval-sum/description
   - 类型：线段树求和
   - 难度：中等

5. **207. 区间求和 II** - https://www.lintcode.com/problem/interval-sum-ii/description
   - 类型：线段树+区间更新
   - 难度：困难

### SPOJ
1. **GSS1 - Can you answer these queries I** - https://www.spoj.com/problems/GSS1/
   - 类型：线段树最大子段和
   - 难度：中等

2. **GSS3 - Can you answer these queries III** - https://www.spoj.com/problems/GSS3/
   - 类型：线段树最大子段和+单点更新
   - 难度：中等

3. **GSS4 - Can you answer these queries IV** - https://www.spoj.com/problems/GSS4/
   - 类型：线段树+区间开方
   - 难度：困难

4. **GSS5 - Can you answer these queries V** - https://www.spoj.com/problems/GSS5/
   - 类型：线段树+区间查询
   - 难度：困难

5. **GSS6 - Can you answer these queries VI** - https://www.spoj.com/problems/GSS6/
   - 类型：平衡树+线段树
   - 难度：困难

6. **GSS7 - Can you answer these queries VII** - https://www.spoj.com/problems/GSS7/
   - 类型：树链剖分+线段树
   - 难度：困难

### HDU
1. **HDU 1166 敌兵布阵** - http://acm.hdu.edu.cn/showproblem.php?pid=1166
   - 类型：线段树基础
   - 难度：简单

2. **HDU 1754 I Hate It** - http://acm.hdu.edu.cn/showproblem.php?pid=1754
   - 类型：线段树维护最值
   - 难度：简单

3. **HDU 1698 Just a Hook** - http://acm.hdu.edu.cn/showproblem.php?pid=1698
   - 类型：线段树+区间更新
   - 难度：中等

4. **HDU 4528 小明系列故事——捉迷藏** - http://acm.hdu.edu.cn/showproblem.php?pid=4528
   - 类型：线段树+几何
   - 难度：困难

### POJ
1. **POJ 3468 A Simple Problem with Integers** - http://poj.org/problem?id=3468
   - 类型：线段树+区间加法
   - 难度：中等

2. **POJ 2777 Count Color** - http://poj.org/problem?id=2777
   - 类型：线段树+位运算
   - 难度：中等

3. **POJ 2528 Mayor's posters** - http://poj.org/problem?id=2528
   - 类型：线段树+离散化
   - 难度：中等

4. **POJ 3264 Balanced Lineup** - http://poj.org/problem?id=3264
   - 类型：线段树维护最值
   - 难度：简单

## 🧠 线段树常见变种

### 1. 动态开点线段树
- 适用场景：数据范围很大，但实际使用较少的情况
- 典型题目：Codeforces 915E

### 2. 可持久化线段树（主席树）
- 适用场景：需要保存历史版本的信息
- 典型题目：POJ 2104

### 3. 扫描线 + 线段树
- 适用场景：平面几何问题，如矩形面积并
- 典型题目：POJ 1151

### 4. 树链剖分 + 线段树
- 适用场景：树上路径操作
- 典型题目：洛谷 P3384

### 5. 线段树合并
- 适用场景：动态维护多个集合的信息
- 典型题目：Codeforces 600E

## 🎯 学习建议

1. **基础阶段**：
   - 熟练掌握线段树的基本操作（建树、单点更新、区间查询）
   - 练习简单的区间求和、区间最值问题

2. **进阶阶段**：
   - 学习懒标记技术，掌握区间更新操作
   - 练习复杂的节点信息维护

3. **高手阶段**：
   - 学习线段树的各种变种和高级应用
   - 掌握线段树与其他算法的结合使用

## 📚 参考资料

1. 《算法竞赛进阶指南》- 李煜东
2. 《挑战程序设计竞赛》- 秋叶拓哉等
3. TopCoder数据结构教程
4. Codeforces Educational Round相关讲解

## 🧠 总结

线段树是处理区间问题的强大工具，虽然实现相对复杂，但其灵活性和效率使其在算法竞赛和工程应用中都有重要地位。掌握线段树的关键在于：

1. 理解其分治思想和二叉树结构
2. 根据具体问题设计节点信息和合并逻辑
3. 正确处理懒标记的下传和更新
4. 熟练掌握各种经典应用场景
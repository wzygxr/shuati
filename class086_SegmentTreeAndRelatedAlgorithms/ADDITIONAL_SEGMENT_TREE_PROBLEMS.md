# 线段树补充题目清单

本文件整理了与class113中线段树相关的更多练习题目，来源于各大算法平台。

## 📚 按平台分类

### LeetCode (力扣)
1. **LeetCode 218. 天际线问题** - https://leetcode.cn/problems/the-skyline-problem/
   - 类型：线段树+扫描线
   - 难度：困难
   - 题目描述：城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。

2. **LeetCode 307. 区域和检索 - 数组可修改** - https://leetcode.cn/problems/range-sum-query-mutable/
   - 类型：线段树基础
   - 难度：中等
   - 题目描述：实现支持更新和区间求和操作的数据结构。

3. **LeetCode 308. 二维区域和检索 - 可变** - https://leetcode.cn/problems/range-sum-query-2d-mutable/
   - 类型：二维线段树
   - 难度：困难
   - 题目描述：实现支持更新和二维区间求和操作的数据结构。

4. **LeetCode 315. 计算右侧小于当前元素的个数** - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 类型：线段树+离散化
   - 难度：困难
   - 题目描述：给定一个整数数组 nums，返回一个新数组 counts，其中 counts[i] 是 nums[i] 右侧小于 nums[i] 的元素的数量。

5. **LeetCode 327. 区间和的个数** - https://leetcode.cn/problems/count-of-range-sum/
   - 类型：线段树+前缀和
   - 难度：困难
   - 题目描述：给定一个整数数组 nums 以及两个整数 lower 和 upper，求值位于范围 [lower, upper] 之间的区间和的个数。

6. **LeetCode 493. 翻转对** - https://leetcode.cn/problems/reverse-pairs/
   - 类型：线段树+离散化
   - 难度：困难
   - 题目描述：给定一个数组 nums，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对，需要返回给定数组中的重要翻转对的总数。

7. **LeetCode 699. 掉落的方块** - https://leetcode.cn/problems/falling-squares/
   - 类型：线段树+坐标离散化
   - 难度：困难
   - 题目描述：在无限长的数轴（坐标轴）上，我们根据给定的顺序放置“方块”，计算每次放置后堆叠的最大高度。

8. **LeetCode 715. Range 模块** - https://leetcode.cn/problems/range-module/
   - 类型：线段树+区间合并
   - 难度：困难
   - 题目描述：实现一个 RangeModule 类，用于跟踪数字范围并支持添加、查询和移除范围操作。

9. **LeetCode 729. 我的日程安排表 I** - https://leetcode.cn/problems/my-calendar-i/
   - 类型：线段树区间查询
   - 难度：中等
   - 题目描述：实现一个 MyCalendar 类来存放你的日程安排，如果要添加的日程安排不会造成重复预订，则可以存储这个新的日程安排。

10. **LeetCode 731. 我的日程安排表 II** - https://leetcode.cn/problems/my-calendar-ii/
    - 类型：线段树+最大值维护
    - 难度：中等
    - 题目描述：实现一个 MyCalendarTwo 类来存放你的日程安排，如果要添加的时间不会导致三重预订时，则可以存储这个新的日程安排。

11. **LeetCode 732. 我的日程安排表 III** - https://leetcode.cn/problems/my-calendar-iii/
    - 类型：线段树+最大值维护
    - 难度：困难
    - 题目描述：实现一个 MyCalendarThree 类来存放你的日程安排，可以存放相同时间的多个日程安排。

12. **LeetCode 850. 矩形面积 II** - https://leetcode.cn/problems/rectangle-area-ii/
    - 类型：线段树+扫描线
    - 难度：困难
    - 题目描述：我们给出了一个（轴对齐的）二维矩形列表 rectangles，计算并返回所有矩形所覆盖的总面积。

13. **LeetCode 1157. 子数组中占绝大多数的元素** - https://leetcode.cn/problems/online-majority-element-in-subarray/
    - 类型：线段树+二分查找
    - 难度：困难
    - 题目描述：实现一个数据结构，支持查询指定子数组中出现次数超过阈值的元素。

14. **LeetCode 1649. 通过指令创建有序数组** - https://leetcode.cn/problems/create-sorted-array-through-instructions/
    - 类型：线段树+离散化
    - 难度：困难
    - 题目描述：通过一系列指令创建一个有序数组，计算插入每个元素的代价。

### Codeforces
1. **339D - Xenia and Bit Operations** - https://codeforces.com/problemset/problem/339/D
   - 类型：线段树基础
   - 难度：1900
   - 题目描述：给定一个数组，交替进行OR和XOR操作，支持单点更新和查询根节点的值。

2. **380C - Sereja and Brackets** - https://codeforces.com/problemset/problem/380/C
   - 类型：线段树维护括号匹配
   - 难度：1700
   - 题目描述：给定一个括号序列，查询区间内能匹配的括号对数。

3. **52C - Circular RMQ** - https://codeforces.com/problemset/problem/52/C
   - 类型：线段树+区间更新
   - 难度：2200
   - 题目描述：环形数组上的区间最小值查询和区间加法操作。

4. **145E - Lucky Queries** - https://codeforces.com/problemset/problem/145/E
   - 类型：线段树+懒标记
   - 难度：2200
   - 题目描述：维护一个由4和7组成的字符串，支持区间翻转和查询最长非递减子序列。

5. **242E - XOR on Segment** - https://codeforces.com/problemset/problem/242/E
   - 类型：线段树+位运算
   - 难度：2000
   - 题目描述：支持区间异或和区间求和操作。

6. **438D - The Child and Sequence** - https://codeforces.com/problemset/problem/438/D
   - 类型：线段树+区间取模
   - 难度：2400
   - 题目描述：支持区间取模、单点更新和区间最大值查询。

7. **446C - DZY Loves Fibonacci Numbers** - https://codeforces.com/problemset/problem/446/C
   - 类型：线段树+斐波那契数列
   - 难度：2700
   - 题目描述：支持区间加斐波那契数列和区间求和操作。

8. **558E - A Simple Task** - https://codeforces.com/problemset/problem/558/E
   - 类型：线段树+字符排序
   - 难度：2300
   - 题目描述：支持区间按升序或降序排序操作。

9. **610E - Alphabet Permutations** - https://codeforces.com/problemset/problem/610/E
   - 类型：线段树+字符串处理
   - 难度：2400
   - 题目描述：维护字符串的逆序对数量，支持单点更新。

10. **718C - Sasha and Array** - https://codeforces.com/problemset/problem/718/C
    - 类型：线段树+矩阵乘法
    - 难度：2400
    - 题目描述：支持区间加法和区间斐波那契数列求和操作。

11. **915E - Physical Education Lessons** - https://codeforces.com/problemset/problem/915/E
    - 类型：动态开点线段树
    - 难度：2200
    - 题目描述：维护一个区间，支持区间设置为0或1，查询区间和。

### 洛谷 (Luogu)
1. **P3372 【模板】线段树 1** - https://www.luogu.com.cn/problem/P3372
   - 类型：线段树基础模板
   - 难度：普及+/提高-
   - 题目描述：实现支持区间加法和区间求和的线段树。

2. **P3373 【模板】线段树 2** - https://www.luogu.com.cn/problem/P3373
   - 类型：线段树+多种操作
   - 难度：提高+/省选-
   - 题目描述：实现支持区间加法、区间乘法和区间求和的线段树。

3. **P2572 [SDOI2010] 序列操作** - https://www.luogu.com.cn/problem/P2572
   - 类型：线段树+多种操作
   - 难度：省选/NOI-
   - 题目描述：支持区间置0、置1、取反、查询1的个数和查询连续1的最长长度。

4. **P1503 [SHOI2008] 洞穴勘测** - https://www.luogu.com.cn/problem/P1503
   - 类型：线段树维护连通性
   - 难度：提高+/省选-
   - 题目描述：维护连续1的前缀和后缀长度，支持摧毁和恢复操作。

5. **P2894 [USACO08FEB] Hotel G** - https://www.luogu.com.cn/problem/P2894
   - 类型：线段树维护连续区间
   - 难度：提高+/省选-
   - 题目描述：维护连续空房间信息，支持查找最左连续空房间和清空房间操作。

6. **P4514 上帝造题的七分钟** - https://www.luogu.com.cn/problem/P4514
   - 类型：二维线段树
   - 难度：省选/NOI-
   - 题目描述：实现二维区间加法和二维区间求和的线段树。

7. **P4198 楼房重建** - https://www.luogu.com.cn/problem/P4198
   - 类型：线段树维护斜率
   - 难度：省选/NOI-
   - 题目描述：维护区间前缀最大值个数，用于解决楼房可见性问题。

8. **P2468 [SDOI2010] 粟粟的书架** - https://www.luogu.com.cn/problem/P2468
   - 类型：主席树+二分
   - 难度：省选/NOI-
   - 题目描述：二维前缀和+主席树，查询子矩阵中前k大元素的和。

9. **P2617 Dynamic Rankings** - https://www.luogu.com.cn/problem/P2617
   - 类型：树套树
   - 难度：省选/NOI-
   - 题目描述：支持单点修改和区间第k小查询。

10. **P3313 [SDOI2014] 旅行** - https://www.luogu.com.cn/problem/P3313
    - 类型：树链剖分+线段树
    - 难度：省选/NOI-
    - 题目描述：树上路径操作，支持路径最大值查询和路径求和。

### HackerRank
1. **Array and simple queries** - https://www.hackerrank.com/challenges/array-and-simple-queries/problem
   - 类型：线段树基础
   - 难度：Advanced
   - 题目描述：支持区间移动操作和查询。

2. **Roy and Coin Boxes** - https://www.hackerrank.com/challenges/roy-and-coin-boxes/problem
   - 类型：线段树+差分
   - 难度：Advanced
   - 题目描述：维护盒子中的硬币数量，支持区间加法和查询。

3. **Polynomial Division** - https://www.hackerrank.com/challenges/polynomial-divison/problem
   - 类型：线段树+多项式
   - 难度：Expert
   - 题目描述：多项式除法问题。

### LintCode (炼码)
1. **201. 线段树的构造** - https://www.lintcode.com/problem/segment-tree-build/description
   - 类型：线段树基础
   - 难度：中等
   - 题目描述：构造线段树。

2. **202. 线段树查询** - https://www.lintcode.com/problem/segment-tree-query/description
   - 类型：线段树查询
   - 难度：中等
   - 题目描述：查询线段树区间最大值。

3. **203. 线段树修改** - https://www.lintcode.com/problem/segment-tree-modify/description
   - 类型：线段树更新
   - 难度：中等
   - 题目描述：单点更新线段树。

4. **206. 区间求和 I** - https://www.lintcode.com/problem/interval-sum/description
   - 类型：线段树求和
   - 难度：中等
   - 题目描述：实现区间求和操作。

5. **207. 区间求和 II** - https://www.lintcode.com/problem/interval-sum-ii/description
   - 类型：线段树+区间更新
   - 难度：困难
   - 题目描述：实现区间加法和区间求和操作。

### SPOJ
1. **GSS1 - Can you answer these queries I** - https://www.spoj.com/problems/GSS1/
   - 类型：线段树最大子段和
   - 难度：中等
   - 题目描述：查询区间最大子段和。

2. **GSS3 - Can you answer these queries III** - https://www.spoj.com/problems/GSS3/
   - 类型：线段树最大子段和+单点更新
   - 难度：中等
   - 题目描述：支持单点更新和查询区间最大子段和。

3. **GSS4 - Can you answer these queries IV** - https://www.spoj.com/problems/GSS4/
   - 类型：线段树+区间开方
   - 难度：困难
   - 题目描述：支持区间开方和区间求和操作。

4. **GSS5 - Can you answer these queries V** - https://www.spoj.com/problems/GSS5/
   - 类型：线段树+区间查询
   - 难度：困难
   - 题目描述：查询指定范围内的最大子段和。

5. **GSS6 - Can you answer these queries VI** - https://www.spoj.com/problems/GSS6/
   - 类型：平衡树+线段树
   - 难度：困难
   - 题目描述：支持插入、删除、修改和查询最大子段和。

6. **GSS7 - Can you answer these queries VII** - https://www.spoj.com/problems/GSS7/
   - 类型：树链剖分+线段树
   - 难度：困难
   - 题目描述：树上路径最大子段和查询。

### HDU
1. **HDU 1166 敌兵布阵** - http://acm.hdu.edu.cn/showproblem.php?pid=1166
   - 类型：线段树基础
   - 难度：简单
   - 题目描述：支持单点更新和区间求和操作。

2. **HDU 1754 I Hate It** - http://acm.hdu.edu.cn/showproblem.php?pid=1754
   - 类型：线段树维护最值
   - 难度：简单
   - 题目描述：支持单点更新和区间最大值查询。

3. **HDU 1698 Just a Hook** - http://acm.hdu.edu.cn/showproblem.php?pid=1698
   - 类型：线段树+区间更新
   - 难度：中等
   - 题目描述：支持区间设置为指定值和区间求和操作。

4. **HDU 2795 Billboard** - http://acm.hdu.edu.cn/showproblem.php?pid=2795
   - 类型：线段树+贪心
   - 难度：中等
   - 题目描述：维护公告板，支持查询最上可行位置。

### POJ
1. **POJ 3468 A Simple Problem with Integers** - http://poj.org/problem?id=3468
   - 类型：线段树+区间加法
   - 难度：中等
   - 题目描述：支持区间加法和区间求和操作。

2. **POJ 2777 Count Color** - http://poj.org/problem?id=2777
   - 类型：线段树+位运算
   - 难度：中等
   - 题目描述：支持区间染色和查询区间颜色种类数。

3. **POJ 2528 Mayor's posters** - http://poj.org/problem?id=2528
   - 类型：线段树+离散化
   - 难度：中等
   - 题目描述：区间覆盖问题，查询可见海报数。

4. **POJ 3264 Balanced Lineup** - http://poj.org/problem?id=3264
   - 类型：线段树维护最值
   - 难度：简单
   - 题目描述：查询区间最大值和最小值的差。

### 牛客网 (Nowcoder)
1. **NC16534 线段树练习** - https://ac.nowcoder.com/acm/problem/16534
   - 类型：线段树基础
   - 难度：中等
   - 题目描述：基础线段树操作练习。

2. **NC16535 线段树练习2** - https://ac.nowcoder.com/acm/problem/16535
   - 类型：线段树+区间更新
   - 难度：中等
   - 题目描述：区间更新和区间查询练习。

3. **NC16536 线段树练习3** - https://ac.nowcoder.com/acm/problem/16536
   - 类型：线段树+区间更新
   - 难度：困难
   - 题目描述：复杂区间更新操作练习。

### AtCoder
1. **ABC185 F - Range Xor Query** - https://atcoder.jp/contests/abc185/tasks/abc185_f
   - 类型：线段树+异或
   - 难度：1186
   - 题目描述：支持单点异或更新和区间异或查询。

2. **ARC075 D - Widespread** - https://atcoder.jp/contests/arc075/tasks/arc075_d
   - 类型：线段树+二分
   - 难度：1765
   - 题目描述：使用二分查找和线段树优化的组合问题。

### CodeChef
1. **FLIPCOIN - Flipping Coins** - https://www.codechef.com/problems/FLIPCOIN
   - 类型：线段树+概率
   - 难度：Medium
   - 题目描述：维护硬币翻转状态，支持区间翻转和查询正面朝上的期望数量。

### USACO
1. **Balanced Lineup** - http://www.usaco.org/index.php?page=viewproblem2&cpid=62
   - 类型：线段树维护最值
   - 难度：Silver
   - 题目描述：查询区间最大值和最小值的差。

2. **Hotel** - http://www.usaco.org/index.php?page=viewproblem2&cpid=63
   - 类型：线段树维护连续区间
   - 难度：Gold
   - 题目描述：维护连续空房间信息。

## 🧠 线段树常见变种

### 1. 动态开点线段树
- 适用场景：数据范围很大，但实际使用较少的情况
- 典型题目：Codeforces 915E

### 2. 可持久化线段树（主席树）
- 适用场景：需要保存历史版本的信息
- 典型题目：洛谷 P3834、P3919

### 3. 扫描线 + 线段树
- 适用场景：平面几何问题，如矩形面积并
- 典型题目：LeetCode 218、850

### 4. 树链剖分 + 线段树
- 适用场景：树上路径操作
- 典型题目：洛谷 P3384、SPOJ GSS7

### 5. 线段树合并
- 适用场景：动态维护多个集合的信息
- 典型题目：Codeforces 600E

### 6. 吉司机线段树
- 适用场景：区间最值操作与历史最值
- 典型题目：SPOJ GSS系列

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
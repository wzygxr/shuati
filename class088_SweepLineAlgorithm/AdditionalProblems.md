# 扫描线算法补充题目详解

## 1. 会议室问题系列

### 1.1 会议室 I (Leetcode 252)
- 题目链接: https://leetcode.cn/problems/meeting-rooms/
- 题目描述: 给定一个会议时间安排的数组 intervals ，每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi] ，请你判断一个人是否能够参加这里面的全部会议。

### 1.2 会议室 II (Leetcode 253)
- 题目链接: https://leetcode.cn/problems/meeting-rooms-ii/
- 题目描述: 给你一个会议时间安排的数组 intervals ，每个会议时间包括开始和结束的时间 intervals[i] = [starti, endi] ，返回所需会议室的最小数量。

### 1.3 我的日程安排表 I (Leetcode 729)
- 题目链接: https://leetcode.cn/problems/my-calendar-i/
- 题目描述: 实现一个 MyCalendar 类来存放你的日程安排。如果要添加的时间内没有其他安排，则可以存储这个新的日程安排。

### 1.4 我的日程安排表 II (Leetcode 731)
- 题目链接: https://leetcode.cn/problems/my-calendar-ii/
- 题目描述: 实现一个 MyCalendarTwo 类来存放你的日程安排。如果要添加的时间内不会导致三重预订时，则可以存储这个新的日程安排。

### 1.5 我的日程安排表 III (Leetcode 732)
- 题目链接: https://leetcode.cn/problems/my-calendar-iii/
- 题目描述: 实现一个 MyCalendarThree 类来存放你的日程安排，你可以一直添加新的日程安排。

### 1.6 员工空闲时间 (Leetcode 759)
- 题目链接: https://leetcode.cn/problems/employee-free-time/
- 题目描述: 给定员工的 schedule 列表，表示每个员工的工作时间。每个员工都有一个非重叠的时间段 Intervals 列表，这些时间段已经排好序。返回表示所有员工的共同、正数长度的空闲时间的有限时间段的列表，同样需要排好序。

## 2. 矩形相关问题

### 2.1 矩形面积 (Leetcode 223)
- 题目链接: https://leetcode.cn/problems/rectangle-area/
- 题目描述: 给你二维平面上两个矩形，计算并返回两个矩形覆盖的总面积。每个矩形由其左下顶点和右上顶点坐标表示。

### 2.2 完美矩形 (Leetcode 391)
- 题目链接: https://leetcode.cn/problems/perfect-rectangle/
- 题目描述: 给你一个数组 rectangles ，其中 rectangles[i] = [xi, yi, ai, bi] 表示一个坐标轴平行的矩形。这个矩形的左下顶点是 (xi, yi) ，右上顶点是 (ai, bi) 。如果所有矩形一起精确覆盖某个矩形区域，则返回 true ；否则，返回 false 。

### 2.3 天际线问题 (Leetcode 218)
- 题目链接: https://leetcode.cn/problems/the-skyline-problem/
- 题目描述: 城市的天际线是从远处观看建筑物形成的轮廓线。给定所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。

### 2.4 Atlantis (POJ 1151)
- 题目链接: http://poj.org/problem?id=1151
- 题目描述: 给定多个矩形区域的地图，计算这些地图覆盖的总面积。

### 2.5 覆盖的面积 (HDU 1255)
- 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1255
- 题目描述: 给定多个矩形，计算被至少两个矩形覆盖的区域面积。

### 2.6 City Horizon (POJ 3277)
- 题目链接: http://poj.org/problem?id=3277
- 题目描述: 给定多个建筑物的位置和高度，计算建筑物轮廓形成的总面积。

### 2.7 矩形面积并 (洛谷 P5490)
- 题目链接: https://www.luogu.com.cn/problem/P5490
- 题目描述: 求n个四边平行于坐标轴的矩形的面积并。

### 2.8 窗口的星星 (洛谷 P1502)
- 题目链接: https://www.luogu.com.cn/problem/P1502
- 题目描述: 给定一些星星的位置和亮度，求一个固定大小的窗口内星星亮度总和的最大值。

## 3. 线段相交问题

### 3.1 线段相交 (POJ 2653)
- 题目链接: http://poj.org/problem?id=2653
- 题目描述: 给出n条线段，按照顺序放置，每次放置的线段可能会覆盖之前放置的线段。找出所有没有被其他线段覆盖的线段。

### 3.2 Picture (POJ 1177)
- 题目链接: http://poj.org/problem?id=1177
- 题目描述: 给定多个矩形，计算这些矩形并集的周长。

### 3.3 矩形周长并 (HDU 1828)
- 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1828
- 题目描述: 给定多个矩形，计算这些矩形并集的周长。

## 4. 圆相关问题

### 4.1 监狱突围 (HDU 3511)
- 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=3511
- 题目描述: 在一个圆形监狱周围有许多圆形障碍物，求从监狱能到达的最深深度。

## 5. 点相关问题

### 5.1 最近点对问题
- 经典计算几何问题，使用扫描线算法可以在O(n log n)时间内解决。

## 6. 其他扫描线问题

### 6.1 跳跃游戏 II (Leetcode 45)
- 题目链接: https://leetcode.cn/problems/jump-game-ii/
- 题目描述: 给你一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。返回到达 nums[n - 1] 的最小跳跃次数。

### 6.2 神奇字符串 (Leetcode 481)
- 题目链接: https://leetcode.cn/problems/magical-string/
- 题目描述: 神奇字符串 s 仅由 '1' 和 '2' 组成，并需要遵守下面的规则：神奇字符串 s 的神奇之处在于，串联字符串中 '1' 和 '2' 的连续出现次数可以生成该字符串。

### 6.3 November Rain (POJ 1765)
- 题目链接: http://poj.org/problem?id=1765
- 题目描述: 给定多个屋顶的斜面，计算从每个斜面流下的雨水量。

### 6.4 降雨量 (洛谷 P2471)
- 题目链接: https://www.luogu.com.cn/problem/P2471
- 题目描述: 根据历史降雨量数据，回答一些关于降雨量的询问。

### 6.5 数学计算 (洛谷 P4588)
- 题目链接: https://www.luogu.com.cn/problem/P4588
- 题目描述: 在一个数据结构中维护一个初始值为1的变量，支持两种操作：乘上一个变量或除以一个变量。

### 6.6 降雨量加强版 (洛谷 P2824)
- 题目链接: https://www.luogu.com.cn/problem/P2824
- 题目描述: 给定一个排列，支持区间升序排序和降序排序操作，求最终某个位置的值。

## 7. Codeforces题目

### 7.1 Divide Square (Codeforces 1401E)
- 题目链接: https://codeforces.com/problemset/problem/1401/E
- 题目描述: 给定一个正方形和一些水平线段和垂直线段，计算这些线段将正方形分割成多少个区域。

## 8. 其他平台题目

### 8.1 Rectangles (Gym 101982F)
- 题目链接: https://codeforces.com/gym/101982/problem/F
- 题目描述: 给定多个轴对齐的矩形，计算被奇数个矩形覆盖的区域面积。

### 8.2 Finally, christmas! (Gym 102448F)
- 题目链接: https://codeforces.com/gym/102448/problem/F
- 题目描述: 计算城市装饰的总面积。

## 9. 新增实现题目

### 9.1 矩形面积并 (POJ 1151, HDU 1542)
- 题目链接: POJ 1151: http://poj.org/problem?id=1151
- 题目链接: HDU 1542: http://acm.hdu.edu.cn/showproblem.php?pid=1542
- 题目描述: 给定多个矩形，计算这些矩形并集的面积。
- 实现文件: Code07_RectangleAreaUnion.java/.cpp/.py

### 9.2 矩形周长并 (POJ 1177, HDU 1828)
- 题目链接: POJ 1177: http://poj.org/problem?id=1177
- 题目链接: HDU 1828: http://acm.hdu.edu.cn/showproblem.php?pid=1828
- 题目描述: 给定多个矩形，计算这些矩形并集的周长。
- 实现文件: Code08_RectanglePerimeterUnion.java/.cpp/.py

### 9.3 天际线问题 (LeetCode 218)
- 题目链接: https://leetcode.cn/problems/the-skyline-problem/
- 题目描述: 城市的天际线是从远处观看建筑物形成的轮廓线。
- 实现文件: Code09_SkylineProblem.java/.cpp/.py

### 9.4 完美矩形 (LeetCode 391)
- 题目链接: https://leetcode.cn/problems/perfect-rectangle/
- 题目描述: 判断多个矩形是否能精确覆盖某个矩形区域。
- 实现文件: Code10_PerfectRectangle.java/.cpp/.py

### 9.5 矩形面积 II (LeetCode 850)
- 题目链接: https://leetcode.cn/problems/rectangle-area-ii/
- 题目描述: 计算多个矩形覆盖的总面积。
- 实现文件: Code11_RectangleAreaII.java/.cpp/.py

### 9.6 我的日程安排表系列 (LeetCode 729, 731, 732)
- 题目链接: 
  - 729: https://leetcode.cn/problems/my-calendar-i/
  - 731: https://leetcode.cn/problems/my-calendar-ii/
  - 732: https://leetcode.cn/problems/my-calendar-iii/
- 题目描述: 实现日程安排系统，支持不同级别的预订检查。
- 实现文件: Code12_MyCalendarSeries.java/.cpp/.py

### 9.7 覆盖的面积 (HDU 1255)
- 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1255
- 题目描述: 计算被至少两个矩形覆盖的区域面积。
- 实现文件: Code13_CoveredArea.java/.cpp/.py

### 9.8 窗口的星星 (洛谷 P1502)
- 题目链接: https://www.luogu.com.cn/problem/P1502
- 题目描述: 给定星星位置和亮度，求固定大小窗口内亮度总和的最大值。
- 实现文件: Code14_WindowStars.java/.cpp/.py

## 算法核心思想

扫描线算法是一种重要的计算几何算法，其核心思想是：

1. 将问题转化为事件点处理
2. 按照特定顺序处理事件点
3. 维护一个状态结构来记录当前情况
4. 根据状态变化计算结果

## 常见应用场景

1. 几何问题（矩形面积、周长、线段相交等）
2. 区间问题（会议室安排、区间合并等）
3. 图形覆盖问题
4. 最优化问题

## 数据结构选择

在扫描线算法中，常用的数据结构包括：

1. 优先队列 - 用于事件点排序
2. 平衡二叉搜索树 - 维护当前状态
3. 线段树 - 处理区间查询和更新
4. 并查集 - 处理集合合并问题
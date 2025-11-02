# 线段树相关题目汇总

## 经典题目列表

### 1. HDU 题目

#### HDU 5306 Gorgeous Sequence
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=5306
- **题目描述**: 维护一个序列，支持区间取min操作，查询区间最大值和区间和
- **算法**: 吉司机线段树
- **难度**: 困难
- **相关文件**: [Code03_SegmentTreeSetminQueryMaxSum2.java](Code03_SegmentTreeSetminQueryMaxSum2.java)

#### HDU 1166 敌兵布阵
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1166
- **题目描述**: 单点更新，区间求和
- **算法**: 基础线段树
- **难度**: 简单

#### HDU 1754 I Hate It
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1754
- **题目描述**: 单点更新，区间最值查询
- **算法**: 基础线段树
- **难度**: 简单

#### HDU 1698 Just a Hook
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1698
- **题目描述**: 区间更新，区间求和
- **算法**: 线段树 + 懒惰标记
- **难度**: 中等

#### HDU 1542 Atlantis
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1542
- **题目描述**: 矩形面积并
- **算法**: 线段树 + 扫描线 + 离散化
- **难度**: 困难

#### HDU 438D The Child and Sequence
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=438D
- **题目描述**: 区间取模，区间最大值，区间和
- **算法**: 吉司机线段树
- **难度**: 困难

### 2. POJ 题目

#### POJ 3468 A Simple Problem with Integers
- **题目链接**: http://poj.org/problem?id=3468
- **题目描述**: 区间加法，区间求和
- **算法**: 线段树 + 懒惰标记
- **难度**: 中等

#### POJ 2528 Mayor's posters
- **题目链接**: http://poj.org/problem?id=2528
- **题目描述**: 区间染色问题，求可见海报数
- **算法**: 线段树 + 离散化
- **难度**: 中等

#### POJ 2777 Count Color
- **题目链接**: http://poj.org/problem?id=2777
- **题目描述**: 区间染色，查询区间颜色数
- **算法**: 线段树 + 位运算
- **难度**: 中等
- **相关文件**: [Code08_CountColor.java](Code08_CountColor.java), [Code08_CountColor.cpp](Code08_CountColor.cpp), [Code08_CountColor.py](Code08_CountColor.py)

#### POJ 1177 Picture
- **题目链接**: http://poj.org/problem?id=1177
- **题目描述**: 矩形周长并
- **算法**: 线段树 + 扫描线
- **难度**: 困难

#### POJ 3667 Hotel
- **题目链接**: http://poj.org/problem?id=3667
- **题目描述**: 区间分配问题，支持区间占用和释放
- **算法**: 线段树 + 区间合并
- **难度**: 困难

### 3. 洛谷题目

#### P3372 【模板】线段树 1
- **题目链接**: https://www.luogu.com.cn/problem/P3372
- **题目描述**: 区间加法，区间求和
- **算法**: 线段树 + 懒惰标记
- **难度**: 中等

#### P3373 【模板】线段树 2
- **题目链接**: https://www.luogu.com.cn/problem/P3373
- **题目描述**: 区间乘法和加法，区间求和
- **算法**: 线段树 + 双懒惰标记
- **难度**: 中等

#### P6242 【模板】线段树 3
- **题目链接**: https://www.luogu.com.cn/problem/P6242
- **题目描述**: 区间加法、区间取min、区间求和、区间最值、区间历史最值
- **算法**: 吉司机线段树 + 历史信息维护
- **难度**: 困难
- **相关文件**: [Code05_MaximumMinimumHistory.java](Code05_MaximumMinimumHistory.java)

#### P5490 【模板】扫描线
- **题目链接**: https://www.luogu.com.cn/problem/P5490
- **题目描述**: 矩形面积并
- **算法**: 线段树 + 扫描线 + 离散化
- **难度**: 困难

### 4. LeetCode 题目

#### 307. Range Sum Query - Mutable
- **题目链接**: https://leetcode.cn/problems/range-sum-query-mutable/
- **题目描述**: 数组可变时的区间求和
- **算法**: 线段树
- **难度**: 中等

#### 715. Range Module
- **题目链接**: https://leetcode.cn/problems/range-module/
- **题目描述**: 范围模块，支持添加、查询和删除区间
- **算法**: 动态开点线段树
- **难度**: 困难
- **相关文件**: [Code01_DynamicSegmentTree.java](Code01_DynamicSegmentTree.java), [Code02_CountIntervals.java](Code02_CountIntervals.java)

#### 218. The Skyline Problem
- **题目链接**: https://leetcode.cn/problems/the-skyline-problem/
- **题目描述**: 天际线问题
- **算法**: 线段树 + 扫描线 或 multiset
- **难度**: 困难

#### 699. Falling Squares
- **题目链接**: https://leetcode.cn/problems/falling-squares/
- **题目描述**: 掉落的方块
- **算法**: 线段树 + 懒惰标记
- **难度**: 困难

#### 731. My Calendar II
- **题目链接**: https://leetcode.cn/problems/my-calendar-ii/
- **题目描述**: 我的日程安排表II（检测三重预订）
- **算法**: 动态开点线段树
- **难度**: 中等

#### 732. My Calendar III
- **题目链接**: https://leetcode.cn/problems/my-calendar-iii/
- **题目描述**: 我的日程安排表III（检测K重预订）
- **算法**: 动态开点线段树
- **难度**: 困难

### 5. Codeforces 题目

#### 52C Circular RMQ
- **题目链接**: https://codeforces.com/problemset/problem/52/C
- **题目描述**: 循环区间最小值查询
- **算法**: 线段树 + 懒惰标记
- **难度**: 中等

#### 380C Sereja and Brackets
- **题目链接**: https://codeforces.com/problemset/problem/380/C
- **题目描述**: 括号匹配查询
- **算法**: 线段树
- **难度**: 中等

#### 446C DZY Loves Fibonacci Numbers
- **题目链接**: https://codeforces.com/problemset/problem/446/C
- **题目描述**: 斐波那契数列区间加法
- **算法**: 线段树 + 斐波那契性质
- **难度**: 困难

#### 438D The Child and Sequence
- **题目链接**: https://codeforces.com/problemset/problem/438/D
- **题目描述**: 区间取模，区间最大值，区间和
- **算法**: 吉司机线段树
- **难度**: 困难

### 6. SPOJ 题目

#### GSS1 Can you answer these queries I
- **题目链接**: https://www.spoj.com/problems/GSS1/
- **题目描述**: 最大子段和查询
- **算法**: 线段树
- **难度**: 中等
- **相关文件**: [Code06_MaximumSubarraySum.java](Code06_MaximumSubarraySum.java), [Code06_MaximumSubarraySum.cpp](Code06_MaximumSubarraySum.cpp), [Code06_MaximumSubarraySum.py](Code06_MaximumSubarraySum.py)

#### GSS2 Can you answer these queries II
- **题目链接**: https://www.spoj.com/problems/GSS2/
- **题目描述**: 历史最大子段和查询
- **算法**: 线段树 + 历史信息维护
- **难度**: 困难

#### GSS3 Can you answer these queries III
- **题目链接**: https://www.spoj.com/problems/GSS3/
- **题目描述**: 最大子段和查询（支持单点更新）
- **算法**: 线段树
- **难度**: 中等

#### GSS4 Can you answer these queries IV
- **题目链接**: https://www.spoj.com/problems/GSS4/
- **题目描述**: 区间开方，区间求和
- **算法**: 线段树 + 区间操作
- **难度**: 中等

#### GSS5 Can you answer these queries V
- **题目链接**: https://www.spoj.com/problems/GSS5/
- **题目描述**: 最大子段和查询（支持任意区间）
- **算法**: 线段树
- **难度**: 中等

#### GSS6 Can you answer these queries VI
- **题目链接**: https://www.spoj.com/problems/GSS6/
- **题目描述**: 支持插入、删除、修改、查询最大子段和
- **算法**: 平衡树 + 线段树
- **难度**: 困难

#### GSS7 Can you answer these queries VII
- **题目链接**: https://www.spoj.com/problems/GSS7/
- **题目描述**: 树上路径最大子段和查询
- **算法**: 树链剖分 + 线段树
- **难度**: 困难

#### HORRIBLE Horrible Queries
- **题目链接**: https://www.spoj.com/problems/HORRIBLE/
- **题目描述**: 区间加法，区间求和
- **算法**: 线段树 + 懒惰标记
- **难度**: 中等

#### KGSS Maximum Sum
- **题目链接**: https://www.spoj.com/problems/KGSS/
- **题目描述**: 查询区间内两个最大值的和
- **算法**: 线段树
- **难度**: 中等
- **相关文件**: [Code07_MaximumTwoValuesSum.java](Code07_MaximumTwoValuesSum.java), [Code07_MaximumTwoValuesSum.cpp](Code07_MaximumTwoValuesSum.cpp), [Code07_MaximumTwoValuesSum.py](Code07_MaximumTwoValuesSum.py)

## 题目分类

### 按操作类型分类

#### 1. 基础操作
- 单点更新 + 区间查询
  - HDU 1166 敌兵布阵
  - HDU 1754 I Hate It
- 区间更新 + 区间查询
  - POJ 3468 A Simple Problem with Integers
  - HDU 1698 Just a Hook
  - 洛谷 P3372 【模板】线段树 1

#### 2. 高级操作
- 区间最值操作
  - HDU 5306 Gorgeous Sequence
  - Codeforces 438D The Child and Sequence
- 区间历史信息维护
  - 洛谷 P6242 【模板】线段树 3
  - LeetCode 715. Range Module
  - SPOJ GSS2 Can you answer these queries II
- 多维线段树
  - POJ 1177 Picture
  - HDU 1542 Atlantis

#### 3. 特殊应用
- 离散化 + 线段树
  - POJ 2528 Mayor's posters
  - POJ 2777 Count Color
- 扫描线 + 线段树
  - POJ 1177 Picture
  - HDU 1542 Atlantis
  - LeetCode 218. The Skyline Problem
  - 洛谷 P5490 【模板】扫描线
- 线段树 + 其他算法
  - Codeforces 446C DZY Loves Fibonacci Numbers (斐波那契性质)
  - SPOJ GSS1/GSS3 (最大子段和)
  - SPOJ KGSS (两个最大值之和)
  - SPOJ GSS4 (区间开方)
  - SPOJ GSS6/GSS7 (平衡树/树链剖分)

### 按难度分类

#### 简单 (适合入门)
1. HDU 1166 敌兵布阵
2. HDU 1754 I Hate It
3. 洛谷 P3372 【模板】线段树 1

#### 中等 (掌握基础后练习)
1. POJ 3468 A Simple Problem with Integers
2. HDU 1698 Just a Hook
3. POJ 2528 Mayor's posters
4. POJ 2777 Count Color
5. 洛谷 P3373 【模板】线段树 2
6. LeetCode 307. Range Sum Query - Mutable
7. Codeforces 52C Circular RMQ
8. SPOJ HORRIBLE Horrible Queries
9. SPOJ GSS1 Can you answer these queries I
10. SPOJ KGSS Maximum Sum
11. SPOJ GSS3 Can you answer these queries III
12. SPOJ GSS4 Can you answer these queries IV
13. SPOJ GSS5 Can you answer these queries V

#### 困难 (高阶应用)
1. HDU 5306 Gorgeous Sequence
2. 洛谷 P6242 【模板】线段树 3
3. LeetCode 715. Range Module
4. LeetCode 218. The Skyline Problem
5. Codeforces 438D The Child and Sequence
6. Codeforces 446C DZY Loves Fibonacci Numbers
7. POJ 1177 Picture
8. HDU 1542 Atlantis
9. SPOJ GSS2 Can you answer these queries II
10. SPOJ GSS6 Can you answer these queries VI
11. SPOJ GSS7 Can you answer these queries VII
12. LeetCode 732. My Calendar III

## 解题技巧总结

### 1. 基础线段树技巧
- 理解线段树的递归结构
- 掌握 push_up 操作（自底向上更新）
- 熟练使用懒惰标记优化区间更新

### 2. 动态开点技巧
- 按需创建节点，节省空间
- 注意左右子节点的动态分配
- 合理设置空间上限

### 3. 区间最值操作技巧
- 维护最大值、次大值和最大值个数
- 利用势能分析法分析时间复杂度
- 区分三种更新情况：
  1. 更新值 >= 最大值：无需更新
  2. 次大值 < 更新值 < 最大值：直接更新
  3. 更新值 <= 次大值：递归处理

### 4. 历史信息维护技巧
- 使用多重懒惰标记
- 区分最大值和非最大值的处理
- 维护历史最大值、历史最小值等信息

### 5. 离散化技巧
- 对大数据范围进行映射处理
- 注意相邻点之间添加额外点避免错误
- 结合离散化和线段树解决区间问题

### 6. 位运算优化技巧
- 使用位运算表示集合状态
- 通过位运算快速计算集合操作
- 适用于颜色统计、状态压缩等问题

### 7. 最大子段和技巧
- 维护区间最大子段和、左最大子段和、右最大子段和
- 通过合并子区间信息得到父区间信息
- 适用于区间最大子段和查询问题

## 常见错误及注意事项

### 1. 数组大小问题
- 静态线段树通常需要开 4 倍空间
- 动态开点线段树需要合理估算节点数量

### 2. 懒惰标记问题
- push_down 操作必须正确实现
- 多种标记的优先级处理
- 标记的正确下传和清除

### 3. 离散化问题
- 相邻点之间可能需要添加额外点
- 端点处理要特别注意
- 离散化后的坐标映射要正确

### 4. 边界条件问题
- 空区间查询的处理
- 单点区间和区间长度为1的处理
- 递归终止条件的正确判断

### 5. 位运算问题
- 注意位运算的优先级
- 正确使用左移右移操作
- 避免整数溢出问题

## 学习建议

### 1. 学习路径
1. 先掌握基础线段树（单点更新/查询）
2. 学习区间更新和懒惰标记
3. 理解动态开点线段树
4. 掌握区间最值操作（吉司机线段树）
5. 学习历史信息维护
6. 练习各类经典题目

### 2. 实践建议
- 每种类型的题目至少练习3-5道
- 对比不同实现方式的优劣
- 注重代码的可读性和可维护性
- 总结每道题目的关键点和易错点

### 3. 进阶方向
- 多维线段树
- 线段树与其他数据结构结合
- 线段树在实际项目中的应用
- 线段树的扩展应用（如李超线段树等）

## 新增题目列表

以下是我们新增实现的题目：

### SPOJ GSS1 - Can you answer these queries I
- **题目描述**: 最大子段和查询
- **算法**: 线段树
- **难度**: 中等
- **相关文件**: [Code06_MaximumSubarraySum.java](Code06_MaximumSubarraySum.java), [Code06_MaximumSubarraySum.cpp](Code06_MaximumSubarraySum.cpp), [Code06_MaximumSubarraySum.py](Code06_MaximumSubarraySum.py)

### SPOJ KGSS - Maximum Sum
- **题目描述**: 查询区间内两个最大值的和
- **算法**: 线段树
- **难度**: 中等
- **相关文件**: [Code07_MaximumTwoValuesSum.java](Code07_MaximumTwoValuesSum.java), [Code07_MaximumTwoValuesSum.cpp](Code07_MaximumTwoValuesSum.cpp), [Code07_MaximumTwoValuesSum.py](Code07_MaximumTwoValuesSum.py)

### POJ 2777 - Count Color
- **题目描述**: 区间染色，查询区间颜色数
- **算法**: 线段树 + 位运算
- **难度**: 中等
- **相关文件**: [Code08_CountColor.java](Code08_CountColor.java), [Code08_CountColor.cpp](Code08_CountColor.cpp), [Code08_CountColor.py](Code08_CountColor.py)
# Class140 - 数论算法与线性丢番图方程

## 概述

Class140主要讲解数论中的核心算法，包括扩展欧几里得算法、最大公约数计算、线性丢番图方程求解、Pick定理应用以及赛瓦维斯特定理等。这些算法在解决各种数学问题和编程竞赛题目中具有重要作用。

## 核心算法详解

### 1. 扩展欧几里得算法 (Extended Euclidean Algorithm)

#### 基本概念
扩展欧几里得算法不仅能够计算两个整数a和b的最大公约数，还能找到整数x和y，使得ax + by = gcd(a, b)。

#### 核心思想
基于欧几里得算法的递归性质，通过回溯过程求解线性丢番图方程。

#### 时间复杂度
O(log(min(a,b)))

#### 应用场景
- 求解线性丢番图方程
- 计算模逆元
- 解决同余方程

### 2. 线性丢番图方程 (Linear Diophantine Equations)

#### 基本概念
形如ax + by = c的方程，其中a、b、c为整数，求整数解x和y。

#### 解的存在性
方程ax + by = c有整数解当且仅当gcd(a,b)能整除c。

#### 通解公式
如果(x0,y0)是ax + by = c的一组特解，那么通解为：
- x = x0 + (b/gcd(a,b)) * t
- y = y0 - (a/gcd(a,b)) * t
其中t为任意整数。

### 3. Pick定理 (Pick's Theorem)

#### 基本概念
用于计算顶点均为格点的简单多边形的面积。

#### 公式
A = i + b/2 - 1
其中A是多边形面积，i是内部格点数，b是边界格点数。

#### 应用场景
- 计算格点多边形面积
- 统计格点数量

### 4. 赛瓦维斯特定理 (Chicken McNugget Theorem)

#### 基本概念
当正整数a和b互质时，不能表示为ax+by（x,y≥0）的最大整数是ab-a-b。

#### 应用场景
- 硬币问题
- 数论问题

## 题目详解

### 1. 二元一次不定方程 (Code01_DiophantineEquation)

#### 问题描述
给定a、b、c，求解方程ax + by = c。

#### 解题思路
1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
4. 根据通解公式求出满足条件的解

#### 相关题目
1. **洛谷 P5656 【模板】二元一次不定方程 (exgcd)**
   - 链接：https://www.luogu.com.cn/problem/P5656
   - 这是本题的来源，是一道模板题

2. **LeetCode 1250. 检查「好数组」**
   - 链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
   - 本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组

3. **Codeforces 1244C. The Football Stage**
   - 链接：https://codeforces.com/problemset/problem/1244/C
   - 本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量

4. **HDU 5512 Pagodas**
   - 链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
   - 本题涉及数论知识，与最大公约数有关

5. **POJ 2115 C Looooops**
   - 链接：http://poj.org/problem?id=2115
   - 本题需要求解模线性方程，可以转化为线性丢番图方程

### 2. 青蛙的约会 (Code02_FrogsMeeting)

#### 问题描述
有两只青蛙A和B在一个圆环上，给定它们的初始位置和跳跃速度，求它们何时能相遇。

#### 解题思路
1. 建立方程：设t秒后相遇，则有 (x1 + m*t) ≡ (x2 + n*t) (mod l)
2. 化简方程：(m-n)*t ≡ (x2-x1) (mod l)
3. 转换为线性丢番图方程：(m-n)*t + l*k = (x2-x1)
4. 使用扩展欧几里得算法求解

#### 相关题目
1. **洛谷 P1516 青蛙的约会**
   - 链接：https://www.luogu.com.cn/problem/P1516
   - 这是本题的来源，是一道经典题

2. **POJ 1061 青蛙的约会**
   - 链接：http://poj.org/problem?id=1061
   - 与本题完全相同，是POJ上的经典题目

3. **HDU 5512 Pagodas**
   - 链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
   - 本题涉及数论知识，与最大公约数有关

### 3. 格点连线上有几个格点 (Code03_HowManyPoints)

#### 问题描述
给定两个格点A(x1,y1)和B(x2,y2)，求线段AB上格点的数量（包括端点）。

#### 解题思路
1. 线段上的格点数量等于dx和dy的最大公约数加1
2. dx = |x2-x1|，dy = |y2-y1|
3. 结果 = gcd(dx, dy) + 1

#### 相关题目
1. **LightOJ 1077 How Many Points?**
   - 链接：https://lightoj.com/problem/how-many-points
   - 这是本题的来源，是一道经典题

2. **POJ 1265 Area**
   - 链接：http://poj.org/problem?id=1265
   - 本题需要计算多边形边界上的格点数量，用到了相同的知识点

### 4. 机器人的移动区域 (Code04_Area)

#### 问题描述
机器人在二维网格上移动形成一个简单多边形，求多边形内部格点数、边界格点数和面积。

#### 解题思路
1. 使用鞋带公式计算多边形面积
2. 使用gcd计算每条边上的格点数，累加得到边界格点数
3. 使用Pick定理计算内部格点数：内部格点数 = 面积 - 边界格点数/2 + 1

#### 相关题目
1. **POJ 1265 Area**
   - 链接：http://poj.org/problem?id=1265
   - 这是本题的来源，是一道经典题

2. **UVA 10088 - Trees on My Island**
   - 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1029
   - 本题同样是Pick定理的应用

### 5. 无法组成的最大值 (Code05_LargestUnattainable)

#### 问题描述
给定两种面值为a和b的硬币（a和b互质），每种硬币有无限个，求无法用这两种硬币组成的最大钱数。

#### 解题思路
1. 根据赛瓦维斯特定理（Chicken McNugget Theorem），当a和b互质时，
   无法表示的最大整数是a*b-a-b

#### 相关题目
1. **洛谷 P3951 [NOIP2017 提高组] 小凯的疑惑**
   - 链接：https://www.luogu.com.cn/problem/P3951
   - 这是本题的来源，是一道经典题

2. **HDU 1792 A New Change Problem**
   - 链接：https://acm.hdu.edu.cn/showproblem.php?pid=1792
   - 本题是小凯的疑惑的变形，求无法表示的最大数和无法表示的数的个数

## 新增题目详解

### 6. LeetCode 365. 水壶问题 (Code09_LeetCode365_WaterJug)

#### 问题描述
有两个容量分别为 x 升和 y 升的水壶以及无限多的水。请判断能否通过使用这两个水壶，从而可以得到恰好 z 升的水？

#### 解题思路
1. 根据裴蜀定理，如果 z 是 x 和 y 的最大公约数的倍数，且 z <= x + y，则有解
2. 特殊情况：如果 z == 0，直接返回 true
3. 如果 x + y < z，返回 false
4. 如果 x == 0 或 y == 0，需要特殊处理

#### 相关题目
1. **LeetCode 365. 水壶问题**
   - 链接：https://leetcode.cn/problems/water-and-jug-problem/
   - 这是本题的来源，是一道经典题

2. **POJ 2142 The Balance**
   - 链接：http://poj.org/problem?id=2142
   - 本题需要求解线性丢番图方程并找到最优解

### 7. LeetCode 878. 第 N 个神奇数字 (Code10_LeetCode878_NthMagicalNumber)

#### 问题描述
如果正整数可以被 A 或 B 整除，那么它是神奇的。返回第 N 个神奇数字。由于答案可能非常大，返回它模 10^9 + 7 的结果。

#### 解题思路
1. 使用二分搜索法在可能的范围内查找第 N 个神奇数字
2. 对于给定的数字 x，计算小于等于 x 的神奇数字个数
3. 神奇数字个数 = x/A + x/B - x/lcm(A,B)
4. 使用容斥原理避免重复计数

#### 相关题目
1. **LeetCode 878. 第 N 个神奇数字**
   - 链接：https://leetcode.cn/problems/nth-magical-number/
   - 这是本题的来源，是一道经典题

### 8. POJ 2142 The Balance (Code11_Poj2142_TheBalance)

#### 问题描述
给定a、b、c，求解方程ax + by = c，要求找到一组解(x,y)，使得|x| + |y|最小。如果有多个解，选择x最小的解。

#### 解题思路
1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
4. 根据通解公式求出满足条件的解
5. 在所有解中寻找|x| + |y|最小的解

#### 相关题目
1. **POJ 2142 The Balance**
   - 链接：http://poj.org/problem?id=2142
   - 这是本题的来源，是一道经典题

### 9. Codeforces 7C. Line (Code12_Codeforces7C_Line)

#### 问题描述
给定直线方程Ax + By + C = 0，求直线上任意一个整数点(x,y)。如果不存在整数点，输出-1。

#### 解题思路
1. 将直线方程转换为标准形式：Ax + By = -C
2. 使用扩展欧几里得算法求解方程Ax + By = gcd(A,B)的一组特解
3. 判断方程是否有整数解：当-C能被gcd(A,B)整除时有解
4. 如果有解，将特解乘以(-C)/gcd(A,B)得到原方程的一组特解

#### 相关题目
1. **Codeforces 7C. Line**
   - 链接：https://codeforces.com/problemset/problem/7C
   - 这是本题的来源，是一道经典题

### 10. UVA 10090 Marbles (Code13_Uva10090_Marbles)

#### 问题描述
有两种盒子：第一种盒子可以装n1个弹珠，价格为c1；第二种盒子可以装n2个弹珠，价格为c2。需要装恰好n个弹珠，求最小总价格。如果无法恰好装n个弹珠，输出"failed"。

#### 解题思路
1. 设第一种盒子用x个，第二种盒子用y个，则方程为：n1*x + n2*y = n
2. 使用扩展欧几里得算法求解方程
3. 判断方程是否有解：当n能被gcd(n1,n2)整除时有解
4. 如果有解，根据通解公式求出所有可能的解
5. 在所有解中寻找c1*x + c2*y最小的解

#### 相关题目
1. **UVA 10090 Marbles**
   - 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
   - 这是本题的来源，是一道经典题

## 算法技巧总结

### 见到什么样的题目用这种数据结构与算法

1. **线性丢番图方程问题**
   - 特征：涉及形如ax + by = c的方程求解
   - 适用算法：扩展欧几里得算法

2. **同余方程问题**
   - 特征：涉及模运算的方程
   - 适用算法：扩展欧几里得算法转化为线性丢番图方程

3. **格点计数问题**
   - 特征：涉及网格点上的几何计算
   - 适用算法：最大公约数、Pick定理

4. **硬币问题**
   - 特征：涉及用固定面值硬币组成金额
   - 适用算法：赛瓦维斯特定理

## 工程化考量

### 1. 异常处理
- 输入验证：检查输入参数的有效性
- 特殊情况处理：处理边界输入、极端数据
- 错误信息清晰提示

### 2. 性能优化
- 时间复杂度优化：通过数学方法优化算法复杂度
- 空间复杂度优化：使用原地操作，减少内存占用
- 防止溢出：使用适当的数据类型处理大数运算

### 3. 可读性
- 变量命名：使用有意义的变量名
- 注释完整：为每个方法和关键步骤添加详细注释
- 模块化：将复杂逻辑拆分为独立函数

### 4. 跨语言实现
- Java版本：面向对象实现，详细注释
- C++版本：高效实现，适合竞赛
- Python版本：简洁实现，适合快速验证

## 复杂度分析

### 时间复杂度
- 扩展欧几里得算法：O(log(min(a,b)))
- 最大公约数计算：O(log(min(a,b)))
- Pick定理应用：O(n*log(max(dx,dy)))

### 空间复杂度
- 扩展欧几里得算法：O(log(min(a,b)))（递归调用栈）
- 其他算法：O(1)

## 学习建议

1. 熟练掌握扩展欧几里得算法的原理和实现
2. 理解线性丢番图方程的解法和应用
3. 掌握Pick定理和赛瓦维斯特定理的应用场景
4. 多做练习题，加深对算法本质的理解
5. 注意算法在工程实践中的应用

## 参考资料

1. 《算法导论》
2. 《算法竞赛入门经典》
3. 《挑战程序设计竞赛》
4. 各大OJ平台的官方题解
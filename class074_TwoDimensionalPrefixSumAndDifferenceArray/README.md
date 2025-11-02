# 二维前缀和与差分数组算法详解与题目实现

## 算法简介

二维前缀和与差分数组是处理二维数组区间操作的重要算法技巧。二维前缀和主要用于快速计算子矩阵元素和，而二维差分数组则用于高效处理子矩阵的区间更新操作。

### 二维前缀和

对于二维数组`matrix`，其二维前缀和数组`preSum`定义为：
```
preSum[i][j] = sum of all elements in rectangle from (0,0) to (i-1,j-1)
```

区域和计算（容斥原理）：
```
sum((r1,c1), (r2,c2)) = preSum[r2+1][c2+1] - preSum[r1][c2+1] - preSum[r2+1][c1] + preSum[r1][c1]
```

### 二维差分数组

二维差分数组是二维前缀和的逆运算。对于二维数组`matrix`，其二维差分数组`diff`用于处理区域更新操作：
- 对子矩阵区域`[(r1,c1),(r2,c2)]`中的每个元素加上`x`，可以通过以下操作实现：
  1. `diff[r1][c1] += x`
  2. `diff[r2+1][c1] -= x`
  3. `diff[r1][c2+1] -= x`
  4. `diff[r2+1][c2+1] += x`
  5. 然后通过计算二维差分数组的二维前缀和得到更新后的原数组

## 应用场景

1. 图像处理中的区域统计和操作
2. 游戏开发中的区域影响计算
3. 地理信息系统中的区域统计
4. 机器学习中的特征提取
5. 资源分配问题
6. 计算机视觉中的目标检测
7. 游戏地图中的区域影响计算
8. 数据分析中的区域统计

## 时间复杂度分析

- 二维前缀和构建: O(m*n)
- 二维前缀和查询: O(1)
- 二维差分数组更新: O(1)
- 二维差分数组还原: O(m*n)

## 空间复杂度分析

- 需要额外的前缀和/差分数组空间: O(m*n)

## 已实现题目列表

### 题目1: 二维前缀和矩阵
**题目来源**: 基础模板题  
**相关题目**: 
- LeetCode 304. Range Sum Query 2D - Immutable
- Codeforces 1371C - A Cookie for You
- AtCoder ABC106D - AtCoder Express 2
- HDU 1556 Color the ball（二维扩展）
- POJ 2155 Matrix
- SPOJ MATSUM - Matrix Summation  
**实现文件**: 
- [Code01_PrefixSumMatrix.java](Code01_PrefixSumMatrix.java)

### 题目2: 边框为1的最大正方形
**题目来源**: LeetCode 1139  
**题目链接**: https://leetcode.cn/problems/largest-1-bordered-square/  
**相关题目**: 
- LeetCode 221. 最大正方形
- LeetCode 764. 最大加号标志
- Codeforces 835C - Star sky
- HDU 1559 最大子矩阵
- POJ 1050 To the Max  
**实现文件**: 
- [Code02_LargestOneBorderedSquare.java](Code02_LargestOneBorderedSquare.java)

### 题目3: 二维差分数组（洛谷版）
**题目来源**: 洛谷 P3397 地毯  
**题目链接**: https://www.luogu.com.cn/problem/P3397  
**相关题目**: 
- Codeforces 835C - Star sky
- LeetCode 2132. 用邮票贴满网格图
- HDU 1556 Color the ball
- POJ 2155 Matrix
- SPOJ UPDATEIT - Update the array  
**实现文件**: 
- [Code03_DiffMatrixLuogu.java](Code03_DiffMatrixLuogu.java)

### 题目4: 二维差分数组（牛客版）
**题目来源**: 牛客 226337 二维差分  
**题目链接**: https://www.nowcoder.com/practice/50e1a93989df42efb0b1dec386fb4ccc  
**相关题目**: 
- 洛谷 P3397 地毯
- LeetCode 2132. 用邮票贴满网格图
- Codeforces 816B - Karen and Coffee
- AtCoder ABC106D - AtCoder Express 2  
**实现文件**: 
- [Code03_DiffMatrixNowcoder.java](Code03_DiffMatrixNowcoder.java)

### 题目5: 用邮票贴满网格图
**题目来源**: LeetCode 2132  
**题目链接**: https://leetcode.cn/problems/stamping-the-grid/  
**相关题目**: 
- Codeforces 816B - Karen and Coffee
- AtCoder ABC106D - AtCoder Express 2
- HDU 1556 Color the ball
- POJ 2155 Matrix
- SPOJ HORRIBLE - Horrible Queries  
**实现文件**: 
- [Code04_StampingTheGrid.java](Code04_StampingTheGrid.java)

### 题目6: 最强祝福力场
**题目来源**: LeetCode LCP 74  
**题目链接**: https://leetcode.cn/problems/xepqZ5/  
**相关题目**: 
- Codeforces 816B - Karen and Coffee
- AtCoder ABC106D - AtCoder Express 2
- HDU 1542 Atlantis（扫描线算法）
- POJ 1151 Atlantis
- SPOJ HISTOGRA - Largest Rectangle in a Histogram  
**实现文件**: 
- [Code05_StrongestForceField.java](Code05_StrongestForceField.java)

### 题目7: 二维区域和检索 - 矩阵不可变
**题目来源**: LeetCode 304  
**题目链接**: https://leetcode.cn/problems/range-sum-query-2d-immutable/  
**相关题目**: 
- LeetCode 303. Range Sum Query - Immutable
- Codeforces 1371C - A Cookie for You
- AtCoder ABC106D - AtCoder Express 2
- HDU 1559 最大子矩阵
- POJ 1050 To the Max  
**实现文件**: 
- [Code06_RangeSumQuery2DImmutable.java](Code06_RangeSumQuery2DImmutable.java)
- [Code06_RangeSumQuery2DImmutable.cpp](Code06_RangeSumQuery2DImmutable.cpp)
- [Code06_RangeSumQuery2DImmutable.py](Code06_RangeSumQuery2DImmutable.py)

### 题目8: 航班预订统计
**题目来源**: LeetCode 1109  
**题目链接**: https://leetcode.cn/problems/corporate-flight-bookings/  
**相关题目**: 
- LeetCode 370. Range Addition
- HackerRank Array Manipulation
- Codeforces 276C - Little Girl and Maximum Sum
- AtCoder ABC127D - Integer Cards
- SPOJ UPDATEIT - Update the array  
**实现文件**: 
- [Code07_CorporateFlightBookings.java](Code07_CorporateFlightBookings.java)
- [Code07_CorporateFlightBookings.cpp](Code07_CorporateFlightBookings.cpp)
- [Code07_CorporateFlightBookings.py](Code07_CorporateFlightBookings.py)

### 题目9: 子矩阵元素加1
**题目来源**: LeetCode 2536  
**题目链接**: https://leetcode.cn/problems/increment-submatrices-by-one/  
**相关题目**: 
- LeetCode 2132. 用邮票贴满网格图
- 牛客 226337 二维差分
- Codeforces 835C - Star sky
- HDU 1556 Color the ball
- POJ 2155 Matrix  
**实现文件**: 
- [Code08_IncrementSubmatricesByOne.java](Code08_IncrementSubmatricesByOne.java)
- [Code08_IncrementSubmatricesByOne.cpp](Code08_IncrementSubmatricesByOne.cpp)
- [Code08_IncrementSubmatricesByOne.py](Code08_IncrementSubmatricesByOne.py)

### 题目10: 接雨水问题
**题目来源**: LeetCode 42  
**题目链接**: https://leetcode.cn/problems/trapping-rain-water/  
**相关题目**: 
- LeetCode 407. Trapping Rain Water II
- LeetCode 11. Container With Most Water
- Codeforces 988D - Points and Powers of Two
- AtCoder ABC131D - Megalomania
- HDU 1506 Largest Rectangle in a Histogram
- POJ 2559 Largest Rectangle in a Histogram  
**实现文件**: 
- [Code18_TrappingRainWater.java](Code18_TrappingRainWater.java)
- [Code18_TrappingRainWater.cpp](Code18_TrappingRainWater.cpp)
- [Code18_TrappingRainWater.py](Code18_TrappingRainWater.py)

## 扩展题目列表（待实现）

### 经典二维前缀和题目
1. **LeetCode 221. 最大正方形** - 动态规划+前缀和优化
2. **LeetCode 1277. 统计全为1的正方形子矩阵** - 221题的扩展
3. **LeetCode 1504. 统计全1子矩形** - 二维前缀和的应用
4. **Codeforces 835C. Star sky** - 二维前缀和+时间维度
5. **HDU 1559. 最大子矩阵** - 最大子矩阵和问题

### 经典二维差分题目
1. **LeetCode 370. Range Addition** - 一维差分扩展到二维
2. **Codeforces 816B. Karen and Coffee** - 差分+前缀和统计
3. **AtCoder ABC106D. AtCoder Express 2** - 二维差分应用
4. **POJ 2155. Matrix** - 二维树状数组/差分
5. **SPOJ MATSUM. Matrix Summation** - 二维树状数组

### 扫描线算法相关
1. **LeetCode 218. 天际线问题** - 扫描线+优先队列
2. **LeetCode 850. 矩形面积 II** - 扫描线+离散化
3. **HDU 1542. Atlantis** - 经典扫描线问题
4. **POJ 1151. Atlantis** - 扫描线算法模板题
5. **SPOJ HISTOGRA. Largest Rectangle in a Histogram** - 单调栈应用

### 其他相关算法
1. **LeetCode 84. 柱状图中最大的矩形** - 单调栈
2. **LeetCode 85. 最大矩形** - 84题的二维扩展
3. **LeetCode 407. 接雨水 II** - 三维接雨水问题
4. **LeetCode 11. 盛最多水的容器** - 双指针应用

## 算法技巧总结

### 1. 二维前缀和
适用于快速计算子矩阵元素和，时间复杂度O(1)进行查询。

### 2. 二维差分数组
适用于二维矩阵的区域更新操作，通过容斥原理进行标记。

### 3. 坐标离散化
在处理浮点数坐标或大范围坐标时，通过离散化技术减少空间复杂度。

### 4. 边界处理
通过扩展数组边界避免特殊判断，简化代码实现。

### 5. 扫描线算法
处理矩形重叠、面积并集等问题的高效算法。

## 工程化考虑

### 1. 输入输出优化
- 使用BufferedReader/StreamTokenizer提高IO效率
- 避免频繁的系统调用，批量处理数据

### 2. 内存管理
- 通过复用数组避免重复分配内存
- 使用对象池技术减少GC压力

### 3. 边界处理
- 扩展数组边界避免特殊判断
- 添加空指针和越界检查

### 4. 异常处理
- 添加对空矩阵、越界查询的处理
- 提供友好的错误信息

### 5. 数据类型选择
- 使用long类型防止整数溢出
- 考虑浮点数精度问题

### 6. 性能优化
- 避免不必要的对象创建
- 使用局部变量减少内存访问
- 优化循环结构，减少分支预测失败

### 7. 可测试性
- 添加单元测试覆盖边界情况
- 提供性能测试用例
- 支持多种输入格式

### 8. 可维护性
- 代码模块化，职责单一
- 添加详细的注释和文档
- 使用有意义的变量名

## 算法复杂度对比

| 算法 | 时间复杂度 | 空间复杂度 | 适用场景 |
|------|------------|------------|----------|
| 二维前缀和 | 构建O(mn)，查询O(1) | O(mn) | 多次查询子矩阵和 |
| 二维差分 | 更新O(1)，还原O(mn) | O(mn) | 多次区间更新 |
| 暴力枚举 | O(m²n²) | O(1) | 小规模数据 |
| 动态规划 | O(mn) | O(mn) | 最大子矩阵等问题 |
| 扫描线 | O(n log n) | O(n) | 矩形重叠问题 |

## 面试考点总结

### 基础概念
1. 二维前缀和的构建原理和查询方法
2. 二维差分数组的更新原理和还原方法
3. 容斥原理在二维数组中的应用

### 算法优化
1. 如何将O(n⁴)暴力解法优化到O(n²)
2. 空间复杂度的优化策略
3. 边界情况的处理方法

### 实际应用
1. 图像处理中的区域统计
2. 游戏开发中的碰撞检测
3. 地理信息系统中的区域查询

### 代码实现
1. 二维数组的遍历技巧
2. 坐标系的转换和处理
3. 异常输入的处理方法

## 学习建议

1. **先掌握一维前缀和和差分**，再扩展到二维
2. **理解容斥原理**，这是二维算法的核心
3. **多做练习题**，从简单到复杂逐步提升
4. **注意边界处理**，这是容易出错的地方
5. **学习优化技巧**，提高代码效率

通过系统学习二维前缀和与差分数组算法，可以解决很多实际的二维区间操作问题，为后续学习更复杂的算法打下坚实基础。
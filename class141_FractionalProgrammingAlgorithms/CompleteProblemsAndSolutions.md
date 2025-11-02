# 01分数规划完整题目与解答

## 目录

1. [基础01分数规划](#基础01分数规划)
   - [Dropping Tests](#dropping-tests)
   - [Codeforces 489E Hiking](#codeforces-489e-hiking)
   - [洛谷 P1642 规划](#洛谷-p1642-规划)

2. [最优比率生成树](#最优比率生成树)
   - [Desert King](#desert-king)

3. [最优比率环](#最优比率环)
   - [Sightseeing Cows](#sightseeing-cows)
   - [洛谷 P3199 最小圈](#洛谷-p3199-最小圈)
   - [洛谷 P1768 天路](#洛谷-p1768-天路)

4. [背包问题+01分数规划](#背包问题01分数规划)
   - [Talent Show](#talent-show)

5. [树形背包+01分数规划](#树形背包01分数规划)
   - [Best Team](#best-team)

6. [网络流+01分数规划](#网络流01分数规划)
   - [新生舞会](#新生舞会)
   - [方伯伯运椰子](#方伯伯运椰子)

7. [最小密度路径](#最小密度路径)
   - [最小密度路径](#最小密度路径-1)

8. [Dinkelbach算法](#dinkelbach算法)
   - [Dinkelbach算法示例](#dinkelbach算法示例)

9. [最大密度子图](#最大密度子图)
   - [Hard Life](#hard-life)

## 基础01分数规划

### Dropping Tests

#### 题目描述
给定n个数据，每个数据有(a, b)两个值，都为整数，并且都是非负的。请舍弃掉k个数据，希望让剩下数据做到，所有a的和 / 所有b的和，这个比值尽量大。

#### 解题思路
使用二分法求解01分数规划问题。对于给定的比率值L，判断是否存在一种选择方案使得：
```
(sum(a_i * x_i)) / (sum(b_i * x_i)) > L
```
等价于：
```
sum((a_i - L * b_i) * x_i) > 0
```
我们通过二分L的值，使用贪心策略判断是否可行。

#### 代码实现

**Java版本：**
```java
// 详见 Code01_DroppingTests.java
```

**C++版本：**
```cpp
// 详见 Code01_DroppingTests.cpp
```

**Python版本：**
```python
# 详见 Code01_DroppingTests.py
```

### Codeforces 489E Hiking

#### 题目描述
给定一些点，每个点有位置和价值，选择一些点使得价值和与距离和的比值最大。

#### 解题思路
这是基础01分数规划的变体，需要结合动态规划来解决。

### 洛谷 P1642 规划

#### 题目描述
给定一棵树，每个节点有产值和污染值，需要拆除一些节点使得剩余节点的产值和与污染值和的比值最大。

#### 解题思路
基础01分数规划问题，使用树形结构进行贪心选择。

## 最优比率生成树

### Desert King

#### 题目描述
有n个村庄，每个村庄由(x, y, z)表示，其中(x,y)是二维地图中的位置，z是海拔高度。任意两个村庄之间的距离是二维地图中的欧式距离，修路花费是海拔差值的绝对值。要求将所有村庄连通，使得总花费/总距离的比值最小。

#### 解题思路
使用二分法求解01分数规划问题，结合Prim算法求最小生成树进行可行性判断。对于给定的比率值L，判断是否存在生成树使得：
```
(sum(cost_e)) / (sum(dist_e)) < L
```
等价于：
```
sum((cost_e - L * dist_e)) < 0
```

#### 代码实现

**Java版本：**
```java
// 详见 Code03_DesertKing.java
```

**C++版本：**
```cpp
// 详见 Code03_DesertKing.cpp
```

**Python版本：**
```python
# 详见 Code03_DesertKing.py
```

## 最优比率环

### Sightseeing Cows

#### 题目描述
给定一个有向图，每个点有一个点权value[i]，每条边有一个边权weight[i]。找到一个环，使得环上点权和除以边权和最大。

#### 解题思路
使用01分数规划 + DFS判负环的方法。对于给定的比率值L，将每条边的权值更新为(weight_e - L)，然后判断图中是否存在正环。

#### 代码实现

**Java版本：**
```java
// 详见 Code06_SightseeingCows.java
```

**C++版本：**
```cpp
// 详见 Code06_SightseeingCows.cpp
```

**Python版本：**
```python
# 详见 Code06_SightseeingCows.py
```

### 洛谷 P3199 最小圈

#### 题目描述
给定一个有向带权图，求所有环的平均值中最小的平均值。环的平均值定义为：环中边的权值和 / 环中边的数量。

#### 解题思路
这是标准的最优比率环问题，使用01分数规划 + 二分查找 + DFS判负环解决。

#### 代码实现

**Java版本：**
```java
// 详见 Code04_MinimumAverageCircle.java
```

**C++版本：**
```cpp
// 详见 Code04_MinimumAverageCircle.cpp
```

**Python版本：**
```python
# 详见 Code04_MinimumAverageCircle.py
```

### 洛谷 P1768 天路

#### 题目描述
给定一个有向图，每条边有两个权值，求最小密度环。

#### 解题思路
最优比率环问题的变种，解法类似。

## 背包问题+01分数规划

### Talent Show

#### 题目描述
有n头奶牛，每头奶牛有重量和才艺两个属性值。要求选若干头牛，使得总重量不少于w，并且选出的牛的才艺的和与重量的和的比值尽量大。返回该比值乘以1000的整数结果，小数部分舍弃。

#### 解题思路
使用二分法求解01分数规划问题，结合01背包动态规划进行可行性判断。对于给定的比率值L，判断是否存在选择方案使得：
```
(sum(talent_i * x_i)) / (sum(weight_i * x_i)) > L
```
等价于：
```
sum((talent_i - L * weight_i) * x_i) > 0
```

#### 代码实现

**Java版本：**
```java
// 详见 Code02_TalentShow.java
```

**C++版本：**
```cpp
// 详见 Code02_TalentShow.cpp
```

**Python版本：**
```python
# 详见 Code02_TalentShow.py
```

## 树形背包+01分数规划

### Best Team

#### 题目描述
给定一棵树，节点编号0~n，0号节点是整棵树的头。编号1~n的节点，每个节点都有招募花费和战斗值，0号节点这两个值都是0。当招募某个节点时，必须招募该节点及其所有祖先节点。除了0号节点之外，一共可以招募k个人，希望让战斗值之和/招募花费之和的比值尽量大。

#### 解题思路
使用01分数规划 + 树形背包 + DFN序优化。对于给定的比率值L，判断是否存在招募方案使得：
```
(sum(strength_i)) / (sum(cost_i)) > L
```
等价于：
```
sum((strength_i - L * cost_i)) > 0
```

#### 代码实现

**Java版本：**
```java
// 详见 Code05_BestTeam.java
```

**C++版本：**
```cpp
// 详见 Code05_BestTeam.cpp
```

**Python版本：**
```python
# 详见 Code05_BestTeam.py
```

## 网络流+01分数规划

### 新生舞会

#### 题目描述
给定一个二分图，每条边有两个权值，要求选择一些边使得比值最大。

#### 解题思路
使用01分数规划 + 网络流/费用流解决。

### 方伯伯运椰子

#### 题目描述
网络流相关问题，结合01分数规划求解。

#### 解题思路
网络流+01分数规划的典型应用。

## 最小密度路径

### 最小密度路径

#### 题目描述
给定一个有向图，每条边有两个权值a[i]和b[i]。定义路径密度为路径上所有a[i]的和除以所有b[i]的和。求所有简单路径中密度最小的值。

#### 解题思路
使用Floyd变形 + 01分数规划解决。

#### 代码实现

**Java版本：**
```java
// 详见 Code07_MinimumDensityPath.java
```

**C++版本：**
```cpp
// 详见 Code07_MinimumDensityPath.cpp
```

**Python版本：**
```python
# 详见 Code07_MinimumDensityPath.py
```

## Dinkelbach算法

### Dinkelbach算法示例

#### 题目描述
使用Dinkelbach算法解决01分数规划问题。

#### 解题思路
Dinkelbach算法通过迭代方式逼近最优解，通常比二分法更快收敛。

#### 代码实现

**Java版本：**
```java
// 详见 Code08_DinkelbachExample.java
```

**C++版本：**
```cpp
// 详见 Code08_DinkelbachExample.cpp
```

**Python版本：**
```python
# 详见 Code08_DinkelbachExample.py
```

## 最大密度子图

### Hard Life

#### 题目描述
给定一个无向图，找到一个子图使得其密度最大。密度定义为子图中边数除以点数。

#### 解题思路
使用01分数规划 + 网络流最小割解决。

#### 代码实现

**Java版本：**
```java
// 详见 Code09_MaximumDensitySubgraph.java
```

**C++版本：**
```cpp
// 详见 Code09_MaximumDensitySubgraph.cpp
```

**Python版本：**
```python
# 详见 Code09_MaximumDensitySubgraph.py
```

## 总结

01分数规划作为一种重要的优化技术，在算法竞赛和实际应用中都有广泛的应用。通过本文的详细分析和实现，我们可以看到：

1. **算法多样性**：01分数规划有多种解法，包括二分法和Dinkelbach算法，各有优缺点
2. **题型丰富性**：从基础的01分数规划到结合图论、动态规划、网络流等的复合问题
3. **工程化重要性**：在实际应用中，需要考虑精度控制、异常处理、性能优化等多个方面
4. **跨语言实现**：不同编程语言有不同的特性和优势，需要根据具体场景选择

掌握01分数规划不仅需要理解其数学原理，更需要在实践中不断积累经验，提高解决实际问题的能力。
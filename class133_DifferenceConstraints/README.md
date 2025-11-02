# 差分约束系统 (Difference Constraints System)

## 1. 概述

差分约束系统是一种特殊的n元一次不等式组，它包含n个变量x₁, x₂, ..., xₙ以及m个约束条件，每个约束条件都是由两个变量做差构成的，形如：

```
xᵢ - xⱼ ≤ cₖ  (其中1 ≤ i, j ≤ n, i ≠ j, 1 ≤ k ≤ m，cₖ为常数)
```

我们要解决的问题是：求一组解x₁ = a₁, x₂ = a₂, ..., xₙ = aₙ，使得所有的约束条件得到满足，否则判断出无解。

## 2. 核心思想

差分约束系统可以转化为图论问题来解决：

1. 将每个变量xᵢ看作图中的一个节点
2. 对于每个约束条件xᵢ - xⱼ ≤ cₖ，从节点j向节点i连一条权值为cₖ的有向边
3. 添加一个超级源点0，向所有节点连权值为0的边，确保图的连通性
4. 通过求最短路径来得到解

这是因为差分约束xᵢ - xⱼ ≤ cₖ可以变形为xᵢ ≤ xⱼ + cₖ，这与最短路径中的三角不等式dist[v] ≤ dist[u] + w(u,v)非常相似。

## 3. 解的存在性

- 如果图中存在负环，则差分约束系统无解
- 否则，从超级源点到各点的最短距离就是一组可行解

## 4. 解的特性

如果{x₁, x₂, ..., xₙ}是一组解，那么{x₁+d, x₂+d, ..., xₙ+d}也是一组解，因为做差后常数d会被消掉。

## 5. 代码实现

### 5.1 基础差分约束 (Code01_DifferenceConstraints1.java 和 Code01_DifferenceConstraints2.java)

这两个文件展示了差分约束系统的两种实现方式：

1. **形式1 (Code01_DifferenceConstraints1.java)**：
   - 使用最短路径算法
   - 约束条件xᵢ - xⱼ ≤ cₖ转化为从节点j到节点i的权值为cₖ的边
   - dist数组初始化为Integer.MAX_VALUE

2. **形式2 (Code01_DifferenceConstraints2.java)**：
   - 使用最长路径算法
   - 约束条件xᵢ - xⱼ ≤ cₖ转化为从节点i到节点j的权值为-cₖ的边
   - dist数组初始化为Integer.MIN_VALUE

### 5.2 小K的农场 (Code02_KsFarm.java)

将自然语言描述的约束条件转化为差分约束系统：

- 关系1 a b c：农场a比农场b至少多种植了c个作物 => b - a ≤ -c
- 关系2 a b c：农场a比农场b至多多种植了c个作物 => a - b ≤ c
- 关系3 a b：农场a和农场b种植了一样多的作物 => a - b ≤ 0 且 b - a ≤ 0

### 5.3 布局奶牛 (Code03_LayoutCow.java)

奶牛排队问题，涉及好友关系和情敌关系：

- 好友关系 u v w：希望u和v之间的距离 ≤ w => dist[v] - dist[u] ≤ w
- 情敌关系 u v w：希望u和v之间的距离 ≥ w => dist[v] - dist[u] ≥ w => dist[u] - dist[v] ≤ -w

### 5.4 倍杀测量者 (Code04_Measurer1.java 和 Code04_Measurer2.java)

结合二分答案和差分约束系统的复杂问题：

- 通过二分法找到最大的ans值，使得在调整后的约束条件下仍存在矛盾
- 使用对数变换处理乘除法约束

### 5.5 天平 (Code05_Balance.java)

基于Floyd算法的差分约束问题：

- 根据已知的砝码关系推断所有砝码间的重量关系
- 使用Floyd算法计算所有点对间的最值

## 6. 相关题目

### 6.1 洛谷题目

1. **P5960 【模板】差分约束算法**
   - 链接：https://www.luogu.com.cn/problem/P5960
   - 题意：标准的差分约束模板题

2. **P1993 小K的农场**
   - 链接：https://www.luogu.com.cn/problem/P1993
   - 题意：农场作物数量约束问题

3. **P4878 [USACO05DEC] Layout G**
   - 链接：https://www.luogu.com.cn/problem/P4878
   - 题意：奶牛布局问题

4. **P4926 [1007]倍杀测量者**
   - 链接：https://www.luogu.com.cn/problem/P4926
   - 题意：倍杀测量问题，需要使用对数变换

5. **P2474 [SCOI2008]天平**
   - 链接：https://www.luogu.com.cn/problem/P2474
   - 题意：天平砝码问题，使用Floyd算法

6. **P1250 种树**
   - 链接：https://www.luogu.com.cn/problem/P1250
   - 题意：区间种树问题

7. **P2294 [HNOI2005]狡猾的商人**
   - 链接：https://www.luogu.com.cn/problem/P2294
   - 题意：判断商人的账本是否合理

8. **P3275 [SCOI2011]糖果**
   - 链接：https://www.luogu.com.cn/problem/P3275
   - 题意：分糖果问题

### 6.2 POJ题目

1. **POJ 1201 Intervals**
   - 链接：http://poj.org/problem?id=1201
   - 题意：给定多个区间和每个区间内至少需要选择的整数个数，求满足条件的最少整数个数

2. **POJ 1716 Integer Intervals**
   - 链接：http://poj.org/problem?id=1716
   - 题意：POJ 1201的简化版本

3. **POJ 2983 Is the Information Reliable?**
   - 链接：http://poj.org/problem?id=2983
   - 题意：判断给定的信息是否一致

4. **POJ 3169 Layout**
   - 链接：http://poj.org/problem?id=3169
   - 题意：奶牛排队问题，求1号和n号奶牛的最大距离

5. **POJ 3159 Candies**
   - 链接：http://poj.org/problem?id=3159
   - 题意：分糖果问题

### 6.3 其他平台题目

1. **ZOJ 1508 Intervals**
   - 题意：与POJ 1201类似

2. **SPOJ INVCNT**
   - 题意：逆序对计数相关问题

3. **USACO题目**
   - 多个关于布局和约束的问题

### 6.4 新增练习题目

1. **POJ 1201 Intervals**
   - 链接：http://poj.org/problem?id=1201
   - 题意：给定多个区间和每个区间内至少需要选择的整数个数，求满足条件的最少整数个数
   - 代码实现：POJ1201_Intervals.java, POJ1201_Intervals.py

2. **POJ 2983 Is the Information Reliable?**
   - 链接：http://poj.org/problem?id=2983
   - 题意：判断给定的信息是否一致
   - 代码实现：POJ2983_IsTheInformationReliable.java, POJ2983_IsTheInformationReliable.py

3. **USACO 2005 December Gold Layout**
   - 链接：需要查找具体链接
   - 题意：奶牛排队问题，涉及好友关系和情敌关系的距离约束
   - 代码实现：USACO_Layout.java, USACO_Layout.py

4. **LibreOJ #10087 「一本通3.4 例1」Intervals**
   - 链接：https://loj.ac/p/10087
   - 题意：区间选点问题
   - 代码实现：可参考POJ 1201的实现

5. **LibreOJ #10088 「一本通3.4 例2」出纳员问题**
   - 链接：https://loj.ac/p/10088
   - 题意：出纳员工作时间安排问题
   - 代码实现：可参考差分约束系统模板

6. **AtCoder ABC216G 01Sequence**
   - 链接：https://atcoder.jp/contests/abc216/tasks/abc216_g
   - 题意：01序列问题，涉及差分约束
   - 代码实现：可参考差分约束系统模板

### 6.5 新增经典题目

7. **POJ 1364 King**
   - 链接：http://poj.org/problem?id=1364
   - 题意：国王序列约束问题，判断是否存在满足约束条件的序列
   - 代码实现：POJ1364_King.java, POJ1364_King.cpp, POJ1364_King.py

8. **洛谷 P5960 【模板】差分约束算法**
   - 链接：https://www.luogu.com.cn/problem/P5960
   - 题意：标准的差分约束模板题
   - 代码实现：LuoguP5960_DifferenceConstraints.java, LuoguP5960_DifferenceConstraints.cpp, LuoguP5960_DifferenceConstraints.py

9. **Codeforces 1473E - Minimum Path**
   - 链接：https://codeforces.com/contest/1473/problem/E
   - 题意：复杂图论问题，通过状态扩展和差分约束思想解决
   - 代码实现：Codeforces1473E_MinimumPath.java, Codeforces1473E_MinimumPath.cpp, Codeforces1473E_MinimumPath.py

10. **LibreOJ #10087 「一本通3.4 例1」Intervals**
    - 链接：https://loj.ac/p/10087
    - 题意：区间选点问题，与POJ 1201类似
    - 代码实现：可参考POJ1201_Intervals的实现

11. **LibreOJ #10088 「一本通3.4 例2」出纳员问题**
    - 链接：https://loj.ac/p/10088
    - 题意：出纳员工作时间安排问题
    - 代码实现：可参考差分约束系统模板

12. **AtCoder ABC137 E - Coins Respawn**
    - 链接：https://atcoder.jp/contests/abc137/tasks/abc137_e
    - 题意：在有向图中寻找从起点到终点的最大收益路径，可转化为差分约束问题
    - 代码实现：可参考Codeforces 1473E的实现思路

13. **HDU 3592 World Exhibition**
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=3592
    - 题意：世界展览会排队问题，涉及距离约束
    - 代码实现：可参考USACO Layout的实现

14. **ZOJ 1508 Intervals**
    - 链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1508
    - 题意：区间选点问题，与POJ 1201类似
    - 代码实现：可参考POJ1201_Intervals的实现

15. **SPOJ INVCNT**
    - 链接：https://www.spoj.com/problems/INVCNT/
    - 题意：逆序对计数相关问题，可结合差分约束思想
    - 代码实现：可参考相关计数算法

## 7. 时间与空间复杂度

### 7.1 建图阶段
- 时间复杂度：O(m)，其中m是约束条件数量
- 空间复杂度：O(n + m)，使用链式前向星存储图

### 7.2 SPFA算法
- 时间复杂度：平均O(k*m)，最坏O(n*m)，其中k是常数，n是变量数量
- 空间复杂度：O(n)，用于dist、update、enter数组和队列

### 7.3 总体复杂度
- 时间复杂度：O(n + m)
- 空间复杂度：O(n + m)

## 8. 工程化考虑

### 8.1 异常处理
- 输入校验：检查n、m范围，常数范围
- 图构建：检查边数是否超过限制
- 算法执行：检测负环/正环

### 8.2 性能优化
- 使用链式前向星存储图，节省空间
- 使用静态数组而非动态数组，提高访问速度
- 队列大小预分配，避免动态扩容

### 8.3 可维护性
- 函数职责单一，prepare()初始化，addEdge()加边，spfa()求解
- 变量命名清晰，head、next、to、weight等表示图结构
- 详细注释说明算法原理和关键步骤

### 8.4 可扩展性
- 可以轻松修改为求最长路径（处理≥约束）
- 可以扩展支持更多类型的约束条件
- 可以添加更多输出信息，如具体哪个约束导致无解

## 9. 应用场景

差分约束系统在以下场景中有广泛应用：

1. **布局问题**：如奶牛排队、农场作物安排等
2. **调度问题**：任务调度、时间安排等
3. **资源分配**：资源约束下的分配问题
4. **游戏设计**：游戏中角色属性约束等
5. **经济模型**：价格、成本等经济变量的约束关系

## 10. 总结

差分约束系统是图论中一个非常重要的应用，它将不等式组求解问题转化为图论中的最短路径问题。通过合理建图和使用SPFA等算法，可以高效地解决这类问题。

在实际应用中，关键在于：
1. 正确地将问题约束转化为差分约束形式
2. 合理地构建图模型
3. 正确处理边界条件和特殊情况
4. 选择合适的算法（最短路或最长路）
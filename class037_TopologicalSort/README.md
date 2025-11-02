# 拓扑排序算法详解与相关题目

## 1. 概述

拓扑排序是一种对有向无环图（DAG）的顶点进行线性排序的算法，使得对于任何一条有向边(u, v)，u在线性序列中都出现在v之前。拓扑排序常用于解决任务调度、依赖关系处理等问题。

### 1.1 基本概念

- **有向无环图（DAG）**：没有有向环的有向图
- **入度**：指向某个节点的边的数量
- **出度**：从某个节点出发的边的数量
- **拓扑序**：满足拓扑排序条件的节点线性序列

### 1.2 算法原理

拓扑排序主要有两种实现方式：

1. **Kahn算法（BFS）**：
   - 计算所有节点的入度
   - 将入度为0的节点加入队列
   - 重复以下步骤直到队列为空：
     - 从队列中取出一个节点
     - 将该节点加入结果序列
     - 将该节点的所有邻居节点入度减1
     - 如果邻居节点入度变为0，则加入队列

2. **DFS算法**：
   - 对每个未访问的节点进行深度优先搜索
   - 在DFS回溯时将节点加入结果序列的前端

### 1.3 时间复杂度

- **时间复杂度**：O(V + E)，其中V是节点数，E是边数
- **空间复杂度**：O(V)

## 2. 本目录题目详解

### 2.1 最大食物链计数（Code01_FoodLines.java）

**题目来源**：洛谷 P4017 https://www.luogu.com.cn/problem/P4017

**题目大意**：给定一个食物链有向图，统计从入度为0的节点到出度为0的节点的路径总数。

**解法**：拓扑排序 + 动态规划
- 使用链式前向星建图
- 通过拓扑排序保证处理顺序
- 使用DP统计路径数量

### 2.2 喧闹和富有（Code02_LoudAndRich.java）

**题目来源**：LeetCode 851 https://leetcode.cn/problems/loud-and-rich/

**题目大意**：给定 richer 关系和每个人的安静值，对每个人找出所有不少于他富有的人中最安静的人。

**解法**：拓扑排序
- 将 richer 关系构建成有向图
- 通过拓扑排序传播信息
- 更新每个人的答案

### 2.3 并行课程 III（Code03_ParallelCoursesIII.java）

**题目来源**：LeetCode 2050 https://leetcode.cn/problems/parallel-courses-iii/

**题目大意**：给定课程先修关系和每门课程的学习时间，求完成所有课程的最少时间。

**解法**：拓扑排序 + 动态规划
- 构建课程依赖图
- 通过拓扑排序计算每门课程的最早完成时间
- 记录最大完成时间

### 2.4 参加会议的最多员工数（Code04_MaximumEmployeesToBeInvitedToAMeeting.java）

**题目来源**：LeetCode 2127 https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/

**题目大意**：给定每个员工喜欢的员工，求能参加会议的最大员工数。

**解法**：基环树 + 拓扑排序
- 通过拓扑排序找出所有环
- 分类讨论大环和小环的情况
- 计算链的深度

### 2.5 课程表（Code05_CourseSchedule.java）

**题目来源**：LeetCode 207 https://leetcode.cn/problems/course-schedule/

**题目大意**：判断是否可能完成所有课程的学习。

**解法**：拓扑排序判环
- 使用Kahn算法或DFS+三色标记法判断图中是否有环
- 如果无环则可以完成所有课程

### 2.6 课程表 II（Code06_CourseScheduleII.java）

**题目来源**：LeetCode 210 https://leetcode.cn/problems/course-schedule-ii/

**题目大意**：返回你为了学完所有课程所安排的学习顺序。

**解法**：拓扑排序
- 使用Kahn算法进行拓扑排序
- 返回满足依赖关系的学习顺序

### 2.7 确定比赛名次（Code07_DetermineRanking.java）

**题目来源**：HDU 1285 http://acm.hdu.edu.cn/showproblem.php?pid=1285

**题目大意**：确定所有队伍的名次，要求名次符合给定关系且字典序最小。

**解法**：字典序最小拓扑排序
- 使用优先队列优化的Kahn算法
- 每次选择入度为0且编号最小的节点

### 2.8 矩阵中的最长递增路径（Code08_LongestIncreasingPathInAMatrix.java）

**题目来源**：LeetCode 329 https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/

**题目大意**：给定一个整数矩阵，找出其中最长递增路径的长度。路径可以沿着上、下、左、右四个方向移动。

**解法**：
1. **记忆化搜索**：对每个单元格进行深度优先搜索，缓存中间结果
2. **拓扑排序**：将矩阵建模为DAG，使用拓扑排序找出最长路径

### 2.9 并行课程 II（Code09_ParallelCoursesII.java）

**题目来源**：LeetCode 1494 https://leetcode.cn/problems/parallel-courses-ii/

**题目大意**：给定课程依赖关系和每学期最多可选课程数k，求上完所有课最少需要多少个学期。

**解法**：状态压缩动态规划
- 使用二进制表示已修课程状态
- 预处理每门课程的前置依赖
- 枚举所有可能的课程组合，选择最优解

### 2.10 课程表 III（Code10_CourseScheduleIII.java）

**题目来源**：LeetCode 630 https://leetcode.cn/problems/course-schedule-iii/

**题目大意**：在截止时间前尽可能多地完成课程，考虑课程持续时间和截止时间约束。

**解法**：贪心 + 优先队列
- 按照截止时间排序课程
- 使用最大堆记录已选课程的持续时间
- 根据时间约束进行选择或替换

### 2.11 拓扑排序模板（Code11_TopologicalSortTemplate.java）

**题目来源**：ACWing 848 https://www.acwing.com/problem/content/850/

**题目大意**：对有向无环图进行拓扑排序，检测环的存在。

**解法**：Kahn算法（BFS）
- 计算节点入度，将入度为0的节点加入队列
- BFS处理队列，更新邻居节点入度
- 检查结果序列长度判断是否有环

### 2.12 字典序最小拓扑排序（Code12_LexicographicalTopologicalSort.java）

**题目来源**：牛客网 15184 https://ac.nowcoder.com/acm/problem/15184

**题目大意**：输出字典序最小的拓扑排序序列。

**解法**：优先队列优化的Kahn算法
- 使用最小堆替代普通队列
- 每次选择编号最小的入度为0节点
- 保证输出序列的字典序最小

### 2.13 课程表判环（Code13_CourseScheduleCheckCycle.java）

**题目来源**：LeetCode 207 https://leetcode.cn/problems/course-schedule/

**题目大意**：判断课程安排图中是否存在环。

**解法**：拓扑排序判环
- 构建课程依赖图
- 使用拓扑排序检测环的存在
- 如果结果序列长度等于课程数，说明无环

### 2.14 拓扑排序状态判断（Code14_SortingItAllOut.java）

**题目来源**：POJ 1094 http://poj.org/problem?id=1094

**题目大意**：逐步添加关系并判断拓扑排序状态（唯一确定/存在矛盾/无法确定）。

**解法**：增量式拓扑排序
- 逐步添加边关系
- 每次添加后检查拓扑排序状态
- 精确判断三种可能状态

### 2.15 有向无环图最长路径（Code15_LongestPathInDAG.java）

**题目来源**：POJ 3249 http://poj.org/problem?id=3249

**题目大意**：计算DAG中的最长路径长度。

**解法**：拓扑排序 + 动态规划
- 使用拓扑排序确定节点处理顺序
- dp[i]表示以节点i为终点的最长路径长度
- 在拓扑排序过程中进行状态转移

### 2.16 基环树问题（Code16_MaximumEmployeesToMeeting.java）

**题目来源**：LeetCode 2127 https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/

**题目大意**：处理基环树，计算最大参会人数。

**解法**：拓扑排序 + 分类讨论
- 使用拓扑排序找出环
- 分类处理不同大小的环
- 计算链的深度并组合结果

### 2.17 字典序建图与拓扑排序（Code17_FoxAndNames.java）

**题目来源**：Codeforces 510C https://codeforces.com/problemset/problem/510/C

**题目大意**：通过字符串比较构建字母顺序图，判断是否存在合法的字母顺序。

**解法**：拓扑排序应用
- 比较相邻字符串建立字符顺序关系
- 构建有向图并进行拓扑排序
- 检测环的存在判断合法性

### 2.18 Project Euler密码推导（Code18_PasscodeDerivation.java）

**题目来源**：Project Euler Problem 79 https://projecteuler.net/problem=79

**题目大意**：通过密码片段推断最短可能密码。

**解法**：拓扑排序推断数字顺序
- 从密码片段中提取数字间的顺序关系
- 构建有向图并进行拓扑排序
- 输出字典序最小的拓扑序列

### 2.19 任务调度器（Code19_TaskScheduler.java）

**题目来源**：LeetCode 621 https://leetcode.cn/problems/task-scheduler/

**题目大意**：安排任务执行顺序，满足冷却时间约束。

**解法**：贪心 + 优先队列
- 统计任务频率并使用最大堆
- 每次选择频率最高的任务执行
- 处理冷却时间约束

## 3. 相关题目扩展

### 3.1 经典拓扑排序题目

1. **课程表系列**
   - LeetCode 207. 课程表 - https://leetcode.cn/problems/course-schedule/
   - LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
   - LeetCode 1494. 并行课程 II - https://leetcode.cn/problems/parallel-courses-ii/
   - LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/

2. **任务调度**
   - 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
   - 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983

3. **排序判定**
   - POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
   - 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347

4. **字典序最小**
   - SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
   - HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
   - 牛客网 字典序最小的拓扑序列 - https://ac.nowcoder.com/acm/problem/15184

### 3.2 拓扑排序DP题目

1. **路径计数**
   - 洛谷 P4017 最大食物链计数 - https://www.luogu.com.cn/problem/P4017

2. **最长路径**
   - POJ 3249 Test for Job - http://poj.org/problem?id=3249
   - HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109

3. **信息传播**
   - LeetCode 851. 喧闹和富有 - https://leetcode.cn/problems/loud-and-rich/

4. **矩阵路径**
   - LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/

### 3.3 基环树题目

1. **环的识别**
   - LeetCode 2127. 参加会议的最多员工数 - https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
   - 洛谷 P1453 城市环路 - https://www.luogu.com.cn/problem/P1453

2. **环上DP**
   - 洛谷 P2607 [ZJOI2008]骑士 - https://www.luogu.com.cn/problem/P2607

## 4. 算法技巧总结

### 4.1 建图技巧

1. **邻接表**：适用于稀疏图
2. **邻接矩阵**：适用于稠密图
3. **链式前向星**：节省空间的邻接表实现

### 4.2 拓扑排序技巧

1. **优先队列**：实现字典序最小的拓扑排序
2. **分层处理**：处理需要按层处理的问题
3. **反向图**：某些情况下构建反向图更方便

### 4.3 DP状态设计

1. **路径计数**：dp[i] 表示到达节点 i 的路径数
2. **最短/最长路径**：dp[i] 表示到达节点 i 的最短/最长距离
3. **最优值传播**：dp[i] 表示节点 i 的某种最优属性

## 5. 工程化考虑

### 5.1 性能优化

1. **输入输出优化**：使用 StreamTokenizer 等高效IO
2. **内存优化**：合理选择数据结构
3. **算法优化**：避免重复计算

### 5.2 边界处理

1. **空图处理**：处理没有节点或边的情况
2. **单节点图**：处理只有一个节点的情况
3. **环检测**：检测图中是否有环

### 5.3 异常处理

1. **输入验证**：验证输入数据的有效性
2. **状态检查**：检查算法执行过程中的状态
3. **错误恢复**：在出错时提供有意义的错误信息

## 6. 跨语言实现要点

### 6.1 Java实现要点

1. **集合类使用**：ArrayList、LinkedList等
2. **数组操作**：Arrays工具类的使用
3. **IO优化**：BufferedReader、StreamTokenizer等
4. **优先队列**：PriorityQueue实现字典序最小拓扑排序

### 6.2 C++实现要点

1. **STL使用**：vector、queue、priority_queue等容器
2. **内存管理**：手动管理动态内存
3. **IO优化**：scanf/printf比cin/cout更快
4. **优先队列**：priority_queue实现字典序最小拓扑排序

### 6.3 Python实现要点

1. **列表操作**：列表推导式、切片等
2. **collections模块**：deque、defaultdict等
3. **heapq模块**：优先队列实现
4. **优先队列**：heapq实现字典序最小拓扑排序

## 7. 常见问题与解决方案

### 7.1 如何判断图中是否有环？

**解决方案**：
1. 拓扑排序：如果最终结果序列长度小于节点数，说明有环
2. DFS：使用三色标记法检测环

### 7.2 如何实现字典序最小的拓扑排序？

**解决方案**：
使用优先队列（最小堆）替代普通队列

### 7.3 如何处理动态添加边的情况？

**解决方案**：
增量式拓扑排序，维护入度和队列状态

### 7.4 如何计算所有拓扑排序的数量？

**解决方案**：
使用DFS枚举所有可能的排序

## 8. 学习建议

1. **掌握基础**：熟练掌握Kahn算法和DFS算法
2. **练习经典**：完成课程表系列等经典题目
3. **理解变种**：学习拓扑排序的各种变种应用
4. **工程实践**：关注实际应用中的性能和边界处理

## 9. 算法深度解析与工程化实践

### 9.1 拓扑排序核心思想深度剖析

**本质认知**：拓扑排序的核心在于处理有向无环图中的依赖关系，通过线性排序保证所有依赖关系得到满足。

**关键洞察**：
1. **依赖传播机制**：拓扑排序通过入度机制实现依赖的逐层传播
2. **环检测原理**：结果序列长度小于节点数 ⇔ 图中存在环
3. **多解性分析**：当存在多个入度为0的节点时，拓扑排序结果不唯一

### 9.2 时间复杂度与空间复杂度精确分析

**基础拓扑排序**：
- 时间复杂度：O(V + E) - 每个节点和边只被处理一次
- 空间复杂度：O(V + E) - 存储图结构和辅助数组

**字典序最小拓扑排序**：
- 时间复杂度：O((V + E) log V) - 优先队列操作增加log因子
- 空间复杂度：O(V + E) - 额外存储优先队列

**拓扑排序DP**：
- 时间复杂度：O(V + E) - 拓扑排序基础上增加DP状态转移
- 空间复杂度：O(V + E) - 存储图结构和DP数组

### 9.3 工程化考量深度解析

#### 9.3.1 异常处理与边界场景

**必须处理的边界情况**：
1. **空图处理**：节点数为0时的特殊处理
2. **单节点图**：自环和孤立节点的处理
3. **完全图**：极端稠密图的性能优化
4. **大规模数据**：内存优化和算法效率

**异常防御策略**：
```java
// 输入验证示例
if (n <= 0) return true;  // 空图
if (prerequisites == null || prerequisites.length == 0) return true;  // 无依赖
```

#### 9.3.2 性能优化实战技巧

**内存优化**：
1. **链式前向星**：节省空间的邻接表实现
2. **位运算压缩**：状态压缩DP减少内存占用
3. **对象池技术**：避免频繁对象创建

**计算优化**：
1. **增量式拓扑排序**：避免重复计算
2. **缓存友好设计**：提高缓存命中率
3. **并行化处理**：多线程加速大规模计算

#### 9.3.3 调试与问题定位

**笔试调试技巧**：
```java
// 关键变量打印调试
System.out.println("当前节点: " + u + ", 入度: " + indegree[u]);
System.out.println("邻居节点: " + Arrays.toString(graph[u]));
```

**面试问题定位**：
1. **环检测失败**：检查入度更新逻辑
2. **结果不正确**：验证DP状态转移方程
3. **性能问题**：分析时间复杂度和常数因子

### 9.4 跨语言实现关键差异

#### 9.4.1 Java实现核心要点

**集合类选择**：
- `ArrayList`：随机访问性能好
- `LinkedList`：插入删除性能好
- `PriorityQueue`：优先队列实现

**IO优化**：
```java
// 高效IO示例
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
StreamTokenizer in = new StreamTokenizer(br);
```

#### 9.4.2 C++实现核心要点

**STL容器选择**：
- `vector`：动态数组，内存连续
- `queue`：队列容器
- `priority_queue`：优先队列

**内存管理**：
```cpp
// 手动内存管理
vector<int> graph[MAXN];  // 栈上分配
int* dp = new int[n];     // 堆上分配
```

#### 9.4.3 Python实现核心要点

**数据结构选择**：
- `list`：动态数组
- `deque`：双端队列
- `heapq`：堆队列

**性能优化**：
```python
# 使用生成器表达式
result = ''.join(chr(ord('A') + i) for i in range(n))
```

### 9.5 算法应用场景扩展

#### 9.5.1 机器学习与深度学习

**应用场景**：
1. **神经网络层排序**：确保前向传播的正确顺序
2. **计算图优化**：优化计算图的执行顺序
3. **依赖关系分析**：分析特征间的依赖关系

**技术联系**：
- 与自动微分技术的结合
- 在计算图编译优化中的应用

#### 9.5.2 自然语言处理

**应用场景**：
1. **语法分析**：句子成分的依赖关系分析
2. **知识图谱**：实体关系的拓扑排序
3. **文本生成**：内容生成的顺序控制

### 9.6 面试深度问题准备

#### 9.6.1 算法原理深挖

**可能问题**：
1. 为什么拓扑排序只能用于有向无环图？
2. Kahn算法和DFS算法的时间复杂度相同吗？
3. 如何证明拓扑排序结果的正确性？

**标准回答**：
1. 有向环会导致依赖循环，无法确定线性顺序
2. 都是O(V+E)，但常数因子和实际性能有差异
3. 通过数学归纳法证明每个节点的前驱都已在序列中

#### 9.6.2 工程实践问题

**可能问题**：
1. 如何处理动态添加边的情况？
2. 如何优化大规模图的拓扑排序？
3. 在实际系统中如何检测和避免死锁？

**标准回答**：
1. 增量式拓扑排序，维护入度和队列状态
2. 分块处理、并行计算、内存优化
3. 使用拓扑排序检测资源依赖环

### 9.7 完整题目列表

详见 [EXTENDED_PROBLEMS.md](EXTENDED_PROBLEMS.md) 文件，包含来自LeetCode、洛谷、POJ、HDU、SPOJ、AtCoder、Codeforces、牛客网、USACO、ZOJ、Timus OJ、Aizu OJ、Project Euler、HackerEarth、计蒜客、各大高校OJ、ACWing等平台的拓扑排序相关题目。

### 9.8 学习路径建议

**初级阶段**：
1. 掌握基础拓扑排序算法
2. 完成课程表系列题目
3. 理解环检测原理

**中级阶段**：
1. 学习拓扑排序DP应用
2. 掌握字典序最小拓扑排序
3. 理解基环树问题

**高级阶段**：
1. 研究大规模图优化技术
2. 探索拓扑排序在系统设计中的应用
3. 掌握增量式拓扑排序算法

通过系统学习和实践，拓扑排序算法将成为解决复杂依赖关系问题的有力工具。
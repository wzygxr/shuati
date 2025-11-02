# 拓扑排序相关题目汇总

## 1. 平台分类

### 1.1 LeetCode

1. **207. 课程表**
   - 链接：https://leetcode.cn/problems/course-schedule/
   - 难度：中等
   - 类型：拓扑排序判环
   - 解法：Kahn算法或DFS

2. **210. 课程表 II**
   - 链接：https://leetcode.cn/problems/course-schedule-ii/
   - 难度：中等
   - 类型：拓扑排序构造序列
   - 解法：Kahn算法

3. **329. 矩阵中的最长递增路径**【已实现】
   - 链接：https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
   - 难度：困难
   - 类型：拓扑排序 + 记忆化搜索
   - 解法：构建DAG后拓扑排序

4. **851. 喧闹和富有**
   - 链接：https://leetcode.cn/problems/loud-and-rich/
   - 难度：中等
   - 类型：拓扑排序 + DP
   - 解法：建图后拓扑排序传播信息

5. **1494. 并行课程 II**【已实现】
   - 链接：https://leetcode.cn/problems/parallel-courses-ii/
   - 难度：困难
   - 类型：拓扑排序 + 状态压缩DP
   - 解法：拓扑排序 + 状态压缩

6. **1559. 二维网格图中探测环**
   - 链接：https://leetcode.cn/problems/detect-cycles-in-2d-grid/
   - 难度：中等
   - 类型：基环树
   - 解法：DFS或并查集

7. **2050. 并行课程 III**
   - 链接：https://leetcode.cn/problems/parallel-courses-iii/
   - 难度：困难
   - 类型：拓扑排序 + DP
   - 解法：拓扑排序计算最早完成时间

8. **2127. 参加会议的最多员工数**
   - 链接：https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
   - 难度：困难
   - 类型：基环树 + 拓扑排序
   - 解法：拓扑排序找环 + 分类讨论

9. **630. 课程表 III**
   - 链接：https://leetcode.cn/problems/course-schedule-iii/
   - 难度：困难
   - 类型：贪心 + 优先队列
   - 解法：按截止时间排序 + 贪心选择

10. **621. 任务调度器**
    - 链接：https://leetcode.cn/problems/task-scheduler/
    - 难度：中等
    - 类型：贪心 + 优先队列
    - 解法：频率统计 + 贪心调度

11. **269. 火星词典**
    - 链接：https://leetcode.cn/problems/alien-dictionary/
    - 难度：困难
    - 类型：拓扑排序 + 字符顺序
    - 解法：字符比较 + 拓扑排序

### 1.2 洛谷

1. **P4017 最大食物链计数**
   - 链接：https://www.luogu.com.cn/problem/P4017
   - 难度：普及+/提高
   - 类型：拓扑排序 + DP
   - 解法：拓扑排序过程中统计路径数

2. **P1113 杂务**
   - 链接：https://www.luogu.com.cn/problem/P1113
   - 难度：普及+/提高
   - 类型：拓扑排序 + DP
   - 解法：最长路径的拓扑排序

3. **P1983 车站分级**
   - 链接：https://www.luogu.com.cn/problem/P1983
   - 难度：提高+/省选-
   - 类型：拓扑排序 + 差分约束
   - 解法：建图后拓扑排序分层

4. **P1347 排序**
   - 链接：https://www.luogu.com.cn/problem/P1347
   - 难度：普及+/提高
   - 类型：拓扑排序状态判断
   - 解法：逐步添加关系并检查状态

5. **B3644 【模板】拓扑排序 / 家谱树**
   - 链接：https://www.luogu.com.cn/problem/B3644
   - 难度：普及-
   - 类型：拓扑排序模板
   - 解法：Kahn算法

6. **P1453 城市环路**
   - 链接：https://www.luogu.com.cn/problem/P1453
   - 难度：提高
   - 类型：基环树
   - 解法：基环树DP

7. **P2607 [ZJOI2008]骑士**
   - 链接：https://www.luogu.com.cn/problem/P2607
   - 难度：省选/NOI-
   - 类型：基环树 + 树形DP
   - 解法：基环树处理 + 树形DP

### 1.3 POJ

1. **1094 Sorting It All Out**
   - 链接：http://poj.org/problem?id=1094
   - 难度：中等
   - 类型：拓扑排序状态判断
   - 解法：逐步添加关系并判断状态

2. **3249 Test for Job**
   - 链接：http://poj.org/problem?id=3249
   - 难度：困难
   - 类型：拓扑排序 + DP
   - 解法：最长路径的拓扑DP

3. **2289 Jimmy's Jump**
   - 链接：http://poj.org/problem?id=2289
   - 难度：困难
   - 类型：拓扑排序 + DP
   - 解法：状态压缩 + 拓扑排序

### 1.4 HDU

1. **1285 确定比赛名次**
   - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=1285
   - 难度：中等
   - 类型：字典序最小拓扑排序
   - 解法：优先队列实现字典序最小

2. **4109 Activation**
   - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=4109
   - 难度：困难
   - 类型：拓扑排序 + DP
   - 解法：计算关键路径

3. **3523 Image copy detection**
   - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=3523
   - 难度：困难
   - 类型：拓扑排序 + 匹配
   - 解法：图匹配 + 拓扑排序

### 1.5 SPOJ

1. **TOPOSORT - Topological Sorting**
   - 链接：https://www.spoj.com/problems/TOPOSORT/
   - 难度：中等
   - 类型：字典序最小拓扑排序
   - 解法：优先队列实现字典序最小

2. **PFDEP - Project File Dependencies**
   - 链接：https://www.spoj.com/problems/PFDEP/
   - 难度：简单
   - 类型：拓扑排序模板
   - 解法：Kahn算法

### 1.6 AtCoder

1. **ABC139E League**
   - 链接：https://atcoder.jp/contests/abc139/tasks/abc139_e
   - 难度：中等
   - 类型：拓扑排序 + 贪心
   - 解法：建图后拓扑排序

2. **ABC141E Who Says a Pun?**
   - 链接：https://atcoder.jp/contests/abc141/tasks/abc141_e
   - 难度：困难
   - 类型：字符串 + 拓扑排序
   - 解法：字符串匹配 + 拓扑排序

### 1.7 Codeforces

1. **510C Fox And Names**
   - 链接：https://codeforces.com/problemset/problem/510/C
   - 难度：中等
   - 类型：拓扑排序
   - 解法：根据字典序建图后拓扑排序

2. **1109C Sasha and a Patient Friend**
   - 链接：https://codeforces.com/problemset/problem/1109/C
   - 难度：困难
   - 类型：拓扑排序 + 图论
   - 解法：拓扑排序变种

3. **1091D New Year and the Permutation Concatenation**
   - 链接：https://codeforces.com/problemset/problem/1091/D
   - 难度：困难
   - 类型：组合数学 + 拓扑排序
   - 解法：排列组合 + 拓扑排序思想

### 1.8 牛客网

1. **字典序最小的拓扑序列**
   - 链接：https://ac.nowcoder.com/acm/problem/15184
   - 难度：中等
   - 类型：字典序最小拓扑排序
   - 解法：优先队列实现字典序最小

2. **课程表**
   - 链接：https://ac.nowcoder.com/acm/problem/24725
   - 难度：中等
   - 类型：拓扑排序判环
   - 解法：Kahn算法判环

### 1.9 USACO

1. **Sorting a Three-Valued Sequence**
   - 链接：http://train.usaco.org/usacoprob2?a=BP9H7uJjd9r&S=sort3
   - 难度：普及
   - 类型：特殊排序
   - 解法：贪心交换

2. **The Clocks**
   - 链接：http://train.usaco.org/usacoprob2?a=BP9H7uJjd9r&S=clocks
   - 难度：普及+/提高
   - 类型：状态搜索 + 拓扑思想
   - 解法：BFS + 状态压缩

### 1.10 ZOJ

1. **ZOJ 1060 Sorting It All Out**
   - 链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
   - 难度：中等
   - 类型：拓扑排序状态判断
   - 解法：逐步添加关系并判断状态

### 1.11 Timus OJ

1. **1280. Topological Sorting**
   - 链接：https://acm.timus.ru/problem.aspx?space=1&num=1280
   - 难度：中等
   - 类型：拓扑排序模板
   - 解法：Kahn算法

### 1.12 Aizu OJ

1. **GRL_4_B. Topological Sort**
   - 链接：https://onlinejudge.u-aizu.ac.jp/problems/GRL_4_B
   - 难度：中等
   - 类型：拓扑排序模板
   - 解法：Kahn算法

### 1.13 Project Euler

1. **Problem 79: Passcode derivation**
   - 链接：https://projecteuler.net/problem=79
   - 难度：中等
   - 类型：拓扑排序
   - 解法：根据约束条件建图后拓扑排序

### 1.14 HackerEarth

1. **Topological Sort**
   - 链接：https://www.hackerearth.com/practice/algorithms/graphs/topological-sort/practice-problems/algorithm/topological-sorttutorial/
   - 难度：简单
   - 类型：拓扑排序模板
   - 解法：Kahn算法

2. **Oliver and the Game**
   - 链接：https://www.hackerearth.com/practice/algorithms/graphs/topological-sort/practice-problems/algorithm/oliver-and-the-game-3/
   - 难度：中等
   - 类型：拓扑排序 + DFS
   - 解法：建图后拓扑排序

### 1.15 计蒜客

1. **三值排序**
   - 链接：https://nanti.jisuanke.com/t/T1566
   - 难度：简单
   - 类型：特殊排序
   - 解法：贪心交换

### 1.16 高校OJ

1. **各大高校OJ中的拓扑排序题目**
   - 类型：各种拓扑排序变种
   - 解法：根据具体题目要求实现

### 1.17 ACWing

1. **848. 有向图的拓扑序列**
   - 链接：https://www.acwing.com/problem/content/850/
   - 难度：简单
   - 类型：拓扑排序模板
   - 解法：Kahn算法

2. **850. Dijkstra求最短路 II**
   - 链接：https://www.acwing.com/problem/content/852/
   - 难度：中等
   - 类型：最短路 + 拓扑思想
   - 解法：优先队列优化Dijkstra

## 2. 题型分类

### 2.1 基础拓扑排序

**特征**：直接要求进行拓扑排序

**相关题目**：
- 洛谷 B3644 【模板】拓扑排序
- LeetCode 210. 课程表 II
- Aizu GRL_4_B. Topological Sort
- ACWing 848. 有向图的拓扑序列

**解法要点**：
1. 建图并计算入度
2. 将入度为0的节点加入队列
3. BFS处理队列中节点并更新邻居入度

### 2.2 拓扑排序判环

**特征**：判断图中是否有环

**相关题目**：
- LeetCode 207. 课程表
- POJ 1094 Sorting It All Out（部分情况）
- 洛谷 P1347 排序

**解法要点**：
1. 拓扑排序后检查结果序列长度是否等于节点数
2. 如果小于节点数，说明有环

### 2.3 字典序最小拓扑排序

**特征**：要求输出字典序最小的拓扑序列

**相关题目**：
- HDU 1285 确定比赛名次
- SPOJ TOPOSORT
- 牛客网 字典序最小的拓扑序列

**解法要点**：
1. 使用优先队列（最小堆）替代普通队列
2. 每次选择编号最小的入度为0节点

### 2.4 拓扑排序 + DP

**特征**：在拓扑排序过程中进行动态规划计算

**相关题目**：
- 洛谷 P4017 最大食物链计数
- 洛谷 P1113 杂务
- LeetCode 2050. 并行课程 III
- POJ 3249 Test for Job

**解法要点**：
1. 建图并计算入度
2. 拓扑排序过程中进行状态转移
3. 根据题目要求维护DP状态

### 2.5 拓扑排序状态判断

**特征**：逐步添加边并判断状态（唯一/矛盾/无法确定）

**相关题目**：
- POJ 1094 Sorting It All Out
- 洛谷 P1347 排序
- Timus 1280. Topological Sorting

**解法要点**：
1. 逐步添加关系
2. 每次添加后进行拓扑排序并判断状态
3. 根据题目要求输出相应结果

### 2.6 基环树

**特征**：每个节点只有一条出边的图（基环树森林）

**相关题目**：
- LeetCode 2127. 参加会议的最多员工数
- LeetCode 1559. 二维网格图中探测环
- 洛谷 P1453 城市环路
- 洛谷 P2607 [ZJOI2008]骑士

**解法要点**：
1. 拓扑排序删除所有不在环上的节点
2. 对剩余节点（在环上）进行分类讨论
3. 根据题目要求计算答案

### 2.7 拓扑排序 + 贪心

**特征**：在拓扑排序过程中使用贪心策略

**相关题目**：
- AtCoder ABC139E League
- LeetCode 630. 课程表 III
- LeetCode 621. 任务调度器

**解法要点**：
1. 建图并计算入度
2. 拓扑排序过程中结合贪心策略
3. 根据题目要求维护状态

### 2.8 字符顺序推断

**特征**：通过比较推断字符顺序并验证合法性

**相关题目**：
- Codeforces 510C Fox And Names
- LeetCode 269. 火星词典
- Project Euler Problem 79: Passcode derivation

**解法要点**：
1. 通过比较建立字符顺序关系
2. 构建有向图并进行拓扑排序
3. 检测环的存在判断合法性

## 3. 算法技巧

### 3.1 建图技巧

1. **邻接表**：适用于稀疏图
2. **邻接矩阵**：适用于稠密图
3. **链式前向星**：节省空间的邻接表实现

### 3.2 拓扑排序技巧

1. **优先队列**：实现字典序最小的拓扑排序
2. **分层处理**：处理需要按层处理的问题
3. **反向图**：某些情况下构建反向图更方便

### 3.3 DP状态设计

1. **路径计数**：dp[i] 表示到达节点 i 的路径数
2. **最短/最长路径**：dp[i] 表示到达节点 i 的最短/最长距离
3. **最优值传播**：dp[i] 表示节点 i 的某种最优属性

### 3.4 状态压缩

1. **二进制表示状态**：用位运算表示节点状态
2. **子集枚举**：枚举所有可能的节点组合
3. **位操作优化**：使用位运算提高效率

## 4. 工程化考虑

### 4.1 性能优化

1. **输入输出优化**：使用高效IO
2. **内存优化**：合理选择数据结构
3. **算法优化**：避免重复计算

### 4.2 边界处理

1. **空图处理**：处理没有节点或边的情况
2. **单节点图**：处理只有一个节点的情况
3. **环检测**：检测图中是否有环

### 4.3 异常处理

1. **输入验证**：验证输入数据的有效性
2. **状态检查**：检查算法执行过程中的状态
3. **错误恢复**：在出错时提供有意义的错误信息

## 5. 跨语言实现要点

### 5.1 Java实现要点

1. **集合类使用**：ArrayList、LinkedList等
2. **数组操作**：Arrays工具类的使用
3. **IO优化**：BufferedReader、StreamTokenizer等

### 5.2 C++实现要点

1. **STL使用**：vector、queue等容器
2. **内存管理**：手动管理动态内存
3. **IO优化**：scanf/printf比cin/cout更快

### 5.3 Python实现要点

1. **列表操作**：列表推导式、切片等
2. **collections模块**：deque、defaultdict等
3. **heapq模块**：优先队列实现
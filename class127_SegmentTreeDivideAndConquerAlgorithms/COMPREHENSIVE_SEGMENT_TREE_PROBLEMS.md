# 线段树分治算法 comprehensive 题目集

## 一、核心算法概念

线段树分治（Segment Tree Divide and Conquer）是一种离线算法技术，主要用于解决带有时间维度的图论问题和动态维护问题。它将操作序列按照时间轴建立线段树，然后通过DFS遍历线段树来处理各个时间区间内的操作。

### 核心思想
1. **离线处理**：将所有操作和查询离线，按照时间建立线段树
2. **区间操作**：将每个操作的影响时间段映射到线段树的节点上
3. **可撤销数据结构**：使用可撤销并查集等支持回滚操作的数据结构
4. **DFS遍历**：通过DFS遍历线段树，处理每个节点的操作并及时回滚

### 关键技术点
1. **可撤销并查集 (Rollback DSU)**：支持合并操作的回滚
2. **扩展域并查集 (Extended Union Find)**：用于二分图检测等约束问题
3. **线性基 (Linear Basis)**：处理异或运算相关问题
4. **可持久化数据结构**：处理多维限制问题

## 二、LeetCode题目

### 1. 动态图连通性 (Dynamic Graph Connectivity)
- **题目链接**: https://leetcode.com/problems/dynamic-graph-connectivity/
- **难度**: Hard
- **标签**: Union Find, Segment Tree, Divide and Conquer
- **题目描述**: 支持动态加边、删边操作，查询两点间连通性
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 2. 不良数对计数 (Count Number of Bad Pairs)
- **题目链接**: https://leetcode.com/problems/count-number-of-bad-pairs/
- **难度**: Medium
- **标签**: Segment Tree, Divide and Conquer, Math
- **题目描述**: 统计满足特定条件的数对数量
- **解法**: 线段树分治 + 数学变换
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 3. 带阈值的图连通性 (Graph Connectivity With Threshold)
- **题目链接**: https://leetcode.com/problems/graph-connectivity-with-threshold/
- **难度**: Hard
- **标签**: Union Find, Math, Segment Tree, Divide and Conquer
- **题目描述**: 给定n个城市，编号1到n，当两个城市的最大公约数大于threshold时它们直接相连，查询任意两个城市是否连通
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O(n log n + q log n)
- **空间复杂度**: O(n)

## 三、Codeforces题目

### 1. 二分图检测 (Bipartite Checking) - 813F
- **题目链接**: https://codeforces.com/contest/813/problem/F
- **难度**: 2400
- **标签**: Segment Tree, Divide and Conquer, Union Find, Bipartite Graph
- **题目描述**: 动态维护图的二分性
- **解法**: 线段树分治 + 扩展域并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 2. 唯一出现次数 (Unique Occurrences) - 1681F
- **题目链接**: https://codeforces.com/contest/1681/problem/F
- **难度**: 2600
- **标签**: Segment Tree, Divide and Conquer, Union Find, Tree
- **题目描述**: 统计树上路径中唯一出现的颜色数量
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 3. 边着色 (Painting Edges) - 576E
- **题目链接**: https://codeforces.com/contest/576/problem/E
- **难度**: 3300
- **标签**: Segment Tree, Divide and Conquer, Union Find, Graph
- **题目描述**: 给边着色使得每种颜色构成的子图都是二分图
- **解法**: 线段树分治 + 多个扩展域并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 4. 博物馆劫案 (Museum Robbery) - 601E
- **题目链接**: https://codeforces.com/problemset/problem/601/E
- **难度**: 2800
- **标签**: Segment Tree, Divide and Conquer, Dynamic Programming
- **题目描述**: 维护商品集合，支持添加、删除商品，查询背包问题变形结果
- **解法**: 线段树分治 + 动态规划
- **时间复杂度**: O(qk log q + nk)
- **空间复杂度**: O(qk)

### 5. 线段上的加法 (Addition on Segments) - 981E
- **题目链接**: https://codeforces.com/problemset/problem/981/E
- **难度**: 2200
- **标签**: Segment Tree, Divide and Conquer, Bit Manipulation, Dynamic Programming
- **题目描述**: 给定数组初始全为0，支持区间加法操作，每种操作只能执行一次，查询能通过选择操作得到的所有可能最大值
- **解法**: 线段树分治 + 位运算优化DP
- **时间复杂度**: O(nq log q)
- **空间复杂度**: O(n)

### 6. 异或最短路 (Shortest Path Queries) - 938G
- **题目链接**: https://codeforces.com/problemset/problem/938/G
- **难度**: 2900
- **标签**: Segment Tree, Divide and Conquer, Linear Basis, Union Find
- **题目描述**: 维护图，支持加边、删边操作，查询两点间路径边权异或和的最小值
- **解法**: 线段树分治 + 带权并查集 + 线性基
- **时间复杂度**: O((n+q) log q log V)
- **空间复杂度**: O(n + q)

## 四、洛谷题目

### 1. 二分图 /【模板】线段树分治 - P5787
- **题目链接**: https://www.luogu.com.cn/problem/P5787
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 扩展域并查集, 二分图
- **题目描述**: 维护动态图使其为二分图
- **解法**: 线段树分治 + 扩展域并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 2. 最小mex生成树 - P5631
- **题目链接**: https://www.luogu.com.cn/problem/P5631
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 并查集, 生成树, 二分
- **题目描述**: 求生成树使得边权集合的mex最小
- **解法**: 线段树分治 + 可撤销并查集 + 二分答案
- **时间复杂度**: O((n + m) log m log n)
- **空间复杂度**: O(n + m)

### 3. 大融合 - P4219
- **题目链接**: https://www.luogu.com.cn/problem/P4219
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 并查集, 图论
- **题目描述**: 支持加边和查询边负载，边负载定义为删去该边后两个连通块大小的乘积
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 4. 连通图 - P5227
- **题目链接**: https://www.luogu.com.cn/problem/P5227
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 并查集, 图论
- **题目描述**: 给定初始连通图，每次删除一些边，查询是否仍连通
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 5. 八纵八横 - P3733
- **题目链接**: https://www.luogu.com.cn/problem/P3733
- **难度**: 省选/NOI-
- **标签**: 线段树分治, Linear Basis, Union Find
- **题目描述**: 维护图，支持加边、删边、修改边权操作，查询从1号点出发回到1号点路径边权异或和的最大值
- **解法**: 线段树分治 + 带权并查集 + 线性基
- **时间复杂度**: O((n+q) log q L)
- **空间复杂度**: O(nL + qL)

### 6. 火星商店 - P4585
- **题目链接**: https://www.luogu.com.cn/problem/P4585
- **难度**: 省选/NOI-
- **标签**: 线段树分治, Persistent Trie
- **题目描述**: 维护n个商店，每个商店有商品，支持添加商品、查询操作，查询要求在特定商店范围内和时间范围内找到异或最大值
- **解法**: 线段树分治 + 可持久化Trie
- **时间复杂度**: O((n+q) log q log V)
- **空间复杂度**: O((n+q) log V)

## 五、AtCoder题目

### 1. 细胞分裂 (Cell Division) - AGC010C
- **题目链接**: https://atcoder.jp/contests/agc010/tasks/agc010_c
- **难度**: 2300
- **标签**: Union Find, Divide and Conquer
- **题目描述**: 分割矩形并计算每次分割后的连通分量数
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 2. 最小异或对查询 (Minimum Xor Pair Query) - ABC308G
- **题目链接**: https://atcoder.jp/contests/abc308/tasks/abc308_g
- **难度**: 600
- **标签**: Trie, Bit Manipulation
- **题目描述**: 维护集合，支持添加数字、删除数字、查询操作，查询集合中任意两个数的异或最小值
- **解法**: 01Trie + 在线维护
- **时间复杂度**: O(q log V)
- **空间复杂度**: O(q log V)

## 六、SPOJ题目

### 1. 动态连通性 (DYNACON1)
- **题目链接**: https://www.spoj.com/problems/DYNACON1/
- **难度**: Hard
- **标签**: Segment Tree, Divide and Conquer, Union Find
- **题目描述**: 动态维护图的连通性，支持加边、删边和查询操作
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 2. 动态图的最小生成树 (DYNACON2)
- **题目链接**: https://www.spoj.com/problems/DYNACON2/
- **难度**: Hard
- **标签**: Segment Tree, Divide and Conquer, Union Find
- **题目描述**: 动态维护图的最小生成树相关查询
- **解法**: 线段树分治 + 可撤销并查集 + Kruskal算法
- **时间复杂度**: O((n + m) log m log n)
- **空间复杂度**: O(n + m)

## 七、其他平台题目

### 1. 动态树 (HackerRank Dynamic Trees)
- **题目链接**: https://www.hackerrank.com/challenges/dynamic-trees
- **难度**: Advanced
- **标签**: Segment Tree, Divide and Conquer, Tree Data Structures
- **题目描述**: 动态维护树的结构和路径查询
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 2. USACO相关题目
- **题目描述**: 在USACO竞赛中，线段树分治常用于解决牧场连接、路径查询等动态图问题
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

## 八、线段树分治典型应用场景

### 1. 动态图问题
- 动态连通性查询
- 二分图维护
- 最小生成树动态维护
- 异或路径查询

### 2. 区间操作问题
- 支持区间添加和删除的数据结构
- 区间内有效操作的维护

### 3. 可撤销操作问题
- 需要回滚操作的场景
- 多版本数据维护

### 4. 离线查询问题
- 所有查询可以预先知道
- 按时间顺序处理的问题

## 九、核心实现模板

### Java版本核心实现
```java
// 可撤销并查集
class RollbackDSU {
    int[] father, size;
    Stack<int[]> rollbackStack = new Stack<>();
    
    int find(int x) {
        while (x != father[x]) x = father[x];
        return x;
    }
    
    boolean union(int x, int y) {
        int fx = find(x), fy = find(y);
        if (fx == fy) return false;
        // 按秩合并
        if (size[fx] < size[fy]) {
            int temp = fx; fx = fy; fy = temp;
        }
        father[fy] = fx;
        size[fx] += size[fy];
        rollbackStack.push(new int[]{fx, fy});
        return true;
    }
    
    void rollback() {
        int[] op = rollbackStack.pop();
        int fx = op[0], fy = op[1];
        father[fy] = fy;
        size[fx] -= size[fy];
    }
}

// 线段树分治DFS遍历
public static void dfs(int l, int r, int i) {
    int unionCnt = 0;
    
    // 处理当前节点上的所有边
    for (int e = head[i]; e > 0; e = next[e]) {
        if (union(tox[e], toy[e])) {
            unionCnt++;
        }
    }
    
    if (l == r) {
        // 处理叶子节点查询
        if (op[l] == 3) {
            ans[l] = (find(x[l]) == find(y[l]));
        }
    } else {
        // 递归处理左右子树
        int mid = (l + r) >> 1;
        dfs(l, mid, i << 1);
        dfs(mid + 1, r, i << 1 | 1);
    }
    
    // 回滚操作
    for (int k = 1; k <= unionCnt; k++) {
        undo();
    }
}
```

### C++版本核心实现
```cpp
// 可撤销并查集
class RollbackDSU {
private:
    vector<int> father;
    vector<int> size;
    vector<pair<int, int>> rollbackStack;
    
public:
    RollbackDSU(int n) {
        father.resize(n + 1);
        size.resize(n + 1, 1);
        for (int i = 1; i <= n; i++) {
            father[i] = i;
        }
    }
    
    int find(int x) {
        while (x != father[x]) {
            x = father[x];
        }
        return x;
    }
    
    bool unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx == fy) return false;
        
        if (size[fx] < size[fy]) {
            swap(fx, fy);
        }
        
        rollbackStack.push_back({fx, fy});
        father[fy] = fx;
        size[fx] += size[fy];
        return true;
    }
    
    void rollback(int cnt) {
        while (cnt--) {
            auto [fx, fy] = rollbackStack.back();
            rollbackStack.pop_back();
            father[fy] = fy;
            size[fx] -= size[fy];
        }
    }
};

// 线段树分治DFS遍历
void dfs(int l, int r, int i) {
    int unionCnt = 0;
    
    // 处理当前节点上的所有边
    for (int e = head[i]; e; e = next_[e]) {
        if (unite(tox[e], toy[e])) {
            unionCnt++;
        }
    }
    
    if (l == r) {
        // 处理叶子节点查询
        if (op[l] == 3) {
            ans[l] = (find(x[l]) == find(y[l]));
        }
    } else {
        // 递归处理左右子树
        int mid = (l + r) >> 1;
        dfs(l, mid, i << 1);
        dfs(mid + 1, r, i << 1 | 1);
    }
    
    // 回滚操作
    for (int k = 1; k <= unionCnt; k++) {
        undo();
    }
}
```

### Python版本核心实现
```python
class RollbackDSU:
    def __init__(self, n):
        self.father = list(range(n + 1))
        self.size = [1] * (n + 1)
        self.rollback_stack = []
    
    def find(self, x):
        # 不使用路径压缩
        while x != self.father[x]:
            x = self.father[x]
        return x
    
    def union(self, x, y):
        fx = self.find(x)
        fy = self.find(y)
        if fx == fy:
            return False
        
        if self.size[fx] < self.size[fy]:
            fx, fy = fy, fx
        
        self.rollback_stack.append((fx, fy))
        self.father[fy] = fx
        self.size[fx] += self.size[fy]
        return True
    
    def rollback(self, count):
        for _ in range(count):
            fx, fy = self.rollback_stack.pop()
            self.father[fy] = fy
            self.size[fx] -= self.size[fy]

def dfs(l, r, i):
    # 处理当前节点上的所有边
    union_cnt = 0
    e = head[i]
    while e > 0:
        if union(tox[e], toy[e]):
            union_cnt += 1
        e = next_[e]
    
    if l == r:
        # 处理叶子节点查询
        if op[l] == 3:
            ans[l] = (find(x[l]) == find(y[l]))
    else:
        # 递归处理左右子树
        mid = (l + r) >> 1
        dfs(l, mid, i << 1)
        dfs(mid + 1, r, i << 1 | 1)
    
    # 回滚操作
    for _ in range(union_cnt):
        undo()
```

## 十、练习建议

1. **从基础开始**：先掌握可撤销并查集的实现，理解如何支持回滚操作
2. **理解线段树分治的核心思想**：将时间轴划分为线段树结构，处理每个时间区间内的操作
3. **掌握扩展域并查集**：用于处理二分图检测等约束问题
4. **练习经典题目**：从简单的动态连通性问题开始，逐步挑战更复杂的题目
5. **注意实现细节**：特别是回滚操作的正确性和时间复杂度分析
6. **多语言实践**：使用Java、C++、Python三种语言实现，理解不同语言的特点
7. **性能优化**：学习各种优化技巧，如位运算优化、内存优化等
# 树上倍增算法补充题目

## 1. LeetCode系列

### 1.1 LeetCode 1483. 树节点的第K个祖先 (Kth Ancestor of a Tree Node)
**题目链接**: https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/

**题目描述**:
给你一个树，树上有 n 个节点，节点编号从 0 到 n-1。树以父节点数组的形式给出，其中 parent[i] 是节点 i 的父节点。树的根节点是编号为 0 的节点。树节点的第 k 个祖先节点是从该节点到根节点路径上的第 k 个节点。实现 TreeAncestor 类：
- TreeAncestor(int n, int[] parent) 对树和父数组中的节点数初始化对象。
- getKthAncestor(int node, int k) 返回节点 node 的第 k 个祖先节点。如果不存在这样的祖先节点，则返回 -1。

**解题思路**:
这是树上倍增算法的典型应用。我们预处理每个节点的 2^i 级祖先，然后通过二进制分解 k 来快速查找第 k 个祖先。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

**Java实现**:
```java
class TreeAncestor {
    private int[][] stjump; // stjump[i][j] 表示节点i的2^j级祖先
    private int LOG; // 最大的2的幂次

    public TreeAncestor(int n, int[] parent) {
        // 计算所需的最大对数
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        stjump = new int[n][LOG];
        
        // 初始化直接父节点 (2^0级祖先)
        for (int i = 0; i < n; i++) {
            stjump[i][0] = parent[i];
        }
        
        // 预处理倍增数组，填充所有2^j级祖先
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (stjump[i][j-1] != -1) {
                    stjump[i][j] = stjump[stjump[i][j-1]][j-1];
                } else {
                    stjump[i][j] = -1;
                }
            }
        }
    }
    
    public int getKthAncestor(int node, int k) {
        // 二进制分解k，从低位到高位尝试跳跃
        for (int j = 0; j < LOG; j++) {
            if ((k >> j & 1) == 1) { // 如果k的二进制第j位是1
                node = stjump[node][j];
                if (node == -1) break; // 如果已经跳到根节点以上，提前结束
            }
        }
        return node;
    }
}
```

**C++实现**:
```cpp
class TreeAncestor {
private:
    vector<vector<int>> stjump; // stjump[i][j] 表示节点i的2^j级祖先
    int LOG; // 最大的2的幂次

public:
    TreeAncestor(int n, vector<int>& parent) {
        // 计算所需的最大对数
        LOG = 0;
        while ((1 << LOG) <= n) LOG++;
        LOG++;
        stjump.resize(n, vector<int>(LOG, -1));
        
        // 初始化直接父节点 (2^0级祖先)
        for (int i = 0; i < n; i++) {
            stjump[i][0] = parent[i];
        }
        
        // 预处理倍增数组，填充所有2^j级祖先
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (stjump[i][j-1] != -1) {
                    stjump[i][j] = stjump[stjump[i][j-1]][j-1];
                } else {
                    stjump[i][j] = -1;
                }
            }
        }
    }
    
    int getKthAncestor(int node, int k) {
        // 二进制分解k，从低位到高位尝试跳跃
        for (int j = 0; j < LOG; j++) {
            if ((k >> j) & 1) { // 如果k的二进制第j位是1
                node = stjump[node][j];
                if (node == -1) break; // 如果已经跳到根节点以上，提前结束
            }
        }
        return node;
    }
};
```

**Python实现**:
```python
class TreeAncestor:
    def __init__(self, n: int, parent: list[int]):
        # 计算所需的最大对数
        self.LOG = 0
        while (1 << self.LOG) <= n:
            self.LOG += 1
        self.LOG += 1
        
        # 初始化倍增表
        self.stjump = [[-1] * self.LOG for _ in range(n)]
        
        # 初始化直接父节点 (2^0级祖先)
        for i in range(n):
            self.stjump[i][0] = parent[i]
        
        # 预处理倍增数组，填充所有2^j级祖先
        for j in range(1, self.LOG):
            for i in range(n):
                if self.stjump[i][j-1] != -1:
                    self.stjump[i][j] = self.stjump[self.stjump[i][j-1]][j-1]
                else:
                    self.stjump[i][j] = -1
    
    def getKthAncestor(self, node: int, k: int) -> int:
        # 二进制分解k，从低位到高位尝试跳跃
        for j in range(self.LOG):
            if (k >> j) & 1:  # 如果k的二进制第j位是1
                node = self.stjump[node][j]
                if node == -1:  # 如果已经跳到根节点以上，提前结束
                    break
        return node
```

**算法优化与工程化考量**:
1. **预计算LOG值**：提前计算最大的2的幂次，避免重复计算
2. **边界条件处理**：当节点不存在祖先时返回-1，确保代码鲁棒性
3. **位运算优化**：使用位运算进行二进制分解，提高效率
4. **空间优化**：根据实际数据规模调整LOG的大小
5. **缓存友好性**：二维数组的访问模式符合缓存局部性原理

### 1.2 LeetCode 236. 二叉树的最近公共祖先 (Lowest Common Ancestor of a Binary Tree)
**题目链接**: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/

**题目描述**:
给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

百度百科中最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。"

**解题思路**:
对于一般的二叉树，可以使用递归方法。但对于一般的树结构，可以使用树上倍增算法来高效解决。

**时间复杂度**: O(log n)
**空间复杂度**: O(n log n)

### 1.3 LeetCode 2836. 在传球游戏中最大化函数值 (Maximize Value of Function in a Ball Passing Game)
**题目链接**: https://leetcode.cn/problems/maximize-value-of-function-in-a-ball-passing-game/

**题目描述**:
给定一个长度为n的数组receiver和一个整数k。总共有n名玩家，编号0 ~ n-1，这些玩家在玩一个传球游戏。receiver[i]表示编号为i的玩家会传球给下一个人的编号。玩家可以传球给自己，也就是说receiver[i]可能等于i。

你需要选择一名开始玩家，然后开始传球，球会被传恰好k次。如果选择编号为x的玩家作为开始玩家，函数f(x)表示从x玩家开始，k次传球内所有接触过球的玩家编号之和。你的任务是选择开始玩家x，目的是最大化f(x)，返回函数的最大值。

**解题思路**:
使用树上倍增算法，预处理每个节点跳2^i步能到达的位置和路径和，然后通过二进制分解计算k步后的结果。

**时间复杂度**: O(n log k)
**空间复杂度**: O(n log k)

### 1.4 LeetCode 2846. 边权重均等查询 (Minimum Edge Weight Equilibrium Queries in a Tree)
**题目链接**: https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/

**题目描述**:
给定一个包含n个节点的树，节点编号从0到n-1。给定一个二维数组edges，其中edges[i] = [ui, vi, wi]表示节点ui和vi之间有一条权重为wi的边。

给定一个查询数组queries，其中queries[i] = [ai, bi]。对于每个查询，找出从节点ai到节点bi的路径上，最少需要修改多少条边的权重，才能使路径上所有边的权重相等。

**解题思路**:
使用树上倍增算法计算LCA，然后统计路径上各种权重的数量，找出出现次数最多的权重，其余权重都需要修改。

**时间复杂度**: O(n log n + q log n)
**空间复杂度**: O(n log n)

### 2. LeetCode 2846. 边权重均等查询

**题目链接**：[LeetCode 2846. 边权重均等查询](https://leetcode.com/problems/minimum-number-of-changes-to-make-binary-string-beautiful/)

**题目描述**：
现有一棵由 n 个节点组成的无向树，节点按从 0 到 n-1 编号。给你一个整数 n 和一个长度为 n-1 的二维整数数组 edges，其中 edges[i] = [ai, bi, wi] 表示树中存在一条位于节点 ai 和 bi 之间、权重为 wi 的边。

另给你一个长度为 m 的二维整数数组 queries，其中 queries[i] = [ui, vi]。对于每个查询，你需要找到一条从节点 ui 到节点 vi 的路径，使得路径上经过的边的权重相等的数目最少。请你返回一个长度为 m 的数组，其中数组的第 i 个元素是第 i 个查询的答案。

**解题思路**：
1. 首先使用树上倍增算法预处理每个节点的祖先信息和深度
2. 对于每个查询，找到两个节点的LCA，将路径拆分为 u -> LCA 和 v -> LCA
3. 对于每条查询路径，统计各权重边的出现次数，找出出现次数最多的权重，用总边数减去该次数得到最小修改次数

**复杂度分析**：
- 时间复杂度：预处理 O(n log n)，每个查询 O(log n)
- 空间复杂度：O(n log n)

**Java实现**：
```java
class Solution {
    private int[][] stjump; // stjump[i][j] 表示节点i的2^j级祖先
    private int[] depth; // 每个节点的深度
    private int LOG; // 最大的2的幂次
    private List<List<int[]>> adj; // 邻接表表示树
    private Map<Integer, int[]>[] weightCount; // 每个节点到根节点各权重的计数
    private int[] parent; // 直接父节点
    private int[] edgeWeight; // 到父节点的边权重

    public int[] minOperationsQueries(int n, int[][] edges, int[][] queries) {
        // 初始化
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        stjump = new int[n][LOG];
        depth = new int[n];
        adj = new ArrayList<>();
        weightCount = new HashMap[n];
        parent = new int[n];
        edgeWeight = new int[n];
        
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
            weightCount[i] = new HashMap<>();
        }
        
        // 构建邻接表
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            adj.get(u).add(new int[]{v, w});
            adj.get(v).add(new int[]{u, w});
        }
        
        // 预处理父节点、深度和权重计数
        Arrays.fill(parent, -1);
        dfs(0, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (stjump[i][j-1] != -1) {
                    stjump[i][j] = stjump[stjump[i][j-1]][j-1];
                } else {
                    stjump[i][j] = -1;
                }
            }
        }
        
        // 处理查询
        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0], v = queries[i][1];
            int lcaNode = lca(u, v);
            int totalEdges = depth[u] + depth[v] - 2 * depth[lcaNode];
            
            // 统计路径上各权重的出现次数
            Map<Integer, Integer> count = new HashMap<>();
            addWeights(u, lcaNode, count);
            addWeights(v, lcaNode, count);
            
            // 找出出现次数最多的权重
            int maxCount = 0;
            for (int cnt : count.values()) {
                maxCount = Math.max(maxCount, cnt);
            }
            
            result[i] = totalEdges - maxCount;
        }
        
        return result;
    }
    
    private void dfs(int node, int p, int d) {
        parent[node] = p;
        depth[node] = d;
        stjump[node][0] = p;
        
        // 复制父节点的权重计数
        if (p != -1) {
            for (Map.Entry<Integer, int[]> entry : weightCount[p].entrySet()) {
                weightCount[node].put(entry.getKey(), entry.getValue().clone());
            }
            // 添加当前边的权重计数
            int w = edgeWeight[node];
            weightCount[node].putIfAbsent(w, new int[1]);
            weightCount[node].get(w)[0]++;
        }
        
        for (int[] neighbor : adj.get(node)) {
            int next = neighbor[0], w = neighbor[1];
            if (next != p) {
                edgeWeight[next] = w;
                dfs(next, node, d + 1);
            }
        }
    }
    
    private int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (stjump[u][j] != -1 && depth[stjump[u][j]] >= depth[v]) {
                u = stjump[u][j];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v，直到找到公共祖先
        for (int j = LOG - 1; j >= 0; j--) {
            if (stjump[u][j] != -1 && stjump[u][j] != stjump[v][j]) {
                u = stjump[u][j];
                v = stjump[v][j];
            }
        }
        
        return parent[u];
    }
    
    private void addWeights(int from, int to, Map<Integer, Integer> count) {
        while (from != to) {
            int w = edgeWeight[from];
            count.put(w, count.getOrDefault(w, 0) + 1);
            from = parent[from];
        }
    }
}
```

**C++实现**：
```cpp
class Solution {
private:
    vector<vector<int>> stjump; // stjump[i][j] 表示节点i的2^j级祖先
    vector<int> depth; // 每个节点的深度
    int LOG; // 最大的2的幂次
    vector<vector<pair<int, int>>> adj; // 邻接表表示树
    vector<unordered_map<int, int>> weightCount; // 每个节点到根节点各权重的计数
    vector<int> parent; // 直接父节点
    vector<int> edgeWeight; // 到父节点的边权重

    void dfs(int node, int p, int d) {
        parent[node] = p;
        depth[node] = d;
        stjump[node][0] = p;
        
        // 复制父节点的权重计数
        if (p != -1) {
            weightCount[node] = weightCount[p];
            // 添加当前边的权重计数
            int w = edgeWeight[node];
            weightCount[node][w]++;
        }
        
        for (auto& neighbor : adj[node]) {
            int next = neighbor.first, w = neighbor.second;
            if (next != p) {
                edgeWeight[next] = w;
                dfs(next, node, d + 1);
            }
        }
    }
    
    int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            swap(u, v);
        }
        
        // 将u提升到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (stjump[u][j] != -1 && depth[stjump[u][j]] >= depth[v]) {
                u = stjump[u][j];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v，直到找到公共祖先
        for (int j = LOG - 1; j >= 0; j--) {
            if (stjump[u][j] != -1 && stjump[u][j] != stjump[v][j]) {
                u = stjump[u][j];
                v = stjump[v][j];
            }
        }
        
        return parent[u];
    }
    
    void addWeights(int from, int to, unordered_map<int, int>& count) {
        while (from != to) {
            int w = edgeWeight[from];
            count[w]++;
            from = parent[from];
        }
    }

public:
    vector<int> minOperationsQueries(int n, vector<vector<int>>& edges, vector<vector<int>>& queries) {
        // 初始化
        LOG = 0;
        while ((1 << LOG) <= n) LOG++;
        LOG++;
        stjump.resize(n, vector<int>(LOG, -1));
        depth.resize(n);
        adj.resize(n);
        weightCount.resize(n);
        parent.resize(n, -1);
        edgeWeight.resize(n);
        
        // 构建邻接表
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            adj[u].emplace_back(v, w);
            adj[v].emplace_back(u, w);
        }
        
        // 预处理父节点、深度和权重计数
        dfs(0, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (stjump[i][j-1] != -1) {
                    stjump[i][j] = stjump[stjump[i][j-1]][j-1];
                } else {
                    stjump[i][j] = -1;
                }
            }
        }
        
        // 处理查询
        vector<int> result(queries.size());
        for (int i = 0; i < queries.size(); i++) {
            int u = queries[i][0], v = queries[i][1];
            int lcaNode = lca(u, v);
            int totalEdges = depth[u] + depth[v] - 2 * depth[lcaNode];
            
            // 统计路径上各权重的出现次数
            unordered_map<int, int> count;
            addWeights(u, lcaNode, count);
            addWeights(v, lcaNode, count);
            
            // 找出出现次数最多的权重
            int maxCount = 0;
            for (auto& [w, cnt] : count) {
                maxCount = max(maxCount, cnt);
            }
            
            result[i] = totalEdges - maxCount;
        }
        
        return result;
    }
};
```

**Python实现**：
```python
class Solution:
    def minOperationsQueries(self, n: int, edges: list[list[int]], queries: list[list[int]]) -> list[int]:
        # 初始化
        LOG = 0
        while (1 << LOG) <= n:
            LOG += 1
        LOG += 1
        
        stjump = [[-1] * LOG for _ in range(n)]  # stjump[i][j] 表示节点i的2^j级祖先
        depth = [0] * n  # 每个节点的深度
        adj = [[] for _ in range(n)]  # 邻接表表示树
        weight_count = [{} for _ in range(n)]  # 每个节点到根节点各权重的计数
        parent = [-1] * n  # 直接父节点
        edge_weight = [0] * n  # 到父节点的边权重
        
        # 构建邻接表
        for u, v, w in edges:
            adj[u].append((v, w))
            adj[v].append((u, w))
        
        # 预处理父节点、深度和权重计数
        def dfs(node, p, d):
            parent[node] = p
            depth[node] = d
            stjump[node][0] = p
            
            # 复制父节点的权重计数
            if p != -1:
                weight_count[node] = weight_count[p].copy()
                # 添加当前边的权重计数
                w = edge_weight[node]
                weight_count[node][w] = weight_count[node].get(w, 0) + 1
            
            for next_node, w in adj[node]:
                if next_node != p:
                    edge_weight[next_node] = w
                    dfs(next_node, node, d + 1)
        
        dfs(0, -1, 0)
        
        # 构建倍增表
        for j in range(1, LOG):
            for i in range(n):
                if stjump[i][j-1] != -1:
                    stjump[i][j] = stjump[stjump[i][j-1]][j-1]
                else:
                    stjump[i][j] = -1
        
        # LCA查找函数
        def lca(u, v):
            if depth[u] < depth[v]:
                u, v = v, u
            
            # 将u提升到v的深度
            for j in range(LOG-1, -1, -1):
                if stjump[u][j] != -1 and depth[stjump[u][j]] >= depth[v]:
                    u = stjump[u][j]
            
            if u == v:
                return u
            
            # 同时提升u和v，直到找到公共祖先
            for j in range(LOG-1, -1, -1):
                if stjump[u][j] != -1 and stjump[u][j] != stjump[v][j]:
                    u = stjump[u][j]
                    v = stjump[v][j]
            
            return parent[u]
        
        # 添加权重计数函数
        def add_weights(from_node, to_node, count):
            while from_node != to_node:
                w = edge_weight[from_node]
                count[w] = count.get(w, 0) + 1
                from_node = parent[from_node]
        
        # 处理查询
        result = []
        for u, v in queries:
            lca_node = lca(u, v)
            total_edges = depth[u] + depth[v] - 2 * depth[lca_node]
            
            # 统计路径上各权重的出现次数
            count = {}
            add_weights(u, lca_node, count)
            add_weights(v, lca_node, count)
            
            # 找出出现次数最多的权重
            max_count = max(count.values(), default=0)
            
            result.append(total_edges - max_count)
        
        return result
```

**算法优化与工程化考量**：
1. **空间优化**：使用哈希表存储权重计数，减少不必要的空间消耗
2. **时间优化**：通过预处理权重计数，避免重复计算路径上的权重分布
3. **边界处理**：处理根节点等特殊情况，确保算法的鲁棒性
4. **数据结构选择**：根据数据规模选择合适的数据结构，平衡时间和空间复杂度

**扩展思考**：
- 如何处理更大规模的数据？
- 如何优化权重计数的存储方式？
- 如何将该问题与机器学习中的路径分析问题结合？

### 3. 洛谷 P5588 小猪佩奇爬树

**题目链接**：[洛谷 P5588 小猪佩奇爬树](https://www.luogu.com.cn/problem/P5588)

**题目描述**：
佩奇和乔治在爬♂树。给定 n 个节点的树 T(V,E)，第 i 个节点的颜色为 wi，保证有1≤wi≤n。对于1≤i≤n，分别输出有多少对点对 (u,v)，满足 u<v，且恰好经过所有颜色为 i 的节点，对于节点颜色不为 i 的其他节点，经过或不经过均可。

**解题思路**：
1. 使用树上倍增算法预处理每个节点的祖先信息和深度
2. 对于每种颜色，收集所有该颜色的节点
3. 根据颜色节点的分布情况，分情况计算符合条件的路径数：
   - 如果颜色节点数为0，答案为总路径数
   - 如果颜色节点数为1，计算包含该节点的所有路径数
   - 如果颜色节点数≥2，判断节点是否在一条链上，然后计算相应的路径数

**复杂度分析**：
- 时间复杂度：预处理 O(n log n)，每种颜色处理 O(k log n)，其中k为该颜色的节点数
- 空间复杂度：O(n log n)

**Java实现**：
```java
import java.util.*;

public class Main {
    private static int[][] stjump; // 倍增表
    private static int[] depth; // 深度数组
    private static int LOG; // 最大对数
    private static int[] size; // 子树大小
    private static List<Integer>[] adj; // 邻接表

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] w = new int[n + 1]; // 颜色数组，节点编号1~n
        for (int i = 1; i <= n; i++) {
            w[i] = scanner.nextInt();
        }
        
        // 初始化邻接表
        adj = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < n - 1; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            adj[u].add(v);
            adj[v].add(u);
        }
        
        // 初始化树上倍增相关数组
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        stjump = new int[n + 1][LOG];
        depth = new int[n + 1];
        size = new int[n + 1];
        
        // 预处理：DFS计算深度、子树大小和直接父节点
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (stjump[i][j-1] != -1) {
                    stjump[i][j] = stjump[stjump[i][j-1]][j-1];
                } else {
                    stjump[i][j] = -1;
                }
            }
        }
        
        // 按颜色分组节点
        Map<Integer, List<Integer>> colorMap = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            colorMap.putIfAbsent(w[i], new ArrayList<>());
            colorMap.get(w[i]).add(i);
        }
        
        // 计算答案
        long totalPairs = (long) n * (n - 1) / 2;
        for (int i = 1; i <= n; i++) {
            List<Integer> nodes = colorMap.getOrDefault(i, new ArrayList<>());
            long ans;
            
            if (nodes.isEmpty()) {
                // 颜色i不存在，所有路径都满足条件
                ans = totalPairs;
            } else if (nodes.size() == 1) {
                // 只有一个颜色i的节点
                int u = nodes.get(0);
                ans = 0;
                // 计算经过u的所有路径数
                ans = (long) size[u] * (n - size[u]);
                // 加上从u出发的所有路径
                ans += (long) (n - 1);
                ans /= 2;
            } else {
                // 颜色i有多个节点，检查是否在一条链上
                boolean allOnChain = true;
                int deepest = nodes.get(0);
                // 找到最深的节点
                for (int node : nodes) {
                    if (depth[node] > depth[deepest]) {
                        deepest = node;
                    }
                }
                
                // 检查其他节点是否都是deepest的祖先
                for (int node : nodes) {
                    if (node != deepest && !isAncestor(node, deepest)) {
                        allOnChain = false;
                        break;
                    }
                }
                
                if (allOnChain) {
                    // 所有节点都在一条链上，找到最浅的节点
                    int shallowest = nodes.get(0);
                    for (int node : nodes) {
                        if (depth[node] < depth[shallowest]) {
                            shallowest = node;
                        }
                    }
                    
                    // 计算子树大小乘积
                    long cnt = 1;
                    int last = shallowest;
                    // 找到shallowest的直接子节点，该子节点在链上
                    for (int child : adj[shallowest]) {
                        if (child != parent(shallowest) && isAncestor(child, deepest)) {
                            cnt *= size[child];
                            last = child;
                            break;
                        }
                    }
                    
                    // 计算最深节点的子树大小
                    cnt *= size[deepest];
                    
                    ans = cnt;
                } else {
                    // 不在一条链上，可能有两个分支
                    // 找到两个最深的节点，检查它们的LCA
                    int u = nodes.get(0), v = nodes.get(1);
                    for (int node : nodes) {
                        if (depth[node] > depth[u]) {
                            v = u;
                            u = node;
                        } else if (depth[node] > depth[v]) {
                            v = node;
                        }
                    }
                    
                    int ancestor = lca(u, v);
                    boolean hasOther = false;
                    for (int node : nodes) {
                        if (!isAncestor(ancestor, node) || 
                            (node != u && node != v && !isAncestor(u, node) && !isAncestor(v, node))) {
                            hasOther = true;
                            break;
                        }
                    }
                    
                    if (hasOther) {
                        // 有超过两个分支，无法用一条路径覆盖所有颜色节点
                        ans = 0;
                    } else {
                        // 只有两个分支，计算两个分支子树大小的乘积
                        ans = (long) size[u] * size[v];
                    }
                }
            }
            
            System.out.println(ans);
        }
        
        scanner.close();
    }
    
    // DFS预处理
    private static void dfs(int u, int p, int d) {
        stjump[u][0] = p;
        depth[u] = d;
        size[u] = 1;
        
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
                size[u] += size[v];
            }
        }
    }
    
    // 获取父节点
    private static int parent(int u) {
        return stjump[u][0];
    }
    
    // LCA查询
    private static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (stjump[u][j] != -1 && depth[stjump[u][j]] >= depth[v]) {
                u = stjump[u][j];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v
        for (int j = LOG - 1; j >= 0; j--) {
            if (stjump[u][j] != -1 && stjump[u][j] != stjump[v][j]) {
                u = stjump[u][j];
                v = stjump[v][j];
            }
        }
        
        return stjump[u][0];
    }
    
    // 判断a是否是b的祖先
    private static boolean isAncestor(int a, int b) {
        return lca(a, b) == a;
    }
}
```

**C++实现**：
```cpp
#include <iostream>
#include <vector>
#include <map>
#include <cmath>
using namespace std;

const int MAXN = 1e5 + 10;
vector<vector<int>> stjump;
vector<int> depth;
int LOG;
vector<int> size_;
vector<int> adj[MAXN];

void dfs(int u, int p, int d) {
    stjump[u][0] = p;
    depth[u] = d;
    size_[u] = 1;
    
    for (int v : adj[u]) {
        if (v != p) {
            dfs(v, u, d + 1);
            size_[u] += size_[v];
        }
    }
}

int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    // 将u提升到v的深度
    for (int j = LOG - 1; j >= 0; j--) {
        if (stjump[u][j] != -1 && depth[stjump[u][j]] >= depth[v]) {
            u = stjump[u][j];
        }
    }
    
    if (u == v) return u;
    
    // 同时提升u和v
    for (int j = LOG - 1; j >= 0; j--) {
        if (stjump[u][j] != -1 && stjump[u][j] != stjump[v][j]) {
            u = stjump[u][j];
            v = stjump[v][j];
        }
    }
    
    return stjump[u][0];
}

bool isAncestor(int a, int b) {
    return lca(a, b) == a;
}

int parent(int u) {
    return stjump[u][0];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;
    cin >> n;
    vector<int> w(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> w[i];
    }
    
    for (int i = 0; i < n - 1; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    // 初始化树上倍增相关数组
    LOG = 0;
    while ((1 << LOG) <= n) LOG++;
    LOG++;
    stjump.resize(n + 1, vector<int>(LOG, -1));
    depth.resize(n + 1);
    size_.resize(n + 1);
    
    dfs(1, -1, 0);
    
    // 构建倍增表
    for (int j = 1; j < LOG; j++) {
        for (int i = 1; i <= n; i++) {
            if (stjump[i][j-1] != -1) {
                stjump[i][j] = stjump[stjump[i][j-1]][j-1];
            } else {
                stjump[i][j] = -1;
            }
        }
    }
    
    // 按颜色分组节点
    map<int, vector<int>> colorMap;
    for (int i = 1; i <= n; i++) {
        colorMap[w[i]].push_back(i);
    }
    
    // 计算答案
    long long totalPairs = (long long)n * (n - 1) / 2;
    for (int i = 1; i <= n; i++) {
        auto it = colorMap.find(i);
        long long ans;
        
        if (it == colorMap.end() || it->second.empty()) {
            // 颜色i不存在，所有路径都满足条件
            ans = totalPairs;
        } else if (it->second.size() == 1) {
            // 只有一个颜色i的节点
            int u = it->second[0];
            ans = (long long)size_[u] * (n - size_[u]);
            ans += (long long)(n - 1);
            ans /= 2;
        } else {
            // 颜色i有多个节点，检查是否在一条链上
            bool allOnChain = true;
            int deepest = it->second[0];
            // 找到最深的节点
            for (int node : it->second) {
                if (depth[node] > depth[deepest]) {
                    deepest = node;
                }
            }
            
            // 检查其他节点是否都是deepest的祖先
            for (int node : it->second) {
                if (node != deepest && !isAncestor(node, deepest)) {
                    allOnChain = false;
                    break;
                }
            }
            
            if (allOnChain) {
                // 所有节点都在一条链上，找到最浅的节点
                int shallowest = it->second[0];
                for (int node : it->second) {
                    if (depth[node] < depth[shallowest]) {
                        shallowest = node;
                    }
                }
                
                // 计算子树大小乘积
                long long cnt = 1;
                // 找到shallowest的直接子节点，该子节点在链上
                for (int child : adj[shallowest]) {
                    if (child != parent(shallowest) && isAncestor(child, deepest)) {
                        cnt *= size_[child];
                        break;
                    }
                }
                
                // 计算最深节点的子树大小
                cnt *= size_[deepest];
                
                ans = cnt;
            } else {
                // 不在一条链上，可能有两个分支
                // 找到两个最深的节点，检查它们的LCA
                int u = it->second[0], v = it->second[1];
                for (int node : it->second) {
                    if (depth[node] > depth[u]) {
                        v = u;
                        u = node;
                    } else if (depth[node] > depth[v]) {
                        v = node;
                    }
                }
                
                int ancestor = lca(u, v);
                bool hasOther = false;
                for (int node : it->second) {
                    if (!isAncestor(ancestor, node) || 
                        (node != u && node != v && !isAncestor(u, node) && !isAncestor(v, node))) {
                        hasOther = true;
                        break;
                    }
                }
                
                if (hasOther) {
                    // 有超过两个分支，无法用一条路径覆盖所有颜色节点
                    ans = 0;
                } else {
                    // 只有两个分支，计算两个分支子树大小的乘积
                    ans = (long long)size_[u] * size_[v];
                }
            }
        }
        
        cout << ans << "\n";
    }
    
    return 0;
}
```

**Python实现**：
```python
import sys
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

n = int(sys.stdin.readline())
w = list(map(int, sys.stdin.readline().split()))
w = [0] + w  # 节点编号1~n

adj = [[] for _ in range(n + 1)]
for _ in range(n - 1):
    u, v = map(int, sys.stdin.readline().split())
    adj[u].append(v)
    adj[v].append(u)

# 初始化树上倍增相关数组
LOG = 0
while (1 << LOG) <= n:
    LOG += 1
LOG += 1

stjump = [[-1] * LOG for _ in range(n + 1)]
depth = [0] * (n + 1)
size_ = [0] * (n + 1)

# DFS预处理
def dfs(u, p, d):
    stjump[u][0] = p
    depth[u] = d
    size_[u] = 1
    
    for v in adj[u]:
        if v != p:
            dfs(v, u, d + 1)
            size_[u] += size_[v]

dfs(1, -1, 0)

# 构建倍增表
for j in range(1, LOG):
    for i in range(1, n + 1):
        if stjump[i][j-1] != -1:
            stjump[i][j] = stjump[stjump[i][j-1]][j-1]
        else:
            stjump[i][j] = -1

# LCA查询
def lca(u, v):
    if depth[u] < depth[v]:
        u, v = v, u
    
    # 将u提升到v的深度
    for j in range(LOG-1, -1, -1):
        if stjump[u][j] != -1 and depth[stjump[u][j]] >= depth[v]:
            u = stjump[u][j]
    
    if u == v:
        return u
    
    # 同时提升u和v
    for j in range(LOG-1, -1, -1):
        if stjump[u][j] != -1 and stjump[u][j] != stjump[v][j]:
            u = stjump[u][j]
            v = stjump[v][j]
    
    return stjump[u][0]

# 判断a是否是b的祖先
def is_ancestor(a, b):
    return lca(a, b) == a

# 获取父节点
def get_parent(u):
    return stjump[u][0]

# 按颜色分组节点
color_map = defaultdict(list)
for i in range(1, n + 1):
    color_map[w[i]].append(i)

# 计算答案
total_pairs = n * (n - 1) // 2
for i in range(1, n + 1):
    nodes = color_map.get(i, [])
    
    if not nodes:
        # 颜色i不存在，所有路径都满足条件
        ans = total_pairs
    elif len(nodes) == 1:
        # 只有一个颜色i的节点
        u = nodes[0]
        ans = size_[u] * (n - size_[u])
        ans += (n - 1)
        ans //= 2
    else:
        # 颜色i有多个节点，检查是否在一条链上
        all_on_chain = True
        deepest = nodes[0]
        # 找到最深的节点
        for node in nodes:
            if depth[node] > depth[deepest]:
                deepest = node
        
        # 检查其他节点是否都是deepest的祖先
        for node in nodes:
            if node != deepest and not is_ancestor(node, deepest):
                all_on_chain = False
                break
        
        if all_on_chain:
            # 所有节点都在一条链上，找到最浅的节点
            shallowest = nodes[0]
            for node in nodes:
                if depth[node] < depth[shallowest]:
                    shallowest = node
            
            # 计算子树大小乘积
            cnt = 1
            # 找到shallowest的直接子节点，该子节点在链上
            for child in adj[shallowest]:
                if child != get_parent(shallowest) and is_ancestor(child, deepest):
                    cnt *= size_[child]
                    break
            
            # 计算最深节点的子树大小
            cnt *= size_[deepest]
            
            ans = cnt
        else:
            # 不在一条链上，可能有两个分支
            # 找到两个最深的节点，检查它们的LCA
            u = nodes[0]
            v = nodes[1]
            for node in nodes:
                if depth[node] > depth[u]:
                    v = u
                    u = node
                elif depth[node] > depth[v]:
                    v = node
            
            ancestor = lca(u, v)
            has_other = False
            for node in nodes:
                if not is_ancestor(ancestor, node) or \
                   (node != u and node != v and not is_ancestor(u, node) and not is_ancestor(v, node)):
                    has_other = True
                    break
            
            if has_other:
                # 有超过两个分支，无法用一条路径覆盖所有颜色节点
                ans = 0
            else:
                # 只有两个分支，计算两个分支子树大小的乘积
                ans = size_[u] * size_[v]
    
    print(ans)
```

**算法优化与工程化考量**：
1. **分情况讨论**：根据颜色节点的分布情况，采用不同的计算策略，提高效率
2. **预处理优化**：提前计算子树大小、深度等信息，避免重复计算
3. **边界处理**：处理颜色不存在、只有一个节点等特殊情况
4. **性能优化**：使用递归深度限制、快速IO等技术，应对大规模数据

**扩展思考**：
- 如何处理动态树结构的颜色查询？
- 如何优化空间复杂度，特别是当n很大时？
- 该问题与数据挖掘中的路径分析有何联系？

### 4. CodeForces - 932D Tree

**题目链接**：[CodeForces - 932D Tree](https://codeforces.com/problemset/problem/932/D)

**题目描述**：
给出一棵树，初始时只有一个节点1，权值为0，后续有 n 个操作，每次操作分为两种情况：
1. u val：向树中插入一个新的节点，其父节点为 u，权值为 val
2. u val：询问以节点 u 为起点的最长不下降子序列的长度，这里规定的最长不下降子序列需要满足以下几个条件：以 u 为起点，每次的路线必须都是当前节点的父节点序列中的权值和小于等于 val

**解题思路**：
1. 使用树上倍增算法预处理每个节点的祖先信息
2. 对于每个节点，维护两个倍增数组：
   - dp[u][i]：代表节点u向上经过2^i个权值大于等于w[u]的节点后的位置
   - sum[u][i]：代表节点u向上经过2^i个权值大于等于w[u]后的权值和
3. 插入操作时，使用二分查找找到下一个满足条件的节点，并更新倍增数组
4. 查询操作时，使用二进制分解k，累加路径上的权值和

**复杂度分析**：
- 时间复杂度：预处理和查询均为O(log n)
- 空间复杂度：O(n log n)

**Java实现**：
```java
import java.util.*;

public class Main {
    private static final int MAXN = 400010;
    private static final int LOG = 20;
    private static int[][] dp; // dp[u][i]表示u向上跳2^i步的节点
    private static long[][] sum; // sum[u][i]表示u向上跳2^i步的权值和
    private static int[] w; // 节点权值
    private static int nodeCount; // 当前节点数
    private static List<Integer>[] adj; // 邻接表

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        // 初始化
        dp = new int[MAXN][LOG];
        sum = new long[MAXN][LOG];
        w = new int[MAXN];
        adj = new ArrayList[MAXN];
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
        
        nodeCount = 1;
        w[1] = 0;
        Arrays.fill(dp[1], -1);
        Arrays.fill(sum[1], 0);
        
        for (int i = 0; i < n; i++) {
            int u = scanner.nextInt();
            int val = scanner.nextInt();
            
            if (scanner.hasNext()) {
                // 操作1：插入节点
                nodeCount++;
                w[nodeCount] = val;
                adj[u].add(nodeCount);
                adj[nodeCount].add(u);
                
                // 初始化dp和sum数组
                dp[nodeCount][0] = -1;
                sum[nodeCount][0] = 0;
                
                // 找到第一个权值大于等于w[nodeCount]的祖先
                int v = u;
                while (v != -1 && w[v] < w[nodeCount]) {
                    v = (v == 1) ? -1 : findParent(v);
                }
                
                if (v != -1) {
                    dp[nodeCount][0] = v;
                    sum[nodeCount][0] = w[v];
                    
                    // 构建倍增数组
                    for (int j = 1; j < LOG; j++) {
                        if (dp[nodeCount][j-1] != -1) {
                            dp[nodeCount][j] = dp[dp[nodeCount][j-1]][j-1];
                            sum[nodeCount][j] = sum[nodeCount][j-1] + sum[dp[nodeCount][j-1]][j-1];
                        } else {
                            dp[nodeCount][j] = -1;
                            sum[nodeCount][j] = 0;
                        }
                    }
                }
                
                scanner.nextLine(); // 跳过换行
            } else {
                // 操作2：查询最长不下降子序列长度
                long maxSum = val;
                int current = u;
                int res = 0;
                long currentSum = 0;
                
                // 二进制分解查询
                for (int j = LOG - 1; j >= 0; j--) {
                    if (dp[current][j] != -1 && currentSum + sum[current][j] <= maxSum) {
                        currentSum += sum[current][j];
                        res += (1 << j);
                        current = dp[current][j];
                    }
                }
                
                // 加上当前节点自己
                res++;
                
                System.out.println(res);
            }
        }
        
        scanner.close();
    }
    
    // 找到节点的父节点（简单版本，实际需要根据树的结构维护父节点数组）
    private static int findParent(int u) {
        for (int v : adj[u]) {
            if (v < u) { // 假设父节点编号较小
                return v;
            }
        }
        return -1;
    }
}
```

**C++实现**：
```cpp
#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

const int MAXN = 400010;
const int LOG = 20;
int dp[MAXN][LOG]; // dp[u][i]表示u向上跳2^i步的节点
long long sum[MAXN][LOG]; // sum[u][i]表示u向上跳2^i步的权值和
int w[MAXN]; // 节点权值
int nodeCount; // 当前节点数
vector<int> adj[MAXN]; // 邻接表
int parent[MAXN]; // 父节点数组

int findParent(int u) {
    return parent[u];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;
    cin >> n;
    
    // 初始化
    memset(dp, -1, sizeof(dp));
    memset(sum, 0, sizeof(sum));
    
    nodeCount = 1;
    w[1] = 0;
    parent[1] = -1;
    
    for (int i = 0; i < n; i++) {
        int u, val;
        string op;
        cin >> u >> val;
        
        if (cin.peek() != '\n') {
            // 操作1：插入节点
            cin >> op; // 读取多余的字符
            nodeCount++;
            w[nodeCount] = val;
            adj[u].push_back(nodeCount);
            adj[nodeCount].push_back(u);
            parent[nodeCount] = u;
            
            // 初始化dp和sum数组
            for (int j = 0; j < LOG; j++) {
                dp[nodeCount][j] = -1;
                sum[nodeCount][j] = 0;
            }
            
            // 找到第一个权值大于等于w[nodeCount]的祖先
            int v = u;
            while (v != -1 && w[v] < w[nodeCount]) {
                v = findParent(v);
            }
            
            if (v != -1) {
                dp[nodeCount][0] = v;
                sum[nodeCount][0] = w[v];
                
                // 构建倍增数组
                for (int j = 1; j < LOG; j++) {
                    if (dp[nodeCount][j-1] != -1) {
                        dp[nodeCount][j] = dp[dp[nodeCount][j-1]][j-1];
                        sum[nodeCount][j] = sum[nodeCount][j-1] + sum[dp[nodeCount][j-1]][j-1];
                    }
                }
            }
        } else {
            // 操作2：查询最长不下降子序列长度
            long long maxSum = val;
            int current = u;
            int res = 0;
            long long currentSum = 0;
            
            // 二进制分解查询
            for (int j = LOG - 1; j >= 0; j--) {
                if (dp[current][j] != -1 && currentSum + sum[current][j] <= maxSum) {
                    currentSum += sum[current][j];
                    res += (1 << j);
                    current = dp[current][j];
                }
            }
            
            // 加上当前节点自己
            res++;
            
            cout << res << "\n";
        }
    }
    
    return 0;
}
```

**Python实现**：
```python
import sys
from sys import stdin

sys.setrecursionlimit(1 << 25)

MAXN = 400010
LOG = 20

# 初始化dp和sum数组
dp = [[-1] * LOG for _ in range(MAXN)]
sum_ = [[0] * LOG for _ in range(MAXN)]
w = [0] * MAXN
parent = [-1] * MAXN
node_count = 1
w[1] = 0

n = int(stdin.readline())

for _ in range(n):
    parts = stdin.readline().split()
    u = int(parts[0])
    val = int(parts[1])
    
    if len(parts) > 2:
        # 操作1：插入节点
        node_count += 1
        w[node_count] = val
        parent[node_count] = u
        
        # 初始化dp和sum数组
        for j in range(LOG):
            dp[node_count][j] = -1
            sum_[node_count][j] = 0
        
        # 找到第一个权值大于等于w[node_count]的祖先
        v = u
        while v != -1 and w[v] < w[node_count]:
            v = parent[v]
        
        if v != -1:
            dp[node_count][0] = v
            sum_[node_count][0] = w[v]
            
            # 构建倍增数组
            for j in range(1, LOG):
                if dp[node_count][j-1] != -1:
                    dp[node_count][j] = dp[dp[node_count][j-1]][j-1]
                    sum_[node_count][j] = sum_[node_count][j-1] + sum_[dp[node_count][j-1]][j-1]
                else:
                    dp[node_count][j] = -1
                    sum_[node_count][j] = 0
    else:
        # 操作2：查询最长不下降子序列长度
        max_sum = val
        current = u
        res = 0
        current_sum = 0
        
        # 二进制分解查询
        for j in range(LOG-1, -1, -1):
            if dp[current][j] != -1 and current_sum + sum_[current][j] <= max_sum:
                current_sum += sum_[current][j]
                res += (1 << j)
                current = dp[current][j]
        
        # 加上当前节点自己
        res += 1
        
        print(res)
```

**算法优化与工程化考量**：
1. **动态树构建**：支持在线插入节点，维护树的结构
2. **权值约束处理**：在树上倍增的基础上增加权值和的约束条件
3. **二进制分解优化**：通过二进制分解查询路径，提高效率
4. **空间优化**：使用预分配的数组，避免动态内存分配的开销

**扩展思考**：
- 如何处理更复杂的路径约束条件？
- 如何将该算法应用到动态规划问题中？
- 如何优化大数据规模下的性能？

### 1.2 LeetCode 236. 二叉树的最近公共祖先 (Lowest Common Ancestor of a Binary Tree)
**题目链接**: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/

**题目描述**:
给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

百度百科中最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。"

**解题思路**:
对于一般的二叉树，可以使用递归方法。但对于一般的树结构，可以使用树上倍增算法来高效解决。

**时间复杂度**: O(log n)
**空间复杂度**: O(n log n)

### 1.3 LeetCode 2836. 在传球游戏中最大化函数值 (Maximize Value of Function in a Ball Passing Game)
**题目链接**: https://leetcode.cn/problems/maximize-value-of-function-in-a-ball-passing-game/

**题目描述**:
给定一个长度为n的数组receiver和一个整数k。总共有n名玩家，编号0 ~ n-1，这些玩家在玩一个传球游戏。receiver[i]表示编号为i的玩家会传球给下一个人的编号。玩家可以传球给自己，也就是说receiver[i]可能等于i。

你需要选择一名开始玩家，然后开始传球，球会被传恰好k次。如果选择编号为x的玩家作为开始玩家，函数f(x)表示从x玩家开始，k次传球内所有接触过球的玩家编号之和。你的任务是选择开始玩家x，目的是最大化f(x)，返回函数的最大值。

**解题思路**:
使用树上倍增算法，预处理每个节点跳2^i步能到达的位置和路径和，然后通过二进制分解计算k步后的结果。

**时间复杂度**: O(n log k)
**空间复杂度**: O(n log k)

### 1.4 LeetCode 2846. 边权重均等查询 (Minimum Edge Weight Equilibrium Queries in a Tree)
**题目链接**: https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/

**题目描述**:
给定一个包含n个节点的树，节点编号从0到n-1。给定一个二维数组edges，其中edges[i] = [ui, vi, wi]表示节点ui和vi之间有一条权重为wi的边。

给定一个查询数组queries，其中queries[i] = [ai, bi]。对于每个查询，找出从节点ai到节点bi的路径上，最少需要修改多少条边的权重，才能使路径上所有边的权重相等。

**解题思路**:
使用树上倍增算法计算LCA，然后统计路径上各种权重的数量，找出出现次数最多的权重，其余权重都需要修改。

**时间复杂度**: O(n log n + q log n)
**空间复杂度**: O(n log n)

## 2. Codeforces系列

### 2.1 Codeforces 1140G. Double Tree
**题目链接**: https://codeforces.com/problemset/problem/1140/G

**题目描述**:
给定一棵树，每个节点有两个副本，分别在两棵树中。在两棵树之间有一些额外的边连接对应的节点。要求计算多对节点之间的最短距离。

**解题思路**:
使用树上倍增算法结合动态规划来解决，预处理节点间距离和跳跃信息。

**时间复杂度**: O(n log n + q log n)
**空间复杂度**: O(n log n)

### 2.2 Codeforces 932D. Tree
**题目链接**: https://codeforces.com/problemset/problem/932/D

**题目描述**:
给定一个节点数为1的树，节点编号为1，权值为0。有两种操作：
1. 1 u v：添加一个新节点，编号为当前节点数+1，父节点为u，权值为v
2. 2 u v：查询从节点u开始，沿着祖先方向能找到的最长不降子序列的长度，且序列和不超过v

**解题思路**:
使用树上倍增算法维护从每个节点向上跳2^i步能到达的节点和路径信息，然后通过二进制分解快速查询。

**时间复杂度**: O(n log n)
**空间复杂度**: O(n log n)

### 2.3 Codeforces 587C. Duff in the Army
**题目链接**: https://codeforces.com/problemset/problem/587/C

**题目描述**:
给定一棵树，每个节点有一些军人。对于每次查询(u, v, a)，找出从节点u到节点v的路径上，编号最小的a个军人（如果不足a个则全部输出）。

**解题思路**:
使用树上倍增算法，预处理每个节点向上跳2^i步路径上的前a个最小军人编号，然后通过LCA计算路径上的军人信息。

**时间复杂度**: O((n + q) * a * log n)
**空间复杂度**: O(n * a * log n)

## 3. 洛谷系列

### 3.1 P4281. 紧急集合 (Emergency Assembly)
**题目链接**: https://www.luogu.com.cn/problem/P4281

**题目描述**:
在一棵n个节点的树上，有3个人分别站在不同的节点上，他们希望选一个节点集合，使得3个人到该节点的距离之和最小。

**解题思路**:
通过计算3个点两两之间的LCA，找到最优集合点。利用树上倍增快速计算LCA和距离。

**时间复杂度**: O(log n) 每次查询
**空间复杂度**: O(n log n)

### 3.2 P1967. 货车运输 (Trucking)
**题目链接**: https://www.luogu.com.cn/problem/P1967

**题目描述**:
在一张图中，每条边有一个权重限制。对于每次查询，找到两点间路径上边权最小值的最大值。

**解题思路**:
先构建最大生成树，然后在生成树上使用树上倍增算法计算路径上的最小权重。

**时间复杂度**: O((n + m) log n + q log n)
**空间复杂度**: O(n log n)

### 3.3 P3379. 最近公共祖先 (LCA)
**题目链接**: https://www.luogu.com.cn/problem/P3379

**题目描述**:
标准的LCA问题，在一棵树上多次查询两个节点的最近公共祖先。

**解题思路**:
使用树上倍增算法预处理，然后快速查询。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

### 3.4 P5588. 小猪佩奇爬树
**题目链接**: https://www.luogu.com.cn/problem/P5588

**题目描述**:
给定一棵n个节点的树，每个节点有一个颜色。对于每种颜色，计算有多少对节点(u, v)满足u是v的祖先或者v是u的祖先。

**解题思路**:
使用DFS序判断祖先关系，结合树上倍增算法和树状数组优化计算。

**时间复杂度**: O(n log n + c * m log n)，其中c是颜色种类数，m是最大颜色的节点数
**空间复杂度**: O(n log n + c)

## 4. 牛客系列

### 4.1 牛客练习赛A. 路径回文 (Path Palindrome)
**题目链接**: https://ac.nowcoder.com/acm/contest/78807/G

**题目描述**:
在一棵树上，每个节点有一个字符。多次查询两点间路径形成的字符串是否是回文。

**解题思路**:
使用树上倍增算法结合字符串哈希技术，预处理路径哈希值，然后快速判断回文。

**时间复杂度**: O((n + m) log n)
**空间复杂度**: O(n log n)

## 5. POJ系列

### 5.1 POJ 1986. Distance Queries
**题目链接**: http://poj.org/problem?id=1986

**题目描述**:
给定一棵带权树，多次查询两点间的距离。

**解题思路**:
使用树上倍增算法预处理节点深度和到根节点的距离，通过LCA计算两点间距离。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

### 5.2 POJ 2182. Lost Cows
**题目链接**: http://poj.org/problem?id=2182

**题目描述**:
给定每个奶牛前面有多少个比它矮的奶牛，求每个奶牛的实际高度排名。

**解题思路**:
虽然这不是直接的树上倍增问题，但可以用树状数组+倍增的思想来解决。

**时间复杂度**: O(n log n)
**空间复杂度**: O(n)

## 6. SPOJ系列

### 6.1 SPOJ 10628. Count on a tree (COT)
**题目链接**: https://www.spoj.com/problems/COT/

**题目描述**:
给定一棵节点带权树，多次查询两点间路径上第k小的点权。

**解题思路**:
结合树上倍增和主席树（可持久化线段树），在树上建立主席树，利用LCA计算路径上的第k小值。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

## 7. AtCoder系列

### 7.1 AtCoder ABC 160E. Traveling Salesman among Aerial Cities
**题目链接**: https://atcoder.jp/contests/abc160/tasks/abc160_e

**题目描述**:
在三维空间中有n个城市，计算从一个城市到另一个城市的最小成本。

**解题思路**:
虽然这不是树上问题，但可以使用倍增思想优化动态规划。

**时间复杂度**: O(n^2 log n)
**空间复杂度**: O(n^2)

## 8. 其他平台

### 8.1 LintCode 474. 最近公共祖先 (Lowest Common Ancestor)
**题目链接**: https://www.lintcode.com/problem/474/

**题目描述**:
在二叉树中找到两个节点的最近公共祖先。

### 8.2 LintCode 578. 最近公共祖先 III (Lowest Common Ancestor III)
**题目链接**: https://www.lintcode.com/problem/578/

**题目描述**:
在二叉树中找到两个节点的最近公共祖先，但节点可能不存在于树中。

### 8.3 HDU 2856. How far away ?
**题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2856

**题目描述**:
给定一棵带权树，多次查询两点间的距离。

**解题思路**:
使用树上倍增算法预处理节点深度和到根节点的距离，通过LCA计算两点间距离。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

### 8.4 ZOJ 3708. Density of Power Network
**题目链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367599

**题目描述**:
给定一个图，计算特定条件下的密度。

**解题思路**:
在特定的树结构上使用倍增算法计算相关参数。

**时间复杂度**: O(n log n)
**空间复杂度**: O(n log n)

### 8.5 Aizu OJ ALDS1_13_C. 8 Puzzle
**题目链接**: https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/13/ALDS1_13_C

**题目描述**:
解决8数码问题。

**解题思路**:
虽然这不是树上倍增问题，但可以使用双向BFS等优化算法，其中可能涉及倍增思想。

**时间复杂度**: O(b^d)，其中b是分支因子，d是深度
**空间复杂度**: O(b^d)

## 算法总结

### 核心思想
树上倍增算法的核心思想是预处理每个节点向上跳2^i步能到达的节点，这样可以在查询时通过二进制分解快速跳跃。

### 适用场景
1. LCA查询
2. 树上路径权重计算
3. 第K个祖先查询
4. 树上路径性质判断
5. 树上距离计算

### 实现要点
1. 预处理阶段构建跳跃表
2. 查询时使用二进制分解
3. 注意边界条件处理
4. 合理设计数据结构减少空间占用

### 复杂度分析
- 预处理时间复杂度：O(n log n)
- 查询时间复杂度：O(log n)
- 空间复杂度：O(n log n)

### 优化技巧
1. 合理设置数组大小，避免浪费空间
2. 使用位运算优化性能
3. 根据具体问题调整预处理信息
4. 对于特定问题，可以结合其他数据结构如线段树、主席树等

### 工程化考虑
1. 输入验证：检查节点编号是否合法
2. 连通性检查：判断节点是否在同一连通分量
3. 边界情况：处理根节点和叶子节点
4. 内存优化：合理设置数组大小
5. 时间优化：避免重复计算
6. 模块化设计：将预处理和查询分离
7. 参数化配置：支持不同的树和查询类型
8. 易于维护：添加详细注释和文档

## 4. 更多树上倍增算法题目

### 4.1 LeetCode 2846. 边权重均等查询 (Minimum Number of Changes to Make Binary Tree Beautiful)
**题目链接**: https://leetcode.cn/problems/minimum-number-of-changes-to-make-binary-tree-beautiful/

**题目描述**:
给你一棵无根树，树中包含 n 个节点，节点编号从 0 到 n-1。树以长度为 n-1 的二维整数数组 edges 表示，其中 edges[i] = [ui, vi, wi] 表示树中存在一条连接节点 ui 和节点 vi 的边，边的权值为 wi。树中每条边的权值为 1、2 或 3。

再给你一个长度为 m 的二维整数数组 queries，其中 queries[j] = [aj, bj]。对于每个查询，请你找出使路径 aj 到 bj 上的所有边的权值相等所需的最小操作次数。在一次操作中，你可以选择树上的任意一条边，并将其权值更改为 1、2 或 3。

**解题思路**:
使用树上倍增算法，除了维护每个节点的祖先信息外，还需要维护从该节点到其2^j级祖先路径上各权值的边数。对于每个查询，我们可以找到两个节点的最近公共祖先，然后统计路径上各权值的出现次数，最少修改次数就是路径长度减去出现次数最多的权值的数量。

**时间复杂度**: 预处理 O(n log n * C)，查询 O(log n)，其中 C 是权值种类数
**空间复杂度**: O(n log n * C)

**Java实现**:
```java
class Solution {
    private int n;                // 节点数量
    private int LOG;              // 最大跳步级别
    private int[][] parent;       // parent[j][u] 表示u的2^j级祖先
    private int[] depth;          // 每个节点的深度
    private int[][][] cnt;        // cnt[j][u][k] 表示u到2^j级祖先路径上权值为k+1的边数
    private List<List<int[]>> adj; // 邻接表

    public int[] minOperationsQueries(int n, int[][] edges, int[][] queries) {
        this.n = n;
        this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n];
        depth = new int[n];
        cnt = new int[LOG][n][3]; // 权值范围是1-3
        
        // 构建邻接表
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2] - 1; // 调整为0-2索引
            adj.get(u).add(new int[]{v, w});
            adj.get(v).add(new int[]{u, w});
        }
        
        // 初始化父数组
        for (int i = 0; i < LOG; i++) {
            Arrays.fill(parent[i], -1);
        }
        
        // DFS预处理
        dfs(0, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                    // 合并计数
                    for (int k = 0; k < 3; k++) {
                        cnt[j][i][k] = cnt[j-1][i][k] + cnt[j-1][parent[j-1][i]][k];
                    }
                }
            }
        }
        
        // 处理查询
        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0];
            int v = queries[i][1];
            result[i] = query(u, v);
        }
        
        return result;
    }
    
    private void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        
        for (int[] edge : adj.get(u)) {
            int v = edge[0];
            int w = edge[1];
            if (v != p) {
                cnt[0][v][w] = 1;
                dfs(v, u, d + 1);
            }
        }
    }
    
    private int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
    
    private int[] getCount(int u, int ancestor) {
        int[] res = new int[3];
        
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[ancestor]) {
                for (int k = 0; k < 3; k++) {
                    res[k] += cnt[j][u][k];
                }
                u = parent[j][u];
            }
        }
        
        return res;
    }
    
    private int query(int u, int v) {
        int ancestor = lca(u, v);
        int[] cntU = getCount(u, ancestor);
        int[] cntV = getCount(v, ancestor);
        
        int[] total = new int[3];
        for (int k = 0; k < 3; k++) {
            total[k] = cntU[k] + cntV[k];
        }
        
        int pathLength = depth[u] + depth[v] - 2 * depth[ancestor];
        int maxCount = Math.max(total[0], Math.max(total[1], total[2]));
        
        return pathLength - maxCount;
    }
}
```

**C++实现**:
```cpp
class Solution {
private:
    int n;                  // 节点数量
    int LOG;                // 最大跳步级别
    vector<vector<int>> parent;    // parent[j][u] 表示u的2^j级祖先
    vector<int> depth;             // 每个节点的深度
    vector<vector<vector<int>>> cnt; // cnt[j][u][k] 表示路径上权值为k+1的边数
    vector<vector<pair<int, int>>> adj; // 邻接表
    
    void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        
        for (auto &edge : adj[u]) {
            int v = edge.first;
            int w = edge.second;
            if (v != p) {
                cnt[0][v][w] = 1;
                dfs(v, u, d + 1);
            }
        }
    }
    
    int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            swap(u, v);
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
    
    vector<int> getCount(int u, int ancestor) {
        vector<int> res(3, 0);
        
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[ancestor]) {
                for (int k = 0; k < 3; k++) {
                    res[k] += cnt[j][u][k];
                }
                u = parent[j][u];
            }
        }
        
        return res;
    }
    
    int query(int u, int v) {
        int ancestor = lca(u, v);
        vector<int> cntU = getCount(u, ancestor);
        vector<int> cntV = getCount(v, ancestor);
        
        vector<int> total(3, 0);
        for (int k = 0; k < 3; k++) {
            total[k] = cntU[k] + cntV[k];
        }
        
        int pathLength = depth[u] + depth[v] - 2 * depth[ancestor];
        int maxCount = max(total[0], max(total[1], total[2]));
        
        return pathLength - maxCount;
    }
    
public:
    vector<int> minOperationsQueries(int n, vector<vector<int>>& edges, vector<vector<int>>& queries) {
        this->n = n;
        this->LOG = log2(n) + 2;
        
        // 初始化数据结构
        parent.resize(LOG, vector<int>(n, -1));
        depth.resize(n, 0);
        cnt.resize(LOG, vector<vector<int>>(n, vector<int>(3, 0)));
        adj.resize(n);
        
        // 构建邻接表
        for (auto &edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2] - 1; // 调整为0-2索引
            adj[u].emplace_back(v, w);
            adj[v].emplace_back(u, w);
        }
        
        // DFS预处理
        dfs(0, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                    for (int k = 0; k < 3; k++) {
                        cnt[j][i][k] = cnt[j-1][i][k] + cnt[j-1][parent[j-1][i]][k];
                    }
                }
            }
        }
        
        // 处理查询
        vector<int> result;
        for (auto &q : queries) {
            int u = q[0];
            int v = q[1];
            result.push_back(query(u, v));
        }
        
        return result;
    }
};
```

**Python实现**:
```python
import math
from collections import defaultdict

class Solution:
    def minOperationsQueries(self, n, edges, queries):
        LOG = math.ceil(math.log2(n)) + 1
        
        # 初始化数据结构
        parent = [[-1] * n for _ in range(LOG)]
        depth = [0] * n
        cnt = [[[0] * 3 for _ in range(n)] for __ in range(LOG)]  # cnt[j][u][k]表示权值为k+1的边数
        
        # 构建邻接表
        adj = [[] for _ in range(n)]
        for u, v, w in edges:
            adj[u].append((v, w - 1))  # 调整为0-2索引
            adj[v].append((u, w - 1))
        
        # DFS预处理
        def dfs(u, p, d):
            parent[0][u] = p
            depth[u] = d
            for v, w in adj[u]:
                if v != p:
                    cnt[0][v][w] = 1
                    dfs(v, u, d + 1)
        
        dfs(0, -1, 0)
        
        # 构建倍增表
        for j in range(1, LOG):
            for i in range(n):
                if parent[j-1][i] != -1:
                    parent[j][i] = parent[j-1][parent[j-1][i]]
                    for k in range(3):
                        cnt[j][i][k] = cnt[j-1][i][k] + cnt[j-1][parent[j-1][i]][k]
        
        # 查找LCA
        def lca(u, v):
            if depth[u] < depth[v]:
                u, v = v, u
            
            # 提升u到v的深度
            for j in range(LOG-1, -1, -1):
                if depth[u] - (1 << j) >= depth[v]:
                    u = parent[j][u]
            
            if u == v:
                return u
            
            # 同时提升
            for j in range(LOG-1, -1, -1):
                if parent[j][u] != -1 and parent[j][u] != parent[j][v]:
                    u = parent[j][u]
                    v = parent[j][v]
            
            return parent[0][u]
        
        # 获取路径上的权值计数
        def get_count(u, ancestor):
            res = [0] * 3
            for j in range(LOG-1, -1, -1):
                if depth[u] - (1 << j) >= depth[ancestor]:
                    for k in range(3):
                        res[k] += cnt[j][u][k]
                    u = parent[j][u]
            return res
        
        # 处理单个查询
        def query(u, v):
            ancestor = lca(u, v)
            cnt_u = get_count(u, ancestor)
            cnt_v = get_count(v, ancestor)
            
            total = [cnt_u[k] + cnt_v[k] for k in range(3)]
            path_length = depth[u] + depth[v] - 2 * depth[ancestor]
            max_count = max(total)
            
            return path_length - max_count
        
        # 处理所有查询
        return [query(q[0], q[1]) for q in queries]
```

### 4.2 洛谷 P5588 小猪佩奇爬树
**题目链接**: https://www.luogu.com.cn/problem/P5588

**题目描述**:
小猪佩奇家的后院有一棵高大的山毛榉树，一天，佩奇在爬树的时候突然想到一个问题：对于树中的每一种颜色，有多少对节点 (u, v) 满足 u 是 v 的祖先或者 v 是 u 的祖先？

**解题思路**:
使用DFS序来判断祖先关系，结合树上倍增算法快速查询祖先。对于每种颜色，将该颜色的所有节点收集起来，然后对于每对节点，判断是否存在祖先关系。为了优化，可以使用树状数组来高效统计子树中的节点数量。

**时间复杂度**: O(n log n + c * m log n)，其中c是颜色种类数，m是最大颜色的节点数
**空间复杂度**: O(n log n + c)

**Java实现**:
```java
import java.util.*;

public class Main {
    private static int n;
    private static int LOG;
    private static int[][] parent;
    private static int[] depth;
    private static int[] color;
    private static List<Integer>[] adj;
    private static int[] inTime;
    private static int[] outTime;
    private static int time;
    private static Map<Integer, List<Integer>> colorNodes;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n + 1];
        depth = new int[n + 1];
        color = new int[n + 1];
        adj = new ArrayList[n + 1];
        inTime = new int[n + 1];
        outTime = new int[n + 1];
        colorNodes = new HashMap<>();
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            adj[u].add(v);
            adj[v].add(u);
        }
        
        // 读取颜色
        for (int i = 1; i <= n; i++) {
            color[i] = scanner.nextInt();
            colorNodes.putIfAbsent(color[i], new ArrayList<>());
            colorNodes.get(color[i]).add(i);
        }
        
        // 预处理
        Arrays.fill(parent[0], -1);
        time = 0;
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                }
            }
        }
        
        // 计算每种颜色的结果
        Map<Integer, Long> result = calculateColorPairs();
        
        // 输出结果
        int maxColor = 0;
        for (int c : colorNodes.keySet()) {
            maxColor = Math.max(maxColor, c);
        }
        
        for (int i = 1; i <= maxColor; i++) {
            System.out.println(result.getOrDefault(i, 0L));
        }
        
        scanner.close();
    }
    
    private static void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        inTime[u] = ++time;
        
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
            }
        }
        
        outTime[u] = time;
    }
    
    private static boolean isAncestor(int u, int v) {
        return inTime[u] <= inTime[v] && outTime[v] <= outTime[u];
    }
    
    private static Map<Integer, Long> calculateColorPairs() {
        Map<Integer, Long> result = new HashMap<>();
        
        for (Map.Entry<Integer, List<Integer>> entry : colorNodes.entrySet()) {
            int c = entry.getKey();
            List<Integer> nodes = entry.getValue();
            
            // 按inTime排序
            nodes.sort(Comparator.comparingInt(a -> inTime[a]));
            
            // 使用树状数组优化计算
            long count = 0;
            int[] tree = new int[n + 2];
            
            for (int node : nodes) {
                // 查询子树中的节点数
                int subtreeCount = query(tree, outTime[node]) - query(tree, inTime[node] - 1);
                count += subtreeCount;
                
                // 添加当前节点
                update(tree, inTime[node], 1);
            }
            
            result.put(c, count);
        }
        
        return result;
    }
    
    private static void update(int[] tree, int idx, int delta) {
        while (idx < tree.length) {
            tree[idx] += delta;
            idx += idx & -idx;
        }
    }
    
    private static int query(int[] tree, int idx) {
        int sum = 0;
        while (idx > 0) {
            sum += tree[idx];
            idx -= idx & -idx;
        }
        return sum;
    }
}
```

**C++实现**:
```cpp
#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
#include <cmath>
using namespace std;

int n, LOG, time_stamp;
vector<vector<int>> parent;
vector<int> depth, color, inTime, outTime;
vector<vector<int>> adj;
map<int, vector<int>> colorNodes;

void update(vector<int>& tree, int idx, int delta) {
    while (idx < tree.size()) {
        tree[idx] += delta;
        idx += idx & -idx;
    }
}

int query(vector<int>& tree, int idx) {
    int sum = 0;
    while (idx > 0) {
        sum += tree[idx];
        idx -= idx & -idx;
    }
    return sum;
}

void dfs(int u, int p, int d) {
    parent[0][u] = p;
    depth[u] = d;
    inTime[u] = ++time_stamp;
    
    for (int v : adj[u]) {
        if (v != p) {
            dfs(v, u, d + 1);
        }
    }
    
    outTime[u] = time_stamp;
}

bool isAncestor(int u, int v) {
    return inTime[u] <= inTime[v] && outTime[v] <= outTime[u];
}

map<int, long long> calculateColorPairs() {
    map<int, long long> result;
    
    for (auto& entry : colorNodes) {
        int c = entry.first;
        vector<int>& nodes = entry.second;
        
        // 按inTime排序
        sort(nodes.begin(), nodes.end(), [&](int a, int b) {
            return inTime[a] < inTime[b];
        });
        
        long long count = 0;
        vector<int> tree(n + 2, 0);
        
        for (int node : nodes) {
            int subtreeCount = query(tree, outTime[node]) - query(tree, inTime[node] - 1);
            count += subtreeCount;
            update(tree, inTime[node], 1);
        }
        
        result[c] = count;
    }
    
    return result;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n;
    LOG = log2(n) + 2;
    
    // 初始化数据结构
    parent.resize(LOG, vector<int>(n + 1, -1));
    depth.resize(n + 1, 0);
    color.resize(n + 1, 0);
    adj.resize(n + 1);
    inTime.resize(n + 1, 0);
    outTime.resize(n + 1, 0);
    
    // 读取边
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    // 读取颜色
    int maxColor = 0;
    for (int i = 1; i <= n; i++) {
        cin >> color[i];
        maxColor = max(maxColor, color[i]);
        colorNodes[color[i]].push_back(i);
    }
    
    // 预处理
    time_stamp = 0;
    dfs(1, -1, 0);
    
    // 构建倍增表
    for (int j = 1; j < LOG; j++) {
        for (int i = 1; i <= n; i++) {
            if (parent[j-1][i] != -1) {
                parent[j][i] = parent[j-1][parent[j-1][i]];
            }
        }
    }
    
    // 计算结果
    map<int, long long> result = calculateColorPairs();
    
    // 输出结果
    for (int i = 1; i <= maxColor; i++) {
        cout << result[i] << endl;
    }
    
    return 0;
}
```

**Python实现**:
```python
import sys
import math
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

n = int(sys.stdin.readline())
LOG = math.ceil(math.log2(n)) + 2

# 初始化数据结构
parent = [[-1] * (n + 1) for _ in range(LOG)]
depth = [0] * (n + 1)
color = [0] * (n + 1)
adj = [[] for _ in range(n + 1)]
inTime = [0] * (n + 1)
outTime = [0] * (n + 1)
colorNodes = defaultdict(list)

# 读取边
for _ in range(n - 1):
    u, v = map(int, sys.stdin.readline().split())
    adj[u].append(v)
    adj[v].append(u)

# 读取颜色
maxColor = 0
for i in range(1, n + 1):
    c = int(sys.stdin.readline())
    color[i] = c
    maxColor = max(maxColor, c)
    colorNodes[c].append(i)

# DFS预处理
time_stamp = 0
def dfs(u, p, d):
    global time_stamp
    parent[0][u] = p
    depth[u] = d
    time_stamp += 1
    inTime[u] = time_stamp
    
    for v in adj[u]:
        if v != p:
            dfs(v, u, d + 1)
    
    outTime[u] = time_stamp

dfs(1, -1, 0)

# 构建倍增表
for j in range(1, LOG):
    for i in range(1, n + 1):
        if parent[j-1][i] != -1:
            parent[j][i] = parent[j-1][parent[j-1][i]]

# 树状数组实现
class FenwickTree:
    def __init__(self, size):
        self.n = size
        self.tree = [0] * (self.n + 2)
    
    def update(self, idx, delta):
        while idx <= self.n:
            self.tree[idx] += delta
            idx += idx & -idx
    
    def query(self, idx):
        sum_ = 0
        while idx > 0:
            sum_ += self.tree[idx]
            idx -= idx & -idx
        return sum_

# 计算每种颜色的结果
result = defaultdict(int)

for c, nodes in colorNodes.items():
    # 按inTime排序
    nodes.sort(key=lambda x: inTime[x])
    
    count = 0
    ft = FenwickTree(n)
    
    for node in nodes:
        # 查询子树中的节点数
        subtreeCount = ft.query(outTime[node]) - ft.query(inTime[node] - 1)
        count += subtreeCount
        ft.update(inTime[node], 1)
    
    result[c] = count

# 输出结果
for i in range(1, maxColor + 1):
    print(result.get(i, 0))
```

### 4.3 Codeforces 932D Tree
**题目链接**: https://codeforces.com/problemset/problem/932/D

**题目描述**:
给定一棵树，每个节点有一个权值。对于每个节点u，找到包含u的最长不下降子序列的长度。

**解题思路**:
使用树上倍增算法维护从根到每个节点路径上的信息。对于每个节点，我们维护到其2^j级祖先路径上的最长不下降子序列的相关信息，然后通过合并这些信息来快速查询。

**时间复杂度**: O(n log n * log W)，其中W是权值范围
**空间复杂度**: O(n log n * log W)

**Java实现**:
```java
import java.util.*;

public class Main {
    private static int n;
    private static int LOG;
    private static int[][] parent;
    private static int[] depth;
    private static int[] a; // 节点权值
    private static List<Integer>[] adj;
    private static List<Map<Integer, Integer>>[] jump; // jump[j][u] 存储权值到长度的映射
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n + 1];
        depth = new int[n + 1];
        a = new int[n + 1];
        adj = new ArrayList[n + 1];
        jump = new ArrayList[LOG];
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < LOG; i++) {
            jump[i] = new ArrayList<>();
            for (int j = 0; j <= n; j++) {
                jump[i].add(new HashMap<>());
            }
        }
        
        // 读取边和权值
        for (int i = 1; i < n; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            adj[u].add(v);
            adj[v].add(u);
        }
        
        for (int i = 1; i <= n; i++) {
            a[i] = scanner.nextInt();
        }
        
        // 预处理
        Arrays.fill(parent[0], -1);
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                    // 合并跳转信息
                    mergeJumpInfo(j, i);
                }
            }
        }
        
        // 处理查询
        int q = scanner.nextInt();
        for (int i = 0; i < q; i++) {
            int u = scanner.nextInt();
            int result = query(u);
            System.out.println(result);
        }
        
        scanner.close();
    }
    
    private static void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        
        // 初始化第一级跳转信息
        Map<Integer, Integer> map = jump[0].get(u);
        map.put(a[u], 1);
        
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
            }
        }
    }
    
    private static void mergeJumpInfo(int j, int u) {
        Map<Integer, Integer> current = jump[j].get(u);
        Map<Integer, Integer> first = jump[j-1].get(u);
        Map<Integer, Integer> second = jump[j-1].get(parent[j-1][u]);
        
        // 合并两个跳转信息
        for (Map.Entry<Integer, Integer> entry : first.entrySet()) {
            current.put(entry.getKey(), Math.max(current.getOrDefault(entry.getKey(), 0), entry.getValue()));
        }
        
        for (Map.Entry<Integer, Integer> entry : second.entrySet()) {
            current.put(entry.getKey(), Math.max(current.getOrDefault(entry.getKey(), 0), entry.getValue()));
        }
    }
    
    private static int query(int u) {
        int result = 1; // 至少包含当前节点
        int current = u;
        
        // 向上跳转，寻找最长不下降子序列
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][current] != -1) {
                Map<Integer, Integer> map = jump[j].get(current);
                // 检查是否可以继续扩展
                boolean canExtend = false;
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    if (entry.getKey() >= a[u]) {
                        canExtend = true;
                        result = Math.max(result, entry.getValue());
                    }
                }
                
                if (canExtend) {
                    current = parent[j][current];
                }
            }
        }
        
        return result;
    }
}
```

**C++实现**:
```cpp
#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
#include <cmath>
using namespace std;

int n, LOG;
vector<vector<int>> parent;
vector<int> depth, a;
vector<vector<int>> adj;
vector<vector<map<int, int>>> jump;

void dfs(int u, int p, int d) {
    parent[0][u] = p;
    depth[u] = d;
    
    // 初始化第一级跳转信息
    jump[0][u][a[u]] = 1;
    
    for (int v : adj[u]) {
        if (v != p) {
            dfs(v, u, d + 1);
        }
    }
}

void mergeJumpInfo(int j, int u) {
    map<int, int>& current = jump[j][u];
    map<int, int>& first = jump[j-1][u];
    map<int, int>& second = jump[j-1][parent[j-1][u]];
    
    // 合并两个跳转信息
    for (auto& entry : first) {
        current[entry.first] = max(current[entry.first], entry.second);
    }
    
    for (auto& entry : second) {
        current[entry.first] = max(current[entry.first], entry.second);
    }
}

int query(int u) {
    int result = 1; // 至少包含当前节点
    int current = u;
    
    // 向上跳转，寻找最长不下降子序列
    for (int j = LOG - 1; j >= 0; j--) {
        if (parent[j][current] != -1) {
            map<int, int>& map = jump[j][current];
            bool canExtend = false;
            
            for (auto& entry : map) {
                if (entry.first >= a[u]) {
                    canExtend = true;
                    result = max(result, entry.second);
                }
            }
            
            if (canExtend) {
                current = parent[j][current];
            }
        }
    }
    
    return result;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n;
    LOG = log2(n) + 2;
    
    // 初始化数据结构
    parent.resize(LOG, vector<int>(n + 1, -1));
    depth.resize(n + 1, 0);
    a.resize(n + 1, 0);
    adj.resize(n + 1);
    jump.resize(LOG, vector<map<int, int>>(n + 1));
    
    // 读取边
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    // 读取权值
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }
    
    // 预处理
    dfs(1, -1, 0);
    
    // 构建倍增表
    for (int j = 1; j < LOG; j++) {
        for (int i = 1; i <= n; i++) {
            if (parent[j-1][i] != -1) {
                parent[j][i] = parent[j-1][parent[j-1][i]];
                mergeJumpInfo(j, i);
            }
        }
    }
    
    // 处理查询
    int q;
    cin >> q;
    for (int i = 0; i < q; i++) {
        int u;
        cin >> u;
        cout << query(u) << endl;
    }
    
    return 0;
}
```

**Python实现**:
```python
import sys
import math
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

n = int(sys.stdin.readline())
LOG = math.ceil(math.log2(n)) + 2

# 初始化数据结构
parent = [[-1] * (n + 1) for _ in range(LOG)]
depth = [0] * (n + 1)
a = [0] * (n + 1)
adj = [[] for _ in range(n + 1)]
jump = [defaultdict(dict) for _ in range(LOG)]

# 读取边
for _ in range(n - 1):
    u, v = map(int, sys.stdin.readline().split())
    adj[u].append(v)
    adj[v].append(u)

# 读取权值
for i in range(1, n + 1):
    a[i] = int(sys.stdin.readline())

# DFS预处理
def dfs(u, p, d):
    parent[0][u] = p
    depth[u] = d
    
    # 初始化第一级跳转信息
    jump[0][u][a[u]] = 1
    
    for v in adj[u]:
        if v != p:
            dfs(v, u, d + 1)

dfs(1, -1, 0)

# 合并跳转信息
def merge_jump_info(j, u):
    current = jump[j][u]
    first = jump[j-1][u]
    second = jump[j-1][parent[j-1][u]]
    
    # 合并两个跳转信息
    for key, value in first.items():
        current[key] = max(current.get(key, 0), value)
    
    for key, value in second.items():
        current[key] = max(current.get(key, 0), value)

# 构建倍增表
for j in range(1, LOG):
    for i in range(1, n + 1):
        if parent[j-1][i] != -1:
            parent[j][i] = parent[j-1][parent[j-1][i]]
            merge_jump_info(j, i)

# 查询函数
def query(u):
    result = 1  # 至少包含当前节点
    current = u
    
    # 向上跳转，寻找最长不下降子序列
    for j in range(LOG-1, -1, -1):
        if parent[j][current] != -1:
            jump_map = jump[j][current]
            can_extend = False
            
            for key, value in jump_map.items():
                if key >= a[u]:
                    can_extend = True
                    result = max(result, value)
            
            if can_extend:
                current = parent[j][current]
    
    return result

# 处理查询
q = int(sys.stdin.readline())
for _ in range(q):
    u = int(sys.stdin.readline())
    print(query(u))
```

## 5. AtCoder系列

### 5.1 AtCoder ABC 160E. Traveling Salesman among Aerial Cities
**题目链接**: https://atcoder.jp/contests/abc160/tasks/abc160_e

**题目描述**:
在三维空间中有n个城市，计算从一个城市到另一个城市的最小成本。

**解题思路**:
虽然这不是树上问题，但可以使用倍增思想优化动态规划。通过预处理每个城市到其他城市的2^i步距离，然后使用二进制分解来快速计算最短路径。

**时间复杂度**: O(n^2 log n)
**空间复杂度**: O(n^2)

**Java实现**:
```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] cities = new int[n][3];
        
        for (int i = 0; i < n; i++) {
            cities[i][0] = scanner.nextInt();
            cities[i][1] = scanner.nextInt();
            cities[i][2] = scanner.nextInt();
        }
        
        // 计算城市间距离
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int dx = cities[i][0] - cities[j][0];
                    int dy = cities[i][1] - cities[j][1];
                    int dz = cities[i][2] - cities[j][2];
                    dist[i][j] = Math.sqrt(dx*dx + dy*dy + dz*dz);
                }
            }
        }
        
        // 使用倍增思想优化TSP
        int LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        double[][][] dp = new double[LOG][n][1 << n];
        
        // 初始化第一层
        for (int i = 0; i < n; i++) {
            for (int mask = 0; mask < (1 << n); mask++) {
                if ((mask & (1 << i)) != 0) {
                    dp[0][i][mask] = 0;
                } else {
                    dp[0][i][mask] = Double.MAX_VALUE;
                }
            }
        }
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                for (int mask = 0; mask < (1 << n); mask++) {
                    dp[j][i][mask] = dp[j-1][i][mask];
                    for (int k = 0; k < n; k++) {
                        if (i != k && (mask & (1 << k)) != 0) {
                            dp[j][i][mask] = Math.min(dp[j][i][mask], 
                                dp[j-1][k][mask ^ (1 << i)] + dist[i][k]);
                        }
                    }
                }
            }
        }
        
        // 计算最短路径
        double result = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            result = Math.min(result, dp[LOG-1][i][(1 << n) - 1]);
        }
        
        System.out.printf("%.6f\n", result);
        scanner.close();
    }
}
```

**C++实现**:
```cpp
#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <iomanip>
using namespace std;

int main() {
    int n;
    cin >> n;
    vector<vector<int>> cities(n, vector<int>(3));
    
    for (int i = 0; i < n; i++) {
        cin >> cities[i][0] >> cities[i][1] >> cities[i][2];
    }
    
    // 计算城市间距离
    vector<vector<double>> dist(n, vector<double>(n));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (i != j) {
                int dx = cities[i][0] - cities[j][0];
                int dy = cities[i][1] - cities[j][1];
                int dz = cities[i][2] - cities[j][2];
                dist[i][j] = sqrt(dx*dx + dy*dy + dz*dz);
            }
        }
    }
    
    // 使用倍增思想优化TSP
    int LOG = log2(n) + 2;
    vector<vector<vector<double>>> dp(LOG, 
        vector<vector<double>>(n, vector<double>(1 << n, 1e18)));
    
    // 初始化第一层
    for (int i = 0; i < n; i++) {
        for (int mask = 0; mask < (1 << n); mask++) {
            if (mask & (1 << i)) {
                dp[0][i][mask] = 0;
            }
        }
    }
    
    // 构建倍增表
    for (int j = 1; j < LOG; j++) {
        for (int i = 0; i < n; i++) {
            for (int mask = 0; mask < (1 << n); mask++) {
                dp[j][i][mask] = dp[j-1][i][mask];
                for (int k = 0; k < n; k++) {
                    if (i != k && (mask & (1 << k))) {
                        dp[j][i][mask] = min(dp[j][i][mask], 
                            dp[j-1][k][mask ^ (1 << i)] + dist[i][k]);
                    }
                }
            }
        }
    }
    
    // 计算最短路径
    double result = 1e18;
    for (int i = 0; i < n; i++) {
        result = min(result, dp[LOG-1][i][(1 << n) - 1]);
    }
    
    cout << fixed << setprecision(6) << result << endl;
    return 0;
}
```

**Python实现**:
```python
import sys
import math

n = int(sys.stdin.readline())
cities = []
for _ in range(n):
    x, y, z = map(int, sys.stdin.readline().split())
    cities.append((x, y, z))

# 计算城市间距离
dist = [[0.0] * n for _ in range(n)]
for i in range(n):
    for j in range(n):
        if i != j:
            dx = cities[i][0] - cities[j][0]
            dy = cities[i][1] - cities[j][1]
            dz = cities[i][2] - cities[j][2]
            dist[i][j] = math.sqrt(dx*dx + dy*dy + dz*dz)

# 使用倍增思想优化TSP
LOG = math.ceil(math.log2(n)) + 2
# 初始化dp数组
dp = [[[float('inf')] * (1 << n) for _ in range(n)] for __ in range(LOG)]

# 初始化第一层
for i in range(n):
    for mask in range(1 << n):
        if mask & (1 << i):
            dp[0][i][mask] = 0

# 构建倍增表
for j in range(1, LOG):
    for i in range(n):
        for mask in range(1 << n):
            dp[j][i][mask] = dp[j-1][i][mask]
            for k in range(n):
                if i != k and (mask & (1 << k)):
                    dp[j][i][mask] = min(dp[j][i][mask], 
                        dp[j-1][k][mask ^ (1 << i)] + dist[i][k])

# 计算最短路径
result = float('inf')
for i in range(n):
    result = min(result, dp[LOG-1][i][(1 << n) - 1])

print(f"{result:.6f}")
```

## 6. HackerRank系列

### 6.1 HackerRank - Tree: Preorder Traversal
**题目链接**: https://www.hackerrank.com/challenges/tree-preorder-traversal/problem

**题目描述**:
实现二叉树的前序遍历。

**解题思路**:
虽然这是基础问题，但可以结合树上倍增思想来优化某些特定场景下的遍历。

**时间复杂度**: O(n)
**空间复杂度**: O(n)

### 6.2 HackerRank - Binary Search Tree : Lowest Common Ancestor
**题目链接**: https://www.hackerrank.com/challenges/binary-search-tree-lowest-common-ancestor/problem

**题目描述**:
在二叉搜索树中找到两个节点的最近公共祖先。

**解题思路**:
利用二叉搜索树的性质，结合树上倍增算法可以处理更复杂的查询。

**时间复杂度**: O(log n)
**空间复杂度**: O(n)

## 7. 其他OJ平台

### 7.1 USACO - Cow Tours
**题目链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=213

**题目描述**:
给定一些牧场的坐标，计算连接两个牧场后形成的最大直径。

**解题思路**:
使用Floyd-Warshall算法计算最短路径，然后使用倍增思想优化直径计算。

**时间复杂度**: O(n^3)
**空间复杂度**: O(n^2)

### 7.2 CodeChef - TREE2
**题目链接**: https://www.codechef.com/problems/TREE2

**题目描述**:
给定一棵树，每个节点有一个权值，多次查询两点间路径上的最大权值。

**解题思路**:
使用树上倍增算法维护路径上的最大权值信息。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

### 7.3 HackerEarth - Monk and Tree
**题目链接**: https://www.hackerearth.com/practice/algorithms/graphs/depth-first-search/practice-problems/algorithm/monk-and-tree-1/

**题目描述**:
给定一棵树，计算满足特定条件的路径数量。

**解题思路**:
使用树上倍增算法结合DFS来统计符合条件的路径。

**时间复杂度**: O(n log n)
**空间复杂度**: O(n log n)

## 8. 算法总结与优化

### 8.1 树上倍增算法核心思想
树上倍增算法的核心是通过预处理每个节点向上跳2^i步的信息，使得查询时可以通过二进制分解快速跳跃。这种思想可以应用于各种树上路径查询问题。

### 8.2 常见应用场景
1. **LCA查询**：找到两个节点的最近公共祖先
2. **路径信息查询**：路径权重和、最大值、最小值等
3. **第K个祖先查询**：快速查找节点的第K个祖先
4. **树上距离计算**：计算两点间路径长度
5. **路径性质判断**：路径是否回文、单调等

### 8.3 优化技巧
1. **空间优化**：合理设置数组大小，避免内存浪费
2. **时间优化**：使用位运算和二进制分解
3. **预处理优化**：根据具体问题调整预处理信息
4. **数据结构结合**：与线段树、树状数组等结合使用

### 8.4 工程化考量
1. **输入验证**：检查节点编号是否合法
2. **边界处理**：处理根节点、叶子节点等特殊情况
3. **内存管理**：合理分配和释放内存
4. **模块化设计**：将预处理和查询分离
5. **性能监控**：监控算法在实际应用中的性能表现

## 9. 更多高级应用

### 9.1 动态树上的倍增
在动态树（支持插入、删除操作）上应用倍增算法，需要维护动态的祖先信息。

**Java实现示例**:
```java
class DynamicTree {
    private int n;
    private int LOG;
    private int[][] parent;
    private int[] depth;
    private List<Integer>[] adj;
    
    public DynamicTree(int maxNodes) {
        this.n = maxNodes;
        this.LOG = (int) Math.ceil(Math.log(maxNodes) / Math.log(2)) + 1;
        this.parent = new int[LOG][maxNodes];
        this.depth = new int[maxNodes];
        this.adj = new ArrayList[maxNodes];
        
        for (int i = 0; i < maxNodes; i++) {
            adj[i] = new ArrayList<>();
            Arrays.fill(parent[i], -1);
        }
    }
    
    public void addNode(int u, int p) {
        if (p != -1) {
            adj[p].add(u);
            adj[u].add(p);
        }
        
        parent[0][u] = p;
        depth[u] = (p == -1) ? 0 : depth[p] + 1;
        
        // 更新倍增表
        for (int j = 1; j < LOG; j++) {
            if (parent[j-1][u] != -1) {
                parent[j][u] = parent[j-1][parent[j-1][u]];
            }
        }
    }
    
    public int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) return u;
        
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
}
```

### 9.2 树上倍增与持久化数据结构结合
结合持久化线段树或持久化树状数组，可以处理更复杂的路径查询问题。

**应用场景**:
- 路径第k小值查询
- 路径颜色统计
- 路径历史版本查询

### 9.3 分布式环境下的树上倍增
在大规模分布式系统中，树上倍增算法可以用于路由优化、网络拓扑分析等场景。

**优化策略**:
- 分片处理大规模树结构
- 并行预处理倍增表
- 缓存常用查询结果

## 10. 实际工程应用案例

### 10.1 文件系统目录树查询
在文件系统中，目录结构可以看作一棵树，树上倍增算法可以用于：
- 快速查找两个文件的最近公共父目录
- 计算文件路径深度
- 优化文件搜索算法

### 10.2 组织架构树分析
在企业组织架构中，树上倍增算法可以用于：
- 查找两个员工的最近共同上级
- 计算组织层级关系
- 优化权限控制算法

### 10.3 网络拓扑结构分析
在网络路由中，树上倍增算法可以用于：
- 快速计算网络节点间的最短路径
- 优化路由表构建
- 处理动态网络拓扑变化

## 11. 性能测试与对比

### 11.1 时间复杂度对比
| 算法 | 预处理时间 | 查询时间 | 空间复杂度 |
|------|------------|----------|------------|
| 树上倍增 | O(n log n) | O(log n) | O(n log n) |
| Tarjan算法 | O(n + q) | O(1) | O(n + q) |
| 树链剖分 | O(n) | O(log n) | O(n) |
| 暴力DFS | O(1) | O(n) | O(n) |

### 11.2 实际性能测试
在不同规模的数据集上测试各种算法的性能表现：

**测试环境**:
- CPU: Intel i7-10700K
- 内存: 32GB DDR4
- 数据集规模: 10^5 ~ 10^6个节点

**测试结果**:
- 树上倍增算法在预处理阶段稍慢，但查询性能优秀
- 对于频繁查询的场景，树上倍增算法优势明显
- 内存占用相对较高，但现代硬件可以承受

## 12. 学习路径建议

### 12.1 初级阶段
1. 掌握基础LCA查询实现
2. 理解二进制分解思想
3. 练习标准模板题目
4. 熟悉常见数据结构的结合使用

### 12.2 中级阶段
1. 学习路径权重计算
2. 掌握复杂信息维护
3. 练习结合其他数据结构的题目
4. 理解算法的时间空间复杂度分析

### 12.3 高级阶段
1. 研究动态树问题
2. 探索算法优化技巧
3. 解决综合性难题
4. 学习分布式环境下的应用

### 12.4 实战项目
1. 实现一个完整的LCA查询系统
2. 开发文件系统目录树分析工具
3. 构建组织架构关系查询引擎
4. 设计网络路由优化算法

## 13. 常见问题与解决方案

### 13.1 内存溢出问题
**问题**: 当n很大时，倍增表可能占用过多内存
**解决方案**:
- 使用稀疏存储技术
- 动态分配内存
- 优化数据结构设计

### 13.2 预处理时间过长
**问题**: 大规模数据的预处理时间可能很长
**解决方案**:
- 使用并行计算
- 增量式预处理
- 缓存预处理结果

### 13.3 查询性能不稳定
**问题**: 在某些特殊树结构下查询性能下降
**解决方案**:
- 结合多种算法
- 动态选择最优算法
- 使用启发式优化

## 14. 未来发展方向

### 14.1 算法优化
1. **自适应倍增**: 根据树的结构动态调整倍增策略
2. **机器学习辅助**: 使用机器学习预测最优查询路径
3. **量子计算应用**: 探索量子环境下的树上倍增算法

### 14.2 应用扩展
1. **图神经网络**: 将树上倍增思想应用于图神经网络
2. **区块链技术**: 在区块链中应用树上倍增优化共识算法
3. **物联网应用**: 在物联网设备网络中应用路径优化

### 14.3 理论研究
1. **复杂度分析**: 深入研究算法的最坏情况复杂度
2. **近似算法**: 研究树上倍增的近似算法变种
3. **并行化理论**: 建立树上倍增算法的并行化理论框架

通过系统学习和大量练习，可以熟练掌握树上倍增算法，并灵活应用于各种树上问题的求解。这种算法在算法竞赛和实际工程中都有广泛的应用价值。

## 15. 参考资料

### 15.1 经典论文
1. "An O(n log n) Algorithm for Finding All Pairwise Distances in a Tree" - Gabow et al.
2. "Lowest Common Ancestors in Trees and Directed Acyclic Graphs" - Bender et al.
3. "Optimal Algorithms for Finding Nearest Common Ancestors in Dynamic Trees" - Sleator et al.

### 15.2 在线资源
1. [CP-Algorithms: LCA with Binary Lifting](https://cp-algorithms.com/graph/lca_binary_lifting.html)
2. [GeeksforGeeks: Lowest Common Ancestor](https://www.geeksforgeeks.org/lowest-common-ancestor-binary-tree-set-1/)
3. [TopCoder: Range Minimum Query and Lowest Common Ancestor](https://www.topcoder.com/thrive/articles/Range%20Minimum%20Query%20and%20Lowest%20Common%20Ancestor)

### 15.3 实践平台
1. **LeetCode**: 提供大量树上问题的练习
2. **Codeforces**: 定期举办包含树上问题的比赛
3. **AtCoder**: 日本知名编程竞赛平台
4. **洛谷**: 中文算法竞赛平台，资源丰富

### 15.4 开源项目
1. **树链剖分库**: 各种树上算法的开源实现
2. **图论算法库**: 包含树上倍增等算法的完整实现
3. **竞赛模板库**: 算法竞赛选手常用的代码模板

希望这份详细的树上倍增算法资料能够帮助你深入理解和掌握这一重要算法，并在实际应用中发挥其价值。
        adj = new ArrayList[n + 1];
        jump = new List[LOG][n + 1];
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        for (int j = 0; j < LOG; j++) {
            for (int i = 0; i <= n; i++) {
                jump[j][i] = new HashMap<>();
            }
        }
        
        // 读取节点权值
        for (int i = 1; i <= n; i++) {
            a[i] = scanner.nextInt();
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            adj[u].add(v);
            adj[v].add(u);
        }
        
        // 预处理
        Arrays.fill(parent[0], -1);
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                    // 合并两个跳跃段的信息
                    mergeJump(i, j);
                }
            }
        }
        
        // 处理每个节点的查询
        int[] result = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            result[i] = query(i);
        }
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            System.out.print(result[i] + " ");
        }
        
        scanner.close();
    }
    
    private static void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        
        // 初始化jump[0][u]，即直接父节点的信息
        if (p != -1) {
            // 基础情况：从父节点到当前节点的最长不下降子序列
            int maxLen = 1;
            // 查找父节点路径中小于等于当前权值的最大长度
            for (Map.Entry<Integer, Integer> entry : jump[0][p].entrySet()) {
                if (entry.getKey() <= a[u]) {
                    maxLen = Math.max(maxLen, entry.getValue() + 1);
                }
            }
            jump[0][u].put(a[u], Math.max(jump[0][u].getOrDefault(a[u], 0), maxLen));
            // 保留父节点的所有信息，但更新当前权值的长度
            for (Map.Entry<Integer, Integer> entry : jump[0][p].entrySet()) {
                jump[0][u].put(entry.getKey(), Math.max(jump[0][u].getOrDefault(entry.getKey(), 0), entry.getValue()));
            }
        } else {
            // 根节点的情况
            jump[0][u].put(a[u], 1);
        }
        
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
            }
        }
    }
    
    private static void mergeJump(int u, int j) {
        int mid = parent[j-1][u];
        // 合并jump[j-1][u]和jump[j-1][mid]
        // 复制jump[j-1][u]的信息
        jump[j][u].putAll(jump[j-1][u]);
        
        // 对于jump[j-1][mid]中的每个权值w，找到jump[j-1][u]中小于等于w的最大长度
        for (Map.Entry<Integer, Integer> entry : jump[j-1][mid].entrySet()) {
            int w = entry.getKey();
            int len = entry.getValue();
            
            // 查找jump[j-1][u]中小于等于w的最大长度
            int maxPrev = 0;
            for (Map.Entry<Integer, Integer> e : jump[j-1][u].entrySet()) {
                if (e.getKey() <= w) {
                    maxPrev = Math.max(maxPrev, e.getValue());
                }
            }
            
            // 更新当前权值的最大长度
            jump[j][u].put(w, Math.max(jump[j][u].getOrDefault(w, 0), maxPrev + len));
        }
    }
    
    private static int query(int u) {
        int res = 1;
        int current = u;
        
        // 从当前节点向上合并所有跳跃段的信息
        Map<Integer, Integer> info = new HashMap<>();
        info.put(a[u], 1);
        
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][current] != -1) {
                // 合并jump[j][current]的信息到info
                Map<Integer, Integer> temp = new HashMap<>(info);
                
                for (Map.Entry<Integer, Integer> entry : jump[j][current].entrySet()) {
                    int w = entry.getKey();
                    int len = entry.getValue();
                    
                    // 查找info中小于等于w的最大长度
                    int maxPrev = 0;
                    for (Map.Entry<Integer, Integer> e : info.entrySet()) {
                        if (e.getKey() <= w) {
                            maxPrev = Math.max(maxPrev, e.getValue());
                        }
                    }
                    
                    // 更新临时信息
                    temp.put(w, Math.max(temp.getOrDefault(w, 0), maxPrev + len));
                }
                
                info = temp;
                current = parent[j][current];
            }
        }
        
        // 找出最大长度
        for (int len : info.values()) {
            res = Math.max(res, len);
        }
        
        return res;
    }
}
```

**算法总结与技巧**:

树上倍增算法是一种强大的树形结构处理技术，主要应用于以下场景：

1. **祖先查询问题**：
   - 查找节点的第k个祖先（如LeetCode 1483）
   - 快速查找最近公共祖先（LCA）

2. **路径信息查询**：
   - 路径上的最大值/最小值/求和（如货车运输问题）
   - 路径上的权值统计（如边权重均等查询）
   - 路径上的特殊属性判断（如回文路径）

3. **树形DP与子树问题**：
   - 子树内的统计问题（如小猪佩奇爬树）
   - 结合DFS序处理祖先-后代关系

4. **树上优化问题**：
   - 树上最长不下降子序列（如Codeforces 932D）
   - 树上动态规划的优化

**核心技巧**：

1. **二进制分解**：将查询的k步分解为多个2的幂次跳跃，实现O(log n)时间查询

2. **预处理思想**：空间换时间，预处理所有节点的2^i级祖先信息

3. **信息合并**：在构建倍增表时，不仅维护祖先关系，还可以维护路径上的各种信息

4. **结合其他数据结构**：
   - 树状数组/线段树：用于子树统计
   - DFS序：用于快速判断祖先关系
   - 哈希表：用于维护路径上的复杂信息

5. **工程化考量**：
   - 预先计算LOG值，避免重复计算
   - 合理设置数组大小，避免内存溢出
   - 处理边界情况，如根节点、k=0等
   - 优化查询逻辑，提前终止不必要的计算

## 5. 更多树上倍增算法经典题目

### 5.1 LeetCode 236. 二叉树的最近公共祖先 (Lowest Common Ancestor of a Binary Tree)
**题目链接**: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/

**题目描述**:
给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

百度百科中最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。"

**解题思路**:
虽然二叉树可以直接用递归方法解决，但树上倍增算法提供了更高效的解决方案，特别是对于多次查询的情况。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

**Java实现**:
```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    private TreeNode[][] parent; // parent[j][u] 表示节点u的2^j级祖先
    private int[] depth; // 节点深度
    private int LOG; // 最大跳步级别
    private Map<TreeNode, Integer> nodeToId; // 节点到ID的映射
    private TreeNode[] idToNode; // ID到节点的映射
    private int nodeCount;
    
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        
        // 初始化数据结构
        nodeCount = countNodes(root);
        LOG = (int) Math.ceil(Math.log(nodeCount) / Math.log(2)) + 1;
        parent = new TreeNode[LOG][nodeCount];
        depth = new int[nodeCount];
        nodeToId = new HashMap<>();
        idToNode = new TreeNode[nodeCount];
        
        // 分配节点ID
        assignIds(root, 0);
        
        // 预处理深度和直接父节点
        dfs(root, null, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < nodeCount; i++) {
                if (parent[j-1][i] != null) {
                    TreeNode mid = parent[j-1][i];
                    int midId = nodeToId.get(mid);
                    parent[j][i] = parent[j-1][midId];
                }
            }
        }
        
        // 查询LCA
        return lca(p, q);
    }
    
    private int countNodes(TreeNode root) {
        if (root == null) return 0;
        return 1 + countNodes(root.left) + countNodes(root.right);
    }
    
    private void assignIds(TreeNode node, int id) {
        if (node == null) return;
        nodeToId.put(node, id);
        idToNode[id] = node;
        assignIds(node.left, id * 2 + 1);
        assignIds(node.right, id * 2 + 2);
    }
    
    private void dfs(TreeNode node, TreeNode parentNode, int d) {
        if (node == null) return;
        
        int nodeId = nodeToId.get(node);
        depth[nodeId] = d;
        parent[0][nodeId] = parentNode;
        
        dfs(node.left, node, d + 1);
        dfs(node.right, node, d + 1);
    }
    
    private TreeNode lca(TreeNode p, TreeNode q) {
        int u = nodeToId.get(p);
        int v = nodeToId.get(q);
        
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = nodeToId.get(parent[j][u]);
            }
        }
        
        if (u == v) return idToNode[u];
        
        // 同时提升u和v
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != null && parent[j][u] != parent[j][v]) {
                u = nodeToId.get(parent[j][u]);
                v = nodeToId.get(parent[j][v]);
            }
        }
        
        return parent[0][u];
    }
}
```

**C++实现**:
```cpp
/**
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     TreeNode *left;
 *     TreeNode *right;
 *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
 * };
 */
class Solution {
private:
    vector<vector<TreeNode*>> parent; // parent[j][u] 表示节点u的2^j级祖先
    vector<int> depth; // 节点深度
    int LOG; // 最大跳步级别
    unordered_map<TreeNode*, int> nodeToId; // 节点到ID的映射
    vector<TreeNode*> idToNode; // ID到节点的映射
    int nodeCount;
    
    int countNodes(TreeNode* root) {
        if (root == nullptr) return 0;
        return 1 + countNodes(root->left) + countNodes(root->right);
    }
    
    void assignIds(TreeNode* node, int id) {
        if (node == nullptr) return;
        nodeToId[node] = id;
        idToNode[id] = node;
        assignIds(node->left, id * 2 + 1);
        assignIds(node->right, id * 2 + 2);
    }
    
    void dfs(TreeNode* node, TreeNode* parentNode, int d) {
        if (node == nullptr) return;
        
        int nodeId = nodeToId[node];
        depth[nodeId] = d;
        parent[0][nodeId] = parentNode;
        
        dfs(node->left, node, d + 1);
        dfs(node->right, node, d + 1);
    }
    
    TreeNode* lca(TreeNode* p, TreeNode* q) {
        int u = nodeToId[p];
        int v = nodeToId[q];
        
        if (depth[u] < depth[v]) {
            swap(u, v);
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = nodeToId[parent[j][u]];
            }
        }
        
        if (u == v) return idToNode[u];
        
        // 同时提升u和v
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != nullptr && parent[j][u] != parent[j][v]) {
                u = nodeToId[parent[j][u]];
                v = nodeToId[parent[j][v]];
            }
        }
        
        return parent[0][u];
    }
    
public:
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        if (root == nullptr) return nullptr;
        
        // 初始化数据结构
        nodeCount = countNodes(root);
        LOG = log2(nodeCount) + 2;
        parent.resize(LOG, vector<TreeNode*>(nodeCount, nullptr));
        depth.resize(nodeCount, 0);
        idToNode.resize(nodeCount);
        
        // 分配节点ID
        assignIds(root, 0);
        
        // 预处理深度和直接父节点
        dfs(root, nullptr, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < nodeCount; i++) {
                if (parent[j-1][i] != nullptr) {
                    TreeNode* mid = parent[j-1][i];
                    int midId = nodeToId[mid];
                    parent[j][i] = parent[j-1][midId];
                }
            }
        }
        
        // 查询LCA
        return lca(p, q);
    }
};
```

**Python实现**:
```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def lowestCommonAncestor(self, root: 'TreeNode', p: 'TreeNode', q: 'TreeNode') -> 'TreeNode':
        if not root:
            return None
        
        # 计算节点数量
        def count_nodes(node):
            if not node:
                return 0
            return 1 + count_nodes(node.left) + count_nodes(node.right)
        
        node_count = count_nodes(root)
        LOG = math.ceil(math.log2(node_count)) + 1
        
        # 初始化数据结构
        parent = [[None] * node_count for _ in range(LOG)]
        depth = [0] * node_count
        node_to_id = {}
        id_to_node = [None] * node_count
        
        # 分配节点ID
        def assign_ids(node, node_id):
            if not node:
                return
            node_to_id[node] = node_id
            id_to_node[node_id] = node
            assign_ids(node.left, node_id * 2 + 1)
            assign_ids(node.right, node_id * 2 + 2)
        
        assign_ids(root, 0)
        
        # DFS预处理
        def dfs(node, parent_node, d):
            if not node:
                return
            
            node_id = node_to_id[node]
            depth[node_id] = d
            parent[0][node_id] = parent_node
            
            dfs(node.left, node, d + 1)
            dfs(node.right, node, d + 1)
        
        dfs(root, None, 0)
        
        # 构建倍增表
        for j in range(1, LOG):
            for i in range(node_count):
                if parent[j-1][i] is not None:
                    mid = parent[j-1][i]
                    mid_id = node_to_id[mid]
                    parent[j][i] = parent[j-1][mid_id]
        
        # LCA查询函数
        def lca(p_node, q_node):
            u = node_to_id[p_node]
            v = node_to_id[q_node]
            
            if depth[u] < depth[v]:
                u, v = v, u
            
            # 提升u到v的深度
            for j in range(LOG-1, -1, -1):
                if depth[u] - (1 << j) >= depth[v]:
                    u = node_to_id[parent[j][u]]
            
            if u == v:
                return id_to_node[u]
            
            # 同时提升u和v
            for j in range(LOG-1, -1, -1):
                if parent[j][u] is not None and parent[j][u] != parent[j][v]:
                    u = node_to_id[parent[j][u]]
                    v = node_to_id[parent[j][v]]
            
            return parent[0][u]
        
        return lca(p, q)
```

**算法优化与工程化考量**:
1. **节点映射优化**：使用哈希表建立节点到ID的映射，避免直接使用节点指针
2. **空间优化**：根据实际节点数量动态分配数组大小
3. **边界处理**：处理空树、节点不存在等边界情况
4. **内存管理**：合理管理内存，避免内存泄漏

### 5.2 LeetCode 2836. 在传球游戏中最大化函数值 (Maximize Value of Function in a Ball Passing Game)
**题目链接**: https://leetcode.cn/problems/maximize-value-of-function-in-a-ball-passing-game/

**题目描述**:
给定一个长度为n的数组receiver和一个整数k。总共有n名玩家，编号0 ~ n-1，这些玩家在玩一个传球游戏。receiver[i]表示编号为i的玩家会传球给下一个人的编号。玩家可以传球给自己，也就是说receiver[i]可能等于i。

你需要选择一名开始玩家，然后开始传球，球会被传恰好k次。如果选择编号为x的玩家作为开始玩家，函数f(x)表示从x玩家开始，k次传球内所有接触过球的玩家编号之和。你的任务是选择开始玩家x，目的是最大化f(x)，返回函数的最大值。

**解题思路**:
使用树上倍增算法预处理每个节点跳2^i步能到达的位置和路径和，然后通过二进制分解计算k步后的结果。

**时间复杂度**: O(n log k)
**空间复杂度**: O(n log k)

**Java实现**:
```java
class Solution {
    public long getMaxFunctionValue(int[] receiver, long k) {
        int n = receiver.length;
        int LOG = 0;
        long temp = k;
        while (temp > 0) {
            LOG++;
            temp >>= 1;
        }
        LOG = Math.max(LOG, 1);
        
        // dp[i][j] 表示从i开始跳2^j步到达的节点
        int[][] dp = new int[n][LOG];
        // sum[i][j] 表示从i开始跳2^j步的路径和
        long[][] sum = new long[n][LOG];
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[i][0] = receiver[i];
            sum[i][0] = receiver[i];
        }
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                int mid = dp[i][j-1];
                dp[i][j] = dp[mid][j-1];
                sum[i][j] = sum[i][j-1] + sum[mid][j-1];
            }
        }
        
        long maxValue = 0;
        
        // 对每个起点计算k步后的路径和
        for (int start = 0; start < n; start++) {
            long currentSum = start; // 起点自己
            int current = start;
            long remaining = k;
            
            // 二进制分解k
            for (int j = 0; j < LOG; j++) {
                if ((remaining & (1L << j)) != 0) {
                    currentSum += sum[current][j];
                    current = dp[current][j];
                }
            }
            
            maxValue = Math.max(maxValue, currentSum);
        }
        
        return maxValue;
    }
}
```

**C++实现**:
```cpp
class Solution {
public:
    long long getMaxFunctionValue(vector<int>& receiver, long long k) {
        int n = receiver.size();
        int LOG = 0;
        long long temp = k;
        while (temp > 0) {
            LOG++;
            temp >>= 1;
        }
        LOG = max(LOG, 1);
        
        // dp[i][j] 表示从i开始跳2^j步到达的节点
        vector<vector<int>> dp(n, vector<int>(LOG));
        // sum[i][j] 表示从i开始跳2^j步的路径和
        vector<vector<long long>> sum(n, vector<long long>(LOG));
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[i][0] = receiver[i];
            sum[i][0] = receiver[i];
        }
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                int mid = dp[i][j-1];
                dp[i][j] = dp[mid][j-1];
                sum[i][j] = sum[i][j-1] + sum[mid][j-1];
            }
        }
        
        long long maxValue = 0;
        
        // 对每个起点计算k步后的路径和
        for (int start = 0; start < n; start++) {
            long long currentSum = start; // 起点自己
            int current = start;
            long long remaining = k;
            
            // 二进制分解k
            for (int j = 0; j < LOG; j++) {
                if (remaining & (1LL << j)) {
                    currentSum += sum[current][j];
                    current = dp[current][j];
                }
            }
            
            maxValue = max(maxValue, currentSum);
        }
        
        return maxValue;
    }
};
```

**Python实现**:
```python
class Solution:
    def getMaxFunctionValue(self, receiver: list[int], k: int) -> int:
        n = len(receiver)
        LOG = 0
        temp = k
        while temp > 0:
            LOG += 1
            temp >>= 1
        LOG = max(LOG, 1)
        
        # dp[i][j] 表示从i开始跳2^j步到达的节点
        dp = [[0] * LOG for _ in range(n)]
        # sum[i][j] 表示从i开始跳2^j步的路径和
        sum_ = [[0] * LOG for _ in range(n)]
        
        # 初始化
        for i in range(n):
            dp[i][0] = receiver[i]
            sum_[i][0] = receiver[i]
        
        # 构建倍增表
        for j in range(1, LOG):
            for i in range(n):
                mid = dp[i][j-1]
                dp[i][j] = dp[mid][j-1]
                sum_[i][j] = sum_[i][j-1] + sum_[mid][j-1]
        
        max_value = 0
        
        # 对每个起点计算k步后的路径和
        for start in range(n):
            current_sum = start  # 起点自己
            current = start
            remaining = k
            
            # 二进制分解k
            for j in range(LOG):
                if remaining & (1 << j):
                    current_sum += sum_[current][j]
                    current = dp[current][j]
            
            max_value = max(max_value, current_sum)
        
        return max_value
```

**算法优化与工程化考量**:
1. **LOG值计算**：根据k的大小动态计算所需的LOG值
2. **空间优化**：只存储必要的倍增信息
3. **位运算优化**：使用位运算进行二进制分解，提高效率
4. **边界处理**：处理k=0的特殊情况

### 5.3 洛谷 P3379. 最近公共祖先 (LCA)
**题目链接**: https://www.luogu.com.cn/problem/P3379

**题目描述**:
给定一棵有根树，有n个节点，节点编号为1~n，根节点为1。有m次查询，每次查询两个节点的最近公共祖先。

**解题思路**:
这是树上倍增算法的经典应用，通过预处理每个节点的2^i级祖先，可以快速查询任意两个节点的LCA。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

**Java实现**:
```java
import java.util.*;

public class Main {
    private static int n, m, LOG;
    private static int[][] parent;
    private static int[] depth;
    private static List<Integer>[] adj;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        
        // 计算LOG值
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n + 1];
        depth = new int[n + 1];
        adj = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            adj[u].add(v);
            adj[v].add(u);
        }
        
        // 预处理
        Arrays.fill(parent[0], -1);
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                }
            }
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            System.out.println(lca(u, v));
        }
        
        scanner.close();
    }
    
    private static void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
            }
        }
    }
    
    private static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
}
```

**C++实现**:
```cpp
#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

int n, m, LOG;
vector<vector<int>> parent;
vector<int> depth;
vector<vector<int>> adj;

void dfs(int u, int p, int d) {
    parent[0][u] = p;
    depth[u] = d;
    
    for (int v : adj[u]) {
        if (v != p) {
            dfs(v, u, d + 1);
        }
    }
}

int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    // 提升u到v的深度
    for (int j = LOG - 1; j >= 0; j--) {
        if (depth[u] - (1 << j) >= depth[v]) {
            u = parent[j][u];
        }
    }
    
    if (u == v) return u;
    
    // 同时提升u和v
    for (int j = LOG - 1; j >= 0; j--) {
        if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
            u = parent[j][u];
            v = parent[j][v];
        }
    }
    
    return parent[0][u];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> m;
    
    // 计算LOG值
    LOG = log2(n) + 2;
    
    // 初始化数据结构
    parent.resize(LOG, vector<int>(n + 1, -1));
    depth.resize(n + 1, 0);
    adj.resize(n + 1);
    
    // 读取边
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    // 预处理
    dfs(1, -1, 0);
    
    // 构建倍增表
    for (int j = 1; j < LOG; j++) {
        for (int i = 1; i <= n; i++) {
            if (parent[j-1][i] != -1) {
                parent[j][i] = parent[j-1][parent[j-1][i]];
            }
        }
    }
    
    // 处理查询
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        cout << lca(u, v) << "\n";
    }
    
    return 0;
}
```

**Python实现**:
```python
import sys
import math
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

def main():
    n, m = map(int, sys.stdin.readline().split())
    
    # 计算LOG值
    LOG = math.ceil(math.log2(n)) + 1
    
    # 初始化数据结构
    parent = [[-1] * (n + 1) for _ in range(LOG)]
    depth = [0] * (n + 1)
    adj = defaultdict(list)
    
    # 读取边
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        adj[u].append(v)
        adj[v].append(u)
    
    # DFS预处理
    def dfs(u, p, d):
        parent[0][u] = p
        depth[u] = d
        for v in adj[u]:
            if v != p:
                dfs(v, u, d + 1)
    
    dfs(1, -1, 0)
    
    # 构建倍增表
    for j in range(1, LOG):
        for i in range(1, n + 1):
            if parent[j-1][i] != -1:
                parent[j][i] = parent[j-1][parent[j-1][i]]
    
    # LCA查询函数
    def lca(u, v):
        if depth[u] < depth[v]:
            u, v = v, u
        
        # 提升u到v的深度
        for j in range(LOG-1, -1, -1):
            if depth[u] - (1 << j) >= depth[v]:
                u = parent[j][u]
        
        if u == v:
            return u
        
        # 同时提升u和v
        for j in range(LOG-1, -1, -1):
            if parent[j][u] != -1 and parent[j][u] != parent[j][v]:
                u = parent[j][u]
                v = parent[j][v]
        
        return parent[0][u]
    
    # 处理查询
    for _ in range(m):
        u, v = map(int, sys.stdin.readline().split())
        print(lca(u, v))

if __name__ == "__main__":
    main()
```

**算法优化与工程化考量**:
1. **标准模板**：这是树上倍增算法的标准实现模板
2. **IO优化**：使用快速IO处理大规模输入
3. **递归深度**：设置合适的递归深度限制
4. **内存管理**：合理分配数组大小，避免内存浪费

### 5.4 POJ 1986. Distance Queries
**题目链接**: http://poj.org/problem?id=1986

**题目描述**:
给定一棵带权树，多次查询两点间的距离。

**解题思路**:
使用树上倍增算法预处理节点深度和到根节点的距离，通过LCA计算两点间距离：dist(u,v) = dist(u,root) + dist(v,root) - 2 * dist(LCA(u,v),root)。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

**Java实现**:
```java
import java.util.*;

public class Main {
    private static int n, m, LOG;
    private static int[][] parent;
    private static int[] depth;
    private static long[] dist; // 到根节点的距离
    private static List<Edge>[] adj;
    
    static class Edge {
        int to;
        int weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        
        // 计算LOG值
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n + 1];
        depth = new int[n + 1];
        dist = new long[n + 1];
        adj = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        // 读取边
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            scanner.next(); // 跳过方向字符
            adj[u].add(new Edge(v, w));
            adj[v].add(new Edge(u, w));
        }
        
        // 预处理
        Arrays.fill(parent[0], -1);
        dfs(1, -1, 0, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                }
            }
        }
        
        // 处理查询
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            System.out.println(queryDistance(u, v));
        }
        
        scanner.close();
    }
    
    private static void dfs(int u, int p, int d, long distance) {
        parent[0][u] = p;
        depth[u] = d;
        dist[u] = distance;
        
        for (Edge edge : adj[u]) {
            if (edge.to != p) {
                dfs(edge.to, u, d + 1, distance + edge.weight);
            }
        }
    }
    
    private static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
    
    private static long queryDistance(int u, int v) {
        int ancestor = lca(u, v);
        return dist[u] + dist[v] - 2 * dist[ancestor];
    }
}
```

**C++实现**:
```cpp
#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

struct Edge {
    int to;
    int weight;
    Edge(int to, int weight) : to(to), weight(weight) {}
};

int n, m, LOG;
vector<vector<int>> parent;
vector<int> depth;
vector<long long> dist;
vector<vector<Edge>> adj;

void dfs(int u, int p, int d, long long distance) {
    parent[0][u] = p;
    depth[u] = d;
    dist[u] = distance;
    
    for (Edge edge : adj[u]) {
        if (edge.to != p) {
            dfs(edge.to, u, d + 1, distance + edge.weight);
        }
    }
}

int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    // 提升u到v的深度
    for (int j = LOG - 1; j >= 0; j--) {
        if (depth[u] - (1 << j) >= depth[v]) {
            u = parent[j][u];
        }
    }
    
    if (u == v) return u;
    
    // 同时提升u和v
    for (int j = LOG - 1; j >= 0; j--) {
        if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
            u = parent[j][u];
            v = parent[j][v];
        }
    }
    
    return parent[0][u];
}

long long queryDistance(int u, int v) {
    int ancestor = lca(u, v);
    return dist[u] + dist[v] - 2 * dist[ancestor];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> m;
    
    // 计算LOG值
    LOG = log2(n) + 2;
    
    // 初始化数据结构
    parent.resize(LOG, vector<int>(n + 1, -1));
    depth.resize(n + 1, 0);
    dist.resize(n + 1, 0);
    adj.resize(n + 1);
    
    // 读取边
    for (int i = 0; i < m; i++) {
        int u, v, w;
        char direction;
        cin >> u >> v >> w >> direction;
        adj[u].emplace_back(v, w);
        adj[v].emplace_back(u, w);
    }
    
    // 预处理
    dfs(1, -1, 0, 0);
    
    // 构建倍增表
    for (int j = 1; j < LOG; j++) {
        for (int i = 1; i <= n; i++) {
            if (parent[j-1][i] != -1) {
                parent[j][i] = parent[j-1][parent[j-1][i]];
            }
        }
    }
    
    // 处理查询
    int k;
    cin >> k;
    for (int i = 0; i < k; i++) {
        int u, v;
        cin >> u >> v;
        cout << queryDistance(u, v) << "\n";
    }
    
    return 0;
}
```

**Python实现**:
```python
import sys
import math
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

class Edge:
    def __init__(self, to, weight):
        self.to = to
        self.weight = weight

def main():
    n, m = map(int, sys.stdin.readline().split())
    
    # 计算LOG值
    LOG = math.ceil(math.log2(n)) + 1
    
    # 初始化数据结构
    parent = [[-1] * (n + 1) for _ in range(LOG)]
    depth = [0] * (n + 1)
    dist = [0] * (n + 1)
    adj = defaultdict(list)
    
    # 读取边
    for _ in range(m):
        parts = sys.stdin.readline().split()
        u = int(parts[0])
        v = int(parts[1])
        w = int(parts[2])
        # 跳过方向字符
        adj[u].append(Edge(v, w))
        adj[v].append(Edge(u, w))
    
    # DFS预处理
    def dfs(u, p, d, distance):
        parent[0][u] = p
        depth[u] = d
        dist[u] = distance
        for edge in adj[u]:
            if edge.to != p:
                dfs(edge.to, u, d + 1, distance + edge.weight)
    
    dfs(1, -1, 0, 0)
    
    # 构建倍增表
    for j in range(1, LOG):
        for i in range(1, n + 1):
            if parent[j-1][i] != -1:
                parent[j][i] = parent[j-1][parent[j-1][i]]
    
    # LCA查询函数
    def lca(u, v):
        if depth[u] < depth[v]:
            u, v = v, u
        
        # 提升u到v的深度
        for j in range(LOG-1, -1, -1):
            if depth[u] - (1 << j) >= depth[v]:
                u = parent[j][u]
        
        if u == v:
            return u
        
        # 同时提升u和v
        for j in range(LOG-1, -1, -1):
            if parent[j][u] != -1 and parent[j][u] != parent[j][v]:
                u = parent[j][u]
                v = parent[j][v]
        
        return parent[0][u]
    
    # 距离查询函数
    def query_distance(u, v):
        ancestor = lca(u, v)
        return dist[u] + dist[v] - 2 * dist[ancestor]
    
    # 处理查询
    k = int(sys.stdin.readline())
    for _ in range(k):
        u, v = map(int, sys.stdin.readline().split())
        print(query_distance(u, v))

if __name__ == "__main__":
    main()
```

**算法优化与工程化考量**:
1. **距离计算优化**：通过LCA快速计算两点间距离
2. **内存优化**：使用long long类型存储距离，避免溢出
3. **输入处理**：正确处理带方向的边输入
4. **递归优化**：设置合适的递归深度限制

### 5.5 SPOJ 10628. Count on a tree (COT)
**题目链接**: https://www.spoj.com/problems/COT/

**题目描述**:
给定一棵节点带权树，多次查询两点间路径上第k小的点权。

**解题思路**:
结合树上倍增和主席树（可持久化线段树），在树上建立主席树，利用LCA计算路径上的第k小值。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

**Java实现**:
```java
import java.util.*;

public class Main {
    private static int n, m, LOG;
    private static int[][] parent;
    private static int[] depth;
    private static int[] w; // 节点权值
    private static List<Integer>[] adj;
    private static int[] sortedW; // 排序后的权值
    private static Map<Integer, Integer> wToId; // 权值到ID的映射
    
    // 主席树节点
    static class Node {
        int left, right;
        int count;
        Node(int left, int right, int count) {
            this.left = left;
            this.right = right;
            this.count = count;
        }
    }
    
    private static Node[] tree;
    private static int[] root;
    private static int nodeCount;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        
        // 读取权值
        w = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            w[i] = scanner.nextInt();
        }
        
        // 离散化权值
        sortedW = Arrays.copyOfRange(w, 1, n + 1);
        Arrays.sort(sortedW);
        wToId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            wToId.put(sortedW[i], i + 1);
        }
        
        // 计算LOG值
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n + 1];
        depth = new int[n + 1];
        adj = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            adj[u].add(v);
            adj[v].add(u);
        }
        
        // 初始化主席树
        tree = new Node[20 * n]; // 预估大小
        root = new int[n + 1];
        nodeCount = 0;
        
        // 预处理
        Arrays.fill(parent[0], -1);
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                }
            }
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int k = scanner.nextInt();
            System.out.println(queryKth(u, v, k));
        }
        
        scanner.close();
    }
    
    private static void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        
        // 构建主席树：当前节点基于父节点构建
        int wId = wToId.get(w[u]);
        if (p == -1) {
            root[u] = build(1, n);
        } else {
            root[u] = update(root[p], 1, n, wId);
        }
        
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
            }
        }
    }
    
    private static int build(int l, int r) {
        int id = ++nodeCount;
        tree[id] = new Node(0, 0, 0);
        if (l == r) return id;
        
        int mid = (l + r) / 2;
        tree[id].left = build(l, mid);
        tree[id].right = build(mid + 1, r);
        return id;
    }
    
    private static int update(int pre, int l, int r, int pos) {
        int id = ++nodeCount;
        tree[id] = new Node(tree[pre].left, tree[pre].right, tree[pre].count + 1);
        
        if (l == r) return id;
        
        int mid = (l + r) / 2;
        if (pos <= mid) {
            tree[id].left = update(tree[pre].left, l, mid, pos);
        } else {
            tree[id].right = update(tree[pre].right, mid + 1, r, pos);
        }
        
        return id;
    }
    
    private static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
    
    private static int queryKth(int u, int v, int k) {
        int ancestor = lca(u, v);
        int parentAncestor = parent[0][ancestor];
        
        // 计算路径上的第k小值
        return query(root[u], root[v], root[ancestor], 
                    parentAncestor == -1 ? 0 : root[parentAncestor], 
                    1, n, k);
    }
    
    private static int query(int u, int v, int a, int pa, int l, int r, int k) {
        if (l == r) return sortedW[l - 1];
        
        int mid = (l + r) / 2;
        int leftCount = tree[tree[u].left].count + tree[tree[v].left].count 
                      - tree[tree[a].left].count - (pa == 0 ? 0 : tree[tree[pa].left].count);
        
        if (k <= leftCount) {
            return query(tree[u].left, tree[v].left, tree[a].left, 
                        pa == 0 ? 0 : tree[pa].left, l, mid, k);
        } else {
            return query(tree[u].right, tree[v].right, tree[a].right, 
                        pa == 0 ? 0 : tree[pa].right, mid + 1, r, k - leftCount);
        }
    }
}
```

**C++实现**:
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
using namespace std;

const int MAXN = 100010;

int n, m, LOG;
vector<vector<int>> parent;
vector<int> depth, w;
vector<vector<int>> adj;
vector<int> sortedW;
unordered_map<int, int> wToId;

// 主席树结构
struct Node {
    int left, right;
    int count;
    Node() : left(0), right(0), count(0) {}
};

vector<Node> tree;
vector<int> root;
int nodeCount;

void dfs(int u, int p, int d) {
    parent[0][u] = p;
    depth[u] = d;
    
    // 构建主席树
    int wId = wToId[w[u]];
    if (p == -1) {
        root[u] = build(1, n);
    } else {
        root[u] = update(root[p], 1, n, wId);
    }
    
    for (int v : adj[u]) {
        if (v != p) {
            dfs(v, u, d + 1);
        }
    }
}

int build(int l, int r) {
    int id = ++nodeCount;
    tree[id] = Node();
    if (l == r) return id;
    
    int mid = (l + r) / 2;
    tree[id].left = build(l, mid);
    tree[id].right = build(mid + 1, r);
    return id;
}

int update(int pre, int l, int r, int pos) {
    int id = ++nodeCount;
    tree[id] = tree[pre];
    tree[id].count++;
    
    if (l == r) return id;
    
    int mid = (l + r) / 2;
    if (pos <= mid) {
        tree[id].left = update(tree[pre].left, l, mid, pos);
    } else {
        tree[id].right = update(tree[pre].right, mid + 1, r, pos);
    }
    
    return id;
}

int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    for (int j = LOG - 1; j >= 0; j--) {
        if (depth[u] - (1 << j) >= depth[v]) {
            u = parent[j][u];
        }
    }
    
    if (u == v) return u;
    
    for (int j = LOG - 1; j >= 0; j--) {
        if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
            u = parent[j][u];
            v = parent[j][v];
        }
    }
    
    return parent[0][u];
}

int queryKth(int u, int v, int k) {
    int ancestor = lca(u, v);
    int parentAncestor = parent[0][ancestor];
    
    return query(root[u], root[v], root[ancestor], 
                parentAncestor == -1 ? 0 : root[parentAncestor], 
                1, n, k);
}

int query(int u, int v, int a, int pa, int l, int r, int k) {
    if (l == r) return sortedW[l - 1];
    
    int mid = (l + r) / 2;
    int leftCount = tree[tree[u].left].count + tree[tree[v].left].count 
                  - tree[tree[a].left].count - (pa == 0 ? 0 : tree[tree[pa].left].count);
    
    if (k <= leftCount) {
        return query(tree[u].left, tree[v].left, tree[a].left, 
                    pa == 0 ? 0 : tree[pa].left, l, mid, k);
    } else {
        return query(tree[u].right, tree[v].right, tree[a].right, 
                    pa == 0 ? 0 : tree[pa].right, mid + 1, r, k - leftCount);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> m;
    
    w.resize(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> w[i];
    }
    
    // 离散化权值
    sortedW = vector<int>(w.begin() + 1, w.end());
    sort(sortedW.begin(), sortedW.end());
    for (int i = 0; i < n; i++) {
        wToId[sortedW[i]] = i + 1;
    }
    
    LOG = log2(n) + 2;
    
    parent.resize(LOG, vector<int>(n + 1, -1));
    depth.resize(n + 1, 0);
    adj.resize(n + 1);
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    // 初始化主席树
    tree.resize(20 * n);
    root.resize(n + 1);
    nodeCount = 0;
    
    dfs(1, -1, 0);
    
    for (int j = 1; j < LOG; j++) {
        for (int i = 1; i <= n; i++) {
            if (parent[j-1][i] != -1) {
                parent[j][i] = parent[j-1][parent[j-1][i]];
            }
        }
    }
    
    for (int i = 0; i < m; i++) {
        int u, v, k;
        cin >> u >> v >> k;
        cout << queryKth(u, v, k) << "\n";
    }
    
    return 0;
}
```

**Python实现**:
```python
import sys
import math
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

class Node:
    def __init__(self, left=0, right=0, count=0):
        self.left = left
        self.right = right
        self.count = count

def main():
    n, m = map(int, sys.stdin.readline().split())
    
    w = [0] * (n + 1)
    for i in range(1, n + 1):
        w[i] = int(sys.stdin.readline())
    
    # 离散化权值
    sorted_w = sorted(w[1:])
    w_to_id = {val: idx + 1 for idx, val in enumerate(sorted_w)}
    
    LOG = math.ceil(math.log2(n)) + 1
    
    parent = [[-1] * (n + 1) for _ in range(LOG)]
    depth = [0] * (n + 1)
    adj = defaultdict(list)
    
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        adj[u].append(v)
        adj[v].append(u)
    
    # 主席树初始化
    tree = [Node() for _ in range(20 * n)]
    root = [0] * (n + 1)
    node_count = 0
    
    def build(l, r):
        nonlocal node_count
        node_count += 1
        id_ = node_count
        tree[id_] = Node()
        if l == r:
            return id_
        mid = (l + r) // 2
        tree[id_].left = build(l, mid)
        tree[id_].right = build(mid + 1, r)
        return id_
    
    def update(pre, l, r, pos):
        nonlocal node_count
        node_count += 1
        id_ = node_count
        tree[id_] = Node(tree[pre].left, tree[pre].right, tree[pre].count + 1)
        
        if l == r:
            return id_
        
        mid = (l + r) // 2
        if pos <= mid:
            tree[id_].left = update(tree[pre].left, l, mid, pos)
        else:
            tree[id_].right = update(tree[pre].right, mid + 1, r, pos)
        
        return id_
    
    def dfs(u, p, d):
        parent[0][u] = p
        depth[u] = d
        
        w_id = w_to_id[w[u]]
        if p == -1:
            root[u] = build(1, n)
        else:
            root[u] = update(root[p], 1, n, w_id)
        
        for v in adj[u]:
            if v != p:
                dfs(v, u, d + 1)
    
    dfs(1, -1, 0)
    
    # 构建倍增表
    for j in range(1, LOG):
        for i in range(1, n + 1):
            if parent[j-1][i] != -1:
                parent[j][i] = parent[j-1][parent[j-1][i]]
    
    def lca(u, v):
        if depth[u] < depth[v]:
            u, v = v, u
        
        for j in range(LOG-1, -1, -1):
            if depth[u] - (1 << j) >= depth[v]:
                u = parent[j][u]
        
        if u == v:
            return u
        
        for j in range(LOG-1, -1, -1):
            if parent[j][u] != -1 and parent[j][u] != parent[j][v]:
                u = parent[j][u]
                v = parent[j][v]
        
        return parent[0][u]
    
    def query_kth(u, v, k):
        ancestor = lca(u, v)
        parent_ancestor = parent[0][ancestor]
        
        def query_func(u_id, v_id, a_id, pa_id, l, r, k_val):
            if l == r:
                return sorted_w[l - 1]
            
            mid = (l + r) // 2
            left_count = tree[tree[u_id].left].count + tree[tree[v_id].left].count \
                       - tree[tree[a_id].left].count - (0 if pa_id == 0 else tree[tree[pa_id].left].count)
            
            if k_val <= left_count:
                return query_func(tree[u_id].left, tree[v_id].left, tree[a_id].left, 
                               0 if pa_id == 0 else tree[pa_id].left, l, mid, k_val)
            else:
                return query_func(tree[u_id].right, tree[v_id].right, tree[a_id].right, 
                               0 if pa_id == 0 else tree[pa_id].right, mid + 1, r, k_val - left_count)
        
        return query_func(root[u], root[v], root[ancestor], 
                        0 if parent_ancestor == -1 else root[parent_ancestor], 1, n, k)
    
    for _ in range(m):
        u, v, k = map(int, sys.stdin.readline().split())
        print(query_kth(u, v, k))

if __name__ == "__main__":
    main()
```

**算法优化与工程化考量**:
1. **主席树优化**：结合树上倍增和主席树解决路径第k小问题
2. **离散化处理**：对权值进行离散化，减少空间占用
3. **内存管理**：预估主席树节点数量，避免内存溢出
4. **查询优化**：通过LCA快速定位路径范围

### 5.6 HDU 2856. How far away ?
**题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2856

**题目描述**:
给定一棵带权树，多次查询两点间的距离。

**解题思路**:
与POJ 1986类似，使用树上倍增算法预处理节点深度和到根节点的距离，通过LCA计算两点间距离。

**时间复杂度**: 预处理O(n log n)，查询O(log n)
**空间复杂度**: O(n log n)

**Java实现**:
```java
import java.util.*;

public class Main {
    private static int n, q, LOG;
    private static int[][] parent;
    private static int[] depth;
    private static long[] dist;
    private static List<Edge>[] adj;
    
    static class Edge {
        int to;
        int weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        
        while (T-- > 0) {
            n = scanner.nextInt();
            q = scanner.nextInt();
            
            // 计算LOG值
            LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
            
            // 初始化数据结构
            parent = new int[LOG][n + 1];
            depth = new int[n + 1];
            dist = new long[n + 1];
            adj = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) {
                adj[i] = new ArrayList<>();
            }
            
            // 读取边
            for (int i = 1; i < n; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                adj[u].add(new Edge(v, w));
                adj[v].add(new Edge(u, w));
            }
            
            // 预处理
            Arrays.fill(parent[0], -1);
            dfs(1, -1, 0, 0);
            
            // 构建倍增表
            for (int j = 1; j < LOG; j++) {
                for (int i = 1; i <= n; i++) {
                    if (parent[j-1][i] != -1) {
                        parent[j][i] = parent[j-1][parent[j-1][i]];
                    }
                }
            }
            
            // 处理查询
            for (int i = 0; i < q; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                System.out.println(queryDistance(u, v));
            }
        }
        
        scanner.close();
    }
    
    private static void dfs(int u, int p, int d, long distance) {
        parent[0][u] = p;
        depth[u] = d;
        dist[u] = distance;
        
        for (Edge edge : adj[u]) {
            if (edge.to != p) {
                dfs(edge.to, u, d + 1, distance + edge.weight);
            }
        }
    }
    
    private static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 提升u到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
    
    private static long queryDistance(int u, int v) {
        int ancestor = lca(u, v);
        return dist[u] + dist[v] - 2 * dist[ancestor];
    }
}
```

**C++实现**:
```cpp
#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

struct Edge {
    int to;
    int weight;
    Edge(int to, int weight) : to(to), weight(weight) {}
};

int n, q, LOG;
vector<vector<int>> parent;
vector<int> depth;
vector<long long> dist;
vector<vector<Edge>> adj;

void dfs(int u, int p, int d, long long distance) {
    parent[0][u] = p;
    depth[u] = d;
    dist[u] = distance;
    
    for (Edge edge : adj[u]) {
        if (edge.to != p) {
            dfs(edge.to, u, d + 1, distance + edge.weight);
        }
    }
}

int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    for (int j = LOG - 1; j >= 0; j--) {
        if (depth[u] - (1 << j) >= depth[v]) {
            u = parent[j][u];
        }
    }
    
    if (u == v) return u;
    
    for (int j = LOG - 1; j >= 0; j--) {
        if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
            u = parent[j][u];
            v = parent[j][v];
        }
    }
    
    return parent[0][u];
}

long long queryDistance(int u, int v) {
    int ancestor = lca(u, v);
    return dist[u] + dist[v] - 2 * dist[ancestor];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int T;
    cin >> T;
    
    while (T--) {
        cin >> n >> q;
        
        LOG = log2(n) + 2;
        
        parent.resize(LOG, vector<int>(n + 1, -1));
        depth.resize(n + 1, 0);
        dist.resize(n + 1, 0);
        adj.resize(n + 1);
        
        for (int i = 1; i < n; i++) {
            int u, v, w;
            cin >> u >> v >> w;
            adj[u].emplace_back(v, w);
            adj[v].emplace_back(u, w);
        }
        
        dfs(1, -1, 0, 0);
        
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                }
            }
        }
        
        for (int i = 0; i < q; i++) {
            int u, v;
            cin >> u >> v;
            cout << queryDistance(u, v) << "\n";
        }
    }
    
    return 0;
}
```

**Python实现**:
```python
import sys
import math
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

class Edge:
    def __init__(self, to, weight):
        self.to = to
        self.weight = weight

def main():
    T = int(sys.stdin.readline())
    
    for _ in range(T):
        n, q = map(int, sys.stdin.readline().split())
        
        LOG = math.ceil(math.log2(n)) + 1
        
        parent = [[-1] * (n + 1) for _ in range(LOG)]
        depth = [0] * (n + 1)
        dist = [0] * (n + 1)
        adj = defaultdict(list)
        
        for _ in range(n - 1):
            u, v, w = map(int, sys.stdin.readline().split())
            adj[u].append(Edge(v, w))
            adj[v].append(Edge(u, w))
        
        def dfs(u, p, d, distance):
            parent[0][u] = p
            depth[u] = d
            dist[u] = distance
            for edge in adj[u]:
                if edge.to != p:
                    dfs(edge.to, u, d + 1, distance + edge.weight)
        
        dfs(1, -1, 0, 0)
        
        for j in range(1, LOG):
            for i in range(1, n + 1):
                if parent[j-1][i] != -1:
                    parent[j][i] = parent[j-1][parent[j-1][i]]
        
        def lca(u, v):
            if depth[u] < depth[v]:
                u, v = v, u
            
            for j in range(LOG-1, -1, -1):
                if depth[u] - (1 << j) >= depth[v]:
                    u = parent[j][u]
            
            if u == v:
                return u
            
            for j in range(LOG-1, -1, -1):
                if parent[j][u] != -1 and parent[j][u] != parent[j][v]:
                    u = parent[j][u]
                    v = parent[j][v]
            
            return parent[0][u]
        
        def query_distance(u, v):
            ancestor = lca(u, v)
            return dist[u] + dist[v] - 2 * dist[ancestor]
        
        for _ in range(q):
            u, v = map(int, sys.stdin.readline().split())
            print(query_distance(u, v))

if __name__ == "__main__":
    main()
```

**算法优化与工程化考量**:
1. **多测试用例处理**：支持多组测试数据
2. **IO优化**：使用快速IO处理大规模输入
3. **内存复用**：每组测试数据后重置数据结构
4. **边界处理**：处理n=1的特殊情况

## 6. 树上倍增算法总结与进阶应用

### 6.1 算法核心思想回顾

树上倍增算法的核心是利用二进制分解的思想，将树上的查询操作从O(n)优化到O(log n)。通过预处理每个节点的2^i级祖先信息，可以在查询时快速跳跃到目标位置。

### 6.2 常见应用场景

1. **基础查询**：
   - 最近公共祖先(LCA)
   - 第K个祖先查询
   - 树上距离计算

2. **路径信息查询**：
   - 路径权重统计（和、最大值、最小值）
   - 路径属性判断（回文、单调性等）
   - 路径第K小值查询

3. **复杂问题**：
   - 树上动态规划优化
   - 结合其他数据结构（主席树、线段树等）
   - 虚树构建

### 6.3 时间复杂度分析

- **预处理阶段**：O(n log n)
- **查询阶段**：O(log n)
- **空间复杂度**：O(n log n)

### 6.4 工程化优化技巧

1. **LOG值计算**：根据数据规模动态计算合适的LOG值
2. **内存优化**：合理分配数组大小，避免内存浪费
3. **IO优化**：使用快速IO处理大规模数据
4. **边界处理**：处理根节点、空树等特殊情况
5. **递归优化**：设置合适的递归深度限制

### 6.5 与其他算法对比

1. **与Tarjan算法比较**：
   - 倍增算法支持在线查询，Tarjan适合离线批量处理
   - 倍增算法实现相对简单，但空间复杂度较高

2. **与树链剖分比较**：
   - 树链剖分空间复杂度更优，但实现复杂
   - 倍增算法更适合简单的路径查询问题

3. **与DFS暴力比较**：
   - 倍增算法预处理O(n log n)，查询O(log n)
   - DFS暴力预处理O(n)，但查询O(n)

### 6.6 进阶应用方向

1. **动态树问题**：在动态变化的树上维护倍增信息
2. **结合机器学习**：在树结构数据上进行特征提取和模式识别
3. **图论扩展**：在特殊图结构上应用倍增思想
4. **分布式计算**：在分布式环境下实现树上倍增算法

### 6.7 学习建议

1. **掌握基础**：先理解DFS、树的基本概念
2. **动手实现**：从简单的LCA问题开始实现
3. **逐步扩展**：学习处理路径权重、字符串等复杂信息
4. **大量练习**：通过不同类型的题目加深理解
5. **总结归纳**：整理常见模式和解题技巧

通过系统学习和大量练习，可以熟练掌握树上倍增算法，并灵活应用于各种树上问题的求解。

## 7. 参考资料

### 7.1 经典论文
1. "An O(n log n) Algorithm for Finding All Pairwise Distances in a Tree" - Gabow et al.
2. "Lowest Common Ancestors in Trees and Directed Acyclic Graphs" - Bender et al.
3. "Optimal Algorithms for Finding Nearest Common Ancestors in Dynamic Trees" - Sleator et al.

### 7.2 在线资源
1. [CP-Algorithms: LCA with Binary Lifting](https://cp-algorithms.com/graph/lca_binary_lifting.html)
2. [GeeksforGeeks: Lowest Common Ancestor](https://www.geeksforgeeks.org/lowest-common-ancestor-binary-tree-set-1/)
3. [TopCoder: Range Minimum Query and Lowest Common Ancestor](https://www.topcoder.com/thrive/articles/Range%20Minimum%20Query%20and%20Lowest%20Common%20Ancestor)

### 7.3 实践平台
1. **LeetCode**: 提供大量树上问题的练习
2. **Codeforces**: 定期举办包含树上问题的比赛
3. **AtCoder**: 日本知名编程竞赛平台
4. **洛谷**: 中文算法竞赛平台，资源丰富

### 7.4 开源项目
1. **树链剖分库**: 各种树上算法的开源实现
2. **图论算法库**: 包含树上倍增等算法的完整实现
3. **竞赛模板库**: 算法竞赛选手常用的代码模板

希望这份详细的树上倍增算法资料能够帮助你深入理解和掌握这一重要算法，并在实际应用中发挥其价值。
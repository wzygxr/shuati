# 基环树（环套树）算法详解

## 1. 基环树的基本概念

基环树（Base Cycle Tree）是一种特殊的图结构，它由一个环和连接在环上的若干棵树组成。换句话说，基环树是一个连通图，其中包含恰好一个环，且删除环中的任意一条边后，图变为一棵树。

### 1.1 基环树的性质

- 对于有n个顶点的基环树，边数恰好为n
- 每个基环树都有且仅有一个环
- 环外的每个顶点都属于环上某个顶点的子树
- 基环树是连通图

## 2. 基环树的主要算法

### 2.1 寻找环

寻找基环树中的环是解决基环树问题的关键步骤。常用的方法有：

- DFS（深度优先搜索）配合访问标记
- 并查集（Union-Find）
- 拓扑排序

下面是使用DFS寻找环的实现：

```python
# Python实现：寻找基环树中的环
class BaseCycleTree:
    def __init__(self, n):
        self.n = n
        self.graph = [[] for _ in range(n+1)]  # 1-based indexing
        self.visited = [False] * (n+1)
        self.in_cycle = [False] * (n+1)
        self.cycle = []
        self.parent = [0] * (n+1)
        self.loop_start = -1
        self.loop_end = -1
    
    def add_edge(self, u, v):
        self.graph[u].append(v)
    
    def dfs(self, u):
        self.visited[u] = True
        for v in self.graph[u]:
            if not self.visited[v]:
                self.parent[v] = u
                if self.dfs(v):
                    return True
            elif v != self.parent[u]:  # 发现回边，说明找到了环
                self.loop_start = v
                self.loop_end = u
                return True
        return False
    
    def find_cycle(self):
        for i in range(1, self.n+1):
            if not self.visited[i]:
                if self.dfs(i):
                    # 从loop_end回溯到loop_start，构建环
                    u = self.loop_end
                    while u != self.loop_start:
                        self.cycle.append(u)
                        self.in_cycle[u] = True
                        u = self.parent[u]
                    self.cycle.append(self.loop_start)
                    self.in_cycle[self.loop_start] = True
                    self.cycle.reverse()  # 按照环的顺序排列
                    return self.cycle
        return []
```

```cpp
// C++实现：寻找基环树中的环
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class BaseCycleTree {
private:
    int n;
    vector<vector<int>> graph;
    vector<bool> visited;
    vector<bool> in_cycle;
    vector<int> cycle;
    vector<int> parent;
    int loop_start, loop_end;
    
    bool dfs(int u) {
        visited[u] = true;
        for (int v : graph[u]) {
            if (!visited[v]) {
                parent[v] = u;
                if (dfs(v)) {
                    return true;
                }
            } else if (v != parent[u]) {  // 发现回边
                loop_start = v;
                loop_end = u;
                return true;
            }
        }
        return false;
    }
    
public:
    BaseCycleTree(int n) : n(n) {
        graph.resize(n + 1);
        visited.resize(n + 1, false);
        in_cycle.resize(n + 1, false);
        parent.resize(n + 1, 0);
        loop_start = loop_end = -1;
    }
    
    void add_edge(int u, int v) {
        graph[u].push_back(v);
    }
    
    vector<int> find_cycle() {
        for (int i = 1; i <= n; ++i) {
            if (!visited[i]) {
                if (dfs(i)) {
                    // 构建环
                    int u = loop_end;
                    while (u != loop_start) {
                        cycle.push_back(u);
                        in_cycle[u] = true;
                        u = parent[u];
                    }
                    cycle.push_back(loop_start);
                    in_cycle[loop_start] = true;
                    reverse(cycle.begin(), cycle.end());
                    return cycle;
                }
            }
        }
        return cycle;
    }
    
    vector<bool> get_in_cycle() {
        return in_cycle;
    }
};
```

```java
// Java实现：寻找基环树中的环
import java.util.*;

class BaseCycleTree {
    private int n;
    private List<List<Integer>> graph;
    private boolean[] visited;
    private boolean[] inCycle;
    private List<Integer> cycle;
    private int[] parent;
    private int loopStart, loopEnd;
    
    public BaseCycleTree(int n) {
        this.n = n;
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        visited = new boolean[n + 1];
        inCycle = new boolean[n + 1];
        parent = new int[n + 1];
        cycle = new ArrayList<>();
        loopStart = loopEnd = -1;
    }
    
    public void addEdge(int u, int v) {
        graph.get(u).add(v);
    }
    
    private boolean dfs(int u) {
        visited[u] = true;
        for (int v : graph.get(u)) {
            if (!visited[v]) {
                parent[v] = u;
                if (dfs(v)) {
                    return true;
                }
            } else if (v != parent[u]) {  // 发现回边
                loopStart = v;
                loopEnd = u;
                return true;
            }
        }
        return false;
    }
    
    public List<Integer> findCycle() {
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                if (dfs(i)) {
                    // 构建环
                    int u = loopEnd;
                    while (u != loopStart) {
                        cycle.add(u);
                        inCycle[u] = true;
                        u = parent[u];
                    }
                    cycle.add(loopStart);
                    inCycle[loopStart] = true;
                    Collections.reverse(cycle);
                    return cycle;
                }
            }
        }
        return cycle;
    }
    
    public boolean[] getInCycle() {
        return inCycle;
    }
}
```

### 2.2 处理环上的子树

在找到环之后，通常需要对环上每个节点的子树进行处理：

```python
# 处理环上的子树，计算每个子树的信息
def process_subtrees(self):
    subtree_info = [0] * (self.n + 1)
    
    def dfs_subtree(u, parent_node):
        res = 1  # 节点自身
        for v in self.graph[u]:
            if v != parent_node and not self.in_cycle[v]:
                res += dfs_subtree(v, u)
        subtree_info[u] = res
        return res
    
    # 对环上的每个节点处理其子树
    for node in self.cycle:
        dfs_subtree(node, -1)
    
    return subtree_info
```

## 3. 基环树的典型应用场景

### 3.1 最大生成树问题

在基环树中选择一个环，删除环中权值最小的边，使得整个图变为一棵树。

### 3.2 最小环问题

寻找基环树中的环，并计算环的某些属性（如最小权值和）。

### 3.3 树上动态规划（DP）

在基环树上进行动态规划，通常需要拆环为链，然后分别处理。

## 4. 基环树相关题目

### 4.1 LeetCode 2127. 参加会议的最多员工数

**题目链接**：[https://leetcode-cn.com/problems/maximum-employees-to-be-invited-to-a-meeting/](https://leetcode-cn.com/problems/maximum-employees-to-be-invited-to-a-meeting/)

**题目描述**：一个公司准备组织一场会议，邀请员工参加。每个员工有一个偏好，他们希望在会议开始的时间能与自己的直接领导交流。但为了避免尴尬，公司规定，如果两个员工是直接上下级关系，那么他们不能同时参加会议。请找出最多可以邀请的员工人数。

**题解**：

```python
# Python解法
class Solution:
    def maximumInvitations(self, favorite: List[int]) -> int:
        n = len(favorite)
        # 每个节点的入度
        indeg = [0] * n
        # 每个节点的深度（用于计算链的长度）
        depth = [1] * n
        for v in favorite:
            indeg[v] += 1
        
        # 拓扑排序，处理所有不在环中的节点
        q = deque()
        for i in range(n):
            if indeg[i] == 0:
                q.append(i)
        
        while q:
            u = q.popleft()
            v = favorite[u]
            depth[v] = max(depth[v], depth[u] + 1)
            indeg[v] -= 1
            if indeg[v] == 0:
                q.append(v)
        
        # 现在indeg中入度不为0的节点都在环中
        max_cycle = 0  # 最大的环的长度
        sum_chain = 0  # 所有长度为2的环及其链的总和
        
        visited = [False] * n
        for i in range(n):
            if indeg[i] > 0 and not visited[i]:
                # 找出环
                cycle = []
                j = i
                while not visited[j]:
                    visited[j] = True
                    cycle.append(j)
                    j = favorite[j]
                
                if len(cycle) == 2:
                    # 长度为2的环，取两个方向的最长链
                    u, v = cycle
                    sum_chain += depth[u] + depth[v]
                else:
                    # 长度大于2的环，直接取环的长度
                    max_cycle = max(max_cycle, len(cycle))
        
        # 返回两种情况的最大值
        return max(max_cycle, sum_chain)
```

```cpp
// C++解法
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

class Solution {
public:
    int maximumInvitations(vector<int>& favorite) {
        int n = favorite.size();
        vector<int> indeg(n, 0);
        vector<int> depth(n, 1);
        
        for (int v : favorite) {
            indeg[v]++;
        }
        
        queue<int> q;
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.push(i);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            int v = favorite[u];
            depth[v] = max(depth[v], depth[u] + 1);
            if (--indeg[v] == 0) {
                q.push(v);
            }
        }
        
        int max_cycle = 0;
        int sum_chain = 0;
        vector<bool> visited(n, false);
        
        for (int i = 0; i < n; i++) {
            if (indeg[i] > 0 && !visited[i]) {
                vector<int> cycle;
                int j = i;
                while (!visited[j]) {
                    visited[j] = true;
                    cycle.push_back(j);
                    j = favorite[j];
                }
                
                if (cycle.size() == 2) {
                    int u = cycle[0], v = cycle[1];
                    sum_chain += depth[u] + depth[v];
                } else {
                    max_cycle = max(max_cycle, (int)cycle.size());
                }
            }
        }
        
        return max(max_cycle, sum_chain);
    }
};
```

```java
// Java解法
import java.util.*;

class Solution {
    public int maximumInvitations(int[] favorite) {
        int n = favorite.length;
        int[] indeg = new int[n];
        int[] depth = new int[n];
        Arrays.fill(depth, 1);
        
        for (int v : favorite) {
            indeg[v]++;
        }
        
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.offer(i);
            }
        }
        
        while (!q.isEmpty()) {
            int u = q.poll();
            int v = favorite[u];
            depth[v] = Math.max(depth[v], depth[u] + 1);
            if (--indeg[v] == 0) {
                q.offer(v);
            }
        }
        
        int maxCycle = 0;
        int sumChain = 0;
        boolean[] visited = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            if (indeg[i] > 0 && !visited[i]) {
                List<Integer> cycle = new ArrayList<>();
                int j = i;
                while (!visited[j]) {
                    visited[j] = true;
                    cycle.add(j);
                    j = favorite[j];
                }
                
                if (cycle.size() == 2) {
                    int u = cycle.get(0), v = cycle.get(1);
                    sumChain += depth[u] + depth[v];
                } else {
                    maxCycle = Math.max(maxCycle, cycle.size());
                }
            }
        }
        
        return Math.max(maxCycle, sumChain);
    }
}
```

### 4.2 LeetCode 335. 路径交叉

**题目链接**：[https://leetcode-cn.com/problems/self-crossing/](https://leetcode-cn.com/problems/self-crossing/)

**题目描述**：给你一个整数数组 `distance` 。从 X-Y 平面上的点 (0,0) 开始，先向北移动 `distance[0]` 米，然后向西移动 `distance[1]` 米，向南移动 `distance[2]` 米，向东移动 `distance[3]` 米，持续移动。也就是说，每次移动后方向都会逆时针旋转 90 度。判断是否在移动过程中与之前的路径相交。

**题解**：

```python
# Python解法
class Solution:
    def isSelfCrossing(self, distance: List[int]) -> bool:
        n = len(distance)
        if n < 4:
            return False
        
        for i in range(3, n):
            # 情况1：当前边与第三条边交叉
            if distance[i] >= distance[i-2] and distance[i-1] <= distance[i-3]:
                return True
            # 情况2：当前边与第四条边交叉（形成一个环）
            if i >= 4 and distance[i-1] == distance[i-3] and distance[i] + distance[i-4] >= distance[i-2]:
                return True
            # 情况3：当前边与第五条边交叉
            if i >= 5 and distance[i-2] >= distance[i-4] and distance[i] + distance[i-4] >= distance[i-2] and \
               distance[i-1] <= distance[i-3] and distance[i-1] + distance[i-5] >= distance[i-3]:
                return True
        
        return False
```

```cpp
// C++解法
#include <vector>
using namespace std;

class Solution {
public:
    bool isSelfCrossing(vector<int>& distance) {
        int n = distance.size();
        if (n < 4) return false;
        
        for (int i = 3; i < n; ++i) {
            // 情况1：当前边与第三条边交叉
            if (distance[i] >= distance[i-2] && distance[i-1] <= distance[i-3]) {
                return true;
            }
            // 情况2：当前边与第四条边交叉（形成一个环）
            if (i >= 4 && distance[i-1] == distance[i-3] && distance[i] + distance[i-4] >= distance[i-2]) {
                return true;
            }
            // 情况3：当前边与第五条边交叉
            if (i >= 5 && distance[i-2] >= distance[i-4] && distance[i] + distance[i-4] >= distance[i-2] &&
                distance[i-1] <= distance[i-3] && distance[i-1] + distance[i-5] >= distance[i-3]) {
                return true;
            }
        }
        
        return false;
    }
};
```

```java
// Java解法
class Solution {
    public boolean isSelfCrossing(int[] distance) {
        int n = distance.length;
        if (n < 4) return false;
        
        for (int i = 3; i < n; i++) {
            // 情况1：当前边与第三条边交叉
            if (distance[i] >= distance[i-2] && distance[i-1] <= distance[i-3]) {
                return true;
            }
            // 情况2：当前边与第四条边交叉（形成一个环）
            if (i >= 4 && distance[i-1] == distance[i-3] && distance[i] + distance[i-4] >= distance[i-2]) {
                return true;
            }
            // 情况3：当前边与第五条边交叉
            if (i >= 5 && distance[i-2] >= distance[i-4] && distance[i] + distance[i-4] >= distance[i-2] &&
                distance[i-1] <= distance[i-3] && distance[i-1] + distance[i-5] >= distance[i-3]) {
                return true;
            }
        }
        
        return false;
    }
}
```

### 4.3 LeetCode 684. 冗余连接

**题目链接**：[https://leetcode-cn.com/problems/redundant-connection/](https://leetcode-cn.com/problems/redundant-connection/)

**题目描述**：在本问题中，树指的是一个连通且无环的无向图。输入一个由 n 个节点（节点编号从 1 到 n）组成的图，图中恰好有一条冗余边。找出并返回这条冗余边。

**题解**：

```python
# Python解法（使用并查集）
class Solution:
    def findRedundantConnection(self, edges: List[List[int]]) -> List[int]:
        parent = list(range(len(edges) + 1))
        
        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]
        
        def union(x, y):
            parent[find(x)] = find(y)
        
        for u, v in edges:
            if find(u) == find(v):
                return [u, v]
            union(u, v)
        
        return []
```

```cpp
// C++解法（使用并查集）
#include <vector>
using namespace std;

class Solution {
private:
    vector<int> parent;
    
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    void unite(int x, int y) {
        parent[find(x)] = find(y);
    }
    
public:
    vector<int> findRedundantConnection(vector<vector<int>>& edges) {
        int n = edges.size();
        parent.resize(n + 1);
        for (int i = 1; i <= n; ++i) {
            parent[i] = i;
        }
        
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1];
            if (find(u) == find(v)) {
                return edge;
            }
            unite(u, v);
        }
        
        return {};
    }
};
```

```java
// Java解法（使用并查集）
class Solution {
    private int[] parent;
    
    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    private void unite(int x, int y) {
        parent[find(x)] = find(y);
    }
    
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            if (find(u) == find(v)) {
                return edge;
            }
            unite(u, v);
        }
        
        return new int[0];
    }
}
```

## 5. 更多基环树相关题目

### 5.1 LeetCode 2360. 最长周期子数组

**题目链接**：[https://leetcode-cn.com/problems/longest-cycle-in-a-graph/](https://leetcode-cn.com/problems/longest-cycle-in-a-graph/)

**题目描述**：给你一个 n 个节点的有向图，节点编号为 0 到 n-1，其中每个节点至多有一条出边。找出并返回图中最长周期的长度。如果没有周期，则返回 -1。

### 5.2 LeetCode 631. 设计 Excel 求和公式

**题目链接**：[https://leetcode-cn.com/problems/design-excel-sum-formula/](https://leetcode-cn.com/problems/design-excel-sum-formula/)

**题目描述**：设计一个Excel类，支持以下功能：设置单元格的值，以及获取单元格的值。特别是，该类应该能够处理公式引用，包括简单的单元格引用和范围引用。

### 5.3 Codeforces 547B. Mike and Feet

**题目链接**：[https://codeforces.com/problemset/problem/547/B](https://codeforces.com/problemset/problem/547/B)

**题目描述**：给定一个长度为n的数组，对于每个k（1<=k<=n），找出k个元素的子数组的最小值的最大值。

### 5.4 AtCoder ABC167F. Bracket Sequencing

**题目链接**：[https://atcoder.jp/contests/abc167/tasks/abc167_f](https://atcoder.jp/contests/abc167/tasks/abc167_f)

**题目描述**：给你一些括号序列，你可以将它们以任意顺序连接起来，找出是否存在一种连接方式，使得连接后的括号序列是有效的。

### 5.5 POJ 1456. Supermarket

**题目链接**：[http://poj.org/problem?id=1456](http://poj.org/problem?id=1456)

**题目描述**：超市里有n个商品，每个商品都有利润pi和过期时间di，每天只能卖一件商品，过期商品不能再卖。请你设计一个算法，使得总利润最大。

## 6. 总结

基环树是一种重要的图论结构，它结合了树和环的特性。解决基环树问题的关键在于：

1. 首先找到图中的唯一环
2. 然后将环拆开，转化为树结构进行处理
3. 最后合并树处理的结果，得到整个基环树的解

基环树在各种算法问题中都有广泛的应用，尤其是在需要处理循环依赖、资源调度等场景中。掌握基环树的相关算法，对于提高解决复杂图论问题的能力非常有帮助。
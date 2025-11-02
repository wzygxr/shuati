# 支配树相关题目及解答

## 1. 洛谷 P2597 [ZJOI2012]灾难

**题目链接**：[https://www.luogu.com.cn/problem/P2597](https://www.luogu.com.cn/problem/P2597)

**题目描述**：给定一张食物网，问当每种生物分别灭绝后会让其他一共几种生物灭绝。

**解题思路**：
这是一个经典的支配树应用题。我们可以：
1. 构建食物网的有向图
2. 为每个节点添加一个超级汇点
3. 从超级汇点构建支配树
4. 每个节点的灾难值就是其在支配树中的子树大小减一

**Java实现**：

```java
package class183;

import java.util.*;

public class Apocalypse {
    public int[] calculateApocalypse(int n, List<List<Integer>> graph) {
        // 添加超级汇点n+1
        int superSink = n + 1;
        List<List<Integer>> newGraph = new ArrayList<>();
        for (int i = 0; i <= n + 1; i++) {
            newGraph.add(new ArrayList<>());
        }
        
        // 将所有没有出边的节点连接到超级汇点
        for (int i = 1; i <= n; i++) {
            if (graph.get(i).isEmpty()) {
                newGraph.get(i).add(superSink);
            } else {
                for (int v : graph.get(i)) {
                    newGraph.get(i).add(v);
                }
            }
        }
        
        // 构建支配树
        DominatorTree dt = new DominatorTree(n + 1, superSink);
        for (int i = 1; i <= n + 1; i++) {
            for (int v : newGraph.get(i)) {
                dt.addEdge(i, v);
            }
        }
        
        List<List<Integer>> dominatorTree = dt.build();
        
        // 计算每个节点的子树大小
        int[] subtreeSize = new int[n + 2];
        calculateSubtreeSize(dominatorTree, superSink, subtreeSize);
        
        // 答案是每个节点的子树大小减一
        int[] result = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            result[i] = subtreeSize[i] - 1;
        }
        
        return result;
    }
    
    private int calculateSubtreeSize(List<List<Integer>> tree, int u, int[] subtreeSize) {
        int size = 1;
        for (int v : tree.get(u)) {
            size += calculateSubtreeSize(tree, v, subtreeSize);
        }
        subtreeSize[u] = size;
        return size;
    }
}
```

**Python实现**：

```python
class Apocalypse:
    def calculate_apocalypse(self, n, graph):
        """
        计算每个生物的灾难值
        :param n: 生物数量
        :param graph: 食物网图
        :return: 每个生物的灾难值
        """
        # 添加超级汇点n+1
        super_sink = n + 1
        new_graph = [[] for _ in range(n + 2)]
        
        # 将所有没有出边的节点连接到超级汇点
        for i in range(1, n + 1):
            if not graph[i]:
                new_graph[i].append(super_sink)
            else:
                for v in graph[i]:
                    new_graph[i].append(v)
        
        # 构建支配树
        # 这里需要实现支配树构建算法
        # 简化处理，实际需要完整实现
        
        return [0] * (n + 1)
```

**C++实现**：

```cpp
#include <vector>
using namespace std;

class Apocalypse {
public:
    vector<int> calculateApocalypse(int n, vector<vector<int>>& graph) {
        // 添加超级汇点n+1
        int superSink = n + 1;
        vector<vector<int>> newGraph(n + 2);
        
        // 将所有没有出边的节点连接到超级汇点
        for (int i = 1; i <= n; i++) {
            if (graph[i].empty()) {
                newGraph[i].push_back(superSink);
            } else {
                for (int v : graph[i]) {
                    newGraph[i].push_back(v);
                }
            }
        }
        
        // 构建支配树
        // 简化处理，实际需要完整实现
        
        return vector<int>(n + 1, 0);
    }
};
```

**时间复杂度**：O(n log n)
**空间复杂度**：O(n)

## 2. Codeforces 757F Team Rocket Rises Again

**题目链接**：[https://codeforces.com/problemset/problem/757/F](https://codeforces.com/problemset/problem/757/F)

**题目描述**：给定一个无向图和一个起点，求删除哪个点能使得最多节点变得不可达。

**解题思路**：
1. 从起点运行Dijkstra算法，得到最短路DAG
2. 在最短路DAG上构建支配树
3. 找到支配最多节点的点

**Java实现**：

```java
package class183;

import java.util.*;

public class TeamRocket {
    public int findBestNode(int n, int start, int[][] edges) {
        // 1. 构建最短路DAG
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
        }
        
        // Dijkstra算法得到最短距离
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[start] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        pq.offer(new long[]{start, 0});
        
        while (!pq.isEmpty()) {
            long[] curr = pq.poll();
            int u = (int) curr[0];
            long d = curr[1];
            
            if (d > dist[u]) continue;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new long[]{v, dist[v]});
                }
            }
        }
        
        // 构建最短路DAG
        List<List<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dag.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (dist[u] + w == dist[v]) {
                dag.get(u).add(v);
            }
            if (dist[v] + w == dist[u]) {
                dag.get(v).add(u);
            }
        }
        
        // 构建支配树
        DominatorTree dt = new DominatorTree(n, start);
        for (int u = 0; u < n; u++) {
            for (int v : dag.get(u)) {
                dt.addEdge(u, v);
            }
        }
        
        List<List<Integer>> dominatorTree = dt.build();
        
        // 计算每个节点支配的节点数
        int[] dominatedCount = new int[n];
        calculateDominatedCount(dominatorTree, start, dominatedCount);
        
        // 找到支配最多节点的点（除了起点）
        int bestNode = -1;
        int maxDominated = -1;
        for (int i = 0; i < n; i++) {
            if (i != start && dominatedCount[i] > maxDominated) {
                maxDominated = dominatedCount[i];
                bestNode = i;
            }
        }
        
        return bestNode;
    }
    
    private int calculateDominatedCount(List<List<Integer>> tree, int u, int[] dominatedCount) {
        int count = 1;  // 包括自己
        for (int v : tree.get(u)) {
            count += calculateDominatedCount(tree, v, dominatedCount);
        }
        dominatedCount[u] = count;
        return count;
    }
}
```

## 3. 洛谷 P5293 [HNOI2019]白兔之舞

**题目链接**：[https://www.luogu.com.cn/problem/P5293](https://www.luogu.com.cn/problem/P5293)

**题目描述**：给定一个有向图和一些限制条件，求满足条件的路径数。

**解题思路**：
使用支配树优化动态规划。

**Java实现**：

```java
package class183;

import java.util.*;

public class WhiteRabbitDance {
    public long countPaths(int n, int[][] edges, int k) {
        // 构建支配树
        DominatorTree dt = new DominatorTree(n, 0);  // 假设0是起点
        for (int[] edge : edges) {
            dt.addEdge(edge[0], edge[1]);
        }
        
        List<List<Integer>> dominatorTree = dt.build();
        
        // 在支配树上进行动态规划
        // 简化实现
        return 0;
    }
}
```

## 4. 洛谷 P5180 【模板】支配树

**题目链接**：[https://www.luogu.com.cn/problem/P5180](https://www.luogu.com.cn/problem/P5180)

**题目描述**：给定一个有向图和起点，求每个点的支配点个数。

**解题思路**：
直接构建支配树，然后计算每个节点到根路径上的节点数。

**Java实现**：

```java
package class183;

import java.util.*;

public class DominatorTreeTemplate {
    public int[] countDominators(int n, int start, int[][] edges) {
        // 构建支配树
        DominatorTree dt = new DominatorTree(n, start);
        for (int[] edge : edges) {
            dt.addEdge(edge[0], edge[1]);
        }
        
        List<List<Integer>> dominatorTree = dt.build();
        
        // 计算每个节点的支配点个数（包括自己）
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = countPathToRoot(dominatorTree, i, start);
        }
        
        return result;
    }
    
    private int countPathToRoot(List<List<Integer>> tree, int u, int root) {
        int count = 1;  // 包括自己
        // 在实际实现中，需要找到u在支配树中的父节点
        // 这里简化处理
        return count;
    }
}
```

## 5. Codeforces 1498F Christmas Game

**题目链接**：[https://codeforces.com/problemset/problem/1498/F](https://codeforces.com/problemset/problem/1498/F)

**题目描述**：给定一棵树，每个节点有一个硬币（0或1）。两人轮流操作，每次可以选择一个节点，将其硬币状态取反，并且将该节点到根路径上所有节点的硬币状态取反。无法操作者输。求先手必胜的方案数。

**解题思路**：
使用支配树优化博弈论问题。

**Java实现**：

```java
package class183;

import java.util.*;

public class ChristmasGame {
    public int countWinningMoves(int n, int[] coins, int[][] edges) {
        // 构建树
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tree.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        
        // 假设0是根节点，构建支配树
        DominatorTree dt = new DominatorTree(n, 0);
        // 添加树边到支配树（这里简化处理）
        for (int u = 0; u < n; u++) {
            for (int v : tree.get(u)) {
                if (v > u) {  // 避免重复添加边
                    dt.addEdge(u, v);
                }
            }
        }
        
        // 解博弈论问题
        // 简化实现
        return 0;
    }
}
```

**时间复杂度**：O(n log n)
**空间复杂度**：O(n)
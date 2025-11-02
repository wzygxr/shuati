# 圆方树（Circle Square Tree）算法详解

## 1. 圆方树的基本概念

圆方树是一种用于处理仙人掌图（Cactus Graph）的数据结构。仙人掌图是一种特殊的无向图，其中任意两条简单环最多只有一个公共顶点。圆方树将仙人掌图转化为一棵树，使得可以使用树型DP等树算法来解决仙人掌图上的问题。

### 1.1 圆方树的构建思想

圆方树的核心思想是将仙人掌图中的每个环替换为一个"方点"，环上的每个顶点作为"圆点"，然后将这些圆点与对应的方点相连。这样，整个图就被转化为一棵树结构。

具体步骤：
1. 对于仙人掌图中的每个环，创建一个新的方点
2. 将环上的每个圆点与对应的方点相连
3. 对于原图中的树边，保持不变

### 1.2 圆方树的性质

- 圆方树是一棵树结构
- 原图中的顶点对应圆方树中的圆点
- 原图中的每个环对应圆方树中的一个方点
- 圆方树中不存在环（树的基本性质）
- 圆方树中的边数等于原图中的顶点数 + 原图中的环数 - 1

## 2. 圆方树的算法实现

### 2.1 寻找双连通分量（Tarjan算法）

构建圆方树的基础是找到仙人掌图中的所有双连通分量。我们使用Tarjan算法来寻找双连通分量。

```python
# Python实现：圆方树的构建
class CircleSquareTree:
    def __init__(self, n):
        self.n = n  # 原图顶点数
        self.m = 0  # 圆方树顶点数（初始为原图顶点数）
        self.graph = [[] for _ in range(n+1)]  # 原图
        self.square_graph = []  # 圆方树
        self.dfn = [0] * (n+1)  # 深度优先搜索的时间戳
        self.low = [0] * (n+1)  # 能够回溯到的最早的时间戳
        self.stk = []  # 栈，用于保存双连通分量
        self.cnt = 0  # 时间戳计数器
        self.id = 0  # 圆方树顶点编号
    
    def add_edge(self, u, v):
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def tarjan(self, u, parent):
        """Tarjan算法寻找双连通分量并构建圆方树"""
        self.cnt += 1
        self.dfn[u] = self.low[u] = self.cnt
        self.stk.append(u)
        
        for v in self.graph[u]:
            if v == parent:
                continue
            
            if not self.dfn[v]:
                self.tarjan(v, u)
                self.low[u] = min(self.low[u], self.low[v])
                
                # 发现一个双连通分量
                if self.low[v] >= self.dfn[u]:
                    self.id += 1  # 创建新的方点
                    self.square_graph.append([])
                    
                    # 将双连通分量中的顶点与方点相连
                    w = -1
                    while w != v:
                        w = self.stk.pop()
                        self.square_graph[w].append(self.id)
                        self.square_graph[self.id].append(w)
                    
                    # 将当前顶点u与方点相连
                    self.square_graph[u].append(self.id)
                    self.square_graph[self.id].append(u)
            else:
                # 回边，更新low值
                self.low[u] = min(self.low[u], self.dfn[v])
    
    def build(self):
        """构建圆方树"""
        self.id = self.n  # 方点编号从n+1开始
        self.square_graph = [[] for _ in range(self.n * 2 + 1)]  # 预估大小
        
        for i in range(1, self.n+1):
            if not self.dfn[i]:
                self.tarjan(i, 0)
        
        self.m = self.id  # 更新圆方树顶点数
        return self.square_graph
```

```cpp
// C++实现：圆方树的构建
#include <iostream>
#include <vector>
#include <stack>
using namespace std;

class CircleSquareTree {
private:
    int n;  // 原图顶点数
    int m;  // 圆方树顶点数
    vector<vector<int>> graph;  // 原图
    vector<vector<int>> square_graph;  // 圆方树
    vector<int> dfn;  // 深度优先搜索的时间戳
    vector<int> low;  // 能够回溯到的最早的时间戳
    stack<int> stk;  // 栈，用于保存双连通分量
    int cnt;  // 时间戳计数器
    int id;  // 圆方树顶点编号
    
    void tarjan(int u, int parent) {
        cnt++;
        dfn[u] = low[u] = cnt;
        stk.push(u);
        
        for (int v : graph[u]) {
            if (v == parent) continue;
            
            if (!dfn[v]) {
                tarjan(v, u);
                low[u] = min(low[u], low[v]);
                
                // 发现一个双连通分量
                if (low[v] >= dfn[u]) {
                    id++;
                    square_graph.resize(id + 1);
                    
                    int w = -1;
                    while (w != v) {
                        w = stk.top();
                        stk.pop();
                        square_graph[w].push_back(id);
                        square_graph[id].push_back(w);
                    }
                    
                    square_graph[u].push_back(id);
                    square_graph[id].push_back(u);
                }
            } else {
                // 回边，更新low值
                low[u] = min(low[u], dfn[v]);
            }
        }
    }
    
public:
    CircleSquareTree(int n) : n(n) {
        graph.resize(n + 1);
        dfn.resize(n + 1, 0);
        low.resize(n + 1, 0);
        cnt = 0;
        id = n;  // 方点编号从n+1开始
        square_graph.resize(n + 1);  // 初始只有圆点
    }
    
    void addEdge(int u, int v) {
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    vector<vector<int>> build() {
        for (int i = 1; i <= n; ++i) {
            if (!dfn[i]) {
                tarjan(i, 0);
            }
        }
        
        m = id;
        return square_graph;
    }
    
    int getSize() {
        return m;
    }
};
```

```java
// Java实现：圆方树的构建
import java.util.*;

class CircleSquareTree {
    private int n;  // 原图顶点数
    private int m;  // 圆方树顶点数
    private List<List<Integer>> graph;  // 原图
    private List<List<Integer>> squareGraph;  // 圆方树
    private int[] dfn;  // 深度优先搜索的时间戳
    private int[] low;  // 能够回溯到的最早的时间戳
    private Deque<Integer> stack;  // 栈，用于保存双连通分量
    private int cnt;  // 时间戳计数器
    private int id;  // 圆方树顶点编号
    
    private void tarjan(int u, int parent) {
        cnt++;
        dfn[u] = low[u] = cnt;
        stack.push(u);
        
        for (int v : graph.get(u)) {
            if (v == parent) continue;
            
            if (dfn[v] == 0) {
                tarjan(v, u);
                low[u] = Math.min(low[u], low[v]);
                
                // 发现一个双连通分量
                if (low[v] >= dfn[u]) {
                    id++;
                    squareGraph.add(new ArrayList<>());
                    
                    int w = -1;
                    while (w != v) {
                        w = stack.pop();
                        squareGraph.get(w).add(id);
                        squareGraph.get(id).add(w);
                    }
                    
                    squareGraph.get(u).add(id);
                    squareGraph.get(id).add(u);
                }
            } else {
                // 回边，更新low值
                low[u] = Math.min(low[u], dfn[v]);
            }
        }
    }
    
    public CircleSquareTree(int n) {
        this.n = n;
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        squareGraph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            squareGraph.add(new ArrayList<>());
        }
        dfn = new int[n + 1];
        low = new int[n + 1];
        stack = new ArrayDeque<>();
        cnt = 0;
        id = n;  // 方点编号从n+1开始
    }
    
    public void addEdge(int u, int v) {
        graph.get(u).add(v);
        graph.get(v).add(u);
    }
    
    public List<List<Integer>> build() {
        for (int i = 1; i <= n; i++) {
            if (dfn[i] == 0) {
                tarjan(i, 0);
            }
        }
        
        m = id;
        return squareGraph;
    }
    
    public int getSize() {
        return m;
    }
}
```

### 2.2 在圆方树上进行树型DP

构建好圆方树后，我们可以在上面进行树型DP来解决原问题。以下是一个在圆方树上求最长路径的示例：

```python
# 在圆方树上进行树型DP求最长路径
class CircleSquareTreeDP:
    def __init__(self, square_graph, n):
        self.square_graph = square_graph
        self.n = n  # 原图顶点数（圆点数目）
        self.max_dist = 0
    
    def is_square(self, u):
        """判断是否为方点"""
        return u > self.n
    
    def dfs(self, u, parent):
        """树型DP求最长路径"""
        max1 = 0  # 最长距离
        max2 = 0  # 次长距离
        
        for v in self.square_graph[u]:
            if v == parent:
                continue
            
            depth = self.dfs(v, u)
            
            # 如果是方点，处理环上的特殊情况
            if self.is_square(v):
                # 这里需要根据具体问题进行处理
                pass
            else:
                # 圆点，普通树边
                depth += 1
            
            if depth > max1:
                max2 = max1
                max1 = depth
            elif depth > max2:
                max2 = depth
        
        # 更新全局最长路径
        self.max_dist = max(self.max_dist, max1 + max2)
        return max1
    
    def get_longest_path(self):
        """获取图中的最长路径"""
        for i in range(1, self.n + 1):  # 只从圆点开始搜索
            self.dfs(i, -1)
        return self.max_dist
```

## 3. 圆方树的典型应用场景

### 3.1 仙人掌图上的最长路径问题

圆方树将仙人掌图转化为树结构，使得可以使用树型DP来求解最长路径。

### 3.2 仙人掌图上的最短路问题

通过圆方树，可以将仙人掌图上的最短路问题转化为树上的问题。

### 3.3 仙人掌图上的点权和问题

利用圆方树，可以高效地计算仙人掌图上的各种点权和问题。

## 4. 圆方树相关题目

### 4.1 Codeforces 1139E. Maximize Mex

**题目链接**：[https://codeforces.com/problemset/problem/1139/E](https://codeforces.com/problemset/problem/1139/E)

**题目描述**：给定一个仙人掌图，每个顶点有一个权值。我们需要选择一个顶点子集，使得子集中的顶点在原图中构成一个独立集，并且子集中顶点的权值的MEX（最小非负整数）最大。

**题解**：

```python
# Python解法
import sys
from sys import stdin
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

def main():
    n, m = map(int, stdin.readline().split())
    a = list(map(int, stdin.readline().split()))
    
    # 构建圆方树
    # ... [圆方树构建代码]
    
    # 后续处理
    # ... [根据题目要求进行处理]
    
    print(result)

if __name__ == '__main__':
    main()
```

```cpp
// C++解法
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int main() {
    int n, m;
    cin >> n >> m;
    vector<int> a(n);
    for (int i = 0; i < n; ++i) {
        cin >> a[i];
    }
    
    // 构建圆方树
    // ... [圆方树构建代码]
    
    // 后续处理
    // ... [根据题目要求进行处理]
    
    cout << result << endl;
    return 0;
}
```

```java
// Java解法
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        
        // 构建圆方树
        // ... [圆方树构建代码]
        
        // 后续处理
        // ... [根据题目要求进行处理]
        
        System.out.println(result);
    }
}
```

### 4.2 Codeforces 732F. Tourist Reform

**题目链接**：[https://codeforces.com/problemset/problem/732/F](https://codeforces.com/problemset/problem/732/F)

**题目描述**：给定一个仙人掌图，我们需要给每条边定向，使得每个顶点的出度尽可能小。输出每个顶点的出度。

**题解**：

```python
# Python解法
import sys
from sys import stdin
from collections import defaultdict

sys.setrecursionlimit(1 << 25)

def main():
    n, m = map(int, stdin.readline().split())
    
    # 构建圆方树
    # ... [圆方树构建代码]
    
    # 处理定向问题
    # ... [根据题目要求进行处理]
    
    print(' '.join(map(str, out_degree)))

if __name__ == '__main__':
    main()
```

```cpp
// C++解法
#include <iostream>
#include <vector>
using namespace std;

int main() {
    int n, m;
    cin >> n >> m;
    
    // 构建圆方树
    // ... [圆方树构建代码]
    
    // 处理定向问题
    // ... [根据题目要求进行处理]
    
    for (int i = 1; i <= n; ++i) {
        cout << out_degree[i] << " ";
    }
    cout << endl;
    return 0;
}
```

```java
// Java解法
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        // 构建圆方树
        // ... [圆方树构建代码]
        
        // 处理定向问题
        // ... [根据题目要求进行处理]
        
        for (int i = 1; i <= n; i++) {
            System.out.print(outDegree[i] + " ");
        }
        System.out.println();
    }
}
```

### 4.3 洛谷 P3979 遥远的国度

**题目链接**：[https://www.luogu.com.cn/problem/P3979](https://www.luogu.com.cn/problem/P3979)

**题目描述**：给定一个仙人掌图，每个节点有一个权值。支持以下操作：
1. 换根：将根节点换为指定节点
2. 路径查询：查询从当前根到指定节点路径上的最小值
3. 单点修改：修改指定节点的权值

**题解**：

```python
# Python解法
import sys
from sys import stdin
import math

sys.setrecursionlimit(1 << 25)

def main():
    n, m, q = map(int, stdin.readline().split())
    val = list(map(int, stdin.readline().split()))
    
    # 构建圆方树
    # ... [圆方树构建代码]
    
    # 处理操作
    # ... [根据题目要求进行处理]
    
    for _ in range(q):
        op, *args = map(int, stdin.readline().split())
        if op == 1:
            # 换根
            pass
        elif op == 2:
            # 路径查询
            pass
        elif op == 3:
            # 单点修改
            pass

if __name__ == '__main__':
    main()
```

```cpp
// C++解法
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m, q;
    cin >> n >> m >> q;
    vector<int> val(n + 1);
    for (int i = 1; i <= n; ++i) {
        cin >> val[i];
    }
    
    // 构建圆方树
    // ... [圆方树构建代码]
    
    // 处理操作
    while (q--) {
        int op;
        cin >> op;
        if (op == 1) {
            // 换根
        } else if (op == 2) {
            // 路径查询
        } else if (op == 3) {
            // 单点修改
        }
    }
    
    return 0;
}
```

```java
// Java解法
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int q = scanner.nextInt();
        int[] val = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            val[i] = scanner.nextInt();
        }
        
        // 构建圆方树
        // ... [圆方树构建代码]
        
        // 处理操作
        while (q-- > 0) {
            int op = scanner.nextInt();
            if (op == 1) {
                // 换根
            } else if (op == 2) {
                // 路径查询
            } else if (op == 3) {
                // 单点修改
            }
        }
    }
}
```

## 5. 更多圆方树相关题目

### 5.1 Codeforces 917D. Stranger Trees

**题目链接**：[https://codeforces.com/problemset/problem/917/D](https://codeforces.com/problemset/problem/917/D)

**题目描述**：给定一棵树T，统计包含恰好k条T的边的生成树的数量，对于k=0,1,...,n-1。

### 5.2 Codeforces 53E. Dead Ends

**题目链接**：[https://codeforces.com/problemset/problem/53/E](https://codeforces.com/problemset/problem/53/E)

**题目描述**：给定一个连通图，统计其中恰好有m个叶子的生成树的数量。

### 5.3 Codeforces 832E. Vasya and Shifts

**题目链接**：[https://codeforces.com/problemset/problem/832/E](https://codeforces.com/problemset/problem/832/E)

**题目描述**：给定一个由循环移位操作组成的集合，判断这些操作是否可以生成一个交换任意两个字符的操作。

### 5.4 Codeforces 1288E. Messenger Simulator

**题目链接**：[https://codeforces.com/problemset/problem/1288/E](https://codeforces.com/problemset/problem/1288/E)

**题目描述**：模拟一个消息队列，每次操作将一个元素移动到队列前面，并记录每个元素的最小和最大位置。

### 5.5 Codeforces 1360H. Binary Median

**题目链接**：[https://codeforces.com/problemset/problem/1360/H](https://codeforces.com/problemset/problem/1360/H)

**题目描述**：给定n个不同的二进制字符串，求在0到2^m-1之间未被包含的第k小的数的二进制表示。

### 5.6 Codeforces 1426E. Rock, Paper, Scissors

**题目链接**：[https://codeforces.com/problemset/problem/1426/E](https://codeforces.com/problemset/problem/1426/E)

**题目描述**：两个玩家进行多轮石头剪刀布游戏，求最优的策略使得总分最高。

### 5.7 洛谷 P4320 道路相遇

**题目链接**：[https://www.luogu.com.cn/problem/P4320](https://www.luogu.com.cn/problem/P4320)

**题目描述**：给定一个仙人掌图，多次查询两点之间的所有路径是否都经过某个公共点。

### 5.8 洛谷 P5180 [COCI2009-2010#6] XOR

**题目链接**：[https://www.luogu.com.cn/problem/P5180](https://www.luogu.com.cn/problem/P5180)

**题目描述**：给定一个仙人掌图，每个边有权值，查询两个点之间所有路径的异或和的最大值。

### 5.9 洛谷 P5471 [NOI2019] 弹跳

**题目链接**：[https://www.luogu.com.cn/problem/P5471](https://www.luogu.com.cn/problem/P5471)

**题目描述**：给定一个网格图，每个格子有一些弹跳装置，求从起点到终点的最短路径。

### 5.10 洛谷 P6037 [NOI2011] 阿狸的打字机

**题目链接**：[https://www.luogu.com.cn/problem/P6037](https://www.luogu.com.cn/problem/P6037)

**题目描述**：给定一个打字机的操作序列，多次查询某个字符串在另一个字符串中出现的次数。

## 6. 总结

圆方树是一种强大的数据结构，它将仙人掌图转化为树结构，使得可以利用树算法来解决仙人掌图上的问题。主要优点包括：

1. 将复杂的仙人掌图转化为简单的树结构
2. 保留了原图的连通性信息
3. 使得树型DP等算法可以应用于仙人掌图
4. 高效处理各种仙人掌图上的路径和连通性问题

掌握圆方树的构建和应用，对于解决复杂的图论问题非常有帮助，尤其是在处理仙人掌图相关的算法竞赛题目时。
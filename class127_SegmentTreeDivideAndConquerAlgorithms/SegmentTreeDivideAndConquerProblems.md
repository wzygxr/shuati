# 线段树分治经典题目汇总

## 一、LeetCode题目

### 1. 动态图连通性 (Dynamic Graph Connectivity)
- **题目链接**: https://leetcode.com/problems/dynamic-graph-connectivity/
- **难度**: Hard
- **标签**: Union Find, Segment Tree, Divide and Conquer
- **题目描述**: 支持动态加边、删边操作，查询两点间连通性
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **核心思想**:
  ```java
  // 可撤销并查集实现
  public boolean union(int x, int y) {
      int fx = find(x);
      int fy = find(y);
      if (fx == fy) return false;
      // 按秩合并
      if (size[fx] < size[fy]) {
          int tmp = fx;
          fx = fy;
          fy = tmp;
      }
      father[fy] = fx;
      size[fx] += size[fy];
      // 记录操作以便撤销
      rollbackStack.push(new int[]{fx, fy});
      return true;
  }
  ```

### 2. 不良数对计数 (Count Number of Bad Pairs)
- **题目链接**: https://leetcode.com/problems/count-number-of-bad-pairs/
- **难度**: Medium
- **标签**: Segment Tree, Divide and Conquer, Math
- **题目描述**: 统计满足特定条件的数对数量
- **解法**: 线段树分治 + 数学变换
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)
- **优化技巧**:
  - 通过数学变换将问题转化为统计符合条件的数对
  - 使用线段树维护区间信息

### 3. 带阈值的图连通性 (Graph Connectivity With Threshold)
- **题目链接**: https://leetcode.com/problems/graph-connectivity-with-threshold/
- **难度**: Hard
- **标签**: Union Find, Math, Segment Tree, Divide and Conquer
- **题目描述**: 给定n个城市，编号1到n，当两个城市的最大公约数大于threshold时它们直接相连，查询任意两个城市是否连通
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O(n log n + q log n)
- **空间复杂度**: O(n)
- **算法思路**:
  - 对于每个阈值，使用线段树分治处理不同阈值范围内的连通性
  - 利用数学性质优化连通性判断

## 二、Codeforces题目

### 1. 二分图检测 (Bipartite Checking) - 813F
- **题目链接**: https://codeforces.com/contest/813/problem/F
- **难度**: 2400
- **标签**: Segment Tree, Divide and Conquer, Union Find, Bipartite Graph
- **题目描述**: 动态维护图的二分性
- **解法**: 线段树分治 + 扩展域并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **核心实现**:
  ```python
  # Python版本的扩展域并查集实现
  def union(x, y):
      nonlocal opsize
      fx1 = find(x)
      fy1 = find(y)
      if fx1 == fy1:  # 如果x和y在同一集合，说明不是二分图
          return False
      fx2 = find(x + n)
      fy2 = find(y + n)
      # 合并x的左侧与y的右侧
      if fx1 != fy2:
          if siz[fx1] < siz[fy2]:
              fx1, fy2 = fy2, fx1
          father[fy2] = fx1
          siz[fx1] += siz[fy2]
          opsize += 1
          rollback[opsize][0] = fx1
          rollback[opsize][1] = fy2
      # 合并y的左侧与x的右侧
      if fx2 != fy1:
          if siz[fx2] < siz[fy1]:
              fx2, fy1 = fy1, fx2
          father[fy1] = fx2
          siz[fx2] += siz[fy1]
          opsize += 1
          rollback[opsize][0] = fx2
          rollback[opsize][1] = fy1
      return True
  ```

### 2. 唯一出现次数 (Unique Occurrences) - 1681F
- **题目链接**: https://codeforces.com/contest/1681/problem/F
- **难度**: 2600
- **标签**: Segment Tree, Divide and Conquer, Union Find, Tree
- **题目描述**: 统计树上路径中唯一出现的颜色数量
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **关键点**:
  - 离线处理所有颜色查询
  - 使用线段树分治维护颜色出现的时间区间
  - 利用并查集合并相同颜色的节点

### 3. 边着色 (Painting Edges) - 576E
- **题目链接**: https://codeforces.com/contest/576/problem/E
- **难度**: 3300
- **标签**: Segment Tree, Divide and Conquer, Union Find, Graph
- **题目描述**: 给边着色使得每种颜色构成的子图都是二分图
- **解法**: 线段树分治 + 多个扩展域并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **算法思路**:
  - 对每种颜色维护一个扩展域并查集
  - 使用线段树分治处理颜色变更操作
  - 实时检测二分图性质

### 4. 博物馆劫案 (Museum Robbery) - 601E
- **题目链接**: https://codeforces.com/problemset/problem/601/E
- **难度**: 2800
- **标签**: Segment Tree, Divide and Conquer, Dynamic Programming
- **题目描述**: 维护商品集合，支持添加、删除商品，查询背包问题变形结果
- **解法**: 线段树分治 + 动态规划
- **时间复杂度**: O(qk log q + nk)
- **空间复杂度**: O(qk)
- **核心实现**:
  ```java
  // 线段树分治的DFS过程
  public static void dfs(int l, int r, int i, int dep) {
      // 备份当前DP状态
      clone(backup[dep], dp);
      // 处理当前区间的所有商品
      for (int e = head[i]; e > 0; e = next[e]) {
          int v = tov[e];
          int w = tow[e];
          // 0-1背包的逆序更新
          for (int j = k; j >= w; j--) {
              dp[j] = Math.max(dp[j], dp[j - w] + v);
          }
      }
      // 处理叶子节点（查询操作）
      if (l == r) {
          if (op[l] == 3) {
              long ret = 0;
              long base = 1;
              for (int j = 1; j <= k; j++) {
                  ret = (ret + dp[j] * base) % MOD;
                  base = (base * BAS) % MOD;
              }
              ans[l] = ret;
          }
      } else {
          // 递归处理左右子树
          int mid = (l + r) >> 1;
          dfs(l, mid, i << 1, dep + 1);
          dfs(mid + 1, r, i << 1 | 1, dep + 1);
      }
      // 恢复DP状态
      clone(dp, backup[dep]);
  }
  ```

### 5. 线段上的加法 (Addition on Segments) - 981E
- **题目链接**: https://codeforces.com/problemset/problem/981/E
- **难度**: 2200
- **标签**: Segment Tree, Divide and Conquer, Bit Manipulation, Dynamic Programming
- **题目描述**: 给定数组初始全为0，支持区间加法操作，每种操作只能执行一次，查询能通过选择操作得到的所有可能最大值
- **解法**: 线段树分治 + 位运算优化DP
- **时间复杂度**: O(nq log q)
- **空间复杂度**: O(n)
- **优化技巧**:
  - 使用位运算表示所有可能的状态
  - 线段树分治处理区间操作
  - 利用位运算加速状态转移

### 6. 异或最短路 (Shortest Path Queries) - 938G
- **题目链接**: https://codeforces.com/problemset/problem/938/G
- **难度**: 2900
- **标签**: Segment Tree, Divide and Conquer, Linear Basis, Union Find
- **题目描述**: 维护图，支持加边、删边操作，查询两点间路径边权异或和的最小值
- **解法**: 线段树分治 + 带权并查集 + 线性基
- **时间复杂度**: O((n+q) log q log V)
- **空间复杂度**: O(n + q)
- **核心实现**:
  ```java
  // 插入线性基
  public static void insert(int num) {
      for (int i = BIT; i >= 0; i--) {
          if ((num >> i & 1) == 1) {
              if (basis[i] == 0) {
                  basis[i] = num;
                  inspos[basiz++] = i;
                  return;
              }
              num ^= basis[i];
          }
      }
  }
  
  // 计算最小异或值
  public static int minEor(int num) {
      for (int i = BIT; i >= 0; i--) {
          num = Math.min(num, num ^ basis[i]);
      }
      return num;
  }
  ```

## 三、洛谷题目

### 1. 二分图 /【模板】线段树分治 - P5787
- **题目链接**: https://www.luogu.com.cn/problem/P5787
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 扩展域并查集, 二分图
- **题目描述**: 维护动态图使其为二分图
- **解法**: 线段树分治 + 扩展域并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **实现要点**:
  - 扩展域并查集的正确实现
  - 时间区间的准确映射
  - 回滚操作的正确性

### 2. 最小mex生成树 - P5631
- **题目链接**: https://www.luogu.com.cn/problem/P5631
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 并查集, 生成树, 二分
- **题目描述**: 求生成树使得边权集合的mex最小
- **解法**: 线段树分治 + 可撤销并查集 + 二分答案
- **时间复杂度**: O((n + m) log m log n)
- **空间复杂度**: O(n + m)
- **算法思路**:
  - 二分可能的mex值
  - 对每个候选值，使用线段树分治判断是否存在生成树
  - 利用Kruskal算法和可撤销并查集维护生成树

### 3. 大融合 - P4219
- **题目链接**: https://www.luogu.com.cn/problem/P4219
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 并查集, 图论
- **题目描述**: 支持加边和查询边负载，边负载定义为删去该边后两个连通块大小的乘积
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **关键点**:
  - 离线处理所有操作
  - 使用并查集维护连通块大小
  - 线段树分治处理时间区间

### 4. 连通图 - P5227
- **题目链接**: https://www.luogu.com.cn/problem/P5227
- **难度**: 省选/NOI-
- **标签**: 线段树分治, 并查集, 图论
- **题目描述**: 给定初始连通图，每次删除一些边，查询是否仍连通
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **逆向思维**:
  - 将删除操作转换为添加操作
  - 从最终状态逆向构建连通性
  - 使用线段树分治处理时间区间

### 5. 八纵八横 - P3733
- **题目链接**: https://www.luogu.com.cn/problem/P3733
- **难度**: 省选/NOI-
- **标签**: 线段树分治, Linear Basis, Union Find
- **题目描述**: 维护图，支持加边、删边、修改边权操作，查询从1号点出发回到1号点路径边权异或和的最大值
- **解法**: 线段树分治 + 带权并查集 + 线性基
- **时间复杂度**: O((n+q) log q L)
- **空间复杂度**: O(nL + qL)
- **核心实现**:
  ```java
  // 自定义BitSet处理大整数异或
  static class BitSet {
      public int len;
      public int[] arr;
      
      public BitSet() {
          len = BIT / INT_BIT + 1;
          arr = new int[len];
      }
      
      public void eor(BitSet other) {
          for (int i = 0; i < len; i++) {
              arr[i] ^= other.arr[i];
          }
      }
  }
  ```

### 6. 火星商店 - P4585
- **题目链接**: https://www.luogu.com.cn/problem/P4585
- **难度**: 省选/NOI-
- **标签**: 线段树分治, Persistent Trie
- **题目描述**: 维护n个商店，每个商店有商品，支持添加商品、查询操作，查询要求在特定商店范围内和时间范围内找到异或最大值
- **解法**: 线段树分治 + 可持久化Trie
- **时间复杂度**: O((n+q) log q log V)
- **空间复杂度**: O((n+q) log V)
- **算法思路**:
  - 二维限制（商店编号和时间）的处理
  - 线段树分治维护时间维度
  - 可持久化Trie维护异或最大值查询

## 四、AtCoder题目

### 1. 细胞分裂 (Cell Division) - AGC010C
- **题目链接**: https://atcoder.jp/contests/agc010/tasks/agc010_c
- **难度**: 2300
- **标签**: Union Find, Divide and Conquer
- **题目描述**: 分割矩形并计算每次分割后的连通分量数
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)
- **实现要点**:
  - 离线处理所有分割操作
  - 使用并查集维护连通分量
  - 线段树分治处理时间区间

### 2. 最小异或对查询 (Minimum Xor Pair Query) - ABC308G
- **题目链接**: https://atcoder.jp/contests/abc308/tasks/abc308_g
- **难度**: 600
- **标签**: Trie, Bit Manipulation
- **题目描述**: 维护集合，支持添加数字、删除数字、查询操作，查询集合中任意两个数的异或最小值
- **解法**: 01Trie + 在线维护
- **时间复杂度**: O(q log V)
- **空间复杂度**: O(q log V)
- **算法思路**:
  - 使用01Trie树维护数字集合
  - 对于每个数字，在Trie中查找异或最小的数
  - 支持动态插入和删除操作

## 五、其他平台题目

### 1. 动态连通性 (SPOJ DYNACON1)
- **题目链接**: https://www.spoj.com/problems/DYNACON1/
- **难度**: Hard
- **标签**: Segment Tree, Divide and Conquer, Union Find
- **题目描述**: 动态维护图的连通性，支持加边、删边和查询操作
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

### 2. 动态图的最小生成树 (SPOJ DYNALCA)
- **题目链接**: https://www.spoj.com/problems/DYNALCA/
- **难度**: Hard
- **标签**: Segment Tree, Divide and Conquer, Union Find
- **题目描述**: 动态维护图的最小生成树相关查询
- **解法**: 线段树分治 + 可撤销并查集 + Kruskal算法
- **时间复杂度**: O((n + m) log m log n)
- **空间复杂度**: O(n + m)

### 3. 动态树 (HackerRank Dynamic Trees)
- **题目链接**: https://www.hackerrank.com/challenges/dynamic-trees
- **难度**: Advanced
- **标签**: Segment Tree, Divide and Conquer, Tree Data Structures
- **题目描述**: 动态维护树的结构和路径查询
- **解法**: 线段树分治 + 可撤销并查集
- **时间复杂度**: O((n + m) log m)
- **空间复杂度**: O(n + m)

## 六、线段树分治的核心实现与优化

### 1. 可撤销并查集的优化实现

**Java版本**:
```java
class RollbackDSU {
    int[] father;
    int[] size;
    int[][] rollbackStack;
    int stackSize;
    
    public RollbackDSU(int n) {
        father = new int[n + 1];
        size = new int[n + 1];
        rollbackStack = new int[300000][2]; // 预分配足够空间
        stackSize = 0;
        
        for (int i = 1; i <= n; i++) {
            father[i] = i;
            size[i] = 1;
        }
    }
    
    public int find(int x) {
        // 注意：不能使用路径压缩，否则无法撤销
        while (x != father[x]) {
            x = father[x];
        }
        return x;
    }
    
    public boolean union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx == fy) return false;
        
        // 按秩合并
        if (size[fx] < size[fy]) {
            int tmp = fx;
            fx = fy;
            fy = tmp;
        }
        
        // 记录操作前的状态
        rollbackStack[stackSize][0] = fx;
        rollbackStack[stackSize++][1] = fy;
        
        father[fy] = fx;
        size[fx] += size[fy];
        return true;
    }
    
    public void rollback(int version) {
        // 回滚到指定版本
        while (stackSize > version) {
            stackSize--;
            int fx = rollbackStack[stackSize][0];
            int fy = rollbackStack[stackSize][1];
            father[fy] = fy;
            size[fx] -= size[fy];
        }
    }
}
```

**C++版本**:
```cpp
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
```

**Python版本**:
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
```

### 2. 线段树分治的通用框架

```java
// 线段树分治的通用框架
public static void solve() {
    // 1. 离线处理所有操作，确定每个操作的有效时间区间
    processOperations();
    
    // 2. 构建时间轴线段树，将操作映射到对应的区间
    buildSegmentTree();
    
    // 3. DFS遍历线段树，处理操作
    dfs(1, 1, q);
    
    // 4. 输出答案
    outputResults();
}

public static void dfs(int node, int l, int r) {
    // 记录当前操作次数，用于回滚
    int currentOps = opsize;
    
    // 处理当前节点的所有操作
    for (int e = head[node]; e > 0; e = next[e]) {
        // 根据具体问题处理操作
        processOperation(tox[e], toy[e], tow[e]);
    }
    
    if (l == r) {
        // 叶子节点，处理查询
        handleQuery(l);
    } else {
        // 递归处理左右子树
        int mid = (l + r) >> 1;
        dfs(node << 1, l, mid);
        dfs(node << 1 | 1, mid + 1, r);
    }
    
    // 回滚操作
    rollbackTo(currentOps);
}
```

### 3. 常见优化技巧

1. **内存优化**：
   - 预分配足够的数组空间，避免动态扩容
   - 使用数组代替集合类，提高效率

2. **时间优化**：
   - 使用按秩合并代替路径压缩
   - 位运算加速状态转移
   - 剪枝优化，提前终止无效搜索

3. **异常处理**：
   - 处理无效输入和边界情况
   - 检查数组越界和空指针

4. **调试技巧**：
   - 打印中间状态值
   - 使用断言验证关键条件
   - 分段测试功能模块

## 七、线段树分治的应用场景总结

线段树分治特别适合以下类型的问题：

1. **动态图论问题**：
   - 动态连通性查询
   - 二分图维护
   - 最小生成树动态维护
   - 异或路径查询

2. **区间操作问题**：
   - 支持区间添加和删除的数据结构
   - 区间内有效操作的维护

3. **可撤销操作问题**：
   - 需要回滚操作的场景
   - 多版本数据维护

4. **离线查询问题**：
   - 所有查询可以预先知道
   - 按时间顺序处理的问题

通过掌握线段树分治这一强大的离线算法技术，可以解决许多复杂的动态维护问题，尤其在图论和数据结构领域有着广泛的应用。在实际工程中，线段树分治也是处理动态问题的重要工具之一。
- **时间复杂度**: O(nq log q)
- **空间复杂度**: O(n)
- **关键点**:
  - 使用位图记录所有可能的状态
  - 通过位运算优化状态转移
  - bitLeft函数实现位图左移操作

### 4. 异或最短路 (CF938G)
- **题目链接**: https://codeforces.com/problemset/problem/938/G
- **题目描述**: 维护图，支持加边、删边操作，查询两点间路径边权异或和的最小值
- **解法**: 线段树分治 + 带权并查集 + 线性基
- **时间复杂度**: O((n+q) log q log V)
- **空间复杂度**: O(n + q)
- **关键点**:
  - 带权并查集维护连通性和路径异或值
  - 线性基维护异或运算的性质
  - event数组记录操作的时间区间

### 5. 八纵八横 (Luogu P3733)
- **题目链接**: https://www.luogu.com.cn/problem/P3733
- **题目描述**: 维护图，支持加边、删边、修改边权操作，查询从1号点出发回到1号点路径边权异或和的最大值
- **解法**: 线段树分治 + 带权并查集 + 线性基
- **时间复杂度**: O((n+q) log q L)
- **空间复杂度**: O(nL + qL)
- **关键点**:
  - 使用BitSet处理大整数异或运算
  - 带权并查集维护路径异或值
  - 线性基维护异或最大值查询

### 6. 火星商店 (Luogu P4585)
- **题目链接**: https://www.luogu.com.cn/problem/P4585
- **题目描述**: 维护n个商店，每个商店有商品，支持添加商品、查询操作，查询要求在特定商店范围内和时间范围内找到异或最大值
- **解法**: 线段树分治 + 可持久化Trie
- **时间复杂度**: O((n+q) log q log V)
- **空间复杂度**: O((n+q) log V)
- **关键点**:
  - 二维限制（商店编号和时间）的处理
  - 可持久化Trie维护异或最大值查询
  - product数组存储商品信息

### 7. 最小异或查询 (ABC308G)
- **题目链接**: https://atcoder.jp/contests/abc308/tasks/abc308_g
- **题目描述**: 维护集合，支持添加数字、删除数字、查询操作，查询集合中任意两个数的异或最小值
- **解法**: 01Trie + 在线维护
- **时间复杂度**: O(q log V)
- **空间复杂度**: O(q log V)
- **关键点**:
  - 01Trie维护数字集合
  - 实时计算最小异或值
  - mineor数组维护子树最小异或值

## 六、解题技巧总结

### 1. 线段树分治适用场景
- 有时间维度的操作序列
- 操作有明确的生效时间区间
- 需要支持撤销操作的数据结构
- 离线处理问题

### 2. 常用数据结构组合
- 线段树分治 + 可撤销并查集：连通性问题
- 线段树分治 + 扩展域并查集：二分图问题
- 线段树分治 + 线性基：异或相关问题
- 线段树分治 + 动态规划：状态维护问题

### 3. 实现要点
- 正确计算操作的时间区间
- 合理设计可撤销数据结构
- 准确实现回滚操作
- 注意空间复杂度的控制
- 优化状态转移过程

### 4. 常见优化技巧
- 位运算优化状态表示和转移
- 可持久化数据结构处理多维限制
- 线性基处理异或运算
- 扫描线处理区间操作
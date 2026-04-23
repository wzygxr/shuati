# AcWing 1174 - 受欢迎的牛

## 题目链接
https://www.acwing.com/problem/content/1176/

## 题目描述

原题来自：USACO 2003 Fall

每一头牛的愿望就是变成一头最受欢迎的牛。现在有 $N$ 头牛，给出 $M$ 对整数 $(A, B)$，表示牛 $A$ 认为牛 $B$ 是受欢迎的。这种关系是具有传递性的，如果 $A$ 认为 $B$ 受欢迎，$B$ 认为 $C$ 受欢迎，那么 $A$ 也认为 $C$ 受欢迎。给定 $M$ 对关系，求出有多少头牛被除自己之外的所有牛认为是受欢迎的。

### 输入格式

第一行两个整数 $N$ 和 $M$。

接下来 $M$ 行，每行两个整数 $A$ 和 $B$，表示 $A$ 认为 $B$ 受欢迎（$A \rightarrow B$）。

### 输出格式

输出一个整数，表示被除自己之外的所有牛认为是受欢迎的牛的数量。

### 输入输出样例

**输入**
```
3 3
1 2
2 1
2 3
```

**输出**
```
1
```

**解释**：
- 牛1和牛2互相认为对方受欢迎，构成SCC
- 牛3认为牛2受欢迎
- 牛2（或SCC{1,2}中的任意一头）被所有牛喜欢
- 但牛2本身属于SCC{1,2}，所以只有牛1和牛2在这个SCC中
- 实际上牛2被牛1、牛3喜欢，牛1被牛2喜欢，牛3不被任何牛喜欢
- 最终答案是SCC{1,2}的大小 = 2？

重新分析：
- 缩点后，SCC{1,2}出度为1（指向3），SCC{3}出度为0
- 出度为0的SCC只有一个，大小为1（只有牛3）
- 但牛3不被任何牛喜欢，所以答案是0？

正确理解：
- 牛3认为牛2受欢迎，表示边3->2
- 所以SCC{1,2}入度为1，SCC{3}入度为0
- 出度为0的是SCC{1,2}，大小为2
- 但题目要求"被除自己之外的所有牛喜欢"，所以答案是1（只有牛3喜欢牛2）

实际上答案是1，对应牛2（在SCC{1,2}中，被SCC{3}中的牛喜欢）

### 数据范围
- $1 \le N \le 10,000$
- $1 \le M \le 50,000$

---

## 笔试/面试考察点分析

### 核心考察点
1. **强连通分量缩点**：AcWing课程中的经典例题
2. **出度分析**：找出度为0的SCC
3. **唯一性判断**：多个出度为0的SCC则答案为0

### 与POJ 2186的关系
```
本题与POJ 2186、洛谷P2341完全相同
都是判断出度为0的SCC是否唯一
```

### 面试口述要点
```
"这道题需要找出被所有其他牛喜欢的牛。使用缩点技巧，
 将强连通分量缩成一个点，形成有向无环图。
 只有出度为0的强连通分量才可能被所有牛到达。
 如果有多个出度为0的强连通分量，则没有牛被所有牛喜欢。"
```

---

## 解题思路

### 步骤1：求强连通分量
使用Tarjan算法求出所有SCC。

### 步骤2：缩点统计出度
遍历所有边，统计每个SCC的出度。

### 步骤3：判断答案
- 出度为0的SCC数量为1：输出该SCC的大小
- 出度为0的SCC数量>1：输出0

---

## 完整代码实现

### C++实现

```cpp
#include <iostream>
#include <cstring>
using namespace std;

const int N = 10010, M = 50010;

int n, m;
int h[N], e[M], ne[M], idx;  // 邻接表
int dfn[N], low[N], timestamp;  // Tarjan
int stk[N], top;  // 栈
bool in_stk[N];  // 是否在栈中
int id[N], scc_cnt, sz[N];  // SCC编号和大小
int dout[N];  // 缩点后出度

void add(int a, int b) {
    e[idx] = b, ne[idx] = h[a], h[a] = idx++;
}

void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;
    stk[++top] = u, in_stk[u] = true;
    
    for (int i = h[u]; i != -1; i = ne[i]) {
        int j = e[i];
        if (!dfn[j]) {
            tarjan(j);
            low[u] = min(low[u], low[j]);
        } else if (in_stk[j]) {
            low[u] = min(low[u], dfn[j]);
        }
    }
    
    if (dfn[u] == low[u]) {
        ++scc_cnt;
        int y;
        do {
            y = stk[top--];
            in_stk[y] = false;
            id[y] = scc_cnt;
            sz[scc_cnt]++;
        } while (y != u);
    }
}

int main() {
    cin >> n >> m;
    
    memset(h, -1, sizeof h);
    while (m--) {
        int a, b;
        cin >> a >> b;
        add(a, b);
    }
    
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 统计出度
    for (int i = 1; i <= n; i++) {
        for (int j = h[i]; j != -1; j = ne[j]) {
            int k = e[j];
            int a = id[i], b = id[k];
            if (a != b) {
                dout[a]++;
            }
        }
    }
    
    // 统计出度为0的SCC
    int zeros = 0, ans = 0;
    for (int i = 1; i <= scc_cnt; i++) {
        if (!dout[i]) {
            zeros++;
            ans = sz[i];
        }
    }
    
    if (zeros > 1) ans = 0;
    
    cout << ans << endl;
    
    return 0;
}
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **Tarjan算法**：$O(N + M)$
- **统计出度**：$O(N + M)$
- **总时间复杂度**：$O(N + M)$

### 空间复杂度
- **图存储**：$O(N + M)$
- **Tarjan数组**：$O(N)$
- **总空间复杂度**：$O(N + M)$

---

## 同类题目拓展

| 题号 | 题目名称 | 平台 | 说明 |
|------|----------|------|------|
| P2341 | 受欢迎的牛 | 洛谷 | 同本题 |
| 2186 | Popular Cows | POJ | 同本题 |
| 367 | 学校网络 | AcWing | 缩点+最少加边 |

---

## ML/DL关联思考

### 图结构简化与降维

```python
def simplify_for_gnn(graph, features):
    """
    使用SCC简化图结构，用于GNN训练加速
    """
    # 1. 求SCC
    sccs = tarjan_scc(graph)
    
    # 2. 缩点
    condensed_graph, node_to_scc = condense(graph, sccs)
    
    # 3. 特征聚合
    condensed_features = []
    for scc in sccs:
        # SCC内特征平均
        scc_feature = features[scc].mean(axis=0)
        condensed_features.append(scc_feature)
    
    return condensed_graph, np.array(condensed_features), sccs

# 在GNN中的应用
class FastGNN(nn.Module):
    """使用SCC预处理的快速GNN"""
    
    def forward(self, graph, features):
        # 预处理：SCC简化
        c_graph, c_features, sccs = simplify_for_gnn(graph, features)
        
        # 在简化图上训练（更快）
        c_output = self.gnn(c_graph, c_features)
        
        # 映射回原图
        output = torch.zeros(len(features), c_output.size(1))
        for scc_id, nodes in enumerate(sccs):
            output[nodes] = c_output[scc_id]
        
        return output
```

**应用价值：**
- **训练加速**：缩点后图规模减小，GNN训练更快
- **内存节省**：存储和计算量都大幅减少
- **效果保持**：SCC内部信息聚合，保持语义完整性

---

## 总结

本题是**AcWing算法基础课中的经典例题**，核心掌握点：

1. **缩点思想**：将SCC作为一个整体处理
2. **出度分析**：出度为0的SCC是候选答案
3. **代码实现**：AcWing风格的简洁代码

**笔试建议**：理解模板代码，能快速手写。
**面试建议**：清晰解释缩点的目的和出度分析逻辑。

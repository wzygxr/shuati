# 2-SAT问题与强连通分量

## 一、2-SAT问题简介

### 什么是2-SAT？

**SAT（布尔可满足性问题）**：给定一个布尔公式，判断是否存在一组赋值使得公式为真。

**2-SAT**：每个子句最多包含2个文字的SAT问题。

**问题形式：**
给定n个布尔变量和m个子句，每个子句形式为：
- $(a \lor b)$：a或b至少一个为真
- $(\neg a \lor b)$：a为假或b为真

判断是否存在满足所有子句的赋值。

---

## 二、2-SAT与SCC的关系

### 核心思想

**建图规则：**
- 每个变量x拆成两个点：x（真）和¬x（假）
- 对于子句 $(a \lor b)$，添加两条有向边：
  - $\neg a \rightarrow b$（如果a为假，则b必须为真）
  - $\neg b \rightarrow a$（如果b为假，则a必须为真）

**判定定理：**
> 如果存在某个变量x，使得x和¬x在同一个强连通分量中，则2-SAT问题无解；否则有解。

**为什么？**
- 如果在同一个SCC中，说明x可以推出¬x，且¬x可以推出x
- 这意味着x和¬x必须同时为真，矛盾！

---

## 三、算法流程

```
1. 建图：每个变量拆成两个点，每个子句加两条边
2. 求SCC：使用Tarjan或Kosaraju算法
3. 判定：检查是否有变量的真假在同一SCC
4. 赋值：按拓扑序逆序赋值
```

---

## 四、完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 20005;  // 2倍变量数

vector<int> adj[MAXN];

int dfn[MAXN], low[MAXN], timestamp;
stack<int> st;
bool in_stack[MAXN];

int scc_id[MAXN], scc_cnt;
int n, m;  // n个变量，m个子句

/**
 * 变量x的编号：x（真）= 2*x，¬x（假）= 2*x+1
 * 或者：x = 2*x，¬x = 2*x^1（异或1实现取反）
 */
int var(int x, bool is_true) {
    return 2 * x + (is_true ? 0 : 1);
}

int NOT(int x) {
    return x ^ 1;  // 异或1，0变1，1变0
}

/**
 * 添加子句 (a OR b)
 * 即：¬a -> b，¬b -> a
 */
void add_clause(int a, int b) {
    adj[NOT(a)].push_back(b);
    adj[NOT(b)].push_back(a);
}

void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;
    st.push(u);
    in_stack[u] = true;
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (in_stack[v]) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    if (dfn[u] == low[u]) {
        scc_cnt++;
        int v;
        do {
            v = st.top(); st.pop();
            in_stack[v] = false;
            scc_id[v] = scc_cnt;
        } while (v != u);
    }
}

/**
 * 求解2-SAT
 * @return 是否有解
 */
bool solve() {
    // 初始化
    memset(dfn, 0, sizeof(dfn));
    memset(low, 0, sizeof(low));
    memset(in_stack, false, sizeof(in_stack));
    memset(scc_id, 0, sizeof(scc_id));
    timestamp = 0;
    scc_cnt = 0;
    
    // 求SCC
    for (int i = 0; i < 2 * n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 判定：检查每个变量的真假是否在同一SCC
    for (int i = 0; i < n; i++) {
        if (scc_id[2*i] == scc_id[2*i+1]) {
            return false;  // 矛盾，无解
        }
    }
    
    return true;  // 有解
}

/**
 * 构造一组可行解
 * 按拓扑序逆序赋值
 */
vector<bool> get_solution() {
    vector<bool> ans(n);
    
    // scc_id越大，拓扑序越靠前（越先被访问）
    // 所以我们按scc_id从大到小赋值
    for (int i = 0; i < n; i++) {
        // 如果x的SCC编号 > ¬x的SCC编号，x为真
        // 因为x在拓扑序前面，应该优先满足
        ans[i] = scc_id[2*i] > scc_id[2*i+1];
    }
    
    return ans;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    
    // 读入m个子句
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        
        // 转换为内部编号
        // 输入：正数表示真，负数表示假
        int u, v;
        if (a > 0) u = var(abs(a)-1, true);
        else u = var(abs(a)-1, false);
        
        if (b > 0) v = var(abs(b)-1, true);
        else v = var(abs(b)-1, false);
        
        add_clause(u, v);
    }
    
    if (solve()) {
        cout << "YES" << endl;
        vector<bool> ans = get_solution();
        for (int i = 0; i < n; i++) {
            cout << (ans[i] ? "True" : "False") << " ";
        }
        cout << endl;
    } else {
        cout << "NO" << endl;
    }
    
    return 0;
}
```

---

## 五、经典例题

### 例题1： Wedding（POJ 3683）

**题目大意：**
有n对新婚夫妇，每对夫妇必须且只能派一人参加婚礼。有些人之间有矛盾，不能同时出现。判断是否有可行方案。

**建图：**
- 每对夫妇拆成两个点：选丈夫、选妻子
- 如果两个人有矛盾，添加限制：不能同时选

### 例题2： Party at Hali-Bula（POJ 3207）

**题目大意：**
平面上n个点，m对点需要连接。连接可以是直线或弧线（在圆外）。判断是否可以做到连线不相交。

**建图：**
- 每对点拆成两个点：直线连接、弧线连接
- 如果两种连接方式会相交，添加限制

---

## 六、复杂度分析

| 步骤 | 时间复杂度 | 空间复杂度 |
|------|-----------|-----------|
| 建图 | $O(m)$ | $O(n + m)$ |
| 求SCC | $O(n + m)$ | $O(n)$ |
| 判定 | $O(n)$ | $O(1)$ |
| **总体** | **$O(n + m)$** | **$O(n + m)$** |

---

## 七、笔试面试高频问题

**Q1: 2-SAT为什么可以用SCC求解？**
> 每个子句(a∨b)可以转化为两条蕴含关系：¬a→b和¬b→a。这些蕴含关系形成有向图。如果x和¬x在同一SCC中，说明x和¬x互相可达，即x→¬x且¬x→x，这意味着x和¬x必须同时为真，矛盾！

**Q2: 如何构造一组可行解？**
> 按拓扑序逆序赋值。如果x的SCC编号大于¬x的SCC编号，说明x在拓扑序前面，应该优先满足，赋值为真。

**Q3: 2-SAT的时间复杂度？**
> O(n+m)，主要是求SCC的复杂度。

**Q4: 3-SAT能用这种方法吗？**
> 不能。3-SAT是NP完全问题，没有已知的多项式时间算法。SCC方法只适用于2-SAT。

---

## 八、ML/DL关联

### 布尔可满足性与神经网络

**神经SAT求解器：**
- 使用图神经网络（GNN）学习SAT问题的结构
- 将变量和子句建模为二分图
- 通过消息传递预测可满足性

**2-SAT的特殊性：**
- 2-SAT有线性时间算法，适合作为GNN的基准测试
- 可以生成大量2-SAT实例训练神经网络
- 研究GNN是否能学会类似SCC的推理

### 应用场景

**1. 电路设计验证**
- 数字电路的等价性检查可以转化为SAT问题
- 2-SAT用于验证简单电路

**2. 软件验证**
- 程序路径可行性分析
- 约束求解

**3. 配置问题**
- 软件包依赖解析
- 特征模型配置

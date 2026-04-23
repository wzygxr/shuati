# 高级LCA难题合集

## 1. 动态树上的LCA问题

### 问题1：动态加边树的LCA查询

**题目描述**：
给定一棵初始只包含节点1的树，有Q次操作，每次操作为以下两种之一：
1. `add u v`：在树中添加一条连接u和v的边（保证添加后仍为树结构）
2. `query u v`：查询节点u和v的LCA

**输入格式**：
- 第一行：Q (操作次数)
- 接下来Q行：每行为一个操作

**解法**：
使用Link-Cut Tree或动态树数据结构，支持动态加边和LCA查询。

```cpp
#include <iostream>
#include <vector>
using namespace std;

// 动态树的Link-Cut Tree实现（简化版）
const int MAXN = 1e5 + 5;

struct Node {
    int ch[2], fa;  // 左右子树，父节点
    int rev;        // 翻转标记
    int val;        // 节点值
    int min_depth;  // 子树最小深度
    int path_parent; // 虚边父节点
    
    Node() : rev(0), val(0), min_depth(0), path_parent(0) {
        ch[0] = ch[1] = fa = 0;
    }
} T[MAXN];

// 旋转操作
void rotate(int x) {
    int y = T[x].fa, z = T[y].fa;
    int k = (T[y].ch[1] == x);
    
    T[z].ch[T[z].ch[1] == y] = x; T[x].fa = z;
    T[y].ch[k] = T[x].ch[k^1]; if(T[x].ch[k^1]) T[T[x].ch[k^1]].fa = y;
    T[x].ch[k^1] = y; T[y].fa = x;
}

// Splay操作
void splay(int x) {
    while(T[x].fa) {
        int y = T[x].fa, z = T[y].fa;
        if(z) rotate((T[y].ch[1] == x) == (T[z].ch[1] == y) ? y : x);
        rotate(x);
    }
}

// 访问节点x到根的路径
int access(int x) {
    int last = 0;
    for(int y = x; y; y = T[y].path_parent) {
        splay(y);
        T[y].ch[1] = last;
        last = y;
    }
    return last;
}

// 断开节点x与其实际父节点的连接
void cut(int x) {
    access(x);
    splay(x);
    if(T[x].ch[0]) {
        T[T[x].ch[0]].fa = 0;
        T[x].ch[0] = 0;
    }
}

// 连接x和y（x是y的实际父节点）
void link(int x, int y) {
    cut(y);
    T[y].path_parent = x;
}

// 获取节点x的根
int findRoot(int x) {
    access(x);
    splay(x);
    int u = x;
    while(T[u].ch[0]) u = T[u].ch[0];
    splay(u);
    return u;
}

// 查询x和y的LCA
int getLCA(int x, int y) {
    access(x);
    return access(y);
}

int main() {
    int Q;
    cin >> Q;
    
    for(int i = 0; i < Q; i++) {
        string op;
        cin >> op;
        
        if(op == "add") {
            int u, v;
            cin >> u >> v;
            link(u, v);  // u是v的父节点
        } else if(op == "query") {
            int u, v;
            cin >> u >> v;
            cout << getLCA(u, v) << endl;
        }
    }
    
    return 0;
}
```

## 2. 带权树上的LCA问题

### 问题2：带边权树的路径和查询

**题目描述**：
给定一棵带边权的树，多次查询两点间路径上的边权和。

**解法**：
在LCA的基础上，维护从根到每个节点的路径权值和。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 1e5 + 5;
const int LOG = 20;

vector<pair<int, int>> adj[MAXN];  // 邻接表，存储(邻接节点, 边权)
int depth[MAXN], dist[MAXN];       // 节点深度，到根的路径权值和
int up[MAXN][LOG];                 // 倍增数组

void dfs(int u, int parent, int w) {
    depth[u] = depth[parent] + 1;
    dist[u] = dist[parent] + w;    // 从根到u的路径权值和
    
    up[u][0] = parent;
    for (int k = 1; k < LOG; k++) {
        if (up[u][k-1] != 0) up[u][k] = up[up[u][k-1]][k-1];
        else up[u][k] = 0;
    }
    
    for (auto& edge : adj[u]) {
        int v = edge.first, weight = edge.second;
        if (v != parent) {
            dfs(v, u, weight);
        }
    }
}

int getLCA(int u, int v) {
    if (depth[u] < depth[v]) swap(u, v);
    
    // 将u提升到与v相同深度
    int diff = depth[u] - depth[v];
    for (int k = 0; k < LOG; k++) {
        if (diff & (1 << k)) {
            u = up[u][k];
        }
    }
    
    if (u == v) return u;
    
    // 同时向上提升
    for (int k = LOG - 1; k >= 0; k--) {
        if (up[u][k] != up[v][k]) {
            u = up[u][k];
            v = up[v][k];
        }
    }
    
    return up[u][0];
}

// 查询u到v的路径权值和
int queryPathSum(int u, int v) {
    int lca = getLCA(u, v);
    return dist[u] + dist[v] - 2 * dist[lca];
}

int main() {
    int n;
    cin >> n;
    
    for (int i = 1; i < n; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        adj[u].push_back({v, w});
        adj[v].push_back({u, w});
    }
    
    dfs(1, 0, 0);
    
    int q;
    cin >> q;
    while (q--) {
        int u, v;
        cin >> u >> v;
        cout << "节点" << u << "到节点" << v << "的路径权值和: " 
             << queryPathSum(u, v) << endl;
    }
    
    return 0;
}
```

## 3. 欧拉序高级应用

### 问题3：子树查询与路径查询

**题目描述**：
在一棵树上支持以下操作：
1. `update u val`：将节点u的权值增加val
2. `query_subtree u`：查询以u为根的子树权值和
3. `query_path u v`：查询u到v路径上的权值和

**解法**：
结合欧拉序和树状数组/线段树。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 1e5 + 5;

vector<int> adj[MAXN];
int in[MAXN], out[MAXN];  // DFS序的进入和退出时间
int euler[MAXN * 2];      // 欧拉序列
int values[MAXN];         // 节点权值
int timer = 0;

// 构建欧拉序和DFS序
void dfs(int u, int parent) {
    in[u] = timer;
    euler[timer++] = u;
    
    for (int v : adj[u]) {
        if (v != parent) {
            dfs(v, u);
        }
    }
    
    out[u] = timer - 1;  // 实际上是子树结束位置
}

// 树状数组实现区间更新和单点查询
struct BIT {
    vector<long long> tree;
    int n;
    
    BIT(int _n) : n(_n) {
        tree.resize(n + 1, 0);
    }
    
    void update(int idx, long long val) {
        for (++idx; idx <= n; idx += idx & (-idx)) {
            tree[idx] += val;
        }
    }
    
    long long query(int idx) {
        long long res = 0;
        for (++idx; idx > 0; idx -= idx & (-idx)) {
            res += tree[idx];
        }
        return res;
    }
    
    // 区间更新：[l, r]增加val
    void rangeUpdate(int l, int r, long long val) {
        update(l, val);
        update(r + 1, -val);
    }
};

int main() {
    int n, q;
    cin >> n >> q;
    
    for (int i = 1; i <= n; i++) {
        cin >> values[i];
    }
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    dfs(1, 0);
    
    BIT bit_updates(timer);
    BIT bit_values(timer);
    
    // 初始化节点权值
    for (int i = 1; i <= n; i++) {
        bit_values.rangeUpdate(in[i], in[i], values[i]);
    }
    
    for (int i = 0; i < q; i++) {
        string op;
        cin >> op;
        
        if (op == "update") {
            int u, val;
            cin >> u >> val;
            bit_updates.rangeUpdate(in[u], in[u], val);
        } else if (op == "query_subtree") {
            int u;
            cin >> u;
            long long result = bit_values.query(out[u]) - bit_values.query(in[u] - 1) +
                              bit_updates.query(out[u]) - bit_updates.query(in[u] - 1);
            cout << "子树" << u << "的权值和: " << result << endl;
        } else if (op == "query_path") {
            int u, v;
            cin >> u >> v;
            // 这里需要结合LCA来实现路径查询
            cout << "路径查询需要结合LCA实现，略" << endl;
        }
    }
    
    return 0;
}
```

## 4. 路径统计问题

### 问题4：路径上特定值的计数

**题目描述**：
在一棵树上，每个节点有一个权值。多次查询路径上特定值的出现次数。

**解法**：
使用莫队算法在欧拉序上处理。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <map>
using namespace std;

const int MAXN = 1e5 + 5;
const int SQRT = 320;  // sqrt(100000)

vector<int> adj[MAXN];
int values[MAXN], first[MAXN], last[MAXN];
vector<int> euler;
int block_size;

void eulerTour(int u, int parent) {
    first[u] = euler.size();
    euler.push_back(values[u]);
    
    for (int v : adj[u]) {
        if (v != parent) {
            eulerTour(v, u);
        }
    }
    
    last[u] = euler.size();
    euler.push_back(values[u]);  // 退出时再次记录
}

struct Query {
    int l, r, id, val;
    bool operator<(const Query& other) const {
        int block_l = l / block_size;
        int block_r = r / block_size;
        if (block_l != block_r) return block_l < block_r;
        return (block_r & 1) ? r < other.r : r > other.r;  // 奇偶优化
    }
};

int main() {
    int n, q;
    cin >> n >> q;
    
    for (int i = 1; i <= n; i++) {
        cin >> values[i];
    }
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    eulerTour(1, 0);
    block_size = max(1, (int)sqrt(euler.size()));
    
    vector<Query> queries(q);
    for (int i = 0; i < q; i++) {
        int u, v, target_val;
        cin >> u >> v >> target_val;
        int l = first[u], r = first[v];
        if (l > r) swap(l, r);
        queries[i] = {l, r, i, target_val};
    }
    
    sort(queries.begin(), queries.end());
    
    vector<int> results(q);
    int current_l = 0, current_r = -1;
    map<int, int> count;
    int answer = 0;
    
    for (auto& query : queries) {
        int l = query.l, r = query.r;
        
        while (current_l > l) {
            current_l--;
            count[euler[current_l]]++;
            if (euler[current_l] == query.val) answer++;
        }
        while (current_r < r) {
            current_r++;
            count[euler[current_r]]++;
            if (euler[current_r] == query.val) answer++;
        }
        while (current_l < l) {
            if (euler[current_l] == query.val) answer--;
            count[euler[current_l]]--;
            current_l++;
        }
        while (current_r > r) {
            if (euler[current_r] == query.val) answer--;
            count[euler[current_r]]--;
            current_r--;
        }
        
        results[query.id] = answer;
    }
    
    for (int result : results) {
        cout << result << endl;
    }
    
    return 0;
}
```

## 5. 树上启发式合并

### 问题5：树上颜色种类统计

**题目描述**：
在一棵树上，每个节点有一种颜色。对于每个节点u，统计以u为根的子树中不同颜色的数量。

**解法**：
使用启发式合并（DSU on Tree）。

```cpp
#include <iostream>
#include <vector>
#include <map>
#include <set>
using namespace std;

const int MAXN = 1e5 + 5;

vector<int> adj[MAXN];
int color[MAXN];
int subtree_size[MAXN];
int ans[MAXN];
map<int, int> *cnt[MAXN];  // 每个节点子树中各颜色的数量

void dfs_size(int u, int parent) {
    subtree_size[u] = 1;
    for (int v : adj[u]) {
        if (v != parent) {
            dfs_size(v, u);
            subtree_size[u] += subtree_size[v];
        }
    }
}

void add(int u, int parent, int x, int v) {
    cnt[v]->operator[](color[u]) += x;
    for (int w : adj[u]) {
        if (w != parent) {
            add(w, u, x, v);
        }
    }
}

void dfs(int u, int parent, bool keep) {
    int mx = -1, big_child = -1;
    for (int v : adj[u]) {
        if (v != parent && subtree_size[v] > mx) {
            mx = subtree_size[v];
            big_child = v;
        }
    }
    
    for (int v : adj[u]) {
        if (v != parent && v != big_child) {
            dfs(v, u, 0);
        }
    }
    
    if (big_child != -1) {
        dfs(big_child, u, 1);
        cnt[u] = cnt[big_child];
    } else {
        cnt[u] = new map<int, int>();
    }
    
    // 添加轻儿子的贡献
    for (int v : adj[u]) {
        if (v != parent && v != big_child) {
            add(v, u, 1, u);
        }
    }
    
    // 添加当前节点
    (*cnt[u])[color[u]]++;
    
    // 计算答案
    ans[u] = cnt[u]->size();
    
    if (keep == 0) {
        add(u, parent, -1, u);
    }
}

int main() {
    int n;
    cin >> n;
    
    for (int i = 1; i <= n; i++) {
        cin >> color[i];
    }
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    dfs_size(1, 0);
    dfs(1, 0, 1);
    
    for (int i = 1; i <= n; i++) {
        cout << "节点" << i << "子树中不同颜色数量: " << ans[i] << endl;
    }
    
    return 0;
}
```

## 6. 综合难题：树上路径问题

### 问题6：树上路径最大值查询

**题目描述**：
在一棵带边权的树上，多次查询从节点u到节点v路径上的最大边权。

**解法**：
使用倍增法维护路径上的最大值信息。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

const int MAXN = 1e5 + 5;
const int LOG = 20;
const int INF = 1e9;

vector<pair<int, int>> adj[MAXN];  // (邻接节点, 边权)
int depth[MAXN], up[MAXN][LOG], max_up[MAXN][LOG];  // max_up[u][k]表示u到其2^k级祖先路径上的最大边权

void dfs(int u, int parent, int parent_weight) {
    depth[u] = depth[parent] + 1;
    
    up[u][0] = parent;
    max_up[u][0] = parent_weight;
    
    for (int k = 1; k < LOG; k++) {
        if (up[u][k-1] != 0) {
            up[u][k] = up[up[u][k-1]][k-1];
            max_up[u][k] = max(max_up[u][k-1], max_up[up[u][k-1]][k-1]);
        } else {
            up[u][k] = 0;
            max_up[u][k] = -INF;
        }
    }
    
    for (auto& edge : adj[u]) {
        int v = edge.first, weight = edge.second;
        if (v != parent) {
            dfs(v, u, weight);
        }
    }
}

// 查询从u到其祖先节点的路径上的最大边权
int getMaxOnPath(int u, int target_depth) {
    if (depth[u] <= target_depth) return -INF;
    
    int diff = depth[u] - target_depth;
    int result = -INF;
    
    for (int k = 0; k < LOG; k++) {
        if (diff & (1 << k)) {
            result = max(result, max_up[u][k]);
            u = up[u][k];
        }
    }
    
    return result;
}

int queryMaxEdge(int u, int v) {
    if (u == v) return -INF;
    
    int original_u = u, original_v = v;
    int result = -INF;
    
    // 将深度更深的节点提升到与另一节点相同深度
    if (depth[u] < depth[v]) swap(u, v);
    
    int diff = depth[u] - depth[v];
    for (int k = 0; k < LOG; k++) {
        if (diff & (1 << k)) {
            result = max(result, max_up[u][k]);
            u = up[u][k];
        }
    }
    
    if (u == v) {
        // v是u的祖先
        return result;
    }
    
    // 同时向上提升，直到找到LCA
    for (int k = LOG - 1; k >= 0; k--) {
        if (up[u][k] != up[v][k]) {
            result = max(result, max(max_up[u][k], max_up[v][k]));
            u = up[u][k];
            v = up[v][k];
        }
    }
    
    // 最后处理u和v到它们LCA的路径
    result = max(result, max(max_up[u][0], max_up[v][0]));
    
    return result;
}

int main() {
    int n;
    cin >> n;
    
    for (int i = 1; i < n; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        adj[u].push_back({v, w});
        adj[v].push_back({u, w});
    }
    
    dfs(1, 0, -INF);
    
    int q;
    cin >> q;
    while (q--) {
        int u, v;
        cin >> u >> v;
        int result = queryMaxEdge(u, v);
        cout << "节点" << u << "到节点" << v << "路径上的最大边权: " 
             << (result == -INF ? 0 : result) << endl;
    }
    
    return 0;
}
```

这些高级题目涵盖了LCA算法的各种复杂应用场景，包括动态树、带权树、路径统计、启发式合并等，体现了LCA在算法竞赛和实际应用中的重要性。
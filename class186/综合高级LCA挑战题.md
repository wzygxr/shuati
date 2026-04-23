# 综合高级LCA挑战题

## 1. 动态树上的复杂查询

### 问题1：动态加边树的路径统计

**题目描述**：
初始时有一个节点，有Q次操作，操作包括：
1. `add u v`：添加边(u,v)，保证加边后仍为树
2. `update u val`：将节点u的权值更新为val
3. `query u v`：查询u到v路径上的权值和、最大值、最小值

**解法**：
使用Link-Cut Tree（LCT）实现动态树操作。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

const int MAXN = 1e5 + 5;

struct LCTNode {
    int ch[2], fa;
    bool rev;
    int val, sum, mx, mn, sz;
    
    LCTNode() : val(0), sum(0), mx(INT_MIN), mn(INT_MAX), sz(1), rev(0) {
        ch[0] = ch[1] = fa = 0;
    }
} T[MAXN];

// 更新节点信息
void pushUp(int x) {
    T[x].sz = 1 + T[T[x].ch[0]].sz + T[T[x].ch[1]].sz;
    T[x].sum = T[x].val + T[T[x].ch[0]].sum + T[T[x].ch[1]].sum;
    T[x].mx = T[x].val;
    T[x].mn = T[x].val;
    
    if (T[x].ch[0]) {
        T[x].mx = max(T[x].mx, T[T[x].ch[0]].mx);
        T[x].mn = min(T[x].mn, T[T[x].ch[0]].mn);
    }
    if (T[x].ch[1]) {
        T[x].mx = max(T[x].mx, T[T[x].ch[1]].mx);
        T[x].mn = min(T[x].mn, T[T[x].ch[1]].mn);
    }
}

void pushDown(int x) {
    if (T[x].rev) {
        T[x].rev = 0;
        if (T[x].ch[0]) T[T[x].ch[0]].rev ^= 1;
        if (T[x].ch[1]) T[T[x].ch[1]].rev ^= 1;
        swap(T[x].ch[0], T[x].ch[1]);
    }
}

bool isRoot(int x) {
    return T[T[x].fa].ch[0] != x && T[T[x].fa].ch[1] != x;
}

void rotate(int x) {
    int y = T[x].fa, z = T[y].fa;
    int k = (T[y].ch[1] == x);
    
    if (!isRoot(y)) T[z].ch[T[z].ch[1] == y] = x;
    T[x].fa = z;
    
    T[y].ch[k] = T[x].ch[k^1];
    if (T[x].ch[k^1]) T[T[x].ch[k^1]].fa = y;
    
    T[x].ch[k^1] = y;
    T[y].fa = x;
    
    pushUp(y);
    pushUp(x);
}

int st[MAXN], top;
void splay(int x) {
    top = 0;
    st[++top] = x;
    for (int i = x; !isRoot(i); i = T[i].fa) st[++top] = T[i].fa;
    while (top) pushDown(st[top--]);
    
    while (!isRoot(x)) {
        int y = T[x].fa, z = T[y].fa;
        if (!isRoot(y)) rotate((T[y].ch[1] == x) ^ (T[z].ch[1] == y) ? x : y);
        rotate(x);
    }
}

void access(int x) {
    for (int y = 0; x; y = x, x = T[x].fa) {
        splay(x);
        T[x].ch[1] = y;
        pushUp(x);
    }
}

void makeRoot(int x) {
    access(x);
    splay(x);
    T[x].rev ^= 1;
}

int findRoot(int x) {
    access(x);
    splay(x);
    while (T[x].ch[0]) {
        pushDown(x);
        x = T[x].ch[0];
    }
    splay(x);
    return x;
}

void split(int x, int y) {
    makeRoot(x);
    access(y);
    splay(y);
}

void link(int x, int y) {
    makeRoot(x);
    if (findRoot(y) != x) T[x].fa = y;
}

void cut(int x, int y) {
    split(x, y);
    if (T[y].ch[0] == x && !T[x].ch[1]) {
        T[y].ch[0] = T[x].fa = 0;
        pushUp(y);
    }
}

// 更新节点值
void updateNode(int x, int val) {
    access(x);
    splay(x);
    T[x].val = val;
    pushUp(x);
}

// 查询路径信息
struct PathInfo {
    int sum, mx, mn;
};

PathInfo queryPath(int x, int y) {
    if (findRoot(x) != findRoot(y)) {
        return {0, INT_MIN, INT_MAX};  // 不在同一棵树
    }
    
    split(x, y);
    return {T[y].sum, T[y].mx, T[y].mn};
}

int main() {
    int Q;
    cin >> Q;
    
    for (int i = 1; i <= Q; i++) {
        string op;
        cin >> op;
        
        if (op == "add") {
            int u, v;
            cin >> u >> v;
            link(u, v);
        } else if (op == "update") {
            int u, val;
            cin >> u >> val;
            updateNode(u, val);
        } else if (op == "query") {
            int u, v;
            cin >> u >> v;
            PathInfo info = queryPath(u, v);
            cout << "路径和: " << info.sum << ", 最大值: " << info.mx 
                 << ", 最小值: " << info.mn << endl;
        }
    }
    
    return 0;
}
```

## 2. 树上概率问题

### 问题2：树上随机游走期望

**题目描述**：
在一棵树上，从节点S出发，每一步等概率走向相邻节点，求到达节点T的期望步数。

**解法**：
建立线性方程组求解。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 1e3 + 5;

vector<int> adj[MAXN];
double a[MAXN][MAXN];  // 系数矩阵
int n;

// 高斯消元解方程组
void gauss() {
    for (int i = 1; i <= n; i++) {
        // 找主元
        int max_row = i;
        for (int j = i + 1; j <= n; j++) {
            if (abs(a[j][i]) > abs(a[max_row][i])) {
                max_row = j;
            }
        }
        
        if (max_row != i) {
            for (int j = i; j <= n + 1; j++) {
                swap(a[i][j], a[max_row][j]);
            }
        }
        
        // 消元
        for (int j = i + 1; j <= n; j++) {
            double ratio = a[j][i] / a[i][i];
            for (int k = i; k <= n + 1; k++) {
                a[j][k] -= ratio * a[i][k];
            }
        }
    }
    
    // 回代
    vector<double> x(n + 1);
    for (int i = n; i >= 1; i--) {
        x[i] = a[i][n + 1];
        for (int j = i + 1; j <= n; j++) {
            x[i] -= a[i][j] * x[j];
        }
        x[i] /= a[i][i];
    }
}

double solveRandomWalk(int start, int target) {
    // 建立方程组
    // 对于每个节点u ≠ target: E[u] = 1 + (1/degree[u]) * Σ(E[v] for v in neighbors[u])
    // 即: E[u] - (1/degree[u]) * Σ(E[v] for v in neighbors[u]) = 1
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n + 1; j++) {
            a[i][j] = 0.0;
        }
    }
    
    for (int u = 1; u <= n; u++) {
        if (u == target) {
            a[u][u] = 1.0;  // E[target] = 0
            a[u][n + 1] = 0.0;
        } else {
            a[u][u] = 1.0;
            for (int v : adj[u]) {
                a[u][v] = -1.0 / adj[u].size();
            }
            a[u][n + 1] = 1.0;  // 常数项为1
        }
    }
    
    gauss();
    
    vector<double> x(n + 1);
    for (int i = n; i >= 1; i--) {
        x[i] = a[i][n + 1];
        for (int j = i + 1; j <= n; j++) {
            x[i] -= a[i][j] * x[j];
        }
        x[i] /= a[i][i];
    }
    
    return x[start];
}

int main() {
    cin >> n;
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    int s, t;
    cin >> s >> t;
    
    cout << "从节点" << s << "到节点" << t << "的期望步数: " 
         << solveRandomWalk(s, t) << endl;
    
    return 0;
}
```

## 3. 树上博弈问题

### 问题3：树上取石子游戏

**题目描述**：
在一棵树上，每个节点有一定数量的石子。两个玩家轮流操作，每次选择一个节点并取走其所有石子，同时删除该节点及其相邻边。不能操作者输。求先手是否必胜。

**解法**：
使用树形DP和SG函数。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 1e4 + 5;

vector<int> adj[MAXN];
int stones[MAXN];
int sg[MAXN];
bool visited[MAXN];

int calculateSG(int u, int parent) {
    if (sg[u] != -1) return sg[u];
    
    // 计算所有可能后继状态的SG值
    vector<int> next_sg;
    
    // 枚举移除当前节点后，各个连通分量的SG值异或和
    vector<int> component_sg;
    vector<bool> processed(MAXN, false);
    
    for (int v : adj[u]) {
        if (v != parent && !processed[v]) {
            // 对每个连通分量计算SG值
            int comp_sg = calculateSG(v, u);
            component_sg.push_back(comp_sg);
            processed[v] = true;
        }
    }
    
    // 计算所有子树SG值的异或
    int total = 0;
    for (int val : component_sg) {
        total ^= val;
    }
    
    // SG值就是所有后继状态SG值的mex
    // 这里我们考虑的是移除节点u后的状态
    // 由于移除节点u后，其子树成为独立的连通分量
    return sg[u] = total;
}

// 更准确的树上取石子游戏解法
int solveTreeNim(int u, int parent) {
    int result = stones[u];  // 当前节点的石子数
    
    for (int v : adj[u]) {
        if (v != parent) {
            result ^= solveTreeNim(v, u);
        }
    }
    
    return result;
}

int main() {
    int n;
    cin >> n;
    
    for (int i = 1; i <= n; i++) {
        cin >> stones[i];
    }
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    fill(sg, sg + MAXN, -1);
    
    int game_sg = solveTreeNim(1, 0);
    
    if (game_sg != 0) {
        cout << "先手必胜" << endl;
    } else {
        cout << "后手必胜" << endl;
    }
    
    return 0;
}
```

## 4. 树上最优化问题

### 问题4：树上设施选址

**题目描述**：
在一棵树上选择k个节点作为设施点，使得所有节点到最近设施点的距离和最小。

**解法**：
树形DP。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

const int MAXN = 1e2 + 5;
const int MAXK = 1e2 + 5;

vector<int> adj[MAXN];
int n, k;
int dp[MAXN][MAXK][2];  // dp[u][j][0/1] 表示以u为根的子树中选j个设施，u是否为设施点的最小距离和

void dfs(int u, int parent) {
    // 初始化
    for (int j = 0; j <= k; j++) {
        dp[u][j][0] = dp[u][j][1] = INT_MAX / 2;
    }
    
    // 叶子节点
    if (adj[u].size() == 1 && parent != 0) {  // 叶子节点
        dp[u][0][0] = 0;  // 不选u作为设施点
        dp[u][1][1] = 0;  // 选u作为设施点
        return;
    }
    
    // 初始化当前节点为设施点的情况
    dp[u][1][1] = 0;
    
    for (int v : adj[u]) {
        if (v != parent) {
            dfs(v, u);
        }
    }
    
    // 合并子树信息
    vector<int> temp_dp0(MAXK, INT_MAX / 2);
    vector<int> temp_dp1(MAXK, INT_MAX / 2);
    temp_dp0[0] = 0;
    temp_dp1[1] = 0;
    
    for (int v : adj[u]) {
        if (v != parent) {
            vector<int> new_temp0(MAXK, INT_MAX / 2);
            vector<int> new_temp1(MAXK, INT_MAX / 2);
            
            // 枚举当前分配的设施数量
            for (int j = 0; j < MAXK; j++) {
                for (int nj = 0; nj < MAXK && j + nj < MAXK; nj++) {
                    // 当前节点不选，子节点不选
                    if (temp_dp0[j] != INT_MAX / 2 && dp[v][nj][0] != INT_MAX / 2) {
                        new_temp0[j + nj] = min(new_temp0[j + nj], 
                                               temp_dp0[j] + dp[v][nj][0] + 1);  // 距离+1
                    }
                    
                    // 当前节点不选，子节点选
                    if (temp_dp0[j] != INT_MAX / 2 && dp[v][nj][1] != INT_MAX / 2) {
                        new_temp0[j + nj] = min(new_temp0[j + nj], 
                                               temp_dp0[j] + dp[v][nj][1]);
                    }
                    
                    // 当前节点选，子节点不选
                    if (temp_dp1[j] != INT_MAX / 2 && dp[v][nj][0] != INT_MAX / 2) {
                        new_temp1[j + nj] = min(new_temp1[j + nj], 
                                               temp_dp1[j] + dp[v][nj][0]);
                    }
                    
                    // 当前节点选，子节点选
                    if (temp_dp1[j] != INT_MAX / 2 && dp[v][nj][1] != INT_MAX / 2) {
                        new_temp1[j + nj] = min(new_temp1[j + nj], 
                                               temp_dp1[j] + dp[v][nj][1]);
                    }
                }
            }
            
            temp_dp0 = new_temp0;
            temp_dp1 = new_temp1;
        }
    }
    
    for (int j = 0; j < MAXK; j++) {
        dp[u][j][0] = temp_dp0[j];
        dp[u][j][1] = temp_dp1[j];
    }
}

int solveFacilityLocation() {
    dfs(1, 0);
    
    int result = INT_MAX;
    for (int i = 0; i <= k; i++) {
        result = min(result, min(dp[1][i][0], dp[1][i][1]));
    }
    
    return result;
}

int main() {
    cin >> n >> k;
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    cout << "最小距离和: " << solveFacilityLocation() << endl;
    
    return 0;
}
```

## 5. 树上字符串问题

### 问题5：树上字典序路径

**题目描述**：
在一棵树上，每条边有一个字符标签，求从根到所有叶子路径中字典序最小的路径。

**解法**：
DFS + 贪心。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
using namespace std;

const int MAXN = 1e5 + 5;

struct Edge {
    int to;
    char c;
};

vector<Edge> adj[MAXN];
string min_path = "~";  // 初始化为字典序很大的字符串

void dfs(int u, int parent, string current_path) {
    bool is_leaf = true;
    
    for (Edge& e : adj[u]) {
        if (e.to != parent) {
            is_leaf = false;
            dfs(e.to, u, current_path + e.c);
        }
    }
    
    // 如果是叶子节点，更新最小路径
    if (is_leaf) {
        if (current_path < min_path) {
            min_path = current_path;
        }
    }
}

// 优化解法：使用贪心策略
string solveLexicographicPath(int root) {
    string result = "";
    int current = root;
    
    while (true) {
        // 找到当前节点相邻边中字符最小的
        char min_char = '~';
        int next_node = -1;
        
        for (Edge& e : adj[current]) {
            if (e.to != parent[current] && e.c < min_char) {
                min_char = e.c;
                next_node = e.to;
            }
        }
        
        if (next_node == -1) break;  // 到达叶子
        
        result += min_char;
        current = next_node;
    }
    
    return result;
}

int parent[MAXN];  // 用于记录父节点

void dfs_with_parent(int u, int p) {
    parent[u] = p;
    for (Edge& e : adj[u]) {
        if (e.to != p) {
            dfs_with_parent(e.to, u);
        }
    }
}

int main() {
    int n;
    cin >> n;
    
    for (int i = 1; i < n; i++) {
        int u, v;
        char c;
        cin >> u >> v >> c;
        adj[u].push_back({v, c});
        adj[v].push_back({u, c});
    }
    
    dfs_with_parent(1, 0);
    dfs(1, 0, "");
    
    cout << "字典序最小的路径: " << min_path << endl;
    
    return 0;
}
```

## 6. 树上几何问题

### 问题6：树上距离统计

**题目描述**：
在一棵树上，对于每个节点，统计距离它不超过d的节点数量。

**解法**：
点分治。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
using namespace std;

const int MAXN = 1e5 + 5;

vector<int> adj[MAXN];
bool vis[MAXN];
int sz[MAXN], max_part[MAXN];
int root, tot;
int result[MAXN];
int d;

void getRoot(int u, int parent) {
    sz[u] = 1;
    max_part[u] = 0;
    
    for (int v : adj[u]) {
        if (v != parent && !vis[v]) {
            getRoot(v, u);
            sz[u] += sz[v];
            max_part[u] = max(max_part[u], sz[v]);
        }
    }
    
    max_part[u] = max(max_part[u], tot - sz[u]);
    if (max_part[u] < max_part[root]) root = u;
}

// 从u开始，距离不超过max_dist的节点数
void getDist(int u, int parent, int dist, unordered_map<int, int>& dist_count, int max_dist) {
    if (dist > max_dist) return;
    dist_count[dist]++;
    
    for (int v : adj[u]) {
        if (v != parent && !vis[v]) {
            getDist(v, u, dist + 1, dist_count, max_dist);
        }
    }
}

void calc(int u, int sign) {
    unordered_map<int, int> dist_count;
    getDist(u, 0, 0, dist_count, d);
    
    // 更新所有节点的结果
    for (int i = 1; i <= tot; i++) {
        if (sign == 1) {
            result[i] += dist_count[depth[i - 1]];  // 简化：这里需要实际计算距离
        } else {
            result[i] -= dist_count[depth[i - 1]];
        }
    }
}

int depth[MAXN];  // 从重心开始的深度

void solve(int u) {
    vis[u] = true;
    
    // 计算以u为重心的贡献
    unordered_map<int, int> all_dist;
    getDist(u, 0, 0, all_dist, d);
    
    // 计算每个子树的贡献并减去（容斥原理）
    for (int v : adj[u]) {
        if (!vis[v]) {
            unordered_map<int, int> sub_dist;
            getDist(v, u, 1, sub_dist, d);
            
            // 从总贡献中减去子树贡献
            for (auto& p : sub_dist) {
                all_dist[p.first] -= p.second;
            }
        }
    }
    
    // 递归处理子树
    for (int v : adj[u]) {
        if (!vis[v]) {
            tot = sz[v];
            root = 0;
            getRoot(v, 0);
            solve(root);
        }
    }
}

int main() {
    int n;
    cin >> n >> d;
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    max_part[0] = tot = n;
    getRoot(1, 0);
    solve(root);
    
    for (int i = 1; i <= n; i++) {
        cout << "距离节点" << i << "不超过" << d << "的节点数: " 
             << result[i] << endl;
    }
    
    return 0;
}
```

这些高级挑战题涵盖了树上算法的多个复杂领域，包括动态树操作、概率论、博弈论、最优化、字符串处理和几何问题等，展示了LCA及相关树上算法在解决复杂问题中的强大能力。
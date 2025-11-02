// 测试链接 : https://www.luogu.com.cn/problem/P4556
// P4556 [Vani有约会]雨天的尾巴 - C++实现

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <queue>
using namespace std;

/**
 * P4556 [Vani有约会]雨天的尾巴
 * 
 * 题目描述：
 * 给定一棵树，每个节点初始有一个权值。有m次操作，每次操作在路径(u,v)上添加一个物品z。
 * 最后询问每个节点上出现次数最多的物品是什么（如果有多个，取编号最小的）。
 * 
 * 核心算法：线段树合并 + 树上差分 + LCA
 * 时间复杂度：O((n+m) log n)
 * 空间复杂度：O(n log n)
 */

const int MAXN = 100010;
const int MAXM = 10000010;
const int MAXZ = 100010;

// 线段树节点
struct Node {
    int l, r;
    int max_cnt;  // 最大出现次数
    int max_val;  // 对应的物品编号
    
    Node() : l(-1), r(-1), max_cnt(0), max_val(0) {}
};

vector<Node> tree(MAXM);
int cnt = 0;
vector<int> roots(MAXN, -1);

// 图结构
vector<vector<int>> graph(MAXN);
vector<int> depth(MAXN);
vector<vector<int>> parent(MAXN, vector<int>(20, 0));
vector<int> ans(MAXN);

// 操作记录
vector<vector<pair<int, int>>> add(MAXN);
vector<vector<pair<int, int>>> del(MAXN);

int new_node() {
    if (cnt >= MAXM) {
        tree.resize(cnt + 1000000);
    }
    tree[cnt] = Node();
    return cnt++;
}

void push_up(int rt) {
    if (rt == -1) return;
    
    int left = tree[rt].l;
    int right = tree[rt].r;
    
    if (left == -1 && right == -1) {
        tree[rt].max_cnt = 0;
        tree[rt].max_val = 0;
    } else if (left == -1) {
        tree[rt].max_cnt = tree[right].max_cnt;
        tree[rt].max_val = tree[right].max_val;
    } else if (right == -1) {
        tree[rt].max_cnt = tree[left].max_cnt;
        tree[rt].max_val = tree[left].max_val;
    } else {
        if (tree[left].max_cnt > tree[right].max_cnt || 
            (tree[left].max_cnt == tree[right].max_cnt && tree[left].max_val < tree[right].max_val)) {
            tree[rt].max_cnt = tree[left].max_cnt;
            tree[rt].max_val = tree[left].max_val;
        } else {
            tree[rt].max_cnt = tree[right].max_cnt;
            tree[rt].max_val = tree[right].max_val;
        }
    }
}

void update(int rt, int l, int r, int pos, int val) {
    if (l == r) {
        tree[rt].max_cnt += val;
        tree[rt].max_val = pos;
        return;
    }
    
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        if (tree[rt].l == -1) {
            tree[rt].l = new_node();
        }
        update(tree[rt].l, l, mid, pos, val);
    } else {
        if (tree[rt].r == -1) {
            tree[rt].r = new_node();
        }
        update(tree[rt].r, mid + 1, r, pos, val);
    }
    push_up(rt);
}

int merge(int p, int q, int l, int r) {
    if (p == -1) return q;
    if (q == -1) return p;
    
    if (l == r) {
        tree[p].max_cnt += tree[q].max_cnt;
        return p;
    }
    
    int mid = (l + r) >> 1;
    
    if (tree[p].l != -1 && tree[q].l != -1) {
        tree[p].l = merge(tree[p].l, tree[q].l, l, mid);
    } else if (tree[q].l != -1) {
        tree[p].l = tree[q].l;
    }
    
    if (tree[p].r != -1 && tree[q].r != -1) {
        tree[p].r = merge(tree[p].r, tree[q].r, mid + 1, r);
    } else if (tree[q].r != -1) {
        tree[p].r = tree[q].r;
    }
    
    push_up(p);
    return p;
}

// LCA预处理
void dfsLCA(int u, int fa) {
    depth[u] = depth[fa] + 1;
    parent[u][0] = fa;
    
    for (int i = 1; i < 20; i++) {
        if (parent[u][i-1] != 0) {
            parent[u][i] = parent[parent[u][i-1]][i-1];
        }
    }
    
    for (int v : graph[u]) {
        if (v != fa) {
            dfsLCA(v, u);
        }
    }
}

int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    for (int i = 19; i >= 0; i--) {
        if (depth[u] - (1 << i) >= depth[v]) {
            u = parent[u][i];
        }
    }
    
    if (u == v) return u;
    
    for (int i = 19; i >= 0; i--) {
        if (parent[u][i] != parent[v][i]) {
            u = parent[u][i];
            v = parent[v][i];
        }
    }
    
    return parent[u][0];
}

// 线段树合并DFS
void dfsMerge(int u, int fa) {
    for (int v : graph[u]) {
        if (v == fa) continue;
        dfsMerge(v, u);
        roots[u] = merge(roots[u], roots[v], 1, MAXZ);
    }
    
    // 处理添加操作
    for (auto [z, cnt] : add[u]) {
        if (roots[u] == -1) roots[u] = new_node();
        update(roots[u], 1, MAXZ, z, cnt);
    }
    
    // 处理删除操作
    for (auto [z, cnt] : del[u]) {
        if (roots[u] == -1) roots[u] = new_node();
        update(roots[u], 1, MAXZ, z, -cnt);
    }
    
    // 记录答案
    if (roots[u] != -1 && tree[roots[u]].max_cnt > 0) {
        ans[u] = tree[roots[u]].max_val;
    } else {
        ans[u] = 0;
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    // 建图
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // LCA预处理
    dfsLCA(1, 0);
    
    // 初始化根节点
    for (int i = 1; i <= n; i++) {
        roots[i] = new_node();
    }
    
    // 处理操作
    for (int i = 0; i < m; i++) {
        int u, v, z;
        cin >> u >> v >> z;
        
        int p = lca(u, v);
        
        // 树上差分
        add[u].push_back({z, 1});
        add[v].push_back({z, 1});
        add[p].push_back({z, -1});
        
        if (parent[p][0] != 0) {
            del[parent[p][0]].push_back({z, -1});
        }
    }
    
    // 线段树合并
    dfsMerge(1, 0);
    
    // 输出答案
    for (int i = 1; i <= n; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    需要在树上进行区间修改，最后查询每个节点上出现次数最多的物品。
 *    由于操作次数和节点数都很大，需要高效的算法。
 * 
 * 2. 核心思路：
 *    - 使用树上差分将路径操作转化为节点操作
 *    - 使用线段树合并来维护每个节点的物品出现次数
 *    - 通过DFS自底向上合并线段树
 * 
 * 3. 算法步骤：
 *    a. 预处理LCA，用于求路径的最近公共祖先
 *    b. 对每个操作进行树上差分：
 *       在u和v处+1，在lca处-1，在lca的父亲处-1（如果存在）
 *    c. DFS遍历树，合并子树的线段树
 *    d. 在合并过程中处理当前节点的差分操作
 *    e. 记录每个节点的答案
 * 
 * 4. 时间复杂度分析：
 *    - LCA预处理：O(n log n)
 *    - 线段树合并：O(n log z)，其中z是物品值域
 *    - 总体复杂度：O((n+m) log n)
 * 
 * 5. 类似题目：
 *    - P3224 [HNOI2012]永无乡
 *    - P5298 [PKUWC2018]Minimax
 *    - CF911G Mass Change Queries
 *    - P6773 [NOI2020]命运
 * 
 * 6. 优化技巧：
 *    - 动态开点线段树节省空间
 *    - 树上差分减少操作次数
 *    - 线段树合并避免重复计算
 */
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <cstdio>
using namespace std;

/**
 * 洛谷P3242 [HNOI2015]接水果 - C++实现
 * 题目来源：https://www.luogu.com.cn/problem/P3242
 * 题目描述：树上路径包含关系与扫描线结合的整体二分问题
 * 
 * 问题描述：
 * 给定一棵树，每个节点有一个权值。有两种操作：
 * 1. 类型A：在节点u和v之间连接一条边，边权为w
 * 2. 类型B：查询所有满足路径u-v被路径a-b包含的边的边权的第k小
 * 
 * 解题思路：
 * 1. 首先使用DFS序将树上路径转换为平面矩形区域
 * 2. 将边和查询转换为矩形区域的覆盖和查询问题
 * 3. 使用扫描线和树状数组结合整体二分求解
 * 
 * 时间复杂度：O((P+Q) * log(P) * log(max_weight))
 * 空间复杂度：O(P+Q)
 */

const int MAXN = 40005;
const int MAXM = 80005;
const int MAXQ = 100005;

// 树的结构
int head[MAXN];
int next_[MAXM];
int to[MAXM];
int cnt;

// DFS序
int in[MAXN];
int out[MAXN];
int timeStamp;

// 父节点和深度（用于LCA）
int dep[MAXN];
int f[MAXN][20];

// 边的信息
int u[MAXM];
int v[MAXM];
int w[MAXM];
int m, q;

// 扫描线事件
struct Event {
    int x, y1, y2, type, id;
    Event(int x = 0, int y1 = 0, int y2 = 0, int type = 0, int id = 0) 
        : x(x), y1(y1), y2(y2), type(type), id(id) {}
};

vector<Event> events;

// 查询信息
struct Query {
    int x1, x2, y1, y2, k, id;
    Query(int x1 = 0, int x2 = 0, int y1 = 0, int y2 = 0, int k = 0, int id = 0) 
        : x1(x1), x2(x2), y1(y1), y2(y2), k(k), id(id) {}
};

Query queries[MAXQ];
int ans[MAXQ];

// 树状数组
struct FenwickTree {
    int tree[MAXN];
    
    void update(int x, int val) {
        for (; x < MAXN; x += x & -x) {
            tree[x] += val;
        }
    }
    
    int query(int x) {
        int res = 0;
        for (; x > 0; x -= x & -x) {
            res += tree[x];
        }
        return res;
    }
    
    int query(int l, int r) {
        return query(r) - query(l - 1);
    }
};

FenwickTree ft;

// 初始化树的邻接表
void addEdge(int u, int v) {
    next_[++cnt] = head[u];
    head[u] = cnt;
    to[cnt] = v;
}

// DFS计算入时间戳和出时间戳
void dfs(int u, int fa) {
    in[u] = ++timeStamp;
    dep[u] = dep[fa] + 1;
    f[u][0] = fa;
    for (int i = 1; i < 20; i++) {
        f[u][i] = f[f[u][i-1]][i-1];
    }
    for (int i = head[u]; i; i = next_[i]) {
        int v = to[i];
        if (v != fa) {
            dfs(v, u);
        }
    }
    out[u] = timeStamp;
}

// 求LCA
int lca(int u, int v) {
    if (dep[u] < dep[v]) {
        swap(u, v);
    }
    
    for (int i = 19; i >= 0; i--) {
        if (dep[f[u][i]] >= dep[v]) {
            u = f[u][i];
        }
    }
    
    if (u == v) return u;
    
    for (int i = 19; i >= 0; i--) {
        if (f[u][i] != f[v][i]) {
            u = f[u][i];
            v = f[v][i];
        }
    }
    
    return f[u][0];
}

// 处理边，将其转换为扫描线事件
void processEdge(int u, int v, int w, int id, vector<Event>& tmpEvents) {
    if (dep[u] < dep[v]) {
        swap(u, v);
    }
    
    // 将边转换为矩形区域
    tmpEvents.emplace_back(1, in[u], out[u], 1, id);
    tmpEvents.emplace_back(in[v], in[u], out[u], -1, id);
    tmpEvents.emplace_back(out[v] + 1, in[u], out[u], 1, id);
}

// 整体二分核心函数
void solve(int ql, int qr, int l, int r) {
    if (ql > qr || l > r) return;
    
    if (l == r) {
        // 所有查询的答案都是l
        for (int i = ql; i <= qr; i++) {
            ans[queries[i].id] = l;
        }
        return;
    }
    
    int mid = (l + r) >> 1;
    
    // 收集扫描线事件
    vector<Event> tmpEvents;
    for (int i = 1; i <= m; i++) {
        if (w[i] <= mid) {
            processEdge(u[i], v[i], w[i], i, tmpEvents);
        }
    }
    
    // 将查询也加入事件列表
    for (int i = ql; i <= qr; i++) {
        tmpEvents.emplace_back(queries[i].x1, queries[i].y1, queries[i].y2, -2, i);
        tmpEvents.emplace_back(queries[i].x2 + 1, queries[i].y1, queries[i].y2, -3, i);
    }
    
    // 按x坐标排序事件
    sort(tmpEvents.begin(), tmpEvents.end(), [](const Event& a, const Event& b) {
        return a.x < b.x;
    });
    
    // 初始化答案计数
    vector<int> cnt(qr - ql + 1, 0);
    
    // 处理扫描线
    int eventPtr = 0;
    for (int x = 1; x <= timeStamp; x++) {
        // 处理所有x坐标等于当前x的事件
        while (eventPtr < tmpEvents.size() && tmpEvents[eventPtr].x == x) {
            Event e = tmpEvents[eventPtr++];
            if (e.type == 1 || e.type == -1) {
                // 矩形覆盖事件
                ft.update(e.y1, e.type);
                ft.update(e.y2 + 1, -e.type);
            } else if (e.type == -2) {
                // 查询开始事件
                int idx = e.id - ql;
                cnt[idx] -= ft.query(e.y1, e.y2);
            } else if (e.type == -3) {
                // 查询结束事件
                int idx = e.id - ql;
                cnt[idx] += ft.query(e.y1, e.y2);
            }
        }
    }
    
    // 清理树状数组
    for (Event e : tmpEvents) {
        if (e.type == 1 || e.type == -1) {
            ft.update(e.y1, -e.type);
            ft.update(e.y2 + 1, e.type);
        }
    }
    
    // 分类查询
    int left = ql, right = qr;
    vector<int> leftQueries(qr - ql + 1);
    vector<int> rightQueries(qr - ql + 1);
    
    for (int i = ql; i <= qr; i++) {
        int idx = i - ql;
        if (cnt[idx] >= queries[i].k) {
            // 答案在左半部分
            leftQueries[left - ql] = i;
            left++;
        } else {
            // 答案在右半部分，调整k值
            queries[i].k -= cnt[idx];
            rightQueries[right - qr] = i;
            right--;
        }
    }
    
    // 保存当前查询状态
    vector<Query> tmp(qr - ql + 1);
    for (int i = ql; i <= qr; i++) {
        tmp[i - ql] = queries[i];
    }
    
    // 合并查询顺序
    for (int i = ql; i < left; i++) {
        queries[i] = tmp[leftQueries[i - ql] - ql];
    }
    for (int i = qr; i > right; i--) {
        queries[i] = tmp[rightQueries[i - qr] - ql];
    }
    
    // 递归处理左右两部分
    solve(ql, left - 1, l, mid);
    solve(right + 1, qr, mid + 1, r);
}

int main() {
    // 输入优化
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取输入
    int n;
    cin >> n >> m >> q;
    
    // 初始化邻接表
    memset(head, 0, sizeof(head));
    cnt = 0;
    
    // 读取树的边
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 计算DFS序和LCA所需信息
    timeStamp = 0;
    memset(dep, 0, sizeof(dep));
    memset(f, 0, sizeof(f));
    dfs(1, 0);
    
    // 读取水果（边）的信息
    vector<int> weights(m + 1);
    for (int i = 1; i <= m; i++) {
        cin >> u[i] >> v[i] >> w[i];
        weights[i] = w[i];
    }
    
    // 离散化边权
    sort(weights.begin() + 1, weights.end());
    int uniqueWeights = 1;
    for (int i = 2; i <= m; i++) {
        if (weights[i] != weights[uniqueWeights]) {
            weights[++uniqueWeights] = weights[i];
        }
    }
    
    for (int i = 1; i <= m; i++) {
        w[i] = lower_bound(weights.begin() + 1, weights.begin() + uniqueWeights + 1, w[i]) - weights.begin();
    }
    
    // 读取查询
    for (int i = 1; i <= q; i++) {
        int a, b, k;
        cin >> a >> b >> k;
        int l = lca(a, b);
        if (l == a) {
            // 路径a-b是链状的，且a是LCA
            if (dep[a] > dep[b]) swap(a, b);
            queries[i] = Query(in[b], out[b], in[l] + 1, in[a], k, i);
        } else if (l == b) {
            // 路径a-b是链状的，且b是LCA
            if (dep[a] < dep[b]) swap(a, b);
            queries[i] = Query(in[a], out[a], in[l] + 1, in[b], k, i);
        } else {
            // 路径a-b经过LCA，分成两段
            if (in[a] > in[b]) swap(a, b);
            queries[i] = Query(in[a], out[a], in[b], out[b], k, i);
        }
    }
    
    // 整体二分求解
    solve(1, q, 1, uniqueWeights);
    
    // 输出结果
    for (int i = 1; i <= q; i++) {
        cout << weights[ans[i]] << '\n';
    }
    
    return 0;
}
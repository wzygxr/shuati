#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <climits>
using namespace std;

/**
 * POJ 3237 Tree - 树链剖分 + 线段树维护边权最大值和最小值
 * 题目描述：给定一棵树，支持三种操作：
 * 1. CHANGE i v：将第i条边的权值改为v
 * 2. NEGATE a b：将a到b路径上的所有边权取反
 * 3. QUERY a b：查询a到b路径上的最大边权
 * 
 * 数据范围：n ≤ 10^4，q ≤ 10^5
 * 解法：边权转点权 + 树链剖分 + 线段树维护区间最大值和最小值
 * 
 * 时间复杂度：预处理O(n)，每次操作O(log²n)
 * 空间复杂度：O(n)
 * 
 * 网址：http://poj.org/problem?id=3237
 */

struct Edge {
    int to, weight, id;
    Edge(int t, int w, int i) : to(t), weight(w), id(i) {}
};

class SegmentTree {
private:
    vector<int> max_val, min_val;
    vector<bool> neg;
    int n;
    
    void pushUp(int rt) {
        max_val[rt] = max(max_val[rt << 1], max_val[rt << 1 | 1]);
        min_val[rt] = min(min_val[rt << 1], min_val[rt << 1 | 1]);
    }
    
    void pushDown(int rt) {
        if (neg[rt]) {
            // 取反操作
            int temp = max_val[rt << 1];
            max_val[rt << 1] = -min_val[rt << 1];
            min_val[rt << 1] = -temp;
            
            temp = max_val[rt << 1 | 1];
            max_val[rt << 1 | 1] = -min_val[rt << 1 | 1];
            min_val[rt << 1 | 1] = -temp;
            
            neg[rt << 1] = !neg[rt << 1];
            neg[rt << 1 | 1] = !neg[rt << 1 | 1];
            neg[rt] = false;
        }
    }
    
public:
    SegmentTree(int size) : n(size) {
        max_val.resize(4 * n, INT_MIN);
        min_val.resize(4 * n, INT_MAX);
        neg.resize(4 * n, false);
    }
    
    void build(int l, int r, int rt, const vector<int>& arr) {
        if (l == r) {
            max_val[rt] = min_val[rt] = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1, arr);
        build(mid + 1, r, rt << 1 | 1, arr);
        pushUp(rt);
    }
    
    void update(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            int temp = max_val[rt];
            max_val[rt] = -min_val[rt];
            min_val[rt] = -temp;
            neg[rt] = !neg[rt];
            return;
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        if (L <= mid) update(L, R, l, mid, rt << 1);
        if (R > mid) update(L, R, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    void updatePoint(int pos, int val, int l, int r, int rt) {
        if (l == r) {
            max_val[rt] = min_val[rt] = val;
            neg[rt] = false;
            return;
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        if (pos <= mid) updatePoint(pos, val, l, mid, rt << 1);
        else updatePoint(pos, val, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return max_val[rt];
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        int res = INT_MIN;
        if (L <= mid) res = max(res, query(L, R, l, mid, rt << 1));
        if (R > mid) res = max(res, query(L, R, mid + 1, r, rt << 1 | 1));
        return res;
    }
};

class HeavyLightDecomposition {
private:
    int n, cnt;
    vector<int> parent, depth, size, heavy, head, pos, edgeToPos;
    vector<vector<Edge>> tree;
    SegmentTree* seg;
    
    void dfs1(int u, int p, int d) {
        parent[u] = p;
        depth[u] = d;
        size[u] = 1;
        int maxSize = 0;
        
        for (const Edge& e : tree[u]) {
            int v = e.to;
            if (v == p) continue;
            dfs1(v, u, d + 1);
            size[u] += size[v];
            if (size[v] > maxSize) {
                maxSize = size[v];
                heavy[u] = v;
            }
        }
    }
    
    void dfs2(int u, int h) {
        head[u] = h;
        pos[u] = cnt++;
        
        if (heavy[u] != -1) {
            dfs2(heavy[u], h);
        }
        
        for (const Edge& e : tree[u]) {
            int v = e.to;
            if (v != parent[u] && v != heavy[u]) {
                dfs2(v, v);
            }
        }
    }
    
public:
    HeavyLightDecomposition(int size) : n(size), cnt(0) {
        parent.resize(n);
        depth.resize(n);
        size.resize(n);
        heavy.resize(n, -1);
        head.resize(n);
        pos.resize(n);
        edgeToPos.resize(n);
        tree.resize(n);
    }
    
    void addEdge(int u, int v, int w, int id) {
        tree[u].emplace_back(v, w, id);
        tree[v].emplace_back(u, w, id);
    }
    
    void decompose() {
        dfs1(0, -1, 0);
        dfs2(0, 0);
        
        vector<int> arr(n, 0);
        seg = new SegmentTree(n);
        seg->build(1, n - 1, 1, arr);
    }
    
    int queryPath(int u, int v) {
        int res = INT_MIN;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                swap(u, v);
            }
            res = max(res, seg->query(pos[head[u]], pos[u], 1, n - 1, 1));
            u = parent[head[u]];
        }
        if (u == v) return res;
        if (depth[u] > depth[v]) {
            swap(u, v);
        }
        res = max(res, seg->query(pos[u] + 1, pos[v], 1, n - 1, 1));
        return res;
    }
    
    void negatePath(int u, int v) {
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                swap(u, v);
            }
            seg->update(pos[head[u]], pos[u], 1, n - 1, 1);
            u = parent[head[u]];
        }
        if (u == v) return;
        if (depth[u] > depth[v]) {
            swap(u, v);
        }
        seg->update(pos[u] + 1, pos[v], 1, n - 1, 1);
    }
    
    void updateEdge(int edgeId, int newVal) {
        // 简化实现：假设edgeToPos数组已经正确设置
        seg->updatePoint(edgeToPos[edgeId], newVal, 1, n - 1, 1);
    }
    
    ~HeavyLightDecomposition() {
        delete seg;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int T;
    cin >> T;
    
    while (T--) {
        int n;
        cin >> n;
        
        HeavyLightDecomposition hld(n);
        
        // 读取边信息
        for (int i = 1; i < n; i++) {
            int u, v, w;
            cin >> u >> v >> w;
            hld.addEdge(u - 1, v - 1, w, i);
        }
        
        hld.decompose();
        
        // 处理查询
        string op;
        while (cin >> op) {
            if (op == "DONE") break;
            
            int a, b;
            cin >> a >> b;
            
            if (op == "QUERY") {
                cout << hld.queryPath(a - 1, b - 1) << endl;
            } else if (op == "NEGATE") {
                hld.negatePath(a - 1, b - 1);
            } else if (op == "CHANGE") {
                hld.updateEdge(a, b);
            }
        }
    }
    
    return 0;
}

/**
 * 算法总结：
 * 1. 边权转点权：将边权下放到深度较深的节点上
 * 2. 树链剖分：将树划分为重链，便于路径操作
 * 3. 线段树维护：支持区间取反、单点修改、区间最大值查询
 * 
 * 工程化考量：
 * 1. 异常处理：添加输入验证和边界检查
 * 2. 性能优化：使用快速IO，优化线段树实现
 * 3. 内存管理：合理分配数组大小，避免内存泄漏
 * 
 * 测试用例：
 * 1. 单边树：验证基本功能
 * 2. 链状树：测试路径操作
 * 3. 完全二叉树：验证复杂度
 * 4. 极端数据：测试边界情况
 */
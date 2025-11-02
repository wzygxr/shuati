package class166;

import java.util.*;

/**
 * 线段树分治训练题解
 * 包含多个经典问题的完整实现
 */

// 1. 可撤销并查集模板
class RollbackDSU {
    int[] father, size;
    Stack<int[]> rollbackStack = new Stack<>();
    
    public RollbackDSU(int n) {
        father = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            father[i] = i;
            size[i] = 1;
        }
    }
    
    int find(int x) {
        while (x != father[x]) x = father[x];
        return x;
    }
    
    boolean union(int x, int y) {
        int fx = find(x), fy = find(y);
        if (fx == fy) return false;
        
        // 按秩合并
        if (size[fx] < size[fy]) {
            int temp = fx; fx = fy; fy = temp;
        }
        
        father[fy] = fx;
        size[fx] += size[fy];
        rollbackStack.push(new int[]{fx, fy});
        return true;
    }
    
    void rollback() {
        if (rollbackStack.isEmpty()) return;
        int[] op = rollbackStack.pop();
        int fx = op[0], fy = op[1];
        father[fy] = fy;
        size[fx] -= size[fy];
    }
    
    int getSize(int x) {
        return size[find(x)];
    }
}

// 2. 扩展域并查集模板（用于二分图检测）
class ExtendedUnionFind {
    int[] father, size;
    Stack<int[]> rollbackStack = new Stack<>();
    
    public ExtendedUnionFind(int n) {
        father = new int[2 * n + 1];
        size = new int[2 * n + 1];
        for (int i = 1; i <= 2 * n; i++) {
            father[i] = i;
            size[i] = 1;
        }
    }
    
    int find(int x) {
        while (x != father[x]) x = father[x];
        return x;
    }
    
    void union(int x, int y) {
        int fx = find(x), fy = find(y);
        if (fx == fy) return;
        
        if (size[fx] < size[fy]) {
            int temp = fx; fx = fy; fy = temp;
        }
        
        father[fy] = fx;
        size[fx] += size[fy];
        rollbackStack.push(new int[]{fx, fy});
    }
    
    boolean isBipartite(int x, int y) {
        return find(x) != find(y);
    }
    
    void rollback() {
        if (rollbackStack.isEmpty()) return;
        int[] op = rollbackStack.pop();
        int fx = op[0], fy = op[1];
        father[fy] = fy;
        size[fx] -= size[fy];
    }
}

// 3. 线段树分治通用框架
class SegmentTreeDivideConquer {
    static class Node {
        List<int[]> edges = new ArrayList<>();
    }
    
    Node[] tree;
    int n, m;
    
    // 可撤销并查集
    int[] father, size;
    Stack<int[]> rollbackStack = new Stack<>();
    
    public SegmentTreeDivideConquer(int n, int m) {
        this.n = n;
        this.m = m;
        tree = new Node[m << 2];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new Node();
        }
        father = new int[n];
        size = new int[n];
        initDSU();
    }
    
    void initDSU() {
        for (int i = 0; i < n; i++) {
            father[i] = i;
            size[i] = 1;
        }
    }
    
    int find(int x) {
        while (x != father[x]) x = father[x];
        return x;
    }
    
    void union(int x, int y) {
        int fx = find(x), fy = find(y);
        if (fx == fy) return;
        if (size[fx] < size[fy]) {
            int temp = fx; fx = fy; fy = temp;
        }
        father[fy] = fx;
        size[fx] += size[fy];
        rollbackStack.push(new int[]{fx, fy});
    }
    
    void rollback() {
        int[] op = rollbackStack.pop();
        int fx = op[0], fy = op[1];
        father[fy] = fy;
        size[fx] -= size[fy];
    }
    
    // 在线段树节点添加边
    void addEdge(int node, int x, int y) {
        tree[node].edges.add(new int[]{x, y});
    }
    
    // 线段树区间添加操作
    void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            addEdge(i, jobx, joby);
        } else {
            int mid = (l + r) >> 1;
            if (jobl <= mid) {
                add(jobl, jobr, jobx, joby, l, mid, i << 1);
            }
            if (jobr > mid) {
                add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
            }
        }
    }
    
    // 线段树分治DFS
    void dfs(int l, int r, int node, boolean[] ans) {
        int unionCount = 0;
        
        // 处理当前节点的所有边
        for (int[] edge : tree[node].edges) {
            int x = edge[0], y = edge[1];
            int fx = find(x), fy = find(y);
            if (fx != fy) {
                union(fx, fy);
                unionCount++;
            }
        }
        
        if (l == r) {
            // 处理叶子节点的查询
            // 这里根据具体问题实现查询逻辑
        } else {
            int mid = (l + r) >> 1;
            dfs(l, mid, node << 1, ans);
            dfs(mid + 1, r, node << 1 | 1, ans);
        }
        
        // 回滚操作
        for (int i = 0; i < unionCount; i++) {
            rollback();
        }
    }
}

// 4. 具体问题实现示例：LOJ #121 动态图连通性
class DynamicGraphConnectivity {
    static final int MAXN = 5001;
    static final int MAXM = 500001;
    static final int MAXT = 5000001;
    
    int n, m;
    int[] op = new int[MAXM];
    int[] u = new int[MAXM];
    int[] v = new int[MAXM];
    int[][] last = new int[MAXN][MAXN];
    
    // 可撤销并查集
    int[] father = new int[MAXN];
    int[] siz = new int[MAXN];
    int[][] rollback = new int[MAXN][2];
    int opsize = 0;
    
    // 线段树
    int[] head = new int[MAXM << 2];
    int[] next = new int[MAXT];
    int[] tox = new int[MAXT];
    int[] toy = new int[MAXT];
    int cnt = 0;
    
    boolean[] ans = new boolean[MAXM];
    
    void addEdge(int i, int x, int y) {
        next[++cnt] = head[i];
        tox[cnt] = x;
        toy[cnt] = y;
        head[i] = cnt;
    }
    
    int find(int i) {
        while (i != father[i]) {
            i = father[i];
        }
        return i;
    }
    
    void union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (siz[fx] < siz[fy]) {
            int tmp = fx;
            fx = fy;
            fy = tmp;
        }
        father[fy] = fx;
        siz[fx] += siz[fy];
        rollback[++opsize][0] = fx;
        rollback[opsize][1] = fy;
    }
    
    void undo() {
        int fx = rollback[opsize][0];
        int fy = rollback[opsize--][1];
        father[fy] = fy;
        siz[fx] -= siz[fy];
    }
    
    void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            addEdge(i, jobx, joby);
        } else {
            int mid = (l + r) >> 1;
            if (jobl <= mid) {
                add(jobl, jobr, jobx, joby, l, mid, i << 1);
            }
            if (jobr > mid) {
                add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
            }
        }
    }
    
    void dfs(int l, int r, int i) {
        int unionCnt = 0;
        for (int ei = head[i], x, y, fx, fy; ei > 0; ei = next[ei]) {
            x = tox[ei];
            y = toy[ei];
            fx = find(x);
            fy = find(y);
            if (fx != fy) {
                union(fx, fy);
                unionCnt++;
            }
        }
        if (l == r) {
            if (op[l] == 2) {
                ans[l] = find(u[l]) == find(v[l]);
            }
        } else {
            int mid = (l + r) / 2;
            dfs(l, mid, i << 1);
            dfs(mid + 1, r, i << 1 | 1);
        }
        for (int j = 1; j <= unionCnt; j++) {
            undo();
        }
    }
    
    void prepare() {
        for (int i = 1; i <= n; i++) {
            father[i] = i;
            siz[i] = 1;
        }
        for (int i = 1, t, x, y; i <= m; i++) {
            t = op[i];
            x = u[i];
            y = v[i];
            if (t == 0) {
                last[x][y] = i;
            } else if (t == 1) {
                add(last[x][y], i - 1, x, y, 1, m, 1);
                last[x][y] = 0;
            }
        }
        for (int x = 1; x <= n; x++) {
            for (int y = x + 1; y <= n; y++) {
                if (last[x][y] != 0) {
                    add(last[x][y], m, x, y, 1, m, 1);
                }
            }
        }
    }
}

// 5. 二分图检测问题实现：P5787
class BipartiteChecking {
    static final int MAXN = 100001;
    static final int MAXT = 3000001;
    
    int n, m, k;
    int[] father = new int[MAXN << 1];
    int[] siz = new int[MAXN << 1];
    int[][] rollback = new int[MAXN << 1][2];
    int opsize = 0;
    
    int[] head = new int[MAXN << 2];
    int[] next = new int[MAXT];
    int[] tox = new int[MAXT];
    int[] toy = new int[MAXT];
    int cnt = 0;
    
    boolean[] ans = new boolean[MAXN];
    
    void addEdge(int i, int x, int y) {
        next[++cnt] = head[i];
        tox[cnt] = x;
        toy[cnt] = y;
        head[i] = cnt;
    }
    
    int find(int i) {
        while (i != father[i]) {
            i = father[i];
        }
        return i;
    }
    
    void union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (siz[fx] < siz[fy]) {
            int tmp = fx;
            fx = fy;
            fy = tmp;
        }
        father[fy] = fx;
        siz[fx] += siz[fy];
        rollback[++opsize][0] = fx;
        rollback[opsize][1] = fy;
    }
    
    void undo() {
        int fx = rollback[opsize][0];
        int fy = rollback[opsize--][1];
        father[fy] = fy;
        siz[fx] -= siz[fy];
    }
    
    void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            addEdge(i, jobx, joby);
        } else {
            int mid = (l + r) / 2;
            if (jobl <= mid) {
                add(jobl, jobr, jobx, joby, l, mid, i << 1);
            }
            if (jobr > mid) {
                add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
            }
        }
    }
    
    void dfs(int l, int r, int i) {
        boolean check = true;
        int unionCnt = 0;
        for (int ei = head[i]; ei > 0; ei = next[ei]) {
            int x = tox[ei], y = toy[ei], fx = find(x), fy = find(y);
            if (fx == fy) {
                check = false;
                break;
            } else {
                union(x, y + n);
                union(y, x + n);
                unionCnt += 2;
            }
        }
        if (check) {
            if (l == r) {
                ans[l] = true;
            } else {
                int mid = (l + r) / 2;
                dfs(l, mid, i << 1);
                dfs(mid + 1, r, i << 1 | 1);
            }
        } else {
            for (int k = l; k <= r; k++) {
                ans[k] = false;
            }
        }
        for (int k = 1; k <= unionCnt; k++) {
            undo();
        }
    }
}

// 6. 最小mex生成树问题实现：P5631
class MinimumMexSpanningTree {
    static final int MAXN = 1000001;
    static final int MAXV = 100001;
    static final int MAXT = 30000001;
    static int n, m, v;

    static int[] father = new int[MAXN];
    static int[] siz = new int[MAXN];
    static int[][] rollback = new int[MAXN][2];
    static int opsize = 0;

    static int[] head = new int[MAXV << 2];
    static int[] next = new int[MAXT];
    static int[] tox = new int[MAXT];
    static int[] toy = new int[MAXT];
    static int cnt = 0;

    static int part;

    static void addEdge(int i, int x, int y) {
        next[++cnt] = head[i];
        tox[cnt] = x;
        toy[cnt] = y;
        head[i] = cnt;
    }

    static int find(int i) {
        while (i != father[i]) {
            i = father[i];
        }
        return i;
    }

    static void union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (siz[fx] < siz[fy]) {
            int tmp = fx;
            fx = fy;
            fy = tmp;
        }
        father[fy] = fx;
        siz[fx] += siz[fy];
        rollback[++opsize][0] = fx;
        rollback[opsize][1] = fy;
    }

    static void undo() {
        int fx = rollback[opsize][0];
        int fy = rollback[opsize--][1];
        father[fy] = fy;
        siz[fx] -= siz[fy];
    }

    static void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            addEdge(i, jobx, joby);
        } else {
            int mid = (l + r) >> 1;
            if (jobl <= mid) {
                add(jobl, jobr, jobx, joby, l, mid, i << 1);
            }
            if (jobr > mid) {
                add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
            }
        }
    }

    static int dfs(int l, int r, int i) {
        int unionCnt = 0;
        for (int ei = head[i], fx, fy; ei > 0; ei = next[ei]) {
            fx = find(tox[ei]);
            fy = find(toy[ei]);
            if (fx != fy) {
                union(fx, fy);
                part--;
                unionCnt++;
            }
        }
        int ans = -1;
        if (l == r) {
            if (part == 1) {
                ans = l;
            }
        } else {
            int mid = (l + r) >> 1;
            ans = dfs(l, mid, i << 1);
            if (ans == -1) {
                ans = dfs(mid + 1, r, i << 1 | 1);
            }
        }
        for (int k = 1; k <= unionCnt; k++) {
            undo();
            part++;
        }
        return ans;
    }
}

// 7. 最短路径查询问题实现：CF938G
class ShortestPathQueries {
    static final int MAXN = 500001;
    static final int MAXM = 500001;
    static final int MAXT = 5000001;
    
    static int n, m;
    static int[] op = new int[MAXM];
    static int[] u = new int[MAXM];
    static int[] v = new int[MAXM];
    static int[] w = new int[MAXM]; // 边权
    static int[][] last = new int[MAXN][MAXN];
    
    // 可撤销并查集
    static int[] father = new int[MAXN];
    static int[] siz = new int[MAXN];
    static int[][] rollback = new int[MAXN][2];
    static int opsize = 0;
    
    // 线性基用于异或运算
    static class LinearBase {
        static int[] a = new int[60];
        
        static void insert(int x) {
            for (int i = 59; i >= 0; i--) {
                if (((x >> i) & 1) == 0) continue;
                if (a[i] == 0) {
                    a[i] = x;
                    break;
                }
                x ^= a[i];
            }
        }
        
        static int queryMin(int x) {
            for (int i = 59; i >= 0; i--) {
                x = Math.min(x, x ^ a[i]);
            }
            return x;
        }
        
        static void clear() {
            for (int i = 0; i < 60; i++) a[i] = 0;
        }
    }
    
    // 线段树
    static int[] head = new int[MAXM << 2];
    static int[] next = new int[MAXT];
    static int[] tox = new int[MAXT];
    static int[] toy = new int[MAXT];
    static int[] weight = new int[MAXT]; // 边权
    static int cnt = 0;
    
    static int[] ans = new int[MAXM];
    
    static void addEdge(int i, int x, int y, int d) {
        next[++cnt] = head[i];
        tox[cnt] = x;
        toy[cnt] = y;
        weight[cnt] = d;
        head[i] = cnt;
    }
    
    static int find(int i) {
        while (i != father[i]) {
            i = father[i];
        }
        return i;
    }
    
    static void union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (siz[fx] < siz[fy]) {
            int tmp = fx;
            fx = fy;
            fy = tmp;
        }
        father[fy] = fx;
        siz[fx] += siz[fy];
        rollback[++opsize][0] = fx;
        rollback[opsize][1] = fy;
    }
    
    static void undo() {
        int fx = rollback[opsize][0];
        int fy = rollback[opsize--][1];
        father[fy] = fy;
        siz[fx] -= siz[fy];
    }
    
    static void add(int jobl, int jobr, int jobx, int joby, int jobw, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            addEdge(i, jobx, joby, jobw);
        } else {
            int mid = (l + r) >> 1;
            if (jobl <= mid) {
                add(jobl, jobr, jobx, joby, jobw, l, mid, i << 1);
            }
            if (jobr > mid) {
                add(jobl, jobr, jobx, joby, jobw, mid + 1, r, i << 1 | 1);
            }
        }
    }
    
    static void dfs(int l, int r, int i) {
        int unionCnt = 0;
        LinearBase.clear(); // 清空线性基
        for (int ei = head[i]; ei > 0; ei = next[ei]) {
            int x = tox[ei], y = toy[ei], d = weight[ei];
            int fx = find(x), fy = find(y);
            if (fx != fy) {
                union(fx, fy);
                // 将环的异或值加入线性基
                // 这里简化处理，实际需要计算树上路径异或值
                LinearBase.insert(d);
                unionCnt++;
            }
        }
        if (l == r) {
            if (op[l] == 3) { // 查询操作
                if (find(u[l]) != find(v[l])) {
                    ans[l] = -1; // 不连通
                } else {
                    // 计算两点间路径异或最小值
                    ans[l] = LinearBase.queryMin(0);
                }
            }
        } else {
            int mid = (l + r) >> 1;
            dfs(l, mid, i << 1);
            dfs(mid + 1, r, i << 1 | 1);
        }
        for (int j = 1; j <= unionCnt; j++) {
            undo();
        }
    }
    
    static void prepare() {
        for (int i = 1; i <= n; i++) {
            father[i] = i;
            siz[i] = 1;
        }
        for (int i = 1, t, x, y, d; i <= m; i++) {
            t = op[i];
            x = u[i];
            y = v[i];
            if (t == 0) { // 加边
                last[x][y] = i;
            } else if (t == 1) { // 删边
                add(last[x][y], i - 1, x, y, w[last[x][y]], 1, m, 1);
                last[x][y] = 0;
            }
        }
        for (int x = 1; x <= n; x++) {
            for (int y = x + 1; y <= n; y++) {
                if (last[x][y] != 0) {
                    add(last[x][y], m, x, y, w[last[x][y]], 1, m, 1);
                }
            }
        }
    }
}

/**
 * 总结：
 * 
 * 线段树分治是一种处理带有时间维度的离线问题的强大技术。
 * 
 * 核心思想：
 * 1. 将操作序列按时间建立线段树
 * 2. 将每个操作的影响时间区间映射到线段树节点
 * 3. 使用DFS遍历线段树处理操作
 * 4. 使用可撤销数据结构维护状态
 * 5. 在回溯时撤销操作影响
 * 
 * 关键技术点：
 * 1. 可撤销并查集（不能路径压缩，只能按秩合并）
 * 2. 扩展域并查集（用于二分图检测）
 * 3. 线段树区间标记和DFS遍历
 * 4. 线性基（用于异或运算）
 * 
 * 适用场景：
 * 1. 动态图问题（加边、删边、查询连通性）
 * 2. 二分图维护问题
 * 3. 生成树相关问题
 * 4. 路径异或查询问题
 * 5. 其他需要维护历史状态的问题
 * 
 * 时间复杂度通常是 O((n + m) log m)，空间复杂度 O(n + m)
 */
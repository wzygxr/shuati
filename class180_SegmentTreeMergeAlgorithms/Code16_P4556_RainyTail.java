// 测试链接 : https://www.luogu.com.cn/problem/P4556
// P4556 [Vani有约会]雨天的尾巴 - Java实现

import java.io.*;
import java.util.*;

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

public class Code16_P4556_RainyTail {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        }
        
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        int nextInt() { return Integer.parseInt(next()); }
        
        void println(Object obj) { out.println(obj); }
        void close() { out.close(); }
    }
    
    static final int MAXN = 100010;
    static final int MAXM = 10000010;
    static final int MAXZ = 100010;
    
    // 线段树节点
    static class Node {
        int l, r;
        int maxCnt;  // 最大出现次数
        int maxVal;  // 对应的物品编号
        
        Node() {
            l = r = -1;
            maxCnt = 0;
            maxVal = 0;
        }
    }
    
    static Node[] tree = new Node[MAXM];
    static int cnt = 0;
    static int[] roots = new int[MAXN];
    
    // 图结构
    static List<Integer>[] graph = new ArrayList[MAXN];
    static int[] depth = new int[MAXN];
    static int[][] parent = new int[MAXN][20];
    static int[] ans = new int[MAXN];
    
    // 操作记录
    static List<int[]>[] add = new ArrayList[MAXN];
    static List<int[]>[] del = new ArrayList[MAXN];
    
    static {
        for (int i = 0; i < MAXM; i++) {
            tree[i] = new Node();
        }
        for (int i = 0; i < MAXN; i++) {
            graph[i] = new ArrayList<>();
            add[i] = new ArrayList<>();
            del[i] = new ArrayList<>();
        }
    }
    
    static int newNode() {
        if (cnt >= MAXM) {
            System.gc();
            return -1;
        }
        tree[cnt].l = tree[cnt].r = -1;
        tree[cnt].maxCnt = 0;
        tree[cnt].maxVal = 0;
        return cnt++;
    }
    
    static void pushUp(int rt) {
        if (rt == -1) return;
        
        int left = tree[rt].l;
        int right = tree[rt].r;
        
        if (left == -1 && right == -1) {
            tree[rt].maxCnt = 0;
            tree[rt].maxVal = 0;
        } else if (left == -1) {
            tree[rt].maxCnt = tree[right].maxCnt;
            tree[rt].maxVal = tree[right].maxVal;
        } else if (right == -1) {
            tree[rt].maxCnt = tree[left].maxCnt;
            tree[rt].maxVal = tree[left].maxVal;
        } else {
            if (tree[left].maxCnt > tree[right].maxCnt || 
                (tree[left].maxCnt == tree[right].maxCnt && tree[left].maxVal < tree[right].maxVal)) {
                tree[rt].maxCnt = tree[left].maxCnt;
                tree[rt].maxVal = tree[left].maxVal;
            } else {
                tree[rt].maxCnt = tree[right].maxCnt;
                tree[rt].maxVal = tree[right].maxVal;
            }
        }
    }
    
    static void update(int rt, int l, int r, int pos, int val) {
        if (l == r) {
            tree[rt].maxCnt += val;
            tree[rt].maxVal = pos;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (tree[rt].l == -1) tree[rt].l = newNode();
            update(tree[rt].l, l, mid, pos, val);
        } else {
            if (tree[rt].r == -1) tree[rt].r = newNode();
            update(tree[rt].r, mid + 1, r, pos, val);
        }
        pushUp(rt);
    }
    
    static int merge(int p, int q, int l, int r) {
        if (p == -1) return q;
        if (q == -1) return p;
        
        if (l == r) {
            tree[p].maxCnt += tree[q].maxCnt;
            return p;
        }
        
        int mid = (l + r) >> 1;
        tree[p].l = merge(tree[p].l, tree[q].l, l, mid);
        tree[p].r = merge(tree[p].r, tree[q].r, mid + 1, r);
        
        pushUp(p);
        return p;
    }
    
    // LCA预处理
    static void dfsLCA(int u, int fa) {
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
    
    static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u; u = v; v = temp;
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
    static void dfsMerge(int u, int fa) {
        for (int v : graph[u]) {
            if (v == fa) continue;
            dfsMerge(v, u);
            roots[u] = merge(roots[u], roots[v], 1, MAXZ);
        }
        
        // 处理添加操作
        for (int[] op : add[u]) {
            int z = op[0], cnt = op[1];
            if (roots[u] == -1) roots[u] = newNode();
            update(roots[u], 1, MAXZ, z, cnt);
        }
        
        // 处理删除操作
        for (int[] op : del[u]) {
            int z = op[0], cnt = op[1];
            if (roots[u] == -1) roots[u] = newNode();
            update(roots[u], 1, MAXZ, z, -cnt);
        }
        
        // 记录答案
        if (roots[u] != -1 && tree[roots[u]].maxCnt > 0) {
            ans[u] = tree[roots[u]].maxVal;
        } else {
            ans[u] = 0;
        }
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        int n = io.nextInt();
        int m = io.nextInt();
        
        // 建图
        for (int i = 1; i < n; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // LCA预处理
        dfsLCA(1, 0);
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            int z = io.nextInt();
            
            int p = lca(u, v);
            
            // 树上差分
            add[u].add(new int[]{z, 1});
            add[v].add(new int[]{z, 1});
            add[p].add(new int[]{z, -1});
            if (parent[p][0] != 0) {
                del[parent[p][0]].add(new int[]{z, -1});
            }
        }
        
        // 初始化根节点
        for (int i = 1; i <= n; i++) {
            roots[i] = newNode();
        }
        
        // 线段树合并
        dfsMerge(1, 0);
        
        // 输出答案
        for (int i = 1; i <= n; i++) {
            io.println(ans[i]);
        }
        
        io.close();
    }
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
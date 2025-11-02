// 测试链接 : https://www.luogu.com.cn/problem/P4556
// 线段树合并经典题目 - 雨天的尾巴（树上差分+线段树合并）

import java.io.*;
import java.util.*;

/**
 * P4556 [Vani有约会]雨天的尾巴 - 线段树合并+树上差分
 * 
 * 题目描述：
 * 给定一棵n个节点的树，m次操作，每次在路径(u,v)上发放一个类型为z的物品。
 * 求每个节点存放最多的物品类型（如果有多个，输出编号最小的）。
 * 
 * 核心算法：树上差分 + 线段树合并
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code26_P4556_RainyTail_Advanced {
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
        long nextLong() { return Long.parseLong(next()); }
        
        void println(Object obj) { out.println(obj); }
        void close() { out.close(); }
    }
    
    static final int MAXN = 100010;
    static final int MAXM = 20000000;
    static final int MAXZ = 100000;
    
    static int n, m;
    static List<Integer>[] graph;
    static int[] depth = new int[MAXN];
    static int[][] parent = new int[MAXN][20];
    
    // 线段树节点
    static class Node {
        int l, r;
        int maxVal, maxPos; // 最大值和最大值位置
        
        Node() {
            l = r = 0;
            maxVal = 0;
            maxPos = 0;
        }
    }
    
    static Node[] tree = new Node[MAXM];
    static int cnt = 0;
    static int[] root = new int[MAXN];
    
    static {
        for (int i = 0; i < MAXM; i++) {
            tree[i] = new Node();
        }
    }
    
    // 创建新节点
    static int newNode() {
        if (cnt >= MAXM) {
            System.gc();
            return 0;
        }
        tree[cnt].l = tree[cnt].r = 0;
        tree[cnt].maxVal = 0;
        tree[cnt].maxPos = 0;
        return cnt++;
    }
    
    // 上传信息
    static void pushUp(int rt) {
        if (tree[rt].l == 0 && tree[rt].r == 0) {
            tree[rt].maxVal = 0;
            tree[rt].maxPos = 0;
            return;
        }
        
        if (tree[rt].l == 0) {
            tree[rt].maxVal = tree[tree[rt].r].maxVal;
            tree[rt].maxPos = tree[tree[rt].r].maxPos;
            return;
        }
        
        if (tree[rt].r == 0) {
            tree[rt].maxVal = tree[tree[rt].l].maxVal;
            tree[rt].maxPos = tree[tree[rt].l].maxPos;
            return;
        }
        
        if (tree[tree[rt].l].maxVal > tree[tree[rt].r].maxVal) {
            tree[rt].maxVal = tree[tree[rt].l].maxVal;
            tree[rt].maxPos = tree[tree[rt].l].maxPos;
        } else if (tree[tree[rt].l].maxVal < tree[tree[rt].r].maxVal) {
            tree[rt].maxVal = tree[tree[rt].r].maxVal;
            tree[rt].maxPos = tree[tree[rt].r].maxPos;
        } else {
            tree[rt].maxVal = tree[tree[rt].l].maxVal;
            tree[rt].maxPos = Math.min(tree[tree[rt].l].maxPos, tree[tree[rt].r].maxPos);
        }
    }
    
    // 单点更新
    static void update(int rt, int l, int r, int pos, int val) {
        if (l == r) {
            tree[rt].maxVal += val;
            tree[rt].maxPos = (tree[rt].maxVal > 0) ? l : 0;
            return;
        }
        
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            if (tree[rt].l == 0) tree[rt].l = newNode();
            update(tree[rt].l, l, mid, pos, val);
        } else {
            if (tree[rt].r == 0) tree[rt].r = newNode();
            update(tree[rt].r, mid + 1, r, pos, val);
        }
        
        pushUp(rt);
    }
    
    // 线段树合并
    static int merge(int u, int v, int l, int r) {
        if (u == 0) return v;
        if (v == 0) return u;
        
        if (l == r) {
            tree[u].maxVal += tree[v].maxVal;
            tree[u].maxPos = (tree[u].maxVal > 0) ? l : 0;
            return u;
        }
        
        int mid = (l + r) >> 1;
        
        tree[u].l = merge(tree[u].l, tree[v].l, l, mid);
        tree[u].r = merge(tree[u].r, tree[v].r, mid + 1, r);
        
        pushUp(u);
        return u;
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
            if (v == fa) continue;
            dfsLCA(v, u);
        }
    }
    
    // 求LCA
    static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
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
    
    // 树上差分
    static void addOperation(int u, int v, int z) {
        int w = lca(u, v);
        
        // u到w路径上+z
        update(root[u], 1, MAXZ, z, 1);
        // v到w路径上+z
        update(root[v], 1, MAXZ, z, 1);
        // w处-z
        update(root[w], 1, MAXZ, z, -1);
        // w的父节点处-z
        if (parent[w][0] != 0) {
            update(root[parent[w][0]], 1, MAXZ, z, -1);
        }
    }
    
    // DFS合并线段树
    static void dfsMerge(int u, int fa) {
        for (int v : graph[u]) {
            if (v == fa) continue;
            
            dfsMerge(v, u);
            root[u] = merge(root[u], root[v], 1, MAXZ);
        }
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        m = io.nextInt();
        
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
            root[i] = newNode();
        }
        
        // 读入树结构
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
            
            addOperation(u, v, z);
        }
        
        // 合并线段树
        dfsMerge(1, 0);
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            io.println(tree[root[i]].maxPos);
        }
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    - 树上路径操作：在路径(u,v)上发放物品
 *    - 需要统计每个节点存放最多的物品类型
 *    - 操作次数多：最多1e5次操作
 * 
 * 2. 解决方案：
 *    - 树上差分：将路径操作转化为点操作
 *    - 线段树合并：高效合并子树信息
 *    - 动态开点：节省内存空间
 * 
 * 3. 核心优化：
 *    - 树上差分：将O(n)的路径操作转化为O(1)的点操作
 *    - 线段树合并：将O(n²)的合并复杂度优化到O(n log n)
 *    - 值域压缩：针对大值域优化
 * 
 * 4. 时间复杂度：
 *    - LCA预处理：O(n log n)
 *    - 每次操作：O(log n)
 *    - 线段树合并：O(n log n)
 *    - 总体复杂度：O((n+m) log n)
 * 
 * 5. 类似题目：
 *    - P5494 【模板】线段树分裂
 *    - P6773 [NOI2020]命运
 *    - P5298 [PKUWC2018]Minimax
 * 
 * 6. 扩展应用：
 *    - 其他树上路径统计问题
 *    - 区间赋值问题
 *    - 动态维护问题
 * 
 * 7. 实现技巧：
 *    - 注意LCA的实现
 *    - 合理设计线段树节点信息
 *    - 注意内存管理
 */
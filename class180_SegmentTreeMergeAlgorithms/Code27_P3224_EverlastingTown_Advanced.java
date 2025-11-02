// 测试链接 : https://www.luogu.com.cn/problem/P3224
// 线段树合并+并查集 - HNOI2012 永无乡

import java.io.*;
import java.util.*;

/**
 * P3224 [HNOI2012]永无乡 - 线段树合并+并查集
 * 
 * 题目描述：
 * 有n个岛屿，每个岛屿有一个重要度。支持两种操作：
 * 1. 在岛屿x和y之间建一座桥
 * 2. 询问与岛屿x连通的所有岛屿中重要度第k小的岛屿编号
 * 
 * 核心算法：并查集 + 线段树合并
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code27_P3224_EverlastingTown_Advanced {
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
    
    static int n, m, q;
    static int[] importance = new int[MAXN]; // 重要度
    static int[] id = new int[MAXN]; // 重要度到岛屿编号的映射
    
    // 并查集
    static int[] parent = new int[MAXN];
    static int[] size = new int[MAXN];
    
    // 线段树节点
    static class Node {
        int l, r;
        int sum; // 子树大小
        
        Node() {
            l = r = 0;
            sum = 0;
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
        tree[cnt].sum = 0;
        return cnt++;
    }
    
    // 上传信息
    static void pushUp(int rt) {
        tree[rt].sum = 0;
        if (tree[rt].l != 0) tree[rt].sum += tree[tree[rt].l].sum;
        if (tree[rt].r != 0) tree[rt].sum += tree[tree[rt].r].sum;
    }
    
    // 单点更新
    static void update(int rt, int l, int r, int pos, int val) {
        if (l == r) {
            tree[rt].sum += val;
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
            tree[u].sum += tree[v].sum;
            return u;
        }
        
        int mid = (l + r) >> 1;
        
        tree[u].l = merge(tree[u].l, tree[v].l, l, mid);
        tree[u].r = merge(tree[u].r, tree[v].r, mid + 1, r);
        
        pushUp(u);
        return u;
    }
    
    // 查询第k小
    static int queryKth(int rt, int l, int r, int k) {
        if (l == r) {
            return l;
        }
        
        int mid = (l + r) >> 1;
        int leftSum = (tree[rt].l != 0) ? tree[tree[rt].l].sum : 0;
        
        if (k <= leftSum) {
            return queryKth(tree[rt].l, l, mid, k);
        } else {
            return queryKth(tree[rt].r, mid + 1, r, k - leftSum);
        }
    }
    
    // 并查集初始化
    static void initDSU() {
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }
    
    // 查找根节点
    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    // 合并两个集合
    static void union(int x, int y) {
        int rx = find(x);
        int ry = find(y);
        
        if (rx == ry) return;
        
        // 启发式合并：小树合并到大树
        if (size[rx] < size[ry]) {
            int temp = rx;
            rx = ry;
            ry = temp;
        }
        
        // 合并线段树
        root[rx] = merge(root[rx], root[ry], 1, n);
        
        // 更新并查集
        parent[ry] = rx;
        size[rx] += size[ry];
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        m = io.nextInt();
        
        // 读入重要度
        for (int i = 1; i <= n; i++) {
            importance[i] = io.nextInt();
            id[importance[i]] = i;
        }
        
        // 初始化并查集和线段树
        initDSU();
        for (int i = 1; i <= n; i++) {
            root[i] = newNode();
            update(root[i], 1, n, importance[i], 1);
        }
        
        // 读入初始边
        for (int i = 0; i < m; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            union(u, v);
        }
        
        q = io.nextInt();
        
        // 处理查询
        for (int i = 0; i < q; i++) {
            String op = io.next();
            
            if (op.equals("B")) {
                // 建桥操作
                int u = io.nextInt();
                int v = io.nextInt();
                union(u, v);
            } else {
                // 查询操作
                int u = io.nextInt();
                int k = io.nextInt();
                
                int ru = find(u);
                
                if (tree[root[ru]].sum < k) {
                    io.println(-1);
                } else {
                    int importanceKth = queryKth(root[ru], 1, n, k);
                    io.println(id[importanceKth]);
                }
            }
        }
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    - 动态连通性问题：支持建桥操作
 *    - 查询操作：求连通块中第k小的岛屿
 *    - 需要高效维护连通块信息
 * 
 * 2. 解决方案：
 *    - 并查集：维护连通性
 *    - 线段树合并：维护每个连通块的重要度信息
 *    - 启发式合并：优化合并效率
 * 
 * 3. 核心优化：
 *    - 并查集路径压缩：提高查找效率
 *    - 线段树合并：将O(n²)的合并复杂度优化到O(n log n)
 *    - 启发式合并：减少合并次数
 * 
 * 4. 时间复杂度：
 *    - 每次合并操作：O(log n)
 *    - 每次查询操作：O(log n)
 *    - 总体复杂度：O((m+q) log n)
 * 
 * 5. 类似题目：
 *    - P5494 【模板】线段树分裂
 *    - P4556 雨天的尾巴
 *    - P6773 [NOI2020]命运
 * 
 * 6. 扩展应用：
 *    - 其他动态连通性问题
 *    - 区间统计问题
 *    - 集合维护问题
 * 
 * 7. 实现技巧：
 *    - 注意并查集的路径压缩
 *    - 合理设计线段树节点信息
 *    - 注意内存管理
 */
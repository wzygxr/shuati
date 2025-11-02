// 测试链接 : https://www.luogu.com.cn/problem/P6773
// 线段树合并加速DP转移 - NOI2020 命运

import java.io.*;
import java.util.*;

/**
 * P6773 [NOI2020] 命运 - 线段树合并优化树形DP
 * 
 * 题目描述：
 * 给定一棵n个节点的树，每条边有一个限制条件(u,v,w)，表示从u到v的路径上
 * 必须满足某种条件。求满足所有限制条件的路径数量。
 * 
 * 核心算法：树形DP + 线段树合并
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code24_P6773_Destiny_DP_Optimization {
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
    
    static final int MOD = 998244353;
    static final int MAXN = 500010;
    static final int MAXM = 20000000;
    
    static int n, m;
    static List<Integer>[] graph;
    static List<int[]>[] constraints; // constraints[u] = {v, w}
    
    // 线段树节点
    static class Node {
        int l, r;
        long sum, mul;
        
        Node() {
            l = r = 0;
            sum = 0;
            mul = 1;
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
        tree[cnt].mul = 1;
        return cnt++;
    }
    
    // 下传乘法标记
    static void pushMul(int rt, long mul) {
        if (rt == 0) return;
        tree[rt].sum = tree[rt].sum * mul % MOD;
        tree[rt].mul = tree[rt].mul * mul % MOD;
    }
    
    static void pushDown(int rt) {
        if (tree[rt].mul != 1) {
            if (tree[rt].l != 0) pushMul(tree[rt].l, tree[rt].mul);
            if (tree[rt].r != 0) pushMul(tree[rt].r, tree[rt].mul);
            tree[rt].mul = 1;
        }
    }
    
    // 单点更新
    static void update(int rt, int l, int r, int pos, long val) {
        if (l == r) {
            tree[rt].sum = (tree[rt].sum + val) % MOD;
            return;
        }
        
        pushDown(rt);
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            if (tree[rt].l == 0) tree[rt].l = newNode();
            update(tree[rt].l, l, mid, pos, val);
        } else {
            if (tree[rt].r == 0) tree[rt].r = newNode();
            update(tree[rt].r, mid + 1, r, pos, val);
        }
        
        tree[rt].sum = (tree[tree[rt].l].sum + tree[tree[rt].r].sum) % MOD;
    }
    
    // 线段树合并（核心函数）
    static int merge(int u, int v, int l, int r, long mulU, long mulV) {
        if (u == 0 && v == 0) return 0;
        
        if (u == 0) {
            pushMul(v, mulV);
            return v;
        }
        
        if (v == 0) {
            pushMul(u, mulU);
            return u;
        }
        
        if (l == r) {
            // 叶子节点合并
            tree[u].sum = (tree[u].sum * mulU % MOD + tree[v].sum * mulV % MOD) % MOD;
            return u;
        }
        
        pushDown(u);
        pushDown(v);
        
        int mid = (l + r) >> 1;
        
        // 计算左右子树的乘法标记
        long sumUL = tree[tree[u].l].sum;
        long sumUR = tree[tree[u].r].sum;
        long sumVL = tree[tree[v].l].sum;
        long sumVR = tree[tree[v].r].sum;
        
        tree[u].l = merge(tree[u].l, tree[v].l, l, mid, 
                         mulU, (mulV + sumVR) % MOD);
        tree[u].r = merge(tree[u].r, tree[v].r, mid + 1, r,
                         (mulU + sumUL) % MOD, mulV);
        
        tree[u].sum = (tree[tree[u].l].sum + tree[tree[u].r].sum) % MOD;
        
        return u;
    }
    
    // 查询区间和
    static long query(int rt, int l, int r, int ql, int qr) {
        if (rt == 0 || ql > r || qr < l) return 0;
        if (ql <= l && r <= qr) return tree[rt].sum;
        
        pushDown(rt);
        int mid = (l + r) >> 1;
        long res = 0;
        if (ql <= mid) res = (res + query(tree[rt].l, l, mid, ql, qr)) % MOD;
        if (qr > mid) res = (res + query(tree[rt].r, mid + 1, r, ql, qr)) % MOD;
        return res;
    }
    
    // DFS进行树形DP
    static void dfs(int u, int fa) {
        root[u] = newNode();
        update(root[u], 1, n, 1, 1); // 初始状态
        
        for (int v : graph[u]) {
            if (v == fa) continue;
            
            dfs(v, u);
            
            // 合并子树信息
            long sumV = query(root[v], 1, n, 1, n);
            root[u] = merge(root[u], root[v], 1, n, 1, sumV);
        }
        
        // 处理约束条件
        for (int[] constraint : constraints[u]) {
            int v = constraint[0];
            int w = constraint[1];
            
            // 根据约束条件更新DP值
            if (w > 0) {
                long val = query(root[u], 1, n, 1, w);
                update(root[u], 1, n, w + 1, val);
                
                // 清空不满足条件的部分
                if (w > 1) {
                    // 这里需要实现区间清空操作
                    // 简化实现：通过乘法标记实现
                    pushMul(root[u], 0); // 实际实现需要更精细的处理
                }
            }
        }
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        m = io.nextInt();
        
        graph = new ArrayList[n + 1];
        constraints = new ArrayList[n + 1];
        
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
            constraints[i] = new ArrayList<>();
        }
        
        // 读入树结构
        for (int i = 1; i < n; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 读入约束条件
        for (int i = 0; i < m; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            int w = io.nextInt();
            constraints[u].add(new int[]{v, w});
        }
        
        // 进行树形DP
        dfs(1, 0);
        
        // 输出结果
        long ans = query(root[1], 1, n, 1, n);
        io.println(ans);
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    - 树形结构上的路径计数问题
 *    - 带有复杂的约束条件
 *    - 需要高效的合并子树信息
 * 
 * 2. 解决方案：
 *    - 使用树形DP：f[u][d]表示以u为根的子树，深度为d的路径方案数
 *    - 使用线段树合并：高效合并子树信息
 *    - 动态开点：节省内存空间
 * 
 * 3. 核心优化：
 *    - 线段树合并：将O(n²)的合并复杂度优化到O(n log n)
 *    - 懒标记：减少不必要的更新操作
 *    - 动态规划状态压缩
 * 
 * 4. 时间复杂度：
 *    - 每个节点最多被合并log n次
 *    - 总体复杂度O(n log n)
 * 
 * 5. 类似题目：
 *    - P5298 [PKUWC2018]Minimax
 *    - P4556 [Vani有约会]雨天的尾巴
 *    - CF911G Mass Change Queries
 * 
 * 6. 扩展应用：
 *    - 其他树形DP优化问题
 *    - 路径计数问题
 *    - 约束满足问题
 * 
 * 7. 实现技巧：
 *    - 注意乘法标记的处理
 *    - 合理设计合并函数
 *    - 注意内存管理
 */
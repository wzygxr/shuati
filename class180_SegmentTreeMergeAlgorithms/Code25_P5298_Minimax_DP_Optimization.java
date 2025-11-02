// 测试链接 : https://www.luogu.com.cn/problem/P5298
// 线段树合并加速概率DP - PKUWC2018 Minimax

import java.io.*;
import java.util.*;

/**
 * P5298 [PKUWC2018]Minimax - 线段树合并优化概率DP
 * 
 * 题目描述：
 * 给定一棵二叉树，每个叶子节点有一个权值，非叶子节点有概率选择左子树或右子树的最大值。
 * 求根节点取每个可能值的概率。
 * 
 * 核心算法：概率DP + 线段树合并
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code25_P5298_Minimax_DP_Optimization {
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
    static final int MAXN = 300010;
    static final int MAXM = 20000000;
    
    static int n;
    static int[] parent = new int[MAXN];
    static int[] lc = new int[MAXN], rc = new int[MAXN]; // 左右孩子
    static long[] prob = new long[MAXN]; // 选择概率
    static long[] leafVal = new long[MAXN]; // 叶子节点值
    static boolean[] isLeaf = new boolean[MAXN];
    
    // 离散化相关
    static long[] vals = new long[MAXN];
    static int valCnt = 0;
    
    // 线段树节点
    static class Node {
        int l, r;
        long sum, mul, add;
        
        Node() {
            l = r = 0;
            sum = 0;
            mul = 1;
            add = 0;
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
        tree[cnt].add = 0;
        return cnt++;
    }
    
    // 下传标记
    static void pushDown(int rt) {
        if (tree[rt].mul != 1 || tree[rt].add != 0) {
            if (tree[rt].l != 0) {
                applyMul(tree[rt].l, tree[rt].mul);
                applyAdd(tree[rt].l, tree[rt].add);
            }
            if (tree[rt].r != 0) {
                applyMul(tree[rt].r, tree[rt].mul);
                applyAdd(tree[rt].r, tree[rt].add);
            }
            tree[rt].mul = 1;
            tree[rt].add = 0;
        }
    }
    
    static void applyMul(int rt, long mul) {
        if (rt == 0) return;
        tree[rt].sum = tree[rt].sum * mul % MOD;
        tree[rt].mul = tree[rt].mul * mul % MOD;
        tree[rt].add = tree[rt].add * mul % MOD;
    }
    
    static void applyAdd(int rt, long add) {
        if (rt == 0) return;
        tree[rt].add = (tree[rt].add + add) % MOD;
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
    static int merge(int u, int v, int l, int r, long mulU, long mulV, long addU, long addV) {
        if (u == 0 && v == 0) return 0;
        
        if (u == 0) {
            applyMul(v, mulV);
            applyAdd(v, addV);
            return v;
        }
        
        if (v == 0) {
            applyMul(u, mulU);
            applyAdd(u, addU);
            return u;
        }
        
        if (l == r) {
            // 叶子节点合并
            long sumU = tree[u].sum;
            long sumV = tree[v].sum;
            
            tree[u].sum = (sumU * mulU % MOD + sumV * mulV % MOD + addU + addV) % MOD;
            return u;
        }
        
        pushDown(u);
        pushDown(v);
        
        int mid = (l + r) >> 1;
        
        // 计算左右子树的和
        long sumUL = tree[tree[u].l].sum;
        long sumUR = tree[tree[u].r].sum;
        long sumVL = tree[tree[v].l].sum;
        long sumVR = tree[tree[v].r].sum;
        
        // 递归合并
        tree[u].l = merge(tree[u].l, tree[v].l, l, mid,
                         mulU, mulV,
                         (addU + sumUR * prob[u] % MOD) % MOD,
                         (addV + sumVR * (1 - prob[u] + MOD) % MOD) % MOD);
        
        tree[u].r = merge(tree[u].r, tree[v].r, mid + 1, r,
                         (mulU + sumUL * (1 - prob[u] + MOD) % MOD) % MOD,
                         (mulV + sumVL * prob[u] % MOD) % MOD,
                         addU, addV);
        
        tree[u].sum = (tree[tree[u].l].sum + tree[tree[u].r].sum) % MOD;
        
        return u;
    }
    
    // 离散化
    static int getIndex(long val) {
        return Arrays.binarySearch(vals, 0, valCnt, val) + 1;
    }
    
    // DFS进行树形DP
    static void dfs(int u) {
        if (isLeaf[u]) {
            // 叶子节点：初始化线段树
            root[u] = newNode();
            int pos = getIndex(leafVal[u]);
            update(root[u], 1, valCnt, pos, 1);
            return;
        }
        
        // 递归处理子树
        if (lc[u] != 0) dfs(lc[u]);
        if (rc[u] != 0) dfs(rc[u]);
        
        if (lc[u] != 0 && rc[u] != 0) {
            // 有两个孩子：合并线段树
            root[u] = merge(root[lc[u]], root[rc[u]], 1, valCnt,
                           prob[u], 1 - prob[u] + MOD, 0, 0);
        } else if (lc[u] != 0) {
            // 只有一个孩子：直接继承
            root[u] = root[lc[u]];
        } else {
            root[u] = root[rc[u]];
        }
    }
    
    // 收集结果
    static void collectResult(int rt, int l, int r, long[] result) {
        if (rt == 0) return;
        
        if (l == r) {
            result[l] = tree[rt].sum;
            return;
        }
        
        pushDown(rt);
        int mid = (l + r) >> 1;
        
        collectResult(tree[rt].l, l, mid, result);
        collectResult(tree[rt].r, mid + 1, r, result);
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        
        // 读入树结构
        for (int i = 1; i <= n; i++) {
            parent[i] = io.nextInt();
            if (parent[i] != 0) {
                if (lc[parent[i]] == 0) {
                    lc[parent[i]] = i;
                } else {
                    rc[parent[i]] = i;
                }
            }
        }
        
        // 读入概率和叶子节点值
        for (int i = 1; i <= n; i++) {
            long p = io.nextLong();
            if (lc[i] == 0 && rc[i] == 0) {
                // 叶子节点
                isLeaf[i] = true;
                leafVal[i] = p;
                vals[valCnt++] = p;
            } else {
                // 非叶子节点：概率
                prob[i] = p * 796898467L % MOD; // 逆元处理
            }
        }
        
        // 离散化叶子节点值
        Arrays.sort(vals, 0, valCnt);
        int uniqueCnt = 1;
        for (int i = 1; i < valCnt; i++) {
            if (vals[i] != vals[i - 1]) {
                vals[uniqueCnt++] = vals[i];
            }
        }
        valCnt = uniqueCnt;
        
        // 进行树形DP
        dfs(1);
        
        // 收集结果
        long[] result = new long[valCnt + 1];
        collectResult(root[1], 1, valCnt, result);
        
        // 输出结果
        for (int i = 1; i <= valCnt; i++) {
            io.println(result[i]);
        }
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    - 二叉树上的概率计算问题
 *    - 每个节点有概率选择左子树或右子树的最大值
 *    - 需要计算根节点取每个可能值的概率
 * 
 * 2. 解决方案：
 *    - 使用概率DP：f[u][x]表示节点u取值为x的概率
 *    - 使用线段树合并：高效合并子树概率分布
 *    - 离散化：处理大值域问题
 * 
 * 3. 核心优化：
 *    - 线段树合并：将O(n²)的合并复杂度优化到O(n log n)
 *    - 懒标记：处理概率的线性组合
 *    - 动态开点：节省内存空间
 * 
 * 4. 时间复杂度：
 *    - 每个节点最多被合并log n次
 *    - 总体复杂度O(n log n)
 * 
 * 5. 类似题目：
 *    - P6773 [NOI2020]命运
 *    - P4556 [Vani有约会]雨天的尾巴
 *    - CF911G Mass Change Queries
 * 
 * 6. 扩展应用：
 *    - 其他概率DP问题
 *    - 期望计算问题
 *    - 随机算法分析
 * 
 * 7. 实现技巧：
 *    - 注意概率的模运算
 *    - 合理设计合并函数
 *    - 注意离散化的处理
 */
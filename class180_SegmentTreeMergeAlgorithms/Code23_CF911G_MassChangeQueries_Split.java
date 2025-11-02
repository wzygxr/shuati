// 测试链接 : https://codeforces.com/problemset/problem/911/G
// 线段树分裂经典题目 - 批量修改查询

import java.io.*;
import java.util.*;

/**
 * CF911G Mass Change Queries - 线段树分裂实现
 * 
 * 题目描述：
 * 给定一个长度为n的数组，支持m次操作：
 * 1. 将区间[l, r]内所有等于x的值改为y
 * 2. 查询整个数组
 * 
 * 核心算法：线段树分裂 + 值域线段树
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code23_CF911G_MassChangeQueries_Split {
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
    
    // 线段树节点类
    static class Node {
        int l, r;  // 左右子节点索引
        int val;    // 当前节点的值（如果是叶子节点）
        int lazy;   // 懒标记
        
        Node() {
            l = r = -1;
            val = lazy = 0;
        }
    }
    
    static final int MAXN = 200010;
    static final int MAXM = 20000010;
    static Node[] tree = new Node[MAXM];
    static int cnt = 0;
    static int root;
    
    // 初始化线段树节点池
    static {
        for (int i = 0; i < MAXM; i++) {
            tree[i] = new Node();
        }
    }
    
    // 创建新节点
    static int newNode() {
        if (cnt >= MAXM) {
            System.gc();
            return -1;
        }
        tree[cnt].l = tree[cnt].r = -1;
        tree[cnt].val = 0;
        tree[cnt].lazy = 0;
        return cnt++;
    }
    
    // 下传懒标记
    static void pushDown(int rt) {
        if (tree[rt].lazy != 0) {
            if (tree[rt].l == -1) tree[rt].l = newNode();
            if (tree[rt].r == -1) tree[rt].r = newNode();
            
            tree[tree[rt].l].lazy = tree[rt].lazy;
            tree[tree[rt].r].lazy = tree[rt].lazy;
            tree[rt].lazy = 0;
        }
    }
    
    // 线段树分裂：将值为x的节点分裂出来
    static void splitByValue(int p, int q, int l, int r, int x) {
        if (p == -1) return;
        
        pushDown(p);
        
        if (l == r) {
            if (tree[p].val == x) {
                if (q == -1) q = newNode();
                tree[q].val = x;
                tree[p].val = 0;  // 原节点清空
            }
            return;
        }
        
        int mid = (l + r) >> 1;
        
        if (tree[p].l != -1) {
            if (tree[q].l == -1) tree[q].l = newNode();
            splitByValue(tree[p].l, tree[q].l, l, mid, x);
        }
        
        if (tree[p].r != -1) {
            if (tree[q].r == -1) tree[q].r = newNode();
            splitByValue(tree[p].r, tree[q].r, mid + 1, r, x);
        }
    }
    
    // 线段树合并
    static int merge(int p, int q, int l, int r) {
        if (p == -1) return q;
        if (q == -1) return p;
        
        if (l == r) {
            tree[p].val = (tree[p].val != 0) ? tree[p].val : tree[q].val;
            return p;
        }
        
        pushDown(p);
        pushDown(q);
        
        int mid = (l + r) >> 1;
        tree[p].l = merge(tree[p].l, tree[q].l, l, mid);
        tree[p].r = merge(tree[p].r, tree[q].r, mid + 1, r);
        
        return p;
    }
    
    // 区间赋值操作：将区间[l, r]内所有值为x的改为y
    static void massChange(int rt, int l, int r, int L, int R, int x, int y) {
        if (rt == -1 || L > r || R < l) return;
        
        pushDown(rt);
        
        if (L >= l && R <= r) {
            // 分裂出值为x的节点
            int tempRoot = newNode();
            splitByValue(rt, tempRoot, 1, MAXN, x);
            
            // 将分裂出的节点值改为y
            if (tempRoot != -1) {
                tree[tempRoot].lazy = y;
                // 合并回去
                merge(rt, tempRoot, 1, MAXN);
            }
            return;
        }
        
        int mid = (L + R) >> 1;
        if (l <= mid && tree[rt].l != -1) {
            massChange(tree[rt].l, l, r, L, mid, x, y);
        }
        if (r > mid && tree[rt].r != -1) {
            massChange(tree[rt].r, l, r, mid + 1, R, x, y);
        }
    }
    
    // 查询操作：获取最终数组
    static void queryArray(int rt, int l, int r, int[] result) {
        if (rt == -1) return;
        
        pushDown(rt);
        
        if (l == r) {
            if (tree[rt].val != 0) {
                result[l] = tree[rt].val;
            }
            return;
        }
        
        int mid = (l + r) >> 1;
        if (tree[rt].l != -1) {
            queryArray(tree[rt].l, l, mid, result);
        }
        if (tree[rt].r != -1) {
            queryArray(tree[rt].r, mid + 1, r, result);
        }
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        int n = io.nextInt();
        int[] arr = new int[n + 1];
        
        // 读入初始数组
        for (int i = 1; i <= n; i++) {
            arr[i] = io.nextInt();
        }
        
        // 构建初始线段树
        root = newNode();
        buildTree(root, 1, n, arr);
        
        int m = io.nextInt();
        
        while (m-- > 0) {
            int l = io.nextInt();
            int r = io.nextInt();
            int x = io.nextInt();
            int y = io.nextInt();
            
            if (x != y) {
                massChange(root, l, r, 1, n, x, y);
            }
        }
        
        // 查询最终结果
        int[] result = new int[n + 1];
        queryArray(root, 1, n, result);
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            io.print(result[i] + " ");
        }
        io.println();
        
        io.close();
    }
    
    // 构建初始线段树
    static void buildTree(int rt, int l, int r, int[] arr) {
        if (l == r) {
            tree[rt].val = arr[l];
            return;
        }
        
        int mid = (l + r) >> 1;
        
        tree[rt].l = newNode();
        buildTree(tree[rt].l, l, mid, arr);
        
        tree[rt].r = newNode();
        buildTree(tree[rt].r, mid + 1, r, arr);
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题特点：
 *    - 需要支持批量修改：将区间内所有等于x的值改为y
 *    - 值域较大：x和y的范围是1-100
 *    - 操作次数多：最多2e5次操作
 * 
 * 2. 解决方案：
 *    - 使用线段树分裂技术：将需要修改的值分裂出来
 *    - 对分裂出的子树进行批量修改
 *    - 再合并回原线段树
 * 
 * 3. 核心优化：
 *    - 懒标记：减少不必要的节点访问
 *    - 动态开点：节省内存空间
 *    - 值域压缩：针对小值域优化
 * 
 * 4. 时间复杂度：
 *    - 每次操作O(log n)
 *    - 总体复杂度O(m log n)
 * 
 * 5. 类似题目：
 *    - P5494 【模板】线段树分裂
 *    - P4556 雨天的尾巴
 *    - P3224 永无乡
 * 
 * 6. 扩展应用：
 *    - 支持多种批量修改操作
 *    - 可持久化版本
 *    - 分布式线段树
 */
// 测试链接 : https://codeforces.com/problemset/problem/1401/F
// 线段树分裂与反转操作 - CF1401F Reverse and Swap

import java.io.*;
import java.util.*;

/**
 * CF1401F Reverse and Swap - 线段树分裂与反转操作
 * 
 * 题目描述：
 * 给定一个长度为2^n的数组，支持4种操作：
 * 1. 将位置x的值改为v
 * 2. 反转区间[l, r]
 * 3. 交换两个不相交的区间
 * 4. 查询区间和
 * 
 * 核心算法：线段树分裂 + 反转标记
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code30_CF1401F_ReverseAndSwap {
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
    
    static final int MAXN = 300010;
    static final int MAXM = 20000000;
    
    static int n, q;
    static long[] arr;
    
    // 线段树节点
    static class Node {
        int l, r;
        long sum;
        boolean rev; // 反转标记
        
        Node() {
            l = r = -1;
            sum = 0;
            rev = false;
        }
    }
    
    static Node[] tree = new Node[MAXM];
    static int cnt = 0;
    static int root;
    
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
        tree[cnt].sum = 0;
        tree[cnt].rev = false;
        return cnt++;
    }
    
    // 下传反转标记
    static void pushDown(int rt, int l, int r) {
        if (tree[rt].rev) {
            int mid = (l + r) >> 1;
            
            // 交换左右子树
            int temp = tree[rt].l;
            tree[rt].l = tree[rt].r;
            tree[rt].r = temp;
            
            // 下传标记
            if (tree[rt].l != -1) tree[tree[rt].l].rev ^= true;
            if (tree[rt].r != -1) tree[tree[rt].r].rev ^= true;
            
            tree[rt].rev = false;
        }
    }
    
    // 构建初始线段树
    static void buildTree(int rt, int l, int r, long[] arr) {
        if (l == r) {
            tree[rt].sum = arr[l];
            return;
        }
        
        int mid = (l + r) >> 1;
        
        tree[rt].l = newNode();
        buildTree(tree[rt].l, l, mid, arr);
        
        tree[rt].r = newNode();
        buildTree(tree[rt].r, mid + 1, r, arr);
        
        tree[rt].sum = tree[tree[rt].l].sum + tree[tree[rt].r].sum;
    }
    
    // 单点更新
    static void update(int rt, int l, int r, int pos, long val) {
        if (l == r) {
            tree[rt].sum = val;
            return;
        }
        
        pushDown(rt, l, r);
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            update(tree[rt].l, l, mid, pos, val);
        } else {
            update(tree[rt].r, mid + 1, r, pos, val);
        }
        
        tree[rt].sum = tree[tree[rt].l].sum + tree[tree[rt].r].sum;
    }
    
    // 区间反转
    static void reverse(int rt, int l, int r, int ql, int qr) {
        if (ql > r || qr < l) return;
        
        if (ql <= l && r <= qr) {
            tree[rt].rev ^= true;
            return;
        }
        
        pushDown(rt, l, r);
        int mid = (l + r) >> 1;
        
        reverse(tree[rt].l, l, mid, ql, qr);
        reverse(tree[rt].r, mid + 1, r, ql, qr);
        
        tree[rt].sum = tree[tree[rt].l].sum + tree[tree[rt].r].sum;
    }
    
    // 线段树分裂
    static int split(int rt, int l, int r, int ql, int qr) {
        if (rt == -1 || ql > r || qr < l) return -1;
        
        pushDown(rt, l, r);
        
        if (ql <= l && r <= qr) {
            // 分裂出整个区间
            int newRt = newNode();
            tree[newRt] = tree[rt]; // 浅拷贝
            tree[rt].l = tree[rt].r = -1; // 原节点清空
            tree[rt].sum = 0;
            return newRt;
        }
        
        int mid = (l + r) >> 1;
        int leftSplit = -1, rightSplit = -1;
        
        if (ql <= mid && tree[rt].l != -1) {
            leftSplit = split(tree[rt].l, l, mid, ql, qr);
        }
        
        if (qr > mid && tree[rt].r != -1) {
            rightSplit = split(tree[rt].r, mid + 1, r, ql, qr);
        }
        
        // 创建新根节点
        int newRt = newNode();
        tree[newRt].l = leftSplit;
        tree[newRt].r = rightSplit;
        
        // 更新新根节点的和
        long leftSum = (leftSplit != -1) ? tree[leftSplit].sum : 0;
        long rightSum = (rightSplit != -1) ? tree[rightSplit].sum : 0;
        tree[newRt].sum = leftSum + rightSum;
        
        // 更新原根节点的和
        leftSum = (tree[rt].l != -1) ? tree[tree[rt].l].sum : 0;
        rightSum = (tree[rt].r != -1) ? tree[tree[rt].r].sum : 0;
        tree[rt].sum = leftSum + rightSum;
        
        return newRt;
    }
    
    // 线段树合并
    static int merge(int u, int v, int l, int r) {
        if (u == -1) return v;
        if (v == -1) return u;
        
        pushDown(u, l, r);
        pushDown(v, l, r);
        
        if (l == r) {
            tree[u].sum += tree[v].sum;
            return u;
        }
        
        int mid = (l + r) >> 1;
        
        tree[u].l = merge(tree[u].l, tree[v].l, l, mid);
        tree[u].r = merge(tree[u].r, tree[v].r, mid + 1, r);
        
        tree[u].sum = tree[tree[u].l].sum + tree[tree[u].r].sum;
        
        return u;
    }
    
    // 区间交换
    static void swap(int rt, int l, int r, int l1, int r1, int l2, int r2) {
        // 分裂出第一个区间
        int seg1 = split(rt, l, r, l1, r1);
        
        // 分裂出第二个区间
        int seg2 = split(rt, l, r, l2, r2);
        
        // 交换两个区间
        rt = merge(rt, seg2, l, r);
        rt = merge(rt, seg1, l, r);
    }
    
    // 区间查询
    static long query(int rt, int l, int r, int ql, int qr) {
        if (rt == -1 || ql > r || qr < l) return 0;
        
        pushDown(rt, l, r);
        
        if (ql <= l && r <= qr) {
            return tree[rt].sum;
        }
        
        int mid = (l + r) >> 1;
        long res = 0;
        
        if (ql <= mid && tree[rt].l != -1) {
            res += query(tree[rt].l, l, mid, ql, qr);
        }
        
        if (qr > mid && tree[rt].r != -1) {
            res += query(tree[rt].r, mid + 1, r, ql, qr);
        }
        
        return res;
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = 1 << io.nextInt(); // 2^n
        q = io.nextInt();
        
        arr = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            arr[i] = io.nextLong();
        }
        
        // 构建线段树
        root = newNode();
        buildTree(root, 1, n, arr);
        
        while (q-- > 0) {
            int type = io.nextInt();
            
            switch (type) {
                case 1: // 单点更新
                    int x = io.nextInt();
                    long v = io.nextLong();
                    update(root, 1, n, x, v);
                    break;
                    
                case 2: // 区间反转
                    int l = io.nextInt();
                    int r = io.nextInt();
                    reverse(root, 1, n, l, r);
                    break;
                    
                case 3: // 区间交换
                    int l1 = io.nextInt(), r1 = io.nextInt();
                    int l2 = io.nextInt(), r2 = io.nextInt();
                    swap(root, 1, n, l1, r1, l2, r2);
                    break;
                    
                case 4: // 区间查询
                    int ql = io.nextInt(), qr = io.nextInt();
                    long ans = query(root, 1, n, ql, qr);
                    io.println(ans);
                    break;
            }
        }
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题特点：
 *    - 支持复杂的区间操作：反转、交换
 *    - 需要高效处理大规模数据
 *    - 操作之间相互影响
 * 
 * 2. 解决方案：
 *    - 使用线段树分裂技术：支持区间分离
 *    - 使用反转标记：高效处理反转操作
 *    - 动态开点：节省内存空间
 * 
 * 3. 核心优化：
 *    - 懒标记：减少不必要的节点访问
 *    - 启发式合并：优化合并效率
 *    - 内存池管理：减少内存分配开销
 * 
 * 4. 时间复杂度：
 *    - 每次操作O(log n)
 *    - 总体复杂度O(q log n)
 * 
 * 5. 类似题目：
 *    - P5494 【模板】线段树分裂
 *    - CF911G Mass Change Queries
 *    - P4556 雨天的尾巴
 * 
 * 6. 扩展应用：
 *    - 支持更多复杂操作
 *    - 可持久化版本
 *    - 分布式处理
 */
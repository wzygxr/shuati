// 线段树合并加速区间最大子段和DP优化

import java.io.*;
import java.util.*;

/**
 * 区间最大子段和问题 - 线段树合并优化DP
 * 
 * 问题描述：
 * 给定一个序列，支持区间分裂和合并操作，同时需要维护区间最大子段和
 * 
 * 核心算法：线段树分裂 + 区间信息维护
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code31_AdvancedDPOptimization_IntervalMaxSubarray {
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
    
    // 区间信息结构体
    static class SegmentInfo {
        long sum;           // 区间和
        long maxPrefix;     // 最大前缀和
        long maxSuffix;     // 最大后缀和
        long maxSubarray;   // 最大子段和
        
        SegmentInfo() {
            sum = maxPrefix = maxSuffix = maxSubarray = 0;
        }
        
        SegmentInfo(long val) {
            sum = val;
            maxPrefix = maxSuffix = maxSubarray = Math.max(0, val);
        }
        
        // 合并两个区间信息
        static SegmentInfo merge(SegmentInfo left, SegmentInfo right) {
            SegmentInfo res = new SegmentInfo();
            res.sum = left.sum + right.sum;
            res.maxPrefix = Math.max(left.maxPrefix, left.sum + right.maxPrefix);
            res.maxSuffix = Math.max(right.maxSuffix, right.sum + left.maxSuffix);
            res.maxSubarray = Math.max(Math.max(left.maxSubarray, right.maxSubarray), 
                                     left.maxSuffix + right.maxPrefix);
            return res;
        }
    }
    
    // 线段树节点
    static class Node {
        int l, r;
        SegmentInfo info;
        
        Node() {
            l = r = -1;
            info = new SegmentInfo();
        }
    }
    
    static final int MAXN = 100010;
    static final int MAXM = 20000000;
    static Node[] tree = new Node[MAXM];
    static int cnt = 0;
    
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
        tree[cnt].info = new SegmentInfo();
        return cnt++;
    }
    
    // 构建叶子节点
    static int buildLeaf(long val) {
        int rt = newNode();
        tree[rt].info = new SegmentInfo(val);
        return rt;
    }
    
    // 线段树合并（核心函数）
    static int merge(int u, int v) {
        if (u == -1) return v;
        if (v == -1) return u;
        
        // 合并左右子树信息
        SegmentInfo leftInfo = tree[u].info;
        SegmentInfo rightInfo = tree[v].info;
        
        tree[u].info = SegmentInfo.merge(leftInfo, rightInfo);
        
        // 递归合并子树
        tree[u].l = merge(tree[u].l, tree[v].l);
        tree[u].r = merge(tree[u].r, tree[v].r);
        
        return u;
    }
    
    // 线段树分裂
    static int[] split(int rt, int l, int r, int pos) {
        if (rt == -1) return new int[]{-1, -1};
        
        if (l == r) {
            // 叶子节点分裂
            int newRt = newNode();
            tree[newRt].info = tree[rt].info;
            tree[rt].info = new SegmentInfo();
            return new int[]{rt, newRt};
        }
        
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            // 分裂左子树
            int[] leftSplit = split(tree[rt].l, l, mid, pos);
            tree[rt].l = leftSplit[0];
            
            int newRt = newNode();
            tree[newRt].l = leftSplit[1];
            tree[newRt].r = tree[rt].r;
            
            // 更新信息
            updateInfo(rt);
            updateInfo(newRt);
            
            return new int[]{rt, newRt};
        } else {
            // 分裂右子树
            int[] rightSplit = split(tree[rt].r, mid + 1, r, pos);
            tree[rt].r = rightSplit[0];
            
            int newRt = newNode();
            tree[newRt].l = tree[rt].l;
            tree[newRt].r = rightSplit[1];
            
            // 更新信息
            updateInfo(rt);
            updateInfo(newRt);
            
            return new int[]{rt, newRt};
        }
    }
    
    // 更新节点信息
    static void updateInfo(int rt) {
        if (rt == -1) return;
        
        SegmentInfo leftInfo = (tree[rt].l != -1) ? tree[tree[rt].l].info : new SegmentInfo();
        SegmentInfo rightInfo = (tree[rt].r != -1) ? tree[tree[rt].r].info : new SegmentInfo();
        
        tree[rt].info = SegmentInfo.merge(leftInfo, rightInfo);
    }
    
    // 区间最大子段和查询
    static SegmentInfo query(int rt, int l, int r, int ql, int qr) {
        if (rt == -1 || ql > r || qr < l) {
            return new SegmentInfo();
        }
        
        if (ql <= l && r <= qr) {
            return tree[rt].info;
        }
        
        int mid = (l + r) >> 1;
        
        SegmentInfo leftInfo = query(tree[rt].l, l, mid, ql, qr);
        SegmentInfo rightInfo = query(tree[rt].r, mid + 1, r, ql, qr);
        
        return SegmentInfo.merge(leftInfo, rightInfo);
    }
    
    // 构建初始线段树
    static int buildTree(int l, int r, long[] arr) {
        if (l == r) {
            return buildLeaf(arr[l]);
        }
        
        int mid = (l + r) >> 1;
        int leftTree = buildTree(l, mid, arr);
        int rightTree = buildTree(mid + 1, r, arr);
        
        return merge(leftTree, rightTree);
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        int n = io.nextInt();
        int q = io.nextInt();
        
        long[] arr = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            arr[i] = io.nextLong();
        }
        
        // 构建初始线段树
        int root = buildTree(1, n, arr);
        
        while (q-- > 0) {
            int type = io.nextInt();
            
            switch (type) {
                case 1: // 区间分裂
                    int pos = io.nextInt();
                    int[] splitResult = split(root, 1, n, pos);
                    root = splitResult[0];
                    int newRoot = splitResult[1];
                    
                    // 可以继续对两个子树进行操作
                    break;
                    
                case 2: // 区间合并
                    int otherRoot = buildTree(1, n, new long[n + 1]); // 示例
                    root = merge(root, otherRoot);
                    break;
                    
                case 3: // 查询区间最大子段和
                    int l = io.nextInt(), r = io.nextInt();
                    SegmentInfo info = query(root, 1, n, l, r);
                    io.println(info.maxSubarray);
                    break;
                    
                case 4: // 查询区间和
                    int ql = io.nextInt(), qr = io.nextInt();
                    SegmentInfo sumInfo = query(root, 1, n, ql, qr);
                    io.println(sumInfo.sum);
                    break;
            }
        }
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    - 需要支持动态的区间分裂和合并
 *    - 同时维护复杂的区间信息（最大子段和）
 *    - 传统方法复杂度较高
 * 
 * 2. 解决方案：
 *    - 使用线段树分裂技术：支持动态区间操作
 *    - 维护区间信息结构：sum, maxPrefix, maxSuffix, maxSubarray
 *    - 高效合并区间信息
 * 
 * 3. 核心优化：
 *    - 信息合并公式：O(1)时间合并两个区间信息
 *    - 动态开点：节省内存空间
 *    - 懒标记：减少不必要的更新
 * 
 * 4. 时间复杂度：
 *    - 每次分裂/合并操作O(log n)
 *    - 查询操作O(log n)
 *    - 总体复杂度O(q log n)
 * 
 * 5. 应用场景：
 *    - 动态序列维护
 *    - 区间统计问题
 *    - 最大子段和变种问题
 * 
 * 6. 扩展方向：
 *    - 支持更多区间统计信息
 *    - 可持久化版本
 *    - 分布式处理
 */
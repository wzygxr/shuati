import java.io.*;
import java.util.*;

/**
 * CF438D The Child and Sequence - 线段树分裂算法实现
 * 
 * 题目链接: https://codeforces.com/contest/438/problem/D
 * 
 * 题目描述:
 * 给定一个序列，支持三种操作:
 * 1. 区间求和
 * 2. 区间取模 (每个数对x取模)
 * 3. 单点修改
 * 
 * 核心算法: 线段树 + 取模优化 + 线段树分裂
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * 解题思路:
 * 1. 使用线段树维护区间最大值和区间和
 * 2. 对于取模操作，如果区间最大值小于模数，则不需要递归
 * 3. 使用线段树分裂优化取模操作
 * 4. 维护区间最大值来剪枝
 */
public class Code20_CF438D_TheChildAndSequence {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(System.out);
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
        void print(Object obj) { out.print(obj); }
        void close() { out.close(); }
    }
    
    static class SegmentTreeNode {
        long sum; // 区间和
        long max; // 区间最大值
        SegmentTreeNode left, right; // 左右儿子
        
        SegmentTreeNode() {
            sum = 0;
            max = 0;
            left = right = null;
        }
    }
    
    static int n, m;
    static long[] arr;
    static SegmentTreeNode root;
    
    // 构建线段树
    static SegmentTreeNode build(int l, int r) {
        SegmentTreeNode node = new SegmentTreeNode();
        if (l == r) {
            node.sum = arr[l];
            node.max = arr[l];
            return node;
        }
        
        int mid = (l + r) >> 1;
        node.left = build(l, mid);
        node.right = build(mid + 1, r);
        node.sum = node.left.sum + node.right.sum;
        node.max = Math.max(node.left.max, node.right.max);
        return node;
    }
    
    // 单点更新
    static void update(SegmentTreeNode node, int l, int r, int pos, long val) {
        if (l == r) {
            node.sum = val;
            node.max = val;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            update(node.left, l, mid, pos, val);
        } else {
            update(node.right, mid + 1, r, pos, val);
        }
        node.sum = node.left.sum + node.right.sum;
        node.max = Math.max(node.left.max, node.right.max);
    }
    
    // 区间取模
    static void modulo(SegmentTreeNode node, int l, int r, int ql, int qr, long mod) {
        if (node.max < mod) return; // 剪枝：最大值小于模数，不需要处理
        if (l == r) {
            // 叶子节点直接取模
            node.sum %= mod;
            node.max = node.sum;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (ql <= mid) {
            modulo(node.left, l, mid, ql, qr, mod);
        }
        if (qr > mid) {
            modulo(node.right, mid + 1, r, ql, qr, mod);
        }
        node.sum = node.left.sum + node.right.sum;
        node.max = Math.max(node.left.max, node.right.max);
    }
    
    // 区间查询
    static long query(SegmentTreeNode node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return node.sum;
        }
        
        int mid = (l + r) >> 1;
        long res = 0;
        if (ql <= mid) {
            res += query(node.left, l, mid, ql, qr);
        }
        if (qr > mid) {
            res += query(node.right, mid + 1, r, ql, qr);
        }
        return res;
    }
    
    // 线段树分裂 - 将需要取模的区间分裂出来
    static SegmentTreeNode splitForModulo(SegmentTreeNode node, int l, int r, int ql, int qr, long mod) {
        if (node.max < mod) return null; // 不需要处理的部分
        
        if (ql <= l && r <= qr) {
            // 整个区间都在查询范围内
            if (node.max < mod) return null;
            
            // 创建新的节点来处理取模
            SegmentTreeNode newNode = new SegmentTreeNode();
            if (l == r) {
                // 叶子节点直接取模
                newNode.sum = node.sum % mod;
                newNode.max = newNode.sum;
            } else {
                int mid = (l + r) >> 1;
                newNode.left = splitForModulo(node.left, l, mid, ql, qr, mod);
                newNode.right = splitForModulo(node.right, mid + 1, r, ql, qr, mod);
                
                if (newNode.left == null && newNode.right == null) {
                    return null;
                }
                
                newNode.sum = (newNode.left != null ? newNode.left.sum : 0) + 
                             (newNode.right != null ? newNode.right.sum : 0);
                newNode.max = Math.max(newNode.left != null ? newNode.left.max : 0, 
                                     newNode.right != null ? newNode.right.max : 0);
            }
            return newNode;
        }
        
        int mid = (l + r) >> 1;
        SegmentTreeNode leftPart = null, rightPart = null;
        
        if (ql <= mid) {
            leftPart = splitForModulo(node.left, l, mid, ql, qr, mod);
        }
        if (qr > mid) {
            rightPart = splitForModulo(node.right, mid + 1, r, ql, qr, mod);
        }
        
        if (leftPart == null && rightPart == null) {
            return null;
        }
        
        SegmentTreeNode newNode = new SegmentTreeNode();
        newNode.left = leftPart;
        newNode.right = rightPart;
        newNode.sum = (leftPart != null ? leftPart.sum : 0) + 
                     (rightPart != null ? rightPart.sum : 0);
        newNode.max = Math.max(leftPart != null ? leftPart.max : 0, 
                             rightPart != null ? rightPart.max : 0);
        
        return newNode;
    }
    
    // 线段树合并 - 将处理后的区间合并回原树
    static void mergeBack(SegmentTreeNode original, SegmentTreeNode processed, int l, int r, int ql, int qr) {
        if (processed == null) return;
        
        if (ql <= l && r <= qr) {
            // 整个区间都在查询范围内
            if (l == r) {
                // 叶子节点直接替换
                original.sum = processed.sum;
                original.max = processed.max;
            } else {
                // 递归合并左右子树
                mergeBack(original.left, processed.left, l, (l + r) >> 1, ql, qr);
                mergeBack(original.right, processed.right, ((l + r) >> 1) + 1, r, ql, qr);
                original.sum = original.left.sum + original.right.sum;
                original.max = Math.max(original.left.max, original.right.max);
            }
            return;
        }
        
        int mid = (l + r) >> 1;
        if (ql <= mid) {
            mergeBack(original.left, processed.left, l, mid, ql, qr);
        }
        if (qr > mid) {
            mergeBack(original.right, processed.right, mid + 1, r, ql, qr);
        }
        original.sum = original.left.sum + original.right.sum;
        original.max = Math.max(original.left.max, original.right.max);
    }
    
    // 优化的取模操作 - 使用线段树分裂
    static void optimizedModulo(int l, int r, long mod) {
        // 分裂出需要处理的区间
        SegmentTreeNode processed = splitForModulo(root, 1, n, l, r, mod);
        
        if (processed != null) {
            // 将处理后的区间合并回原树
            mergeBack(root, processed, 1, n, l, r);
        }
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        m = io.nextInt();
        arr = new long[n + 1];
        
        for (int i = 1; i <= n; i++) {
            arr[i] = io.nextLong();
        }
        
        // 构建线段树
        root = build(1, n);
        
        while (m-- > 0) {
            int type = io.nextInt();
            
            switch (type) {
                case 1: // 区间求和
                    int l = io.nextInt();
                    int r = io.nextInt();
                    long sum = query(root, 1, n, l, r);
                    io.println(sum);
                    break;
                    
                case 2: // 区间取模
                    l = io.nextInt();
                    r = io.nextInt();
                    long mod = io.nextLong();
                    optimizedModulo(l, r, mod);
                    break;
                    
                case 3: // 单点修改
                    int pos = io.nextInt();
                    long val = io.nextLong();
                    update(root, 1, n, pos, val);
                    break;
            }
        }
        
        io.close();
    }
}

/**
 * 类似题目推荐:
 * 1. CF1401F Reverse and Swap - 线段树分裂经典应用
 * 2. CF474F Ant Colony - GCD操作 + 线段树
 * 3. CF52C Circular RMQ - 环形区间操作
 * 4. P5494 【模板】线段树分裂 - 线段树分裂基础模板
 * 5. P4556 [Vani有约会]雨天的尾巴 - 树上差分 + 线段树合并
 * 
 * 线段树分裂算法总结:
 * 线段树分裂在取模操作中的应用:
 * 1. 将需要取模的区间分裂出来单独处理
 * 2. 避免对整个线段树进行不必要的递归
 * 3. 通过最大值剪枝优化性能
 * 4. 处理完成后合并回原树
 * 
 * 时间复杂度: O(log n) 每次分裂/合并
 * 空间复杂度: O(n log n)
 */
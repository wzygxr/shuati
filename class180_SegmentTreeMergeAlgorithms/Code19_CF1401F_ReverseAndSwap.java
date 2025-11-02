import java.io.*;
import java.util.*;

/**
 * CF1401F Reverse and Swap - 线段树分裂算法实现
 * 
 * 题目链接: https://codeforces.com/contest/1401/problem/F
 * 
 * 题目描述:
 * 给定一个长度为2^n的数组，支持4种操作:
 * 1. 将某个位置的值替换为x
 * 2. 反转区间[l, r]
 * 3. 交换两个长度为2^k的连续区间
 * 4. 查询区间[l, r]的和
 * 
 * 核心算法: 线段树分裂 + 懒标记
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 使用线段树维护区间和
 * 2. 使用懒标记记录反转操作
 * 3. 通过线段树分裂实现区间反转和交换
 * 4. 维护每个节点的反转状态
 */
public class Code19_CF1401F_ReverseAndSwap {
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
        boolean rev; // 反转标记
        SegmentTreeNode left, right; // 左右儿子
        
        SegmentTreeNode() {
            sum = 0;
            rev = false;
            left = right = null;
        }
    }
    
    static int n, q;
    static long[] arr;
    static SegmentTreeNode root;
    
    // 构建线段树
    static SegmentTreeNode build(int l, int r) {
        SegmentTreeNode node = new SegmentTreeNode();
        if (l == r) {
            node.sum = arr[l];
            return node;
        }
        
        int mid = (l + r) >> 1;
        node.left = build(l, mid);
        node.right = build(mid + 1, r);
        node.sum = node.left.sum + node.right.sum;
        return node;
    }
    
    // 下传反转标记
    static void pushDown(SegmentTreeNode node, int l, int r) {
        if (node.rev) {
            // 交换左右子树
            SegmentTreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            
            // 下传标记
            if (l != r) {
                node.left.rev = !node.left.rev;
                node.right.rev = !node.right.rev;
            }
            node.rev = false;
        }
    }
    
    // 单点更新
    static void update(SegmentTreeNode node, int l, int r, int pos, long val) {
        pushDown(node, l, r);
        if (l == r) {
            node.sum = val;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            update(node.left, l, mid, pos, val);
        } else {
            update(node.right, mid + 1, r, pos, val);
        }
        node.sum = node.left.sum + node.right.sum;
    }
    
    // 区间查询
    static long query(SegmentTreeNode node, int l, int r, int ql, int qr) {
        pushDown(node, l, r);
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
    
    // 反转区间
    static void reverse(SegmentTreeNode node, int l, int r, int k) {
        if (k == 0) return;
        pushDown(node, l, r);
        
        // 反转当前层的左右子树
        node.rev = !node.rev;
        
        // 递归处理下一层
        int mid = (l + r) >> 1;
        reverse(node.left, l, mid, k - 1);
        reverse(node.right, mid + 1, r, k - 1);
    }
    
    // 交换两个区间
    static void swap(SegmentTreeNode node, int l, int r, int k) {
        if (k == 0) return;
        pushDown(node, l, r);
        
        // 交换左右子树
        SegmentTreeNode temp = node.left;
        node.left = node.right;
        node.right = temp;
        
        // 递归处理下一层
        int mid = (l + r) >> 1;
        swap(node.left, l, mid, k - 1);
        swap(node.right, mid + 1, r, k - 1);
    }
    
    // 线段树分裂 - 将区间[l, r]分裂出来
    static SegmentTreeNode split(SegmentTreeNode node, int l, int r, int ql, int qr) {
        pushDown(node, l, r);
        if (ql <= l && r <= qr) {
            // 整个区间都在查询范围内，直接返回这个子树
            return node;
        }
        
        int mid = (l + r) >> 1;
        SegmentTreeNode leftPart = null, rightPart = null;
        
        if (ql <= mid) {
            leftPart = split(node.left, l, mid, ql, qr);
        }
        if (qr > mid) {
            rightPart = split(node.right, mid + 1, r, ql, qr);
        }
        
        // 创建新的节点来合并分裂结果
        SegmentTreeNode newNode = new SegmentTreeNode();
        if (leftPart != null && rightPart != null) {
            newNode.left = leftPart;
            newNode.right = rightPart;
            newNode.sum = leftPart.sum + rightPart.sum;
        } else if (leftPart != null) {
            newNode = leftPart;
        } else {
            newNode = rightPart;
        }
        
        return newNode;
    }
    
    // 线段树合并
    static SegmentTreeNode merge(SegmentTreeNode node1, SegmentTreeNode node2, int l, int r) {
        if (node1 == null) return node2;
        if (node2 == null) return node1;
        
        pushDown(node1, l, r);
        pushDown(node2, l, r);
        
        if (l == r) {
            // 叶子节点，直接合并值
            node1.sum += node2.sum;
            return node1;
        }
        
        int mid = (l + r) >> 1;
        node1.left = merge(node1.left, node2.left, l, mid);
        node1.right = merge(node1.right, node2.right, mid + 1, r);
        node1.sum = node1.left.sum + node1.right.sum;
        
        return node1;
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        q = io.nextInt();
        int size = 1 << n;
        arr = new long[size + 1];
        
        for (int i = 1; i <= size; i++) {
            arr[i] = io.nextLong();
        }
        
        // 构建线段树
        root = build(1, size);
        
        while (q-- > 0) {
            int type = io.nextInt();
            
            switch (type) {
                case 1: // 单点更新
                    int pos = io.nextInt();
                    long val = io.nextLong();
                    update(root, 1, size, pos, val);
                    break;
                    
                case 2: // 反转区间
                    int k = io.nextInt();
                    reverse(root, 1, size, k);
                    break;
                    
                case 3: // 交换区间
                    k = io.nextInt();
                    swap(root, 1, size, k);
                    break;
                    
                case 4: // 区间查询
                    int l = io.nextInt();
                    int r = io.nextInt();
                    long sum = query(root, 1, size, l, r);
                    io.println(sum);
                    break;
            }
        }
        
        io.close();
    }
}

/**
 * 类似题目推荐:
 * 1. P5494 【模板】线段树分裂 - 线段树分裂基础模板
 * 2. P4556 [Vani有约会]雨天的尾巴 - 树上差分 + 线段树合并
 * 3. P3224 [HNOI2012]永无乡 - 平衡树合并/线段树合并
 * 4. CF911G Mass Change Queries - 线段树合并 + 映射维护
 * 5. P5298 [PKUWC2018]Minimax - 概率DP + 线段树合并
 * 
 * 线段树分裂算法总结:
 * 线段树分裂主要用于处理需要将线段树按照某种条件拆分的场景，常见应用包括:
 * 1. 区间反转操作
 * 2. 区间交换操作  
 * 3. 动态维护多个线段树
 * 4. 支持复杂区间操作的数据结构
 * 
 * 时间复杂度: O(log n) 每次分裂/合并
 * 空间复杂度: O(n log n)
 */
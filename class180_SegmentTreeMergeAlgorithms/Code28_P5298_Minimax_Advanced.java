import java.io.*;
import java.util.*;

/**
 * Code28: P5298 Minimax Advanced (线段树合并 + 概率DP优化)
 * 题目：给定一棵二叉树，每个叶子节点有概率值，非叶子节点取子节点最大值或最小值
 * 使用线段树合并优化概率DP计算
 * 时间复杂度：O(nlogn)
 */
public class Code28_P5298_Minimax_Advanced {
    
    static class Node {
        int type; // 0: 叶子节点, 1: 取最大值, 2: 取最小值
        double prob;
        Node left, right;
        
        Node(int type, double prob) {
            this.type = type;
            this.prob = prob;
        }
    }
    
    static class SegmentTreeNode {
        int l, r;
        double sum, lazy;
        SegmentTreeNode left, right;
        
        SegmentTreeNode(int l, int r) {
            this.l = l;
            this.r = r;
            this.sum = 0;
            this.lazy = 1;
        }
    }
    
    static int n, maxVal = 30000;
    static Node[] tree;
    static SegmentTreeNode[] dp;
    static double[] leafProbs;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        n = Integer.parseInt(br.readLine());
        tree = new Node[n + 1];
        dp = new SegmentTreeNode[n + 1];
        leafProbs = new double[n + 1];
        
        for (int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());
            if (type == 0) {
                // 叶子节点
                double prob = Double.parseDouble(st.nextToken());
                tree[i] = new Node(0, prob);
                leafProbs[i] = prob;
            } else {
                // 非叶子节点
                int left = Integer.parseInt(st.nextToken());
                int right = Integer.parseInt(st.nextToken());
                tree[i] = new Node(type, 0);
                tree[i].left = tree[left];
                tree[i].right = tree[right];
            }
        }
        
        // 线段树合并计算DP
        dfsDP(1);
        
        // 输出根节点的概率分布
        printDistribution(pw, dp[1]);
        
        pw.flush();
        pw.close();
    }
    
    static void dfsDP(int u) {
        if (tree[u].type == 0) {
            // 叶子节点：创建单点线段树
            dp[u] = new SegmentTreeNode(1, maxVal);
            updatePoint(dp[u], (int)(leafProbs[u] * maxVal), 1.0);
            return;
        }
        
        // 递归处理子节点
        int left = getLeftChild(u);
        int right = getRightChild(u);
        dfsDP(left);
        dfsDP(right);
        
        if (tree[u].type == 1) {
            // 取最大值：dp[u] = dp[left] * P(right <= x) + dp[right] * P(left <= x) - dp[left] * dp[right]
            dp[u] = mergeMax(dp[left], dp[right]);
        } else {
            // 取最小值：dp[u] = dp[left] * P(right >= x) + dp[right] * P(left >= x) - dp[left] * dp[right]
            dp[u] = mergeMin(dp[left], dp[right]);
        }
    }
    
    static SegmentTreeNode mergeMax(SegmentTreeNode a, SegmentTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        pushDown(a);
        pushDown(b);
        
        if (a.l == a.r) {
            // 叶子节点：a.sum * prefixSum(b) + b.sum * prefixSum(a) - a.sum * b.sum
            double prefixA = queryPrefix(a, a.l);
            double prefixB = queryPrefix(b, a.l);
            a.sum = a.sum * prefixB + b.sum * prefixA - a.sum * b.sum;
            return a;
        }
        
        a.left = mergeMax(a.left, b.left);
        a.right = mergeMax(a.right, b.right);
        a.sum = (a.left != null ? a.left.sum : 0) + (a.right != null ? a.right.sum : 0);
        
        return a;
    }
    
    static SegmentTreeNode mergeMin(SegmentTreeNode a, SegmentTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        pushDown(a);
        pushDown(b);
        
        if (a.l == a.r) {
            // 叶子节点：a.sum * suffixSum(b) + b.sum * suffixSum(a) - a.sum * b.sum
            double suffixA = querySuffix(a, a.l);
            double suffixB = querySuffix(b, a.l);
            a.sum = a.sum * suffixB + b.sum * suffixA - a.sum * b.sum;
            return a;
        }
        
        a.left = mergeMin(a.left, b.left);
        a.right = mergeMin(a.right, b.right);
        a.sum = (a.left != null ? a.left.sum : 0) + (a.right != null ? a.right.sum : 0);
        
        return a;
    }
    
    static void pushDown(SegmentTreeNode node) {
        if (node.l == node.r) return;
        
        int mid = (node.l + node.r) >> 1;
        if (node.left == null) {
            node.left = new SegmentTreeNode(node.l, mid);
            node.right = new SegmentTreeNode(mid + 1, node.r);
        }
        
        if (node.lazy != 1) {
            node.left.sum *= node.lazy;
            node.right.sum *= node.lazy;
            node.left.lazy *= node.lazy;
            node.right.lazy *= node.lazy;
            node.lazy = 1;
        }
    }
    
    static void updatePoint(SegmentTreeNode node, int pos, double val) {
        if (node.l == node.r) {
            node.sum = val;
            return;
        }
        
        pushDown(node);
        int mid = (node.l + node.r) >> 1;
        if (pos <= mid) {
            updatePoint(node.left, pos, val);
        } else {
            updatePoint(node.right, pos, val);
        }
        node.sum = (node.left != null ? node.left.sum : 0) + (node.right != null ? node.right.sum : 0);
    }
    
    static double queryPrefix(SegmentTreeNode node, int x) {
        if (node == null) return 0;
        if (x >= node.r) return node.sum;
        if (x < node.l) return 0;
        
        pushDown(node);
        int mid = (node.l + node.r) >> 1;
        double res = 0;
        if (x <= mid) {
            res = queryPrefix(node.left, x);
        } else {
            res = (node.left != null ? node.left.sum : 0) + queryPrefix(node.right, x);
        }
        return res;
    }
    
    static double querySuffix(SegmentTreeNode node, int x) {
        if (node == null) return 0;
        if (x <= node.l) return node.sum;
        if (x > node.r) return 0;
        
        pushDown(node);
        int mid = (node.l + node.r) >> 1;
        double res = 0;
        if (x > mid) {
            res = querySuffix(node.right, x);
        } else {
            res = querySuffix(node.left, x) + (node.right != null ? node.right.sum : 0);
        }
        return res;
    }
    
    static int getLeftChild(int u) {
        // 根据实际情况获取左子节点
        return u * 2 <= n ? u * 2 : -1;
    }
    
    static int getRightChild(int u) {
        // 根据实际情况获取右子节点
        return u * 2 + 1 <= n ? u * 2 + 1 : -1;
    }
    
    static void printDistribution(PrintWriter pw, SegmentTreeNode node) {
        if (node == null) return;
        
        if (node.l == node.r) {
            if (node.sum > 1e-9) {
                pw.printf("%.6f %.6f\n", (double)node.l / maxVal, node.sum);
            }
            return;
        }
        
        pushDown(node);
        printDistribution(pw, node.left);
        printDistribution(pw, node.right);
    }
}
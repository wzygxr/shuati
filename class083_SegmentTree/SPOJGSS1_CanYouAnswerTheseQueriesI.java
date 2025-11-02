package class110.problems.java;

// SPOJ GSS1. Can you answer these queries I
// 题目链接: https://www.spoj.com/problems/GSS1/
// 题目描述:
// 给定一个长度为N的整数序列A1, A2, ..., AN。你需要处理M个查询。
// 对于每个查询，给定两个整数i和j，你需要找到序列中从Ai到Aj的最大子段和。
// 最大子段和定义为：max{Ak + Ak+1 + ... + Al | i <= k <= l <= j}
//
// 输入:
// 第一行包含一个整数N (1 <= N <= 50000)，表示序列的长度。
// 第二行包含N个整数，表示序列A1, A2, ..., AN (-15007 <= Ai <= 15007)。
// 第三行包含一个整数M (1 <= M <= 10000)，表示查询的数量。
// 接下来M行，每行包含两个整数i和j (1 <= i <= j <= N)，表示一个查询。
//
// 输出:
// 对于每个查询，输出一行包含一个整数，表示从Ai到Aj的最大子段和。
//
// 示例:
// 输入:
// 5
// -1 2 -3 4 -5
// 3
// 1 3
// 2 5
// 1 5
//
// 输出:
// 2
// 4
// 4
//
// 解题思路:
// 这是一个经典的线段树问题，需要维护区间最大子段和。
// 对于每个线段树节点，我们需要维护以下信息：
// 1. 区间和(sum)
// 2. 区间最大子段和(maxSum)
// 3. 区间以左端点开始的最大子段和(prefixMax)
// 4. 区间以右端点结束的最大子段和(suffixMax)
//
// 合并两个子区间[l, mid]和[mid+1, r]的信息时：
// 1. 区间和 = 左区间和 + 右区间和
// 2. 区间最大子段和 = max(左区间最大子段和, 右区间最大子段和, 左区间后缀最大值 + 右区间前缀最大值)
// 3. 区间前缀最大值 = max(左区间前缀最大值, 左区间和 + 右区间前缀最大值)
// 4. 区间后缀最大值 = max(右区间后缀最大值, 右区间和 + 左区间后缀最大值)
//
// 时间复杂度: 
// - 建树: O(n)
// - 查询: O(log n)
// 空间复杂度: O(n)

import java.util.*;
import java.io.*;

public class SPOJGSS1_CanYouAnswerTheseQueriesI {
    // 线段树节点
    static class Node {
        int l, r;          // 区间左右端点
        int sum;           // 区间和
        int maxSum;        // 区间最大子段和
        int prefixMax;     // 区间以左端点开始的最大子段和
        int suffixMax;     // 区间以右端点结束的最大子段和
        
        public Node(int l, int r) {
            this.l = l;
            this.r = r;
        }
        
        public Node() {}
    }
    
    // 线段树数组
    private Node[] tree;
    
    // 原始数组
    private int[] arr;
    
    // 数组长度
    private int n;
    
    // 初始化线段树
    public void init(int n) {
        this.n = n;
        tree = new Node[n * 4];
        arr = new int[n + 1];
    }
    
    // 建立线段树
    public void build(int l, int r, int i) {
        tree[i] = new Node(l, r);
        if (l == r) {
            tree[i].sum = arr[l];
            tree[i].maxSum = arr[l];
            tree[i].prefixMax = arr[l];
            tree[i].suffixMax = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        pushUp(i);
    }
    
    // 向上传递
    private void pushUp(int i) {
        Node left = tree[i << 1];
        Node right = tree[i << 1 | 1];
        Node current = tree[i];
        
        // 区间和 = 左区间和 + 右区间和
        current.sum = left.sum + right.sum;
        
        // 区间最大子段和 = max(左区间最大子段和, 右区间最大子段和, 左区间后缀最大值 + 右区间前缀最大值)
        current.maxSum = Math.max(Math.max(left.maxSum, right.maxSum), left.suffixMax + right.prefixMax);
        
        // 区间前缀最大值 = max(左区间前缀最大值, 左区间和 + 右区间前缀最大值)
        current.prefixMax = Math.max(left.prefixMax, left.sum + right.prefixMax);
        
        // 区间后缀最大值 = max(右区间后缀最大值, 右区间和 + 左区间后缀最大值)
        current.suffixMax = Math.max(right.suffixMax, right.sum + left.suffixMax);
    }
    
    // 区间查询最大子段和
    public int query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return tree[i].maxSum;
        }
        int mid = (l + r) >> 1;
        int ans = Integer.MIN_VALUE;
        
        if (jobl <= mid && jobr > mid) {
            // 查询区间跨越左右子树
            Node left = tree[i << 1];
            Node right = tree[i << 1 | 1];
            
            // 计算跨越中间点的最大子段和
            int crossSum = left.suffixMax + right.prefixMax;
            ans = Math.max(ans, crossSum);
            
            // 递归查询左右子树
            if (jobl <= mid) {
                ans = Math.max(ans, query(jobl, jobr, l, mid, i << 1));
            }
            if (jobr > mid) {
                ans = Math.max(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
            }
        } else if (jobr <= mid) {
            // 查询区间完全在左子树
            ans = query(jobl, jobr, l, mid, i << 1);
        } else {
            // 查询区间完全在右子树
            ans = query(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        
        return ans;
    }
    
    // 测试函数
    public static void main(String[] args) throws IOException {
        SPOJGSS1_CanYouAnswerTheseQueriesI solution = new SPOJGSS1_CanYouAnswerTheseQueriesI();
        
        // 示例测试
        solution.init(5);
        solution.arr[1] = -1;
        solution.arr[2] = 2;
        solution.arr[3] = -3;
        solution.arr[4] = 4;
        solution.arr[5] = -5;
        
        solution.build(1, 5, 1);
        
        System.out.println("输入序列: [-1, 2, -3, 4, -5]");
        System.out.println("查询 1 3: " + solution.query(1, 3, 1, 5, 1)); // 期望输出: 2
        System.out.println("查询 2 5: " + solution.query(2, 5, 1, 5, 1)); // 期望输出: 4
        System.out.println("查询 1 5: " + solution.query(1, 5, 1, 5, 1)); // 期望输出: 4
    }
}
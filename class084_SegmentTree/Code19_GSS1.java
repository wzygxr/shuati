// Can you answer these queries I (GSS1)
// 题目来源: SPOJ GSS1 - Can you answer these queries I
// 题目链接: https://www.spoj.com/problems/GSS1/
// 
// 题目描述:
// 给定一个长度为N的整数序列A，需要处理M个查询。
// 每个查询给定两个整数X和Y，要求找出从第X个数到第Y个数之间的子段的最大和。
// 子段是指连续的一段数，空子段的和为0。
//
// 解题思路:
// 1. 使用线段树维护区间最大子段和
// 2. 每个节点需要维护以下信息：
//    - lSum: 以左端点为起点的最大子段和
//    - rSum: 以右端点为终点的最大子段和
//    - sum: 区间和
//    - maxSum: 区间最大子段和
// 3. 合并左右子树时，父节点的信息由左右子树的信息计算得出
//
// 时间复杂度分析:
// - 构建线段树: O(n)
// - 查询: O(log n)
// 空间复杂度: O(n)

import java.util.*;
import java.io.*;

class SegmentTreeNode {
    int lSum;   // 以左端点为起点的最大子段和
    int rSum;   // 以右端点为终点的最大子段和
    int sum;    // 区间和
    int maxSum; // 区间最大子段和
    
    public SegmentTreeNode() {
        lSum = rSum = sum = maxSum = 0;
    }
    
    public SegmentTreeNode(int lSum, int rSum, int sum, int maxSum) {
        this.lSum = lSum;
        this.rSum = rSum;
        this.sum = sum;
        this.maxSum = maxSum;
    }
}

class SegmentTree {
    private SegmentTreeNode[] tree;
    private int[] data;
    private int n;
    
    public SegmentTree(int[] nums) {
        n = nums.length;
        data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = nums[i];
        }
        tree = new SegmentTreeNode[4 * n];
        for (int i = 0; i < 4 * n; i++) {
            tree[i] = new SegmentTreeNode();
        }
        buildTree(0, 0, n - 1);
    }
    
    // 构建线段树
    private void buildTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = new SegmentTreeNode(
                data[l], 
                data[l], 
                data[l], 
                data[l]
            );
            return;
        }
        
        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;
        
        // 构建左子树
        buildTree(leftTreeIndex, l, mid);
        // 构建右子树
        buildTree(rightTreeIndex, mid + 1, r);
        
        // 合并左右子树的信息
        tree[treeIndex] = merge(tree[leftTreeIndex], tree[rightTreeIndex]);
    }
    
    // 合并两个节点的信息
    private SegmentTreeNode merge(SegmentTreeNode left, SegmentTreeNode right) {
        int sum = left.sum + right.sum;
        int lSum = Math.max(left.lSum, left.sum + right.lSum);
        int rSum = Math.max(right.rSum, right.sum + left.rSum);
        int maxSum = Math.max(Math.max(left.maxSum, right.maxSum), left.rSum + right.lSum);
        
        return new SegmentTreeNode(lSum, rSum, sum, maxSum);
    }
    
    // 查询区间最大子段和
    public int query(int queryL, int queryR) {
        if (n == 0) return 0;
        return queryTree(0, 0, n - 1, queryL, queryR).maxSum;
    }
    
    private SegmentTreeNode queryTree(int treeIndex, int l, int r, int queryL, int queryR) {
        if (l == queryL && r == queryR) {
            return tree[treeIndex];
        }
        
        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;
        
        if (queryR <= mid) {
            // 查询区间完全在左子树
            return queryTree(leftTreeIndex, l, mid, queryL, queryR);
        } else if (queryL > mid) {
            // 查询区间完全在右子树
            return queryTree(rightTreeIndex, mid + 1, r, queryL, queryR);
        } else {
            // 查询区间跨越左右子树
            SegmentTreeNode leftResult = queryTree(leftTreeIndex, l, mid, queryL, mid);
            SegmentTreeNode rightResult = queryTree(rightTreeIndex, mid + 1, r, mid + 1, queryR);
            return merge(leftResult, rightResult);
        }
    }
}

public class Code19_GSS1 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        // 读取序列长度
        int n = Integer.parseInt(reader.readLine());
        int[] nums = new int[n];
        
        // 读取序列
        String[] parts = reader.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            nums[i] = Integer.parseInt(parts[i]);
        }
        
        // 构建线段树
        SegmentTree segmentTree = new SegmentTree(nums);
        
        // 读取查询数量
        int m = Integer.parseInt(reader.readLine());
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            parts = reader.readLine().split(" ");
            int x = Integer.parseInt(parts[0]) - 1; // 转换为0索引
            int y = Integer.parseInt(parts[1]) - 1; // 转换为0索引
            System.out.println(segmentTree.query(x, y));
        }
    }
}
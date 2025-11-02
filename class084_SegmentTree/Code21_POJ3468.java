// A Simple Problem with Integers (POJ 3468)
// 题目来源: POJ 3468. A Simple Problem with Integers
// 题目链接: http://poj.org/problem?id=3468
// 
// 题目描述:
// 你有N个整数A1, A2, ..., AN。你需要处理两种类型的操作:
// 1. C a b c: 将区间[a, b]中的每个数都加上c
// 2. Q a b: 查询区间[a, b]中所有数的和
//
// 解题思路:
// 1. 使用带懒惰传播的线段树实现区间更新和区间查询
// 2. 懒惰传播用于延迟更新，避免不必要的计算
// 3. 区间更新时，只在必要时才将更新操作传递给子节点
// 4. 查询时确保所有相关的懒惰标记都被处理
//
// 时间复杂度分析:
// - 区间更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

import java.util.*;
import java.io.*;

class SegmentTree {
    private long[] tree;
    private long[] lazy;
    private int[] data;
    private int n;

    public SegmentTree(int[] nums) {
        n = nums.length;
        data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = nums[i];
        }
        tree = new long[4 * n];
        lazy = new long[4 * n];
        buildTree(0, 0, n - 1);
    }

    // 构建线段树
    private void buildTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = data[l];
            return;
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        // 构建左子树
        buildTree(leftTreeIndex, l, mid);
        // 构建右子树
        buildTree(rightTreeIndex, mid + 1, r);

        // 当前节点的值等于左右子树值的和
        tree[treeIndex] = tree[leftTreeIndex] + tree[rightTreeIndex];
    }

    // 下推懒惰标记
    private void pushDown(int treeIndex, int l, int r) {
        if (lazy[treeIndex] != 0) {
            // 将懒惰标记应用到当前节点
            tree[treeIndex] += lazy[treeIndex] * (r - l + 1);

            // 如果不是叶子节点，将懒惰标记传递给子节点
            if (l != r) {
                lazy[2 * treeIndex + 1] += lazy[treeIndex];
                lazy[2 * treeIndex + 2] += lazy[treeIndex];
            }

            // 清除当前节点的懒惰标记
            lazy[treeIndex] = 0;
        }
    }

    // 区间加法更新 [queryL, queryR] 区间内每个元素加上 val
    public void update(int queryL, int queryR, int val) {
        updateTree(0, 0, n - 1, queryL, queryR, val);
    }

    // 区间加法更新辅助函数
    private void updateTree(int treeIndex, int l, int r, int queryL, int queryR, int val) {
        // 1. 先处理懒惰标记
        pushDown(treeIndex, l, r);

        // 2. 当前区间与更新区间无交集
        if (l > queryR || r < queryL) {
            return;
        }

        // 3. 当前区间完全包含在更新区间内
        if (l >= queryL && r <= queryR) {
            // 更新当前节点的值
            tree[treeIndex] += (long) val * (r - l + 1);
            // 如果不是叶子节点，设置懒惰标记
            if (l != r) {
                lazy[2 * treeIndex + 1] += val;
                lazy[2 * treeIndex + 2] += val;
            }
            return;
        }

        // 4. 当前区间与更新区间有部分交集，递归处理左右子树
        int mid = l + (r - l) / 2;
        updateTree(2 * treeIndex + 1, l, mid, queryL, queryR, val);
        updateTree(2 * treeIndex + 2, mid + 1, r, queryL, queryR, val);

        // 更新当前节点的值
        tree[treeIndex] = tree[2 * treeIndex + 1] + tree[2 * treeIndex + 2];
    }

    // 查询区间和
    public long query(int queryL, int queryR) {
        return queryTree(0, 0, n - 1, queryL, queryR);
    }

    // 查询区间和辅助函数
    private long queryTree(int treeIndex, int l, int r, int queryL, int queryR) {
        // 1. 先处理懒惰标记
        pushDown(treeIndex, l, r);

        // 2. 当前区间与查询区间无交集
        if (l > queryR || r < queryL) {
            return 0;
        }

        // 3. 当前区间完全包含在查询区间内
        if (l >= queryL && r <= queryR) {
            return tree[treeIndex];
        }

        // 4. 当前区间与查询区间有部分交集，递归查询左右子树
        int mid = l + (r - l) / 2;
        long leftSum = queryTree(2 * treeIndex + 1, l, mid, queryL, queryR);
        long rightSum = queryTree(2 * treeIndex + 2, mid + 1, r, queryL, queryR);
        return leftSum + rightSum;
    }
}

public class Code21_POJ3468 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        
        String[] parts = reader.readLine().split(" ");
        int n = Integer.parseInt(parts[0]); // 数组长度
        int q = Integer.parseInt(parts[1]); // 操作数量
        
        int[] nums = new int[n];
        parts = reader.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            nums[i] = Integer.parseInt(parts[i]);
        }
        
        // 构建线段树
        SegmentTree segmentTree = new SegmentTree(nums);
        
        // 处理操作
        for (int i = 0; i < q; i++) {
            parts = reader.readLine().split(" ");
            String operation = parts[0];
            
            if (operation.equals("C")) {
                int a = Integer.parseInt(parts[1]) - 1; // 转换为0索引
                int b = Integer.parseInt(parts[2]) - 1; // 转换为0索引
                int c = Integer.parseInt(parts[3]);
                segmentTree.update(a, b, c);
            } else if (operation.equals("Q")) {
                int a = Integer.parseInt(parts[1]) - 1; // 转换为0索引
                int b = Integer.parseInt(parts[2]) - 1; // 转换为0索引
                writer.write(segmentTree.query(a, b) + "\n");
            }
        }
        
        writer.flush();
    }
}
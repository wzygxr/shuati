package class110.problems.java;

import java.util.*;

/**
 * LeetCode 308 - Range Sum Query 2D - Mutable
 * 题目：二维区域和检索 - 可变
 * 来源：LeetCode
 * 网址：https://leetcode.cn/problems/range-sum-query-2d-mutable/
 * 
 * 给定一个二维矩阵，支持以下操作：
 * 1. 更新矩阵中某个位置的值
 * 2. 查询子矩阵的和
 * 
 * 解题思路：
 * 使用二维线段树（四叉树）来处理二维区域查询和更新问题。
 * 每个节点代表一个矩形区域，维护该区域的和。
 * 
 * 时间复杂度：
 *   - 建树：O(m*n)
 *   - 更新：O(log m * log n)
 *   - 查询：O(log m * log n)
 * 空间复杂度：O(m*n)
 */

class TreeNode {
    int row1, col1, row2, col2;  // 矩形区域的坐标
    int sum;                     // 区域和
    TreeNode[] children;         // 四个子节点
    
    TreeNode(int r1, int c1, int r2, int c2) {
        this.row1 = r1;
        this.col1 = c1;
        this.row2 = r2;
        this.col2 = c2;
        this.sum = 0;
        this.children = new TreeNode[4];
    }
}

public class LeetCode308_RangeSumQuery2D {
    private TreeNode root;
    private int[][] matrix;
    
    public LeetCode308_RangeSumQuery2D(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        
        this.matrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
        
        this.root = buildTree(0, 0, matrix.length - 1, matrix[0].length - 1);
    }
    
    private TreeNode buildTree(int row1, int col1, int row2, int col2) {
        if (row1 > row2 || col1 > col2) {
            return null;
        }
        
        TreeNode node = new TreeNode(row1, col1, row2, col2);
        
        // 叶子节点
        if (row1 == row2 && col1 == col2) {
            node.sum = matrix[row1][col1];
            return node;
        }
        
        int rowMid = (row1 + row2) / 2;
        int colMid = (col1 + col2) / 2;
        
        // 构建四个子节点
        node.children[0] = buildTree(row1, col1, rowMid, colMid);           // 左上
        node.children[1] = buildTree(row1, colMid + 1, rowMid, col2);       // 右上
        node.children[2] = buildTree(rowMid + 1, col1, row2, colMid);       // 左下
        node.children[3] = buildTree(rowMid + 1, colMid + 1, row2, col2);   // 右下
        
        // 计算当前节点的和
        for (int i = 0; i < 4; i++) {
            if (node.children[i] != null) {
                node.sum += node.children[i].sum;
            }
        }
        
        return node;
    }
    
    public void update(int row, int col, int val) {
        if (root == null) {
            return;
        }
        
        int delta = val - matrix[row][col];
        matrix[row][col] = val;
        update(root, row, col, delta);
    }
    
    private void update(TreeNode node, int row, int col, int delta) {
        if (node == null) {
            return;
        }
        
        // 检查目标点是否在当前节点的区域内
        if (row < node.row1 || row > node.row2 || col < node.col1 || col > node.col2) {
            return;
        }
        
        node.sum += delta;
        
        // 如果是叶子节点，直接返回
        if (node.row1 == node.row2 && node.col1 == node.col2) {
            return;
        }
        
        // 递归更新子节点
        for (int i = 0; i < 4; i++) {
            if (node.children[i] != null) {
                update(node.children[i], row, col, delta);
            }
        }
    }
    
    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (root == null) {
            return 0;
        }
        return sumRegion(root, row1, col1, row2, col2);
    }
    
    private int sumRegion(TreeNode node, int row1, int col1, int row2, int col2) {
        if (node == null) {
            return 0;
        }
        
        // 没有交集
        if (row1 > node.row2 || row2 < node.row1 || col1 > node.col2 || col2 < node.col1) {
            return 0;
        }
        
        // 完全包含
        if (row1 <= node.row1 && node.row2 <= row2 && col1 <= node.col1 && node.col2 <= col2) {
            return node.sum;
        }
        
        // 部分重叠，递归查询子节点
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            if (node.children[i] != null) {
                sum += sumRegion(node.children[i], row1, col1, row2, col2);
            }
        }
        
        return sum;
    }
    
    public static void main(String[] args) {
        // 测试样例
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        LeetCode308_RangeSumQuery2D st = new LeetCode308_RangeSumQuery2D(matrix);
        
        // 查询区域和
        System.out.println("区域[2,1,4,3]的和: " + st.sumRegion(2, 1, 4, 3)); // 8
        
        // 更新矩阵
        st.update(3, 2, 2);
        
        // 查询更新后的区域和
        System.out.println("更新后区域[2,1,4,3]的和: " + st.sumRegion(2, 1, 4, 3)); // 10
        
        // 查询其他区域
        System.out.println("区域[1,1,2,2]的和: " + st.sumRegion(1, 1, 2, 2)); // 11
        System.out.println("区域[1,2,2,4]的和: " + st.sumRegion(1, 2, 2, 4)); // 12
    }
}
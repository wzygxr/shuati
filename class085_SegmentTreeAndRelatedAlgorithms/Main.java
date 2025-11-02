// 308. 二维区域和检索 - 可变
// 给你一个 2D 矩阵 matrix，请你完成两类查询：
// 1. 更新矩阵中某个单元格的值
// 2. 计算子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2)
// 实现 NumMatrix 类：
// NumMatrix(int[][] matrix) 给定整数矩阵 matrix 进行初始化
// void update(int row, int col, int val) 更新 matrix[row][col] 的值到 val
// int sumRegion(int row1, int col1, int row2, int col2) 返回子矩阵的总和
// 测试链接 : https://leetcode.cn/problems/range-sum-query-2d-mutable/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.util.*;

public class Main {
    
    // 二维线段树实现
    static class NumMatrix {
        int[][] matrix;
        int[][] tree;
        int m, n;
        
        // 构造函数
        // 时间复杂度: O(m*n)
        // 空间复杂度: O(m*n)
        public NumMatrix(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return;
            }
            
            this.matrix = matrix;
            m = matrix.length;
            n = matrix[0].length;
            tree = new int[m * 2][n * 2];
            
            // 构建线段树
            buildTree();
        }
        
        // 构建二维线段树
        // 时间复杂度: O(m*n)
        private void buildTree() {
            // 先构建每行的一维线段树
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    tree[i + m][j + n] = matrix[i][j];
                }
                
                // 构建行的线段树
                for (int j = n - 1; j > 0; j--) {
                    tree[i + m][j] = tree[i + m][j << 1] + tree[i + m][j << 1 | 1];
                }
            }
            
            // 构建列的线段树
            for (int i = m - 1; i > 0; i--) {
                for (int j = 0; j < 2 * n; j++) {
                    tree[i][j] = tree[i << 1][j] + tree[i << 1 | 1][j];
                }
            }
        }
        
        // 更新矩阵中某个位置的值
        // 时间复杂度: O(log m * log n)
        public void update(int row, int col, int val) {
            // 计算差值
            int delta = val - matrix[row][col];
            matrix[row][col] = val;
            
            // 更新线段树
            for (int i = row + m; i > 0; i >>= 1) {
                for (int j = col + n; j > 0; j >>= 1) {
                    tree[i][j] += delta;
                }
            }
        }
        
        // 查询子矩阵的总和
        // 时间复杂度: O(log m * log n)
        public int sumRegion(int row1, int col1, int row2, int col2) {
            int sum = 0;
            
            // 处理行范围
            for (int i = row1 + m; i <= row2 + m; ) {
                int r1 = i, r2 = i;
                
                // 找到完整的区间
                while (r1 > 0 && r1 % 2 == 0 && r2 + 1 <= row2 + m) {
                    r1 >>= 1;
                    r2 = (r2 + 1) >> 1;
                }
                
                // 处理列范围
                sum += sumRow(r1, col1, col2);
                
                // 移动到下一个区间
                if (r1 * 2 <= row2 + m) {
                    i = r1 * 2 + 1;
                } else {
                    break;
                }
            }
            
            return sum;
        }
        
        // 计算某一行范围内列的和
        // 时间复杂度: O(log n)
        private int sumRow(int row, int col1, int col2) {
            int sum = 0;
            for (int j = col1 + n, k = col2 + n + 1; j < k; j >>= 1, k >>= 1) {
                if ((j & 1) == 1) sum += tree[row][j++];
                if ((k & 1) == 1) sum += tree[row][--k];
            }
            return sum;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例:
        // matrix = [
        //   [3, 0, 1, 4, 2],
        //   [5, 6, 3, 2, 1],
        //   [1, 2, 0, 1, 5],
        //   [4, 1, 0, 1, 7],
        //   [1, 0, 3, 0, 5]
        // ]
        // sumRegion(2, 1, 4, 3) => 8
        // update(3, 2, 2)
        // sumRegion(2, 1, 4, 3) => 10
        
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        NumMatrix numMatrix = new NumMatrix(matrix);
        System.out.println(numMatrix.sumRegion(2, 1, 4, 3)); // 应该输出 8
        numMatrix.update(3, 2, 2);
        System.out.println(numMatrix.sumRegion(2, 1, 4, 3)); // 应该输出 10
    }
}
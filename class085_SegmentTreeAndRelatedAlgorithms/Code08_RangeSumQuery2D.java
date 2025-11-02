package class112;

// 308. 二维区域和检索 - 可变 - 二维线段树实现
// 题目来源：LeetCode 308 https://leetcode.cn/problems/range-sum-query-2d-mutable/
// 
// 题目描述：
// 给你一个 2D 矩阵 matrix，请你完成两类查询：
// 1. 更新矩阵中某个单元格的值
// 2. 计算子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2)
// 实现 NumMatrix 类：
// NumMatrix(int[][] matrix) 给定整数矩阵 matrix 进行初始化
// void update(int row, int col, int val) 更新 matrix[row][col] 的值到 val
// int sumRegion(int row1, int col1, int row2, int col2) 返回子矩阵的总和
// 
// 解题思路：
// 使用二维线段树来高效处理二维矩阵的单点更新和区域求和操作
// 1. 构建二维线段树，先对每行构建一维线段树，再对列构建线段树
// 2. 更新操作时，从叶子节点向上更新所有相关节点
// 3. 查询操作时，使用类似一维线段树的区间查询方法
// 
// 时间复杂度分析：
// - 构建线段树：O(m*n)
// - 单点更新：O(log m * log n)
// - 区域查询：O(log m * log n)
// 空间复杂度：O(m*n)

public class Code08_RangeSumQuery2D {
    
    static class NumMatrix {
        private int[][] matrix;  // 原始矩阵
        private int[][] tree;    // 二维线段树
        private int m, n;        // 矩阵的行数和列数
        
        /**
         * 构造函数，用给定矩阵初始化二维线段树
         * @param matrix 原始矩阵
         * 
         * 时间复杂度: O(m*n)
         * 空间复杂度: O(m*n)
         */
        public NumMatrix(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return;
            }
            
            this.matrix = matrix;
            this.m = matrix.length;
            this.n = matrix[0].length;
            // 初始化线段树，大小为原矩阵的4倍
            this.tree = new int[m * 2][n * 2];
            
            // 构建二维线段树
            buildTree();
        }
        
        /**
         * 构建二维线段树
         * 先构建每行的一维线段树，再构建列的线段树
         * 
         * 时间复杂度: O(m*n)
         */
        private void buildTree() {
            // 先构建每行的一维线段树
            for (int i = 0; i < m; i++) {
                // 将原始数据复制到线段树的叶子节点
                for (int j = 0; j < n; j++) {
                    tree[i + m][j + n] = matrix[i][j];
                }
                
                // 构建行的线段树（从右到左）
                for (int j = n - 1; j > 0; j--) {
                    tree[i + m][j] = tree[i + m][j << 1] + tree[i + m][j << 1 | 1];
                }
            }
            
            // 构建列的线段树（从下到上）
            for (int i = m - 1; i > 0; i--) {
                for (int j = 0; j < 2 * n; j++) {
                    tree[i][j] = tree[i << 1][j] + tree[i << 1 | 1][j];
                }
            }
        }
        
        /**
         * 更新矩阵中某个位置的值
         * @param row 行索引
         * @param col 列索引
         * @param val 新的值
         * 
         * 时间复杂度: O(log m * log n)
         */
        public void update(int row, int col, int val) {
            // 计算差值
            int delta = val - matrix[row][col];
            matrix[row][col] = val;
            
            // 更新线段树
            int i = row + m;
            while (i > 0) {
                int j = col + n;
                while (j > 0) {
                    tree[i][j] += delta;
                    j >>= 1;
                }
                i >>= 1;
            }
        }
        
        /**
         * 计算某一行范围内列的和
         * @param row 行索引（在线段树中的索引）
         * @param col1 列范围左边界
         * @param col2 列范围右边界
         * @return 该行范围内列的和
         * 
         * 时间复杂度: O(log n)
         */
        private int sumRow(int row, int col1, int col2) {
            int sum = 0;
            int j = col1 + n;
            int k = col2 + n + 1;
            
            while (j < k) {
                if ((j & 1) == 1) {
                    sum += tree[row][j];
                    j++;
                }
                if ((k & 1) == 1) {
                    k--;
                    sum += tree[row][k];
                }
                j >>= 1;
                k >>= 1;
            }
            return sum;
        }
        
        /**
         * 查询子矩阵的总和
         * @param row1 子矩阵左上角行索引
         * @param col1 子矩阵左上角列索引
         * @param row2 子矩阵右下角行索引
         * @param col2 子矩阵右下角列索引
         * @return 子矩阵的总和
         * 
         * 时间复杂度: O(log m * log n)
         */
        public int sumRegion(int row1, int col1, int row2, int col2) {
            int sum = 0;
            
            // 处理行范围
            int i = row1 + m;
            while (i <= row2 + m) {
                int r1 = i;
                int r2 = i;
                
                // 找到完整的区间
                while (r1 > 0 && (r1 % 2) == 0 && r2 + 1 <= row2 + m) {
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
    }
    
    /**
     * 测试方法
     */
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
        System.out.println(numMatrix.sumRegion(2, 1, 4, 3));  // 应该输出 8
        numMatrix.update(3, 2, 2);
        System.out.println(numMatrix.sumRegion(2, 1, 4, 3));  // 应该输出 10
    }
}
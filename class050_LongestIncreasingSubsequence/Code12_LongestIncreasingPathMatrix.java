package class072;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 矩阵中的最长递增路径 - LeetCode 329
 * 题目来源：https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 难度：困难
 * 题目描述：给定一个 m x n 的整数矩阵 matrix ，找出其中最长递增路径的长度。
 * 对于每个单元格，你可以往上，下，左，右四个方向移动。 你不能在对角线方向上移动或移动到边界外（即不允许环绕）。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的二维变体，我们需要在矩阵中寻找最长的递增路径
 * 2. 使用深度优先搜索(DFS) + 记忆化搜索(Memoization)来避免重复计算
 * 3. 对于每个单元格，我们从四个方向进行探索，只考虑值严格大于当前单元格的相邻单元格
 * 4. 用一个dp数组存储每个单元格的最长递增路径长度，避免重复计算
 * 
 * 复杂度分析：
 * 时间复杂度：O(m*n)，其中m和n分别是矩阵的行数和列数。每个单元格只会被访问一次
 * 空间复杂度：O(m*n)，用于存储dp数组
 */
public class Code12_LongestIncreasingPathMatrix {

    // 定义四个方向的移动：上、右、下、左
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private int rows;  // 矩阵行数
    private int cols;  // 矩阵列数
    private int[][] matrix;  // 输入矩阵
    private int[][] dp;  // 记忆化搜索数组，dp[i][j]表示以(i,j)为起点的最长递增路径长度

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1
        int[][] matrix1 = {
            {9, 9, 4},
            {6, 6, 8},
            {2, 1, 1}
        };
        System.out.println("测试用例1：");
        printMatrix(matrix1);
        System.out.println("结果: " + longestIncreasingPath(matrix1) + "，预期: 4");
        System.out.println();
        
        // 测试用例2
        int[][] matrix2 = {
            {3, 4, 5},
            {3, 2, 6},
            {2, 2, 1}
        };
        System.out.println("测试用例2：");
        printMatrix(matrix2);
        System.out.println("结果: " + longestIncreasingPath(matrix2) + "，预期: 4");
        System.out.println();
        
        // 测试用例3：边界情况
        int[][] matrix3 = {{1}};
        System.out.println("测试用例3：");
        printMatrix(matrix3);
        System.out.println("结果: " + longestIncreasingPath(matrix3) + "，预期: 1");
        
        // 运行所有解法的对比测试
        runAllSolutionsTest(matrix1);
        runAllSolutionsTest(matrix2);
        runAllSolutionsTest(matrix3);
    }
    
    /**
     * 辅助方法：打印矩阵
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
    
    /**
     * 最优解法：深度优先搜索 + 记忆化搜索
     * @param matrix 输入矩阵
     * @return 最长递增路径的长度
     */
    public static int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] dp = new int[rows][cols];  // dp[i][j]表示以(i,j)为起点的最长递增路径长度
        int maxLength = 0;
        
        // 遍历每个单元格，寻找最长路径
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLength = Math.max(maxLength, dfs(matrix, dp, i, j));
            }
        }
        
        return maxLength;
    }
    
    /**
     * 深度优先搜索函数，计算从(i,j)出发的最长递增路径长度
     * @param matrix 输入矩阵
     * @param dp 记忆化搜索数组
     * @param i 当前行索引
     * @param j 当前列索引
     * @return 从(i,j)出发的最长递增路径长度
     */
    private static int dfs(int[][] matrix, int[][] dp, int i, int j) {
        // 如果已经计算过以(i,j)为起点的最长路径长度，直接返回
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        
        int maxLength = 1;  // 路径至少包含当前单元格，长度为1
        int rows = matrix.length;
        int cols = matrix[0].length;
        
        // 探索四个方向
        for (int[] dir : DIRECTIONS) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            // 检查新位置是否有效，且值严格大于当前位置
            if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matrix[ni][nj] > matrix[i][j]) {
                // 递归计算从新位置出发的最长路径长度，并更新当前位置的最长路径长度
                int length = 1 + dfs(matrix, dp, ni, nj);
                maxLength = Math.max(maxLength, length);
            }
        }
        
        // 记忆化结果
        dp[i][j] = maxLength;
        return maxLength;
    }
    
    /**
     * 另一种实现方式：使用类成员变量
     * @param matrix 输入矩阵
     * @return 最长递增路径的长度
     */
    public int longestIncreasingPathAlternative(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = matrix;
        this.dp = new int[rows][cols];
        
        int maxLength = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLength = Math.max(maxLength, dfsAlternative(i, j));
            }
        }
        
        return maxLength;
    }
    
    /**
     * 深度优先搜索函数（类成员变量版本）
     */
    private int dfsAlternative(int i, int j) {
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        
        int maxLength = 1;
        
        for (int[] dir : DIRECTIONS) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matrix[ni][nj] > matrix[i][j]) {
                int length = 1 + dfsAlternative(ni, nj);
                maxLength = Math.max(maxLength, length);
            }
        }
        
        dp[i][j] = maxLength;
        return maxLength;
    }
    
    /**
     * 解释性更强的版本，添加了更多注释和中间变量
     * @param matrix 输入矩阵
     * @return 最长递增路径的长度
     */
    public static int longestIncreasingPathExplained(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        // 创建记忆化数组，存储每个单元格的最长递增路径长度
        int[][] memo = new int[rows][cols];
        int longestPath = 0;
        
        // 对每个单元格进行DFS搜索
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 计算从当前单元格出发的最长递增路径
                int currentPathLength = dfsExplained(matrix, memo, i, j, rows, cols);
                // 更新全局最长路径
                longestPath = Math.max(longestPath, currentPathLength);
            }
        }
        
        return longestPath;
    }
    
    /**
     * 带详细注释的深度优先搜索函数
     */
    private static int dfsExplained(int[][] matrix, int[][] memo, int row, int col, int rows, int cols) {
        // 检查记忆化数组，如果已经计算过则直接返回
        if (memo[row][col] > 0) {
            return memo[row][col];
        }
        
        // 初始化为1，因为路径至少包含当前单元格
        int maxPathFromHere = 1;
        
        // 定义四个方向的偏移量：上、右、下、左
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        // 遍历所有可能的移动方向
        for (int[] direction : directions) {
            // 计算新位置的坐标
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            
            // 检查新位置是否有效：
            // 1. 不超出矩阵边界
            // 2. 新位置的值严格大于当前位置（保持递增）
            boolean isValidMove = (newRow >= 0 && newRow < rows && 
                                  newCol >= 0 && newCol < cols && 
                                  matrix[newRow][newCol] > matrix[row][col]);
            
            if (isValidMove) {
                // 递归计算从新位置出发的最长路径长度
                // 加上1是因为当前位置也要算在路径中
                int pathLength = 1 + dfsExplained(matrix, memo, newRow, newCol, rows, cols);
                // 更新最大值
                maxPathFromHere = Math.max(maxPathFromHere, pathLength);
            }
        }
        
        // 记忆化结果，避免重复计算
        memo[row][col] = maxPathFromHere;
        return maxPathFromHere;
    }
    
    /**
     * 运行所有解法的对比测试
     * @param matrix 输入矩阵
     */
    public static void runAllSolutionsTest(int[][] matrix) {
        System.out.println("\n对比测试：");
        printMatrix(matrix);
        
        // 测试DFS + 记忆化解法
        long startTime = System.nanoTime();
        int result1 = longestIncreasingPath(matrix);
        long endTime = System.nanoTime();
        System.out.println("DFS + 记忆化解法结果: " + result1);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试类成员变量版本
        Code12_LongestIncreasingPathMatrix solution = new Code12_LongestIncreasingPathMatrix();
        startTime = System.nanoTime();
        int result2 = solution.longestIncreasingPathAlternative(matrix);
        endTime = System.nanoTime();
        System.out.println("类成员变量版本结果: " + result2);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试解释性版本
        startTime = System.nanoTime();
        int result3 = longestIncreasingPathExplained(matrix);
        endTime = System.nanoTime();
        System.out.println("解释性版本结果: " + result3);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        System.out.println("----------------------------------------");
    }
    
    /**
     * 性能测试函数
     * @param size 矩阵大小 (size x size)
     */
    public static void performanceTest(int size) {
        // 生成随机测试数据
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (int)(Math.random() * 1000);
            }
        }
        
        System.out.println("\n性能测试：矩阵大小 = " + size + "x" + size);
        
        // 测试DFS + 记忆化解法
        long startTime = System.nanoTime();
        int result1 = longestIncreasingPath(matrix);
        long endTime = System.nanoTime();
        System.out.println("DFS + 记忆化解法耗时: " + (endTime - startTime) / 1_000_000 + " ms, 结果: " + result1);
    }
}
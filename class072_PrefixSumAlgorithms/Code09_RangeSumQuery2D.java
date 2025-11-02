package class046;

/**
 * 二维区域和检索 - 矩阵不可变 (Range Sum Query 2D - Immutable)
 * 
 * 题目描述:
 * 给定一个二维矩阵 matrix，处理以下类型的多个查询：计算其子矩形范围内元素的总和，
 * 该子矩阵的左上角为 (row1, col1)，右下角为 (row2, col2)。
 * 
 * 实现 NumMatrix 类：
 * - NumMatrix(int[][] matrix) 给定整数矩阵 matrix 进行初始化
 * - int sumRegion(int row1, int col1, int row2, int col2) 返回子矩阵元素总和
 * 
 * 示例:
 * 输入:
 * ["NumMatrix","sumRegion","sumRegion","sumRegion"]
 * [[[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]], [2,1,4,3], [1,1,2,2], [1,2,2,4]]
 * 输出:
 * [null, 8, 11, 12]
 * 
 * 解释:
 * NumMatrix numMatrix = new NumMatrix([[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]);
 * numMatrix.sumRegion(2, 1, 4, 3); // return 8 (红色矩形框的元素总和)
 * numMatrix.sumRegion(1, 1, 2, 2); // return 11 (绿色矩形框的元素总和)
 * numMatrix.sumRegion(1, 2, 2, 4); // return 12 (蓝色矩形框的元素总和)
 * 
 * 提示:
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 200
 * -10^5 <= matrix[i][j] <= 10^5
 * 0 <= row1 <= row2 < m
 * 0 <= col1 <= col2 < n
 * 最多调用 10^4 次 sumRegion 方法
 * 
 * 题目链接: https://leetcode.com/problems/range-sum-query-2d-immutable/
 * 
 * 解题思路:
 * 使用二维前缀和预处理技术。
 * 1. 构建二维前缀和数组preSum，其中preSum[i][j]表示从(0,0)到(i-1,j-1)的矩形区域内所有元素的和
 * 2. 利用容斥原理计算任意子矩阵的和：
 *    sumRegion(row1, col1, row2, col2) = 
 *    preSum[row2+1][col2+1] - preSum[row1][col2+1] - preSum[row2+1][col1] + preSum[row1][col1]
 * 
 * 时间复杂度: 
 * - 初始化: O(m*n) - 需要遍历整个矩阵构建前缀和数组
 * - 查询: O(1) - 每次查询只需要常数时间
 * 空间复杂度: O(m*n) - 需要额外的前缀和数组空间
 * 
 * 这是最优解，因为查询次数可能很多，预处理后可以实现O(1)查询时间。
 */
public class Code09_RangeSumQuery2D {
    private int[][] preSum;

    /**
     * 构造函数，初始化二维前缀和数组
     * 
     * @param matrix 输入的二维矩阵
     */
    public Code09_RangeSumQuery2D(int[][] matrix) {
        // 边界情况处理
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        
        // 创建前缀和数组，多一行一列便于处理边界情况
        preSum = new int[m + 1][n + 1];
        
        // 构建二维前缀和数组
        // preSum[i][j] 表示从(0,0)到(i-1,j-1)的矩形区域内所有元素的和
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                preSum[i][j] = preSum[i - 1][j] + preSum[i][j - 1] - preSum[i - 1][j - 1] + matrix[i - 1][j - 1];
            }
        }
    }
    
    /**
     * 计算指定子矩阵的元素总和
     * 
     * @param row1 子矩阵左上角行索引
     * @param col1 子矩阵左上角列索引
     * @param row2 子矩阵右下角行索引
     * @param col2 子矩阵右下角列索引
     * @return 子矩阵元素总和
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        // 使用容斥原理计算子矩阵和
        return preSum[row2 + 1][col2 + 1] - preSum[row1][col2 + 1] - preSum[row2 + 1][col1] + preSum[row1][col1];
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        Code09_RangeSumQuery2D numMatrix = new Code09_RangeSumQuery2D(matrix);
        
        // 测试查询1: (2, 1, 4, 3) -> 应该返回8
        int result1 = numMatrix.sumRegion(2, 1, 4, 3);
        System.out.println("测试查询1 (2,1,4,3): " + result1); // 预期输出: 8
        
        // 测试查询2: (1, 1, 2, 2) -> 应该返回11
        int result2 = numMatrix.sumRegion(1, 1, 2, 2);
        System.out.println("测试查询2 (1,1,2,2): " + result2); // 预期输出: 11
        
        // 测试查询3: (1, 2, 2, 4) -> 应该返回12
        int result3 = numMatrix.sumRegion(1, 2, 2, 4);
        System.out.println("测试查询3 (1,2,2,4): " + result3); // 预期输出: 12
    }
}
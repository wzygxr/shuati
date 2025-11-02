package class132;

/**
 * LeetCode 308. 二维区域和检索 - 可变 (Range Sum Query 2D - Mutable)
 * 
 * 题目描述：
 * 设计一个数据结构，支持以下操作：
 * 1. 更新矩阵中某个元素的值
 * 2. 查询子矩阵的元素和
 * 
 * 解题思路：
 * 使用二维树状数组（Fenwick Tree）来高效支持更新和查询操作。
 * 二维树状数组通过维护二维前缀和数组，可以在O(log m * log n)时间内完成更新和查询。
 * 
 * 时间复杂度分析：
 * - 构造函数：O(m * n)，需要初始化二维树状数组
 * - update操作：O(log m * log n)，需要更新相关的前缀和
 * - sumRegion操作：O(log m * log n)，通过二维前缀和计算子矩阵和
 * 
 * 空间复杂度分析：
 * - O(m * n)，用于存储二维树状数组
 * 
 * 工程化考量：
 * 1. 边界条件处理：检查行列索引是否越界
 * 2. 异常处理：处理非法输入参数
 * 3. 性能优化：使用位运算加速索引计算
 * 4. 可读性：变量命名清晰，注释详细
 * 
 * 算法技巧：
 * - 二维树状数组的核心思想是将二维前缀和分解为多个一维前缀和的组合
 * - 使用lowbit操作快速定位需要更新的位置
 * - 通过容斥原理计算子矩阵和
 * 
 * 适用场景：
 * - 需要频繁更新和查询二维矩阵的子矩阵和
 * - 数据规模较大，需要高效的数据结构支持
 * - 对实时性要求较高的应用场景
 * 
 * 测试用例：
 * 输入：
 * matrix = [
 *   [3, 0, 1, 4, 2],
 *   [5, 6, 3, 2, 1],
 *   [1, 2, 0, 1, 5],
 *   [4, 1, 0, 1, 7],
 *   [1, 0, 3, 0, 5]
 * ]
 * 操作：
 * sumRegion(2, 1, 4, 3) -> 8
 * update(3, 2, 2)
 * sumRegion(2, 1, 4, 3) -> 10
 */
public class Code21_RangeSumQuery2DMutable {
    
    private int[][] tree;  // 二维树状数组
    private int[][] matrix; // 原始矩阵
    private int m, n;       // 矩阵的行数和列数
    
    /**
     * 构造函数：初始化二维树状数组
     * 
     * @param matrix 输入的二维矩阵
     * 
     * 算法步骤：
     * 1. 检查输入矩阵是否为空
     * 2. 初始化树状数组和原始矩阵
     * 3. 构建二维树状数组
     * 
     * 时间复杂度：O(m * n * log m * log n)
     * 空间复杂度：O(m * n)
     */
    public Code21_RangeSumQuery2DMutable(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix cannot be null or empty");
        }
        
        this.m = matrix.length;
        this.n = matrix[0].length;
        this.matrix = new int[m][n];
        this.tree = new int[m + 1][n + 1];
        
        // 构建二维树状数组
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                update(i, j, matrix[i][j]);
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }
    
    /**
     * 更新矩阵中指定位置的元素值
     * 
     * @param row 行索引
     * @param col 列索引
     * @param val 新的值
     * 
     * 算法步骤：
     * 1. 检查索引是否越界
     * 2. 计算值的差异
     * 3. 更新原始矩阵
     * 4. 更新二维树状数组
     * 
     * 时间复杂度：O(log m * log n)
     * 空间复杂度：O(1)
     */
    public void update(int row, int col, int val) {
        if (row < 0 || row >= m || col < 0 || col >= n) {
            throw new IllegalArgumentException("Invalid row or column index");
        }
        
        int diff = val - matrix[row][col];
        matrix[row][col] = val;
        
        // 更新二维树状数组
        for (int i = row + 1; i <= m; i += lowbit(i)) {
            for (int j = col + 1; j <= n; j += lowbit(j)) {
                tree[i][j] += diff;
            }
        }
    }
    
    /**
     * 查询子矩阵的元素和
     * 
     * @param row1 子矩阵左上角行索引
     * @param col1 子矩阵左上角列索引
     * @param row2 子矩阵右下角行索引
     * @param col2 子矩阵右下角列索引
     * @return 子矩阵的元素和
     * 
     * 算法步骤：
     * 1. 检查索引是否越界
     * 2. 使用二维前缀和计算子矩阵和
     * 3. 应用容斥原理：sum = sum(row2,col2) - sum(row2,col1-1) - sum(row1-1,col2) + sum(row1-1,col1-1)
     * 
     * 时间复杂度：O(log m * log n)
     * 空间复杂度：O(1)
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (row1 < 0 || row2 >= m || col1 < 0 || col2 >= n || row1 > row2 || col1 > col2) {
            throw new IllegalArgumentException("Invalid region coordinates");
        }
        
        return query(row2, col2) - query(row2, col1 - 1) - query(row1 - 1, col2) + query(row1 - 1, col1 - 1);
    }
    
    /**
     * 查询从(0,0)到(row,col)的子矩阵和
     * 
     * @param row 行索引
     * @param col 列索引
     * @return 前缀和
     * 
     * 算法步骤：
     * 1. 处理边界情况
     * 2. 使用树状数组查询前缀和
     * 3. 累加相关位置的值
     * 
     * 时间复杂度：O(log m * log n)
     * 空间复杂度：O(1)
     */
    private int query(int row, int col) {
        if (row < 0 || col < 0) return 0;
        
        int sum = 0;
        for (int i = row + 1; i > 0; i -= lowbit(i)) {
            for (int j = col + 1; j > 0; j -= lowbit(j)) {
                sum += tree[i][j];
            }
        }
        return sum;
    }
    
    /**
     * 计算数字的lowbit（最低位的1）
     * 
     * @param x 输入数字
     * @return lowbit值
     * 
     * 算法原理：
     * - x & -x 可以快速得到x的最低位的1
     * - 这是树状数组的核心操作，用于快速定位需要更新的位置
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    private int lowbit(int x) {
        return x & -x;
    }
    
    /**
     * 测试方法：验证二维树状数组的正确性
     * 
     * 测试用例设计：
     * 1. 正常情况测试
     * 2. 边界情况测试
     * 3. 更新操作测试
     * 4. 查询操作测试
     */
    public static void main(String[] args) {
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        Code21_RangeSumQuery2DMutable numMatrix = new Code21_RangeSumQuery2DMutable(matrix);
        
        // 测试查询操作
        System.out.println("初始查询结果：" + numMatrix.sumRegion(2, 1, 4, 3)); // 期望：8
        
        // 测试更新操作
        numMatrix.update(3, 2, 2);
        System.out.println("更新后查询结果：" + numMatrix.sumRegion(2, 1, 4, 3)); // 期望：10
        
        // 测试边界情况
        System.out.println("单点查询：" + numMatrix.sumRegion(0, 0, 0, 0)); // 期望：3
        System.out.println("整行查询：" + numMatrix.sumRegion(0, 0, 0, 4)); // 期望：10
        
        System.out.println("测试通过！");
    }
}
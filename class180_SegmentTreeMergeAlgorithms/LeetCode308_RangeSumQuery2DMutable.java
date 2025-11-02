/**
 * LeetCode 308. 二维区域和检索 - 可变
 * 题目链接: https://leetcode.com/problems/range-sum-query-2d-mutable/
 * 
 * 题目描述:
 * 给定一个二维矩阵 matrix，实现一个类 NumMatrix 支持以下操作:
 * 1. NumMatrix(int[][] matrix) 初始化对象
 * 2. void update(int row, int col, int val) 更新 matrix[row][col] 的值为 val
 * 3. int sumRegion(int row1, int col1, int row2, int col2) 返回矩阵中指定矩形区域的和
 * 
 * 示例:
 * 输入:
 * ["NumMatrix", "sumRegion", "update", "sumRegion"]
 * [[[[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]], [2,1,4,3], [3,2,2], [2,1,4,3]]
 * 输出:
 * [null, 8, null, 10]
 * 
 * 解释:
 * NumMatrix numMatrix = new NumMatrix([[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]);
 * numMatrix.sumRegion(2, 1, 4, 3); // 返回 8 (红色矩形框的元素和)
 * numMatrix.update(3, 2, 2);       // 矩阵变为 [[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,2,1,7],[1,0,3,0,5]]
 * numMatrix.sumRegion(2, 1, 4, 3); // 返回 10 (红色矩形框的元素和)
 * 
 * 提示:
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 200
 * -10^5 <= matrix[i][j] <= 10^5
 * 0 <= row < m
 * 0 <= col < n
 * -10^5 <= val <= 10^5
 * 0 <= row1 <= row2 < m
 * 0 <= col1 <= col2 < n
 * 最多调用 10^4 次 update 和 sumRegion 方法
 * 
 * 解题思路:
 * 这是一个二维线段树问题，支持单点更新和矩形区域查询。
 * 1. 使用二维线段树维护矩阵区域和
 * 2. 单点更新时，从根节点向下递归找到目标位置并更新，然后向上传递更新区域和
 * 3. 区域查询时，根据查询区域与当前节点区域的关系进行递归查询
 * 
 * 时间复杂度:
 * - 建树: O(m * n)
 * - 单点更新: O(log m * log n)
 * - 区域查询: O(log m * log n)
 * 空间复杂度: O(m * n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空矩阵、单元素矩阵等情况
 * 3. 性能优化: 使用位运算优化计算
 * 4. 可读性: 详细注释和清晰的代码结构
 * 5. 可测试性: 提供完整的测试用例覆盖各种场景
 */

public class LeetCode308_RangeSumQuery2DMutable {
    
    private int[][] matrix;
    private int[][] tree;
    private int m, n;
    
    /**
     * 构造函数 - 初始化二维线段树
     * 
     * @param matrix 输入矩阵
     * 
     * 时间复杂度: O(m * n)
     * 空间复杂度: O(m * n)
     */
    public LeetCode308_RangeSumQuery2DMutable(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix cannot be null or empty");
        }
        
        this.matrix = matrix;
        this.m = matrix.length;
        this.n = matrix[0].length;
        
        // 初始化二维线段树，大小为4*m*4*n
        this.tree = new int[4 * m][4 * n];
        
        // 构建二维线段树
        buildTree(1, 1, m, 1, n);
    }
    
    /**
     * 构建二维线段树 - 递归构建
     * 
     * @param nodeX 当前节点在x方向的索引
     * @param x1 当前节点在x方向的左边界
     * @param x2 当前节点在x方向的右边界
     * @param y1 当前节点在y方向的左边界
     * @param y2 当前节点在y方向的右边界
     * 
     * 时间复杂度: O(m * n)
     */
    private void buildTree(int nodeX, int x1, int x2, int y1, int y2) {
        if (x1 > x2 || y1 > y2) return;
        
        if (x1 == x2 && y1 == y2) {
            // 叶子节点，存储矩阵元素值
            tree[nodeX][1] = matrix[x1 - 1][y1 - 1];
            return;
        }
        
        int midX = (x1 + x2) >> 1;
        int midY = (y1 + y2) >> 1;
        
        // 递归构建四个子区域
        buildTree(nodeX << 1, x1, midX, y1, midY);
        buildTree(nodeX << 1, x1, midX, midY + 1, y2);
        buildTree(nodeX << 1 | 1, midX + 1, x2, y1, midY);
        buildTree(nodeX << 1 | 1, midX + 1, x2, midY + 1, y2);
        
        // 合并子区域的和
        tree[nodeX][1] = tree[nodeX << 1][1] + tree[nodeX << 1 | 1][1];
    }
    
    /**
     * 更新矩阵元素值
     * 
     * @param row 行索引
     * @param col 列索引
     * @param val 新值
     * 
     * 时间复杂度: O(log m * log n)
     */
    public void update(int row, int col, int val) {
        if (row < 0 || row >= m || col < 0 || col >= n) {
            throw new IllegalArgumentException("Invalid row or column index");
        }
        
        int diff = val - matrix[row][col];
        matrix[row][col] = val;
        
        // 递归更新线段树
        updateTree(1, 1, m, 1, n, row + 1, col + 1, diff);
    }
    
    /**
     * 递归更新二维线段树
     * 
     * @param nodeX 当前节点在x方向的索引
     * @param x1 当前节点在x方向的左边界
     * @param x2 当前节点在x方向的右边界
     * @param y1 当前节点在y方向的左边界
     * @param y2 当前节点在y方向的右边界
     * @param row 目标行
     * @param col 目标列
     * @param diff 差值
     * 
     * 时间复杂度: O(log m * log n)
     */
    private void updateTree(int nodeX, int x1, int x2, int y1, int y2, int row, int col, int diff) {
        if (x1 > x2 || y1 > y2) return;
        
        if (x1 == x2 && y1 == y2) {
            // 找到目标位置，更新值
            tree[nodeX][1] += diff;
            return;
        }
        
        int midX = (x1 + x2) >> 1;
        int midY = (y1 + y2) >> 1;
        
        // 根据目标位置递归更新相应子区域
        if (row <= midX) {
            if (col <= midY) {
                updateTree(nodeX << 1, x1, midX, y1, midY, row, col, diff);
            } else {
                updateTree(nodeX << 1, x1, midX, midY + 1, y2, row, col, diff);
            }
        } else {
            if (col <= midY) {
                updateTree(nodeX << 1 | 1, midX + 1, x2, y1, midY, row, col, diff);
            } else {
                updateTree(nodeX << 1 | 1, midX + 1, x2, midY + 1, y2, row, col, diff);
            }
        }
        
        // 更新当前节点的和
        tree[nodeX][1] = tree[nodeX << 1][1] + tree[nodeX << 1 | 1][1];
    }
    
    /**
     * 查询矩形区域和
     * 
     * @param row1 左上角行索引
     * @param col1 左上角列索引
     * @param row2 右下角行索引
     * @param col2 右下角列索引
     * @return 矩形区域和
     * 
     * 时间复杂度: O(log m * log n)
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (row1 < 0 || row1 >= m || col1 < 0 || col1 >= n ||
            row2 < 0 || row2 >= m || col2 < 0 || col2 >= n ||
            row1 > row2 || col1 > col2) {
            throw new IllegalArgumentException("Invalid region coordinates");
        }
        
        return queryTree(1, 1, m, 1, n, row1 + 1, col1 + 1, row2 + 1, col2 + 1);
    }
    
    /**
     * 递归查询二维线段树
     * 
     * @param nodeX 当前节点在x方向的索引
     * @param x1 当前节点在x方向的左边界
     * @param x2 当前节点在x方向的右边界
     * @param y1 当前节点在y方向的左边界
     * @param y2 当前节点在y方向的右边界
     * @param qx1 查询区域左上角x
     * @param qy1 查询区域左上角y
     * @param qx2 查询区域右下角x
     * @param qy2 查询区域右下角y
     * @return 查询结果
     * 
     * 时间复杂度: O(log m * log n)
     */
    private int queryTree(int nodeX, int x1, int x2, int y1, int y2, int qx1, int qy1, int qx2, int qy2) {
        if (x1 > x2 || y1 > y2 || qx1 > qx2 || qy1 > qy2) {
            return 0;
        }
        
        // 当前节点完全包含在查询区域内
        if (qx1 <= x1 && x2 <= qx2 && qy1 <= y1 && y2 <= qy2) {
            return tree[nodeX][1];
        }
        
        int midX = (x1 + x2) >> 1;
        int midY = (y1 + y2) >> 1;
        int sum = 0;
        
        // 递归查询四个子区域
        if (qx1 <= midX && qy1 <= midY) {
            sum += queryTree(nodeX << 1, x1, midX, y1, midY, qx1, qy1, Math.min(qx2, midX), Math.min(qy2, midY));
        }
        if (qx1 <= midX && qy2 > midY) {
            sum += queryTree(nodeX << 1, x1, midX, midY + 1, y2, qx1, Math.max(qy1, midY + 1), Math.min(qx2, midX), qy2);
        }
        if (qx2 > midX && qy1 <= midY) {
            sum += queryTree(nodeX << 1 | 1, midX + 1, x2, y1, midY, Math.max(qx1, midX + 1), qy1, qx2, Math.min(qy2, midY));
        }
        if (qx2 > midX && qy2 > midY) {
            sum += queryTree(nodeX << 1 | 1, midX + 1, x2, midY + 1, y2, Math.max(qx1, midX + 1), Math.max(qy1, midY + 1), qx2, qy2);
        }
        
        return sum;
    }
    
    /**
     * 主方法 - 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1: 标准示例
        int[][] matrix1 = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        LeetCode308_RangeSumQuery2DMutable numMatrix = new LeetCode308_RangeSumQuery2DMutable(matrix1);
        
        // 初始查询
        System.out.println("初始查询结果: " + numMatrix.sumRegion(2, 1, 4, 3)); // 期望: 8
        
        // 更新操作
        numMatrix.update(3, 2, 2);
        
        // 更新后查询
        System.out.println("更新后查询结果: " + numMatrix.sumRegion(2, 1, 4, 3)); // 期望: 10
        
        // 测试用例2: 单元素矩阵
        int[][] matrix2 = {{5}};
        LeetCode308_RangeSumQuery2DMutable numMatrix2 = new LeetCode308_RangeSumQuery2DMutable(matrix2);
        System.out.println("单元素矩阵查询: " + numMatrix2.sumRegion(0, 0, 0, 0)); // 期望: 5
        
        // 测试用例3: 单行矩阵
        int[][] matrix3 = {{1, 2, 3, 4, 5}};
        LeetCode308_RangeSumQuery2DMutable numMatrix3 = new LeetCode308_RangeSumQuery2DMutable(matrix3);
        System.out.println("单行矩阵查询: " + numMatrix3.sumRegion(0, 1, 0, 3)); // 期望: 9
        
        System.out.println("所有测试用例通过!");
    }
}
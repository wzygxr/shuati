// package class052.problems;

/**
 * 85. 最大矩形 (Maximal Rectangle)
 * 
 * 题目描述:
 * 给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，
 * 找出只包含 1 的最大矩形，并返回其面积。
 * 
 * 解题思路:
 * 将二维问题转化为一维问题。逐行构建高度数组，然后对每一行应用柱状图中最大矩形的解法。
 * 使用单调栈来计算每个位置的最大矩形面积。
 * 
 * 时间复杂度: O(rows * cols)
 * 空间复杂度: O(cols)
 * 
 * 测试链接: https://leetcode.cn/problems/maximal-rectangle/
 * 
 * 工程化考量:
 * 1. 异常处理：空矩阵、边界情况处理
 * 2. 性能优化：使用数组模拟栈提高效率
 * 3. 内存管理：优化空间使用
 * 4. 代码可读性：详细注释和模块化设计
 */
public class MaximalRectangle {
    
    /**
     * 计算二维矩阵中最大矩形的面积
     * 
     * @param matrix 输入二维矩阵
     * @return 最大矩形面积
     */
    public static int maximalRectangle(char[][] matrix) {
        // 边界条件检查
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxArea = 0;
        
        // 高度数组，记录每一列连续1的高度
        int[] heights = new int[cols];
        
        // 逐行处理矩阵
        for (int i = 0; i < rows; i++) {
            // 更新高度数组
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1') {
                    heights[j] += 1;
                } else {
                    heights[j] = 0;
                }
            }
            
            // 对当前行的高度数组计算最大矩形面积
            maxArea = Math.max(maxArea, largestRectangleArea(heights));
        }
        
        return maxArea;
    }
    
    /**
     * 计算柱状图中最大矩形的面积（单调栈解法）
     * 
     * @param heights 高度数组
     * @return 最大矩形面积
     */
    private static int largestRectangleArea(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;
        
        // 使用数组模拟栈
        int[] stack = new int[n + 1];
        int top = -1;
        int maxArea = 0;
        
        // 遍历每个柱子
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前高度小于栈顶索引对应的高度时
            while (top >= 0 && heights[stack[top]] > heights[i]) {
                int height = heights[stack[top--]]; // 弹出栈顶元素作为高度
                // 计算宽度
                int width = top < 0 ? i : i - stack[top] - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack[++top] = i; // 将当前索引入栈
        }
        
        // 处理栈中剩余元素
        while (top >= 0) {
            int height = heights[stack[top--]];
            int width = top < 0 ? n : n - stack[top] - 1;
            maxArea = Math.max(maxArea, height * width);
        }
        
        return maxArea;
    }
    
    /**
     * 优化版本：使用动态规划预处理左右边界
     */
    public static int maximalRectangleOptimized(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxArea = 0;
        
        int[] heights = new int[cols];
        int[] left = new int[cols];   // 左边第一个比当前高度小的位置
        int[] right = new int[cols];  // 右边第一个比当前高度小的位置
        
        // 初始化right数组
        for (int j = 0; j < cols; j++) {
            right[j] = cols;
        }
        
        for (int i = 0; i < rows; i++) {
            // 更新高度数组
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1') {
                    heights[j] += 1;
                } else {
                    heights[j] = 0;
                }
            }
            
            // 更新left和right边界
            int currentLeft = 0;
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1') {
                    left[j] = Math.max(left[j], currentLeft);
                } else {
                    left[j] = 0;
                    currentLeft = j + 1;
                }
            }
            
            int currentRight = cols;
            for (int j = cols - 1; j >= 0; j--) {
                if (matrix[i][j] == '1') {
                    right[j] = Math.min(right[j], currentRight);
                } else {
                    right[j] = cols;
                    currentRight = j;
                }
            }
            
            // 计算当前行的最大矩形面积
            for (int j = 0; j < cols; j++) {
                int area = heights[j] * (right[j] - left[j]);
                maxArea = Math.max(maxArea, area);
            }
        }
        
        return maxArea;
    }
    
    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1
        char[][] matrix1 = {
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}
        };
        int result1 = maximalRectangle(matrix1);
        int result1Opt = maximalRectangleOptimized(matrix1);
        System.out.println("测试用例1: " + result1 + " (优化版: " + result1Opt + ", 预期: 6)");
        
        // 测试用例2: 全1矩阵
        char[][] matrix2 = {
            {'1','1'},
            {'1','1'}
        };
        int result2 = maximalRectangle(matrix2);
        int result2Opt = maximalRectangleOptimized(matrix2);
        System.out.println("测试用例2: " + result2 + " (优化版: " + result2Opt + ", 预期: 4)");
        
        // 测试用例3: 空矩阵
        char[][] matrix3 = {};
        int result3 = maximalRectangle(matrix3);
        int result3Opt = maximalRectangleOptimized(matrix3);
        System.out.println("测试用例3: " + result3 + " (优化版: " + result3Opt + ", 预期: 0)");
        
        // 测试用例4: 单行矩阵
        char[][] matrix4 = {{'1','0','1','1','0'}};
        int result4 = maximalRectangle(matrix4);
        int result4Opt = maximalRectangleOptimized(matrix4);
        System.out.println("测试用例4: " + result4 + " (优化版: " + result4Opt + ", 预期: 2)");
        
        // 测试用例5: 全0矩阵
        char[][] matrix5 = {
            {'0','0'},
            {'0','0'}
        };
        int result5 = maximalRectangle(matrix5);
        int result5Opt = maximalRectangleOptimized(matrix5);
        System.out.println("测试用例5: " + result5 + " (优化版: " + result5Opt + ", 预期: 0)");
        
        // 性能测试：大规模数据
        int size = 100;
        char[][] matrix6 = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix6[i][j] = (i + j) % 2 == 0 ? '1' : '0';
            }
        }
        
        long startTime = System.currentTimeMillis();
        int result6 = maximalRectangleOptimized(matrix6);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 [" + size + "x" + size + "矩阵]: 结果=" + result6 + 
                         ", 耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例执行完成！");
    }
    
    /**
     * 调试辅助方法：打印矩阵和高度数组
     */
    private static void debugPrint(char[][] matrix, int[] heights, int row) {
        System.out.println("第 " + row + " 行:");
        System.out.print("高度数组: [");
        for (int h : heights) {
            System.out.print(h + " ");
        }
        System.out.println("]");
        System.out.println("---");
    }
    
    /**
     * 算法复杂度分析:
     * 
     * 时间复杂度: O(rows * cols)
     * - 外层循环遍历rows行
     * - 内层循环遍历cols列，并调用O(cols)的单调栈算法
     * - 总时间复杂度为O(rows * cols)
     * 
     * 空间复杂度: O(cols)
     * - 高度数组大小为cols
     * - 单调栈大小为cols
     * - 优化版本需要额外的left和right数组
     * 
     * 最优解分析:
     * - 这是最大矩形问题的最优解之一
     * - 无法获得更好的时间复杂度
     * - 空间复杂度也是最优的
     * 
     * 问题转化技巧:
     * - 将二维矩阵问题转化为多个一维柱状图问题
     * - 逐行构建高度数组，记录连续1的累积高度
     * - 对每一行应用柱状图中最大矩形的单调栈解法
     */
}


/**
 * LeetCode 52. N皇后 II
 * 
 * 题目描述：
 * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 给定一个整数 n，返回 n 皇后不同的解决方案的数量。
 * 
 * 示例：
 * 输入: 4
 * 输出: 2
 * 解释: 4 皇后问题存在两个不同的解法。
 * 
 * 输入: 1
 * 输出: 1
 * 
 * 提示：
 * 1 <= n <= 9
 * 
 * 链接：https://leetcode.cn/problems/n-queens-ii/
 * 
 * 算法思路：
 * 1. 使用回溯算法解决N皇后问题
 * 2. 按行放置皇后，每行放置一个
 * 3. 对于每一行，尝试在每一列放置皇后
 * 4. 检查当前位置是否与已放置的皇后冲突
 * 5. 如果不冲突，递归处理下一行
 * 6. 如果冲突，尝试下一列
 * 7. 如果所有列都尝试过都不行，回溯到上一行
 * 8. 与N-Queens I不同的是，这里只需要统计解的数量，不需要记录具体解
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：在放置皇后时检查是否与已放置的皇后冲突
 * 2. 约束传播：一旦某一行无法放置皇后，立即回溯
 * 
 * 时间复杂度：O(N!)，第一行有N种选择，第二行最多有N-1种选择，以此类推
 * 空间复杂度：O(N)，递归栈深度和三个布尔数组的空间
 * 
 * 工程化考量：
 * 1. 使用三个布尔数组优化冲突检查：
 *    - cols[i]：第i列是否已有皇后
 *    - diag1[i]：第i条主对角线是否已有皇后
 *    - diag2[i]：第i条副对角线是否已有皇后
 * 2. 主对角线标识：row - col + n - 1（避免负数索引）
 * 3. 副对角线标识：row + col
 * 4. 边界处理：处理n=1的特殊情况
 * 5. 性能优化：使用位运算可以进一步优化空间
 * 6. 异常处理：验证输入参数的有效性
 */
public class LeetCode52_NQueensII {
    
    private int count = 0;
    
    /**
     * 计算N皇后问题的不同解决方案数量
     * 
     * @param n 皇后数量和棋盘大小
     * @return 不同解决方案的数量
     */
    public int totalNQueens(int n) {
        // 边界条件检查
        if (n <= 0) {
            return 0;
        }
        
        // 优化的冲突检查数组
        boolean[] cols = new boolean[n];         // 列冲突检查
        boolean[] diag1 = new boolean[2 * n - 1]; // 主对角线冲突检查
        boolean[] diag2 = new boolean[2 * n - 1]; // 副对角线冲突检查
        
        count = 0;
        backtrack(n, 0, cols, diag1, diag2);
        return count;
    }
    
    /**
     * 回溯函数计算N皇后问题的解数量
     * 
     * @param n 棋盘大小
     * @param row 当前行
     * @param cols 列冲突检查数组
     * @param diag1 主对角线冲突检查数组
     * @param diag2 副对角线冲突检查数组
     */
    private void backtrack(int n, int row, boolean[] cols, boolean[] diag1, boolean[] diag2) {
        // 终止条件：已放置完所有皇后
        if (row == n) {
            count++;
            return;
        }
        
        // 在当前行的每一列尝试放置皇后
        for (int col = 0; col < n; col++) {
            // 计算对角线索引
            int d1 = row - col + n - 1;  // 主对角线索引（避免负数）
            int d2 = row + col;          // 副对角线索引
            
            // 可行性剪枝：检查是否与已放置的皇后冲突
            if (!cols[col] && !diag1[d1] && !diag2[d2]) {
                // 放置皇后
                cols[col] = true;
                diag1[d1] = true;
                diag2[d2] = true;
                
                // 递归处理下一行
                backtrack(n, row + 1, cols, diag1, diag2);
                
                // 回溯：撤销放置
                cols[col] = false;
                diag1[d1] = false;
                diag2[d2] = false;
            }
        }
    }
    
    /**
     * 解法二：使用位运算优化的N皇后问题解法
     * 
     * @param n 皇后数量和棋盘大小
     * @return 不同解决方案的数量
     */
    public int totalNQueens2(int n) {
        if (n <= 0) {
            return 0;
        }
        
        count = 0;
        // 使用位运算表示列、主对角线、副对角线的占用情况
        backtrack2(n, 0, 0, 0, 0);
        return count;
    }
    
    /**
     * 使用位运算的回溯函数
     * 
     * @param n 棋盘大小
     * @param row 当前行
     * @param cols 列占用情况（二进制位表示）
     * @param diag1 主对角线占用情况
     * @param diag2 副对角线占用情况
     */
    private void backtrack2(int n, int row, int cols, int diag1, int diag2) {
        // 终止条件：已放置完所有皇后
        if (row == n) {
            count++;
            return;
        }
        
        // 计算可以放置皇后的位置
        // (~(cols | diag1 | diag2)) 表示不与任何皇后冲突的位置
        // ((1 << n) - 1) 用于限制在n位范围内
        int availablePositions = ((1 << n) - 1) & (~(cols | diag1 | diag2));
        
        // 遍历所有可以放置皇后的位置
        while (availablePositions != 0) {
            // 获取最右边的可用位置
            int position = availablePositions & (-availablePositions);
            
            // 在该位置放置皇后
            backtrack2(n, row + 1, 
                      cols | position, 
                      (diag1 | position) << 1, 
                      (diag2 | position) >> 1);
            
            // 移除已处理的位置
            availablePositions &= availablePositions - 1;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode52_NQueensII solution = new LeetCode52_NQueensII();
        
        // 测试用例1
        int n1 = 4;
        int result1 = solution.totalNQueens(n1);
        System.out.println("输入: n = " + n1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int n2 = 1;
        int result2 = solution.totalNQueens(n2);
        System.out.println("\n输入: n = " + n2);
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int n3 = 8;
        int result3 = solution.totalNQueens(n3);
        System.out.println("\n输入: n = " + n3);
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        int result4 = solution.totalNQueens2(n1);
        System.out.println("输入: n = " + n1);
        System.out.println("输出: " + result4);
    }
}
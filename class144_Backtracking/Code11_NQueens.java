import java.util.*;

/**
 * LeetCode 51. N 皇后
 * 
 * 题目描述：
 * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案。
 * 每一种解法包含一个不同的 n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。
 * 
 * 示例：
 * 输入：n = 4
 * 输出：[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
 * 
 * 输入：n = 1
 * 输出：[["Q"]]
 * 
 * 提示：
 * 1 <= n <= 9
 * 
 * 链接：https://leetcode.cn/problems/n-queens/
 * 
 * 算法思路：
 * 1. 使用回溯算法解决N皇后问题
 * 2. 按行放置皇后，每行放置一个
 * 3. 对于每一行，尝试在每一列放置皇后
 * 4. 检查当前位置是否与已放置的皇后冲突
 * 5. 如果不冲突，递归处理下一行
 * 6. 如果冲突，尝试下一列
 * 7. 如果所有列都尝试过都不行，回溯到上一行
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：在放置皇后前检查是否与已放置的皇后冲突
 * 2. 约束传播：一旦某行无法放置皇后，立即回溯
 * 3. 提前终止：当发现冲突时立即停止当前路径的探索
 * 
 * 时间复杂度：O(N!)，第一行有N种选择，第二行最多有N-1种选择，以此类推
 * 空间复杂度：O(N^2)，棋盘空间和递归栈深度
 * 
 * 工程化考量：
 * 1. 边界处理：处理n=1的特殊情况
 * 2. 性能优化：使用位运算可以进一步优化冲突检查
 * 3. 内存管理：合理使用数据结构减少内存占用
 * 4. 可读性：添加详细注释和变量命名
 * 5. 异常处理：验证输入参数的有效性
 * 6. 模块化设计：将核心逻辑封装成独立方法
 * 7. 可维护性：添加详细注释和文档说明
 */
public class Code11_NQueens {

    /**
     * 解决N皇后问题
     * 
     * @param n 皇后数量和棋盘大小
     * @return 所有解决方案
     */
    public static List<List<String>> solveNQueens(int n) {
        // 边界条件检查
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        
        // 初始化棋盘
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                board[i][j] = '.';
        
        backtrack(result, board, 0);
        return result;
    }

    /**
     * 回溯函数解决N皇后问题
     * 
     * @param result 结果列表
     * @param board 棋盘
     * @param row 当前行
     */
    private static void backtrack(List<List<String>> result, char[][] board, int row) {
        // 终止条件：已放置完所有皇后
        if (row == board.length) {
            result.add(construct(board));
            return;
        }
        
        // 在当前行的每一列尝试放置皇后
        for (int col = 0; col < board.length; col++) {
            // 可行性剪枝：检查是否与已放置的皇后冲突
            if (isValid(board, row, col)) {
                board[row][col] = 'Q';
                backtrack(result, board, row + 1);
                board[row][col] = '.'; // 回溯
            }
        }
    }

    /**
     * 检查在指定位置放置皇后是否合法
     * 
     * @param board 棋盘
     * @param row 行索引
     * @param col 列索引
     * @return 是否合法
     */
    private static boolean isValid(char[][] board, int row, int col) {
        // 检查列
        for (int i = 0; i < row; i++)
            if (board[i][col] == 'Q') return false;
        
        // 检查左上对角线
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 'Q') return false;
        
        // 检查右上对角线
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++)
            if (board[i][j] == 'Q') return false;
        
        return true;
    }

    /**
     * 优化版本：使用位运算优化的N皇后问题解法
     * 
     * @param n 皇后数量和棋盘大小
     * @return 所有解决方案
     */
    public static List<List<String>> solveNQueensOptimized(int n) {
        // 边界条件检查
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        
        List<List<String>> result = new ArrayList<>();
        int[] queens = new int[n];
        
        // 使用位运算表示列、主对角线、副对角线的占用情况
        backtrackOptimized(n, 0, 0, 0, 0, queens, result);
        return result;
    }
    
    /**
     * 使用位运算的回溯函数
     * 
     * @param n 棋盘大小
     * @param row 当前行
     * @param cols 列占用情况（二进制位表示）
     * @param diag1 主对角线占用情况
     * @param diag2 副对角线占用情况
     * @param queens 皇后位置数组
     * @param result 结果列表
     */
    private static void backtrackOptimized(int n, int row, int cols, int diag1, int diag2, int[] queens, List<List<String>> result) {
        // 终止条件：已放置完所有皇后
        if (row == n) {
            result.add(constructFromQueens(queens, n));
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
            
            // 记录皇后位置
            queens[row] = Integer.numberOfTrailingZeros(position);
            
            // 在该位置放置皇后
            backtrackOptimized(n, row + 1, 
                          cols | position, 
                          (diag1 | position) << 1, 
                          (diag2 | position) >> 1,
                          queens, result);
            
            // 移除已处理的位置
            availablePositions &= availablePositions - 1;
        }
    }
    
    /**
     * 从皇后位置数组构造解决方案
     * 
     * @param queens 皇后位置数组
     * @param n 棋盘大小
     * @return 解决方案字符串列表
     */
    private static List<String> constructFromQueens(int[] queens, int n) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            char[] row = new char[n];
            for (int j = 0; j < n; j++) {
                row[j] = '.';
            }
            row[queens[i]] = 'Q';
            result.add(new String(row));
        }
        return result;
    }

    /**
     * 构造解决方案字符串
     * 
     * @param board 棋盘
     * @return 解决方案字符串列表
     */
    private static List<String> construct(char[][] board) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++)
            result.add(new String(board[i]));
        return result;
    }

    /**
     * 验证解决方案是否正确
     * 
     * @param solution 解决方案
     * @return 是否正确
     */
    public static boolean isValidSolution(List<String> solution) {
        if (solution == null || solution.isEmpty()) return false;
        
        int n = solution.size();
        // 检查每行是否只有一个皇后
        for (String row : solution) {
            if (row.length() != n) return false;
            int queenCount = 0;
            for (char c : row.toCharArray()) {
                if (c == 'Q') queenCount++;
                else if (c != '.') return false;
            }
            if (queenCount != 1) return false;
        }
        
        // 检查皇后是否相互攻击
        int[] queens = new int[n];
        for (int i = 0; i < n; i++) {
            queens[i] = solution.get(i).indexOf('Q');
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 检查列冲突
                if (queens[i] == queens[j]) return false;
                // 检查对角线冲突
                if (Math.abs(queens[i] - queens[j]) == Math.abs(i - j)) return false;
            }
        }
        
        return true;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4;
        List<List<String>> result1 = solveNQueens(n1);
        System.out.println("=== 测试用例1 (n = " + n1 + ") ===");
        System.out.println("输出:");
        for (List<String> solution : result1) {
            for (String row : solution) {
                System.out.println(row);
            }
            System.out.println("解是否正确: " + isValidSolution(solution));
            System.out.println();
        }
        
        // 测试用例2
        int n2 = 1;
        List<List<String>> result2 = solveNQueens(n2);
        System.out.println("=== 测试用例2 (n = " + n2 + ") ===");
        System.out.println("输出:");
        for (List<String> solution : result2) {
            for (String row : solution) {
                System.out.println(row);
            }
            System.out.println("解是否正确: " + isValidSolution(solution));
            System.out.println();
        }
        
        // 测试用例3：优化版本
        int n3 = 8;
        long startTime = System.currentTimeMillis();
        List<List<String>> result3 = solveNQueens(n3);
        long endTime = System.currentTimeMillis();
        System.out.println("=== 测试用例3 (n = " + n3 + ", 基础版本) ===");
        System.out.println("解的数量: " + result3.size());
        System.out.println("耗时: " + (endTime - startTime) + " ms");
        
        startTime = System.currentTimeMillis();
        List<List<String>> result4 = solveNQueensOptimized(n3);
        endTime = System.currentTimeMillis();
        System.out.println("=== 测试用例3 (n = " + n3 + ", 优化版本) ===");
        System.out.println("解的数量: " + result4.size());
        System.out.println("耗时: " + (endTime - startTime) + " ms");
    }
}
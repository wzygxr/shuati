package class184;

import java.util.*;

/**
 * LeetCode 289. Game of Life 解决方案
 * 
 * 题目链接: https://leetcode.com/problems/game-of-life/
 * 题目描述: 实现康威生命游戏的下一个状态
 * 解题思路: 使用原地算法，通过特殊标记避免额外空间
 * 
 * 时间复杂度: O(m*n) - m行n列
 * 空间复杂度: O(1) - 原地算法
 */
public class LeetCode_GameOfLife {
    
    /**
     * 计算生命游戏的下一个状态（原地算法）
     * @param board 当前状态的二维数组
     */
    public void gameOfLife(int[][] board) {
        // 检查输入有效性
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }
        
        int rows = board.length;
        int cols = board[0].length;
        
        // 编码规则：
        // 0: 死细胞 -> 死细胞
        // 1: 活细胞 -> 活细胞
        // 2: 活细胞 -> 死细胞
        // 3: 死细胞 -> 活细胞
        
        // 第一遍遍历：计算每个细胞的下一个状态并用编码标记
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int liveNeighbors = countLiveNeighbors(board, i, j);
                
                // 应用生命游戏规则
                if (board[i][j] == 1) {  // 活细胞
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        board[i][j] = 2;  // 标记为将死亡
                    }
                    // 否则保持为1，继续存活
                } else {  // 死细胞
                    if (liveNeighbors == 3) {
                        board[i][j] = 3;  // 标记为将复活
                    }
                    // 否则保持为0，继续死亡
                }
            }
        }
        
        // 第二遍遍历：解码，将标记转换回0和1
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] %= 2;  // 2 -> 0, 3 -> 1
            }
        }
    }
    
    /**
     * 计算指定位置周围活细胞的数量
     * @param board 当前状态的二维数组
     * @param row 行索引
     * @param col 列索引
     * @return 周围活细胞的数量
     */
    private int countLiveNeighbors(int[][] board, int row, int col) {
        int liveNeighbors = 0;
        int rows = board.length;
        int cols = board[0].length;
        
        // 8个方向的偏移
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            // 检查边界并计算活细胞
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                // 1和2表示原始状态为活细胞（1：保持活，2：将死亡）
                if (board[newRow][newCol] == 1 || board[newRow][newCol] == 2) {
                    liveNeighbors++;
                }
            }
        }
        
        return liveNeighbors;
    }
    
    /**
     * 使用额外空间计算下一个状态（用于对比）
     * @param board 当前状态的二维数组
     */
    public void gameOfLifeWithExtraSpace(int[][] board) {
        // 检查输入有效性
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }
        
        int rows = board.length;
        int cols = board[0].length;
        
        // 创建新数组存储下一个状态
        int[][] nextBoard = new int[rows][cols];
        
        // 计算每个细胞的下一个状态
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int liveNeighbors = countLiveNeighbors(board, i, j);
                
                // 应用生命游戏规则
                if (board[i][j] == 1) {  // 活细胞
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        nextBoard[i][j] = 0;  // 死亡：人口稀少或过度拥挤
                    } else {
                        nextBoard[i][j] = 1;  // 存活
                    }
                } else {  // 死细胞
                    if (liveNeighbors == 3) {
                        nextBoard[i][j] = 1;  // 繁殖
                    } else {
                        nextBoard[i][j] = 0;  // 保持死亡
                    }
                }
            }
        }
        
        // 更新原数组
        for (int i = 0; i < rows; i++) {
            System.arraycopy(nextBoard[i], 0, board[i], 0, cols);
        }
    }
    
    /**
     * 打印棋盘状态
     * @param board 棋盘状态
     */
    public void printBoard(int[][] board) {
        if (board == null) {
            System.out.println("null");
            return;
        }
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        LeetCode_GameOfLife solution = new LeetCode_GameOfLife();
        
        // 测试用例1：闪烁器（Blinker）
        System.out.println("=== 测试用例1：闪烁器 ===");
        int[][] board1 = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
        
        System.out.println("初始状态:");
        solution.printBoard(board1);
        
        solution.gameOfLife(board1);
        System.out.println("下一个状态:");
        solution.printBoard(board1);
        
        // 测试用例2：滑翔机（Glider）
        System.out.println("=== 测试用例2：滑翔机 ===");
        int[][] board2 = {
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
        };
        
        System.out.println("初始状态:");
        solution.printBoard(board2);
        
        solution.gameOfLife(board2);
        System.out.println("下一个状态:");
        solution.printBoard(board2);
        
        // 测试用例3：使用额外空间的方法
        System.out.println("=== 测试用例3：使用额外空间的方法 ===");
        int[][] board3 = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
        
        System.out.println("初始状态:");
        solution.printBoard(board3);
        
        solution.gameOfLifeWithExtraSpace(board3);
        System.out.println("下一个状态（额外空间）:");
        solution.printBoard(board3);
    }
}
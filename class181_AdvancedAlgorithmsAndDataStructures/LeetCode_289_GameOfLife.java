package class185.game_of_life_problems;

import java.util.*;

/**
 * LeetCode 289. 生命游戏 (Game of Life)
 * 题目链接：https://leetcode.com/problems/game-of-life/
 * 
 * 题目描述：
 * 根据百度百科，生命游戏，简称为生命，是英国数学家约翰·何顿·康威在1970年发明的细胞自动机。
 * 给定一个包含 m × n 个格子的面板，每一个格子都可以看成是一个细胞。每个细胞都具有一个初始状态：
 * 1 即为活细胞（live），或 0 即为死细胞（dead）。每个细胞与其八个相邻位置（水平，垂直，对角线）的细胞都遵循以下四条生存定律：
 * 
 * 1. 如果活细胞周围八个位置的活细胞数少于两个，则该位置活细胞死亡；
 * 2. 如果活细胞周围八个位置有超过三个活细胞，则该位置活细胞死亡；
 * 3. 如果活细胞周围八个位置有两个或三个活细胞，则该位置活细胞仍然存活；
 * 4. 如果死细胞周围正好有三个活细胞，则该位置死细胞复活；
 * 
 * 根据当前状态，写一个函数来计算面板上所有细胞的下一个（一次更新后的）状态。
 * 下一个状态是通过将上述规则同时应用于当前状态下的每个细胞所形成的，其中细胞的出生和死亡是同时发生的。
 * 
 * 要求：使用原地算法，空间复杂度O(1)
 * 
 * 算法思路：
 * 1. 使用特殊标记法来同时保存当前状态和下一状态
 * 2. 标记规则：
 *    - 如果细胞从活到死，标记为2（当前为活，下一状态为死）
 *    - 如果细胞从死到活，标记为-1（当前为死，下一状态为活）
 * 3. 遍历两次：第一次应用规则并标记，第二次解析标记
 * 
 * 时间复杂度：O(m*n)
 * 空间复杂度：O(1)
 * 
 * 边界情况：
 * 1. 空面板或单细胞面板
 * 2. 边界细胞（需要处理边界条件）
 * 3. 所有细胞死亡或所有细胞存活
 * 4. 特殊图案（稳定图案、周期图案等）
 */
public class LeetCode_289_GameOfLife {
    
    /**
     * 原地算法解决生命游戏问题
     * @param board 细胞面板，会被原地修改
     */
    public void gameOfLife(int[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }
        
        int m = board.length;
        int n = board[0].length;
        
        // 第一遍遍历：应用规则并标记
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int liveNeighbors = countLiveNeighbors(board, i, j, m, n);
                
                // 规则1和2：活细胞死亡条件
                if (board[i][j] == 1 && (liveNeighbors < 2 || liveNeighbors > 3)) {
                    board[i][j] = 2; // 标记为从活到死
                }
                // 规则4：死细胞复活条件
                else if (board[i][j] == 0 && liveNeighbors == 3) {
                    board[i][j] = -1; // 标记为从死到活
                }
                // 规则3：活细胞存活条件（保持1不变）
                // 其他情况：死细胞保持死亡（保持0不变）
            }
        }
        
        // 第二遍遍历：解析标记
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 2) {
                    board[i][j] = 0; // 从活到死
                } else if (board[i][j] == -1) {
                    board[i][j] = 1; // 从死到活
                }
                // 其他情况保持不变
            }
        }
    }
    
    /**
     * 计算细胞周围活细胞数量（考虑标记）
     * @param board 细胞面板
     * @param i 当前细胞行索引
     * @param j 当前细胞列索引
     * @param m 面板行数
     * @param n 面板列数
     * @return 活细胞数量
     */
    private int countLiveNeighbors(int[][] board, int i, int j, int m, int n) {
        int count = 0;
        
        // 8个方向的偏移量
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (int[] dir : directions) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            // 检查边界
            if (ni >= 0 && ni < m && nj >= 0 && nj < n) {
                // 当前为活细胞(1)或标记为从活到死(2)的都算作活细胞
                if (board[ni][nj] == 1 || board[ni][nj] == 2) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 使用额外空间的解法（非原地，更容易理解）
     * @param board 细胞面板
     */
    public void gameOfLifeWithExtraSpace(int[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }
        
        int m = board.length;
        int n = board[0].length;
        int[][] nextBoard = new int[m][n];
        
        // 复制当前状态
        for (int i = 0; i < m; i++) {
            System.arraycopy(board[i], 0, nextBoard[i], 0, n);
        }
        
        // 应用规则
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int liveNeighbors = countLiveNeighborsSimple(board, i, j, m, n);
                
                if (board[i][j] == 1) {
                    // 活细胞规则
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        nextBoard[i][j] = 0; // 死亡
                    } else {
                        nextBoard[i][j] = 1; // 存活
                    }
                } else {
                    // 死细胞规则
                    if (liveNeighbors == 3) {
                        nextBoard[i][j] = 1; // 复活
                    } else {
                        nextBoard[i][j] = 0; // 保持死亡
                    }
                }
            }
        }
        
        // 更新原数组
        for (int i = 0; i < m; i++) {
            System.arraycopy(nextBoard[i], 0, board[i], 0, n);
        }
    }
    
    /**
     * 简单计算活细胞数量（不考虑标记）
     */
    private int countLiveNeighborsSimple(int[][] board, int i, int j, int m, int n) {
        int count = 0;
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (int[] dir : directions) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            if (ni >= 0 && ni < m && nj >= 0 && nj < n && board[ni][nj] == 1) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 单元测试
     */
    public static void main(String[] args) {
        LeetCode_289_GameOfLife solution = new LeetCode_289_GameOfLife();
        
        // 测试用例1：闪烁器（Blinker）
        System.out.println("=== 测试用例1：闪烁器 ===");
        int[][] board1 = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
        
        System.out.println("初始状态:");
        printBoard(board1);
        
        solution.gameOfLife(board1);
        System.out.println("下一代状态:");
        printBoard(board1);
        
        // 测试用例2：滑翔机（Glider）
        System.out.println("=== 测试用例2：滑翔机 ===");
        int[][] board2 = {
            {0, 1, 0},
            {0, 0, 1},
            {1, 1, 1}
        };
        
        System.out.println("初始状态:");
        printBoard(board2);
        
        solution.gameOfLife(board2);
        System.out.println("下一代状态:");
        printBoard(board2);
        
        // 测试用例3：稳定图案（方块）
        System.out.println("=== 测试用例3：稳定图案 ===");
        int[][] board3 = {
            {1, 1},
            {1, 1}
        };
        
        System.out.println("初始状态:");
        printBoard(board3);
        
        solution.gameOfLife(board3);
        System.out.println("下一代状态（应该保持不变）:");
        printBoard(board3);
        
        // 测试用例4：边界情况
        System.out.println("=== 测试用例4：单细胞 ===");
        int[][] board4 = {{1}};
        
        System.out.println("初始状态:");
        printBoard(board4);
        
        solution.gameOfLife(board4);
        System.out.println("下一代状态（应该死亡）:");
        printBoard(board4);
    }
    
    /**
     * 打印面板
     */
    private static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print(cell == 1 ? "█ " : "· ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
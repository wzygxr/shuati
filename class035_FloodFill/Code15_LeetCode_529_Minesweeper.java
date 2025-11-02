package class058;

import java.util.*;

/**
 * LeetCode 529. 扫雷游戏 (Minesweeper)
 * 题目链接: https://leetcode.cn/problems/minesweeper/
 * 
 * 题目描述:
 * 给定一个代表游戏板的二维字符矩阵。'M'代表一个未挖出的地雷，'E'代表一个未挖出的空方块，
 * 'B'代表没有相邻地雷的已挖出的空白方块，数字（'1'到'8'）表示有多少地雷与这块已挖出的方块相邻，
 * 'X'则表示一个已挖出的地雷。
 * 
 * 现在给出在所有未挖出的方块（'M'或者'E'）中的下一个点击位置（行和列索引），
 * 根据以下规则，返回相应的点击后的面板：
 * 1. 如果一个地雷（'M'）被挖出，游戏就结束了- 把它改为 'X'
 * 2. 如果一个没有相邻地雷的空方块（'E'）被挖出，修改它为（'B'），并且所有和其相邻的未挖出方块都应该被递归地揭露
 * 3. 如果一个至少与一个地雷相邻的空方块（'E'）被挖出，修改它为数字（'1'到'8'），表示相邻地雷的数量
 * 4. 如果在此次点击中，若无更多方块可被揭露，则返回面板
 * 
 * 解题思路:
 * 使用Flood Fill算法进行递归揭露：
 * 1. 如果点击到地雷，游戏结束
 * 2. 如果点击到空白方块，递归揭露相邻的空白方块
 * 3. 计算相邻地雷数量，决定是否继续递归
 * 
 * 时间复杂度: O(m*n) - 最坏情况下需要访问所有格子
 * 空间复杂度: O(m*n) - 递归栈深度
 * 是否最优解: 是
 */
public class Code15_LeetCode_529_Minesweeper {
    
    // 八个方向的偏移量
    private static final int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    /**
     * 扫雷游戏主函数
     * 
     * @param board 游戏面板
     * @param click 点击位置 [row, col]
     * @return 点击后的面板
     */
    public static char[][] updateBoard(char[][] board, int[] click) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return board;
        }
        
        int x = click[0], y = click[1];
        
        // 如果点击到地雷
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
            return board;
        }
        
        // 使用DFS揭露方块
        dfs(board, x, y);
        return board;
    }
    
    private static void dfs(char[][] board, int x, int y) {
        int m = board.length, n = board[0].length;
        
        // 边界检查
        if (x < 0 || x >= m || y < 0 || y >= n || board[x][y] != 'E') {
            return;
        }
        
        // 计算相邻地雷数量
        int mines = countMines(board, x, y);
        
        if (mines > 0) {
            // 有相邻地雷，显示数字
            board[x][y] = (char)('0' + mines);
        } else {
            // 没有相邻地雷，显示空白并递归揭露相邻方块
            board[x][y] = 'B';
            for (int i = 0; i < 8; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                dfs(board, nx, ny);
            }
        }
    }
    
    /**
     * 计算相邻地雷数量
     */
    private static int countMines(char[][] board, int x, int y) {
        int m = board.length, n = board[0].length;
        int count = 0;
        
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && board[nx][ny] == 'M') {
                count++;
            }
        }
        
        return count;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        char[][] board1 = {
            {'E', 'E', 'E', 'E', 'E'},
            {'E', 'E', 'M', 'E', 'E'},
            {'E', 'E', 'E', 'E', 'E'},
            {'E', 'E', 'E', 'E', 'E'}
        };
        
        System.out.println("测试用例1 - 扫雷游戏:");
        System.out.println("原始面板:");
        printBoard(board1);
        updateBoard(board1, new int[]{3, 0});
        System.out.println("点击后面板:");
        printBoard(board1);
    }
    
    private static void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
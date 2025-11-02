/**
 * LeetCode 37. 解数独
 * 
 * 题目描述：
 * 编写一个程序，通过填充空格来解决数独问题。
 * 数独的解法需遵循如下规则：
 * 1. 数字 1-9 在每一行只能出现一次。
 * 2. 数字 1-9 在每一列只能出现一次。
 * 3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
 * 空白格用 '.' 表示。
 * 
 * 示例：
 * 输入：
 * [
 *  ["5","3",".",".","7",".",".",".","."],
 *  ["6",".",".","1","9","5",".",".","."],
 *  [".","9","8",".",".",".",".","6","."],
 *  ["8",".",".",".","6",".",".",".","3"],
 *  ["4",".",".","8",".","3",".",".","1"],
 *  ["7",".",".",".","2",".",".",".","6"],
 *  [".","6",".",".",".",".","2","8","."],
 *  [".",".",".","4","1","9",".",".","5"],
 *  [".",".",".",".","8",".",".","7","9"]
 * ]
 * 输出：
 * [
 *  ["5","3","4","6","7","8","9","1","2"],
 *  ["6","7","2","1","9","5","3","4","8"],
 *  ["1","9","8","3","4","2","5","6","7"],
 *  ["8","5","9","7","6","1","4","2","3"],
 *  ["4","2","6","8","5","3","7","9","1"],
 *  ["7","1","3","9","2","4","8","5","6"],
 *  ["9","6","1","5","3","7","2","8","4"],
 *  ["2","8","7","4","1","9","6","3","5"],
 *  ["3","4","5","2","8","6","1","7","9"]
 * ]
 * 
 * 提示：
 * board.length == 9
 * board[i].length == 9
 * board[i][j] 是一位数字或者 '.'
 * 题目数据保证输入数独仅有一个解
 * 
 * 链接：https://leetcode.cn/problems/sudoku-solver/
 * 
 * 算法思路：
 * 1. 使用回溯算法解决数独问题
 * 2. 遍历棋盘，找到第一个空格
 * 3. 尝试填入1-9的数字，检查是否符合数独规则
 * 4. 如果符合规则，递归解决剩余空格
 * 5. 如果递归返回false，说明当前填法不正确，回溯并尝试下一个数字
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：在填入数字前检查是否符合数独规则
 * 2. 约束传播：一旦某个位置填入数字，立即更新约束条件
 * 3. 提前终止：当发现冲突时立即回溯
 * 
 * 时间复杂度：O(9^(n*n))，最坏情况下每个空格都要尝试9个数字
 * 空间复杂度：O(n*n)，递归栈深度
 * 
 * 工程化考量：
 * 1. 边界处理：处理空棋盘和已填满棋盘的情况
 * 2. 性能优化：通过剪枝减少不必要的计算
 * 3. 内存管理：合理使用数据结构减少内存占用
 * 4. 可读性：添加详细注释和变量命名
 * 5. 异常处理：处理可能的异常情况
 * 6. 模块化设计：将核心逻辑封装成独立方法
 * 7. 可维护性：添加详细注释和文档说明
 */
public class Code10_SudokuSolver {

    /**
     * 解决数独问题
     * 
     * @param board 数独棋盘
     */
    public static void solveSudoku(char[][] board) {
        // 边界条件检查
        if (board == null || board.length != 9 || board[0].length != 9) {
            throw new IllegalArgumentException("Invalid board size");
        }
        
        solve(board);
    }

    /**
     * 回溯函数解决数独
     * 
     * @param board 数独棋盘
     * @return 是否成功解决
     */
    private static boolean solve(char[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // 找到空格
                if (board[row][col] == '.') {
                    // 尝试填入1-9
                    for (char c = '1'; c <= '9'; c++) {
                        // 可行性剪枝：检查是否合法
                        if (isValid(board, row, col, c)) {
                            board[row][col] = c;
                            
                            // 递归求解
                            if (solve(board))
                                return true;
                            else
                                board[row][col] = '.'; // 回溯
                        }
                    }
                    return false; // 1-9都尝试过都不行
                }
            }
        }
        return true; // 全部填完
    }

    /**
     * 检查在指定位置填入指定数字是否合法
     * 
     * @param board 数独棋盘
     * @param row 行索引
     * @param col 列索引
     * @param c 要填入的数字
     * @return 是否合法
     */
    private static boolean isValid(char[][] board, int row, int col, char c) {
        for (int i = 0; i < 9; i++) {
            // 检查行
            if (board[i][col] == c) return false;
            // 检查列
            if (board[row][i] == c) return false;
            // 检查3x3子网格
            if (board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c) return false;
        }
        return true;
    }

    /**
     * 优化版本：使用位运算优化的数独求解器
     * 
     * @param board 数独棋盘
     */
    public static void solveSudokuOptimized(char[][] board) {
        // 边界条件检查
        if (board == null || board.length != 9 || board[0].length != 9) {
            throw new IllegalArgumentException("Invalid board size");
        }
        
        // 使用位运算优化
        int[] rows = new int[9];
        int[] cols = new int[9];
        int[][] boxes = new int[3][3];
        
        // 初始化约束条件
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int digit = board[i][j] - '1';
                    rows[i] |= (1 << digit);
                    cols[j] |= (1 << digit);
                    boxes[i/3][j/3] |= (1 << digit);
                }
            }
        }
        
        solveOptimized(board, rows, cols, boxes, 0, 0);
    }
    
    /**
     * 优化版本的回溯函数
     * 
     * @param board 数独棋盘
     * @param rows 行约束
     * @param cols 列约束
     * @param boxes 3x3盒子约束
     * @param row 当前行
     * @param col 当前列
     * @return 是否成功解决
     */
    private static boolean solveOptimized(char[][] board, int[] rows, int[] cols, int[][] boxes, int row, int col) {
        // 找到下一个空格
        while (row < 9 && board[row][col] != '.') {
            col++;
            if (col == 9) {
                col = 0;
                row++;
            }
        }
        
        // 终止条件：已处理完所有格子
        if (row == 9) {
            return true;
        }
        
        // 计算可用数字
        int boxRow = row / 3;
        int boxCol = col / 3;
        int used = rows[row] | cols[col] | boxes[boxRow][boxCol];
        
        // 尝试填入可用数字
        for (int digit = 0; digit < 9; digit++) {
            if ((used & (1 << digit)) == 0) {  // 数字未被使用
                // 填入数字
                board[row][col] = (char)('1' + digit);
                rows[row] |= (1 << digit);
                cols[col] |= (1 << digit);
                boxes[boxRow][boxCol] |= (1 << digit);
                
                // 递归求解
                if (solveOptimized(board, rows, cols, boxes, row, col)) {
                    return true;
                }
                
                // 回溯
                board[row][col] = '.';
                rows[row] &= ~(1 << digit);
                cols[col] &= ~(1 << digit);
                boxes[boxRow][boxCol] &= ~(1 << digit);
            }
        }
        
        return false;
    }

    /**
     * 打印数独棋盘
     * 
     * @param board 数独棋盘
     */
    public static void printBoard(char[][] board) {
        System.out.println("Current Board:");
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 验证数独解是否正确
     * 
     * @param board 数独棋盘
     * @return 是否正确
     */
    public static boolean isValidSolution(char[][] board) {
        // 检查行
        for (int i = 0; i < 9; i++) {
            boolean[] used = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] < '1' || board[i][j] > '9') return false;
                int digit = board[i][j] - '1';
                if (used[digit]) return false;
                used[digit] = true;
            }
        }
        
        // 检查列
        for (int j = 0; j < 9; j++) {
            boolean[] used = new boolean[9];
            for (int i = 0; i < 9; i++) {
                int digit = board[i][j] - '1';
                if (used[digit]) return false;
                used[digit] = true;
            }
        }
        
        // 检查3x3子网格
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                boolean[] used = new boolean[9];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = boxRow * 3 + i;
                        int col = boxCol * 3 + j;
                        int digit = board[row][col] - '1';
                        if (used[digit]) return false;
                        used[digit] = true;
                    }
                }
            }
        }
        
        return true;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        char[][] board1 = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        
        System.out.println("=== 测试用例1 ===");
        System.out.println("数独题目:");
        printBoard(board1);
        
        solveSudoku(board1);
        
        System.out.println("数独解答:");
        printBoard(board1);
        
        System.out.println("解是否正确: " + isValidSolution(board1));
        
        // 测试用例2：空棋盘
        char[][] board2 = {
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'}
        };
        
        System.out.println("\n=== 测试用例2（空棋盘） ===");
        System.out.println("数独题目:");
        printBoard(board2);
        
        solveSudokuOptimized(board2);
        
        System.out.println("数独解答:");
        printBoard(board2);
        
        System.out.println("解是否正确: " + isValidSolution(board2));
    }
}
package class058;

/**
 * 被围绕的区域 (Surrounded Regions)
 * 来源: LeetCode 130
 * 题目链接: https://leetcode.cn/problems/surrounded-regions/
 * 
 * 题目描述:
 * 给你一个 m x n 的矩阵 board ，由若干字符 'X' 和 'O' ，找到所有被 'X' 围绕的区域，
 * 并将这些区域里所有的 'O' 用 'X' 填充。
 * 
 * 解题思路（逆向思维）:
 * 1. 从边界上的'O'开始进行DFS/BFS，标记所有与边界相连的'O'（这些不会被围绕）
 * 2. 遍历整个矩阵，将未被标记的'O'（即被围绕的区域）修改为'X'
 * 3. 将标记的'O'恢复为原来的'O'
 * 
 * 时间复杂度: O(m*n) - 每个单元格最多被访问两次
 * 空间复杂度: O(m*n) - 递归调用栈的深度最多为m*n
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空，矩阵尺寸是否有效
 * 2. 边界条件：处理单行、单列矩阵
 * 3. 标记策略：使用特殊字符标记不会被围绕的区域
 * 4. 可配置性：可以扩展到更多边界条件
 * 
 * 语言特性差异:
 * Java: 递归实现简洁，但需要注意递归深度
 * C++: 可以选择递归或使用栈手动实现
 * Python: 递归实现简洁，但有递归深度限制
 * 
 * 极端输入场景:
 * 1. 空矩阵：直接返回
 * 2. 全'X'矩阵：不需要修改
 * 3. 全'O'矩阵：边界相连的'O'不会被修改
 * 4. 单行/单列矩阵：特殊边界处理
 * 
 * 性能优化:
 * 1. 逆向思维：从边界开始搜索，避免重复计算
 * 2. 原地修改：通过修改原矩阵来标记状态
 * 3. 提前终止：在DFS中及时返回边界条件
 * 
 * 调试技巧:
 * 1. 打印中间状态：可以在DFS前后打印矩阵状态
 * 2. 可视化标记：使用不同字符标记不同状态
 * 3. 边界测试：测试各种边界情况确保算法正确性
 */
public class Code02_SurroundedRegions {

    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 解决被围绕的区域问题的主函数
     * 
     * @param board 二维字符矩阵，'X'表示墙，'O'表示区域
     * 
     * 算法步骤:
     * 1. 检查输入有效性
     * 2. 从四个边界开始DFS，标记所有与边界相连的'O'
     * 3. 遍历整个矩阵，将未被标记的'O'修改为'X'
     * 4. 将标记的'O'恢复为原来的'O'
     * 
     * 时间复杂度分析:
     * - 边界DFS: O(m+n)
     * - 矩阵遍历: O(m*n)
     * - 总时间复杂度: O(m*n)
     * 
     * 空间复杂度分析:
     * - 递归调用栈深度最多为m*n: O(m*n)
     * - 没有使用额外空间: O(1)（不考虑输入空间）
     */
    public static void solve(char[][] board) {
        // 边界条件检查
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }
        
        int n = board.length;
        int m = board[0].length;
        
        // 如果矩阵太小（小于3x3），所有'O'都与边界相连
        if (n < 3 || m < 3) {
            return;
        }
        
        // 步骤1: 从四个边界开始DFS，标记与边界相连的'O'
        // 上边界和下边界
        for (int j = 0; j < m; j++) {
            if (board[0][j] == 'O') {
                dfs(board, n, m, 0, j);
            }
            if (board[n - 1][j] == 'O') {
                dfs(board, n, m, n - 1, j);
            }
        }
        
        // 左边界和右边界（跳过四个角，因为已经在上下边界处理过）
        for (int i = 1; i < n - 1; i++) {
            if (board[i][0] == 'O') {
                dfs(board, n, m, i, 0);
            }
            if (board[i][m - 1] == 'O') {
                dfs(board, n, m, i, m - 1);
            }
        }
        
        // 步骤2: 遍历整个矩阵，修改被围绕的区域
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    // 未被标记的'O'，是被围绕的区域
                    board[i][j] = 'X';
                } else if (board[i][j] == 'F') {
                    // 被标记的'O'，恢复为原来的'O'
                    board[i][j] = 'O';
                }
            }
        }
    }
    
    /**
     * 深度优先搜索标记与边界相连的区域
     * 
     * @param board 二维矩阵
     * @param n 行数
     * @param m 列数
     * @param i 当前行坐标
     * @param j 当前列坐标
     * 
     * 标记策略:
     * - 将边界相连的'O'标记为'F'
     * - 这样在后续处理中可以区分哪些'O'需要被保留
     */
    private static void dfs(char[][] board, int n, int m, int i, int j) {
        // 边界条件检查
        if (i < 0 || i >= n || j < 0 || j >= m || board[i][j] != 'O') {
            return;
        }
        
        // 标记当前单元格为与边界相连
        board[i][j] = 'F';
        
        // 递归处理四个方向的相邻单元格
        for (int k = 0; k < 4; k++) {
            int newI = i + dx[k];
            int newJ = j + dy[k];
            dfs(board, n, m, newI, newJ);
        }
    }
    
    /**
     * 广度优先搜索版本（避免递归深度问题）
     * 
     * @param board 二维矩阵
     */
    public static void solveBFS(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }
        
        int n = board.length;
        int m = board[0].length;
        
        if (n < 3 || m < 3) {
            return;
        }
        
        // 使用队列进行BFS
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        
        // 将边界上的'O'加入队列并标记
        for (int j = 0; j < m; j++) {
            if (board[0][j] == 'O') {
                board[0][j] = 'F';
                queue.offer(new int[]{0, j});
            }
            if (board[n - 1][j] == 'O') {
                board[n - 1][j] = 'F';
                queue.offer(new int[]{n - 1, j});
            }
        }
        
        for (int i = 1; i < n - 1; i++) {
            if (board[i][0] == 'O') {
                board[i][0] = 'F';
                queue.offer(new int[]{i, 0});
            }
            if (board[i][m - 1] == 'O') {
                board[i][m - 1] = 'F';
                queue.offer(new int[]{i, m - 1});
            }
        }
        
        // BFS扩展标记
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int i = cell[0], j = cell[1];
            
            for (int k = 0; k < 4; k++) {
                int newI = i + dx[k];
                int newJ = j + dy[k];
                
                if (newI >= 0 && newI < n && newJ >= 0 && newJ < m && board[newI][newJ] == 'O') {
                    board[newI][newJ] = 'F';
                    queue.offer(new int[]{newI, newJ});
                }
            }
        }
        
        // 修改被围绕的区域
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                } else if (board[i][j] == 'F') {
                    board[i][j] = 'O';
                }
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准被围绕区域
        char[][] board1 = {
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','X','O','X'},
            {'X','O','X','X'}
        };
        
        System.out.println("测试用例1 - 标准被围绕区域:");
        System.out.println("原始矩阵:");
        printBoard(board1);
        
        char[][] board1Copy = copyBoard(board1);
        solve(board1Copy);
        System.out.println("DFS版本处理后:");
        printBoard(board1Copy);
        
        char[][] board1Copy2 = copyBoard(board1);
        solveBFS(board1Copy2);
        System.out.println("BFS版本处理后:");
        printBoard(board1Copy2);
        
        // 测试用例2：所有区域都与边界相连
        char[][] board2 = {
            {'X','O','X','X'},
            {'O','O','O','X'},
            {'X','O','O','X'},
            {'X','X','O','X'}
        };
        
        System.out.println("测试用例2 - 所有区域都与边界相连:");
        System.out.println("原始矩阵:");
        printBoard(board2);
        
        char[][] board2Copy = copyBoard(board2);
        solve(board2Copy);
        System.out.println("DFS版本处理后:");
        printBoard(board2Copy);
        
        // 测试用例3：空矩阵
        char[][] board3 = {};
        System.out.println("测试用例3 - 空矩阵:");
        solve(board3);
        System.out.println("处理完成");
    }
    
    // 辅助方法：打印矩阵
    private static void printBoard(char[][] board) {
        if (board == null || board.length == 0) {
            System.out.println("空矩阵");
            return;
        }
        
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    // 辅助方法：复制矩阵
    private static char[][] copyBoard(char[][] board) {
        if (board == null) return null;
        char[][] copy = new char[board.length][];
        for (int i = 0; i < board.length; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }
}

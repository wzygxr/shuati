/**
 * Alpha-Beta剪枝算法实现（以井字棋为例）
 * 
 * 算法原理：
 * Alpha-Beta剪枝是一种优化极小极大搜索算法的技术，通过剪掉搜索树中不会影响最终结果的分支来减少搜索节点数量。
 * 
 * 算法特点：
 * 1. 减少搜索空间，提高搜索效率
 * 2. 不影响最终结果的正确性
 * 3. 剪枝效果与搜索顺序密切相关
 * 4. 最好情况下可以将时间复杂度从O(b^d)降低到O(b^(d/2))
 * 
 * 算法步骤：
 * 1. 在极小极大搜索过程中维护alpha和beta值
 * 2. alpha表示最大化玩家能够保证的最小收益
 * 3. beta表示最小化玩家能够保证的最大损失
 * 4. 当alpha >= beta时，剪枝剩余分支
 * 
 * 应用场景：
 * - 博弈树搜索（井字棋、五子棋、象棋等）
 * - 决策系统
 * - 对抗性问题求解
 * 
 * 时间复杂度：
 * - 最坏情况：O(b^d)
 * - 最好情况：O(b^(d/2))
 * - 平均情况：O(b^(3d/4))
 * 
 * 空间复杂度：O(d)，递归栈深度
 * 
 * 工程化考量：
 * 1. 移动排序：优先搜索较优的移动可以提高剪枝效果
 * 2. 迭代加深：结合迭代加深搜索使用
 * 3. 转置表：缓存已计算的节点避免重复计算
 * 4. 启发式评估：设计合理的评估函数
 * 5. 边界处理：处理游戏结束状态
 * 6. 性能优化：通过剪枝减少不必要的计算
 */

import java.util.ArrayList;
import java.util.List;

public class AlphaBetaPruning {
    
    // 井字棋棋盘状态
    public static final int EMPTY = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;
    
    // 评估分数
    public static final int WIN_SCORE = 10000;
    public static final int LOSE_SCORE = -10000;
    public static final int TIE_SCORE = 0;
    
    // 棋盘大小
    private static final int BOARD_SIZE = 3;
    
    /**
     * Alpha-Beta剪枝搜索函数
     * 
     * @param board 当前棋盘状态
     * @param depth 当前搜索深度
     * @param alpha Alpha值（最大化玩家的最小保证收益）
     * @param beta Beta值（最小化玩家的最大保证损失）
     * @param isMaximizingPlayer 是否为最大化玩家（PLAYER_X）
     * @return 最佳评估分数
     */
    public static int alphaBetaSearch(int[][] board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // 检查游戏是否结束或达到最大深度
        int gameResult = evaluateGame(board);
        if (gameResult != -2 || depth == 0) {  // -2表示游戏继续
            return gameResult;
        }
        
        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            List<int[]> moves = generateMoves(board);
            
            // 启发式排序：优先考虑中心和角落位置
            moves.sort((a, b) -> {
                // 中心位置优先
                if (a[0] == 1 && a[1] == 1) return -1;
                if (b[0] == 1 && b[1] == 1) return 1;
                // 角落位置次之
                if ((a[0] == 0 || a[0] == 2) && (a[1] == 0 || a[1] == 2)) return -1;
                if ((b[0] == 0 || b[0] == 2) && (b[1] == 0 || b[1] == 2)) return 1;
                return 0;
            });
            
            for (int[] move : moves) {
                // 执行移动
                board[move[0]][move[1]] = PLAYER_X;
                
                // 递归搜索
                int eval = alphaBetaSearch(board, depth - 1, alpha, beta, false);
                
                // 撤销移动
                board[move[0]][move[1]] = EMPTY;
                
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                
                // Alpha-Beta剪枝
                if (beta <= alpha) {
                    break; // beta剪枝
                }
            }
            
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            List<int[]> moves = generateMoves(board);
            
            // 启发式排序：优先考虑中心和角落位置
            moves.sort((a, b) -> {
                // 中心位置优先
                if (a[0] == 1 && a[1] == 1) return -1;
                if (b[0] == 1 && b[1] == 1) return 1;
                // 角落位置次之
                if ((a[0] == 0 || a[0] == 2) && (a[1] == 0 || a[1] == 2)) return -1;
                if ((b[0] == 0 || b[0] == 2) && (b[1] == 0 || b[1] == 2)) return 1;
                return 0;
            });
            
            for (int[] move : moves) {
                // 执行移动
                board[move[0]][move[1]] = PLAYER_O;
                
                // 递归搜索
                int eval = alphaBetaSearch(board, depth - 1, alpha, beta, true);
                
                // 撤销移动
                board[move[0]][move[1]] = EMPTY;
                
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                
                // Alpha-Beta剪枝
                if (beta <= alpha) {
                    break; // alpha剪枝
                }
            }
            
            return minEval;
        }
    }
    
    /**
     * 获取最佳移动
     * 
     * @param board 当前棋盘状态
     * @param depth 搜索深度
     * @param isMaximizingPlayer 是否为最大化玩家
     * @return 最佳移动位置 [row, col]
     */
    public static int[] getBestMove(int[][] board, int depth, boolean isMaximizingPlayer) {
        int bestValue = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {-1, -1};
        List<int[]> moves = generateMoves(board);
        
        // 启发式排序：优先考虑中心和角落位置
        moves.sort((a, b) -> {
            // 中心位置优先
            if (a[0] == 1 && a[1] == 1) return -1;
            if (b[0] == 1 && b[1] == 1) return 1;
            // 角落位置次之
            if ((a[0] == 0 || a[0] == 2) && (a[1] == 0 || a[1] == 2)) return -1;
            if ((b[0] == 0 || b[0] == 2) && (b[1] == 0 || b[1] == 2)) return 1;
            return 0;
        });
        
        for (int[] move : moves) {
            // 执行移动
            board[move[0]][move[1]] = isMaximizingPlayer ? PLAYER_X : PLAYER_O;
            
            // 评估移动
            int moveValue = alphaBetaSearch(board, depth - 1, 
                                          isMaximizingPlayer ? Integer.MIN_VALUE : bestValue,
                                          isMaximizingPlayer ? bestValue : Integer.MAX_VALUE,
                                          !isMaximizingPlayer);
            
            // 撤销移动
            board[move[0]][move[1]] = EMPTY;
            
            // 更新最佳移动
            if (isMaximizingPlayer && moveValue > bestValue) {
                bestValue = moveValue;
                bestMove[0] = move[0];
                bestMove[1] = move[1];
            } else if (!isMaximizingPlayer && moveValue < bestValue) {
                bestValue = moveValue;
                bestMove[0] = move[0];
                bestMove[1] = move[1];
            }
        }
        
        return bestMove;
    }
    
    /**
     * 生成所有可能的移动
     * 
     * @param board 当前棋盘状态
     * @return 所有可能的移动列表
     */
    private static List<int[]> generateMoves(int[][] board) {
        List<int[]> moves = new ArrayList<>();
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        
        return moves;
    }
    
    /**
     * 评估游戏状态
     * 
     * @param board 当前棋盘状态
     * @return 游戏结果：WIN_SCORE（X胜）、LOSE_SCORE（O胜）、TIE_SCORE（平局）、-2（游戏继续）
     */
    private static int evaluateGame(int[][] board) {
        // 检查行
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0] == PLAYER_X ? WIN_SCORE : LOSE_SCORE;
            }
        }
        
        // 检查列
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] != EMPTY && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return board[0][j] == PLAYER_X ? WIN_SCORE : LOSE_SCORE;
            }
        }
        
        // 检查对角线
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0] == PLAYER_X ? WIN_SCORE : LOSE_SCORE;
        }
        
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2] == PLAYER_X ? WIN_SCORE : LOSE_SCORE;
        }
        
        // 检查是否平局
        boolean isFull = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    isFull = false;
                    break;
                }
            }
            if (!isFull) break;
        }
        
        if (isFull) {
            return TIE_SCORE; // 平局
        }
        
        return -2; // 游戏继续
    }
    
    /**
     * 打印棋盘
     * 
     * @param board 棋盘状态
     */
    public static void printBoard(int[][] board) {
        System.out.println("Current Board:");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                switch (board[i][j]) {
                    case EMPTY:
                        System.out.print(". ");
                        break;
                    case PLAYER_X:
                        System.out.print("X ");
                        break;
                    case PLAYER_O:
                        System.out.print("O ");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 检查移动是否合法
     * 
     * @param board 棋盘状态
     * @param row 行索引
     * @param col 列索引
     * @return 是否合法
     */
    public static boolean isValidMove(int[][] board, int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board[row][col] == EMPTY;
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== Alpha-Beta剪枝算法测试（井字棋） ===");
        
        // 初始化空棋盘
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        
        // 简单测试：评估空棋盘
        int eval = alphaBetaSearch(board, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        System.out.println("空棋盘评估值: " + eval);
        
        // 测试：X在中心位置
        board[1][1] = PLAYER_X;
        printBoard(board);
        eval = alphaBetaSearch(board, 8, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        System.out.println("X在中心位置的评估值: " + eval);
        
        // 获取最佳移动
        int[] bestMove = getBestMove(board, 7, false);
        System.out.println("O的最佳移动: [" + bestMove[0] + ", " + bestMove[1] + "]");
        
        // 模拟一局游戏
        System.out.println("\n=== 模拟游戏 ===");
        // 重置棋盘
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        
        boolean isXMove = true;
        int movesCount = 0;
        
        while (evaluateGame(board) == -2 && movesCount < 9) {
            printBoard(board);
            int[] move = getBestMove(board, 9 - movesCount, isXMove);
            
            if (move[0] != -1 && move[1] != -1) {
                board[move[0]][move[1]] = isXMove ? PLAYER_X : PLAYER_O;
                System.out.println((isXMove ? "X" : "O") + " 下在 [" + move[0] + ", " + move[1] + "]");
                isXMove = !isXMove;
                movesCount++;
            } else {
                break;
            }
            
            // 短暂延迟以便观察
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        printBoard(board);
        int result = evaluateGame(board);
        if (result == WIN_SCORE) {
            System.out.println("X 获胜！");
        } else if (result == LOSE_SCORE) {
            System.out.println("O 获胜！");
        } else {
            System.out.println("平局！");
        }
    }
}
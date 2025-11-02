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

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <utility>
using namespace std;

class AlphaBetaPruning {
public:
    // 井字棋棋盘状态
    static const int EMPTY = 0;
    static const int PLAYER_X = 1;
    static const int PLAYER_O = 2;
    
    // 评估分数
    static const int WIN_SCORE = 10000;
    static const int LOSE_SCORE = -10000;
    static const int TIE_SCORE = 0;
    
    // 棋盘大小
    static const int BOARD_SIZE = 3;
    
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
    static int alphaBetaSearch(vector<vector<int>>& board, int depth, int alpha, int beta, bool isMaximizingPlayer) {
        // 检查游戏是否结束或达到最大深度
        int gameResult = evaluateGame(board);
        if (gameResult != -2 || depth == 0) {  // -2表示游戏继续
            return gameResult;
        }
        
        if (isMaximizingPlayer) {
            int maxEval = INT_MIN;
            vector<pair<int, int>> moves = generateMoves(board);
            
            // 启发式排序：优先考虑中心和角落位置
            sort(moves.begin(), moves.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
                // 中心位置优先
                if (a.first == 1 && a.second == 1) return true;
                if (b.first == 1 && b.second == 1) return false;
                // 角落位置次之
                if ((a.first == 0 || a.first == 2) && (a.second == 0 || a.second == 2)) return true;
                if ((b.first == 0 || b.first == 2) && (b.second == 0 || b.second == 2)) return false;
                return false;
            });
            
            for (const auto& move : moves) {
                // 执行移动
                board[move.first][move.second] = PLAYER_X;
                
                // 递归搜索
                int eval = alphaBetaSearch(board, depth - 1, alpha, beta, false);
                
                // 撤销移动
                board[move.first][move.second] = EMPTY;
                
                maxEval = max(maxEval, eval);
                alpha = max(alpha, eval);
                
                // Alpha-Beta剪枝
                if (beta <= alpha) {
                    break; // beta剪枝
                }
            }
            
            return maxEval;
        } else {
            int minEval = INT_MAX;
            vector<pair<int, int>> moves = generateMoves(board);
            
            // 启发式排序：优先考虑中心和角落位置
            sort(moves.begin(), moves.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
                // 中心位置优先
                if (a.first == 1 && a.second == 1) return true;
                if (b.first == 1 && b.second == 1) return false;
                // 角落位置次之
                if ((a.first == 0 || a.first == 2) && (a.second == 0 || a.second == 2)) return true;
                if ((b.first == 0 || b.first == 2) && (b.second == 0 || b.second == 2)) return false;
                return false;
            });
            
            for (const auto& move : moves) {
                // 执行移动
                board[move.first][move.second] = PLAYER_O;
                
                // 递归搜索
                int eval = alphaBetaSearch(board, depth - 1, alpha, beta, true);
                
                // 撤销移动
                board[move.first][move.second] = EMPTY;
                
                minEval = min(minEval, eval);
                beta = min(beta, eval);
                
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
    static pair<int, int> getBestMove(vector<vector<int>>& board, int depth, bool isMaximizingPlayer) {
        int bestValue = isMaximizingPlayer ? INT_MIN : INT_MAX;
        pair<int, int> bestMove = make_pair(-1, -1);
        vector<pair<int, int>> moves = generateMoves(board);
        
        // 启发式排序：优先考虑中心和角落位置
        sort(moves.begin(), moves.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
            // 中心位置优先
            if (a.first == 1 && a.second == 1) return true;
            if (b.first == 1 && b.second == 1) return false;
            // 角落位置次之
            if ((a.first == 0 || a.first == 2) && (a.second == 0 || a.second == 2)) return true;
            if ((b.first == 0 || b.first == 2) && (b.second == 0 || b.second == 2)) return false;
            return false;
        });
        
        for (const auto& move : moves) {
            // 执行移动
            board[move.first][move.second] = isMaximizingPlayer ? PLAYER_X : PLAYER_O;
            
            // 评估移动
            int moveValue = alphaBetaSearch(board, depth - 1, 
                                          isMaximizingPlayer ? INT_MIN : bestValue,
                                          isMaximizingPlayer ? bestValue : INT_MAX,
                                          !isMaximizingPlayer);
            
            // 撤销移动
            board[move.first][move.second] = EMPTY;
            
            // 更新最佳移动
            if (isMaximizingPlayer && moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            } else if (!isMaximizingPlayer && moveValue < bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
    
private:
    /**
     * 生成所有可能的移动
     * 
     * @param board 当前棋盘状态
     * @return 所有可能的移动列表
     */
    static vector<pair<int, int>> generateMoves(const vector<vector<int>>& board) {
        vector<pair<int, int>> moves;
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    moves.push_back(make_pair(i, j));
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
    static int evaluateGame(const vector<vector<int>>& board) {
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
        bool isFull = true;
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
    
public:
    /**
     * 打印棋盘
     * 
     * @param board 棋盘状态
     */
    static void printBoard(const vector<vector<int>>& board) {
        cout << "Current Board:" << endl;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                switch (board[i][j]) {
                    case EMPTY:
                        cout << ". ";
                        break;
                    case PLAYER_X:
                        cout << "X ";
                        break;
                    case PLAYER_O:
                        cout << "O ";
                        break;
                }
            }
            cout << endl;
        }
        cout << endl;
    }
    
    /**
     * 检查移动是否合法
     * 
     * @param board 棋盘状态
     * @param row 行索引
     * @param col 列索引
     * @return 是否合法
     */
    static bool isValidMove(const vector<vector<int>>& board, int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board[row][col] == EMPTY;
    }
};

// 测试方法
int main() {
    cout << "=== Alpha-Beta剪枝算法测试（井字棋） ===" << endl;
    
    // 初始化空棋盘
    vector<vector<int>> board(3, vector<int>(3, 0));
    
    // 简单测试：评估空棋盘
    int eval = AlphaBetaPruning::alphaBetaSearch(board, 9, INT_MIN, INT_MAX, true);
    cout << "空棋盘评估值: " << eval << endl;
    
    // 测试：X在中心位置
    board[1][1] = AlphaBetaPruning::PLAYER_X;
    AlphaBetaPruning::printBoard(board);
    eval = AlphaBetaPruning::alphaBetaSearch(board, 8, INT_MIN, INT_MAX, false);
    cout << "X在中心位置的评估值: " << eval << endl;
    
    // 获取最佳移动
    pair<int, int> bestMove = AlphaBetaPruning::getBestMove(board, 7, false);
    cout << "O的最佳移动: [" << bestMove.first << ", " << bestMove.second << "]" << endl;
    
    // 模拟一局游戏
    cout << "\n=== 模拟游戏 ===" << endl;
    // 重置棋盘
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            board[i][j] = AlphaBetaPruning::EMPTY;
        }
    }
    
    bool isXMove = true;
    int movesCount = 0;
    
    while (AlphaBetaPruning::evaluateGame(board) == -2 && movesCount < 9) {
        AlphaBetaPruning::printBoard(board);
        pair<int, int> move = AlphaBetaPruning::getBestMove(board, 9 - movesCount, isXMove);
        
        if (move.first != -1 && move.second != -1) {
            board[move.first][move.second] = isXMove ? AlphaBetaPruning::PLAYER_X : AlphaBetaPruning::PLAYER_O;
            cout << (isXMove ? "X" : "O") << " 下在 [" << move.first << ", " << move.second << "]" << endl;
            isXMove = !isXMove;
            movesCount++;
        } else {
            break;
        }
    }
    
    AlphaBetaPruning::printBoard(board);
    int result = AlphaBetaPruning::evaluateGame(board);
    if (result == AlphaBetaPruning::WIN_SCORE) {
        cout << "X 获胜！" << endl;
    } else if (result == AlphaBetaPruning::LOSE_SCORE) {
        cout << "O 获胜！" << endl;
    } else {
        cout << "平局！" << endl;
    }
    
    return 0;
}
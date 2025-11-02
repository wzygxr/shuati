#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

/**
 * LeetCode 289. 生命游戏 (Game of Life) - C++版本
 * 题目链接：https://leetcode.com/problems/game-of-life/
 * 
 * 算法思路：
 * 使用原地算法，通过特殊标记来同时保存当前状态和下一状态
 * 标记规则：
 *   - 如果细胞从活到死，标记为2（当前为活，下一状态为死）
 *   - 如果细胞从死到活，标记为-1（当前为死，下一状态为活）
 * 
 * 时间复杂度：O(m*n)
 * 空间复杂度：O(1)
 * 
 * 工程化考量：
 * 1. 边界检查：确保不越界访问
 * 2. 原地修改：避免额外空间使用
 * 3. 标记解析：两遍遍历确保正确性
 * 4. 异常处理：空输入检查
 */
class Solution {
public:
    void gameOfLife(vector<vector<int>>& board) {
        if (board.empty() || board[0].empty()) {
            return;
        }
        
        int m = board.size();
        int n = board[0].size();
        
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
            }
        }
    }
    
private:
    /**
     * 计算细胞周围活细胞数量（考虑标记）
     * @param board 细胞面板
     * @param i 当前细胞行索引
     * @param j 当前细胞列索引
     * @param m 面板行数
     * @param n 面板列数
     * @return 活细胞数量
     */
    int countLiveNeighbors(vector<vector<int>>& board, int i, int j, int m, int n) {
        int count = 0;
        
        // 8个方向的偏移量
        vector<vector<int>> directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (auto& dir : directions) {
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
    
public:
    /**
     * 使用额外空间的解法（非原地，更容易理解）
     * @param board 细胞面板
     */
    void gameOfLifeWithExtraSpace(vector<vector<int>>& board) {
        if (board.empty() || board[0].empty()) {
            return;
        }
        
        int m = board.size();
        int n = board[0].size();
        vector<vector<int>> nextBoard(m, vector<int>(n, 0));
        
        // 复制当前状态
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                nextBoard[i][j] = board[i][j];
            }
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
        board = nextBoard;
    }
    
private:
    /**
     * 简单计算活细胞数量（不考虑标记）
     */
    int countLiveNeighborsSimple(vector<vector<int>>& board, int i, int j, int m, int n) {
        int count = 0;
        vector<vector<int>> directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (auto& dir : directions) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            if (ni >= 0 && ni < m && nj >= 0 && nj < n && board[ni][nj] == 1) {
                count++;
            }
        }
        
        return count;
    }
};

/**
 * 打印面板
 */
void printBoard(const vector<vector<int>>& board) {
    for (const auto& row : board) {
        for (int cell : row) {
            cout << (cell == 1 ? "█ " : "· ");
        }
        cout << endl;
    }
    cout << endl;
}

/**
 * 单元测试
 */
int main() {
    Solution solution;
    
    // 测试用例1：闪烁器（Blinker）
    cout << "=== 测试用例1：闪烁器 ===" << endl;
    vector<vector<int>> board1 = {
        {0, 1, 0},
        {0, 1, 0},
        {0, 1, 0}
    };
    
    cout << "初始状态:" << endl;
    printBoard(board1);
    
    solution.gameOfLife(board1);
    cout << "下一代状态:" << endl;
    printBoard(board1);
    
    // 测试用例2：滑翔机（Glider）
    cout << "=== 测试用例2：滑翔机 ===" << endl;
    vector<vector<int>> board2 = {
        {0, 1, 0},
        {0, 0, 1},
        {1, 1, 1}
    };
    
    cout << "初始状态:" << endl;
    printBoard(board2);
    
    solution.gameOfLife(board2);
    cout << "下一代状态:" << endl;
    printBoard(board2);
    
    // 测试用例3：稳定图案（方块）
    cout << "=== 测试用例3：稳定图案 ===" << endl;
    vector<vector<int>> board3 = {
        {1, 1},
        {1, 1}
    };
    
    cout << "初始状态:" << endl;
    printBoard(board3);
    
    solution.gameOfLife(board3);
    cout << "下一代状态（应该保持不变）:" << endl;
    printBoard(board3);
    
    // 测试用例4：边界情况
    cout << "=== 测试用例4：单细胞 ===" << endl;
    vector<vector<int>> board4 = {{1}};
    
    cout << "初始状态:" << endl;
    printBoard(board4);
    
    solution.gameOfLife(board4);
    cout << "下一代状态（应该死亡）:" << endl;
    printBoard(board4);
    
    // 测试用例5：使用额外空间版本
    cout << "=== 测试用例5：额外空间版本 ===" << endl;
    vector<vector<int>> board5 = {
        {0, 0, 0, 0, 0},
        {0, 0, 1, 0, 0},
        {0, 0, 1, 0, 0},
        {0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0}
    };
    
    cout << "初始状态:" << endl;
    printBoard(board5);
    
    solution.gameOfLifeWithExtraSpace(board5);
    cout << "下一代状态（额外空间版本）:" << endl;
    printBoard(board5);
    
    return 0;
}
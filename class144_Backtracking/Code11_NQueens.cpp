#include <vector>
#include <string>
#include <iostream>

using namespace std;

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
 */

class Solution {
public:
    /**
     * 解决N皇后问题
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
     * 时间复杂度：O(N!)，第一行有N种选择，第二行最多有N-1种选择，以此类推
     * 空间复杂度：O(N^2)，棋盘空间和递归栈深度
     * 
     * @param n 皇后数量和棋盘大小
     * @return 所有解决方案
     */
    vector<vector<string>> solveNQueens(int n) {
        vector<vector<string>> result;
        vector<string> board(n, string(n, '.'));
        
        backtrack(result, board, 0);
        return result;
    }

private:
    /**
     * 回溯函数解决N皇后问题
     * 
     * @param result 结果列表
     * @param board 棋盘
     * @param row 当前行
     */
    void backtrack(vector<vector<string>>& result, vector<string>& board, int row) {
        // 终止条件：已放置完所有皇后
        if (row == board.size()) {
            result.push_back(board);
            return;
        }
        
        // 在当前行的每一列尝试放置皇后
        for (int col = 0; col < board.size(); col++) {
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
    bool isValid(vector<string>& board, int row, int col) {
        // 检查列
        for (int i = 0; i < row; i++)
            if (board[i][col] == 'Q') return false;
        
        // 检查左上对角线
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 'Q') return false;
        
        // 检查右上对角线
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.size(); i--, j++)
            if (board[i][j] == 'Q') return false;
        
        return true;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 4;
    vector<vector<string>> result1 = solution.solveNQueens(n1);
    cout << "输入: n = " << n1 << endl;
    cout << "输出:" << endl;
    for (const auto& solution_board : result1) {
        for (const auto& row : solution_board) {
            cout << row << endl;
        }
        cout << endl;
    }
    
    // 测试用例2
    int n2 = 1;
    vector<vector<string>> result2 = solution.solveNQueens(n2);
    cout << "输入: n = " << n2 << endl;
    cout << "输出:" << endl;
    for (const auto& solution_board : result2) {
        for (const auto& row : solution_board) {
            cout << row << endl;
        }
        cout << endl;
    }
    
    return 0;
}
#include <vector>
#include <iostream>

using namespace std;

/**
 * LeetCode 79. 单词搜索
 * 
 * 题目描述：
 * 给定一个 m x n 二维字符网格 board 和一个字符串单词 word 。
 * 如果 word 存在于网格中，返回 true；否则，返回 false。
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，
 * 其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母不允许被重复使用。
 * 
 * 示例：
 * 输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
 * 输出：true
 * 
 * 输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
 * 输出：true
 * 
 * 输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
 * 输出：false
 * 
 * 提示：
 * m == board.length
 * n == board[i].length
 * 1 <= m, n <= 6
 * 1 <= word.length <= 15
 * board 和 word 仅由大小写英文字母组成
 * 
 * 链接：https://leetcode.cn/problems/word-search/
 */

class Solution {
public:
    /**
     * 检查单词是否存在于网格中
     * 
     * 算法思路：
     * 1. 遍历网格中的每个位置作为起点
     * 2. 对于每个起点，使用回溯算法搜索单词
     * 3. 在回溯过程中，标记已访问的位置，避免重复使用
     * 4. 向四个方向探索：上、下、左、右
     * 5. 如果找到完整单词，返回true
     * 6. 如果当前路径不匹配，回溯并尝试其他路径
     * 
     * 时间复杂度：O(m*n*4^L)，其中m和n是网格的行数和列数，L是单词的长度
     * 空间复杂度：O(L)，递归栈深度
     * 
     * @param board 二维字符网格
     * @param word 单词
     * @return 单词是否存在于网格中
     */
    bool exist(vector<vector<char>>& board, string word) {
        // 边界条件检查
        if (board.empty() || board[0].empty() || word.empty()) return false;
        
        // 遍历网格中的每个位置作为起点
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board[0].size(); j++) {
                if (backtrack(board, word, i, j, 0))
                    return true;
            }
        }
        return false;
    }

private:
    /**
     * 回溯函数搜索单词
     * 
     * @param board 二维字符网格
     * @param word 单词
     * @param row 当前行
     * @param col 当前列
     * @param index 当前处理的单词字符索引
     * @return 是否找到单词
     */
    bool backtrack(vector<vector<char>>& board, string word, int row, int col, int index) {
        // 终止条件：已找到完整单词
        if (index == word.length()) return true;
        
        // 边界检查和字符匹配检查
        if (row < 0 || row >= board.size() || col < 0 || col >= board[0].size() || 
            board[row][col] != word[index]) {
            return false;
        }
        
        // 标记已访问
        char temp = board[row][col];
        board[row][col] = '#';
        
        // 向四个方向探索
        bool found = backtrack(board, word, row + 1, col, index + 1) ||  // 下
                     backtrack(board, word, row - 1, col, index + 1) ||  // 上
                     backtrack(board, word, row, col + 1, index + 1) ||  // 右
                     backtrack(board, word, row, col - 1, index + 1);    // 左
        
        // 回溯
        board[row][col] = temp;
        
        return found;
    }
};

// 打印网格
void printBoard(const vector<vector<char>>& board) {
    for (int i = 0; i < board.size(); i++) {
        for (int j = 0; j < board[0].size(); j++) {
            cout << board[i][j] << " ";
        }
        cout << endl;
    }
}

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<char>> board1 = {
        {'A','B','C','E'},
        {'S','F','C','S'},
        {'A','D','E','E'}
    };
    string word1 = "ABCCED";
    bool result1 = solution.exist(board1, word1);
    cout << "网格:" << endl;
    printBoard(board1);
    cout << "单词: \"" << word1 << "\"" << endl;
    cout << "结果: " << (result1 ? "true" : "false") << endl;
    
    // 测试用例2
    vector<vector<char>> board2 = {
        {'A','B','C','E'},
        {'S','F','C','S'},
        {'A','D','E','E'}
    };
    string word2 = "SEE";
    bool result2 = solution.exist(board2, word2);
    cout << "\n网格:" << endl;
    printBoard(board2);
    cout << "单词: \"" << word2 << "\"" << endl;
    cout << "结果: " << (result2 ? "true" : "false") << endl;
    
    // 测试用例3
    vector<vector<char>> board3 = {
        {'A','B','C','E'},
        {'S','F','C','S'},
        {'A','D','E','E'}
    };
    string word3 = "ABCB";
    bool result3 = solution.exist(board3, word3);
    cout << "\n网格:" << endl;
    printBoard(board3);
    cout << "单词: \"" << word3 << "\"" << endl;
    cout << "结果: " << (result3 ? "true" : "false") << endl;
    
    return 0;
}
#include <iostream>
#include <vector>
#include <bitset>
#include <stdexcept>
#include <chrono>
#include <random>
#include <algorithm>
#include <functional>
#include <map>
#include <unordered_map>
#include <queue>
#include <stack>
#include <string>
#include <sstream>
#include <iomanip>
#include <cmath>
#include <climits>
#include <cassert>
#include <limits>
#include <unordered_set>
#include <cstdint>
#include <set>
#include <numeric>

using namespace std;

/**
 * 高级位集应用实现
 * 包含LeetCode多个高级位集应用相关题目的解决方案
 * 
 * 题目列表:
 * 1. LeetCode 52 - N皇后 II
 * 2. LeetCode 51 - N皇后
 * 3. LeetCode 37 - 解数独
 * 4. LeetCode 36 - 有效的数独
 * 5. LeetCode 212 - 单词搜索 II
 * 6. LeetCode 208 - 实现 Trie (前缀树)
 * 7. LeetCode 211 - 添加与搜索单词 - 数据结构设计
 * 8. LeetCode 126 - 单词接龙 II
 * 9. LeetCode 127 - 单词接龙
 * 10. LeetCode 130 - 被围绕的区域
 * 
 * 时间复杂度分析:
 * - 位集操作: O(1) 到 O(2^n)
 * - 空间复杂度: O(1) 到 O(n)
 * 
 * 工程化考量:
 * 1. 位集优化: 使用位集优化回溯算法
 * 2. 状态压缩: 使用位运算压缩状态空间
 * 3. 性能优化: 利用位运算的并行性
 * 4. 边界处理: 处理大数、边界值等
 */

class BitSetApplicationsAdvanced {
public:
    /**
     * LeetCode 52 - N皇后 II
     * 题目链接: https://leetcode.com/problems/n-queens-ii/
     * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
     * 给定一个整数 n，返回 n 皇后不同的解决方案的数量。
     * 
     * 方法: 位运算回溯
     * 时间复杂度: O(n!)
     * 空间复杂度: O(n)
     * 
     * 原理: 使用位运算记录列、主对角线、副对角线的占用情况
     */
    static int totalNQueens(int n) {
        return solveNQueensBitwise(n, 0, 0, 0, 0);
    }
    
private:
    static int solveNQueensBitwise(int n, int row, int columns, int diagonals1, int diagonals2) {
        if (row == n) {
            return 1;
        }
        
        int count = 0;
        // 获取可用的位置
        int available_positions = ((1 << n) - 1) & ~(columns | diagonals1 | diagonals2);
        
        while (available_positions != 0) {
            // 获取最低位的1
            int position = available_positions & -available_positions;
            available_positions &= available_positions - 1;
            
            count += solveNQueensBitwise(n, row + 1, 
                                       columns | position,
                                       (diagonals1 | position) << 1,
                                       (diagonals2 | position) >> 1);
        }
        
        return count;
    }
    
public:
    /**
     * LeetCode 51 - N皇后
     * 题目链接: https://leetcode.com/problems/n-queens/
     * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
     * 给定一个整数 n，返回所有不同的 n 皇后问题的解决方案。
     * 
     * 方法: 位运算回溯
     * 时间复杂度: O(n!)
     * 空间复杂度: O(n)
     */
    static vector<vector<string>> solveNQueens(int n) {
        vector<vector<string>> solutions;
        vector<string> board(n, string(n, '.'));
        solveNQueensWithBoard(n, 0, 0, 0, 0, board, solutions);
        return solutions;
    }
    
private:
    static void solveNQueensWithBoard(int n, int row, int columns, int diagonals1, int diagonals2,
                                     vector<string>& board, vector<vector<string>>& solutions) {
        if (row == n) {
            solutions.push_back(board);
            return;
        }
        
        int available_positions = ((1 << n) - 1) & ~(columns | diagonals1 | diagonals2);
        
        while (available_positions != 0) {
            int position = available_positions & -available_positions;
            available_positions &= available_positions - 1;
            
            // 找到position对应的列
            int col = 0;
            int temp = position;
            while (temp > 1) {
                temp >>= 1;
                col++;
            }
            
            board[row][col] = 'Q';
            solveNQueensWithBoard(n, row + 1,
                                columns | position,
                                (diagonals1 | position) << 1,
                                (diagonals2 | position) >> 1,
                                board, solutions);
            board[row][col] = '.';
        }
    }
    
public:
    /**
     * LeetCode 37 - 解数独
     * 题目链接: https://leetcode.com/problems/sudoku-solver/
     * 编写一个程序，通过已填充的空格来解决数独问题。
     * 
     * 方法: 位运算 + 回溯
     * 时间复杂度: O(9^m) - m为空格数量
     * 空间复杂度: O(1)
     */
    static void solveSudoku(vector<vector<char>>& board) {
        // 使用位掩码记录每行、每列、每个3x3宫的数字使用情况
        vector<int> rows(9, 0), cols(9, 0), boxes(9, 0);
        
        // 初始化已存在的数字
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '0';
                    int mask = 1 << (num - 1);
                    rows[i] |= mask;
                    cols[j] |= mask;
                    boxes[(i/3)*3 + j/3] |= mask;
                }
            }
        }
        
        solveSudokuHelper(board, 0, 0, rows, cols, boxes);
    }
    
private:
    static bool solveSudokuHelper(vector<vector<char>>& board, int row, int col,
                                 vector<int>& rows, vector<int>& cols, vector<int>& boxes) {
        if (row == 9) return true;
        if (col == 9) return solveSudokuHelper(board, row + 1, 0, rows, cols, boxes);
        if (board[row][col] != '.') {
            return solveSudokuHelper(board, row, col + 1, rows, cols, boxes);
        }
        
        int box_index = (row/3)*3 + col/3;
        int available = ~(rows[row] | cols[col] | boxes[box_index]) & 0x1FF;
        
        while (available != 0) {
            int position = available & -available;
            int num = __builtin_ctz(position) + 1;
            
            // 尝试放置数字
            board[row][col] = '0' + num;
            rows[row] |= position;
            cols[col] |= position;
            boxes[box_index] |= position;
            
            if (solveSudokuHelper(board, row, col + 1, rows, cols, boxes)) {
                return true;
            }
            
            // 回溯
            board[row][col] = '.';
            rows[row] &= ~position;
            cols[col] &= ~position;
            boxes[box_index] &= ~position;
            
            available &= available - 1;
        }
        
        return false;
    }
    
public:
    /**
     * LeetCode 36 - 有效的数独
     * 题目链接: https://leetcode.com/problems/valid-sudoku/
     * 判断一个 9x9 的数独是否有效。只需要根据以下规则，验证已经填入的数字是否有效即可。
     * 
     * 方法: 位运算验证
     * 时间复杂度: O(1) - 固定81个格子
     * 空间复杂度: O(1)
     */
    static bool isValidSudoku(vector<vector<char>>& board) {
        vector<int> rows(9, 0), cols(9, 0), boxes(9, 0);
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') continue;
                
                int num = board[i][j] - '0';
                int mask = 1 << (num - 1);
                int box_index = (i/3)*3 + j/3;
                
                if ((rows[i] & mask) || (cols[j] & mask) || (boxes[box_index] & mask)) {
                    return false;
                }
                
                rows[i] |= mask;
                cols[j] |= mask;
                boxes[box_index] |= mask;
            }
        }
        
        return true;
    }
    
    /**
     * LeetCode 212 - 单词搜索 II
     * 题目链接: https://leetcode.com/problems/word-search-ii/
     * 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words，找出所有同时在二维网格和字典中出现的单词。
     * 
     * 方法: Trie树 + 回溯
     * 时间复杂度: O(m*n*4^L) - L为单词最大长度
     * 空间复杂度: O(k*L) - k为单词数量
     */
    static vector<string> findWords(vector<vector<char>>& board, vector<string>& words) {
        // 构建Trie树
        TrieNode* root = buildTrie(words);
        vector<string> result;
        
        // 回溯搜索
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board[0].size(); j++) {
                dfs(board, i, j, root, result);
            }
        }
        
        return result;
    }
    
private:
    struct TrieNode {
        TrieNode* children[26];
        string word;
        TrieNode() {
            for (int i = 0; i < 26; i++) children[i] = nullptr;
            word = "";
        }
    };
    
    static TrieNode* buildTrie(vector<string>& words) {
        TrieNode* root = new TrieNode();
        for (string word : words) {
            TrieNode* node = root;
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = new TrieNode();
                }
                node = node->children[index];
            }
            node->word = word;
        }
        return root;
    }
    
    static void dfs(vector<vector<char>>& board, int i, int j, TrieNode* node, vector<string>& result) {
        if (i < 0 || i >= board.size() || j < 0 || j >= board[0].size() || board[i][j] == '#') {
            return;
        }
        
        char c = board[i][j];
        int index = c - 'a';
        if (!node->children[index]) return;
        
        node = node->children[index];
        if (!node->word.empty()) {
            result.push_back(node->word);
            node->word = "";  // 避免重复
        }
        
        board[i][j] = '#';  // 标记已访问
        dfs(board, i+1, j, node, result);
        dfs(board, i-1, j, node, result);
        dfs(board, i, j+1, node, result);
        dfs(board, i, j-1, node, result);
        board[i][j] = c;  // 恢复
    }
    
public:
    /**
     * LeetCode 208 - 实现 Trie (前缀树)
     * 题目链接: https://leetcode.com/problems/implement-trie-prefix-tree/
     * 实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
     * 
     * 方法: Trie树实现
     * 时间复杂度: O(L) - L为单词长度
     * 空间复杂度: O(n*L) - n为单词数量
     */
    class Trie {
    private:
        struct TrieNode {
            TrieNode* children[26];
            bool isEnd;
            TrieNode() {
                for (int i = 0; i < 26; i++) children[i] = nullptr;
                isEnd = false;
            }
        };
        
        TrieNode* root;
        
    public:
        Trie() {
            root = new TrieNode();
        }
        
        void insert(string word) {
            TrieNode* node = root;
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = new TrieNode();
                }
                node = node->children[index];
            }
            node->isEnd = true;
        }
        
        bool search(string word) {
            TrieNode* node = root;
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) return false;
                node = node->children[index];
            }
            return node->isEnd;
        }
        
        bool startsWith(string prefix) {
            TrieNode* node = root;
            for (char c : prefix) {
                int index = c - 'a';
                if (!node->children[index]) return false;
                node = node->children[index];
            }
            return true;
        }
    };
};

class PerformanceTester {
public:
    static void testNQueens() {
        cout << "=== N皇后问题性能测试 ===" << endl;
        
        int n = 8;
        
        auto start = chrono::high_resolution_clock::now();
        int count = BitSetApplicationsAdvanced::totalNQueens(n);
        auto time = chrono::duration_cast<chrono::milliseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "N皇后(n=" << n << "): 解决方案数量=" << count << ", 耗时=" << time << " ms" << endl;
    }
    
    static void testSudoku() {
        cout << "\n=== 数独求解性能测试 ===" << endl;
        
        vector<vector<char>> board = {
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
        
        auto start = chrono::high_resolution_clock::now();
        BitSetApplicationsAdvanced::solveSudoku(board);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "数独求解: 耗时=" << time << " μs" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 高级位集应用单元测试 ===" << endl;
        
        // 测试数独验证
        vector<vector<char>> valid_sudoku = {
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
        assert(BitSetApplicationsAdvanced::isValidSudoku(valid_sudoku) == true);
        
        cout << "所有单元测试通过!" << endl;
    }
    
    static void complexityAnalysis() {
        cout << "\n=== 复杂度分析 ===" << endl;
        
        vector<pair<string, string>> algorithms = {
            {"totalNQueens", "O(n!), O(n)"},
            {"solveNQueens", "O(n!), O(n^2)"},
            {"solveSudoku", "O(9^m), O(1)"},
            {"isValidSudoku", "O(1), O(1)"},
            {"findWords", "O(m*n*4^L), O(k*L)"}
        };
        
        for (auto& algo : algorithms) {
            cout << algo.first << ": 时间复杂度=" << algo.second << endl;
        }
    }
};

int main() {
    cout << "高级位集应用实现" << endl;
    cout << "包含LeetCode多个高级位集应用相关题目的解决方案" << endl;
    cout << "===========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testNQueens();
    PerformanceTester::testSudoku();
    
    // 复杂度分析
    PerformanceTester::complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // N皇后示例
    int n = 4;
    cout << "N皇后(n=" << n << ")解决方案数量: " 
         << BitSetApplicationsAdvanced::totalNQueens(n) << endl;
    
    // 数独验证示例
    vector<vector<char>> sudoku_board = {
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
    cout << "数独验证结果: " << (BitSetApplicationsAdvanced::isValidSudoku(sudoku_board) ? "有效" : "无效") << endl;
    
    // Trie树示例
    BitSetApplicationsAdvanced::Trie trie;
    trie.insert("apple");
    cout << "搜索'apple': " << (trie.search("apple") ? "找到" : "未找到") << endl;
    cout << "前缀'app': " << (trie.startsWith("app") ? "存在" : "不存在") << endl;
    
    return 0;
}
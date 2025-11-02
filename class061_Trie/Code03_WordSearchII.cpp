// LeetCode 212. 单词搜索 II - C++实现
// 题目描述：在二维字符网格中查找所有单词。
// 测试链接：https://leetcode.cn/problems/word-search-ii/

#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
using namespace std;

/**
 * 单词搜索II问题 - 使用前缀树优化
 * 
 * 算法思路：
 * 1. 先将所有待查找的单词构建成前缀树
 * 2. 从网格的每个位置开始深度优先搜索
 * 3. 在搜索过程中，利用前缀树剪枝，避免无效搜索
 * 4. 找到单词后，将其加入结果集
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(M)，其中M是所有单词的字符总数
 * - 网格搜索：O(N*M*4^L)，其中N是网格中的单元格数量，L是单词的最大长度
 * - 总体时间复杂度：O(M + N*4^L)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(M)
 * - 递归栈空间：O(L)
 * - 总体空间复杂度：O(M + L)
 * 
 * 是否最优解：是
 * 理由：前缀树能够有效减少无效搜索路径，提高搜索效率
 * 
 * 工程化考虑：
 * 1. 边界条件处理：处理空网格、空单词列表等情况
 * 2. 避免重复计算：使用哈希集合存储结果
 * 3. 路径回溯：使用标记数组或位操作避免重复访问
 */

class TrieNode {
public:
    // 子节点数组
    TrieNode* children[26];
    // 存储完整单词，如果当前节点是单词结尾
    string word;
    
    /**
     * 初始化前缀树节点
     */
    TrieNode() {
        word = "";
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
    }
    
    /**
     * 析构函数，释放子节点内存
     */
    ~TrieNode() {
        for (int i = 0; i < 26; i++) {
            if (children[i]) {
                delete children[i];
            }
        }
    }
};

class WordSearch {
private:
    // 方向数组：上、右、下、左
    int dirs[4][2] = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    
    /**
     * 深度优先搜索函数
     * 
     * @param board 字符网格
     * @param row 当前行
     * @param col 当前列
     * @param node 当前前缀树节点
     * @param result 结果集
     */
    void dfs(vector<vector<char>>& board, int row, int col, TrieNode* node, unordered_set<string>& result) {
        // 检查边界条件
        if (row < 0 || row >= board.size() || col < 0 || col >= board[0].size() || board[row][col] == '#') {
            return;
        }
        
        char c = board[row][col];
        int index = c - 'a';
        
        // 如果当前字符不存在于前缀树中，直接返回
        if (!node->children[index]) {
            return;
        }
        
        // 移动到下一个前缀树节点
        node = node->children[index];
        
        // 如果当前节点是某个单词的结尾，将单词加入结果集
        if (!node->word.empty()) {
            result.insert(node->word);
        }
        
        // 标记当前位置为已访问
        char temp = board[row][col];
        board[row][col] = '#';
        
        // 向四个方向搜索
        for (int i = 0; i < 4; i++) {
            int newRow = row + dirs[i][0];
            int newCol = col + dirs[i][1];
            dfs(board, newRow, newCol, node, result);
        }
        
        // 恢复当前位置
        board[row][col] = temp;
    }
    
public:
    /**
     * 在字符网格中查找所有单词
     * 
     * @param board 字符网格
     * @param words 单词列表
     * @return 找到的单词列表
     */
    vector<string> findWords(vector<vector<char>>& board, vector<string>& words) {
        if (board.empty() || board[0].empty() || words.empty()) {
            return {};
        }
        
        // 构建前缀树
        TrieNode* root = new TrieNode();
        for (const string& word : words) {
            TrieNode* node = root;
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = new TrieNode();
                }
                node = node->children[index];
            }
            node->word = word;  // 存储完整单词
        }
        
        unordered_set<string> resultSet;
        int rows = board.size();
        int cols = board[0].size();
        
        // 从每个位置开始搜索
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfs(board, i, j, root, resultSet);
            }
        }
        
        // 清理前缀树内存
        delete root;
        
        // 转换为vector返回
        return vector<string>(resultSet.begin(), resultSet.end());
    }
};

// 测试代码
int main() {
    WordSearch solution;
    
    // 测试用例1
    vector<vector<char>> board1 = {
        {'o', 'a', 'a', 'n'},
        {'e', 't', 'a', 'e'},
        {'i', 'h', 'k', 'r'},
        {'i', 'f', 'l', 'v'}
    };
    vector<string> words1 = {"oath", "pea", "eat", "rain"};
    vector<string> result1 = solution.findWords(board1, words1);
    
    cout << "测试用例1结果: [";
    for (size_t i = 0; i < result1.size(); i++) {
        cout << "\"" << result1[i] << "\"";
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例2
    vector<vector<char>> board2 = {
        {'a', 'b'},
        {'c', 'd'}
    };
    vector<string> words2 = {"abcb"};
    vector<string> result2 = solution.findWords(board2, words2);
    
    cout << "测试用例2结果: [";
    for (size_t i = 0; i < result2.size(); i++) {
        cout << "\"" << result2[i] << "\"";
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    return 0;
}
#include <vector>
#include <string>
#include <iostream>
#include <unordered_set>

using namespace std;

/**
 * LeetCode 212. 单词搜索 II
 * 
 * 题目描述：
 * 给定一个二维网格 board 和一个字典中的单词列表 words，找出所有同时在二维网格和字典中出现的单词。
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母在一个单词中不允许被重复使用。
 * 
 * 示例：
 * 输入：
 * board = [
 *   ['o','a','a','n'],
 *   ['e','t','a','e'],
 *   ['i','h','k','r'],
 *   ['i','f','l','v']
 * ]
 * words = ["oath","pea","eat","rain"]
 * 输出：["eat","oath"]
 * 
 * 提示：
 * m == board.length
 * n == board[i].length
 * 1 <= m, n <= 12
 * 1 <= words.length <= 3 * 10^4
 * 1 <= words[i].length <= 10
 * board 和 words[i] 仅由小写英文字母组成
 * words 中的所有字符串互不相同
 * 
 * 链接：https://leetcode.cn/problems/word-search-ii/
 */

// Trie树节点定义
struct TrieNode {
    TrieNode* children[26]; // 26个小写字母
    string word; // 存储完整单词，非空表示这是一个单词的结尾
    
    // 构造函数
    TrieNode() : word("") {
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
    }
    
    // 析构函数
    ~TrieNode() {
        for (int i = 0; i < 26; i++) {
            if (children[i]) {
                delete children[i];
            }
        }
    }
};

class Solution {
public:
    /**
     * 查找二维网格中所有存在于字典中的单词
     * 
     * 算法思路：
     * 1. 构建Trie树，将所有单词插入Trie中
     * 2. 对二维网格中的每个单元格作为起点，进行深度优先搜索
     * 3. 使用Trie树来剪枝无效的搜索路径
     * 4. 找到单词后，将其加入结果集并从Trie中移除，避免重复添加
     * 
     * 时间复杂度：O(M*N*4^L)，其中M和N是网格的行数和列数，L是单词的最大长度。每个单元格最多被访问4^L次。
     * 空间复杂度：O(K)，其中K是所有单词的字符总数，用于存储Trie树。
     * 
     * @param board 二维字符网格
     * @param words 单词列表
     * @return 网格中存在的单词列表
     */
    vector<string> findWords(vector<vector<char>>& board, vector<string>& words) {
        vector<string> result;
        if (board.empty() || board[0].empty() || words.empty()) {
            return result;
        }
        
        // 构建Trie树
        TrieNode* root = buildTrie(words);
        
        // 遍历网格中的每个单元格作为起点
        int m = board.size();
        int n = board[0].size();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(board, i, j, root, result);
            }
        }
        
        // 释放Trie树内存
        delete root;
        
        return result;
    }

private:
    /**
     * 构建Trie树
     * 
     * @param words 单词列表
     * @return Trie树的根节点
     */
    TrieNode* buildTrie(vector<string>& words) {
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
            node->word = word; // 标记单词结尾
        }
        return root;
    }
    
    /**
     * 深度优先搜索
     * 
     * @param board 二维字符网格
     * @param i 当前行索引
     * @param j 当前列索引
     * @param node 当前Trie节点
     * @param result 结果列表
     */
    void dfs(vector<vector<char>>& board, int i, int j, TrieNode* node, vector<string>& result) {
        // 检查边界条件
        if (i < 0 || i >= board.size() || j < 0 || j >= board[0].size() || board[i][j] == '#') {
            return;
        }
        
        char c = board[i][j];
        int index = c - 'a';
        
        // 如果当前字符不在Trie的子节点中，剪枝
        if (!node->children[index]) {
            return;
        }
        
        // 移动到下一个Trie节点
        node = node->children[index];
        
        // 如果找到一个单词
        if (!node->word.empty()) {
            result.push_back(node->word);
            node->word = ""; // 移除单词，避免重复添加
        }
        
        // 标记当前单元格为已访问
        board[i][j] = '#';
        
        // 向四个方向搜索
        dfs(board, i + 1, j, node, result);
        dfs(board, i - 1, j, node, result);
        dfs(board, i, j + 1, node, result);
        dfs(board, i, j - 1, node, result);
        
        // 回溯：恢复当前单元格
        board[i][j] = c;
    }
};

// 辅助函数：打印结果
void printResult(const vector<string>& result) {
    cout << "[";
    for (size_t i = 0; i < result.size(); i++) {
        cout << "\"" << result[i] << "\"";
        if (i < result.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<char>> board1 = {
        {'o','a','a','n'},
        {'e','t','a','e'},
        {'i','h','k','r'},
        {'i','f','l','v'}
    };
    vector<string> words1 = {"oath","pea","eat","rain"};
    vector<string> result1 = solution.findWords(board1, words1);
    cout << "输入:" << endl;
    cout << "board = [[o,a,a,n],[e,t,a,e],[i,h,k,r],[i,f,l,v]]" << endl;
    cout << "words = [oath,pea,eat,rain]" << endl;
    cout << "输出: ";
    printResult(result1);
    
    // 测试用例2
    vector<vector<char>> board2 = {{'a','b'},{'c','d'}};
    vector<string> words2 = {"abcb"};
    vector<string> result2 = solution.findWords(board2, words2);
    cout << "\n输入:" << endl;
    cout << "board = [[a,b],[c,d]]" << endl;
    cout << "words = [abcb]" << endl;
    cout << "输出: ";
    printResult(result2);
    
    return 0;
}
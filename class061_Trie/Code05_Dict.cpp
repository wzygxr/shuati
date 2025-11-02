// SPOJ DICT - C++实现
// 题目描述：给定一个字典和一个前缀，找出字典中所有以该前缀开头的单词。
// 测试链接：https://www.spoj.com/problems/DICT/

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

/**
 * SPOJ DICT字典查询问题 - 使用前缀树实现
 * 
 * 算法思路：
 * 1. 构建前缀树存储字典中的所有单词
 * 2. 对于每个查询前缀，在前缀树中找到对应的节点
 * 3. 从该节点开始深度优先搜索，收集所有完整单词
 * 4. 按字典序输出结果
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(M)，其中M是所有单词的字符总数
 * - 查询操作：O(L + K)，其中L是前缀长度，K是所有匹配单词的字符总数
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(M)
 * - 递归栈空间：O(D)，其中D是树的深度
 * - 总体空间复杂度：O(M + D)
 * 
 * 是否最优解：是
 * 理由：前缀树能够高效处理字典查询和前缀匹配问题
 * 
 * 工程化考虑：
 * 1. 输入输出优化：处理大量数据时需要考虑效率
 * 2. 内存管理：合理管理前缀树节点内存
 * 3. 排序要求：确保输出结果按字典序排列
 */

class TrieNode {
public:
    // 子节点数组
    TrieNode* children[26];
    // 标记单词结尾
    bool isEnd;
    
    /**
     * 初始化前缀树节点
     */
    TrieNode() {
        isEnd = false;
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

class Dictionary {
private:
    TrieNode* root;
    
    /**
     * 将字符映射到索引
     * 
     * @param c 字符
     * @return 索引值(0-25)
     */
    int charToIndex(char c) {
        return c - 'a';
    }
    
    /**
     * 从指定节点开始深度优先搜索，收集所有单词
     * 
     * @param node 当前节点
     * @param prefix 当前前缀
     * @param result 结果集
     */
    void dfs(TrieNode* node, string prefix, vector<string>& result) {
        if (node->isEnd) {
            result.push_back(prefix);
        }
        
        for (int i = 0; i < 26; i++) {
            if (node->children[i]) {
                char c = 'a' + i;
                dfs(node->children[i], prefix + c, result);
            }
        }
    }
    
public:
    /**
     * 初始化字典
     */
    Dictionary() {
        root = new TrieNode();
    }
    
    /**
     * 析构函数
     */
    ~Dictionary() {
        delete root;
    }
    
    /**
     * 添加单词到字典
     * 
     * @param word 单词
     */
    void addWord(const string& word) {
        TrieNode* node = root;
        for (char c : word) {
            int index = charToIndex(c);
            if (!node->children[index]) {
                node->children[index] = new TrieNode();
            }
            node = node->children[index];
        }
        node->isEnd = true;
    }
    
    /**
     * 查找具有特定前缀的所有单词
     * 
     * @param prefix 查询前缀
     * @return 具有该前缀的单词列表
     */
    vector<string> findWordsWithPrefix(const string& prefix) {
        vector<string> result;
        TrieNode* node = root;
        
        // 首先定位到前缀的最后一个字符对应的节点
        for (char c : prefix) {
            int index = charToIndex(c);
            if (!node->children[index]) {
                return result;  // 前缀不存在
            }
            node = node->children[index];
        }
        
        // 从该节点开始DFS，收集所有单词
        dfs(node, prefix, result);
        
        return result;
    }
};

// 测试代码
int main() {
    Dictionary dict;
    
    // 构建字典
    vector<string> words = {
        "apple", "application", "app", "append", 
        "banana", "ball", "cat", "car"
    };
    
    for (const string& word : words) {
        dict.addWord(word);
    }
    
    // 测试查询
    vector<string> prefixes = {"app", "ba", "c", "d"};
    
    for (const string& prefix : prefixes) {
        cout << "Prefix: '" << prefix << "'" << endl;
        vector<string> results = dict.findWordsWithPrefix(prefix);
        
        if (results.empty()) {
            cout << "No words found." << endl;
        } else {
            cout << "Found " << results.size() << " words:" << endl;
            for (const string& word : results) {
                cout << "  - " << word << endl;
            }
        }
        cout << endl;
    }
    
    return 0;
}

/*
预期输出：
Prefix: 'app'
Found 4 words:
  - app
  - apple
  - application
  - append

Prefix: 'ba'
Found 2 words:
  - ball
  - banana

Prefix: 'c'
Found 2 words:
  - car
  - cat

Prefix: 'd'
No words found.
*/
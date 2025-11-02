#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>

using namespace std;

/**
 * HackerRank No Prefix Set
 * 
 * 题目描述：
 * 给定一个字符串集合，判断是否存在一个字符串是另一个字符串的前缀。
 * 如果存在，输出"BAD SET"和第一个违反规则的字符串；否则输出"GOOD SET"。
 * 
 * 解题思路：
 * 1. 使用前缀树存储字符串
 * 2. 在插入过程中检查是否存在前缀关系
 * 3. 如果在插入一个字符串时，发现路径上有已经标记为完整字符串的节点，
 *    或者插入完成后当前节点有子节点，说明存在前缀关系
 * 
 * 时间复杂度：O(N*M)，其中N是字符串数量，M是平均字符串长度
 * 空间复杂度：O(N*M)
 */

// Trie树节点结构
struct TrieNode {
    unordered_map<char, TrieNode*> children; // 子节点映射
    bool isEnd;                              // 标记是否为一个完整字符串的结尾
    
    TrieNode() {
        isEnd = false;
    }
};

TrieNode* root;

/**
 * 初始化Trie树
 */
void init() {
    root = new TrieNode();
}

/**
 * 向Trie树中插入一个字符串，并检查是否存在前缀关系
 * @param str 要插入的字符串
 * @return 如果存在前缀关系返回true，否则返回false
 */
bool insert(const string& str) {
    if (str.empty()) {
        return false;
    }
    
    TrieNode* node = root;
    for (char c : str) {
        // 如果子节点不存在，创建新节点
        if (node->children.find(c) == node->children.end()) {
            node->children[c] = new TrieNode();
        }
        
        node = node->children[c];
        
        // 如果当前节点已经是某个完整字符串的结尾，说明当前字符串是另一个字符串的前缀
        if (node->isEnd) {
            return true;
        }
    }
    
    // 标记当前节点为完整字符串的结尾
    node->isEnd = true;
    
    // 检查当前节点是否有子节点，如果有说明存在前缀关系
    if (!node->children.empty()) {
        return true;
    }
    
    return false;
}

int main() {
    int n;
    cin >> n; // 字符串数量
    cin.ignore(); // 消费换行符
    
    init(); // 初始化Trie树
    
    vector<string> strings(n);
    for (int i = 0; i < n; i++) {
        getline(cin, strings[i]);
    }
    
    bool goodSet = true;
    string badString = "";
    
    for (const string& str : strings) {
        if (insert(str)) {
            goodSet = false;
            badString = str;
            break;
        }
    }
    
    if (goodSet) {
        cout << "GOOD SET" << endl;
    } else {
        cout << "BAD SET" << endl;
        cout << badString << endl;
    }
    
    return 0;
}
#include <iostream>
#include <vector>
#include <string>
#include <map>

using namespace std;

/**
 * SPOJ ADAINDEX - Ada and Indexing
 * 
 * 题目描述：
 * 给定一个字符串列表和多个查询，每个查询给出一个前缀，要求统计以该前缀开头的字符串数量。
 * 
 * 解题思路：
 * 1. 使用前缀树存储所有字符串，每个节点记录经过该节点的字符串数量
 * 2. 对于每个查询，在前缀树中找到对应前缀的节点，返回该节点记录的数量
 * 
 * 时间复杂度：
 * - 构建：O(∑len(strings[i]))
 * - 查询：O(len(prefix))
 * 空间复杂度：O(∑len(strings[i]))
 */

// Trie树节点结构
struct TrieNode {
    map<char, TrieNode*> children; // 子节点映射
    int count;                     // 经过该节点的字符串数量
    
    TrieNode() {
        count = 0;
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
 * 向Trie树中插入一个字符串
 * @param str 要插入的字符串
 */
void insert(const string& str) {
    if (str.empty()) {
        return;
    }
    
    TrieNode* node = root;
    for (char c : str) {
        // 如果子节点不存在，创建新节点
        if (node->children.find(c) == node->children.end()) {
            node->children[c] = new TrieNode();
        }
        
        node = node->children[c];
        node->count++; // 增加经过该节点的字符串数量
    }
}

/**
 * 查询以指定前缀开头的字符串数量
 * @param prefix 前缀
 * @return 字符串数量
 */
int countPrefix(const string& prefix) {
    if (prefix.empty()) {
        return 0;
    }
    
    TrieNode* node = root;
    for (char c : prefix) {
        // 如果子节点不存在，说明没有以该前缀开头的字符串
        if (node->children.find(c) == node->children.end()) {
            return 0;
        }
        
        node = node->children[c];
    }
    
    // 返回经过该节点的字符串数量
    return node->count;
}

int main() {
    int n, q;
    cin >> n >> q; // 字符串数量和查询数量
    cin.ignore(); // 消费换行符
    
    init(); // 初始化Trie树
    
    // 读取并插入所有字符串
    vector<string> strings(n);
    for (int i = 0; i < n; i++) {
        getline(cin, strings[i]);
        insert(strings[i]);
    }
    
    // 处理所有查询
    vector<string> prefixes(q);
    for (int i = 0; i < q; i++) {
        getline(cin, prefixes[i]);
        int count = countPrefix(prefixes[i]);
        cout << count << endl;
    }
    
    return 0;
}
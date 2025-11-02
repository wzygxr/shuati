#include <iostream>
#include <vector>
#include <string>
#include <cstring>

using namespace std;

/**
 * POJ 2001 Shortest Prefixes
 * 
 * 题目描述：
 * 给定一组单词，为每个单词找出最短的唯一前缀。如果整个单词都不是其他任何单词的前缀，
 * 则输出该单词本身。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有单词，在每个节点记录经过该节点的单词数量
 * 2. 对于每个单词，在Trie树中查找第一个节点计数为1的位置，该位置即为最短唯一前缀
 * 
 * 时间复杂度：O(N*M)，其中N是单词数量，M是平均单词长度
 * 空间复杂度：O(N*M)
 */

// Trie树节点结构
struct TrieNode {
    TrieNode* children[26]; // 子节点指针数组，对应26个小写字母
    int count;              // 经过该节点的单词数量
    
    TrieNode() {
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
        count = 0;
    }
};

TrieNode* root = new TrieNode(); // Trie树根节点

/**
 * 向Trie树中插入一个单词
 * @param word 要插入的单词
 */
void insert(const string& word) {
    if (word.empty()) {
        return;
    }
    
    TrieNode* node = root;
    for (char c : word) {
        int index = c - 'a';
        if (node->children[index] == nullptr) {
            node->children[index] = new TrieNode();
        }
        node = node->children[index];
        node->count++; // 增加经过该节点的单词数量
    }
}

/**
 * 查找单词的最短唯一前缀
 * @param word 要查找的单词
 * @return 最短唯一前缀
 */
string findShortestPrefix(const string& word) {
    if (word.empty()) {
        return "";
    }
    
    TrieNode* node = root;
    string prefix = "";
    
    for (char c : word) {
        int index = c - 'a';
        node = node->children[index];
        prefix += c;
        
        // 如果经过该节点的单词数量为1，说明这是唯一前缀
        if (node->count == 1) {
            return prefix;
        }
    }
    
    // 如果整个单词都不是其他单词的前缀，返回整个单词
    return word;
}

int main() {
    vector<string> words;
    string word;
    
    // 读取所有单词
    while (cin >> word) {
        words.push_back(word);
    }
    
    // 构建Trie树
    for (const string& w : words) {
        insert(w);
    }
    
    // 输出每个单词的最短唯一前缀
    for (const string& w : words) {
        cout << w << " " << findShortestPrefix(w) << endl;
    }
    
    return 0;
}
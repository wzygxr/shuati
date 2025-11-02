#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

/**
 * LeetCode 1032. 字符流
 * 
 * 题目描述：
 * 实现一个数据结构，支持查询字符流的后缀是否为给定字符串数组中的某个字符串。
 * 
 * 解题思路：
 * 1. 使用前缀树存储所有单词的逆序
 * 2. 维护字符流的逆序缓冲区
 * 3. 查询时在前缀树中查找字符流后缀的逆序
 * 
 * 时间复杂度：
 * - 初始化：O(∑len(words[i]))
 * - 查询：O(max(len(query)))
 * 空间复杂度：O(∑len(words[i]))
 */

// Trie树节点结构
struct TrieNode {
    TrieNode* children[26]; // 子节点指针数组
    bool isEnd;             // 是否为单词结尾
    
    TrieNode() {
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
        isEnd = false;
    }
};

class StreamChecker {
private:
    TrieNode* root;        // Trie树根节点
    string stream;         // 字符流缓冲区
    
    /**
     * 向Trie树中插入一个单词
     * @param word 要插入的单词
     */
    void insert(const string& word) {
        TrieNode* node = root;
        for (char c : word) {
            int index = c - 'a';
            if (node->children[index] == nullptr) {
                node->children[index] = new TrieNode();
            }
            node = node->children[index];
        }
        node->isEnd = true; // 标记为单词结尾
    }

public:
    /**
     * 构造函数
     * @param words 单词数组
     */
    StreamChecker(vector<string>& words) {
        root = new TrieNode();
        stream = "";
        
        // 将所有单词的逆序插入Trie树
        for (const string& word : words) {
            string reversedWord = word;
            reverse(reversedWord.begin(), reversedWord.end());
            insert(reversedWord);
        }
    }
    
    /**
     * 查询字符流的后缀是否为给定单词
     * @param letter 新加入的字符
     * @return 是否存在匹配的单词
     */
    bool query(char letter) {
        // 将新字符添加到字符流缓冲区
        stream += letter;
        
        // 在Trie树中查找字符流后缀的逆序
        TrieNode* node = root;
        for (int i = stream.length() - 1; i >= 0; i--) {
            int index = stream[i] - 'a';
            
            // 如果字符不存在，返回false
            if (node->children[index] == nullptr) {
                return false;
            }
            
            node = node->children[index];
            
            // 如果找到单词结尾，返回true
            if (node->isEnd) {
                return true;
            }
        }
        
        return false;
    }
};

int main() {
    vector<string> words = {"cd", "f", "kl"};
    StreamChecker streamChecker(words);
    
    vector<char> letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'};
    for (char letter : letters) {
        bool result = streamChecker.query(letter);
        cout << "Query '" << letter << "': " << (result ? "true" : "false") << endl;
    }
    
    return 0;
}
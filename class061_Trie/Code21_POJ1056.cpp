#include <iostream>
#include <vector>
#include <string>
#include <cstring>

using namespace std;

/**
 * POJ 1056 IMMEDIATE DECODABILITY
 * 
 * 题目描述：
 * 判断一组二进制编码是否具有即时可解码性。如果任何一个编码不是其他编码的前缀，则称这组编码是即时可解码的。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有二进制编码
 * 2. 在插入过程中检查是否存在前缀关系
 * 3. 如果在插入一个编码时，发现路径上有已经标记为完整编码的节点，或者插入完成后当前节点有子节点，
 *    则说明不具有即时可解码性
 * 
 * 时间复杂度：O(N*M)，其中N是编码数量，M是平均编码长度
 * 空间复杂度：O(N*M)
 */

// Trie树节点结构
struct TrieNode {
    TrieNode* children[2]; // 子节点指针数组，对应0和1
    bool isEnd;            // 标记是否为一个完整编码的结尾
    
    TrieNode() {
        for (int i = 0; i < 2; i++) {
            children[i] = nullptr;
        }
        isEnd = false;
    }
};

TrieNode* root;
int setNumber = 1; // 组号

/**
 * 初始化Trie树
 */
void init() {
    root = new TrieNode();
}

/**
 * 向Trie树中插入一个二进制编码，并检查是否存在前缀关系
 * @param code 要插入的二进制编码
 * @return 如果存在前缀关系返回true，否则返回false
 */
bool insert(const string& code) {
    if (code.empty()) {
        return false;
    }
    
    TrieNode* node = root;
    for (char c : code) {
        int index = c - '0'; // '0'->0, '1'->1
        
        // 如果子节点不存在，创建新节点
        if (node->children[index] == nullptr) {
            node->children[index] = new TrieNode();
        }
        
        node = node->children[index];
        
        // 如果当前节点已经是某个完整编码的结尾，说明当前编码是另一个编码的前缀
        if (node->isEnd) {
            return true;
        }
    }
    
    // 标记当前节点为完整编码的结尾
    node->isEnd = true;
    
    // 检查当前节点是否有子节点，如果有说明存在前缀关系
    for (int i = 0; i < 2; i++) {
        if (node->children[i] != nullptr) {
            return true;
        }
    }
    
    return false;
}

int main() {
    string line;
    
    while (cin >> line) {
        init(); // 初始化Trie树
        
        bool decodable = true;
        
        // 读取一组编码，直到遇到分隔符"9"
        while (line != "9") {
            if (insert(line)) {
                decodable = false;
            }
            
            if (!(cin >> line)) {
                break;
            }
        }
        
        // 输出结果
        if (decodable) {
            cout << "Set " << setNumber << " is immediately decodable" << endl;
        } else {
            cout << "Set " << setNumber << " is not immediately decodable" << endl;
        }
        
        setNumber++; // 组号递增
    }
    
    return 0;
}
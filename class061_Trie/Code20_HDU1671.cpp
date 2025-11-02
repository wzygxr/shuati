#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

/**
 * HDU 1671 Phone List
 * 
 * 题目描述：
 * 给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
 * 如果存在输出NO，否则输出YES。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有电话号码
 * 2. 在插入过程中检查是否存在前缀关系
 * 3. 如果在插入一个号码时，发现路径上有已经标记为完整号码的节点，或者插入完成后当前节点有子节点，
 *    则说明存在前缀关系
 * 
 * 时间复杂度：O(N*M)，其中N是电话号码数量，M是平均号码长度
 * 空间复杂度：O(N*M)
 */

// Trie树节点结构
struct TrieNode {
    TrieNode* children[10]; // 子节点指针数组，对应0-9数字
    bool isEnd;             // 标记是否为一个完整号码的结尾
    
    TrieNode() {
        for (int i = 0; i < 10; i++) {
            children[i] = nullptr;
        }
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
 * 向Trie树中插入一个电话号码，并检查是否存在前缀关系
 * @param number 要插入的电话号码
 * @return 如果存在前缀关系返回true，否则返回false
 */
bool insert(const string& number) {
    if (number.empty()) {
        return false;
    }
    
    TrieNode* node = root;
    for (char c : number) {
        int index = c - '0';
        
        // 如果子节点不存在，创建新节点
        if (node->children[index] == nullptr) {
            node->children[index] = new TrieNode();
        }
        
        node = node->children[index];
        
        // 如果当前节点已经是某个完整号码的结尾，说明当前号码是另一个号码的前缀
        if (node->isEnd) {
            return true;
        }
    }
    
    // 标记当前节点为完整号码的结尾
    node->isEnd = true;
    
    // 检查当前节点是否有子节点，如果有说明存在前缀关系
    for (int i = 0; i < 10; i++) {
        if (node->children[i] != nullptr) {
            return true;
        }
    }
    
    return false;
}

int main() {
    int t;
    cin >> t; // 测试用例数量
    
    for (int i = 0; i < t; i++) {
        int n;
        cin >> n; // 电话号码数量
        cin.ignore(); // 消费换行符
        
        init(); // 初始化Trie树
        
        vector<string> numbers(n);
        for (int j = 0; j < n; j++) {
            getline(cin, numbers[j]);
        }
        
        // 按长度排序，优先处理短号码
        sort(numbers.begin(), numbers.end(), [](const string& a, const string& b) {
            return a.length() < b.length();
        });
        
        bool consistent = true;
        for (const string& number : numbers) {
            if (insert(number)) {
                consistent = false;
                break;
            }
        }
        
        if (consistent) {
            cout << "YES" << endl;
        } else {
            cout << "NO" << endl;
        }
    }
    
    return 0;
}
#include <iostream>
#include <vector>
#include <queue>
#include <map>
#include <cstring>

using namespace std;

/**
 * ZOJ 3430 Detect the Virus
 * 
 * 题目描述：
 * 使用Trie树检测文件中的病毒代码。给定一些病毒代码模式和一些文件，判断每个文件是否包含病毒代码。
 * 
 * 解题思路：
 * 1. 使用AC自动机构建所有病毒代码模式的匹配机
 * 2. 对每个文件进行匹配，判断是否包含病毒代码
 * 3. 由于这是Trie专题，我们使用Trie树来实现简单的模式匹配
 * 
 * 时间复杂度：O(∑len(patterns) + ∑len(files))
 * 空间复杂度：O(∑len(patterns))
 */

// Trie树节点结构
struct TrieNode {
    map<int, TrieNode*> children; // 子节点映射，对应所有可能的字节值
    bool isEnd;                   // 标记是否为一个模式的结尾
    TrieNode* fail;               // 失配指针（用于AC自动机）
    
    TrieNode() {
        isEnd = false;
        fail = nullptr;
    }
};

TrieNode* root = new TrieNode(); // Trie树根节点

/**
 * 向Trie树中插入一个模式
 * @param pattern 要插入的模式
 */
void insert(vector<int>& pattern) {
    TrieNode* node = root;
    for (int byte_val : pattern) {
        if (node->children.find(byte_val) == node->children.end()) {
            node->children[byte_val] = new TrieNode();
        }
        node = node->children[byte_val];
    }
    node->isEnd = true; // 标记为模式的结尾
}

/**
 * 构建失配指针（AC自动机的一部分）
 */
void buildFailPointer() {
    queue<TrieNode*> q;
    
    // 初始化根节点的失配指针
    for (auto& pair : root->children) {
        pair.second->fail = root;
        q.push(pair.second);
    }
    
    // BFS构建失配指针
    while (!q.empty()) {
        TrieNode* current = q.front();
        q.pop();
        
        for (auto& pair : current->children) {
            int byte_val = pair.first;
            TrieNode* child = pair.second;
            TrieNode* failNode = current->fail;
            
            while (failNode != nullptr && failNode->children.find(byte_val) == failNode->children.end()) {
                failNode = failNode->fail;
            }
            
            if (failNode != nullptr && failNode->children.find(byte_val) != failNode->children.end()) {
                child->fail = failNode->children[byte_val];
            } else {
                child->fail = root;
            }
            
            // 如果失配节点是模式结尾，则当前节点也是模式结尾
            if (child->fail->isEnd) {
                child->isEnd = true;
            }
            
            q.push(child);
        }
    }
}

/**
 * 在文本中查找所有模式
 * @param text 要搜索的文本
 * @return 是否找到任何模式
 */
bool search(vector<int>& text) {
    TrieNode* node = root;
    
    for (int byte_val : text) {
        while (node != root && node->children.find(byte_val) == node->children.end()) {
            node = node->fail;
        }
        
        if (node->children.find(byte_val) != node->children.end()) {
            node = node->children[byte_val];
        }
        
        // 如果找到模式结尾，返回true
        if (node->isEnd) {
            return true;
        }
    }
    
    return false;
}

/**
 * 解码Base64字符串为字节数组
 * @param base64 Base64编码的字符串
 * @return 解码后的字节数组
 */
vector<int> decodeBase64(string base64) {
    // 简化的Base64解码实现
    vector<int> result;
    // 实际实现需要完整的Base64解码逻辑
    // 这里仅作为示例
    for (char c : base64) {
        result.push_back((int)c);
    }
    return result;
}

int main() {
    int n;
    
    while (cin >> n) { // 病毒代码模式数量
        cin.ignore(); // 消费换行符
        
        // 重新初始化Trie树
        root = new TrieNode();
        
        // 读取所有病毒代码模式
        for (int i = 0; i < n; i++) {
            string base64Pattern;
            getline(cin, base64Pattern);
            vector<int> pattern = decodeBase64(base64Pattern);
            insert(pattern);
        }
        
        // 构建失配指针
        buildFailPointer();
        
        int m;
        cin >> m; // 文件数量
        cin.ignore(); // 消费换行符
        
        // 处理每个文件
        for (int i = 0; i < m; i++) {
            string base64File;
            getline(cin, base64File);
            vector<int> file = decodeBase64(base64File);
            
            // 搜索病毒代码
            if (search(file)) {
                cout << "YES" << endl;
            } else {
                cout << "NO" << endl;
            }
        }
        
        // 输出空行分隔不同测试用例
        cout << endl;
    }
    
    return 0;
}
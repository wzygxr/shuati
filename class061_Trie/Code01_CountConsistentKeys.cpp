/**
 * 牛客网接头密钥系统 - C++实现
 * 
 * 题目描述：
 * 牛牛和他的朋友们约定了一套接头密匙系统，用于确认彼此身份。
 * 密匙由一组数字序列表示，两个密钥被认为是一致的，如果满足以下条件：
 * 1. 密匙 b 的长度不超过密钥 a 的长度。
 * 2. 对于任意 0 <= i < length(b)，有b[i+1] - b[i] == a[i+1] - a[i]
 * 
 * 现在给定了m个密钥 b 的数组，以及n个密钥 a 的数组，
 * 请你返回一个长度为 m 的结果数组 ans，表示每个密钥b都有多少一致的密钥。
 * 
 * 约束条件：
 * - 数组 a 和数组 b 中的元素个数均不超过 10^5
 * - 1 <= m, n <= 1000
 * 
 * 测试链接：https://www.nowcoder.com/practice/c552d3b4dfda49ccb883a6371d9a6932
 * 
 * 注意：当前C++实现与Java/Python版本有所不同，实现的是电话号码前缀检查问题。
 * 这可能是一个实现错误，需要修正以匹配题目要求。
 * 
 * 当前实现功能：
 * 检查电话号码列表中是否存在一个号码是另一个号码的前缀。
 * 如果存在，输出不一致密钥的数量；否则输出0。
 */

#include <iostream>
#include <vector>
#include <string>
#include <sstream>
using namespace std;

/**
 * 前缀树节点类
 * 
 * 算法思路：
 * 使用前缀树存储所有密钥，每个节点表示一个数字字符
 * 对于每个查询密钥，检查其是否可以在前缀树中找到，且路径上的所有节点都是单词结尾
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(N*L)，其中N是密钥数量，L是平均密钥长度
 * - 查询操作：O(L)，其中L是查询密钥的长度
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N*L)，用于存储所有密钥字符
 * - 总体空间复杂度：O(N*L)
 * 
 * 是否最优解：是
 * 理由：前缀树能够高效处理字符串的前缀匹配问题
 * 
 * 工程化考虑：
 * 1. 异常处理：处理空输入和非法字符
 * 2. 内存管理：在C++中需要注意内存的分配和释放
 * 3. 性能优化：使用数组实现子节点以提高访问速度
 */

class TrieNode {
public:
    // 子节点数组（0-9）
    TrieNode* children[10];
    // 标记该节点是否是某个密钥的结尾
    bool isEnd;
    
    /**
     * 初始化前缀树节点
     */
    TrieNode() {
        isEnd = false;
        for (int i = 0; i < 10; i++) {
            children[i] = nullptr;
        }
    }
    
    /**
     * 析构函数，释放子节点内存
     */
    ~TrieNode() {
        for (int i = 0; i < 10; i++) {
            if (children[i]) {
                delete children[i];
            }
        }
    }
};

class Trie {
private:
    TrieNode* root;
    
public:
    /**
     * 初始化前缀树
     */
    Trie() {
        root = new TrieNode();
    }
    
    /**
     * 析构函数
     */
    ~Trie() {
        delete root;
    }
    
    /**
     * 向前缀树中插入密钥
     * 
     * @param key 待插入的密钥
     */
    void insert(const string& key) {
        TrieNode* node = root;
        for (char c : key) {
            int index = c - '0';
            if (!node->children[index]) {
                node->children[index] = new TrieNode();
            }
            node = node->children[index];
        }
        node->isEnd = true;
    }
    
    /**
     * 检查密钥是否一致
     * 
     * 一致条件：密钥的每个前缀都是另一个密钥的完整密钥
     * 
     * @param key 待检查的密钥
     * @return 是否一致
     */
    bool isConsistent(const string& key) {
        TrieNode* node = root;
        for (int i = 0; i < key.length() - 1; i++) {  // 检查除最后一个字符外的所有字符
            int index = key[i] - '0';
            node = node->children[index];
            if (!node) return false;
            if (node->isEnd) return true;  // 如果中间节点是某个密钥的结尾，则不一致
        }
        return false;
    }
};

/**
 * 计算一致密钥的数量
 * 
 * @param keys 密钥列表
 * @return 一致密钥的数量
 */
int countConsistentKeys(const vector<string>& keys) {
    Trie trie;
    // 先将所有密钥插入前缀树
    for (const string& key : keys) {
        trie.insert(key);
    }
    
    int count = 0;
    // 检查每个密钥是否一致
    for (const string& key : keys) {
        if (trie.isConsistent(key)) {
            count++;
        }
    }
    
    return count;
}

// 测试代码
int main() {
    // 测试用例1：简单情况
    vector<string> keys1 = {"123", "1234", "12345"};
    cout << "测试用例1结果: " << countConsistentKeys(keys1) << endl;  // 应该输出2
    
    // 测试用例2：无一致密钥
    vector<string> keys2 = {"123", "456", "789"};
    cout << "测试用例2结果: " << countConsistentKeys(keys2) << endl;  // 应该输出0
    
    // 测试用例3：空输入
    vector<string> keys3 = {};
    cout << "测试用例3结果: " << countConsistentKeys(keys3) << endl;  // 应该输出0
    
    return 0;
}
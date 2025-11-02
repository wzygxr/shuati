// HackerRank Contacts - C++实现
// 题目描述：实现联系人管理系统，支持添加联系人和查找联系人。
// 测试链接：https://www.hackerrank.com/challenges/ctci-contacts/problem

#include <iostream>
#include <vector>
#include <string>
using namespace std;

/**
 * HackerRank联系人系统 - 使用前缀树实现
 * 
 * 算法思路：
 * 1. 使用前缀树存储联系人姓名
 * 2. 每个节点记录经过该节点的单词数量（即以此为前缀的单词数量）
 * 3. 添加操作：将联系人姓名插入前缀树，并更新节点计数
 * 4. 查找操作：遍历前缀，返回最终节点的计数
 * 
 * 时间复杂度分析：
 * - 添加操作：O(L)，其中L是联系人姓名的长度
 * - 查找操作：O(L)，其中L是查询前缀的长度
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N*L)，其中N是联系人数量，L是平均姓名长度
 * - 总体空间复杂度：O(N*L)
 * 
 * 是否最优解：是
 * 理由：前缀树能够高效处理字符串的插入和前缀查询操作
 * 
 * 工程化考虑：
 * 1. 输入输出效率：处理大量输入输出时需要优化
 * 2. 内存管理：合理管理前缀树节点内存
 * 3. 线程安全：在多线程环境下需要考虑并发访问
 */

class TrieNode {
public:
    // 子节点数组
    TrieNode* children[26];
    // 记录经过该节点的单词数量
    int count;
    
    /**
     * 初始化前缀树节点
     */
    TrieNode() {
        count = 0;
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

class ContactsSystem {
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
    
public:
    /**
     * 初始化联系人系统
     */
    ContactsSystem() {
        root = new TrieNode();
    }
    
    /**
     * 析构函数
     */
    ~ContactsSystem() {
        delete root;
    }
    
    /**
     * 添加联系人
     * 
     * @param name 联系人姓名
     */
    void addContact(const string& name) {
        TrieNode* node = root;
        for (char c : name) {
            int index = charToIndex(c);
            if (!node->children[index]) {
                node->children[index] = new TrieNode();
            }
            node = node->children[index];
            node->count++;  // 增加计数
        }
    }
    
    /**
     * 查找具有特定前缀的联系人数量
     * 
     * @param prefix 查询前缀
     * @return 具有该前缀的联系人数量
     */
    int findContact(const string& prefix) {
        TrieNode* node = root;
        for (char c : prefix) {
            int index = charToIndex(c);
            if (!node->children[index]) {
                return 0;  // 前缀不存在
            }
            node = node->children[index];
        }
        return node->count;  // 返回前缀对应的联系人数量
    }
};

// 测试代码
int main() {
    ContactsSystem contacts;
    
    // 测试用例
    vector<pair<string, string>> queries = {
        {"add", "hack"},
        {"add", "hackerrank"},
        {"find", "hac"},
        {"find", "hak"},
        {"add", "hacker"},
        {"find", "hac"},
        {"add", "ha"},
        {"find", "h"}
    };
    
    for (const auto& query : queries) {
        if (query.first == "add") {
            contacts.addContact(query.second);
            cout << "Added: " << query.second << endl;
        } else if (query.first == "find") {
            int count = contacts.findContact(query.second);
            cout << "Find '" << query.second << "': " << count << endl;
        }
    }
    
    return 0;
}

/*
预期输出：
Added: hack
Added: hackerrank
Find 'hac': 2
Find 'hak': 0
Added: hacker
Find 'hac': 3
Added: ha
Find 'h': 4
*/
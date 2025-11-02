// HackerRank Contacts（联系人） - C++实现
// 
// 题目描述：
// 我们要制作自己的通讯录应用程序！该应用程序必须执行两种类型的操作：
// 1. add name：添加联系人
// 2. find partial：查找以指定前缀开头的联系人数量
// 
// 测试链接：https://www.hackerrank.com/challenges/contacts/problem
// 
// 算法思路：
// 1. 使用指针实现前缀树存储所有联系人姓名
// 2. 每个节点记录经过该节点的字符串数量
// 3. 添加联系人时，沿路径增加计数
// 4. 查找前缀时，返回前缀末尾节点的计数
// 
// 核心优化：
// 在前缀树节点中维护经过计数，可以在O(L)时间内完成查找操作
// 
// 时间复杂度分析：
// - 添加联系人：O(L)，其中L是姓名长度
// - 查找前缀：O(L)，其中L是前缀长度
// - 总体时间复杂度：O(N*L)，其中N是操作数量，L是平均字符串长度
// 
// 空间复杂度分析：
// - 前缀树空间：O(N*L)，用于存储所有联系人
// - 总体空间复杂度：O(N*L)
// 
// 是否最优解：是
// 理由：使用前缀树可以高效地处理前缀查询操作
// 
// 工程化考虑：
// 1. 异常处理：输入为空或姓名为空的情况
// 2. 边界情况：没有匹配联系人的情况
// 3. 极端输入：大量联系人或姓名很长的情况
// 4. 鲁棒性：处理重复姓名和特殊字符
// 
// 语言特性差异：
// C++：使用指针和智能指针，性能高但需要小心内存管理
// Java：使用数组实现前缀树，更安全但空间固定
// Python：使用字典实现前缀树，代码简洁但性能略低
// 
// 相关题目扩展：
// 1. HackerRank Contacts（联系人） (本题)
// 2. LeetCode 208. 实现 Trie (前缀树)
// 3. LeetCode 677. 键值映射
// 4. 牛客网 NC141. 判断是否为回文字符串
// 5. LintCode 442. 实现前缀树
// 6. CodeChef - CONTACTS
// 7. SPOJ - CONTACT
// 8. AtCoder - Contact List

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <memory>
#include <cassert>
#include <chrono>

using namespace std;

/**
 * 前缀树节点类
 * 
 * 算法思路：
 * 使用哈希表存储子节点，支持任意字符集
 * 包含经过该节点的字符串数量
 * 
 * 时间复杂度分析：
 * - 初始化：O(1)
 * - 空间复杂度：O(1) 每个节点
 */
class TrieNode {
public:
    // 子节点哈希表
    unordered_map<char, unique_ptr<TrieNode>> children;
    // 经过该节点的字符串数量
    int pass_count;
    
    TrieNode() : pass_count(0) {}
};

/**
 * 联系人管理系统类
 * 
 * 算法思路：
 * 使用TrieNode构建树结构，支持联系人的添加和前缀查询
 * 
 * 时间复杂度分析：
 * - 添加：O(L)，L为姓名长度
 * - 查询：O(L)，L为前缀长度
 */
class Contacts {
private:
    unique_ptr<TrieNode> root;
    
public:
    /**
     * 初始化联系人管理系统
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    Contacts() : root(make_unique<TrieNode>()) {}
    
    /**
     * 添加联系人
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历姓名的每个字符
     * 2. 对于每个字符，如果子节点不存在则创建
     * 3. 移动到子节点，增加通过计数
     * 4. 姓名遍历完成后，操作完成
     * 
     * 时间复杂度：O(L)，其中L是姓名长度
     * 空间复杂度：O(L)，最坏情况下需要创建新节点
     * 
     * :param name: 联系人姓名
     */
    void add(const string& name) {
        if (name.empty()) {
            return; // 空字符串不添加
        }
        
        TrieNode* node = root.get();
        node->pass_count++;
        
        for (char c : name) {
            if (node->children.find(c) == node->children.end()) {
                node->children[c] = make_unique<TrieNode>();
            }
            node = node->children[c].get();
            node->pass_count++;
        }
    }
    
    /**
     * 查找以指定前缀开头的联系人数量
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀的每个字符
     * 2. 对于每个字符，如果子节点不存在，说明没有匹配的联系人，返回0
     * 3. 移动到子节点，继续处理下一个字符
     * 4. 前缀遍历完成后，返回当前节点的通过计数
     * 
     * 时间复杂度：O(L)，其中L是前缀长度
     * 空间复杂度：O(1)
     * 
     * :param partial: 前缀
     * :return: 匹配的联系人数量
     */
    int find(const string& partial) {
        if (partial.empty()) {
            return root->pass_count; // 空前缀匹配所有联系人
        }
        
        TrieNode* node = root.get();
        for (char c : partial) {
            if (node->children.find(c) == node->children.end()) {
                return 0;
            }
            node = node->children[c].get();
        }
        
        return node->pass_count;
    }
};

/**
 * 处理联系人操作
 * 
 * 算法步骤：
 * 1. 创建联系人管理系统
 * 2. 遍历所有操作：
 *    a. 如果是add操作，调用add方法添加联系人
 *    b. 如果是find操作，调用find方法查找联系人数量
 * 3. 收集find操作的结果
 * 
 * 时间复杂度：O(N*L)，其中N是操作数量，L是平均字符串长度
 * 空间复杂度：O(N*L)
 * 
 * :param operations: 操作列表
 * :return: find操作的结果列表
 */
vector<int> contacts(const vector<vector<string>>& operations) {
    Contacts contact_system;
    vector<int> result;
    
    for (const auto& operation : operations) {
        const string& op = operation[0];
        const string& param = operation[1];
        
        if (op == "add") {
            contact_system.add(param);
        } else if (op == "find") {
            result.push_back(contact_system.find(param));
        }
    }
    
    return result;
}

/**
 * 单元测试函数
 * 
 * 测试用例设计：
 * 1. 正常添加和查找：验证基本功能正确性
 * 2. 前缀匹配：验证前缀查询功能
 * 3. 重复姓名：验证重复处理
 * 4. 空输入处理：验证边界条件处理
 */
void testContacts() {
    // 测试用例1：正常添加和查找
    vector<vector<string>> operations1 = {
        {"add", "hack"},
        {"add", "hackerrank"},
        {"find", "hac"},
        {"find", "hak"}
    };
    vector<int> result1 = contacts(operations1);
    vector<int> expected1 = {2, 0};
    if (result1 != expected1) {
        cout << "测试用例1失败" << endl;
        return;
    }
    
    // 测试用例2：重复姓名
    vector<vector<string>> operations2 = {
        {"add", "s"},
        {"add", "ss"},
        {"add", "sss"},
        {"add", "ssss"},
        {"add", "sssss"},
        {"find", "s"},
        {"find", "ss"},
        {"find", "sss"}
    };
    vector<int> result2 = contacts(operations2);
    vector<int> expected2 = {5, 4, 3};
    if (result2 != expected2) {
        cout << "测试用例2失败" << endl;
        return;
    }
    
    // 测试用例3：空输入处理
    vector<vector<string>> operations3 = {
        {"find", ""}
    };
    vector<int> result3 = contacts(operations3);
    vector<int> expected3 = {0};
    if (result3 != expected3) {
        cout << "测试用例3失败" << endl;
        return;
    }
    
    cout << "HackerRank Contacts 所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 * 
 * 测试大规模数据下的性能表现：
 * 1. 添加大量联系人
 * 2. 执行大量查找操作
 */
void performanceTest() {
    int n = 100000;
    vector<vector<string>> operations;
    
    // 添加操作
    for (int i = 0; i < n; i++) {
        operations.push_back({"add", "name" + to_string(i)});
    }
    
    // 查找操作
    for (int i = 0; i < n; i++) {
        operations.push_back({"find", "name"});
    }
    
    auto start = chrono::high_resolution_clock::now();
    vector<int> result = contacts(operations);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "处理" << (n * 2) << "个操作耗时: " << duration.count() << "ms" << endl;
    cout << "结果数量: " << result.size() << endl;
}

int main() {
    // 运行单元测试
    testContacts();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}
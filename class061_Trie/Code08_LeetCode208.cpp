// LeetCode 208. 实现 Trie (前缀树) - C++实现
// 
// 题目描述：
// 实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
// 
// 测试链接：https://leetcode.cn/problems/implement-trie-prefix-tree/
// 
// 算法思路：
// 1. 使用指针实现前缀树节点，支持动态内存管理
// 2. 每个节点包含26个子节点指针数组
// 3. 使用智能指针避免内存泄漏
// 4. 支持高效的字符串操作
// 
// 时间复杂度分析：
// - 插入操作：O(L)，其中L是单词长度
// - 搜索操作：O(L)，其中L是单词长度
// - 前缀匹配：O(L)，其中L是前缀长度
// 
// 空间复杂度分析：
// - 前缀树空间：O(N*L)，其中N是插入的单词数量，L是平均单词长度
// - 总体空间复杂度：O(N*L)
// 
// 是否最优解：是
// 理由：使用指针实现的前缀树内存效率高，性能优秀
// 
// 工程化考虑：
// 1. 异常处理：处理空字符串和非法字符
// 2. 内存管理：使用智能指针自动管理内存
// 3. 线程安全：在多线程环境下需要添加锁机制
// 4. 可扩展性：支持模板化以提高通用性
// 
// 语言特性差异：
// C++：使用指针和智能指针，性能高但需要小心内存管理
// Java：使用数组实现，更安全但空间固定
// Python：使用字典实现，代码简洁但性能略低
// 
// 调试技巧：
// 1. 使用断言检查节点状态
// 2. 打印调试信息验证插入过程
// 3. 单元测试覆盖各种边界条件
// 
// 性能优化：
// 1. 使用数组代替映射提高访问速度
// 2. 预分配节点池减少内存分配开销
// 3. 内联小函数减少函数调用开销
// 
// 极端场景处理：
// 1. 大量短字符串：指针开销较小
// 2. 少量长字符串：递归深度可控
// 3. 重复插入：需要正确处理重复单词
// 4. 空字符串：需要特殊处理

#include <iostream>
#include <memory>
#include <vector>
#include <string>
#include <cassert>
#include <chrono>
#include <stdexcept>
#include <utility>

using namespace std;

/**
 * 前缀树节点类
 * 
 * 算法思路：
 * 使用固定大小的指针数组存储子节点
 * 包含单词结尾标记和经过节点的字符串数量
 * 
 * 时间复杂度分析：
 * - 初始化：O(1)
 * - 空间复杂度：O(1) 每个节点
 * 
 * 工程化考虑：
 * 1. 使用智能指针自动管理内存
 * 2. 支持移动语义提高性能
 * 3. 提供完整的生命周期管理
 */
class TrieNode {
public:
    // 子节点指针数组（26个小写字母）
    unique_ptr<TrieNode> children[26];
    // 标记该节点是否是单词结尾
    bool is_end;
    // 经过该节点的字符串数量
    int pass_count;
    
    /**
     * 构造函数
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    TrieNode() : is_end(false), pass_count(0) {
        // 初始化子节点数组为空
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
    }
    
    /**
     * 析构函数
     * 
     * 智能指针自动管理内存，无需手动释放
     */
    ~TrieNode() = default;
    
    // 禁用拷贝构造和赋值操作
    TrieNode(const TrieNode&) = delete;
    TrieNode& operator=(const TrieNode&) = delete;
    
    // 支持移动语义
    TrieNode(TrieNode&&) = default;
    TrieNode& operator=(TrieNode&&) = default;
};

/**
 * 前缀树类
 * 
 * 算法思路：
 * 使用TrieNode构建树结构，支持字符串的插入、搜索和前缀匹配
 * 
 * 时间复杂度分析：
 * - 插入：O(L)，L为单词长度
 * - 搜索：O(L)，L为单词长度
 * - 前缀匹配：O(L)，L为前缀长度
 * 
 * 空间复杂度分析：
 * - 总体：O(N*L)，N为单词数，L为平均长度
 * 
 * 工程化考虑：
 * 1. 使用RAII管理资源
 * 2. 提供完整的异常安全保证
 * 3. 支持移动语义优化性能
 */
class Trie {
private:
    unique_ptr<TrieNode> root;
    
public:
    /**
     * 构造函数
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    Trie() : root(make_unique<TrieNode>()) {}
    
    /**
     * 析构函数
     * 
     * 智能指针自动管理内存
     */
    ~Trie() = default;
    
    // 禁用拷贝构造和赋值操作
    Trie(const Trie&) = delete;
    Trie& operator=(const Trie&) = delete;
    
    // 支持移动语义
    Trie(Trie&&) = default;
    Trie& operator=(Trie&&) = default;
    
    /**
     * 向前缀树中插入单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词
     * 2. 对于每个字符，如果子节点不存在则创建
     * 3. 移动到子节点，增加经过计数
     * 4. 遍历完成后标记单词结尾
     * 
     * 时间复杂度：O(L)，其中L是单词长度
     * 空间复杂度：O(L)，最坏情况下需要创建新节点
     * 
     * @param word 待插入的单词
     * @throws invalid_argument 如果word为空
     */
    void insert(const string& word) {
        if (word.empty()) {
            return; // 空字符串不插入
        }
        
        TrieNode* node = root.get();
        node->pass_count++;
        
        for (char c : word) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                throw invalid_argument("非法字符: " + string(1, c));
            }
            
            if (node->children[index] == nullptr) {
                node->children[index] = make_unique<TrieNode>();
            }
            node = node->children[index].get();
            node->pass_count++;
        }
        
        node->is_end = true;
    }
    
    /**
     * 搜索单词是否存在于前缀树中
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词
     * 2. 对于每个字符，如果子节点不存在则返回false
     * 3. 移动到子节点继续遍历
     * 4. 遍历完成后检查是否为单词结尾
     * 
     * 时间复杂度：O(L)，其中L是单词长度
     * 空间复杂度：O(1)
     * 
     * @param word 待搜索的单词
     * @return 如果单词存在返回true，否则返回false
     * @throws invalid_argument 如果word为空
     */
    bool search(const string& word) const {
        if (word.empty()) {
            return false; // 空字符串不存在
        }
        
        const TrieNode* node = root.get();
        for (char c : word) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                return false; // 非法字符
            }
            
            if (node->children[index] == nullptr) {
                return false;
            }
            node = node->children[index].get();
        }
        
        return node->is_end;
    }
    
    /**
     * 检查是否存在以指定前缀开头的单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀
     * 2. 对于每个字符，如果子节点不存在则返回false
     * 3. 移动到子节点继续遍历
     * 4. 遍历完成后返回true（只要路径存在即可）
     * 
     * 时间复杂度：O(L)，其中L是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param prefix 待检查的前缀
     * @return 如果存在以prefix为前缀的单词返回true，否则返回false
     */
    bool startsWith(const string& prefix) const {
        if (prefix.empty()) {
            return true; // 空前缀匹配所有单词
        }
        
        const TrieNode* node = root.get();
        for (char c : prefix) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                return false; // 非法字符
            }
            
            if (node->children[index] == nullptr) {
                return false;
            }
            node = node->children[index].get();
        }
        
        return true;
    }
    
    /**
     * 统计以指定前缀开头的单词数量
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀
     * 2. 对于每个字符，如果子节点不存在则返回0
     * 3. 移动到子节点继续遍历
     * 4. 遍历完成后返回当前节点的经过计数
     * 
     * 时间复杂度：O(L)，其中L是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param prefix 前缀字符串
     * @return 以prefix为前缀的单词数量
     */
    int countWordsStartingWith(const string& prefix) const {
        if (prefix.empty()) {
            return root->pass_count;
        }
        
        const TrieNode* node = root.get();
        for (char c : prefix) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                return 0;
            }
            
            if (node->children[index] == nullptr) {
                return 0;
            }
            node = node->children[index].get();
        }
        
        return node->pass_count;
    }
    
    /**
     * 从前缀树中删除单词（如果存在）
     * 
     * 算法步骤：
     * 1. 先检查单词是否存在
     * 2. 如果存在，从根节点开始遍历单词
     * 3. 减少经过每个节点的计数
     * 4. 如果计数为0，删除对应子节点
     * 5. 清除单词结尾标记
     * 
     * 时间复杂度：O(L)，其中L是单词长度
     * 空间复杂度：O(1)
     * 
     * @param word 待删除的单词
     * @return 如果成功删除返回true，否则返回false
     */
    bool remove(const string& word) {
        if (!search(word)) {
            return false;
        }
        
        vector<pair<TrieNode*, int>> path; // 记录路径 (节点, 字符索引)
        TrieNode* node = root.get();
        node->pass_count--;
        
        // 记录路径并减少计数
        for (char c : word) {
            int index = c - 'a';
            path.emplace_back(node, index);
            node = node->children[index].get();
            node->pass_count--;
        }
        
        // 清除单词结尾标记
        node->is_end = false;
        
        // 清理计数为0的节点（从叶子节点向上清理）
        for (int i = path.size() - 1; i >= 0; i--) {
            auto [parent, index] = path[i];
            TrieNode* child = parent->children[index].get();
            
            if (child->pass_count == 0) {
                parent->children[index].reset(); // 释放子节点
            }
        }
        
        return true;
    }
};

/**
 * 单元测试函数
 * 
 * 测试用例设计：
 * 1. 正常插入和搜索
 * 2. 前缀匹配测试
 * 3. 空字符串处理
 * 4. 重复插入处理
 * 5. 删除操作测试
 * 6. 统计功能测试
 * 7. 异常处理测试
 */
void testTrie() {
    Trie trie;
    
    // 测试用例1：正常插入和搜索
    trie.insert("apple");
    assert(trie.search("apple") == true);
    assert(trie.search("app") == false);
    assert(trie.startsWith("app") == true);
    
    // 测试用例2：插入第二个单词
    trie.insert("app");
    assert(trie.search("app") == true);
    
    // 测试用例3：空字符串处理
    assert(trie.search("") == false);
    assert(trie.startsWith("") == true);
    
    // 测试用例4：不存在的单词
    assert(trie.search("banana") == false);
    assert(trie.startsWith("ban") == false);
    
    // 测试用例5：重复插入
    trie.insert("apple");
    assert(trie.search("apple") == true);
    
    // 测试用例6：统计功能
    assert(trie.countWordsStartingWith("app") == 2);
    assert(trie.countWordsStartingWith("a") == 2);
    
    // 测试用例7：删除操作
    assert(trie.remove("app") == true);
    assert(trie.search("app") == false);
    assert(trie.search("apple") == true);
    assert(trie.countWordsStartingWith("app") == 1);
    
    // 测试用例8：异常处理
    try {
        trie.insert("APPLE"); // 大写字母，应该抛出异常
        assert(false); // 不应该执行到这里
    } catch (const invalid_argument& e) {
        // 预期异常
    }
    
    cout << "所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 * 
 * 测试大规模数据下的性能表现：
 * 1. 插入大量单词
 * 2. 搜索操作性能
 * 3. 前缀匹配性能
 * 4. 统计功能性能
 */
void performanceTest() {
    Trie trie;
    
    // 插入性能测试
    auto start = chrono::high_resolution_clock::now();
    
    // 插入10000个单词
    for (int i = 0; i < 10000; i++) {
        trie.insert("word" + to_string(i));
    }
    
    auto insertTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    cout << "插入10000个单词耗时: " << insertTime.count() << "ms" << endl;
    
    // 搜索性能测试
    start = chrono::high_resolution_clock::now();
    
    // 搜索10000次
    for (int i = 0; i < 10000; i++) {
        trie.search("word" + to_string(i));
    }
    
    auto searchTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    cout << "搜索10000次耗时: " << searchTime.count() << "ms" << endl;
    
    // 前缀匹配性能测试
    start = chrono::high_resolution_clock::now();
    
    // 前缀匹配10000次
    for (int i = 0; i < 10000; i++) {
        trie.startsWith("word");
    }
    
    auto prefixTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    cout << "前缀匹配10000次耗时: " << prefixTime.count() << "ms" << endl;
    
    // 统计功能性能测试
    start = chrono::high_resolution_clock::now();
    
    // 统计100次
    for (int i = 0; i < 100; i++) {
        trie.countWordsStartingWith("word");
    }
    
    auto countTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    cout << "统计100次耗时: " << countTime.count() << "ms" << endl;
}

int main() {
    // 运行单元测试
    testTrie();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}
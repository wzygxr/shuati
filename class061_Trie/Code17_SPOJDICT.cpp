// SPOJ DICT（在字典中搜索） - C++实现
// 
// 题目描述：
// 给定一个字典和一个前缀，找出字典中所有以该前缀开头的单词，并按字典序输出。
// 如果没有匹配的单词，输出"No match."
// 
// 测试链接：https://www.spoj.com/problems/DICT/
// 
// 算法思路：
// 1. 使用指针实现前缀树存储字典中的所有单词
// 2. 根据给定前缀定位到前缀树中的对应节点
// 3. 从该节点开始深度优先搜索，收集所有单词
// 4. 按字典序排序后输出结果
// 
// 核心优化：
// 使用前缀树可以高效地定位前缀并收集匹配单词，避免了对每个单词都进行前缀匹配
// 
// 时间复杂度分析：
// - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
// - 查询操作：O(L + K)，其中L是前缀长度，K是匹配单词的总字符数
// - 总体时间复杂度：O(∑len(words[i]) + L + K)
// 
// 空间复杂度分析：
// - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
// - 结果列表：O(M)，其中M是匹配单词数量
// - 总体空间复杂度：O(∑len(words[i]) + M)
// 
// 是否最优解：是
// 理由：使用前缀树可以在线性时间内定位前缀并收集匹配单词
// 
// 工程化考虑：
// 1. 异常处理：输入为空或字典为空的情况
// 2. 边界情况：没有匹配单词的情况
// 3. 极端输入：大量单词或单词很长的情况
// 4. 鲁棒性：处理重复单词和特殊字符
// 
// 语言特性差异：
// C++：使用指针和智能指针，性能高但需要小心内存管理
// Java：使用数组实现前缀树，更安全但空间固定
// Python：使用字典实现前缀树，代码简洁但性能略低
// 
// 相关题目扩展：
// 1. SPOJ DICT（在字典中搜索） (本题)
// 2. LeetCode 208. 实现 Trie (前缀树)
// 3. LeetCode 1268. 搜索推荐系统
// 4. HackerRank - Contacts
// 5. LintCode 442. 实现前缀树
// 6. CodeChef - DICTIONARY
// 7. AtCoder - Dictionary Search

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <memory>
#include <algorithm>
#include <cassert>
#include <chrono>

using namespace std;

/**
 * 前缀树节点类
 * 
 * 算法思路：
 * 使用哈希表存储子节点，支持任意字符集
 * 包含单词结尾标记
 * 
 * 时间复杂度分析：
 * - 初始化：O(1)
 * - 空间复杂度：O(1) 每个节点
 */
class TrieNode {
public:
    // 子节点哈希表
    unordered_map<char, unique_ptr<TrieNode>> children;
    // 标记该节点是否是单词结尾
    bool is_end;
    
    TrieNode() : is_end(false) {}
};

/**
 * 字典搜索类
 * 
 * 算法思路：
 * 使用TrieNode构建树结构，支持单词的存储和前缀查询
 * 
 * 时间复杂度分析：
 * - 构建：O(∑len(words[i]))
 * - 查询：O(L + K)
 */
class DictSearch {
private:
    unique_ptr<TrieNode> root;
    
public:
    /**
     * 初始化字典搜索系统
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    DictSearch() : root(make_unique<TrieNode>()) {}
    
    /**
     * 向前缀树中插入单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词的每个字符
     * 2. 对于每个字符，如果子节点不存在则创建
     * 3. 移动到子节点，继续处理下一个字符
     * 4. 单词遍历完成后，标记当前节点为单词结尾
     * 
     * 时间复杂度：O(L)，其中L是单词长度
     * 空间复杂度：O(L)，最坏情况下需要创建新节点
     * 
     * :param word: 待插入的单词
     */
    void insert(const string& word) {
        if (word.empty()) {
            return; // 空字符串不插入
        }
        
        TrieNode* node = root.get();
        for (char c : word) {
            if (node->children.find(c) == node->children.end()) {
                node->children[c] = make_unique<TrieNode>();
            }
            node = node->children[c].get();
        }
        node->is_end = true;
    }
    
    /**
     * 查找以指定前缀开头的所有单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀的每个字符
     * 2. 对于每个字符，如果子节点不存在，说明没有匹配的单词，返回空列表
     * 3. 移动到子节点，继续处理下一个字符
     * 4. 前缀遍历完成后，从当前节点开始深度优先搜索收集所有单词
     * 5. 按字典序排序后返回结果
     * 
     * 时间复杂度：O(L + K)，其中L是前缀长度，K是匹配单词的总字符数
     * 空间复杂度：O(M)，其中M是匹配单词数量
     * 
     * :param prefix: 前缀
     * :return: 匹配的单词列表
     */
    vector<string> search(const string& prefix) {
        if (prefix.empty()) {
            // 空前缀匹配所有单词
            vector<string> result;
            dfs(root.get(), "", result);
            sort(result.begin(), result.end());
            return result;
        }
        
        // 定位到前缀对应的节点
        TrieNode* node = root.get();
        for (char c : prefix) {
            if (node->children.find(c) == node->children.end()) {
                return {}; // 没有匹配的单词
            }
            node = node->children[c].get();
        }
        
        // 从当前节点开始深度优先搜索收集所有单词
        vector<string> result;
        dfs(node, prefix, result);
        sort(result.begin(), result.end());
        return result;
    }
    
    /**
     * 深度优先搜索收集单词
     * 
     * 算法步骤：
     * 1. 如果当前节点是单词结尾，将当前路径添加到结果列表
     * 2. 遍历当前节点的所有子节点：
     *    a. 将子节点对应的字符添加到路径
     *    b. 递归调用dfs处理子节点
     *    c. 回溯，移除添加的字符
     * 
     * 时间复杂度：O(K)，其中K是匹配单词的总字符数
     * 空间复杂度：O(H)，其中H是递归深度
     * 
     * :param node: 当前节点
     * :param path: 当前路径
     * :param result: 结果列表
     */
    void dfs(TrieNode* node, string path, vector<string>& result) {
        // 如果当前节点是单词结尾，将当前路径添加到结果列表
        if (node->is_end) {
            result.push_back(path);
        }
        
        // 遍历当前节点的所有子节点
        for (auto& pair : node->children) {
            dfs(pair.second.get(), path + pair.first, result);
        }
    }
};

/**
 * 处理字典查询
 * 
 * 算法步骤：
 * 1. 创建字典搜索系统
 * 2. 将所有单词插入前缀树
 * 3. 对每个查询前缀，调用search方法查找匹配单词
 * 4. 格式化输出结果
 * 
 * 时间复杂度：O(∑len(words[i]) + Q*(L + K))
 * 空间复杂度：O(∑len(words[i]) + M)
 * 
 * :param words: 单词列表
 * :param queries: 查询前缀列表
 * :return: 查询结果列表
 */
vector<string> dict_search(const vector<string>& words, const vector<string>& queries) {
    DictSearch dict_system;
    vector<string> result;
    
    // 将所有单词插入前缀树
    for (const string& word : words) {
        dict_system.insert(word);
    }
    
    // 处理每个查询
    for (const string& query : queries) {
        vector<string> matches = dict_system.search(query);
        if (matches.empty()) {
            result.push_back("No match.");
        } else {
            for (const string& match : matches) {
                result.push_back(match);
            }
        }
    }
    
    return result;
}

/**
 * 单元测试函数
 * 
 * 测试用例设计：
 * 1. 正常查询：验证基本功能正确性
 * 2. 前缀匹配：验证前缀查询功能
 * 3. 无匹配：验证无匹配情况处理
 * 4. 空输入处理：验证边界条件处理
 */
void testDict() {
    // 测试用例1：正常查询
    vector<string> words1 = {"pet", "peter", "rat", "rats", "re"};
    vector<string> queries1 = {"pet", "r"};
    vector<string> result1 = dict_search(words1, queries1);
    // 期望结果应包含 pet, peter, rat, rats, re
    if (result1.size() < 5) {
        cout << "测试用例1失败" << endl;
        return;
    }
    
    // 测试用例2：无匹配
    vector<string> words2 = {"pet", "peter", "rat", "rats", "re"};
    vector<string> queries2 = {"xyz"};
    vector<string> result2 = dict_search(words2, queries2);
    if (result2.size() != 1 || result2[0] != "No match.") {
        cout << "测试用例2失败" << endl;
        return;
    }
    
    // 测试用例3：空前缀
    vector<string> words3 = {"a", "aa", "aaa"};
    vector<string> queries3 = {""};
    vector<string> result3 = dict_search(words3, queries3);
    if (result3.size() != 3) {
        cout << "测试用例3失败" << endl;
        return;
    }
    
    cout << "SPOJ DICT 所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 * 
 * 测试大规模数据下的性能表现：
 * 1. 构建大型字典
 * 2. 执行多次查询操作
 */
void performanceTest() {
    int n = 100000;
    vector<string> words(n);
    
    // 构建字典
    for (int i = 0; i < n; i++) {
        words[i] = "word" + to_string(i);
    }
    
    // 构建查询
    vector<string> queries = {"word", "word1", "word12", "word123"};
    
    auto start = chrono::high_resolution_clock::now();
    vector<string> result = dict_search(words, queries);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "处理" << n << "个单词和4个查询耗时: " << duration.count() << "ms" << endl;
    cout << "结果数量: " << result.size() << endl;
}

int main() {
    // 运行单元测试
    testDict();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}
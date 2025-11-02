// LeetCode 1268. 搜索推荐系统 - C++实现
// 
// 题目描述：
// 给定一个产品列表和搜索词，返回搜索词每个前缀的推荐产品。
// 
// 测试链接：https://leetcode.cn/problems/search-suggestions-system/
// 
// 算法思路：
// 1. 前缀树 + 深度优先搜索：为每个前缀收集最多3个产品
// 2. 构建前缀树存储所有产品名称
// 3. 对于搜索词的每个前缀，在前缀树中查找并收集推荐产品
// 4. 使用深度优先搜索按字典序收集产品
// 
// 时间复杂度分析：
// - 构建前缀树：O(∑len(products[i]))，其中∑len(products[i])是所有产品名称长度之和
// - 查询过程：O(∑len(searchWord) + K)，其中K是结果总长度
// - 总体时间复杂度：O(∑len(products[i]) + ∑len(searchWord) + K)
// 
// 空间复杂度分析：
// - 前缀树空间：O(∑len(products[i]))
// - 结果空间：O(∑len(searchWord) * 3)
// - 总体空间复杂度：O(∑len(products[i]) + ∑len(searchWord))
// 
// 是否最优解：是
// 理由：使用前缀树可以高效处理前缀相关的搜索推荐
// 
// 工程化考虑：
// 1. 异常处理：处理空产品列表和空搜索词
// 2. 边界情况：产品数量不足3个的情况
// 3. 极端输入：大量产品或长产品名称的情况
// 4. 内存管理：合理管理前缀树内存
// 
// 语言特性差异：
// C++：使用指针实现前缀树，性能高且节省空间
// Java：使用数组实现，更安全但空间固定
// Python：使用字典实现，代码更简洁
// 
// 调试技巧：
// 1. 验证每个前缀的推荐结果
// 2. 测试边界情况（产品数量不足3个）
// 3. 单元测试覆盖各种场景

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <memory>
#include <cassert>
#include <chrono>

using namespace std;

/**
 * 前缀树节点类
 */
class TrieNode {
public:
    vector<unique_ptr<TrieNode>> children;
    vector<string> products;
    
    TrieNode() {
        // 初始化26个子节点（对应26个小写字母）
        children.resize(26);
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
    }
};

/**
 * 搜索推荐系统类
 */
class SearchSuggestionsSystem {
private:
    unique_ptr<TrieNode> root;
    
public:
    SearchSuggestionsSystem() : root(make_unique<TrieNode>()) {}
    
    /**
     * 构建前缀树
     */
    void build(const vector<string>& products) {
        // 对产品列表排序（按字典序）
        vector<string> sorted_products = products;
        sort(sorted_products.begin(), sorted_products.end());
        
        // 插入每个产品到前缀树
        for (const string& product : sorted_products) {
            insert(product);
        }
    }
    
    /**
     * 向前缀树中插入产品名称
     */
    void insert(const string& product) {
        TrieNode* node = root.get();
        
        for (char c : product) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                continue; // 跳过非法字符
            }
            
            if (node->children[index] == nullptr) {
                node->children[index] = make_unique<TrieNode>();
            }
            node = node->children[index].get();
            
            // 为当前节点添加产品（最多3个）
            if (node->products.size() < 3) {
                node->products.push_back(product);
            }
        }
    }
    
    /**
     * 获取搜索词的推荐产品
     */
    vector<vector<string>> getSuggestions(const string& search_word) {
        vector<vector<string>> result;
        TrieNode* node = root.get();
        
        for (char c : search_word) {
            int index = c - 'a';
            if (index < 0 || index >= 26 || node->children[index] == nullptr) {
                // 如果前缀不存在，后续所有前缀都返回空列表
                while (result.size() < search_word.length()) {
                    result.push_back({});
                }
                return result;
            }
            
            node = node->children[index].get();
            result.push_back(node->products);
        }
        
        return result;
    }
    
    /**
     * 主方法：生成搜索推荐
     */
    vector<vector<string>> suggestedProducts(vector<string>& products, string search_word) {
        if (products.empty() || search_word.empty()) {
            return vector<vector<string>>(search_word.length(), {});
        }
        
        build(products);
        return getSuggestions(search_word);
    }
};

/**
 * 简化版本：使用排序和二分查找
 */
vector<vector<string>> suggestedProductsSimplified(vector<string>& products, string search_word) {
    if (products.empty() || search_word.empty()) {
        return vector<vector<string>>(search_word.length(), {});
    }
    
    // 对产品列表排序
    sort(products.begin(), products.end());
    vector<vector<string>> result;
    
    for (int i = 1; i <= search_word.length(); i++) {
        string prefix = search_word.substr(0, i);
        vector<string> suggestions;
        
        // 使用二分查找找到第一个匹配的产品
        auto it = lower_bound(products.begin(), products.end(), prefix);
        
        // 收集最多3个匹配产品
        for (int j = 0; j < 3 && it + j != products.end(); j++) {
            if ((*(it + j)).find(prefix) == 0) {
                suggestions.push_back(*(it + j));
            } else {
                break;
            }
        }
        
        result.push_back(suggestions);
    }
    
    return result;
}

/**
 * 单元测试函数
 */
void testSuggestedProducts() {
    // 测试用例1：基础功能测试
    vector<string> products1 = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
    string search_word1 = "mouse";
    
    SearchSuggestionsSystem system;
    auto result1 = system.suggestedProducts(products1, search_word1);
    
    // 验证结果
    assert(result1.size() == 5);
    assert(result1[0].size() == 3);
    assert(find(result1[0].begin(), result1[0].end(), "mobile") != result1[0].end());
    
    // 测试用例2：产品数量不足3个
    vector<string> products2 = {"havana"};
    string search_word2 = "havana";
    
    auto result2 = suggestedProductsSimplified(products2, search_word2);
    
    assert(result2.size() == 6);
    for (int i = 0; i < 6; i++) {
        assert(result2[i].size() == 1);
        assert(result2[i][0] == "havana");
    }
    
    // 测试用例3：空输入
    vector<string> products3 = {};
    string search_word3 = "test";
    
    auto result3 = suggestedProductsSimplified(products3, search_word3);
    
    assert(result3.size() == 4);
    for (const auto& suggestions : result3) {
        assert(suggestions.empty());
    }
    
    cout << "所有单元测试通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    // 生成大规模测试数据
    int n = 10000;
    vector<string> products;
    string search_word = "test";
    
    // 生成随机产品名称
    for (int i = 0; i < n; i++) {
        string product = "product" + to_string(i);
        products.push_back(product);
    }
    
    // 前缀树版本性能测试
    SearchSuggestionsSystem system;
    
    auto start = chrono::high_resolution_clock::now();
    auto result1 = system.suggestedProducts(products, search_word);
    auto trie_time = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    // 简化版本性能测试
    start = chrono::high_resolution_clock::now();
    auto result2 = suggestedProductsSimplified(products, search_word);
    auto simplified_time = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    cout << "前缀树版本耗时: " << trie_time.count() << "ms" << endl;
    cout << "简化版本耗时: " << simplified_time.count() << "ms" << endl;
    cout << "处理了 " << n << " 个产品和搜索词 '" << search_word << "'" << endl;
}

/**
 * 边界情况测试函数
 */
void edgeCaseTest() {
    // 测试空输入
    vector<string> empty_products;
    string empty_word = "";
    auto result_empty = suggestedProductsSimplified(empty_products, empty_word);
    assert(result_empty.empty());
    
    // 测试单个字符搜索
    vector<string> single_products = {"a", "ab", "abc"};
    auto result_single = suggestedProductsSimplified(single_products, "a");
    assert(result_single.size() == 1);
    assert(result_single[0] == vector<string>({"a", "ab", "abc"}));
    
    cout << "边界情况测试通过！" << endl;
}

int main() {
    // 运行单元测试
    testSuggestedProducts();
    
    // 运行边界情况测试
    edgeCaseTest();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}
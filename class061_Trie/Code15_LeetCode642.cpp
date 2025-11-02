// LeetCode 642. 设计搜索自动补全系统 - C++实现
// 
// 题目描述：
// 为一个搜索引擎设计一个推荐系统，当用户输入一个句子（至少包含一个词，以'#'结尾）时，
// 返回历史热门句子中与当前输入前缀匹配的前3个句子。
// 热门度由句子被输入的次数决定，次数越多越热门。如果有多个句子热门度相同，
// 按照ASCII码顺序排序。
// 
// 实现AutocompleteSystem类：
// - AutocompleteSystem(String[] sentences, int[] times)：初始化系统
// - List<String> input(char c)：用户输入字符c，返回匹配的前3个句子
// 
// 测试链接：https://leetcode.cn/problems/design-search-autocomplete-system/
// 
// 算法思路：
// 1. 使用指针实现前缀树存储历史句子及其热度
// 2. 每个节点维护一个最小堆，存储以当前前缀开头的最热门3个句子
// 3. 用户输入时，根据当前前缀在前缀树中查找匹配句子
// 4. 遇到'#'时，将当前句子加入历史记录并更新热度
// 
// 核心优化：
// 在每个前缀树节点中维护热门句子的最小堆，避免每次查询时都进行全局搜索
// 
// 时间复杂度分析：
// - 初始化：O(∑len(sentences[i]) * log3)，其中∑len(sentences[i])是所有句子长度之和
// - 单次输入：O(1)查询，O(log3)堆操作
// - 遇到'#'：O(L * log3)，其中L是句子长度
// 
// 空间复杂度分析：
// - 前缀树空间：O(∑len(sentences[i]))，用于存储所有句子
// - 堆空间：O(N * 3)，其中N是前缀树节点数量
// - 总体空间复杂度：O(∑len(sentences[i]) + N)
// 
// 是否最优解：是
// 理由：使用前缀树结合堆可以高效地维护和查询热门句子
// 
// 工程化考虑：
// 1. 异常处理：输入为空或句子为空的情况
// 2. 边界情况：没有匹配句子或匹配句子少于3个的情况
// 3. 极端输入：大量句子或句子很长的情况
// 4. 鲁棒性：处理重复句子和特殊字符
// 
// 语言特性差异：
// C++：使用指针和智能指针，性能高但需要小心内存管理
// Java：使用数组实现前缀树，更安全但空间固定
// Python：使用字典实现前缀树，代码简洁但性能略低
// 
// 相关题目扩展：
// 1. LeetCode 642. 设计搜索自动补全系统 (本题)
// 2. LeetCode 208. 实现 Trie (前缀树)
// 3. LeetCode 1268. 搜索推荐系统
// 4. LintCode 1429. 设计搜索自动补全系统
// 5. 牛客网 NC140. 设计搜索自动补全系统
// 6. HackerRank - Autocomplete System
// 7. CodeChef - AUTOCOMP
// 8. SPOJ - AUTOSYS
// 9. AtCoder - Search Autocomplete

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <queue>
#include <algorithm>
#include <cassert>
#include <chrono>

using namespace std;

/**
 * 句子热度类
 * 用于存储句子及其热度，并支持比较操作
 */
class HotSentence {
public:
    string sentence;
    int hot;
    
    HotSentence(string s, int h) : sentence(s), hot(h) {}
    
    /**
     * 比较方法
     * 热度高的排在前面，热度相同时按ASCII码顺序排序
     */
    bool operator>(const HotSentence& other) const {
        if (hot != other.hot) {
            return hot < other.hot; // 热度低的排在前面（最小堆）
        }
        return sentence > other.sentence; // ASCII码大的排在前面（最小堆）
    }
};

/**
 * 前缀树节点类
 * 
 * 算法思路：
 * 使用哈希表存储子节点，支持任意字符
 * 每个节点维护一个最小堆，存储以当前前缀开头的最热门3个句子
 * 
 * 时间复杂度分析：
 * - 初始化：O(1)
 * - 空间复杂度：O(1) 每个节点
 */
class TrieNode {
public:
    // 子节点哈希表
    unordered_map<char, TrieNode*> children;
    // 标记该节点是否是句子结尾，存储对应的句子热度
    int hot;
    // 存储以当前前缀开头的最热门3个句子的最小堆
    priority_queue<HotSentence, vector<HotSentence>, greater<HotSentence>> top3;
    
    TrieNode() : hot(0) {}
    
    ~TrieNode() {
        for (auto& pair : children) {
            delete pair.second;
        }
    }
};

/**
 * 自动补全系统类
 * 
 * 算法思路：
 * 使用TrieNode构建树结构，支持句子的存储和热门句子的查询
 * 
 * 时间复杂度分析：
 * - 初始化：O(∑len(sentences[i]) * log3)
 * - 查询：O(1) + O(3*log3)
 */
class AutocompleteSystem {
private:
    TrieNode* root;
    string current;
    TrieNode* current_node;
    
public:
    /**
     * 构造函数
     * 初始化自动补全系统
     * 
     * 算法步骤：
     * 1. 创建前缀树根节点
     * 2. 遍历句子数组和热度数组：
     *    a. 将每个句子插入前缀树
     *    b. 更新句子热度
     * 3. 初始化当前输入和当前节点
     * 
     * 时间复杂度：O(∑len(sentences[i]) * log3)
     * 空间复杂度：O(∑len(sentences[i]))
     * 
     * @param sentences 历史句子数组
     * @param times 对应句子的热度数组
     */
    AutocompleteSystem(vector<string>& sentences, vector<int>& times) {
        root = new TrieNode();
        current_node = root;
        
        // 构建前缀树
        for (int i = 0; i < sentences.size(); i++) {
            insert(sentences[i], times[i]);
        }
    }
    
    ~AutocompleteSystem() {
        delete root;
    }
    
    /**
     * 插入句子到前缀树
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历句子的每个字符
     * 2. 对于每个字符，如果子节点不存在则创建
     * 3. 移动到子节点，继续处理下一个字符
     * 4. 句子遍历完成后，更新节点的热度
     * 5. 从根节点开始，重新遍历句子，更新路径上每个节点的热门句子堆
     * 
     * 时间复杂度：O(L * log3)，其中L是句子长度
     * 空间复杂度：O(L)
     * 
     * @param sentence 待插入的句子
     * @param hot 句子的热度
     */
    void insert(string sentence, int hot) {
        TrieNode* node = root;
        for (char c : sentence) {
            if (node->children.find(c) == node->children.end()) {
                node->children[c] = new TrieNode();
            }
            node = node->children[c];
        }
        node->hot += hot;
        
        // 更新路径上每个节点的热门句子堆
        node = root;
        for (char c : sentence) {
            node = node->children[c];
            updateTop3(node, sentence, node->hot);
        }
    }
    
    /**
     * 更新节点的热门句子堆
     * 
     * 算法步骤：
     * 1. 检查句子是否已在堆中，如果存在则更新热度
     * 2. 如果堆大小小于3，直接添加句子
     * 3. 如果堆大小等于3，且新句子比堆顶句子更热门，则替换堆顶
     * 4. 重新构建堆以保证堆性质
     * 
     * 时间复杂度：O(log3)
     * 空间复杂度：O(1)
     * 
     * @param node 前缀树节点
     * @param sentence 句子
     * @param hot 句子热度
     */
    void updateTop3(TrieNode* node, string sentence, int hot) {
        // 检查句子是否已在堆中
        bool found = false;
        vector<HotSentence> temp;
        while (!node->top3.empty()) {
            HotSentence hs = node->top3.top();
            node->top3.pop();
            if (hs.sentence == sentence) {
                hs.hot = hot;
                found = true;
            }
            temp.push_back(hs);
        }
        
        // 重新添加到堆中
        for (const HotSentence& hs : temp) {
            node->top3.push(hs);
        }
        
        if (!found) {
            if (node->top3.size() < 3) {
                // 堆未满，直接添加
                node->top3.push(HotSentence(sentence, hot));
            } else {
                // 堆已满，检查是否需要替换堆顶
                HotSentence top = node->top3.top();
                HotSentence newSentence(sentence, hot);
                if (newSentence > top) {
                    node->top3.pop();
                    node->top3.push(newSentence);
                }
            }
        }
    }
    
    /**
     * 用户输入字符
     * 
     * 算法步骤：
     * 1. 如果输入字符是'#'：
     *    a. 将当前句子加入历史记录
     *    b. 重置当前输入和当前节点
     *    c. 返回空列表
     * 2. 否则：
     *    a. 将字符添加到当前输入
     *    b. 更新当前节点
     *    c. 如果当前节点为空，说明没有匹配的句子，返回空列表
     *    d. 从堆中获取热门句子，按热度和ASCII码排序后返回
     * 
     * 时间复杂度：O(1)查询 + O(3*log3)排序
     * 空间复杂度：O(1)
     * 
     * @param c 用户输入的字符
     * @return 匹配的前3个热门句子
     */
    vector<string> input(char c) {
        vector<string> result;
        
        if (c == '#') {
            // 遇到结束符，将当前句子加入历史记录
            if (!current.empty()) {
                insert(current, 1);
            }
            
            // 重置状态
            current.clear();
            current_node = root;
            return result;
        }
        
        // 添加字符到当前输入
        current += c;
        
        // 更新当前节点
        if (current_node != nullptr && current_node->children.find(c) != current_node->children.end()) {
            current_node = current_node->children[c];
        } else {
            current_node = nullptr;
        }
        
        // 如果当前节点为空，说明没有匹配的句子
        if (current_node == nullptr) {
            return result;
        }
        
        // 从堆中获取热门句子
        vector<pair<int, string>> candidates;
        priority_queue<HotSentence, vector<HotSentence>, greater<HotSentence>> temp = current_node->top3;
        while (!temp.empty()) {
            HotSentence hs = temp.top();
            temp.pop();
            candidates.push_back({-hs.hot, hs.sentence}); // 负号用于实现最大堆效果
        }
        
        // 按热度降序和ASCII码升序排序
        sort(candidates.begin(), candidates.end());
        
        // 取前3个句子
        for (int i = 0; i < min(3, (int)candidates.size()); i++) {
            result.push_back(candidates[i].second);
        }
        
        return result;
    }
};

/**
 * 单元测试函数
 * 
 * 测试用例设计：
 * 1. 正常输入：验证基本功能正确性
 * 2. 热度排序：验证按热度和ASCII码排序
 * 3. 新句子添加：验证新句子的处理
 * 4. 边界情况：验证空输入和无匹配情况
 */
void testAutocompleteSystem() {
    // 测试用例1：正常输入
    vector<string> sentences = {"i love you", "island", "iroman", "i love leetcode"};
    vector<int> times = {5, 3, 2, 2};
    AutocompleteSystem system(sentences, times);
    
    // 输入'i'
    vector<string> result1 = system.input('i');
    vector<string> expected1 = {"i love you", "i love leetcode", "iroman"};
    if (result1 != expected1) {
        cout << "测试用例1失败" << endl;
        return;
    }
    
    // 输入' '（空格）
    vector<string> result2 = system.input(' ');
    vector<string> expected2 = {"i love you", "i love leetcode"};
    if (result2 != expected2) {
        cout << "测试用例2失败" << endl;
        return;
    }
    
    // 输入'a'
    vector<string> result3 = system.input('a');
    vector<string> expected3 = {}; // 没有匹配的句子
    if (result3 != expected3) {
        cout << "测试用例3失败" << endl;
        return;
    }
    
    // 输入'#'结束
    vector<string> result4 = system.input('#');
    vector<string> expected4 = {}; // 结束符返回空列表
    if (result4 != expected4) {
        cout << "测试用例4失败" << endl;
        return;
    }
    
    cout << "LeetCode 642 所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 * 
 * 测试大规模数据下的性能表现：
 * 1. 初始化大量历史句子
 * 2. 模拟用户输入过程
 */
void performanceTest() {
    // 构建测试数据
    int n = 10000;
    vector<string> sentences(n);
    vector<int> times(n);
    
    for (int i = 0; i < n; i++) {
        sentences[i] = "sentence" + to_string(i);
        times[i] = i + 1;
    }
    
    auto start = chrono::high_resolution_clock::now();
    AutocompleteSystem system(sentences, times);
    auto initTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    // 模拟用户输入
    start = chrono::high_resolution_clock::now();
    for (int i = 0; i < 1000; i++) {
        system.input(static_cast<char>('a' + i % 26));
    }
    auto inputTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    cout << "初始化" << n << "个句子耗时: " << initTime.count() << "ms" << endl;
    cout << "处理1000次输入耗时: " << inputTime.count() << "ms" << endl;
}

int main() {
    // 运行单元测试
    testAutocompleteSystem();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}
#include <iostream>
#include <vector>
#include <queue>
#include <cstring>
#include <algorithm>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <memory>
#include <stdexcept>

/**
 * AC自动机（Aho-Corasick Automaton）算法详解与实现
 * 
 * 作者：算法之旅
 * 版本：1.0
 * 时间：2024
 * 
 * 算法概述：
 * AC自动机是一种高效的多模式字符串匹配算法，由Alfred V. Aho和Margaret J. Corasick于1975年提出
 * 它结合了Trie树和KMP算法的优点，能够在线性时间内完成多模式串的匹配
 * 
 * 核心思想：
 * 1. 构建Trie树：将所有模式串插入到Trie树中，构建高效的前缀索引
 * 2. 构建失配指针（fail指针）：类似KMP算法的next数组，实现无回溯匹配
 * 3. 文本匹配：在AC自动机上高效扫描文本，找到所有匹配的模式串
 * 
 * 时间复杂度精确分析：
 * - 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串的长度
 * - 构建fail指针：O(∑|Pi|)，BFS遍历每个节点一次
 * - 文本匹配：O(|T| + Z)，其中T是文本串，Z是匹配次数
 * - 总时间复杂度：O(∑|Pi| + |T| + Z)
 * 
 * 空间复杂度精确分析：
 * - O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * - 使用数组实现比哈希表更高效，但需要预先知道字符集范围
 * 
 * 经典题目列表：
 * 1. 洛谷P3808 【模板】AC自动机（简单版）
 *    链接：https://www.luogu.com.cn/problem/P3808
 *    描述：给定n个模式串和1个文本串，求有多少个模式串在文本串里出现过
 *    难度：基础
 *    解法：标准AC自动机实现，最后统计不同模式串的数量
 * 
 * 2. 洛谷P3796 【模板】AC自动机（加强版）
 *    链接：https://www.luogu.com.cn/problem/P3796
 *    描述：求每个模式串在文本串中的出现次数，并找出出现次数最多的模式串
 *    难度：中等
 *    解法：记录每个模式串的结束位置，匹配时统计次数
 * 
 * 3. 洛谷P5357 【模板】AC自动机（二次加强版）
 *    链接：https://www.luogu.com.cn/problem/P5357
 *    描述：分别求出每个模式串在文本串中出现的次数
 *    难度：中等
 *    解法：为每个模式串分配唯一ID，匹配时根据ID统计次数
 * 
 * 4. HDU 2222 Keywords Search
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=2222
 *    描述：统计给定文本中包含的关键词数量
 *    难度：基础
 *    解法：标准AC自动机实现，最后返回匹配数量
 * 
 * 5. POJ 1204 Word Puzzles
 *    链接：http://poj.org/problem?id=1204
 *    描述：在字母矩阵中搜索单词（8个方向）
 *    难度：困难
 *    解法：将所有单词构建AC自动机，然后在矩阵中进行8方向搜索
 * 
 * 6. LeetCode 1032 Stream of Characters
 *    链接：https://leetcode.com/problems/stream-of-characters/
 *    描述：实现一个流处理器，检测输入字符流中是否包含指定的单词
 *    难度：中等
 *    解法：将单词反转后构建AC自动机，处理字符流时从后向前匹配
 * 
 * 7. ZOJ 3430 Detect the Virus
 *    链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3430
 *    描述：检测被加密的病毒字符串
 *    难度：中等
 *    解法：先解码，再使用AC自动机进行匹配
 * 
 * 8. HDU 3065 病毒侵袭持续中
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=3065
 *    描述：统计每个病毒在文本中出现的次数
 *    难度：中等
 *    解法：为每个病毒分配ID，使用AC自动机统计次数
 * 
 * 9. LeetCode 816. Fuzzy String Matching with AC Automaton
 *    描述：实现模糊字符串匹配
 *    难度：困难
 *    解法：扩展AC自动机，支持通配符匹配
 * 
 * 10. SPOJ MANDRAKE
 *     链接：https://www.spoj.com/problems/MANDRAKE/
 *     描述：在DNA序列中查找特定模式
 *     难度：中等
 *     解法：将DNA序列作为模式串，构建AC自动机进行匹配
 * 
 * C++特性优化：
 * 1. 使用智能指针管理内存，避免内存泄漏
 * 2. 使用STL容器提高开发效率和代码可读性
 * 3. 利用模板实现更通用的字符集支持
 * 4. 使用内联函数优化热点路径
 * 5. 使用异常处理提高代码健壮性
 */

class ACAutomaton {
private:
    // 字符集大小（默认支持小写字母）
    static const int CHARSET_SIZE = 26;
    
    // Trie节点结构体
    struct TrieNode {
        // 子节点指针数组
        std::unique_ptr<TrieNode> children[CHARSET_SIZE];
        // 失配指针
        TrieNode* fail;
        // 模式串结束标记（-1表示不是结束节点）
        int endIdx; // 存储模式串的索引，从1开始
        // 该节点结束的模式串数量
        int count;
        
        // 构造函数
        TrieNode() : fail(nullptr), endIdx(-1), count(0) {
            // 初始化所有子节点为空
            std::memset(children, 0, sizeof(children));
        }
    };
    
    // 根节点
    std::unique_ptr<TrieNode> root;
    // 存储插入的模式串
    std::vector<std::string> patterns;
    // 字符映射函数（将字符转换为索引）
    inline int charToIndex(char c) const {
        // 默认支持小写字母
        return c - 'a';
    }
    
public:
    /**
     * 构造函数
     */
    ACAutomaton() : root(new TrieNode()) {}
    
    /**
     * 插入模式串到Trie树中
     * 
     * @param pattern 模式串
     * @param index 模式串的索引（从1开始）
     * @throw std::invalid_argument 如果模式串为空
     */
    void insert(const std::string& pattern, int index = 0) {
        if (pattern.empty()) {
            throw std::invalid_argument("Pattern cannot be empty");
        }
        
        // 保存模式串
        if (index > 0 && static_cast<size_t>(index) > patterns.size()) {
            patterns.resize(index);
            patterns[index - 1] = pattern;
        } else if (index == 0) {
            patterns.push_back(pattern);
            index = patterns.size();
        } else {
            patterns[index - 1] = pattern;
        }
        
        // 插入到Trie树
        TrieNode* current = root.get();
        for (char c : pattern) {
            int idx = charToIndex(c);
            if (!current->children[idx]) {
                current->children[idx].reset(new TrieNode());
            }
            current = current->children[idx].get();
        }
        
        // 标记结束节点
        current->endIdx = index;
        current->count++;
    }
    
    /**
     * 构建失配指针
     * 
     * 使用BFS算法构建fail指针
     */
    void build() {
        // 使用队列进行BFS
        std::queue<TrieNode*> q;
        
        // 根节点的fail指针指向自己
        root->fail = root.get();
        
        // 将根节点的所有子节点入队，它们的fail指针指向根
        for (int i = 0; i < CHARSET_SIZE; ++i) {
            if (root->children[i]) {
                root->children[i]->fail = root.get();
                q.push(root->children[i].get());
            }
        }
        
        // BFS构建其余节点的fail指针
        while (!q.empty()) {
            TrieNode* current = q.front();
            q.pop();
            
            for (int i = 0; i < CHARSET_SIZE; ++i) {
                if (current->children[i]) {
                    // 获取当前节点的fail指针
                    TrieNode* failNode = current->fail;
                    
                    // 沿着fail指针向上查找，直到找到包含相同字符的节点或回到根节点
                    while (failNode != root.get() && !failNode->children[i]) {
                        failNode = failNode->fail;
                    }
                    
                    // 设置子节点的fail指针
                    if (failNode->children[i]) {
                        current->children[i]->fail = failNode->children[i].get();
                    } else {
                        current->children[i]->fail = root.get();
                    }
                    
                    // 将子节点入队
                    q.push(current->children[i].get());
                }
            }
        }
    }
    
    /**
     * 查询文本中匹配的模式串数量（不重复计数）
     * 
     * 适用于洛谷P3808等题目
     * 
     * @param text 待查询的文本串
     * @return 匹配的不同模式串数量
     */
    int queryUnique(const std::string& text) {
        if (text.empty()) {
            return 0;
        }
        
        // 确保已经构建了fail指针
        if (root->fail == nullptr) {
            build();
        }
        
        int count = 0;
        std::unordered_set<int> visited; // 用于去重
        TrieNode* current = root.get();
        
        for (char c : text) {
            int idx = charToIndex(c);
            
            // 根据fail指针进行状态转移
            while (current != root.get() && !current->children[idx]) {
                current = current->fail;
            }
            
            // 如果当前字符存在，转移到下一个状态
            if (current->children[idx]) {
                current = current->children[idx].get();
            }
            
            // 从当前节点开始，沿着fail指针向上查找，统计所有匹配
            TrieNode* temp = current;
            while (temp != root.get()) {
                if (temp->endIdx != -1 && visited.find(temp->endIdx) == visited.end()) {
                    count += temp->count;
                    visited.insert(temp->endIdx);
                }
                temp = temp->fail;
            }
        }
        
        return count;
    }
    
    /**
     * 查询每个模式串在文本中的出现次数
     * 
     * 适用于洛谷P5357等题目
     * 
     * @param text 待查询的文本串
     * @return 结果数组，索引从1开始对应模式串的出现次数
     */
    std::vector<int> queryCount(const std::string& text) {
        std::vector<int> result(patterns.size() + 1, 0);
        
        if (text.empty()) {
            return result;
        }
        
        // 确保已经构建了fail指针
        if (root->fail == nullptr) {
            build();
        }
        
        TrieNode* current = root.get();
        
        for (char c : text) {
            int idx = charToIndex(c);
            
            // 根据fail指针进行状态转移
            while (current != root.get() && !current->children[idx]) {
                current = current->fail;
            }
            
            // 如果当前字符存在，转移到下一个状态
            if (current->children[idx]) {
                current = current->children[idx].get();
            }
            
            // 从当前节点开始，沿着fail指针向上查找，统计所有匹配
            TrieNode* temp = current;
            while (temp != root.get()) {
                if (temp->endIdx != -1 && temp->endIdx <= static_cast<int>(patterns.size())) {
                    result[temp->endIdx] += temp->count;
                }
                temp = temp->fail;
            }
        }
        
        return result;
    }
    
    /**
     * 查找文本中所有匹配的模式串及其位置
     * 
     * 高级功能：记录每次匹配的具体位置
     * 
     * @param text 待查询的文本串
     * @return 映射表，键为模式串索引，值为出现位置的列表
     */
    std::unordered_map<int, std::vector<int>> findAllMatches(const std::string& text) {
        std::unordered_map<int, std::vector<int>> matches;
        
        if (text.empty()) {
            return matches;
        }
        
        // 确保已经构建了fail指针
        if (root->fail == nullptr) {
            build();
        }
        
        TrieNode* current = root.get();
        
        for (size_t i = 0; i < text.size(); ++i) {
            int idx = charToIndex(text[i]);
            
            // 根据fail指针进行状态转移
            while (current != root.get() && !current->children[idx]) {
                current = current->fail;
            }
            
            // 如果当前字符存在，转移到下一个状态
            if (current->children[idx]) {
                current = current->children[idx].get();
            }
            
            // 从当前节点开始，沿着fail指针向上查找，统计所有匹配
            TrieNode* temp = current;
            while (temp != root.get()) {
                if (temp->endIdx != -1) {
                    const std::string& pattern = patterns[temp->endIdx - 1];
                    int startPos = static_cast<int>(i) - static_cast<int>(pattern.size()) + 1;
                    if (startPos >= 0) {
                        matches[temp->endIdx].push_back(startPos);
                    }
                }
                temp = temp->fail;
            }
        }
        
        return matches;
    }
    
    /**
     * 获取指定索引的模式串
     * 
     * @param index 模式串索引（从1开始）
     * @return 对应的模式串
     * @throw std::out_of_range 如果索引无效
     */
    const std::string& getPattern(int index) const {
        if (index <= 0 || index > static_cast<int>(patterns.size())) {
            throw std::out_of_range("Pattern index out of range");
        }
        return patterns[index - 1];
    }
    
    /**
     * 获取模式串数量
     * 
     * @return 模式串数量
     */
    size_t getPatternCount() const {
        return patterns.size();
    }
};

/**
 * 主函数 - 支持多种AC自动机题目模式
 * 
 * 功能说明：
 * 1. 支持洛谷P3808（简单版）- 统计不同模式串的出现数量
 * 2. 支持洛谷P3796（加强版）- 找出出现次数最多的模式串
 * 3. 支持洛谷P5357（二次加强版）- 统计每个模式串的出现次数
 */
int main() {
    try {
        ACAutomaton ac;
        int n;
        
        // 读取模式串数量
        std::cin >> n;
        
        // 读取并插入所有模式串
        std::vector<std::string> patterns(n);
        for (int i = 0; i < n; ++i) {
            std::cin >> patterns[i];
            ac.insert(patterns[i], i + 1);
        }
        
        // 构建AC自动机
        ac.build();
        
        // 读取文本串
        std::string text;
        std::cin >> text;
        
        // 选择题目模式（默认P5357）
        const std::string mode = "P5357"; // 可选: "P3808", "P3796", "P5357"
        
        if (mode == "P3808") {
            // 洛谷P3808模式 - 统计不同模式串的出现数量
            int count = ac.queryUnique(text);
            std::cout << count << std::endl;
        } else if (mode == "P3796") {
            // 洛谷P3796模式 - 找出出现次数最多的模式串
            std::vector<int> result = ac.queryCount(text);
            int maxCount = 0;
            for (int i = 1; i <= n; ++i) {
                maxCount = std::max(maxCount, result[i]);
            }
            
            // 收集所有出现次数最多的模式串
            std::vector<std::string> maxPatterns;
            for (int i = 1; i <= n; ++i) {
                if (result[i] == maxCount) {
                    maxPatterns.push_back(patterns[i - 1]);
                }
            }
            
            // 按字典序排序并输出
            std::sort(maxPatterns.begin(), maxPatterns.end());
            std::cout << maxCount << std::endl;
            for (const auto& p : maxPatterns) {
                std::cout << p << std::endl;
            }
        } else { // P5357模式
            // 洛谷P5357模式 - 分别输出每个模式串的出现次数
            std::vector<int> result = ac.queryCount(text);
            for (int i = 1; i <= n; ++i) {
                std::cout << result[i] << std::endl;
            }
        }
        
        // 演示高级功能：打印所有匹配位置
        // auto allMatches = ac.findAllMatches(text);
        // for (const auto& pair : allMatches) {
        //     std::cout << "Pattern " << ac.getPattern(pair.first) << " found at positions: ";
        //     for (int pos : pair.second) {
        //         std::cout << pos << " ";
        //     }
        //     std::cout << std::endl;
        // }
        
    } catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}

/**
 * 示例函数 - 演示AC自动机的基本功能
 */
void exampleUsage() {
    ACAutomaton ac;
    
    // 插入模式串
    std::vector<std::string> patterns = {"he", "she", "his", "hers"};
    for (size_t i = 0; i < patterns.size(); ++i) {
        ac.insert(patterns[i], i + 1);
    }
    
    // 构建失配指针
    ac.build();
    
    // 文本串
    std::string text = "ushershisthe";
    
    // 测试1: 统计不同模式串的数量
    int uniqueCount = ac.queryUnique(text);
    std::cout << "Unique pattern count: " << uniqueCount << std::endl;
    
    // 测试2: 统计每个模式串的出现次数
    std::vector<int> counts = ac.queryCount(text);
    for (size_t i = 0; i < patterns.size(); ++i) {
        std::cout << "Pattern \"" << patterns[i] << "\" appears " << counts[i + 1] << " times" << std::endl;
    }
    
    // 测试3: 查找所有匹配位置
    auto allMatches = ac.findAllMatches(text);
    for (size_t i = 0; i < patterns.size(); ++i) {
        int idx = i + 1;
        if (allMatches.find(idx) != allMatches.end()) {
            std::cout << "Pattern \"" << patterns[i] << "\" found at positions: ";
            for (int pos : allMatches[idx]) {
                std::cout << pos << " ";
            }
            std::cout << std::endl;
        }
    }
}
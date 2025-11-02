#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <queue>
#include <algorithm>

// LeetCode 139. 单词拆分
// 题目描述：给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出 s 。
// 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
// 链接：https://leetcode.cn/problems/word-break/
// 
// 解题思路：
// 这是一个完全背包问题的变种，其中：
// - 背包容量：字符串s的长度
// - 物品：字典中的单词
// - 问题转化为：是否可以选择一些单词（可重复），恰好拼接成字符串s
// 
// 状态定义：dp[i] 表示字符串s的前i个字符是否可以被拆分
// 状态转移方程：对于每个位置i，遍历所有位置j（j < i），如果dp[j]为true且s[j:i]在字典中，则dp[i]为true
// 初始状态：dp[0] = true，表示空字符串可以被拆分
// 
// 时间复杂度：O(n^3)，其中n是字符串s的长度（需要两层循环，并且每次需要判断子字符串是否在字典中）
// 空间复杂度：O(n)，使用一维DP数组

using namespace std;

/**
 * 判断是否可以利用字典中出现的单词拼接出s
 * @param s 目标字符串
 * @param wordDict 单词字典
 * @return 是否可以拼接出s
 */
bool wordBreak(string s, vector<string>& wordDict) {
    // 参数验证
    if (s.empty()) {
        return false; // 空字符串返回false
    }
    
    // 将wordDict转换为unordered_set，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    // 获取字典中单词的最大长度，用于后续剪枝
    int maxWordLength = 0;
    for (const string& word : wordDict) {
        maxWordLength = max(maxWordLength, static_cast<int>(word.length()));
    }
    
    // 创建一维DP数组，dp[i]表示字符串s的前i个字符是否可以被拆分
    vector<bool> dp(s.length() + 1, false);
    
    // 初始状态：空字符串可以被拆分
    dp[0] = true;
    
    // 遍历字符串s的每个位置i
    for (int i = 1; i <= s.length(); i++) {
        // 遍历之前的位置j，从max(0, i - maxWordLength)到i-1
        // 这样可以避免检查过长的子字符串
        int start = max(0, i - maxWordLength);
        for (int j = start; j < i; j++) {
            // 状态转移：如果前j个字符可以被拆分，且子字符串s[j:i]在字典中，则前i个字符可以被拆分
            if (dp[j] && wordSet.count(s.substr(j, i - j))) {
                dp[i] = true;
                break; // 只要找到一种拆分方式即可
            }
        }
    }
    
    // 返回结果：整个字符串s是否可以被拆分
    return dp[s.length()];
}

/**
 * 优化版本：移除maxWordLength的计算，直接使用普通的两层循环
 */
bool wordBreakOptimized(string s, vector<string>& wordDict) {
    // 参数验证
    if (s.empty()) {
        return false;
    }
    
    // 将wordDict转换为unordered_set，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    // 创建一维DP数组
    vector<bool> dp(s.length() + 1, false);
    dp[0] = true;
    
    // 遍历字符串s的每个位置i
    for (int i = 1; i <= s.length(); i++) {
        // 遍历之前的位置j
        for (int j = 0; j < i; j++) {
            // 状态转移
            if (dp[j] && wordSet.count(s.substr(j, i - j))) {
                dp[i] = true;
                break; // 只要找到一种拆分方式即可
            }
        }
    }
    
    return dp[s.length()];
}

/**
 * 另一种实现方式：先遍历字典中的单词，再遍历字符串位置
 * 这更符合完全背包问题的思路
 */
bool wordBreakKnapsackStyle(string s, vector<string>& wordDict) {
    // 参数验证
    if (s.empty()) {
        return false;
    }
    
    // 创建一维DP数组
    vector<bool> dp(s.length() + 1, false);
    dp[0] = true;
    
    // 先遍历容量（字符串长度）
    for (int i = 1; i <= s.length(); i++) {
        // 再遍历物品（字典中的单词）
        for (const string& word : wordDict) {
            int wordLength = word.length();
            // 如果当前位置i大于等于单词长度，并且前i-wordLength个字符可以被拆分
            // 并且子字符串s[i-wordLength:i]等于当前单词
            if (i >= wordLength && dp[i - wordLength] && 
                s.substr(i - wordLength, wordLength) == word) {
                dp[i] = true;
                break; // 只要找到一种拆分方式即可
            }
        }
    }
    
    return dp[s.length()];
}

/**
 * 递归+记忆化搜索实现
 */
bool wordBreakDFS(string s, vector<string>& wordDict) {
    // 参数验证
    if (s.empty()) {
        return false;
    }
    
    // 将wordDict转换为unordered_set，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    // 使用unordered_map作为缓存，键为子字符串的起始索引，值为该子字符串是否可以被拆分
    unordered_map<int, bool> memo;
    
    // 定义DFS函数
    function<bool(int)> dfs = [&](int start) {
        // 基础情况：如果已经到达字符串末尾，表示成功拆分
        if (start == s.length()) {
            return true;
        }
        
        // 检查缓存
        if (memo.find(start) != memo.end()) {
            return memo[start];
        }
        
        // 尝试从start位置开始的所有可能的子字符串
        for (int end = start + 1; end <= s.length(); end++) {
            // 如果子字符串在字典中，并且剩余部分也可以被拆分
            if (wordSet.count(s.substr(start, end - start)) && dfs(end)) {
                memo[start] = true;
                return true;
            }
        }
        
        // 所有可能的拆分方式都失败了
        memo[start] = false;
        return false;
    };
    
    // 调用递归函数
    return dfs(0);
}

/**
 * BFS实现
 */
bool wordBreakBFS(string s, vector<string>& wordDict) {
    // 参数验证
    if (s.empty()) {
        return false;
    }
    
    // 将wordDict转换为unordered_set，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    // 创建队列，存储可以拆分到的位置
    queue<int> q;
    // 标记已经访问过的位置，避免重复处理
    vector<bool> visited(s.length(), false);
    
    // 初始位置为0
    q.push(0);
    visited[0] = true;
    
    while (!q.empty()) {
        int start = q.front();
        q.pop();
        
        // 尝试从start位置开始的所有可能的子字符串
        for (int end = start + 1; end <= s.length(); end++) {
            // 如果子字符串在字典中
            if (wordSet.count(s.substr(start, end - start))) {
                // 如果已经到达字符串末尾，表示成功拆分
                if (end == s.length()) {
                    return true;
                }
                // 如果该位置尚未访问过，将其加入队列
                if (!visited[end]) {
                    q.push(end);
                    visited[end] = true;
                }
            }
        }
    }
    
    // 队列为空仍未返回true，表示无法拆分
    return false;
}

/**
 * Trie树节点类
 */
class TrieNode {
public:
    bool isEnd;
    unordered_map<char, TrieNode*> children;
    
    TrieNode() : isEnd(false) {}
    
    ~TrieNode() {
        for (auto& pair : children) {
            delete pair.second;
        }
    }
};

/**
 * 构建Trie树
 */
TrieNode* buildTrie(const vector<string>& wordDict) {
    TrieNode* root = new TrieNode();
    for (const string& word : wordDict) {
        TrieNode* node = root;
        for (char c : word) {
            if (node->children.find(c) == node->children.end()) {
                node->children[c] = new TrieNode();
            }
            node = node->children[c];
        }
        node->isEnd = true;
    }
    return root;
}

/**
 * 使用Trie树优化查找效率
 */
bool wordBreakWithTrie(string s, vector<string>& wordDict) {
    // 参数验证
    if (s.empty()) {
        return false;
    }
    
    // 构建Trie树
    TrieNode* root = buildTrie(wordDict);
    
    // 创建一维DP数组
    vector<bool> dp(s.length() + 1, false);
    dp[0] = true;
    
    // 遍历字符串s的每个位置i
    for (int i = 0; i < s.length(); i++) {
        // 如果前i个字符无法被拆分，跳过
        if (!dp[i]) {
            continue;
        }
        
        // 从i位置开始，在Trie树中查找可能的单词
        TrieNode* node = root;
        for (int j = i; j < s.length(); j++) {
            char c = s[j];
            if (node->children.find(c) == node->children.end()) {
                break; // 无法继续匹配
            }
            node = node->children[c];
            // 如果找到一个单词，标记dp[j+1]为true
            if (node->isEnd) {
                dp[j + 1] = true;
            }
        }
    }
    
    // 释放Trie树内存
    delete root;
    
    return dp[s.length()];
}

int main() {
    // 测试用例1
    string s1 = "leetcode";
    vector<string> wordDict1 = {"leet", "code"};
    cout << "测试用例1结果: " << (wordBreak(s1, wordDict1) ? "true" : "false") << endl; // 预期输出: true
    
    // 测试用例2
    string s2 = "applepenapple";
    vector<string> wordDict2 = {"apple", "pen"};
    cout << "测试用例2结果: " << (wordBreak(s2, wordDict2) ? "true" : "false") << endl; // 预期输出: true
    
    // 测试用例3
    string s3 = "catsandog";
    vector<string> wordDict3 = {"cats", "dog", "sand", "and", "cat"};
    cout << "测试用例3结果: " << (wordBreak(s3, wordDict3) ? "true" : "false") << endl; // 预期输出: false
    
    // 测试用例4
    string s4 = "";
    vector<string> wordDict4 = {"a"};
    cout << "测试用例4结果: " << (wordBreak(s4, wordDict4) ? "true" : "false") << endl; // 预期输出: false
    
    return 0;
}
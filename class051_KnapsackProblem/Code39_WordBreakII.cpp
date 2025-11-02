// LeetCode 140. 单词拆分 II
// 题目描述：给定一个字符串 s 和一个字符串字典 wordDict ，在字符串 s 中增加空格来构建一个句子，使得句子中所有的单词都在词典中。返回所有这样的可能句子。
// 链接：https://leetcode.cn/problems/word-break-ii/
// 
// 解题思路：
// 这是一个完全背包问题的变种，同时也是一个组合问题。我们需要找到所有可能的单词组合，使得它们的拼接等于字符串s。
// 
// 我们可以使用递归+记忆化搜索来解决这个问题：
// 1. 使用记忆化缓存，避免重复计算
// 2. 对于每个位置i，我们尝试所有可能的单词，如果s.substr(i, j-i)在字典中，我们递归处理剩余部分
// 3. 将当前单词与剩余部分的结果组合
// 
// 时间复杂度：O(n^2 * 2^n)，其中n是字符串s的长度。在最坏情况下，每个字符之间都可以拆分，会产生2^(n-1)种拆分方式。
// 空间复杂度：O(n^2)，用于存储记忆化缓存。

#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <algorithm>
using namespace std;

/**
 * 使用动态规划检查字符串是否可以拆分
 * @param s 字符串
 * @param wordSet 字典集合
 * @return 是否可以拆分
 */
bool canBreak(const string& s, const unordered_set<string>& wordSet) {
    int n = s.length();
    vector<bool> dp(n + 1, false);
    dp[0] = true; // 空字符串可以被拆分
    
    // 找出字典中最长单词的长度
    int maxLength = 0;
    for (const string& word : wordSet) {
        maxLength = max(maxLength, (int)word.length());
    }
    
    for (int i = 1; i <= n; i++) {
        // 只检查j >= i - maxLength的情况，避免不必要的检查
        int start = max(0, i - maxLength);
        for (int j = start; j < i; j++) {
            if (dp[j] && wordSet.find(s.substr(j, i - j)) != wordSet.end()) {
                dp[i] = true;
                break;
            }
        }
    }
    
    return dp[n];
}

/**
 * 递归辅助函数，使用记忆化搜索找出所有可能的拆分方案
 * @param s 字符串
 * @param start 起始位置
 * @param wordSet 字典集合
 * @param memo 记忆化缓存
 * @return 从start位置开始的子串的所有可能拆分方案
 */
vector<string> dfs(const string& s, int start, const unordered_set<string>& wordSet,
                  unordered_map<int, vector<string>>& memo) {
    // 如果已经计算过，直接返回缓存的结果
    if (memo.find(start) != memo.end()) {
        return memo[start];
    }
    
    vector<string> result;
    int n = s.length();
    
    // 基础情况：已经处理到字符串末尾
    if (start == n) {
        result.push_back(""); // 添加空字符串作为递归终止条件
        return result;
    }
    
    // 尝试所有可能的结束位置
    for (int end = start + 1; end <= n; end++) {
        // 获取当前子串
        string word = s.substr(start, end - start);
        
        // 如果当前子串在字典中，递归处理剩余部分
        if (wordSet.find(word) != wordSet.end()) {
            // 递归获取剩余部分的所有拆分方案
            vector<string> subList = dfs(s, end, wordSet, memo);
            
            // 将当前单词与剩余部分的拆分方案组合
            for (const string& sub : subList) {
                // 如果sub为空字符串，说明已经到达字符串末尾，不需要添加空格
                if (sub.empty()) {
                    result.push_back(word);
                } else {
                    result.push_back(word + " " + sub);
                }
            }
        }
    }
    
    // 缓存结果
    memo[start] = result;
    return result;
}

/**
 * 返回所有可能的单词拆分方案
 * @param s 字符串
 * @param wordDict 字典
 * @return 所有可能的拆分方案列表
 */
vector<string> wordBreak(string s, vector<string>& wordDict) {
    if (s.empty() || wordDict.empty()) {
        return {};
    }
    
    // 将字典转换为集合，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    // 首先使用动态规划检查是否可以拆分，如果不能拆分直接返回空列表
    // 这一步可以避免不必要的递归计算
    if (!canBreak(s, wordSet)) {
        return {};
    }
    
    // 创建记忆化缓存，memo[i]表示从位置i开始的子串的所有可能拆分方案
    unordered_map<int, vector<string>> memo;
    
    return dfs(s, 0, wordSet, memo);
}

/**
 * 另一种实现方式，使用动态规划来存储所有可能的拆分方案
 * @param s 字符串
 * @param wordDict 字典
 * @return 所有可能的拆分方案列表
 */
vector<string> wordBreakDP(string s, vector<string>& wordDict) {
    if (s.empty() || wordDict.empty()) {
        return {};
    }
    
    // 将字典转换为集合，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    int n = s.length();
    
    // dp[i]存储前i个字符的所有可能拆分方案
    vector<vector<string>> dp(n + 1);
    
    // 初始状态：空字符串有一个拆分方案（空字符串）
    dp[0].push_back("");
    
    // 填充dp数组
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j < i; j++) {
            string word = s.substr(j, i - j);
            if (wordSet.find(word) != wordSet.end() && !dp[j].empty()) {
                // 将当前单词与前j个字符的所有拆分方案组合
                for (const string& prev : dp[j]) {
                    if (prev.empty()) {
                        dp[i].push_back(word);
                    } else {
                        dp[i].push_back(prev + " " + word);
                    }
                }
            }
        }
    }
    
    return dp[n];
}

/**
 * 优化的递归辅助函数，使用最大单词长度来限制搜索范围
 * @param s 字符串
 * @param start 起始位置
 * @param wordSet 字典集合
 * @param memo 记忆化缓存
 * @param maxLength 字典中最长单词的长度
 * @return 从start位置开始的子串的所有可能拆分方案
 */
vector<string> dfsOptimized(const string& s, int start, const unordered_set<string>& wordSet,
                          unordered_map<int, vector<string>>& memo, int maxLength) {
    // 如果已经计算过，直接返回缓存的结果
    if (memo.find(start) != memo.end()) {
        return memo[start];
    }
    
    vector<string> result;
    int n = s.length();
    
    // 基础情况：已经处理到字符串末尾
    if (start == n) {
        result.push_back("");
        return result;
    }
    
    // 限制end的范围为start + maxLength，避免不必要的检查
    int endMax = min(start + maxLength, n);
    for (int end = start + 1; end <= endMax; end++) {
        string word = s.substr(start, end - start);
        
        if (wordSet.find(word) != wordSet.end()) {
            vector<string> subList = dfsOptimized(s, end, wordSet, memo, maxLength);
            
            for (const string& sub : subList) {
                if (sub.empty()) {
                    result.push_back(word);
                } else {
                    result.push_back(word + " " + sub);
                }
            }
        }
    }
    
    // 缓存结果
    memo[start] = result;
    return result;
}

/**
 * 优化的DFS实现，使用最大单词长度来限制搜索范围
 * @param s 字符串
 * @param wordDict 字典
 * @return 所有可能的拆分方案列表
 */
vector<string> wordBreakOptimized(string s, vector<string>& wordDict) {
    if (s.empty() || wordDict.empty()) {
        return {};
    }
    
    // 将字典转换为集合，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    // 找出字典中最长单词的长度
    int maxLength = 0;
    for (const string& word : wordDict) {
        maxLength = max(maxLength, (int)word.length());
    }
    
    // 首先检查是否可以拆分
    if (!canBreak(s, wordSet)) {
        return {};
    }
    
    // 创建记忆化缓存
    unordered_map<int, vector<string>> memo;
    
    return dfsOptimized(s, 0, wordSet, memo, maxLength);
}

// 打印测试结果
void printResult(const string& s, const vector<string>& wordDict, const vector<string>& result) {
    cout << "字符串: \"" << s << "\"\n";
    cout << "字典: [";
    for (size_t i = 0; i < wordDict.size(); i++) {
        cout << "\"" << wordDict[i] << "\"";
        if (i < wordDict.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]\n";
    
    cout << "结果: [";
    for (size_t i = 0; i < result.size(); i++) {
        cout << "\"" << result[i] << "\"";
        if (i < result.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]\n";
    cout << "---------------------------\n";
}

// 主函数，用于测试
int main() {
    // 测试用例1
    string s1 = "catsanddog";
    vector<string> wordDict1 = {"cat", "cats", "and", "sand", "dog"};
    cout << "测试用例1:\n";
    printResult(s1, wordDict1, wordBreak(s1, wordDict1));
    
    // 测试用例2
    string s2 = "pineapplepenapple";
    vector<string> wordDict2 = {"apple", "pen", "applepen", "pine", "pineapple"};
    cout << "测试用例2:\n";
    printResult(s2, wordDict2, wordBreak(s2, wordDict2));
    
    // 测试用例3
    string s3 = "catsandog";
    vector<string> wordDict3 = {"cats", "dog", "sand", "and", "cat"};
    cout << "测试用例3:\n";
    printResult(s3, wordDict3, wordBreak(s3, wordDict3));
    
    // 测试不同实现
    cout << "测试不同实现:\n";
    cout << "动态规划版本 (测试用例1): \n";
    printResult(s1, wordDict1, wordBreakDP(s1, wordDict1));
    
    cout << "优化DFS版本 (测试用例1): \n";
    printResult(s1, wordDict1, wordBreakOptimized(s1, wordDict1));
    
    return 0;
}
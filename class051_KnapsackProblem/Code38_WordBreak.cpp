// LeetCode 139. 单词拆分
// 题目描述：给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出 s 。
// 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
// 链接：https://leetcode.cn/problems/word-break/
// 
// 解题思路：
// 这是一个完全背包问题的变种。我们可以将字符串s看作是背包，将字典中的单词看作是物品。
// 问题转化为：是否可以从字典中选择一些单词（可以重复选择），使得它们的拼接恰好等于字符串s。
// 
// 状态定义：dp[i] 表示字符串s的前i个字符是否可以被拆分成字典中的单词
// 状态转移方程：对于每个i，我们检查所有j < i，如果dp[j]为true且s.substr(j, i-j)在字典中，则dp[i]为true
// 初始状态：dp[0] = true，表示空字符串可以被拆分
// 
// 时间复杂度：O(n^3)，其中n是字符串s的长度
// 空间复杂度：O(n + m)，其中m是字典中所有单词的字符总数

#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <queue>
using namespace std;

/**
 * 判断字符串是否可以被拆分成字典中的单词
 * @param s 字符串
 * @param wordDict 字典
 * @return 是否可以拆分
 */
bool wordBreak(string s, vector<string>& wordDict) {
    if (s.empty()) {
        return false;
    }
    
    // 将字典转换为集合，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    int n = s.length();
    
    // 创建DP数组，dp[i]表示字符串s的前i个字符是否可以被拆分成字典中的单词
    vector<bool> dp(n + 1, false);
    
    // 初始状态：空字符串可以被拆分
    dp[0] = true;
    
    // 遍历字符串的每个位置
    for (int i = 1; i <= n; i++) {
        // 遍历所有可能的拆分点j
        for (int j = 0; j < i; j++) {
            // 如果dp[j]为true（前j个字符可以拆分），且s.substr(j, i-j)在字典中，那么dp[i]为true
            if (dp[j] && wordSet.find(s.substr(j, i - j)) != wordSet.end()) {
                dp[i] = true;
                break; // 只要找到一个可行的拆分方式就可以提前结束内层循环
            }
        }
    }
    
    return dp[n];
}

/**
 * 优化的版本，限制j的范围为最大单词长度，避免不必要的检查
 * @param s 字符串
 * @param wordDict 字典
 * @return 是否可以拆分
 */
bool wordBreakOptimized(string s, vector<string>& wordDict) {
    if (s.empty()) {
        return false;
    }
    
    // 将字典转换为集合，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    int n = s.length();
    
    // 找出字典中最长单词的长度
    int maxLength = 0;
    for (const string& word : wordDict) {
        maxLength = max(maxLength, (int)word.length());
    }
    
    // 创建DP数组
    vector<bool> dp(n + 1, false);
    dp[0] = true;
    
    // 遍历字符串的每个位置
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
 * 使用递归+记忆化搜索实现
 * 这个方法对于较大的输入可能会超时，但展示了递归的思路
 * @param s 字符串
 * @param wordDict 字典
 * @return 是否可以拆分
 */
bool wordBreakRecursive(string s, vector<string>& wordDict) {
    if (s.empty()) {
        return false;
    }
    
    // 将字典转换为集合
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    // 创建记忆化缓存，memo[i]表示从位置i开始的子串是否可以被拆分
    vector<int> memo(s.length(), -1); // -1表示未计算，0表示false，1表示true
    
    function<bool(int)> dfs = [&](int start) -> bool {
        // 基础情况：已经处理到字符串末尾
        if (start == s.length()) {
            return true;
        }
        
        // 检查缓存
        if (memo[start] != -1) {
            return memo[start] == 1;
        }
        
        // 尝试所有可能的结束位置
        for (int end = start + 1; end <= s.length(); end++) {
            // 如果s.substr(start, end-start)在字典中，且剩余部分可以拆分，则返回true
            if (wordSet.find(s.substr(start, end - start)) != wordSet.end() && dfs(end)) {
                memo[start] = 1;
                return true;
            }
        }
        
        // 如果所有可能性都尝试过仍无法拆分，返回false
        memo[start] = 0;
        return false;
    };
    
    return dfs(0);
}

/**
 * 使用BFS实现
 * @param s 字符串
 * @param wordDict 字典
 * @return 是否可以拆分
 */
bool wordBreakBFS(string s, vector<string>& wordDict) {
    if (s.empty()) {
        return false;
    }
    
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    vector<bool> visited(s.length(), false); // 记录哪些位置已经被访问过，避免重复处理
    
    // 使用队列进行BFS，队列中存储的是当前处理到的位置
    queue<int> q;
    q.push(0);
    visited[0] = true;
    
    while (!q.empty()) {
        int start = q.front();
        q.pop();
        
        // 尝试所有可能的结束位置
        for (int end = start + 1; end <= s.length(); end++) {
            // 如果当前子串在字典中，且结束位置尚未访问过，则继续BFS
            if (wordSet.find(s.substr(start, end - start)) != wordSet.end() && !visited[end]) {
                if (end == s.length()) {
                    return true; // 已经到达字符串末尾，找到了解决方案
                }
                q.push(end);
                visited[end] = true;
            }
        }
    }
    
    return false; // 无法拆分
}

// 打印测试结果
void printResult(const string& s, vector<string>& wordDict, bool result) {
    cout << "字符串: \"" << s << "\"\n";
    cout << "字典: [";
    for (size_t i = 0; i < wordDict.size(); i++) {
        cout << "\"" << wordDict[i] << "\"";
        if (i < wordDict.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]\n";
    cout << "结果: " << (result ? "true" : "false") << "\n";
    cout << "---------------------------\n";
}

// 主函数，用于测试
int main() {
    // 测试用例1
    string s1 = "leetcode";
    vector<string> wordDict1 = {"leet", "code"};
    cout << "测试用例1:\n";
    printResult(s1, wordDict1, wordBreak(s1, wordDict1));
    
    // 测试用例2
    string s2 = "applepenapple";
    vector<string> wordDict2 = {"apple", "pen"};
    cout << "测试用例2:\n";
    printResult(s2, wordDict2, wordBreak(s2, wordDict2));
    
    // 测试用例3
    string s3 = "catsandog";
    vector<string> wordDict3 = {"cats", "dog", "sand", "and", "cat"};
    cout << "测试用例3:\n";
    printResult(s3, wordDict3, wordBreak(s3, wordDict3));
    
    // 测试不同实现
    cout << "测试不同实现:\n";
    cout << "优化版本 (测试用例1): " << (wordBreakOptimized(s1, wordDict1) ? "true" : "false") << "\n";
    cout << "递归版本 (测试用例1): " << (wordBreakRecursive(s1, wordDict1) ? "true" : "false") << "\n";
    cout << "BFS版本 (测试用例1): " << (wordBreakBFS(s1, wordDict1) ? "true" : "false") << "\n";
    
    // 测试用例4
    string s4 = "catsandog";
    vector<string> wordDict4 = {"cats", "dog", "sand", "and", "cat", "sando", "g"};
    cout << "测试用例4:\n";
    printResult(s4, wordDict4, wordBreak(s4, wordDict4));
    
    return 0;
}
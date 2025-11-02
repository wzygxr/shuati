// 单词拆分
// 给你一个字符串 s 和一个字符串列表 wordDict 作为字典。
// 如果可以利用字典中出现的一个或多个单词拼接出 s 则返回 true。
// 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
// 测试链接 : https://leetcode.cn/problems/word-break/

#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <algorithm>
using namespace std;

// DFS辅助函数声明
bool dfs(string s, unordered_set<string>& wordSet, unordered_map<string, bool>& memo);

// 标准DP版本
bool wordBreak(string s, vector<string>& wordDict) {
    if (s.empty()) return true;
    if (wordDict.empty()) return false;
    
    // 将字典转换为unordered_set，提高查找效率
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    
    int n = s.length();
    // dp[i]表示s的前i个字符是否可以被拆分
    vector<bool> dp(n + 1, false);
    dp[0] = true; // 空字符串可以被拆分
    
    // 记录字典中最长单词的长度，用于剪枝
    int maxLen = 0;
    for (const string& word : wordDict) {
        maxLen = max(maxLen, (int)word.length());
    }
    
    // 填充DP数组
    for (int i = 1; i <= n; i++) {
        // 从后往前遍历，减少不必要的比较
        for (int j = i - 1; j >= 0 && j >= i - maxLen; j--) {
            // 如果前j个字符可以被拆分，且s.substr(j, i-j)在字典中
            if (dp[j] && wordSet.find(s.substr(j, i - j)) != wordSet.end()) {
                dp[i] = true;
                break; // 找到一种可行方案即可退出
            }
        }
    }
    
    return dp[n];
}

// DFS + 记忆化搜索版本
bool wordBreakMemo(string s, vector<string>& wordDict) {
    if (s.empty()) return true;
    if (wordDict.empty()) return false;
    
    unordered_set<string> wordSet(wordDict.begin(), wordDict.end());
    unordered_map<string, bool> memo;
    
    return dfs(s, wordSet, memo);
}

// DFS辅助函数定义
bool dfs(string s, unordered_set<string>& wordSet, unordered_map<string, bool>& memo) {
    if (memo.find(s) != memo.end()) {
        return memo[s];
    }
    
    if (s.empty()) {
        memo[s] = true;
        return true;
    }
    
    for (int i = 1; i <= (int)s.length(); i++) {
        string prefix = s.substr(0, i);
        if (wordSet.find(prefix) != wordSet.end()) {
            string suffix = s.substr(i);
            if (dfs(suffix, wordSet, memo)) {
                memo[s] = true;
                return true;
            }
        }
    }
    
    memo[s] = false;
    return false;
}

// 测试方法
int main() {
    // 测试用例1
    string s1 = "leetcode";
    vector<string> wordDict1 = {"leet", "code"};
    cout << "测试用例1:" << endl;
    cout << "标准版本: " << (wordBreak(s1, wordDict1) ? "true" : "false") << endl;
    cout << "记忆化版本: " << (wordBreakMemo(s1, wordDict1) ? "true" : "false") << endl;
    cout << "预期结果: true" << endl << endl;
    
    // 测试用例2
    string s2 = "applepenapple";
    vector<string> wordDict2 = {"apple", "pen"};
    cout << "测试用例2:" << endl;
    cout << "标准版本: " << (wordBreak(s2, wordDict2) ? "true" : "false") << endl;
    cout << "记忆化版本: " << (wordBreakMemo(s2, wordDict2) ? "true" : "false") << endl;
    cout << "预期结果: true" << endl << endl;
    
    // 测试用例3
    string s3 = "catsandog";
    vector<string> wordDict3 = {"cats", "dog", "sand", "and", "cat"};
    cout << "测试用例3:" << endl;
    cout << "标准版本: " << (wordBreak(s3, wordDict3) ? "true" : "false") << endl;
    cout << "记忆化版本: " << (wordBreakMemo(s3, wordDict3) ? "true" : "false") << endl;
    cout << "预期结果: false" << endl << endl;
    
    // 测试用例4：边界情况
    string s4 = "";
    vector<string> wordDict4 = {"a"};
    cout << "测试用例4（空字符串）:" << endl;
    cout << "标准版本: " << (wordBreak(s4, wordDict4) ? "true" : "false") << endl;
    cout << "记忆化版本: " << (wordBreakMemo(s4, wordDict4) ? "true" : "false") << endl;
    cout << "预期结果: true" << endl << endl;
    
    // 测试用例5：复杂情况
    string s5 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
    vector<string> wordDict5 = {"a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa"};
    cout << "测试用例5（复杂情况）:" << endl;
    cout << "标准版本: " << (wordBreak(s5, wordDict5) ? "true" : "false") << endl;
    cout << "记忆化版本: " << (wordBreakMemo(s5, wordDict5) ? "true" : "false") << endl;
    cout << "预期结果: false" << endl;
    
    return 0;
}
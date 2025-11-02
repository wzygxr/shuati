#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <functional>

using namespace std;

// 函数声明
void backtrackIP(string& s, int start, vector<string>& current, vector<string>& result);
void backtrackWordBreak(string& s, int start, vector<string>& wordDict, 
                       string& current, vector<string>& result);

/**
 * 分割回文串问题 - C++版本
 * 
 * 问题描述：
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
 * 
 * 算法思路：
 * 方法1：回溯算法 - 枚举所有可能的分割方案
 * 方法2：动态规划预处理 - 优化回文串判断
 * 
 * 时间复杂度分析：
 * - 回溯算法：O(n * 2^n)，其中n为字符串长度
 * - 动态规划预处理：O(n^2)，预处理回文信息
 * 
 * 空间复杂度分析：
 * - 回溯算法：O(n)，递归栈深度
 * - 动态规划预处理：O(n^2)，存储回文信息
 * 
 * 工程化考量：
 * 1. 剪枝优化：提前终止无效分支
 * 2. 记忆化搜索：缓存中间结果
 * 3. 边界处理：空字符串、单字符等情况
 * 4. 可测试性：设计全面的测试用例
 */

class PalindromePartitioningSolver {
public:
    /**
     * 方法1：回溯算法
     * 枚举所有可能的分割方案
     */
    vector<vector<string>> partition(string s) {
        vector<vector<string>> result;
        vector<string> current;
        backtrack(s, 0, current, result);
        return result;
    }
    
    /**
     * 方法2：回溯算法 + 动态规划预处理
     * 使用动态规划预处理回文信息，优化判断效率
     */
    vector<vector<string>> partitionWithDP(string s) {
        int n = s.length();
        // 预处理回文信息
        vector<vector<bool>> dp(n, vector<bool>(n, false));
        
        // 初始化动态规划数组
        for (int i = 0; i < n; i++) {
            dp[i][i] = true; // 单个字符是回文
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s[i] == s[j]) {
                    if (len == 2 || dp[i + 1][j - 1]) {
                        dp[i][j] = true;
                    }
                }
            }
        }
        
        vector<vector<string>> result;
        vector<string> current;
        backtrackWithDP(s, 0, dp, current, result);
        return result;
    }
    
    /**
     * 方法3：记忆化搜索
     * 使用记忆化技术避免重复计算
     */
    vector<vector<string>> partitionMemo(string s) {
        int n = s.length();
        vector<vector<vector<string>>> memo(n);
        return partitionHelper(s, 0, memo);
    }
    
private:
    /**
     * 回溯函数
     */
    void backtrack(string& s, int start, vector<string>& current, vector<vector<string>>& result) {
        if (start == s.length()) {
            result.push_back(current);
            return;
        }
        
        for (int end = start; end < s.length(); end++) {
            // 检查子串s[start..end]是否是回文
            if (isPalindrome(s, start, end)) {
                // 选择当前分割
                current.push_back(s.substr(start, end - start + 1));
                // 递归处理剩余部分
                backtrack(s, end + 1, current, result);
                // 回溯
                current.pop_back();
            }
        }
    }
    
    /**
     * 使用动态规划预处理的回溯函数
     */
    void backtrackWithDP(string& s, int start, vector<vector<bool>>& dp, 
                        vector<string>& current, vector<vector<string>>& result) {
        if (start == s.length()) {
            result.push_back(current);
            return;
        }
        
        for (int end = start; end < s.length(); end++) {
            if (dp[start][end]) {
                current.push_back(s.substr(start, end - start + 1));
                backtrackWithDP(s, end + 1, dp, current, result);
                current.pop_back();
            }
        }
    }
    
    /**
     * 记忆化搜索辅助函数
     */
    vector<vector<string>> partitionHelper(string& s, int start, 
                                          vector<vector<vector<string>>>& memo) {
        if (start == s.length()) {
            return {{}}; // 返回包含空列表的列表
        }
        
        if (!memo[start].empty()) {
            return memo[start];
        }
        
        vector<vector<string>> result;
        
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome(s, start, end)) {
                string substring = s.substr(start, end - start + 1);
                vector<vector<string>> subResults = partitionHelper(s, end + 1, memo);
                
                for (auto& subResult : subResults) {
                    vector<string> newResult = {substring};
                    newResult.insert(newResult.end(), subResult.begin(), subResult.end());
                    result.push_back(newResult);
                }
            }
        }
        
        memo[start] = result;
        return result;
    }
    
    /**
     * 判断子串是否是回文
     */
    bool isPalindrome(string& s, int left, int right) {
        while (left < right) {
            if (s[left] != s[right]) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
};

/**
 * 补充训练题目 - C++实现
 */

/**
 * LeetCode 132. 分割回文串 II
 * 给定一个字符串s，将s分割成一些子串，使每个子串都是回文串，返回符合要求的最少分割次数
 */
int minCut(string s) {
    int n = s.length();
    if (n <= 1) return 0;
    
    // dp[i]表示s[0..i]的最小分割次数
    vector<int> dp(n, n);
    // isPal[i][j]表示s[i..j]是否是回文
    vector<vector<bool>> isPal(n, vector<bool>(n, false));
    
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= i; j++) {
            if (s[i] == s[j] && (i - j <= 2 || isPal[j + 1][i - 1])) {
                isPal[j][i] = true;
                if (j == 0) {
                    dp[i] = 0; // 整个字符串是回文，不需要分割
                } else {
                    dp[i] = min(dp[i], dp[j - 1] + 1);
                }
            }
        }
    }
    
    return dp[n - 1];
}

/**
 * LeetCode 93. 复原IP地址
 * 给定一个只包含数字的字符串，复原它并返回所有可能的IP地址格式
 */
vector<string> restoreIpAddresses(string s) {
    vector<string> result;
    vector<string> current;
    backtrackIP(s, 0, current, result);
    return result;
}

void backtrackIP(string& s, int start, vector<string>& current, vector<string>& result) {
    if (current.size() == 4) {
        if (start == s.length()) {
            result.push_back(current[0] + "." + current[1] + "." + current[2] + "." + current[3]);
        }
        return;
    }
    
    for (int len = 1; len <= 3; len++) {
        if (start + len > s.length()) break;
        
        string segment = s.substr(start, len);
        
        // 检查段是否有效
        if ((segment[0] == '0' && segment.length() > 1) || stoi(segment) > 255) {
            continue;
        }
        
        current.push_back(segment);
        backtrackIP(s, start + len, current, result);
        current.pop_back();
    }
}

/**
 * LeetCode 140. 单词拆分 II
 * 给定一个非空字符串s和一个包含非空单词列表的字典，在字符串中增加空格来构建一个句子，
 * 使得句子中所有的单词都在词典中。返回所有这些可能的句子。
 */
vector<string> wordBreak(string s, vector<string>& wordDict) {
    vector<string> result;
    string current;
    backtrackWordBreak(s, 0, wordDict, current, result);
    return result;
}

void backtrackWordBreak(string& s, int start, vector<string>& wordDict, 
                       string& current, vector<string>& result) {
    if (start == s.length()) {
        result.push_back(current);
        return;
    }
    
    for (string& word : wordDict) {
        int len = word.length();
        if (start + len <= s.length() && s.substr(start, len) == word) {
            string newCurrent = current.empty() ? word : current + " " + word;
            backtrackWordBreak(s, start + len, wordDict, newCurrent, result);
        }
    }
}

/**
 * LeetCode 131. 分割回文串（优化版）
 * 使用中心扩展法预处理回文信息
 */
vector<vector<string>> partitionOptimized(string s) {
    int n = s.length();
    vector<vector<bool>> dp(n, vector<bool>(n, false));
    
    // 预处理回文信息
    for (int i = 0; i < n; i++) {
        dp[i][i] = true;
    }
    
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            if (s[i] == s[j] && (len == 2 || dp[i + 1][j - 1])) {
                dp[i][j] = true;
            }
        }
    }
    
    vector<vector<string>> result;
    vector<string> current;
    
    function<void(int)> backtrack = [&](int start) {
        if (start == n) {
            result.push_back(current);
            return;
        }
        
        for (int end = start; end < n; end++) {
            if (dp[start][end]) {
                current.push_back(s.substr(start, end - start + 1));
                backtrack(end + 1);
                current.pop_back();
            }
        }
    };
    
    backtrack(0);
    return result;
}

// 测试函数
void testPalindromePartitioning() {
    PalindromePartitioningSolver solver;
    
    // 测试用例1
    string s1 = "aab";
    vector<vector<string>> result1 = solver.partition(s1);
    vector<vector<string>> result1_dp = solver.partitionWithDP(s1);
    vector<vector<string>> result1_memo = solver.partitionMemo(s1);
    
    cout << "测试用例1 - 字符串: \"" << s1 << "\"" << endl;
    cout << "回溯算法结果数量: " << result1.size() << endl;
    cout << "动态规划预处理结果数量: " << result1_dp.size() << endl;
    cout << "记忆化搜索结果数量: " << result1_memo.size() << endl;
    
    cout << "具体分割方案:" << endl;
    for (int i = 0; i < result1.size(); i++) {
        cout << "方案 " << i + 1 << ": [";
        for (int j = 0; j < result1[i].size(); j++) {
            cout << "\"" << result1[i][j] << "\"";
            if (j < result1[i].size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
    cout << endl;
    
    // 测试用例2
    string s2 = "a";
    vector<vector<string>> result2 = solver.partition(s2);
    
    cout << "测试用例2 - 字符串: \"" << s2 << "\"" << endl;
    cout << "结果数量: " << result2.size() << endl;
    cout << "具体分割方案:" << endl;
    for (auto& partition : result2) {
        cout << "[";
        for (int j = 0; j < partition.size(); j++) {
            cout << "\"" << partition[j] << "\"";
            if (j < partition.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
    cout << endl;
    
    // 测试补充题目
    cout << "=== 补充训练题目测试 ===" << endl;
    
    // 测试分割回文串II
    string s3 = "aab";
    cout << "分割回文串II - 字符串: \"" << s3 << "\" -> 最少分割次数: " << minCut(s3) << endl;
    
    // 测试复原IP地址
    string s4 = "25525511135";
    vector<string> ipResults = restoreIpAddresses(s4);
    cout << "复原IP地址 - 字符串: \"" << s4 << "\" -> 结果数量: " << ipResults.size() << endl;
    for (string& ip : ipResults) {
        cout << "IP地址: " << ip << endl;
    }
}

int main() {
    testPalindromePartitioning();
    return 0;
}

/**
 * 算法技巧总结 - C++版本
 * 
 * 核心概念：
 * 1. 回溯算法框架：
 *    - 选择：选择当前分割点
 *    - 约束：检查子串是否是回文
 *    - 目标：完成整个字符串的分割
 *    - 回溯：撤销当前选择，尝试其他选择
 * 
 * 2. 优化技术：
 *    - 动态规划预处理：提前计算回文信息
 *    - 记忆化搜索：缓存中间结果避免重复计算
 *    - 剪枝优化：提前终止无效分支
 * 
 * 3. 字符串处理技巧：
 *    - 子串提取：使用substr函数
 *    - 回文判断：双指针法或动态规划
 *    - 边界处理：空字符串、单字符等情况
 * 
 * 调试技巧：
 * 1. 打印中间状态：跟踪回溯过程
 * 2. 边界值测试：测试各种边界情况
 * 3. 性能分析：比较不同算法的执行时间
 * 
 * 工程化实践：
 * 1. 模块化设计：分离算法逻辑和业务逻辑
 * 2. 异常安全：确保资源正确释放
 * 3. 代码可读性：使用有意义的变量名和注释
 */
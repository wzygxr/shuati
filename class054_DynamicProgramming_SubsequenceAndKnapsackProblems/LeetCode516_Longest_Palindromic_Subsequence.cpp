#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;

/**
 * LeetCode 516. 最长回文子序列
 * 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
 * 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
 * 测试链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 
 * 算法详解：
 * 使用动态规划求解最长回文子序列问题。
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²) 或 O(n)（优化版本）
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入字符串有效性
 * 2. 边界处理：单字符字符串的情况
 * 3. 性能优化：使用空间优化技术
 * 4. 代码质量：清晰的变量命名和注释
 */

class Solution {
public:
    /**
     * 基础动态规划解法
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     */
    static int longestPalindromeSubseq(const string& s) {
        if (s.empty()) {
            return 0;
        }
        
        int n = s.length();
        if (n == 1) {
            return 1;
        }
        
        // 创建dp表
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化：单个字符都是回文
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 从长度为2的子串开始计算
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                if (s[i] == s[j]) {
                    if (len == 2) {
                        dp[i][j] = 2;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1] + 2;
                    }
                } else {
                    dp[i][j] = max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 空间优化版本（使用一维数组）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     */
    static int longestPalindromeSubseqOptimized(const string& s) {
        if (s.empty()) {
            return 0;
        }
        
        int n = s.length();
        if (n == 1) {
            return 1;
        }
        
        vector<int> dp(n, 1); // 初始化每个字符自身都是回文
        
        for (int i = n - 2; i >= 0; i--) {
            int prev = 0; // 保存dp[i+1][j-1]的值
            for (int j = i + 1; j < n; j++) {
                int temp = dp[j]; // 保存当前值
                
                if (s[i] == s[j]) {
                    dp[j] = prev + 2;
                } else {
                    dp[j] = max(dp[j], dp[j - 1]);
                }
                
                prev = temp; // 更新prev
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 递归+记忆化搜索解法
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     */
    static int longestPalindromeSubseqMemo(const string& s) {
        if (s.empty()) {
            return 0;
        }
        
        int n = s.length();
        vector<vector<int>> memo(n, vector<int>(n, -1));
        return dfs(s, 0, n - 1, memo);
    }
    
private:
    static int dfs(const string& s, int i, int j, vector<vector<int>>& memo) {
        if (i > j) return 0;
        if (i == j) return 1;
        
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        int result;
        if (s[i] == s[j]) {
            result = dfs(s, i + 1, j - 1, memo) + 2;
        } else {
            result = max(dfs(s, i + 1, j, memo), dfs(s, i, j - 1, memo));
        }
        
        memo[i][j] = result;
        return result;
    }
};

/**
 * 测试辅助函数
 */
void runTest(const string& description, const string& s, int expected) {
    cout << description << endl;
    cout << "输入字符串: \"" << s << "\"" << endl;
    cout << "期望结果: " << expected << endl;
    
    int result1 = Solution::longestPalindromeSubseq(s);
    int result2 = Solution::longestPalindromeSubseqOptimized(s);
    int result3 = Solution::longestPalindromeSubseqMemo(s);
    
    cout << "基础DP: " << result1 << " " << (result1 == expected ? "✓" : "✗") << endl;
    cout << "优化DP: " << result2 << " " << (result2 == expected ? "✓" : "✗") << endl;
    cout << "记忆化搜索: " << result3 << " " << (result3 == expected ? "✓" : "✗") << endl;
    
    if (result1 == result2 && result2 == result3 && result1 == expected) {
        cout << "测试通过 ✓" << endl;
    } else {
        cout << "测试失败 ✗" << endl;
    }
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成测试数据
    const int n = 1000;
    string s;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis('a', 'z');
    
    for (int i = 0; i < n; i++) {
        s += static_cast<char>(dis(gen));
    }
    
    cout << "测试数据规模: " << n << "个字符" << endl;
    
    // 测试基础DP算法
    auto start = chrono::high_resolution_clock::now();
    int result1 = Solution::longestPalindromeSubseq(s);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "基础DP算法:" << endl;
    cout << "  结果: " << result1 << endl;
    cout << "  耗时: " << duration1.count() << " 毫秒" << endl;
    
    // 测试优化DP算法
    start = chrono::high_resolution_clock::now();
    int result2 = Solution::longestPalindromeSubseqOptimized(s);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "优化DP算法:" << endl;
    cout << "  结果: " << result2 << endl;
    cout << "  耗时: " << duration2.count() << " 毫秒" << endl;
    
    // 验证结果一致性
    if (result1 == result2) {
        cout << "结果一致性验证: 通过 ✓" << endl;
    } else {
        cout << "结果一致性验证: 失败 ✗" << endl;
    }
    
    cout << "注意：记忆化搜索在大规模数据下可能栈溢出" << endl;
    cout << endl;
}

int main() {
    cout << "=== LeetCode 516 最长回文子序列测试 ===" << endl << endl;
    
    // 测试用例1：基本功能测试
    runTest("测试用例1 - 基本功能", "bbbab", 4);
    
    // 测试用例2：LeetCode官方示例
    runTest("测试用例2 - 官方示例", "cbbd", 2);
    
    // 测试用例3：全相同字符
    runTest("测试用例3 - 全相同字符", "aaaa", 4);
    
    // 测试用例4：单字符
    runTest("测试用例4 - 单字符", "a", 1);
    
    // 测试用例5：空字符串
    runTest("测试用例5 - 空字符串", "", 0);
    
    // 测试用例6：交替字符
    runTest("测试用例6 - 交替字符", "abab", 3);
    
    // 性能测试
    performanceTest();
    
    cout << "所有测试完成！" << endl;
    return 0;
}

/**
 * 复杂度分析详细计算：
 * 
 * 基础动态规划：
 * - 时间：双重循环O(n²)
 * - 空间：二维vector大小n×n → O(n²)
 * 
 * 空间优化版本：
 * - 时间：O(n²)
 * - 空间：一维vector大小n → O(n)
 * 
 * 记忆化搜索：
 * - 时间：O(n²)
 * - 空间：记忆化数组O(n²) + 递归栈O(n) → O(n²)
 * 
 * C++特性说明：
 * 1. 使用const引用避免字符串拷贝
 * 2. STL容器提供高效的内存管理
 * 3. 使用chrono库进行精确性能测试
 * 4. RAII机制自动管理资源
 * 
 * 工程化建议：
 * 1. 对于生产环境使用空间优化版本
 * 2. 添加异常处理确保程序健壮性
 * 3. 编写单元测试覆盖各种边界情况
 * 4. 使用性能分析工具优化关键路径
 */
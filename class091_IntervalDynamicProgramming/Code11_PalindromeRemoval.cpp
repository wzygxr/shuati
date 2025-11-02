#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>

using namespace std;

/**
 * LeetCode 1246. 删除回文子数组
 * 题目链接：https://leetcode.cn/problems/palindrome-removal/
 * 难度：困难
 * 
 * 题目描述：
 * 给你一个整数数组 arr，每一次操作你都可以选择并删除它的一个回文子数组。
 * 注意，每当你删除掉一个子数组后，右侧元素会自动左侧移动以填补空缺。
 * 请你计算并返回从数组中删除所有数字所需的最少操作次数。
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，需要处理回文子数组的删除。
 * 状态定义：dp[i][j]表示删除区间[i,j]所需的最少操作次数
 * 状态转移：
 * 1. 如果arr[i] == arr[j]，可以一起删除
 * 2. 枚举分割点k，将区间分成两部分分别删除
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 工程化考量：
 * 1. 边界条件处理：单个元素是回文，操作次数为1
 * 2. 优化：当arr[i] == arr[j]时，可以利用dp[i+1][j-1]的结果
 * 3. 特殊情况：整个数组是回文时，只需要1次操作
 * 
 * C++实现注意事项：
 * 1. 使用vector代替原生数组
 * 2. 注意整数溢出，使用INT_MAX
 * 3. 使用动态规划解决区间问题
 * 
 * 相关题目扩展：
 * 1. LeetCode 1246. 删除回文子数组 - https://leetcode.cn/problems/palindrome-removal/
 * 2. LeetCode 1312. 让字符串成为回文串的最少插入次数 - https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 * 3. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 4. LeetCode 1216. 验证回文字符串 III - https://leetcode.cn/problems/valid-palindrome-iii/
 * 5. LeetCode 1682. 最长回文子序列 II - https://leetcode.cn/problems/longest-palindromic-subsequence-ii/
 * 6. LintCode 1419. 最少行程 - https://www.lintcode.com/problem/1419/
 * 7. LintCode 1797. 模糊坐标 - https://www.lintcode.com/problem/1797/
 * 8. HackerRank - Palindrome Index - https://www.hackerrank.com/challenges/palindrome-index/problem
 * 9. Codeforces 1373C - Pluses and Minuses - https://codeforces.com/problemset/problem/1373/C
 * 10. AtCoder ABC161D - Lunlun Number - https://atcoder.jp/contests/abc161/tasks/abc161_d
 */

class Solution {
public:
    int minimumMoves(vector<int>& arr) {
        int n = arr.size();
        if (n == 0) return 0;
        
        // dp[i][j]表示删除区间[i,j]所需的最少操作次数
        vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
        
        // 初始化：单个元素需要1次操作
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 两个元素的情况
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] == arr[i + 1]) {
                dp[i][i + 1] = 1; // 两个相同元素可以一次删除
            } else {
                dp[i][i + 1] = 2; // 两个不同元素需要两次删除
            }
        }
        
        // 区间动态规划
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 情况1：如果首尾元素相同，可以一起删除
                if (arr[i] == arr[j]) {
                    dp[i][j] = dp[i + 1][j - 1];
                }
                
                // 情况2：枚举分割点
                for (int k = i; k < j; k++) {
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    int minimumMovesOptimized(vector<int>& arr) {
        int n = arr.size();
        if (n == 0) return 0;
        
        vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 默认情况：单独删除第一个元素
                dp[i][j] = dp[i + 1][j] + 1;
                
                // 如果arr[i] == arr[k]，可以考虑一起删除
                for (int k = i + 1; k <= j; k++) {
                    if (arr[i] == arr[k]) {
                        int left = (i + 1 <= k - 1) ? dp[i + 1][k - 1] : 0;
                        int right = (k + 1 <= j) ? dp[k + 1][j] : 0;
                        int cost = left + right;
                        if (left == 0 && right == 0) cost = 1;
                        dp[i][j] = min(dp[i][j], cost);
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }
};

void testPalindromeRemoval() {
    Solution solution;
    
    // 测试用例1
    vector<int> arr1 = {1, 2};
    cout << "测试用例1: arr = [1,2]" << endl;
    cout << "预期结果: 2" << endl;
    cout << "实际结果: " << solution.minimumMoves(arr1) << endl;
    cout << "优化版本: " << solution.minimumMovesOptimized(arr1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> arr2 = {1, 3, 4, 1, 5};
    cout << "测试用例2: arr = [1,3,4,1,5]" << endl;
    cout << "预期结果: 3" << endl;
    cout << "实际结果: " << solution.minimumMoves(arr2) << endl;
    cout << "优化版本: " << solution.minimumMovesOptimized(arr2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<int> arr3 = {1, 2, 3, 4, 5};
    cout << "测试用例3: arr = [1,2,3,4,5]" << endl;
    cout << "预期结果: 5" << endl;
    cout << "实际结果: " << solution.minimumMoves(arr3) << endl;
    cout << "优化版本: " << solution.minimumMovesOptimized(arr3) << endl;
}

int main() {
    testPalindromeRemoval();
    return 0;
}
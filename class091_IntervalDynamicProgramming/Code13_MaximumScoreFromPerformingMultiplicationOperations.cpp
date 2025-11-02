#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>

using namespace std;

/**
 * LeetCode 1770. 执行乘法运算的最大分数
 * 题目链接：https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
 * 难度：中等
 * 
 * 题目描述：
 * 给你两个长度分别为 n 和 m 的整数数组 nums 和 multipliers。
 * 你需要执行恰好 m 步操作。在第 i 步操作（从 1 开始计数）中，你需要：
 * - 选择数组 nums 开头或者结尾的一个元素 x
 * - 获得 multipliers[i] * x 的分数，并将 x 从数组 nums 中移除
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，需要处理从数组两端取元素的情况。
 * 状态定义：dp[i][j]表示已经取了i个开头元素和j个结尾元素时的最大分数
 * 状态转移：每次可以选择取开头或结尾的元素
 * 
 * 时间复杂度：O(m^2)
 * 空间复杂度：O(m^2)
 * 
 * 工程化考量：
 * 1. 空间优化：使用滚动数组将空间复杂度优化到O(m)
 * 2. 边界条件处理：m可能为0的情况
 * 3. 优化：只考虑必要的状态
 * 
 * 相关题目扩展：
 * 1. LeetCode 1770. 执行乘法运算的最大分数 - https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
 * 2. LeetCode 486. 预测赢家 - https://leetcode.cn/problems/predict-the-winner/
 * 3. LeetCode 877. 石子游戏 - https://leetcode.cn/problems/stone-game/
 * 4. LeetCode 1140. 石子游戏 II - https://leetcode.cn/problems/stone-game-ii/
 * 5. LeetCode 1406. 石子游戏 III - https://leetcode.cn/problems/stone-game-iii/
 * 6. LintCode 390. 石子游戏 - https://www.lintcode.com/problem/390/
 * 7. LintCode 1718. 石子游戏 VI - https://www.lintcode.com/problem/1718/
 * 8. HackerRank - Game of Stones - https://www.hackerrank.com/challenges/game-of-stones-1/problem
 * 9. Codeforces 1312C - Add One - https://codeforces.com/problemset/problem/1312/C
 * 10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 */

class Solution {
public:
    int maximumScore(vector<int>& nums, vector<int>& multipliers) {
        int n = nums.size();
        int m = multipliers.size();
        
        vector<vector<int>> dp(m + 1, vector<int>(m + 1, INT_MIN));
        dp[0][0] = 0;
        
        for (int total = 1; total <= m; total++) {
            for (int left = 0; left <= total; left++) {
                int right = total - left;
                
                if (left > 0) {
                    int score1 = dp[left - 1][right] + multipliers[total - 1] * nums[left - 1];
                    dp[left][right] = max(dp[left][right], score1);
                }
                
                if (right > 0) {
                    int score2 = dp[left][right - 1] + multipliers[total - 1] * nums[n - right];
                    dp[left][right] = max(dp[left][right], score2);
                }
            }
        }
        
        int maxScore = INT_MIN;
        for (int left = 0; left <= m; left++) {
            int right = m - left;
            if (dp[left][right] > maxScore) {
                maxScore = dp[left][right];
            }
        }
        
        return maxScore;
    }
    
    int maximumScoreOptimized(vector<int>& nums, vector<int>& multipliers) {
        int n = nums.size();
        int m = multipliers.size();
        
        vector<int> dp(m + 1, INT_MIN);
        dp[0] = 0;
        
        for (int op = 0; op < m; op++) {
            vector<int> nextDp(m + 1, INT_MIN);
            
            for (int left = 0; left <= op + 1; left++) {
                int right = op + 1 - left;
                
                if (left > 0) {
                    int score1 = dp[left - 1] + multipliers[op] * nums[left - 1];
                    nextDp[left] = max(nextDp[left], score1);
                }
                
                if (right > 0) {
                    int score2 = dp[left] + multipliers[op] * nums[n - right];
                    nextDp[left] = max(nextDp[left], score2);
                }
            }
            
            dp = nextDp;
        }
        
        int maxScore = INT_MIN;
        for (int score : dp) {
            if (score > maxScore) {
                maxScore = score;
            }
        }
        
        return maxScore;
    }
};

void testMaximumScore() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    vector<int> multipliers1 = {3, 2, 1};
    cout << "测试用例1: nums = [1,2,3], multipliers = [3,2,1]" << endl;
    cout << "预期结果: 14" << endl;
    cout << "DP解法: " << solution.maximumScore(nums1, multipliers1) << endl;
    cout << "优化版本: " << solution.maximumScoreOptimized(nums1, multipliers1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {-5, -3, -3, -2, 7, 1};
    vector<int> multipliers2 = {-10, -5, 3, 4, 6};
    cout << "测试用例2: 复杂数组" << endl;
    cout << "DP解法: " << solution.maximumScore(nums2, multipliers2) << endl;
    cout << "优化版本: " << solution.maximumScoreOptimized(nums2, multipliers2) << endl;
}

int main() {
    testMaximumScore();
    return 0;
}
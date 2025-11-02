#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * AcWing 282. 石子合并
 * 题目链接：https://www.acwing.com/problem/content/284/
 * 难度：简单
 * 
 * 题目描述：
 * 设有N堆石子排成一排，其编号为1，2，3，…，N。
 * 每堆石子有一定的质量，可以用一个整数来描述，现在要将这N堆石子合并成为一堆。
 * 每次只能合并相邻的两堆，合并的代价为这两堆石子的质量之和，合并后与这两堆石子相邻的石子将和新堆相邻。
 * 找出一种合理的方法，使总的代价最小，输出最小代价。
 * 
 * 解题思路：
 * 经典的区间动态规划问题，石子合并问题。
 * 状态定义：dp[i][j]表示合并区间[i,j]的石子所需的最小代价
 * 状态转移：dp[i][j] = min(dp[i][k] + dp[k+1][j] + sum[i][j])
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 工程化考量：
 * 1. 使用前缀和优化区间和计算
 * 2. 四边形不等式优化可以将时间复杂度优化到O(n^2)
 * 3. 处理边界条件：单个石子代价为0
 * 
 * 相关题目扩展：
 * 1. AcWing 282. 石子合并 - https://www.acwing.com/problem/content/284/
 * 2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 3. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 * 4. LintCode 1000. 合并石头的最低成本 - https://www.lintcode.com/problem/1000/
 * 5. LintCode 476. Stone Game - https://www.lintcode.com/problem/476/
 * 6. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 7. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 8. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 9. POJ 1141 Brackets Sequence - http://poj.org/problem?id=1141
 * 10. HDU 4632 - Palindrome Subsequence - http://acm.hdu.edu.cn/showproblem.php?pid=4632
 */

class Solution {
public:
    int minCost(vector<int>& stones) {
        int n = stones.size();
        if (n == 1) return 0;
        
        vector<int> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
        
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                              (prefixSum[j + 1] - prefixSum[i]);
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    int minCostOptimized(vector<int>& stones) {
        int n = stones.size();
        if (n == 1) return 0;
        
        vector<int> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
        vector<vector<int>> best(n, vector<int>(n, 0));
        
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
            best[i][i] = i;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                int left = best[i][j - 1];
                int right = (j - 1 < i + 1) ? i : best[i + 1][j];
                
                for (int k = left; k <= right; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                              (prefixSum[j + 1] - prefixSum[i]);
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        best[i][j] = k;
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }
};

void testStonesMerge() {
    Solution solution;
    
    // 测试用例1
    vector<int> stones1 = {1, 3, 5, 2};
    cout << "测试用例1: stones = [1,3,5,2]" << endl;
    cout << "预期结果: 22" << endl;
    cout << "DP解法: " << solution.minCost(stones1) << endl;
    cout << "优化版本: " << solution.minCostOptimized(stones1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> stones2 = {4, 2, 1, 3};
    cout << "测试用例2: stones = [4,2,1,3]" << endl;
    cout << "预期结果: 20" << endl;
    cout << "DP解法: " << solution.minCost(stones2) << endl;
    cout << "优化版本: " << solution.minCostOptimized(stones2) << endl;
}

int main() {
    testStonesMerge();
    return 0;
}
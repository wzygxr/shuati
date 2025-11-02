#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>

using namespace std;

/**
 * LeetCode 1000. 合并石头的最低成本
 * 题目链接：https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 难度：困难
 * 
 * 题目描述：
 * 有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。
 * 每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
 * 找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1。
 * 
 * 解题思路：
 * 这是一个经典的区间动态规划问题，需要处理K堆合并的特殊情况。
 * 状态定义：dp[i][j]表示将区间[i,j]的石头合并成若干堆的最小成本
 * 状态转移：枚举分割点k，将区间分成两部分进行合并
 * 
 * 时间复杂度：O(n^3 * K)
 * 空间复杂度：O(n^2)
 * 
 * 工程化考量：
 * 1. 边界条件处理：当K=1时直接返回0，当(n-1)%(K-1)!=0时返回-1
 * 2. 前缀和优化：使用前缀和数组快速计算区间和
 * 3. 状态压缩：可以优化空间复杂度到O(n)
 * 
 * C++实现注意事项：
 * 1. 使用vector代替原生数组，更安全
 * 2. 注意整数溢出问题，使用INT_MAX/2防止溢出
 * 3. 使用前缀和优化区间和计算
 * 4. 三维DP数组使用vector<vector<vector<int>>>
 * 
 * 相关题目扩展：
 * 1. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 2. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 3. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 4. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 5. LintCode 1000. 合并石头的最低成本 - https://www.lintcode.com/problem/1000/
 * 6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 7. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 */

class Solution {
public:
    /**
     * 合并石头的最低成本 - 区间动态规划解法
     * 时间复杂度：O(n^3 * K)
     * 空间复杂度：O(n^2)
     */
    int mergeStones(vector<int>& stones, int K) {
        int n = stones.size();
        
        // 特殊情况处理
        if (n == 1) return 0;
        if (K < 2 || (n - 1) % (K - 1) != 0) return -1;
        
        // 前缀和数组
        vector<int> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // DP数组初始化
        vector<vector<int>> dp(n, vector<int>(n, INT_MAX / 2));
        
        // 单个堆的成本为0
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
        }
        
        // 区间动态规划
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 枚举分割点，步长为K-1
                for (int k = i; k < j; k += K - 1) {
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j]);
                }
                
                // 如果可以合并成一堆，加上合并成本
                if ((len - 1) % (K - 1) == 0) {
                    dp[i][j] += prefixSum[j + 1] - prefixSum[i];
                }
            }
        }
        
        return dp[0][n - 1] < INT_MAX / 2 ? dp[0][n - 1] : -1;
    }
    
    /**
     * 优化版本：三维DP
     * 时间复杂度：O(n^3 * K^2)
     * 空间复杂度：O(n^2 * K)
     */
    int mergeStonesOptimized(vector<int>& stones, int K) {
        int n = stones.size();
        if ((n - 1) % (K - 1) != 0) return -1;
        
        vector<int> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // 三维DP数组：dp[i][j][m]表示区间[i,j]合并成m堆的最小成本
        vector<vector<vector<int>>> dp(
            n, vector<vector<int>>(
                n, vector<int>(K + 1, INT_MAX / 2)
            )
        );
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[i][i][1] = 0;  // 单个堆合并成1堆成本为0
        }
        
        // 区间动态规划
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 初始化各种堆数的成本
                for (int m = 2; m <= K; m++) {
                    dp[i][j][m] = INT_MAX / 2;
                }
                
                // 枚举分割点
                for (int k = i; k < j; k++) {
                    for (int m1 = 1; m1 <= K; m1++) {
                        for (int m2 = 1; m2 <= K; m2++) {
                            if (m1 + m2 <= K) {
                                if (dp[i][k][m1] < INT_MAX / 2 && 
                                    dp[k + 1][j][m2] < INT_MAX / 2) {
                                    dp[i][j][m1 + m2] = min(
                                        dp[i][j][m1 + m2],
                                        dp[i][k][m1] + dp[k + 1][j][m2]
                                    );
                                }
                            }
                        }
                    }
                }
                
                // 如果可以合并成1堆
                if (dp[i][j][K] < INT_MAX / 2) {
                    dp[i][j][1] = min(
                        dp[i][j][1],
                        dp[i][j][K] + prefixSum[j + 1] - prefixSum[i]
                    );
                }
            }
        }
        
        return dp[0][n - 1][1] < INT_MAX / 2 ? dp[0][n - 1][1] : -1;
    }
};

/**
 * 测试函数
 */
void testMergeStones() {
    Solution solution;
    
    // 测试用例1
    vector<int> stones1 = {3, 2, 4, 1};
    int K1 = 2;
    cout << "测试用例1: stones = [3,2,4,1], K = 2" << endl;
    cout << "预期结果: 20" << endl;
    cout << "实际结果: " << solution.mergeStones(stones1, K1) << endl;
    cout << "优化版本: " << solution.mergeStonesOptimized(stones1, K1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> stones2 = {3, 2, 4, 1};
    int K2 = 3;
    cout << "测试用例2: stones = [3,2,4,1], K = 3" << endl;
    cout << "预期结果: -1" << endl;
    cout << "实际结果: " << solution.mergeStones(stones2, K2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<int> stones3 = {3, 5, 1, 2, 6};
    int K3 = 3;
    cout << "测试用例3: stones = [3,5,1,2,6], K = 3" << endl;
    cout << "预期结果: 25" << endl;
    cout << "实际结果: " << solution.mergeStones(stones3, K3) << endl;
    cout << "优化版本: " << solution.mergeStonesOptimized(stones3, K3) << endl;
}

int main() {
    testMergeStones();
    return 0;
}
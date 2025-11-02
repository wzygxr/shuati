#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

// 石子合并
// 在一条直线上有n堆石子，每堆有一个重量。现在要合并这些石子成为一堆，
// 每次只能合并相邻的两堆石子，合并的代价为这两堆石子的重量之和。
// 求合并所有石子的最小代价。
// 测试链接 : https://www.luogu.com.cn/problem/P1880

class Solution {
public:
    // 区间动态规划解法
    // 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    // 空间复杂度: O(n^2) - dp数组占用空间
    // 解题思路:
    // 1. 状态定义：minDp[i][j]表示合并区间[i,j]石子的最小代价，maxDp[i][j]表示最大代价
    // 2. 状态转移：枚举分割点k，minDp[i][j] = min(minDp[i][k] + minDp[k+1][j]) + sum[i][j]
    // 3. 前缀和优化：使用前缀和快速计算区间和
    pair<int, int> stoneMerge(vector<int>& stones) {
        int n = stones.size();
        
        // 计算前缀和
        vector<int> preSum(n + 1, 0);
        for (int i = 1; i <= n; i++) {
            preSum[i] = preSum[i - 1] + stones[i - 1];
        }
        
        // minDp[i][j]表示合并区间[i,j]石子的最小代价
        vector<vector<int>> minDp(n + 1, vector<int>(n + 1, 0));
        // maxDp[i][j]表示合并区间[i,j]石子的最大代价
        vector<vector<int>> maxDp(n + 1, vector<int>(n + 1, 0));
        
        // 枚举区间长度，从2开始（至少要有2堆石子才能合并）
        for (int len = 2; len <= n; len++) {
            // 枚举区间起点i
            for (int i = 1; i <= n - len + 1; i++) {
                // 计算区间终点j
                int j = i + len - 1;
                minDp[i][j] = INT_MAX;
                maxDp[i][j] = INT_MIN;
                
                // 枚举分割点k
                for (int k = i; k < j; k++) {
                    // 计算区间[i,j]的石子重量和
                    int sum = preSum[j] - preSum[i - 1];
                    
                    // 更新最小代价
                    minDp[i][j] = min(minDp[i][j], 
                        minDp[i][k] + minDp[k + 1][j] + sum);
                    
                    // 更新最大代价
                    maxDp[i][j] = max(maxDp[i][j], 
                        maxDp[i][k] + maxDp[k + 1][j] + sum);
                }
            }
        }
        
        return {minDp[1][n], maxDp[1][n]};
    }
};

int main() {
    // 读取输入
    int n;
    cin >> n;
    vector<int> stones(n);
    for (int i = 0; i < n; i++) {
        cin >> stones[i];
    }
    
    // 计算结果
    Solution solution;
    pair<int, int> result = solution.stoneMerge(stones);
    
    // 输出结果
    cout << result.first << endl;  // 最小代价
    cout << result.second << endl; // 最大代价
    
    return 0;
}
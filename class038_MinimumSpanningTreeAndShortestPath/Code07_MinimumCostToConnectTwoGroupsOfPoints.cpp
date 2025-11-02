// LeetCode 1595. Minimum Cost to Connect Two Groups of Points
// 题目链接: https://leetcode.cn/problems/minimum-cost-to-connect-two-groups-of-points/
// 
// 题目描述:
// 给你两组点，其中第一组中有size1个点，第二组中有size2个点，且size1 <= size2。
// 任意两点间的连接费用定义为这两点坐标的曼哈顿距离。
// 我们需要把所有第一组的点与第二组的点连接起来，使得：
// 1. 每个第一组的点必须至少连接到一个第二组的点
// 2. 每个第二组的点可以连接到任意数量的第一组的点
// 3. 总连接费用最小
//
// 解题思路:
// 这是一个最小生成树的变种问题，但更适合用状态压缩动态规划来解决。
// 我们可以将问题转化为：选择一些边，使得所有第一组的点都被覆盖，同时尽可能覆盖第二组的点，
// 最后可能需要添加一些边来连接未被覆盖的第二组的点。
//
// 时间复杂度: O(size1 * 2^size2 * size2)，其中size1是第一组的点数量，size2是第二组的点数量
// 空间复杂度: O(size1 * 2^size2)
// 是否为最优解: 对于给定的约束条件，这是一个有效的解法，但对于较大的size2可能需要其他优化方法

#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
using namespace std;

int minCost(vector<vector<int>>& connectCost) {
    int size1 = connectCost.size();
    if (size1 == 0) return 0;
    int size2 = connectCost[0].size();
    
    // 预处理第二组每个点连接到第一组点的最小费用
    vector<int> minCostGroup2(size2, INT_MAX);
    for (int j = 0; j < size2; j++) {
        for (int i = 0; i < size1; i++) {
            minCostGroup2[j] = min(minCostGroup2[j], connectCost[i][j]);
        }
    }
    
    // dp[i][mask] 表示处理了第一组的前i个点，且第二组中已连接的点集合为mask时的最小费用
    int maxMask = 1 << size2;
    vector<vector<int>> dp(size1 + 1, vector<int>(maxMask, INT_MAX));
    dp[0][0] = 0;  // 初始状态
    
    for (int i = 0; i < size1; i++) {
        for (int mask = 0; mask < maxMask; mask++) {
            if (dp[i][mask] == INT_MAX) continue;
            
            // 尝试将第一组的第i个点连接到第二组的每个点j
            for (int j = 0; j < size2; j++) {
                int newMask = mask | (1 << j);
                if (dp[i][mask] != INT_MAX && dp[i + 1][newMask] > dp[i][mask] + connectCost[i][j]) {
                    dp[i + 1][newMask] = dp[i][mask] + connectCost[i][j];
                }
            }
        }
    }
    
    // 计算最终结果：确保所有第二组的点都被连接
    int result = INT_MAX;
    for (int mask = 0; mask < maxMask; mask++) {
        if (dp[size1][mask] == INT_MAX) continue;
        
        int additionalCost = 0;
        for (int j = 0; j < size2; j++) {
            if (!(mask & (1 << j))) {
                additionalCost += minCostGroup2[j];
            }
        }
        
        if (result > dp[size1][mask] + additionalCost) {
            result = dp[size1][mask] + additionalCost;
        }
    }
    
    return result;
}

// 测试函数
void test() {
    // 测试用例1
    vector<vector<int>> connectCost1 = {{15, 96}, {36, 2}};
    cout << "Test 1: " << minCost(connectCost1) << endl;  // 预期输出: 17
    
    // 测试用例2
    vector<vector<int>> connectCost2 = {{1, 3, 5}, {4, 1, 1}, {1, 5, 3}};
    cout << "Test 2: " << minCost(connectCost2) << endl;  // 预期输出: 4
    
    // 测试用例3
    vector<vector<int>> connectCost3 = {{2, 5, 1}, {3, 4, 7}, {8, 1, 2}, {6, 2, 4}, {3, 8, 8}};
    cout << "Test 3: " << minCost(connectCost3) << endl;  // 预期输出: 10
}

int main() {
    test();
    return 0;
}
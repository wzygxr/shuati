#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

// 最低加油次数
// 汽车从起点出发驶向目的地，该目的地位于起点正东 target 英里处。
// 沿途有加油站，每个 station[i] 代表一个加油站，它位于起点正东 station[i][0] 英里处，并且有 station[i][1] 升汽油。
// 假设汽车油箱的容量是无限的，其中最初有 startFuel 升燃料。它每行驶 1 英里就会用掉 1 升汽油。
// 当汽车到达加油站时，它可能停下来加油，将所有汽油从加油站转移到汽车中。
// 为了到达目的地，汽车所必要的最低加油次数是多少？如果无法到达目的地，则返回 -1。
// 测试链接: https://leetcode.cn/problems/minimum-number-of-refueling-stops/

class Solution {
public:
    /**
     * 最低加油次数问题的贪心解法
     * 
     * 解题思路：
     * 1. 使用贪心策略，在能够到达的范围内，选择油量最多的加油站加油
     * 2. 使用最大堆来存储经过的加油站的油量
     * 3. 当油量不足以到达下一个加油站时，从堆中取出最大的油量进行加油
     * 
     * 贪心策略的正确性：
     * 局部最优：每次加油都选择油量最多的加油站
     * 全局最优：使用最少的加油次数到达目的地
     * 
     * 时间复杂度：O(n log n)，其中n是加油站数量
     * 空间复杂度：O(n)，用于存储加油站油量的堆
     * 
     * @param target 目的地距离
     * @param startFuel 初始油量
     * @param stations 加油站数组
     * @return 最低加油次数，无法到达返回-1
     */
    int minRefuelStops(int target, int startFuel, vector<vector<int>>& stations) {
        // 边界条件处理
        if (startFuel >= target) return 0; // 初始油量足够到达目的地
        
        // 使用最大堆存储加油站的油量
        priority_queue<int> maxHeap;
        
        int currentFuel = startFuel; // 当前油量
        int refuels = 0;             // 加油次数
        int i = 0;                   // 加油站索引
        int n = stations.size();     // 加油站数量
        
        // 当当前油量不足以到达目的地时继续循环
        while (currentFuel < target) {
            // 将能够到达的加油站的油量加入堆中
            while (i < n && stations[i][0] <= currentFuel) {
                maxHeap.push(stations[i][1]);
                i++;
            }
            
            // 如果没有可用的加油站且油量不足以到达目的地，返回-1
            if (maxHeap.empty()) {
                return -1;
            }
            
            // 选择油量最多的加油站加油
            currentFuel += maxHeap.top();
            maxHeap.pop();
            refuels++;
        }
        
        return refuels;
    }

    /**
     * 最低加油次数问题的动态规划解法
     * 
     * 解题思路：
     * 1. 使用动态规划，dp[i]表示加油i次能够到达的最远距离
     * 2. 对于每个加油站，更新dp数组
     * 3. 找到第一个能够到达目的地的加油次数
     * 
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n)
     */
    int minRefuelStopsDP(int target, int startFuel, vector<vector<int>>& stations) {
        int n = stations.size();
        // dp[i]表示加油i次能够到达的最远距离
        vector<long> dp(n + 1, 0);
        dp[0] = startFuel; // 不加油，初始油量能够到达的距离
        
        // 遍历每个加油站
        for (int i = 0; i < n; i++) {
            // 从后往前更新，避免重复计算
            for (int j = i; j >= 0; j--) {
                // 如果当前加油次数能够到达这个加油站
                if (dp[j] >= stations[i][0]) {
                    // 更新加油j+1次能够到达的距离
                    dp[j + 1] = max(dp[j + 1], dp[j] + stations[i][1]);
                }
            }
        }
        
        // 找到第一个能够到达目的地的加油次数
        for (int i = 0; i <= n; i++) {
            if (dp[i] >= target) {
                return i;
            }
        }
        
        return -1;
    }
};

// 测试函数
void testMinRefuelStops() {
    Solution solution;
    
    // 测试用例1
    // 输入: target = 1, startFuel = 1, stations = []
    // 输出: 0
    // 解释: 初始油量足够到达目的地，不需要加油
    int target1 = 1;
    int startFuel1 = 1;
    vector<vector<int>> stations1 = {};
    cout << "测试用例1结果: " << solution.minRefuelStops(target1, startFuel1, stations1) << endl; // 期望输出: 0
    
    // 测试用例2
    // 输入: target = 100, startFuel = 1, stations = [[10,100]]
    // 输出: -1
    // 解释: 初始油量不足以到达第一个加油站
    int target2 = 100;
    int startFuel2 = 1;
    vector<vector<int>> stations2 = {{10, 100}};
    cout << "测试用例2结果: " << solution.minRefuelStops(target2, startFuel2, stations2) << endl; // 期望输出: -1
    
    // 测试用例3
    // 输入: target = 100, startFuel = 10, stations = [[10,60],[20,30],[30,30],[60,40]]
    // 输出: 2
    // 解释: 行驶10英里到达第一个加油站，加油60升；行驶到20英里处，加油30升；行驶到60英里处，加油40升；到达100英里
    int target3 = 100;
    int startFuel3 = 10;
    vector<vector<int>> stations3 = {{10, 60}, {20, 30}, {30, 30}, {60, 40}};
    cout << "测试用例3结果: " << solution.minRefuelStops(target3, startFuel3, stations3) << endl; // 期望输出: 2
    
    // 测试用例4：边界情况
    // 输入: target = 100, startFuel = 50, stations = [[25,25],[50,50]]
    // 输出: 1
    int target4 = 100;
    int startFuel4 = 50;
    vector<vector<int>> stations4 = {{25, 25}, {50, 50}};
    cout << "测试用例4结果: " << solution.minRefuelStops(target4, startFuel4, stations4) << endl; // 期望输出: 1
    
    // 测试用例5：复杂情况
    // 输入: target = 1000, startFuel = 299, stations = [[13,21],[26,115],[100,47],[225,99],[299,141],[444,198],[608,190],[636,157],[647,255],[841,123]]
    // 输出: 4
    int target5 = 1000;
    int startFuel5 = 299;
    vector<vector<int>> stations5 = {{13,21},{26,115},{100,47},{225,99},{299,141},{444,198},{608,190},{636,157},{647,255},{841,123}};
    cout << "测试用例5结果: " << solution.minRefuelStops(target5, startFuel5, stations5) << endl; // 期望输出: 4
}

int main() {
    testMinRefuelStops();
    return 0;
}
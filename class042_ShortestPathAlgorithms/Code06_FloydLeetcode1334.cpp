#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
using namespace std;

// LeetCode 1334. 阈值距离内邻居最少的城市
// 题目链接: https://leetcode.cn/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
// 题目描述: 有 n 个城市，按从 0 到 n-1 编号。给你一个边数组 edges，其中 edges[i] = [fromi, toi, weighti] 
// 代表 fromi 和 toi 两个城市之间的双向加权边，距离阈值是一个整数 distanceThreshold。
// 返回在路径距离限制为 distanceThreshold 以内可到达城市最少的城市。如果有多个这样的城市，则返回编号最大的城市。
//
// 解题思路:
// 这道题可以使用Floyd算法来解决。我们需要计算任意两个城市之间的最短距离，
// 然后统计每个城市在距离阈值内能到达的城市数量，最后返回数量最少且编号最大的城市。
//
// 时间复杂度: O(N^3)，其中N是城市数量
// 空间复杂度: O(N^2)

class Solution {
public:
    int findTheCity(int n, vector<vector<int>>& edges, int distanceThreshold) {
        // 初始化距离矩阵
        vector<vector<int>> distance(n, vector<int>(n, INT_MAX));
        for (int i = 0; i < n; i++) {
            distance[i][i] = 0;
        }
        
        // 根据边的信息初始化距离矩阵
        for (const auto& edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];
            distance[from][to] = weight;
            distance[to][from] = weight;  // 因为是无向图
        }
        
        // Floyd算法求所有点对之间的最短距离
        floyd(n, distance);
        
        // 统计每个城市在距离阈值内能到达的城市数量
        int minCount = n;  // 最少城市数量
        int result = -1;   // 结果城市编号
        
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && distance[i][j] <= distanceThreshold) {
                    count++;
                }
            }
            
            // 更新结果：城市数量更少，或者城市数量相同但编号更大
            if (count < minCount || (count == minCount && i > result)) {
                minCount = count;
                result = i;
            }
        }
        
        return result;
    }
    
    // Floyd算法核心实现
    void floyd(int n, vector<vector<int>>& distance) {
        // 三层循环：中间节点k，起点i，终点j
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // 注意处理INT_MAX的情况，避免溢出
                    if (distance[i][k] != INT_MAX && 
                        distance[k][j] != INT_MAX && 
                        distance[i][k] + distance[k][j] < distance[i][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> edges1 = {{0,1,3},{1,2,1},{1,3,4},{2,3,1}};
    int n1 = 4;
    int distanceThreshold1 = 4;
    cout << "测试用例1结果: " << solution.findTheCity(n1, edges1, distanceThreshold1) << endl; // 期望输出: 3
    
    // 测试用例2
    vector<vector<int>> edges2 = {{0,1,2},{0,4,8},{1,2,3},{1,4,2},{2,3,1},{3,4,1}};
    int n2 = 5;
    int distanceThreshold2 = 2;
    cout << "测试用例2结果: " << solution.findTheCity(n2, edges2, distanceThreshold2) << endl; // 期望输出: 0
    
    return 0;
}
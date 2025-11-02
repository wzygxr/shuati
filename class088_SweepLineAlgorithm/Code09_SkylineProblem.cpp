#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <map>
using namespace std;

/**
 * 天际线问题 (LeetCode 218)
 * 题目链接: https://leetcode.cn/problems/the-skyline-problem/
 * 
 * 解题思路:
 * 使用扫描线算法，将建筑物的左右边界作为事件点
 * 维护当前扫描线位置的所有建筑物高度，使用最大堆
 * 当高度发生变化时，记录关键点
 * 
 * 时间复杂度: O(n log n) - 排序和堆操作
 * 空间复杂度: O(n) - 存储事件点和堆
 */

class SkylineProblem {
public:
    vector<vector<int>> getSkyline(vector<vector<int>>& buildings) {
        if (buildings.empty()) return {};
        
        // 创建事件点: (x坐标, 高度, 类型: 1表示进入，-1表示离开)
        vector<vector<int>> events;
        for (auto& building : buildings) {
            int left = building[0], right = building[1], height = building[2];
            events.push_back({left, height, 1});   // 进入事件
            events.push_back({right, height, -1}); // 离开事件
        }
        
        // 按x坐标排序，x相同时进入事件优先
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            if (a[0] != b[0]) return a[0] < b[0];
            if (a[2] != b[2]) return a[2] > b[2];
            if (a[2] == 1) return a[1] > b[1]; // 进入事件按高度降序
            return a[1] < b[1]; // 离开事件按高度升序
        });
        
        // 使用multiset作为最大堆（自动排序）
        multiset<int> heights;
        heights.insert(0); // 地面高度
        
        vector<vector<int>> result;
        int prevHeight = 0;
        
        for (auto& event : events) {
            int x = event[0], height = event[1], type = event[2];
            
            if (type == 1) {
                // 进入事件：添加高度
                heights.insert(height);
            } else {
                // 离开事件：移除高度
                heights.erase(heights.find(height));
            }
            
            // 获取当前最大高度
            int currHeight = *heights.rbegin();
            
            // 如果高度发生变化，记录关键点
            if (currHeight != prevHeight) {
                result.push_back({x, currHeight});
                prevHeight = currHeight;
            }
        }
        
        return result;
    }
};

int main() {
    SkylineProblem solver;
    
    // 测试用例1: 简单建筑物
    vector<vector<int>> buildings1 = {{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}};
    auto result1 = solver.getSkyline(buildings1);
    cout << "测试用例1结果:" << endl;
    for (auto& point : result1) {
        cout << "[" << point[0] << ", " << point[1] << "] " << endl;
    }
    
    // 测试用例2: 单个建筑物
    vector<vector<int>> buildings2 = {{0, 2, 3}, {2, 5, 3}};
    auto result2 = solver.getSkyline(buildings2);
    cout << "测试用例2结果:" << endl;
    for (auto& point : result2) {
        cout << "[" << point[0] << ", " << point[1] << "] " << endl;
    }
    
    return 0;
}
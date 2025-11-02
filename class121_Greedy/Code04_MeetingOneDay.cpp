/**
 * 会议只占一天的最大会议数量 - 贪心算法 + 优先队列
 * 
 * 题目描述：
 * 给定若干会议的开始、结束时间，任何会议的召开期间，你只需要抽出1天来参加。
 * 但是你安排的那一天，只能参加这个会议，不能同时参加其他会议。
 * 返回你能参加的最大会议数量。
 * 
 * 解题思路：
 * 1. 按会议开始时间排序
 * 2. 使用优先队列（小根堆）维护当前可选择的会议（按结束时间排序）
 * 3. 按时间顺序遍历每一天：
 *    - 将当天开始的会议加入优先队列
 *    - 移除已过期的会议（结束时间早于当前日期）
 *    - 选择结束时间最早的会议参加
 * 
 * 算法原理：
 * - 贪心策略：在每一天，选择结束时间最早的可参加会议
 * - 优先队列：维护可参加会议的结束时间，快速获取最早结束的会议
 * 
 * 时间复杂度：O(n*logn + d*logn) - n为会议数，d为时间范围
 * 空间复杂度：O(n) - 优先队列的空间
 * 
 * 相关题目：
 * - LeetCode 1353: https://leetcode.cn/problems/maximum-number-of-events-that-can-be-attended/
 * - 会议调度问题的变种
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
using namespace std;

/**
 * 计算最大可参加会议数
 * 
 * @param events 会议数组，events[i][0]为开始时间，events[i][1]为结束时间
 * @return 最大可参加会议数
 */
int maxEvents(vector<vector<int>>& events) {
    int n = events.size();
    
    // 按会议开始时间升序排序
    sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[0] < b[0];
    });
    
    // 计算时间范围
    int minDay = events[0][0];
    int maxDay = events[0][1];
    for (int i = 1; i < n; i++) {
        maxDay = max(maxDay, events[i][1]);
    }
    
    // 小根堆: 会议的结束时间
    priority_queue<int, vector<int>, greater<int>> heap;
    int i = 0, ans = 0;
    
    // 按时间顺序遍历每一天
    for (int day = minDay; day <= maxDay; day++) {
        // 将当天开始的会议加入优先队列
        while (i < n && events[i][0] == day) {
            heap.push(events[i][1]);
            i++;
        }
        
        // 移除已过期的会议（结束时间早于当前日期）
        while (!heap.empty() && heap.top() < day) {
            heap.pop();
        }
        
        // 选择结束时间最早的会议参加
        if (!heap.empty()) {
            heap.pop();
            ans++;
        }
    }
    
    return ans;
}

// 测试代码
int main() {
    // 测试用例
    vector<vector<int>> events1 = {{1,2},{2,3},{3,4}};
    cout << maxEvents(events1) << endl;  // 应该输出 3
    
    vector<vector<int>> events2 = {{1,2},{2,3},{3,4},{1,2}};
    cout << maxEvents(events2) << endl;  // 应该输出 4
    
    return 0;
}
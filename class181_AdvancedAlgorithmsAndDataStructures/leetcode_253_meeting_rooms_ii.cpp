#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 253. 会议室II (Meeting Rooms II) - C++版本
 * 
 * 解题思路：
 * 使用扫描线算法解决会议室安排问题
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class Solution {
public:
    int minMeetingRooms(vector<vector<int>>& intervals) {
        if (intervals.empty()) {
            return 0;
        }
        
        // 创建事件点列表：[时间, 类型]
        // 类型：0表示会议开始，1表示会议结束
        vector<pair<int, int>> events;
        
        // 为每个会议创建开始和结束事件
        for (const auto& interval : intervals) {
            events.emplace_back(interval[0], 0);  // 开始事件
            events.emplace_back(interval[1], 1);  // 结束事件
        }
        
        // 按照时间排序事件点
        // 如果时间相同，结束事件优先于开始事件
        sort(events.begin(), events.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
            if (a.first != b.first) {
                return a.first < b.first;
            }
            return a.second > b.second;
        });
        
        int maxRooms = 0;
        int currentRooms = 0;
        
        // 扫描所有事件点
        for (const auto& event : events) {
            if (event.second == 0) {
                // 会议开始事件
                currentRooms++;
                maxRooms = max(maxRooms, currentRooms);
            } else {
                // 会议结束事件
                currentRooms--;
            }
        }
        
        return maxRooms;
    }
};

/**
 * 测试会议室II解法
 */
void testMeetingRoomsII() {
    cout << "=== LeetCode 253. 会议室II (C++版本) ===" << endl;
    
    Solution solution;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> intervals1 = {{0, 30}, {5, 10}, {15, 20}};
    int result1 = solution.minMeetingRooms(intervals1);
    cout << "输入: ";
    for (const auto& interval : intervals1) {
        cout << "[" << interval[0] << "," << interval[1] << "] ";
    }
    cout << endl;
    cout << "输出: " << result1 << endl;
    cout << "期望: 2" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> intervals2 = {{7, 10}, {2, 4}};
    int result2 = solution.minMeetingRooms(intervals2);
    cout << "输入: ";
    for (const auto& interval : intervals2) {
        cout << "[" << interval[0] << "," << interval[1] << "] ";
    }
    cout << endl;
    cout << "输出: " << result2 << endl;
    cout << "期望: 1" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    default_random_engine generator(42);
    uniform_int_distribution<int> distribution(0, 1000000);
    
    int n = 10000;
    vector<vector<int>> largeIntervals(n, vector<int>(2));
    
    for (int i = 0; i < n; i++) {
        int start = distribution(generator);
        int end = start + distribution(generator) % 1000 + 1;
        largeIntervals[i] = {start, end};
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int largeResult = solution.minMeetingRooms(largeIntervals);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << n << "个会议的会议室计算完成" << endl;
    cout << "所需会议室数量: " << largeResult << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    // C++语言特性考量
    cout << "\n=== C++语言特性考量 ===" << endl;
    cout << "1. 使用pair存储事件点，提高内存效率" << endl;
    cout << "2. 使用emplace_back避免临时对象构造" << endl;
    cout << "3. 使用lambda表达式进行自定义排序" << endl;
    cout << "4. 使用const引用避免不必要的拷贝" << endl;
}

int main() {
    testMeetingRoomsII();
    return 0;
}
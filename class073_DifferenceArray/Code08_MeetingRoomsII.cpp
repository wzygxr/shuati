#include <iostream>
#include <vector>
#include <map>
#include <climits>
#include <algorithm>

/**
 * LeetCode 253. 会议室预定 (Meeting Rooms II)
 * 
 * 题目描述:
 * 给定一个会议时间安排的数组，每个会议时间都会包括开始和结束的时间 [[s1,e1],[s2,e2],...] (si < ei)，
 * 为避免会议冲突，同时要考虑充分利用会议室资源，请你计算至少需要多少间会议室，才能满足这些会议安排。
 * 
 * 示例1:
 * 输入: [[0, 30],[5, 10],[15, 20]]
 * 输出: 2
 * 解释: 需要至少两个会议室，一个会议室举办 [0, 30]，另一个举办 [5, 10] 和 [15, 20]。
 * 
 * 示例2:
 * 输入: [[7,10],[2,4]]
 * 输出: 1
 * 
 * 提示:
 * - 所有输入的会议时间都是有效的，并且不会有重叠的时间区间。
 * - 输入的会议时间可能有任意顺序。
 * 
 * 题目链接: https://leetcode.com/problems/meeting-rooms-ii/
 * 
 * 解题思路:
 * 方法一：差分数组法
 * 1. 找出所有会议的最早开始时间和最晚结束时间
 * 2. 创建一个差分数组，大小为最晚结束时间减去最早开始时间加1
 * 3. 对于每个会议，在差分数组的开始时间位置加1，在结束时间位置减1
 * 4. 计算差分数组的前缀和，前缀和的最大值就是所需的最少会议室数量
 * 
 * 方法二：排序+扫描线法（使用map实现，更高效地处理离散时间点）
 * 1. 使用map来记录每个时间点的会议数量变化
 * 2. 对于每个会议，在开始时间加1，在结束时间减1
 * 3. 按时间顺序遍历map，累加会议数量，记录最大值
 * 
 * 时间复杂度: O(n log n) - n是会议数量，主要来自map的排序
 * 空间复杂度: O(n) - 需要存储时间点和对应的变化量
 * 
 * 这是最优解，因为我们需要考虑所有会议的时间点，并且需要排序来按时间顺序处理。
 */
using namespace std;

class Solution {
public:
    /**
     * 计算最少需要的会议室数量
     * 
     * @param intervals 会议时间安排的数组
     * @return 最少需要的会议室数量
     */
    int minMeetingRooms(vector<vector<int>>& intervals) {
        // 边界情况处理
        if (intervals.empty()) {
            return 0;
        }
        if (intervals.size() == 1) {
            return 1;
        }
        
        // 方法二：使用map实现扫描线算法
        // map会自动按时间顺序排序
        map<int, int> timePointChanges;
        
        // 对于每个会议，在开始时间增加1个会议室需求，在结束时间减少1个会议室需求
        for (const auto& interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            
            // 在开始时间增加1个会议室需求
            timePointChanges[start]++;
            // 在结束时间减少1个会议室需求
            timePointChanges[end]--;
        }
        
        // 按时间顺序计算同时进行的会议数量
        int currentRooms = 0;
        int maxRooms = 0;
        
        for (const auto& pair : timePointChanges) {
            // 更新当前使用的会议室数量
            currentRooms += pair.second;
            // 更新最大会议室数量
            maxRooms = max(maxRooms, currentRooms);
        }
        
        return maxRooms;
    }
    
    /**
     * 方法一：差分数组实现
     * 注意：此方法适用于时间范围较小的情况
     */
    int minMeetingRoomsWithDiffArray(vector<vector<int>>& intervals) {
        if (intervals.empty()) {
            return 0;
        }
        
        // 找出最早开始时间和最晚结束时间
        int earliestStart = INT_MAX;
        int latestEnd = INT_MIN;
        
        for (const auto& interval : intervals) {
            earliestStart = min(earliestStart, interval[0]);
            latestEnd = max(latestEnd, interval[1]);
        }
        
        // 创建差分数组
        vector<int> diff(latestEnd - earliestStart + 1, 0);
        
        // 对每个会议进行差分标记
        for (const auto& interval : intervals) {
            int start = interval[0] - earliestStart;
            int end = interval[1] - earliestStart;
            
            diff[start] += 1;  // 开始时间增加会议室需求
            if (end < diff.size()) {
                diff[end] -= 1;  // 结束时间减少会议室需求
            }
        }
        
        // 计算前缀和并找出最大值
        int currentRooms = 0;
        int maxRooms = 0;
        
        for (int i = 0; i < diff.size(); i++) {
            currentRooms += diff[i];
            maxRooms = max(maxRooms, currentRooms);
        }
        
        return maxRooms;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> intervals1 = {{0, 30}, {5, 10}, {15, 20}};
    int result1 = solution.minMeetingRooms(intervals1);
    // 预期输出: 2
    cout << "测试用例1: " << result1 << endl;
    cout << "测试用例1 (差分数组): " << solution.minMeetingRoomsWithDiffArray(intervals1) << endl;

    // 测试用例2
    vector<vector<int>> intervals2 = {{7, 10}, {2, 4}};
    int result2 = solution.minMeetingRooms(intervals2);
    // 预期输出: 1
    cout << "测试用例2: " << result2 << endl;
    cout << "测试用例2 (差分数组): " << solution.minMeetingRoomsWithDiffArray(intervals2) << endl;
    
    // 测试用例3
    vector<vector<int>> intervals3 = {{1, 5}, {8, 9}, {8, 9}};
    int result3 = solution.minMeetingRooms(intervals3);
    // 预期输出: 2
    cout << "测试用例3: " << result3 << endl;
    cout << "测试用例3 (差分数组): " << solution.minMeetingRoomsWithDiffArray(intervals3) << endl;
    
    // 测试用例4
    vector<vector<int>> intervals4 = {{13, 15}, {1, 13}, {0, 2}};
    int result4 = solution.minMeetingRooms(intervals4);
    // 预期输出: 2
    cout << "测试用例4: " << result4 << endl;
    cout << "测试用例4 (差分数组): " << solution.minMeetingRoomsWithDiffArray(intervals4) << endl;
    
    return 0;
}
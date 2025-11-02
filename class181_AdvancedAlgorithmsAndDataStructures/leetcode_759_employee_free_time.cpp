#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 759. 员工空闲时间 (Employee Free Time)
 * 
 * 题目来源：https://leetcode.cn/problems/employee-free-time/
 * 
 * 题目描述：
 * 给定员工的 schedule 列表，表示每个员工的工作时间。
 * 每个员工都有一个非重叠的时间段 Intervals 列表，这些时间段已经排好序。
 * 返回表示所有员工的共同，有限时间段的列表，且该时间段需均为空闲时间，同时该时间段需要按升序排列。
 * 
 * 示例 1：
 * 输入：schedule = [[[1,2],[5,6]],[[1,3]],[[4,10]]]
 * 输出：[[3,4]]
 * 解释：在所有员工工作时间之外的共同空闲时间是[3,4]
 * 
 * 示例 2：
 * 输入：schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]
 * 输出：[[5,6],[7,9]]
 * 
 * 提示：
 * 1 <= schedule.length , schedule[i].length <= 50
 * 0 <= schedule[i].start < schedule[i].end <= 10^8
 * 
 * 解题思路：
 * 使用扫描线算法解决员工空闲时间问题。核心思想是：
 * 1. 将所有员工的工作时间转换为事件点
 * 2. 对所有事件点按时间排序
 * 3. 扫描所有事件点，维护当前正在工作的员工数量
 * 4. 当员工数量从大于0变为0时，开始空闲时间；从0变为大于0时，结束空闲时间
 * 
 * 时间复杂度：O(n log n)，其中 n 是所有工作时间段的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 56. 合并区间
 * - LeetCode 253. 会议室II
 */

// 定义区间结构体
struct Interval {
    int start;
    int end;
    
    Interval() : start(0), end(0) {}
    Interval(int s, int e) : start(s), end(e) {}
};

class Solution {
public:
    /**
     * 员工空闲时间的扫描线解法
     * @param schedule 员工工作时间安排
     * @return 所有员工的共同空闲时间
     */
    static vector<Interval> employeeFreeTime(vector<vector<Interval>>& schedule) {
        if (schedule.empty()) {
            return {};
        }
        
        // 创建事件点列表：[时间, 类型]
        // 类型：0表示工作开始，1表示工作结束
        vector<pair<int, int>> events;
        
        // 为每个工作时间段创建开始和结束事件
        for (const auto& employee : schedule) {
            for (const auto& interval : employee) {
                events.emplace_back(interval.start, 0);  // 开始事件
                events.emplace_back(interval.end, 1);    // 结束事件
            }
        }
        
        // 按照时间排序事件点
        // 如果时间相同，结束事件优先于开始事件
        sort(events.begin(), events.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
            if (a.first != b.first) {
                return a.first < b.first;
            }
            return a.second > b.second;
        });
        
        vector<Interval> result;
        int workingCount = 0;
        int freeStart = 0;
        
        // 扫描所有事件点
        for (const auto& event : events) {
            int time = event.first;
            int type = event.second;
            
            if (type == 0) {
                // 工作开始事件
                if (workingCount == 0 && freeStart < time) {
                    // 结束空闲时间
                    result.emplace_back(freeStart, time);
                }
                workingCount++;
            } else {
                // 工作结束事件
                workingCount--;
                if (workingCount == 0) {
                    // 开始空闲时间
                    freeStart = time;
                }
            }
        }
        
        return result;
    }
};

/**
 * 测试员工空闲时间解法
 */
void testEmployeeFreeTime() {
    cout << "=== LeetCode 759. 员工空闲时间 ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<Interval>> schedule1 = {
        {{1, 2}, {5, 6}},
        {{1, 3}},
        {{4, 10}}
    };
    vector<Interval> result1 = Solution::employeeFreeTime(schedule1);
    cout << "输入: ";
    for (const auto& employee : schedule1) {
        cout << "[";
        for (size_t i = 0; i < employee.size(); i++) {
            cout << "[" << employee[i].start << "," << employee[i].end << "]";
            if (i < employee.size() - 1) cout << ",";
        }
        cout << "] ";
    }
    cout << endl;
    cout << "输出: ";
    for (const auto& interval : result1) {
        cout << "[" << interval.start << "," << interval.end << "] ";
    }
    cout << endl;
    cout << "期望: [[3,4]]" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<Interval>> schedule2 = {
        {{1, 3}, {6, 7}},
        {{2, 4}},
        {{2, 5}, {9, 12}}
    };
    vector<Interval> result2 = Solution::employeeFreeTime(schedule2);
    cout << "输入: ";
    for (const auto& employee : schedule2) {
        cout << "[";
        for (size_t i = 0; i < employee.size(); i++) {
            cout << "[" << employee[i].start << "," << employee[i].end << "]";
            if (i < employee.size() - 1) cout << ",";
        }
        cout << "] ";
    }
    cout << endl;
    cout << "输出: ";
    for (const auto& interval : result2) {
        cout << "[" << interval.start << "," << interval.end << "] ";
    }
    cout << endl;
    cout << "期望: [[5,6],[7,9]]" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dis(0, 1000);
    uniform_int_distribution<int> dis2(1, 1000);
    uniform_int_distribution<int> dis3(0, 1000);
    
    int employeeCount = 50;
    int intervalsPerEmployee = 50;
    vector<vector<Interval>> schedule;
    
    for (int i = 0; i < employeeCount; i++) {
        vector<Interval> employee;
        int currentTime = 0;
        for (int j = 0; j < intervalsPerEmployee; j++) {
            int start = currentTime + dis(gen);
            int end = start + dis2(gen);
            employee.emplace_back(start, end);
            currentTime = end + dis3(gen);
        }
        schedule.push_back(employee);
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    vector<Interval> result = Solution::employeeFreeTime(schedule);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "50个员工，每个员工50个工作时间段的空闲时间计算完成" << endl;
    cout << "共同空闲时间段数量: " << result.size() << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testEmployeeFreeTime();
    return 0;
}
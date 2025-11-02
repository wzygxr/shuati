#include <vector>
#include <algorithm>
#include <queue>
#include <string>
#include <deque>
#include <functional>
#include <numeric>
#include <iostream>

using namespace std;

/**
 * 贪心算法总结与实战练习 - C++版本
 */

/**
 * 区间调度问题模板
 */
int intervalScheduling(vector<vector<int>>& intervals) {
    if (intervals.empty()) return 0;
    
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    int count = 1;
    int end = intervals[0][1];
    
    for (int i = 1; i < intervals.size(); i++) {
        if (intervals[i][0] >= end) {
            count++;
            end = intervals[i][1];
        }
    }
    
    return count;
}

/**
 * 资源分配问题模板
 */
int resourceAllocation(vector<vector<int>>& tasks, int resource) {
    if (tasks.empty() || resource <= 0) return 0;
    
    sort(tasks.begin(), tasks.end(), [](const vector<int>& a, const vector<int>& b) {
        return (double) b[1] / b[0] > (double) a[1] / a[0];
    });
    
    int profit = 0;
    int remaining = resource;
    
    for (auto& task : tasks) {
        int cost = task[0];
        int value = task[1];
        
        if (remaining >= cost) {
            profit += value;
            remaining -= cost;
        } else {
            profit += value * remaining / cost;
            break;
        }
    }
    
    return profit;
}

/**
 * 综合区间调度练习
 */
int comprehensiveIntervalProblems(vector<vector<int>>& intervals) {
    if (intervals.empty()) return 0;
    
    // 最多不重叠区间数
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    int nonOverlapCount = 1;
    int end = intervals[0][1];
    
    for (int i = 1; i < intervals.size(); i++) {
        if (intervals[i][0] >= end) {
            nonOverlapCount++;
            end = intervals[i][1];
        }
    }
    
    // 合并重叠区间
    vector<vector<int>> merged;
    merged.push_back(intervals[0]);
    
    for (int i = 1; i < intervals.size(); i++) {
        vector<int>& last = merged.back();
        if (intervals[i][0] <= last[1]) {
            last[1] = max(last[1], intervals[i][1]);
        } else {
            merged.push_back(intervals[i]);
        }
    }
    
    return merged.size();
}

/**
 * 综合资源分配练习
 */
int comprehensiveResourceAllocation(vector<vector<int>>& items, int capacity) {
    if (items.empty() || capacity <= 0) return 0;
    
    // 策略1: 按价值排序
    sort(items.begin(), items.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] > b[1];
    });
    
    int valueStrategy = 0;
    int remaining = capacity;
    
    for (auto& item : items) {
        if (item[0] <= remaining) {
            valueStrategy += item[1];
            remaining -= item[0];
        }
    }
    
    // 策略2: 按价值密度排序
    sort(items.begin(), items.end(), [](const vector<int>& a, const vector<int>& b) {
        return (double) a[1] / a[0] > (double) b[1] / b[0];
    });
    
    int densityStrategy = 0;
    remaining = capacity;
    
    for (auto& item : items) {
        if (item[0] <= remaining) {
            densityStrategy += item[1];
            remaining -= item[0];
        } else {
            densityStrategy += item[1] * remaining / item[0];
            break;
        }
    }
    
    return max(valueStrategy, densityStrategy);
}

/**
 * 调试技巧：打印中间结果
 */
void printIntermediateResults(vector<int>& arr) {
    cout << "原始数组: ";
    for (int num : arr) cout << num << " ";
    cout << endl;
    
    vector<int> sorted = arr;
    sort(sorted.begin(), sorted.end());
    
    cout << "排序后数组: ";
    for (int num : sorted) cout << num << " ";
    cout << endl;
    
    int sum = 0;
    for (int i = 0; i < sorted.size(); i++) {
        sum += sorted[i];
        cout << "第" << (i + 1) << "步选择: " << sorted[i] 
             << ", 当前总和: " << sum << endl;
    }
}

/**
 * 贪心算法核心思想总结
 */
class GreedyPrinciples {
public:
    /**
     * 贪心选择性质验证
     */
    static bool verifyGreedyProperty(vector<int>& arr) {
        // 验证排序后选择是否最优
        vector<int> sorted = arr;
        sort(sorted.begin(), sorted.end());
        
        int greedySum = 0;
        for (int num : sorted) {
            greedySum += num;
        }
        
        // 简单验证：贪心解应该不小于任意其他选择
        return greedySum >= accumulate(arr.begin(), arr.end(), 0);
    }
    
    /**
     * 最优子结构验证
     */
    static bool verifyOptimalSubstructure(vector<vector<int>>& intervals) {
        if (intervals.empty()) return true;
        
        // 验证移除一个区间后，剩余问题的最优解是否包含在原问题最优解中
        sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        int originalCount = 1;
        int end = intervals[0][1];
        
        for (int i = 1; i < intervals.size(); i++) {
            if (intervals[i][0] >= end) {
                originalCount++;
                end = intervals[i][1];
            }
        }
        
        // 测试移除第一个区间
        vector<vector<int>> subproblem(intervals.begin() + 1, intervals.end());
        int subCount = intervalScheduling(subproblem);
        
        return originalCount == subCount || originalCount == subCount + 1;
    }
};

// 测试函数
int main() {
    // 测试区间调度
    vector<vector<int>> intervals = {{1, 3}, {2, 4}, {3, 5}, {4, 6}};
    cout << "区间调度结果: " << intervalScheduling(intervals) << endl;
    
    // 测试资源分配
    vector<vector<int>> items = {{2, 10}, {3, 15}, {5, 20}};
    cout << "资源分配结果: " << resourceAllocation(items, 7) << endl;
    
    // 测试调试技巧
    vector<int> testArray = {3, 1, 4, 1, 5};
    printIntermediateResults(testArray);
    
    return 0;
}
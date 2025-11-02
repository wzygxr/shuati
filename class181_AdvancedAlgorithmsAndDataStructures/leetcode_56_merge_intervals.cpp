#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 56. 合并区间 (Merge Intervals) - C++版本
 * 
 * 题目来源：https://leetcode.cn/problems/merge-intervals/
 * 
 * 解题思路：
 * 使用扫描线算法解决区间合并问题
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class Solution {
public:
    vector<vector<int>> merge(vector<vector<int>>& intervals) {
        if (intervals.empty()) {
            return {};
        }
        
        // 按照区间开始时间排序
        sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[0] < b[0];
        });
        
        vector<vector<int>> merged;
        vector<int> currentInterval = intervals[0];
        
        for (int i = 1; i < intervals.size(); i++) {
            if (intervals[i][0] <= currentInterval[1]) {
                // 当前区间与合并区间重叠，合并
                currentInterval[1] = max(currentInterval[1], intervals[i][1]);
            } else {
                // 不重叠，将当前合并区间加入结果，开始新的合并区间
                merged.push_back(currentInterval);
                currentInterval = intervals[i];
            }
        }
        
        // 添加最后一个合并区间
        merged.push_back(currentInterval);
        
        return merged;
    }
};

/**
 * 打印二维数组
 */
void printVector2D(const vector<vector<int>>& vec) {
    cout << "[";
    for (int i = 0; i < vec.size(); i++) {
        cout << "[" << vec[i][0] << "," << vec[i][1] << "]";
        if (i < vec.size() - 1) cout << ",";
    }
    cout << "]" << endl;
}

int main() {
    cout << "=== LeetCode 56. 合并区间 (C++版本) ===" << endl;
    
    Solution solution;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
    vector<vector<int>> result1 = solution.merge(intervals1);
    cout << "输入: ";
    printVector2D(intervals1);
    cout << "输出: ";
    printVector2D(result1);
    cout << "期望: [[1,6],[8,10],[15,18]]" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> intervals2 = {{1, 4}, {4, 5}};
    vector<vector<int>> result2 = solution.merge(intervals2);
    cout << "输入: ";
    printVector2D(intervals2);
    cout << "输出: ";
    printVector2D(result2);
    cout << "期望: [[1,5]]" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    default_random_engine generator(42);
    uniform_int_distribution<int> distribution(0, 100000);
    
    int n = 10000;
    vector<vector<int>> largeIntervals(n, vector<int>(2));
    
    for (int i = 0; i < n; i++) {
        int start = distribution(generator);
        int end = start + distribution(generator) % 1000 + 1;
        largeIntervals[i] = {start, end};
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    vector<vector<int>> largeResult = solution.merge(largeIntervals);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << n << "个区间的合并计算完成" << endl;
    cout << "合并后区间数量: " << largeResult.size() << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    // C++语言特性考量
    cout << "\n=== C++语言特性考量 ===" << endl;
    cout << "1. 使用lambda表达式进行自定义排序" << endl;
    cout << "2. 使用vector容器管理动态数组" << endl;
    cout << "3. 使用chrono库进行精确时间测量" << endl;
    cout << "4. 使用RAII原则管理资源" << endl;
    
    return 0;
}
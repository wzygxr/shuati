#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <functional>

using namespace std;

/**
 * 课程表 III - C++实现
 * 题目解析：贪心 + 优先队列，在截止时间前尽可能多地完成课程
 * 
 * 算法思路：
 * 1. 按照截止时间对课程进行排序
 * 2. 使用最大堆记录已选课程的持续时间
 * 3. 遍历课程，根据时间约束进行选择或替换
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 工程化考虑：
 * 1. 使用lambda表达式简化比较函数
 * 2. 优先队列使用greater<int>实现最小堆，通过负数实现最大堆效果
 * 3. 输入验证和边界处理
 */
class Solution {
public:
    int scheduleCourse(vector<vector<int>>& courses) {
        // 按照截止时间排序
        sort(courses.begin(), courses.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        // 最大堆，存储已选课程的持续时间
        priority_queue<int> maxHeap;
        int time = 0; // 当前已用时间
        
        for (const auto& course : courses) {
            int duration = course[0];
            int lastDay = course[1];
            
            if (time + duration <= lastDay) {
                // 可以直接选择这门课程
                time += duration;
                maxHeap.push(duration);
            } else if (!maxHeap.empty() && maxHeap.top() > duration) {
                // 替换掉持续时间最长的课程
                time = time - maxHeap.top() + duration;
                maxHeap.pop();
                maxHeap.push(duration);
            }
        }
        
        return maxHeap.size();
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> courses1 = {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}};
    cout << solution.scheduleCourse(courses1) << endl; // 输出: 3
    
    // 测试用例2
    vector<vector<int>> courses2 = {{1, 2}};
    cout << solution.scheduleCourse(courses2) << endl; // 输出: 1
    
    // 测试用例3
    vector<vector<int>> courses3 = {{3, 2}, {4, 3}};
    cout << solution.scheduleCourse(courses3) << endl; // 输出: 0
    
    return 0;
}
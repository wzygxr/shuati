#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 无重叠区间
// 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
// 返回需要移除区间的最小数量，使剩余区间互不重叠。
// 测试链接 : https://leetcode.cn/problems/non-overlapping-intervals/

class Solution {
public:
    /*
     * 贪心算法解法
     * 
     * 核心思想：
     * 1. 将所有区间按照右边界进行排序
     * 2. 选择尽可能多的不重叠区间
     * 3. 每次选择结束时间最早的区间，这样可以为后面的区间留出更多空间
     * 4. 总区间数减去不重叠区间数就是需要移除的最小数量
     * 
     * 时间复杂度：O(n log n) - 排序的时间复杂度为O(n log n)，遍历数组的时间复杂度为O(n)
     * 空间复杂度：O(log n) - 排序所需的栈空间
     * 
     * 为什么这是最优解？
     * 1. 贪心策略保证了选择尽可能多的不重叠区间
     * 2. 按结束时间排序是关键，这样可以确保我们总是选择最早结束的区间
     * 3. 通过数学归纳法可以证明这种策略能得到全局最优解
     * 
     * 工程化考虑：
     * 1. 边界条件处理：空数组、单元素数组
     * 2. 异常处理：处理可能的整数溢出问题
     * 3. 可读性：变量命名清晰，注释详细
     * 
     * 算法调试技巧：
     * 1. 可以通过打印排序后的区间数组来验证排序是否正确
     * 2. 可以打印每一步选择的区间
     */

    int eraseOverlapIntervals(vector<vector<int>>& intervals) {
        // 边界条件：如果没有区间，不需要移除
        if (intervals.empty()) {
            return 0;
        }

        // 边界条件：如果只有一个区间，不需要移除
        if (intervals.size() == 1) {
            return 0;
        }

        // 按照区间的右边界进行排序
        sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });

        // 初始化不重叠区间的数量为1，第一个区间是默认选择的
        int nonOverlapCount = 1;
        // 当前选择的区间的结束时间
        int currentEnd = intervals[0][1];

        // 遍历所有区间
        for (size_t i = 1; i < intervals.size(); i++) {
            // 如果当前区间的开始时间大于等于上一个选择的区间的结束时间，说明不重叠
            if (intervals[i][0] >= currentEnd) {
                // 选择当前区间
                nonOverlapCount++;
                // 更新结束时间
                currentEnd = intervals[i][1];
            }
            // 否则，当前区间与已选择的区间重叠，不选择当前区间
        }

        // 需要移除的区间数量 = 总区间数 - 不重叠区间数
        return static_cast<int>(intervals.size()) - nonOverlapCount;
    }

    // 另一种实现方式，直接计算需要移除的区间数量
    int eraseOverlapIntervals2(vector<vector<int>>& intervals) {
        if (intervals.size() <= 1) {
            return 0;
        }

        // 按照结束时间排序
        sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });

        int end = intervals[0][1];
        int removeCount = 0;

        for (size_t i = 1; i < intervals.size(); i++) {
            // 如果当前区间的开始时间小于前一个区间的结束时间，说明重叠
            if (intervals[i][0] < end) {
                // 需要移除
                removeCount++;
            } else {
                // 更新结束时间
                end = intervals[i][1];
            }
        }

        return removeCount;
    }
};

// 辅助函数：打印二维数组
void print2DVector(const vector<vector<int>>& vec) {
    cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        cout << "[";
        for (size_t j = 0; j < vec[i].size(); j++) {
            cout << vec[i][j];
            if (j < vec[i].size() - 1) {
                cout << ", ";
            }
        }
        cout << "]";
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]";
}

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1: [[1,2],[2,3],[3,4],[1,3]] -> 1
    vector<vector<int>> intervals1 = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
    cout << "测试用例1: ";
    print2DVector(intervals1);
    cout << endl;
    cout << "预期结果: 1, 实际结果: " << solution.eraseOverlapIntervals(intervals1) << endl;
    cout << "另一种实现结果: " << solution.eraseOverlapIntervals2(intervals1) << endl << endl;

    // 测试用例2: [[1,2],[1,2],[1,2]] -> 2
    vector<vector<int>> intervals2 = {{1, 2}, {1, 2}, {1, 2}};
    cout << "测试用例2: ";
    print2DVector(intervals2);
    cout << endl;
    cout << "预期结果: 2, 实际结果: " << solution.eraseOverlapIntervals(intervals2) << endl;
    cout << "另一种实现结果: " << solution.eraseOverlapIntervals2(intervals2) << endl << endl;

    // 测试用例3: [[1,2],[2,3]] -> 0
    vector<vector<int>> intervals3 = {{1, 2}, {2, 3}};
    cout << "测试用例3: ";
    print2DVector(intervals3);
    cout << endl;
    cout << "预期结果: 0, 实际结果: " << solution.eraseOverlapIntervals(intervals3) << endl;
    cout << "另一种实现结果: " << solution.eraseOverlapIntervals2(intervals3) << endl << endl;

    // 测试用例4: [] -> 0
    vector<vector<int>> intervals4 = {};
    cout << "测试用例4: []" << endl;
    cout << "预期结果: 0, 实际结果: " << solution.eraseOverlapIntervals(intervals4) << endl;
    cout << "另一种实现结果: " << solution.eraseOverlapIntervals2(intervals4) << endl << endl;

    // 测试用例5: [[1,2]] -> 0
    vector<vector<int>> intervals5 = {{1, 2}};
    cout << "测试用例5: [[1, 2]]" << endl;
    cout << "预期结果: 0, 实际结果: " << solution.eraseOverlapIntervals(intervals5) << endl;
    cout << "另一种实现结果: " << solution.eraseOverlapIntervals2(intervals5) << endl << endl;

    // 测试用例6: 极端情况
    vector<vector<int>> intervals6 = {{-2147483648, 2147483647}};
    cout << "测试用例6: [[-2147483648, 2147483647]]" << endl;
    cout << "预期结果: 0, 实际结果: " << solution.eraseOverlapIntervals(intervals6) << endl;
    cout << "另一种实现结果: " << solution.eraseOverlapIntervals2(intervals6) << endl;
}

int main() {
    test();
    return 0;
}
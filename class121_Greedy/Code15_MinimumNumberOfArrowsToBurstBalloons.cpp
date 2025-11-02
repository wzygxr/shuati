#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 用最少数量的箭引爆气球
// 题目描述：有一些球形气球贴在一堵用 XY 平面表示的墙面上。
// 墙面上的气球记录在整数数组 points ，其中 points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend 之间的气球。
// 一支弓箭可以沿着 x 轴从不同点完全垂直地射出。在坐标 x 处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend，
// 且满足 xstart ≤ x ≤ xend，则该气球会被引爆。
// 给你一个数组 points ，返回引爆所有气球所必须射出的最小弓箭数。
// 测试链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/

class Code15_MinimumNumberOfArrowsToBurstBalloons {
public:
    /**
     * 用最少数量的箭引爆气球的贪心解法
     * 
     * 解题思路：
     * 1. 将气球按照右端点升序排序
     * 2. 从排序后的第一个气球开始，将箭放在该气球的右端点位置
     * 3. 依次检查后续气球，如果当前气球的左端点小于等于箭的位置，则可以用同一支箭引爆
     *    否则，需要新增一支箭，并将箭放在当前气球的右端点位置
     * 
     * 贪心策略的正确性：
     * 每次将箭放在能够覆盖当前气球的最右位置，这样可以尽可能多地覆盖后续气球
     * 通过按照右端点排序，确保我们总是优先处理结束较早的气球
     * 
     * 时间复杂度：O(n log n)，主要消耗在排序操作上
     * 
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param points 气球的坐标数组，每个元素表示气球的左端点和右端点
     * @return 引爆所有气球所需的最小弓箭数
     */
    static int findMinArrowShots(vector<vector<int>>& points) {
        // 边界条件处理
        if (points.empty()) {
            return 0;
        }

        // 按气球的右端点升序排序
        // 注意：使用lambda表达式进行排序
        sort(points.begin(), points.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });

        int count = 1;  // 需要的箭数，至少需要1支箭
        int arrowPos = points[0][1];  // 第一支箭的位置，放在第一个气球的右端点

        // 遍历排序后的气球
        for (int i = 1; i < points.size(); i++) {
            // 如果当前气球的左端点大于箭的位置，说明不能用同一支箭引爆
            if (points[i][0] > arrowPos) {
                count++;  // 需要新增一支箭
                arrowPos = points[i][1];  // 更新箭的位置到当前气球的右端点
            }
            // 否则，当前气球可以被现有的箭引爆，不需要额外操作
        }

        return count;
    }
};

// 测试方法
int main() {
    // 测试用例1
    // 输入: points = [[10,16],[2,8],[1,6],[7,12]]
    // 输出: 2
    // 解释: 可以在x = 6处发射一支箭，引爆气球[2,8]和[1,6]。
    // 然后在x = 11处发射另一支箭，引爆气球[10,16]和[7,12]。
    vector<vector<int>> points1 = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
    cout << "测试用例1结果: " << Code15_MinimumNumberOfArrowsToBurstBalloons::findMinArrowShots(points1) << endl; // 期望输出: 2

    // 测试用例2
    // 输入: points = [[1,2],[3,4],[5,6],[7,8]]
    // 输出: 4
    // 解释: 每个气球需要单独一支箭
    vector<vector<int>> points2 = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
    cout << "测试用例2结果: " << Code15_MinimumNumberOfArrowsToBurstBalloons::findMinArrowShots(points2) << endl; // 期望输出: 4

    // 测试用例3
    // 输入: points = [[1,2],[2,3],[3,4],[4,5]]
    // 输出: 2
    // 解释: 可以在x = 2处发射一支箭，引爆气球[1,2]和[2,3]。
    // 然后在x = 4处发射另一支箭，引爆气球[3,4]和[4,5]。
    vector<vector<int>> points3 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
    cout << "测试用例3结果: " << Code15_MinimumNumberOfArrowsToBurstBalloons::findMinArrowShots(points3) << endl; // 期望输出: 2

    // 测试用例4：边界情况 - 空数组
    // 输入: points = []
    // 输出: 0
    vector<vector<int>> points4 = {};
    cout << "测试用例4结果: " << Code15_MinimumNumberOfArrowsToBurstBalloons::findMinArrowShots(points4) << endl; // 期望输出: 0

    // 测试用例5：边界情况 - 只有一个气球
    // 输入: points = [[1,2]]
    // 输出: 1
    vector<vector<int>> points5 = {{1, 2}};
    cout << "测试用例5结果: " << Code15_MinimumNumberOfArrowsToBurstBalloons::findMinArrowShots(points5) << endl; // 期望输出: 1

    // 测试用例6：复杂情况 - 多层重叠
    // 输入: points = [[1,5],[2,3],[4,7],[6,9],[8,10]]
    // 输出: 2
    vector<vector<int>> points6 = {{1, 5}, {2, 3}, {4, 7}, {6, 9}, {8, 10}};
    cout << "测试用例6结果: " << Code15_MinimumNumberOfArrowsToBurstBalloons::findMinArrowShots(points6) << endl; // 期望输出: 2

    return 0;
}
// 用最少数量的箭引爆气球（Minimum Number of Arrows to Burst Balloons）
// 题目来源：LeetCode 452
// 题目链接：https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
// 
// 问题描述：
// 在二维空间中有许多球形的气球，每个气球在水平方向上的直径范围是[xstart, xend]。
// 用最少数量的箭引爆所有气球。一支箭可以垂直向上射出，在xstart和xend之间穿过气球。
// 只要箭的x坐标在气球的直径范围内，气球就会被引爆。
// 
// 算法思路：
// 使用贪心策略，按照气球结束坐标排序：
// 1. 将气球按照结束坐标从小到大排序
// 2. 遍历排序后的气球，记录当前箭的位置
// 3. 如果当前气球的开始坐标大于箭的位置，说明需要新的箭
// 4. 否则，当前箭可以引爆这个气球
// 
// 时间复杂度：O(n log n) - 排序的时间复杂度
// 空间复杂度：O(1) - 只使用了常数额外空间
// 
// 是否最优解：是。这是该问题的最优解法。
// 
// 适用场景：
// 1. 区间覆盖问题
// 2. 最小点覆盖区间问题
// 
// 异常处理：
// 1. 处理空数组情况
// 2. 处理单元素数组
// 
// 工程化考量：
// 1. 输入验证：检查数组是否为空
// 2. 边界条件：处理单元素和双元素数组
// 3. 性能优化：使用快速排序提高效率
// 
// 相关题目：
// 1. LeetCode 435. 无重叠区间 - 类似区间问题
// 2. LeetCode 56. 合并区间 - 区间合并问题
// 3. LeetCode 252. 会议室 - 区间重叠判断
// 4. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
// 5. LintCode 391. 数飞机 - 区间调度相关
// 6. HackerRank - Jim and the Orders - 贪心调度问题
// 7. CodeChef - TACHSTCK - 区间配对问题
// 8. AtCoder ABC104C - All Green - 动态规划相关
// 9. Codeforces 1363C - Game On Leaves - 博弈论相关
// 10. POJ 3169 - Layout - 差分约束系统

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * 计算引爆所有气球所需的最少箭数
 * 
 * @param points 气球直径范围数组，每个元素是[xstart, xend]
 * @return 最少需要的箭数
 */
int findMinArrowShots(vector<vector<int>>& points) {
    // 边界条件检查
    if (points.empty()) {
        return 0;
    }
    
    int n = points.size();
    if (n == 1) {
        return 1; // 只有一个气球，需要一支箭
    }
    
    // 按照气球结束坐标排序
    sort(points.begin(), points.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    int arrows = 1; // 至少需要一支箭
    int arrowPos = points[0][1]; // 第一支箭的位置
    
    for (int i = 1; i < n; i++) {
        // 如果当前气球的开始坐标大于箭的位置，需要新的箭
        if (points[i][0] > arrowPos) {
            arrows++;
            arrowPos = points[i][1]; // 更新箭的位置
        }
        // 否则，当前箭可以引爆这个气球，继续使用同一支箭
    }
    
    return arrows;
}

/**
 * 测试函数，验证算法正确性
 */
int main() {
    // 测试用例1: 基本情况 - 有重叠气球
    vector<vector<int>> points1 = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
    int result1 = findMinArrowShots(points1);
    cout << "测试用例1:" << endl;
    cout << "气球范围: [";
    for (int i = 0; i < points1.size(); i++) {
        cout << "[" << points1[i][0] << "," << points1[i][1] << "]";
        if (i < points1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "最少箭数: " << result1 << endl;
    cout << "期望输出: 2" << endl << endl;
    
    // 测试用例2: 基本情况 - 无重叠气球
    vector<vector<int>> points2 = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
    int result2 = findMinArrowShots(points2);
    cout << "测试用例2:" << endl;
    cout << "气球范围: [";
    for (int i = 0; i < points2.size(); i++) {
        cout << "[" << points2[i][0] << "," << points2[i][1] << "]";
        if (i < points2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "最少箭数: " << result2 << endl;
    cout << "期望输出: 4" << endl << endl;
    
    // 测试用例3: 复杂情况 - 完全重叠
    vector<vector<int>> points3 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
    int result3 = findMinArrowShots(points3);
    cout << "测试用例3:" << endl;
    cout << "气球范围: [";
    for (int i = 0; i < points3.size(); i++) {
        cout << "[" << points3[i][0] << "," << points3[i][1] << "]";
        if (i < points3.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "最少箭数: " << result3 << endl;
    cout << "期望输出: 2" << endl << endl;
    
    // 测试用例4: 边界情况 - 单元素数组
    vector<vector<int>> points4 = {{1, 2}};
    int result4 = findMinArrowShots(points4);
    cout << "测试用例4:" << endl;
    cout << "气球范围: [";
    cout << "[" << points4[0][0] << "," << points4[0][1] << "]";
    cout << "]" << endl;
    cout << "最少箭数: " << result4 << endl;
    cout << "期望输出: 1" << endl << endl;
    
    // 测试用例5: 边界情况 - 空数组
    vector<vector<int>> points5 = {};
    int result5 = findMinArrowShots(points5);
    cout << "测试用例5:" << endl;
    cout << "气球范围: []" << endl;
    cout << "最少箭数: " << result5 << endl;
    cout << "期望输出: 0" << endl << endl;
    
    // 测试用例6: 复杂情况 - 大数测试
    vector<vector<int>> points6 = {{INT_MIN, INT_MIN + 1}, {INT_MAX - 1, INT_MAX}};
    int result6 = findMinArrowShots(points6);
    cout << "测试用例6:" << endl;
    cout << "气球范围: [";
    for (int i = 0; i < points6.size(); i++) {
        cout << "[" << points6[i][0] << "," << points6[i][1] << "]";
        if (i < points6.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "最少箭数: " << result6 << endl;
    cout << "期望输出: 2" << endl;
    
    return 0;
}
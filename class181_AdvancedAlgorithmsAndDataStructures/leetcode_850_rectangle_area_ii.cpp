#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <map>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 850. 矩形面积II (Rectangle Area II)
 * 
 * 题目来源：https://leetcode.cn/problems/rectangle-area-ii/
 * 
 * 题目描述：
 * 我们给出了一个（轴对齐的）二维矩形列表 rectangles 。
 * 对于 rectangle[i] = [x1, y1, x2, y2] ，其中 (x1, y1) 是矩形 i 左下角的坐标， (x2, y2) 是该矩形右上角的坐标。
 * 计算平面中所有 rectangles 所覆盖的总面积。任何被两个或多个矩形覆盖的区域应只计算一次。
 * 返回总面积。因为答案可能太大，返回 10^9 + 7 的模。
 * 
 * 示例 1：
 * 输入：rectangles = [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]
 * 输出：6
 * 解释：如图所示，三个矩形代表三个矩形，红色的为绿色的为蓝色的为。它们共同覆盖了总面积为6的区域。
 * 
 * 示例 2：
 * 输入：rectangles = [[1,1,2,2],[2,2,3,3]]
 * 输出：1
 * 解释：如图所示，两个矩形之间没有重叠区域。
 * 
 * 示例 3：
 * 输入：rectangles = [[0,0,2,2],[1,0,2,3],[1,0,3,1]]
 * 输出：6
 * 解释：如图所示，三个矩形的总面积为6。
 * 
 * 提示：
 * 1 <= rectangles.length <= 200
 * rectangles[i].length == 4
 * 0 <= xi1, yi1, xi2, yi2 <= 10^9
 * 
 * 解题思路：
 * 使用扫描线算法解决矩形面积问题。核心思想是：
 * 1. 将每个矩形的左右边界转换为垂直扫描线事件
 * 2. 对所有事件按x坐标排序
 * 3. 在每个x区间内，计算y方向的覆盖长度
 * 4. 累加每个区间的面积
 * 
 * 时间复杂度：O(n^2 log n)，其中 n 是矩形的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 218. 天际线问题
 * - LeetCode 391. 完美矩形
 */

class Solution {
private:
    static const int MOD = 1000000007;
    
public:
    /**
     * 矩形面积II的扫描线解法
     * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
     * @return 覆盖的总面积
     */
    static int rectangleArea(vector<vector<int>>& rectangles) {
        // 创建垂直扫描线事件
        vector<vector<int>> events;
        set<int> yCoordinates;
        
        for (const auto& rect : rectangles) {
            int x1 = rect[0];
            int y1 = rect[1];
            int x2 = rect[2];
            int y2 = rect[3];
            
            // 添加开始和结束事件
            events.push_back({x1, 0, y1, y2});  // 开始事件
            events.push_back({x2, 1, y1, y2});  // 结束事件
            
            // 收集所有y坐标
            yCoordinates.insert(y1);
            yCoordinates.insert(y2);
        }
        
        // 排序事件
        sort(events.begin(), events.end());
        
        // 对y坐标排序
        vector<int> sortedY(yCoordinates.begin(), yCoordinates.end());
        
        // 用于跟踪当前活动的矩形
        vector<pair<int, int>> activeIntervals;
        long long totalArea = 0;
        int prevX = events[0][0];
        
        // 处理每个事件
        for (const auto& event : events) {
            int currentX = event[0];
            long long width = currentX - prevX;
            
            if (width > 0) {
                // 计算当前活动的y区间总长度
                long long height = calculateActiveHeight(activeIntervals, sortedY);
                
                // 增加面积
                totalArea = (totalArea + width * height) % MOD;
            }
            
            // 更新活动区间
            if (event[1] == 0) {
                activeIntervals.push_back({event[2], event[3]});
            } else {
                // 移除对应的区间
                activeIntervals.erase(
                    remove_if(activeIntervals.begin(), activeIntervals.end(),
                             [&event](const pair<int, int>& interval) {
                                 return interval.first == event[2] && 
                                        interval.second == event[3];
                             }),
                    activeIntervals.end()
                );
            }
            
            prevX = currentX;
        }
        
        return (int)totalArea;
    }

private:
    /**
     * 计算当前活动的y区间总长度
     * @param activeIntervals 活动区间列表
     * @param sortedY 排序后的y坐标
     * @return 总长度
     */
    static long long calculateActiveHeight(const vector<pair<int, int>>& activeIntervals, 
                                          const vector<int>& sortedY) {
        if (activeIntervals.empty()) {
            return 0;
        }
        
        // 复制并排序区间
        vector<pair<int, int>> intervals = activeIntervals;
        sort(intervals.begin(), intervals.end());
        
        long long totalHeight = 0;
        int currentStart = intervals[0].first;
        int currentEnd = intervals[0].second;
        
        for (size_t i = 1; i < intervals.size(); i++) {
            if (intervals[i].first <= currentEnd) {
                // 重叠，合并区间
                currentEnd = max(currentEnd, intervals[i].second);
            } else {
                // 不重叠，计算长度并更新当前区间
                totalHeight += currentEnd - currentStart;
                currentStart = intervals[i].first;
                currentEnd = intervals[i].second;
            }
        }
        
        // 加上最后一个区间
        totalHeight += currentEnd - currentStart;
        
        return totalHeight;
    }
};

/**
 * 测试矩形面积II解法
 */
void testRectangleArea() {
    cout << "=== LeetCode 850. 矩形面积II ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> rectangles1 = {
        {1,1,3,3},{3,1,4,2},{3,2,4,4},{1,3,2,4},{2,3,3,4}
    };
    int result1 = Solution::rectangleArea(rectangles1);
    cout << "输入: ";
    for (const auto& rect : rectangles1) {
        cout << "[" << rect[0] << "," << rect[1] << "," << rect[2] << "," << rect[3] << "] ";
    }
    cout << endl;
    cout << "输出: " << result1 << endl;
    cout << "期望: 6" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> rectangles2 = {
        {1,1,2,2},{2,2,3,3}
    };
    int result2 = Solution::rectangleArea(rectangles2);
    cout << "输入: ";
    for (const auto& rect : rectangles2) {
        cout << "[" << rect[0] << "," << rect[1] << "," << rect[2] << "," << rect[3] << "] ";
    }
    cout << endl;
    cout << "输出: " << result2 << endl;
    cout << "期望: 1" << endl;
    cout << endl;
    
    // 测试用例3
    cout << "测试用例3:" << endl;
    vector<vector<int>> rectangles3 = {
        {0,0,2,2},{1,0,2,3},{1,0,3,1}
    };
    int result3 = Solution::rectangleArea(rectangles3);
    cout << "输入: ";
    for (const auto& rect : rectangles3) {
        cout << "[" << rect[0] << "," << rect[1] << "," << rect[2] << "," << rect[3] << "] ";
    }
    cout << endl;
    cout << "输出: " << result3 << endl;
    cout << "期望: 6" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dis(0, 1000);
    uniform_int_distribution<int> dis2(1, 100);
    
    int n = 200;
    vector<vector<int>> rectangles;
    
    for (int i = 0; i < n; i++) {
        int x1 = dis(gen);
        int y1 = dis(gen);
        int x2 = x1 + dis2(gen);
        int y2 = y1 + dis2(gen);
        rectangles.push_back({x1, y1, x2, y2});
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int result = Solution::rectangleArea(rectangles);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "200个随机矩形的总面积计算完成" << endl;
    cout << "总面积: " << result << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testRectangleArea();
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <random>
#include <chrono>
#include <iomanip>

using namespace std;

/**
 * POJ 1151 Atlantis (矩形面积并)
 * 
 * 题目来源：http://poj.org/problem?id=1151
 * 
 * 题目描述：
 * 给定一些矩形，求这些矩形的总面积（重叠部分只计算一次）。
 * 
 * 输入格式：
 * 输入包含多个测试用例。每个测试用例以一个整数n开始，表示矩形的数量。
 * 接下来n行，每行包含四个实数x1, y1, x2, y2，表示一个矩形的左下角和右上角坐标。
 * 当n=0时输入结束。
 * 
 * 输出格式：
 * 对于每个测试用例，输出一行"Test case #k"，其中k是测试用例编号。
 * 然后输出一行"Total explored area: a"，其中a是总面积，保留两位小数。
 * 每个测试用例后输出一个空行。
 * 
 * 示例输入：
 * 2
 * 10 10 20 20
 * 15 15 25 25.5
 * 0
 * 
 * 示例输出：
 * Test case #1
 * Total explored area: 180.00
 * 
 * 解题思路：
 * 使用扫描线算法解决矩形面积并问题。核心思想是：
 * 1. 将每个矩形的左右边界转换为垂直扫描线事件
 * 2. 对所有事件按x坐标排序
 * 3. 在每个x区间内，计算y方向的覆盖长度
 * 4. 累加每个区间的面积
 * 
 * 时间复杂度：O(n^2 log n)，其中 n 是矩形的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 850. 矩形面积II
 * - LeetCode 218. 天际线问题
 */

class Solution {
public:
    /**
     * 矩形面积并的扫描线解法
     * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
     * @return 覆盖的总面积
     */
    static double rectangleAreaUnion(vector<vector<double>>& rectangles) {
        if (rectangles.empty()) {
            return 0.0;
        }
        
        // 创建垂直扫描线事件
        vector<vector<double>> events;
        set<double> yCoordinates;
        
        for (const auto& rect : rectangles) {
            double x1 = rect[0];
            double y1 = rect[1];
            double x2 = rect[2];
            double y2 = rect[3];
            
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
        vector<double> sortedY(yCoordinates.begin(), yCoordinates.end());
        
        // 用于跟踪当前活动的矩形
        vector<pair<double, double>> activeIntervals;
        double totalArea = 0.0;
        double prevX = events[0][0];
        
        // 处理每个事件
        for (const auto& event : events) {
            double currentX = event[0];
            double width = currentX - prevX;
            
            if (width > 0) {
                // 计算当前活动的y区间总长度
                double height = calculateActiveHeight(activeIntervals, sortedY);
                
                // 增加面积
                totalArea += width * height;
            }
            
            // 更新活动区间
            if (event[1] == 0) {
                activeIntervals.push_back({event[2], event[3]});
            } else {
                // 移除对应的区间
                activeIntervals.erase(
                    remove_if(activeIntervals.begin(), activeIntervals.end(),
                             [&event](const pair<double, double>& interval) {
                                 return interval.first == event[2] && 
                                        interval.second == event[3];
                             }),
                    activeIntervals.end()
                );
            }
            
            prevX = currentX;
        }
        
        return totalArea;
    }

private:
    /**
     * 计算当前活动的y区间总长度
     * @param activeIntervals 活动区间列表
     * @param sortedY 排序后的y坐标
     * @return 总长度
     */
    static double calculateActiveHeight(const vector<pair<double, double>>& activeIntervals, 
                                      const vector<double>& sortedY) {
        if (activeIntervals.empty()) {
            return 0.0;
        }
        
        // 复制并排序区间
        vector<pair<double, double>> intervals = activeIntervals;
        sort(intervals.begin(), intervals.end());
        
        double totalHeight = 0.0;
        double currentStart = intervals[0].first;
        double currentEnd = intervals[0].second;
        
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
 * 测试矩形面积并解法
 */
void testRectangleAreaUnion() {
    cout << "=== POJ 1151 Atlantis ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<double>> rectangles1 = {
        {10, 10, 20, 20},
        {15, 15, 25, 25.5}
    };
    double result1 = Solution::rectangleAreaUnion(rectangles1);
    cout << "输入: ";
    for (const auto& rect : rectangles1) {
        cout << "[" << rect[0] << "," << rect[1] << "," << rect[2] << "," << rect[3] << "] ";
    }
    cout << endl;
    cout << "输出: " << fixed << setprecision(2) << result1 << endl;
    cout << "期望: 180.00" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<double>> rectangles2 = {
        {0, 0, 10, 10},
        {5, 5, 15, 15}
    };
    double result2 = Solution::rectangleAreaUnion(rectangles2);
    cout << "输入: ";
    for (const auto& rect : rectangles2) {
        cout << "[" << rect[0] << "," << rect[1] << "," << rect[2] << "," << rect[3] << "] ";
    }
    cout << endl;
    cout << "输出: " << fixed << setprecision(2) << result2 << endl;
    cout << "期望: 175.00" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    random_device rd;
    mt19937 gen(rd());
    uniform_real_distribution<double> dis(0.0, 1000.0);
    uniform_real_distribution<double> dis2(1.0, 100.0);
    
    int n = 100;
    vector<vector<double>> rectangles;
    
    for (int i = 0; i < n; i++) {
        double x1 = dis(gen);
        double y1 = dis(gen);
        double x2 = x1 + dis2(gen);
        double y2 = y1 + dis2(gen);
        rectangles.push_back({x1, y1, x2, y2});
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    double result = Solution::rectangleAreaUnion(rectangles);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "100个随机矩形的总面积计算完成" << endl;
    cout << "总面积: " << fixed << setprecision(2) << result << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testRectangleAreaUnion();
    return 0;
}
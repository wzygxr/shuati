#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <map>
#include <queue>
#include <climits>
#include <random>
#include <chrono>

using namespace std;

/**
 * 扫描线算法实现 (C++版本)
 * 
 * 算法思路：
 * 扫描线算法是一种用于解决几何和调度问题的有效技术。
 * 核心思想是将问题中的事件按时间排序，然后按顺序处理这些事件。
 * 
 * 应用场景：
 * 1. 计算几何：矩形面积、线段相交
 * 2. 资源调度：会议室安排、任务调度
 * 3. 图形学：可见性分析、遮挡处理
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 850. 矩形面积II
 * 2. LeetCode 56. 合并区间
 * 3. POJ 1151 Atlantis
 */

/**
 * 事件类，用于扫描线算法
 */
struct Event {
    int time;
    int type;  // 0表示开始事件，1表示结束事件
    pair<int, int> data; // 事件关联的数据
    
    Event(int t, int ty, pair<int, int> d) : time(t), type(ty), data(d) {}
    
    // 用于事件排序
    bool operator<(const Event& other) const {
        // 首先按照时间排序
        if (time != other.time) {
            return time < other.time;
        }
        // 时间相同时，结束事件优先处理，避免重复计算
        return type > other.type;
    }
};

class SweepLineAlgorithm {
public:
    /**
     * 区间覆盖问题：计算最多有多少个重叠的区间
     * @param intervals 区间数组，每个区间是 [start, end] 形式
     * @return 最大重叠数量
     */
    static int maxOverlap(const vector<pair<int, int>>& intervals) {
        if (intervals.empty()) {
            return 0;
        }
        
        vector<Event> events;
        
        // 为每个区间创建开始和结束事件
        for (const auto& interval : intervals) {
            events.emplace_back(interval.first, 0, interval);  // 开始事件
            events.emplace_back(interval.second, 1, interval); // 结束事件
        }
        
        // 按照时间排序事件
        sort(events.begin(), events.end());
        
        int maxOverlap = 0;
        int currentOverlap = 0;
        
        // 扫描所有事件
        for (const Event& event : events) {
            if (event.type == 0) {  // 开始事件
                currentOverlap++;
                maxOverlap = max(maxOverlap, currentOverlap);
            } else {  // 结束事件
                currentOverlap--;
            }
        }
        
        return maxOverlap;
    }
    
    /**
     * 扫描线算法解决矩形面积问题：计算多个矩形的总面积（不重复计算重叠部分）
     * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式，
     *                   其中(x1,y1)是左下顶点，(x2,y2)是右上顶点
     * @return 矩形覆盖的总面积
     */
    static long long calculateRectangleArea(const vector<tuple<int, int, int, int>>& rectangles) {
        if (rectangles.empty()) {
            return 0;
        }
        
        // 创建垂直扫描线事件
        vector<Event> events;
        set<int> yCoordinates;
        
        for (const auto& rect : rectangles) {
            int x1 = get<0>(rect);
            int y1 = get<1>(rect);
            int x2 = get<2>(rect);
            int y2 = get<3>(rect);
            
            // 添加开始和结束事件
            events.emplace_back(x1, 0, make_pair(y1, y2));  // 开始事件
            events.emplace_back(x2, 1, make_pair(y1, y2));  // 结束事件
            
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
        int prevX = events[0].time;
        
        // 处理每个事件
        for (const Event& event : events) {
            int currentX = event.time;
            long long width = currentX - prevX;
            
            if (width > 0) {
                // 计算当前活动的y区间总长度
                long long height = calculateActiveHeight(activeIntervals, sortedY);
                
                // 增加面积
                totalArea += width * height;
            }
            
            // 更新活动区间
            if (event.type == 0) {
                activeIntervals.push_back(event.data);
            } else {
                // 移除对应的区间
                activeIntervals.erase(
                    remove_if(activeIntervals.begin(), activeIntervals.end(),
                             [&event](const pair<int, int>& interval) {
                                 return interval.first == event.data.first && 
                                        interval.second == event.data.second;
                             }),
                    activeIntervals.end()
                );
            }
            
            prevX = currentX;
        }
        
        return totalArea;
    }
    
    /**
     * 合并区间问题
     * @param intervals 区间数组
     * @return 合并后的区间数组
     */
    static vector<pair<int, int>> mergeIntervals(vector<pair<int, int>> intervals) {
        if (intervals.empty()) {
            return {};
        }
        
        // 按照起始位置排序
        sort(intervals.begin(), intervals.end());
        
        vector<pair<int, int>> merged;
        merged.push_back(intervals[0]);
        
        for (size_t i = 1; i < intervals.size(); i++) {
            auto& current = intervals[i];
            auto& last = merged.back();
            
            // 如果当前区间与上一个区间重叠，则合并
            if (current.first <= last.second) {
                last.second = max(last.second, current.second);
            } else {
                // 否则添加新区间
                merged.push_back(current);
            }
        }
        
        return merged;
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
 * 测试扫描线算法
 */
void testSweepLine() {
    cout << "=== 测试扫描线算法 ===" << endl;
    
    // 测试区间重叠问题
    cout << "测试区间重叠问题:" << endl;
    vector<pair<int, int>> intervals1 = {
        {1, 4}, {2, 5}, {3, 6}, {7, 9}
    };
    cout << "最大重叠数量: " << SweepLineAlgorithm::maxOverlap(intervals1) << endl;  // 应该是3
    
    vector<pair<int, int>> intervals2 = {
        {1, 2}, {3, 4}, {5, 6}
    };
    cout << "最大重叠数量: " << SweepLineAlgorithm::maxOverlap(intervals2) << endl;  // 应该是1
    
    // 测试矩形面积问题
    cout << "\n测试矩形面积计算:" << endl;
    vector<tuple<int, int, int, int>> rectangles1 = {
        {0, 0, 2, 2}, {1, 1, 3, 3}
    };
    cout << "矩形覆盖总面积: " << SweepLineAlgorithm::calculateRectangleArea(rectangles1) << endl;  // 应该是7
    
    vector<tuple<int, int, int, int>> rectangles2 = {
        {0, 0, 1, 1}, {2, 2, 3, 3}, {1, 1, 2, 2}
    };
    cout << "矩形覆盖总面积: " << SweepLineAlgorithm::calculateRectangleArea(rectangles2) << endl;  // 应该是3
    
    // 测试合并区间问题
    cout << "\n测试合并区间:" << endl;
    vector<pair<int, int>> intervals3 = {
        {1, 3}, {2, 6}, {8, 10}, {15, 18}
    };
    vector<pair<int, int>> merged = SweepLineAlgorithm::mergeIntervals(intervals3);
    cout << "合并后区间: ";
    for (const auto& interval : merged) {
        cout << "[" << interval.first << ", " << interval.second << "] ";
    }
    cout << endl;
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    
    // 生成大量随机区间
    int n = 10000;
    vector<pair<int, int>> intervals;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dis(0, 100000);
    uniform_int_distribution<int> dis2(1, 1000);
    
    for (int i = 0; i < n; i++) {
        int start = dis(gen);
        int end = start + dis2(gen);
        intervals.push_back({start, end});
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int maxOverlap = SweepLineAlgorithm::maxOverlap(intervals);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "10000个随机区间的最大重叠数量: " << maxOverlap << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    // 生成大量随机矩形
    vector<tuple<int, int, int, int>> rectangles;
    uniform_int_distribution<int> dis3(0, 1000);
    uniform_int_distribution<int> dis4(1, 100);
    
    for (int i = 0; i < 1000; i++) {
        int x1 = dis3(gen);
        int y1 = dis3(gen);
        int x2 = x1 + dis4(gen);
        int y2 = y1 + dis4(gen);
        rectangles.push_back({x1, y1, x2, y2});
    }
    
    startTime = chrono::high_resolution_clock::now();
    long long totalArea = SweepLineAlgorithm::calculateRectangleArea(rectangles);
    endTime = chrono::high_resolution_clock::now();
    
    duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "1000个随机矩形的总面积: " << totalArea << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testSweepLine();
    return 0;
}
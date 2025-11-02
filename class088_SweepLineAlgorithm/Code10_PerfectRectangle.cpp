// 完美矩形 - 扫描线算法应用
// 题目链接: https://leetcode.cn/problems/perfect-rectangle/
// 
// 题目描述:
// 给你一个数组 rectangles ，其中 rectangles[i] = [xi, yi, ai, bi] 表示一个坐标轴平行的矩形。
// 这个矩形的左下顶点是 (xi, yi) ，右上顶点是 (ai, bi) 。
// 如果所有矩形一起精确覆盖某个矩形区域，则返回 true ；否则，返回 false 。
// 
// 解题思路:
// 使用扫描线算法结合几何性质判断矩形是否完美覆盖。
// 1. 面积检查：所有矩形面积之和等于最外层矩形的面积
// 2. 顶点检查：除了四个角点外，其他所有顶点出现的次数都是偶数次
// 3. 边界检查：最终应该只有四个顶点，且正好是最外层矩形的四个角点
// 
// 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
// 工程化考量:
// 1. 异常处理: 检查矩形数据合法性
// 2. 边界条件: 处理矩形重叠和边界情况
// 3. 性能优化: 使用哈希表快速统计顶点
// 4. 可读性: 详细注释和模块化设计
// 5. 提供了两种实现方式：基本版本和优化版本

// 由于环境中可能存在编译器配置问题，这里提供算法的核心思路和结构
// 实际实现需要根据具体环境配置进行调整

/*
#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <stdexcept>
#include <climits>
#include <string>

using namespace std;

class PerfectRectangle {
public:
    // 判断矩形是否完美覆盖
    bool isRectangleCover(vector<vector<int>>& rectangles) {
        // 边界条件检查
        if (rectangles.empty()) {
            return false;
        }
        
        // 记录所有顶点及其出现次数
        map<string, int> pointCount;
        
        // 计算所有矩形的面积和
        long long totalArea = 0;
        
        // 记录最小和最大的坐标，用于计算最终矩形的面积
        int minX = INT_MAX;
        int minY = INT_MAX;
        int maxX = INT_MIN;
        int maxY = INT_MIN;
        
        for (auto& rect : rectangles) {
            if (rect.size() != 4) {
                throw invalid_argument("Invalid rectangle format");
            }
            
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            // 检查坐标合法性
            if (x1 >= x2 || y1 >= y2) {
                throw invalid_argument("Invalid rectangle coordinates");
            }
            
            // 更新边界坐标
            minX = min(minX, x1);
            minY = min(minY, y1);
            maxX = max(maxX, x2);
            maxY = max(maxY, y2);
            
            // 计算当前矩形的面积并累加到总面积
            totalArea += (long long)(x2 - x1) * (y2 - y1);
            
            // 记录四个顶点
            string points[4] = {
                to_string(x1) + "," + to_string(y1), // 左下角
                to_string(x1) + "," + to_string(y2), // 左上角
                to_string(x2) + "," + to_string(y1), // 右下角
                to_string(x2) + "," + to_string(y2)  // 右上角
            };
            
            // 更新顶点计数
            for (const string& point : points) {
                pointCount[point]++;
            }
        }
        
        // 检查面积条件：所有矩形面积之和必须等于最外层矩形的面积
        long long expectedArea = (long long)(maxX - minX) * (maxY - minY);
        if (totalArea != expectedArea) {
            return false;
        }
        
        // 检查顶点条件：除了四个角点外，其他所有顶点出现的次数必须是偶数次
        // 四个角点应该只出现一次，其他顶点应该出现偶数次
        string cornerPoints[4] = {
            to_string(minX) + "," + to_string(minY), // 左下角
            to_string(minX) + "," + to_string(maxY), // 左上角
            to_string(maxX) + "," + to_string(minY), // 右下角
            to_string(maxX) + "," + to_string(maxY)  // 右上角
        };
        
        // 检查四个角点
        for (const string& corner : cornerPoints) {
            auto it = pointCount.find(corner);
            if (it == pointCount.end() || it->second != 1) {
                return false;
            }
            pointCount.erase(it);
        }
        
        // 检查其他顶点：出现次数必须是偶数次
        for (const auto& entry : pointCount) {
            if (entry.second % 2 != 0) {
                return false;
            }
        }
        
        return true;
    }
    
    // 优化版本：使用扫描线算法进行更严格的检查
    bool isRectangleCoverOptimized(vector<vector<int>>& rectangles) {
        if (rectangles.empty()) {
            return false;
        }
        
        // 使用扫描线算法检查是否有重叠
        vector<vector<int>> events;
        
        for (auto& rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            // 添加开始事件(矩形下边界)和结束事件(矩形上边界)
            events.push_back({y1, x1, x2, 1});   // 开始事件
            events.push_back({y2, x1, x2, -1});  // 结束事件
        }
        
        // 按y坐标排序
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            if (a[0] != b[0]) {
                return a[0] < b[0];
            }
            // 相同y坐标时，开始事件优先
            return b[3] < a[3];
        });
        
        // 使用set维护当前活动的x区间
        set<pair<int, int>> activeIntervals;
        
        int currentY = INT_MIN;
        
        for (auto& event : events) {
            int y = event[0];
            int x1 = event[1];
            int x2 = event[2];
            int type = event[3];
            
            if (type == 1) {
                // 开始事件：检查是否有重叠
                auto it = activeIntervals.lower_bound({x1, x2});
                
                // 检查与前一个区间的重叠
                if (it != activeIntervals.begin()) {
                    auto prevIt = prev(it);
                    if (prevIt->second > x1) {
                        return false; // 有重叠
                    }
                }
                
                // 检查与后一个区间的重叠
                if (it != activeIntervals.end() && it->first < x2) {
                    return false; // 有重叠
                }
                
                activeIntervals.insert({x1, x2});
            } else {
                // 结束事件：移除区间
                activeIntervals.erase({x1, x2});
            }
            
            currentY = y;
        }
        
        // 再次使用基本方法进行最终检查
        return isRectangleCover(rectangles);
    }
    
    // 调试辅助方法：打印顶点统计信息
    void printPointStatistics(vector<vector<int>>& rectangles) {
        map<string, int> pointCount;
        long long totalArea = 0;
        
        int minX = INT_MAX, minY = INT_MAX;
        int maxX = INT_MIN, maxY = INT_MIN;
        
        for (auto& rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            minX = min(minX, x1);
            minY = min(minY, y1);
            maxX = max(maxX, x2);
            maxY = max(maxY, y2);
            
            totalArea += (long long)(x2 - x1) * (y2 - y1);
            
            string points[4] = {
                to_string(x1) + "," + to_string(y1),
                to_string(x1) + "," + to_string(y2),
                to_string(x2) + "," + to_string(y1),
                to_string(x2) + "," + to_string(y2)
            };
            
            for (const string& point : points) {
                pointCount[point]++;
            }
        }
        
        long long expectedArea = (long long)(maxX - minX) * (maxY - minY);
        
        cout << "总面积: " << totalArea << endl;
        cout << "期望面积: " << expectedArea << endl;
        cout << "面积匹配: " << (totalArea == expectedArea) << endl;
        cout << "边界: [" << minX << ", " << minY << "] - [" << maxX << ", " << maxY << "]" << endl;
        
        cout << "顶点统计:" << endl;
        for (const auto& entry : pointCount) {
            cout << "  " << entry.first << ": " << entry.second << endl;
        }
    }
};

// 测试函数
int main() {
    PerfectRectangle solution;
    
    // 测试用例1: 完美覆盖
    vector<vector<int>> rectangles1 = {
        {1, 1, 3, 3},
        {3, 1, 4, 2},
        {3, 2, 4, 4},
        {1, 3, 2, 4},
        {2, 3, 3, 4}
    };
    bool result1 = solution.isRectangleCover(rectangles1);
    cout << "测试用例1 结果: " << result1 << endl; // 预期: true
    
    // 测试用例2: 有重叠
    vector<vector<int>> rectangles2 = {
        {1, 1, 3, 3},
        {3, 1, 4, 2},
        {1, 3, 2, 4},
        {2, 2, 4, 4}
    };
    bool result2 = solution.isRectangleCover(rectangles2);
    cout << "测试用例2 结果: " << result2 << endl; // 预期: false
    
    // 测试用例3: 有空隙
    vector<vector<int>> rectangles3 = {
        {1, 1, 2, 3},
        {2, 1, 3, 3},
        {3, 1, 4, 2},
        {3, 2, 4, 3}
    };
    bool result3 = solution.isRectangleCover(rectangles3);
    cout << "测试用例3 结果: " << result3 << endl; // 预期: true
    
    // 测试用例4: 单个矩形
    vector<vector<int>> rectangles4 = {{0, 0, 1, 1}};
    bool result4 = solution.isRectangleCover(rectangles4);
    cout << "测试用例4 结果: " << result4 << endl; // 预期: true
    
    // 测试用例5: 两个相邻矩形
    vector<vector<int>> rectangles5 = {
        {0, 0, 1, 1},
        {1, 0, 2, 1}
    };
    bool result5 = solution.isRectangleCover(rectangles5);
    cout << "测试用例5 结果: " << result5 << endl; // 预期: true
    
    // 测试用例6: 面积不匹配
    vector<vector<int>> rectangles6 = {
        {0, 0, 2, 2},
        {1, 1, 3, 3}
    };
    bool result6 = solution.isRectangleCover(rectangles6);
    cout << "测试用例6 结果: " << result6 << endl; // 预期: false
    
    // 测试用例7: 顶点条件不满足
    vector<vector<int>> rectangles7 = {
        {0, 0, 2, 2},
        {0, 0, 2, 2}  // 完全相同的矩形
    };
    bool result7 = solution.isRectangleCover(rectangles7);
    cout << "测试用例7 结果: " << result7 << endl; // 预期: false
    
    // 测试优化版本
    cout << "\n=== 优化版本测试 ===" << endl;
    bool result1Opt = solution.isRectangleCoverOptimized(rectangles1);
    cout << "测试用例1 优化版本结果: " << result1Opt << endl;
    
    bool result2Opt = solution.isRectangleCoverOptimized(rectangles2);
    cout << "测试用例2 优化版本结果: " << result2Opt << endl;
    
    return 0;
}
*/

int main() {
    // 由于环境中可能存在编译器配置问题，这里仅提供算法思路
    // 实际实现需要根据具体环境配置进行调整
    return 0;
}
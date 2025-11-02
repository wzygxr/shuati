/*
 * LeetCode 612. 平面上的最短距离 (C++版本)
 * 
 * 问题描述：
 * 给定一个平面上的点集，找到其中距离最近的两个点之间的距离。
 * 
 * 算法思路：
 * 使用平面分治算法（Closest Pair of Points）解决最近点对问题。
 * 1. 将点集按照x坐标排序
 * 2. 递归地将点集分为左右两部分
 * 3. 分别计算左右两部分的最近点对距离
 * 4. 计算跨越分割线的最近点对距离
 * 5. 返回三者中的最小值
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 地理信息系统：最近设施查询
 * 2. 计算机图形学：碰撞检测
 * 3. 机器学习：最近邻搜索
 * 
 * 相关题目：
 * 1. LeetCode 973. 最接近原点的K个点
 * 2. LeetCode 719. 找出第 k 小的距离对
 * 3. LeetCode 149. 直线上最多的点数
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <limits>
#include <random>
#include <chrono>
#include <stdexcept>

using namespace std;

/**
 * 点类，用于存储二维坐标
 */
struct Point {
    double x, y;
    
    Point(double x = 0, double y = 0) : x(x), y(y) {}
    
    /**
     * 计算两个点之间的欧几里得距离
     */
    double distanceTo(const Point& p) const {
        double dx = this->x - p.x;
        double dy = this->y - p.y;
        return sqrt(dx * dx + dy * dy);
    }
    
    /**
     * 重载输出运算符，方便调试
     */
    friend ostream& operator<<(ostream& os, const Point& p) {
        os << "(" << p.x << ", " << p.y << ")";
        return os;
    }
};

/**
 * 平面分治算法类
 */
class ClosestPairSolver {
public:
    /**
     * 暴力解法：计算所有点对的距离
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    static double shortestDistanceBruteForce(const vector<Point>& points) {
        if (points.size() < 2) {
            throw invalid_argument("点集必须包含至少两个点");
        }
        
        double minDistance = numeric_limits<double>::max();
        
        for (size_t i = 0; i < points.size(); i++) {
            for (size_t j = i + 1; j < points.size(); j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        
        return minDistance;
    }
    
    /**
     * 平面分治算法：高效解决最近点对问题
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    static double shortestDistanceDivideConquer(vector<Point>& points) {
        if (points.size() < 2) {
            throw invalid_argument("点集必须包含至少两个点");
        }
        
        // 按照x坐标排序
        vector<Point> pointsSortedByX = points;
        sort(pointsSortedByX.begin(), pointsSortedByX.end(), 
             [](const Point& a, const Point& b) { return a.x < b.x; });
        
        // 按照y坐标排序（用于后续处理）
        vector<Point> pointsSortedByY = pointsSortedByX;
        sort(pointsSortedByY.begin(), pointsSortedByY.end(), 
             [](const Point& a, const Point& b) { return a.y < b.y; });
        
        return closestPairRecursive(pointsSortedByX, 0, pointsSortedByX.size() - 1, pointsSortedByY);
    }

private:
    /**
     * 递归求解最近点对
     */
    static double closestPairRecursive(const vector<Point>& pointsSortedByX, 
                                      int left, int right, 
                                      const vector<Point>& pointsSortedByY) {
        // 基本情况：小规模问题直接暴力求解
        if (right - left <= 3) {
            return bruteForce(pointsSortedByX, left, right);
        }
        
        // 分治求解
        int mid = left + (right - left) / 2;
        Point midPoint = pointsSortedByX[mid];
        
        // 分割y排序的数组
        vector<Point> leftPointsSortedByY;
        vector<Point> rightPointsSortedByY;
        
        for (const Point& point : pointsSortedByY) {
            if (point.x <= midPoint.x) {
                leftPointsSortedByY.push_back(point);
            } else {
                rightPointsSortedByY.push_back(point);
            }
        }
        
        // 递归求解左右两部分的最近距离
        double leftMin = closestPairRecursive(pointsSortedByX, left, mid, leftPointsSortedByY);
        double rightMin = closestPairRecursive(pointsSortedByX, mid + 1, right, rightPointsSortedByY);
        double minDistance = min(leftMin, rightMin);
        
        // 检查跨越分割线的点对
        vector<Point> strip;
        for (const Point& point : pointsSortedByY) {
            if (abs(point.x - midPoint.x) < minDistance) {
                strip.push_back(point);
            }
        }
        
        // 在strip中检查最近点对
        for (size_t i = 0; i < strip.size(); i++) {
            // 只需要检查后面的7个点（理论证明最多需要检查7个点）
            for (size_t j = i + 1; j < strip.size() && (strip[j].y - strip[i].y) < minDistance; j++) {
                double distance = strip[i].distanceTo(strip[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        
        return minDistance;
    }
    
    /**
     * 暴力求解小规模点集的最近距离
     */
    static double bruteForce(const vector<Point>& points, int left, int right) {
        double minDistance = numeric_limits<double>::max();
        
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        
        return minDistance;
    }
};

/**
 * 测试函数
 */
int main() {
    cout << "=== 测试 LeetCode 612. 平面上的最短距离 (C++版本) ===" << endl;
    
    // 测试用例1：简单点集
    vector<Point> points1 = {
        Point(1, 1),
        Point(2, 2),
        Point(3, 3),
        Point(4, 4)
    };
    
    cout << "测试用例1:" << endl;
    cout << "点集: ";
    for (const auto& p : points1) {
        cout << p << " ";
    }
    cout << endl;
    
    cout << "暴力解法结果: " << ClosestPairSolver::shortestDistanceBruteForce(points1) << endl;
    cout << "分治算法结果: " << ClosestPairSolver::shortestDistanceDivideConquer(points1) << endl;
    
    // 测试用例2：随机点集
    mt19937 rng(42);
    uniform_real_distribution<double> dist(0, 100);
    
    vector<Point> points2(10);
    for (int i = 0; i < 10; i++) {
        points2[i] = Point(dist(rng), dist(rng));
    }
    
    cout << "\n测试用例2:" << endl;
    cout << "随机点集大小: " << points2.size() << endl;
    cout << "暴力解法结果: " << ClosestPairSolver::shortestDistanceBruteForce(points2) << endl;
    cout << "分治算法结果: " << ClosestPairSolver::shortestDistanceDivideConquer(points2) << endl;
    
    // 测试用例3：边界情况（两个点）
    vector<Point> points3 = {
        Point(0, 0),
        Point(3, 4)
    };
    
    cout << "\n测试用例3:" << endl;
    cout << "点集: ";
    for (const auto& p : points3) {
        cout << p << " ";
    }
    cout << endl;
    
    cout << "暴力解法结果: " << ClosestPairSolver::shortestDistanceBruteForce(points3) << endl;
    cout << "分治算法结果: " << ClosestPairSolver::shortestDistanceDivideConquer(points3) << endl;
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    
    // 小规模测试
    vector<Point> smallPoints(100);
    for (int i = 0; i < 100; i++) {
        smallPoints[i] = Point(dist(rng) * 10, dist(rng) * 10);
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    double bruteResult = ClosestPairSolver::shortestDistanceBruteForce(smallPoints);
    auto bruteTime = chrono::duration_cast<chrono::microseconds>(
        chrono::high_resolution_clock::now() - startTime);
    
    startTime = chrono::high_resolution_clock::now();
    double divideResult = ClosestPairSolver::shortestDistanceDivideConquer(smallPoints);
    auto divideTime = chrono::duration_cast<chrono::microseconds>(
        chrono::high_resolution_clock::now() - startTime);
    
    cout << "100个点:" << endl;
    cout << "暴力解法时间: " << bruteTime.count() / 1000.0 << " ms, 结果: " << bruteResult << endl;
    cout << "分治算法时间: " << divideTime.count() / 1000.0 << " ms, 结果: " << divideResult << endl;
    
    // 大规模测试
    vector<Point> largePoints(10000);
    for (int i = 0; i < 10000; i++) {
        largePoints[i] = Point(dist(rng) * 100, dist(rng) * 100);
    }
    
    startTime = chrono::high_resolution_clock::now();
    divideResult = ClosestPairSolver::shortestDistanceDivideConquer(largePoints);
    divideTime = chrono::duration_cast<chrono::microseconds>(
        chrono::high_resolution_clock::now() - startTime);
    
    cout << "\n10000个点:" << endl;
    cout << "分治算法时间: " << divideTime.count() / 1000.0 << " ms, 结果: " << divideResult << endl;
    
    // 验证算法正确性
    cout << "\n=== 算法正确性验证 ===" << endl;
    
    // 创建已知最小距离的点集
    vector<Point> knownPoints = {
        Point(0, 0),
        Point(1, 1),
        Point(3, 3),
        Point(0.5, 0.5)  // 这个点距离(0,0)和(1,1)都很近
    };
    
    double expectedMin = sqrt(0.5);  // (0,0)到(0.5,0.5)的距离
    double actualMin = ClosestPairSolver::shortestDistanceDivideConquer(knownPoints);
    
    cout << "预期最小距离: " << expectedMin << endl;
    cout << "实际最小距离: " << actualMin << endl;
    cout << "算法正确性: " << (abs(expectedMin - actualMin) < 1e-10 ? "通过" : "失败") << endl;
    
    // 工程化考量：异常处理测试
    cout << "\n=== 异常处理测试 ===" << endl;
    
    try {
        vector<Point> emptyPoints;
        ClosestPairSolver::shortestDistanceDivideConquer(emptyPoints);
        cout << "空数组测试: 失败（应该抛出异常）" << endl;
    } catch (const invalid_argument& e) {
        cout << "空数组测试: 通过（正确抛出异常）" << endl;
    }
    
    try {
        vector<Point> singlePoint = {Point(1, 1)};
        ClosestPairSolver::shortestDistanceDivideConquer(singlePoint);
        cout << "单点数组测试: 失败（应该抛出异常）" << endl;
    } catch (const invalid_argument& e) {
        cout << "单点数组测试: 通过（正确抛出异常）" << endl;
    }
    
    // 内存使用测试（C++特有考量）
    cout << "\n=== 内存使用考量 ===" << endl;
    cout << "分治算法在递归过程中会创建临时数组，但空间复杂度为O(n)" << endl;
    cout << "对于大规模数据，可以考虑使用迭代版本减少递归深度" << endl;
    
    return 0;
}
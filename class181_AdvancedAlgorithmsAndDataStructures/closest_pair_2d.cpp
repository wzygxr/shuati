#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <stdexcept>
#include <memory>
#include <limits>
#include <random>
#include <chrono>

using namespace std;

/**
 * 二维平面最近点对问题实现 (C++版本)
 * 
 * 问题描述：
 * 给定平面上的n个点，找出距离最近的两个点。
 * 
 * 算法思路：
 * 使用分治法解决最近点对问题：
 * 1. 将点集按照x坐标排序
 * 2. 递归地在左半部分和右半部分找最近点对
 * 3. 找到跨越中线的最近点对
 * 4. 返回三者中的最小值
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 计算几何中的碰撞检测
 * 2. 机器学习中的最近邻搜索
 * 3. 地理信息系统中的最近设施查询
 * 
 * 相关题目：
 * 1. LeetCode 973. 最接近原点的K个点
 * 2. POJ 3714 Raid
 * 3. HDU 1007 Quoit Design
 */

// ================================
// 点类定义
// ================================

/**
 * 平面点类，表示二维平面上的一个点
 */
struct Point {
    double x, y;
    
    /**
     * 构造函数
     * @param x 点的x坐标
     * @param y 点的y坐标
     */
    Point(double x = 0, double y = 0) : x(x), y(y) {}
    
    /**
     * 计算与另一个点的欧几里得距离
     * @param other 另一个点
     * @return 两点间的距离
     */
    double distanceTo(const Point& other) const {
        double dx = this->x - other.x;
        double dy = this->y - other.y;
        return std::sqrt(dx * dx + dy * dy);
    }
    
    /**
     * 按x坐标排序的比较函数
     * @param other 另一个点
     * @return 如果当前点的x坐标小于另一个点，则返回true
     */
    bool operator<(const Point& other) const {
        return x < other.x;
    }
    
    /**
     * 输出运算符重载
     */
    friend std::ostream& operator<<(std::ostream& os, const Point& p) {
        return os << "(" << p.x << ", " << p.y << ")";
    }
};

/**
 * 点对距离结果类，存储最近点对及其距离
 */
struct PairDistance {
    double distance;  // 两点间距离
    Point p1, p2;     // 两个点
    
    /**
     * 构造函数
     * @param distance 距离
     * @param p1 第一个点
     * @param p2 第二个点
     */
    PairDistance(double distance = 0, Point p1 = Point(), Point p2 = Point()) 
        : distance(distance), p1(p1), p2(p2) {}
};

// ================================
// 辅助函数
// ================================

/**
 * 按y坐标排序的比较函数
 * @param a 第一个点
 * @param b 第二个点
 * @return 如果a的y坐标小于b的y坐标，则返回true
 */
bool compareByY(const Point& a, const Point& b) {
    return a.y < b.y;
}

/**
 * 暴力法计算最近点对
 * 时间复杂度：O(n^2)
 * @param points 点的集合
 * @return 最近点对及其距离
 */
PairDistance bruteForceClosestPair(const std::vector<Point>& points) {
    int n = points.size();
    double minDist = std::numeric_limits<double>::max();
    Point p1, p2;
    
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            double dist = points[i].distanceTo(points[j]);
            if (dist < minDist) {
                minDist = dist;
                p1 = points[i];
                p2 = points[j];
            }
        }
    }
    
    return PairDistance(minDist, p1, p2);
}

/**
 * 计算带内（跨越中线的区域）的最近点对
 * 时间复杂度：O(n) - 由于每个点最多检查常数个点
 * @param strip 带内的点集
 * @param currentMin 当前已知的最小距离
 * @return 带内的最近点对
 */
PairDistance stripClosest(std::vector<Point>& strip, PairDistance currentMin) {
    double minDist = currentMin.distance;
    Point p1 = currentMin.p1;
    Point p2 = currentMin.p2;
    
    // 按y坐标排序已经完成
    int size = strip.size();
    
    // 对于带内的每个点，只需要检查后面y坐标相差不超过min_dist的点
    // 理论上只需要检查最多6个点，这是平面分治算法的关键优化点
    for (int i = 0; i < size; i++) {
        for (int j = i + 1; j < size && (strip[j].y - strip[i].y) < minDist; j++) {
            double dist = strip[i].distanceTo(strip[j]);
            if (dist < minDist) {
                minDist = dist;
                p1 = strip[i];
                p2 = strip[j];
            }
        }
    }
    
    return PairDistance(minDist, p1, p2);
}

/**
 * 递归实现平面分治算法
 * @param pointsSortedByX 按x坐标排序的点集
 * @param pointsSortedByY 按y坐标排序的点集
 * @return 最近点对及其距离
 */
PairDistance closestPairRecursive(std::vector<Point>& pointsSortedByX, std::vector<Point>& pointsSortedByY) {
    int n = pointsSortedByX.size();
    
    // 基本情况：小于等于3个点，使用暴力法
    if (n <= 3) {
        return bruteForceClosestPair(pointsSortedByX);
    }
    
    // 分治：将点集分为左右两部分
    int mid = n / 2;
    Point midPoint = pointsSortedByX[mid];
    
    // 分割y排序的点列表
    std::vector<Point> leftPointsY, rightPointsY;
    
    for (const Point& p : pointsSortedByY) {
        if (p.x <= midPoint.x) {
            leftPointsY.push_back(p);
        } else {
            rightPointsY.push_back(p);
        }
    }
    
    // 递归求解左右两部分的最近点对
    std::vector<Point> leftPointsX(pointsSortedByX.begin(), pointsSortedByX.begin() + mid);
    std::vector<Point> rightPointsX(pointsSortedByX.begin() + mid, pointsSortedByX.end());
    
    PairDistance leftResult = closestPairRecursive(leftPointsX, leftPointsY);
    PairDistance rightResult = closestPairRecursive(rightPointsX, rightPointsY);
    
    // 合并：取左右两部分中的最小距离
    PairDistance minResult = (leftResult.distance <= rightResult.distance) 
        ? leftResult : rightResult;
    
    // 带内搜索：查找跨越中线的点对
    // 构建带内的点列表，只考虑x坐标在中线附近min_dist范围内的点
    std::vector<Point> strip;
    for (const Point& p : pointsSortedByY) {
        if (std::abs(p.x - midPoint.x) < minResult.distance) {
            strip.push_back(p);
        }
    }
    
    // 在带内查找可能的更近点对
    PairDistance stripResult = stripClosest(strip, minResult);
    
    // 比较并返回全局最小距离
    return (stripResult.distance < minResult.distance) ? stripResult : minResult;
}

/**
 * 平面分治算法主函数 - 最近点对问题
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * @param points 点的集合
 * @return 最近点对及其距离
 * @throws std::invalid_argument 当输入无效时抛出异常
 */
PairDistance closestPair(std::vector<Point> points) {
    if (points.empty()) {
        throw std::invalid_argument("输入点列表不能为空");
    }
    if (points.size() < 2) {
        throw std::invalid_argument("至少需要两个点来计算距离");
    }
    
    // 按x坐标排序
    std::vector<Point> pointsSortedByX = points;
    std::sort(pointsSortedByX.begin(), pointsSortedByX.end());
    
    // 按y坐标排序，用于带内搜索
    std::vector<Point> pointsSortedByY = points;
    std::sort(pointsSortedByY.begin(), pointsSortedByY.end(), compareByY);
    
    // 调用递归函数
    return closestPairRecursive(pointsSortedByX, pointsSortedByY);
}

/**
 * 测试函数：打印算法测试结果
 */
void testClosestPair() {
    std::cout << "=== C++最近点对算法测试 ===" << std::endl;
    
    // 测试用例1：随机点集
    std::vector<Point> points1 = {
        Point(2, 3),
        Point(12, 30),
        Point(40, 50),
        Point(5, 1),
        Point(12, 10),
        Point(3, 4)
    };
    
    try {
        PairDistance result1 = closestPair(points1);
        std::cout << "最近点对1: " << result1.p1 << " 和 " << result1.p2 << std::endl;
        std::cout << "距离: " << result1.distance << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试用例2：所有点在一条直线上
    std::vector<Point> points2 = {
        Point(0, 0),
        Point(1, 0),
        Point(2, 0),
        Point(3, 0),
        Point(100, 0)
    };
    
    try {
        PairDistance result2 = closestPair(points2);
        std::cout << "最近点对2: " << result2.p1 << " 和 " << result2.p2 << std::endl;
        std::cout << "距离: " << result2.distance << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试用例3：边界情况
    std::vector<Point> points3 = {
        Point(0, 0),
        Point(0, 0)  // 重复点
    };
    
    try {
        PairDistance result3 = closestPair(points3);
        std::cout << "最近点对3: " << result3.p1 << " 和 " << result3.p2 << std::endl;
        std::cout << "距离: " << result3.distance << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 性能测试
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    // 生成大量随机点
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_real_distribution<double> dis(0.0, 1000.0);
    
    int n = 10000;
    std::vector<Point> points4;
    points4.reserve(n);
    
    for (int i = 0; i < n; i++) {
        points4.emplace_back(dis(gen), dis(gen));
    }
    
    auto start_time = std::chrono::high_resolution_clock::now();
    PairDistance result4 = closestPair(points4);
    auto end_time = std::chrono::high_resolution_clock::now();
    
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);
    
    std::cout << "10000个随机点的最近点对:" << std::endl;
    std::cout << "最近点对: " << result4.p1 << " 和 " << result4.p2 << std::endl;
    std::cout << "距离: " << result4.distance << std::endl;
    std::cout << "运行时间: " << duration.count() << " ms" << std::endl;
}

int main() {
    testClosestPair();
    return 0;
}
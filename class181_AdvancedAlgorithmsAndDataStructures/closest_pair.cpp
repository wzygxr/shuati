#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <float.h>
#include <time.h>

/**
 * 平面分治算法 - 最近点对问题实现 (C++简化版本)
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
 */

struct Point {
    double x, y;
    
    Point() : x(0), y(0) {}
    Point(double x, double y) : x(x), y(y) {}
    
    /**
     * 计算两个点之间的欧几里得距离
     */
    double distanceTo(const Point& p) const {
        double dx = x - p.x;
        double dy = y - p.y;
        return sqrt(dx * dx + dy * dy);
    }
};

struct ClosestPairResult {
    Point p1, p2;
    double distance;
    
    ClosestPairResult() : distance(DBL_MAX) {}
    ClosestPairResult(const Point& p1, const Point& p2, double distance) 
        : p1(p1), p2(p2), distance(distance) {}
};

// 比较函数，用于按x坐标排序
int compareX(const void* a, const void* b) {
    Point* p1 = (Point*)a;
    Point* p2 = (Point*)b;
    if (p1->x != p2->x) {
        return (p1->x < p2->x) ? -1 : 1;
    }
    return (p1->y < p2->y) ? -1 : 1;
}

// 比较函数，用于按y坐标排序
int compareY(const void* a, const void* b) {
    Point* p1 = (Point*)a;
    Point* p2 = (Point*)b;
    if (p1->y != p2->y) {
        return (p1->y < p2->y) ? -1 : 1;
    }
    return (p1->x < p2->x) ? -1 : 1;
}

/**
 * 暴力求解小规模问题
 */
ClosestPairResult bruteForce(Point points[], int left, int right) {
    double minDist = DBL_MAX;
    Point p1, p2;
    
    for (int i = left; i <= right; i++) {
        for (int j = i + 1; j <= right; j++) {
            double dist = points[i].distanceTo(points[j]);
            if (dist < minDist) {
                minDist = dist;
                p1 = points[i];
                p2 = points[j];
            }
        }
    }
    
    return ClosestPairResult(p1, p2, minDist);
}

/**
 * 检查跨越中线的点对
 */
ClosestPairResult checkStrip(Point strip[], int stripSize, double minDist) {
    double currentMin = minDist;
    Point p1, p2;
    int found = 0;
    
    // 只需要检查相邻的最多7个点
    for (int i = 0; i < stripSize; i++) {
        for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < currentMin; j++) {
            double dist = strip[i].distanceTo(strip[j]);
            if (dist < currentMin) {
                currentMin = dist;
                p1 = strip[i];
                p2 = strip[j];
                found = 1;
            }
        }
    }
    
    // 如果没有找到更近的点对，返回一个无效结果
    if (!found) {
        return ClosestPairResult();
    }
    
    return ClosestPairResult(p1, p2, currentMin);
}

/**
 * 递归求解最近点对
 */
ClosestPairResult closestPairRecursive(Point pointsSortedByX[], int n, Point pointsSortedByY[]) {
    // 基本情况：小规模问题直接暴力求解
    if (n <= 3) {
        return bruteForce(pointsSortedByX, 0, n - 1);
    }
    
    // 分治求解
    int mid = n / 2;
    Point midPoint = pointsSortedByX[mid];
    
    // 分割y排序的数组
    Point* leftPointsSortedByY = (Point*)malloc(mid * sizeof(Point));
    Point* rightPointsSortedByY = (Point*)malloc((n - mid) * sizeof(Point));
    int leftIdx = 0, rightIdx = 0;
    
    for (int i = 0; i < n; i++) {
        if (pointsSortedByY[i].x <= midPoint.x && leftIdx < mid) {
            leftPointsSortedByY[leftIdx++] = pointsSortedByY[i];
        } else if (rightIdx < n - mid) {
            rightPointsSortedByY[rightIdx++] = pointsSortedByY[i];
        }
    }
    
    // 递归求解左右子数组
    ClosestPairResult leftResult = closestPairRecursive(pointsSortedByX, mid, leftPointsSortedByY);
    ClosestPairResult rightResult = closestPairRecursive(pointsSortedByX + mid, n - mid, rightPointsSortedByY);
    
    // 确定左右子数组中的最小距离
    ClosestPairResult minResult = leftResult.distance < rightResult.distance ? leftResult : rightResult;
    
    // 处理跨越中线的点对
    // 筛选出在中线附近的点
    Point* strip = (Point*)malloc(n * sizeof(Point));
    int stripSize = 0;
    for (int i = 0; i < n; i++) {
        if (abs(pointsSortedByY[i].x - midPoint.x) < minResult.distance) {
            strip[stripSize++] = pointsSortedByY[i];
        }
    }
    
    // 检查strip中的点对
    ClosestPairResult stripResult = checkStrip(strip, stripSize, minResult.distance);
    
    // 释放内存
    free(leftPointsSortedByY);
    free(rightPointsSortedByY);
    free(strip);
    
    // 返回最小距离的点对
    if (stripResult.distance < minResult.distance) {
        return stripResult;
    } else {
        return minResult;
    }
}

/**
 * 查找最近点对
 */
ClosestPairResult findClosestPair(Point points[], int n) {
    if (n < 2) {
        printf("点集必须包含至少两个点\n");
        exit(1);
    }
    
    // 按照x坐标排序
    Point* pointsSortedByX = (Point*)malloc(n * sizeof(Point));
    for (int i = 0; i < n; i++) {
        pointsSortedByX[i] = points[i];
    }
    qsort(pointsSortedByX, n, sizeof(Point), compareX);
    
    // 按照y坐标排序
    Point* pointsSortedByY = (Point*)malloc(n * sizeof(Point));
    for (int i = 0; i < n; i++) {
        pointsSortedByY[i] = points[i];
    }
    qsort(pointsSortedByY, n, sizeof(Point), compareY);
    
    // 调用递归函数
    ClosestPairResult result = closestPairRecursive(pointsSortedByX, n, pointsSortedByY);
    
    // 释放内存
    free(pointsSortedByX);
    free(pointsSortedByY);
    
    return result;
}

/**
 * 测试最近点对算法
 */
void testClosestPair() {
    printf("=== 测试最近点对算法 ===\n");
    
    // 测试用例1：随机点集
    Point points1[] = {
        Point(2, 3),
        Point(12, 30),
        Point(40, 50),
        Point(5, 1),
        Point(12, 10),
        Point(3, 4)
    };
    int n1 = sizeof(points1) / sizeof(points1[0]);
    
    ClosestPairResult result1 = findClosestPair(points1, n1);
    printf("最近点对1: (%.2f, %.2f) 和 (%.2f, %.2f)\n", 
           result1.p1.x, result1.p1.y, result1.p2.x, result1.p2.y);
    printf("距离: %.2f\n", result1.distance);
    
    // 测试用例2：所有点在一条直线上
    Point points2[] = {
        Point(0, 0),
        Point(1, 0),
        Point(2, 0),
        Point(3, 0),
        Point(100, 0)
    };
    int n2 = sizeof(points2) / sizeof(points2[0]);
    
    ClosestPairResult result2 = findClosestPair(points2, n2);
    printf("最近点对2: (%.2f, %.2f) 和 (%.2f, %.2f)\n", 
           result2.p1.x, result2.p1.y, result2.p2.x, result2.p2.y);
    printf("距离: %.2f\n", result2.distance);
    
    // 测试用例3：边界情况
    Point points3[] = {
        Point(0, 0),
        Point(0, 0)  // 重复点
    };
    int n3 = sizeof(points3) / sizeof(points3[0]);
    
    ClosestPairResult result3 = findClosestPair(points3, n3);
    printf("最近点对3: (%.2f, %.2f) 和 (%.2f, %.2f)\n", 
           result3.p1.x, result3.p1.y, result3.p2.x, result3.p2.y);
    printf("距离: %.2f\n", result3.distance);
}

int main() {
    testClosestPair();
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <random>
#include <chrono>

using namespace std;

using namespace std;

/**
 * LeetCode 973. 最接近原点的K个点
 * 
 * 问题描述：
 * 给定一个由平面上的点组成的数组 points，其中 points[i] = [xi, yi]，
 * 从中选取 k 个距离原点 (0, 0) 最近的点。可以按任意顺序返回答案。
 * 
 * 算法思路：
 * 本题可以使用多种方法解决：
 * 1. 排序法：按照距离原点的距离排序，取前k个点
 * 2. 最小堆法：维护一个大小为k的最大堆
 * 3. 快速选择法：使用快速选择算法找到第k小的元素
 * 4. 最近点对算法的变种
 * 
 * 时间复杂度：
 * - 排序法：O(n log n)
 * - 最小堆法：O(n log k)
 * - 快速选择法：O(n) 平均情况
 * 空间复杂度：O(k)
 * 
 * 应用场景：
 * 1. 机器学习中的最近邻搜索
 * 2. 地理信息系统中的最近设施查询
 * 3. 推荐系统中的相似用户查找
 * 
 * 相关题目：
 * 1. LeetCode 347. 前 K 个高频元素
 * 2. LeetCode 215. 数组中的第K个最大元素
 * 3. LeetCode 719. 找出第 k 小的距离对
 */

class LeetCode973KClosestPointsToOrigin {
public:
    /**
     * 方法1：排序法
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    vector<vector<int>> kClosestSort(vector<vector<int>> points, int k) {
        // 按照距离原点的平方排序（避免开方运算）
        sort(points.begin(), points.end(), [](const vector<int>& a, const vector<int>& b) {
            return (a[0]*a[0] + a[1]*a[1]) < (b[0]*b[0] + b[1]*b[1]);
        });
        
        // 返回前k个点
        return vector<vector<int>>(points.begin(), points.begin() + k);
    }
    
    /**
     * 方法2：最小堆法
     * 时间复杂度：O(n log k)
     * 空间复杂度：O(k)
     */
    vector<vector<int>> kClosestHeap(vector<vector<int>> points, int k) {
        // 使用最大堆，保持堆大小为k
        // 自定义比较器，按距离平方的负值排序（模拟最大堆）
        auto cmp = [](const pair<int, vector<int>>& a, const pair<int, vector<int>>& b) {
            return a.first > b.first;  // 最小堆
        };
        priority_queue<pair<int, vector<int>>, vector<pair<int, vector<int>>>, decltype(cmp)> maxHeap(cmp);
        
        // 遍历所有点
        for (const auto& point : points) {
            int dist = point[0]*point[0] + point[1]*point[1];
            if (maxHeap.size() < k) {
                // 堆未满，直接添加
                maxHeap.push({dist, point});
            } else if (dist < maxHeap.top().first) {
                // 当前点比堆顶更近，替换堆顶
                maxHeap.pop();
                maxHeap.push({dist, point});
            }
        }
        
        // 提取结果
        vector<vector<int>> result;
        while (!maxHeap.empty()) {
            result.push_back(maxHeap.top().second);
            maxHeap.pop();
        }
        
        return result;
    }
    
    /**
     * 方法3：快速选择法
     * 时间复杂度：O(n) 平均情况
     * 空间复杂度：O(1)
     */
    vector<vector<int>> kClosestQuickSelect(vector<vector<int>> points, int k) {
        quickSelect(points, 0, points.size() - 1, k);
        return vector<vector<int>>(points.begin(), points.begin() + k);
    }

private:
    /**
     * 计算点到原点的距离的平方
     */
    int getDistance(const vector<int>& point) {
        return point[0] * point[0] + point[1] * point[1];
    }
    
    /**
     * 交换数组中两个元素
     */
    void swap(vector<vector<int>>& points, int i, int j) {
        vector<int> temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
    
    /**
     * 快速选择实现
     */
    void quickSelect(vector<vector<int>>& points, int left, int right, int k) {
        if (left >= right) return;
        
        // 随机选择pivot以避免最坏情况
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(left, right);
        int pivotIndex = dis(gen);
        swap(points, pivotIndex, right);
        
        // 分区操作
        int pivotDist = getDistance(points[right]);
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (getDistance(points[j]) <= pivotDist) {
                swap(points, i, j);
                i++;
            }
        }
        
        swap(points, i, right);
        
        // 递归处理
        if (i == k - 1) {
            return;
        } else if (i < k - 1) {
            quickSelect(points, i + 1, right, k);
        } else {
            quickSelect(points, left, i - 1, k);
        }
    }
};

/**
 * 测试函数
 */
void testKClosestPoints() {
    LeetCode973KClosestPointsToOrigin solution;
    
    cout << "=== 测试 LeetCode 973. 最接近原点的K个点 ===" << endl;
    
    // 测试用例1
    vector<vector<int>> points1 = {{1,1},{2,2},{3,3}};
    int k1 = 1;
    cout << "测试用例1:" << endl;
    cout << "点集: ";
    for (const auto& point : points1) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "k = " << k1 << endl;
    
    auto result1 = solution.kClosestSort(points1, k1);
    cout << "排序法结果: ";
    for (const auto& point : result1) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    result1 = solution.kClosestHeap(points1, k1);
    cout << "堆法结果: ";
    for (const auto& point : result1) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    result1 = solution.kClosestQuickSelect(points1, k1);
    cout << "快速选择法结果: ";
    for (const auto& point : result1) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    // 测试用例2
    vector<vector<int>> points2 = {{3,3},{5,-1},{-2,4}};
    int k2 = 2;
    cout << "\n测试用例2:" << endl;
    cout << "点集: ";
    for (const auto& point : points2) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "k = " << k2 << endl;
    
    auto result2 = solution.kClosestSort(points2, k2);
    cout << "排序法结果: ";
    for (const auto& point : result2) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    result2 = solution.kClosestHeap(points2, k2);
    cout << "堆法结果: ";
    for (const auto& point : result2) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    result2 = solution.kClosestQuickSelect(points2, k2);
    cout << "快速选择法结果: ";
    for (const auto& point : result2) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    mt19937 gen(42);
    uniform_int_distribution<> dis(-5000, 5000);
    int n = 10000;
    vector<vector<int>> points3(n, vector<int>(2));
    for (int i = 0; i < n; i++) {
        points3[i][0] = dis(gen);
        points3[i][1] = dis(gen);
    }
    int k3 = 100;
    
    auto startTime = chrono::high_resolution_clock::now();
    solution.kClosestSort(points3, k3);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "排序法处理" << n << "个点选取" << k3 << "个最近点时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    startTime = chrono::high_resolution_clock::now();
    solution.kClosestHeap(points3, k3);
    endTime = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "堆法处理" << n << "个点选取" << k3 << "个最近点时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    startTime = chrono::high_resolution_clock::now();
    solution.kClosestQuickSelect(points3, k3);
    endTime = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "快速选择法处理" << n << "个点选取" << k3 << "个最近点时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testKClosestPoints();
    return 0;
}
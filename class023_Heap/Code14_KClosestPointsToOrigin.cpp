#include <iostream>
#include <vector>
#include <queue>
using namespace std;

/**
 * 相关题目6: LeetCode 973. 最接近原点的 K 个点
 * 题目链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 * 题目描述: 给定一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
 * 并且是一个整数 k ，返回离原点 (0,0) 最近的 k 个点。
 * 这里，平面上两点之间的距离是欧几里德距离。
 * 解题思路: 使用最大堆维护K个最近的点，堆中存储点的平方距离和点坐标
 * 时间复杂度: O(n log k)，其中n是点的数量，堆操作需要O(log k)时间
 * 空间复杂度: O(k)，堆最多存储k个点
 * 是否最优解: 是，这是解决Top K最近点问题的经典解法
 * 
 * 本题属于堆的典型应用场景：需要在大量数据中动态维护前K个最小/最大值
 */
class Solution {
public:
    /**
     * 找出离原点最近的K个点
     * @param points 二维整数数组，每个元素表示一个点的坐标 [x, y]
     * @param k 需要返回的最近点的数量
     * @return 离原点最近的k个点组成的二维数组
     * @throws invalid_argument 当输入参数无效时抛出异常
     */
    vector<vector<int>> kClosest(vector<vector<int>>& points, int k) {
        // 异常处理：检查输入数组是否为空
        if (points.empty()) {
            throw invalid_argument("输入点数组不能为空");
        }
        
        // 异常处理：检查k是否在有效范围内
        if (k <= 0 || k > points.size()) {
            throw invalid_argument("k的值必须在1到数组长度之间");
        }
        
        // 创建最大堆，按照距离的平方降序排列（这样堆顶是当前最远的点）
        // 堆中存储的是三元组: 距离平方, x坐标, y坐标
        priority_queue<vector<int>> maxHeap;
        
        // 遍历所有点
        for (const vector<int>& point : points) {
            int x = point[0];
            int y = point[1];
            // 计算点到原点的距离的平方（避免浮点数运算和平方根操作）
            int distSquare = x * x + y * y;
            
            // 调试信息：打印当前处理的点和距离
            // cout << "处理点: [" << x << ", " << y << "], 距离平方: " << distSquare << endl;
            
            if (maxHeap.size() < k) {
                // 如果堆的大小小于k，直接将当前点加入堆
                maxHeap.push({distSquare, x, y});
            } else if (distSquare < maxHeap.top()[0]) {
                // 如果当前点比堆顶的点更近（距离平方更小）
                // 则移除堆顶的点（当前k个点中最远的），并加入新点
                maxHeap.pop();
                maxHeap.push({distSquare, x, y});
            }
            // 否则（当前点比堆顶的点更远或相等），不做任何操作
        }
        
        // 将堆中的k个点转换为结果数组
        vector<vector<int>> result;
        while (!maxHeap.empty()) {
            vector<int> pointWithDist = maxHeap.top();
            maxHeap.pop();
            result.push_back({pointWithDist[1], pointWithDist[2]}); // x坐标和y坐标
        }
        
        return result;
    }
};

/**
 * 打印二维数组的辅助函数
 */
void printPoints(const vector<vector<int>>& points) {
    cout << "[";
    for (size_t i = 0; i < points.size(); i++) {
        cout << "[" << points[i][0] << ", " << points[i][1] << "]";
        if (i < points.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    Solution solution;
    
    // 测试用例1：基本情况
    vector<vector<int>> points1 = {{1, 3}, {-2, 2}, {5, 8}, {0, 1}};
    int k1 = 2;
    cout << "示例1输出: ";
    vector<vector<int>> result1 = solution.kClosest(points1, k1);
    printPoints(result1); // 期望输出: [[-2, 2], [0, 1]] 或 [[0, 1], [-2, 2]]
    
    // 测试用例2：k等于数组长度
    vector<vector<int>> points2 = {{3, 3}, {5, -1}, {-2, 4}};
    int k2 = 3;
    cout << "示例2输出: ";
    vector<vector<int>> result2 = solution.kClosest(points2, k2);
    printPoints(result2); // 期望输出: 原数组的所有点，按距离排序
    
    // 测试用例3：k=1，只有一个点
    vector<vector<int>> points3 = {{1, 2}, {1, 3}};
    int k3 = 1;
    cout << "示例3输出: ";
    vector<vector<int>> result3 = solution.kClosest(points3, k3);
    printPoints(result3); // 期望输出: [[1, 2]]
    
    // 测试用例4：边界情况 - 原点
    vector<vector<int>> points4 = {{0, 0}, {1, 2}, {3, 4}};
    int k4 = 1;
    cout << "示例4输出: ";
    vector<vector<int>> result4 = solution.kClosest(points4, k4);
    printPoints(result4); // 期望输出: [[0, 0]]
    
    // 测试异常情况
    try {
        vector<vector<int>> emptyPoints;
        solution.kClosest(emptyPoints, 1);
        cout << "异常测试失败：未抛出预期的异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常测试通过: " << e.what() << endl;
    }
    
    try {
        vector<vector<int>> points5 = {{1, 1}};
        solution.kClosest(points5, 2);
        cout << "异常测试失败：未抛出预期的异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常测试通过: " << e.what() << endl;
    }
    
    return 0;
}
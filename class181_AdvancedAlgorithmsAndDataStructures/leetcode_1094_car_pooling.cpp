#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 1094. 拼车 (Car Pooling) - C++版本
 * 
 * 解题思路：
 * 使用差分数组解决拼车问题
 * 
 * 时间复杂度：O(n + m)
 * 空间复杂度：O(m)
 */
class Solution {
public:
    bool carPooling(vector<vector<int>>& trips, int capacity) {
        if (trips.empty()) {
            return true;
        }
        
        // 找到最大位置
        int maxLocation = 0;
        for (const auto& trip : trips) {
            maxLocation = max(maxLocation, trip[2]);
        }
        
        // 创建差分数组，大小为最大位置 + 1
        vector<int> diff(maxLocation + 1, 0);
        
        // 处理每个行程
        for (const auto& trip : trips) {
            int passengers = trip[0];
            int from = trip[1];
            int to = trip[2];
            
            // 在差分数组中标记乘客变化
            diff[from] += passengers;  // 上车位置增加乘客
            if (to < maxLocation) {
                diff[to] -= passengers;  // 下车位置减少乘客
            }
        }
        
        // 计算每个位置的乘客数量
        int currentPassengers = 0;
        for (int i = 0; i <= maxLocation; i++) {
            currentPassengers += diff[i];
            // 如果某个位置的乘客数量超过容量，返回false
            if (currentPassengers > capacity) {
                return false;
            }
        }
        
        return true;
    }
    
    // 暴力解法（用于对比）
    bool carPoolingBruteForce(vector<vector<int>>& trips, int capacity) {
        if (trips.empty()) {
            return true;
        }
        
        // 找到最大位置
        int maxLocation = 0;
        for (const auto& trip : trips) {
            maxLocation = max(maxLocation, trip[2]);
        }
        
        // 创建乘客数量数组
        vector<int> passengers(maxLocation + 1, 0);
        
        // 处理每个行程
        for (const auto& trip : trips) {
            int numPassengers = trip[0];
            int from = trip[1];
            int to = trip[2];
            
            // 直接更新每个位置的乘客数量
            for (int i = from; i < to; i++) {
                passengers[i] += numPassengers;
                if (passengers[i] > capacity) {
                    return false;
                }
            }
        }
        
        return true;
    }
};

/**
 * 测试拼车问题解法
 */
void testCarPooling() {
    cout << "=== LeetCode 1094. 拼车 (C++版本) ===" << endl;
    
    Solution solution;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> trips1 = {{2, 1, 5}, {3, 3, 7}};
    int capacity1 = 4;
    bool result1 = solution.carPooling(trips1, capacity1);
    cout << "行程: ";
    for (const auto& trip : trips1) {
        cout << "[" << trip[0] << "," << trip[1] << "," << trip[2] << "] ";
    }
    cout << endl;
    cout << "容量: " << capacity1 << endl;
    cout << "差分数组结果: " << (result1 ? "true" : "false") << endl;
    cout << "暴力解法结果: " << (solution.carPoolingBruteForce(trips1, capacity1) ? "true" : "false") << endl;
    cout << "期望: false" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> trips2 = {{2, 1, 5}, {3, 3, 7}};
    int capacity2 = 5;
    bool result2 = solution.carPooling(trips2, capacity2);
    cout << "行程: ";
    for (const auto& trip : trips2) {
        cout << "[" << trip[0] << "," << trip[1] << "," << trip[2] << "] ";
    }
    cout << endl;
    cout << "容量: " << capacity2 << endl;
    cout << "差分数组结果: " << (result2 ? "true" : "false") << endl;
    cout << "暴力解法结果: " << (solution.carPoolingBruteForce(trips2, capacity2) ? "true" : "false") << endl;
    cout << "期望: true" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    default_random_engine generator(42);
    uniform_int_distribution<int> distribution(0, 1000);
    
    int n = 1000;
    int maxLoc = 1000;
    vector<vector<int>> trips(n, vector<int>(3));
    
    for (int i = 0; i < n; i++) {
        int from = distribution(generator) % (maxLoc - 1);
        int to = from + distribution(generator) % (maxLoc - from) + 1;
        int passengers = distribution(generator) % 10 + 1;
        trips[i] = {passengers, from, to};
    }
    
    int capacity = 50;
    
    auto startTime = chrono::high_resolution_clock::now();
    bool diffResult = solution.carPooling(trips, capacity);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "差分数组法处理" << n << "个行程时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    startTime = chrono::high_resolution_clock::now();
    bool bruteResult = solution.carPoolingBruteForce(trips, capacity);
    endTime = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "暴力解法处理" << n << "个行程时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    cout << "两种方法结果是否一致: " << (diffResult == bruteResult ? "是" : "否") << endl;
    
    // C++语言特性考量
    cout << "\n=== C++语言特性考量 ===" << endl;
    cout << "1. 使用vector容器管理动态数组" << endl;
    cout << "2. 使用const引用避免不必要的拷贝" << endl;
    cout << "3. 使用chrono库进行精确时间测量" << endl;
    cout << "4. 使用RAII原则管理资源" << endl;
}

int main() {
    testCarPooling();
    return 0;
}
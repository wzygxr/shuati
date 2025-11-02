#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <queue>
#include <climits>
#include <chrono>
#include <random>

using namespace std;

/**
 * LeetCode 1094. 拼车 (Car Pooling) - C++版本
 * 
 * 题目来源：https://leetcode.cn/problems/car-pooling/
 * 
 * 题目描述：
 * 车上最初有 capacity 个空座位可以用来载客。由于道路拥堵，只能顺序行驶，
 * 车上只能在指定的地点接送乘客。
 * 给定整数 capacity 和一个数组 trips，其中 trip[i] = [numPassengersi, fromi, toi]
 * 表示第 i 次旅行有 numPassengersi 乘客，接他们和放他们的位置分别是 fromi 和 toi。
 * 这些位置是从汽车的初始位置向东的公里数。
 * 当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则返回 false。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 扫描线算法：将所有事件按位置排序处理
 * 2. 差分数组：记录每个位置的乘客变化
 * 3. 优先队列：模拟乘客上下车过程
 * 
 * 使用扫描线算法的方法：
 * 1. 创建事件列表：上车事件和下车事件
 * 2. 按位置排序事件
 * 3. 扫描所有事件，维护当前乘客数量
 * 
 * 时间复杂度：
 * - 扫描线算法：O(n log n)
 * - 差分数组：O(n + m)
 * - 优先队列：O(n log n)
 * - 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 交通调度：车辆载客能力规划
 * 2. 资源分配：服务器负载均衡
 * 3. 项目管理：资源需求分析
 * 
 * 相关题目：
 * 1. LeetCode 253. 会议室 II
 * 2. LeetCode 56. 合并区间
 * 3. LeetCode 218. 天际线问题
 */

/**
 * 方法1：扫描线算法
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * @param trips 行程数组
 * @param capacity 车辆容量
 * @return 是否能完成所有行程
 */
bool carPoolingSweepLine(vector<vector<int>>& trips, int capacity) {
    // 创建事件列表：[位置, 乘客变化]
    vector<pair<int, int>> events;
    
    // 为每个行程创建上车和下车事件
    for (const auto& trip : trips) {
        int passengers = trip[0];
        int start = trip[1];
        int end = trip[2];
        
        // 上车事件（乘客增加）
        events.push_back({start, passengers});
        // 下车事件（乘客减少）
        events.push_back({end, -passengers});
    }
    
    // 按位置排序事件，如果位置相同，下车事件优先于上车事件
    sort(events.begin(), events.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
        if (a.first != b.first) {
            return a.first < b.first;
        }
        return a.second < b.second;
    });
    
    int currentPassengers = 0;
    
    // 扫描所有事件
    for (const auto& event : events) {
        currentPassengers += event.second;
        if (currentPassengers > capacity) {
            return false;
        }
    }
    
    return true;
}

/**
 * 方法2：差分数组
 * 时间复杂度：O(n + m)
 * 空间复杂度：O(m)
 * @param trips 行程数组
 * @param capacity 车辆容量
 * @return 是否能完成所有行程
 */
bool carPoolingDifferenceArray(vector<vector<int>>& trips, int capacity) {
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
        int fromLoc = trip[1];
        int toLoc = trip[2];
        
        // 在差分数组中标记乘客变化
        diff[fromLoc] += passengers;  // 上车位置增加乘客
        if (toLoc < maxLocation) {
            diff[toLoc] -= passengers;  // 下车位置减少乘客
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

/**
 * 方法3：优先队列（最小堆）
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * @param trips 行程数组
 * @param capacity 车辆容量
 * @return 是否能完成所有行程
 */
bool carPoolingPriorityQueue(vector<vector<int>>& trips, int capacity) {
    // 按起始位置排序行程
    sort(trips.begin(), trips.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    // 最小堆，存储下车位置和乘客数量
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    
    int currentPassengers = 0;
    
    // 处理每个行程
    for (const auto& trip : trips) {
        int passengers = trip[0];
        int fromLoc = trip[1];
        int toLoc = trip[2];
        
        // 让已经到达下车位置的乘客下车
        while (!pq.empty() && pq.top().first <= fromLoc) {
            currentPassengers -= pq.top().second;
            pq.pop();
        }
        
        // 上车
        currentPassengers += passengers;
        if (currentPassengers > capacity) {
            return false;
        }
        
        // 记录下车事件
        pq.push({toLoc, passengers});
    }
    
    return true;
}

/**
 * 测试函数
 */
void testCarPooling() {
    cout << "=== LeetCode 1094. 拼车 (C++版本) ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> trips1 = {{2, 1, 5}, {3, 3, 7}};
    int capacity1 = 4;
    cout << "行程: [[2,1,5],[3,3,7]]" << endl;
    cout << "容量: " << capacity1 << endl;
    cout << "扫描线算法结果: " << (carPoolingSweepLine(trips1, capacity1) ? "true" : "false") << endl;
    cout << "差分数组结果: " << (carPoolingDifferenceArray(trips1, capacity1) ? "true" : "false") << endl;
    cout << "优先队列结果: " << (carPoolingPriorityQueue(trips1, capacity1) ? "true" : "false") << endl;
    cout << "期望: false" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> trips2 = {{2, 1, 5}, {3, 3, 7}};
    int capacity2 = 5;
    cout << "行程: [[2,1,5],[3,3,7]]" << endl;
    cout << "容量: " << capacity2 << endl;
    cout << "扫描线算法结果: " << (carPoolingSweepLine(trips2, capacity2) ? "true" : "false") << endl;
    cout << "差分数组结果: " << (carPoolingDifferenceArray(trips2, capacity2) ? "true" : "false") << endl;
    cout << "优先队列结果: " << (carPoolingPriorityQueue(trips2, capacity2) ? "true" : "false") << endl;
    cout << "期望: true" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis_loc(0, 1000);
    uniform_int_distribution<> dis_pass(1, 10);
    
    int n = 1000;
    vector<vector<int>> trips(n);
    
    for (int i = 0; i < n; i++) {
        int fromLoc = dis_loc(gen);
        int toLoc = fromLoc + dis_loc(gen) % 100 + 1;
        int passengers = dis_pass(gen);
        trips[i] = {passengers, fromLoc, toLoc};
    }
    
    int capacity = 50;
    
    auto start_time = chrono::high_resolution_clock::now();
    bool result1 = carPoolingSweepLine(trips, capacity);
    auto end_time = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    cout << "扫描线算法处理" << n << "个行程时间: " << duration1.count() / 1000.0 << " ms" << endl;
    
    start_time = chrono::high_resolution_clock::now();
    bool result2 = carPoolingDifferenceArray(trips, capacity);
    end_time = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    cout << "差分数组法处理" << n << "个行程时间: " << duration2.count() / 1000.0 << " ms" << endl;
    
    start_time = chrono::high_resolution_clock::now();
    bool result3 = carPoolingPriorityQueue(trips, capacity);
    end_time = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    cout << "优先队列法处理" << n << "个行程时间: " << duration3.count() / 1000.0 << " ms" << endl;
    
    cout << "三种方法结果是否一致: " << (result1 == result2 && result2 == result3 ? "是" : "否") << endl;
    
    // C++语言特性考量
    cout << "\n=== C++语言特性考量 ===" << endl;
    cout << "1. 使用STL容器提高开发效率" << endl;
    cout << "2. 使用lambda表达式简化排序逻辑" << endl;
    cout << "3. 使用智能指针管理内存（如果需要）" << endl;
    cout << "4. 使用异常处理机制保证程序健壮性" << endl;
    
    // 算法复杂度分析
    cout << "\n=== 算法复杂度分析 ===" << endl;
    cout << "扫描线算法:" << endl;
    cout << "  时间复杂度: O(n log n) - 主要消耗在排序上" << endl;
    cout << "  空间复杂度: O(n) - 存储事件列表" << endl;
    cout << "差分数组:" << endl;
    cout << "  时间复杂度: O(n + m) - n是行程数，m是最大位置" << endl;
    cout << "  空间复杂度: O(m) - 差分数组" << endl;
    cout << "优先队列:" << endl;
    cout << "  时间复杂度: O(n log n) - 排序和堆操作" << endl;
    cout << "  空间复杂度: O(n) - 堆存储" << endl;
}

int main() {
    testCarPooling();
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

/**
 * Codeforces 612D. The Union of k-Segments
 * 
 * 题目来源：https://codeforces.com/problemset/problem/612/D
 * 
 * 题目描述：
 * 给定n条线段和一个整数k，求被至少k条线段覆盖的区间的并集。
 * 
 * 输入格式：
 * 第一行包含两个整数n和k(1 ≤ n ≤ 10^6, 1 ≤ k ≤ n)。
 * 接下来n行，每行包含两个整数li和ri(-10^9 ≤ li ≤ ri ≤ 10^9)，表示第i条线段的左右端点。
 * 
 * 输出格式：
 * 第一行输出一个整数m，表示结果区间的数量。
 * 接下来m行，每行输出两个整数aj和bj，表示结果区间。
 * 
 * 示例输入：
 * 3 2
 * 0 5
 * -3 2
 * 3 8
 * 
 * 示例输出：
 * 2
 * -3 2
 * 3 5
 * 
 * 解题思路：
 * 使用扫描线算法解决线段覆盖问题。核心思想是：
 * 1. 将每个线段的左右端点转换为事件点
 * 2. 对所有事件点按位置排序
 * 3. 扫描所有事件点，维护当前覆盖的线段数量
 * 4. 当覆盖数量从<k变为≥k时开始新区间，从≥k变为<k时结束区间
 * 
 * 时间复杂度：O(n log n)，其中 n 是线段的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 56. 合并区间
 * - LeetCode 253. 会议室II
 */

class Solution {
public:
    /**
     * 线段覆盖问题的扫描线解法
     * @param segments 线段数组，每个线段是 [left, right] 形式
     * @param k 覆盖线段的最小数量
     * @return 被至少k条线段覆盖的区间列表
     */
    static vector<vector<int>> unionOfKSegments(vector<vector<int>>& segments, int k) {
        if (segments.empty() || k <= 0) {
            return {};
        }
        
        // 创建事件点列表：[位置, 类型]
        // 类型：0表示线段开始，1表示线段结束
        vector<pair<int, int>> events;
        
        // 为每个线段创建开始和结束事件
        for (const auto& segment : segments) {
            events.emplace_back(segment[0], 0);  // 开始事件
            events.emplace_back(segment[1], 1);  // 结束事件
        }
        
        // 按照位置排序事件点
        // 如果位置相同，结束事件优先于开始事件
        sort(events.begin(), events.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
            if (a.first != b.first) {
                return a.first < b.first;
            }
            return a.second > b.second;
        });
        
        vector<vector<int>> result;
        int coverageCount = 0;
        int start = 0;
        
        // 扫描所有事件点
        for (const auto& event : events) {
            int position = event.first;
            int type = event.second;
            
            if (type == 0) {
                // 线段开始事件
                coverageCount++;
                if (coverageCount == k) {
                    // 开始新区间
                    start = position;
                }
            } else {
                // 线段结束事件
                if (coverageCount == k) {
                    // 结束区间
                    result.push_back({start, position});
                }
                coverageCount--;
            }
        }
        
        return result;
    }
};

/**
 * 测试线段覆盖问题解法
 */
void testUnionOfKSegments() {
    cout << "=== Codeforces 612D. The Union of k-Segments ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> segments1 = {
        {0, 5}, {-3, 2}, {3, 8}
    };
    int k1 = 2;
    vector<vector<int>> result1 = Solution::unionOfKSegments(segments1, k1);
    cout << "输入线段: ";
    for (const auto& segment : segments1) {
        cout << "[" << segment[0] << "," << segment[1] << "] ";
    }
    cout << endl;
    cout << "k = " << k1 << endl;
    cout << "输出区间数量: " << result1.size() << endl;
    cout << "输出区间: ";
    for (const auto& interval : result1) {
        cout << "[" << interval[0] << "," << interval[1] << "] ";
    }
    cout << endl;
    cout << "期望: 2个区间，[-3,2] [3,5]" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> segments2 = {
        {0, 10}, {5, 15}, {10, 20}
    };
    int k2 = 3;
    vector<vector<int>> result2 = Solution::unionOfKSegments(segments2, k2);
    cout << "输入线段: ";
    for (const auto& segment : segments2) {
        cout << "[" << segment[0] << "," << segment[1] << "] ";
    }
    cout << endl;
    cout << "k = " << k2 << endl;
    cout << "输出区间数量: " << result2.size() << endl;
    cout << "输出区间: ";
    for (const auto& interval : result2) {
        cout << "[" << interval[0] << "," << interval[1] << "] ";
    }
    cout << endl;
    cout << "期望: 1个区间，[10,10]" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dis(-1000000, 1000000);
    uniform_int_distribution<int> dis2(1, 10000);
    
    int n = 100000;
    vector<vector<int>> segments;
    
    for (int i = 0; i < n; i++) {
        int left = dis(gen);
        int right = left + dis2(gen);
        segments.push_back({left, right});
    }
    
    int k = 50000;
    
    auto startTime = chrono::high_resolution_clock::now();
    vector<vector<int>> result = Solution::unionOfKSegments(segments, k);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "100000个随机线段，k=50000的覆盖计算完成" << endl;
    cout << "覆盖区间数量: " << result.size() << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testUnionOfKSegments();
    return 0;
}
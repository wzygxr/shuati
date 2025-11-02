#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <random>
#include <chrono>

using namespace std;

using namespace std;

/**
 * LeetCode 218. 天际线问题 (The Skyline Problem)
 * 
 * 题目来源：https://leetcode.cn/problems/the-skyline-problem/
 * 
 * 题目描述：
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 现在，假设您获得了城市中所有建筑物的位置和高度，请编写一个程序来输出由这些建筑物形成的天际线。
 * 每个建筑物的几何信息用三元组 [Li, Ri, Hi] 表示，其中 Li 和 Ri 分别是第 i 座建筑物左右边缘的 x 坐标，
 * Hi 是其高度。可以保证 0 ≤ Li, Ri ≤ INT_MAX, 0 < Hi ≤ INT_MAX 和 Ri - Li > 0。
 * 您可以假设所有建筑物都是在绝对平坦且高度为 0 的表面上的完美矩形。
 * 
 * 例如，图 A 中所有建筑物的尺寸记录为：[ [2 9 10], [3 7 15], [5 12 12], [15 20 10], [19 24 8] ]。
 * 输出是以 [ [x1,y1], [x2, y2], [x3, y3], ... ] 格式的"关键点"的列表，
 * 按 x 坐标进行排序。关键点是水平线段的左端点。最后一个点的 y 值始终为 0，
 * 仅用于标记天际线的终点。此外，任何两个相邻建筑物之间的地面都应被视为天际线轮廓的一部分。
 * 
 * 注意：输出天际线中不得有连续的相同高度的水平线。
 * 例如 [...[2 3], [4 5], [7 5], [11 5], [12 7]...] 是不正确的答案；
 * 三条高度为 5 的线应该在最终输出中合并为一个：[...[2 3], [4 5], [12 7], ...]
 * 
 * 示例 1：
 * 输入：buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
 * 输出：[[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 * 
 * 示例 2：
 * 输入：buildings = [[0,2,3],[2,5,3]]
 * 输出：[[0,3],[5,0]]
 * 
 * 提示：
 * 1 <= buildings.length <= 10^4
 * 0 <= lefti < righti <= 2^31 - 1
 * 1 <= heighti <= 2^31 - 1
 * buildings 按 lefti 非递减排序
 * 
 * 解题思路：
 * 使用扫描线算法解决天际线问题。核心思想是：
 * 1. 将每个建筑物的左右边界转换为事件点
 * 2. 对所有事件点按x坐标排序
 * 3. 使用优先队列维护当前活跃建筑物的高度
 * 4. 扫描所有事件点，更新天际线关键点
 * 
 * 时间复杂度：O(n log n)，其中 n 是建筑物的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 850. 矩形面积II
 * - LeetCode 391. 完美矩形
 */

class Solution {
public:
    /**
     * 天际线问题的扫描线解法
     * @param buildings 建筑物数组，每个建筑物是 [left, right, height] 形式
     * @return 天际线关键点列表
     */
    static vector<vector<int>> getSkyline(vector<vector<int>>& buildings) {
        vector<vector<int>> result;
        
        // 创建事件点列表：[x坐标, 高度变化]
        // 高度为负数表示建筑物开始，正数表示建筑物结束
        vector<pair<int, int>> events;
        
        for (const auto& building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];
            
            // 添加建筑物开始事件（高度为负数）
            events.emplace_back(left, -height);
            // 添加建筑物结束事件（高度为正数）
            events.emplace_back(right, height);
        }
        
        // 按照x坐标排序事件点
        // 如果x坐标相同，按照高度排序（开始事件优先于结束事件）
        sort(events.begin(), events.end());
        
        // 使用 map 维护当前活跃建筑物的高度及其计数
        // map 可以自动排序，便于获取最大高度
        map<int, int> heightMap;
        // 初始高度为0
        heightMap[0] = 1;
        
        int prevHeight = 0;
        
        // 扫描所有事件点
        for (const auto& event : events) {
            int x = event.first;
            int height = event.second;
            
            if (height < 0) {
                // 建筑物开始事件
                int h = -height;
                heightMap[h]++;
            } else {
                // 建筑物结束事件
                heightMap[height]--;
                if (heightMap[height] == 0) {
                    heightMap.erase(height);
                }
            }
            
            // 获取当前最大高度
            int currentHeight = heightMap.rbegin()->first;
            
            // 如果高度发生变化，添加关键点
            if (currentHeight != prevHeight) {
                result.push_back({x, currentHeight});
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }
};

/**
 * 测试天际线问题解法
 */
void testSkyline() {
    cout << "=== LeetCode 218. 天际线问题 ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> buildings1 = {
        {2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}
    };
    vector<vector<int>> result1 = Solution::getSkyline(buildings1);
    cout << "输入: ";
    for (const auto& building : buildings1) {
        cout << "[" << building[0] << "," << building[1] << "," << building[2] << "] ";
    }
    cout << endl;
    cout << "输出: ";
    for (const auto& point : result1) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "期望: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> buildings2 = {
        {0, 2, 3}, {2, 5, 3}
    };
    vector<vector<int>> result2 = Solution::getSkyline(buildings2);
    cout << "输入: ";
    for (const auto& building : buildings2) {
        cout << "[" << building[0] << "," << building[1] << "," << building[2] << "] ";
    }
    cout << endl;
    cout << "输出: ";
    for (const auto& point : result2) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "期望: [[0,3],[5,0]]" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dis(0, 1000000);
    uniform_int_distribution<int> dis2(1, 10000);
    uniform_int_distribution<int> dis3(1, 1000000);
    
    int n = 10000;
    vector<vector<int>> buildings;
    
    for (int i = 0; i < n; i++) {
        int left = dis(gen);
        int right = left + dis2(gen);
        int height = dis3(gen);
        buildings.push_back({left, right, height});
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    vector<vector<int>> result = Solution::getSkyline(buildings);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "10000个建筑物的天际线计算完成" << endl;
    cout << "关键点数量: " << result.size() << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testSkyline();
    return 0;
}
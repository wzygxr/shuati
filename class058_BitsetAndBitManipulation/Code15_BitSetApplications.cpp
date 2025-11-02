#include <iostream>
#include <vector>
#include <bitset>
#include <stdexcept>
#include <chrono>
#include <random>
#include <algorithm>
#include <functional>
#include <map>
#include <unordered_map>
#include <queue>
#include <stack>
#include <string>
#include <sstream>
#include <iomanip>
#include <cmath>
#include <climits>
#include <cassert>
#include <limits>
#include <unordered_set>
#include <cstdint>

using namespace std;

/**
 * 位集应用算法实现
 * 包含多个实际应用场景的位集算法
 * 
 * 题目来源:
 * 1. LeetCode 78 - 子集
 * 2. LeetCode 90 - 子集 II
 * 3. LeetCode 401 - 二进制手表
 * 4. LeetCode 477 - 汉明距离总和
 * 5. 组合数学问题
 * 6. 状态压缩动态规划
 * 
 * 时间复杂度分析:
 * - 子集生成: O(2^n * n) - 指数级复杂度
 * - 二进制手表: O(12 * 60) - 常数时间
 * - 汉明距离总和: O(n) - 线性复杂度
 * - 状态压缩: O(2^n * n) - 指数级复杂度
 * 
 * 空间复杂度分析:
 * - 子集生成: O(2^n) - 指数级空间
 * - 二进制手表: O(1) - 常数空间
 * - 汉明距离总和: O(1) - 常数空间
 * - 状态压缩: O(2^n) - 指数级空间
 * 
 * 工程化考量:
 * 1. 性能优化: 使用位运算优化计算
 * 2. 内存管理: 处理大规模数据时的内存使用
 * 3. 边界处理: 处理空集和边界情况
 * 4. 可读性: 添加详细注释说明算法原理
 */

class BitSetApplications {
public:
    /**
     * LeetCode 78 - 子集
     * 题目链接: https://leetcode.com/problems/subsets/
     * 生成数组的所有可能子集
     * 
     * 方法1: 位运算枚举
     * 时间复杂度: O(2^n * n)
     * 空间复杂度: O(2^n)
     */
    static vector<vector<int>> subsets(vector<int>& nums) {
        int n = nums.size();
        int total = 1 << n;  // 2^n个子集
        vector<vector<int>> result;
        
        for (int mask = 0; mask < total; mask++) {
            vector<int> subset;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            result.push_back(subset);
        }
        
        return result;
    }
    
    /**
     * LeetCode 90 - 子集 II
     * 题目链接: https://leetcode.com/problems/subsets-ii/
     * 生成包含重复元素的数组的所有可能子集（去重）
     * 
     * 方法: 先排序，然后使用位运算枚举，跳过重复元素
     * 时间复杂度: O(2^n * n)
     * 空间复杂度: O(2^n)
     */
    static vector<vector<int>> subsetsWithDup(vector<int>& nums) {
        sort(nums.begin(), nums.end());
        int n = nums.size();
        int total = 1 << n;
        vector<vector<int>> result;
        unordered_set<string> seen;
        
        for (int mask = 0; mask < total; mask++) {
            vector<int> subset;
            string key;
            bool valid = true;
            
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    // 检查是否跳过重复元素
                    if (i > 0 && nums[i] == nums[i-1] && !(mask & (1 << (i-1)))) {
                        valid = false;
                        break;
                    }
                    subset.push_back(nums[i]);
                    key += to_string(nums[i]) + ",";
                }
            }
            
            if (valid && seen.find(key) == seen.end()) {
                result.push_back(subset);
                seen.insert(key);
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 401 - 二进制手表
     * 题目链接: https://leetcode.com/problems/binary-watch/
     * 显示所有可能的二进制手表时间
     * 
     * 方法: 枚举所有可能的小时和分钟组合
     * 时间复杂度: O(12 * 60) = O(720)
     * 空间复杂度: O(n) - n为结果数量
     */
    static vector<string> readBinaryWatch(int turnedOn) {
        vector<string> result;
        
        for (int hour = 0; hour < 12; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                if (__builtin_popcount(hour) + __builtin_popcount(minute) == turnedOn) {
                    string time = to_string(hour) + ":" + 
                                 (minute < 10 ? "0" : "") + to_string(minute);
                    result.push_back(time);
                }
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 477 - 汉明距离总和
     * 题目链接: https://leetcode.com/problems/total-hamming-distance/
     * 计算数组中所有数对之间的汉明距离总和
     * 
     * 方法: 按位统计，对于每一位，计算0和1的个数
     * 时间复杂度: O(32 * n) = O(n)
     * 空间复杂度: O(1)
     */
    static int totalHammingDistance(vector<int>& nums) {
        int total = 0;
        int n = nums.size();
        
        // 对每一位进行统计
        for (int i = 0; i < 32; i++) {
            int countOnes = 0;
            for (int num : nums) {
                if (num & (1 << i)) {
                    countOnes++;
                }
            }
            total += countOnes * (n - countOnes);
        }
        
        return total;
    }
    
    /**
     * 组合数学: 计算组合数 C(n, k)
     * 使用位运算枚举所有k个元素的组合
     * 
     * 方法: Gosper's Hack算法
     * 时间复杂度: O(C(n, k))
     * 空间复杂度: O(1)
     */
    static vector<vector<int>> combinations(int n, int k) {
        vector<vector<int>> result;
        
        if (k == 0 || k > n) return result;
        
        int mask = (1 << k) - 1;  // 初始组合：最低k位为1
        int limit = 1 << n;
        
        while (mask < limit) {
            // 将当前掩码转换为组合
            vector<int> comb;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    comb.push_back(i);
                }
            }
            result.push_back(comb);
            
            // 使用Gosper's Hack获取下一个组合
            int rightmost = mask & -mask;
            int next = mask + rightmost;
            mask = next | (((mask ^ next) / rightmost) >> 2);
        }
        
        return result;
    }
    
    /**
     * 状态压缩动态规划: 旅行商问题（TSP）
     * 使用位集表示访问过的城市状态
     * 
     * 方法: 动态规划 + 状态压缩
     * 时间复杂度: O(2^n * n^2)
     * 空间复杂度: O(2^n * n)
     */
    static int tsp(vector<vector<int>>& graph) {
        int n = graph.size();
        if (n == 0) return 0;
        
        // dp[mask][i]: 访问过mask表示的城市集合，当前在城市i的最小代价
        vector<vector<int>> dp(1 << n, vector<int>(n, INT_MAX));
        
        // 初始化：从城市0出发
        dp[1][0] = 0;
        
        // 动态规划
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int i = 0; i < n; i++) {
                if (!(mask & (1 << i))) continue;  // 城市i不在当前集合中
                
                for (int j = 0; j < n; j++) {
                    if (mask & (1 << j)) continue;  // 城市j已经在集合中
                    if (graph[i][j] == INT_MAX) continue;  // 不可达
                    
                    int newMask = mask | (1 << j);
                    if (dp[mask][i] != INT_MAX) {
                        dp[newMask][j] = min(dp[newMask][j], dp[mask][i] + graph[i][j]);
                    }
                }
            }
        }
        
        // 找到回到起点的最小代价
        int result = INT_MAX;
        int finalMask = (1 << n) - 1;
        for (int i = 1; i < n; i++) {
            if (dp[finalMask][i] != INT_MAX && graph[i][0] != INT_MAX) {
                result = min(result, dp[finalMask][i] + graph[i][0]);
            }
        }
        
        return result == INT_MAX ? -1 : result;
    }
    
    /**
     * 位集在集合操作中的应用
     * 实现集合的并集、交集、差集等操作
     */
    static void setOperationsDemo() {
        cout << "=== 位集集合操作演示 ===" << endl;
        
        // 使用bitset表示集合
        bitset<8> setA("10101010");  // 集合A: {1, 3, 5, 7}
        bitset<8> setB("11001100");  // 集合B: {2, 3, 6, 7}
        
        cout << "集合A: " << setA << " (十进制: " << setA.to_ulong() << ")" << endl;
        cout << "集合B: " << setB << " (十进制: " << setB.to_ulong() << ")" << endl;
        
        // 并集
        bitset<8> unionSet = setA | setB;
        cout << "并集 A ∪ B: " << unionSet << endl;
        
        // 交集
        bitset<8> intersectSet = setA & setB;
        cout << "交集 A ∩ B: " << intersectSet << endl;
        
        // 差集
        bitset<8> diffSet = setA & ~setB;
        cout << "差集 A - B: " << diffSet << endl;
        
        // 对称差
        bitset<8> symDiffSet = setA ^ setB;
        cout << "对称差 A Δ B: " << symDiffSet << endl;
        
        // 子集判断
        bool isSubset = (setA & setB) == setA;
        cout << "A是B的子集: " << (isSubset ? "是" : "否") << endl;
    }
};

class PerformanceTester {
public:
    static void testSubsets() {
        cout << "=== 子集生成性能测试 ===" << endl;
        
        vector<int> nums = {1, 2, 3, 4, 5};
        
        auto start = chrono::high_resolution_clock::now();
        auto result = BitSetApplications::subsets(nums);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "数组大小: " << nums.size() << endl;
        cout << "生成子集数量: " << result.size() << endl;
        cout << "耗时: " << time << " μs" << endl;
        
        // 显示前几个子集
        cout << "前5个子集: " << endl;
        for (int i = 0; i < min(5, (int)result.size()); i++) {
            cout << "  {";
            for (int j = 0; j < result[i].size(); j++) {
                cout << result[i][j];
                if (j < result[i].size() - 1) cout << ", ";
            }
            cout << "}" << endl;
        }
    }
    
    static void testCombinations() {
        cout << "\n=== 组合生成性能测试 ===" << endl;
        
        int n = 10, k = 3;
        
        auto start = chrono::high_resolution_clock::now();
        auto result = BitSetApplications::combinations(n, k);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "C(" << n << ", " << k << ") = " << result.size() << endl;
        cout << "耗时: " << time << " μs" << endl;
        
        // 显示前几个组合
        cout << "前5个组合: " << endl;
        for (int i = 0; i < min(5, (int)result.size()); i++) {
            cout << "  {";
            for (int j = 0; j < result[i].size(); j++) {
                cout << result[i][j];
                if (j < result[i].size() - 1) cout << ", ";
            }
            cout << "}" << endl;
        }
    }
    
    static void testHammingDistance() {
        cout << "\n=== 汉明距离总和测试 ===" << endl;
        
        vector<int> nums = {4, 14, 2, 8};
        
        auto start = chrono::high_resolution_clock::now();
        int result = BitSetApplications::totalHammingDistance(nums);
        auto time = chrono::duration_cast<chrono::nanoseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "数组: ";
        for (int num : nums) cout << num << " ";
        cout << endl;
        cout << "汉明距离总和: " << result << endl;
        cout << "耗时: " << time << " ns" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 位集应用单元测试 ===" << endl;
        
        // 测试子集生成
        vector<int> nums = {1, 2, 3};
        auto subsets = BitSetApplications::subsets(nums);
        assert(subsets.size() == 8);
        cout << "子集生成测试通过" << endl;
        
        // 测试汉明距离总和
        vector<int> nums2 = {4, 14, 2};
        int hamming = BitSetApplications::totalHammingDistance(nums2);
        assert(hamming > 0);
        cout << "汉明距离总和测试通过" << endl;
        
        // 测试组合生成
        auto combs = BitSetApplications::combinations(5, 2);
        assert(combs.size() == 10);
        cout << "组合生成测试通过" << endl;
        
        // 测试二进制手表
        auto times = BitSetApplications::readBinaryWatch(1);
        assert(!times.empty());
        cout << "二进制手表测试通过" << endl;
        
        cout << "所有单元测试通过!" << endl;
    }
};

int main() {
    cout << "位集应用算法实现" << endl;
    cout << "包含子集生成、组合数学、状态压缩等应用" << endl;
    cout << "==========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testSubsets();
    PerformanceTester::testCombinations();
    PerformanceTester::testHammingDistance();
    
    // 演示集合操作
    BitSetApplications::setOperationsDemo();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // 子集生成示例
    vector<int> nums = {1, 2, 3};
    cout << "数组 [1, 2, 3] 的所有子集:" << endl;
    auto allSubsets = BitSetApplications::subsets(nums);
    for (const auto& subset : allSubsets) {
        cout << "{";
        for (int i = 0; i < subset.size(); i++) {
            cout << subset[i];
            if (i < subset.size() - 1) cout << ", ";
        }
        cout << "}" << endl;
    }
    
    // 组合生成示例
    cout << "\nC(5, 2) 的所有组合:" << endl;
    auto combs = BitSetApplications::combinations(5, 2);
    for (const auto& comb : combs) {
        cout << "{";
        for (int i = 0; i < comb.size(); i++) {
            cout << comb[i];
            if (i < comb.size() - 1) cout << ", ";
        }
        cout << "}" << endl;
    }
    
    return 0;
}
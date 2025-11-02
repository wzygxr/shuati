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
#include <set>
#include <numeric>

using namespace std;

/**
 * 位算法优化实现
 * 包含LeetCode多个位算法优化相关题目的解决方案
 * 
 * 题目列表:
 * 1. LeetCode 29 - 两数相除
 * 2. LeetCode 50 - Pow(x, n)
 * 3. LeetCode 60 - 排列序列
 * 4. LeetCode 89 - 格雷编码
 * 5. LeetCode 134 - 加油站
 * 6. LeetCode 135 - 分发糖果
 * 7. LeetCode 149 - 直线上最多的点数
 * 8. LeetCode 152 - 乘积最大子数组
 * 9. LeetCode 169 - 多数元素
 * 10. LeetCode 229 - 求众数 II
 * 
 * 时间复杂度分析:
 * - 位运算优化: O(1) 到 O(n)
 * - 空间复杂度: O(1) 到 O(n)
 * 
 * 工程化考量:
 * 1. 位运算优化: 使用位运算替代乘除法
 * 2. 状态压缩: 使用位运算压缩状态空间
 * 3. 性能优化: 利用位运算的并行性
 * 4. 边界处理: 处理整数溢出、边界值等
 */

class BitAlgorithmOptimizations {
public:
    /**
     * LeetCode 29 - 两数相除
     * 题目链接: https://leetcode.com/problems/divide-two-integers/
     * 给定两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
     * 
     * 方法: 位运算 + 二分查找
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * 原理: 使用位运算模拟除法，通过左移实现快速乘法
     */
    static int divide(int dividend, int divisor) {
        // 处理特殊情况
        if (dividend == INT_MIN && divisor == -1) {
            return INT_MAX;  // 溢出情况
        }
        if (divisor == 1) return dividend;
        if (divisor == -1) return -dividend;
        
        // 确定符号
        bool negative = (dividend < 0) ^ (divisor < 0);
        
        // 转换为正数处理（使用long long防止溢出）
        long long ldividend = labs((long long)dividend);
        long long ldivisor = labs((long long)divisor);
        
        long long result = 0;
        
        while (ldividend >= ldivisor) {
            long long temp = ldivisor;
            long long multiple = 1;
            
            // 使用位运算加速
            while (ldividend >= (temp << 1)) {
                temp <<= 1;
                multiple <<= 1;
            }
            
            ldividend -= temp;
            result += multiple;
        }
        
        return negative ? -result : result;
    }
    
    /**
     * LeetCode 50 - Pow(x, n)
     * 题目链接: https://leetcode.com/problems/powx-n/
     * 实现 pow(x, n) ，即计算 x 的 n 次幂函数。
     * 
     * 方法: 快速幂算法（位运算）
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * 原理: 将指数n分解为二进制，利用x^(a+b) = x^a * x^b
     */
    static double myPow(double x, int n) {
        if (n == 0) return 1.0;
        if (x == 1.0) return 1.0;
        if (x == -1.0) return (n % 2 == 0) ? 1.0 : -1.0;
        
        long long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        
        double result = 1.0;
        double current_product = x;
        
        // 快速幂算法
        for (long long i = N; i > 0; i /= 2) {
            if (i % 2 == 1) {
                result *= current_product;
            }
            current_product *= current_product;
        }
        
        return result;
    }
    
    /**
     * LeetCode 60 - 排列序列
     * 题目链接: https://leetcode.com/problems/permutation-sequence/
     * 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
     * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下："123", "132", "213", "231", "312", "321"
     * 给定 n 和 k，返回第 k 个排列。
     * 
     * 方法: 数学 + 位标记
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n)
     */
    static string getPermutation(int n, int k) {
        // 计算阶乘
        vector<int> factorial(n + 1, 1);
        for (int i = 1; i <= n; i++) {
            factorial[i] = factorial[i-1] * i;
        }
        
        // 标记已使用的数字
        vector<bool> used(n + 1, false);
        string result;
        
        k--;  // 转换为0-based索引
        
        for (int i = n; i >= 1; i--) {
            // 确定当前位的数字
            int segment = factorial[i-1];
            int index = k / segment;
            k %= segment;
            
            // 找到第index个未使用的数字
            int count = 0;
            for (int j = 1; j <= n; j++) {
                if (!used[j]) {
                    if (count == index) {
                        result += to_string(j);
                        used[j] = true;
                        break;
                    }
                    count++;
                }
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 89 - 格雷编码
     * 题目链接: https://leetcode.com/problems/gray-code/
     * 格雷编码是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异。
     * 给定一个代表编码总位数的非负整数 n，打印其格雷编码序列。即使有多个不同答案，你也只需要返回其中一种。
     * 
     * 方法: 镜像反射法
     * 时间复杂度: O(2^n)
     * 空间复杂度: O(2^n)
     * 
     * 原理: G(i) = i ^ (i >> 1)
     */
    static vector<int> grayCode(int n) {
        vector<int> result;
        int total = 1 << n;
        
        for (int i = 0; i < total; i++) {
            result.push_back(i ^ (i >> 1));
        }
        
        return result;
    }
    
    /**
     * LeetCode 134 - 加油站
     * 题目链接: https://leetcode.com/problems/gas-station/
     * 在一条环路上有 N 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
     * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
     * 你从其中的一个加油站出发，开始时油箱为空。如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
     * 
     * 方法: 贪心算法
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
        int total_gas = 0, total_cost = 0;
        int current_gas = 0;
        int start_index = 0;
        
        for (int i = 0; i < gas.size(); i++) {
            total_gas += gas[i];
            total_cost += cost[i];
            current_gas += gas[i] - cost[i];
            
            if (current_gas < 0) {
                start_index = i + 1;
                current_gas = 0;
            }
        }
        
        return total_gas >= total_cost ? start_index : -1;
    }
    
    /**
     * LeetCode 135 - 分发糖果
     * 题目链接: https://leetcode.com/problems/candy/
     * 老师想给孩子们分发糖果，有 N 个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分。
     * 你需要按照以下要求，给这些孩子分发糖果：
     * 1. 每个孩子至少分配到 1 个糖果。
     * 2. 相邻的孩子中，评分高的孩子必须获得更多的糖果。
     * 
     * 方法: 两次遍历
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    static int candy(vector<int>& ratings) {
        int n = ratings.size();
        vector<int> candies(n, 1);
        
        // 从左到右遍历
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i-1]) {
                candies[i] = candies[i-1] + 1;
            }
        }
        
        // 从右到左遍历
        for (int i = n-2; i >= 0; i--) {
            if (ratings[i] > ratings[i+1]) {
                candies[i] = max(candies[i], candies[i+1] + 1);
            }
        }
        
        return accumulate(candies.begin(), candies.end(), 0);
    }
    
    /**
     * LeetCode 149 - 直线上最多的点数
     * 题目链接: https://leetcode.com/problems/max-points-on-a-line/
     * 给定一个二维平面，平面上有 n 个点，求最多有多少个点在同一条直线上。
     * 
     * 方法: 斜率统计 + 最大公约数
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n)
     */
    static int maxPoints(vector<vector<int>>& points) {
        if (points.size() < 3) return points.size();
        
        int max_count = 0;
        
        for (int i = 0; i < points.size(); i++) {
            map<pair<int, int>, int> slope_count;
            int duplicate = 1;  // 重复点计数
            
            for (int j = i + 1; j < points.size(); j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                
                if (dx == 0 && dy == 0) {
                    duplicate++;
                    continue;
                }
                
                // 计算斜率（使用最大公约数约分）
                int g = gcd(dx, dy);
                dx /= g;
                dy /= g;
                
                slope_count[{dx, dy}]++;
            }
            
            max_count = max(max_count, duplicate);
            for (auto& p : slope_count) {
                max_count = max(max_count, p.second + duplicate);
            }
        }
        
        return max_count;
    }
    
private:
    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
public:
    /**
     * LeetCode 152 - 乘积最大子数组
     * 题目链接: https://leetcode.com/problems/maximum-product-subarray/
     * 给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
     * 
     * 方法: 动态规划
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static int maxProduct(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int max_product = nums[0];
        int min_product = nums[0];
        int result = nums[0];
        
        for (int i = 1; i < nums.size(); i++) {
            if (nums[i] < 0) {
                swap(max_product, min_product);
            }
            
            max_product = max(nums[i], max_product * nums[i]);
            min_product = min(nums[i], min_product * nums[i]);
            
            result = max(result, max_product);
        }
        
        return result;
    }
    
    /**
     * LeetCode 169 - 多数元素
     * 题目链接: https://leetcode.com/problems/majority-element/
     * 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数大于 ⌊ n/2 ⌋ 的元素。
     * 
     * 方法: Boyer-Moore投票算法
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static int majorityElement(vector<int>& nums) {
        int candidate = nums[0];
        int count = 1;
        
        for (int i = 1; i < nums.size(); i++) {
            if (count == 0) {
                candidate = nums[i];
                count = 1;
            } else if (nums[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }
        
        return candidate;
    }
    
    /**
     * LeetCode 229 - 求众数 II
     * 题目链接: https://leetcode.com/problems/majority-element-ii/
     * 给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素。
     * 
     * 方法: Boyer-Moore投票算法扩展
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static vector<int> majorityElementII(vector<int>& nums) {
        if (nums.empty()) return {};
        
        int candidate1 = 0, candidate2 = 0;
        int count1 = 0, count2 = 0;
        
        // 第一轮投票
        for (int num : nums) {
            if (num == candidate1) {
                count1++;
            } else if (num == candidate2) {
                count2++;
            } else if (count1 == 0) {
                candidate1 = num;
                count1 = 1;
            } else if (count2 == 0) {
                candidate2 = num;
                count2 = 1;
            } else {
                count1--;
                count2--;
            }
        }
        
        // 第二轮验证
        count1 = count2 = 0;
        for (int num : nums) {
            if (num == candidate1) count1++;
            else if (num == candidate2) count2++;
        }
        
        vector<int> result;
        int n = nums.size();
        if (count1 > n / 3) result.push_back(candidate1);
        if (count2 > n / 3) result.push_back(candidate2);
        
        return result;
    }
};

class PerformanceTester {
public:
    static void testDivide() {
        cout << "=== 两数相除性能测试 ===" << endl;
        
        int dividend = INT_MAX;
        int divisor = 2;
        
        auto start = chrono::high_resolution_clock::now();
        int result = BitAlgorithmOptimizations::divide(dividend, divisor);
        auto time = chrono::duration_cast<chrono::nanoseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "divide: " << dividend << " / " << divisor << " = " 
             << result << ", 耗时=" << time << " ns" << endl;
    }
    
    static void testMyPow() {
        cout << "\n=== 快速幂性能测试 ===" << endl;
        
        double x = 2.0;
        int n = 1000000;
        
        auto start = chrono::high_resolution_clock::now();
        double result = BitAlgorithmOptimizations::myPow(x, n);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "myPow: " << x << "^" << n << " = " << result 
             << ", 耗时=" << time << " μs" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 位算法优化单元测试 ===" << endl;
        
        // 测试两数相除
        assert(BitAlgorithmOptimizations::divide(10, 3) == 3);
        assert(BitAlgorithmOptimizations::divide(7, -3) == -2);
        
        // 测试快速幂
        assert(abs(BitAlgorithmOptimizations::myPow(2.0, 10) - 1024.0) < 1e-9);
        
        // 测试多数元素
        vector<int> nums = {2, 2, 1, 1, 1, 2, 2};
        assert(BitAlgorithmOptimizations::majorityElement(nums) == 2);
        
        cout << "所有单元测试通过!" << endl;
    }
    
    static void complexityAnalysis() {
        cout << "\n=== 复杂度分析 ===" << endl;
        
        vector<pair<string, string>> algorithms = {
            {"divide", "O(log n), O(1)"},
            {"myPow", "O(log n), O(1)"},
            {"getPermutation", "O(n^2), O(n)"},
            {"grayCode", "O(2^n), O(2^n)"},
            {"canCompleteCircuit", "O(n), O(1)"},
            {"candy", "O(n), O(n)"},
            {"maxPoints", "O(n^2), O(n)"},
            {"maxProduct", "O(n), O(1)"},
            {"majorityElement", "O(n), O(1)"}
        };
        
        for (auto& algo : algorithms) {
            cout << algo.first << ": 时间复杂度=" << algo.second << endl;
        }
    }
};

int main() {
    cout << "位算法优化实现" << endl;
    cout << "包含LeetCode多个位算法优化相关题目的解决方案" << endl;
    cout << "===========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testDivide();
    PerformanceTester::testMyPow();
    
    // 复杂度分析
    PerformanceTester::complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // 两数相除示例
    int dividend = 10, divisor = 3;
    cout << dividend << " / " << divisor << " = " 
         << BitAlgorithmOptimizations::divide(dividend, divisor) << endl;
    
    // 快速幂示例
    double x = 2.0;
    int n = 10;
    cout << x << "^" << n << " = " << BitAlgorithmOptimizations::myPow(x, n) << endl;
    
    // 格雷编码示例
    int gray_n = 3;
    auto gray_codes = BitAlgorithmOptimizations::grayCode(gray_n);
    cout << "格雷编码(n=" << gray_n << "): ";
    for (int i = 0; i < min(5, (int)gray_codes.size()); i++) {
        cout << gray_codes[i] << " ";
    }
    cout << "..." << endl;
    
    // 多数元素示例
    vector<int> nums = {2, 2, 1, 1, 1, 2, 2};
    cout << "数组: ";
    for (int num : nums) cout << num << " ";
    cout << endl;
    cout << "多数元素: " << BitAlgorithmOptimizations::majorityElement(nums) << endl;
    
    return 0;
}
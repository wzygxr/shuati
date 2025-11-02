#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>

using namespace std;

/**
 * 相邻与结果不为0的最长子序列 - C++实现
 * 给定一个长度为n的数组arr，你可以随意选择数字组成子序列
 * 但是要求任意相邻的两个数&的结果不能是0，这样的子序列才是合法的
 * 返回最长合法子序列的长度
 * 1 <= n <= 10^5
 * 0 <= arr[i] <= 10^9
 * 测试链接 : https://www.luogu.com.cn/problem/P4310
 * 
 * 算法思路：
 * 1. 这是一个动态规划问题
 * 2. 对于每个数，我们关注它的二进制表示中为1的位
 * 3. dp[i]表示以第i位为结尾的最长子序列长度
 * 4. 对于当前数num，我们找出它二进制表示中为1的位j
 * 5. 找到所有以位j结尾的最长子序列长度，取最大值+1作为新的长度
 * 6. 更新所有num中为1的位j对应的dp[j]
 * 
 * 时间复杂度：O(n * 31) = O(n)，其中n是数组长度
 * 空间复杂度：O(1)，只使用了固定大小的数组
 */

class LongestAddNotZero {
public:
    int compute(vector<int>& arr) {
        int n = arr.size();
        // pre数组存储以每个二进制位结尾的最长子序列长度
        vector<int> pre(32, 0);
        
        for (int i = 0; i < n; i++) {
            int num = arr[i];
            int cur = 1; // 当前数字可以单独构成一个子序列
            
            // 第一遍遍历：找到当前数之前，以num中任意为1的位结尾的最长子序列长度的最大值
            for (int j = 0; j < 31; j++) {
                // 如果num的第j位为1
                if ((num >> j) & 1) {
                    // 更新cur为以第j位结尾的最长子序列长度+1的最大值
                    cur = max(cur, pre[j] + 1);
                }
            }
            
            // 第二遍遍历：更新pre数组
            for (int j = 0; j < 31; j++) {
                // 如果num的第j位为1
                if ((num >> j) & 1) {
                    // 更新以第j位结尾的最长子序列长度
                    pre[j] = max(pre[j], cur);
                }
            }
        }
        
        // 找到所有位结尾的最长子序列长度的最大值
        int ans = 0;
        for (int j = 0; j < 31; j++) {
            ans = max(ans, pre[j]);
        }
        return ans;
    }
    
    /**
     * 暴力方法 - 用于验证
     * 时间复杂度：O(2^n)，指数级复杂度，仅用于小规模测试
     */
    int bruteForce(vector<int>& arr) {
        int n = arr.size();
        int maxLen = 0;
        
        // 枚举所有子序列
        for (int mask = 1; mask < (1 << n); mask++) {
            vector<int> seq;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    seq.push_back(arr[i]);
                }
            }
            
            // 检查子序列是否合法
            bool valid = true;
            for (int i = 1; i < seq.size(); i++) {
                if ((seq[i-1] & seq[i]) == 0) {
                    valid = false;
                    break;
                }
            }
            
            if (valid) {
                maxLen = max(maxLen, (int)seq.size());
            }
        }
        
        return maxLen;
    }
    
    /**
     * 优化版本：使用更简洁的代码实现
     */
    int computeOptimized(vector<int>& arr) {
        int n = arr.size();
        vector<int> dp(32, 0);
        
        for (int num : arr) {
            int cur = 1;
            
            // 找到当前数可以接在哪些位后面
            for (int j = 0; j < 31; j++) {
                if ((num >> j) & 1) {
                    cur = max(cur, dp[j] + 1);
                }
            }
            
            // 更新所有为1的位
            for (int j = 0; j < 31; j++) {
                if ((num >> j) & 1) {
                    dp[j] = max(dp[j], cur);
                }
            }
        }
        
        return *max_element(dp.begin(), dp.end());
    }
    
    /**
     * 测试函数
     */
    void test() {
        // 测试用例1
        vector<int> test1 = {1, 2, 3, 4, 5};
        int result1 = compute(test1);
        int result1Opt = computeOptimized(test1);
        cout << "Test 1: " << result1 << ", Optimized: " << result1Opt << endl;
        
        // 测试用例2：包含0的情况
        vector<int> test2 = {0, 1, 2, 0, 4};
        int result2 = compute(test2);
        int result2Opt = computeOptimized(test2);
        cout << "Test 2: " << result2 << ", Optimized: " << result2Opt << endl;
        
        // 测试用例3：大规模数据
        vector<int> test3;
        for (int i = 0; i < 1000; i++) {
            test3.push_back(i);
        }
        int result3 = compute(test3);
        cout << "Test 3 (1000 elements): " << result3 << endl;
        
        // 暴力方法验证（小规模）
        vector<int> test4 = {1, 2, 3};
        int result4 = compute(test4);
        int result4Brute = bruteForce(test4);
        cout << "Test 4 - Validation: " << result4 << ", Brute Force: " << result4Brute << endl;
    }
    
    /**
     * 性能测试
     */
    void performanceTest() {
        int n = 100000; // 10万数据
        vector<int> arr;
        
        cout << "生成测试数据..." << endl;
        for (int i = 0; i < n; i++) {
            arr.push_back(rand() % 1000000000);
        }
        
        cout << "开始性能测试..." << endl;
        auto start = chrono::high_resolution_clock::now();
        
        int result = compute(arr);
        
        auto end = chrono::high_resolution_clock::now();
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "性能测试结果:" << endl;
        cout << "最长子序列长度: " << result << endl;
        cout << "耗时: " << duration.count() << "ms" << endl;
    }
};

int main() {
    LongestAddNotZero solution;
    
    // 基础测试
    cout << "=== 基础测试 ===" << endl;
    solution.test();
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    solution.performanceTest();
    
    return 0;
}

/**
 * C++工程化实战建议：
 * 
 * 1. 内存管理：
 *    - 使用vector代替原生数组，避免手动内存管理
 *    - 注意vector的初始化方式，确保正确初始化
 *    - 对于大规模数据，考虑使用reserve预分配内存
 * 
 * 2. 位运算优化：
 *    - 使用位运算检查二进制位，比除法取模更高效
 *    - 注意位运算的优先级，使用括号确保正确性
 *    - 对于32位整数，只需要检查0-30位
 * 
 * 3. 性能优化：
 *    - 使用引用传递避免不必要的拷贝
 *    - 对于频繁调用的函数，考虑内联优化
 *    - 使用-O2或-O3编译优化
 * 
 * 4. 算法优化思路：
 *    - 原始暴力方法的时间复杂度为O(2^n)，无法处理大规模数据
 *    - 优化方法利用二进制位状态，将时间复杂度降至O(n)
 *    - 关键思路是维护以每个二进制位结尾的最长子序列长度
 *    - 这种方法避免了枚举所有子序列，大大提高了效率
 * 
 * 5. 边界情况处理：
 *    - 处理数组为空的情况
 *    - 处理数组中包含0的情况
 *    - 处理所有数字都为0的情况
 *    - 处理大数的情况
 * 
 * 6. 调试技巧：
 *    - 使用gdb进行调试
 *    - 添加assert断言验证中间结果
 *    - 使用valgrind检查内存泄漏
 * 
 * 7. 相关题目扩展：
 *    - LeetCode 300: Longest Increasing Subsequence
 *    - LeetCode 128: Longest Consecutive Sequence
 *    - LeetCode 152: Maximum Product Subarray
 *    - 这些题目都涉及子序列或子数组的统计，可以对比学习
 * 
 * 8. 数学原理：
 *    - 两个数相与不为0，意味着它们至少有一个相同的二进制位为1
 *    - 子序列中相邻数字的约束条件可以转化为二进制位的约束
 *    - 利用二进制位状态可以高效地维护子序列信息
 * 
 * 9. 工程化考量：
 *    - 代码的可读性和可维护性
 *    - 异常处理和边界条件检查
 *    - 性能优化和内存管理
 *    - 测试用例的覆盖度
 */
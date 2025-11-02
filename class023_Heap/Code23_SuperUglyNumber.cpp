#include <iostream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <climits>
#include <chrono>

/**
 * 相关题目23: LeetCode 313. 超级丑数
 * 题目链接: https://leetcode.cn/problems/super-ugly-number/
 * 题目描述: 超级丑数 是一个正整数，并满足其所有质因数都出现在质数数组 primes 中。
 * 给你一个整数 n 和一个整数数组 primes，返回第 n 个 超级丑数 。
 * 解题思路1: 使用最小堆生成超级丑数序列
 * 解题思路2: 使用动态规划，为每个质数维护一个指针
 * 时间复杂度: 最小堆O(n log k)，动态规划O(nk)，其中k是primes数组的长度
 * 空间复杂度: 最小堆O(n)，动态规划O(n + k)
 * 是否最优解: 根据具体输入，两种解法各有优劣
 */

class Solution {
public:
    /**
     * 使用最小堆生成超级丑数序列
     * 
     * @param n 第n个超级丑数
     * @param primes 质因数数组
     * @return 第n个超级丑数
     * @throws std::invalid_argument 当输入参数无效时抛出异常
     */
    int nthSuperUglyNumberHeap(int n, const std::vector<int>& primes) {
        // 异常处理：检查n和primes是否有效
        if (n <= 0) {
            throw std::invalid_argument("n必须是正整数");
        }
        if (primes.empty()) {
            throw std::invalid_argument("primes数组不能为空");
        }
        
        // 特殊情况：第1个超级丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 使用集合来记录已经生成的超级丑数，避免重复
        std::unordered_set<long long> seen;
        // 创建最小堆
        std::priority_queue<long long, std::vector<long long>, std::greater<long long>> heap;
        
        // 初始化堆和集合
        seen.insert(1LL);
        heap.push(1LL);
        
        // 用于记录当前找到的超级丑数
        long long currentUgly = 1;
        
        // 循环n次，找到第n个超级丑数
        for (int i = 0; i < n; ++i) {
            // 取出堆顶元素，即当前最小的超级丑数
            currentUgly = heap.top();
            heap.pop();
            
            // 生成新的超级丑数
            for (int prime : primes) {
                long long nextUgly = currentUgly * prime;
                // 如果新超级丑数未被生成过，则加入堆和集合
                if (seen.find(nextUgly) == seen.end()) {
                    seen.insert(nextUgly);
                    heap.push(nextUgly);
                }
            }
        }
        
        // 返回第n个超级丑数
        return static_cast<int>(currentUgly);
    }
    
    /**
     * 使用动态规划生成超级丑数序列
     * 
     * @param n 第n个超级丑数
     * @param primes 质因数数组
     * @return 第n个超级丑数
     * @throws std::invalid_argument 当输入参数无效时抛出异常
     */
    int nthSuperUglyNumberDP(int n, const std::vector<int>& primes) {
        // 异常处理：检查n和primes是否有效
        if (n <= 0) {
            throw std::invalid_argument("n必须是正整数");
        }
        if (primes.empty()) {
            throw std::invalid_argument("primes数组不能为空");
        }
        
        // 特殊情况：第1个超级丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 创建一个数组来存储前n个超级丑数
        std::vector<long long> superUgly(n);
        // 第1个超级丑数是1
        superUgly[0] = 1;
        
        // 为每个质数维护一个指针
        int k = primes.size();
        std::vector<int> pointers(k, 0);
        
        // 生成前n个超级丑数
        for (int i = 1; i < n; ++i) {
            // 初始化最小值为一个很大的数
            long long minUgly = LLONG_MAX;
            
            // 计算所有可能的下一个超级丑数，并找出最小值
            for (int j = 0; j < k; ++j) {
                long long candidate = superUgly[pointers[j]] * primes[j];
                if (candidate < minUgly) {
                    minUgly = candidate;
                }
            }
            
            // 当前超级丑数为最小值
            superUgly[i] = minUgly;
            
            // 更新对应的指针
            for (int j = 0; j < k; ++j) {
                if (superUgly[pointers[j]] * primes[j] == minUgly) {
                    pointers[j]++;
                }
            }
        }
        
        // 返回第n个超级丑数
        return static_cast<int>(superUgly[n - 1]);
    }
    
    /**
     * 一种优化的动态规划实现，减少一些重复计算
     * 
     * @param n 第n个超级丑数
     * @param primes 质因数数组
     * @return 第n个超级丑数
     */
    int nthSuperUglyNumberOptimized(int n, const std::vector<int>& primes) {
        // 异常处理
        if (n <= 0) {
            throw std::invalid_argument("n必须是正整数");
        }
        if (primes.empty()) {
            throw std::invalid_argument("primes数组不能为空");
        }
        
        // 初始化结果数组
        std::vector<long long> dp(n);
        dp[0] = 1;
        
        // 初始化指针
        int k = primes.size();
        std::vector<int> pointers(k, 0);
        
        // 缓存当前每个质数对应的下一个可能的超级丑数
        std::vector<long long> nextUglies(k);
        for (int i = 0; i < k; ++i) {
            nextUglies[i] = primes[i];
        }
        
        for (int i = 1; i < n; ++i) {
            // 找到最小的下一个超级丑数
            dp[i] = findMin(nextUglies);
            
            // 更新指针和对应的下一个可能值
            for (int j = 0; j < k; ++j) {
                if (dp[i] == nextUglies[j]) {
                    pointers[j]++;
                    nextUglies[j] = dp[pointers[j]] * primes[j];
                }
            }
        }
        
        return static_cast<int>(dp[n - 1]);
    }
    
private:
    /**
     * 辅助方法：找到数组中的最小值
     * 
     * @param arr 输入数组
     * @return 数组中的最小值
     */
    long long findMin(const std::vector<long long>& arr) {
        long long min = arr[0];
        for (long long num : arr) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }
};

// 测试函数
void testSuperUglyNumber() {
    std::cout << "=== 测试超级丑数算法 ===" << std::endl;
    Solution solution;
    
    // 测试用例1：基本用例
    std::cout << "\n测试用例1：基本用例" << std::endl;
    int n1 = 12;
    std::vector<int> primes1 = {2, 7, 13, 19};
    int expected1 = 32;
    
    int resultHeap1 = solution.nthSuperUglyNumberHeap(n1, primes1);
    int resultDP1 = solution.nthSuperUglyNumberDP(n1, primes1);
    int resultOpt1 = solution.nthSuperUglyNumberOptimized(n1, primes1);
    
    std::cout << "最小堆实现: " << resultHeap1 << ", 期望: " << expected1 
              << ", " << (resultHeap1 == expected1 ? "✓" : "✗") << std::endl;
    std::cout << "动态规划实现: " << resultDP1 << ", 期望: " << expected1 
              << ", " << (resultDP1 == expected1 ? "✓" : "✗") << std::endl;
    std::cout << "优化动态规划实现: " << resultOpt1 << ", 期望: " << expected1 
              << ", " << (resultOpt1 == expected1 ? "✓" : "✗") << std::endl;
    
    // 测试用例2：简单质数数组
    std::cout << "\n测试用例2：简单质数数组" << std::endl;
    int n2 = 10;
    std::vector<int> primes2 = {2, 3, 5};
    int expected2 = 12; // 等同于普通丑数的第10个
    
    int resultHeap2 = solution.nthSuperUglyNumberHeap(n2, primes2);
    int resultDP2 = solution.nthSuperUglyNumberDP(n2, primes2);
    int resultOpt2 = solution.nthSuperUglyNumberOptimized(n2, primes2);
    
    std::cout << "最小堆实现: " << resultHeap2 << ", 期望: " << expected2 
              << ", " << (resultHeap2 == expected2 ? "✓" : "✗") << std::endl;
    std::cout << "动态规划实现: " << resultDP2 << ", 期望: " << expected2 
              << ", " << (resultDP2 == expected2 ? "✓" : "✗") << std::endl;
    std::cout << "优化动态规划实现: " << resultOpt2 << ", 期望: " << expected2 
              << ", " << (resultOpt2 == expected2 ? "✓" : "✗") << std::endl;
    
    // 测试用例3：只有一个质数
    std::cout << "\n测试用例3：只有一个质数" << std::endl;
    int n3 = 5;
    std::vector<int> primes3 = {2};
    int expected3 = 16; // 2^4
    
    int resultHeap3 = solution.nthSuperUglyNumberHeap(n3, primes3);
    int resultDP3 = solution.nthSuperUglyNumberDP(n3, primes3);
    int resultOpt3 = solution.nthSuperUglyNumberOptimized(n3, primes3);
    
    std::cout << "最小堆实现: " << resultHeap3 << ", 期望: " << expected3 
              << ", " << (resultHeap3 == expected3 ? "✓" : "✗") << std::endl;
    std::cout << "动态规划实现: " << resultDP3 << ", 期望: " << expected3 
              << ", " << (resultDP3 == expected3 ? "✓" : "✗") << std::endl;
    std::cout << "优化动态规划实现: " << resultOpt3 << ", 期望: " << expected3 
              << ", " << (resultOpt3 == expected3 ? "✓" : "✗") << std::endl;
    
    // 测试异常情况
    std::cout << "\n=== 测试异常情况 ===" << std::endl;
    try {
        solution.nthSuperUglyNumberHeap(0, std::vector<int>{2, 3});
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    try {
        solution.nthSuperUglyNumberDP(5, std::vector<int>{});
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    // 性能测试
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    // 测试中等规模输入
    int n4 = 1000;
    std::vector<int> primes4 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
    
    auto startTime = std::chrono::high_resolution_clock::now();
    int resultHeap = solution.nthSuperUglyNumberHeap(n4, primes4);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto heapTime = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime).count() / 1000.0;
    std::cout << "最小堆实现在n=" << n4 << "时的结果: " << resultHeap 
              << ", 用时: " << heapTime << "毫秒" << std::endl;
    
    startTime = std::chrono::high_resolution_clock::now();
    int resultDP = solution.nthSuperUglyNumberDP(n4, primes4);
    endTime = std::chrono::high_resolution_clock::now();
    auto dpTime = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime).count() / 1000.0;
    std::cout << "动态规划实现在n=" << n4 << "时的结果: " << resultDP 
              << ", 用时: " << dpTime << "毫秒" << std::endl;
    
    startTime = std::chrono::high_resolution_clock::now();
    int resultOpt = solution.nthSuperUglyNumberOptimized(n4, primes4);
    endTime = std::chrono::high_resolution_clock::now();
    auto optTime = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime).count() / 1000.0;
    std::cout << "优化动态规划实现在n=" << n4 << "时的结果: " << resultOpt 
              << ", 用时: " << optTime << "毫秒" << std::endl;
    
    std::cout << "\n性能比较:" << std::endl;
    if (dpTime > 0) {
        double ratio = heapTime / dpTime;
        std::cout << "最小堆比动态规划 " << (ratio > 1 ? "慢" : "快") << " " 
                  << std::abs(ratio - 1) << "倍" << std::endl;
    }
    if (optTime > 0) {
        double ratio = dpTime / optTime;
        std::cout << "原始动态规划比优化动态规划 " << (ratio > 1 ? "慢" : "快") << " " 
                  << std::abs(ratio - 1) << "倍" << std::endl;
    }
}

int main() {
    testSuperUglyNumber();
    return 0;
}

/*
 * 解题思路总结：
 * 1. 最小堆方法：
 *    - 使用最小堆来维护待处理的超级丑数候选
 *    - 每次取出最小的超级丑数，然后生成新的超级丑数
 *    - 使用集合避免重复
 *    - 时间复杂度O(n log k)，空间复杂度O(n)
 *    - 当primes数组长度较大时，这种方法可能会更高效
 * 
 * 2. 动态规划方法：
 *    - 维护primes数组长度个指针，分别指向每个质数需要乘的下一个位置
 *    - 每次选择所有可能的下一个超级丑数中的最小值
 *    - 更新对应的指针
 *    - 时间复杂度O(nk)，空间复杂度O(n + k)
 *    - 当primes数组长度较小时，这种方法通常比堆方法更高效
 * 
 * 3. 优化技巧：
 *    - 对于动态规划，可以缓存每个质数的下一个可能值，避免重复计算
 *    - 注意处理重复元素，特别是当多个质数生成相同的超级丑数时
 *    - 使用辅助方法提高代码可读性
 * 
 * 4. C++实现注意事项：
 *    - 使用long long类型避免整数溢出
 *    - 正确处理异常情况，使用std::invalid_argument
 *    - 使用LLONG_MAX作为初始最小值
 *    - 使用std::priority_queue创建最小堆，注意使用std::greater<long long>作为比较函数
 */
#include <iostream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <algorithm>
#include <chrono>

class Solution {
    /*
     相关题目22: LeetCode 264. 丑数 II
     题目链接: https://leetcode.cn/problems/ugly-number-ii/
     题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
     丑数 就是只包含质因数 2、3 和 5 的正整数。
     解题思路1: 使用最小堆生成丑数序列
     解题思路2: 使用动态规划，维护三个指针分别指向2、3、5的下一个乘数
     时间复杂度: 最小堆O(n log n)，动态规划O(n)
     空间复杂度: 最小堆O(n)，动态规划O(n)
     是否最优解: 动态规划解法是最优的，时间复杂度为O(n)
     */
public:
    /**
     * 使用最小堆生成丑数序列
     * 
     * @param n 第n个丑数
     * @return 第n个丑数
     * @throws std::invalid_argument 当n参数无效时抛出异常
     */
    int nthUglyNumberHeap(int n) {
        // 异常处理：检查n是否有效
        if (n <= 0) {
            throw std::invalid_argument("n必须是正整数");
        }
        
        // 特殊情况：第1个丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 使用集合来记录已经生成的丑数，避免重复
        std::unordered_set<long long> seen;
        // 创建最小堆（C++的priority_queue默认是最大堆，需要使用greater来创建最小堆）
        std::priority_queue<long long, std::vector<long long>, std::greater<long long>> heap;
        
        // 质因数列表
        std::vector<long long> factors = {2, 3, 5};
        
        // 初始化堆和集合
        seen.insert(1);
        heap.push(1);
        
        // 用于记录当前找到的丑数
        long long currentUgly = 1;
        
        // 循环n次，找到第n个丑数
        for (int i = 0; i < n; i++) {
            // 取出堆顶元素，即当前最小的丑数
            currentUgly = heap.top();
            heap.pop();
            
            // 生成新的丑数
            for (long long factor : factors) {
                long long nextUgly = currentUgly * factor;
                // 如果新丑数未被生成过，则加入堆和集合
                if (seen.find(nextUgly) == seen.end()) {
                    seen.insert(nextUgly);
                    heap.push(nextUgly);
                }
            }
        }
        
        // 返回第n个丑数
        return static_cast<int>(currentUgly);
    }
    
    /**
     * 使用动态规划生成丑数序列
     * 
     * @param n 第n个丑数
     * @return 第n个丑数
     * @throws std::invalid_argument 当n参数无效时抛出异常
     */
    int nthUglyNumberDP(int n) {
        // 异常处理：检查n是否有效
        if (n <= 0) {
            throw std::invalid_argument("n必须是正整数");
        }
        
        // 特殊情况：第1个丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 创建一个数组来存储前n个丑数
        std::vector<int> uglyNumbers(n);
        // 第1个丑数是1
        uglyNumbers[0] = 1;
        
        // 初始化三个指针，分别指向2、3、5的下一个乘数
        int p2 = 0, p3 = 0, p5 = 0;
        
        // 生成前n个丑数
        for (int i = 1; i < n; i++) {
            // 计算下一个可能的丑数
            int nextUgly2 = uglyNumbers[p2] * 2;
            int nextUgly3 = uglyNumbers[p3] * 3;
            int nextUgly5 = uglyNumbers[p5] * 5;
            
            // 取三个可能的丑数中的最小值作为当前丑数
            int minUgly = std::min({nextUgly2, nextUgly3, nextUgly5});
            uglyNumbers[i] = minUgly;
            
            // 更新对应的指针
            if (minUgly == nextUgly2) {
                p2++;
            }
            if (minUgly == nextUgly3) {
                p3++;
            }
            if (minUgly == nextUgly5) {
                p5++;
            }
        }
        
        // 返回第n个丑数
        return uglyNumbers[n - 1];
    }
    
    /**
     * 一种优化的动态规划实现，代码更简洁
     * 
     * @param n 第n个丑数
     * @return 第n个丑数
     */
    int nthUglyNumberEfficient(int n) {
        if (n <= 0) {
            throw std::invalid_argument("n必须是正整数");
        }
        
        // 初始化结果数组
        std::vector<int> res(n);
        res[0] = 1;
        
        // 初始化三个指针
        int i2 = 0, i3 = 0, i5 = 0;
        
        for (int i = 1; i < n; i++) {
            // 计算下一个可能的最小值
            res[i] = std::min({res[i2] * 2, res[i3] * 3, res[i5] * 5});
            
            // 更新指针
            if (res[i] == res[i2] * 2) i2++;
            if (res[i] == res[i3] * 3) i3++;
            if (res[i] == res[i5] * 5) i5++;
        }
        
        return res[n - 1];
    }
};

// 测试函数，验证算法在不同输入情况下的正确性
void testNthUglyNumber() {
    Solution solution;
    
    // 测试用例
    std::vector<int> testCases = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    std::vector<int> expectedResults = {1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24};
    
    std::cout << "=== 测试最小堆实现 ===" << std::endl;
    for (int i = 0; i < testCases.size(); i++) {
        int n = testCases[i];
        int result = solution.nthUglyNumberHeap(n);
        int expected = expectedResults[i];
        std::cout << "第" << n << "个丑数 = " << result 
                  << ", 期望结果 = " << expected 
                  << ", " << (result == expected ? "✓" : "✗") << std::endl;
    }
    
    std::cout << "\n=== 测试动态规划实现 ===" << std::endl;
    for (int i = 0; i < testCases.size(); i++) {
        int n = testCases[i];
        int result = solution.nthUglyNumberDP(n);
        int expected = expectedResults[i];
        std::cout << "第" << n << "个丑数 = " << result 
                  << ", 期望结果 = " << expected 
                  << ", " << (result == expected ? "✓" : "✗") << std::endl;
    }
    
    std::cout << "\n=== 测试优化的动态规划实现 ===" << std::endl;
    for (int i = 0; i < testCases.size(); i++) {
        int n = testCases[i];
        int result = solution.nthUglyNumberEfficient(n);
        int expected = expectedResults[i];
        std::cout << "第" << n << "个丑数 = " << result 
                  << ", 期望结果 = " << expected 
                  << ", " << (result == expected ? "✓" : "✗") << std::endl;
    }
    
    // 测试异常情况
    std::cout << "\n=== 测试异常情况 ===" << std::endl;
    try {
        solution.nthUglyNumberHeap(0);
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    try {
        solution.nthUglyNumberDP(-5);
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    // 性能测试
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    // 测试大输入
    int n = 1690; // 最大的第1690个丑数在题目约束范围内
    
    auto start = std::chrono::high_resolution_clock::now();
    int resultHeap = solution.nthUglyNumberHeap(n);
    auto end = std::chrono::high_resolution_clock::now();
    auto heapTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "最小堆实现在n=" << n << "时的结果: " << resultHeap << ", 用时: " << heapTime << "毫秒" << std::endl;
    
    start = std::chrono::high_resolution_clock::now();
    int resultDP = solution.nthUglyNumberDP(n);
    end = std::chrono::high_resolution_clock::now();
    auto dpTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "动态规划实现在n=" << n << "时的结果: " << resultDP << ", 用时: " << dpTime << "毫秒" << std::endl;
    
    double speedup = dpTime > 0 ? static_cast<double>(heapTime) / dpTime : 0;
    std::cout << "\n性能比较: 动态规划比最小堆快 " << speedup << "倍" << std::endl;
}

int main() {
    testNthUglyNumber();
    return 0;
}

/*
解题思路总结：
1. 最小堆方法：
   - 使用最小堆来维护待处理的丑数候选
   - 每次取出最小的丑数，然后生成新的丑数
   - 使用集合避免重复
   - 时间复杂度O(n log n)，空间复杂度O(n)

2. 动态规划方法（最优解）：
   - 维护三个指针，分别指向2、3、5需要乘的下一个位置
   - 每次选择三个指针生成的最小值作为下一个丑数
   - 更新对应的指针
   - 时间复杂度O(n)，空间复杂度O(n)

3. C++实现注意事项：
   - 使用long long类型避免整数溢出
   - priority_queue默认是最大堆，需要使用greater<>来创建最小堆
   - 使用std::min({a, b, c})需要C++11或更高版本
   - 注意异常处理的格式和信息
   - 使用std::chrono来进行性能测量
*/
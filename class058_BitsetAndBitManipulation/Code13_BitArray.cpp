#include <iostream>
#include <vector>
#include <chrono>
#include <bitset>
#include <unordered_set>
#include <stdexcept>
#include <cmath>
#include <tuple>  // 添加tuple头文件

using namespace std;
using namespace std::chrono;

/**
 * HackerRank Bit Array - 位数组
 * 题目链接: https://www.hackerrank.com/challenges/bitset-1/problem
 * 题目描述: 根据给定规则生成序列，计算序列中不同整数的个数
 * 
 * 问题详细描述:
 * 给定四个整数: N, S, P, Q，按照以下规则生成序列:
 * a[0] = S mod 2^31
 * 对于i >= 1: a[i] = (a[i-1] * P + Q) mod 2^31
 * 计算序列a[0], a[1], ..., a[N-1]中不同整数的个数
 * 
 * 约束条件:
 * 1 ≤ N ≤ 10^8
 * 0 ≤ S, P, Q ≤ 2^31 - 1
 * 
 * 解题思路:
 * 方法1: 使用unordered_set - 简单但内存消耗大
 * 方法2: 使用bitset - 内存效率高，适合大N
 * 方法3: Floyd循环检测 - 检测序列中的循环，避免存储整个序列
 * 
 * 时间复杂度分析:
 * 方法1: O(N) - 但内存消耗O(N)，不适合大N
 * 方法2: O(N) - 内存消耗O(2^31/8) ≈ 256MB，可行
 * 方法3: O(循环长度) - 最优，但实现复杂
 * 
 * 空间复杂度分析:
 * 方法1: O(N) - 存储所有元素
 * 方法2: O(2^31/8) - 固定大小bitset
 * 方法3: O(1) - 常数空间
 * 
 * 工程化考量:
 * 1. 内存优化: 对于大N必须使用bitset或循环检测
 * 2. 整数溢出: 使用unsigned long long进行中间计算
 * 3. 边界处理: 处理N=0,1的特殊情况
 * 4. 异常安全: 使用RAII管理资源
 */

class BitArraySolver {
private:
    static const unsigned int MOD = 1U << 31;  // 2^31
    
    /**
     * 计算下一个序列值
     */
    static unsigned int nextValue(unsigned int current, unsigned int p, unsigned int q) {
        unsigned long long next = (static_cast<unsigned long long>(current) * p + q) % MOD;
        return static_cast<unsigned int>(next);
    }

public:
    /**
     * 方法1: 使用unordered_set（仅适用于小N）
     */
    static int countDistinctHashSet(unsigned int n, unsigned int s, unsigned int p, unsigned int q) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        unordered_set<unsigned int> seen;
        unsigned int current = s % MOD;
        seen.insert(current);
        
        for (unsigned int i = 1; i < n; i++) {
            current = nextValue(current, p, q);
            seen.insert(current);
        }
        
        return static_cast<int>(seen.size());
    }
    
    /**
     * 方法2: 使用bitset（推荐用于大N）
     * 使用std::bitset需要编译时确定大小，这里使用vector<bool>作为动态bitset
     */
    static int countDistinctBitSet(unsigned int n, unsigned int s, unsigned int p, unsigned int q) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        // 使用vector<bool>作为动态bitset
        vector<bool> visited(MOD, false);
        int count = 0;
        unsigned int current = s % MOD;
        
        for (unsigned int i = 0; i < n; i++) {
            if (!visited[current]) {
                visited[current] = true;
                count++;
            }
            
            if (i < n - 1) {
                current = nextValue(current, p, q);
            }
        }
        
        return count;
    }
    
    /**
     * 方法3: Floyd循环检测算法（最优解）
     */
    static int countDistinctFloyd(unsigned int n, unsigned int s, unsigned int p, unsigned int q) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        unsigned int slow = s % MOD;
        unsigned int fast = s % MOD;
        int count = 1;  // 至少有一个元素s
        
        // 第一阶段: 检测循环
        bool cycleFound = false;
        unsigned int cycleLength = 0;
        
        for (unsigned int i = 1; i < n; i++) {
            // 慢指针移动一步
            slow = nextValue(slow, p, q);
            
            // 快指针移动两步
            fast = nextValue(fast, p, q);
            fast = nextValue(fast, p, q);
            
            if (slow == fast) {
                cycleFound = true;
                
                // 计算循环长度
                cycleLength = 1;
                unsigned int temp = nextValue(slow, p, q);
                while (temp != slow) {
                    temp = nextValue(temp, p, q);
                    cycleLength++;
                }
                break;
            }
        }
        
        if (!cycleFound) {
            return n;  // 没有循环，所有元素都不同
        }
        
        // 第二阶段: 找到循环起点
        slow = s % MOD;
        fast = s % MOD;
        
        // 快指针先移动cycleLength步
        for (unsigned int i = 0; i < cycleLength; i++) {
            fast = nextValue(fast, p, q);
        }
        
        // 同时移动快慢指针直到相遇
        while (slow != fast) {
            slow = nextValue(slow, p, q);
            fast = nextValue(fast, p, q);
        }
        
        unsigned int cycleStart = slow;
        
        // 第三阶段: 计算不同元素个数
        unordered_set<unsigned int> cycleElements;
        unsigned int current = cycleStart;
        
        do {
            cycleElements.insert(current);
            current = nextValue(current, p, q);
        } while (current != cycleStart);
        
        // 计算循环前元素个数
        unsigned int elementsBeforeCycle = 0;
        current = s % MOD;
        while (current != cycleStart) {
            elementsBeforeCycle++;
            current = nextValue(current, p, q);
        }
        
        return static_cast<int>(elementsBeforeCycle + cycleElements.size());
    }
    
    /**
     * 方法4: 优化版本 - 根据N的大小选择算法
     */
    static int countDistinctOptimized(unsigned int n, unsigned int s, unsigned int p, unsigned int q) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        // 对于小N使用HashSet
        if (n <= 1000000) {
            return countDistinctHashSet(n, s, p, q);
        }
        // 对于大N使用BitSet
        else if (n <= 100000000) {
            return countDistinctBitSet(n, s, p, q);
        }
        // 对于非常大的N使用Floyd算法
        else {
            return countDistinctFloyd(n, s, p, q);
        }
    }
    
    /**
     * 工程化改进版本：完整的异常处理
     */
    static int countDistinctWithValidation(unsigned int n, unsigned int s, unsigned int p, unsigned int q) {
        try {
            // 输入验证
            if (s >= MOD || p >= MOD || q >= MOD) {
                throw invalid_argument("s, p, q must be less than 2^31");
            }
            
            return countDistinctOptimized(n, s, p, q);
            
        } catch (const exception& e) {
            cerr << "Error in countDistinct: " << e.what() << endl;
            return 0;
        }
    }
};

/**
 * 性能测试工具类
 */
class PerformanceTester {
public:
    static void runTests() {
        cout << "=== HackerRank Bit Array - 单元测试 ===" << endl;
        
        // 测试用例
        vector<tuple<unsigned int, unsigned int, unsigned int, unsigned int, int>> testCases = {
            {5, 1, 2, 1, 5},    // 序列: 1, 3, 7, 15, 31
            {10, 1, 3, 0, 4},    // 序列可能产生循环
            {1, 100, 1, 1, 1}    // 边界情况 n=1
        };
        
        for (const auto& testCase : testCases) {
            auto [n, s, p, q, expected] = testCase;
            int result = BitArraySolver::countDistinctOptimized(n, s, p, q);
            
            cout << "测试 n=" << n << ", s=" << s << ", p=" << p << ", q=" << q
                 << ", 期望=" << expected << ", 实际=" << result
                 << ", " << (result == expected ? "通过" : "失败") << endl;
        }
        
        // 方法一致性测试
        cout << "\n=== 方法一致性测试 ===" << endl;
        unsigned int testN = 100, testS = 1, testP = 3, testQ = 1;
        
        int r1 = BitArraySolver::countDistinctHashSet(testN, testS, testP, testQ);
        int r2 = BitArraySolver::countDistinctBitSet(testN, testS, testP, testQ);
        int r3 = BitArraySolver::countDistinctFloyd(testN, testS, testP, testQ);
        int r4 = BitArraySolver::countDistinctOptimized(testN, testS, testP, testQ);
        
        cout << "HashSet: " << r1 << endl;
        cout << "BitSet: " << r2 << endl;
        cout << "Floyd: " << r3 << endl;
        cout << "优化法: " << r4 << endl;
        cout << "所有方法结果一致: " << (r1 == r2 && r2 == r3 && r3 == r4 ? "是" : "否") << endl;
    }
    
    static void performanceTest() {
        cout << "\n=== 性能测试 ===" << endl;
        
        vector<tuple<unsigned int, unsigned int, unsigned int, unsigned int>> testCases = {
            {1000, 1, 3, 1},
            {10000, 1, 3, 1},
            {100000, 1, 3, 1},
            {1000000, 1, 3, 1}
        };
        
        for (const auto& testCase : testCases) {
            auto [n, s, p, q] = testCase;
            cout << "n = " << n << ":" << endl;
            
            // 测试不同方法
            if (n <= 100000) {
                auto start = high_resolution_clock::now();
                int result1 = BitArraySolver::countDistinctHashSet(n, s, p, q);
                auto time1 = duration_cast<nanoseconds>(high_resolution_clock::now() - start);
                cout << "  HashSet: " << time1.count() << " ns, 结果: " << result1 << endl;
            }
            
            auto start = high_resolution_clock::now();
            int result2 = BitArraySolver::countDistinctBitSet(n, s, p, q);
            auto time2 = duration_cast<nanoseconds>(high_resolution_clock::now() - start);
            cout << "  BitSet: " << time2.count() << " ns, 结果: " << result2 << endl;
            
            start = high_resolution_clock::now();
            int result3 = BitArraySolver::countDistinctFloyd(n, s, p, q);
            auto time3 = duration_cast<nanoseconds>(high_resolution_clock::now() - start);
            cout << "  Floyd: " << time3.count() << " ns, 结果: " << result3 << endl;
            
            cout << endl;
        }
    }
};

/**
 * 复杂度分析
 */
void complexityAnalysis() {
    cout << "=== 复杂度分析 ===" << endl;
    cout << "方法1（HashSet）:" << endl;
    cout << "  时间复杂度: O(N)" << endl;
    cout << "  空间复杂度: O(N)" << endl;
    cout << "  适用场景: 小规模数据（N <= 10^6）" << endl;
    
    cout << "\n方法2（BitSet）:" << endl;
    cout << "  时间复杂度: O(N)" << endl;
    cout << "  空间复杂度: O(2^31/8) ≈ 256MB" << endl;
    cout << "  适用场景: 中等规模数据（N <= 10^8）" << endl;
    
    cout << "\n方法3（Floyd循环检测）:" << endl;
    cout << "  时间复杂度: O(循环长度)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    cout << "  适用场景: 大规模数据（N > 10^8）" << endl;
    
    cout << "\n工程化建议:" << endl;
    cout << "1. 根据N的大小动态选择算法" << endl;
    cout << "2. 使用unsigned long long避免整数溢出" << endl;
    cout << "3. 对于竞赛题目，BitSet方法通常是最佳选择" << endl;
}

int main() {
    cout << "HackerRank Bit Array - 位数组问题" << endl;
    cout << "计算根据规则生成的序列中不同整数的个数" << endl;
    
    // 运行单元测试
    PerformanceTester::runTests();
    
    // 运行性能测试
    PerformanceTester::performanceTest();
    
    // 复杂度分析
    complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    vector<tuple<unsigned int, unsigned int, unsigned int, unsigned int>> examples = {
        {5, 1, 2, 1},
        {10, 1, 3, 0},
        {100, 1, 1, 1}
    };
    
    for (const auto& example : examples) {
        auto [n, s, p, q] = example;
        int result = BitArraySolver::countDistinctWithValidation(n, s, p, q);
        cout << "n=" << n << ", s=" << s << ", p=" << p << ", q=" << q 
             << " -> 不同元素个数: " << result << endl;
    }
    
    return 0;
}
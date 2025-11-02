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

using namespace std;

/**
 * SPOJ问题: Counting Bits
 * 题目链接: https://www.spoj.com/problems/COUNT1BIT/
 * 题目描述: 计算从1到n的所有整数的二进制表示中1的个数之和
 * 
 * 方法1: 逐位计算法
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * 方法2: 动态规划法
 * 时间复杂度: O(log n)
 * 空间复杂度: O(log n)
 * 
 * 方法3: 内置函数法
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(1)
 * 
 * 工程化考量:
 * 1. 异常处理: 处理负数输入
 * 2. 性能优化: 使用位运算替代除法
 * 3. 可读性: 清晰的变量命名和注释
 * 4. 测试验证: 包含单元测试和性能测试
 */

class CountBits {
public:
    /**
     * 方法1: 逐位计算法（最优解）
     * 思路: 计算每一位对总和的贡献
     * 对于第i位，每2^(i+1)个数中，有2^i个数的该位为1
     */
    static long long countBitsMethod1(long long n) {
        if (n < 0) {
            throw invalid_argument("输入必须是非负数");
        }
        
        long long count = 0;
        long long i = 1;  // 当前处理的位
        
        while (i <= n) {
            // 计算当前位的周期
            long long divisor = i * 2;
            // 完整周期的数量
            long long fullCycles = n / divisor;
            // 完整周期中1的个数
            count += fullCycles * i;
            
            // 不完整周期中1的个数
            long long remainder = n % divisor;
            if (remainder >= i) {
                count += remainder - i + 1;
            }
            
            // 防止溢出
            if (i > LLONG_MAX / 2) break;
            i *= 2;
        }
        
        return count;
    }
    
    /**
     * 方法2: 动态规划法
     * 思路: 利用已知结果计算更大数的结果
     * dp[n] = dp[n/2] + (n % 2)
     */
    static long long countBitsMethod2(long long n) {
        if (n < 0) {
            throw invalid_argument("输入必须是非负数");
        }
        
        if (n == 0) return 0;
        
        // 找到最大的2的幂
        long long power = 1;
        while (power * 2 <= n) {
            power *= 2;
        }
        
        // 计算结果
        // 对于[0, power-1]区间的数，1的个数是已知的
        // 对于[power, n]区间的数，可以递归计算
        return (power / 2) * (64 - __builtin_clzll(power)) / 2 + 
               (n - power + 1) + 
               countBitsMethod2(n - power);
    }
    
    /**
     * 方法3: 内置函数法（简单但较慢）
     * 思路: 对每个数使用内置的popcount函数
     */
    static long long countBitsMethod3(long long n) {
        if (n < 0) {
            throw invalid_argument("输入必须是非负数");
        }
        
        long long count = 0;
        for (long long i = 1; i <= n; i++) {
            count += __builtin_popcountll(i);
        }
        return count;
    }
    
    /**
     * 方法4: 查表法（空间换时间）
     * 思路: 预计算小范围的popcount，然后分段计算
     */
    static long long countBitsMethod4(long long n) {
        if (n < 0) {
            throw invalid_argument("输入必须是非负数");
        }
        
        // 预计算0-255的popcount
        vector<int> table(256, 0);
        for (int i = 0; i < 256; i++) {
            table[i] = table[i >> 1] + (i & 1);
        }
        
        long long count = 0;
        for (long long i = 1; i <= n; i++) {
            unsigned long long num = i;
            // 分8次查表（64位整数）
            count += table[num & 0xFF] +
                    table[(num >> 8) & 0xFF] +
                    table[(num >> 16) & 0xFF] +
                    table[(num >> 24) & 0xFF] +
                    table[(num >> 32) & 0xFF] +
                    table[(num >> 40) & 0xFF] +
                    table[(num >> 48) & 0xFF] +
                    table[(num >> 56) & 0xFF];
        }
        
        return count;
    }
    
    /**
     * 性能测试工具
     */
    static void performanceTest() {
        vector<long long> testCases = {1000, 10000, 100000, 1000000, 10000000};
        vector<pair<string, function<long long(long long)>>> methods = {
            {"逐位计算法", countBitsMethod1},
            {"动态规划法", countBitsMethod2},
            {"内置函数法", countBitsMethod3},
            {"查表法", countBitsMethod4}
        };
        
        cout << "=== 性能测试 ===" << endl;
        for (auto n : testCases) {
            cout << "n = " << n << ":" << endl;
            
            for (auto& method : methods) {
                auto start = chrono::high_resolution_clock::now();
                long long result = method.second(n);
                auto end = chrono::high_resolution_clock::now();
                auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
                
                cout << setw(15) << method.first << ": " 
                     << setw(10) << result << " (" 
                     << duration.count() << " μs)" << endl;
            }
            cout << endl;
        }
    }
    
    /**
     * 单元测试
     */
    static void unitTest() {
        cout << "=== 单元测试 ===" << endl;
        
        vector<pair<long long, long long>> testCases = {
            {0, 0},
            {1, 1},
            {2, 2},
            {3, 4},
            {4, 5},
            {5, 7},
            {10, 17},
            {100, 197},
            {1000, 1987}
        };
        
        vector<function<long long(long long)>> methods = {
            countBitsMethod1,
            countBitsMethod2,
            countBitsMethod3,
            countBitsMethod4
        };
        
        for (auto& testCase : testCases) {
            long long n = testCase.first;
            long long expected = testCase.second;
            
            cout << "测试 n = " << n << " (期望: " << expected << ")" << endl;
            
            for (size_t i = 0; i < methods.size(); i++) {
                try {
                    long long result = methods[i](n);
                    bool passed = (result == expected);
                    cout << "  方法" << (i+1) << ": " << result 
                         << " (" << (passed ? "通过" : "失败") << ")" << endl;
                         
                    if (!passed) {
                        cerr << "错误: 方法" << (i+1) << "计算结果错误" << endl;
                    }
                } catch (const exception& e) {
                    cerr << "错误: 方法" << (i+1) << "抛出异常: " << e.what() << endl;
                }
            }
            cout << endl;
        }
    }
    
    /**
     * 复杂度分析
     */
    static void complexityAnalysis() {
        cout << "=== 复杂度分析 ===" << endl;
        
        cout << "方法1 - 逐位计算法:" << endl;
        cout << "  时间复杂度: O(log n) - 处理每一位" << endl;
        cout << "  空间复杂度: O(1) - 只使用常数空间" << endl;
        cout << "  优势: 最优时间复杂度，适合大数计算" << endl;
        cout << "  劣势: 逻辑相对复杂" << endl;
        
        cout << "方法2 - 动态规划法:" << endl;
        cout << "  时间复杂度: O(log n) - 递归深度为log n" << endl;
        cout << "  空间复杂度: O(log n) - 递归栈空间" << endl;
        cout << "  优势: 思路清晰，易于理解" << endl;
        cout << "  劣势: 递归可能栈溢出" << endl;
        
        cout << "方法3 - 内置函数法:" << endl;
        cout << "  时间复杂度: O(n log n) - 每个数需要O(log n)时间" << endl;
        cout << "  空间复杂度: O(1) - 常数空间" << endl;
        cout << "  优势: 实现简单" << endl;
        cout << "  劣势: 时间复杂度较高" << endl;
        
        cout << "方法4 - 查表法:" << endl;
        cout << "  时间复杂度: O(n) - 线性扫描" << endl;
        cout << "  空间复杂度: O(1) - 查表空间固定" << endl;
        cout << "  优势: 比内置函数法快" << endl;
        cout << "  劣势: 仍然需要线性时间" << endl;
    }
    
    /**
     * 边界测试
     */
    static void boundaryTest() {
        cout << "=== 边界测试 ===" << endl;
        
        vector<long long> boundaryCases = {
            0, 1, 2, 3, 
            LLONG_MAX,  // 最大long long
            (1LL << 62) - 1,  // 2^62 - 1
            (1LL << 62)       // 2^62
        };
        
        for (auto n : boundaryCases) {
            cout << "测试边界值 n = " << n << ":" << endl;
            
            try {
                long long result1 = countBitsMethod1(n);
                long long result2 = countBitsMethod2(n);
                
                cout << "  方法1结果: " << result1 << endl;
                cout << "  方法2结果: " << result2 << endl;
                
                if (n <= 1000000) {  // 避免超时
                    long long result3 = countBitsMethod3(n);
                    long long result4 = countBitsMethod4(n);
                    cout << "  方法3结果: " << result3 << endl;
                    cout << "  方法4结果: " << result4 << endl;
                    
                    // 验证一致性
                    if (result1 == result2 && result2 == result3 && result3 == result4) {
                        cout << "  ✓ 所有方法结果一致" << endl;
                    } else {
                        cerr << "  ✗ 方法结果不一致" << endl;
                    }
                }
            } catch (const exception& e) {
                cerr << "  错误: " << e.what() << endl;
            }
            cout << endl;
        }
    }
};

/**
 * 主函数 - 测试和演示
 */
int main() {
    cout << "SPOJ Counting Bits 问题解决方案" << endl;
    cout << "计算从1到n的所有整数的二进制表示中1的个数之和" << endl;
    cout << "==============================================" << endl;
    
    // 运行单元测试
    CountBits::unitTest();
    
    // 运行边界测试
    CountBits::boundaryTest();
    
    // 运行性能测试
    CountBits::performanceTest();
    
    // 复杂度分析
    CountBits::complexityAnalysis();
    
    // 交互式测试
    cout << "=== 交互式测试 ===" << endl;
    cout << "请输入要测试的n值 (输入-1退出): ";
    
    long long n;
    while (cin >> n) {
        if (n == -1) break;
        
        if (n < 0) {
            cout << "错误: 输入必须是非负数" << endl;
            continue;
        }
        
        try {
            auto start = chrono::high_resolution_clock::now();
            long long result = CountBits::countBitsMethod1(n);
            auto end = chrono::high_resolution_clock::now();
            auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
            
            cout << "结果: " << result << " (计算时间: " << duration.count() << " μs)" << endl;
            
            // 对于较小的n，显示详细计算过程
            if (n <= 100) {
                cout << "详细计算过程:" << endl;
                long long sum = 0;
                for (long long i = 1; i <= n; i++) {
                    int bits = __builtin_popcountll(i);
                    sum += bits;
                    cout << "  " << i << " (" << bitset<8>(i) << "): " << bits << " 个1" << endl;
                }
                cout << "总和: " << sum << endl;
            }
            
        } catch (const exception& e) {
            cerr << "计算错误: " << e.what() << endl;
        }
        
        cout << endl << "请输入下一个n值 (输入-1退出): ";
    }
    
    cout << "程序结束" << endl;
    return 0;
}
#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include <chrono>

/**
 * 排列计数问题（Permutation Counting Problem）
 * 题目：洛谷 P4071 [SDOI2016]排列计数
 * 链接：https://www.luogu.com.cn/problem/P4071
 * 描述：求有多少种1到n的排列a，满足序列恰好有m个位置i，使得a_i = i
 * 
 * C++实现特点：
 * - 使用类封装问题求解
 * - 预处理阶乘和逆元
 * - 提供多种实现方法：直接计算和优化版本
 * - 详细的异常处理和性能分析
 */

using namespace std;
using namespace chrono;

const long long MOD = 1000000007;  // 模数
const int MAXN = 1000001;  // 最大数据范围

class PermutationCounting {
private:
    int n;  // 元素个数
    int k;  // 恰好k个固定点
    vector<long long> fact;      // 阶乘数组
    vector<long long> inv_fact;  // 阶乘逆元数组
    
    /**
     * 快速幂算法 - 计算base^exponent % mod
     * 
     * 时间复杂度：O(log exponent)
     * 空间复杂度：O(1)
     * 
     * @param base 底数
     * @param exponent 指数
     * @param mod 模数
     * @return 计算结果
     */
    long long quick_pow(long long base, long long exponent, long long mod) {
        long long result = 1;
        base %= mod;
        
        while (exponent > 0) {
            if (exponent & 1) {  // 如果当前二进制位为1
                result = (result * base) % mod;
            }
            base = (base * base) % mod;  // 底数自乘
            exponent >>= 1;  // 右移一位
        }
        
        return result;
    }
    
    /**
     * 预处理阶乘和阶乘的逆元
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    void precompute() {
        // 计算阶乘数组
        fact.resize(n + 1);
        fact[0] = 1;
        for (int i = 1; i <= n; ++i) {
            fact[i] = (fact[i - 1] * i) % MOD;
        }
        
        // 计算最大阶乘的逆元
        inv_fact.resize(n + 1);
        inv_fact[n] = quick_pow(fact[n], MOD - 2, MOD);
        
        // 倒序计算其他阶乘的逆元
        for (int i = n - 1; i >= 0; --i) {
            inv_fact[i] = (inv_fact[i + 1] * (i + 1)) % MOD;
        }
    }
    
public:
    /**
     * 构造函数
     * 
     * @param n 元素个数
     * @param k 恰好k个固定点
     * 
     * @throws invalid_argument 当参数无效时抛出异常
     */
    PermutationCounting(int n, int k) {
        // 参数验证
        if (n < 0) {
            throw invalid_argument("参数n必须是非负整数");
        }
        if (k < 0 || k > n) {
            throw invalid_argument("参数k必须在0到n之间");
        }
        
        this->n = n;
        this->k = k;
        
        // 预处理阶乘和逆元
        precompute();
    }
    
    /**
     * 计算组合数C(a, b)
     * 时间复杂度：O(1) - 使用预处理的阶乘和逆元
     * 空间复杂度：O(1)
     * 
     * @param a 总数
     * @param b 选取的数量
     * @return 组合数C(a, b)对MOD取模的结果
     */
    long long comb(int a, int b) {
        if (b < 0 || b > a) {
            return 0;
        }
        return (fact[a] * inv_fact[b] % MOD) * inv_fact[a - b] % MOD;
    }
    
    /**
     * 使用二项式反演计算恰好k个固定点的排列数
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @return 恰好k个固定点的排列数，对MOD取模
     */
    long long count_fixed_points() {
        // 计算C(n, k)
        long long c_n_k = comb(n, k);
        
        // 计算D(n-k)：(n-k)个元素的错排数
        int m = n - k;
        long long derangement = 0;
        
        // 计算D(m) = m! * Σ(i=0到m) (-1)^i / i!
        for (int i = 0; i <= m; ++i) {
            // 计算符号 (-1)^i
            long long sign = (i % 2 == 0) ? 1 : MOD - 1;  // -1 mod MOD
            
            // 计算项：m! * (-1)^i / i! = fact[m] * inv_fact[i] * sign
            long long term = (fact[m] * inv_fact[i]) % MOD;
            term = (term * sign) % MOD;
            
            // 累加结果
            derangement = (derangement + term) % MOD;
        }
        
        // 最终结果：C(n, k) * D(n-k)
        return (c_n_k * derangement) % MOD;
    }
    
    /**
     * 优化版本：直接使用递推计算错排数
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @return 恰好k个固定点的排列数，对MOD取模
     */
    long long count_fixed_points_optimized() {
        // 计算C(n, k)
        long long c_n_k = comb(n, k);
        
        // 计算D(n-k)：使用递推公式
        int m = n - k;
        
        // 边界条件
        if (m == 0) {
            return c_n_k;  // D(0) = 1
        }
        if (m == 1) {
            return 0;  // D(1) = 0
        }
        
        // 使用递推公式计算错排数：D(m) = (m-1) * (D(m-1) + D(m-2))
        long long d_prev2 = 1;  // D(0)
        long long d_prev1 = 0;  // D(1)
        long long d_curr = 0;
        
        for (int i = 2; i <= m; ++i) {
            d_curr = ((i - 1) * ((d_prev1 + d_prev2) % MOD)) % MOD;
            // 更新状态
            d_prev2 = d_prev1;
            d_prev1 = d_curr;
        }
        
        // 最终结果：C(n, k) * D(n-k)
        return (c_n_k * d_curr) % MOD;
    }
    
    /**
     * 运行测试用例
     * 
     * @return bool 所有测试通过返回true，否则返回false
     */
    bool test() {
        // 测试用例
        vector<tuple<int, int, long long>> test_cases = {
            {3, 1, 3},   // n=3, k=1, 期望结果3种排列
            {4, 2, 6},   // n=4, k=2, 期望结果6种排列
            {1, 0, 0},   // n=1, k=0, 期望结果0
            {1, 1, 1},   // n=1, k=1, 期望结果1
            {0, 0, 1}    // n=0, k=0, 期望结果1
        };
        
        bool all_passed = true;
        
        cout << "排列计数问题测试：" << endl;
        cout << "======================================================" << endl;
        cout << "n  k  预期结果  常规方法  优化方法  状态" << endl;
        cout << "======================================================" << endl;
        
        for (const auto& test_case : test_cases) {
            int test_n = get<0>(test_case);
            int test_k = get<1>(test_case);
            long long expected = get<2>(test_case);
            
            try {
                PermutationCounting problem(test_n, test_k);
                long long result1 = problem.count_fixed_points();
                long long result2 = problem.count_fixed_points_optimized();
                
                bool passed = (result1 == expected) && (result2 == expected);
                all_passed &= passed;
                
                cout << test_n << "  " << test_k << "  " << expected << "  " 
                     << result1 << "  " << result2 << "  " 
                     << (passed ? "✓" : "✗") << endl;
            } catch (const exception& e) {
                cout << test_n << "  " << test_k << "  " << expected 
                     << "  异常  异常  ✗" << endl;
                cout << "  错误信息: " << e.what() << endl;
                all_passed = false;
            }
        }
        
        cout << "======================================================" << endl;
        cout << "测试结果: " << (all_passed ? "通过" : "失败") << endl << endl;
        
        return all_passed;
    }
};

/**
 * 算法复杂度分析函数
 */
void analyze_complexity() {
    cout << "算法复杂度分析：" << endl;
    cout << "======================================================" << endl;
    cout << "常规方法：" << endl;
    cout << "  时间复杂度：O(n) - 预处理和计算各需要O(n)时间" << endl;
    cout << "  空间复杂度：O(n) - 需要存储阶乘和逆元数组" << endl << endl;
    
    cout << "优化方法：" << endl;
    cout << "  时间复杂度：O(n) - 预处理需要O(n)，错排计算需要O(m) = O(n)" << endl;
    cout << "  空间复杂度：O(n) - 需要存储阶乘和逆元数组，但错排计算只需要O(1)额外空间" << endl << endl;
    
    cout << "工程化考量：" << endl;
    cout << "  1. 预处理阶乘和逆元以加速组合数计算" << endl;
    cout << "  2. 对于多次查询，预处理可以复用" << endl;
    cout << "  3. 注意处理大数值时的溢出问题，使用long long类型" << endl;
    cout << "  4. 模运算需要小心处理负数情况" << endl;
    cout << "======================================================" << endl;
}

/**
 * 主函数
 */
int main() {
    // 运行测试
    try {
        PermutationCounting problem(1, 1);
        problem.test();
    } catch (const exception& e) {
        cerr << "测试失败: " << e.what() << endl;
    }
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    try {
        cout << "测试负数输入 n=-1: " << endl;
        PermutationCounting problem1(-1, 0);
        cout << "  错误：未捕获到异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "  成功：捕获到异常 - " << e.what() << endl;
    }
    
    try {
        cout << "测试k > n: " << endl;
        PermutationCounting problem2(5, 6);
        cout << "  错误：未捕获到异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "  成功：捕获到异常 - " << e.what() << endl;
    }
    cout << endl;
    
    // 分析复杂度
    analyze_complexity();
    
    // 性能测试
    cout << "\n性能测试：" << endl;
    cout << "======================================================" << endl;
    
    try {
        // 测试较大的数据规模
        int test_n = 100000;
        int test_k = 50000;
        
        auto start = high_resolution_clock::now();
        PermutationCounting large_problem(test_n, test_k);
        long long result = large_problem.count_fixed_points_optimized();
        auto end = high_resolution_clock::now();
        auto duration = duration_cast<milliseconds>(end - start).count();
        
        cout << "n = " << test_n << ", k = " << test_k << endl;
        cout << "结果 = " << result << endl;
        cout << "计算时间 = " << duration << " ms" << endl;
    } catch (const exception& e) {
        cerr << "性能测试失败: " << e.what() << endl;
    }
    cout << "======================================================" << endl;
    
    // 处理用户输入
    try {
        int n, k;
        cout << "\n请输入n和k：" << endl;
        cout << "n = ";
        cin >> n;
        cout << "k = ";
        cin >> k;
        
        PermutationCounting user_problem(n, k);
        long long user_result = user_problem.count_fixed_points_optimized();
        cout << "恰好有" << k << "个固定点的排列数为: " << user_result << endl;
        
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}
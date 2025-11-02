#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include <chrono>
#include <tuple>

/**
 * 集合计数问题（Set Counting Problem）
 * 题目：洛谷 P10596 集合计数 / BZOJ2839 集合计数
 * 链接：https://www.luogu.com.cn/problem/P10596
 * 描述：从2^n个子集中选出若干个集合，使交集恰好包含k个元素的方案数
 * 
 * C++实现特点：
 * - 使用类封装问题求解
 * - 预处理阶乘和逆元
 * - 二项式反演算法
 * - 详细的异常处理和性能分析
 */

using namespace std;
using namespace chrono;

const long long MOD = 1000000007;  // 模数

class SetCounting {
private:
    int n;  // 元素总数
    int k;  // 交集恰好包含的元素个数
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
     * 时间复杂度：O(max_n)
     * 空间复杂度：O(max_n)
     * 
     * @param max_n 预处理的最大阶乘值
     */
    void precompute(int max_n) {
        fact.resize(max_n + 1);
        inv_fact.resize(max_n + 1);
        
        // 计算阶乘
        fact[0] = 1;
        for (int i = 1; i <= max_n; ++i) {
            fact[i] = (fact[i - 1] * i) % MOD;
        }
        
        // 计算最大阶乘的逆元
        inv_fact[max_n] = quick_pow(fact[max_n], MOD - 2, MOD);
        
        // 倒序计算其他阶乘的逆元
        for (int i = max_n - 1; i >= 0; --i) {
            inv_fact[i] = (inv_fact[i + 1] * (i + 1)) % MOD;
        }
    }
    
public:
    /**
     * 构造函数
     * 
     * @param n 元素总数
     * @param k 交集恰好包含的元素个数
     * 
     * @throws invalid_argument 当参数无效时抛出异常
     */
    SetCounting(int n, int k) {
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
        int max_needed = max(n, 1);  // 确保至少为1
        precompute(max_needed);
    }
    
    /**
     * 计算组合数 C(a, b) = a!/(b! * (a-b)!)
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
        if (a == 0 && b == 0) {
            return 1;
        }
        
        // 确保a在预处理范围内
        if (a >= fact.size()) {
            // 如果a大于预处理的范围，重新预处理
            precompute(a);
        }
        
        return (fact[a] * inv_fact[b] % MOD) * inv_fact[a - b] % MOD;
    }
    
    /**
     * 计算集合计数问题的解
     * 
     * 算法原理：
     * 1. 定义f(k)为交集恰好有k个元素的方案数
     * 2. 定义g(k)为交集至少有k个元素的方案数
     * 3. 通过二项式反演，f(k) = Σ(i=k到n) [(-1)^(i-k) * C(i,k) * g(i)]
     * 4. g(i) = C(n,i) * (2^(2^(n-i)) - 1) 表示选择i个固定元素，其余元素任意组合
     * 
     * 时间复杂度分析：O(n log max_pow) - 计算2的高次幂需要O(log max_pow)时间
     * 空间复杂度分析：O(n) - 需要存储阶乘和逆元数组
     * 
     * @return 交集恰好有k个元素的方案数，对MOD取模
     */
    long long compute() {
        // 边界情况处理
        if (k == 0 && n == 0) {
            return 0;
        }
        
        // 计算选择k个元素作为交集的方案数：C(n, k)
        long long c_n_k = comb(n, k);
        
        // 计算剩下的m = n - k个元素的可能组合
        int m = n - k;
        
        // 如果m为0，那么只有一种可能
        if (m == 0) {
            return c_n_k * 1 % MOD;
        }
        
        // 结果初始化为0
        long long result = 0;
        
        // 应用二项式反演公式，计算f(k)
        for (int i = 0; i <= m; ++i) {
            // 计算C(m, i)
            long long c_m_i = comb(m, i);
            
            // 计算符号 (-1)^i
            long long sign = (i % 2 == 0) ? 1 : MOD - 1;  // -1 mod MOD
            
            // 计算2^(2^(m-i)) mod MOD
            // 使用费马小定理简化计算，因为MOD是质数
            long long exponent = quick_pow(2, m - i, MOD - 1);
            long long power_val = quick_pow(2, exponent, MOD);
            
            // 计算项：C(m, i) * (-1)^i * (2^(2^(m-i)) - 1)
            long long term = (c_m_i * ((power_val - 1 + MOD) % MOD)) % MOD;
            term = (term * sign) % MOD;
            
            // 累加到结果中
            result = (result + term) % MOD;
        }
        
        // 最终结果：C(n, k) * f(k)
        return (c_n_k * result) % MOD;
    }
    
    /**
     * 运行测试用例
     * 
     * @return bool 所有测试通过返回true，否则返回false
     */
    bool test() {
        // 测试用例
        vector<tuple<int, int, long long>> test_cases = {
            {3, 1, 9},  // n=3, k=1, 期望结果9
            {4, 2, 18}, // n=4, k=2, 期望结果18
            {1, 0, 1},  // n=1, k=0, 期望结果1
            {1, 1, 1},  // n=1, k=1, 期望结果1
            {0, 0, 0}   // n=0, k=0, 期望结果0
        };
        
        bool all_passed = true;
        
        cout << "集合计数问题测试：" << endl;
        cout << "======================================================" << endl;
        cout << "n  k  预期结果  实际结果  状态" << endl;
        cout << "======================================================" << endl;
        
        for (const auto& test_case : test_cases) {
            int test_n = get<0>(test_case);
            int test_k = get<1>(test_case);
            long long expected = get<2>(test_case);
            
            try {
                SetCounting problem(test_n, test_k);
                long long actual = problem.compute();
                bool passed = (actual == expected);
                all_passed &= passed;
                
                cout << test_n << "  " << test_k << "  " << expected << "  " << actual << "  " 
                     << (passed ? "✓" : "✗") << endl;
            } catch (const exception& e) {
                cout << test_n << "  " << test_k << "  " << expected << "  异常  ✗" << endl;
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
    cout << "时间复杂度：" << endl;
    cout << "  - 预处理阶乘和逆元：O(n)" << endl;
    cout << "  - 计算结果：O(n log(2^(n-k))) = O(n^2) 最坏情况下" << endl;
    cout << "  - 组合数计算：O(1) 每次查询" << endl << endl;
    
    cout << "空间复杂度：" << endl;
    cout << "  - 阶乘和逆元数组：O(n)" << endl;
    cout << "  - 其他变量：O(1)" << endl << endl;
    
    cout << "优化点：" << endl;
    cout << "  1. 预处理阶乘和逆元以加速组合数计算" << endl;
    cout << "  2. 使用费马小定理计算大指数的模幂" << endl;
    cout << "  3. 处理负数模运算时的溢出问题" << endl;
    cout << "======================================================" << endl;
}

/**
 * 主函数
 */
int main() {
    // 运行测试
    try {
        SetCounting problem(1, 1);
        problem.test();
    } catch (const exception& e) {
        cerr << "测试失败: " << e.what() << endl;
    }
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    try {
        cout << "测试负数输入 n=-1: " << endl;
        SetCounting problem1(-1, 0);
        cout << "  错误：未捕获到异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "  成功：捕获到异常 - " << e.what() << endl;
    }
    
    try {
        cout << "测试k > n: " << endl;
        SetCounting problem2(5, 6);
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
        int test_n = 1000;
        int test_k = 500;
        
        auto start = high_resolution_clock::now();
        SetCounting large_problem(test_n, test_k);
        long long result = large_problem.compute();
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
        
        SetCounting user_problem(n, k);
        long long user_result = user_problem.compute();
        cout << "从2^" << n << "个子集中选出若干个集合，使交集恰好包含" << k 
             << "个元素的方案数为: " << user_result << endl;
        
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}
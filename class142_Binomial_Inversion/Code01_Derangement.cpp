#include <iostream>
#include <vector>
#include <string>
#include <chrono>
#include <stdexcept>

/**
 * 错排问题（Derangement Problem）
 * 题目：洛谷 P1595 信封问题
 * 链接：https://www.luogu.com.cn/problem/P1595
 * 描述：n个人写信，求所有人都没有收到自己信的方案数
 * 
 * C++实现特点：
 * - 使用constexpr和静态数组提升性能
 * - 提供多种计算方法：动态规划、二项式反演、空间优化版本
 * - 详细的时间和空间复杂度分析
 * - 完整的异常处理和边界条件检查
 * - 包含性能测试和单元测试
 */

using namespace std;
using namespace chrono;

const long long MOD = 1000000007;  // 模数

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
 * 计算错排数 - 动态规划方法
 * 
 * 算法原理：使用递推关系式 D(n) = (n-1) * (D(n-1) + D(n-2))
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * @param n 元素个数
 * @return n个元素的错排数，对MOD取模
 * @throws invalid_argument 当n为负数时抛出异常
 */
long long derangement_dp(int n) {
    if (n < 0) {
        throw invalid_argument("输入参数必须是非负整数");
    }
    
    // 边界条件
    if (n == 0) return 1;  // 空排列视为一种错排
    if (n == 1) return 0;  // 1个元素不可能错排
    
    // 动态规划数组
    vector<long long> dp(n + 1, 0);
    dp[0] = 1;
    dp[1] = 0;
    
    // 递推计算
    for (int i = 2; i <= n; ++i) {
        dp[i] = (i - 1) * ((dp[i - 1] + dp[i - 2]) % MOD) % MOD;
    }
    
    return dp[n];
}

/**
 * 计算错排数 - 二项式反演方法
 * 
 * 算法原理：使用公式 D(n) = n! * Σ(i=0到n) (-1)^i / i!
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * @param n 元素个数
 * @return n个元素的错排数，对MOD取模
 * @throws invalid_argument 当n为负数时抛出异常
 */
long long derangement_inversion(int n) {
    if (n < 0) {
        throw invalid_argument("输入参数必须是非负整数");
    }
    
    // 边界条件
    if (n == 0) return 1;
    if (n == 1) return 0;
    
    // 预处理阶乘和逆元
    vector<long long> fact(n + 1, 1);     // 阶乘数组
    vector<long long> inv_fact(n + 1, 1);  // 阶乘的逆元数组
    
    // 计算阶乘
    for (int i = 1; i <= n; ++i) {
        fact[i] = (fact[i - 1] * i) % MOD;
    }
    
    // 使用费马小定理计算逆元
    // 因为MOD是质数，所以 (n!)^(-1) ≡ (n!)^(MOD-2) mod MOD
    inv_fact[n] = quick_pow(fact[n], MOD - 2, MOD);
    
    // 倒序计算其他阶乘的逆元
    for (int i = n - 1; i >= 0; --i) {
        inv_fact[i] = (inv_fact[i + 1] * (i + 1)) % MOD;
    }
    
    // 应用二项式反演公式计算错排数
    long long result = 0;
    for (int k = 0; k <= n; ++k) {
        // 计算符号：(-1)^k
        long long sign = (k % 2 == 0) ? 1 : MOD - 1;  // -1 mod MOD
        
        // 计算项：n! * (-1)^k / k! = fact[n] * inv_fact[k] * sign
        long long term = (fact[n] * inv_fact[k]) % MOD;
        term = (term * sign) % MOD;
        
        // 累加结果
        result = (result + term) % MOD;
    }
    
    return result;
}

/**
 * 计算错排数 - 空间优化的动态规划方法
 * 
 * 算法原理：基于递推式，但只保存前两个状态
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @param n 元素个数
 * @return n个元素的错排数，对MOD取模
 * @throws invalid_argument 当n为负数时抛出异常
 */
long long derangement_optimized(int n) {
    if (n < 0) {
        throw invalid_argument("输入参数必须是非负整数");
    }
    
    // 边界条件
    if (n == 0) return 1;
    if (n == 1) return 0;
    
    // 只保存前两个状态
    long long a = 1;  // D(0)
    long long b = 0;  // D(1)
    long long res = 0;
    
    for (int i = 2; i <= n; ++i) {
        res = (i - 1) * ((a + b) % MOD) % MOD;
        
        // 更新状态
        a = b;
        b = res;
    }
    
    return res;
}

/**
 * 单元测试函数
 * 测试不同方法计算错排数的正确性
 */
void run_unit_tests() {
    // 测试用例：已知的错排数结果
    vector<pair<int, long long>> test_cases = {
        {0, 1},   // 0个元素的错排数为1
        {1, 0},   // 1个元素的错排数为0
        {2, 1},   // 2个元素的错排数为1
        {3, 2},   // 3个元素的错排数为2
        {4, 9},   // 4个元素的错排数为9
        {5, 44},  // 5个元素的错排数为44
        {6, 265}  // 6个元素的错排数为265
    };
    
    cout << "错排数单元测试：" << endl;
    cout << "==========================================================" << endl;
    cout << "n  预期结果  动态规划方法  二项式反演方法  空间优化方法  结果" << endl;
    cout << "==========================================================" << endl;
    
    bool all_passed = true;
    
    for (const auto& [n, expected] : test_cases) {
        try {
            long long dp_res = derangement_dp(n);
            long long inv_res = derangement_inversion(n);
            long long opt_res = derangement_optimized(n);
            
            // 小数值时直接比较，大数值时比较模后的值
            bool dp_correct = (n <= 6) && (dp_res == expected);
            bool inv_correct = (n <= 6) && (inv_res == expected);
            bool opt_correct = (n <= 6) && (opt_res == expected);
            
            bool test_passed = dp_correct && inv_correct && opt_correct;
            all_passed &= test_passed;
            
            cout << n << "  " << expected << "  " << dp_res << "  " << inv_res << "  " << opt_res << "  ";
            cout << (test_passed ? "✓" : "✗") << endl;
        } catch (const exception& e) {
            cout << n << "  " << expected << "  异常  异常  异常  ✗" << endl;
            cout << "  错误信息: " << e.what() << endl;
            all_passed = false;
        }
    }
    
    cout << "==========================================================" << endl;
    cout << "测试结果: " << (all_passed ? "通过" : "失败") << endl << endl;
}

/**
 * 性能测试函数
 * 比较不同方法在大规模数据下的性能
 */
void run_performance_tests() {
    cout << "性能测试：" << endl;
    cout << "==========================================================" << endl;
    
    int large_n = 1000000;  // 100万
    
    // 测试动态规划方法
    auto start = high_resolution_clock::now();
    long long dp_result = derangement_dp(large_n);
    auto end = high_resolution_clock::now();
    auto dp_duration = duration_cast<milliseconds>(end - start).count();
    
    // 测试二项式反演方法
    start = high_resolution_clock::now();
    long long inv_result = derangement_inversion(large_n);
    end = high_resolution_clock::now();
    auto inv_duration = duration_cast<milliseconds>(end - start).count();
    
    // 测试空间优化方法
    start = high_resolution_clock::now();
    long long opt_result = derangement_optimized(large_n);
    end = high_resolution_clock::now();
    auto opt_duration = duration_cast<milliseconds>(end - start).count();
    
    cout << "动态规划方法 (n=" << large_n << "): 结果 = " << dp_result 
         << ", 耗时 = " << dp_duration << " ms" << endl;
    cout << "二项式反演方法 (n=" << large_n << "): 结果 = " << inv_result 
         << ", 耗时 = " << inv_duration << " ms" << endl;
    cout << "空间优化方法 (n=" << large_n << "): 结果 = " << opt_result 
         << ", 耗时 = " << opt_duration << " ms" << endl;
    
    cout << "==========================================================" << endl;
}

/**
 * 边界情况测试
 */
void run_edge_case_tests() {
    cout << "边界情况测试：" << endl;
    try {
        cout << "测试负数输入 n=-1: " << endl;
        derangement_dp(-1);
        cout << "  错误：未捕获到异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "  成功：捕获到异常 - " << e.what() << endl;
    }
    cout << endl;
}

/**
 * 算法复杂度分析函数
 */
void analyze_complexity() {
    cout << "算法复杂度分析：" << endl;
    cout << "==========================================================" << endl;
    cout << "动态规划方法：" << endl;
    cout << "  时间复杂度：O(n) - 需要n次迭代计算每个位置的错排数" << endl;
    cout << "  空间复杂度：O(n) - 需要存储大小为n+1的dp数组" << endl << endl;
    
    cout << "二项式反演方法：" << endl;
    cout << "  时间复杂度：O(n) - 预处理阶乘和逆元需要O(n)，计算结果也需要O(n)" << endl;
    cout << "  空间复杂度：O(n) - 需要存储阶乘和逆元数组" << endl << endl;
    
    cout << "空间优化方法：" << endl;
    cout << "  时间复杂度：O(n) - 同样需要n次迭代" << endl;
    cout << "  空间复杂度：O(1) - 只需要常数级额外空间" << endl << endl;
    
    cout << "工程化考量：" << endl;
    cout << "  1. 对于大规模数据，空间优化方法更节省内存" << endl;
    cout << "  2. 对于多次调用，建议预处理阶乘和逆元以提高效率" << endl;
    cout << "  3. 注意处理大数值时的溢出问题，使用long long类型" << endl;
    cout << "  4. 模运算需要小心处理负数情况" << endl;
    cout << "==========================================================" << endl;
}

/**
 * 主函数
 */
int main() {
    // 运行各种测试
    run_unit_tests();
    run_edge_case_tests();
    run_performance_tests();
    analyze_complexity();
    
    // 处理用户输入
    try {
        int n;
        cout << "请输入n: " << endl;
        cin >> n;
        
        // 计算并输出结果
        long long result = derangement_optimized(n);
        cout << "错排数 D(" << n << ") = " << result << endl;
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}
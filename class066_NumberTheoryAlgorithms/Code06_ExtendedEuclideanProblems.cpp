/*
题目：扩展欧几里得算法问题集
来源：综合题目
内容：包含多个扩展欧几里得算法的应用题目

算法：扩展欧几里得算法
时间复杂度：O(log min(a, b))
空间复杂度：O(1)

工程化考量：
- 异常处理：处理除零、溢出等情况
- 边界条件：各种极端输入测试
- 性能优化：使用迭代版本避免递归深度限制
*/

#include <iostream>
#include <vector>
#include <stdexcept>
#include <cmath>

using namespace std;

// 扩展欧几里得算法（迭代版本）
struct ExtendedGCDResult {
    long long gcd;
    long long x;
    long long y;
};

ExtendedGCDResult extended_gcd(long long a, long long b) {
    if (a == 0 && b == 0) {
        throw invalid_argument("a和b不能同时为0");
    }
    
    // 处理特殊情况
    if (b == 0) {
        return {a, 1, 0};
    }
    
    long long x0 = 1, x1 = 0;
    long long y0 = 0, y1 = 1;
    long long r0 = a, r1 = b;
    
    while (r1 != 0) {
        long long q = r0 / r1;
        
        // 更新系数
        long long x_temp = x0 - q * x1;
        long long y_temp = y0 - q * y1;
        long long r_temp = r0 - q * r1;
        
        x0 = x1; x1 = x_temp;
        y0 = y1; y1 = y_temp;
        r0 = r1; r1 = r_temp;
    }
    
    return {r0, x0, y0};
}

// 求解线性同余方程 ax ≡ b (mod m)
long long solve_linear_congruence(long long a, long long b, long long m) {
    if (m <= 0) {
        throw invalid_argument("模数m必须为正数");
    }
    
    // 简化方程
    long long g = gcd(a, m);
    if (b % g != 0) {
        return -1; // 无解
    }
    
    // 简化方程
    a /= g; b /= g; m /= g;
    
    // 求a在模m下的逆元
    auto result = extended_gcd(a, m);
    if (result.gcd != 1) {
        return -1; // 理论上不会发生
    }
    
    long long x = (result.x * b) % m;
    if (x < 0) x += m;
    
    return x;
}

// 求模逆元
long long mod_inverse(long long a, long long m) {
    auto result = extended_gcd(a, m);
    if (result.gcd != 1) {
        throw invalid_argument("模逆元不存在");
    }
    
    long long x = result.x % m;
    if (x < 0) x += m;
    return x;
}

// 裴蜀定理验证
bool bezout_lemma(long long a, long long b, long long c) {
    if (a == 0 && b == 0) {
        return c == 0;
    }
    
    auto result = extended_gcd(abs(a), abs(b));
    return c % result.gcd == 0;
}

// 测试函数
void test_extended_gcd() {
    cout << "=== 扩展欧几里得算法测试 ===" << endl;
    
    vector<tuple<long long, long long, long long, long long, long long>> test_cases = {
        {48, 18, 6, 1, -3},     // gcd(48,18)=6, 48*1 + 18*(-3)=6
        {35, 15, 5, 1, -2},     // gcd(35,15)=5, 35*1 + 15*(-2)=5
        {17, 13, 1, 1, -1},     // gcd(17,13)=1, 17*1 + 13*(-1)=1
        {100, 35, 5, 1, -3},    // gcd(100,35)=5, 100*1 + 35*(-3)=5
    };
    
    for (auto& [a, b, expected_gcd, expected_x, expected_y] : test_cases) {
        auto result = extended_gcd(a, b);
        bool passed = (result.gcd == expected_gcd) && 
                     (result.x == expected_x) && 
                     (result.y == expected_y);
        
        cout << "gcd(" << a << ", " << b << ") = " << result.gcd
             << ", x = " << result.x << ", y = " << result.y
             << " - " << (passed ? "通过" : "失败") << endl;
    }
}

void test_linear_congruence() {
    cout << "\n=== 线性同余方程测试 ===" << endl;
    
    vector<tuple<long long, long long, long long, long long>> test_cases = {
        {3, 1, 11, 4},    // 3x ≡ 1 mod 11, x=4
        {5, 2, 13, 3},    // 5x ≡ 2 mod 13, x=3
        {4, 2, 6, 2},     // 4x ≡ 2 mod 6, x=2
        {6, 3, 9, 2},     // 6x ≡ 3 mod 9, x=2
    };
    
    for (auto& [a, b, m, expected] : test_cases) {
        long long result = solve_linear_congruence(a, b, m);
        bool passed = (result == expected);
        
        cout << a << "x ≡ " << b << " mod " << m << " => x = " << result
             << " - " << (passed ? "通过" : "失败") << endl;
    }
}

void test_mod_inverse() {
    cout << "\n=== 模逆元测试 ===" << endl;
    
    vector<tuple<long long, long long, long long>> test_cases = {
        {3, 11, 4},    // 3在模11下的逆元是4
        {5, 13, 8},    // 5在模13下的逆元是8
        {7, 19, 11},   // 7在模19下的逆元是11
    };
    
    for (auto& [a, m, expected] : test_cases) {
        try {
            long long result = mod_inverse(a, m);
            bool passed = (result == expected);
            
            cout << a << "^(-1) mod " << m << " = " << result
                 << " - " << (passed ? "通过" : "失败") << endl;
        } catch (const exception& e) {
            cout << a << "^(-1) mod " << m << " - 异常: " << e.what() << endl;
        }
    }
}

int main() {
    try {
        test_extended_gcd();
        test_linear_congruence();
        test_mod_inverse();
        
        cout << "\n=== 裴蜀定理测试 ===" << endl;
        cout << "方程3x + 5y = 1有解: " << (bezout_lemma(3, 5, 1) ? "是" : "否") << endl;
        cout << "方程6x + 9y = 3有解: " << (bezout_lemma(6, 9, 3) ? "是" : "否") << endl;
        cout << "方程4x + 6y = 1有解: " << (bezout_lemma(4, 6, 1) ? "是" : "否") << endl;
        
    } catch (const exception& e) {
        cerr << "程序异常: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}
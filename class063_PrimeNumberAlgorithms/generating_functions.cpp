// 生成函数和组合计数（Burnside引理/Polya定理）的C++实现
// 时间复杂度：根据具体问题而定
// 空间复杂度：根据具体问题而定

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
#include <cmath>
#include <cstring>
using namespace std;

// 生成函数 - 多项式乘法（普通生成函数）
// 时间复杂度：O(n²)
vector<long long> multiply_polynomials(const vector<long long>& a, const vector<long long>& b) {
    vector<long long> res(a.size() + b.size() - 1, 0);
    for (size_t i = 0; i < a.size(); i++) {
        for (size_t j = 0; j < b.size(); j++) {
            res[i + j] += a[i] * b[j];
        }
    }
    return res;
}

// 生成函数 - 计算组合数 C(n, k)
// 使用动态规划方法，时间复杂度：O(nk)
vector<vector<long long>> compute_combinations(int n, int k) {
    vector<vector<long long>> C(n + 1, vector<long long>(k + 1, 0));
    for (int i = 0; i <= n; i++) {
        C[i][0] = 1;
        for (int j = 1; j <= min(i, k); j++) {
            C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
        }
    }
    return C;
}

// 快速幂算法
long long power(long long a, long long b) {
    long long res = 1;
    while (b > 0) {
        if (b % 2 == 1) {
            res *= a;
        }
        a *= a;
        b /= 2;
    }
    return res;
}

// 快速幂算法（模运算）
long long power_mod(long long a, long long b, long long mod) {
    long long res = 1;
    a %= mod;
    while (b > 0) {
        if (b % 2 == 1) {
            res = (res * a) % mod;
        }
        a = (a * a) % mod;
        b /= 2;
    }
    return res;
}

// 计算欧拉函数 φ(n)
long long euler_phi(long long n) {
    long long res = n;
    for (long long p = 2; p * p <= n; p++) {
        if (n % p == 0) {
            while (n % p == 0) {
                n /= p;
            }
            res -= res / p;
        }
    }
    if (n > 1) {
        res -= res / n;
    }
    return res;
}

// 扩展欧几里得算法
long long extended_gcd(long long a, long long b, long long& x, long long& y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    long long x1, y1;
    long long g = extended_gcd(b, a % b, x1, y1);
    x = y1;
    y = x1 - (a / b) * y1;
    return g;
}

// 模逆元
long long mod_inverse(long long a, long long mod) {
    long long x, y;
    long long g = extended_gcd(a, mod, x, y);
    if (g != 1) {
        return -1; // 不存在逆元
    }
    return (x % mod + mod) % mod;
}

// Burnside引理：计算等价类的数量
// 给定置换群的大小m和每个置换的不动点数目，计算等价类数目
long long burnside(long long m, const vector<long long>& fixed_points) {
    long long sum = 0;
    for (long long fp : fixed_points) {
        sum += fp;
    }
    return sum / m;
}

// Polya定理：计算涂色方案数
// n: 物体数量
// k: 颜色数量
// rotations: 旋转置换的循环分解
long long polya(int n, int k, const vector<int>& rotations) {
    long long sum = 0;
    for (int cycles : rotations) {
        sum += power(k, cycles);
    }
    return sum / rotations.size();
}

// 计算旋转置换的循环分解数（项链问题）
vector<int> get_cycle_counts(int n) {
    vector<int> cycles;
    for (int d = 1; d <= n; d++) {
        if (n % d == 0) {
            cycles.push_back(euler_phi(d));
        }
    }
    return cycles;
}

// 项链问题：计算用k种颜色涂色n个珠子的项链的不同方案数
// 考虑旋转等价
long long necklace(int n, int k) {
    long long sum = 0;
    for (int d = 1; d <= n; d++) {
        if (n % d == 0) {
            sum += euler_phi(d) * power(k, n / d);
        }
    }
    return sum / n;
}

// 手镯问题：计算用k种颜色涂色n个珠子的手镯的不同方案数
// 考虑旋转和平移等价
long long bracelet(int n, int k) {
    long long sum = 0;
    // 旋转等价部分
    for (int d = 1; d <= n; d++) {
        if (n % d == 0) {
            sum += euler_phi(d) * power(k, n / d);
        }
    }
    
    // 翻转等价部分
    if (n % 2 == 0) {
        // 偶数情况：n/2个翻转经过两个珠子，n/2个翻转经过两个对中心点
        sum += (n / 2) * power(k, n / 2 + 1);
        sum += (n / 2) * power(k, n / 2);
    } else {
        // 奇数情况：n个翻转都经过一个珠子和一个对中心点
        sum += n * power(k, (n + 1) / 2);
    }
    
    return sum / (2 * n);
}

// 指数生成函数乘法
vector<long long> multiply_exponential(const vector<long long>& a, const vector<long long>& b) {
    vector<long long> res(a.size() + b.size() - 1, 0);
    for (size_t i = 0; i < a.size(); i++) {
        for (size_t j = 0; j < b.size(); j++) {
            res[i + j] += a[i] * b[j];
        }
    }
    return res;
}

// 计算阶乘和阶乘的逆元
void compute_factorials(int n, vector<long long>& fact, vector<long long>& inv_fact, long long mod) {
    fact.resize(n + 1);
    inv_fact.resize(n + 1);
    fact[0] = 1;
    for (int i = 1; i <= n; i++) {
        fact[i] = (fact[i - 1] * i) % mod;
    }
    inv_fact[n] = power_mod(fact[n], mod - 2, mod);
    for (int i = n - 1; i >= 0; i--) {
        inv_fact[i] = (inv_fact[i + 1] * (i + 1)) % mod;
    }
}

// 组合数 C(n, k) 模运算
long long comb_mod(int n, int k, const vector<long long>& fact, const vector<long long>& inv_fact, long long mod) {
    if (k < 0 || k > n) return 0;
    return fact[n] * inv_fact[k] % mod * inv_fact[n - k] % mod;
}

// 打印多项式
void print_polynomial(const vector<long long>& poly, const string& name) {
    cout << name << ": ";
    for (size_t i = 0; i < poly.size(); i++) {
        if (poly[i] != 0) {
            if (i > 0 && poly[i] > 0) {
                cout << " + ";
            }
            if (poly[i] < 0) {
                if (i > 0) cout << " ";
                cout << "- ";
            }
            if (abs(poly[i]) != 1 || i == 0) {
                cout << abs(poly[i]);
            }
            if (i > 0) {
                cout << "x^" << i;
            }
        }
    }
    cout << endl;
}

// 力扣第1758题：生成交替二进制字符串的最少操作次数
int min_changes(string s) {
    int changes_start_0 = 0; // 以0开头的交替字符串需要的最少修改次数
    int changes_start_1 = 0; // 以1开头的交替字符串需要的最少修改次数
    
    for (int i = 0; i < s.size(); i++) {
        if (i % 2 == 0) {
            // 偶数位置
            if (s[i] == '1') changes_start_0++;
            else changes_start_1++;
        } else {
            // 奇数位置
            if (s[i] == '0') changes_start_0++;
            else changes_start_1++;
        }
    }
    
    return min(changes_start_0, changes_start_1);
}

// 主函数 - 测试代码
int main() {
    // 测试多项式乘法（普通生成函数）
    cout << "=== 普通生成函数测试 ===" << endl;
    vector<long long> a = {1, 2, 3}; // 1 + 2x + 3x^2
    vector<long long> b = {4, 5, 6}; // 4 + 5x + 6x^2
    
    print_polynomial(a, "多项式A");
    print_polynomial(b, "多项式B");
    
    vector<long long> product = multiply_polynomials(a, b);
    print_polynomial(product, "乘积");
    
    // 测试组合数计算
    cout << "\n=== 组合数计算测试 ===" << endl;
    int n = 5, k = 3;
    vector<vector<long long>> C = compute_combinations(n, k);
    cout << "C(5, 3) = " << C[5][3] << endl;
    
    // 测试Burnside引理
    cout << "\n=== Burnside引理测试 ===" << endl;
    vector<long long> fixed_points = {4, 0, 0, 0}; // 正方形的4个旋转置换的不动点数
    long long equivalence_classes = burnside(4, fixed_points);
    cout << "等价类数目（正方形旋转）: " << equivalence_classes << endl;
    
    // 测试Polya定理
    cout << "\n=== Polya定理测试 ===" << endl;
    vector<int> rotations = {4, 1, 2, 1}; // 正方形的4个旋转置换的循环数
    long long colorings = polya(4, 2, rotations);
    cout << "用2种颜色给正方形顶点涂色的方案数: " << colorings << endl;
    
    // 测试项链问题
    cout << "\n=== 项链问题测试 ===" << endl;
    int beads = 5; // 5个珠子
    int colors = 3; // 3种颜色
    long long necklace_count = necklace(beads, colors);
    long long bracelet_count = bracelet(beads, colors);
    cout << beads << "个珠子，" << colors << "种颜色的项链方案数: " << necklace_count << endl;
    cout << beads << "个珠子，" << colors << "种颜色的手镯方案数: " << bracelet_count << endl;
    
    // 测试力扣第1758题
    cout << "\n=== 力扣第1758题测试 ===" << endl;
    string test1 = "0100";
    string test2 = "10";
    string test3 = "1111";
    cout << "输入: \"" << test1 << "\"，最少操作次数: " << min_changes(test1) << endl;
    cout << "输入: \"" << test2 << "\"，最少操作次数: " << min_changes(test2) << endl;
    cout << "输入: \"" << test3 << "\"，最少操作次数: " << min_changes(test3) << endl;
    
    /*
     * 生成函数和组合计数算法总结：
     * 
     * 1. 普通生成函数：
     *    - 用于计数组合问题，如物品选择、整数分拆等
     *    - 多项式乘法对应组合的合并
     *    - 时间复杂度：多项式乘法O(n²)，可以使用FFT优化到O(n log n)
     * 
     * 2. 指数生成函数：
     *    - 用于排列问题，考虑顺序的组合
     *    - 乘法规则与普通生成函数不同
     * 
     * 3. Burnside引理：
     *    - 计算群作用下的等价类数目
     *    - 公式：等价类数目 = (1/|G|) * Σ(不动点数目)
     *    - 适用于解决对称性计数问题
     * 
     * 4. Polya定理：
     *    - Burnside引理的特例，针对置换群作用下的计数问题
     *    - 特别适用于涂色问题
     *    - 公式：方案数 = (1/|G|) * Σ(k^c(π))，其中c(π)是置换π的循环数
     * 
     * 应用场景：
     * 1. 组合数学中的计数问题
     * 2. 离散数学中的群论应用
     * 3. 密码学中的哈希函数设计
     * 4. 计算机图形学中的对称性检测
     * 5. 分子生物学中的序列分析
     * 
     * 相关题目：
     * 1. 力扣第77题：组合 - 组合问题
     * 2. 力扣第46题：全排列 - 排列问题
     * 3. Burnside引理/Polya定理相关问题 - 对称计数问题
     */
    
    return 0;
}
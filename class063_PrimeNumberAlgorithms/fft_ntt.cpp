// FFT/NTT 算法实现
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)

#include <iostream>
#include <vector>
#include <complex>
#include <cmath>
#include <algorithm>

using namespace std;

const double PI = acos(-1.0);

// FFT (快速傅里叶变换)
// 将多项式转换为点值表示
void fft(vector<complex<double>>& a, bool invert) {
    int n = a.size();
    
    // 位反转置换
    for (int i = 1, j = 0; i < n; i++) {
        int bit = n >> 1;
        for (; j & bit; bit >>= 1) {
            j ^= bit;
        }
        j ^= bit;
        
        if (i < j) {
            swap(a[i], a[j]);
        }
    }
    
    // 蝴蝶操作
    for (int len = 2; len <= n; len <<= 1) {
        double ang = 2 * PI / len * (invert ? -1 : 1);
        complex<double> wlen(cos(ang), sin(ang));
        for (int i = 0; i < n; i += len) {
            complex<double> w(1);
            for (int j = 0; j < len / 2; j++) {
                complex<double> u = a[i + j];
                complex<double> v = a[i + j + len / 2] * w;
                a[i + j] = u + v;
                a[i + j + len / 2] = u - v;
                w *= wlen;
            }
        }
    }
    
    // 逆变换时需要除以n
    if (invert) {
        for (auto& x : a) {
            x /= n;
        }
    }
}

// 多项式乘法 (FFT实现)
vector<long long> multiply_fft(const vector<long long>& a, const vector<long long>& b) {
    vector<complex<double>> fa(a.begin(), a.end());
    vector<complex<double>> fb(b.begin(), b.end());
    
    // 计算需要的最小长度（2的幂次）
    int n = 1;
    while (n < a.size() + b.size() - 1) {
        n <<= 1;
    }
    
    fa.resize(n);
    fb.resize(n);
    
    // 执行FFT
    fft(fa, false);
    fft(fb, false);
    
    // 点值相乘
    for (int i = 0; i < n; i++) {
        fa[i] *= fb[i];
    }
    
    // 执行逆FFT
    fft(fa, true);
    
    // 转换为整数结果
    vector<long long> result(n);
    for (int i = 0; i < n; i++) {
        result[i] = round(fa[i].real());
    }
    
    // 移除末尾的零
    while (result.size() > 1 && result.back() == 0) {
        result.pop_back();
    }
    
    return result;
}

// NTT (数论变换) 模数版本
// 模数需要是形如 c*2^k + 1 的素数
// 常用模数: 998244353 (原根3), 1004535809 (原根3)
const int MOD = 998244353;
const int ROOT = 3; // 原根

// 快速幂
long long pow_mod(long long a, long long b, long long mod) {
    long long res = 1;
    while (b > 0) {
        if (b % 2 == 1) {
            res = res * a % mod;
        }
        a = a * a % mod;
        b /= 2;
    }
    return res;
}

// 数论逆元
long long inv_mod(long long a, long long mod) {
    return pow_mod(a, mod - 2, mod);
}

// NTT实现
void ntt(vector<long long>& a, bool invert) {
    int n = a.size();
    
    // 位反转置换
    for (int i = 1, j = 0; i < n; i++) {
        int bit = n >> 1;
        for (; j & bit; bit >>= 1) {
            j ^= bit;
        }
        j ^= bit;
        
        if (i < j) {
            swap(a[i], a[j]);
        }
    }
    
    // 蝴蝶操作
    for (int len = 2; len <= n; len <<= 1) {
        long long wlen = pow_mod(ROOT, (MOD - 1) / len, MOD);
        if (invert) {
            wlen = inv_mod(wlen, MOD);
        }
        for (int i = 0; i < n; i += len) {
            long long w = 1;
            for (int j = 0; j < len / 2; j++) {
                long long u = a[i + j];
                long long v = a[i + j + len / 2] * w % MOD;
                a[i + j] = (u + v) % MOD;
                a[i + j + len / 2] = (u - v + MOD) % MOD;
                w = w * wlen % MOD;
            }
        }
    }
    
    // 逆变换时需要处理
    if (invert) {
        long long inv_n = inv_mod(n, MOD);
        for (auto& x : a) {
            x = x * inv_n % MOD;
        }
    }
}

// 多项式乘法 (NTT实现)
vector<long long> multiply_ntt(const vector<long long>& a, const vector<long long>& b) {
    vector<long long> fa(a.begin(), a.end());
    vector<long long> fb(b.begin(), b.end());
    
    // 计算需要的最小长度（2的幂次）
    int n = 1;
    while (n < a.size() + b.size() - 1) {
        n <<= 1;
    }
    
    fa.resize(n);
    fb.resize(n);
    
    // 执行NTT
    ntt(fa, false);
    ntt(fb, false);
    
    // 点值相乘
    for (int i = 0; i < n; i++) {
        fa[i] = fa[i] * fb[i] % MOD;
    }
    
    // 执行逆NTT
    ntt(fa, true);
    
    // 移除末尾的零
    while (fa.size() > 1 && fa.back() == 0) {
        fa.pop_back();
    }
    
    return fa;
}

// 测试函数
int main() {
    // FFT测试
    vector<long long> a = {1, 2, 3};
    vector<long long> b = {4, 5, 6};
    vector<long long> res_fft = multiply_fft(a, b);
    
    cout << "FFT乘法结果: ";
    for (auto x : res_fft) {
        cout << x << " ";
    }
    cout << endl;
    
    // NTT测试
    vector<long long> c = {1, 2, 3};
    vector<long long> d = {4, 5, 6};
    vector<long long> res_ntt = multiply_ntt(c, d);
    
    cout << "NTT乘法结果: ";
    for (auto x : res_ntt) {
        cout << x << " ";
    }
    cout << endl;
    
    return 0;
}

// 算法优化说明：
// 1. 位反转置换使用了高效的递推方式
// 2. 蝴蝶操作通过迭代实现，避免了递归的栈开销
// 3. NTT中的模数选择对性能影响很大
// 4. 对于非常大的多项式，需要考虑内存优化

// 典型应用场景：
// 1. 大数乘法
// 2. 多项式卷积
// 3. 图像处理中的卷积操作
// 4. 字符串匹配（通过FFT加速）

// 边界情况处理：
// 1. 空多项式处理
// 2. 单元素多项式处理
// 3. 精度问题（FFT中的浮点误差）
// 4. 模数选择问题（NTT）

// 力扣相关题目：
// 1. 43. 字符串相乘 - 可以用FFT解决大数乘法
// 2. 372. 超级次方 - 可以用NTT优化

// 算法竞赛相关题目：
// 1. Codeforces 954I - Yet Another String Matching Problem
// 2. Codeforces 632E - Thief in a Shop
// 3. AtCoder ARC093F - Dark Horse
// 4. 洛谷 P3803 多项式乘法
// 5. 洛谷 P4721 分治FFT
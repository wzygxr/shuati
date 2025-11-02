// 离散傅里叶变换（DFT）的C++实现
// 时间复杂度：O(n²)
// 空间复杂度：O(n)

#include <iostream>
#include <vector>
#include <complex>
#include <cmath>
#include <iomanip>
#include <set>
#include <algorithm>
using namespace std;

using Complex = complex<double>;
const double PI = acos(-1);

// 计算前缀和
vector<int> prefixSum(const vector<int>& arr) {
    vector<int> prefix(arr.size() + 1, 0);
    for (int i = 0; i < arr.size(); i++) {
        prefix[i + 1] = prefix[i] + arr[i];
    }
    return prefix;
}

// 离散傅里叶变换（DFT）
// 时间复杂度：O(n²)
vector<Complex> dft(const vector<Complex>& a, bool invert = false) {
    int n = a.size();
    vector<Complex> result(n);
    
    // 计算旋转因子
    for (int k = 0; k < n; k++) {
        result[k] = 0;
        for (int j = 0; j < n; j++) {
            // 旋转因子 W_n^(kj) = e^(-2πikj/n) 或 W_n^(-kj) = e^(2πikj/n)
            double angle = 2 * PI * k * j / n;
            if (invert) {
                angle = -angle; // 逆变换时角度取反
            }
            Complex w(cos(angle), sin(angle));
            result[k] += a[j] * w;
        }
    }
    
    // 逆变换需要除以n
    if (invert) {
        for (auto& x : result) {
            x /= n;
        }
    }
    
    return result;
}

// 快速傅里叶变换（FFT）- 用于对比
// 时间复杂度：O(n log n)
void fft(vector<Complex>& a, bool invert = false) {
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
    
    // 迭代实现的FFT
    for (int len = 2; len <= n; len <<= 1) {
        double ang = 2 * PI / len * (invert ? -1 : 1);
        Complex wlen(cos(ang), sin(ang));
        for (int i = 0; i < n; i += len) {
            Complex w(1);
            for (int j = 0; j < len / 2; j++) {
                Complex u = a[i + j];
                Complex v = a[i + j + len / 2] * w;
                a[i + j] = u + v;
                a[i + j + len / 2] = u - v;
                w *= wlen;
            }
        }
    }
    
    if (invert) {
        for (auto& x : a) {
            x /= n;
        }
    }
}

// 多项式乘法 - 使用DFT
// 时间复杂度：O(n²)
vector<long long> multiply_polynomials_dft(const vector<long long>& a, const vector<long long>& b) {
    int n = 1;
    while (n < a.size() + b.size() - 1) {
        n <<= 1; // 向上取到2的幂次
    }
    
    // 转换为复数
    vector<Complex> fa(n), fb(n);
    for (int i = 0; i < a.size(); i++) {
        fa[i] = a[i];
    }
    for (int i = 0; i < b.size(); i++) {
        fb[i] = b[i];
    }
    
    // 进行DFT
    vector<Complex> fa_dft = dft(fa);
    vector<Complex> fb_dft = dft(fb);
    
    // 点乘
    vector<Complex> fc_dft(n);
    for (int i = 0; i < n; i++) {
        fc_dft[i] = fa_dft[i] * fb_dft[i];
    }
    
    // 逆DFT得到结果
    vector<Complex> fc = dft(fc_dft, true);
    
    // 转换回整数
    vector<long long> result(a.size() + b.size() - 1);
    for (int i = 0; i < result.size(); i++) {
        result[i] = round(fc[i].real());
    }
    
    return result;
}

// 多项式乘法 - 使用FFT
// 时间复杂度：O(n log n)
vector<long long> multiply_polynomials_fft(const vector<long long>& a, const vector<long long>& b) {
    int n = 1;
    while (n < a.size() + b.size() - 1) {
        n <<= 1;
    }
    
    vector<Complex> fa(n), fb(n);
    for (int i = 0; i < a.size(); i++) {
        fa[i] = a[i];
    }
    for (int i = 0; i < b.size(); i++) {
        fb[i] = b[i];
    }
    
    fft(fa);
    fft(fb);
    
    for (int i = 0; i < n; i++) {
        fa[i] *= fb[i];
    }
    
    fft(fa, true);
    
    vector<long long> result(a.size() + b.size() - 1);
    for (int i = 0; i < result.size(); i++) {
        result[i] = round(fa[i].real());
    }
    
    return result;
}

// 打印复数向量
void print_complex_vector(const vector<Complex>& v, const string& name) {
    cout << name << ":" << endl;
    for (const auto& x : v) {
        cout << "(" << x.real() << ", " << x.imag() << ")" << endl;
    }
}

// 打印整数向量
void print_vector(const vector<long long>& v, const string& name) {
    cout << name << ":" << endl;
    for (long long x : v) {
        cout << x << " ";
    }
    cout << endl;
}

// 计算算法执行时间
#include <chrono>
double measure_time(function<void()> func) {
    auto start = chrono::high_resolution_clock::now();
    func();
    auto end = chrono::high_resolution_clock::now();
    return chrono::duration<double, milli>(end - start).count();
}

int main() {
    // 测试DFT
    cout << "=== DFT测试 ===" << endl;
    vector<Complex> a = {1, 2, 3, 4};
    
    vector<Complex> a_dft = dft(a);
    print_complex_vector(a_dft, "DFT结果");
    
    vector<Complex> a_idft = dft(a_dft, true);
    print_complex_vector(a_idft, "逆DFT结果");
    
    // 测试多项式乘法
    cout << "\n=== 多项式乘法测试 ===" << endl;
    vector<long long> poly1 = {1, 2, 3};
    vector<long long> poly2 = {4, 5, 6};
    
    vector<long long> result_dft = multiply_polynomials_dft(poly1, poly2);
    print_vector(result_dft, "DFT多项式乘法结果");
    
    vector<long long> result_fft = multiply_polynomials_fft(poly1, poly2);
    print_vector(result_fft, "FFT多项式乘法结果");
    
    // 性能对比
    cout << "\n=== 性能对比测试 ===" << endl;
    int size = 1024;
    vector<Complex> large_vector(size);
    for (int i = 0; i < size; i++) {
        large_vector[i] = Complex(rand() % 10, rand() % 10);
    }
    
    double dft_time = measure_time([&]() {
        vector<Complex> temp = dft(large_vector);
    });
    
    double fft_time = measure_time([&]() {
        vector<Complex> temp = large_vector;
        fft(temp);
    });
    
    cout << "DFT 时间 (n=" << size << "): " << dft_time << " ms" << endl;
    cout << "FFT 时间 (n=" << size << "): " << fft_time << " ms" << endl;
    cout << "FFT 比 DFT 快约 " << dft_time / fft_time << " 倍" << endl;
    
    // 力扣第363题：矩形区域不超过 K 的最大数值和
// 使用二维前缀和优化
// 时间复杂度：O(m²*n*log(n))
// 空间复杂度：O(n)
int maxSumSubmatrix(vector<vector<int>>& matrix, int k) {
    if (matrix.empty() || matrix[0].empty()) {
        return 0;
    }
    
    int m = matrix.size();
    int n = matrix[0].size();
    int result = INT_MIN;
    
    // 枚举上下边界
    for (int top = 0; top < m; top++) {
        vector<int> sum(n, 0); // 每一列的和
        
        for (int bottom = top; bottom < m; bottom++) {
            // 更新每一列的和
            for (int col = 0; col < n; col++) {
                sum[col] += matrix[bottom][col];
            }
            
            // 在一维数组中找到最大子数组和不超过k的值
            // 使用set进行二分查找优化
            set<int> prefixSet;
            prefixSet.insert(0);
            int prefixSum = 0;
            
            for (int col = 0; col < n; col++) {
                prefixSum += sum[col];
                // 查找是否存在前缀和使得 prefixSum - previousPrefixSum <= k
                // 即 previousPrefixSum >= prefixSum - k
                auto it = prefixSet.lower_bound(prefixSum - k);
                if (it != prefixSet.end()) {
                    result = max(result, prefixSum - *it);
                }
                prefixSet.insert(prefixSum);
            }
        }
    }
    
    return result;
}

/*
     * 离散傅里叶变换（DFT）算法解释：
     * 1. DFT将时域信号转换为频域表示
     * 2. 基本公式：X[k] = Σ(x[n] * e^(-2πikn/N)) for n = 0..N-1
     * 3. 逆变换公式：x[n] = (1/N) * Σ(X[k] * e^(2πikn/N)) for k = 0..N-1
     * 
     * 时间复杂度分析：
     * - 直接DFT：O(n²)，因为需要计算n个k值，每个k值需要n次乘法和加法
     * - FFT（快速傅里叶变换）：O(n log n)，利用了旋转因子的周期性和对称性
     * 
     * 空间复杂度：
     * - O(n)，需要存储输入和输出数组
     * 
     * 应用场景：
     * 1. 信号处理和频谱分析
     * 2. 图像处理中的卷积和滤波
     * 3. 多项式乘法（如本题中的应用）
     * 4. 密码学中的某些算法
     * 5. 量子计算中的量子傅里叶变换
     * 
     * 相关题目：
     * 1. 力扣第43题：字符串相乘 - 大数乘法，可以使用FFT优化
     * 2. 力扣第363题：矩形区域不超过 K 的最大数值和 - 二维前缀和的应用
     * 3. Codeforces 954I：Yet Another String Matching Problem - 字符串匹配问题
     * 4. Codeforces 914G：Sum the Fibonacci - 斐波那契数列相关的卷积问题
     */
    
    return 0;
}
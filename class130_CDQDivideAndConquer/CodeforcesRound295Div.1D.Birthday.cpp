// Codeforces Round #295 (Div. 1) D. Birthday
// 平台: Codeforces
// 难度: 3000
// 标签: CDQ分治, 字符串, FFT
// 链接: https://codeforces.com/problemset/problem/528/D
// 
// 题目描述:
// 给定两个字符串s和t，以及一个整数k
// 定义字符c在位置i是"好的"，如果存在位置j满足|i-j| <= k且s[j] = c
// 对于t的每个长度为|s|的子串，判断是否每个字符在s中都有对应的"好的"位置
// 
// 解题思路:
// 1. 使用CDQ分治思想将问题分解
// 2. 对于每个字符，预处理其在s中的"好的"位置
// 3. 使用FFT（快速傅里叶变换）进行字符串匹配
// 4. 对于每个字符，计算匹配度
// 5. 综合所有字符的匹配结果
// 
// 时间复杂度: O(n log n) 使用FFT优化

#include <bits/stdc++.h>
using namespace std;

const int MAXN = 200005;
const double PI = acos(-1.0);

struct Complex {
    double real, imag;
    Complex(double r = 0, double i = 0) : real(r), imag(i) {}
    
    Complex operator+(const Complex& other) const {
        return Complex(real + other.real, imag + other.imag);
    }
    
    Complex operator-(const Complex& other) const {
        return Complex(real - other.real, imag - other.imag);
    }
    
    Complex operator*(const Complex& other) const {
        return Complex(real * other.real - imag * other.imag,
                      real * other.imag + imag * other.real);
    }
};

// FFT实现
void fft(vector<Complex>& a, bool invert) {
    int n = a.size();
    
    for (int i = 1, j = 0; i < n; i++) {
        int bit = n >> 1;
        for (; j >= bit; bit >>= 1) {
            j -= bit;
        }
        j += bit;
        if (i < j) {
            swap(a[i], a[j]);
        }
    }
    
    for (int len = 2; len <= n; len <<= 1) {
        double angle = 2 * PI / len * (invert ? -1 : 1);
        Complex wlen(cos(angle), sin(angle));
        
        for (int i = 0; i < n; i += len) {
            Complex w(1);
            for (int j = 0; j < len / 2; j++) {
                Complex u = a[i + j];
                Complex v = a[i + j + len / 2] * w;
                a[i + j] = u + v;
                a[i + j + len / 2] = u - v;
                w = w * wlen;
            }
        }
    }
    
    if (invert) {
        for (int i = 0; i < n; i++) {
            a[i].real /= n;
        }
    }
}

// 字符串匹配函数
vector<int> stringMatch(const string& s, const string& t, int k) {
    int n = s.length();
    int m = t.length();
    
    // 预处理每个字符的"好的"位置
    vector<vector<bool>> good(4, vector<bool>(n, false));
    map<char, int> charToIndex = {{'A', 0}, {'T', 1}, {'G', 2}, {'C', 3}};
    
    // 使用滑动窗口预处理每个字符的"好的"位置
    for (int c = 0; c < 4; c++) {
        vector<int> prefix(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + (charToIndex[s[i]] == c ? 1 : 0);
        }
        
        for (int i = 0; i < n; i++) {
            int left = max(0, i - k);
            int right = min(n - 1, i + k);
            if (prefix[right + 1] - prefix[left] > 0) {
                good[c][i] = true;
            }
        }
    }
    
    vector<int> result(m - n + 1, 1);
    
    // 对每个字符分别处理
    for (int c = 0; c < 4; c++) {
        // 构建多项式
        int len = 1;
        while (len < n + m) len <<= 1;
        
        vector<Complex> a(len), b(len);
        
        // s的多项式（反转）
        for (int i = 0; i < n; i++) {
            a[i] = good[c][n - 1 - i] ? 1.0 : 0.0;
        }
        
        // t的多项式
        for (int i = 0; i < m; i++) {
            b[i] = (charToIndex[t[i]] == c) ? 1.0 : 0.0;
        }
        
        // FFT计算卷积
        fft(a, false);
        fft(b, false);
        
        for (int i = 0; i < len; i++) {
            a[i] = a[i] * b[i];
        }
        
        fft(a, true);
        
        // 检查匹配结果
        for (int i = 0; i <= m - n; i++) {
            int matchCount = round(a[n - 1 + i].real);
            int requiredCount = 0;
            
            // 计算t子串中该字符的数量
            for (int j = i; j < i + n; j++) {
                if (charToIndex[t[j]] == c) {
                    requiredCount++;
                }
            }
            
            if (matchCount < requiredCount) {
                result[i] = 0;
            }
        }
    }
    
    return result;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m, k;
    string s, t;
    cin >> n >> m >> k;
    cin >> s >> t;
    
    vector<int> result = stringMatch(s, t, k);
    
    int count = 0;
    for (int i = 0; i < result.size(); i++) {
        if (result[i] == 1) {
            count++;
        }
    }
    
    cout << count << endl;
    
    return 0;
}
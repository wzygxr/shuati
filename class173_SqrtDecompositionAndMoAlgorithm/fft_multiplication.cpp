#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>
#include <chrono>
#include <random>
#include <complex>

/**
 * FFT乘法算法实现
 * 使用快速傅里叶变换将整数乘法转换为多项式乘法，时间复杂度达到O(n log n)
 * 传统乘法: O(n²)，Karatsuba: O(n^1.585)，Toom-Cook (k=3): O(n^1.465)，FFT: O(n log n)
 * 适用于非常大的高精度整数乘法计算
 */

using namespace std;
using namespace std::chrono;
using namespace std::complex_literals; // 用于复数字面量

/**
 * 使用传统方法进行大整数乘法（用于比较性能）
 * 时间复杂度：O(n²)
 * 
 * @param x 第一个整数的数字向量表示（低位在前）
 * @param y 第二个整数的数字向量表示（低位在前）
 * @return 乘积的数字向量表示（低位在前）
 */
vector<int> naiveMultiply(const vector<int>& x, const vector<int>& y) {
    int n = x.size();
    int m = y.size();
    vector<int> result(n + m, 0);

    // 逐位相乘并累加
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            result[i + j] += x[i] * y[j];
            // 处理进位
            result[i + j + 1] += result[i + j] / 10;
            result[i + j] %= 10;
        }
    }

    // 移除前导零
    int lastNonZero = n + m - 1;
    while (lastNonZero > 0 && result[lastNonZero] == 0) {
        lastNonZero--;
    }

    return vector<int>(result.begin(), result.begin() + lastNonZero + 1);
}

/**
 * 处理大整数乘法结果中的进位
 * 
 * @param result 乘法结果数字向量（将被原地修改）
 */
void processCarries(vector<int>& result) {
    int carry = 0;
    for (int i = 0; i < (int)result.size(); i++) {
        int sum = result[i] + carry;
        result[i] = sum % 10;
        carry = sum / 10;
    }

    // 如果还有进位，需要扩展向量
    while (carry > 0) {
        result.push_back(carry % 10);
        carry /= 10;
    }

    // 移除末尾的零（前导零）
    while (result.size() > 1 && result.back() == 0) {
        result.pop_back();
    }
}

/**
 * 快速傅里叶变换实现
 * 使用迭代版本的Cooley-Tukey算法，性能更好
 * 
 * @param a 输入复数向量
 * @param inverse 是否为逆FFT
 */
void fft(vector<complex<double>>& a, bool inverse) {
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

    // 迭代版本的FFT
    for (int len = 2; len <= n; len <<= 1) {
        double ang = 2 * M_PI / len * (inverse ? -1 : 1);
        complex<double> wlen(cos(ang), sin(ang));
        for (int i = 0; i < n; i += len) {
            complex<double> w(1);
            for (int j = 0; j < len / 2; j++) {
                complex<double> u = a[i + j];
                complex<double> v = a[i + j + len/2] * w;
                a[i + j] = u + v;
                a[i + j + len/2] = u - v;
                w *= wlen;
            }
        }
    }

    // 逆FFT需要除以n
    if (inverse) {
        for (auto& x : a) {
            x /= n;
        }
    }
}

/**
 * 使用FFT进行大整数乘法
 * 时间复杂度：O(n log n)
 * 
 * @param a 第一个整数的数字向量表示（低位在前）
 * @param b 第二个整数的数字向量表示（低位在前）
 * @return 乘积的数字向量表示（低位在前）
 */
vector<int> fftMultiply(const vector<int>& a, const vector<int>& b) {
    vector<complex<double>> fa(a.begin(), a.end());
    vector<complex<double>> fb(b.begin(), b.end());

    // 计算大于等于a.size() + b.size() - 1的最小的2的幂次
    int n = 1;
    while (n < (int)(a.size() + b.size() - 1)) {
        n <<= 1;
    }

    // 补零到2的幂次长度
    fa.resize(n);
    fb.resize(n);

    // 执行FFT
    fft(fa, false);
    fft(fb, false);

    // 点乘得到频域乘积
    for (int i = 0; i < n; i++) {
        fa[i] *= fb[i];
    }

    // 执行逆FFT得到时域结果
    fft(fa, true);

    // 将复数结果转换为整数
    vector<int> result(n);
    for (int i = 0; i < n; i++) {
        // 四舍五入到最近的整数
        result[i] = round(fa[i].real());
    }

    // 处理进位
    processCarries(result);

    return result;
}

/**
 * 将字符串转换为数字向量（低位在前）
 * 
 * @param s 数字字符串
 * @return 数字向量（低位在前）
 */
vector<int> stringToDigits(const string& s) {
    vector<int> digits;
    digits.reserve(s.size());
    for (int i = 0; i < (int)s.size(); i++) {
        digits.push_back(s[s.size() - 1 - i] - '0');
    }
    return digits;
}

/**
 * 将数字向量转换为字符串（低位在前转换为正常表示）
 * 
 * @param digits 数字向量（低位在前）
 * @return 数字字符串
 */
string digitsToString(const vector<int>& digits) {
    if (digits.empty()) {
        return "0";
    }
    string result;
    result.reserve(digits.size());
    for (int i = digits.size() - 1; i >= 0; i--) {
        result.push_back(digits[i] + '0');
    }
    return result;
}

/**
 * 生成指定长度的随机数字字符串
 * 
 * @param length 字符串长度
 * @return 随机数字字符串
 */
string generateRandomNumber(int length) {
    static random_device rd;
    static mt19937 gen(rd());
    static uniform_int_distribution<> dis09(0, 9);
    static uniform_int_distribution<> dis19(1, 9);

    string result;
    result.reserve(length);
    // 第一位不能是0
    result.push_back(dis19(gen) + '0');
    // 生成剩余位
    for (int i = 1; i < length; i++) {
        result.push_back(dis09(gen) + '0');
    }
    return result;
}

/**
 * 性能测试方法，比较FFT算法与其他算法
 * 
 * @param size 测试数字的位数
 */
void benchmark(int size) {
    // 生成测试用的大整数
    string num1 = generateRandomNumber(size);
    string num2 = generateRandomNumber(size);

    // 转换为数字向量（低位在前）
    vector<int> digits1 = stringToDigits(num1);
    vector<int> digits2 = stringToDigits(num2);

    // 测试FFT算法
    auto start = high_resolution_clock::now();
    vector<int> resultFFT = fftMultiply(digits1, digits2);
    string resultFFTstr = digitsToString(resultFFT);
    auto end = high_resolution_clock::now();
    auto fftTime = duration_cast<microseconds>(end - start).count() / 1000.0; // 转换为毫秒

    // 测试传统算法（对于较小的数字）
    string resultNaive = "";
    double naiveTime = 0;
    if (size <= 500) { // 对于大数字，传统算法可能太慢
        start = high_resolution_clock::now();
        vector<int> naiveResult = naiveMultiply(digits1, digits2);
        resultNaive = digitsToString(naiveResult);
        end = high_resolution_clock::now();
        naiveTime = duration_cast<microseconds>(end - start).count() / 1000.0; // 转换为毫秒
    }

    cout << "数字位数: " << size << endl;
    cout << "FFT算法耗时: " << fftTime << " ms" << endl;
    
    if (size <= 500) {
        cout << "传统算法耗时: " << naiveTime << " ms" << endl;
        cout << "算法加速比 (传统/FFT): " << naiveTime / fftTime << "x" << endl;
        cout << "结果一致 (FFT vs 传统): " << (resultFFTstr == resultNaive) << endl;
    }
    cout << "乘积位数: " << resultFFTstr.length() << endl;
}

/**
 * 验证算法正确性
 */
void verifyCorrectness() {
    vector<pair<string, string>> testCases = {
        {"1234", "5678"},
        {"9999", "9999"},
        {"0", "12345"},
        {"1", "98765"},
        {"999999", "999999"}
    };

    for (const auto& testCase : testCases) {
        const string& x = testCase.first;
        const string& y = testCase.second;
        
        // 使用FFT算法
        vector<int> xDigits = stringToDigits(x);
        vector<int> yDigits = stringToDigits(y);
        vector<int> product = fftMultiply(xDigits, yDigits);
        string result = digitsToString(product);
        
        // 对于小数字，使用C++内置的大整数类型验证
        if (x.length() <= 20 && y.length() <= 20) { // 确保可以放入unsigned long long
            unsigned long long num1 = stoull(x);
            unsigned long long num2 = stoull(y);
            unsigned long long expectedValue = num1 * num2;
            string expected = to_string(expectedValue);
            cout << x << " * " << y << " = " << result 
                 << " (正确: " << (result == expected) << ")" << endl;
        } else {
            cout << x << " * " << y << " = " << result << endl;
            cout << "乘积位数: " << result.length() << endl;
        }
    }
}

/**
 * 交互式测试函数
 */
void interactiveMode() {
    cout << "请输入两个大整数进行乘法计算（输入exit退出）:" << endl;
    string num1, num2;
    while (true) {
        cout << "第一个数: " << endl;
        getline(cin, num1);
        if (num1 == "exit") break;
        
        cout << "第二个数: " << endl;
        getline(cin, num2);
        if (num2 == "exit") break;
        
        // 验证输入是否为有效数字
        bool validInput = true;
        for (char c : num1) {
            if (!isdigit(c)) {
                validInput = false;
                break;
            }
        }
        for (char c : num2) {
            if (!isdigit(c)) {
                validInput = false;
                break;
            }
        }
        
        if (!validInput) {
            cout << "错误: 请输入有效的正整数" << endl;
            continue;
        }
        
        // 转换为数字向量并执行FFT乘法
        vector<int> digits1 = stringToDigits(num1);
        vector<int> digits2 = stringToDigits(num2);
        
        auto start = high_resolution_clock::now();
        vector<int> product = fftMultiply(digits1, digits2);
        string result = digitsToString(product);
        auto end = high_resolution_clock::now();
        double time_taken = duration_cast<microseconds>(end - start).count() / 1000.0;
        
        cout << "结果: " << result << endl;
        cout << "计算耗时: " << time_taken << " ms" << endl;
    }
}

int main() {
    cout << "验证算法正确性:" << endl;
    verifyCorrectness();
    
    cout << "\n性能测试:" << endl;
    benchmark(100);
    benchmark(1000);
    benchmark(5000);
    
    try {
        interactiveMode();
    } catch (const exception& e) {
        cerr << "发生错误: " << e.what() << endl;
    }
    
    return 0;
}
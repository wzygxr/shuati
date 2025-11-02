#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <ctime>
#include <random>
#include <chrono>

/**
 * Toom-Cook乘法算法实现
 * 是Karatsuba算法的一般化，通过更高阶的分治策略进一步降低时间复杂度
 * 传统乘法: O(n²)，Karatsuba: O(n^1.585)，Toom-Cook (k=3): O(n^1.465)
 * 适用于高精度大整数乘法计算
 */

using namespace std;
using namespace std::chrono;

/**
 * 使用传统方法进行大整数乘法
 * 时间复杂度：O(n²)
 * 作为Toom-Cook算法的基础情况
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
 * 将数字向量添加到结果向量的指定位置
 * 
 * @param result 结果向量
 * @param addend 要添加的数字向量
 * @param offset 起始位置
 */
void addToResult(vector<int>& result, const vector<int>& addend, int offset) {
    int newLength = max((int)result.size(), (int)(addend.size() + offset));
    if (newLength > (int)result.size()) {
        result.resize(newLength, 0);
    }

    for (int i = 0; i < (int)addend.size(); i++) {
        if (i + offset < (int)result.size()) {
            result[i + offset] += addend[i];
            // 处理进位
            int pos = i + offset;
            while (pos < (int)result.size() - 1 && result[pos] >= 10) {
                result[pos + 1] += result[pos] / 10;
                result[pos] %= 10;
                pos++;
            }
            // 处理最高位的进位
            if (pos == (int)result.size() - 1 && result[pos] >= 10) {
                int carry = result[pos] / 10;
                result[pos] %= 10;
                result.push_back(carry);
            }
        }
    }
}

/**
 * 对两个数字向量进行加法操作
 * 
 * @param a 第一个数字向量（低位在前）
 * @param b 第二个数字向量（低位在前）
 * @return 和的数字向量（低位在前）
 */
vector<int> addLists(const vector<int>& a, const vector<int>& b) {
    int maxLength = max((int)a.size(), (int)b.size());
    vector<int> result(maxLength + 1, 0);

    for (int i = 0; i < maxLength; i++) {
        int digitA = (i < (int)a.size()) ? a[i] : 0;
        int digitB = (i < (int)b.size()) ? b[i] : 0;
        result[i] = digitA + digitB;

        // 处理进位
        result[i + 1] += result[i] / 10;
        result[i] %= 10;
    }

    // 移除前导零
    int lastNonZero = maxLength;
    while (lastNonZero > 0 && result[lastNonZero] == 0) {
        lastNonZero--;
    }

    return vector<int>(result.begin(), result.begin() + lastNonZero + 1);
}

/**
 * 对两个数字向量进行减法操作
 * 假设 a >= b
 * 
 * @param a 被减数的数字向量（低位在前）
 * @param b 减数的数字向量（低位在前）
 * @return 差的数字向量（低位在前）
 */
vector<int> subtractLists(const vector<int>& a, const vector<int>& b) {
    vector<int> result(a.begin(), a.end());

    for (int i = 0; i < (int)b.size(); i++) {
        result[i] -= b[i];
    }

    // 处理借位
    for (int i = 0; i < (int)result.size() - 1; i++) {
        while (result[i] < 0) {
            result[i] += 10;
            result[i + 1]--;
        }
    }

    // 移除前导零
    int lastNonZero = result.size() - 1;
    while (lastNonZero > 0 && result[lastNonZero] == 0) {
        lastNonZero--;
    }

    return vector<int>(result.begin(), result.begin() + lastNonZero + 1);
}

/**
 * 将数字向量乘以2的幂（乘以2^power）
 * 
 * @param arr 数字向量（低位在前）
 * @param power 2的幂次
 * @return 结果向量（低位在前）
 */
vector<int> multiplyByPowerOfTwo(const vector<int>& arr, int power) {
    vector<int> result(arr.begin(), arr.end());
    
    for (int p = 0; p < power; p++) {
        int carry = 0;
        for (int i = 0; i < (int)result.size(); i++) {
            int product = result[i] * 2 + carry;
            result[i] = product % 10;
            carry = product / 10;
        }
        if (carry > 0) {
            result.push_back(carry);
        }
    }
    
    return result;
}

/**
 * 将数字向量除以2
 * 
 * @param arr 数字向量（低位在前）
 * @return 结果向量（低位在前）
 */
vector<int> divideByTwo(const vector<int>& arr) {
    vector<int> result(arr.size(), 0);
    int remainder = 0;
    
    // 从高位开始除（向量的末尾）
    for (int i = arr.size() - 1; i >= 0; i--) {
        int current = arr[i] + remainder * 10;
        result[i] = current / 2;
        remainder = current % 2;
    }
    
    // 移除前导零
    int lastNonZero = result.size() - 1;
    while (lastNonZero > 0 && result[lastNonZero] == 0) {
        lastNonZero--;
    }
    
    return vector<int>(result.begin(), result.begin() + lastNonZero + 1);
}

/**
 * 将数字向量除以6
 * 
 * @param arr 数字向量（低位在前）
 * @return 结果向量（低位在前）
 */
vector<int> divideBySix(const vector<int>& arr) {
    vector<int> result(arr.size(), 0);
    int remainder = 0;
    
    // 从高位开始除（向量的末尾）
    for (int i = arr.size() - 1; i >= 0; i--) {
        int current = arr[i] + remainder * 10;
        result[i] = current / 6;
        remainder = current % 6;
    }
    
    // 移除前导零
    int lastNonZero = result.size() - 1;
    while (lastNonZero > 0 && result[lastNonZero] == 0) {
        lastNonZero--;
    }
    
    return vector<int>(result.begin(), result.begin() + lastNonZero + 1);
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
    string result;
    result.reserve(digits.size());
    for (int i = digits.size() - 1; i >= 0; i--) {
        result.push_back(digits[i] + '0');
    }
    return result;
}

/**
 * Toom-Cook (Toom-3) 递归乘法实现
 * 
 * @param x 第一个整数的数字向量表示（低位在前）
 * @param y 第二个整数的数字向量表示（低位在前）
 * @return 乘积的数字向量表示（低位在前）
 */
vector<int> toomCookMultiplyRecursive(const vector<int>& x, const vector<int>& y) {
    int n = max((int)x.size(), (int)y.size());
    
    // 基础情况：当向量长度较小时，使用传统乘法
    if (n <= 128) { // 阈值可以根据实际情况调整
        return naiveMultiply(x, y);
    }

    // 计算分割点，将数字分成三个部分
    int m = (n + 2) / 3; // 向上取整到3

    // 分割x为三个部分: x = x2*10^(2*m) + x1*10^m + x0
    vector<int> x0(x.begin(), x.begin() + min(m, (int)x.size()));
    vector<int> x1;
    if (m < (int)x.size()) {
        x1 = vector<int>(x.begin() + m, x.begin() + min(2*m, (int)x.size()));
    }
    vector<int> x2;
    if (2*m < (int)x.size()) {
        x2 = vector<int>(x.begin() + 2*m, x.end());
    }

    // 分割y为三个部分: y = y2*10^(2*m) + y1*10^m + y0
    vector<int> y0(y.begin(), y.begin() + min(m, (int)y.size()));
    vector<int> y1;
    if (m < (int)y.size()) {
        y1 = vector<int>(y.begin() + m, y.begin() + min(2*m, (int)y.size()));
    }
    vector<int> y2;
    if (2*m < (int)y.size()) {
        y2 = vector<int>(y.begin() + 2*m, y.end());
    }

    // 计算f(k)和g(k)在k=-1,0,1,2,∞处的值
    // f(0) = x0, f(1) = x0+x1+x2, f(-1) = x0-x1+x2, f(2) = x0+2x1+4x2, f(∞) = x2
    // g(0) = y0, g(1) = y0+y1+y2, g(-1) = y0-y1+y2, g(2) = y0+2y1+4y2, g(∞) = y2
    vector<int> f0 = x0;
    vector<int> f1 = addLists(addLists(x0, x1), x2);
    vector<int> fNeg1 = addLists(subtractLists(x0, x1), x2);
    vector<int> f2 = addLists(addLists(x0, multiplyByPowerOfTwo(x1, 1)), multiplyByPowerOfTwo(x2, 2));
    vector<int> fInfty = x2;

    vector<int> g0 = y0;
    vector<int> g1 = addLists(addLists(y0, y1), y2);
    vector<int> gNeg1 = addLists(subtractLists(y0, y1), y2);
    vector<int> g2 = addLists(addLists(y0, multiplyByPowerOfTwo(y1, 1)), multiplyByPowerOfTwo(y2, 2));
    vector<int> gInfty = y2;

    // 计算乘积h(k) = f(k)*g(k) 在各点的值
    vector<int> h0 = toomCookMultiplyRecursive(f0, g0); // h(0) = x0*y0
    vector<int> h1 = toomCookMultiplyRecursive(f1, g1); // h(1) = (x0+x1+x2)*(y0+y1+y2)
    vector<int> hNeg1 = toomCookMultiplyRecursive(fNeg1, gNeg1); // h(-1) = (x0-x1+x2)*(y0-y1+y2)
    vector<int> h2 = toomCookMultiplyRecursive(f2, g2); // h(2) = (x0+2x1+4x2)*(y0+2y1+4y2)
    vector<int> hInfty = toomCookMultiplyRecursive(fInfty, gInfty); // h(∞) = x2*y2

    // 使用拉格朗日插值法求解多项式系数
    // h(z) = z^4*h_4 + z^3*h_3 + z^2*h_2 + z*h_1 + h_0
    // 其中h_4 = hInfty

    // 计算中间值
    vector<int> a = h1; // h(1)
    vector<int> b = hNeg1; // h(-1)
    vector<int> c = h2; // h(2)
    vector<int> d = h0; // h(0)

    // 通过插值公式计算h3, h2, h1
    // h3 = (c - 8a + 6b - d) / 6
    vector<int> term1 = subtractLists(c, multiplyByPowerOfTwo(a, 3)); // c - 8a
    vector<int> term2 = addLists(multiplyByPowerOfTwo(b, 2), multiplyByPowerOfTwo(b, 1)); // 6b
    vector<int> numerator = subtractLists(addLists(term1, term2), d);
    vector<int> h3 = divideBySix(numerator);

    // h2 = (a + b - 2d - 6h3 - 2h4) / 2
    vector<int> h4 = hInfty;
    vector<int> term3 = addLists(a, b);
    vector<int> term4 = addLists(multiplyByPowerOfTwo(d, 1), 
                                addLists(multiplyByPowerOfTwo(h3, 2), multiplyByPowerOfTwo(h3, 1))); // 2d + 6h3
    vector<int> term5 = multiplyByPowerOfTwo(h4, 1); // 2h4
    numerator = subtractLists(subtractLists(term3, term4), term5);
    vector<int> h2_coeff = divideByTwo(numerator);

    // h1 = (a - b) / 2 - 2h3 - 3h4
    term1 = divideByTwo(subtractLists(a, b));
    term2 = addLists(multiplyByPowerOfTwo(h3, 1), 
                    addLists(multiplyByPowerOfTwo(h4, 1), h4)); // 2h3 + 3h4
    vector<int> h1_coeff = subtractLists(term1, term2);

    // 现在我们有了所有系数：h4, h3, h2, h1, h0
    // 组合结果: h4*10^(4*m) + h3*10^(3*m) + h2*10^(2*m) + h1*10^m + h0
    vector<int> result(5 * m, 0); // 可能需要调整大小

    // 添加各部分到结果中
    addToResult(result, h0, 0);
    addToResult(result, h1_coeff, m);
    addToResult(result, h2_coeff, 2 * m);
    addToResult(result, h3, 3 * m);
    addToResult(result, h4, 4 * m);

    // 移除前导零
    int lastNonZero = result.size() - 1;
    while (lastNonZero > 0 && result[lastNonZero] == 0) {
        lastNonZero--;
    }

    return vector<int>(result.begin(), result.begin() + lastNonZero + 1);
}

/**
 * 使用Toom-Cook乘法算法进行大整数乘法
 * 这里实现了Toom-3算法，是Toom-Cook的三阶版本
 * 时间复杂度：O(n^log₃5) ≈ O(n^1.465)
 * 
 * @param x 第一个整数的字符串表示
 * @param y 第二个整数的字符串表示
 * @return 乘积的字符串表示
 */
string toomCookMultiply(const string& x, const string& y) {
    // 处理特殊情况
    if (x == "0" || y == "0") {
        return "0";
    }
    if (x == "1") {
        return y;
    }
    if (y == "1") {
        return x;
    }

    // 将字符串转换为数字向量（低位在前）
    vector<int> xDigits = stringToDigits(x);
    vector<int> yDigits = stringToDigits(y);

    // 调用递归Toom-Cook算法
    vector<int> product = toomCookMultiplyRecursive(xDigits, yDigits);

    // 移除前导零并转换为字符串
    return digitsToString(product);
}

// Karatsuba算法实现（用于性能比较）
vector<int> karatsubaRecursive(const vector<int>& a, const vector<int>& b);

string karatsubaMultiply(const string& x, const string& y) {
    if (x == "0" || y == "0") {
        return "0";
    }
    if (x == "1") {
        return y;
    }
    if (y == "1") {
        return x;
    }

    vector<int> xDigits = stringToDigits(x);
    vector<int> yDigits = stringToDigits(y);

    int maxLength = max((int)xDigits.size(), (int)yDigits.size());
    int n = 1;
    while (n < maxLength) {
        n <<= 1;
    }

    xDigits.resize(n, 0);
    yDigits.resize(n, 0);

    vector<int> product = karatsubaRecursive(xDigits, yDigits);
    return digitsToString(product);
}

vector<int> karatsubaRecursive(const vector<int>& a, const vector<int>& b) {
    int n = a.size();
    if (n <= 64) {
        return naiveMultiply(a, b);
    }

    int m = n / 2;
    vector<int> aHigh(a.begin() + m, a.end());
    vector<int> aLow(a.begin(), a.begin() + m);
    vector<int> bHigh(b.begin() + m, b.end());
    vector<int> bLow(b.begin(), b.begin() + m);

    vector<int> z1 = karatsubaRecursive(aHigh, bHigh);
    vector<int> z3 = karatsubaRecursive(aLow, bLow);
    
    vector<int> aSum = addLists(aHigh, aLow);
    vector<int> bSum = addLists(bHigh, bLow);
    vector<int> z2 = karatsubaRecursive(aSum, bSum);
    
    z2 = subtractLists(z2, addLists(z1, z3));

    vector<int> result(2 * n, 0);
    addToResult(result, z3, 0);
    addToResult(result, z2, m);
    addToResult(result, z1, 2 * m);

    int lastNonZero = result.size() - 1;
    while (lastNonZero > 0 && result[lastNonZero] == 0) {
        lastNonZero--;
    }
    return vector<int>(result.begin(), result.begin() + lastNonZero + 1);
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
 * 性能测试方法，比较Toom-Cook算法与其他算法
 * 
 * @param size 测试数字的位数
 */
void benchmark(int size) {
    // 生成测试用的大整数
    string num1 = generateRandomNumber(size);
    string num2 = generateRandomNumber(size);

    // 测试Toom-Cook算法
    auto start = high_resolution_clock::now();
    string resultToomCook = toomCookMultiply(num1, num2);
    auto end = high_resolution_clock::now();
    auto toomCookTime = duration_cast<microseconds>(end - start).count() / 1000.0; // 转换为毫秒

    // 测试Karatsuba算法
    start = high_resolution_clock::now();
    string resultKaratsuba = karatsubaMultiply(num1, num2);
    end = high_resolution_clock::now();
    auto karatsubaTime = duration_cast<microseconds>(end - start).count() / 1000.0; // 转换为毫秒

    // 测试传统算法（对于较小的数字）
    string resultNaive = "";
    double naiveTime = 0;
    if (size <= 500) { // 对于大数字，传统算法可能太慢
        vector<int> digits1 = stringToDigits(num1);
        vector<int> digits2 = stringToDigits(num2);
        start = high_resolution_clock::now();
        vector<int> naiveResult = naiveMultiply(digits1, digits2);
        resultNaive = digitsToString(naiveResult);
        end = high_resolution_clock::now();
        naiveTime = duration_cast<microseconds>(end - start).count() / 1000.0; // 转换为毫秒
    }

    cout << "数字位数: " << size << endl;
    cout << "Toom-Cook算法耗时: " << toomCookTime << " ms" << endl;
    cout << "Karatsuba算法耗时: " << karatsubaTime << " ms" << endl;
    cout << "算法加速比 (Karatsuba/Toom-Cook): " << karatsubaTime / toomCookTime << "x" << endl;
    
    if (size <= 500) {
        cout << "传统算法耗时: " << naiveTime << " ms" << endl;
        cout << "算法加速比 (传统/Toom-Cook): " << naiveTime / toomCookTime << "x" << endl;
        cout << "结果一致 (Toom-Cook vs 传统): " << (resultToomCook == resultNaive) << endl;
    }
    cout << "结果一致 (Toom-Cook vs Karatsuba): " << (resultToomCook == resultKaratsuba) << endl;
    cout << "乘积位数: " << resultToomCook.length() << endl;
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
        
        // 使用Toom-Cook算法
        string result = toomCookMultiply(x, y);
        
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
        
        auto start = high_resolution_clock::now();
        string result = toomCookMultiply(num1, num2);
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
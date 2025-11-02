#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <random>
#include <cctype>

/**
 * Karatsuba乘法算法实现
 * 传统的乘法算法时间复杂度为O(n²)，而Karatsuba算法将其优化至约O(n^1.585)
 * 适用于高精度大整数乘法计算
 */

class KaratsubaMultiplication {
private:
    /**
     * 使用传统方法进行大整数乘法
     * 时间复杂度：O(n²)
     * 作为Karatsuba算法的基础情况
     * 
     * @param x 第一个整数的数字向量表示（低位在前）
     * @param y 第二个整数的数字向量表示（低位在前）
     * @return 乘积的数字向量表示（低位在前）
     */
    static std::vector<int> naiveMultiply(const std::vector<int>& x, const std::vector<int>& y) {
        int n = x.size();
        int m = y.size();
        std::vector<int> result(n + m, 0);

        // 逐位相乘并累加
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i + j] += x[i] * y[j];
                // 处理进位
                result[i + j + 1] += result[i + j] / 10;
                result[i + j] %= 10;
            }
        }

        // 移除前导零（实际上是向量末尾的零）
        int lastNonZero = n + m - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        return std::vector<int>(result.begin(), result.begin() + lastNonZero + 1);
    }

    /**
     * Karatsuba递归乘法实现
     * 
     * @param x 第一个整数的数字向量表示（低位在前）
     * @param y 第二个整数的数字向量表示（低位在前）
     * @return 乘积的数字向量表示（低位在前）
     */
    static std::vector<int> karatsubaMultiplyRecursive(const std::vector<int>& x, const std::vector<int>& y) {
        int n = x.size();
        
        // 基础情况：当向量长度较小时，使用传统乘法
        if (n <= 64) { // 阈值可以根据实际情况调整
            return naiveMultiply(x, y);
        }

        // 计算中点
        int m = n / 2;

        // 分割向量
        std::vector<int> a(x.begin() + m, x.end()); // 高位部分
        std::vector<int> b(x.begin(), x.begin() + m); // 低位部分
        std::vector<int> c(y.begin() + m, y.end()); // 高位部分
        std::vector<int> d(y.begin(), y.begin() + m); // 低位部分

        // 计算三个子乘积
        // z1 = a * c
        std::vector<int> z1 = karatsubaMultiplyRecursive(a, c);
        
        // z3 = b * d
        std::vector<int> z3 = karatsubaMultiplyRecursive(b, d);
        
        // 计算 (a + b) * (c + d)
        std::vector<int> sumAandB = addVectors(a, b);
        std::vector<int> sumCandD = addVectors(c, d);
        std::vector<int> z2 = karatsubaMultiplyRecursive(sumAandB, sumCandD);
        
        // z2 = (a + b) * (c + d) - z1 - z3
        std::vector<int> z1PlusZ3 = addVectors(z1, z3);
        z2 = subtractVectors(z2, z1PlusZ3);

        // 组合结果: z1 * 10^(2*m) + z2 * 10^m + z3
        std::vector<int> result(2 * n, 0);
        
        // 添加z3到结果
        addToResult(result, z3, 0);
        
        // 添加z2 * 10^m到结果
        addToResult(result, z2, m);
        
        // 添加z1 * 10^(2*m)到结果
        addToResult(result, z1, 2 * m);

        // 移除前导零
        int lastNonZero = result.size() - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        return std::vector<int>(result.begin(), result.begin() + lastNonZero + 1);
    }

    /**
     * 将数字向量添加到结果向量的指定位置
     * 
     * @param result 结果向量
     * @param addend 要添加的数字向量
     * @param offset 起始位置
     */
    static void addToResult(std::vector<int>& result, const std::vector<int>& addend, int offset) {
        for (size_t i = 0; i < addend.size(); i++) {
            if (i + offset < result.size()) {
                result[i + offset] += addend[i];
                // 处理进位
                propagateCarry(result, i + offset);
            }
        }
    }

    /**
     * 处理进位传播
     * 
     * @param arr 数字向量
     * @param pos 起始处理位置
     */
    static void propagateCarry(std::vector<int>& arr, int pos) {
        while (pos < static_cast<int>(arr.size()) - 1 && arr[pos] >= 10) {
            arr[pos + 1] += arr[pos] / 10;
            arr[pos] %= 10;
            pos++;
        }
        // 处理最高位的进位（如果需要）
        // 在这个方法中，我们假设arr足够大，不需要扩展
    }

    /**
     * 对两个数字向量进行加法操作
     * 
     * @param a 第一个数字向量（低位在前）
     * @param b 第二个数字向量（低位在前）
     * @return 和的数字向量（低位在前）
     */
    static std::vector<int> addVectors(const std::vector<int>& a, const std::vector<int>& b) {
        int maxLength = std::max(a.size(), b.size());
        std::vector<int> result(maxLength + 1, 0); // 预留进位空间

        for (int i = 0; i < maxLength; i++) {
            int digitA = (i < static_cast<int>(a.size())) ? a[i] : 0;
            int digitB = (i < static_cast<int>(b.size())) ? b[i] : 0;
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

        return std::vector<int>(result.begin(), result.begin() + lastNonZero + 1);
    }

    /**
     * 对两个数字向量进行减法操作
     * 假设 a >= b
     * 
     * @param a 被减数的数字向量（低位在前）
     * @param b 减数的数字向量（低位在前）
     * @return 差的数字向量（低位在前）
     */
    static std::vector<int> subtractVectors(const std::vector<int>& a, const std::vector<int>& b) {
        std::vector<int> result(a.begin(), a.end());

        for (size_t i = 0; i < b.size(); i++) {
            result[i] -= b[i];
        }

        // 处理借位
        for (size_t i = 0; i < result.size() - 1; i++) {
            while (result[i] < 0) {
                result[i] += 10;
                result[i + 1]--;
            }
        }

        // 移除前导零
        int lastNonZero = static_cast<int>(result.size()) - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        return std::vector<int>(result.begin(), result.begin() + lastNonZero + 1);
    }

    /**
     * 将字符串转换为数字向量（低位在前）
     * 
     * @param s 数字字符串
     * @return 数字向量（低位在前）
     */
    static std::vector<int> stringToDigits(const std::string& s) {
        std::vector<int> digits;
        for (int i = static_cast<int>(s.length()) - 1; i >= 0; i--) {
            digits.push_back(s[i] - '0');
        }
        return digits;
    }

    /**
     * 将数字向量转换为字符串（低位在前转换为正常表示）
     * 
     * @param digits 数字向量（低位在前）
     * @return 数字字符串
     */
    static std::string digitsToString(const std::vector<int>& digits) {
        std::string s;
        for (auto it = digits.rbegin(); it != digits.rend(); ++it) {
            s += std::to_string(*it);
        }
        return s;
    }

    /**
     * 填充向量到指定长度
     * 
     * @param arr 原始向量
     * @param length 目标长度
     * @return 填充后的向量
     */
    static std::vector<int> padVector(const std::vector<int>& arr, int length) {
        if (arr.size() >= length) {
            return arr;
        }
        std::vector<int> padded(arr.begin(), arr.end());
        padded.resize(length, 0);
        return padded;
    }

    /**
     * 生成指定长度的随机数字字符串
     * 
     * @param length 字符串长度
     * @return 随机数字字符串
     */
    static std::string generateRandomNumber(int length) {
        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<> firstDigitDist(1, 9);
        std::uniform_int_distribution<> digitDist(0, 9);
        
        std::string s;
        // 第一位不能是0
        s += std::to_string(firstDigitDist(gen));
        // 生成剩余位
        for (int i = 1; i < length; i++) {
            s += std::to_string(digitDist(gen));
        }
        return s;
    }

public:
    /**
     * 使用Karatsuba算法进行大整数乘法
     * 时间复杂度：O(n^log₂3) ≈ O(n^1.585)
     * 
     * @param x 第一个整数的字符串表示
     * @param y 第二个整数的字符串表示
     * @return 乘积的字符串表示
     */
    static std::string multiply(const std::string& x, const std::string& y) {
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
        std::vector<int> xDigits = stringToDigits(x);
        std::vector<int> yDigits = stringToDigits(y);

        // 调整向量长度为相等且为2的幂（以优化分治过程）
        int maxLength = std::max(static_cast<int>(xDigits.size()), static_cast<int>(yDigits.size()));
        int n = 1;
        while (n < maxLength) {
            n <<= 1; // 向上取最近的2的幂
        }

        xDigits = padVector(xDigits, n);
        yDigits = padVector(yDigits, n);

        // 调用递归Karatsuba算法
        std::vector<int> product = karatsubaMultiplyRecursive(xDigits, yDigits);

        // 移除前导零并转换为字符串
        return digitsToString(product);
    }

    /**
     * 性能测试方法，比较Karatsuba算法与传统算法
     * 
     * @param size 测试数字的位数
     */
    static void benchmark(int size) {
        // 生成测试用的大整数
        std::string num1 = generateRandomNumber(size);
        std::string num2 = generateRandomNumber(size);

        // 测试Karatsuba算法
        auto start = std::chrono::high_resolution_clock::now();
        std::string resultKaratsuba = multiply(num1, num2);
        auto end = std::chrono::high_resolution_clock::now();
        auto karatsubaDuration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);

        // 测试传统算法（对于较小的数字）
        std::string resultNaive = "";
        std::chrono::milliseconds naiveDuration(0);
        if (size <= 1000) { // 对于大数字，传统算法可能太慢
            std::vector<int> digits1 = stringToDigits(num1);
            std::vector<int> digits2 = stringToDigits(num2);
            start = std::chrono::high_resolution_clock::now();
            std::vector<int> naiveResult = naiveMultiply(digits1, digits2);
            resultNaive = digitsToString(naiveResult);
            end = std::chrono::high_resolution_clock::now();
            naiveDuration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
        }

        std::cout << "数字位数: " << size << std::endl;
        std::cout << "Karatsuba算法耗时: " << karatsubaDuration.count() << ".000 ms" << std::endl;
        if (size <= 1000) {
            std::cout << "传统算法耗时: " << naiveDuration.count() << ".000 ms" << std::endl;
            double speedup = static_cast<double>(naiveDuration.count()) / karatsubaDuration.count();
            std::cout << "算法加速比: " << speedup << "x" << std::endl;
            std::cout << "结果一致: " << (resultKaratsuba == resultNaive ? "true" : "false") << std::endl;
        }
        std::cout << "乘积位数: " << resultKaratsuba.length() << std::endl;
    }

    /**
     * 验证算法正确性
     */
    static void verifyCorrectness() {
        std::vector<std::pair<std::string, std::string>> testCases = {
            {"1234", "5678"},
            {"9999", "9999"},
            {"0", "12345"},
            {"1", "98765"},
            {"999999", "999999"}
        };

        for (const auto& testCase : testCases) {
            const std::string& x = testCase.first;
            const std::string& y = testCase.second;
            
            // 使用Karatsuba算法
            std::string result = multiply(x, y);
            
            // 对于小数字，使用C++内置类型验证
            try {
                if (x.length() <= 18 && y.length() <= 18) { // 确保可以放入unsigned long long
                    unsigned long long num1 = std::stoull(x);
                    unsigned long long num2 = std::stoull(y);
                    unsigned long long expected = num1 * num2;
                    std::string expectedStr = std::to_string(expected);
                    std::cout << x << " * " << y << " = " << result << 
                              " (正确: " << (result == expectedStr ? "true" : "false") << ")" << std::endl;
                } else {
                    std::cout << x << " * " << y << " = " << result << std::endl;
                    std::cout << "乘积位数: " << result.length() << std::endl;
                }
            } catch (const std::exception& e) {
                std::cout << x << " * " << y << " = " << result << std::endl;
                std::cout << "乘积位数: " << result.length() << std::endl;
            }
        }
    }
};

/**
 * 检查字符串是否为有效数字
 */
bool isValidNumber(const std::string& s) {
    if (s.empty()) return false;
    if (s == "0") return true;
    if (s[0] == '0') return false; // 不允许前导零
    return std::all_of(s.begin(), s.end(), ::isdigit);
}

/**
 * 交互式测试函数
 */
void interactiveMode() {
    std::string num1, num2;
    std::cout << "请输入两个大整数进行乘法计算（输入exit退出）:" << std::endl;
    
    while (true) {
        std::cout << "第一个数: ";
        std::getline(std::cin, num1);
        if (num1 == "exit" || num1 == "EXIT") break;
        
        std::cout << "第二个数: ";
        std::getline(std::cin, num2);
        if (num2 == "exit" || num2 == "EXIT") break;
        
        // 验证输入是否为有效数字
        if (!isValidNumber(num1) || !isValidNumber(num2)) {
            std::cout << "错误: 请输入有效的正整数" << std::endl;
            continue;
        }
        
        auto start = std::chrono::high_resolution_clock::now();
        std::string result = KaratsubaMultiplication::multiply(num1, num2);
        auto end = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
        
        std::cout << "结果: " << result << std::endl;
        std::cout << "计算耗时: " << duration.count() << ".000 ms" << std::endl;
    }
}

int main() {
    std::cout << "验证算法正确性:" << std::endl;
    KaratsubaMultiplication::verifyCorrectness();
    
    std::cout << "\n性能测试:" << std::endl;
    KaratsubaMultiplication::benchmark(100);
    KaratsubaMultiplication::benchmark(1000);
    KaratsubaMultiplication::benchmark(5000);
    
    interactiveMode();
    
    return 0;
}
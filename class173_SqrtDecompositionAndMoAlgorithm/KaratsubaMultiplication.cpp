#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <random>
#include <iomanip>

/**
 * @brief Karatsuba乘法算法C++实现
 * 
 * Karatsuba算法是一种高效的大整数乘法算法，时间复杂度为O(n^log₂3) ≈ O(n^1.585)，
 * 优于传统的O(n²)复杂度算法。算法基于分治思想，通过减少乘法操作次数来提高效率。
 * 
 * 算法原理：
 * 对于两个n位数a和b，可以将它们分为高低两部分：
 * a = a₁ × 10^(n/2) + a₀
 * b = b₁ × 10^(n/2) + b₀
 * 
 * 则a×b = (a₁ × 10^(n/2) + a₀) × (b₁ × 10^(n/2) + b₀)
 *        = a₁b₁ × 10^n + (a₁b₀ + a₀b₁) × 10^(n/2) + a₀b₀
 * 
 * 传统方法需要计算4次乘法：a₁×b₁, a₁×b₀, a₀×b₁, a₀×b₀
 * 
 * Karatsuba的优化之处在于只计算3次乘法：
 * z₁ = a₁ × b₁
 * z₂ = (a₁ + a₀) × (b₁ + b₀)
 * z₃ = a₀ × b₀
 * 
 * 然后通过加减法得到中间项：a₁b₀ + a₀b₁ = z₂ - z₁ - z₃
 * 
 * 最终结果：a×b = z₁ × 10^n + (z₂ - z₁ - z₃) × 10^(n/2) + z₃
 */

class KaratsubaMultiplication {
private:
    /**
     * @brief 将字符串转换为数字向量（低位在前）
     * 
     * @param str 数字字符串
     * @return std::vector<int> 数字向量（低位在前）
     */
    static std::vector<int> stringToDigits(const std::string& str) {
        std::vector<int> digits;
        // 反向读取字符串，低位在前
        for (auto it = str.rbegin(); it != str.rend(); ++it) {
            digits.push_back(*it - '0');
        }
        return digits;
    }

    /**
     * @brief 将数字向量转换为字符串（低位在前转换为正常表示）
     * 
     * @param digits 数字向量（低位在前）
     * @return std::string 数字字符串
     */
    static std::string digitsToString(const std::vector<int>& digits) {
        std::string str;
        // 反向遍历向量，生成正确的数字表示
        for (auto it = digits.rbegin(); it != digits.rend(); ++it) {
            str.push_back(*it + '0');
        }
        return str.empty() ? "0" : str; // 确保不会返回空字符串
    }

    /**
     * @brief 填充向量到指定长度
     * 
     * @param vec 原始向量
     * @param length 目标长度
     * @return std::vector<int> 填充后的向量
     */
    static std::vector<int> padVector(const std::vector<int>& vec, size_t length) {
        std::vector<int> result = vec;
        result.resize(length, 0);
        return result;
    }

    /**
     * @brief 传统的大整数乘法算法（O(n²)复杂度）
     * 
     * @param a 第一个数字向量（低位在前）
     * @param b 第二个数字向量（低位在前）
     * @return std::vector<int> 乘积的数字向量（低位在前）
     */
    static std::vector<int> naiveMultiply(const std::vector<int>& a, const std::vector<int>& b) {
        size_t m = a.size();
        size_t n = b.size();
        
        // 创建结果向量，长度最大为m+n
        std::vector<int> result(m + n, 0);
        
        // 传统的O(n²)乘法算法
        for (size_t i = 0; i < m; ++i) {
            for (size_t j = 0; j < n; ++j) {
                // 每一位相乘，并累加到对应的位置
                result[i + j] += a[i] * b[j];
            }
        }
        
        // 处理进位
        carryPropagation(result);
        
        // 移除前导零（实际上是向量末尾的零）
        removeLeadingZeros(result);
        
        return result;
    }

    /**
     * @brief 处理进位
     * 
     * @param vec 需要处理进位的向量
     */
    static void carryPropagation(std::vector<int>& vec) {
        for (size_t i = 0; i < vec.size() - 1; ++i) {
            if (vec[i] >= 10) {
                vec[i + 1] += vec[i] / 10;
                vec[i] %= 10;
            }
        }
        
        // 处理最高位进位
        while (vec.back() >= 10) {
            int carry = vec.back() / 10;
            vec.back() %= 10;
            vec.push_back(carry);
        }
    }

    /**
     * @brief 移除前导零（实际上是向量末尾的零）
     * 
     * @param vec 需要处理的向量
     */
    static void removeLeadingZeros(std::vector<int>& vec) {
        // 保留至少一个元素（即使是零）
        while (vec.size() > 1 && vec.back() == 0) {
            vec.pop_back();
        }
    }

    /**
     * @brief 对两个数字向量进行加法操作
     * 
     * @param a 第一个数字向量（低位在前）
     * @param b 第二个数字向量（低位在前）
     * @return std::vector<int> 和的数字向量（低位在前）
     */
    static std::vector<int> addVectors(const std::vector<int>& a, const std::vector<int>& b) {
        size_t maxLength = std::max(a.size(), b.size());
        std::vector<int> result(maxLength + 1, 0); // 额外一位用于处理最高位进位
        
        // 逐位相加
        for (size_t i = 0; i < maxLength; ++i) {
            int digitA = (i < a.size()) ? a[i] : 0;
            int digitB = (i < b.size()) ? b[i] : 0;
            result[i] = digitA + digitB;
        }
        
        // 处理进位
        carryPropagation(result);
        
        // 移除前导零
        removeLeadingZeros(result);
        
        return result;
    }

    /**
     * @brief 对两个数字向量进行减法操作
     * 假设 a >= b
     * 
     * @param a 被减数的数字向量（低位在前）
     * @param b 减数的数字向量（低位在前）
     * @return std::vector<int> 差的数字向量（低位在前）
     */
    static std::vector<int> subtractVectors(const std::vector<int>& a, const std::vector<int>& b) {
        std::vector<int> result = a;
        
        // 确保result的长度足够
        result.resize(std::max(a.size(), b.size()), 0);

        // 逐位相减
        for (size_t i = 0; i < b.size(); ++i) {
            result[i] -= b[i];
        }

        // 处理借位
        for (size_t i = 0; i < result.size() - 1; ++i) {
            while (result[i] < 0) {
                result[i] += 10;  // 当前位借位
                result[i + 1] -= 1;  // 高位减1
            }
        }
        
        // 确保最高位非负（假设输入满足a >= b）
        if (result.back() < 0) {
            throw std::invalid_argument("a must be greater than or equal to b");
        }

        // 移除前导零
        removeLeadingZeros(result);
        
        return result;
    }

    /**
     * @brief 将一个数字向量添加到结果向量的指定位置
     * 
     * @param result 结果向量
     * @param addend 要添加的数字向量
     * @param offset 偏移量（表示乘以10^offset）
     */
    static void addToResult(std::vector<int>& result, const std::vector<int>& addend, size_t offset) {
        // 确保result的长度足够
        if (result.size() < offset + addend.size()) {
            result.resize(offset + addend.size(), 0);
        }
        
        // 执行加法
        for (size_t i = 0; i < addend.size(); ++i) {
            result[i + offset] += addend[i];
        }
        
        // 处理进位
        carryPropagation(result);
    }

    /**
     * @brief Karatsuba算法的递归实现
     * 
     * @param x 第一个数字向量（低位在前）
     * @param y 第二个数字向量（低位在前）
     * @return std::vector<int> 乘积的数字向量（低位在前）
     */
    static std::vector<int> karatsubaMultiplyRecursive(const std::vector<int>& x, const std::vector<int>& y) {
        size_t n = x.size();
        
        // 对于小数组，使用传统乘法算法以避免递归开销
        if (n <= 64) {  // 阈值可以根据性能测试调整
            return naiveMultiply(x, y);
        }
        
        // 将向量分为两半
        size_t m = n / 2;
        
        // 分割向量
        std::vector<int> a(x.begin() + m, x.end());  // 高位部分
        std::vector<int> b(x.begin(), x.begin() + m);  // 低位部分
        std::vector<int> c(y.begin() + m, y.end());  // 高位部分
        std::vector<int> d(y.begin(), y.begin() + m);  // 低位部分
        
        // 计算三个主要乘积
        // 1. z1 = a * c (高位乘高位)
        std::vector<int> z1 = karatsubaMultiplyRecursive(a, c);
        
        // 2. z3 = b * d (低位乘低位)
        std::vector<int> z3 = karatsubaMultiplyRecursive(b, d);
        
        // 3. z2 = (a + b) * (c + d) (组合项)
        std::vector<int> sumAB = addVectors(a, b);
        std::vector<int> sumCD = addVectors(c, d);
        std::vector<int> z2 = karatsubaMultiplyRecursive(sumAB, sumCD);
        
        // 计算中间交叉项：(a+b)*(c+d) - a*c - b*d = a*d + b*c
        std::vector<int> z1z3 = addVectors(z1, z3);
        z2 = subtractVectors(z2, z1z3);

        // 组合最终结果: z1 * 10^n + (z2-z1-z3) * 10^m + z3
        std::vector<int> result(2 * n, 0);
        
        // 添加z3到结果的最低位部分
        addToResult(result, z3, 0);
        
        // 添加中间项 (z2-z1-z3) * 10^m
        addToResult(result, z2, m);
        
        // 添加最高位部分 z1 * 10^(2*m)
        addToResult(result, z1, 2 * m);

        // 移除结果向量中的前导零
        removeLeadingZeros(result);

        return result;
    }

public:
    /**
     * @brief 使用Karatsuba算法进行大整数乘法，支持负数
     * 时间复杂度：O(n^log₂3) ≈ O(n^1.585)
     * 
     * @param xStr 第一个整数的字符串表示，可以是负数
     * @param yStr 第二个整数的字符串表示，可以是负数
     * @return std::string 乘积的字符串表示
     */
    static std::string karatsubaMultiply(std::string xStr, std::string yStr) {
        // 处理负数情况
        bool isNegative = false;
        if (xStr[0] == '-') {
            isNegative = !isNegative;
            xStr = xStr.substr(1);
        }
        if (yStr[0] == '-') {
            isNegative = !isNegative;
            yStr = yStr.substr(1);
        }

        // 处理特殊情况
        if (xStr == "0" || yStr == "0") {
            return "0";
        }
        if (xStr == "1") {
            return isNegative ? "-" + yStr : yStr;
        }
        if (yStr == "1") {
            return isNegative ? "-" + xStr : xStr;
        }

        // 将字符串转换为数字向量（低位在前）
        std::vector<int> xDigits = stringToDigits(xStr);
        std::vector<int> yDigits = stringToDigits(yStr);

        // 调整向量长度为相等且为2的幂（以优化分治过程）
        size_t maxLength = std::max(xDigits.size(), yDigits.size());
        size_t n = 1;
        while (n < maxLength) {
            n <<= 1;  // 向上取最近的2的幂
        }

        xDigits = padVector(xDigits, n);
        yDigits = padVector(yDigits, n);

        // 调用递归Karatsuba算法
        std::vector<int> product = karatsubaMultiplyRecursive(xDigits, yDigits);

        // 移除前导零并转换为字符串
        std::string result = digitsToString(product);
        
        // 添加负号（如果需要）
        return isNegative ? "-" + result : result;
    }

    /**
     * @brief 生成指定长度的随机数字字符串
     * 
     * @param length 字符串长度
     * @return std::string 随机数字字符串
     */
    static std::string generateRandomNumber(size_t length) {
        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<> firstDigit(1, 9);  // 第一位不能是0
        std::uniform_int_distribution<> digit(0, 9);      // 其他位可以是0-9

        std::string number;
        // 第一位
        number.push_back('0' + firstDigit(gen));
        // 剩余位
        for (size_t i = 1; i < length; ++i) {
            number.push_back('0' + digit(gen));
        }
        return number;
    }

    /**
     * @brief 验证算法正确性
     * 测试各种边界情况和常见情况，确保Karatsuba算法在所有情况下都能正确工作
     */
    static void verifyCorrectness() {
        std::vector<std::pair<std::string, std::string>> testCases = {
            // 边界情况
            {"0", "12345"},           // 乘以0
            {"12345", "0"},           // 0乘以
            {"1", "98765"},           // 乘以1
            {"98765", "1"},           // 1乘以
            {"-1234", "5678"},        // 负数乘正数
            {"1234", "-5678"},        // 正数乘负数
            {"-1234", "-5678"},       // 负数乘负数
            
            // 常见测试用例
            {"1234", "5678"},         // 普通数字相乘
            {"9999", "9999"},         // 大数相乘
            {"999999", "999999"},     // 更大的数字相乘
            {"123456789", "987654321"}, // 长数字相乘
            
            // 不同位数的数字
            {"123", "45678"},         // 位数不同
            {"999999999", "1"},       // 大数乘1
        };

        std::cout << "=== 算法正确性验证 ===" << std::endl;
        for (const auto& testCase : testCases) {
            const std::string& x = testCase.first;
            const std::string& y = testCase.second;
            
            // 使用Karatsuba算法
            std::string result = karatsubaMultiply(x, y);
            
            // 对于小数字，使用long long验证结果（注意：仅适用于较小的数字）
            std::string expected;
            bool canVerify = true;
            
            try {
                // 仅对较小的数字进行验证，避免溢出
                if (x.length() < 18 && y.length() < 18) {
                    long long num1 = std::stoll(x);
                    long long num2 = std::stoll(y);
                    long long product = num1 * num2;
                    expected = std::to_string(product);
                } else {
                    canVerify = false;
                    expected = "(超过long long范围，无法验证)";
                }
            } catch (const std::exception& e) {
                canVerify = false;
                expected = "(验证失败: " + std::string(e.what()) + ")";
            }
            
            std::cout << x << " * " << y << " = " << result << std::endl;
            if (canVerify) {
                bool correct = result == expected;
                std::cout << "  验证结果: " << (correct ? "✓ 正确" : "✗ 错误");
                if (!correct) {
                    std::cout << " (期望值: " << expected << ")";
                }
            } else {
                std::cout << "  " << expected;
            }
            std::cout << std::endl << std::endl;
        }
        std::cout << "=== 验证完成 ===" << std::endl;
    }

    /**
     * @brief 性能测试方法，比较Karatsuba算法与传统算法
     * 
     * @param size 测试数字的位数
     */
    static void benchmark(size_t size) {
        // 生成测试用的大整数
        std::string num1 = generateRandomNumber(size);
        std::string num2 = generateRandomNumber(size);

        // 测试Karatsuba算法
        auto start = std::chrono::high_resolution_clock::now();
        std::string resultKaratsuba = karatsubaMultiply(num1, num2);
        auto end = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double, std::milli> karatsubaTime = end - start;

        // 测试传统算法（对于较小的数字）
        std::string resultNaive;
        std::chrono::duration<double, std::milli> naiveTime(0);
        if (size <= 1000) {  // 对于大数字，传统算法可能太慢
            std::vector<int> digits1 = stringToDigits(num1);
            std::vector<int> digits2 = stringToDigits(num2);
            
            start = std::chrono::high_resolution_clock::now();
            std::vector<int> naiveResult = naiveMultiply(digits1, digits2);
            resultNaive = digitsToString(naiveResult);
            end = std::chrono::high_resolution_clock::now();
            naiveTime = end - start;
        }

        std::cout << "数字位数: " << size << std::endl;
        std::cout << "Karatsuba算法耗时: " << std::fixed << std::setprecision(3) 
                  << karatsubaTime.count() << " ms" << std::endl;
        
        if (size <= 1000) {
            std::cout << "传统算法耗时: " << std::fixed << std::setprecision(3) 
                      << naiveTime.count() << " ms" << std::endl;
            std::cout << "Karatsuba vs 传统算法加速比: " << std::fixed << std::setprecision(2) 
                      << naiveTime.count() / karatsubaTime.count() << "x" << std::endl;
            
            // 验证两种算法的结果是否一致
            bool resultsMatch = (resultKaratsuba == resultNaive);
            std::cout << "算法结果一致性: " << (resultsMatch ? "✓ 一致" : "✗ 不一致") << std::endl;
        }
        
        std::cout << "乘积位数: " << resultKaratsuba.length() << std::endl;
        std::cout << std::endl;
    }

    /**
     * @brief 交互式测试模式
     */
    static void interactiveMode() {
        std::cout << "=== 交互式测试 ===" << std::endl;
        std::cout << "请输入两个大整数进行乘法计算（输入exit退出）:" << std::endl;
        
        while (true) {
            try {
                std::string num1, num2;
                
                std::cout << "第一个数: ";
                std::getline(std::cin, num1);
                if (num1 == "exit" || num1 == "EXIT") break;
                
                std::cout << "第二个数: ";
                std::getline(std::cin, num2);
                if (num2 == "exit" || num2 == "EXIT") break;
                
                // 验证输入是否为有效的整数
                bool valid1 = true, valid2 = true;
                for (size_t i = 0; i < num1.length(); ++i) {
                    if (i == 0 && num1[i] == '-' && num1.length() > 1) continue;
                    if (!isdigit(num1[i])) {
                        valid1 = false;
                        break;
                    }
                }
                for (size_t i = 0; i < num2.length(); ++i) {
                    if (i == 0 && num2[i] == '-' && num2.length() > 1) continue;
                    if (!isdigit(num2[i])) {
                        valid2 = false;
                        break;
                    }
                }
                
                if (!valid1 || !valid2) {
                    std::cout << "错误: 请输入有效的整数" << std::endl << std::endl;
                    continue;
                }
                
                auto start = std::chrono::high_resolution_clock::now();
                std::string result = karatsubaMultiply(num1, num2);
                auto end = std::chrono::high_resolution_clock::now();
                std::chrono::duration<double, std::milli> duration = end - start;
                
                std::cout << "结果: " << result << std::endl;
                std::cout << "计算耗时: " << std::fixed << std::setprecision(3) 
                          << duration.count() << " ms" << std::endl;
                std::cout << "乘积位数: " << result.length() << std::endl;
            } catch (const std::exception& e) {
                std::cout << "计算错误: " << e.what() << std::endl;
            }
            std::cout << std::endl;
        }
    }
};

/**
 * @brief 主函数
 */
int main() {
    std::cout << "Karatsuba乘法算法实现 (C++)" << std::endl << std::endl;
    
    // 验证算法正确性
    KaratsubaMultiplication::verifyCorrectness();
    
    std::cout << "\n=== 性能测试 ===" << std::endl;
    std::cout << "注意：对于非常大的数字，测试可能需要较长时间" << std::endl << std::endl;
    
    // 性能测试 - 测试不同大小的数字
    KaratsubaMultiplication::benchmark(100);    // 100位数字
    KaratsubaMultiplication::benchmark(500);    // 500位数字
    
    // 进入交互式测试模式
    try {
        KaratsubaMultiplication::interactiveMode();
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    } catch (...) {
        std::cerr << "发生未知错误" << std::endl;
    }
    
    std::cout << "程序结束" << std::endl;
    return 0;
}
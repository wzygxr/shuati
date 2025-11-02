#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <cmath>
#include <random>
#include <regex>
#include <sstream>
#include <iomanip>
#include <limits>

/**
 * 高精度小数与格式化工具类
 * 提供各种高精度小数操作和格式化方法
 * 适用于需要极高精度计算和显示的场景
 * 
 * 注意：C++标准库没有直接提供像Java BigDecimal或Python Decimal这样的高精度浮点数类型
 * 因此我们将使用自定义的高精度小数类来实现这些功能
 */

class HighPrecisionDecimal {
private:
    // 存储数字的内部表示
    std::vector<int> digits; // 存储小数的所有数字（不带小数点），包括整数部分和小数部分
    int decimalPointPos;     // 小数点位置（从左数的索引）
    bool isNegative;         // 是否为负数

    /**
     * 移除前导零和尾随零
     */
    void normalize() {
        // 移除前导零
        while (digits.size() > 1 && digits.front() == 0) {
            digits.erase(digits.begin());
            decimalPointPos--;
        }
        
        // 移除尾随零（但要保留小数点后的至少一个零）
        while (digits.size() > (decimalPointPos > 0 ? 1 : 0) && digits.back() == 0 && 
               (decimalPointPos < static_cast<int>(digits.size()))) {
            digits.pop_back();
        }
        
        // 特殊情况：如果所有数字都是零
        if (digits.size() == 1 && digits[0] == 0) {
            isNegative = false;
            decimalPointPos = 1;
        }
    }

public:
    /**
     * 默认构造函数，创建零值
     */
    HighPrecisionDecimal() : digits{0}, decimalPointPos(1), isNegative(false) {}

    /**
     * 从字符串构造高精度小数
     * @param str 表示小数的字符串
     */
    HighPrecisionDecimal(const std::string& str) {
        // 处理符号
        size_t startPos = 0;
        isNegative = false;
        if (!str.empty() && str[0] == '-') {
            isNegative = true;
            startPos = 1;
        } else if (!str.empty() && str[0] == '+') {
            startPos = 1;
        }

        // 查找小数点位置
        size_t dotPos = str.find('.', startPos);
        
        // 初始化数字向量
        digits.clear();
        
        // 添加整数部分
        for (size_t i = startPos; i < (dotPos != std::string::npos ? dotPos : str.size()); ++i) {
            if (std::isdigit(str[i])) {
                digits.push_back(str[i] - '0');
            }
        }
        
        // 记录小数点位置
        decimalPointPos = digits.size();
        
        // 添加小数部分
        if (dotPos != std::string::npos) {
            for (size_t i = dotPos + 1; i < str.size(); ++i) {
                if (std::isdigit(str[i])) {
                    digits.push_back(str[i] - '0');
                }
            }
        }
        
        // 处理空数字的情况
        if (digits.empty()) {
            digits.push_back(0);
            decimalPointPos = 1;
            isNegative = false;
        }
        
        // 标准化
        normalize();
    }

    /**
     * 构造函数，从整数部分和小数部分构造
     */
    HighPrecisionDecimal(const std::string& integerPart, const std::string& fractionalPart) {
        isNegative = false;
        digits.clear();
        
        // 添加整数部分
        size_t startPos = 0;
        if (!integerPart.empty() && integerPart[0] == '-') {
            isNegative = true;
            startPos = 1;
        } else if (!integerPart.empty() && integerPart[0] == '+') {
            startPos = 1;
        }
        
        for (size_t i = startPos; i < integerPart.size(); ++i) {
            if (std::isdigit(integerPart[i])) {
                digits.push_back(integerPart[i] - '0');
            }
        }
        
        // 记录小数点位置
        decimalPointPos = digits.size();
        
        // 添加小数部分
        for (char c : fractionalPart) {
            if (std::isdigit(c)) {
                digits.push_back(c - '0');
            }
        }
        
        // 处理空数字的情况
        if (digits.empty()) {
            digits.push_back(0);
            decimalPointPos = 1;
            isNegative = false;
        }
        
        // 标准化
        normalize();
    }

    /**
     * 转换为字符串表示
     */
    std::string toString() const {
        std::stringstream ss;
        
        // 处理符号
        if (isNegative && !(digits.size() == 1 && digits[0] == 0)) {
            ss << '-';
        }
        
        // 输出整数部分
        size_t intPartEnd = std::min(static_cast<size_t>(decimalPointPos), digits.size());
        for (size_t i = 0; i < intPartEnd; ++i) {
            ss << digits[i];
        }
        
        // 如果有小数部分，输出小数点和小数
        if (digits.size() > static_cast<size_t>(decimalPointPos)) {
            ss << '.';
            for (size_t i = static_cast<size_t>(decimalPointPos); i < digits.size(); ++i) {
                ss << digits[i];
            }
        }
        
        // 处理整数部分为空的情况
        if (intPartEnd == 0) {
            ss << '0';
        }
        
        return ss.str();
    }

    /**
     * 转换为科学计数法表示
     * @param precision 有效数字位数
     */
    std::string toScientificNotation(int precision) const {
        if (digits.size() == 1 && digits[0] == 0) {
            return "0.00000000000000000000E+00";
        }

        std::stringstream ss;
        
        // 处理符号
        if (isNegative) {
            ss << '-';
        }
        
        // 计算指数
        int exponent = decimalPointPos - 1;
        
        // 第一个数字
        ss << digits[0] << '.';
        
        // 后面的数字，直到精度要求或数字用完
        int digitsToOutput = std::min(precision - 1, static_cast<int>(digits.size() - 1));
        for (int i = 1; i <= digitsToOutput; ++i) {
            ss << digits[i];
        }
        
        // 补零到所需精度
        for (int i = digitsToOutput; i < precision - 1; ++i) {
            ss << '0';
        }
        
        // 输出指数
        ss << "E" << (exponent >= 0 ? "+" : "") << exponent;
        
        return ss.str();
    }

    /**
     * 四舍五入到指定小数位数
     * @param scale 保留的小数位数
     */
    HighPrecisionDecimal round(int scale) const {
        HighPrecisionDecimal result = *this;
        
        // 计算需要保留的总位数
        int targetLength = decimalPointPos + scale;
        
        // 如果小数位数已经小于等于scale，不需要操作
        if (static_cast<int>(result.digits.size()) <= targetLength) {
            return result;
        }
        
        // 需要进行四舍五入
        bool carry = false;
        
        // 从末尾开始处理，找到需要进位的位置
        for (int i = static_cast<int>(result.digits.size()) - 1; i >= targetLength; --i) {
            if (i == targetLength) {
                // 检查是否需要进位
                carry = (result.digits[i] >= 5);
                break;
            }
        }
        
        // 移除超出精度的小数部分
        result.digits.resize(targetLength);
        
        // 处理进位
        if (carry) {
            int i = targetLength - 1;
            while (i >= 0 && carry) {
                result.digits[i]++;
                if (result.digits[i] == 10) {
                    result.digits[i] = 0;
                    carry = (i > 0);
                } else {
                    carry = false;
                }
                --i;
            }
            
            // 如果整数部分最高位需要进位
            if (carry) {
                result.digits.insert(result.digits.begin(), 1);
                result.decimalPointPos++;
            }
        }
        
        result.normalize();
        return result;
    }

    /**
     * 加法操作
     */
    HighPrecisionDecimal operator+(const HighPrecisionDecimal& other) const {
        // 实现加法逻辑（简化版，实际实现会更复杂）
        if (this->isNegative == other.isNegative) {
            // 同号相加
            HighPrecisionDecimal result;
            // 实际的加法实现...
            return result;
        } else {
            // 异号相减
            // 实际的减法实现...
            return HighPrecisionDecimal();
        }
    }

    /**
     * 比较操作
     */
    int compare(const HighPrecisionDecimal& other) const {
        // 如果符号不同，直接判断
        if (isNegative != other.isNegative) {
            return isNegative ? -1 : 1;
        }
        
        // 符号相同，先比较整数部分长度
        if (decimalPointPos != other.decimalPointPos) {
            int result = (decimalPointPos > other.decimalPointPos) ? 1 : -1;
            return isNegative ? -result : result;
        }
        
        // 整数部分长度相同，比较整数部分
        size_t minIntLength = std::min(static_cast<size_t>(decimalPointPos), static_cast<size_t>(other.decimalPointPos));
        for (size_t i = 0; i < minIntLength; ++i) {
            if (digits[i] != other.digits[i]) {
                int result = (digits[i] > other.digits[i]) ? 1 : -1;
                return isNegative ? -result : result;
            }
        }
        
        // 整数部分相同，比较小数部分
        size_t minTotalLength = std::min(digits.size(), other.digits.size());
        for (size_t i = minIntLength; i < minTotalLength; ++i) {
            if (digits[i] != other.digits[i]) {
                int result = (digits[i] > other.digits[i]) ? 1 : -1;
                return isNegative ? -result : result;
            }
        }
        
        // 前面部分相同，比较长度
        if (digits.size() != other.digits.size()) {
            int result = (digits.size() > other.digits.size()) ? 1 : -1;
            // 如果超出的部分都是零，则相等
            bool allZeros = true;
            if (digits.size() > other.digits.size()) {
                for (size_t i = other.digits.size(); i < digits.size(); ++i) {
                    if (digits[i] != 0) {
                        allZeros = false;
                        break;
                    }
                }
            } else {
                for (size_t i = digits.size(); i < other.digits.size(); ++i) {
                    if (other.digits[i] != 0) {
                        allZeros = false;
                        break;
                    }
                }
            }
            if (!allZeros) {
                return isNegative ? -result : result;
            }
        }
        
        // 完全相等
        return 0;
    }

    /**
     * 格式化输出，添加千位分隔符
     */
    std::string formatWithSeparators() const {
        std::stringstream ss;
        
        // 处理符号
        if (isNegative && !(digits.size() == 1 && digits[0] == 0)) {
            ss << '-';
        }
        
        // 整数部分长度
        size_t intLength = std::min(static_cast<size_t>(decimalPointPos), digits.size());
        
        // 输出整数部分，添加千位分隔符
        for (size_t i = 0; i < intLength; ++i) {
            // 在适当的位置添加千位分隔符
            if (i > 0 && (intLength - i) % 3 == 0) {
                ss << ',';
            }
            ss << digits[i];
        }
        
        // 如果有小数部分，输出小数点和小数
        if (digits.size() > static_cast<size_t>(decimalPointPos)) {
            ss << '.';
            for (size_t i = static_cast<size_t>(decimalPointPos); i < digits.size(); ++i) {
                ss << digits[i];
            }
        }
        
        // 处理整数部分为空的情况
        if (intLength == 0) {
            ss << '0';
        }
        
        return ss.str();
    }
};

/**
 * 将科学计数法表示的字符串转换为普通小数表示
 */
std::string scientificToDecimal(const std::string& scientificNotation, int precision) {
    // 正则表达式匹配科学计数法
    std::regex sciRegex(R"(([+-]?)(\d+(?:\.\d+)?)[eE]([+-]?\d+))");
    std::smatch match;
    
    if (!std::regex_match(scientificNotation, match, sciRegex)) {
        // 如果不是科学计数法，直接返回
        return scientificNotation;
    }
    
    std::string sign = match[1].str();
    std::string mantissa = match[2].str();
    int exponent = std::stoi(match[3].str());
    
    // 分离尾数的整数部分和小数部分
    size_t dotPos = mantissa.find('.');
    std::string intPart = (dotPos != std::string::npos) ? mantissa.substr(0, dotPos) : mantissa;
    std::string fracPart = (dotPos != std::string::npos) ? mantissa.substr(dotPos + 1) : "";
    
    // 根据指数调整小数点位置
    std::stringstream result;
    
    if (exponent >= 0) {
        // 小数点右移
        result << intPart;
        if (exponent < static_cast<int>(fracPart.size())) {
            result << fracPart.substr(0, exponent);
            result << '.' << fracPart.substr(exponent);
        } else {
            result << fracPart;
            result << std::string(exponent - fracPart.size(), '0');
        }
    } else {
        // 小数点左移
        exponent = -exponent;
        result << "0.";
        result << std::string(exponent - 1, '0');
        result << intPart << fracPart;
    }
    
    // 添加符号
    if (!sign.empty() && sign != "+") {
        result << sign;
    }
    
    // 四舍五入到指定精度
    HighPrecisionDecimal hp(result.str());
    hp = hp.round(precision);
    
    return hp.toString();
}

/**
 * 格式化高精度小数，添加千位分隔符
 */
std::string formatWithSeparators(const std::string& value, int fractionalDigits) {
    HighPrecisionDecimal hp(value);
    hp = hp.round(fractionalDigits);
    return hp.formatWithSeparators();
}

/**
 * 生成指定位数的随机高精度小数
 */
std::string generateRandomDecimal(int integerDigits, int fractionalDigits) {
    if (integerDigits <= 0 && fractionalDigits <= 0) {
        return "0";
    }
    
    static std::random_device rd;
    static std::mt19937 gen(rd());
    static std::uniform_int_distribution<> dis09(0, 9);
    static std::uniform_int_distribution<> dis19(1, 9);
    
    std::stringstream ss;
    
    // 生成整数部分
    if (integerDigits > 0) {
        // 第一位不能是0
        ss << dis19(gen);
        // 生成剩余的整数位
        for (int i = 1; i < integerDigits; ++i) {
            ss << dis09(gen);
        }
    } else {
        ss << '0';
    }
    
    // 生成小数部分
    if (fractionalDigits > 0) {
        ss << '.';
        for (int i = 0; i < fractionalDigits; ++i) {
            ss << dis09(gen);
        }
    }
    
    return ss.str();
}

/**
 * 计算大数的平方根（简化实现）
 */
std::string calculateSquareRoot(const std::string& n, int precision) {
    // 使用二分法计算平方根的近似值
    double num = std::stod(n);
    if (num < 0) {
        throw std::invalid_argument("无法计算负数的平方根");
    }
    
    double low = 0.0;
    double high = std::max(1.0, num);
    double mid = 0.0;
    
    // 二分查找
    for (int i = 0; i < 100; ++i) { // 100次迭代足够精确
        mid = (low + high) / 2;
        double midSquared = mid * mid;
        
        if (std::abs(midSquared - num) < 1e-20) {
            break;
        }
        
        if (midSquared < num) {
            low = mid;
        } else {
            high = mid;
        }
    }
    
    // 格式化输出
    std::stringstream ss;
    ss << std::fixed << std::setprecision(precision) << mid;
    return ss.str();
}

/**
 * 比较两个高精度小数的大小
 */
int compareDecimals(const std::string& a, const std::string& b) {
    HighPrecisionDecimal hpA(a);
    HighPrecisionDecimal hpB(b);
    return hpA.compare(hpB);
}

/**
 * 格式化高精度小数，显示为工程计数法
 */
std::string formatToEngineeringNotation(const std::string& value, int precision) {
    HighPrecisionDecimal hp(value);
    
    // 转换为科学计数法
    std::string scientific = hp.toScientificNotation(precision);
    
    // 正则表达式匹配科学计数法
    std::regex sciRegex(R"(([+-]?)(\d+\.\d+)[eE]([+-]?\d+))");
    std::smatch match;
    
    if (!std::regex_match(scientific, match, sciRegex)) {
        return scientific;
    }
    
    std::string sign = match[1].str();
    std::string mantissa = match[2].str();
    int exponent = std::stoi(match[3].str());
    
    // 调整指数为3的倍数
    int remainder = exponent % 3;
    if (remainder != 0) {
        if (remainder < 0) {
            remainder += 3;
            exponent -= 3;
        }
        
        // 调整尾数
        double mantissaValue = std::stod(mantissa);
        mantissaValue *= std::pow(10, remainder);
        
        std::stringstream ss;
        ss << std::fixed << std::setprecision(precision - 1) << mantissaValue;
        mantissa = ss.str();
        exponent -= remainder;
    }
    
    // 构建工程计数法字符串
    std::stringstream result;
    result << sign << mantissa << "e" << (exponent >= 0 ? "+" : "") << exponent;
    
    return result.str();
}

/**
 * 解析格式化的数字字符串
 */
std::string parseFormattedDecimal(const std::string& formattedString) {
    // 移除千位分隔符
    std::string cleanString;
    for (char c : formattedString) {
        if (c != ',') {
            cleanString += c;
        }
    }
    
    return cleanString;
}

/**
 * 对高精度小数进行舍入操作
 */
std::string roundDecimal(const std::string& value, int scale, const std::string& roundingMode) {
    HighPrecisionDecimal hp(value);
    HighPrecisionDecimal rounded = hp.round(scale);
    return rounded.toString();
}

/**
 * 测试高精度小数的各种操作
 */
void testOperations() {
    std::cout << "=== 高精度小数操作测试 ===" << std::endl;
    
    // 测试科学计数法转换
    std::string scientific = "1.23456789E5";
    std::cout << "科学计数法: " << scientific << std::endl;
    std::cout << "转为普通小数: " << scientificToDecimal(scientific, 10) << std::endl;
    
    // 测试合并整数和小数部分
    HighPrecisionDecimal combined("12345", "6789");
    std::cout << "\n合并整数和小数部分: " << combined.toString() << std::endl;
    
    // 测试格式化
    std::string value = "1234567.890123456789";
    std::cout << "\n原始值: " << value << std::endl;
    std::cout << "格式化(带千位分隔符): " << formatWithSeparators(value, 8) << std::endl;
    std::cout << "工程计数法: " << formatToEngineeringNotation(value, 10) << std::endl;
    
    // 测试随机数生成
    std::string randomDecimal = generateRandomDecimal(8, 10);
    std::cout << "\n随机高精度小数: " << randomDecimal << std::endl;
    
    // 测试平方根计算
    std::string bigNum = "12345678901234567890";
    std::cout << "\n计算大数的平方根: sqrt(" << bigNum << ")" << std::endl;
    std::string sqrt = calculateSquareRoot(bigNum, 20);
    std::cout << "平方根: " << sqrt << std::endl;
    
    // 测试比较
    std::string a = "123.456";
    std::string b = "123.457";
    std::cout << "\n比较两个高精度小数: " << a << " 和 " << b << std::endl;
    int result = compareDecimals(a, b);
    std::cout << "比较结果: " << result << std::endl;
    
    // 测试解析
    std::string formatted = "1,234,567.8901";
    std::cout << "\n解析格式化字符串: " << formatted << std::endl;
    std::cout << "解析结果: " << parseFormattedDecimal(formatted) << std::endl;
    
    // 测试舍入
    std::string valueToRound = "123.456789";
    std::cout << "\n舍入测试: " << valueToRound << std::endl;
    std::cout << "四舍五入到3位小数: " << roundDecimal(valueToRound, 3, "HALF_UP") << std::endl;
}

/**
 * 交互式测试函数
 */
void interactiveMode() {
    std::cout << "=== 高精度小数与格式化工具 ===" << std::endl;
    std::cout << "输入操作编号:" << std::endl;
    std::cout << "1. 科学计数法转普通小数" << std::endl;
    std::cout << "2. 合并整数和小数部分" << std::endl;
    std::cout << "3. 格式化带千位分隔符" << std::endl;
    std::cout << "4. 生成随机高精度小数" << std::endl;
    std::cout << "5. 计算大数平方根" << std::endl;
    std::cout << "6. 比较两个高精度小数" << std::endl;
    std::cout << "7. 工程计数法格式化" << std::endl;
    std::cout << "8. 解析格式化的数字" << std::endl;
    std::cout << "9. 小数舍入操作" << std::endl;
    std::cout << "0. 退出" << std::endl;
    
    while (true) {
        std::cout << "\n请输入操作编号: ";
        int choice;
        std::cin >> choice;
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // 清空输入缓冲区
        
        try {
            switch (choice) {
                case 0:
                    std::cout << "程序已退出" << std::endl;
                    return;
                case 1: {
                    std::cout << "请输入科学计数法表示的数字: ";
                    std::string sciNotation;
                    std::getline(std::cin, sciNotation);
                    std::cout << "请输入保留的小数位数: ";
                    int precision;
                    std::cin >> precision;
                    std::cin.ignore();
                    std::cout << "转换结果: " << scientificToDecimal(sciNotation, precision) << std::endl;
                    break;
                }
                case 2: {
                    std::cout << "请输入整数部分: ";
                    std::string integerPart;
                    std::getline(std::cin, integerPart);
                    std::cout << "请输入小数部分: ";
                    std::string fractionalPart;
                    std::getline(std::cin, fractionalPart);
                    HighPrecisionDecimal hp(integerPart, fractionalPart);
                    std::cout << "合并结果: " << hp.toString() << std::endl;
                    break;
                }
                case 3: {
                    std::cout << "请输入要格式化的数字: ";
                    std::string numToFormat;
                    std::getline(std::cin, numToFormat);
                    std::cout << "请输入小数位数: ";
                    int fracDigits;
                    std::cin >> fracDigits;
                    std::cin.ignore();
                    std::cout << "格式化结果: " << formatWithSeparators(numToFormat, fracDigits) << std::endl;
                    break;
                }
                case 4: {
                    std::cout << "请输入整数部分位数: ";
                    int intDigits;
                    std::cin >> intDigits;
                    std::cout << "请输入小数部分位数: ";
                    int fracDigits;
                    std::cin >> fracDigits;
                    std::cin.ignore();
                    std::cout << "随机小数: " << generateRandomDecimal(intDigits, fracDigits) << std::endl;
                    break;
                }
                case 5: {
                    std::cout << "请输入要计算平方根的整数: ";
                    std::string intToSqrt;
                    std::getline(std::cin, intToSqrt);
                    std::cout << "请输入保留的小数位数: ";
                    int precision;
                    std::cin >> precision;
                    std::cin.ignore();
                    std::cout << "平方根: " << calculateSquareRoot(intToSqrt, precision) << std::endl;
                    break;
                }
                case 6: {
                    std::cout << "请输入第一个数字: ";
                    std::string num1;
                    std::getline(std::cin, num1);
                    std::cout << "请输入第二个数字: ";
                    std::string num2;
                    std::getline(std::cin, num2);
                    int result = compareDecimals(num1, num2);
                    std::cout << num1 << " " << (result < 0 ? "小于" : (result > 0 ? "大于" : "等于")) << " " << num2 << std::endl;
                    break;
                }
                case 7: {
                    std::cout << "请输入要格式化的数字: ";
                    std::string numToEng;
                    std::getline(std::cin, numToEng);
                    std::cout << "请输入有效数字位数: ";
                    int sigDigits;
                    std::cin >> sigDigits;
                    std::cin.ignore();
                    std::cout << "工程计数法表示: " << formatToEngineeringNotation(numToEng, sigDigits) << std::endl;
                    break;
                }
                case 8: {
                    std::cout << "请输入要解析的格式化数字字符串: ";
                    std::string formattedStr;
                    std::getline(std::cin, formattedStr);
                    std::cout << "解析结果: " << parseFormattedDecimal(formattedStr) << std::endl;
                    break;
                }
                case 9: {
                    std::cout << "请输入要舍入的数字: ";
                    std::string numToRound;
                    std::getline(std::cin, numToRound);
                    std::cout << "请输入保留的小数位数: ";
                    int scale;
                    std::cin >> scale;
                    std::cin.ignore();
                    std::cout << "四舍五入: " << roundDecimal(numToRound, scale, "HALF_UP") << std::endl;
                    break;
                }
                default:
                    std::cout << "无效的操作编号，请重新输入" << std::endl;
            }
        } catch (const std::exception& e) {
            std::cout << "操作出错: " << e.what() << std::endl;
            std::cin.clear(); // 清除错误状态
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // 清空输入缓冲区
        }
    }
}

int main() {
    // 运行测试
    testOperations();
    
    // 启动交互模式
    interactiveMode();
    
    return 0;
}
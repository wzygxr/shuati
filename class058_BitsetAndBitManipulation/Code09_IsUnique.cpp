// LeetCode 面试题 01.01. 判定字符是否唯一
// 题目链接: https://leetcode-cn.com/problems/is-unique-lcci/
// 题目大意:
// 实现一个算法，确定一个字符串 s 的所有字符是否全都不同。
// 
// 示例 1:
// 输入: s = "leetcode"
// 输出: false
// 
// 示例 2:
// 输入: s = "abc"
// 输出: true
// 
// 限制:
// 0 <= len(s) <= 100
// 如果你不使用额外的数据结构，会很加分。
//
// 解题思路:
// 使用位运算优化的方法:
// 1. 由于字符集可能是ASCII或Unicode，但题目中通常假设是小写字母或有限范围
// 2. 我们可以使用一个整数或位集合来表示每个字符是否出现过
// 3. 对于每个字符，检查对应的位是否已经被设置，如果是则返回false，否则设置该位
//
// 时间复杂度: O(n)，其中n是字符串的长度
// 空间复杂度: O(1)，使用了固定大小的位集合或整数

#include <iostream>
#include <string>
#include <unordered_set>
#include <algorithm>
#include <bitset>
#include <chrono>

// 方法1: 使用哈希集合
// 优点: 简单直观，适用于任意字符集
// 缺点: 使用了额外的数据结构
// 参数astr: 待检查的字符串
// 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
bool isUnique1(const std::string& astr) {
    // 边界条件检查
    // 如果字符串为空，认为所有字符都唯一
    if (astr.empty()) {
        return true;
    }
    
    // 使用哈希集合存储已出现的字符
    // unordered_set的查找和插入操作平均时间复杂度为O(1)
    std::unordered_set<char> seen;
    
    // 遍历字符串中的每个字符
    // 使用范围for循环遍历字符串
    for (char c : astr) {
        // 检查字符是否已经在集合中
        // find(c)查找字符c，如果找到返回指向该元素的迭代器，否则返回end()
        // != seen.end()表示找到了该字符
        if (seen.find(c) != seen.end()) {
            // 如果字符已经出现过，说明有重复
            return false;
        }
        // 将字符添加到集合中
        // insert(c)将字符c插入到集合中
        seen.insert(c);
    }
    
    // 所有字符都不重复
    return true;
}

// 方法2: 使用位运算模拟Bitset（仅适用于小写字母a-z）
// 优点: 空间效率更高，不使用额外的数据结构
// 缺点: 仅适用于小写字母范围
// 参数astr: 待检查的字符串（假设只包含小写字母）
// 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
bool isUnique2(const std::string& astr) {
    // 边界条件检查
    // 如果字符串为空，认为所有字符都唯一
    if (astr.empty()) {
        return true;
    }
    
    // 鸽巢原理：如果字符串长度超过字母表大小，必然有重复
    // 小写字母只有26个，如果字符串长度超过26，必然有重复字符
    if (astr.length() > 26) {
        return false;
    }
    
    // 使用整数的二进制位来存储字符出现情况
    // 使用一个整数的低26位来表示字符a-z是否出现
    int checker = 0;
    
    // 遍历字符串中的每个字符
    for (char c : astr) {
        // 检查字符是否为小写字母
        // 如果不是小写字母，回退到方法1处理任意字符集
        if (c < 'a' || c > 'z') {
            // 回退到哈希集合方法，处理任意字符集
            return isUnique1(astr);
        }
        
        // 计算字符对应的位位置
        // c - 'a' 将字符映射到0-25的范围
        int bit = c - 'a';
        
        // 检查该位是否已经被设置
        // (1 << bit) 是将1左移bit位，创建一个只有第bit位为1的数
        // checker & (1 << bit) 按位与操作，检查checker的第bit位是否为1
        // 如果结果大于0，说明该位已经被设置，即字符重复
        if ((checker & (1 << bit)) > 0) {
            // 字符重复，返回false
            return false;
        }
        
        // 设置该位为1
        // checker |= (1 << bit) 按位或操作，将checker的第bit位设置为1
        checker |= (1 << bit);
    }
    
    // 所有字符都不重复
    return true;
}

// 方法3: 使用C++的bitset
// 优点: 适用于较大的字符集，效率高
// 缺点: 需要预先知道字符集大小
// 参数astr: 待检查的字符串
// 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
bool isUnique3(const std::string& astr) {
    // 边界条件检查
    // 如果字符串为空，认为所有字符都唯一
    if (astr.empty()) {
        return true;
    }
    
    // 使用bitset存储字符是否出现过（假设是ASCII字符集）
    // bitset<256>表示一个256位的位集合，用于存储ASCII字符
    std::bitset<256> seen;
    
    // 遍历字符串中的每个字符
    for (char c : astr) {
        // 获取字符的ASCII码值
        // static_cast<unsigned char>(c)将char转换为unsigned char
        // 这样可以正确处理负数字符值
        unsigned char uc = static_cast<unsigned char>(c);
        
        // 检查字符是否已经出现过
        // test(uc)检查第uc位是否为1
        if (seen.test(uc)) {
            // 如果字符已经出现过，说明有重复
            return false;
        }
        
        // 标记字符已出现
        // set(uc)将第uc位设置为1
        seen.set(uc);
    }
    
    // 所有字符都不重复
    return true;
}

// 方法4: 不使用额外数据结构（排序后比较相邻元素）
// 优点: 不使用额外的数据结构
// 缺点: 时间复杂度较高，且会修改原始数据（这里使用副本）
// 参数astr: 待检查的字符串
// 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
bool isUnique4(const std::string& astr) {
    // 边界条件检查
    // 如果字符串为空，认为所有字符都唯一
    if (astr.empty()) {
        return true;
    }
    
    // 鸽巢原理
    // 如果字符串长度超过字符集大小，必然有重复
    // 假设是ASCII字符集，最多有256个不同的字符
    if (astr.length() > 256) {
        return false;
    }
    
    // 创建字符串副本并排序
    // 这样不会修改原始字符串
    std::string sorted_str = astr;
    // 对字符串进行排序
    std::sort(sorted_str.begin(), sorted_str.end());
    
    // 检查相邻字符是否相同
    // 遍历排序后的字符串，比较相邻元素
    for (size_t i = 0; i < sorted_str.length() - 1; ++i) {
        // 如果相邻字符相同，说明有重复
        if (sorted_str[i] == sorted_str[i + 1]) {
            return false;
        }
    }
    
    // 所有字符都不重复
    return true;
}

// 方法5: 工程化版本，增加异常处理和参数验证
// 参数astr: 待检查的字符串
// 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
bool isUniqueWithValidation(const std::string& astr) {
    try {
        // 参数验证（在C++中，传入空指针会导致未定义行为，所以这里假设astr有效）
        
        // 鸽巢原理快速判断
        // 如果字符串长度超过字符集大小，必然有重复
        // 假设使用扩展ASCII字符集，最多128个字符
        if (astr.length() > 128) {
            return false;
        }
        
        // 使用bitset实现
        // 创建一个大小为128的bitset，用于存储ASCII字符
        std::bitset<128> seen;
        
        // 遍历字符串中的每个字符
        for (char c : astr) {
            // 获取字符的ASCII码值
            // static_cast<unsigned char>(c)将char转换为unsigned char
            unsigned char uc = static_cast<unsigned char>(c);
            
            // 检查是否超出处理范围
            // 如果字符的ASCII码值超过127，说明是扩展ASCII或Unicode字符
            if (uc >= 128) {
                // 对于扩展ASCII或Unicode字符，使用哈希集合处理
                return isUnique1(astr);
            }
            
            // 检查字符是否已经出现过
            // test(uc)检查第uc位是否为1
            if (seen.test(uc)) {
                // 字符重复，返回false
                return false;
            }
            
            // 标记字符已出现
            // set(uc)将第uc位设置为1
            seen.set(uc);
        }
        
        // 所有字符都不重复
        return true;
    }
    catch (const std::exception& e) {
        // 记录异常（在实际应用中可以使用日志）
        // 在生产环境中，应该使用日志框架记录异常
        std::cerr << "Error in isUniqueWithValidation: " << e.what() << std::endl;
        // 异常情况下保守返回false
        return false;
    }
}

// 单元测试
void runTests() {
    std::cout << "Running unit tests..." << std::endl;
    
    // 定义测试用例结构体
    struct TestCase {
        std::string input;  // 输入字符串
        bool expected;      // 期望的输出结果
    };
    
    // 定义测试用例
    std::vector<TestCase> testCases = {
        {"leetcode", false},      // 有重复字符
        {"abc", true},            // 无重复字符
        {"", true},               // 空字符串
        {"AbCdEfG", true},        // 包含大小写字母
        {"a", true},              // 单个字符
        {"abcdefghijklmnopqrstuvwxyz", true},   // 包含所有小写字母
        {"abcdefghijklmnopqrstuvwxyzabc", false} // 有重复字符
    };
    
    // 定义要测试的函数和名称结构体
    struct Method {
        bool (*func)(const std::string&);  // 函数指针
        std::string name;                  // 方法名称
    };
    
    // 定义所有测试方法
    std::vector<Method> methods = {
        {isUnique1, "Method 1 (HashSet)"},
        {isUnique2, "Method 2 (Bitwise)"},
        {isUnique3, "Method 3 (Bitset)"},
        {isUnique4, "Method 4 (Sorting)"},
        {isUniqueWithValidation, "Method 5 (With Validation)"}
    };
    
    // 测试所有方法
    for (const auto& method : methods) {
        std::cout << "\n" << method.name << ":" << std::endl;
        // 遍历所有测试用例
        for (const auto& test : testCases) {
            // 调用被测试的方法
            bool result = method.func(test.input);
            // 判断测试结果是否正确
            const std::string& status = (result == test.expected) ? "PASS" : "FAIL";
            // 输出测试结果
            std::cout << "  Input: \"" << test.input << "\" -> Result: " 
                      << (result ? "true" : "false") 
                      << " (Expected: " << (test.expected ? "true" : "false") 
                      << ") - " << status << std::endl;
        }
    }
}

// 性能测试
void performanceTest() {
    std::cout << "\nRunning performance tests..." << std::endl;
    
    // 生成测试数据
    // 创建一个包含所有小写字母的字符串
    std::string uniqueStr;
    for (char c = 'a'; c <= 'z'; ++c) {
        uniqueStr += c;
    }
    
    // 创建一个有重复字符的字符串
    std::string duplicateStr = uniqueStr + 'a';
    
    // 定义要测试的函数和名称结构体
    struct Method {
        bool (*func)(const std::string&);  // 函数指针
        std::string name;                  // 方法名称
    };
    
    // 定义所有测试方法
    std::vector<Method> methods = {
        {isUnique1, "Method 1 (HashSet)"},
        {isUnique2, "Method 2 (Bitwise)"},
        {isUnique3, "Method 3 (Bitset)"},
        {isUnique4, "Method 4 (Sorting)"},
        {isUniqueWithValidation, "Method 5 (With Validation)"}
    };
    
    // 定义测试迭代次数
    const int iterations = 100000;
    
    // 测试每种方法的性能
    for (const auto& method : methods) {
        std::cout << "\n" << method.name << " performance:" << std::endl;
        
        // 测试唯一字符串
        // 记录开始时间
        auto start = std::chrono::high_resolution_clock::now();
        // 执行多次测试以获得更准确的结果
        for (int i = 0; i < iterations; ++i) {
            method.func(uniqueStr);
        }
        // 记录结束时间
        auto end = std::chrono::high_resolution_clock::now();
        // 计算执行时间（微秒）
        auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
        // 计算平均执行时间
        double avgTime = static_cast<double>(duration) / iterations;
        std::cout << "  Unique string average time: " << avgTime << " μs" << std::endl;
        
        // 测试重复字符串
        // 记录开始时间
        start = std::chrono::high_resolution_clock::now();
        // 执行多次测试以获得更准确的结果
        for (int i = 0; i < iterations; ++i) {
            method.func(duplicateStr);
        }
        // 记录结束时间
        end = std::chrono::high_resolution_clock::now();
        // 计算执行时间（微秒）
        duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
        // 计算平均执行时间
        avgTime = static_cast<double>(duration) / iterations;
        std::cout << "  Duplicate string average time: " << avgTime << " μs" << std::endl;
    }
}

// 主函数，程序入口点
int main() {
    std::cout << "LeetCode 面试题 01.01. 判定字符是否唯一" << std::endl;
    std::cout << "Using bitwise operations for optimization" << std::endl;
    
    // 运行单元测试
    runTests();
    
    // 运行性能测试
    performanceTest();
    
    // 复杂度分析
    std::cout << "\n复杂度分析:" << std::endl;
    std::cout << "位运算方法 (isUnique2):" << std::endl;
    std::cout << "  时间复杂度: O(n)，其中n是字符串的长度" << std::endl;
    std::cout << "  空间复杂度: O(1)，仅使用一个整数存储位信息" << std::endl;
    std::cout << "  优势: 不需要额外的数据结构，空间效率高" << std::endl;
    std::cout << "  限制: 仅适用于有限范围的字符（如小写字母a-z）" << std::endl;
    
    std::cout << "\n适用场景总结:" << std::endl;
    std::cout << "1. 当输入字符集较小时（如只有小写字母），位运算方法效率最高" << std::endl;
    std::cout << "2. 当输入字符集较大时，bitset方法更通用且高效" << std::endl;
    std::cout << "3. 当不允许使用额外数据结构时，排序方法是一种选择，但效率较低" << std::endl;
    std::cout << "4. 在工程实践中，应根据具体场景选择合适的方法，并考虑异常处理和边界情况" << std::endl;
    
    return 0;
}
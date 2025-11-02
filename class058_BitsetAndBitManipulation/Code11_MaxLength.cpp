// LeetCode 1239. 串联字符串的最大长度
// 题目链接: https://leetcode-cn.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/
// 题目大意:
// 给定一个字符串数组 arr，字符串 s 是将 arr 某一子序列字符串连接所得的字符串，如果 s 中的每个字符都只出现过一次，
// 请返回所有可能的 s 中最长长度。
// 
// 示例 1:
// 输入：arr = ["un","iq","ue"]
// 输出：4
// 解释：所有可能的串联组合是 "","un","iq","ue","uniq" 和 "ique"，最大长度为 4。
// 
// 示例 2:
// 输入：arr = ["cha","r","act","ers"]
// 输出：6
// 解释：可能的解答是 "chaers"，长度为 6。
// 
// 示例 3:
// 输入：arr = ["abcdefghijklmnopqrstuvwxyz"]
// 输出：26
// 
// 提示：
// 1 <= arr.length <= 16
// 1 <= arr[i].length <= 26
// arr[i] 中只含有小写英文字母
//
// 解题思路:
// 由于题目中要求字符串中的每个字符只出现一次，我们可以使用位掩码来表示每个字符串中包含的字符。
// 对于长度为16的数组，我们可以使用回溯算法或动态规划来求解最长长度。
// 这里我们使用位运算优化，通过掩码来判断字符是否重复。

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <unordered_set>
#include <regex>

// 方法一：回溯算法 + 位运算
int maxLength1(const std::vector<std::string>& arr) {
    // 边界条件检查
    if (arr.empty()) {
        return 0;
    }
    
    // 过滤掉自身包含重复字符的字符串，并将其转换为位掩码
    std::vector<int> masks;
    for (const std::string& s : arr) {
        int mask = 0;
        bool is_valid = true;
        
        for (char c : s) {
            int bit = 1 << (c - 'a');
            // 检查当前字符是否已经在mask中设置
            if (mask & bit) {
                is_valid = false;
                break;
            }
            mask |= bit;
        }
        
        if (is_valid) {
            masks.push_back(mask);
        }
    }
    
    // 回溯函数
    std::function<int(int, int)> backtrack = [&](int index, int current_mask) {
        // 基本情况：已经处理完所有字符串
        if (index == masks.size()) {
            // 计算current_mask中设置的位的数量，即字符数量
            return __builtin_popcount(current_mask);
        }
        
        // 不选当前字符串
        int max_len = backtrack(index + 1, current_mask);
        
        // 选当前字符串（如果不会导致重复字符）
        int current_mask_val = masks[index];
        if ((current_mask & current_mask_val) == 0) {  // 没有共同的字符
            max_len = std::max(max_len, backtrack(index + 1, current_mask | current_mask_val));
        }
        
        return max_len;
    };
    
    // 调用回溯函数，从索引0和空掩码开始
    return backtrack(0, 0);
}

// 方法二：动态规划 + 位运算
int maxLength2(const std::vector<std::string>& arr) {
    // 边界条件检查
    if (arr.empty()) {
        return 0;
    }
    
    // 过滤掉自身包含重复字符的字符串，并将其转换为位掩码
    std::vector<int> valid_masks;
    for (const std::string& s : arr) {
        int mask = 0;
        bool is_valid = true;
        
        for (char c : s) {
            int bit = 1 << (c - 'a');
            if (mask & bit) {
                is_valid = false;
                break;
            }
            mask |= bit;
        }
        
        if (is_valid && mask != 0) {  // 确保mask不为0（空字符串被过滤）
            valid_masks.push_back(mask);
        }
    }
    
    // 动态规划：dp存储所有可能的有效掩码组合
    std::unordered_set<int> dp;
    dp.insert(0);  // 初始状态：空字符串
    int max_len = 0;
    
    // 对于每个有效的字符串掩码
    for (int mask : valid_masks) {
        // 创建一个临时集合，存储新的组合
        std::unordered_set<int> temp;
        
        // 对于当前的所有组合
        for (int existing_mask : dp) {
            // 如果当前mask和已有mask没有重叠的位
            if ((existing_mask & mask) == 0) {
                int combined_mask = existing_mask | mask;
                temp.insert(combined_mask);
                max_len = std::max(max_len, __builtin_popcount(combined_mask));
            }
        }
        
        // 将新的组合添加到dp中
        dp.insert(temp.begin(), temp.end());
    }
    
    return max_len;
}

// 方法三：优化的回溯算法
int maxLength3(const std::vector<std::string>& arr) {
    // 过滤无效字符串并转换为掩码
    std::vector<int> masks;
    std::vector<int> lengths;
    for (const std::string& s : arr) {
        int mask = 0;
        bool valid = true;
        
        for (char c : s) {
            int bit = 1 << (c - 'a');
            if (mask & bit) {
                valid = false;
                break;
            }
            mask |= bit;
        }
        
        if (valid) {
            masks.push_back(mask);
            lengths.push_back(__builtin_popcount(mask));
        }
    }
    
    // 存储最大长度
    int max_len = 0;
    
    // 优化的回溯函数
    std::function<void(int, int, int)> optimized_backtrack = 
        [&](int index, int current_mask, int current_length) {
            // 更新最大长度
            if (current_length > max_len) {
                max_len = current_length;
            }
            
            // 剪枝：如果剩余的字符串即使全部选上也无法超过当前最大长度，提前返回
            int remaining_max_length = current_length;
            for (int i = index; i < masks.size(); i++) {
                if ((current_mask & masks[i]) == 0) {
                    remaining_max_length += lengths[i];
                }
            }
            
            if (remaining_max_length <= max_len) {
                return;  // 剪枝
            }
            
            // 回溯
            for (int i = index; i < masks.size(); i++) {
                if ((current_mask & masks[i]) == 0) {  // 没有共同的字符
                    optimized_backtrack(i + 1, 
                                       current_mask | masks[i], 
                                       current_length + lengths[i]);
                }
            }
        };
    
    // 调用优化的回溯函数
    optimized_backtrack(0, 0, 0);
    return max_len;
}

// 方法四：工程化改进版本，增加参数验证和异常处理
int maxLengthWithValidation(const std::vector<std::string>& arr) {
    try {
        // 参数验证
        // 在C++中，不需要检查arr是否为nullptr，因为vector是值类型
        
        // 检查数组长度是否在题目限制范围内
        if (arr.size() > 16) {
            throw std::invalid_argument("Input list size exceeds maximum allowed length of 16");
        }
        
        // 验证每个字符串是否符合要求
        std::regex lowercase_regex("^[a-z]+$");
        for (const std::string& s : arr) {
            if (s.length() > 26) {
                throw std::invalid_argument("String element exceeds maximum allowed length of 26");
            }
            // 检查是否只包含小写英文字母
            if (!std::regex_match(s, lowercase_regex)) {
                throw std::invalid_argument("String elements must contain only lowercase English letters");
            }
        }
        
        // 使用方法三实现
        return maxLength3(arr);
    } catch (const std::exception& e) {
        // 记录异常日志（在实际应用中）
        std::cerr << "Error in maxLengthWithValidation: " << e.what() << std::endl;
        // 异常情况下返回0
        return 0;
    }
}

// 单元测试函数
void runTests() {
    std::cout << "Running unit tests..." << std::endl;
    
    struct TestCase {
        std::vector<std::string> input;
        int expected;
    };
    
    std::vector<TestCase> testCases = {
        {{"un", "iq", "ue"}, 4},
        {{"cha", "r", "act", "ers"}, 6},
        {{"abcdefghijklmnopqrstuvwxyz"}, 26},
        {{"abc", "def", "a", "ghi", "abb"}, 9}
    };
    
    // 定义要测试的函数和名称
    struct Method {
        int (*func)(const std::vector<std::string>&);
        std::string name;
    };
    
    std::vector<Method> methods = {
        {maxLength1, "Method 1 (Backtracking)"},
        {maxLength2, "Method 2 (Dynamic Programming)"},
        {maxLength3, "Method 3 (Optimized Backtracking)"},
        {maxLengthWithValidation, "Method 4 (With Validation)"}
    };
    
    // 测试所有方法
    for (const auto& method : methods) {
        std::cout << "\n" << method.name << ":" << std::endl;
        for (const auto& test : testCases) {
            int result = method.func(test.input);
            const std::string& status = (result == test.expected) ? "PASS" : "FAIL";
            std::cout << "  Input: [";
            for (size_t i = 0; i < test.input.size(); ++i) {
                std::cout << "\"" << test.input[i] << "\"";
                if (i < test.input.size() - 1) std::cout << ", ";
            }
            std::cout << "] -> Result: " << result 
                      << " (Expected: " << test.expected 
                      << ") - " << status << std::endl;
        }
    }
}

// 性能测试函数
void performanceTest() {
    std::cout << "\nRunning performance tests..." << std::endl;
    
    // 生成测试数据：所有字母组合
    std::vector<std::string> largeArr;
    for (int i = 0; i < 10; i++) {
        // 生成不重复字符的字符串
        std::string s;
        for (int j = 0; j < 5 && (i * 5 + j) < 26; j++) {
            s += (char)('a' + i * 5 + j);
        }
        largeArr.push_back(s);
    }
    
    // 测试方法一时间
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = maxLength1(largeArr);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "\nPerformance of Method 1 (Backtracking): " << duration << " μs, Result: " << result1 << std::endl;
    
    // 测试方法二时间
    start = std::chrono::high_resolution_clock::now();
    int result2 = maxLength2(largeArr);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "Performance of Method 2 (Dynamic Programming): " << duration << " μs, Result: " << result2 << std::endl;
    
    // 测试方法三时间
    start = std::chrono::high_resolution_clock::now();
    int result3 = maxLength3(largeArr);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "Performance of Method 3 (Optimized Backtracking): " << duration << " μs, Result: " << result3 << std::endl;
    
    // 测试方法四时间
    start = std::chrono::high_resolution_clock::now();
    int result4 = maxLengthWithValidation(largeArr);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "Performance of Method 4 (With Validation): " << duration << " μs, Result: " << result4 << std::endl;
}

// 主函数
int main() {
    std::cout << "LeetCode 1239. 串联字符串的最大长度" << std::endl;
    std::cout << "Using bitwise operations for optimization" << std::endl;
    
    // 运行单元测试
    runTests();
    
    // 运行性能测试
    performanceTest();
    
    // 复杂度分析
    std::cout << "\n复杂度分析:" << std::endl;
    std::cout << "方法一（回溯算法）:" << std::endl;
    std::cout << "  时间复杂度: O(2^n)，其中n是有效字符串的数量（过滤后）" << std::endl;
    std::cout << "  空间复杂度: O(n)，递归调用栈的深度" << std::endl;
    
    std::cout << "\n方法二（动态规划）:" << std::endl;
    std::cout << "  时间复杂度: O(n * 2^m)，其中n是字符串数量，m是字符集大小（最多26）" << std::endl;
    std::cout << "  空间复杂度: O(2^m)，存储所有可能的掩码组合" << std::endl;
    
    std::cout << "\n方法三（优化的回溯算法）:" << std::endl;
    std::cout << "  时间复杂度: O(2^n)，但通过剪枝优化实际运行时间" << std::endl;
    std::cout << "  空间复杂度: O(n)" << std::endl;
    std::cout << "  优点: 利用剪枝减少不必要的计算，对于大多数情况效率更高" << std::endl;
    
    std::cout << "\n适用场景总结:" << std::endl;
    std::cout << "1. 当数组长度较小时，三种方法都可以使用" << std::endl;
    std::cout << "2. 当字符串包含大量重复字符时，方法三的剪枝效果更好" << std::endl;
    std::cout << "3. 当需要更稳定的性能时，方法二的动态规划更可靠" << std::endl;
    std::cout << "4. 在工程实践中，应根据具体数据特征选择合适的方法" << std::endl;
    
    return 0;
}
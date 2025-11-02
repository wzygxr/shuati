#include <iostream>
#include <vector>
#include <cstring>
#include <string>
#include <algorithm>
#include <chrono>
#include <functional>
#include <tuple>
using namespace std;

/**
 * 数位DP通用模板 (C++版本)
 * 
 * 数位DP是一种用于解决与数字的数位相关问题的动态规划技术。
 * 它通常用于统计某个区间内满足特定条件的数字个数，或者计算这些数字的某种属性总和。
 * 
 * 核心思想：
 * 1. 将问题转化为计算[0, n]范围内满足条件的数字个数，然后利用前缀和思想计算[a, b]区间的结果
 * 2. 逐位处理数字，使用记忆化搜索避免重复计算
 * 3. 状态设计通常包括：
 *    - 当前处理到第几位
 *    - 前一位数字（或前面的状态）
 *    - 是否受到上界限制
 *    - 是否有前导零
 *    - 其他题目相关的状态
 * 
 * 时间复杂度：通常为O(log n * 状态数)
 * 空间复杂度：O(状态数)
 * 
 * 应用场景：
 * - 统计特定数字出现次数（如LeetCode 233）
 * - 统计满足数位条件的数字个数（如不含连续1的数字）
 * - 统计各位数字不同的数字个数（如LeetCode 1012）
 * - 统计包含或不包含特定子串的数字个数
 * 
 * 作者：algorithm-journey
 * 日期：2024
 */

class DigitDP_Template_Complete {
private:
    // 存储数字的各位
    vector<int> digits;
    // 记忆化数组 - 对于不同的问题需要不同维度的记忆化数组
    // 使用long long避免溢出
    long long memo[20][1 << 10][2][2]; // 位置，已使用数字mask，是否受限，是否已经开始选择数字
    
    /**
     * 数位DP核心函数 - 统计各位数字不重复的数字个数
     * 
     * @param pos 当前处理到第几位（从0开始）
     * @param mask 已使用的数字状态（用位运算表示），每一位表示对应数字是否已使用
     * @param is_limit 是否受到上界限制
     * @param is_num 是否已经开始选择数字（处理前导零）
     * @return 从当前状态到末尾可构造的满足条件的数字个数
     */
    long long dfs(int pos, int mask, bool is_limit, bool is_num) {
        // 递归终止条件：处理完所有数位
        if (pos == digits.size()) {
            // 只有在已经开始选择数字的情况下才算一个有效数字
            return is_num ? 1 : 0;
        }
        
        // 如果当前状态已经被计算过且不受限制，则直接返回缓存的结果
        if (!is_limit && memo[pos][mask][is_num][0] != -1) {
            return memo[pos][mask][is_num][0];
        }
        // 如果当前状态已经被计算过且受限制，则直接返回缓存的结果
        if (is_limit && memo[pos][mask][is_num][1] != -1) {
            return memo[pos][mask][is_num][1];
        }
        
        long long result = 0;
        
        // 如果还没有开始选择数字，可以继续跳过（处理前导零）
        if (!is_num) {
            result += dfs(pos + 1, mask, false, false);
        }
        
        // 确定当前位可以填入的数字范围
        int up = is_limit ? digits[pos] : 9;
        // 确定起始数字：如果还没开始选数字，则从1开始（避免前导零）
        int start = is_num ? 0 : 1;
        
        // 枚举当前位可以填入的数字
        for (int digit = start; digit <= up; ++digit) {
            // 约束条件：避免重复数字
            if ((mask >> digit) & 1) {
                continue;
            }
            
            // 递归处理下一位，更新状态
            // 新的limit状态：只有当前受限且填的数字等于上限时，下一位才受限
            // 新的is_num状态：当前已经开始选择数字
            result += dfs(
                pos + 1, 
                mask | (1 << digit),  // 标记当前数字已使用
                is_limit && (digit == up), 
                true
            );
        }
        
        // 缓存结果
        if (is_limit) {
            memo[pos][mask][is_num][1] = result;
        } else {
            memo[pos][mask][is_num][0] = result;
        }
        
        return result;
    }
    
    /**
     * 初始化数字的各位
     * 
     * @param n 输入数字
     */
    void init(int n) {
        digits.clear();
        if (n == 0) {
            digits.push_back(0);
            return;
        }
        
        while (n > 0) {
            digits.push_back(n % 10);
            n /= 10;
        }
        reverse(digits.begin(), digits.end());
    }
    
    /**
     * 清除记忆化数组
     */
    void clearMemo() {
        memset(memo, -1, sizeof(memo));
    }
    
    /**
     * 数位DP核心函数 - 不含4和连续62的问题
     * 
     * @param pos 当前处理到第几位
     * @param last 上一位的数字
     * @param is_limit 是否受到上界限制
     * @param has_62 是否已经出现62
     * @return 满足条件的数字个数
     */
    long long dfsNo62(int pos, int last, bool is_limit, bool has_62, long long no62_memo[20][10][2][2]) {
        if (has_62) {
            return 0;
        }
        if (pos == digits.size()) {
            return 1;
        }
        
        if (!is_limit && no62_memo[pos][last+1][has_62][0] != -1) {
            return no62_memo[pos][last+1][has_62][0];
        }
        if (is_limit && no62_memo[pos][last+1][has_62][1] != -1) {
            return no62_memo[pos][last+1][has_62][1];
        }
        
        long long result = 0;
        int up = is_limit ? digits[pos] : 9;
        
        for (int digit = 0; digit <= up; ++digit) {
            if (digit == 4) {  // 不能包含4
                continue;
            }
            bool new_has_62 = has_62 || (last == 6 && digit == 2);
            result += dfsNo62(
                pos + 1, 
                digit,
                is_limit && (digit == up), 
                new_has_62,
                no62_memo
            );
        }
        
        if (is_limit) {
            no62_memo[pos][last+1][has_62][1] = result;
        } else {
            no62_memo[pos][last+1][has_62][0] = result;
        }
        
        return result;
    }
    
public:
    /**
     * 构造函数
     */
    DigitDP_Template_Complete() {
        // 初始化记忆化数组
        clearMemo();
    }
    
    /**
     * 主函数：计算[0, n]范围内各位数字都不重复的数字个数
     * 这是LeetCode 2376的解决方案
     * 
     * @param n 上界
     * @return 满足条件的数字个数
     */
    long long countSpecialNumbers(int n) {
        if (n < 0) {
            return 0;
        }
        init(n);
        clearMemo();
        return dfs(0, 0, true, false);
    }
    
    /**
     * 统计[low, high]范围内满足条件的数字个数
     * 使用前缀和思想：count(high) - count(low-1)
     * 
     * @param low 下界
     * @param high 上界
     * @return 区间[low, high]内满足条件的数字个数
     */
    long long countRange(int low, int high) {
        if (low <= 0) {
            return countSpecialNumbers(high);
        }
        return countSpecialNumbers(high) - countSpecialNumbers(low - 1);
    }
    
    /**
     * 统计[0, n]范围内数字1出现的次数
     * 这是LeetCode 233的解决方案
     * 
     * @param n 上界
     * @return 数字1出现的总次数
     */
    long long countDigitOne(int n) {
        if (n < 0) {
            return 0;
        }
        
        string s = to_string(n);
        long long count = 0;
        long long pos = 1; // 当前位的权值
        long long high = n / 10; // 高位部分
        long long current = n % 10; // 当前位
        long long low = 0; // 低位部分
        
        // 逐位分析1的出现次数
        while (high != 0 || current != 0) {
            if (current == 0) {
                // 当前位是0，则1出现的次数由高位决定
                count += high * pos;
            } else if (current == 1) {
                // 当前位是1，则1出现的次数由高位和低位共同决定
                count += high * pos + low + 1;
            } else {
                // 当前位大于1，则1出现的次数由高位决定（高位+1）
                count += (high + 1) * pos;
            }
            
            // 更新各部分
            low += current * pos;
            current = high % 10;
            high /= 10;
            pos *= 10;
        }
        
        return count;
    }
    
    /**
     * 统计[0, n]范围内二进制表示中不含连续1的数字个数
     * 这是LeetCode 600的解决方案
     * 
     * @param n 上界
     * @return 满足条件的数字个数
     */
    int findIntegers(int n) {
        if (n == 0) {
            return 1;
        }
        
        // 转换为二进制字符串
        string binary;
        int temp = n;
        while (temp > 0) {
            binary = to_string(temp % 2) + binary;
            temp /= 2;
        }
        
        int length = binary.size();
        
        // dp[i][0]表示i位二进制数，最高位为0时的有效数
        // dp[i][1]表示i位二进制数，最高位为1时的有效数
        vector<vector<int>> dp(length, vector<int>(2, 0));
        
        // 初始状态：1位二进制数
        dp[0][0] = 1; // 数字0
        dp[0][1] = 1; // 数字1
        
        // 填充dp数组 - 自底向上的动态规划
        for (int i = 1; i < length; ++i) {
            dp[i][0] = dp[i-1][0] + dp[i-1][1];  // 最高位为0，后面可以接0或1
            dp[i][1] = dp[i-1][0];               // 最高位为1，后面只能接0
        }
        
        // 计算结果
        int result = dp[length-1][0] + dp[length-1][1];
        
        // 检查是否存在连续1的情况，需要减去不符合条件的数
        for (int i = 1; i < length; ++i) {
            if (binary[i] == '1' && binary[i-1] == '1') {
                break; // 出现连续1，不需要调整
            }
            if (binary[i] == '0' && binary[i-1] == '1') {
                // 调整结果
                string suffix = binary.substr(i+1);
                int suffix_val = 0;
                if (!suffix.empty()) {
                    suffix_val = stoi(suffix, nullptr, 2);
                }
                result -= suffix_val + 1;
                break;
            }
        }
        
        return result;
    }
    
    /**
     * 统计[0, n]范围内不含数字4和连续的62的数的个数
     * 这是HDU 2089的解决方案
     * 
     * @param n 上界
     * @return 满足条件的数字个数
     */
    long long countNo62(int n) {
        if (n < 0) {
            return 0;
        }
        
        init(n);
        // 使用专用的记忆化数组
        long long no62_memo[20][10][2][2];
        memset(no62_memo, -1, sizeof(no62_memo));
        
        return dfsNo62(0, -1, true, false, no62_memo);
    }
    
    /**
     * 测量函数执行时间的辅助类
     */
    template<typename Func, typename... Args>
    auto measurePerformance(Func&& func, Args&&... args) -> pair<decltype(func(forward<Args>(args)...)), double> {
        auto start = chrono::high_resolution_clock::now();
        auto result = func(forward<Args>(args)...);
        auto end = chrono::high_resolution_clock::now();
        chrono::duration<double> duration = end - start;
        return make_pair(result, duration.count());
    }
};

/**
 * 运行全面的测试用例
 */
/*
void runComprehensiveTests() {
    DigitDP_Template_Complete solution;
    
    cout << "=== 数位DP模板综合测试 ===\n" << endl;
    
    // 测试用例1：统计各位数字不重复的数字个数
    vector<pair<int, long long>> testCases = {
        {20, 19},      // [0,20]中有19个各位数字不重复的数
        {100, 91},     // [0,100]中有91个各位数字不重复的数
        {200, 189},    // [0,200]中有189个各位数字不重复的数
        {1, 1},        // 边界情况：只有0和1
        {0, 1}         // 边界情况：只有0
    };
    
    cout << "1. 测试各位数字不重复的数字统计：" << endl;
    for (const auto& tc : testCases) {
        int n = tc.first;
        long long expected = tc.second;
        auto [result, timeTaken] = solution.measurePerformance(&DigitDP_Template_Complete::countSpecialNumbers, &solution, n);
        string status = (result == expected) ? "通过" : "失败";
        cout << "   n = " << n << ", 结果 = " << result << ", " << status << ", 耗时 = " << timeTaken * 1000 << "毫秒" << endl;
    }
    
    // 测试用例2：统计数字1出现的次数
    vector<pair<int, long long>> digitOneCases = {
        {13, 6},       // [0,13]中1出现6次
        {0, 0},        // 边界情况：0
        {1, 1},        // 边界情况：1
        {100, 21},     // [0,100]中1出现21次
        {1000, 301}    // [0,1000]中1出现301次
    };
    
    cout << "\n2. 测试数字1出现次数统计：" << endl;
    for (const auto& tc : digitOneCases) {
        int n = tc.first;
        long long expected = tc.second;
        auto [result, timeTaken] = solution.measurePerformance(&DigitDP_Template_Complete::countDigitOne, &solution, n);
        string status = (result == expected) ? "通过" : "失败";
        cout << "   n = " << n << ", 结果 = " << result << ", " << status << ", 耗时 = " << timeTaken * 1000 << "毫秒" << endl;
    }
    
    // 测试用例3：统计二进制中不含连续1的数字个数
    vector<pair<int, int>> binaryCases = {
        {5, 5},        // 0,1,10,100,101 -> 5个
        {1, 2},        // 0,1 -> 2个
        {2, 3},        // 0,1,10 -> 3个
        {3, 3},        // 0,1,10 -> 3个（11不满足条件）
        {10, 8}        // 0,1,10,100,101,1000,1001,1010 -> 8个
    };
    
    cout << "\n3. 测试二进制不含连续1的数字统计：" << endl;
    for (const auto& tc : binaryCases) {
        int n = tc.first;
        int expected = tc.second;
        auto [result, timeTaken] = solution.measurePerformance(&DigitDP_Template_Complete::findIntegers, &solution, n);
        string status = (result == expected) ? "通过" : "失败";
        cout << "   n = " << n << ", 结果 = " << result << ", " << status << ", 耗时 = " << timeTaken * 1000 << "毫秒" << endl;
    }
    
    // 测试用例4：测试区间统计
    vector<tuple<int, int, long long>> rangeCases = {
        {10, 20, 9},   // [10,20]中有9个各位数字不重复的数
        {50, 100, 41}, // [50,100]中有41个各位数字不重复的数
        {1, 1, 1}      // 边界情况：单个数
    };
    
    cout << "\n4. 测试区间统计功能：" << endl;
    for (const auto& tc : rangeCases) {
        int low = get<0>(tc);
        int high = get<1>(tc);
        long long expected = get<2>(tc);
        auto [result, timeTaken] = solution.measurePerformance(&DigitDP_Template_Complete::countRange, &solution, low, high);
        string status = (result == expected) ? "通过" : "失败";
        cout << "   区间 [" << low << ", " << high << "], 结果 = " << result << ", " << status << ", 耗时 = " << timeTaken * 1000 << "毫秒" << endl;
    }
    
    cout << "\n=== 测试完成 ===" << endl;
}
*/

int main() {
    // 简单测试
    DigitDP_Template_Complete solution;
    int n = 100;
    cout << "简单测试 - n = " << n << ", 结果 = " << solution.countSpecialNumbers(n) << endl;
    
    // 运行综合测试（可选）
    // runComprehensiveTests();
    
    // 实际应用示例
    cout << "\n实际应用示例：" << endl;
    cout << "数字1在[0, 1000]中出现的次数: " << solution.countDigitOne(1000) << endl;
    cout << "[0, 100]中二进制不含连续1的数字个数: " << solution.findIntegers(100) << endl;
    cout << "[10, 200]中各位数字不重复的数字个数: " << solution.countRange(10, 200) << endl;
    cout << "[0, 200]中不含数字4和连续62的数字个数: " << solution.countNo62(200) << endl;
    
    return 0;
}
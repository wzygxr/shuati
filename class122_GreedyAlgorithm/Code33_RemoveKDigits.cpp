#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <random>
#include <chrono>
#include <stdexcept>
using namespace std;

/**
 * 移掉K位数字
 * 
 * 题目描述：
 * 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。
 * 请你以字符串形式返回这个最小的数字。
 * 
 * 来源：LeetCode 402
 * 链接：https://leetcode.cn/problems/remove-k-digits/
 * 
 * 算法思路：
 * 使用贪心算法 + 单调栈：
 * 1. 使用栈来保存最终结果
 * 2. 遍历字符串中的每个字符：
 *    - 当栈非空且栈顶元素大于当前字符且还有删除次数时，弹出栈顶元素
 *    - 将当前字符入栈
 * 3. 如果遍历完成后还有剩余的删除次数，从栈顶删除（因为栈是单调递增的）
 * 4. 处理前导零并返回结果
 * 
 * 时间复杂度：O(n) - 每个字符最多入栈出栈一次
 * 空间复杂度：O(n) - 栈的空间
 * 
 * 关键点分析：
 * - 贪心策略：移除高位较大的数字可以最大化减少数值
 * - 单调栈：维护一个单调递增的栈
 * - 边界处理：处理前导零和空结果
 * 
 * 工程化考量：
 * - 输入验证：检查字符串和k的有效性
 * - 性能优化：使用vector而非string拼接
 * - 可读性：清晰的变量命名和注释
 */
class Code33_RemoveKDigits {
public:
    /**
     * 移除k位数字使得剩下的数字最小
     * 
     * @param num 输入数字字符串
     * @param k 要移除的数字个数
     * @return 最小的数字字符串
     */
    static string removeKDigits(const string& num, int k) {
        // 输入验证
        if (num.empty()) {
            return "0";
        }
        if (k < 0) {
            throw invalid_argument("k必须是非负整数");
        }
        if (k >= num.length()) {
            return "0";
        }
        
        // 使用栈来保存结果
        vector<char> stack;
        
        for (char current : num) {
            // 当栈非空且栈顶元素大于当前字符且还有删除次数时，弹出栈顶元素
            while (!stack.empty() && k > 0 && stack.back() > current) {
                stack.pop_back();
                k--;
            }
            
            // 将当前字符入栈（避免前导零）
            if (!stack.empty() || current != '0') {
                stack.push_back(current);
            }
        }
        
        // 如果还有剩余的删除次数，从栈顶删除（因为栈是单调递增的）
        if (k > 0) {
            stack.resize(stack.size() - k);
        }
        
        // 处理空栈的情况
        if (stack.empty()) {
            return "0";
        }
        
        // 构建结果字符串
        return string(stack.begin(), stack.end());
    }
    
    /**
     * 优化版本：使用字符串直接操作
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static string removeKDigitsOptimized(const string& num, int k) {
        if (num.empty()) {
            return "0";
        }
        if (k < 0) {
            throw invalid_argument("k必须是非负整数");
        }
        if (k >= num.length()) {
            return "0";
        }
        
        string result;
        int remaining_k = k;
        
        for (char current : num) {
            while (!result.empty() && remaining_k > 0 && result.back() > current) {
                result.pop_back();
                remaining_k--;
            }
            
            if (!result.empty() || current != '0') {
                result.push_back(current);
            }
        }
        
        // 处理剩余的删除次数
        if (remaining_k > 0) {
            result.resize(result.length() - remaining_k);
        }
        
        // 去除前导零
        size_t start = result.find_first_not_of('0');
        if (start == string::npos) {
            return "0";
        }
        return result.substr(start);
    }
    
    /**
     * 验证函数：检查结果是否正确
     */
    static bool validateResult(const string& result, const string& expected) {
        // 去除前导零
        string r = result;
        r.erase(0, r.find_first_not_of('0'));
        if (r.empty()) r = "0";
        
        string e = expected;
        e.erase(0, e.find_first_not_of('0'));
        if (e.empty()) e = "0";
        
        return r == e;
    }
    
    /**
     * 运行测试用例
     */
    static void runTests() {
        cout << "=== 移掉K位数字测试 ===" << endl;
        
        // 测试用例1: num = "1432219", k = 3 -> "1219"
        string num1 = "1432219";
        int k1 = 3;
        cout << "测试用例1: num = \"" << num1 << "\", k = " << k1 << endl;
        string result1 = removeKDigits(num1, k1);
        string result2 = removeKDigitsOptimized(num1, k1);
        cout << "方法1结果: \"" << result1 << "\"" << endl; // "1219"
        cout << "方法2结果: \"" << result2 << "\"" << endl; // "1219"
        cout << "验证: " << (validateResult(result1, "1219") ? "通过" : "失败") << endl;
        
        // 测试用例2: num = "10200", k = 1 -> "200"
        string num2 = "10200";
        int k2 = 1;
        cout << "\n测试用例2: num = \"" << num2 << "\", k = " << k2 << endl;
        result1 = removeKDigits(num2, k2);
        result2 = removeKDigitsOptimized(num2, k2);
        cout << "方法1结果: \"" << result1 << "\"" << endl; // "200"
        cout << "方法2结果: \"" << result2 << "\"" << endl; // "200"
        cout << "验证: " << (validateResult(result1, "200") ? "通过" : "失败") << endl;
        
        // 测试用例3: num = "10", k = 2 -> "0"
        string num3 = "10";
        int k3 = 2;
        cout << "\n测试用例3: num = \"" << num3 << "\", k = " << k3 << endl;
        result1 = removeKDigits(num3, k3);
        result2 = removeKDigitsOptimized(num3, k3);
        cout << "方法1结果: \"" << result1 << "\"" << endl; // "0"
        cout << "方法2结果: \"" << result2 << "\"" << endl; // "0"
        cout << "验证: " << (validateResult(result1, "0") ? "通过" : "失败") << endl;
        
        // 测试用例4: num = "9", k = 1 -> "0"
        string num4 = "9";
        int k4 = 1;
        cout << "\n测试用例4: num = \"" << num4 << "\", k = " << k4 << endl;
        result1 = removeKDigits(num4, k4);
        result2 = removeKDigitsOptimized(num4, k4);
        cout << "方法1结果: \"" << result1 << "\"" << endl; // "0"
        cout << "方法2结果: \"" << result2 << "\"" << endl; // "0"
        cout << "验证: " << (validateResult(result1, "0") ? "通过" : "失败") << endl;
        
        // 测试用例5: num = "123456", k = 3 -> "123"
        string num5 = "123456";
        int k5 = 3;
        cout << "\n测试用例5: num = \"" << num5 << "\", k = " << k5 << endl;
        result1 = removeKDigits(num5, k5);
        result2 = removeKDigitsOptimized(num5, k5);
        cout << "方法1结果: \"" << result1 << "\"" << endl; // "123"
        cout << "方法2结果: \"" << result2 << "\"" << endl; // "123"
        cout << "验证: " << (validateResult(result1, "123") ? "通过" : "失败") << endl;
        
        // 边界测试：k = 0
        string num6 = "123";
        int k6 = 0;
        cout << "\n测试用例6: num = \"" << num6 << "\", k = " << k6 << endl;
        result1 = removeKDigits(num6, k6);
        result2 = removeKDigitsOptimized(num6, k6);
        cout << "方法1结果: \"" << result1 << "\"" << endl; // "123"
        cout << "方法2结果: \"" << result2 << "\"" << endl; // "123"
        cout << "验证: " << (validateResult(result1, "123") ? "通过" : "失败") << endl;
    }
    
    /**
     * 性能测试方法
     */
    static void performanceTest() {
        // 生成大规模测试数据
        string large_num;
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, 9);
        
        for (int i = 0; i < 10000; i++) {
            large_num += to_string(dis(gen));
        }
        int k = 500;
        
        cout << "\n=== 性能测试 ===" << endl;
        
        auto start1 = chrono::high_resolution_clock::now();
        string result1 = removeKDigits(large_num, k);
        auto end1 = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::microseconds>(end1 - start1);
        cout << "方法1执行时间: " << duration1.count() << " 微秒" << endl;
        cout << "结果长度: " << result1.length() << endl;
        
        auto start2 = chrono::high_resolution_clock::now();
        string result2 = removeKDigitsOptimized(large_num, k);
        auto end2 = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::microseconds>(end2 - start2);
        cout << "方法2执行时间: " << duration2.count() << " 微秒" << endl;
        cout << "结果长度: " << result2.length() << endl;
        
        // 验证结果一致性
        cout << "结果一致性: " << (result1 == result2 ? "一致" : "不一致") << endl;
    }
    
    /**
     * 算法复杂度分析
     */
    static void analyzeComplexity() {
        cout << "\n=== 算法复杂度分析 ===" << endl;
        cout << "方法1（单调栈）:" << endl;
        cout << "- 时间复杂度: O(n)" << endl;
        cout << "  - 每个字符最多入栈出栈一次" << endl;
        cout << "  - 总体线性时间复杂度" << endl;
        cout << "- 空间复杂度: O(n)" << endl;
        cout << "  - 栈的空间: O(n)" << endl;
        
        cout << "\n方法2（优化版本）:" << endl;
        cout << "- 时间复杂度: O(n)" << endl;
        cout << "  - 每个字符处理一次" << endl;
        cout << "  - 字符串操作效率高" << endl;
        cout << "- 空间复杂度: O(n)" << endl;
        cout << "  - 字符串空间: O(n)" << endl;
        
        cout << "\n贪心策略证明:" << endl;
        cout << "1. 高位数字对数值影响更大，优先移除高位较大的数字" << endl;
        cout << "2. 单调栈确保每次移除的都是当前最优选择" << endl;
        cout << "3. 数学归纳法证明贪心选择性质" << endl;
        
        cout << "\n工程化考量:" << endl;
        cout << "1. 输入验证：处理非法输入和边界情况" << endl;
        cout << "2. 性能优化：选择高效的数据结构" << endl;
        cout << "3. 可读性：清晰的算法逻辑和注释" << endl;
        cout << "4. 测试覆盖：全面的测试用例设计" << endl;
    }
};

int main() {
    Code33_RemoveKDigits::runTests();
    Code33_RemoveKDigits::performanceTest();
    Code33_RemoveKDigits::analyzeComplexity();
    return 0;
}
#include <iostream>
#include <climits>
#include <stdexcept>
using namespace std;

/**
 * 位运算实现加法器
 * 测试链接：自定义题目，展示位运算在计算机底层运算中的应用
 * 
 * 题目描述：
 * 使用位运算实现整数的加法、减法、乘法和除法运算。
 * 不使用+、-、*、/等算术运算符。
 * 
 * 解题思路：
 * 1. 加法：使用异或运算实现不考虑进位的加法，使用与运算和左移实现进位处理
 * 2. 减法：利用补码原理，a - b = a + (-b)，通过位运算求补码
 * 3. 乘法：使用移位和加法实现，类似于手算乘法
 * 4. 除法：使用移位和减法实现，类似于手算除法
 * 
 * 时间复杂度：
 * - 加法：O(1) - 常数次循环（最多32次）
 * - 减法：O(1) - 基于加法实现
 * - 乘法：O(1) - 最多32次循环
 * - 除法：O(1) - 最多32次循环
 * 
 * 空间复杂度：O(1) - 只使用常数个变量
 */
class BitAdder {
public:
    /**
     * 使用位运算实现整数加法
     * @param a 第一个加数
     * @param b 第二个加数
     * @return a + b 的结果
     */
    static int add(int a, int b) {
        // 思路：加法可以分解为不考虑进位的加法（异或运算）和进位（与运算并左移）
        // 重复这个过程直到进位为0
        
        while (b != 0) {
            // 计算不考虑进位的加法结果
            int sum = a ^ b;
            // 计算进位（需要左移一位）
            int carry = (a & b) << 1;
            // 更新a和b，继续处理进位
            a = sum;
            b = carry;
        }
        return a;
    }
    
    /**
     * 递归实现加法
     * @param a 第一个加数
     * @param b 第二个加数
     * @return a + b 的结果
     */
    static int addRecursive(int a, int b) {
        if (b == 0) {
            return a;
        }
        // 递归计算：a + b = (a ^ b) + ((a & b) << 1)
        return addRecursive(a ^ b, (a & b) << 1);
    }
    
    /**
     * 使用位运算实现整数减法
     * @param a 被减数
     * @param b 减数
     * @return a - b 的结果
     */
    static int subtract(int a, int b) {
        // 利用补码原理：a - b = a + (-b)
        // 求b的补码（按位取反再加1）
        return add(a, add(~b, 1));
    }
    
    /**
     * 使用位运算实现整数乘法
     * @param a 被乘数
     * @param b 乘数
     * @return a * b 的结果
     */
    static int multiply(int a, int b) {
        // 处理特殊情况
        if (a == 0 || b == 0) return 0;
        if (a == 1) return b;
        if (b == 1) return a;
        
        // 记录结果的符号
        bool negative = false;
        if ((a < 0 && b > 0) || (a > 0 && b < 0)) {
            negative = true;
        }
        
        // 取绝对值
        a = abs(a);
        b = abs(b);
        
        int result = 0;
        
        // 类似于手算乘法：将b的每一位与a相乘，然后左移相应的位数
        while (b != 0) {
            // 如果b的最低位是1，则加上a左移相应的位数
            if (b & 1) {
                result = add(result, a);
            }
            // a左移一位，相当于乘以2
            a <<= 1;
            // b右移一位，处理下一位
            b >>= 1;
        }
        
        return negative ? -result : result;
    }
    
    /**
     * 使用位运算实现整数除法（向下取整）
     * @param dividend 被除数
     * @param divisor 除数
     * @return dividend / divisor 的结果
     */
    static int divide(int dividend, int divisor) {
        // 处理特殊情况
        if (divisor == 0) {
            throw runtime_error("Division by zero");
        }
        if (dividend == 0) return 0;
        if (divisor == 1) return dividend;
        if (divisor == -1) {
            // 处理整数溢出情况
            if (dividend == INT_MIN) {
                return INT_MAX;
            }
            return -dividend;
        }
        
        // 记录结果的符号
        bool negative = (dividend < 0) ^ (divisor < 0);
        
        // 转换为正数处理（使用long long避免溢出）
        long long a = abs((long long)dividend);
        long long b = abs((long long)divisor);
        
        int result = 0;
        
        // 从最高位开始尝试
        for (int i = 31; i >= 0; i--) {
            if ((a >> i) >= b) {
                result = add(result, 1 << i);
                a = subtract(a, b << i);
            }
        }
        
        return negative ? -result : result;
    }
    
    /**
     * 更稳健的除法实现
     * @param dividend 被除数
     * @param divisor 除数
     * @return dividend / divisor 的结果
     */
    static int divideRobust(int dividend, int divisor) {
        // 处理除数为0的情况
        if (divisor == 0) return dividend >= 0 ? INT_MAX : INT_MIN;
        
        // 处理被除数为最小值且除数为-1的情况（会溢出）
        if (dividend == INT_MIN && divisor == -1) {
            return INT_MAX;
        }
        
        // 确定符号
        bool negative = (dividend < 0) ^ (divisor < 0);
        
        // 转换为正数（使用long long避免溢出）
        long long a = abs((long long)dividend);
        long long b = abs((long long)divisor);
        
        int result = 0;
        
        // 使用减法实现除法
        while (a >= b) {
            long long temp = b;
            int multiple = 1;
            
            // 快速倍增：每次将除数翻倍，直到接近被除数
            while (a >= (temp << 1)) {
                temp <<= 1;
                multiple <<= 1;
            }
            
            a -= temp;
            result += multiple;
        }
        
        return negative ? -result : result;
    }
    
private:
    // 辅助函数：计算绝对值（避免使用标准库函数）
    static long long abs(long long x) {
        return x < 0 ? -x : x;
    }
};

// 测试函数
int main() {
    // 测试加法
    cout << "=== 加法测试 ===" << endl;
    cout << "5 + 3 = " << BitAdder::add(5, 3) << endl; // 8
    cout << "-5 + 3 = " << BitAdder::add(-5, 3) << endl; // -2
    cout << "5 + (-3) = " << BitAdder::add(5, -3) << endl; // 2
    cout << "-5 + (-3) = " << BitAdder::add(-5, -3) << endl; // -8
    
    // 测试减法
    cout << "\n=== 减法测试 ===" << endl;
    cout << "5 - 3 = " << BitAdder::subtract(5, 3) << endl; // 2
    cout << "3 - 5 = " << BitAdder::subtract(3, 5) << endl; // -2
    cout << "-5 - 3 = " << BitAdder::subtract(-5, 3) << endl; // -8
    cout << "5 - (-3) = " << BitAdder::subtract(5, -3) << endl; // 8
    
    // 测试乘法
    cout << "\n=== 乘法测试 ===" << endl;
    cout << "5 * 3 = " << BitAdder::multiply(5, 3) << endl; // 15
    cout << "5 * (-3) = " << BitAdder::multiply(5, -3) << endl; // -15
    cout << "-5 * 3 = " << BitAdder::multiply(-5, 3) << endl; // -15
    cout << "-5 * (-3) = " << BitAdder::multiply(-5, -3) << endl; // 15
    
    // 测试除法
    cout << "\n=== 除法测试 ===" << endl;
    cout << "15 / 3 = " << BitAdder::divide(15, 3) << endl; // 5
    cout << "15 / (-3) = " << BitAdder::divide(15, -3) << endl; // -5
    cout << "-15 / 3 = " << BitAdder::divide(-15, 3) << endl; // -5
    cout << "-15 / (-3) = " << BitAdder::divide(-15, -3) << endl; // 5
    cout << "16 / 3 = " << BitAdder::divide(16, 3) << endl; // 5（向下取整）
    
    // 测试边界情况
    cout << "\n=== 边界测试 ===" << endl;
    cout << "0 + 5 = " << BitAdder::add(0, 5) << endl; // 5
    cout << "5 * 0 = " << BitAdder::multiply(5, 0) << endl; // 0
    cout << "0 / 5 = " << BitAdder::divide(0, 5) << endl; // 0
    
    // 验证递归加法
    cout << "\n=== 递归加法测试 ===" << endl;
    cout << "5 + 3 = " << BitAdder::addRecursive(5, 3) << endl; // 8
    cout << "-5 + 3 = " << BitAdder::addRecursive(-5, 3) << endl; // -2
    
    return 0;
}
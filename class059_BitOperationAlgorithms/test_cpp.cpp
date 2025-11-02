#include <iostream>
#include <vector>
using namespace std;

// 简单测试函数声明
int add(int a, int b);
int my_minus(int a, int b);
int multiply(int a, int b);
int hammingWeight(unsigned int n);

/**
 * 加法实现
 * 
 * 算法原理：
 * 1. 异或运算(^)实现无进位加法
 * 2. 与运算(&)和左移(<<)实现进位
 * 3. 循环直到没有进位
 * 
 * 时间复杂度：O(1) - 固定位数的整数
 * 空间复杂度：O(1) - 只使用常数级额外空间
 * 
 * @param a 第一个加数
 * @param b 第二个加数
 * @return a与b的和
 */
int add(int a, int b) {
    int ans = a;
    while (b != 0) {
        int temp = a ^ b;
        b = (a & b) << 1;
        a = temp;
    }
    return a;
}

/**
 * 减法实现
 * 
 * 算法原理：
 * 基于加法和相反数实现
 * a - b = a + (-b)
 * 
 * 时间复杂度：O(1) - 固定位数的整数
 * 空间复杂度：O(1) - 只使用常数级额外空间
 * 
 * @param a 被减数
 * @param b 减数
 * @return a与b的差
 */
int my_minus(int a, int b) {
    return add(a, ~b + 1); // a - b = a + (-b)
}

/**
 * 乘法实现
 * 
 * 算法原理：
 * 基于二进制分解
 * 检查乘数b的每一位是否为1
 * 如果为1，则将被乘数a左移相应位数后累加到结果中
 * 
 * 时间复杂度：O(log b) - b的二进制位数
 * 空间复杂度：O(1) - 只使用常数级额外空间
 * 
 * @param a 被乘数
 * @param b 乘数
 * @return a与b的积
 */
int multiply(int a, int b) {
    int ans = 0;
    while (b != 0) {
        if ((b & 1) != 0) {
            ans = add(ans, a);
        }
        a <<= 1;
        b >>= 1;
    }
    return ans;
}

/**
 * 计算一个数字的二进制表示中1的个数（汉明重量）
 * 
 * 算法原理：
 * 遍历32位，检查每一位是否为1
 * 
 * 时间复杂度: O(1) - 最多循环32次（32位整数）
 * 空间复杂度: O(1) - 只使用常数级额外空间
 * 
 * @param n 输入的无符号整数
 * @return n的二进制表示中1的个数
 */
int hammingWeight(unsigned int n) {
    int count = 0;
    for (int i = 0; i < 32; i++) {
        if ((n & (1U << i)) != 0) {
            count++;
        }
    }
    return count;
}

/**
 * 主函数，程序入口点
 * 执行所有位运算算法的测试用例
 */
int main() {
    cout << "位运算测试：" << endl;
    
    // 测试加法
    cout << "加法测试：" << endl;
    cout << "10 + 20 = " << add(10, 20) << endl;
    cout << "(-5) + 3 = " << add(-5, 3) << endl;
    
    // 测试减法
    cout << "\n减法测试：" << endl;
    cout << "10 - 5 = " << my_minus(10, 5) << endl;
    cout << "5 - 10 = " << my_minus(5, 10) << endl;
    
    // 测试乘法
    cout << "\n乘法测试：" << endl;
    cout << "6 * 7 = " << multiply(6, 7) << endl;
    cout << "(-3) * 4 = " << multiply(-3, 4) << endl;
    
    // 测试汉明重量
    cout << "\n汉明重量测试：" << endl;
    cout << "二进制中1的个数 (10): " << hammingWeight(10) << endl;
    
    return 0;
}
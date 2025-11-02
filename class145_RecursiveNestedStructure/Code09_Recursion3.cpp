// HackerRank Day 9: Recursion 3
// 题目来源：https://www.hackerrank.com/challenges/30-recursion/problem
//
// 题目描述：
// 编写一个递归函数来计算一个非负整数的阶乘。
// 阶乘的定义：n! = n × (n-1) × (n-2) × ... × 3 × 2 × 1
// 特别地，0! = 1
//
// 解题思路：
// 使用递归计算阶乘，基础情况是n=0或n=1时返回1
// 递归情况是n * factorial(n-1)
//
// 时间复杂度：O(n)，递归深度为n
// 空间复杂度：O(n)，递归调用栈的深度

#include <iostream>
using namespace std;

class Solution {
public:
    /**
     * 递归计算阶乘
     * @param n 非负整数
     * @return n的阶乘
     */
    int factorial(int n) {
        // 基础情况：0! = 1, 1! = 1
        if (n == 0 || n == 1) {
            return 1;
        }
        
        // 递归情况：n! = n * (n-1)!
        return n * factorial(n - 1);
    }
    
    /**
     * 迭代计算阶乘（避免递归栈溢出）
     * @param n 非负整数
     * @return n的阶乘
     */
    int factorialIterative(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    /**
     * 尾递归优化版本（某些编译器可以优化为迭代）
     * @param n 非负整数
     * @param acc 累积结果
     * @return n的阶乘
     */
    int factorialTailRecursive(int n, int acc = 1) {
        if (n == 0 || n == 1) {
            return acc;
        }
        return factorialTailRecursive(n - 1, n * acc);
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 5;
    cout << "输入: " << n1 << endl;
    cout << "递归输出: " << solution.factorial(n1) << endl;
    cout << "迭代输出: " << solution.factorialIterative(n1) << endl;
    cout << "尾递归输出: " << solution.factorialTailRecursive(n1) << endl;
    cout << "期望: 120" << endl << endl;
    
    // 测试用例2
    int n2 = 0;
    cout << "输入: " << n2 << endl;
    cout << "递归输出: " << solution.factorial(n2) << endl;
    cout << "迭代输出: " << solution.factorialIterative(n2) << endl;
    cout << "尾递归输出: " << solution.factorialTailRecursive(n2) << endl;
    cout << "期望: 1" << endl << endl;
    
    // 测试用例3
    int n3 = 10;
    cout << "输入: " << n3 << endl;
    cout << "递归输出: " << solution.factorial(n3) << endl;
    cout << "迭代输出: " << solution.factorialIterative(n3) << endl;
    cout << "尾递归输出: " << solution.factorialTailRecursive(n3) << endl;
    cout << "期望: 3628800" << endl << endl;
    
    // 性能测试：比较不同方法的时间
    cout << "性能测试（n=20）:" << endl;
    int n4 = 20;
    cout << "迭代输出: " << solution.factorialIterative(n4) << endl;
    cout << "尾递归输出: " << solution.factorialTailRecursive(n4) << endl;
    
    return 0;
}

/**
 * 算法分析：
 * 
 * 时间复杂度分析：
 * - 递归版本：O(n)，需要n次递归调用
 * - 迭代版本：O(n)，需要n次循环迭代
 * - 尾递归版本：O(n)，但某些编译器可以优化为O(1)空间
 * 
 * 空间复杂度分析：
 * - 递归版本：O(n)，递归调用栈的深度
 * - 迭代版本：O(1)，只需要常数空间
 * - 尾递归版本：O(1)（如果编译器支持尾递归优化）
 * 
 * 算法优化思路：
 * 1. 使用迭代避免递归栈溢出
 * 2. 使用尾递归优化（如果编译器支持）
 * 3. 对于大数阶乘，可以使用高精度计算
 * 4. 可以预计算常用阶乘值，使用查表法
 * 
 * 工程化考量：
 * 1. 异常处理：添加对负数的检查
 * 2. 边界条件：处理0和1的特殊情况
 * 3. 性能优化：对于大数，考虑使用高精度算法
 * 4. 内存管理：避免递归深度过大导致的栈溢出
 * 
 * 相关题目对比：
 * 1. LeetCode 50. Pow(x, n)：快速幂的递归实现
 * 2. LeetCode 70. Climbing Stairs：爬楼梯问题的递归解法
 * 3. Fibonacci数列：经典的递归问题
 * 
 * 算法应用场景：
 * 1. 组合数学计算
 * 2. 概率统计
 * 3. 算法复杂度分析
 * 4. 密码学中的排列组合计算
 * 
 * C++语言特性利用：
 * 1. 函数重载：提供多种实现方式
 * 2. 默认参数：简化尾递归调用
 * 3. 内联函数：可以考虑将小函数内联
 * 4. 模板元编程：编译期计算阶乘（对于小数值）
 * 
 * 递归与迭代的选择：
 * 1. 递归：代码简洁，易于理解，但可能有栈溢出风险
 * 2. 迭代：性能更好，不会栈溢出，但代码可能稍复杂
 * 3. 尾递归：结合两者的优点，但依赖编译器优化
 * 
 * 数学性质：
 * 阶乘函数增长非常快，n=13时就会超出32位整数范围
 * 在实际应用中需要考虑使用大数运算库
 */
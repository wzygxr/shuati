#include <iostream>
#include <vector>

/**
 * 修理宝塔 - C++实现
 * 
 * 题目描述：
 * 一共有编号1~n的宝塔，其中a号和b号宝塔已经修好了
 * Yuwgna和Iaka两个人轮流修塔，Yuwgna先手，Iaka后手，谁先修完所有的塔谁赢
 * 每次可以选择j+k号或者j-k号塔进行修理，其中j和k是任意两个已经修好的塔
 * 也就是输入n、a、b，如果先手赢打印"Yuwgna"，后手赢打印"Iaka"
 * 
 * 解题思路：
 * 1. 根据数论知识，能修的塔的数量与gcd(a,b)有关
 * 2. 能修的塔的编号是gcd(a,b)的倍数
 * 3. 总共有n/gcd(a,b)个塔需要修
 * 4. 如果这个数量是奇数，先手赢；否则后手赢
 * 
 * 算法复杂度：
 * 时间复杂度：O(log(min(a, b)))
 * 空间复杂度：O(log(min(a, b)))
 * 
 * 题目链接：
 * HDU 5512 Pagodas
 * https://acm.hdu.edu.cn/showproblem.php?pid=5512
 * 
 * 相关题目：
 * 1. 洛谷 P4549 【模板】裴蜀定理
 *    链接：https://www.luogu.com.cn/problem/P4549
 *    本题是裴蜀定理的模板题，与最大公约数有关
 * 
 * 2. Codeforces 1011E Border
 *    链接：https://codeforces.com/contest/1011/problem/E
 *    本题需要根据裴蜀定理求解可能到达的位置
 * 
 * 3. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法等情况
 * 2. 边界条件：需要考虑n、a、b的边界值
 * 3. 性能优化：使用欧几里得算法计算最大公约数
 * 
 * 调试能力：
 * 1. 添加断言验证中间结果
 * 2. 打印关键变量的实时值
 * 3. 性能退化排查
 */

/**
 * 欧几里得算法计算最大公约数
 * 
 * 算法原理：
 * gcd(a, b) = gcd(b, a % b)，当b为0时，gcd(a, 0) = a
 * 
 * 时间复杂度：O(log(min(a, b)))
 * 空间复杂度：O(log(min(a, b)))（递归调用栈）
 * 
 * @param a 第一个整数
 * @param b 第二个整数
 * @return a和b的最大公约数
 */
int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}

/**
 * 求解修理宝塔问题
 * 
 * @param n 宝塔总数
 * @param a 第一个已修好的宝塔编号
 * @param b 第二个已修好的宝塔编号
 * @return 先手赢返回"Yuwgna"，后手赢返回"Iaka"
 */
std::string solvePagodas(int n, int a, int b) {
    int g = gcd(a, b);
    int count = n / g;
    
    // 如果数量为奇数，先手赢；否则后手赢
    if (count % 2 == 1) {
        return "Yuwgna";
    } else {
        return "Iaka";
    }
}

/**
 * 主方法 - 修理宝塔问题测试
 */
int main() {
    std::cout << "=== 修理宝塔问题测试 ===" << std::endl;
    
    // 测试用例1
    int n1 = 6, a1 = 2, b1 = 3;
    std::string result1 = solvePagodas(n1, a1, b1);
    std::cout << "测试1: n=" << n1 << ", a=" << a1 << ", b=" << b1 
              << ", 结果: " << result1 << std::endl;
    
    // 测试用例2
    int n2 = 10, a2 = 3, b2 = 4;
    std::string result2 = solvePagodas(n2, a2, b2);
    std::cout << "测试2: n=" << n2 << ", a=" << a2 << ", b=" << b2 
              << ", 结果: " << result2 << std::endl;
    
    // 测试用例3
    int n3 = 8, a3 = 4, b3 = 6;
    std::string result3 = solvePagodas(n3, a3, b3);
    std::cout << "测试3: n=" << n3 << ", a=" << a3 << ", b=" << b3 
              << ", 结果: " << result3 << std::endl;
    
    std::cout << "=== 测试完成 ===" << std::endl;
    
    return 0;
}
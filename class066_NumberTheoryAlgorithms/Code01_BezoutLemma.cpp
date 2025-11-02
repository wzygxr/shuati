#include <iostream>
#include <vector>
#include <cmath>

/**
 * 裴蜀定理模版题 - C++实现
 * 
 * 题目描述：
 * 给定长度为n的一组整数值[a1, a2, a3...]，你找到一组数值[x1, x2, x3...]
 * 要让a1*x1 + a2*x2 + a3*x3...得到的结果为最小正整数
 * 返回能得到的最小正整数是多少
 * 
 * 解题思路：
 * 根据裴蜀定理，对于整数a1, a2, ..., an，存在整数x1, x2, ..., xn使得
 * a1*x1 + a2*x2 + ... + an*xn = gcd(a1, a2, ..., an)
 * 因此，线性组合能得到的最小正整数就是这n个数的最大公约数
 * 
 * 算法复杂度：
 * 时间复杂度：O(n * log(min(ai)))
 * 空间复杂度：O(1)
 * 
 * 题目链接：
 * 洛谷 P4549 【模板】裴蜀定理
 * https://www.luogu.com.cn/problem/P4549
 * 
 * 相关题目：
 * 1. HDU 5512 Pagodas
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
 *    本题涉及数论知识，与最大公约数有关
 * 
 * 2. Codeforces 1011E Border
 *    链接：https://codeforces.com/contest/1011/problem/E
 *    本题需要根据裴蜀定理求解可能到达的位置
 * 
 * 3. LeetCode 1250. 检查「好数组」
 *    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
 *    本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、负数等情况
 * 2. 边界条件：需要考虑n=1的情况
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
 * 主方法 - 裴蜀定理模板题
 * 
 * 算法思路：
 * 1. 读取输入的n个整数
 * 2. 依次计算这n个数的最大公约数
 * 3. 根据裴蜀定理，线性组合能得到的最小正整数就是最大公约数
 * 
 * @param argc 命令行参数个数
 * @param argv 命令行参数数组
 * @return 程序退出码
 */
int main(int argc, char* argv[]) {
    std::cout << "=== 裴蜀定理模板题测试 ===" << std::endl;
    
    // 测试用例1
    std::vector<int> nums1 = {6, 10, 15};
    int ans1 = 0;
    for (int num : nums1) {
        ans1 = gcd(std::abs(num), ans1);
    }
    std::cout << "测试1: [6, 10, 15] 的最小正整数组合 = " << ans1 << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {4, 6, 8};
    int ans2 = 0;
    for (int num : nums2) {
        ans2 = gcd(std::abs(num), ans2);
    }
    std::cout << "测试2: [4, 6, 8] 的最小正整数组合 = " << ans2 << std::endl;
    
    // 测试用例3
    std::vector<int> nums3 = {3, 6, 9};
    int ans3 = 0;
    for (int num : nums3) {
        ans3 = gcd(std::abs(num), ans3);
    }
    std::cout << "测试3: [3, 6, 9] 的最小正整数组合 = " << ans3 << std::endl;
    
    std::cout << "=== 测试完成 ===" << std::endl;
    
    return 0;
}
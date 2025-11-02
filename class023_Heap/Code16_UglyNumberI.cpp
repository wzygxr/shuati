#include <iostream>
using namespace std;

/**
 * 相关题目8: LeetCode 263. 丑数
 * 题目链接: https://leetcode.cn/problems/ugly-number/
 * 题目描述: 丑数 就是只包含质因数 2、3 和 5 的正整数。判断一个数是否是丑数。
 * 解题思路: 不断将数字除以2、3、5，直到无法整除，如果最终结果为1，则是丑数
 * 时间复杂度: O(log n)，因为每次除以至少2，数字减小的速度是对数级别的
 * 空间复杂度: O(1)，只使用常量额外空间
 * 是否最优解: 是，这是判断丑数的最优解法
 * 
 * 本题属于数学问题的一种，虽然不直接使用堆，但可以作为堆相关问题(如UglyNumberII)的基础
 */
class Solution {
public:
    /**
     * 判断一个数是否是丑数
     * @param n 需要判断的整数
     * @return 如果n是丑数返回true，否则返回false
     */
    bool isUgly(int n) {
        // 根据题目要求，丑数是正整数
        if (n <= 0) {
            return false; // 题目明确指出丑数是正整数，所以负数和0都不是丑数
        }
        
        // 调试信息：打印当前处理的数
        // cout << "判断是否是丑数: " << n << endl;
        
        // 不断除以2，直到不能再整除
        while (n % 2 == 0) {
            n = n / 2;
        }
        
        // 不断除以3，直到不能再整除
        while (n % 3 == 0) {
            n = n / 3;
        }
        
        // 不断除以5，直到不能再整除
        while (n % 5 == 0) {
            n = n / 5;
        }
        
        // 如果最终结果为1，则说明n的所有质因数只有2、3、5，是丑数
        return n == 1;
    }
};

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    Solution solution;
    
    // 测试用例1：基本情况 - 是丑数
    cout << "测试用例1：" << endl;
    cout << "6是否是丑数: " << (solution.isUgly(6) ? "是" : "否") << endl;  // 6 = 2 * 3，应该返回 true
    cout << "1是否是丑数: " << (solution.isUgly(1) ? "是" : "否") << endl;  // 1 没有质因数，所以是丑数，应该返回 true
    cout << "12是否是丑数: " << (solution.isUgly(12) ? "是" : "否") << endl;  // 12 = 2^2 * 3，应该返回 true
    
    // 测试用例2：基本情况 - 不是丑数
    cout << "\n测试用例2：" << endl;
    cout << "14是否是丑数: " << (solution.isUgly(14) ? "是" : "否") << endl;  // 14 = 2 * 7，包含质因数7，不是丑数，应该返回 false
    cout << "21是否是丑数: " << (solution.isUgly(21) ? "是" : "否") << endl;  // 21 = 3 * 7，包含质因数7，不是丑数，应该返回 false
    
    // 测试用例3：边界情况
    cout << "\n测试用例3：" << endl;
    cout << "0是否是丑数: " << (solution.isUgly(0) ? "是" : "否") << endl;  // 0 不是正整数，所以不是丑数，应该返回 false
    cout << "-6是否是丑数: " << (solution.isUgly(-6) ? "是" : "否") << endl;  // -6 是负数，所以不是丑数，应该返回 false
    cout << "2^30是否是丑数: " << (solution.isUgly(1073741824) ? "是" : "否") << endl;  // 2^30，应该返回 true
    
    // 测试用例4：特殊情况
    cout << "\n测试用例4：" << endl;
    cout << "5是否是丑数: " << (solution.isUgly(5) ? "是" : "否") << endl;  // 5 是质因数之一，应该返回 true
    cout << "100是否是丑数: " << (solution.isUgly(100) ? "是" : "否") << endl;  // 100 = 2^2 * 5^2，应该返回 true
    
    // 验证测试用例正确性
    bool test1 = solution.isUgly(6);
    bool test2 = solution.isUgly(1);
    bool test3 = solution.isUgly(14);
    bool test4 = solution.isUgly(0);
    bool test5 = solution.isUgly(-6);
    
    if (test1 && test2 && !test3 && !test4 && !test5) {
        cout << "\n所有测试用例验证通过！" << endl;
    } else {
        cout << "\n部分测试用例验证失败！" << endl;
    }
    
    return 0;
}
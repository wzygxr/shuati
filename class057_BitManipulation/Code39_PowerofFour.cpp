#include <iostream>
#include <vector>
#include <cmath>
#include <cstdint>
#include <bitset>

using namespace std;

/**
 * 4的幂（位运算解法）
 * 测试链接：https://leetcode.cn/problems/power-of-four/
 * 
 * 题目描述：
 * 给定一个整数，写一个函数来判断它是否是 4 的幂次方。如果是，返回 true；否则，返回 false。
 * 整数 n 是 4 的幂次方需满足：存在整数 x 使得 n == 4^x
 * 
 * 解题思路：
 * 1. 数学方法：循环除以4
 * 2. 位运算法：利用4的幂的二进制特性
 * 3. 对数方法：使用对数函数判断
 * 4. 位运算优化：结合2的幂和特殊位置判断
 * 5. 查表法：预计算所有4的幂
 * 
 * 时间复杂度分析：
 * - 数学方法：O(log₄n)
 * - 位运算法：O(1)
 * - 对数方法：O(1)
 * - 位运算优化：O(1)
 * - 查表法：O(1)
 * 
 * 空间复杂度分析：
 * - 数学方法：O(1)
 * - 位运算法：O(1)
 * - 对数方法：O(1)
 * - 位运算优化：O(1)
 * - 查表法：O(16) = O(1)
 */
class Solution {
public:
    /**
     * 方法1：数学方法（循环除以4）
     * 时间复杂度：O(log₄n)
     * 空间复杂度：O(1)
     */
    bool isPowerOfFour1(int n) {
        if (n <= 0) {
            return false;
        }
        
        while (n % 4 == 0) {
            n /= 4;
        }
        
        return n == 1;
    }
    
    /**
     * 方法2：位运算法（推荐）
     * 核心思想：4的幂一定是2的幂，且1出现在奇数位
     * 4的幂的二进制特点：
     * 1. 只有一个1（是2的幂）
     * 2. 1出现在奇数位（从1开始计数）
     * 3. 满足 n & (n-1) == 0 且 n & 0x55555555 != 0
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfFour2(int n) {
        // 检查n是否为正数，且是2的幂，且1出现在奇数位
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    /**
     * 方法3：对数方法
     * 使用换底公式：log₄n = logn / log4
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfFour3(int n) {
        if (n <= 0) {
            return false;
        }
        
        double logResult = log(n) / log(4);
        // 检查结果是否为整数（考虑浮点数精度）
        return abs(logResult - round(logResult)) < 1e-10;
    }
    
    /**
     * 方法4：位运算优化版
     * 结合多种位运算技巧
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfFour4(int n) {
        if (n <= 0) {
            return false;
        }
        
        // 检查是否是2的幂
        if ((n & (n - 1)) != 0) {
            return false;
        }
        
        // 检查1是否出现在奇数位
        // 0x55555555 = 01010101010101010101010101010101
        return (n & 0x55555555) != 0;
    }
    
    /**
     * 方法5：查表法
     * 预计算所有32位整数范围内的4的幂
     * 时间复杂度：O(1)
     * 空间复杂度：O(16) = O(1)
     */
    bool isPowerOfFour5(int n) {
        // 32位整数范围内所有4的幂
        vector<int> powersOfFour = {
            1,          // 4^0
            4,          // 4^1
            16,         // 4^2
            64,         // 4^3
            256,        // 4^4
            1024,       // 4^5
            4096,       // 4^6
            16384,      // 4^7
            65536,      // 4^8
            262144,     // 4^9
            1048576,    // 4^10
            4194304,    // 4^11
            16777216,   // 4^12
            67108864,   // 4^13
            268435456,  // 4^14
            1073741824  // 4^15
        };
        
        for (int power : powersOfFour) {
            if (n == power) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 方法6：递归方法
     * 时间复杂度：O(log₄n)
     * 空间复杂度：O(log₄n) - 递归栈深度
     */
    bool isPowerOfFour6(int n) {
        if (n <= 0) {
            return false;
        }
        if (n == 1) {
            return true;
        }
        if (n % 4 != 0) {
            return false;
        }
        return isPowerOfFour6(n / 4);
    }
    
    /**
     * 方法7：位运算+数学验证
     * 结合位运算和数学验证
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfFour7(int n) {
        // 检查n是否为正数且是2的幂
        if (n <= 0 || (n & (n - 1)) != 0) {
            return false;
        }
        
        // 4的幂除以3余数为1
        // 2的幂但不是4的幂除以3余数为2
        return n % 3 == 1;
    }
    
    /**
     * 方法8：位计数法
     * 统计1后面的0的个数，检查是否为偶数
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfFour8(int n) {
        if (n <= 0) {
            return false;
        }
        
        // 检查是否是2的幂
        if ((n & (n - 1)) != 0) {
            return false;
        }
        
        // 统计末尾0的个数（从最低位开始）
        int count = 0;
        int temp = n;
        while (temp > 1) {
            temp >>= 1;
            count++;
        }
        
        // 4的幂要求0的个数为偶数
        return count % 2 == 0;
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：4的幂
    int n1 = 16;
    bool result1 = solution.isPowerOfFour2(n1);
    cout << "测试用例1 - 输入: " << n1 << endl;
    cout << "结果: " << (result1 ? "true" : "false") << " (预期: true)" << endl;
    
    // 测试用例2：不是4的幂
    int n2 = 5;
    bool result2 = solution.isPowerOfFour2(n2);
    cout << "测试用例2 - 输入: " << n2 << endl;
    cout << "结果: " << (result2 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例3：1（4^0）
    int n3 = 1;
    bool result3 = solution.isPowerOfFour2(n3);
    cout << "测试用例3 - 输入: " << n3 << endl;
    cout << "结果: " << (result3 ? "true" : "false") << " (预期: true)" << endl;
    
    // 测试用例4：2的幂但不是4的幂
    int n4 = 2;
    bool result4 = solution.isPowerOfFour2(n4);
    cout << "测试用例4 - 输入: " << n4 << endl;
    cout << "结果: " << (result4 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例5：边界情况（0）
    int n5 = 0;
    bool result5 = solution.isPowerOfFour2(n5);
    cout << "测试用例5 - 输入: " << n5 << endl;
    cout << "结果: " << (result5 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例6：负数
    int n6 = -16;
    bool result6 = solution.isPowerOfFour2(n6);
    cout << "测试用例6 - 输入: " << n6 << endl;
    cout << "结果: " << (result6 ? "true" : "false") << " (预期: false)" << endl;
    
    // 所有方法结果对比
    cout << "\n=== 所有方法结果对比 ===" << endl;
    int testNum = 64;  // 4^3
    cout << "测试数字: " << testNum << " (二进制: " << bitset<32>(testNum) << ")" << endl;
    cout << "方法1 (数学方法): " << (solution.isPowerOfFour1(testNum) ? "true" : "false") << endl;
    cout << "方法2 (位运算法): " << (solution.isPowerOfFour2(testNum) ? "true" : "false") << endl;
    cout << "方法3 (对数方法): " << (solution.isPowerOfFour3(testNum) ? "true" : "false") << endl;
    cout << "方法4 (位运算优化): " << (solution.isPowerOfFour4(testNum) ? "true" : "false") << endl;
    cout << "方法5 (查表法): " << (solution.isPowerOfFour5(testNum) ? "true" : "false") << endl;
    cout << "方法6 (递归方法): " << (solution.isPowerOfFour6(testNum) ? "true" : "false") << endl;
    cout << "方法7 (位运算+数学): " << (solution.isPowerOfFour7(testNum) ? "true" : "false") << endl;
    cout << "方法8 (位计数法): " << (solution.isPowerOfFour8(testNum) ? "true" : "false") << endl;
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "方法1 - 数学方法:" << endl;
    cout << "  时间复杂度: O(log₄n)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法2 - 位运算法:" << endl;
    cout << "  时间复杂度: O(1)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法3 - 对数方法:" << endl;
    cout << "  时间复杂度: O(1)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法4 - 位运算优化:" << endl;
    cout << "  时间复杂度: O(1)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法5 - 查表法:" << endl;
    cout << "  时间复杂度: O(1)" << endl;
    cout << "  空间复杂度: O(16) = O(1)" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 算法选择：方法2 (位运算法) 最优" << endl;
    cout << "2. 性能优化：避免循环和函数调用" << endl;
    cout << "3. 边界处理：处理0和负数" << endl;
    cout << "4. 可读性：清晰的位运算逻辑" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. 4的幂特性：是2的幂且1在奇数位" << endl;
    cout << "2. 位运算技巧：n & (n-1) 判断2的幂" << endl;
    cout << "3. 掩码应用：0x55555555 检查奇数位" << endl;
    cout << "4. 数学特性：4的幂除以3余数为1" << endl;
    
    // 二进制特性分析
    cout << "\n=== 二进制特性分析 ===" << endl;
    cout << "4的幂的二进制表示特点：" << endl;
    cout << "  4^0 = 1: 二进制 1" << endl;
    cout << "  4^1 = 4: 二进制 100" << endl;
    cout << "  4^2 = 16: 二进制 10000" << endl;
    cout << "  4^3 = 64: 二进制 1000000" << endl;
    cout << "规律：1后面跟着偶数个0" << endl;
    
    return 0;
}
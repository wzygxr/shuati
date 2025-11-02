/**
 * 2的幂
 * 测试链接：https://leetcode.cn/problems/power-of-two/
 * 
 * 题目描述：
 * 给你一个整数 n，请你判断该整数是否是 2 的幂次方。如果是，返回 true ；否则，返回 false 。
 * 如果存在一个整数 x 使得 n == 2^x ，则认为 n 是 2 的幂次方。
 * 
 * 解题思路：
 * 1. 循环除法：不断除以2直到结果为1
 * 2. 位运算技巧：利用 n & (n-1) == 0 的性质
 * 3. 数学方法：利用对数运算
 * 4. 查表法：预计算所有2的幂
 * 
 * 时间复杂度：O(1) - 最多32次操作
 * 空间复杂度：O(1) - 只使用常数个变量
 */
#include <iostream>
#include <cmath>
#include <vector>
using namespace std;

class Code29_PowerOfTwo {
public:
    /**
     * 方法1：循环除法
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    bool isPowerOfTwo1(int n) {
        if (n <= 0) {
            return false;
        }
        
        while (n % 2 == 0) {
            n = n / 2;
        }
        
        return n == 1;
    }
    
    /**
     * 方法2：位运算技巧（最优解）
     * 利用性质：2的幂的二进制表示中只有一个1
     * n & (n-1) 可以消除最低位的1，如果结果为0则是2的幂
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfTwo2(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * 方法3：数学方法
     * 利用对数运算：log2(n) 应该是整数
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfTwo3(int n) {
        if (n <= 0) {
            return false;
        }
        
        double logResult = log2(n);
        // 检查是否为整数（考虑浮点数精度）
        return abs(logResult - round(logResult)) < 1e-10;
    }
    
    /**
     * 方法4：查表法（适合多次调用）
     * 预计算所有32位有符号整数范围内的2的幂
     * 时间复杂度：O(1)
     * 空间复杂度：O(32)
     */
    bool isPowerOfTwo4(int n) {
        if (n <= 0) {
            return false;
        }
        
        // 预计算所有2的幂（32位有符号整数范围内）
        static vector<int> power_of_two_table;
        static bool initialized = false;
        
        if (!initialized) {
            for (int i = 0; i < 31; i++) {  // 2^30是最大正数2的幂
                power_of_two_table.push_back(1 << i);
            }
            initialized = true;
        }
        
        // 在预计算表中查找
        for (int power : power_of_two_table) {
            if (n == power) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 方法5：利用最大2的幂的约数性质
     * 在32位有符号整数范围内，最大的2的幂是 2^30 = 1073741824
     * 如果n是2的幂，那么最大2的幂应该能被n整除
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfTwo5(int n) {
        return n > 0 && (1073741824 % n == 0);
    }
    
    /**
     * 方法6：利用bitset
     * 检查1的个数是否为1
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    bool isPowerOfTwo6(int n) {
        if (n <= 0) return false;
        bitset<32> bits(n);
        return bits.count() == 1;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        Code29_PowerOfTwo solution;
        
        // 测试用例1：正常情况（是2的幂）
        int n1 = 16;
        bool result1 = solution.isPowerOfTwo1(n1);
        bool result2 = solution.isPowerOfTwo2(n1);
        bool result3 = solution.isPowerOfTwo3(n1);
        bool result4 = solution.isPowerOfTwo4(n1);
        bool result5 = solution.isPowerOfTwo5(n1);
        bool result6 = solution.isPowerOfTwo6(n1);
        cout << "测试用例1 - 输入: " << n1 << " (是2的幂)" << endl;
        cout << "方法1结果: " << result1 << " (预期: true)" << endl;
        cout << "方法2结果: " << result2 << " (预期: true)" << endl;
        cout << "方法3结果: " << result3 << " (预期: true)" << endl;
        cout << "方法4结果: " << result4 << " (预期: true)" << endl;
        cout << "方法5结果: " << result5 << " (预期: true)" << endl;
        cout << "方法6结果: " << result6 << " (预期: true)" << endl;
        
        // 测试用例2：正常情况（不是2的幂）
        int n2 = 18;
        bool result7 = solution.isPowerOfTwo2(n2);
        cout << "测试用例2 - 输入: " << n2 << " (不是2的幂)" << endl;
        cout << "方法2结果: " << result7 << " (预期: false)" << endl;
        
        // 测试用例3：边界情况（0）
        int n3 = 0;
        bool result8 = solution.isPowerOfTwo2(n3);
        cout << "测试用例3 - 输入: " << n3 << endl;
        cout << "方法2结果: " << result8 << " (预期: false)" << endl;
        
        // 测试用例4：边界情况（负数）
        int n4 = -8;
        bool result9 = solution.isPowerOfTwo2(n4);
        cout << "测试用例4 - 输入: " << n4 << endl;
        cout << "方法2结果: " << result9 << " (预期: false)" << endl;
        
        // 测试用例5：边界情况（1）
        int n5 = 1;
        bool result10 = solution.isPowerOfTwo2(n5);
        cout << "测试用例5 - 输入: " << n5 << endl;
        cout << "方法2结果: " << result10 << " (预期: true)" << endl;
        
        // 复杂度分析
        cout << "\n=== 复杂度分析 ===" << endl;
        cout << "方法1 - 循环除法:" << endl;
        cout << "  时间复杂度: O(log n)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法2 - 位运算技巧:" << endl;
        cout << "  时间复杂度: O(1)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法3 - 数学方法:" << endl;
        cout << "  时间复杂度: O(1)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法4 - 查表法:" << endl;
        cout << "  时间复杂度: O(1)" << endl;
        cout << "  空间复杂度: O(32)" << endl;
        
        cout << "方法5 - 约数性质:" << endl;
        cout << "  时间复杂度: O(1)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法6 - bitset方法:" << endl;
        cout << "  时间复杂度: O(1)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        // 工程化考量
        cout << "\n=== 工程化考量 ===" << endl;
        cout << "1. 方法选择：" << endl;
        cout << "   - 实际工程：方法2（位运算）最优" << endl;
        cout << "   - 面试场景：方法2（位运算）最优" << endl;
        cout << "   - 多次调用：方法4（查表法）最优" << endl;
        cout << "2. 边界处理：必须检查n>0" << endl;
        cout << "3. 性能优化：位运算最快，只需一次操作" << endl;
        cout << "4. 可读性：方法2代码简洁易懂" << endl;
        
        // C++特性考量
        cout << "\n=== C++特性考量 ===" << endl;
        cout << "1. 类型安全：使用int类型" << endl;
        cout << "2. 标准库：使用bitset进行位操作" << endl;
        cout << "3. 数学函数：使用log2和abs" << endl;
        cout << "4. 静态变量：使用静态表避免重复计算" << endl;
        
        // 算法技巧总结
        cout << "\n=== 算法技巧总结 ===" << endl;
        cout << "1. n & (n-1) 技巧：判断是否只有一个1" << endl;
        cout << "2. 位运算性质：2的幂的二进制特性" << endl;
        cout << "3. 查表优化：预计算所有可能值" << endl;
        cout << "4. 数学性质：利用对数运算" << endl;
        cout << "5. 边界情况：0和负数都不是2的幂" << endl;
    }
};

int main() {
    Code29_PowerOfTwo::test();
    return 0;
}
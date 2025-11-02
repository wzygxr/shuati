/**
 * 位1的个数
 * 测试链接：https://leetcode.cn/problems/number-of-1-bits/
 * 
 * 题目描述：
 * 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
 * 
 * 解题思路：
 * 1. 逐位检查：检查每一位是否为1
 * 2. 快速方法：使用 n & (n-1) 技巧快速消除最低位的1
 * 3. 查表法：使用预计算的表来快速计算
 * 4. 分治法：使用分治思想并行计算
 * 
 * 时间复杂度：O(1) - 最多32次操作
 * 空间复杂度：O(1) - 只使用常数个变量
 */
#include <iostream>
#include <cstdint>
#include <vector>
#include <bitset>
using namespace std;

class Code28_NumberOf1Bits {
public:
    /**
     * 方法1：逐位检查
     * 时间复杂度：O(k) - k是1的个数，最坏情况O(32)
     * 空间复杂度：O(1)
     */
    int hammingWeight1(uint32_t n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            // 检查最低位是否为1
            if ((n & 1) == 1) {
                count++;
            }
            // 右移处理下一位
            n = n >> 1;
        }
        return count;
    }
    
    /**
     * 方法2：快速方法（最优解）
     * 使用 n & (n-1) 技巧快速消除最低位的1
     * 时间复杂度：O(k) - k是1的个数
     * 空间复杂度：O(1)
     */
    int hammingWeight2(uint32_t n) {
        int count = 0;
        while (n != 0) {
            // 每次操作消除最低位的1
            n = n & (n - 1);
            count++;
        }
        return count;
    }
    
    /**
     * 方法3：查表法（适合多次调用）
     * 时间复杂度：O(1) - 固定4次查表操作
     * 空间复杂度：O(256) - 预计算表
     */
    int hammingWeight3(uint32_t n) {
        static vector<int> bit_count_table(256, 0);
        static bool initialized = false;
        
        if (!initialized) {
            // 预计算0-255的1的个数
            for (int i = 0; i < 256; i++) {
                bit_count_table[i] = __builtin_popcount(i);
            }
            initialized = true;
        }
        
        // 将32位分成4个8位字节
        return bit_count_table[n & 0xff] +
               bit_count_table[(n >> 8) & 0xff] +
               bit_count_table[(n >> 16) & 0xff] +
               bit_count_table[(n >> 24) & 0xff];
    }
    
    /**
     * 方法4：分治法（并行计算）
     * 时间复杂度：O(log32) = O(1)
     * 空间复杂度：O(1)
     */
    int hammingWeight4(uint32_t n) {
        // 分治思想：先计算每2位的1的个数，再计算每4位，依此类推
        n = (n & 0x55555555) + ((n >> 1) & 0x55555555);  // 每2位
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333);  // 每4位
        n = (n & 0x0F0F0F0F) + ((n >> 4) & 0x0F0F0F0F);  // 每8位
        n = (n & 0x00FF00FF) + ((n >> 8) & 0x00FF00FF);  // 每16位
        n = (n & 0x0000FFFF) + ((n >> 16) & 0x0000FFFF); // 每32位
        return n;
    }
    
    /**
     * 方法5：C++内置方法（GCC/Clang）
     * 时间复杂度：O(1) - 使用硬件指令
     * 空间复杂度：O(1)
     */
    int hammingWeight5(uint32_t n) {
        return __builtin_popcount(n);
    }
    
    /**
     * 方法6：标准库方法（C++20）
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int hammingWeight6(uint32_t n) {
        return popcount(n);  // C++20标准库函数
    }
    
    /**
     * 测试方法
     */
    static void test() {
        Code28_NumberOf1Bits solution;
        
        // 测试用例1：正常情况
        uint32_t n1 = 11;  // 二进制: 1011
        int result1 = solution.hammingWeight1(n1);
        int result2 = solution.hammingWeight2(n1);
        int result3 = solution.hammingWeight3(n1);
        int result4 = solution.hammingWeight4(n1);
        int result5 = solution.hammingWeight5(n1);
        cout << "测试用例1 - 输入: " << n1 << " (二进制: 1011)" << endl;
        cout << "方法1结果: " << result1 << " (预期: 3)" << endl;
        cout << "方法2结果: " << result2 << " (预期: 3)" << endl;
        cout << "方法3结果: " << result3 << " (预期: 3)" << endl;
        cout << "方法4结果: " << result4 << " (预期: 3)" << endl;
        cout << "方法5结果: " << result5 << " (预期: 3)" << endl;
        
        // 测试用例2：边界情况（全0）
        uint32_t n2 = 0;
        int result6 = solution.hammingWeight1(n2);
        cout << "测试用例2 - 输入: " << n2 << endl;
        cout << "方法1结果: " << result6 << " (预期: 0)" << endl;
        
        // 测试用例3：边界情况（全1）
        uint32_t n3 = 0xFFFFFFFF;  // 二进制全1
        int result7 = solution.hammingWeight1(n3);
        cout << "测试用例3 - 输入: " << n3 << endl;
        cout << "方法1结果: " << result7 << " (预期: 32)" << endl;
        
        // 性能对比测试
        cout << "\n=== 性能分析 ===" << endl;
        uint32_t testValue = 0x12345678;  // 测试值
        
        // 复杂度分析
        cout << "\n=== 复杂度分析 ===" << endl;
        cout << "方法1 - 逐位检查:" << endl;
        cout << "  时间复杂度: O(32) - 固定32次操作" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法2 - 快速方法:" << endl;
        cout << "  时间复杂度: O(k) - k是1的个数" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法3 - 查表法:" << endl;
        cout << "  时间复杂度: O(1) - 固定4次查表操作" << endl;
        cout << "  空间复杂度: O(256)" << endl;
        
        cout << "方法4 - 分治法:" << endl;
        cout << "  时间复杂度: O(log32) = O(1)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法5 - 内置方法:" << endl;
        cout << "  时间复杂度: O(1) - 硬件指令" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        // 工程化考量
        cout << "\n=== 工程化考量 ===" << endl;
        cout << "1. 方法选择：" << endl;
        cout << "   - 实际工程：方法5/6（内置方法）最优" << endl;
        cout << "   - 面试场景：方法2（快速方法）最优" << endl;
        cout << "   - 多次调用：方法3（查表法）最优" << endl;
        cout << "2. 边界处理：使用uint32_t确保无符号操作" << endl;
        cout << "3. 性能优化：根据1的个数选择最优方法" << endl;
        cout << "4. 可读性：添加详细注释说明算法原理" << endl;
        
        // C++特性考量
        cout << "\n=== C++特性考量 ===" << endl;
        cout << "1. 类型安全：使用uint32_t确保32位无符号整数" << endl;
        cout << "2. 编译器内置：GCC/Clang提供__builtin_popcount" << endl;
        cout << "3. 标准库：C++20提供std::popcount" << endl;
        cout << "4. 静态表：使用静态变量避免重复计算" << endl;
        
        // 算法技巧总结
        cout << "\n=== 算法技巧总结 ===" << endl;
        cout << "1. n & (n-1) 技巧：快速消除最低位的1" << endl;
        cout << "2. 分治思想：并行计算提高效率" << endl;
        cout << "3. 查表优化：空间换时间策略" << endl;
        cout << "4. 硬件指令：利用CPU内置功能" << endl;
        cout << "5. 编译器优化：利用编译器内置函数" << endl;
    }
};

int main() {
    Code28_NumberOf1Bits::test();
    return 0;
}
/**
 * 颠倒二进制位
 * 测试链接：https://leetcode.cn/problems/reverse-bits/
 * 
 * 题目描述：
 * 颠倒给定的 32 位无符号整数的二进制位。
 * 
 * 解题思路：
 * 1. 逐位反转：从最低位开始，依次将每一位移动到对应的高位位置
 * 2. 分治反转：使用分治思想，先交换16位块，再交换8位块，依此类推
 * 3. 查表法：对于8位进行预计算，然后组合成32位
 * 
 * 时间复杂度：O(1) - 固定32次操作
 * 空间复杂度：O(1) - 只使用常数个变量
 */
#include <iostream>
#include <vector>
#include <cstdint>
using namespace std;

class Code27_ReverseBits {
public:
    /**
     * 方法1：逐位反转
     * 时间复杂度：O(1) - 固定32次循环
     * 空间复杂度：O(1)
     */
    uint32_t reverseBits1(uint32_t n) {
        uint32_t result = 0;
        for (int i = 0; i < 32; i++) {
            // 取最低位
            uint32_t bit = n & 1;
            // 将最低位移动到对应的高位位置
            result = (result << 1) | bit;
            // 右移处理下一位
            n = n >> 1;
        }
        return result;
    }
    
    /**
     * 方法2：分治反转（更高效）
     * 时间复杂度：O(1) - 固定5次操作
     * 空间复杂度：O(1)
     */
    uint32_t reverseBits2(uint32_t n) {
        // 分治思想：先交换16位块，再交换8位块，依此类推
        n = (n >> 16) | (n << 16);  // 交换16位块
        n = ((n & 0xff00ff00) >> 8) | ((n & 0x00ff00ff) << 8);  // 交换8位块
        n = ((n & 0xf0f0f0f0) >> 4) | ((n & 0x0f0f0f0f) << 4);  // 交换4位块
        n = ((n & 0xcccccccc) >> 2) | ((n & 0x33333333) << 2);  // 交换2位块
        n = ((n & 0xaaaaaaaa) >> 1) | ((n & 0x55555555) << 1);  // 交换1位块
        return n;
    }
    
    /**
     * 方法3：查表法（最优解，适合多次调用）
     * 时间复杂度：O(1) - 固定4次查表操作
     * 空间复杂度：O(256) - 预计算表
     */
    static uint32_t reverseByte(uint8_t b) {
        // 反转8位字节
        uint32_t result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 1) | (b & 1);
            b >>= 1;
        }
        return result;
    }
    
    uint32_t reverseBits3(uint32_t n) {
        static uint32_t table[256];
        static bool initialized = false;
        
        if (!initialized) {
            // 预计算0-255的8位反转结果
            for (int i = 0; i < 256; i++) {
                table[i] = reverseByte(static_cast<uint8_t>(i));
            }
            initialized = true;
        }
        
        // 将32位分成4个8位字节，分别反转后重新组合
        return (table[n & 0xff] << 24) |          // 最低8位移到最高8位
               (table[(n >> 8) & 0xff] << 16) |   // 次低8位移到次高8位
               (table[(n >> 16) & 0xff] << 8) |   // 次高8位移到次低8位
               (table[(n >> 24) & 0xff]);         // 最高8位移到最低8位
    }
    
    /**
     * 测试方法
     */
    static void test() {
        Code27_ReverseBits solution;
        
        // 测试用例1：正常情况
        uint32_t n1 = 43261596;  // 二进制: 00000010100101000001111010011100
        uint32_t result1 = solution.reverseBits1(n1);
        uint32_t result2 = solution.reverseBits2(n1);
        uint32_t result3 = solution.reverseBits3(n1);
        cout << "测试用例1 - 输入: " << n1 << endl;
        cout << "方法1结果: " << result1 << " (预期: 964176192)" << endl;
        cout << "方法2结果: " << result2 << " (预期: 964176192)" << endl;
        cout << "方法3结果: " << result3 << " (预期: 964176192)" << endl;
        
        // 测试用例2：边界情况（全0）
        uint32_t n2 = 0;
        uint32_t result4 = solution.reverseBits1(n2);
        cout << "测试用例2 - 输入: " << n2 << endl;
        cout << "方法1结果: " << result4 << " (预期: 0)" << endl;
        
        // 测试用例3：边界情况（全1）
        uint32_t n3 = 0xFFFFFFFF;  // 二进制全1
        uint32_t result5 = solution.reverseBits1(n3);
        cout << "测试用例3 - 输入: " << n3 << endl;
        cout << "方法1结果: " << result5 << " (预期: 0xFFFFFFFF)" << endl;
        
        // 复杂度分析
        cout << "\n=== 复杂度分析 ===" << endl;
        cout << "方法1 - 逐位反转:" << endl;
        cout << "  时间复杂度: O(1) - 固定32次操作" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法2 - 分治反转:" << endl;
        cout << "  时间复杂度: O(1) - 固定5次位操作" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法3 - 查表法:" << endl;
        cout << "  时间复杂度: O(1) - 固定4次查表操作" << endl;
        cout << "  空间复杂度: O(256) - 预计算表" << endl;
        
        // 工程化考量
        cout << "\n=== 工程化考量 ===" << endl;
        cout << "1. 方法选择：" << endl;
        cout << "   - 单次调用：方法2（分治）最优" << endl;
        cout << "   - 多次调用：方法3（查表）最优" << endl;
        cout << "2. 边界处理：使用uint32_t确保无符号操作" << endl;
        cout << "3. 性能优化：避免不必要的循环" << endl;
        cout << "4. 可读性：添加详细注释说明位操作原理" << endl;
        
        // C++特性考量
        cout << "\n=== C++特性考量 ===" << endl;
        cout << "1. 类型安全：使用uint32_t和uint8_t确保类型正确" << endl;
        cout << "2. 静态表：使用静态变量避免重复计算" << endl;
        cout << "3. 初始化标志：确保表只初始化一次" << endl;
        cout << "4. 位操作：C++位操作与硬件指令紧密相关" << endl;
        
        // 算法技巧总结
        cout << "\n=== 算法技巧总结 ===" << endl;
        cout << "1. 位掩码技巧：使用十六进制常量作为位掩码" << endl;
        cout << "2. 分治思想：将大问题分解为小问题解决" << endl;
        cout << "3. 查表优化：空间换时间，适合重复计算" << endl;
        cout << "4. 无符号类型：避免符号扩展问题" << endl;
    }
};

int main() {
    Code27_ReverseBits::test();
    return 0;
}
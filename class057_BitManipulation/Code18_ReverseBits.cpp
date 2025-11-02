#include <iostream>
using namespace std;

// 反转比特位
// 测试链接 : https://leetcode.cn/problems/reverse-bits/
/*
题目描述：
颠倒给定的 32 位无符号整数的二进制位。

示例：
输入：n = 00000010100101000001111010011100
输出：964176192 (00111001011110000010100101000000)
解释：输入的二进制串 00000010100101000001111010011100 表示无符号整数 43261596，
     因此返回 964176192，其二进制表示形式为 00111001011110000010100101000000。

输入：n = 11111111111111111111111111111101
输出：3221225471 (10111111111111111111111111111111)
解释：输入的二进制串 11111111111111111111111111111101 表示无符号整数 4294967293，
     因此返回 3221225471，其二进制表示形式为 10111111111111111111111111111111。

提示：
在C++中，我们可以直接使用unsigned int类型来处理无符号整数。

解题思路：
方法1：逐位反转
1. 初始化结果res = 0
2. 对于每一位（0到31），执行以下操作：
   a. 将res左移1位，为新位腾出位置
   b. 获取n的最低位并加到res中
   c. 将n右移1位，处理下一位

方法2：位运算分治
可以使用位运算分治法，通过多次交换相邻的1位、2位、4位、8位和16位来实现反转。
例如：
- 首先交换每两个相邻位
- 然后交换每两个相邻的2位组
- 接着交换每两个相邻的4位组
- 然后交换每两个相邻的8位组
- 最后交换高16位和低16位

时间复杂度：O(1) - 因为我们只处理固定的32位
空间复杂度：O(1) - 只使用了常数级别的额外空间
*/

class Solution {
public:
    /**
     * 反转32位无符号整数的二进制位
     * 使用逐位反转方法
     * @param n 输入的32位无符号整数
     * @return 反转后的32位无符号整数
     */
    uint32_t reverseBits(uint32_t n) {
        uint32_t res = 0;
        // 处理每一位，从最低位到最高位
        for (int i = 0; i < 32; i++) {
            // 将结果左移一位，为新位腾出位置
            res <<= 1;
            // 获取n的最低位并加到结果中
            res |= (n & 1);
            // 将n右移一位，处理下一位
            n >>= 1;
        }
        return res;
    }
    
    /**
     * 反转32位无符号整数的二进制位
     * 使用位运算分治方法（更高效）
     * @param n 输入的32位无符号整数
     * @return 反转后的32位无符号整数
     */
    uint32_t reverseBits2(uint32_t n) {
        // 分治反转：交换相邻的位组
        // 交换每两位
        n = ((n >> 1) & 0x55555555) | ((n & 0x55555555) << 1);
        // 交换每四位中的两位组
        n = ((n >> 2) & 0x33333333) | ((n & 0x33333333) << 2);
        // 交换每八位中的四位组
        n = ((n >> 4) & 0x0F0F0F0F) | ((n & 0x0F0F0F0F) << 4);
        // 交换每16位中的八位组
        n = ((n >> 8) & 0x00FF00FF) | ((n & 0x00FF00FF) << 8);
        // 交换高16位和低16位
        n = (n >> 16) | (n << 16);
        
        return n;
    }
    
    /**
     * 反转32位无符号整数的二进制位
     * 使用查表法（适用于需要频繁调用的场景）
     * 预计算每个字节（8位）的反转结果
     * @param n 输入的32位无符号整数
     * @return 反转后的32位无符号整数
     */
    uint32_t reverseBits3(uint32_t n) {
        // 预计算的查找表，存储0-255每个数的8位反转结果
        // 为了简单起见，这里直接计算而不是硬编码
        uint8_t reverseByte[256];
        for (int i = 0; i < 256; i++) {
            reverseByte[i] = reverse8Bits(i);
        }
        
        // 分别反转四个字节，然后重新组合
        return (static_cast<uint32_t>(reverseByte[n & 0xFF]) << 24) |
               (static_cast<uint32_t>(reverseByte[(n >> 8) & 0xFF]) << 16) |
               (static_cast<uint32_t>(reverseByte[(n >> 16) & 0xFF]) << 8) |
               reverseByte[(n >> 24) & 0xFF];
    }
    
private:
    /**
     * 反转一个字节（8位）的位顺序
     * @param byte 输入的字节（0-255）
     * @return 反转后的字节
     */
    uint8_t reverse8Bits(uint8_t byte) {
        return ((byte * 0x0802LU & 0x22110LU) | (byte * 0x8020LU & 0x88440LU)) * 0x10101LU >> 16;
    }
};

// 辅助函数：打印二进制表示
void printBinary(uint32_t n) {
    for (int i = 31; i >= 0; i--) {
        cout << ((n >> i) & 1);
        // 每4位添加一个空格，方便阅读
        if (i % 4 == 0 && i > 0) {
            cout << " ";
        }
    }
    cout << endl;
}

// 测试代码
int main() {
    Solution solution;
    
    // 示例1: 43261596 (00000010100101000001111010011100)
    uint32_t n1 = 43261596;
    cout << "Test 1:" << endl;
    cout << "Input: " << n1 << endl;
    cout << "Binary: ";
    printBinary(n1);
    cout << "Output1: " << solution.reverseBits(n1) << endl;
    cout << "Output2: " << solution.reverseBits2(n1) << endl;
    cout << "Output3: " << solution.reverseBits3(n1) << endl;
    cout << "Expected: 964176192" << endl;
    
    // 示例2: 4294967293 (11111111111111111111111111111101)
    uint32_t n2 = 4294967293;
    cout << "\nTest 2:" << endl;
    cout << "Input: " << n2 << endl;
    cout << "Binary: ";
    printBinary(n2);
    cout << "Output1: " << solution.reverseBits(n2) << endl;
    cout << "Output2: " << solution.reverseBits2(n2) << endl;
    cout << "Output3: " << solution.reverseBits3(n2) << endl;
    cout << "Expected: 3221225471" << endl;
    
    // 额外测试
    uint32_t n3 = 0; // 全0
    uint32_t n4 = 0xFFFFFFFF; // 全1
    cout << "\nAdditional Tests:" << endl;
    cout << "Input: 0, Output: " << solution.reverseBits(n3) << endl;
    cout << "Input: 0xFFFFFFFF, Output: " << solution.reverseBits(n4) << endl;
    
    return 0;
}
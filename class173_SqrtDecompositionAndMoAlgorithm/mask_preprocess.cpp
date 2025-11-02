#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>

/**
 * 掩码预处理工具类
 * 提供常用位掩码的预处理和位运算优化功能
 * 用于算法中的位操作加速
 */

class MaskPreprocess {
private:
    // 预定义的最大数组大小
    static const int MAX_N = 1 << 20; // 1,048,576
    
    // 预处理数组
    static std::vector<int> lowbit;         // 最低位1的值
    static std::vector<int> highestBit;     // 最高位1的位置（从0开始）
    static std::vector<int> bitCount;       // 二进制中1的个数（popcount）
    static std::vector<int> nextSetBit;     // 下一个设置位的位置
    static std::vector<int> prevSetBit;     // 上一个设置位的位置
    static std::vector<int> parity;         // 奇偶校验位（1的个数的奇偶性）
    
    // 初始化标志
    static bool initialized;
    
    /**
     * 预处理所有掩码数据
     */
    static void initialize() {
        if (initialized) return;
        
        lowbit.resize(MAX_N);
        highestBit.resize(MAX_N);
        bitCount.resize(MAX_N);
        nextSetBit.resize(MAX_N, -1);
        prevSetBit.resize(MAX_N, -1);
        parity.resize(MAX_N);
        
        // 初始化最低位1
        for (int i = 1; i < MAX_N; i++) {
            lowbit[i] = i & (-i);
        }
        
        // 初始化最高位1的位置
        highestBit[0] = -1; // 特殊情况
        for (int i = 1; i < MAX_N; i++) {
            highestBit[i] = highestBit[i >> 1] + 1;
        }
        
        // 初始化bitCount（使用动态规划）
        bitCount[0] = 0;
        for (int i = 1; i < MAX_N; i++) {
            bitCount[i] = bitCount[i & (i - 1)] + 1;
        }
        
        // 初始化nextSetBit
        for (int i = 1; i < MAX_N; i++) {
            int temp = i;
            int lsb = temp & -temp;
            temp ^= lsb; // 清除最低位1
            if (temp != 0) {
                // 计算下一个最低位1的位置
                int next_lsb = temp & -temp;
                nextSetBit[i] = 0;
                while (next_lsb > 1) {
                    next_lsb >>= 1;
                    nextSetBit[i]++;
                }
            }
        }
        
        // 初始化prevSetBit
        for (int i = 1; i < MAX_N; i++) {
            int temp = i;
            int highest = 0;
            int temp_copy = temp;
            while (temp_copy >>= 1) {
                highest++;
            }
            temp ^= (1 << highest); // 清除最高位1
            if (temp != 0) {
                prevSetBit[i] = 0;
                temp_copy = temp;
                while (temp_copy >>= 1) {
                    prevSetBit[i]++;
                }
            }
        }
        
        // 初始化奇偶校验
        parity[0] = 0;
        for (int i = 1; i < MAX_N; i++) {
            parity[i] = parity[i >> 1] ^ (i & 1);
        }
        
        initialized = true;
    }
    
public:
    // 常用掩码
    static const int ALL_ONES_8 = 0xFF;       // 8位全1掩码
    static const int ALL_ONES_16 = 0xFFFF;    // 16位全1掩码
    static const int ALL_ONES_32 = 0xFFFFFFFF; // 32位全1掩码
    
    static const int ALTERNATE_ODD = 0x55555555;  // 01010101...
    static const int ALTERNATE_EVEN = 0xAAAAAAAA; // 10101010...
    
    static const int ALL_ONES_LAST_4 = 0x0000000F; // 最后4位全1
    static const int ALL_ONES_FIRST_4 = 0xF0000000; // 最前4位全1
    
    /**
     * 构造函数，确保初始化
     */
    MaskPreprocess() {
        initialize();
    }
    
    /**
     * 获取最低位1的值
     * 等同于 x & (-x)
     * 
     * @param x 输入整数
     * @return 最低位1的值
     */
    static int getLowbit(int x) {
        initialize();
        if (x == 0) return 0;
        if (x >= 0 && x < MAX_N) return lowbit[x];
        return x & (-x); // 对于超出预计算范围的数
    }
    
    /**
     * 获取最高位1的位置（从0开始）
     * 
     * @param x 输入整数
     * @return 最高位1的位置
     */
    static int getHighestBitPosition(int x) {
        initialize();
        if (x == 0) return -1;
        if (x >= 0 && x < MAX_N) return highestBit[x];
        // 对于超出预计算范围的数
        int pos = -1;
        while (x > 0) {
            x >>= 1;
            pos++;
        }
        return pos;
    }
    
    /**
     * 获取二进制中1的个数
     * 
     * @param x 输入整数
     * @return 1的个数
     */
    static int getBitCount(int x) {
        initialize();
        if (x == 0) return 0;
        if (x >= 0 && x < MAX_N) return bitCount[x];
        
        // 对于超出预计算范围的数，使用Kernighan算法
        int count = 0;
        while (x != 0) {
            x &= x - 1; // 清除最低位的1
            count++;
        }
        return count;
    }
    
    /**
     * 获取下一个设置位的位置
     * 
     * @param x 输入整数
     * @return 下一个设置位的位置，如果没有返回-1
     */
    static int getNextSetBit(int x) {
        initialize();
        if (x >= 0 && x < MAX_N) return nextSetBit[x];
        
        // 动态计算
        int temp = x;
        int lsb = temp & -temp;
        temp ^= lsb;
        if (temp == 0) return -1;
        
        int next_lsb = temp & -temp;
        int pos = 0;
        while (next_lsb > 1) {
            next_lsb >>= 1;
            pos++;
        }
        return pos;
    }
    
    /**
     * 获取上一个设置位的位置
     * 
     * @param x 输入整数
     * @return 上一个设置位的位置，如果没有返回-1
     */
    static int getPrevSetBit(int x) {
        initialize();
        if (x >= 0 && x < MAX_N) return prevSetBit[x];
        
        // 动态计算
        int temp = x;
        int highest = 0;
        int temp_copy = temp;
        while (temp_copy >>= 1) {
            highest++;
        }
        temp ^= (1 << highest);
        if (temp == 0) return -1;
        
        int pos = 0;
        temp_copy = temp;
        while (temp_copy >>= 1) {
            pos++;
        }
        return pos;
    }
    
    /**
     * 获取奇偶校验位（1的个数的奇偶性）
     * 
     * @param x 输入整数
     * @return 1表示奇数个1，0表示偶数个1
     */
    static int getParity(int x) {
        initialize();
        if (x >= 0 && x < MAX_N) return parity[x];
        
        // 动态计算
        x ^= x >> 16;
        x ^= x >> 8;
        x ^= x >> 4;
        x ^= x >> 2;
        x ^= x >> 1;
        return x & 1;
    }
    
    /**
     * 生成特定长度的全1掩码
     * 
     * @param length 掩码长度
     * @return 全1掩码
     */
    static int generateAllOnesMask(int length) {
        return (1 << length) - 1;
    }
    
    /**
     * 生成交替位掩码
     * 
     * @param startWithOne 是否以1开始
     * @return 交替位掩码
     */
    static int generateAlternatingMask(bool startWithOne = true) {
        return startWithOne ? ALTERNATE_ODD : ALTERNATE_EVEN;
    }
    
    /**
     * 检查数x是否是2的幂
     * 
     * @param x 输入整数
     * @return 是否是2的幂
     */
    static bool isPowerOfTwo(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }
    
    /**
     * 对齐到下一个2的幂
     * 
     * @param x 输入整数
     * @return 下一个大于等于x的2的幂
     */
    static int nextPowerOfTwo(int x) {
        if (x <= 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }
    
    /**
     * 将整数转换为二进制字符串
     * 
     * @param num 整数
     * @param bits 位数
     * @return 二进制字符串
     */
    static std::string toBinaryString(int num, int bits = 32) {
        std::string result;
        if (num < 0) {
            // 对于负数，展示其补码表示
            num = (1LL << bits) + num;
        }
        
        for (int i = bits - 1; i >= 0; i--) {
            result += ((num >> i) & 1) ? '1' : '0';
        }
        
        return result;
    }
};

// 静态成员初始化
bool MaskPreprocess::initialized = false;
std::vector<int> MaskPreprocess::lowbit;
std::vector<int> MaskPreprocess::highestBit;
std::vector<int> MaskPreprocess::bitCount;
std::vector<int> MaskPreprocess::nextSetBit;
std::vector<int> MaskPreprocess::prevSetBit;
std::vector<int> MaskPreprocess::parity;

/**
 * 主函数，用于测试
 */
int main() {
    int testNum = 0b10110100; // 180
    std::cout << "测试数字: " << testNum << " (二进制: " << 
              MaskPreprocess::toBinaryString(testNum, 8) << ")" << std::endl;
    
    std::cout << "最低位1的值: " << MaskPreprocess::getLowbit(testNum) << 
              " (二进制: " << MaskPreprocess::toBinaryString(MaskPreprocess::getLowbit(testNum), 8) << ")" << std::endl;
    
    std::cout << "最高位1的位置: " << MaskPreprocess::getHighestBitPosition(testNum) << std::endl;
    
    std::cout << "二进制中1的个数: " << MaskPreprocess::getBitCount(testNum) << std::endl;
    
    std::cout << "下一个设置位的位置: " << MaskPreprocess::getNextSetBit(testNum) << std::endl;
    
    std::cout << "上一个设置位的位置: " << MaskPreprocess::getPrevSetBit(testNum) << std::endl;
    
    std::cout << "奇偶校验位: " << MaskPreprocess::getParity(testNum) << std::endl;
    
    // 测试掩码生成
    std::cout << "\n掩码生成测试:" << std::endl;
    std::cout << "8位全1掩码: " << MaskPreprocess::generateAllOnesMask(8) << 
              " (二进制: " << MaskPreprocess::toBinaryString(MaskPreprocess::generateAllOnesMask(8), 8) << ")" << std::endl;
    
    std::cout << "交替位掩码(以1开始): " << 
              MaskPreprocess::toBinaryString(MaskPreprocess::generateAlternatingMask(true), 8) << std::endl;
    
    std::cout << "交替位掩码(以0开始): " << 
              MaskPreprocess::toBinaryString(MaskPreprocess::generateAlternatingMask(false), 8) << std::endl;
    
    // 测试工具方法
    std::cout << "\n工具方法测试:" << std::endl;
    std::cout << "64是否是2的幂: " << (MaskPreprocess::isPowerOfTwo(64) ? "true" : "false") << std::endl;
    std::cout << "100是否是2的幂: " << (MaskPreprocess::isPowerOfTwo(100) ? "true" : "false") << std::endl;
    
    std::cout << "100的下一个2的幂: " << MaskPreprocess::nextPowerOfTwo(100) << std::endl;
    std::cout << "128的下一个2的幂: " << MaskPreprocess::nextPowerOfTwo(128) << std::endl;
    
    return 0;
}
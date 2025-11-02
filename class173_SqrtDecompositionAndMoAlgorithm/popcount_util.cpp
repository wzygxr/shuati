#include <iostream>
#include <vector>
#include <chrono>
#include <string>
#include <algorithm>

/**
 * Popcount（汉明重量）工具类
 * 提供多种高效计算二进制中1的个数的方法
 * 用于算法优化和位运算加速
 */

/**
 * 使用内置函数计算popcount
 * GCC和Clang提供__builtin_popcount函数
 * 
 * @param x 待计算的整数
 * @return x的二进制表示中1的个数
 */
int popcountBuiltin(int x) {
    #ifdef __GNUC__
        return __builtin_popcount(x);
    #else
        // 备用实现
        int count = 0;
        for (; x; x &= x - 1) {
            count++;
        }
        return count;
    #endif
}

/**
 * 使用位运算优化实现popcount
 * 分治法计算，适用于任何平台
 * 
 * @param x 待计算的整数
 * @return x的二进制表示中1的个数
 */
int popcountBitwise(int x) {
    // 每两位一组，统计每组中1的个数
    x = x - ((x >> 1) & 0x55555555);
    // 每四位一组，统计每组中1的个数
    x = (x & 0x33333333) + ((x >> 2) & 0x33333333);
    // 每八位一组，统计每组中1的个数
    x = (x + (x >> 4)) & 0x0f0f0f0f;
    // 累加所有8位组
    x = x + (x >> 8);
    x = x + (x >> 16);
    // 取低6位作为结果
    return x & 0x3f;
}

/**
 * 使用循环移位实现popcount
 * 简单直观，但效率较低
 * 
 * @param x 待计算的整数
 * @return x的二进制表示中1的个数
 */
int popcountLoop(int x) {
    int count = 0;
    while (x != 0) {
        count += x & 1;
        x >>= 1;
    }
    return count;
}

/**
 * 使用Brian Kernighan算法计算popcount
 * 每次清除最低位的1，直到所有位都为0
 * 
 * @param x 待计算的整数
 * @return x的二进制表示中1的个数
 */
int popcountKernighan(int x) {
    int count = 0;
    while (x != 0) {
        x &= x - 1; // 清除最低位的1
        count++;
    }
    return count;
}

/**
 * 查表法计算popcount（预计算0-255的popcount值）
 * 对于频繁调用的场景，查表法非常高效
 */
class PopcountTable {
private:
    static const int TABLE_SIZE = 256;
    int table[TABLE_SIZE];
    
public:
    PopcountTable() {
        // 初始化查表
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = popcountLoop(i);
        }
    }
    
    /**
     * 使用查表法计算popcount
     * 适用于32位整数
     * 
     * @param x 待计算的整数
     * @return x的二进制表示中1的个数
     */
    int popcount(int x) const {
        return table[(x >> 24) & 0xFF] +
               table[(x >> 16) & 0xFF] +
               table[(x >> 8) & 0xFF] +
               table[x & 0xFF];
    }
};

// 创建全局查表实例
static PopcountTable g_popcountTable;

/**
 * 计算两个数的汉明距离（二进制不同位的个数）
 * 
 * @param x 第一个数
 * @param y 第二个数
 * @return 汉明距离
 */
int hammingDistance(int x, int y) {
    return popcountBuiltin(x ^ y);
}

/**
 * 计算数组中所有数的popcount之和
 * 适用于需要统计多个数的位信息的场景
 * 
 * @param arr 整数数组
 * @return 所有数的popcount之和
 */
int popcountArray(const std::vector<int>& arr) {
    int total = 0;
    for (int x : arr) {
        total += popcountBuiltin(x);
    }
    return total;
}

/**
 * 性能测试方法
 * 比较不同popcount实现的性能
 */
void benchmark() {
    const int ITERATIONS = 10000000;
    std::vector<int> testNumbers = {
        0, 1, -1, INT_MAX, INT_MIN,
        0xAAAAAAAA, 0x55555555, 0x12345678
    };
    
    std::cout << "Popcount性能测试（" << ITERATIONS << "次迭代）：" << std::endl;
    
    // 测试内置方法
    auto start = std::chrono::high_resolution_clock::now();
    int sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
        sum += popcountBuiltin(testNumbers[i % testNumbers.size()]);
    }
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    std::cout << "内置方法: " << duration.count() << ".000 ms, 结果: " << sum << std::endl;
    
    // 测试位运算法
    start = std::chrono::high_resolution_clock::now();
    sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
        sum += popcountBitwise(testNumbers[i % testNumbers.size()]);
    }
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    std::cout << "位运算法: " << duration.count() << ".000 ms, 结果: " << sum << std::endl;
    
    // 测试Kernighan算法
    start = std::chrono::high_resolution_clock::now();
    sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
        sum += popcountKernighan(testNumbers[i % testNumbers.size()]);
    }
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    std::cout << "Kernighan算法: " << duration.count() << ".000 ms, 结果: " << sum << std::endl;
    
    // 测试查表法
    start = std::chrono::high_resolution_clock::now();
    sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
        sum += g_popcountTable.popcount(testNumbers[i % testNumbers.size()]);
    }
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    std::cout << "查表法: " << duration.count() << ".000 ms, 结果: " << sum << std::endl;
}

/**
 * 将整数转换为二进制字符串
 * 
 * @param num 整数
 * @return 二进制字符串
 */
std::string toBinaryString(int num) {
    if (num == 0) return "0";
    
    bool isNegative = false;
    if (num < 0) {
        isNegative = true;
        // 对于负数，我们展示其补码表示（32位）
        num = (1LL << 32) + num;
    }
    
    std::string result;
    while (num > 0) {
        result = (num % 2 ? '1' : '0') + result;
        num /= 2;
    }
    
    if (isNegative) {
        // 补足32位
        while (result.length() < 32) {
            result = '0' + result;
        }
        result = "-" + result;
    }
    
    return result;
}

int main() {
    int testNum = 0b101101;
    std::cout << "测试数字: " << testNum << " (二进制: " << 
              toBinaryString(testNum) << ")" << std::endl;
    std::cout << "内置方法popcount: " << popcountBuiltin(testNum) << std::endl;
    std::cout << "位运算法popcount: " << popcountBitwise(testNum) << std::endl;
    std::cout << "循环法popcount: " << popcountLoop(testNum) << std::endl;
    std::cout << "Kernighan算法popcount: " << popcountKernighan(testNum) << std::endl;
    std::cout << "查表法popcount: " << g_popcountTable.popcount(testNum) << std::endl;
    
    std::cout << "\n汉明距离测试:" << std::endl;
    int a = 0b1010;
    int b = 0b1100;
    std::cout << a << "(" << toBinaryString(a) << ") 和 " << 
              b << "(" << toBinaryString(b) << ") 的汉明距离: " << 
              hammingDistance(a, b) << std::endl;
    
    std::cout << "\n性能测试:" << std::endl;
    benchmark();
    
    return 0;
}
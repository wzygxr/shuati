#include <iostream>
#include <vector>
#include <bitset>
#include <cstdint>
#include <chrono>

using namespace std;

/**
 * 位1的个数（多种解法）
 * 测试链接：https://leetcode.cn/problems/number-of-1-bits/
 * 
 * 题目描述：
 * 编写一个函数，输入是一个无符号整数（以二进制串的形式），
 * 返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
 * 
 * 解题思路：
 * 1. 逐位检查法：检查每一位是否为1
 * 2. Brian Kernighan算法：n & (n-1) 消除最低位的1
 * 3. 查表法：预计算所有可能值的1的个数
 * 4. 分治法：使用位运算技巧分组统计
 * 5. 内置函数法：使用语言内置函数
 * 
 * 时间复杂度分析：
 * - 逐位检查法：O(32) = O(1)
 * - Brian Kernighan算法：O(k)，k为1的个数
 * - 查表法：O(1)
 * - 分治法：O(1)
 * - 内置函数法：O(1)
 * 
 * 空间复杂度分析：
 * - 逐位检查法：O(1)
 * - Brian Kernighan算法：O(1)
 * - 查表法：O(256) = O(1)
 * - 分治法：O(1)
 * - 内置函数法：O(1)
 */
class Solution {
public:
    /**
     * 方法1：逐位检查法
     * 时间复杂度：O(32) = O(1)
     * 空间复杂度：O(1)
     */
    int hammingWeight1(uint32_t n) {
        int count = 0;
        
        // 检查32位中的每一位
        for (int i = 0; i < 32; i++) {
            // 检查第i位是否为1
            if ((n & (1 << i)) != 0) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 方法2：Brian Kernighan算法（推荐）
     * 核心思想：n & (n-1) 可以消除n的二进制表示中最右边的1
     * 时间复杂度：O(k)，k为1的个数
     * 空间复杂度：O(1)
     */
    int hammingWeight2(uint32_t n) {
        int count = 0;
        
        while (n != 0) {
            n &= n - 1;  // 消除最低位的1
            count++;
        }
        
        return count;
    }
    
    /**
     * 方法3：查表法
     * 预计算0-255所有数字的1的个数
     * 时间复杂度：O(1)
     * 空间复杂度：O(256) = O(1)
     */
    int hammingWeight3(uint32_t n) {
        // 预计算表：0-255每个数字的1的个数
        static const int table[256] = {
            0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4,1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,4,5,5,6,5,6,6,7,5,6,6,7,6,7,7,8
        };
        
        // 将32位整数分成4个8位部分
        return table[n & 0xFF] + 
               table[(n >> 8) & 0xFF] + 
               table[(n >> 16) & 0xFF] + 
               table[(n >> 24) & 0xFF];
    }
    
    /**
     * 方法4：分治法（位运算技巧）
     * 使用分治思想，先计算每2位的1的个数，然后合并
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int hammingWeight4(uint32_t n) {
        // 第一步：每2位统计1的个数
        n = (n & 0x55555555) + ((n >> 1) & 0x55555555);
        
        // 第二步：每4位统计1的个数
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333);
        
        // 第三步：每8位统计1的个数
        n = (n & 0x0F0F0F0F) + ((n >> 4) & 0x0F0F0F0F);
        
        // 第四步：每16位统计1的个数
        n = (n & 0x00FF00FF) + ((n >> 8) & 0x00FF00FF);
        
        // 第五步：每32位统计1的个数
        n = (n & 0x0000FFFF) + ((n >> 16) & 0x0000FFFF);
        
        return n;
    }
    
    /**
     * 方法5：内置函数法
     * 使用C++内置的__builtin_popcount()
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int hammingWeight5(uint32_t n) {
        return __builtin_popcount(n);
    }
    
    /**
     * 方法6：移位统计法
     * 时间复杂度：O(32) = O(1)
     * 空间复杂度：O(1)
     */
    int hammingWeight6(uint32_t n) {
        int count = 0;
        
        while (n != 0) {
            count += n & 1;
            n >>= 1;
        }
        
        return count;
    }
    
    /**
     * 方法7：并行计算法（另一种分治）
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int hammingWeight7(uint32_t n) {
        // 并行计算1的个数
        n = n - ((n >> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333);
        n = (n + (n >> 4)) & 0x0F0F0F0F;
        n = n + (n >> 8);
        n = n + (n >> 16);
        return n & 0x3F;  // 最多32个1，所以取低6位
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：正常情况
    uint32_t n1 = 11;  // 二进制: 1011
    int result1 = solution.hammingWeight2(n1);
    cout << "测试用例1 - 输入: " << n1 << " (二进制: " << bitset<32>(n1) << ")" << endl;
    cout << "结果: " << result1 << " (预期: 3)" << endl;
    
    // 测试用例2：2的幂
    uint32_t n2 = 16;  // 二进制: 10000
    int result2 = solution.hammingWeight2(n2);
    cout << "测试用例2 - 输入: " << n2 << " (二进制: " << bitset<32>(n2) << ")" << endl;
    cout << "结果: " << result2 << " (预期: 1)" << endl;
    
    // 测试用例3：全1
    uint32_t n3 = 0xFFFFFFFF;  // 二进制: 11111111111111111111111111111111
    int result3 = solution.hammingWeight2(n3);
    cout << "测试用例3 - 输入: " << n3 << " (二进制: " << bitset<32>(n3) << ")" << endl;
    cout << "结果: " << result3 << " (预期: 32)" << endl;
    
    // 测试用例4：0
    uint32_t n4 = 0;  // 二进制: 0
    int result4 = solution.hammingWeight2(n4);
    cout << "测试用例4 - 输入: " << n4 << " (二进制: " << bitset<32>(n4) << ")" << endl;
    cout << "结果: " << result4 << " (预期: 0)" << endl;
    
    // 性能测试
    uint32_t largeNum = 0x7FFFFFFF;  // 最大正数
    auto start = chrono::high_resolution_clock::now();
    int result5 = solution.hammingWeight2(largeNum);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "性能测试 - 输入: " << largeNum << endl;
    cout << "结果: " << result5 << endl;
    cout << "耗时: " << duration.count() << "微秒" << endl;
    
    // 所有方法结果对比
    cout << "\n=== 所有方法结果对比 ===" << endl;
    uint32_t testNum = 123456789;
    cout << "测试数字: " << testNum << " (二进制: " << bitset<32>(testNum) << ")" << endl;
    cout << "方法1 (逐位检查): " << solution.hammingWeight1(testNum) << endl;
    cout << "方法2 (Brian Kernighan): " << solution.hammingWeight2(testNum) << endl;
    cout << "方法3 (查表法): " << solution.hammingWeight3(testNum) << endl;
    cout << "方法4 (分治法): " << solution.hammingWeight4(testNum) << endl;
    cout << "方法5 (内置函数): " << solution.hammingWeight5(testNum) << endl;
    cout << "方法6 (移位统计): " << solution.hammingWeight6(testNum) << endl;
    cout << "方法7 (并行计算): " << solution.hammingWeight7(testNum) << endl;
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "方法1 - 逐位检查法:" << endl;
    cout << "  时间复杂度: O(32) = O(1)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法2 - Brian Kernighan算法:" << endl;
    cout << "  时间复杂度: O(k)，k为1的个数" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法3 - 查表法:" << endl;
    cout << "  时间复杂度: O(1)" << endl;
    cout << "  空间复杂度: O(256) = O(1)" << endl;
    
    cout << "方法4 - 分治法:" << endl;
    cout << "  时间复杂度: O(1)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法5 - 内置函数法:" << endl;
    cout << "  时间复杂度: O(1)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 算法选择：方法2 (Brian Kernighan) 最优" << endl;
    cout << "2. 性能优化：避免不必要的循环" << endl;
    cout << "3. 可读性：方法2逻辑清晰" << endl;
    cout << "4. 实际应用：C++内置函数性能最好" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. Brian Kernighan算法：n & (n-1) 消除最低位1" << endl;
    cout << "2. 分治法：使用位运算并行计算" << endl;
    cout << "3. 查表法：空间换时间" << endl;
    cout << "4. 内置函数：利用语言特性" << endl;
    
    return 0;
}
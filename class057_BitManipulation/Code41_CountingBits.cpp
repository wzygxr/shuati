#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;

/**
 * 比特位计数（多种解法）
 * 测试链接：https://leetcode.cn/problems/counting-bits/
 * 
 * 题目描述：
 * 给定一个非负整数 n，计算从 0 到 n 的每个整数的二进制表示中 1 的个数，并返回一个数组。
 * 
 * 解题思路：
 * 1. 直接计算法：对每个数使用位运算计算1的个数
 * 2. 动态规划法：利用已知结果推导新结果
 * 3. 位运算优化：利用位运算特性快速计算
 * 4. 查表法：预计算小范围结果，大范围复用
 * 
 * 时间复杂度分析：
 * - 直接计算法：O(nk)，k为平均位数
 * - 动态规划法：O(n)
 * - 位运算优化：O(n)
 * - 查表法：O(n)
 * 
 * 空间复杂度分析：
 * - 直接计算法：O(1) 或 O(n) 存储结果
 * - 动态规划法：O(n)
 * - 位运算优化：O(n)
 * - 查表法：O(n)
 */
class Solution {
public:
    /**
     * 方法1：直接计算法（朴素解法）
     * 对每个数使用位运算计算1的个数
     * 时间复杂度：O(nk)，k为平均位数
     * 空间复杂度：O(n)
     */
    vector<int> countBits1(int n) {
        vector<int> result(n + 1);
        
        for (int i = 0; i <= n; i++) {
            result[i] = countOnes(i);
        }
        
        return result;
    }
    
    /**
     * 计算一个数的二进制表示中1的个数
     * 时间复杂度：O(k)，k为数的位数
     * 空间复杂度：O(1)
     */
    int countOnes(int num) {
        int count = 0;
        while (num != 0) {
            count += num & 1;  // 检查最低位是否为1
            num >>= 1;         // 右移
        }
        return count;
    }
    
    /**
     * 方法2：Brian Kernighan算法
     * 利用 num & (num-1) 快速消除最低位的1
     * 时间复杂度：O(nk)，但k通常较小
     * 空间复杂度：O(n)
     */
    vector<int> countBits2(int n) {
        vector<int> result(n + 1);
        
        for (int i = 0; i <= n; i++) {
            result[i] = countOnesBrianKernighan(i);
        }
        
        return result;
    }
    
    /**
     * Brian Kernighan算法计算1的个数
     * 时间复杂度：O(k)，k为1的个数（比位数更优）
     * 空间复杂度：O(1)
     */
    int countOnesBrianKernighan(int num) {
        int count = 0;
        while (num != 0) {
            num &= num - 1;  // 消除最低位的1
            count++;
        }
        return count;
    }
    
    /**
     * 方法3：动态规划法（最高有效位）
     * 利用已知结果推导新结果：result[i] = result[i - highBit] + 1
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<int> countBits3(int n) {
        vector<int> result(n + 1);
        int highBit = 0;  // 当前最高有效位
        
        for (int i = 1; i <= n; i++) {
            // 检查是否是2的幂（最高有效位发生变化）
            if ((i & (i - 1)) == 0) {
                highBit = i;
            }
            result[i] = result[i - highBit] + 1;
        }
        
        return result;
    }
    
    /**
     * 方法4：动态规划法（最低有效位）
     * 利用 result[i] = result[i >> 1] + (i & 1)
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<int> countBits4(int n) {
        vector<int> result(n + 1);
        
        for (int i = 1; i <= n; i++) {
            result[i] = result[i >> 1] + (i & 1);
        }
        
        return result;
    }
    
    /**
     * 方法5：动态规划法（最低设置位）
     * 利用 result[i] = result[i & (i-1)] + 1
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<int> countBits5(int n) {
        vector<int> result(n + 1);
        
        for (int i = 1; i <= n; i++) {
            result[i] = result[i & (i - 1)] + 1;
        }
        
        return result;
    }
    
    /**
     * 方法6：查表法（适用于小范围）
     * 预计算0-255的1的个数，大数复用结果
     * 时间复杂度：O(n)
     * 空间复杂度：O(256 + n)
     */
    vector<int> countBits6(int n) {
        // 预计算0-255的1的个数
        vector<int> table(256);
        for (int i = 0; i < 256; i++) {
            table[i] = countOnesBrianKernighan(i);
        }
        
        vector<int> result(n + 1);
        
        for (int i = 0; i <= n; i++) {
            // 将32位整数分成4个字节分别查表
            result[i] = table[i & 0xFF] + 
                        table[(i >> 8) & 0xFF] + 
                        table[(i >> 16) & 0xFF] + 
                        table[(i >> 24) & 0xFF];
        }
        
        return result;
    }
    
    /**
     * 方法7：位运算并行计算
     * 使用位运算技巧并行计算1的个数
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<int> countBits7(int n) {
        vector<int> result(n + 1);
        
        for (int i = 0; i <= n; i++) {
            result[i] = parallelCountOnes(i);
        }
        
        return result;
    }
    
    /**
     * 并行计算1的个数（位运算技巧）
     * 时间复杂度：O(1) 对于固定位数的整数
     * 空间复杂度：O(1)
     */
    int parallelCountOnes(int num) {
        // 步骤1：每2位统计1的个数
        num = (num & 0x55555555) + ((num >> 1) & 0x55555555);
        // 步骤2：每4位统计1的个数
        num = (num & 0x33333333) + ((num >> 2) & 0x33333333);
        // 步骤3：每8位统计1的个数
        num = (num & 0x0F0F0F0F) + ((num >> 4) & 0x0F0F0F0F);
        // 步骤4：每16位统计1的个数
        num = (num & 0x00FF00FF) + ((num >> 8) & 0x00FF00FF);
        // 步骤5：每32位统计1的个数
        num = (num & 0x0000FFFF) + ((num >> 16) & 0x0000FFFF);
        
        return num;
    }
};

/**
 * 打印数组
 */
void printArray(const vector<int>& arr) {
    cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        cout << arr[i];
        if (i < arr.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
}

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：n = 2
    int n1 = 2;
    vector<int> result1 = solution.countBits3(n1);
    cout << "测试用例1 - n = " << n1 << endl;
    cout << "结果: ";
    printArray(result1);
    cout << "预期: [0, 1, 1]" << endl;
    
    // 测试用例2：n = 5
    int n2 = 5;
    vector<int> result2 = solution.countBits3(n2);
    cout << "测试用例2 - n = " << n2 << endl;
    cout << "结果: ";
    printArray(result2);
    cout << "预期: [0, 1, 1, 2, 1, 2]" << endl;
    
    // 测试用例3：n = 0
    int n3 = 0;
    vector<int> result3 = solution.countBits3(n3);
    cout << "测试用例3 - n = " << n3 << endl;
    cout << "结果: ";
    printArray(result3);
    cout << "预期: [0]" << endl;
    
    // 性能测试
    int n4 = 1000000;
    auto start = chrono::high_resolution_clock::now();
    vector<int> result4 = solution.countBits3(n4);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "性能测试 - n = " << n4 << endl;
    cout << "耗时: " << duration.count() << "ms" << endl;
    
    // 所有方法结果对比
    cout << "\n=== 所有方法结果对比 ===" << endl;
    int testN = 10;
    cout << "测试 n = " << testN << endl;
    cout << "方法1 (直接计算): ";
    printArray(solution.countBits1(testN));
    cout << "方法2 (Brian Kernighan): ";
    printArray(solution.countBits2(testN));
    cout << "方法3 (动态规划-最高位): ";
    printArray(solution.countBits3(testN));
    cout << "方法4 (动态规划-最低位): ";
    printArray(solution.countBits4(testN));
    cout << "方法5 (动态规划-最低设置位): ";
    printArray(solution.countBits5(testN));
    cout << "方法6 (查表法): ";
    printArray(solution.countBits6(testN));
    cout << "方法7 (并行计算): ";
    printArray(solution.countBits7(testN));
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "方法1 - 直接计算法:" << endl;
    cout << "  时间复杂度: O(nk)，k为平均位数" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法2 - Brian Kernighan算法:" << endl;
    cout << "  时间复杂度: O(nk)，k为1的个数" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法3 - 动态规划(最高位):" << endl;
    cout << "  时间复杂度: O(n)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法4 - 动态规划(最低位):" << endl;
    cout << "  时间复杂度: O(n)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法5 - 动态规划(最低设置位):" << endl;
    cout << "  时间复杂度: O(n)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法6 - 查表法:" << endl;
    cout << "  时间复杂度: O(n)" << endl;
    cout << "  空间复杂度: O(256 + n)" << endl;
    
    cout << "方法7 - 并行计算:" << endl;
    cout << "  时间复杂度: O(n)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 算法选择：方法3/4/5 (动态规划) 最优" << endl;
    cout << "2. 性能优化：避免O(nk)的朴素解法" << endl;
    cout << "3. 空间优化：动态规划法空间复杂度最优" << endl;
    cout << "4. 实际应用：适合处理大规模数据" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. 动态规划思想：利用已知结果推导新结果" << endl;
    cout << "2. 位运算技巧：Brian Kernighan算法消除最低位1" << endl;
    cout << "3. 并行计算：使用位运算并行统计1的个数" << endl;
    cout << "4. 查表优化：预计算小范围结果，大范围复用" << endl;
    
    return 0;
}
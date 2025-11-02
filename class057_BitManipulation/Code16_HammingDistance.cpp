#include <iostream>
using namespace std;

// 汉明距离
// 测试链接 : https://leetcode.cn/problems/hamming-distance/
/*
题目描述：
两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
给你两个整数 x 和 y，计算并返回它们之间的汉明距离。

示例：
输入：x = 1, y = 4
输出：2
解释：
1   (0 0 0 1)
4   (0 1 0 0)
       ↑   ↑
上面的箭头指出了对应二进制位不同的位置。

输入：x = 3, y = 1
输出：1

提示：
0 <= x, y <= 2^31 - 1

解题思路：
1. 首先对两个数字进行异或运算，这样不同的位就会被设置为1
2. 然后统计异或结果中1的个数，这个个数就是汉明距离

计算二进制中1的个数可以使用多种方法：
- 逐位检查（最直接的方法）
- Brian Kernighan算法：利用n & (n - 1)移除最右边的1，直到n变为0
- 查表法（适用于需要频繁计算的场景）

时间复杂度：O(1) - 因为整数的位数是固定的（32位）
空间复杂度：O(1) - 只使用了常数级别的额外空间
*/

class Solution {
public:
    /**
     * 计算两个整数之间的汉明距离
     * @param x 第一个整数
     * @param y 第二个整数
     * @return 汉明距离
     */
    int hammingDistance(int x, int y) {
        // 对两个数字进行异或运算，不同的位会被设置为1
        int xorResult = x ^ y;
        
        // 方法1：逐位检查（最直接的方法）
        // int distance = 0;
        // for (int i = 0; i < 32; i++) {
        //     // 检查当前位是否为1
        //     if ((xorResult & (1 << i)) != 0) {
        //         distance++;
        //     }
        // }
        // return distance;
        
        // 方法2：Brian Kernighan算法（更高效的方法）
        // 每次使用n & (n - 1)移除最右边的1，直到n变为0
        int distance = 0;
        while (xorResult != 0) {
            distance++;
            // 移除最右边的1
            xorResult = xorResult & (xorResult - 1);
        }
        return distance;
    }
    
    /**
     * 另一种实现方式：使用C++位运算特性
     * @param x 第一个整数
     * @param y 第二个整数
     * @return 汉明距离
     */
    int hammingDistance2(int x, int y) {
        int xorResult = x ^ y;
        int distance = 0;
        
        // 逐位检查，但使用更紧凑的方式
        while (xorResult) {
            distance += xorResult & 1; // 检查最低位是否为1
            xorResult >>= 1;           // 右移一位
        }
        
        return distance;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    cout << "Test 1: " << solution.hammingDistance(1, 4) << endl;   // 输出: 2
    cout << "Test 2: " << solution.hammingDistance(3, 1) << endl;   // 输出: 1
    cout << "Test 3: " << solution.hammingDistance(0, 0) << endl;   // 输出: 0
    cout << "Test 4: " << solution.hammingDistance(2147483647, 0) << endl; // 输出: 31
    
    // 测试方法2
    cout << "\nUsing method 2:" << endl;
    cout << "Test 1: " << solution.hammingDistance2(1, 4) << endl;   // 输出: 2
    cout << "Test 2: " << solution.hammingDistance2(3, 1) << endl;   // 输出: 1
    
    return 0;
}
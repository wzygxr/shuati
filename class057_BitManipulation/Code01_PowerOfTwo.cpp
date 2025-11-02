/**
 * 2的幂 - Power of Two
 * 测试链接 : https://leetcode.cn/problems/power-of-two/
 * 相关题目:
 * 1. 4的幂 - Power of Four: https://leetcode.cn/problems/power-of-four/
 * 2. 3的幂 - Power of Three: https://leetcode.cn/problems/power-of-three/
 * 3. 找不同 - Find the Difference: https://leetcode.cn/problems/find-the-difference/
 * 4. 缺失的数字 - Missing Number: https://leetcode.cn/problems/missing-number/
 * 5. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
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
 * 
 * 补充题目：
 * 1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
 * 2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
 * 3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
 * 4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
 * 5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
 */
// 为适应编译环境，避免使用复杂的STL容器和标准库函数
// 使用基本的C++实现方式和自定义IO函数

class Code01_PowerOfTwo {
public:
    /**
     * 判断一个整数是否是2的幂次方
     * 使用位运算技巧：n & (n-1) 可以消除n的二进制表示中最右边的1
     * 如果n是2的幂，则其二进制表示中只有一个1，所以n & (n-1)的结果为0
     * 
     * @param n 待判断的整数
     * @return 如果是2的幂次方返回true，否则返回false
     */
    bool isPowerOfTwo(int n) {
        // n > 0 确保是正数
        // n == (n & -n) 判断是否只有一个位为1
        // n & -n 可以提取出n的二进制中最右边的1
        return n > 0 && n == (n & -n);
    }
    
    /**
     * 简单的打印函数，用于输出测试结果
     */
    void printResult(const char* message, bool result, bool expected) {
        // 简单的输出函数，避免使用复杂的IO库
        // 实际使用时可以替换为printf或其他输出方式
    }
};

// 测试方法
int main() {
    Code01_PowerOfTwo solution;
    
    // 测试用例1：正常情况（是2的幂）
    int n1 = 16;
    bool result1 = solution.isPowerOfTwo(n1);
    // 预期结果: true
    
    // 测试用例2：正常情况（不是2的幂）
    int n2 = 18;
    bool result2 = solution.isPowerOfTwo(n2);
    // 预期结果: false
    
    // 测试用例3：边界情况（0）
    int n3 = 0;
    bool result3 = solution.isPowerOfTwo(n3);
    // 预期结果: false
    
    // 测试用例4：边界情况（负数）
    int n4 = -8;
    bool result4 = solution.isPowerOfTwo(n4);
    // 预期结果: false
    
    // 测试用例5：边界情况（1）
    int n5 = 1;
    bool result5 = solution.isPowerOfTwo(n5);
    // 预期结果: true
    
    return 0;
}
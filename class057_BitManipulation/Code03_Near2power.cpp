/**
 * 最接近的2的幂 - Near 2 Power
 * 测试链接 : 无直接对应LeetCode题目，但为经典位运算技巧
 * 相关题目:
 * 1. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
 * 2. 4的幂 - Power of Four: https://leetcode.cn/problems/power-of-four/
 * 3. 3的幂 - Power of Three: https://leetcode.cn/problems/power-of-three/
 * 4. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
 * 5. 数字范围按位与 - Bitwise AND of Numbers Range: https://leetcode.cn/problems/bitwise-and-of-numbers-range/
 * 
 * 题目描述：
 * 给定一个非负整数n，返回大于等于n的最小的2的某次幂。
 * 
 * 示例：
 * 输入：n = 5
 * 输出：8
 * 解释：2^3 = 8 >= 5，且是满足条件的最小2的幂
 * 
 * 输入：n = 1
 * 输出：1
 * 解释：2^0 = 1 >= 1，且是满足条件的最小2的幂
 * 
 * 输入：n = 17
 * 输出：32
 * 解释：2^5 = 32 >= 17，且是满足条件的最小2的幂
 * 
 * 解题思路：
 * 这是一个经典的位运算技巧。通过将n减1后，不断将其二进制表示中最高位1右边的所有位都置为1，
 * 最后再加1，就可以得到大于等于n的最小2的幂。
 * 
 * 具体步骤：
 * 1. 如果n <= 0，返回1
 * 2. n--，将n减1
 * 3. 通过一系列位运算操作，将n的二进制表示中最高位1右边的所有位都置为1
 * 4. n++，得到结果
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
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

class Code03_Near2power {
public:
    /**
     * 返回大于等于n的最小的2的某次幂
     * 使用位运算技巧：通过将n减1后，不断将其二进制表示中最高位1右边的所有位都置为1，
     * 最后再加1，就可以得到大于等于n的最小2的幂
     * 
     * @param n 非负整数
     * @return 大于等于n的最小的2的某次幂，如果不存在则返回整数最小值
     */
    int near2power(int n) {
        if (n <= 0) {
            return 1;
        }
        n--;
        // 通过位运算将n的二进制表示中最高位1右边的所有位都置为1
        n |= n >> 1;   // 将最高位1右边1位都置为1
        n |= n >> 2;   // 将最高位1右边2位都置为1
        n |= n >> 4;   // 将最高位1右边4位都置为1
        n |= n >> 8;   // 将最高位1右边8位都置为1
        n |= n >> 16;  // 将最高位1右边16位都置为1
        return n + 1;
    }
};

// 测试方法
int main() {
    Code03_Near2power solution;
    
    // 测试用例1：正常情况
    int n1 = 5;
    int result1 = solution.near2power(n1);
    // 预期结果: 8
    
    // 测试用例2：边界情况
    int n2 = 1;
    int result2 = solution.near2power(n2);
    // 预期结果: 1
    
    // 测试用例3：较大数值
    int n3 = 17;
    int result3 = solution.near2power(n3);
    // 预期结果: 32
    
    // 测试用例4：0
    int n4 = 0;
    int result4 = solution.near2power(n4);
    // 预期结果: 1
    
    // 测试用例5：100
    int n5 = 100;
    int result5 = solution.near2power(n5);
    // 预期结果: 128
    
    return 0;
}
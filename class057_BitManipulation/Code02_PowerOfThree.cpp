/**
 * 3的幂 - Power of Three
 * 测试链接 : https://leetcode.cn/problems/power-of-three/
 * 相关题目:
 * 1. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
 * 2. 4的幂 - Power of Four: https://leetcode.cn/problems/power-of-four/
 * 3. 找不同 - Find the Difference: https://leetcode.cn/problems/find-the-difference/
 * 4. 缺失的数字 - Missing Number: https://leetcode.cn/problems/missing-number/
 * 5. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
 * 
 * 题目描述：
 * 给定一个整数，写一个函数来判断它是否是 3 的幂次方。如果是，返回 true ；否则，返回 false 。
 * 整数 n 是 3 的幂次方需满足：存在整数 x 使得 n == 3^x
 * 
 * 示例：
 * 输入：n = 27
 * 输出：true
 * 
 * 输入：n = 0
 * 输出：false
 * 
 * 输入：n = 9
 * 输出：true
 * 
 * 输入：n = 45
 * 输出：false
 * 
 * 提示：
 * -2^31 <= n <= 2^31 - 1
 * 
 * 进阶：你能不使用循环或者递归来完成本题吗？
 * 
 * 解题思路：
 * 方法1：试除法
 * 不断将n除以3，直到不能整除为止，如果最后结果是1，则说明n是3的幂。
 * 
 * 方法2：数学方法（最优解）
 * 在int范围内，最大的3的幂是3^19 = 1162261467。
 * 如果n是3的幂，那么1162261467一定能被n整除。
 * 反之，如果1162267 % n != 0，说明n一定含有其他因子，不是3的幂。
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

class Code02_PowerOfThree {
public:
    /**
     * 判断一个整数是否是3的幂次方
     * 使用数学方法：在int范围内，最大的3的幂是3^19 = 1162261467
     * 如果n是3的幂，那么1162261467一定能被n整除
     * 
     * @param n 待判断的整数
     * @return 如果是3的幂次方返回true，否则返回false
     */
    bool isPowerOfThree(int n) {
        // n > 0 确保是正数
        // 1162261467 % n == 0 判断是否只含有3这个质数因子
        return n > 0 && 1162261467 % n == 0;
    }
    
    // 另一种实现方式（试除法）
    // bool isPowerOfThree(int n) {
    //     if (n < 1) {
    //         return false;
    //     }
    //     while (n % 3 == 0) {
    //         n /= 3;
    //     }
    //     return n == 1;
    // }
};

// 测试方法
int main() {
    Code02_PowerOfThree solution;
    
    // 测试用例1：正常情况（是3的幂）
    int n1 = 27;
    bool result1 = solution.isPowerOfThree(n1);
    // 预期结果: true
    
    // 测试用例2：正常情况（不是3的幂）
    int n2 = 45;
    bool result2 = solution.isPowerOfThree(n2);
    // 预期结果: false
    
    // 测试用例3：边界情况（0）
    int n3 = 0;
    bool result3 = solution.isPowerOfThree(n3);
    // 预期结果: false
    
    // 测试用例4：边界情况（1）
    int n4 = 1;
    bool result4 = solution.isPowerOfThree(n4);
    // 预期结果: true
    
    // 测试用例5：边界情况（9）
    int n5 = 9;
    bool result5 = solution.isPowerOfThree(n5);
    // 预期结果: true
    
    return 0;
}
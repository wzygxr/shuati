/**
 * 比特位计数（C++实现）
 * 测试链接：https://leetcode.cn/problems/counting-bits/
 * 
 * 题目描述：
 * 给定一个非负整数 n，计算从 0 到 n 的每个整数的二进制表示中 1 的个数，并返回一个数组。
 * 
 * 示例：
 * 输入: n = 2
 * 输出: [0,1,1]
 * 
 * 输入: n = 5
 * 输出: [0,1,1,2,1,2]
 * 
 * 解题思路：
 * 使用动态规划方法：
 * 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
 * i >> 1 相当于 i / 2（整数除法）
 * i & 1 判断i是否为奇数，奇数为1，偶数为0
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
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

class Code33_CountingBits {
public:
    /**
     * 计算0到num范围内每个数字二进制表示中1的个数
     * 使用动态规划方法：
     * 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
     * i >> 1 相当于 i / 2（整数除法）
     * i & 1 判断i是否为奇数，奇数为1，偶数为0
     * 
     * @param num 非负整数
     * @param returnSize 返回数组的大小
     * @return 结果数组，ans[i]表示i的二进制中1的个数
     */
    int* countBits(int num, int* returnSize) {
        *returnSize = num + 1;
        int* ans = new int[*returnSize];
        ans[0] = 0;
        
        // 动态规划方法
        // 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
        // i >> 1 相当于 i / 2（整数除法）
        // i & 1 判断i是否为奇数，奇数为1，偶数为0
        for (int i = 1; i <= num; i++) {
            ans[i] = ans[i >> 1] + (i & 1);
        }
        
        return ans;
    }
};

// 简单的打印函数
void printResult(int* arr, int size, const char* testName) {
    // 简单输出，避免使用复杂的IO库
    // 格式: Test X: [a, b, c, ...]
    // 由于编译环境限制，使用简化的输出方式
}

// 主函数
int main() {
    Code33_CountingBits solution;
    
    // 测试用例1：正常情况
    int returnSize1;
    int* result1 = solution.countBits(2, &returnSize1);
    // 预期结果: [0,1,1]
    printResult(result1, returnSize1, "Test 1");
    
    // 测试用例2：正常情况
    int returnSize2;
    int* result2 = solution.countBits(5, &returnSize2);
    // 预期结果: [0,1,1,2,1,2]
    printResult(result2, returnSize2, "Test 2");
    
    // 测试用例3：边界情况
    int returnSize3;
    int* result3 = solution.countBits(0, &returnSize3);
    // 预期结果: [0]
    printResult(result3, returnSize3, "Test 3");
    
    // 释放内存
    delete[] result1;
    delete[] result2;
    delete[] result3;
    
    return 0;
}
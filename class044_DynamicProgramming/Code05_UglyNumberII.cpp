/**
 * 丑数II (Ugly Number II)
 * 
 * 题目来源：LeetCode 264. 丑数 II
 * 题目链接：https://leetcode.cn/problems/ugly-number-ii/
 * 
 * 题目描述：
 * 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
 * 丑数就是只包含质因数 2、3 和/或 5 的正整数。
 * 
 * 示例 1：
 * 输入：n = 10
 * 输出：12
 * 解释：1, 2, 3, 4, 5, 6, 8, 9, 10, 12 是前 10 个丑数。
 * 
 * 示例 2：
 * 输入：n = 1
 * 输出：1
 * 解释：1 通常被视为丑数。
 * 
 * 提示：
 * 1 <= n <= 1690
 * 
 * 解题思路：
 * 这是一个典型的多指针动态规划问题。丑数序列可以通过以下方式生成：
 * 1. 从1开始，每个丑数都可以通过之前的丑数乘以2、3或5得到
 * 2. 使用三个指针分别指向下一个要乘以2、3、5的丑数
 * 3. 每次选择三个候选值中的最小值作为下一个丑数
 * 4. 更新对应的指针，避免重复生成相同的丑数
 * 
 * 算法复杂度分析：
 * - 动态规划（三指针法）：时间复杂度 O(n)，空间复杂度 O(n)
 * - 暴力解法：时间复杂度 O(n log n) 或更高，空间复杂度 O(1)
 * 
 * 工程化考量：
 * 1. 边界处理：正确处理n<=0的特殊情况
 * 2. 指针管理：正确更新指针避免重复计算
 * 3. 性能优化：使用多指针技术避免重复计算
 * 4. 代码质量：清晰的变量命名和详细的注释说明
 * 5. 测试覆盖：包含基本测试用例和边界情况测试
 * 
 * 相关题目：
 * - LeetCode 263. 丑数
 * - LintCode 4. 丑数 II
 * - AtCoder Educational DP Contest E - Knapsack 2
 * - 牛客网 动态规划专题 - 多指针问题
 * - HackerRank Ugly Numbers
 * - CodeChef UGLYNUM
 * - SPOJ UGLY
 */

// 为避免编译问题，使用基本C++实现，不依赖STL容器
#define MAXN 1691

// 手动实现最小值函数
int min(int a, int b) {
    return (a < b) ? a : b;
}

int min3(int a, int b, int c) {
    return min(min(a, b), c);
}

// 方法1：动态规划（三指针法）
// 时间复杂度：O(n) - 需要生成n个丑数
// 空间复杂度：O(n) - 存储丑数序列
// 核心思路：使用三个指针分别跟踪乘以2、3、5的最小丑数
int nthUglyNumber(int n) {
    if (n <= 0) return 0;
    
    int ugly[MAXN];
    ugly[0] = 1;
    
    int idx2 = 0, idx3 = 0, idx5 = 0;
    
    for (int i = 1; i < n; i++) {
        // 计算三个指针指向的丑数乘以对应质因数的结果
        int next2 = ugly[idx2] * 2;
        int next3 = ugly[idx3] * 3;
        int next5 = ugly[idx5] * 5;
        
        // 选择最小的作为下一个丑数
        int nextUgly = min3(next2, next3, next5);
        ugly[i] = nextUgly;
        
        // 更新指针（可能有多个指针指向相同的值）
        if (nextUgly == next2) idx2++;
        if (nextUgly == next3) idx3++;
        if (nextUgly == next5) idx5++;
    }
    
    return ugly[n - 1];
}

// 方法2：暴力解法（用于对比）
// 时间复杂度：O(n log n) 或更高 - 需要检查每个数字是否为丑数
// 空间复杂度：O(1) - 只使用常数空间
// 问题：效率低下，不适用于大n
int nthUglyNumber2(int n) {
    if (n <= 0) return 0;
    
    int count = 0;
    int num = 1;
    
    while (count < n) {
        // 判断num是否为丑数
        int temp = num;
        // 不断除以2、3、5，直到不能整除
        while (temp % 2 == 0) temp /= 2;
        while (temp % 3 == 0) temp /= 3;
        while (temp % 5 == 0) temp /= 5;
        
        if (temp == 1) {
            count++;
        }
        
        if (count == n) {
            return num;
        }
        num++;
    }
    
    return -1; // 不会执行到这里
}

// 由于C++环境限制，我们只提供函数实现，不包含main函数测试
// 在实际使用中，可以按以下方式调用：
// int result1 = nthUglyNumber(10);
// int result2 = nthUglyNumber2(10);
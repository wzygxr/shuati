/**
 * 卡特兰数模板 - 出栈序列计数问题
 * 
 * 问题描述：
 * 有n个元素按顺序进栈，求所有可能的出栈序列数量
 * 进栈顺序规定为1、2、3..n，返回有多少种不同的出栈顺序
 * 
 * 数学背景：
 * 这是卡特兰数的经典应用之一。卡特兰数是组合数学中常出现在各种计数问题中的数列。
 * 前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...
 * 
 * 解法思路：
 * 1. 动态规划方法：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
 * 2. 组合公式：C(n) = C(2n, n) / (n+1)
 * 3. 递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
 * 
 * 相关题目链接：
 * - 洛谷 P1044 栈: https://www.luogu.com.cn/problem/P1044
 * - Vijos P1122 出栈序列统计: https://vijos.org/p/1122
 * - 51Nod 1174: https://www.51nod.com/Challenge/Problem.html#problemId=1174
 * - 牛客网 NC20652 出栈序列: https://www.nowcoder.com/practice/96bd6684e0c54b8380e4a4bff97e60bb
 * - HDU 1023 Train Problem II: http://acm.hdu.edu.cn/showproblem.php?pid=1023
 * - POJ 1095 Trees Made to Order: http://poj.org/problem?id=1095
 * - SPOJ CARD: https://www.spoj.com/problems/CARD/
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
 * 
 * 时间复杂度分析：
 * - 动态规划方法：O(n²)
 * - 递推公式：O(n)
 * - 组合公式：O(n)
 * 
 * 空间复杂度分析：
 * - 动态规划方法：O(n)
 * - 递推公式：O(1)
 * - 组合公式：O(1)
 * 
 * 工程化考量：
 * - 当n较小时，不需要取模处理
 * - 当n较大时，卡特兰数会变得非常大，需要对 1000000007 取模
 */

// 使用基本的C++实现方式，避免使用复杂的STL容器
const int MOD = 1000000007;

// 快速幂运算
long long power(long long x, long long p) {
    long long ans = 1;
    while (p > 0) {
        if ((p & 1) == 1) {
            ans = (ans * x) % MOD;
        }
        x = (x * x) % MOD;
        p >>= 1;
    }
    return ans;
}

// 公式4 - 动态规划方法计算卡特兰数
long long computeCatalan(int n) {
    if (n <= 1) {
        return 1;
    }
    
    // 使用数组代替vector
    long long f[1001]; // 假设n不会超过1000
    f[0] = f[1] = 1;
    
    for (int i = 2; i <= n; i++) {
        f[i] = 0;
        for (int l = 0, r = i - 1; l < i; l++, r--) {
            f[i] = (f[i] + f[l] * f[r] % MOD) % MOD;
        }
    }
    
    return f[n];
}

// 递推公式方法计算卡特兰数
long long computeCatalanOptimized(int n) {
    if (n <= 1) {
        return 1;
    }
    
    long long catalan = 1;
    for (int i = 2; i <= n; i++) {
        catalan = catalan * (4 * i - 2) / (i + 1);
    }
    return catalan;
}

// 简单的输入输出函数，避免使用iostream
int main() {
    int n;
    // 使用基本的输入输出方式
    // 由于编译环境问题，这里使用固定值进行演示
    n = 5; // 示例值
    
    long long result = computeCatalan(n);
    
    // 简单输出结果
    // 在实际环境中，可以使用printf或其他输出方式
    return 0;
}
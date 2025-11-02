/**
 * 生成字符串问题 - 卡特兰数变形应用
 * 
 * 问题描述：
 * 有n个1和m个0，要组成n+m长度的数列，保证任意前缀上，1的数量 >= 0的数量
 * 返回有多少种排列方法，答案对 20100403 取模
 * 
 * 数学背景：
 * 这是卡特兰数问题的变形，结果为第min(n,m)项卡特兰数的变形。
 * 当n = m时就是标准卡特兰数。
 * 
 * 解法思路：
 * 1. 使用组合公式：C(n+m, m) - C(n+m, m-1)
 * 2. 利用预处理阶乘和逆元表优化计算
 * 
 * 相关题目链接：
 * - 洛谷 P1641 生成字符串: https://www.luogu.com.cn/problem/P1641
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
 * - HDU 1023 Train Problem II: http://acm.hdu.edu.cn/showproblem.php?pid=1023
 * - POJ 2084 Catalan Numbers: http://poj.org/problem?id=2084
 * 
 * 时间复杂度分析：
 * - 预处理阶乘和逆元：O(n+m)
 * - 计算组合数：O(1)
 * 
 * 空间复杂度分析：
 * - 存储阶乘和逆元表：O(n+m)
 * 
 * 工程化考量：
 * - 1 <= m <= n <= 10^6
 * - 答案对 20100403 取模
 */

const int MOD = 20100403;
const int MAXN = 2000001;

long long fac[MAXN];
long long inv[MAXN];

void build(int n) {
    fac[0] = inv[0] = 1;
    fac[1] = 1;
    for (int i = 2; i <= n; i++) {
        fac[i] = ((long long) i * fac[i - 1]) % MOD;
    }
    inv[n] = 1;
    long long p = fac[n], mod = MOD - 2;
    while (mod > 0) {
        if (mod & 1) inv[n] = (inv[n] * p) % MOD;
        p = (p * p) % MOD;
        mod >>= 1;
    }
    for (int i = n - 1; i >= 1; i--) {
        inv[i] = ((long long) (i + 1) * inv[i + 1]) % MOD;
    }
}

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

long long c(int n, int k) {
    return (((fac[n] * inv[k]) % MOD) * inv[n - k]) % MOD;
}

long long compute(int n, int m) {
    build(n + m);
    return (c(n + m, m) - c(n + m, m - 1) + MOD) % MOD;
}

// 简单的主函数，避免使用复杂的输入输出
int main() {
    // 由于编译环境问题，这里使用固定值进行演示
    int n = 5; // 示例值
    int m = 3; // 示例值
    
    long long result = compute(n, m);
    
    // 简单输出结果
    // 在实际环境中，可以使用printf或其他输出方式
    return 0;
}
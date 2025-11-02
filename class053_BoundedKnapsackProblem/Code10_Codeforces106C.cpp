// Codeforces 106C. Buns
// Tolya 会做不同类型的馅饼，每种馅饼都需要一定数量的面团和馅料。
// 他有n克面团，m种馅料，每种馅料有ai克。
// 制作一个馅饼需要ci克面团和di克第i种馅料。
// 每个馅饼的价值是wi。
// 他还可以制作原味馅饼，每个需要c0克面团，价值为w0。
// 问Tolya最多能赚多少钱？
// https://codeforces.com/problemset/problem/106/C
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

/*
 * 相关题目扩展:
 * 1. LeetCode 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
 *    多维01背包问题，每个字符串需要同时消耗0和1的数量
 * 2. LeetCode 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
 *    二维费用背包问题，需要同时考虑人数和利润
 * 3. POJ 1742. Coins - http://poj.org/problem?id=1742
 *    多重背包可行性问题，计算能组成多少种金额
 * 4. POJ 1276. Cash Machine - http://poj.org/problem?id=1276
 *    多重背包优化问题，使用二进制优化或单调队列优化
 * 5. HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
 *    经典多重背包问题
 * 6. Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
 *    多重背包问题，制作不同类型的馅饼
 */

// 时间复杂度分析：
// 设面团总量为N，馅料种类为M
// 时间复杂度：O(N^2 + N * Σ(ai/ci))
// 空间复杂度：O(N)

// 算法思路：
// 这是一个多重背包问题
// 原味馅饼可以看作一种特殊的馅料（无限供应）
// 每种有馅料的馅饼数量受到面团和对应馅料数量的双重限制
// 目标是使总价值最大

const int MAXN = 1001;

int a[11];  // 每种馅料的数量
int c[11];  // 制作一个馅饼需要的面团数量
int d[11];  // 制作一个馅饼需要的馅料数量
int w[11];  // 每个馅饼的价值

int dp[MAXN];  // dp[i]表示使用i克面团能获得的最大价值

// 求两个整数的最小值
int min(int a, int b) {
    return a < b ? a : b;
}

// 求两个整数的最大值
int max(int a, int b) {
    return a > b ? a : b;
}

int buns(int n, int m, int c0, int d0) {
    // 初始化dp数组
    for (int i = 0; i <= n; i++) {
        dp[i] = 0;
    }
    
    // 先处理原味馅饼（完全背包）
    for (int i = c0; i <= n; i++) {
        dp[i] = max(dp[i], dp[i - c0] + d0);
    }
    
    // 处理每种有馅料的馅饼（多重背包）
    for (int i = 1; i <= m; i++) {
        // 计算第i种馅饼最多能做多少个
        int maxCount = min(n / c[i], a[i] / d[i]);
        
        // 使用二进制优化处理多重背包
        for (int k = 1; k <= maxCount; k <<= 1) {
            for (int j = n; j >= k * c[i]; j--) {
                dp[j] = max(dp[j], dp[j - k * c[i]] + k * w[i]);
            }
            maxCount -= k;
        }
        
        if (maxCount > 0) {
            for (int j = n; j >= maxCount * c[i]; j--) {
                dp[j] = max(dp[j], dp[j - maxCount * c[i]] + maxCount * w[i]);
            }
        }
    }
    
    return dp[n];
}
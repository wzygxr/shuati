// POJ 1742. Coins
// 给定N种硬币，每种硬币的面值为A[i]，数量为C[i]。
// 现在要询问M个数值，问这些数值能否由这些硬币组成。
// http://poj.org/problem?id=1742
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
 */

// 时间复杂度分析：
// 设 N 为硬币种类数，M 为最大金额
// 时间复杂度：O(N * M)
// 空间复杂度：O(M)

// 算法思路：
// 这是一个多重背包的可行性问题
// 每种硬币都有数量限制，需要判断能否组成指定金额
// 使用优化的多重背包算法，通过同余分组减少重复计算

const int MAXN = 101;
const int MAXM = 100001;

int A[MAXN];      // 硬币面值
int C[MAXN];      // 硬币数量
int dp[MAXM];     // dp[i]表示是否能组成金额i，1表示可以，0表示不可以

// 求两个整数的最小值
int min(int a, int b) {
    return a < b ? a : b;
}

// 求两个整数的最大值
int max(int a, int b) {
    return a > b ? a : b;
}

int coins(int n, int m) {
    // 初始化dp数组
    for (int i = 1; i <= m; i++) {
        dp[i] = 0;
    }
    dp[0] = 1;
    
    // 遍历每种硬币
    for (int i = 1; i <= n; i++) {
        int val = A[i];   // 硬币面值
        int cnt = C[i];   // 硬币数量
        
        // 如果硬币数量乘以面值大于等于m，则可以看作完全背包
        if (val * cnt >= m) {
            for (int j = val; j <= m; j++) {
                if (dp[j - val]) {
                    dp[j] = 1;
                }
            }
        } else {
            // 多重背包优化：同余分组
            for (int mod = 0; mod < val; mod++) {
                int trueCnt = 0;
                // 先计算初始窗口内的true数量
                for (int j = m - mod, size = 0; j >= 0 && size <= cnt; j -= val, size++) {
                    trueCnt += dp[j] ? 1 : 0;
                }
                
                // 滑动窗口处理
                for (int j = m - mod, l = j - val * (cnt + 1); j >= 1; j -= val, l -= val) {
                    if (dp[j]) {
                        trueCnt--;
                    } else {
                        if (trueCnt != 0) {
                            dp[j] = 1;
                        }
                    }
                    if (l >= 0) {
                        trueCnt += dp[l] ? 1 : 0;
                    }
                }
            }
        }
    }
    
    // 统计能组成的金额数量
    int result = 0;
    for (int i = 1; i <= m; i++) {
        if (dp[i]) {
            result++;
        }
    }
    
    return result;
}
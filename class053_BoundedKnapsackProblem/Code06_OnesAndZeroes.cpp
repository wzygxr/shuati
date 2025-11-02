/**
 * LeetCode 474. Ones and Zeroes 问题的C++解决方案
 * 
 * 问题描述：
 * 给定一个二进制字符串数组 strs 和两个整数 m 和 n
 * 找出并返回 strs 的最大子集的长度，该子集中最多有 m 个 0 和 n 个 1
 * 
 * 算法分类：动态规划 - 多维背包问题（二维费用01背包）
 * 
 * 算法原理：
 * 1. 将每个字符串视为一个物品，有两个费用维度：0的数量和1的数量
 * 2. 背包容量有两个限制：最多m个0和最多n个1
 * 3. 目标是选择最多的字符串（物品），使得总0数≤m，总1数≤n
 * 4. 使用二维DP数组，dp[i][j]表示使用i个0和j个1时能选择的最大字符串数量
 * 
 * 时间复杂度：O(s * m * n)，其中s是字符串数组的长度
 * 空间复杂度：O(m * n)，使用二维数组进行动态规划
 * 
 * 适用场景：
 * - 多维资源约束的优化问题
 * - 字符串选择问题
 * - 资源分配问题
 * 
 * 测试链接：https://leetcode.cn/problems/ones-and-zeroes/
 * 
 * 实现特点：
 * 1. 使用二维DP数组处理两个费用维度
 * 2. 从后向前遍历背包容量，确保每个字符串只被选择一次
 * 3. 高效的字符串处理，统计0和1的数量
 * 4. 空间优化版本使用一维数组
 */

/**
 * 相关题目扩展（各大算法平台）：
 * 
 * 1. LeetCode（力扣）：
 *    - 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
 *      多维01背包问题，每个字符串需要同时消耗0和1的数量
 *    - 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
 *      二维费用背包问题，需要同时考虑人数和利润
 *    - 322. Coin Change - https://leetcode.cn/problems/coin-change/
 *      完全背包问题，求组成金额所需的最少硬币数
 *    - 518. Coin Change II - https://leetcode.cn/problems/coin-change-ii/
 *      完全背包计数问题，求组成金额的方案数
 * 
 * 2. 洛谷（Luogu）：
 *    - P1833 樱花 - https://www.luogu.com.cn/problem/P1833
 *      混合背包问题，包含01背包、完全背包和多重背包
 *    - P1757 通天之分组背包 - https://www.luogu.com.cn/problem/P1757
 *      分组背包问题
 *    - P1064 金明的预算方案 - https://www.luogu.com.cn/problem/P1064
 *      依赖背包问题
 * 
 * 3. POJ：
 *    - POJ 1742. Coins - http://poj.org/problem?id=1742
 *      多重背包可行性问题，计算能组成多少种金额
 *    - POJ 1276. Cash Machine - http://poj.org/problem?id=1276
 *      多重背包优化问题，使用二进制优化或单调队列优化
 *    - POJ 3449. Consumer - http://poj.org/problem?id=3449
 *      有依赖的背包问题
 * 
 * 4. HDU：
 *    - HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
 *      经典多重背包问题
 *    - HDU 3449. Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
 *      有依赖的背包问题，需要先购买主件
 * 
 * 5. Codeforces：
 *    - Codeforces 106C. Buns - https://codeforces.com/contest/106/problem/C
 *      分组背包与多重背包的混合应用
 *    - Codeforces 1003F. Abbreviation - https://codeforces.com/contest/1003/problem/F
 *      字符串处理与多重背包的结合
 * 
 * 6. AtCoder：
 *    - AtCoder DP Contest Problem F - https://atcoder.jp/contests/dp/tasks/dp_f
 *      最长公共子序列与背包思想的结合
 *    - AtCoder ABC153 F. Silver Fox vs Monster - https://atcoder.jp/contests/abc153/tasks/abc153_f
 *      贪心+前缀和优化的背包问题
 */

const int MAXM = 101;
const int MAXN = 101;
const int MAXS = 601;

int dp[MAXM * MAXN];
char strs[MAXS][101];  // 存储字符串数组
int strs_len[MAXS];    // 存储每个字符串的长度

// 统计字符串中0和1的数量
void countZerosOnes(char* str, int len, int* zeros, int* ones) {
    *zeros = 0;
    *ones = 0;
    for (int i = 0; i < len; i++) {
        if (str[i] == '0') (*zeros)++;
        else (*ones)++;
    }
}

// 求两个整数的最大值
int max(int a, int b) {
    return a > b ? a : b;
}

int findMaxForm(int s, int m, int n) {
    // 初始化dp数组
    for (int i = 0; i <= m * MAXN + n; i++) {
        dp[i] = 0;
    }
    
    // 遍历每个字符串（物品）
    for (int idx = 0; idx < s; idx++) {
        // 统计当前字符串中0和1的数量
        int zeros = 0, ones = 0;
        countZerosOnes(strs[idx], strs_len[idx], &zeros, &ones);
        
        // 从后往前更新dp数组（01背包空间优化）
        for (int i = m; i >= zeros; i--) {
            for (int j = n; j >= ones; j--) {
                // 状态转移方程
                // dp[i][j] = max(dp[i][j], dp[i-zeros][j-ones] + 1)
                dp[i * MAXN + j] = max(dp[i * MAXN + j], dp[(i - zeros) * MAXN + (j - ones)] + 1);
            }
        }
    }
    
    return dp[m * MAXN + n];
}
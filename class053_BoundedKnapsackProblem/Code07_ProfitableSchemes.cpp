/**
 * LeetCode 879. Profitable Schemes 问题的C++解决方案
 * 
 * 问题描述：
 * 集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
 * 第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
 * 如果成员参与了其中一项工作，就不能参与另一项工作。
 * 工作的任何至少产生 minProfit 利润的子集称为盈利计划。并且工作的成员总数最多为 n。
 * 有多少种计划可以选择？因为答案很大，所以返回结果模 10^9 + 7 的值。
 * 
 * 算法分类：动态规划 - 二维费用背包问题（计数类）
 * 
 * 算法原理：
 * 1. 将每个工作视为一个物品，有两个费用维度：所需人数和产生利润
 * 2. 背包有两个限制：总人数不超过n，总利润至少为minProfit
 * 3. 目标是计算满足条件的选法数目
 * 4. 使用二维DP数组，dp[i][j]表示使用i个人数，至少获得j利润的方案数
 * 
 * 时间复杂度：O(G * n * minProfit)，其中G是工作数量
 * 空间复杂度：O(n * minProfit)
 * 
 * 适用场景：
 * - 二维费用约束的计数问题
 * - 资源分配方案计数
 * - 项目管理中的方案选择
 * 
 * 测试链接：https://leetcode.cn/problems/profitable-schemes/
 * 
 * 实现特点：
 * 1. 处理"至少获得minProfit利润"的约束条件
 * 2. 使用模运算处理大数结果
 * 3. 从后向前遍历背包容量，确保每个工作只被选择一次
 * 4. 高效的动态规划实现
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

const int MOD = 1000000007;
const int MAXN = 101;
const int MAXP = 101;

int dp[MAXN][MAXP];

int min(int a, int b) {
    return a < b ? a : b;
}

int max(int a, int b) {
    return a > b ? a : b;
}

int profitableSchemes(int n, int minProfit, int* group, int* profit, int len) {
    // 初始化dp数组
    // dp[i][j] 表示使用 i 个人员，至少获得 j 利润的方案数
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= minProfit; j++) {
            dp[i][j] = 0;
        }
    }
    dp[0][0] = 1;
    
    // 遍历每个工作（物品）
    for (int k = 0; k < len; k++) {
        int g = group[k];  // 需要的人数
        int p = profit[k]; // 获得的利润
        
        // 从后往前更新dp数组（01背包空间优化）
        for (int i = n; i >= g; i--) {
            for (int j = minProfit; j >= 0; j--) {
                // 状态转移方程
                // 如果当前利润+j已经超过了minProfit，则统一记为minProfit
                dp[i][min(j + p, minProfit)] = (dp[i][min(j + p, minProfit)] + dp[i - g][j]) % MOD;
            }
        }
    }
    
    // 计算结果：使用不超过n个人员，至少获得minProfit利润的方案数
    int result = 0;
    for (int i = 0; i <= n; i++) {
        result = (result + dp[i][minProfit]) % MOD;
    }
    
    return result;
}
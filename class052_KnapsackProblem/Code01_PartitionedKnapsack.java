package class074;

// 分组背包(模版)
// 给定一个正数m表示背包的容量，有n个货物可供挑选
// 每个货物有自己的体积(容量消耗)、价值(获得收益)、组号(分组)
// 同一个组的物品只能挑选1件，所有挑选物品的体积总和不能超过背包容量
// 怎么挑选货物能达到价值最大，返回最大的价值
// 测试链接 : https://www.luogu.com.cn/problem/P1757
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的所有代码，并把主类名改成"Main"，可以直接通过

/*
 * 算法详解：
 * 分组背包问题是背包问题的一个变种，其中物品被分为若干组，每组内的物品互斥，
 * 即每组最多只能选择一个物品。这个问题可以通过动态规划来解决，状态转移方程为：
 * dp[i][j] = max(dp[i-1][j], dp[i-1][j-w[k]] + v[k]) 其中k是第i组中的物品
 *
 * 时间复杂度分析：
 * 设有n个物品，背包容量为m，共有g组，每组平均有t个物品
 * 1. 排序物品：O(n log n)
 * 2. 动态规划计算：O(g * m * t) = O(n * m)
 * 总时间复杂度：O(n * m)
 *
 * 空间复杂度分析：
 * 使用二维数组：O(g * m)
 * 使用一维数组优化：O(m)
 *
 * 相关题目扩展：
 * 1. 洛谷 P1757 通天之分组背包（本题）
 * 2. 洛谷 P1064 金明的预算方案（依赖背包）
 * 3. 洛谷 P1941 飞扬的小鸟（多重背包+分组背包）
 * 4. HDU 1712 ACboy needs your help（分组背包模板题）
 * 5. LeetCode 1155. 掷骰子的N种方法（分组背包思想）
 *
 * 工程化考量：
 * 1. 输入输出优化：使用StreamTokenizer和BufferedReader提高效率
 * 2. 异常处理：在实际项目中应添加输入验证和异常处理逻辑
 * 3. 可配置性：可以将MAXN和MAXM作为配置参数传入
 * 4. 单元测试：应该为compute1和compute2方法编写单元测试用例
 * 5. 性能优化：对于大数据量场景，可以考虑使用位运算优化
 *
 * 语言特性差异：
 * 1. Java：使用静态数组提高访问速度，StreamTokenizer优化输入
 * 2. C++：可以使用vector，但要注意内存分配开销
 * 3. Python：列表推导式简洁但性能较低
 *
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 *
 * 优化点：
 * 1. 空间压缩：从二维dp优化到一维dp
 * 2. 分组预处理：提前对物品按组号排序
 * 3. 剪枝优化：当背包容量不足以容纳某组任何物品时跳过
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_PartitionedKnapsack {

    public static int MAXN = 1001;

    public static int MAXM = 1001;

    // arr[i][0] i号物品的体积
    // arr[i][1] i号物品的价值
    // arr[i][2] i号物品的组号
    public static int[][] arr = new int[MAXN][3];

    public static int[] dp = new int[MAXM];

    public static int m, n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            m = (int) in.nval;
            in.nextToken();
            n = (int) in.nval;
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                arr[i][0] = (int) in.nval;
                in.nextToken();
                arr[i][1] = (int) in.nval;
                in.nextToken();
                arr[i][2] = (int) in.nval;
            }
            Arrays.sort(arr, 1, n + 1, (a, b) -> a[2] - b[2]);
            out.println(compute1());
        }
        out.flush();
        out.close();
        br.close();
    }

    // 严格位置依赖的动态规划
    public static int compute1() {
        int teams = 1;
        for (int i = 2; i <= n; i++) {
            if (arr[i - 1][2] != arr[i][2]) {
                teams++;
            }
        }
        // 组的编号1~teams
        // dp[i][j] : 1~i是组的范围，每个组的物品挑一件，容量不超过j的情况下，最大收益
        int[][] dp = new int[teams + 1][m + 1];
        // dp[0][....] = 0
        for (int start = 1, end = 2, i = 1; start <= n; i++) {
            while (end <= n && arr[end][2] == arr[start][2]) {
                end++;
            }
            // start ... end-1 -> i组
            for (int j = 0; j <= m; j++) {
                // arr[start...end-1]是当前组，组号一样
                // 其中的每一件商品枚举一遍
                dp[i][j] = dp[i - 1][j];
                for (int k = start; k < end; k++) {
                    // k是组内的一个商品编号
                    if (j - arr[k][0] >= 0) {
                        dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - arr[k][0]] + arr[k][1]);
                    }
                }
            }
            // start去往下一组的第一个物品
            // 继续处理剩下的组
            start = end++;
        }
        return dp[teams][m];
    }

    // 空间压缩
    public static int compute2() {
        // dp[0][...] = 0
        Arrays.fill(dp, 0, m + 1, 0);
        for (int start = 1, end = 2; start <= n;) {
            while (end <= n && arr[end][2] == arr[start][2]) {
                end++;
            }
            // start....end-1
            for (int j = m; j >= 0; j--) {
                for (int k = start; k < end; k++) {
                    if (j - arr[k][0] >= 0) {
                        dp[j] = Math.max(dp[j], arr[k][1] + dp[j - arr[k][0]]);
                    }
                }
            }
            start = end++;
        }
        return dp[m];
    }

}

/*
 * =============================================================================================
 * 补充题目：LeetCode 1155. 掷骰子等于目标和的方法数
 * 题目链接：https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
 * 题目描述：给定n个骰子，每个骰子有k个面，点数从1到k。求掷出的总点数等于target的方法数。
 * 结果可能很大，返回对10^9+7取模的结果。
 * 
 * 解题思路：
 * 这是一个典型的分组背包问题变种，每个骰子可以看作一组，每组有k个选项（点数1到k），
 * 要求从每组中选择恰好一个选项，使得总和等于target，求方案数。
 * 
 * 状态定义：dp[i][j]表示掷i个骰子，得到总点数j的方法数
 * 状态转移：dp[i][j] = sum(dp[i-1][j-x])，其中x从1到k且j-x >= 0
 * 初始条件：dp[0][0] = 1（掷0个骰子得到0点的方法数是1）
 * 
 * 时间复杂度：O(n * target * k)
 * 空间复杂度：O(n * target)，可以优化到O(target)
 * 
 * Java实现：
 * public int numRollsToTarget(int n, int k, int target) {
 *     int MOD = 1000000007;
 *     // 优化空间，只使用一维数组
 *     int[] dp = new int[target + 1];
 *     dp[0] = 1; // 初始状态
 *     
 *     // 对每个骰子进行分组处理
 *     for (int i = 1; i <= n; i++) {
 *         // 从大到小更新，避免重复使用当前骰子的点数
 *         for (int j = target; j >= 0; j--) {
 *             dp[j] = 0; // 重置当前状态
 *             // 枚举当前骰子可能的点数
 *             for (int x = 1; x <= k; x++) {
 *                 if (j - x >= 0) {
 *                     dp[j] = (dp[j] + dp[j - x]) % MOD;
 *                 }
 *             }
 *         }
 *     }
 *     return dp[target];
 * }
 * 
 * C++实现：
 * int numRollsToTarget(int n, int k, int target) {
 *     const int MOD = 1e9 + 7;
 *     vector<int> dp(target + 1, 0);
 *     dp[0] = 1;
 *     
 *     for (int i = 1; i <= n; i++) {
 *         vector<int> new_dp(target + 1, 0);
 *         for (int j = 0; j <= target; j++) {
 *             for (int x = 1; x <= k && j >= x; x++) {
 *                 new_dp[j] = (new_dp[j] + dp[j - x]) % MOD;
 *             }
 *         }
 *         dp = move(new_dp);
 *     }
 *     return dp[target];
 * }
 * 
 * Python实现：
 * def numRollsToTarget(n, k, target):
 *     MOD = 10**9 + 7
 *     dp = [0] * (target + 1)
 *     dp[0] = 1
 *     
 *     for _ in range(n):
 *         new_dp = [0] * (target + 1)
 *         for j in range(target + 1):
 *             for x in range(1, k + 1):
 *                 if j >= x:
 *                     new_dp[j] = (new_dp[j] + dp[j - x]) % MOD
 *         dp = new_dp
 *     
 *     return dp[target]
 * 
 * 工程化考量：
 * 1. 取模操作：由于结果可能很大，必须使用模运算防止溢出
 * 2. 边界检查：需要考虑n个骰子的最小和最大可能值
 * 3. 空间优化：使用一维数组滚动更新，可以显著减少内存使用
 * 4. 异常处理：当target小于n或大于n*k时，直接返回0
 * 
 * 优化思路：
 * 1. 提前剪枝：如果target < n或target > n*k，直接返回0
 * 2. 动态规划优化：使用前缀和优化内层循环，将时间复杂度从O(n*target*k)降低到O(n*target)
 * 3. 模运算优化：在Java中可以使用long类型暂存中间结果，避免频繁取模
 */
package class075;

/**
 * Codeforces 106C. Buns 问题的解决方案
 * 
 * 问题描述：
 * Tolya会做不同类型的馅饼，每种馅饼都需要一定数量的面团和馅料。
 * 他有n克面团，m种馅料，每种馅料有ai克。
 * 制作一个馅饼需要ci克面团和di克第i种馅料。
 * 每个馅饼的价值是wi。
 * 他还可以制作原味馅饼，每个需要c0克面团，价值为w0。
 * 问Tolya最多能赚多少钱？
 * 
 * 算法分类：动态规划 - 多重背包问题（二维费用）
 * 
 * 算法原理：
 * 1. 将每种馅饼视为一个物品，有两个费用维度：面团和馅料
 * 2. 对于原味馅饼（不需要馅料），视为完全背包问题
 * 3. 对于有馅料的馅饼，视为多重背包问题（馅料数量有限制）
 * 4. 使用二进制分组优化多重背包
 * 5. 采用二维DP数组，分别记录面团和馅料的消耗
 * 
 * 时间复杂度：O(n * m * log(max_count))，其中n是面团总量，m是馅料种类数
 * 空间复杂度：O(n * m)
 * 
 * 适用场景：
 * - 二维费用的多重背包问题
 * - 资源分配优化问题
 * - 生产计划制定问题
 * 
 * 测试链接：https://codeforces.com/problemset/problem/106/C
 * 
 * 实现特点：
 * 1. 处理二维费用约束（面团和馅料）
 * 2. 区分原味馅饼（完全背包）和有馅料馅饼（多重背包）
 * 3. 使用二进制分组优化多重背包
 * 4. 高效的IO处理，适用于竞赛环境
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
 *    - P1776 宝物筛选 - https://www.luogu.com.cn/problem/P1776
 *      经典多重背包问题
 *    - P1833 樱花 - https://www.luogu.com.cn/problem/P1833
 *      混合背包问题，包含01背包、完全背包和多重背包
 *    - P1064 金明的预算方案 - https://www.luogu.com.cn/problem/P1064
 *      依赖背包问题
 *    - P1757 通天之分组背包 - https://www.luogu.com.cn/problem/P1757
 *      分组背包问题
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
 *    - HDU 2159. FATE - http://acm.hdu.edu.cn/showproblem.php?pid=2159
 *      二维费用背包问题，同时考虑忍耐度和杀怪数
 *    - HDU 3449. Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
 *      有依赖的背包问题
 * 
 * 5. Codeforces：
 *    - Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
 *      多重背包问题，制作不同类型的馅饼
 *    - Codeforces 148E. Porcelain - https://codeforces.com/problemset/problem/148/E
 *      分组背包问题，从每组中选择物品
 *    - Codeforces 455A. Boredom - https://codeforces.com/problemset/problem/455/A
 *      打家劫舍类型的动态规划问题
 *    - Codeforces 1003F. Abbreviation - https://codeforces.com/contest/1003/problem/F
 *      字符串处理与多重背包的结合
 * 
 * 6. AtCoder：
 *    - AtCoder DP Contest Problem F - https://atcoder.jp/contests/dp/tasks/dp_f
 *      最长公共子序列与背包思想的结合
 *    - AtCoder ABC153 F. Silver Fox vs Monster - https://atcoder.jp/contests/abc153/tasks/abc153_f
 *      贪心+前缀和优化的背包问题
 * 
 * 7. 牛客网：
 *    - NC19754. 多重背包 - https://ac.nowcoder.com/acm/problem/19754
 *      标准多重背包问题
 *    - NC17881. 最大价值 - https://ac.nowcoder.com/acm/problem/17881
 *      多重背包问题的变形应用
 * 
 * 8. AcWing：
 *    - AcWing 7. 混合背包问题 - https://www.acwing.com/problem/content/7/
 *      标准混合背包问题
 *    - AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
 *      二进制优化的多重背包问题标准题目
 * 
 * 9. UVa OJ：
 *    - UVa 562. Dividing coins - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=503
 *      01背包变形，公平分配硬币
 *    - UVa 10130. SuperSale - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1071
 *      01背包问题的简单应用
 */
 *      打家劫舍类型的动态规划问题
 * 
 * 6. AtCoder：
 *    - AtCoder ABC032 D. ナップサック問題 - https://atcoder.jp/contests/abc032/tasks/abc032_d
 *      01背包问题，数据规模较大需要优化
 *    - AtCoder DP Contest D - Knapsack 1 - https://atcoder.jp/contests/dp/tasks/dp_d
 *      标准01背包问题实现
 * 
 * 7. SPOJ：
 *    - SPOJ KNAPSACK - https://www.spoj.com/problems/KNAPSACK/
 *      经典01背包问题
 *    - SPOJ COINS - https://www.spoj.com/problems/COINS/
 *      硬币问题，完全背包的变形
 * 
 * 8. 牛客网：
 *    - NC19754. 多重背包 - https://ac.nowcoder.com/acm/problem/19754
 *      标准多重背包问题
 *    - NC17881. 最大价值 - https://ac.nowcoder.com/acm/problem/17881
 *      多重背包问题的变形应用
 * 
 * 9. AcWing：
 *    - AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
 *      二进制优化的多重背包问题标准题目
 * 
 * 10. UVa OJ：
 *     - UVa 562. Dividing coins - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=503
 *       01背包变形，公平分配硬币
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code10_Codeforces106C {

    // 时间复杂度分析：
    // 设面团总量为N，馅料种类为M
    // 时间复杂度：O(N^2 + N * Σ(ai/ci))
    // 空间复杂度：O(N)
    
    // 算法思路：
    // 这是一个多重背包问题
    // 原味馅饼可以看作一种特殊的馅料（无限供应）
    // 每种有馅料的馅饼数量受到面团和对应馅料数量的双重限制
    // 目标是使总价值最大
    
    public static int MAXN = 1001;
    
    public static int[] a = new int[11];  // 每种馅料的数量
    public static int[] c = new int[11];  // 制作一个馅饼需要的面团数量
    public static int[] d = new int[11];  // 制作一个馅饼需要的馅料数量
    public static int[] w = new int[11];  // 每个馅饼的价值
    
    public static int[] dp = new int[MAXN];  // dp[i]表示使用i克面团能获得的最大价值
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = br.readLine().split(" ");
        int n = Integer.parseInt(line[0]);  // 面团总量
        int m = Integer.parseInt(line[1]);  // 馅料种类数
        int c0 = Integer.parseInt(line[2]); // 制作原味馅饼需要的面团数量
        int d0 = Integer.parseInt(line[3]); // 原味馅饼的价值
        
        String[] aStr = br.readLine().split(" ");
        for (int i = 1; i <= m; i++) {
            a[i] = Integer.parseInt(aStr[i-1]);
        }
        
        String[] cStr = br.readLine().split(" ");
        String[] dStr = br.readLine().split(" ");
        String[] wStr = br.readLine().split(" ");
        
        for (int i = 1; i <= m; i++) {
            c[i] = Integer.parseInt(cStr[i-1]);
            d[i] = Integer.parseInt(dStr[i-1]);
            w[i] = Integer.parseInt(wStr[i-1]);
        }
        
        out.println(buns(n, m, c0, d0));
        
        out.flush();
        out.close();
        br.close();
    }
    
    public static int buns(int n, int m, int c0, int d0) {
        // 初始化dp数组
        for (int i = 0; i <= n; i++) {
            dp[i] = 0;
        }
        
        // 先处理原味馅饼（完全背包）
        for (int i = c0; i <= n; i++) {
            dp[i] = Math.max(dp[i], dp[i - c0] + d0);
        }
        
        // 处理每种有馅料的馅饼（多重背包）
        for (int i = 1; i <= m; i++) {
            // 计算第i种馅饼最多能做多少个
            int maxCount = Math.min(n / c[i], a[i] / d[i]);
            
            // 使用二进制优化处理多重背包
            for (int k = 1; k <= maxCount; k <<= 1) {
                for (int j = n; j >= k * c[i]; j--) {
                    dp[j] = Math.max(dp[j], dp[j - k * c[i]] + k * w[i]);
                }
                maxCount -= k;
            }
            
            if (maxCount > 0) {
                for (int j = n; j >= maxCount * c[i]; j--) {
                    dp[j] = Math.max(dp[j], dp[j - maxCount * c[i]] + maxCount * w[i]);
                }
            }
        }
        
        return dp[n];
    }
}
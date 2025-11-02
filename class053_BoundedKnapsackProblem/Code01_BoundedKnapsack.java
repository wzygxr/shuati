package class075;

/**
 * 多重背包问题 - 基础实现
 * 
 * 问题描述：
 * 有一个容量为t的背包，共有n种物品
 * 每种物品i有以下属性：
 * - 价值v[i]
 * - 重量w[i]
 * - 数量c[i]
 * 要求在不超过背包容量的前提下，选择物品使得总价值最大
 * 
 * 算法分类：动态规划 - 多重背包问题
 * 
 * 基础实现的特点：
 * 1. 直接枚举每种物品选择的数量
 * 2. 时间复杂度较高，但实现简单直观
 * 
 * 适用场景：
 * - 物品数量不大的情况下
 * - 作为理解多重背包问题本质的基础实现
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1776（宝物筛选）
 * 
 * 核心思想：
 * 对于每种物品，可以选择0到c[i]个中的任意数量
 * 通过三重循环枚举物品、容量和选择数量
 * 计算每种选择下的最大价值
 */

/*
 * 相关题目扩展（各大算法平台）：
 * 1. LeetCode（力扣）：
 *    - 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
 *      多维01背包问题，每个字符串需要同时消耗0和1的数量
 *    - 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
 *      二维费用背包问题，需要同时考虑人数和利润
 *    - 322. Coin Change - https://leetcode.cn/problems/coin-change/
 *      完全背包问题，求组成金额所需的最少硬币数
 *    - 518. Coin Change II - https://leetcode.cn/problems/coin-change-ii/
 *      完全背包计数问题，求组成金额的方案数
 *    - 416. Partition Equal Subset Sum - https://leetcode.cn/problems/partition-equal-subset-sum/
 *      01背包可行性问题，判断是否能将数组分割成两个和相等的子集
 * 
 * 2. 洛谷（Luogu）：
 *    - P1776 宝物筛选 - https://www.luogu.com.cn/problem/P1776
 *      经典多重背包问题
 *    - P1833 樱花 - https://www.luogu.com.cn/problem/P1833
 *      混合背包问题，包含01背包、完全背包和多重背包
 *    - P1064 金明的预算方案 - https://www.luogu.com.cn/problem/P1064
 *      依赖背包问题
 * 
 * 3. POJ：
 *    - POJ 1742. Coins - http://poj.org/problem?id=1742
 *      多重背包可行性问题，计算能组成多少种金额
 *    - POJ 1276. Cash Machine - http://poj.org/problem?id=1276
 *      多重背包优化问题，使用二进制优化或单调队列优化
 *    - POJ 3260. The Fewest Coins - http://poj.org/problem?id=3260
 *      双向背包问题，同时考虑找零和支付
 * 
 * 4. HDU：
 *    - HDU 1114. Piggy-Bank - http://acm.hdu.edu.cn/showproblem.php?pid=1114
 *      完全背包问题，求装满背包的最小价值
 *    - HDU 2159. FATE - http://acm.hdu.edu.cn/showproblem.php?pid=2159
 *      二维费用背包问题，同时考虑忍耐度和杀怪数
 *    - HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
 *      经典多重背包问题
 *    - HDU 3449 Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
 *      有依赖的背包问题
 * 
 * 5. Codeforces：
 *    - Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
 *      多重背包问题，制作不同种类的面包
 *    - Codeforces 148E. Porcelain - https://codeforces.com/problemset/problem/148/E
 *      分组背包问题，从每组中选择物品
 *    - Codeforces 455A. Boredom - https://codeforces.com/problemset/problem/455/A
 *      打家劫舍类型的动态规划问题
 * 
 * 6. AtCoder：
 *    - AtCoder ABC032 D. ナップサック問題 - https://atcoder.jp/contests/abc032/tasks/abc032_d
 *      01背包问题，数据规模较大需要优化
 *    - AtCoder ABC153 F. Silver Fox vs Monster - https://atcoder.jp/contests/abc153/tasks/abc153_f
 *      贪心+前缀和优化的背包问题
 *    - AtCoder ABC224 E. Integers on Grid - https://atcoder.jp/contests/abc224/tasks/abc224_e
 *      动态规划与背包思想结合
 * 
 * 7. SPOJ：
 *    - SPOJ KNAPSACK - https://www.spoj.com/problems/KNAPSACK/
 *      经典01背包问题
 *    - SPOJ COINS - https://www.spoj.com/problems/COINS/
 *      硬币问题，完全背包的变形
 * 
 * 8. UVa OJ：
 *    - UVa 562. Dividing coins - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=503
 *      01背包变形，公平分配硬币
 *    - UVa 10130. SuperSale - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1071
 *      01背包问题的简单应用
 * 
 * 9. ZOJ：
 *    - ZOJ 2136. Longest Ordered Subsequence - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364779
 *      最长递增子序列，可转化为背包思想
 *    - ZOJ 1002. Fire Net - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364501
 *      回溯法与背包思想结合
 * 
 * 10. 牛客网：
 *     - NC19754. 多重背包 - https://ac.nowcoder.com/acm/problem/19754
 *       标准多重背包问题
 *     - NC16552. 买苹果 - https://ac.nowcoder.com/acm/problem/16552
 *       完全背包问题
 * 
 * 11. AcWing：
 *     - AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
 *       二进制优化的多重背包问题标准题目
 * 
 * 12. 剑指Offer：
 *     - 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
 *       动态规划基础问题
 *     - 剑指 Offer 10- II. 青蛙跳台阶问题 - https://leetcode.cn/problems/qing-wa-tiao-tai-jie-wen-ti-lcof/
 *       动态规划基础问题
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 多重背包问题的基础实现类
 * 
 * 注意事项：
 * 1. 输入数据规模较大时，使用Scanner可能会导致超时，因此使用BufferedReader进行输入
 * 2. 输出使用PrintWriter以提高效率
 * 3. 数组下标从1开始，便于理解动态规划的状态转移过程
 * 4. 代码采用清晰的模块化结构，便于维护和扩展
 */
public class Code01_BoundedKnapsack {
    
    /** 物品数量的最大可能值 */
    public static final int MAXN = 101;

    /** 背包容量的最大可能值 */
    public static final int MAXW = 40001;

    /** 物品价值数组：v[i]表示第i个物品的价值 */
    public static int[] v = new int[MAXN];

    /** 物品重量数组：w[i]表示第i个物品的重量 */
    public static int[] w = new int[MAXN];

    /** 物品数量数组：c[i]表示第i个物品的可用数量 */
    public static int[] c = new int[MAXN];

    /** 动态规划数组：dp[j]表示背包容量为j时的最大价值 */
    public static int[] dp = new int[MAXW];

    /** 物品数量 */
    public static int n;
    
    /** 背包容量 */
    public static int t;

    /**
     * 主方法
     * 处理输入、调用计算方法、输出结果
     * 
     * 工程化考量：
     * 1. 使用BufferedReader进行高效的输入处理，避免StreamTokenizer的复杂性
     * 2. 使用PrintWriter进行高效的输出处理
     * 3. 确保输入输出流被正确关闭，防止资源泄露
     * 4. 支持多组测试用例的连续读取
     * 5. 使用try-with-resources自动关闭资源，提高代码健壮性
     * 
     * @param args 命令行参数（未使用）
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 使用try-with-resources自动关闭资源
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out))) {
            
            String line;
            // 循环读取多组测试用例
            while ((line = br.readLine()) != null) {
                // 跳过空行
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.trim().split("\s+");
                int idx = 0;
                n = Integer.parseInt(parts[idx++]);
                t = Integer.parseInt(parts[idx++]);
                
                // 读取每个物品的价值、重量和数量
                for (int i = 1; i <= n; i++) {
                    line = br.readLine();
                    while (line != null && line.trim().isEmpty()) {
                        line = br.readLine(); // 跳过空行
                    }
                    if (line == null) break;
                    
                    String[] itemParts = line.trim().split("\s+");
                    v[i] = Integer.parseInt(itemParts[0]);
                    w[i] = Integer.parseInt(itemParts[1]);
                    c[i] = Integer.parseInt(itemParts[2]);
                }
                
                // 每次计算前清空dp数组，避免多组测试用例间的影响
                Arrays.fill(dp, 0, t + 1, 0);
                
                // 调用求解方法并输出结果
                out.println(compute2());
            }
            
            // 刷新输出，确保所有内容都被写入
            out.flush();
        }
    }

    /**
     * 严格位置依赖的动态规划实现
     * 使用二维数组存储状态
     * 
     * 算法思路：
     * 1. dp[i][j]表示前i种物品，背包容量为j时的最大价值
     * 2. 对于每个物品，可以选择不选或者选k个（1<=k<=c[i]且k*w[i]<=j）
     * 3. 状态转移方程：dp[i][j] = max(dp[i][j], dp[i-1][j-k*w[i]] + k*v[i])
     * 
     * 时间复杂度分析：
     * O(n * t * k_avg)，其中n是物品数量，t是背包容量，k_avg是每种物品的平均数量
     * 在最坏情况下（每种物品数量都很大），时间复杂度可能达到O(n * t^2)
     * 
     * 空间复杂度分析：
     * O(n * t)，使用二维数组存储所有状态
     * 
     * 优化思路：
     * 1. 可以提前计算每种物品的最大可选择数量，避免无效循环
     * 2. 对于重量为0的物品（如果允许的话），可以特殊处理
     * 3. 对于价值为0的物品，可以直接跳过，因为选择它们不会增加总价值
     * 
     * @return 背包能装下的最大价值
     */
    public static int compute1() {
        // dp[0][....] = 0，表示没有货物的情况下，背包容量不管是多少，最大价值都是0
        int[][] dp = new int[n + 1][t + 1];
        
        // 枚举前i种物品
        for (int i = 1; i <= n; i++) {
            int vi = v[i]; // 当前物品价值
            int wi = w[i]; // 当前物品重量
            int ci = c[i]; // 当前物品数量
            
            // 优化：跳过价值为0的物品
            if (vi == 0) continue;
            
            // 优化：跳过重量超过背包容量的物品
            if (wi > t) continue;
            
            // 枚举背包容量j
            for (int j = 0; j <= t; j++) {
                // 初始状态：不选第i种物品，继承前i-1种物品的最大价值
                dp[i][j] = dp[i - 1][j];
                
                // 计算当前容量下最多能选多少个该物品
                int maxK = Math.min(ci, j / wi);
                
                // 枚举选择第i种物品的数量k（1到maxK个）
                for (int k = 1; k <= maxK; k++) {
                    // 状态转移：选择k个第i种物品，那么剩余容量为j - k*wi，价值增加k*vi
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - k * wi] + k * vi);
                }
            }
        }
        
        // 返回所有物品、背包容量为t时的最大价值
        return dp[n][t];
    }

    /**
     * 空间优化的动态规划实现
     * 使用一维数组存储状态，逆序遍历背包容量
     * 
     * 算法思路：
     * 1. dp[j]表示背包容量为j时的最大价值
     * 2. 逆序遍历背包容量，确保每个物品只能被选择有限次数
     * 3. 枚举每种物品选择的数量，更新状态
     * 
     * 时间复杂度分析：
     * O(n * t * k_avg)，与compute1相同
     * 注意：部分测试用例可能超时，因为没有对枚举进行优化
     * 
     * 空间复杂度分析：
     * O(t)，只需要一维数组存储状态，大幅降低了空间消耗
     * 
     * 核心优化：
     * 1. 使用一维数组替代二维数组，减少空间占用
     * 2. 逆序遍历背包容量，确保每种物品只能被选择有限次数
     * 3. 提前计算maxK，避免重复计算
     * 4. 添加多重剪枝条件，跳过无效物品
     * 
     * @return 背包能装下的最大价值
     */
    public static int compute2() {
        // 枚举每种物品
        for (int i = 1; i <= n; i++) {
            int vi = v[i]; // 当前物品价值
            int wi = w[i]; // 当前物品重量
            int ci = c[i]; // 当前物品数量
            
            // 优化1：跳过价值为0的物品
            if (vi == 0) continue;
            
            // 优化2：跳过重量为0且数量无限的物品（理论上可以无限取，但题目通常不会出现）
            if (wi == 0 && ci >= Integer.MAX_VALUE) continue;
            
            // 优化3：跳过重量超过背包容量的物品
            if (wi > t) continue;
            
            // 优化4：跳过数量为0的物品
            if (ci == 0) continue;
            
            // 逆序枚举背包容量，避免物品被重复选择
            for (int j = t; j >= wi; j--) { // 从wi开始，因为j < wi时无法选择该物品
                // 计算当前容量下最多能选多少个该物品
                int maxK = Math.min(ci, j / wi);
                
                // 枚举选择当前物品的数量k（1到maxK个）
                for (int k = 1; k <= maxK; k++) {
                    int prevJ = j - k * wi;
                    // 状态转移：选择k个第i种物品，那么剩余容量为prevJ，价值增加k*vi
                    if (dp[prevJ] + k * vi > dp[j]) {
                        dp[j] = dp[prevJ] + k * vi;
                    }
                }
            }
        }
        
        // 返回背包容量为t时的最大价值
        return dp[t];
    }
    
    /**
     * 算法详解与原理解析
     * 
     * 1. 问题建模：
     *    - 每种物品是一种资源，有价值、重量和数量限制
     *    - 背包容量是资源约束
     *    - 目标是在约束条件下最大化总价值
     * 
     * 2. 状态定义：
     *    - 二维DP：dp[i][j]表示前i种物品，背包容量为j时的最大价值
     *    - 一维DP：dp[j]表示背包容量为j时的最大价值
     * 
     * 3. 状态转移方程推导：
     *    对于第i种物品，我们可以选择0到c[i]个中的任意数量
     *    dp[i][j] = max{ dp[i-1][j - k*w[i]] + k*v[i] }, 其中0 ≤ k ≤ min(c[i], j/w[i])
     *    
     *    一维优化后：
     *    dp[j] = max{ dp[j - k*w[i]] + k*v[i] }, 其中1 ≤ k ≤ min(c[i], j/w[i])
     *    （从后向前遍历j，确保每种物品只能选有限次数）
     * 
     * 4. 边界条件：
     *    - dp[0][j] = 0（没有物品可选时，任何容量的最大价值都是0）
     *    - dp[i][0] = 0（背包容量为0时，无法装任何物品，价值为0）
     *    - dp[0] = 0（一维DP的初始状态）
     */
    
    /**
     * 代码优化与工程化考量
     * 
     * 1. 输入优化：
     *    - 使用BufferedReader替代StreamTokenizer，代码更简洁且维护性更好
     *    - 处理多组测试用例时，注意跳过空行
     *    - 使用try-with-resources自动关闭资源，防止资源泄露
     * 
     * 2. 算法优化：
     *    - 提前剪枝：跳过价值为0、重量超过容量或数量为0的物品
     *    - 计算maxK，避免重复计算j/w[i]和比较c[i]
     *    - 从wi开始遍历j，减少无效循环
     *    - 使用局部变量缓存v[i]、w[i]、c[i]，减少数组访问
     * 
     * 3. 代码健壮性：
     *    - 处理各种边界情况：n=0、t=0、物品重量或价值为0等
     *    - 避免除零错误（虽然题目通常保证w[i]>0）
     *    - 处理可能的整数溢出问题
     * 
     * 4. 性能优化：
     *    - 使用一维数组替代二维数组，减少内存占用和缓存未命中率
     *    - 逆序遍历j，确保状态转移的正确性
     *    - 优化循环顺序，提高缓存局部性
     */
    
    /**
     * 多重背包问题的高级优化方法
     * 
     * 1. 二进制优化：
     *    - 思路：将数量为c[i]的物品拆分为log(c[i])个物品组
     *    - 每组代表2^k个该物品，转化为01背包问题
     *    - 时间复杂度：O(n * t * log c[i])
     *    - 实现简单，适用范围广
     *    
     * 2. 单调队列优化：
     *    - 思路：利用同余分组和单调队列维护最优状态
     *    - 时间复杂度：O(n * t)
     *    - 实现较复杂，但效率最高
     *    - 适合大规模数据
     *    
     * 3. 完全背包优化：
     *    - 当c[i] * w[i] >= t时，可以将物品视为完全背包
     *    - 时间复杂度：O(n * t)
     *    - 可以结合其他优化方法使用
     */
    
    /**
     * 边界情况分析：
     * 1. 当n=0（没有物品）时，最大价值为0
     * 2. 当t=0（背包容量为0）时，最大价值为0
     * 3. 当所有物品的重量都大于t时，无法装入任何物品，最大价值为0
     * 4. 当所有物品的价值都为0时，最大价值为0
     * 5. 当物品重量为0且价值为正数时，如果数量无限则可以无限选（但题目通常不会出现）
     */
    
    /**
     * 工程应用场景：
     * 1. 资源分配问题：在有限资源约束下实现收益最大化
     * 2. 投资组合优化：选择多种投资产品，在风险和收益之间取得平衡
     * 3. 生产计划制定：安排不同产品的生产数量，最大化利润
     * 4. 物流配送优化：在载重限制下选择最优配送方案
     * 5. 项目选择问题：在预算和时间约束下选择最优项目组合
     * 6. 广告投放优化：在预算限制下选择最优广告组合以最大化转化率
     */
    
    /**
     * 代码调试与测试建议：
     * 1. 小数据测试：使用简单的测试用例验证算法正确性
     * 2. 边界测试：测试n=0、t=0、物品重量或价值为0等边界情况
     * 3. 性能测试：对于大数据集，可以比较不同优化方法的性能差异
     * 4. 调试技巧：添加中间状态输出，观察dp数组的变化过程
     */
    
    /**
     * 算法学习建议：
     * 1. 先掌握01背包和完全背包的基础实现
     * 2. 理解多重背包问题的本质和状态转移过程
     * 3. 学习二进制优化和单调队列优化的原理
     * 4. 尝试解决各种变形问题，加深理解
     * 5. 对比不同背包问题的异同，建立知识体系
     */
    
    /**
     * 面试要点：
     * 1. 能够清晰解释多重背包问题的状态定义和转移方程
     * 2. 了解常见的优化方法（二进制、单调队列等）
     * 3. 能够分析算法的时间复杂度和空间复杂度
     * 4. 能够处理各种边界情况
     * 5. 能够将背包问题思想应用到实际场景中
     */
}
package class075;

/**
 * 完全背包问题实现类
 * 
 * 问题描述：
 * 有一个容量为t的背包，共有n种物品
 * 每种物品i有以下属性：
 * - 价值v[i]
 * - 重量w[i]
 * 每种物品可以选择任意次数（0次或多次）
 * 要求在不超过背包容量的前提下，选择物品使得总价值最大
 * 
 * 算法分类：动态规划 - 完全背包问题
 * 
 * 算法原理：
 * 1. 状态定义：dp[j]表示背包容量为j时的最大价值
 * 2. 状态转移方程：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
 * 3. 遍历顺序：与01背包不同，完全背包需要正序遍历背包容量，允许物品被多次选择
 * 
 * 时间复杂度：O(n*t)
 * 空间复杂度：O(t)
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1616（宝物筛选扩展问题）
 * 
 * 实现特点：
 * 1. 使用一维DP数组进行空间优化
 * 2. 采用正序遍历背包容量，允许物品被多次选择
 * 3. 使用高效的IO处理，适用于大规模数据
 */

/*
 * 相关题目扩展（各大算法平台）：
 * 1. LeetCode（力扣）：
 *    - 322. Coin Change - https://leetcode.cn/problems/coin-change/
 *      完全背包问题，求组成金额所需的最少硬币数
 *    - 518. Coin Change II - https://leetcode.cn/problems/coin-change-ii/
 *      完全背包计数问题，求组成金额的方案数
 *    - 377. Combination Sum IV - https://leetcode.cn/problems/combination-sum-iv/
 *      顺序相关的组合问题，类似完全背包
 * 
 * 2. 洛谷（Luogu）：
 *    - P1616. 疯狂的采药 - https://www.luogu.com.cn/problem/P1616
 *      经典完全背包问题，数据规模较大
 *    - P1679. 神奇的四次方数 - https://www.luogu.com.cn/problem/P1679
 *      完全背包在数学问题中的应用
 * 
 * 3. POJ：
 *    - POJ 1114. Piggy-Bank - http://poj.org/problem?id=1114
 *      完全背包问题，求装满背包的最小价值
 *    - POJ 2063. Investment - http://poj.org/problem?id=2063
 *      完全背包问题的实际应用
 * 
 * 4. HDU：
 *    - HDU 1114. Piggy-Bank - http://acm.hdu.edu.cn/showproblem.php?pid=1114
 *      完全背包问题，求装满背包的最小价值
 *    - HDU 2159. FATE - http://acm.hdu.edu.cn/showproblem.php?pid=2159
 *      二维费用背包问题，同时考虑忍耐度和杀怪数
 * 
 * 5. Codeforces：
 *    - Codeforces 148E. Porcelain - https://codeforces.com/problemset/problem/148/E
 *      分组背包问题，从每组中选择物品
 * 
 * 6. AtCoder：
 *    - AtCoder DP Contest E - Knapsack 2 - https://atcoder.jp/contests/dp/tasks/dp_e
 *      大体积小价值的01背包问题，需要价值维度DP
 * 
 * 7. SPOJ：
 *    - SPOJ COINS - https://www.spoj.com/problems/COINS/
 *      硬币问题，完全背包的变形
 * 
 * 8. 牛客网：
 *    - NC16552. 买苹果 - https://ac.nowcoder.com/acm/problem/16552
 *      完全背包问题
 *    - NC242214 买饮料 - https://ac.nowcoder.com/acm/problem/242214
 *      多重背包变形
 * 
 * 9. UVa OJ：
 *    - UVa 10130. SuperSale - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1071
 *      01背包问题的简单应用
 * 
 * 10. AcWing：
 *     - AcWing 3. 完全背包问题 - https://www.acwing.com/problem/content/3/
 *       标准完全背包问题
 */

import java.io.*;

/**
 * 完全背包问题的标准实现类
 * 
 * 技术要点：
 * 1. 使用一维DP数组进行空间优化
 * 2. 通过正序遍历背包容量实现完全背包逻辑
 * 3. 采用高效的输入输出方式处理大数据量
 */
public class Code03_UnboundedKnapsack {

    /** 物品数量的最大可能值 */
    public static int MAXN = 1001;

    /** 背包容量的最大可能值 */
    public static int MAXW = 10000001;

    /** 物品价值数组：v[i]表示第i个物品的价值 */
    public static int[] v = new int[MAXN];

    /** 物品重量数组：w[i]表示第i个物品的重量 */
    public static int[] w = new int[MAXN];

    /** 动态规划数组：dp[j]表示背包容量为j时的最大价值 */
    public static int[] dp = new int[MAXW];

    /** 物品数量 */
    public static int n;
    
    /** 背包容量 */
    public static int t;

    /**
     * 主方法
     * 处理输入、调用计算逻辑、输出结果
     * 
     * 工程化考量：
     * 1. 使用BufferedReader和StreamTokenizer进行高效的输入处理
     * 2. 使用PrintWriter进行高效的输出处理
     * 3. 确保输入输出流被正确关闭，防止资源泄露
     * 4. 直接在主方法中实现算法核心逻辑，简洁高效
     * 
     * @param args 命令行参数（未使用）
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化输入流，使用BufferedReader和StreamTokenizer提高读取效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        // 初始化输出流，使用PrintWriter提高写入效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取物品数量和背包容量
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        t = (int) in.nval;
        
        // 读取每个物品的重量和价值
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            w[i] = (int) in.nval;
            in.nextToken();
            v[i] = (int) in.nval;
        }
        
        // 完全背包算法核心逻辑
        // 遍历每个物品
        for (int i = 1; i <= n; i++) {
            // 正序遍历背包容量（与01背包的逆序遍历不同）
            // 这样可以确保同一个物品被多次选择
            for (int j = w[i]; j <= t; j++) {
                // 状态转移方程：考虑选择当前物品或不选择当前物品
                // 如果选择当前物品，则容量减少w[i]，价值增加v[i]
                dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
            }
        }
        
        // 输出结果
        out.println(dp[t]);
        
        // 刷新并关闭流，确保输出全部写入
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 完全背包问题的另一种实现方法
     * 使用二维DP数组，更直观地展示状态转移过程
     * 
     * @return 背包能够装下的最大价值
     */
    public static int unboundedKnapsack2D() {
        // 创建二维DP数组
        int[][] dp = new int[n + 1][t + 1];
        
        // 遍历每个物品
        for (int i = 1; i <= n; i++) {
            // 遍历每个可能的背包容量
            for (int j = 0; j <= t; j++) {
                // 不选择当前物品的情况
                dp[i][j] = dp[i - 1][j];
                
                // 选择当前物品的情况（如果容量足够）
                if (j >= w[i]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][j - w[i]] + v[i]);
                }
            }
        }
        
        return dp[n][t];
    }
    
    /**
     * 算法详解与扩展
     * 
     * 1. 完全背包与01背包的区别：
     *    - 01背包：每种物品只能选一次
     *    - 完全背包：每种物品可以选任意次数
     *    - 实现区别：01背包逆序遍历容量，完全背包正序遍历容量
     * 
     * 2. 算法正确性分析：
     *    - 对于完全背包，正序遍历容量j时，dp[j-w[i]]已经包含了之前选择该物品的情况
     *    - 因此，通过正序遍历，可以自然地实现物品的多次选择
     *    - 例如，当j增加到j+w[i]时，又可以再次选择该物品
     * 
     * 3. 优化可能性：
     *    - 对于重量很大的物品，可以提前剪枝
     *    - 对于价值为0的物品，可以直接跳过
     *    - 对于相同重量的物品，可以只保留价值最高的那个
     *    - 当数据规模非常大时，可以考虑使用更高效的动态规划优化技术
     * 
     * 4. 相关问题扩展：
     *    - 多重背包：每种物品有固定数量限制
     *    - 分组背包：物品分成若干组，每组只能选一个
     *    - 二维费用背包：每个物品有两种费用（如重量和体积）
     *    - 有依赖关系的背包：物品之间存在依赖关系
     */
    
    /**
     * 工程应用考量：
     * 
     * 1. 数据规模处理：
     *    - 当背包容量非常大时（如本题中MAXW=1e7），需要注意内存使用
     *    - 可以考虑使用滚动数组或仅保留必要的状态
     * 
     * 2. 代码健壮性：
     *    - 应处理物品重量为0的特殊情况（如果允许）
     *    - 应处理重量大于背包容量的物品（可以跳过）
     *    - 在处理大数值时，需要考虑整数溢出问题
     * 
     * 3. 性能优化：
     *    - 预处理物品，去除无效物品（如价值为0的物品）
     *    - 对于特殊物品（如重量相同的物品）进行合并
     *    - 使用更高效的输入输出方法处理大规模数据
     * 
     * 4. 实际应用场景：
     *    - 资源分配问题
     *    - 生产计划问题
     *    - 投资组合优化
     *    - 货物装载问题
     *    - 数据压缩算法
     */
}
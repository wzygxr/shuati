package class075;

/**
 * LeetCode 879. Profitable Schemes 问题的解决方案
 * 
 * 问题描述：
 * 集团里有 n 名员工，他们可以完成各种各样的工作创造利润
 * 第 i 种工作会产生 profit[i] 的利润，要求 group[i] 名成员共同参与
 * 成员参与其中一项工作后，就不能参与另一项工作
 * 任何至少产生 minProfit 利润且成员总数不超过 n 的工作子集称为盈利计划
 * 要求计算有多少种这样的盈利计划，结果模 10^9 + 7
 * 
 * 算法分类：动态规划 - 二维费用背包问题（计数类）
 * 
 * 算法原理：
 * 1. 将每个工作视为一个物品
 * 2. 每个物品有一个"重量"属性：所需人数
 * 3. 每个物品有一个"价值"属性：产生的利润
 * 4. 背包有两个限制：总人数不超过n，总利润至少为minProfit
 * 5. 目标是计算满足条件的选法数目
 * 
 * 时间复杂度：O(G * n * minProfit)，其中 G 是工作数量
 * 空间复杂度：O(n * minProfit)
 * 
 * 测试链接：https://leetcode.cn/problems/profitable-schemes/
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
 * 
 * 2. 洛谷（Luogu）：
 *    - P1833 樱花 - https://www.luogu.com.cn/problem/P1833
 *      混合背包问题，包含01背包、完全背包和多重背包
 *    - P1757 通天之分组背包 - https://www.luogu.com.cn/problem/P1757
 *      分组背包问题
 * 
 * 3. POJ：
 *    - POJ 1742. Coins - http://poj.org/problem?id=1742
 *      多重背包可行性问题，计算能组成多少种金额
 *    - POJ 1276. Cash Machine - http://poj.org/problem?id=1276
 *      多重背包优化问题，使用二进制优化或单调队列优化
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
 * 
 * 6. AtCoder：
 *    - AtCoder DP Contest Problem F - https://atcoder.jp/contests/dp/tasks/dp_f
 *      最长公共子序列与背包思想的结合
 * 
 * 7. 牛客网：
 *    - NC19754. 多重背包 - https://ac.nowcoder.com/acm/problem/19754
 *      标准多重背包问题
 * 
 * 8. AcWing：
 *    - AcWing 8. 二维费用的背包问题 - https://www.acwing.com/problem/content/8/
 *      标准二维费用背包问题
 * 
 * 9. UVa OJ：
 *    - UVa 562. Dividing coins - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=503
 *      01背包变形，公平分配硬币
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 二维费用背包问题的计数实现
 * 
 * 技术要点：
 * 1. 使用二维DP数组记录状态，优化存储空间
 * 2. 针对利润维度的优化处理，避免不必要的计算
 * 3. 采用模运算处理大数问题
 * 4. 从后往前遍历确保每个物品只被选择一次
 */
public class Code07_ProfitableSchemes {

    /** 模数，用于处理大数问题 */
    public static int MOD = 1000000007;
    /** 最大可能的员工数量 */
    public static int MAXN = 101;
    /** 最大可能的最小利润要求 */
    public static int MAXP = 101;
    
    /** 
     * 动态规划数组
     * dp[i][j] 表示使用i个员工，至少获得j利润的方案数
     */
    public static int[][] dp = new int[MAXN][MAXP];
    
    /**
     * 主方法
     * 处理输入、调用计算逻辑、输出结果
     * 
     * 工程化考量：
     * 1. 使用BufferedReader进行高效的输入读取
     * 2. 使用PrintWriter进行高效的输出写入
     * 3. 确保输入输出流被正确关闭，防止资源泄露
     * 4. 支持多种输入格式，提高代码的通用性
     * 
     * @param args 命令行参数（未使用）
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化输入输出流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入参数
        int n = Integer.parseInt(br.readLine());
        int minProfit = Integer.parseInt(br.readLine());
        int len = Integer.parseInt(br.readLine());
        
        // 读取工作所需人数和利润数组
        int[] group = new int[len];
        int[] profit = new int[len];
        
        String[] groupStr = br.readLine().split(" ");
        String[] profitStr = br.readLine().split(" ");
        
        for (int i = 0; i < len; i++) {
            group[i] = Integer.parseInt(groupStr[i]);
            profit[i] = Integer.parseInt(profitStr[i]);
        }
        
        // 计算并输出结果
        out.println(profitableSchemes(n, minProfit, group, profit));
        
        // 刷新并关闭流
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 核心算法实现：计算盈利计划的数量
     * 
     * @param n 可用员工总数
     * @param minProfit 最低利润要求
     * @param group 每个工作所需的员工数
     * @param profit 每个工作产生的利润
     * @return 满足条件的盈利计划数量，模10^9+7
     */
    public static int profitableSchemes(int n, int minProfit, int[] group, int[] profit) {
        // 初始化dp数组
        // dp[i][j] 表示使用i个员工，至少获得j利润的方案数
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= minProfit; j++) {
                dp[i][j] = 0;
            }
        }
        
        // 初始状态：不使用任何员工，获得0利润的方案数为1
        dp[0][0] = 1;
        
        // 遍历每个工作（物品）
        for (int k = 0; k < group.length; k++) {
            int g = group[k];  // 当前工作需要的人数
            int p = profit[k]; // 当前工作产生的利润
            
            // 剪枝优化：如果工作所需人数超过总人数，跳过该工作
            if (g > n) {
                continue;
            }
            
            // 从后往前更新dp数组（01背包空间优化的关键）
            // 从大到小遍历，确保每个工作只被选择一次
            for (int i = n; i >= g; i--) {
                // 注意利润维度的处理，这里j从0开始，而不是从minProfit开始
                // 因为利润可以叠加，且超过minProfit的部分可以合并处理
                for (int j = minProfit; j >= 0; j--) {
                    // 状态转移方程：
                    // 不选择当前工作：dp[i][j] 保持不变
                    // 选择当前工作：dp[i-g][j] 的方案数可以转移到 dp[i][min(j+p, minProfit)]
                    // 这里的min(j+p, minProfit)是关键优化：当利润超过minProfit时，都视为达到要求
                    int newProfit = Math.min(j + p, minProfit);
                    dp[i][newProfit] = (dp[i][newProfit] + dp[i - g][j]) % MOD;
                }
            }
        }
        
        // 计算结果：所有使用不超过n个员工且获得至少minProfit利润的方案数之和
        int result = 0;
        for (int i = 0; i <= n; i++) {
            result = (result + dp[i][minProfit]) % MOD;
        }
        
        return result;
    }
    
    /**
     * 算法详解与原理解析
     * 
     * 1. 问题建模：
     *    - 每个工作是一个物品，必须选择整个物品（01背包特性）
     *    - 物品的"重量"是所需员工数量
     *    - 物品的"价值"是产生的利润
     *    - 背包容量是员工总数n
     *    - 额外约束是总利润至少为minProfit
     *    - 目标是计算满足条件的选法数目（而非最大化利润）
     * 
     * 2. 状态定义：
     *    - dp[i][j]表示使用i个员工，至少获得j利润的方案数
     *    - 注意这里定义的是"至少"j利润，而不是"恰好"j利润，这是一个重要的优化
     * 
     * 3. 状态转移：
     *    - 对于每个工作，我们有两种选择：选或不选
     *    - 不选：dp[i][j] 保持原值
     *    - 选：dp[i-g][j] 的方案数可以转移到 dp[i][min(j+p, minProfit)]
     *    - 使用min(j+p, minProfit)将超过minProfit的情况合并处理，减少状态数
     *    - 结果需要模10^9+7以避免溢出
     * 
     * 4. 初始化：
     *    - dp[0][0] = 1，表示不使用任何员工，获得0利润的方案数为1
     *    - 其他初始值为0
     * 
     * 5. 结果计算：
     *    - 所有使用0到n个员工且获得至少minProfit利润的方案数之和
     */
    
    /**
     * 工程化考量与代码优化
     * 
     * 1. 空间优化：
     *    - 可以进一步将二维数组优化为一维数组，但为了代码清晰性保留了二维结构
     *    - 对于较大的数据规模，可以考虑使用滚动数组或仅保留必要的状态
     * 
     * 2. 性能优化：
     *    - 添加剪枝逻辑，跳过所需人数超过总人数的工作
     *    - 利润维度的合并处理（使用min(j+p, minProfit)）减少了状态数
     *    - 从后往前遍历确保每个物品只被选择一次
     * 
     * 3. 代码健壮性：
     *    - 没有对输入进行严格校验，实际应用中应添加参数检查
     *    - 没有特别处理极端情况（如minProfit=0、n=0等）
     *    - 使用模运算防止整数溢出
     * 
     * 4. 可扩展性：
     *    - 代码结构清晰，可以轻松修改为求解其他类型的计数背包问题
     *    - 只需调整状态转移方程即可适应不同的问题需求
     */
    
    /**
     * 相关题目扩展与算法变种
     * 
     * 1. LeetCode 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
     *    二维01背包求最大物品数量
     * 
     * 2. LeetCode 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
     *    当前问题，二维费用背包求方案数
     * 
     * 3. POJ 1742. Coins - http://poj.org/problem?id=1742
     *    多重背包可行性问题，计算能组成多少种金额
     * 
     * 4. 扩展到三维背包：
     *    当需要考虑更多维度的限制时（如时间、资源等），可以扩展到三维或更高维的背包问题
     *    处理方式类似，但需要更多的嵌套循环和更大的内存空间
     * 
     * 5. 恰好利润问题：
     *    如果要求利润恰好为minProfit，状态定义和转移方程需要相应调整
     */
}
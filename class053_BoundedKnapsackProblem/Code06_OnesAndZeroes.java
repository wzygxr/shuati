package class075;

/**
 * LeetCode 474. Ones and Zeroes 问题的解决方案
 * 
 * 问题描述：
 * 给定一个二进制字符串数组 strs 和两个整数 m 和 n
 * 找出并返回 strs 的最大子集的长度，该子集中最多有 m 个 0 和 n 个 1
 * 
 * 算法分类：动态规划 - 多维背包问题（二维费用01背包）
 * 
 * 算法原理：
 * 1. 将每个字符串视为一个物品
 * 2. 每个物品需要消耗两种资源：0的数量和1的数量
 * 3. 背包容量是 m 个 0 和 n 个 1
 * 4. 目标是选择最多数量的物品（字符串）
 * 
 * 时间复杂度：O(s * m * n)，其中 s 是字符串数组的长度
 * 空间复杂度：O(m * n)，使用一维数组进行空间优化
 * 
 * 测试链接：https://leetcode.cn/problems/ones-and-zeroes/
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
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 二维费用01背包问题的经典实现
 * 
 * 技术要点：
 * 1. 使用一维数组表示二维DP状态，通过一维索引映射二维坐标
 * 2. 采用从后往前的遍历方式确保每个物品只被选择一次
 * 3. 预处理每个字符串的0和1数量，避免重复计算
 */
public class Code06_OnesAndZeroes {

    /** 最多允许的0的数量上限 */
    public static int MAXM = 101;
    /** 最多允许的1的数量上限 */
    public static int MAXN = 101;
    
    /** 
     * 动态规划数组，使用一维数组模拟二维数组
     * dp[i*MAXN + j] 表示使用i个0和j个1时能选择的最大字符串数量
     */
    public static int[] dp = new int[MAXM * MAXN];
    
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
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        int s = Integer.parseInt(br.readLine());
        String[] strs = new String[s];
        for (int i = 0; i < s; i++) {
            strs[i] = br.readLine();
        }
        String[] line = br.readLine().split(" ");
        int m = Integer.parseInt(line[0]);
        int n = Integer.parseInt(line[1]);
        
        // 计算并输出结果
        out.println(findMaxForm(strs, m, n));
        
        // 刷新并关闭流
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 核心算法实现：求解最多可以选择的字符串数量
     * 
     * @param strs 二进制字符串数组
     * @param m 允许的最大0的数量
     * @param n 允许的最大1的数量
     * @return 最大子集的长度
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        // 初始化dp数组为0
        Arrays.fill(dp, 0);
        
        // 遍历每个字符串（物品）
        for (String str : strs) {
            // 预处理：统计当前字符串中0和1的数量
            int zeros = 0, ones = 0;
            for (char c : str.toCharArray()) {
                if (c == '0') zeros++;
                else ones++;
            }
            
            // 优化：如果字符串的0或1数量超过限制，直接跳过该字符串
            if (zeros > m || ones > n) {
                continue;
            }
            
            // 从后往前更新dp数组（01背包空间优化的关键）
            // 从大到小遍历，确保每个物品只被选择一次
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    // 状态转移方程：
                    // 选择当前字符串：dp[i-zeros][j-ones] + 1
                    // 不选择当前字符串：dp[i][j]
                    // 取两者的最大值
                    dp[i * MAXN + j] = Math.max(
                        dp[i * MAXN + j], 
                        dp[(i - zeros) * MAXN + (j - ones)] + 1
                    );
                }
            }
        }
        
        // 返回使用m个0和n个1时能选择的最大字符串数量
        return dp[m * MAXN + n];
    }
    
    /**
     * 算法详解与原理解析
     * 
     * 1. 问题建模：
     *    - 每个字符串是一个物品，必须选择整个物品（01背包特性）
     *    - 每个物品有两个"重量"属性：0的数量和1的数量
     *    - 背包有两个"容量"限制：m个0和n个1
     *    - 物品的"价值"是1（因为我们要最大化物品数量）
     * 
     * 2. 状态定义：
     *    - dp[i][j]表示使用i个0和j个1时能选择的最大字符串数量
     *    - 使用一维数组优化空间：dp[i*MAXN + j]
     * 
     * 3. 状态转移：
     *    - 对于每个字符串，我们有两种选择：选或不选
     *    - 不选：dp[i][j] = dp[i][j]（保持原值）
     *    - 选：dp[i][j] = dp[i-zeros][j-ones] + 1（加上当前字符串）
     *    - 我们取两者中的最大值
     * 
     * 4. 遍历顺序的重要性：
     *    - 必须从后往前遍历，这样可以保证每个物品只被选择一次
     *    - 如果从前往后遍历，同一个物品可能被多次选择（变成完全背包问题）
     */
    
    /**
     * 工程化考量与代码优化
     * 
     * 1. 空间优化：
     *    - 使用一维数组表示二维状态，节省内存
     *    - 通过i*MAXN + j的映射将二维坐标转换为一维索引
     *    - 时间复杂度不变，但空间复杂度从O(m*n)降低到O(m*n)（理论上相同，但实际实现更高效）
     * 
     * 2. 性能优化：
     *    - 预处理每个字符串的0和1数量，避免重复计算
     *    - 提前过滤掉无法使用的字符串（0或1数量超过限制的）
     *    - 使用Arrays.fill进行数组初始化，效率更高
     * 
     * 3. 代码健壮性：
     *    - 没有对输入进行严格校验，实际应用中应添加参数检查
     *    - 没有处理极端情况（如空数组、m或n为0等）
     * 
     * 4. 可扩展性：
     *    - 代码结构清晰，可以轻松修改为求解其他类型的二维背包问题
     *    - 只需调整状态转移方程即可适应不同的问题需求
     */
    
    /**
     * 相关题目扩展与算法变种
     * 
     * 1. LeetCode 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
     *    原始问题，二维01背包求最大物品数量
     * 
     * 2. LeetCode 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
     *    二维费用背包问题，需要同时考虑人数和利润
     * 
     * 3. POJ 1742. Coins - http://poj.org/problem?id=1742
     *    多重背包可行性问题，计算能组成多少种金额
     * 
     * 4. POJ 1276. Cash Machine - http://poj.org/problem?id=1276
     *    多重背包优化问题，使用二进制优化或单调队列优化
     * 
     * 5. 三维及以上背包问题：
     *    当需要考虑更多维度的限制时，可以扩展到三维或更高维的背包问题
     *    处理方式类似，但需要更多的嵌套循环和更大的内存空间
     */
}
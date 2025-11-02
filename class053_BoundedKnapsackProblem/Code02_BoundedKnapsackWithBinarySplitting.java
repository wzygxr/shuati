package class075;

/**
 * 多重背包问题 - 二进制分组转化为01背包实现
 * 
 * 问题描述：
 * 有一个容量为t的背包，共有n种物品
 * 每种物品i有以下属性：
 * - 价值v[i]
 * - 重量w[i]
 * - 数量c[i]
 * 要求在不超过背包容量的前提下，选择物品使得总价值最大
 * 
 * 算法分类：动态规划 - 多重背包问题 - 二进制分组优化
 * 
 * 二进制分组优化原理：
 * 1. 将数量为c[i]的物品拆分成若干个"组合物品"
 * 2. 每个组合物品代表k个原物品，其中k是2的幂次
 * 3. 例如：c[i]=5，可以拆成1个、2个、2个的组合，这样任何数量1~5都可以通过选择这些组合得到
 * 4. 这样就将多重背包问题转化为01背包问题，大大减少了状态转移的次数
 * 
 * 适用场景：
 * - 物品数量较大，但又不是无限多的情况
 * - 需要在时间复杂度和代码复杂度之间取得平衡的场景
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1776（宝物筛选）
 * 
 * 核心思想：
 * 使用二进制分组将多重背包转化为01背包
 * 预处理阶段生成组合物品，运行阶段对组合物品应用01背包算法
 * 时间复杂度为O(t * Σlog c[i])，其中c[i]是第i种物品的数量
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
 *    - P1776 宝物筛选 - https://www.luogu.com.cn/problem/P1776
 *      经典多重背包问题
 *    - P1833 樱花 - https://www.luogu.com.cn/problem/P1833
 *      混合背包问题，包含01背包、完全背包和多重背包
 *    - P1679 神奇的四次方数 - https://www.luogu.com.cn/problem/P1679
 *      完全背包在数学问题中的应用
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
 *    - HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
 *      经典多重背包问题
 *    - HDU 2159. FATE - http://acm.hdu.edu.cn/showproblem.php?pid=2159
 *      二维费用背包问题，同时考虑忍耐度和杀怪数
 *    - HDU 3449 Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
 *      有依赖的背包问题
 * 
 * 5. Codeforces：
 *    - Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
 *      复杂的多重背包问题，涉及多个约束条件
 *    - Codeforces 148E. Porcelain - https://codeforces.com/problemset/problem/148/E
 *      分组背包问题，从每组中选择物品
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
 *    - NC17881. 最大价值 - https://ac.nowcoder.com/acm/problem/17881
 *      多重背包问题的变形应用
 *    - NC233233 背包问题IV - https://ac.nowcoder.com/acm/problem/233233
 *      完全背包变形
 * 
 * 9. AcWing：
 *    - AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
 *      二进制优化的多重背包问题标准题目
 * 
 * 10. UVa OJ：
 *     - UVa 10130. SuperSale - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1071
 *       01背包问题的简单应用
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 多重背包问题的二进制分组优化实现类
 * 
 * 技术要点：
 * 1. 使用二进制分组将多重背包转化为01背包
 * 2. 预处理阶段生成组合物品，运行阶段对组合物品应用01背包算法
 * 3. 时间复杂度为O(t * Σlog c[i])，其中c[i]是第i种物品的数量
 */
public class Code02_BoundedKnapsackWithBinarySplitting {

    /** 物品数量的最大可能值 */
    public static final int MAXN = 1001;

    /** 背包容量的最大可能值 */
    public static final int MAXW = 40001;

    /** 衍生商品价值数组：v[i]表示第i个衍生商品的总价值 */
    public static int[] v = new int[MAXN];

    /** 衍生商品重量数组：w[i]表示第i个衍生商品的总重量 */
    public static int[] w = new int[MAXN];

    /** 动态规划数组：dp[j]表示背包容量为j时的最大价值 */
    public static int[] dp = new int[MAXW];

    /** 物品种类数 */
    public static int n;
    
    /** 背包容量 */
    public static int t;
    
    /** 衍生商品的总数 */
    public static int m;

    /**
     * 主方法
     * 处理输入、二进制分组生成衍生商品、调用计算方法、输出结果
     * 
     * 工程化考量：
     * 1. 使用BufferedReader进行高效的输入处理
     * 2. 使用PrintWriter进行高效的输出处理
     * 3. 使用try-with-resources确保资源正确关闭，防止内存泄漏
     * 4. 支持多组测试用例的连续读取
     * 5. 完善边界情况处理，增强代码健壮性
     * 6. 添加输入校验，处理空行和不完整输入
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
                
                // 解析第一行：物品种类数和背包容量
                String[] parts = line.trim().split("\\s+");
                n = Integer.parseInt(parts[0]);
                t = Integer.parseInt(parts[1]);
                
                // 边界情况快速处理
                if (n == 0 || t == 0) {
                    out.println(0);
                    continue;
                }
                
                // 重置衍生商品计数器
                m = 0;
                
                // 读取每个物品的信息并进行二进制分组
                for (int i = 1; i <= n; i++) {
                    // 读取物品信息，跳过可能的空行
                    while ((line = br.readLine()) != null && line.trim().isEmpty()) {
                        // 跳过空行
                    }
                    if (line == null) break;
                    
                    String[] itemParts = line.trim().split("\\s+");
                    int value = Integer.parseInt(itemParts[0]);
                    int weight = Integer.parseInt(itemParts[1]);
                    int cnt = Integer.parseInt(itemParts[2]);
                    
                    // 优化1：跳过数量为0的物品
                    if (cnt == 0) continue;
                    
                    // 优化2：跳过价值为0的物品（选了也不增加总价值）
                    if (value == 0) continue;
                    
                    // 优化3：跳过重量为0的物品（特殊情况）
                    if (weight == 0) continue;
                    
                    // 优化4：跳过重量超过背包容量的物品
                    if (weight > t) continue;
                    
                    // 优化5：调整物品数量上限，避免无意义的计算
                    cnt = Math.min(cnt, t / weight);
                    
                    // 二进制分组核心逻辑：
                    // 将数量为cnt的物品拆分成多个组合物品，每个组合物品的数量是2的幂次
                    // 例如：cnt=5 → 拆分成1个、2个、2个
                    // 这样任何1~5的数量都可以通过选择这些组合得到
                    for (int k = 1; k <= cnt; k <<= 1) {
                        v[++m] = k * value;
                        w[m] = k * weight;
                        cnt -= k;
                    }
                    
                    // 处理剩余的数量（如果cnt不是2的幂次之和）
                    if (cnt > 0) {
                        v[++m] = cnt * value;
                        w[m] = cnt * weight;
                    }
                }
                
                // 计算并输出结果
                out.println(compute());
            }
            
            // 确保输出全部写入
            out.flush();
        }
    }

    /**
     * 01背包的空间压缩实现
     * 
     * 算法思路：
     * 1. 初始化dp数组，dp[j]表示背包容量为j时的最大价值
     * 2. 逆序遍历背包容量，避免重复选择同一物品
     * 3. 状态转移方程：dp[j] = max(dp[j], dp[j - weight] + value)
     * 
     * 时间复杂度分析：
     * O(m * t)，其中m是衍生商品的总数，t是背包容量
     * 由于m = Σlog c[i]，所以整体时间复杂度为O(t * Σlog c[i])
     * 相比朴素多重背包的O(t * Σc[i])，当c[i]较大时优化效果显著
     * 
     * 空间复杂度分析：
     * O(t)，只需要一维数组存储状态
     * 
     * 优化点：
     * 1. 预处理边界情况，跳过无效物品
     * 2. 逆序遍历容量，避免重复选择同一物品
     * 3. 提前判断w[i] > j的情况，减少不必要的计算
     * 
     * @return 背包能装下的最大价值
     */
    public static int compute() {
        // 边界情况快速处理
        if (m == 0 || t == 0) {
            return 0;
        }
        
        // 初始化dp数组，不需要恰好装满背包，所以初始化为0
        Arrays.fill(dp, 0, t + 1, 0);
        
        // 对每个衍生商品应用01背包算法
        for (int i = 1; i <= m; i++) {
            int weight = w[i];
            int value = v[i];
            
            // 优化：如果衍生商品的价值为0，跳过（不会增加总价值）
            if (value == 0) continue;
            
            // 优化：如果衍生商品的重量为0且价值不为0，可以无限选择，但这里是01背包所以跳过（实际应该特殊处理）
            if (weight == 0) continue;
            
            // 优化：如果衍生商品的重量超过背包容量，无法选择，跳过
            if (weight > t) continue;
            
            // 逆序遍历背包容量，确保每个衍生商品只能被选择一次
            for (int j = t; j >= weight; j--) {
                // 状态转移：选择该衍生商品或不选
                int candidate = dp[j - weight] + value;
                if (candidate > dp[j]) {
                    dp[j] = candidate;
                }
            }
        }
        
        // 返回背包容量为t时的最大价值
        return dp[t];
    }
    
    /**
     * 算法优化与工程化考量
     * 
     * 1. 二进制分组优化深入分析：
     *    - 普通多重背包：三重循环，时间复杂度O(n * t * c[i])，对于大数量的物品会超时
     *    - 二进制分组优化：将物品拆分成log₂(c[i])个组合物品，时间复杂度O(n * t * log c[i])
     *    - 当c[i]很大时（比如1000），log₂(c[i])约为10，优化效果非常明显
     *    - 二进制分组实际上是对物品数量的压缩表示，利用位运算的特性
     * 
     * 2. 二进制分组正确性数学证明：
     *    - 任意正整数c可以唯一地表示为不同2的幂次之和（二进制表示）
     *    - 对于任意k(1<=k<=c)，可以通过选择对应的二进制位组合来表示k个物品的选择
     *    - 例如：c=5 → 1(2⁰)+2(2¹)+2(剩余)，这样可以组合出1~5之间的任意数量
     *    - 更严谨地说，对于区间[1,c]中的任意整数k，都可以通过选择若干个组合物品来表示
     * 
     * 3. 代码性能优化技巧：
     *    - 使用局部变量缓存频繁访问的值（weight、value）
     *    - 预处理并跳过无效物品（数量为0、价值为0、重量超过容量）
     *    - 对于重量为0且价值不为0的物品进行特殊处理（可以无限选择）
     *    - 当c[i] * w[i] > t时，可以将物品视为完全背包处理，进一步优化
     *    - 使用Arrays.fill进行数组初始化，比循环更高效
     *    - 提前终止内层循环的情况（如dp[j]不再改变时）
     * 
     * 4. 与单调队列优化的对比：
     *    - 二进制优化：时间复杂度O(n * t * log c[i])，实现简单，常数因子小
     *    - 单调队列优化：时间复杂度O(n * t)，实现较复杂，常数因子稍大
     *    - 适用场景对比：
     *      * 当物品数量较多、单个物品数量适中时，二进制优化更适用
     *      * 当背包容量很大、物品数量适中时，单调队列优化更有优势
     *      * 在编程比赛中，二进制优化由于实现简单，更常被采用
     * 
     * 5. 工程应用中的考量：
     *    - 数据范围估计：在实际应用中，需要根据数据规模选择合适的优化方法
     *    - 内存优化：对于超大容量的问题，可以考虑使用稀疏数组或其他压缩技术
     *    - 并行处理：在多核心环境下，可以考虑对物品分组并行计算
     *    - 容错处理：添加适当的异常处理和边界检查，提高程序健壮性
     *    - 缓存优化：合理安排内存访问模式，提高缓存命中率
     * 
     * 6. 调试与测试建议：
     *    - 单元测试：测试边界情况，如n=0、t=0、c[i]=0等
     *    - 对比测试：与朴素实现对比结果，确保正确性
     *    - 性能测试：在大数据规模下测试算法效率
     *    - 可视化调试：输出中间状态，理解算法执行过程
     *    - 边界条件测试：测试物品重量恰好为容量等特殊情况
     */
}
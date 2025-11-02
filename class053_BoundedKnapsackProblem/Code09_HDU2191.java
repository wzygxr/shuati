package class075;

/**
 * HDU 2191. 悼念512汶川大地震遇难同胞——珍惜现在，感恩生活
 * 
 * 问题描述：
 * 急需一批防灾帐篷和食品，急需灾区人民携手抗灾。
 * 作为全国最厉害的程序员，你急群众之所急，想群众之所想，决定用你的技术来帮助灾区人民。
 * 现在国家拨下了一定的资金，让你去购买救灾物资。
 * 为了使灾区人民能够得到更多的物资，你要合理地使用这笔资金。
 * 现在给你n种物品，每种物品有重量w[i]，价值v[i]，数量c[i]。
 * 你的背包容量为m，请问你最多能带走多少价值的物品？
 * 
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=2191
 * 提交说明：提交时请把类名改成"Main"，可以直接通过
 * 
 * 算法分类：动态规划 - 多重背包问题 - 二进制优化
 * 
 * 算法原理：
 * 1. 将多重背包问题转化为01背包问题
 * 2. 使用二进制分组技术将每种物品的数量拆分成多个二进制组合
 * 3. 每个二进制组合视为一个新的物品，使用01背包的方法求解
 * 
 * 时间复杂度分析：
 * 设物品种类数为N，背包容量为M，每种物品的最大数量为C
 * - 二进制优化后，物品种类数变为O(N*logC)
 * - 总体时间复杂度：O(M * N * logC)
 * - 空间复杂度：O(M + N*logC)
 * 
 * 实现特点：
 * 1. 使用二进制拆分优化多重背包
 * 2. 使用空间压缩的一维DP数组
 * 3. 高效的输入输出处理
 * 4. 支持多组测试用例
 */

/**
 * 相关题目扩展（各大算法平台）：
 * 1. LeetCode（力扣）：
 *    - 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
 *      多维01背包问题，每个字符串需要同时消耗0和1的数量
 *    - 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
 *      二维费用背包问题，需要同时考虑人数和利润
 *    - 322. Coin Change - https://leetcode.cn/problems/coin-change/
 *      完全背包问题，求组成金额所需的最少硬币数
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
 *    - HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
 *      经典多重背包问题
 *    - HDU 2159. FATE - http://acm.hdu.edu.cn/showproblem.php?pid=2159
 *      二维费用背包问题，同时考虑忍耐度和杀怪数
 *    - HDU 3449. Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
 *      有依赖的背包问题
 * 
 * 5. Codeforces：
 *    - Codeforces 106C. Buns - https://codeforces.com/contest/106/problem/C
 *      多重背包问题，制作不同种类的面包
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
import java.util.Arrays;

/**
 * HDU 2191题的解决方案类
 * 
 * 技术要点：
 * 1. 二进制优化的多重背包实现
 * 2. 空间压缩的动态规划
 * 3. 高效的输入输出处理
 */
public class Code09_HDU2191 {

    /** 通过二进制分组生成的物品最大数量 */
    public static int MAXN = 1001;
    
    /** 背包容量的最大可能值 */
    public static int MAXM = 101;
    
    /** 分组后物品的价值数组 */
    public static int[] v = new int[MAXN];  
    
    /** 分组后物品的重量数组 */
    public static int[] w = new int[MAXN];  
    
    /** 动态规划数组：dp[j]表示容量为j的背包能装下的最大价值 */
    public static int[] dp = new int[MAXM]; 
    
    /**
     * 主方法
     * 处理输入、调用计算逻辑、输出结果
     * 
     * @param args 命令行参数（未使用）
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化输入流，使用BufferedReader提高读取效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 初始化输出流，使用PrintWriter提高写入效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取测试用例数
        int c = Integer.parseInt(br.readLine()); 
        
        // 处理每组测试用例
        for (int i = 0; i < c; i++) {
            // 读取背包容量和物品种类数
            String[] line1 = br.readLine().split(" ");
            int m = Integer.parseInt(line1[0]); // 背包容量
            int n = Integer.parseInt(line1[1]); // 物品种类数
            
            // 重置分组后物品的总数量
            int k = 0; 
            
            // 读取每种物品的信息并进行二进制分组
            for (int j = 0; j < n; j++) {
                String[] line2 = br.readLine().split(" ");
                int value = Integer.parseInt(line2[0]);   // 物品单价（价值）
                int weight = Integer.parseInt(line2[1]);  // 物品重量（费用）
                int count = Integer.parseInt(line2[2]);   // 物品数量
                
                // 二进制分组优化：将数量count拆分为2^0, 2^1, 2^2, ..., count-2^k的形式
                // 这样任何小于等于count的数都可以表示为这些二进制数的和
                for (int t = 1; t <= count; t <<= 1) { // t = 1, 2, 4, 8...
                    v[++k] = t * value;     // 将t个物品打包成一个新物品的价值
                    w[k] = t * weight;      // 将t个物品打包成一个新物品的重量
                    count -= t;             // 减去已处理的数量
                }
                // 处理剩余的物品数量
                if (count > 0) {
                    v[++k] = count * value;
                    w[k] = count * weight;
                }
            }
            
            // 调用求解函数并输出结果
            out.println(hdu2191(m, k));
        }
        
        // 确保输出全部写入并关闭资源
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 使用01背包算法求解多重背包问题
     * 
     * 算法思路：
     * 1. 初始化dp数组为0，表示空背包的最大价值为0
     * 2. 对每个物品（通过二进制分组后的物品），从大到小遍历背包容量
     * 3. 状态转移方程：dp[j] = max(dp[j], dp[j - w[i]] + v[i])
     *    表示选择当前物品或不选择当前物品中的最大值
     * 
     * @param m 背包容量
     * @param k 分组后的物品总数
     * @return 背包能装下的最大价值
     */
    public static int hdu2191(int m, int k) {
        // 初始化dp数组
        // 使用Arrays.fill提高初始化效率
        Arrays.fill(dp, 0, m + 1, 0);
        
        // 01背包求解：每个物品只能选或不选
        for (int i = 1; i <= k; i++) {
            // 从大到小遍历容量，避免重复选择同一物品
            for (int j = m; j >= w[i]; j--) {
                // 状态转移：选择当前物品或不选当前物品中的最大值
                dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
            }
        }
        
        // 返回最大容量时的最大价值
        return dp[m];
    }
    
    /**
     * 二进制分组优化原理详解
     * 
     * 1. 问题分析：
     *    多重背包问题中，每种物品可以选择0到c[i]个
     *    朴素做法是将每种物品拆分为c[i]个单独的物品，时间复杂度为O(M*Σc[i])
     * 
     * 2. 二进制优化思路：
     *    任何整数n都可以唯一地表示为不同2的幂次的和
     *    例如：13 = 1 + 4 + 8
     *    因此，我们可以将数量为c的物品拆分为log2(c)个物品组
     *    每个组的大小为2^0, 2^1, 2^2, ..., 2^k, r（其中r是剩余部分）
     * 
     * 3. 数学证明：
     *    对于任意整数x ∈ [0, c]，可以唯一表示为这些二进制组的和
     *    这样，选择这些组的组合就能表示选择x个原物品
     * 
     * 4. 优化效果：
     *    物品数量从c[i]减少到log2(c[i])
     *    时间复杂度从O(M*Σc[i])优化到O(M*Σlog c[i])
     */
    
    /**
     * 代码优化与工程化考量
     * 
     * 1. 边界情况处理：
     *    - 处理物品数量为0的情况
     *    - 处理物品重量超过背包容量的情况
     *    - 确保数组索引不会越界
     * 
     * 2. 性能优化技巧：
     *    - 使用BufferedReader和PrintWriter提高IO效率
     *    - 使用Arrays.fill进行数组初始化
     *    - 适当设置MAXN和MAXM的大小，避免不必要的内存浪费
     *    - 从大到小遍历容量，避免重复计算
     * 
     * 3. 代码健壮性增强：
     *    - 可以添加输入参数校验
     *    - 处理可能的数值溢出问题
     *    - 考虑异常情况的处理
     * 
     * 4. 代码可读性提升：
     *    - 使用有意义的变量名
     *    - 添加详细的注释说明
     *    - 模块化设计函数
     */
    
    /**
     * 与其他优化方法的对比
     * 
     * 1. 朴素实现：
     *    - 时间复杂度：O(M*Σc[i])
     *    - 优点：实现简单
     *    - 缺点：当c[i]很大时效率很低
     * 
     * 2. 二进制优化（本实现）：
     *    - 时间复杂度：O(M*Σlog c[i])
     *    - 优点：实现相对简单，效率大幅提升
     *    - 缺点：在极端情况下可能不如单调队列优化
     * 
     * 3. 单调队列优化：
     *    - 时间复杂度：O(N*M)
     *    - 优点：理论时间复杂度最优
     *    - 缺点：实现复杂，常数较大
     * 
     * 工程选择建议：
     * - 对于一般规模的问题，二进制优化是最佳选择
     * - 只有在数据规模非常大且时间限制严格时，才考虑单调队列优化
     */
}
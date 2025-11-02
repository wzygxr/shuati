package class075;

/**
 * POJ 1742. Coins 问题的解决方案
 * 
 * 问题描述：
 * 给定N种硬币，每种硬币的面值为A[i]，数量为C[i]
 * 要求计算在1到M之间有多少个数值可以由这些硬币组成
 * 
 * 算法分类：动态规划 - 多重背包问题（可行性问题）
 * 
 * 算法原理：
 * 1. 将每种硬币视为有数量限制的物品
 * 2. 使用动态规划判断是否能组成各个金额
 * 3. 根据硬币数量和面值，采用不同的优化策略：
 *    - 当硬币数量足够多时，转化为完全背包问题
 *    - 当硬币数量有限时，使用同余分组+滑动窗口优化
 * 
 * 时间复杂度：O(N * M)，其中 N 是硬币种类数，M 是最大金额
 * 空间复杂度：O(M)
 * 
 * 测试链接：http://poj.org/problem?id=1742
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
 *    - 518. Coin Change II - https://leetcode.cn/problems/coin-change-ii/
 *      完全背包计数问题，求组成金额的方案数
 * 
 * 2. 洛谷（Luogu）：
 *    - P1776 宝物筛选 - https://www.luogu.com.cn/problem/P1776
 *      经典多重背包问题
 *    - P1833 樱花 - https://www.luogu.com.cn/problem/P1833
 *      混合背包问题，包含01背包、完全背包和多重背包
 *    - P1679 聪明的收银员 - https://www.luogu.com.cn/problem/P1679
 *      多重背包在找零问题中的应用
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
 *      分组背包与多重背包的混合应用
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
 *    - NC16552. 买苹果 - https://ac.nowcoder.com/acm/problem/16552
 *      完全背包问题
 * 
 * 9. AcWing：
 *    - AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
 *      二进制优化的多重背包问题标准题目
 * 
 * 10. UVa OJ：
 *     - UVa 562. Dividing coins - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=503
 *       01背包变形，公平分配硬币
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
 * 多重背包可行性问题的高效实现
 * 
 * 技术要点：
 * 1. 使用一维布尔型DP数组记录状态
 * 2. 根据硬币数量采用不同的优化策略
 * 3. 使用同余分组和滑动窗口技术优化多重背包部分
 * 4. 高效的输入输出处理
 */
public class Code08_Coins {

    /** 最大硬币种类数 */
    public static int MAXN = 101;
    /** 最大金额 */
    public static int MAXM = 100001;
    
    /** 硬币面值数组 */
    public static int[] A = new int[MAXN];
    /** 硬币数量数组 */
    public static int[] C = new int[MAXN];
    /** 动态规划数组，dp[i]表示是否能组成金额i */
    public static boolean[] dp = new boolean[MAXM];
    
    /**
     * 主方法
     * 处理输入、调用计算逻辑、输出结果
     * 
     * 工程化考量：
     * 1. 使用BufferedReader进行高效的输入读取
     * 2. 使用PrintWriter进行高效的输出写入
     * 3. 实现了多组输入的处理，直到遇到0 0
     * 4. 确保输入输出流被正确关闭，防止资源泄露
     * 
     * @param args 命令行参数（未使用）
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化输入输出流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 处理多组输入
        while (true) {
            // 读取一行输入
            String line = br.readLine();
            if (line == null || line.isEmpty()) break;
            
            // 解析硬币种类数和最大金额
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            
            // 终止条件：当n和m都为0时结束
            if (n == 0 && m == 0) break;
            
            // 读取硬币面值和数量
            String[] aStr = br.readLine().split(" ");
            String[] cStr = br.readLine().split(" ");
            
            // 填充硬币面值和数量数组
            for (int i = 1; i <= n; i++) {
                A[i] = Integer.parseInt(aStr[i-1]);
            }
            for (int i = 1; i <= n; i++) {
                C[i] = Integer.parseInt(cStr[i-1]);
            }
            
            // 计算并输出结果
            out.println(coins(n, m));
        }
        
        // 刷新并关闭流
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 核心算法实现：计算能组成的金额数量
     * 
     * @param n 硬币种类数
     * @param m 最大金额
     * @return 1到m之间能组成的金额数量
     */
    public static int coins(int n, int m) {
        // 初始化dp数组
        // dp[0]=true表示金额0可以组成（不需要任何硬币）
        // 其他初始化为false
        Arrays.fill(dp, 1, m + 1, false);
        dp[0] = true;
        
        // 遍历每种硬币
        for (int i = 1; i <= n; i++) {
            int val = A[i];   // 当前硬币的面值
            int cnt = C[i];   // 当前硬币的数量
            
            // 优化1：跳过面值为0的硬币（如果有的话）
            if (val == 0) continue;
            
            // 优化2：跳过无法使用的硬币（面值超过最大金额）
            if (val > m) continue;
            
            // 优化3：跳过数量为0的硬币
            if (cnt == 0) continue;
            
            // 策略选择：
            // 1. 当硬币数量乘以面值大于等于m时，可以视为完全背包
            //    因为最多只需要m/val个硬币就可以表示所有金额
            // 2. 否则，使用同余分组+滑动窗口的优化方法
            if (val * cnt >= m) {
                // 完全背包处理：正序遍历
                // 可以重复使用硬币，所以从前往后更新
                for (int j = val; j <= m; j++) {
                    // 如果j-val可以组成，那么j也可以组成
                    // 这相当于使用了一个当前面值的硬币
                    if (dp[j - val]) {
                        dp[j] = true;
                    }
                }
            } else {
                // 多重背包优化：同余分组 + 滑动窗口
                // 将金额按模val分组，每组内的金额形式为val*k + r（r为余数，0<=r<val）
                for (int mod = 0; mod < val; mod++) {
                    // 初始化窗口内的true计数
                    int trueCnt = 0;
                    
                    // 计算初始窗口（从当前余数能达到的最大金额开始）
                    // 窗口大小为cnt+1，表示最多使用cnt个硬币
                    for (int j = m - mod, size = 0; j >= 0 && size <= cnt; j -= val, size++) {
                        if (dp[j]) {
                            trueCnt++;
                        }
                    }
                    
                    // 滑动窗口处理当前余数组
                    // j表示当前处理的金额，l表示要滑入窗口的金额
                    for (int j = m - mod, l = j - val * (cnt + 1); j >= 1; j -= val, l -= val) {
                        // 从窗口中移除j位置（当窗口滑动时）
                        if (dp[j]) {
                            trueCnt--;
                        } else {
                            // 如果当前位置j原本不能组成，但窗口中存在能组成的位置
                            // 则j现在也可以组成
                            if (trueCnt != 0) {
                                dp[j] = true;
                            }
                        }
                        // 将l位置加入窗口（如果l有效）
                        if (l >= 0) {
                            if (dp[l]) {
                                trueCnt++;
                            }
                        }
                    }
                }
            }
        }
        
        // 统计能组成的金额数量（从1到m）
        int result = 0;
        for (int i = 1; i <= m; i++) {
            if (dp[i]) {
                result++;
            }
        }
        
        return result;
    }
    
    /**
     * 算法详解与原理解析
     * 
     * 1. 问题建模：
     *    - 每种硬币是一种物品，有数量限制
     *    - 物品的"重量"是硬币面值
     *    - 背包容量是m
     *    - 目标是判断是否能恰好装满背包（可行性问题）
     * 
     * 2. 状态定义：
     *    - dp[i]表示是否能组成金额i
     *    - 使用一维布尔数组，节省空间
     * 
     * 3. 两种优化策略：
     *    - 完全背包策略：当硬币数量足够多时（val*cnt >= m），可以视为每种硬币无限使用
     *      此时正序遍历背包容量，允许重复选择
     *    - 同余分组+滑动窗口策略：当硬币数量有限时
     *      a. 将金额按模val分组，每组内金额形式为val*k + r
     *      b. 对于每组，使用滑动窗口维护最近cnt+1个位置中能组成的数量
     *      c. 如果窗口中存在能组成的位置，则当前位置也能组成
     * 
     * 4. 同余分组优化的数学原理：
     *    - 设金额j = q*val + r，其中0 <= r < val
     *    - 要组成j，最多使用cnt个面值为val的硬币
     *    - 因此，需要检查j-val, j-2*val, ..., j-min(cnt, q)*val这些位置
     *    - 同余分组将这些检查限制在同一余数的组内，减少计算量
     * 
     * 5. 状态转移方程推导：
     *    对于多重背包可行性问题：
     *    dp[j] = dp[j] || dp[j-val] || dp[j-2*val] || ... || dp[j-k*val]，其中0 < k <= min(cnt, j/val)
     *    
     *    当转换为完全背包时（val*cnt >= m）：
     *    dp[j] = dp[j] || dp[j-val]（因为可以使用无限多个硬币）
     *    
     *    当使用同余分组优化时：
     *    对于每个余数r，只需检查该余数组中最近cnt个位置是否可达
     */
    
    /**
     * 工程化考量与代码优化
     * 
     * 1. 空间优化：
     *    - 使用一维布尔数组代替二维数组，节省空间
     *    - 布尔数组比整型数组更节省内存
     *    - 每次测试用例复用同一个数组，减少内存分配开销
     * 
     * 2. 性能优化：
     *    - 根据硬币数量选择不同的优化策略
     *    - 添加多重剪枝：跳过面值为0、大于m或数量为0的硬币
     *    - 使用同余分组和滑动窗口技术将时间复杂度从O(N*M*C)优化到O(N*M)
     *    - 使用Arrays.fill进行数组初始化，效率更高
     *    - 使用BufferedReader和PrintWriter提高IO效率
     * 
     * 3. 代码健壮性：
     *    - 处理了多组输入的情况
     *    - 实现了正确的终止条件（n==0 && m==0）
     *    - 处理了空输入行的情况
     *    - 添加了边界条件检查，避免数组越界
     * 
     * 4. 代码可读性：
     *    - 使用有意义的变量名
     *    - 添加详细的注释说明算法原理和优化策略
     *    - 模块化设计函数，每个函数职责单一
     * 
     * 5. 可扩展性：
     *    - 代码结构清晰，可以轻松修改为求解其他类型的背包问题
     *    - 同余分组+滑动窗口的优化方法可以应用于其他类似问题
     * 
     * 6. 调试与测试建议：
     *    - 可以添加日志输出中间状态，便于调试
     *    - 编写单元测试覆盖各种边界情况
     *    - 使用断言验证关键条件
     */
    
    /**
     * 算法优化比较与选择依据
     * 
     * 1. 二进制优化 vs 同余分组+滑动窗口优化：
     *    - 二进制优化：时间复杂度O(M*N*logC)，实现简单，但常数较大
     *    - 同余分组+滑动窗口：时间复杂度O(M*N)，实现较复杂，但理论最优
     *    - 选择依据：对于本题的可行性问题，滑动窗口优化性能更好
     * 
     * 2. 空间优化技巧：
     *    - 使用一维布尔数组而非二维数组
     *    - 利用滚动数组思想，每次只保留上一阶段的状态
     *    - 对于本题，由于是可行性问题，布尔数组比整数数组更节省空间
     * 
     * 3. 边界情况处理：
     *    - 处理面值为0的硬币
     *    - 处理面值超过最大金额的硬币
     *    - 处理数量为0的硬币
     *    - 处理m=0的特殊情况
     * 
     * 4. 面试要点：
     *    - 能够解释多重背包问题的不同优化策略
     *    - 能够推导状态转移方程
     *    - 理解同余分组和滑动窗口的优化原理
     *    - 分析算法的时间复杂度和空间复杂度
     *    - 能够根据问题特点选择合适的优化策略
     */
    
    /**
     * 代码调试与优化技巧
     * 
     * 1. 调试技巧：
     *    - 打印中间状态：在关键步骤添加输出语句，观察dp数组的变化
     *    - 小数据测试：使用小的测试用例验证算法正确性
     *    - 边界条件测试：测试极端情况如m=0、只有一种硬币等
     * 
     * 2. 性能优化技巧：
     *    - 预分配数组空间，避免频繁扩容
     *    - 使用位运算代替某些逻辑运算
     *    - 适当展开循环，减少循环开销
     *    - 利用CPU缓存局部性，优化数据访问模式
     * 
     * 3. 代码优化方向：
     *    - 可以考虑使用位操作进一步优化布尔数组的空间
     *    - 对于大规模数据，可以考虑并行处理不同的余数分组
     *    - 在实际工程中，可以添加配置参数控制优化策略的选择
     */
    
    /**
     * 背包问题总结
     * 
     * 1. 01背包：每种物品只能选或不选
     *    - 核心：逆序遍历背包容量
     *    - 状态转移：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
     * 
     * 2. 完全背包：每种物品可以选无限次
     *    - 核心：正序遍历背包容量
     *    - 状态转移：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
     * 
     * 3. 多重背包：每种物品有数量限制
     *    - 优化方法：二进制拆分、单调队列优化、同余分组+滑动窗口
     *    - 选择依据：根据问题特点和数据规模选择合适的优化方法
     * 
     * 4. 分组背包：每组物品中最多选一个
     *    - 核心：三重循环，外层遍历分组，中层逆序遍历容量，内层遍历组内物品
     * 
     * 5. 二维费用背包：每个物品消耗两种资源
     *    - 状态扩展：二维数组dp[j][k]表示消耗资源j和k时的最大价值
     */
}
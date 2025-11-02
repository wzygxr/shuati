package class074;

// ACboy needs your help
// 题目描述：ACboy有N门课程，他有M天时间复习。每门课程复习不同的天数会有不同的收益。
// 求在M天时间内，如何安排复习计划使得总收益最大。
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1712

/*
 * 算法详解：
 * 这是一个典型的分组背包问题。每门课程可以看作一组物品，每组内的物品互斥（同一门课程只能选择一种复习天数）。
 * 每组物品的价值就是复习该课程不同天数对应的收益。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示前i门课程，使用j天时间复习的最大收益
 * 2. 状态转移：对于每门课程，枚举所有可能的复习天数k（1 <= k <= j）
 *    dp[i][j] = max(dp[i-1][j], dp[i-1][j-k] + value[i][k])
 * 3. 空间优化：使用滚动数组将二维优化到一维
 * 
 * 时间复杂度分析：
 * 设有N门课程，M天时间，每门课程最多有M种选择
 * 1. 动态规划计算：O(N * M * M)
 * 总时间复杂度：O(N * M^2)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(N * M)
 * 2. 空间优化后：O(M)
 * 
 * 相关题目扩展：
 * 1. HDU 1712 ACboy needs your help（本题）
 * 2. 洛谷 P1757 通天之分组背包
 * 3. LeetCode 1155. 掷骰子的N种方法
 * 4. 洛谷 P1064 金明的预算方案（依赖背包）
 * 5. 洛谷 P1941 飞扬的小鸟（多重背包+分组背包）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理非法输入、边界情况等
 * 3. 可配置性：可以将课程数和天数作为配置参数传入
 * 4. 单元测试：为solve方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用空间优化版本
 * 
 * 语言特性差异：
 * 1. Java：使用Scanner进行输入，需要注意输入效率
 * 2. 数组操作：Java数组索引从0开始，需要注意边界处理
 * 3. 内存管理：自动垃圾回收，无需手动管理内存
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从二维dp优化到一维dp
 * 2. 剪枝优化：当复习天数超过剩余天数时跳过
 * 3. 预处理优化：提前计算每门课程的最大收益
 * 
 * 与标准分组背包的区别：
 * 1. 物品定义：每组物品是同一门课程的不同复习天数
 * 2. 价值函数：价值是复习天数对应的收益
 * 3. 约束条件：总复习天数不能超过M
 */

import java.util.Scanner;

public class Code14_ACboyNeedsYourHelp {
    
    public static int MAXN = 101;  // 最大课程数
    public static int MAXM = 101;  // 最大天数
    
    public static int[][] value = new int[MAXN][MAXM];  // value[i][j]表示第i门课程复习j天的收益
    public static int[] dp = new int[MAXM];            // 空间优化后的DP数组
    
    // 交互式输入主方法（注释掉，避免重复定义）
    /*
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            int N = scanner.nextInt(); // 课程数
            int M = scanner.nextInt(); // 总天数
            
            if (N == 0 && M == 0) break;
            
            // 读取每门课程的收益
            for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= M; j++) {
                    value[i][j] = scanner.nextInt();
                }
            }
            
            // 调用求解方法
            int result = solve(N, M);
            System.out.println(result);
        }
        
        scanner.close();
    }
    */
    
    // 标准二维DP版本
    public static int solve(int N, int M) {
        // 初始化DP数组
        int[][] dp = new int[N + 1][M + 1];
        
        for (int i = 1; i <= N; i++) {  // 遍历每门课程
            for (int j = 0; j <= M; j++) {  // 遍历可用天数
                // 初始值：不复习当前课程
                dp[i][j] = dp[i - 1][j];
                
                // 枚举复习当前课程的天数k
                for (int k = 1; k <= j; k++) {
                    if (value[i][k] > 0) {  // 只有收益为正时才考虑
                        dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - k] + value[i][k]);
                    }
                }
            }
        }
        
        return dp[N][M];
    }
    
    // 空间优化版本（推荐使用）
    public static int solveOptimized(int N, int M) {
        // 初始化DP数组
        for (int j = 0; j <= M; j++) {
            dp[j] = 0;
        }
        
        for (int i = 1; i <= N; i++) {  // 遍历每门课程
            // 从后往前更新，避免重复使用
            for (int j = M; j >= 0; j--) {
                // 枚举复习当前课程的天数k
                for (int k = 1; k <= j; k++) {
                    if (value[i][k] > 0) {  // 只有收益为正时才考虑
                        dp[j] = Math.max(dp[j], dp[j - k] + value[i][k]);
                    }
                }
            }
        }
        
        return dp[M];
    }
    
    // 测试方法
    public static void test() {
        // 测试用例1：标准情况
        int N1 = 2, M1 = 2;
        int[][] value1 = {
            {0, 0, 0},  // 占位，索引从1开始
            {0, 1, 2},  // 课程1：复习1天收益1，复习2天收益2
            {0, 1, 3}   // 课程2：复习1天收益1，复习2天收益3
        };
        
        // 复制测试数据
        for (int i = 1; i <= N1; i++) {
            for (int j = 1; j <= M1; j++) {
                value[i][j] = value1[i][j];
            }
        }
        
        System.out.println("测试用例1:");
        System.out.println("标准版本: " + solve(N1, M1));
        System.out.println("优化版本: " + solveOptimized(N1, M1));
        System.out.println("预期结果: 3");
        System.out.println();
        
        // 测试用例2：边界情况
        int N2 = 0, M2 = 0;
        System.out.println("测试用例2（边界情况）:");
        System.out.println("标准版本: " + solve(N2, M2));
        System.out.println("优化版本: " + solveOptimized(N2, M2));
        System.out.println("预期结果: 0");
        System.out.println();
        
        // 测试用例3：较大规模
        int N3 = 3, M3 = 3;
        int[][] value3 = {
            {0, 0, 0, 0},
            {0, 2, 1, 3},  // 课程1
            {0, 1, 2, 1},  // 课程2
            {0, 3, 2, 1}   // 课程3
        };
        
        for (int i = 1; i <= N3; i++) {
            for (int j = 1; j <= M3; j++) {
                value[i][j] = value3[i][j];
            }
        }
        
        System.out.println("测试用例3:");
        System.out.println("标准版本: " + solve(N3, M3));
        System.out.println("优化版本: " + solveOptimized(N3, M3));
        System.out.println("预期结果: 6"); // 课程1复习3天(3) + 课程2复习0天(0) + 课程3复习0天(0) = 3
        System.out.println("实际分析：应该选择课程1复习3天获得收益3，或者课程2复习2天+课程3复习1天获得收益2+3=5");
    }
    
    // 测试主方法
    public static void main(String[] args) {
        test();
    }
    
    /*
     * =============================================================================================
     * 补充题目：洛谷 P1757 通天之分组背包
     * 题目链接：https://www.luogu.com.cn/problem/P1757
     * 题目描述：有n个物品，每个物品属于一个组。每组物品最多只能选择一个。
     * 在背包容量为m的情况下，求能获得的最大价值。
     * 
     * 解题思路：
     * 标准的分组背包问题，每组物品互斥，每组最多选择一个物品。
     * 
     * 状态定义：dp[i][j]表示前i组物品，背包容量为j时的最大价值
     * 状态转移：
     * dp[i][j] = max(dp[i-1][j], max(dp[i-1][j-w[k]] + v[k])) 其中k属于第i组
     * 
     * 时间复杂度：O(G * M * T)，其中G为组数，M为背包容量，T为每组平均物品数
     * 空间复杂度：O(M)，使用一维数组优化
     * 
     * Java实现：
     * public int groupKnapsack(int m, int n, int[][] items) {
     *     // items[i] = [weight, value, group]
     *     // 先按组号排序
     *     Arrays.sort(items, (a, b) -> a[2] - b[2]);
     *     
     *     int[] dp = new int[m + 1];
     *     
     *     for (int i = 0; i < n; ) {
     *         int group = items[i][2];
     *         int j = i;
     *         while (j < n && items[j][2] == group) j++;
     *         
     *         // 当前组包含物品[i, j-1]
     *         for (int k = m; k >= 0; k--) {
     *             for (int x = i; x < j; x++) {
     *                 if (k >= items[x][0]) {
     *                     dp[k] = Math.max(dp[k], dp[k - items[x][0]] + items[x][1]);
     *                 }
     *             }
     *         }
     *         
     *         i = j;
     *     }
     *     
     *     return dp[m];
     * }
     * 
     * 工程化考量：
     * 1. 输入验证：检查物品数据是否合法
     * 2. 异常处理：处理空组、负重量等特殊情况
     * 3. 性能优化：对于大数据量，使用更高效的排序算法
     * 
     * 优化思路：
     * 1. 空间压缩：使用一维数组进行优化
     * 2. 剪枝优化：当物品重量超过剩余容量时跳过
     * 3. 分组预处理：提前对物品按组号分组
     */
}
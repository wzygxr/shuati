package class069;

/**
 * Coins (概率DP) - 概率动态规划问题
 * 
 * 题目描述：
 * 有N枚硬币，第i枚硬币抛出后正面朝上的概率是p[i]。
 * 现在将这N枚硬币都抛一次，求正面朝上的硬币数比反面朝上的硬币数多的概率。
 * 
 * 题目来源：AtCoder Educational DP Contest I - Coins
 * 测试链接：https://atcoder.jp/contests/dp/tasks/dp_i
 * 
 * 解题思路：
 * 这是一个典型的概率动态规划问题，需要计算多个独立事件组合的概率。
 * 我们可以使用动态规划来计算前i枚硬币中有j枚正面朝上的概率。
 * 
 * 算法实现：
 * 1. 基础动态规划：使用二维DP表存储概率
 * 2. 空间优化：使用一维数组滚动更新
 * 3. 记忆化搜索：递归计算概率，使用记忆化避免重复计算
 * 
 * 时间复杂度分析：
 * - 基础动态规划：O(N^2)，需要填充二维DP表
 * - 空间优化：O(N^2)，时间复杂度相同但空间更优
 * - 记忆化搜索：O(N^2)，每个状态计算一次
 * 
 * 空间复杂度分析：
 * - 基础动态规划：O(N^2)，二维DP表
 * - 空间优化：O(N)，一维数组
 * - 记忆化搜索：O(N^2)，记忆化数组
 * 
 * 关键技巧：
 * 1. 概率计算：独立事件的概率乘法
 * 2. 状态转移：考虑硬币正面和反面两种情况
 * 3. 边界处理：0枚硬币的概率为1
 * 
 * 工程化考量：
 * 1. 精度处理：使用double类型存储概率
 * 2. 边界条件：处理硬币数为0或1的特殊情况
 * 3. 性能优化：空间优化降低内存使用
 * 4. 可测试性：提供多种概率分布的测试用例
 */
public class Code06_Coins {
    
    /**
     * 动态规划解法
     * 
     * @param p 硬币正面朝上的概率数组
     * @return 正面朝上的硬币数比反面朝上的硬币数多的概率
     */
    public static double probabilityOfHeads1(double[] p) {
        int n = p.length;
        // dp[i][j] 表示前i枚硬币中，有j枚正面朝上的概率
        double[][] dp = new double[n + 1][n + 1];
        
        // 初始状态：0枚硬币，0枚正面朝上概率为1
        dp[0][0] = 1.0;
        
        // 状态转移
        for (int i = 1; i <= n; i++) {
            // 0枚正面朝上只能是当前硬币也是反面朝上
            dp[i][0] = dp[i - 1][0] * (1 - p[i - 1]);
            
            for (int j = 1; j <= i; j++) {
                // 第i枚硬币反面朝上 + 第i枚硬币正面朝上
                dp[i][j] = dp[i - 1][j] * (1 - p[i - 1]) + dp[i - 1][j - 1] * p[i - 1];
            }
        }
        
        // 计算正面朝上的硬币数比反面朝上的硬币数多的概率
        // 即正面朝上的硬币数 > n/2
        double result = 0.0;
        for (int j = n / 2 + 1; j <= n; j++) {
            result += dp[n][j];
        }
        
        return result;
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param p 硬币正面朝上的概率数组
     * @return 正面朝上的硬币数比反面朝上的硬币数多的概率
     */
    public static double probabilityOfHeads2(double[] p) {
        int n = p.length;
        // 只需要保存前一层的状态
        double[] dp = new double[n + 1];
        dp[0] = 1.0;
        
        // 状态转移
        for (int i = 1; i <= n; i++) {
            // 从后往前更新，避免重复使用更新后的值
            for (int j = i; j >= 1; j--) {
                dp[j] = dp[j] * (1 - p[i - 1]) + dp[j - 1] * p[i - 1];
            }
            // 更新dp[0]
            dp[0] = dp[0] * (1 - p[i - 1]);
        }
        
        // 计算正面朝上的硬币数比反面朝上的硬币数多的概率
        double result = 0.0;
        for (int j = n / 2 + 1; j <= n; j++) {
            result += dp[j];
        }
        
        return result;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param p 硬币正面朝上的概率数组
     * @return 正面朝上的硬币数比反面朝上的硬币数多的概率
     */
    public static double probabilityOfHeads3(double[] p) {
        int n = p.length;
        // 记忆化数组
        double[][] memo = new double[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                memo[i][j] = -1.0;
            }
        }
        
        // 计算正面朝上的硬币数比反面朝上的硬币数多的概率
        double result = 0.0;
        for (int j = n / 2 + 1; j <= n; j++) {
            result += dfs(p, n, j, memo);
        }
        
        return result;
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param p 硬币正面朝上的概率数组
     * @param i 当前处理到第几枚硬币
     * @param j 需要正面朝上的硬币数
     * @param memo 记忆化数组
     * @return 概率值
     */
    private static double dfs(double[] p, int i, int j, double[][] memo) {
        // 边界条件
        if (j < 0 || j > i) {
            return 0.0;
        }
        
        if (i == 0) {
            return j == 0 ? 1.0 : 0.0;
        }
        
        // 检查是否已经计算过
        if (memo[i][j] != -1.0) {
            return memo[i][j];
        }
        
        // 第i枚硬币反面朝上 + 第i枚硬币正面朝上
        double ans = dfs(p, i - 1, j, memo) * (1 - p[i - 1]) + 
                     dfs(p, i - 1, j - 1, memo) * p[i - 1];
        
        // 记忆化存储
        memo[i][j] = ans;
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        double[] p1 = {0.3, 0.6, 0.8};
        System.out.println("测试用例1:");
        System.out.println("硬币正面朝上概率: [0.3, 0.6, 0.8]");
        System.out.println("方法1结果: " + probabilityOfHeads1(p1));
        System.out.println("方法2结果: " + probabilityOfHeads2(p1));
        System.out.println("方法3结果: " + probabilityOfHeads3(p1));
        System.out.println();
        
        // 测试用例2
        double[] p2 = {0.5};
        System.out.println("测试用例2:");
        System.out.println("硬币正面朝上概率: [0.5]");
        System.out.println("方法1结果: " + probabilityOfHeads1(p2));
        System.out.println("方法2结果: " + probabilityOfHeads2(p2));
        System.out.println("方法3结果: " + probabilityOfHeads3(p2));
        System.out.println();
        
        // 测试用例3
        double[] p3 = {0.42, 0.01, 0.42, 0.99, 0.42};
        System.out.println("测试用例3:");
        System.out.println("硬币正面朝上概率: [0.42, 0.01, 0.42, 0.99, 0.42]");
        System.out.println("方法1结果: " + probabilityOfHeads1(p3));
        System.out.println("方法2结果: " + probabilityOfHeads2(p3));
        System.out.println("方法3结果: " + probabilityOfHeads3(p3));
    }
}
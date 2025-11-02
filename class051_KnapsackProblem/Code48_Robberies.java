package class073;

import java.util.Arrays;

// HDU 2955 Robberies
// 题目描述：抢劫犯想抢劫银行，每个银行有一定的金额和被抓的概率。
// 抢劫犯希望在被抓概率不超过某个值的情况下，抢劫到最多的钱。
// 链接：https://acm.hdu.edu.cn/showproblem.php?pid=2955
// 
// 解题思路：
// 这是一个概率背包问题，需要将问题转化为标准的01背包问题。
// 关键点：将金额作为背包容量，将安全概率（1-被抓概率）作为价值。
// 
// 状态定义：dp[i] 表示抢劫到金额i时的最大安全概率
// 状态转移方程：dp[i] = max(dp[i], dp[i-money[j]] * (1-p[j]))
// 初始状态：dp[0] = 1（抢劫0元时安全概率为1）
// 
// 时间复杂度：O(N * totalMoney)，其中N是银行数量，totalMoney是总金额
// 空间复杂度：O(totalMoney)，使用一维DP数组
// 
// 工程化考量：
// 1. 精度处理：使用double类型处理概率
// 2. 边界条件：处理概率为0或1的情况
// 3. 性能优化：计算总金额作为背包容量上限
// 4. 异常处理：处理非法输入

public class Code48_Robberies {
    
    /**
     * 动态规划解法 - 概率背包问题
     * @param P 最大允许被抓概率
     * @param money 每个银行的金额数组
     * @param prob 每个银行的被抓概率数组
     * @return 在安全概率范围内的最大抢劫金额
     */
    public static double rob(double P, int[] money, double[] prob) {
        // 参数验证
        if (money == null || prob == null || money.length != prob.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (P < 0 || P > 1) {
            throw new IllegalArgumentException("Probability P must be between 0 and 1");
        }
        
        int n = money.length;
        if (n == 0) {
            return 0;
        }
        
        // 计算总金额作为背包容量上限
        int totalMoney = 0;
        for (int m : money) {
            totalMoney += m;
        }
        
        // 创建DP数组，dp[i]表示抢劫到金额i时的最大安全概率
        double[] dp = new double[totalMoney + 1];
        Arrays.fill(dp, 0);
        dp[0] = 1.0; // 抢劫0元时安全概率为1
        
        // 01背包：遍历每个银行
        for (int i = 0; i < n; i++) {
            int m = money[i];
            double p = prob[i];
            double safeProb = 1 - p; // 安全概率
            
            // 倒序遍历金额，避免重复选择
            for (int j = totalMoney; j >= m; j--) {
                if (dp[j - m] > 0) {
                    dp[j] = Math.max(dp[j], dp[j - m] * safeProb);
                }
            }
        }
        
        // 寻找最大的金额，使得安全概率 >= 1-P
        double minSafeProb = 1 - P;
        int maxMoney = 0;
        for (int j = totalMoney; j >= 0; j--) {
            if (dp[j] >= minSafeProb) {
                maxMoney = j;
                break;
            }
        }
        
        return maxMoney;
    }
    
    /**
     * 优化的动态规划解法 - 提前终止遍历
     */
    public static double robOptimized(double P, int[] money, double[] prob) {
        if (money == null || prob == null || money.length != prob.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (P < 0 || P > 1) {
            throw new IllegalArgumentException("Probability P must be between 0 and 1");
        }
        
        int n = money.length;
        if (n == 0) {
            return 0;
        }
        
        // 计算总金额
        int totalMoney = 0;
        for (int m : money) {
            totalMoney += m;
        }
        
        double[] dp = new double[totalMoney + 1];
        Arrays.fill(dp, 0);
        dp[0] = 1.0;
        
        for (int i = 0; i < n; i++) {
            int m = money[i];
            double safeProb = 1 - prob[i];
            
            for (int j = totalMoney; j >= m; j--) {
                if (dp[j - m] > 0) {
                    double newProb = dp[j - m] * safeProb;
                    if (newProb > dp[j]) {
                        dp[j] = newProb;
                    }
                }
            }
        }
        
        double minSafeProb = 1 - P;
        // 从大到小遍历，找到第一个满足条件的金额
        for (int j = totalMoney; j >= 0; j--) {
            if (dp[j] >= minSafeProb) {
                return j;
            }
        }
        
        return 0;
    }
    
    /**
     * 另一种思路：将金额作为价值，概率作为约束
     */
    public static double robAlternative(double P, int[] money, double[] prob) {
        if (money == null || prob == null || money.length != prob.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (P < 0 || P > 1) {
            throw new IllegalArgumentException("Probability P must be between 0 and 1");
        }
        
        int n = money.length;
        if (n == 0) {
            return 0;
        }
        
        // 计算总金额
        int totalMoney = 0;
        for (int m : money) {
            totalMoney += m;
        }
        
        // dp[i]表示达到金额i所需的最小被抓概率
        double[] dp = new double[totalMoney + 1];
        Arrays.fill(dp, 1.0); // 初始化为最大概率1
        dp[0] = 0.0; // 抢劫0元时被抓概率为0
        
        for (int i = 0; i < n; i++) {
            int m = money[i];
            double p = prob[i];
            
            for (int j = totalMoney; j >= m; j--) {
                // 计算选择当前银行的被抓概率
                double newProb = 1 - (1 - dp[j - m]) * (1 - p);
                if (newProb < dp[j]) {
                    dp[j] = newProb;
                }
            }
        }
        
        // 寻找最大的金额，使得被抓概率 <= P
        for (int j = totalMoney; j >= 0; j--) {
            if (dp[j] <= P) {
                return j;
            }
        }
        
        return 0;
    }
    
    /**
     * 递归+记忆化搜索解法
     */
    public static double robDFS(double P, int[] money, double[] prob) {
        if (money == null || prob == null || money.length != prob.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (P < 0 || P > 1) {
            throw new IllegalArgumentException("Probability P must be between 0 and 1");
        }
        
        int n = money.length;
        if (n == 0) {
            return 0;
        }
        
        // 计算总金额
        int totalMoney = 0;
        for (int m : money) {
            totalMoney += m;
        }
        
        // 记忆化数组
        Double[][] memo = new Double[n][totalMoney + 1];
        return dfs(money, prob, 0, 0, 1.0, P, totalMoney, memo);
    }
    
    private static double dfs(int[] money, double[] prob, int index, int currentMoney, 
                             double currentSafeProb, double maxCaughtProb, 
                             int totalMoney, Double[][] memo) {
        // 基础情况：遍历完所有银行
        if (index == money.length) {
            return currentMoney;
        }
        
        // 检查记忆化数组
        if (memo[index][currentMoney] != null) {
            return memo[index][currentMoney];
        }
        
        double maxMoney = 0;
        
        // 不抢劫当前银行
        maxMoney = Math.max(maxMoney, 
            dfs(money, prob, index + 1, currentMoney, currentSafeProb, 
                maxCaughtProb, totalMoney, memo));
        
        // 抢劫当前银行
        int newMoney = currentMoney + money[index];
        double newSafeProb = currentSafeProb * (1 - prob[index]);
        double newCaughtProb = 1 - newSafeProb;
        
        // 如果抢劫后被抓概率不超过限制，则可以选择抢劫
        if (newCaughtProb <= maxCaughtProb) {
            maxMoney = Math.max(maxMoney, 
                dfs(money, prob, index + 1, newMoney, newSafeProb, 
                    maxCaughtProb, totalMoney, memo));
        }
        
        memo[index][currentMoney] = maxMoney;
        return maxMoney;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        double[] P = {0.1, 0.05, 0.5};
        int[][] moneyCases = {
            {10, 20, 30},
            {1, 2, 3, 4},
            {100, 200, 300}
        };
        double[][] probCases = {
            {0.05, 0.1, 0.2},
            {0.01, 0.02, 0.03, 0.04},
            {0.3, 0.2, 0.1}
        };
        
        System.out.println("抢劫银行问题测试：");
        for (int i = 0; i < P.length; i++) {
            double p = P[i];
            int[] money = moneyCases[i];
            double[] prob = probCases[i];
            
            double result1 = rob(p, money, prob);
            double result2 = robOptimized(p, money, prob);
            double result3 = robAlternative(p, money, prob);
            double result4 = robDFS(p, money, prob);
            
            System.out.printf("P=%.2f, money=%s, prob=%s: 方法1=%.0f, 方法2=%.0f, 方法3=%.0f, 方法4=%.0f%n",
                            p, Arrays.toString(money), Arrays.toString(prob),
                            result1, result2, result3, result4);
            
            // 验证结果一致性（允许小的浮点数误差）
            if (Math.abs(result1 - result2) > 1e-6 || 
                Math.abs(result2 - result3) > 1e-6 ||
                Math.abs(result3 - result4) > 1e-6) {
                System.out.println("警告：不同方法结果不一致！");
            }
        }
        
        // 性能测试 - 大规模数据
        int n = 50;
        int[] largeMoney = new int[n];
        double[] largeProb = new double[n];
        
        // 生成随机测试数据
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < n; i++) {
            largeMoney[i] = random.nextInt(1000) + 1;
            largeProb[i] = random.nextDouble() * 0.1; // 概率在0-0.1之间
        }
        
        long startTime = System.currentTimeMillis();
        double largeResult = robOptimized(0.1, largeMoney, largeProb);
        long endTime = System.currentTimeMillis();
        
        System.out.printf("大规模测试: 银行数量=%d, 结果=%.0f, 耗时=%dms%n",
                        n, largeResult, endTime - startTime);
        
        // 边界情况测试
        System.out.println("边界情况测试：");
        System.out.println("空数组: " + rob(0.1, new int[]{}, new double[]{}));
        System.out.println("P=0: " + rob(0.0, new int[]{10}, new double[]{0.1}));
        System.out.println("P=1: " + rob(1.0, new int[]{10, 20}, new double[]{0.1, 0.2}));
        
        // 特殊测试：概率为0的银行
        System.out.println("概率为0的银行测试：");
        double specialResult = rob(0.01, new int[]{100, 200}, new double[]{0.0, 0.0});
        System.out.println("特殊测试结果: " + specialResult);
    }
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（概率背包）
 * - 时间复杂度：O(N * totalMoney)
 *   - N: 银行数量
 *   - totalMoney: 总金额
 * - 空间复杂度：O(totalMoney)
 * 
 * 方法2：优化的动态规划
 * - 时间复杂度：O(N * totalMoney)（与方法1相同）
 * - 空间复杂度：O(totalMoney)
 * 
 * 方法3：替代思路的动态规划
 * - 时间复杂度：O(N * totalMoney)
 * - 空间复杂度：O(totalMoney)
 * 
 * 方法4：递归+记忆化搜索
 * - 时间复杂度：O(N * totalMoney)（每个状态计算一次）
 * - 空间复杂度：O(N * totalMoney)（记忆化数组）
 * 
 * 关键点分析：
 * 1. 问题转化：将概率问题转化为标准的背包问题
 * 2. 精度处理：使用double类型处理概率计算
 * 3. 状态定义：dp[i]表示金额i对应的最大安全概率
 * 4. 结果提取：从后向前遍历找到第一个满足条件的金额
 * 
 * 工程化考量：
 * 1. 精度控制：处理浮点数比较和计算
 * 2. 异常处理：验证输入参数的合法性
 * 3. 性能优化：计算总金额作为背包容量上限
 * 4. 测试覆盖：包含正常、边界、性能测试
 * 
 * 面试要点：
 * 1. 理解概率背包问题的转化思路
 * 2. 掌握浮点数精度处理技巧
 * 3. 了解不同状态定义对算法的影响
 * 4. 能够分析算法的时空复杂度
 * 
 * 扩展应用：
 * 1. 风险管理问题
 * 2. 投资组合优化
 * 3. 资源分配问题
 * 4. 多约束优化问题
 */
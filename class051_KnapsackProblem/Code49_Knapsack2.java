package class073;

import java.util.Arrays;

// AtCoder DP Contest E - Knapsack 2
// 题目描述：经典的01背包问题，但是背包容量非常大（10^9），而物品价值比较小（10^3）。
// 链接：https://atcoder.jp/contests/dp/tasks/dp_e
// 
// 解题思路：
// 当背包容量非常大时，传统的DP方法会超时或超内存。
// 需要转换思路：将价值作为背包容量，求达到某个价值所需的最小重量。
// 
// 状态定义：dp[i] 表示达到价值i所需的最小重量
// 状态转移方程：dp[i] = min(dp[i], dp[i-value[j]] + weight[j])
// 初始状态：dp[0] = 0，其他为无穷大
// 
// 时间复杂度：O(N * totalValue)，其中N是物品数量，totalValue是总价值
// 空间复杂度：O(totalValue)
// 
// 工程化考量：
// 1. 问题转化：从重量维度转为价值维度
// 2. 边界处理：处理无穷大值和结果提取
// 3. 性能优化：计算总价值作为新背包容量
// 4. 异常处理：处理空输入和边界值

public class Code49_Knapsack2 {
    
    private static final long INF = Long.MAX_VALUE / 2; // 表示不可达状态
    
    /**
     * 动态规划解法 - 价值维度DP
     * @param W 背包容量
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @return 能装入背包的最大价值
     */
    public static long knapsack2(long W, int[] weights, int[] values) {
        // 参数验证
        if (weights == null || values == null || weights.length != values.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (W < 0) {
            throw new IllegalArgumentException("Capacity W must be non-negative");
        }
        
        int n = weights.length;
        if (n == 0) {
            return 0;
        }
        
        // 计算总价值
        int totalValue = 0;
        for (int value : values) {
            totalValue += value;
        }
        
        // 创建DP数组，dp[i]表示达到价值i所需的最小重量
        long[] dp = new long[totalValue + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0; // 价值为0时重量为0
        
        // 遍历每个物品
        for (int i = 0; i < n; i++) {
            int weight = weights[i];
            int value = values[i];
            
            // 倒序遍历价值，避免重复选择
            for (int j = totalValue; j >= value; j--) {
                if (dp[j - value] != INF) {
                    dp[j] = Math.min(dp[j], dp[j - value] + weight);
                }
            }
        }
        
        // 寻找最大的价值，使得所需重量 <= W
        long maxValue = 0;
        for (int j = totalValue; j >= 0; j--) {
            if (dp[j] <= W) {
                maxValue = j;
                break;
            }
        }
        
        return maxValue;
    }
    
    /**
     * 优化的动态规划解法 - 提前终止
     */
    public static long knapsack2Optimized(long W, int[] weights, int[] values) {
        if (weights == null || values == null || weights.length != values.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (W < 0) {
            throw new IllegalArgumentException("Capacity W must be non-negative");
        }
        
        int n = weights.length;
        if (n == 0) {
            return 0;
        }
        
        // 计算总价值
        int totalValue = 0;
        for (int value : values) {
            totalValue += value;
        }
        
        long[] dp = new long[totalValue + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        
        for (int i = 0; i < n; i++) {
            int weight = weights[i];
            int value = values[i];
            
            for (int j = totalValue; j >= value; j--) {
                if (dp[j - value] != INF) {
                    long newWeight = dp[j - value] + weight;
                    if (newWeight < dp[j]) {
                        dp[j] = newWeight;
                    }
                }
            }
        }
        
        // 从大到小遍历，找到第一个满足条件的价值
        for (int j = totalValue; j >= 0; j--) {
            if (dp[j] <= W) {
                return j;
            }
        }
        
        return 0;
    }
    
    /**
     * 空间优化的解法 - 使用滚动数组思想
     */
    public static long knapsack2SpaceOptimized(long W, int[] weights, int[] values) {
        if (weights == null || values == null || weights.length != values.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (W < 0) {
            throw new IllegalArgumentException("Capacity W must be non-negative");
        }
        
        int n = weights.length;
        if (n == 0) {
            return 0;
        }
        
        // 计算总价值
        int totalValue = 0;
        for (int value : values) {
            totalValue += value;
        }
        
        // 使用两个数组交替，优化空间
        long[] dp = new long[totalValue + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        
        for (int i = 0; i < n; i++) {
            int weight = weights[i];
            int value = values[i];
            
            // 创建临时数组用于更新
            long[] temp = dp.clone();
            
            for (int j = value; j <= totalValue; j++) {
                if (temp[j - value] != INF) {
                    dp[j] = Math.min(dp[j], temp[j - value] + weight);
                }
            }
        }
        
        // 寻找最大价值
        for (int j = totalValue; j >= 0; j--) {
            if (dp[j] <= W) {
                return j;
            }
        }
        
        return 0;
    }
    
    /**
     * 传统DP解法（用于对比）- 仅适用于小容量
     */
    public static long knapsackTraditional(long W, int[] weights, int[] values) {
        if (weights == null || values == null || weights.length != values.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (W < 0) {
            throw new IllegalArgumentException("Capacity W must be non-negative");
        }
        
        int n = weights.length;
        if (n == 0) {
            return 0;
        }
        
        // 传统DP：dp[i]表示容量为i时的最大价值
        int maxW = (int) Math.min(W, Integer.MAX_VALUE);
        long[] dp = new long[maxW + 1];
        
        for (int i = 0; i < n; i++) {
            int weight = weights[i];
            int value = values[i];
            
            for (int j = maxW; j >= weight; j--) {
                dp[j] = Math.max(dp[j], dp[j - weight] + value);
            }
        }
        
        return dp[maxW];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        long[] W = {100, 1000, 1000000000L};
        int[][] weightsCases = {
            {10, 20, 30},
            {50, 100, 150},
            {1, 2, 3}
        };
        int[][] valuesCases = {
            {60, 100, 120},
            {60, 100, 120},
            {10, 15, 40}
        };
        
        System.out.println("大容量背包问题测试：");
        for (int i = 0; i < W.length; i++) {
            long w = W[i];
            int[] weights = weightsCases[i];
            int[] values = valuesCases[i];
            
            long result1 = knapsack2(w, weights, values);
            long result2 = knapsack2Optimized(w, weights, values);
            long result3 = knapsack2SpaceOptimized(w, weights, values);
            
            // 对于小容量，可以用传统方法验证
            long traditionalResult = 0;
            if (w <= 10000) {
                traditionalResult = knapsackTraditional(w, weights, values);
            }
            
            System.out.printf("W=%d, weights=%s, values=%s: 方法1=%d, 方法2=%d, 方法3=%d",
                            w, Arrays.toString(weights), Arrays.toString(values),
                            result1, result2, result3);
            
            if (w <= 10000) {
                System.out.printf(", 传统方法=%d", traditionalResult);
                // 验证结果一致性
                if (result1 != traditionalResult || result2 != traditionalResult || 
                    result3 != traditionalResult) {
                    System.out.println(" - 警告：结果不一致！");
                } else {
                    System.out.println(" - 验证通过");
                }
            } else {
                System.out.println(" - 大容量测试");
            }
        }
        
        // 性能测试 - 大规模数据
        int n = 100;
        int[] largeWeights = new int[n];
        int[] largeValues = new int[n];
        
        // 生成随机测试数据
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < n; i++) {
            largeWeights[i] = random.nextInt(1000) + 1;
            largeValues[i] = random.nextInt(100) + 1;
        }
        
        long largeW = 1000000000L; // 10^9
        
        long startTime = System.currentTimeMillis();
        long largeResult = knapsack2Optimized(largeW, largeWeights, largeValues);
        long endTime = System.currentTimeMillis();
        
        System.out.printf("大规模测试: 物品数量=%d, 容量=%d, 结果=%d, 耗时=%dms%n",
                        n, largeW, largeResult, endTime - startTime);
        
        // 边界情况测试
        System.out.println("边界情况测试：");
        System.out.println("空数组: " + knapsack2(100, new int[]{}, new int[]{}));
        System.out.println("W=0: " + knapsack2(0, new int[]{10}, new int[]{5}));
        System.out.println("所有物品超重: " + knapsack2(5, new int[]{10, 20}, new int[]{5, 10}));
        
        // 特殊测试：价值为0的物品
        System.out.println("价值为0的物品测试：");
        long specialResult = knapsack2(100, new int[]{10, 20}, new int[]{0, 0});
        System.out.println("特殊测试结果: " + specialResult);
    }
}

/*
 * 复杂度分析：
 * 
 * 方法1：价值维度DP
 * - 时间复杂度：O(N * totalValue)
 *   - N: 物品数量
 *   - totalValue: 总价值
 * - 空间复杂度：O(totalValue)
 * 
 * 方法2：优化的价值维度DP
 * - 时间复杂度：O(N * totalValue)（与方法1相同）
 * - 空间复杂度：O(totalValue)
 * 
 * 方法3：空间优化的DP
 * - 时间复杂度：O(N * totalValue)
 * - 空间复杂度：O(totalValue)（但使用临时数组）
 * 
 * 方法4：传统重量维度DP
 * - 时间复杂度：O(N * W)
 * - 空间复杂度：O(W)
 * 
 * 关键点分析：
 * 1. 问题转化：当W很大时，从重量维度转为价值维度
 * 2. 状态定义：dp[i]表示达到价值i所需的最小重量
 * 3. 结果提取：从后向前遍历找到第一个满足重量约束的价值
 * 4. 适用场景：W很大但总价值不大的情况
 * 
 * 工程化考量：
 * 1. 方法选择：根据W的大小选择合适的算法
 * 2. 内存优化：使用滚动数组思想
 * 3. 边界处理：处理各种极端情况
 * 4. 性能测试：包含大规模数据测试
 * 
 * 面试要点：
 * 1. 理解传统DP的局限性
 * 2. 掌握问题转化的思路
 * 3. 了解不同维度DP的适用场景
 * 4. 能够分析算法的时空复杂度
 * 
 * 扩展应用：
 * 1. 资源分配问题
 * 2. 投资组合优化
 * 3. 多约束优化问题
 * 4. 大规模数据处理
 */
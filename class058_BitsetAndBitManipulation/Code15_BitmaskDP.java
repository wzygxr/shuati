package class032;

import java.util.*;

/**
 * Codeforces 580D Kefa and Dishes - 状态压缩DP
 * 题目链接: https://codeforces.com/problemset/problem/580/D
 * 题目描述: 有n道菜，每道菜有一个满意度值。选择m道菜来吃，使得总满意度最大。
 * 此外，有些菜组合在一起吃会有额外的满意度加成（当y紧跟在x后面时）。
 * 
 * 解题思路:
 * 方法1: 状态压缩DP - 使用bitmask表示已选择的菜品
 * 方法2: 记忆化搜索 - 递归+记忆化
 * 方法3: 迭代DP - 使用二维DP数组
 * 
 * 时间复杂度分析:
 * 方法1: O(2^N * N^2) - 状态压缩DP的标准复杂度
 * 方法2: O(2^N * N) - 记忆化搜索，实际运行可能更快
 * 方法3: O(2^N * N^2) - 与方法1相同，但实现方式不同
 * 
 * 空间复杂度分析:
 * 方法1: O(2^N * N) - DP数组空间
 * 方法2: O(2^N * N) - 记忆化数组空间
 * 方法3: O(2^N * N) - DP数组空间
 * 
 * 工程化考量:
 * 1. 状态表示: 使用bitmask高效表示子集
 * 2. 转移优化: 利用位运算快速进行状态转移
 * 3. 内存优化: 对于大N使用滚动数组或优化状态表示
 * 4. 边界处理: 处理m=0,1的特殊情况
 */

public class Code15_BitmaskDP {
    
    /**
     * 方法1: 状态压缩DP（迭代版本）
     * 使用dp[mask][last]表示在mask状态下最后吃的菜是last时的最大满意度
     */
    public static long kefaAndDishesDP(int n, int m, int[] satisfaction, int[][] rules) {
        if (m <= 0 || n <= 0) return 0;
        if (m > n) m = n;  // 不能选择超过n道菜
        
        int totalStates = 1 << n;
        long[][] dp = new long[totalStates][n];
        
        // 初始化：只选择一道菜的情况
        for (int i = 0; i < n; i++) {
            int mask = 1 << i;
            dp[mask][i] = satisfaction[i];
        }
        
        long maxSatisfaction = 0;
        
        // 遍历所有状态
        for (int mask = 1; mask < totalStates; mask++) {
            int count = Integer.bitCount(mask);
            
            for (int last = 0; last < n; last++) {
                // 如果last不在mask中，跳过
                if ((mask & (1 << last)) == 0) continue;
                
                // 如果当前状态正好选择了m道菜，更新最大值
                if (count == m) {
                    maxSatisfaction = Math.max(maxSatisfaction, dp[mask][last]);
                }
                
                // 如果已经达到m道菜，不再继续添加
                if (count >= m) continue;
                
                // 尝试添加新的菜
                for (int next = 0; next < n; next++) {
                    // 如果next已经在mask中，跳过
                    if ((mask & (1 << next)) != 0) continue;
                    
                    int newMask = mask | (1 << next);
                    long newValue = dp[mask][last] + satisfaction[next];
                    
                    // 添加规则加成
                    if (rules[last][next] > 0) {
                        newValue += rules[last][next];
                    }
                    
                    if (newValue > dp[newMask][next]) {
                        dp[newMask][next] = newValue;
                    }
                }
            }
        }
        
        return maxSatisfaction;
    }
    
    /**
     * 方法2: 记忆化搜索（递归版本）
     * 使用递归+记忆化，代码更清晰
     */
    public static long kefaAndDishesMemo(int n, int m, int[] satisfaction, int[][] rules) {
        if (m <= 0 || n <= 0) return 0;
        if (m > n) m = n;
        
        long[][] memo = new long[1 << n][n];
        for (long[] row : memo) {
            Arrays.fill(row, -1);
        }
        
        long maxSatisfaction = 0;
        for (int i = 0; i < n; i++) {
            maxSatisfaction = Math.max(maxSatisfaction, 
                dfs(1 << i, i, 1, n, m, satisfaction, rules, memo));
        }
        
        return maxSatisfaction;
    }
    
    private static long dfs(int mask, int last, int count, int n, int m, 
                           int[] satisfaction, int[][] rules, long[][] memo) {
        if (count == m) {
            return satisfaction[last];
        }
        
        if (memo[mask][last] != -1) {
            return memo[mask][last];
        }
        
        long maxValue = 0;
        for (int next = 0; next < n; next++) {
            if ((mask & (1 << next)) != 0) continue;
            
            int newMask = mask | (1 << next);
            long value = dfs(newMask, next, count + 1, n, m, satisfaction, rules, memo);
            
            // 添加规则加成
            if (rules[last][next] > 0) {
                value += rules[last][next];
            }
            
            maxValue = Math.max(maxValue, value);
        }
        
        memo[mask][last] = maxValue + satisfaction[last];
        return memo[mask][last];
    }
    
    /**
     * 方法3: 优化版本 - 使用一维DP数组
     * 空间优化，只记录当前状态的最大值
     */
    public static long kefaAndDishesOptimized(int n, int m, int[] satisfaction, int[][] rules) {
        if (m <= 0 || n <= 0) return 0;
        if (m > n) m = n;
        
        int totalStates = 1 << n;
        long[] dp = new long[totalStates];
        
        // 初始化单菜品状态
        for (int i = 0; i < n; i++) {
            dp[1 << i] = satisfaction[i];
        }
        
        long maxSatisfaction = 0;
        
        for (int mask = 1; mask < totalStates; mask++) {
            int count = Integer.bitCount(mask);
            if (count > m) continue;
            
            for (int last = 0; last < n; last++) {
                if ((mask & (1 << last)) == 0) continue;
                
                if (count == m) {
                    maxSatisfaction = Math.max(maxSatisfaction, dp[mask]);
                }
                
                for (int next = 0; next < n; next++) {
                    if ((mask & (1 << next)) != 0) continue;
                    
                    int newMask = mask | (1 << next);
                    long newValue = dp[mask] + satisfaction[next];
                    
                    // 添加规则加成
                    if (rules[last][next] > 0) {
                        newValue += rules[last][next];
                    }
                    
                    if (newValue > dp[newMask]) {
                        dp[newMask] = newValue;
                    }
                }
            }
        }
        
        return maxSatisfaction;
    }
    
    /**
     * 工程化改进版本：完整的异常处理和验证
     */
    public static long kefaAndDishesWithValidation(int n, int m, int[] satisfaction, int[][] rules) {
        try {
            // 输入验证
            if (n <= 0 || m <= 0) {
                throw new IllegalArgumentException("n and m must be positive");
            }
            if (satisfaction == null || satisfaction.length != n) {
                throw new IllegalArgumentException("satisfaction array size must match n");
            }
            if (rules == null || rules.length != n || rules[0].length != n) {
                throw new IllegalArgumentException("rules must be n x n matrix");
            }
            
            // 检查满意度值是否合理
            for (int s : satisfaction) {
                if (s < 0) {
                    throw new IllegalArgumentException("satisfaction values must be non-negative");
                }
            }
            
            return kefaAndDishesOptimized(n, m, satisfaction, rules);
            
        } catch (Exception e) {
            System.err.println("Error in kefaAndDishes: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== Codeforces 580D Kefa and Dishes - 单元测试 ===");
        
        // 测试用例1: 简单情况
        int n1 = 3, m1 = 2;
        int[] satisfaction1 = {1, 2, 3};
        int[][] rules1 = new int[3][3];
        rules1[0][1] = 10;  // 菜0后吃菜1有10点加成
        
        long expected1 = 13;  // 菜0(1) + 菜1(2) + 加成10 = 13
        long result1 = kefaAndDishesWithValidation(n1, m1, satisfaction1, rules1);
        System.out.printf("测试1: n=%d, m=%d, 期望=%d, 实际=%d, %s%n",
                         n1, m1, expected1, result1,
                         result1 == expected1 ? "通过" : "失败");
        
        // 测试用例2: 边界情况 m=1
        int n2 = 3, m2 = 1;
        int[] satisfaction2 = {5, 10, 15};
        int[][] rules2 = new int[3][3];
        
        long expected2 = 15;  // 最大满意度是15
        long result2 = kefaAndDishesWithValidation(n2, m2, satisfaction2, rules2);
        System.out.printf("测试2: n=%d, m=%d, 期望=%d, 实际=%d, %s%n",
                         n2, m2, expected2, result2,
                         result2 == expected2 ? "通过" : "失败");
        
        // 测试不同方法的结果一致性
        System.out.println("\n=== 方法一致性测试 ===");
        int testN = 4, testM = 3;
        int[] testSatisfaction = {1, 2, 3, 4};
        int[][] testRules = new int[4][4];
        testRules[0][1] = 5;
        testRules[1][2] = 10;
        
        long r1 = kefaAndDishesDP(testN, testM, testSatisfaction, testRules);
        long r2 = kefaAndDishesMemo(testN, testM, testSatisfaction, testRules);
        long r3 = kefaAndDishesOptimized(testN, testM, testSatisfaction, testRules);
        
        System.out.printf("迭代DP: %d%n", r1);
        System.out.printf("记忆化搜索: %d%n", r2);
        System.out.printf("优化版本: %d%n", r3);
        System.out.printf("所有方法结果一致: %s%n", 
                         (r1 == r2 && r2 == r3) ? "是" : "否");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 测试不同规模的数据
        int[] testSizes = {10, 15, 18};  // 状态压缩DP通常处理N<=20
        
        for (int n : testSizes) {
            int m = n / 2;  // 选择一半的菜品
            int[] satisfaction = generateRandomArray(n, 100);
            int[][] rules = generateRandomRules(n, 20);
            
            System.out.printf("n = %d, m = %d:%n", n, m);
            
            // 测试迭代DP
            long startTime = System.nanoTime();
            long result1 = kefaAndDishesDP(n, m, satisfaction, rules);
            long time1 = System.nanoTime() - startTime;
            System.out.printf("  迭代DP: %d ns, 结果: %d%n", time1, result1);
            
            // 测试记忆化搜索
            startTime = System.nanoTime();
            long result2 = kefaAndDishesMemo(n, m, satisfaction, rules);
            long time2 = System.nanoTime() - startTime;
            System.out.printf("  记忆化搜索: %d ns, 结果: %d%n", time2, result2);
            
            // 测试优化版本
            startTime = System.nanoTime();
            long result3 = kefaAndDishesOptimized(n, m, satisfaction, rules);
            long time3 = System.nanoTime() - startTime;
            System.out.printf("  优化版本: %d ns, 结果: %d%n", time3, result3);
            
            System.out.println();
        }
    }
    
    /**
     * 生成随机满意度数组
     */
    private static int[] generateRandomArray(int size, int maxValue) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(maxValue) + 1;
        }
        return arr;
    }
    
    /**
     * 生成随机规则矩阵
     */
    private static int[][] generateRandomRules(int size, int maxBonus) {
        Random random = new Random();
        int[][] rules = new int[size][size];
        // 随机生成一些规则
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j && random.nextDouble() < 0.3) {  // 30%的概率有规则
                    rules[i][j] = random.nextInt(maxBonus) + 1;
                }
            }
        }
        return rules;
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("=== 复杂度分析 ===");
        System.out.println("状态压缩DP的核心思想:");
        System.out.println("  使用二进制位掩码表示子集状态");
        System.out.println("  每个状态mask表示选择了哪些菜品");
        System.out.println("  状态转移: 从当前状态扩展到包含新菜品的状态");
        
        System.out.println("\n时间复杂度分析:");
        System.out.println("  状态数量: 2^N");
        System.out.println("  每个状态需要遍历N个可能的最后菜品");
        System.out.println("  每个最后菜品需要尝试N个新菜品");
        System.out.println("  总复杂度: O(2^N * N^2)");
        
        System.out.println("\n空间复杂度分析:");
        System.out.println("  DP数组大小: 2^N * N");
        System.out.println("  实际可处理规模: N <= 20 (2^20 ≈ 1百万状态)");
        
        System.out.println("\n优化技巧:");
        System.out.println("1. 使用Integer.bitCount()快速计算已选菜品数");
        System.out.println("2. 利用位运算快速判断菜品是否已选");
        System.out.println("3. 对于固定m的情况，可以提前终止");
        System.out.println("4. 使用一维DP数组进行空间优化");
    }
    
    public static void main(String[] args) {
        System.out.println("Codeforces 580D Kefa and Dishes - 状态压缩DP");
        System.out.println("使用位掩码技术解决组合优化问题");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 示例使用
        System.out.println("\n=== 示例使用 ===");
        int n = 4, m = 3;
        int[] satisfaction = {5, 10, 15, 20};
        int[][] rules = new int[4][4];
        rules[0][1] = 5;
        rules[1][2] = 10;
        rules[2][3] = 15;
        
        long result = kefaAndDishesWithValidation(n, m, satisfaction, rules);
        System.out.printf("n=%d, m=%d时的最大满意度: %d%n", n, m, result);
        System.out.println("菜品满意度: " + Arrays.toString(satisfaction));
        System.out.println("规则加成矩阵:");
        for (int i = 0; i < n; i++) {
            System.out.println(Arrays.toString(rules[i]));
        }
    }
}
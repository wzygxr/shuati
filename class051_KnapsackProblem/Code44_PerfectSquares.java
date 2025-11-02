package class073;

import java.util.Arrays;

// LeetCode 279. 完全平方数
// 题目描述：给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）使得它们的和等于 n。
// 你需要让组成和的完全平方数的个数最少。
// 链接：https://leetcode.cn/problems/perfect-squares/
// 
// 解题思路：
// 这是一个完全背包问题，其中：
// - 背包容量：正整数 n
// - 物品：完全平方数（1, 4, 9, 16, ...）
// - 每个物品可以无限次使用（完全背包）
// - 目标：使用最少数量的物品（完全平方数）装满背包
// 
// 状态定义：dp[i] 表示和为 i 的完全平方数的最少数量
// 状态转移方程：dp[i] = min(dp[i], dp[i - j*j] + 1)，其中 j*j <= i
// 初始状态：dp[0] = 0，dp[i] = Integer.MAX_VALUE（表示不可达）
// 
// 时间复杂度：O(n * √n)，其中 n 是给定的正整数
// 空间复杂度：O(n)，使用一维DP数组
// 
// 工程化考量：
// 1. 异常处理：处理 n <= 0 的情况
// 2. 边界条件：n=0时返回0，n=1时返回1
// 3. 性能优化：预先生成完全平方数列表
// 4. 可读性：清晰的变量命名和注释

public class Code44_PerfectSquares {
    
    /**
     * 动态规划解法 - 完全背包问题
     * @param n 目标正整数
     * @return 组成n的最少完全平方数个数
     */
    public static int numSquares(int n) {
        // 参数验证
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        
        // 特殊情况处理
        if (n == 1) return 1;
        
        // 创建DP数组，dp[i]表示和为i的最少完全平方数个数
        int[] dp = new int[n + 1];
        
        // 初始化DP数组，除了dp[0]=0外，其他初始化为最大值
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        // 遍历所有可能的完全平方数
        for (int i = 1; i * i <= n; i++) {
            int square = i * i;
            // 完全背包：正序遍历容量
            for (int j = square; j <= n; j++) {
                // 避免整数溢出
                if (dp[j - square] != Integer.MAX_VALUE) {
                    dp[j] = Math.min(dp[j], dp[j - square] + 1);
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 优化的动态规划解法 - 预先生成完全平方数列表
     * @param n 目标正整数
     * @return 组成n的最少完全平方数个数
     */
    public static int numSquaresOptimized(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        
        // 预先生成所有可能的完全平方数
        int maxSquareRoot = (int) Math.sqrt(n);
        int[] squares = new int[maxSquareRoot];
        for (int i = 1; i <= maxSquareRoot; i++) {
            squares[i - 1] = i * i;
        }
        
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        // 先遍历物品（完全平方数），再遍历容量
        for (int square : squares) {
            for (int j = square; j <= n; j++) {
                if (dp[j - square] != Integer.MAX_VALUE) {
                    dp[j] = Math.min(dp[j], dp[j - square] + 1);
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 数学解法 - 利用四平方定理
     * 拉格朗日四平方定理：每个正整数都可以表示为4个整数的平方和
     * 勒让德三平方定理：当且仅当n≠4^a(8b+7)时，n可以表示为3个整数的平方和
     * @param n 目标正整数
     * @return 组成n的最少完全平方数个数
     */
    public static int numSquaresMath(int n) {
        // 检查n是否是完全平方数
        if (isPerfectSquare(n)) {
            return 1;
        }
        
        // 检查是否满足勒让德三平方定理的排除条件
        if (checkLegendreThreeSquare(n)) {
            return 4;
        }
        
        // 检查是否可以表示为两个平方数之和
        for (int i = 1; i * i <= n; i++) {
            int j = n - i * i;
            if (isPerfectSquare(j)) {
                return 2;
            }
        }
        
        // 其他情况返回3
        return 3;
    }
    
    /**
     * 判断一个数是否是完全平方数
     */
    private static boolean isPerfectSquare(int x) {
        int sqrt = (int) Math.sqrt(x);
        return sqrt * sqrt == x;
    }
    
    /**
     * 检查是否满足勒让德三平方定理的排除条件
     * 即 n = 4^a(8b+7)
     */
    private static boolean checkLegendreThreeSquare(int n) {
        while (n % 4 == 0) {
            n /= 4;
        }
        return n % 8 == 7;
    }
    
    /**
     * BFS解法 - 将问题转化为图的最短路径问题
     * 每个数字是一个节点，如果两个数字相差一个完全平方数，则它们之间有边
     */
    public static int numSquaresBFS(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        
        // 使用队列进行BFS
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        // 记录到达每个数字的最短步数
        int[] steps = new int[n + 1];
        Arrays.fill(steps, -1);
        
        // 从0开始
        queue.offer(0);
        steps[0] = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            // 尝试所有可能的完全平方数
            for (int i = 1; i * i <= n - current; i++) {
                int next = current + i * i;
                
                // 如果超出范围或已经访问过，跳过
                if (next > n || steps[next] != -1) {
                    continue;
                }
                
                steps[next] = steps[current] + 1;
                
                // 如果到达目标，直接返回
                if (next == n) {
                    return steps[next];
                }
                
                queue.offer(next);
            }
        }
        
        return steps[n];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        int[] testCases = {12, 13, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        
        System.out.println("完全平方数问题测试：");
        for (int n : testCases) {
            int result1 = numSquares(n);
            int result2 = numSquaresOptimized(n);
            int result3 = numSquaresBFS(n);
            int result4 = numSquaresMath(n);
            
            System.out.printf("n=%d: DP=%d, Optimized=%d, BFS=%d, Math=%d%n", 
                            n, result1, result2, result3, result4);
            
            // 验证所有方法结果一致
            if (result1 != result2 || result2 != result3 || result3 != result4) {
                System.out.println("警告：不同方法结果不一致！");
            }
        }
        
        // 性能测试
        long startTime = System.currentTimeMillis();
        int largeResult = numSquares(10000);
        long endTime = System.currentTimeMillis();
        System.out.printf("n=10000 的结果: %d, 耗时: %dms%n", largeResult, endTime - startTime);
    }
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（完全背包）
 * - 时间复杂度：O(n * √n)
 *   - 外层循环：√n 次（完全平方数的个数）
 *   - 内层循环：n 次（背包容量）
 * - 空间复杂度：O(n)
 * 
 * 方法2：优化的动态规划
 * - 时间复杂度：O(n * √n)（与方法1相同，但常数更小）
 * - 空间复杂度：O(n)
 * 
 * 方法3：数学解法
 * - 时间复杂度：O(√n)
 *   - 检查完全平方数：O(1)
 *   - 检查勒让德条件：O(log n)
 *   - 检查两个平方数之和：O(√n)
 * - 空间复杂度：O(1)
 * 
 * 方法4：BFS解法
 * - 时间复杂度：O(n * √n)（最坏情况）
 * - 空间复杂度：O(n)
 * 
 * 最优解分析：
 * - 对于小规模n（n < 1000）：所有方法都很快
 * - 对于大规模n（n >= 10000）：数学解法最优，时间复杂度最低
 * - 在实际工程中：推荐使用动态规划，代码清晰易懂
 * 
 * 边界场景测试：
 * 1. n=0：应该返回0（根据题目定义，n是正整数，但需要处理边界）
 * 2. n=1：应该返回1（1本身就是完全平方数）
 * 3. n=2：应该返回2（1+1）
 * 4. n=3：应该返回3（1+1+1）
 * 5. n=4：应该返回1（4本身就是完全平方数）
 * 6. n=12：应该返回3（4+4+4）
 * 7. n=13：应该返回2（4+9）
 * 
 * 工程化考量：
 * 1. 异常处理：对非法输入抛出明确异常
 * 2. 性能优化：预计算完全平方数，避免重复计算
 * 3. 可读性：清晰的变量命名和详细注释
 * 4. 测试覆盖：包含正常情况、边界情况、性能测试
 * 5. 多解法对比：提供不同实现，便于理解和选择
 */
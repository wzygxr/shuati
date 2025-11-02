package class073;

import java.util.Arrays;

// UVA 10616 Divisible Group Sums
// 题目描述：给定N个整数，选择M个数字使得它们的和能被D整除，求方案数。
// 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1557
// 
// 解题思路：
// 这是一个分组背包+模数运算的问题，需要计算选择M个数字的和能被D整除的方案数。
// 
// 状态定义：dp[i][j][k] 表示前i个数字，选择j个数字，和对D取模为k的方案数
// 状态转移方程：
//   dp[i][j][k] = dp[i-1][j][k] + dp[i-1][j-1][(k - num[i] % D + D) % D]
// 
// 关键点：
// 1. 模数运算：处理负数取模，使用(k - num[i] % D + D) % D
// 2. 分组背包：每个数字只能选择一次
// 3. 空间优化：使用滚动数组优化空间复杂度
// 
// 时间复杂度：O(N * M * D)，其中N是数字数量，M是选择数量，D是除数
// 空间复杂度：O(M * D)，使用滚动数组优化
// 
// 工程化考量：
// 1. 模数处理：正确处理负数取模
// 2. 边界条件：处理M=0、D=0等特殊情况
// 3. 性能优化：使用滚动数组和模数运算优化
// 4. 异常处理：处理除数为0的情况

public class Code50_DivisibleGroupSums {
    
    /**
     * 动态规划解法 - 分组背包+模数运算
     * @param nums 整数数组
     * @param M 需要选择的数字个数
     * @param D 除数
     * @return 方案数
     */
    public static long divisibleGroupSums(int[] nums, int M, int D) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return M == 0 ? 1 : 0;
        }
        if (D == 0) {
            throw new IllegalArgumentException("Divisor D cannot be zero");
        }
        if (M < 0 || M > nums.length) {
            return 0;
        }
        
        int n = nums.length;
        
        // 创建DP数组，使用滚动数组优化
        long[][] dp = new long[M + 1][D];
        dp[0][0] = 1; // 选择0个数字，和为0（模D为0）的方案数为1
        
        // 遍历每个数字
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int mod = (num % D + D) % D; // 处理负数取模
            
            // 倒序遍历选择数量，避免重复选择
            for (int j = M; j >= 1; j--) {
                // 倒序遍历模数状态
                long[] temp = Arrays.copyOf(dp[j], D);
                for (int k = 0; k < D; k++) {
                    int prevMod = (k - mod + D) % D;
                    dp[j][k] += dp[j - 1][prevMod];
                }
            }
        }
        
        return dp[M][0];
    }
    
    /**
     * 优化的动态规划解法 - 二维数组
     */
    public static long divisibleGroupSums2D(int[] nums, int M, int D) {
        if (nums == null || nums.length == 0) {
            return M == 0 ? 1 : 0;
        }
        if (D == 0) {
            throw new IllegalArgumentException("Divisor D cannot be zero");
        }
        if (M < 0 || M > nums.length) {
            return 0;
        }
        
        int n = nums.length;
        
        // 创建三维DP数组
        long[][][] dp = new long[n + 1][M + 1][D];
        dp[0][0][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            int mod = (num % D + D) % D;
            
            for (int j = 0; j <= M; j++) {
                for (int k = 0; k < D; k++) {
                    // 不选当前数字
                    dp[i][j][k] += dp[i - 1][j][k];
                    
                    // 选当前数字
                    if (j >= 1) {
                        int prevMod = (k - mod + D) % D;
                        dp[i][j][k] += dp[i - 1][j - 1][prevMod];
                    }
                }
            }
        }
        
        return dp[n][M][0];
    }
    
    /**
     * 空间优化的解法 - 使用两个二维数组交替
     */
    public static long divisibleGroupSumsOptimized(int[] nums, int M, int D) {
        if (nums == null || nums.length == 0) {
            return M == 0 ? 1 : 0;
        }
        if (D == 0) {
            throw new IllegalArgumentException("Divisor D cannot be zero");
        }
        if (M < 0 || M > nums.length) {
            return 0;
        }
        
        int n = nums.length;
        
        // 使用两个二维数组交替
        long[][] dp = new long[M + 1][D];
        long[][] next = new long[M + 1][D];
        dp[0][0] = 1;
        
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int mod = (num % D + D) % D;
            
            // 复制当前状态到next
            for (int j = 0; j <= M; j++) {
                System.arraycopy(dp[j], 0, next[j], 0, D);
            }
            
            // 更新next数组
            for (int j = 1; j <= M; j++) {
                for (int k = 0; k < D; k++) {
                    int prevMod = (k - mod + D) % D;
                    next[j][k] += dp[j - 1][prevMod];
                }
            }
            
            // 交换数组
            long[][] temp = dp;
            dp = next;
            next = temp;
            
            // 清空next数组用于下一次迭代
            for (int j = 0; j <= M; j++) {
                Arrays.fill(next[j], 0);
            }
        }
        
        return dp[M][0];
    }
    
    /**
     * 递归+记忆化搜索解法
     */
    public static long divisibleGroupSumsDFS(int[] nums, int M, int D) {
        if (nums == null || nums.length == 0) {
            return M == 0 ? 1 : 0;
        }
        if (D == 0) {
            throw new IllegalArgumentException("Divisor D cannot be zero");
        }
        if (M < 0 || M > nums.length) {
            return 0;
        }
        
        int n = nums.length;
        Long[][][] memo = new Long[n][M + 1][D];
        return dfs(nums, 0, M, 0, D, memo);
    }
    
    private static long dfs(int[] nums, int index, int remaining, int currentMod, int D, Long[][][] memo) {
        // 基础情况
        if (index == nums.length) {
            return (remaining == 0 && currentMod == 0) ? 1 : 0;
        }
        
        // 检查记忆化数组
        if (memo[index][remaining][currentMod] != null) {
            return memo[index][remaining][currentMod];
        }
        
        long count = 0;
        
        // 不选当前数字
        count += dfs(nums, index + 1, remaining, currentMod, D, memo);
        
        // 选当前数字
        if (remaining > 0) {
            int num = nums[index];
            int mod = (num % D + D) % D;
            int newMod = (currentMod + mod) % D;
            count += dfs(nums, index + 1, remaining - 1, newMod, D, memo);
        }
        
        memo[index][remaining][currentMod] = count;
        return count;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        int[][] numsCases = {
            {1, 2, 3, 4, 5},
            {2, 4, 6, 8, 10},
            {-1, 1, -2, 2, -3, 3},
            {10, 20, 30, 40, 50}
        };
        int[] MList = {2, 3, 2, 3};
        int[] DList = {3, 2, 3, 10};
        
        System.out.println("可整除组和问题测试：");
        for (int i = 0; i < numsCases.length; i++) {
            int[] nums = numsCases[i];
            int M = MList[i];
            int D = DList[i];
            
            long result1 = divisibleGroupSums(nums, M, D);
            long result2 = divisibleGroupSums2D(nums, M, D);
            long result3 = divisibleGroupSumsOptimized(nums, M, D);
            long result4 = divisibleGroupSumsDFS(nums, M, D);
            
            System.out.printf("nums=%s, M=%d, D=%d: 方法1=%d, 方法2=%d, 方法3=%d, 方法4=%d%n",
                            Arrays.toString(nums), M, D, result1, result2, result3, result4);
            
            // 验证结果一致性
            if (result1 != result2 || result2 != result3 || result3 != result4) {
                System.out.println("警告：不同方法结果不一致！");
            }
        }
        
        // 性能测试 - 大规模数据
        int n = 50;
        int[] largeNums = new int[n];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < n; i++) {
            largeNums[i] = random.nextInt(1000) - 500; // 包含负数
        }
        int largeM = 10;
        int largeD = 7;
        
        long startTime = System.currentTimeMillis();
        long largeResult = divisibleGroupSumsOptimized(largeNums, largeM, largeD);
        long endTime = System.currentTimeMillis();
        
        System.out.printf("大规模测试: 数字数量=%d, M=%d, D=%d, 结果=%d, 耗时=%dms%n",
                        n, largeM, largeD, largeResult, endTime - startTime);
        
        // 边界情况测试
        System.out.println("边界情况测试：");
        System.out.println("空数组, M=0: " + divisibleGroupSums(new int[]{}, 0, 5));
        System.out.println("空数组, M=1: " + divisibleGroupSums(new int[]{}, 1, 5));
        System.out.println("M=0: " + divisibleGroupSums(new int[]{1, 2, 3}, 0, 5));
        System.out.println("M>n: " + divisibleGroupSums(new int[]{1, 2}, 3, 5));
        
        // 特殊测试：D=1（所有组合都满足）
        System.out.println("D=1特殊测试：");
        long specialResult = divisibleGroupSums(new int[]{1, 2, 3}, 2, 1);
        System.out.println("D=1, 预期=C(3,2)=3, 实际=" + specialResult);
        
        // 验证组合数公式
        long expected = combination(3, 2);
        if (specialResult == expected) {
            System.out.println("D=1测试验证通过");
        } else {
            System.out.println("D=1测试验证失败");
        }
    }
    
    /**
     * 计算组合数C(n, k)
     */
    private static long combination(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;
        
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - i + 1) / i;
        }
        return result;
    }
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（滚动数组）
 * - 时间复杂度：O(N * M * D)
 *   - N: 数字数量
 *   - M: 选择数量
 *   - D: 除数
 * - 空间复杂度：O(M * D)
 * 
 * 方法2：三维动态规划
 * - 时间复杂度：O(N * M * D)
 * - 空间复杂度：O(N * M * D)
 * 
 * 方法3：空间优化的动态规划
 * - 时间复杂度：O(N * M * D)
 * - 空间复杂度：O(M * D)（使用两个二维数组）
 * 
 * 方法4：递归+记忆化搜索
 * - 时间复杂度：O(N * M * D)（每个状态计算一次）
 * - 空间复杂度：O(N * M * D)（记忆化数组）
 * 
 * 关键点分析：
 * 1. 模数运算：正确处理负数取模，使用(k - mod + D) % D
 * 2. 状态定义：dp[i][j][k]表示前i个数字选j个模D为k的方案数
 * 3. 空间优化：使用滚动数组减少空间复杂度
 * 4. 边界处理：M=0时方案数为1（空选择）
 * 
 * 工程化考量：
 * 1. 参数验证：检查D=0、M越界等情况
 * 2. 性能优化：使用滚动数组和模数运算优化
 * 3. 测试覆盖：包含正常、边界、性能测试
 * 4. 特殊验证：D=1时验证组合数公式
 * 
 * 面试要点：
 * 1. 理解分组背包+模数运算的组合
 * 2. 掌握模数运算的处理技巧
 * 3. 了解不同DP实现的空间优化
 * 4. 能够分析算法的时空复杂度
 * 
 * 扩展应用：
 * 1. 数论问题中的模数运算
 * 2. 组合数学中的计数问题
 * 3. 密码学中的模数运算
 * 4. 分布式系统中的一致性哈希
 */
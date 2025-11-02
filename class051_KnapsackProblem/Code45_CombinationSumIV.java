package class073;

import java.util.Arrays;

// LeetCode 377. 组合总和 Ⅳ
// 题目描述：给定一个由不同整数组成的数组 nums 和一个目标整数 target，
// 请从 nums 中找出并返回总和为 target 的元素组合的个数。
// 链接：https://leetcode.cn/problems/combination-sum-iv/
// 
// 解题思路：
// 这是一个完全背包问题的排列数变种，需要计算所有可能的排列数。
// 与零钱兑换II不同，这里顺序不同的序列被视为不同的组合。
// 
// 状态定义：dp[i] 表示总和为 i 的元素组合个数
// 状态转移方程：dp[i] = sum(dp[i - num])，其中 num 是 nums 中的元素且 i >= num
// 初始状态：dp[0] = 1（空组合）
// 
// 关键点：为了计算排列数，需要将目标值循环放在外层，数组元素循环放在内层
// 
// 时间复杂度：O(target * n)，其中 n 是数组长度
// 空间复杂度：O(target)，使用一维DP数组
// 
// 工程化考量：
// 1. 异常处理：处理空数组、负数等情况
// 2. 整数溢出：使用long类型处理大数
// 3. 性能优化：排序数组进行剪枝
// 4. 边界条件：target=0时返回1

public class Code45_CombinationSumIV {
    
    /**
     * 动态规划解法 - 计算排列数
     * @param nums 不同整数组成的数组
     * @param target 目标整数
     * @return 总和为target的元素组合个数
     */
    public static int combinationSum4(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 特殊情况处理
        if (target == 0) {
            return 1; // 空组合
        }
        
        // 创建DP数组
        int[] dp = new int[target + 1];
        dp[0] = 1; // 空组合
        
        // 为了计算排列数，需要将目标值循环放在外层
        // 数组元素循环放在内层
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                }
            }
        }
        
        return dp[target];
    }
    
    /**
     * 优化的动态规划解法 - 处理整数溢出
     * 使用long类型避免整数溢出，最后转换为int
     */
    public static int combinationSum4Optimized(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 使用long数组避免整数溢出
        long[] dp = new long[target + 1];
        dp[0] = 1;
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                    // 如果超过int最大值，取模或返回最大值
                    if (dp[i] > Integer.MAX_VALUE) {
                        dp[i] = Integer.MAX_VALUE;
                    }
                }
            }
        }
        
        return (int) dp[target];
    }
    
    /**
     * 带剪枝优化的动态规划解法
     * 先排序数组，当num > i时提前终止内层循环
     */
    public static int combinationSum4WithPruning(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 排序数组，便于剪枝
        Arrays.sort(nums);
        int[] dp = new int[target + 1];
        dp[0] = 1;
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (num > i) {
                    break; // 剪枝：由于数组已排序，后续数字更大
                }
                dp[i] += dp[i - num];
            }
        }
        
        return dp[target];
    }
    
    /**
     * 递归+记忆化搜索解法
     */
    public static int combinationSum4DFS(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 使用记忆化数组
        Integer[] memo = new Integer[target + 1];
        return dfs(nums, target, memo);
    }
    
    /**
     * 递归辅助函数
     */
    private static int dfs(int[] nums, int target, Integer[] memo) {
        // 基础情况
        if (target == 0) {
            return 1;
        }
        if (target < 0) {
            return 0;
        }
        
        // 检查记忆化数组
        if (memo[target] != null) {
            return memo[target];
        }
        
        int count = 0;
        for (int num : nums) {
            count += dfs(nums, target - num, memo);
        }
        
        memo[target] = count;
        return count;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        int[][] testCases = {
            {1, 2, 3}, 4,  // 预期：7
            {9}, 3,         // 预期：0
            {1, 2, 3}, 0,   // 预期：1
            {1, 2, 3}, 1,   // 预期：1
            {1, 2, 3}, 2,   // 预期：2
            {1, 2, 3}, 3    // 预期：4
        };
        
        System.out.println("组合总和IV问题测试：");
        for (int i = 0; i < testCases.length; i += 2) {
            int[] nums = testCases[i];
            int target = testCases[i + 1];
            
            int result1 = combinationSum4(nums, target);
            int result2 = combinationSum4Optimized(nums, target);
            int result3 = combinationSum4WithPruning(nums, target);
            int result4 = combinationSum4DFS(nums, target);
            
            System.out.printf("nums=%s, target=%d: DP=%d, Optimized=%d, Pruning=%d, DFS=%d%n",
                            Arrays.toString(nums), target, result1, result2, result3, result4);
            
            // 验证结果一致性
            if (result1 != result2 || result2 != result3 || result3 != result4) {
                System.out.println("警告：不同方法结果不一致！");
            }
        }
        
        // 性能测试 - 大规模数据
        int[] largeNums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int largeTarget = 50;
        
        long startTime = System.currentTimeMillis();
        int largeResult = combinationSum4WithPruning(largeNums, largeTarget);
        long endTime = System.currentTimeMillis();
        
        System.out.printf("大规模测试: nums长度=%d, target=%d, 结果=%d, 耗时=%dms%n",
                        largeNums.length, largeTarget, largeResult, endTime - startTime);
        
        // 边界情况测试
        System.out.println("边界情况测试：");
        System.out.println("空数组, target=0: " + combinationSum4(new int[]{}, 0)); // 预期：1
        System.out.println("空数组, target=1: " + combinationSum4(new int[]{}, 1)); // 预期：0
        System.out.println("负数target: " + combinationSum4(new int[]{1, 2, 3}, -1)); // 预期：0
    }
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（排列数）
 * - 时间复杂度：O(target * n)
 *   - 外层循环：target 次
 *   - 内层循环：n 次（数组长度）
 * - 空间复杂度：O(target)
 * 
 * 方法2：优化的动态规划（处理溢出）
 * - 时间复杂度：O(target * n)（与方法1相同）
 * - 空间复杂度：O(target)
 * 
 * 方法3：带剪枝的动态规划
 * - 时间复杂度：O(target * n)（平均情况下由于剪枝可能更快）
 * - 空间复杂度：O(target)
 * 
 * 方法4：递归+记忆化搜索
 * - 时间复杂度：O(target * n)（每个状态计算一次）
 * - 空间复杂度：O(target)（递归栈深度+记忆化数组）
 * 
 * 关键点分析：
 * 1. 排列数 vs 组合数：本题需要计算排列数，因此遍历顺序很重要
 * 2. 整数溢出：当target较大时，结果可能超过int范围
 * 3. 剪枝优化：排序数组可以在内层循环提前终止
 * 
 * 与零钱兑换II的区别：
 * - 零钱兑换II：计算组合数（顺序不同的序列视为相同）
 * - 组合总和IV：计算排列数（顺序不同的序列视为不同）
 * 
 * 工程化考量：
 * 1. 异常防御：处理各种边界输入
 * 2. 性能优化：剪枝、记忆化等技术
 * 3. 可维护性：清晰的代码结构和注释
 * 4. 测试覆盖：包含正常、边界、性能测试
 * 
 * 面试要点：
 * 1. 理解排列数和组合数的区别
 * 2. 掌握动态规划的遍历顺序对结果的影响
 * 3. 能够处理整数溢出等边界情况
 * 4. 了解不同解法的优缺点和适用场景
 */
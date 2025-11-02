package class073;

// LeetCode 377. 组合总和 Ⅳ
// 题目描述：给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。
// 请你从 nums 中找出并返回总和为 target 的元素组合的个数。
// 注意：顺序不同的序列被视作不同的组合。
// 链接：https://leetcode.cn/problems/combination-sum-iv/
// 
// 解题思路：
// 这是一个完全背包问题的变种，但是与传统的完全背包问题不同，这里需要计算的是排列数而不是组合数。
// 对于排列数，我们需要先遍历容量（target），再遍历物品（nums数组），这样可以确保不同顺序的序列被视为不同的组合。
// 
// 状态定义：dp[i] 表示总和为i的元素组合的个数
// 状态转移方程：dp[i] += dp[i - num]，对于每个num，如果i >= num
// 初始状态：dp[0] = 1，表示总和为0的组合只有一种（空组合）
// 
// 时间复杂度：O(target * n)，其中n是nums数组的长度
// 空间复杂度：O(target)，使用一维DP数组

public class Code24_CombinationSum4 {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        int target1 = 4;
        System.out.println("测试用例1结果: " + combinationSum4(nums1, target1)); // 预期输出: 7
        // 解释: 所有可能的组合为：
        // (1, 1, 1, 1)
        // (1, 1, 2)
        // (1, 2, 1)
        // (1, 3)
        // (2, 1, 1)
        // (2, 2)
        // (3, 1)
        
        // 测试用例2
        int[] nums2 = {9};
        int target2 = 3;
        System.out.println("测试用例2结果: " + combinationSum4(nums2, target2)); // 预期输出: 0
        
        // 测试用例3
        int[] nums3 = {1, 2, 4};
        int target3 = 32;
        System.out.println("测试用例3结果: " + combinationSum4(nums3, target3)); // 大数测试
        
        // 测试用例4
        int[] nums4 = {1, 50};
        int target4 = 100;
        System.out.println("测试用例4结果: " + combinationSum4(nums4, target4)); // 预期输出: 3
    }
    
    /**
     * 找出总和为target的元素组合的个数
     * @param nums 不同整数组成的数组
     * @param target 目标整数
     * @return 总和为target的元素组合的个数
     */
    public static int combinationSum4(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 创建一维DP数组，dp[i]表示总和为i的元素组合的个数
        int[] dp = new int[target + 1];
        
        // 初始状态：总和为0的组合只有一种（空组合）
        dp[0] = 1;
        
        // 注意：为了计算排列数，我们先遍历容量（target），再遍历物品（nums数组）
        // 这样可以确保不同顺序的序列被视为不同的组合
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                // 状态转移：如果当前容量i大于等于物品重量num
                if (i >= num) {
                    // 防止整数溢出
                    if (dp[i] > Integer.MAX_VALUE - dp[i - num]) {
                        // 处理溢出情况
                        continue;
                    }
                    dp[i] += dp[i - num];
                }
            }
        }
        
        // 返回结果：总和为target的元素组合的个数
        return dp[target];
    }
    
    /**
     * 优化版本：剪枝处理
     */
    public static int combinationSum4Optimized(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 排序nums数组，方便后续剪枝
        java.util.Arrays.sort(nums);
        
        // 创建一维DP数组
        int[] dp = new int[target + 1];
        dp[0] = 1;
        
        // 先遍历容量，再遍历物品
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                // 剪枝：如果num大于i，后续的num会更大，不需要继续遍历
                if (num > i) {
                    break;
                }
                // 防止整数溢出
                if (dp[i] > Integer.MAX_VALUE - dp[i - num]) {
                    continue;
                }
                dp[i] += dp[i - num];
            }
        }
        
        return dp[target];
    }
    
    /**
     * 递归+记忆化搜索实现
     */
    public static int combinationSum4DFS(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 使用HashMap作为缓存
        java.util.Map<Integer, Integer> memo = new java.util.HashMap<>();
        
        // 调用递归辅助函数
        return dfs(nums, target, memo);
    }
    
    /**
     * 递归辅助函数
     * @param nums 数组
     * @param remaining 剩余需要达到的目标值
     * @param memo 缓存，键为剩余目标值，值为对应的组合数
     * @return 达到剩余目标值的组合数
     */
    private static int dfs(int[] nums, int remaining, java.util.Map<Integer, Integer> memo) {
        // 基础情况：剩余目标值为0，返回1（表示找到一种组合）
        if (remaining == 0) {
            return 1;
        }
        
        // 基础情况：剩余目标值小于0，返回0（表示无法找到组合）
        if (remaining < 0) {
            return 0;
        }
        
        // 检查缓存
        if (memo.containsKey(remaining)) {
            return memo.get(remaining);
        }
        
        // 计算所有可能的组合数
        int count = 0;
        for (int num : nums) {
            // 递归计算使用当前num后的组合数
            int result = dfs(nums, remaining - num, memo);
            // 防止整数溢出
            if (count > Integer.MAX_VALUE - result) {
                continue;
            }
            count += result;
        }
        
        // 缓存结果
        memo.put(remaining, count);
        
        return count;
    }
    
    /**
     * 使用数组作为缓存的优化DFS实现
     */
    public static int combinationSum4DFSWithArrayCache(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 使用数组作为缓存
        Integer[] cache = new Integer[target + 1];
        
        // 调用递归辅助函数
        return dfsWithArrayCache(nums, target, cache);
    }
    
    private static int dfsWithArrayCache(int[] nums, int remaining, Integer[] cache) {
        // 基础情况
        if (remaining == 0) {
            return 1;
        }
        if (remaining < 0) {
            return 0;
        }
        
        // 检查缓存
        if (cache[remaining] != null) {
            return cache[remaining];
        }
        
        // 计算组合数
        int count = 0;
        for (int num : nums) {
            int result = dfsWithArrayCache(nums, remaining - num, cache);
            if (count > Integer.MAX_VALUE - result) {
                continue;
            }
            count += result;
        }
        
        // 缓存结果
        cache[remaining] = count;
        
        return count;
    }
    
    /**
     * 使用long类型防止溢出的版本
     * 注意：LeetCode题目的测试用例可能会导致整数溢出
     */
    public static int combinationSum4WithLong(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 使用long类型的DP数组防止溢出
        long[] dp = new long[target + 1];
        dp[0] = 1;
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                }
            }
            // 如果结果超过Integer.MAX_VALUE，可能会溢出
            if (dp[i] > Integer.MAX_VALUE) {
                // 根据题目要求处理
                // 这里简单返回Integer.MAX_VALUE
                return Integer.MAX_VALUE;
            }
        }
        
        return (int) dp[target];
    }
    
    /**
     * 回溯算法实现（注意：对于大数会超时，仅作为参考）
     */
    public static int combinationSum4Backtracking(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 结果计数器
        java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger(0);
        
        // 调用回溯函数
        backtrack(nums, target, count);
        
        return count.get();
    }
    
    private static void backtrack(int[] nums, int remaining, java.util.concurrent.atomic.AtomicInteger count) {
        // 找到一个有效组合
        if (remaining == 0) {
            count.incrementAndGet();
            return;
        }
        
        // 超过目标值，直接返回
        if (remaining < 0) {
            return;
        }
        
        // 尝试每个数字
        for (int num : nums) {
            backtrack(nums, remaining - num, count);
        }
    }
}
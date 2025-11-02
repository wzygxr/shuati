package class073;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// LeetCode 377. 组合总和 IV
// 题目描述：给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。
// 请你从 nums 中找出并返回总和为 target 的元素组合的个数。
// 注意：顺序不同的序列被视作不同的组合。
// 链接：https://leetcode.cn/problems/combination-sum-iv/
// 
// 解题思路：
// 这是一个与完全背包相关但关注排列而非组合的问题：
// - 数字可以多次使用（完全背包的特点）
// - 顺序不同的序列视为不同的组合（与组合数问题的关键区别）
// 
// 状态定义：dp[i] 表示凑成目标值i的不同排列数
// 状态转移方程：dp[i] += dp[i - num]，其中num是nums中的每个元素，且i >= num
// 初始状态：dp[0] = 1（凑成目标值0只有一种方式：不选择任何数字）
// 
// 时间复杂度：O(target * n)，其中n是数组nums的长度
// 空间复杂度：O(target)，使用一维DP数组

public class Code33_CombinationSumIV {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        int target1 = 4;
        System.out.println("测试用例1结果: " + combinationSum4(nums1, target1)); // 预期输出: 7 ([1,1,1,1], [1,1,2], [1,2,1], [1,3], [2,1,1], [2,2], [3,1])
        
        // 测试用例2
        int[] nums2 = {9};
        int target2 = 3;
        System.out.println("测试用例2结果: " + combinationSum4(nums2, target2)); // 预期输出: 0
    }
    
    /**
     * 计算凑成目标值的不同排列数
     * @param nums 不同整数组成的数组
     * @param target 目标整数
     * @return 总和为target的元素组合的个数
     */
    public static int combinationSum4(int[] nums, int target) {
        // 参数验证
        if (target < 0) {
            return 0;
        }
        if (target == 0) {
            return 1;
        }
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 创建DP数组，dp[i]表示凑成目标值i的不同排列数
        // 注意：由于可能的数值较大，我们需要处理溢出问题
        // 这里使用long类型，最后再转换为int
        long[] dp = new long[target + 1];
        dp[0] = 1; // 凑成目标值0只有一种方式：不选择任何数字
        
        // 遍历目标值，从1到target
        // 注意：与零钱兑换II不同，这里我们先遍历目标值，再遍历数组元素，这样可以考虑不同顺序的排列
        for (int i = 1; i <= target; i++) {
            // 遍历数组中的每个元素
            for (int num : nums) {
                // 如果当前元素小于等于剩余需要凑成的目标值，更新dp[i]
                if (num <= i) {
                    dp[i] += dp[i - num];
                    // 防止溢出（题目保证结果在32位有符号整数范围内）
                    if (dp[i] > Integer.MAX_VALUE) {
                        dp[i] = Integer.MAX_VALUE;
                    }
                }
            }
        }
        
        return (int) dp[target];
    }
    
    /**
     * 递归+记忆化搜索实现
     * @param nums 不同整数组成的数组
     * @param target 目标整数
     * @return 总和为target的元素组合的个数
     */
    public static int combinationSum4DFS(int[] nums, int target) {
        // 参数验证
        if (target < 0) {
            return 0;
        }
        if (target == 0) {
            return 1;
        }
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 使用HashMap作为记忆化缓存
        Map<Integer, Integer> memo = new HashMap<>();
        
        return dfs(nums, target, memo);
    }
    
    /**
     * 递归辅助函数
     * @param nums 不同整数组成的数组
     * @param remain 剩余需要凑成的目标值
     * @param memo 记忆化缓存，键是剩余目标值，值是对应的排列数
     * @return 凑成剩余目标值的不同排列数
     */
    private static int dfs(int[] nums, int remain, Map<Integer, Integer> memo) {
        // 基础情况：如果剩余目标值为0，说明找到了一种排列
        if (remain == 0) {
            return 1;
        }
        
        // 检查缓存
        if (memo.containsKey(remain)) {
            return memo.get(remain);
        }
        
        int ways = 0;
        
        // 尝试使用每个元素
        for (int num : nums) {
            if (num <= remain) {
                // 递归计算剩余值的排列数
                ways += dfs(nums, remain - num, memo);
            }
        }
        
        // 缓存结果
        memo.put(remain, ways);
        return ways;
    }
    
    /**
     * 优化版本：提前排序和剪枝
     * @param nums 不同整数组成的数组
     * @param target 目标整数
     * @return 总和为target的元素组合的个数
     */
    public static int combinationSum4Optimized(int[] nums, int target) {
        // 参数验证
        if (target < 0) {
            return 0;
        }
        if (target == 0) {
            return 1;
        }
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 对数组进行排序，以便在后续处理中进行剪枝
        Arrays.sort(nums);
        
        // 创建DP数组
        long[] dp = new long[target + 1];
        dp[0] = 1;
        
        // 遍历目标值
        for (int i = 1; i <= target; i++) {
            // 遍历数组中的元素
            for (int num : nums) {
                // 如果当前元素大于剩余需要凑成的目标值，由于数组已排序，后面的元素更大，可以提前退出循环
                if (num > i) {
                    break;
                }
                dp[i] += dp[i - num];
                // 防止溢出
                if (dp[i] > Integer.MAX_VALUE) {
                    dp[i] = Integer.MAX_VALUE;
                }
            }
        }
        
        return (int) dp[target];
    }
    
    /**
     * 递归+记忆化搜索实现的另一种方式，使用数组作为缓存
     * @param nums 不同整数组成的数组
     * @param target 目标整数
     * @return 总和为target的元素组合的个数
     */
    public static int combinationSum4DFSArray(int[] nums, int target) {
        // 参数验证
        if (target < 0) {
            return 0;
        }
        if (target == 0) {
            return 1;
        }
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 使用数组作为缓存，初始值为-1表示未计算
        int[] memo = new int[target + 1];
        Arrays.fill(memo, -1);
        memo[0] = 1; // 凑成目标值0只有一种方式
        
        return dfsArray(nums, target, memo);
    }
    
    /**
     * 递归辅助函数 - 使用数组作为缓存
     * @param nums 不同整数组成的数组
     * @param remain 剩余需要凑成的目标值
     * @param memo 记忆化缓存数组
     * @return 凑成剩余目标值的不同排列数
     */
    private static int dfsArray(int[] nums, int remain, int[] memo) {
        // 基础情况：如果剩余目标值为0，说明找到了一种排列
        if (remain == 0) {
            return 1;
        }
        
        // 检查缓存
        if (memo[remain] != -1) {
            return memo[remain];
        }
        
        int ways = 0;
        
        // 尝试使用每个元素
        for (int num : nums) {
            if (num <= remain) {
                ways += dfsArray(nums, remain - num, memo);
            }
        }
        
        // 缓存结果
        memo[remain] = ways;
        return ways;
    }
}
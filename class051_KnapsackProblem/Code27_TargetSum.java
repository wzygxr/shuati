package class073;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// LeetCode 494. 目标和
// 题目描述：给你一个整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
// 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，得到表达式 "+2-1" 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
// 链接：https://leetcode.cn/problems/target-sum/
// 
// 解题思路：
// 这是一个背包问题的变种，我们可以将问题转化为：
// 找到一个子集，使得该子集中的元素和与其余元素和的差等于target
// 设所有元素的和为sum，子集和为subsetSum，则：
// subsetSum - (sum - subsetSum) = target
// 即 2*subsetSum = sum + target
// 因此 subsetSum = (sum + target) / 2
// 
// 所以问题转化为：找到和为subsetSum的子集数目
// 这是一个0-1背包问题（每个元素只能选或不选）
// 
// 状态定义：dp[i] 表示和为i的子集数目
// 状态转移方程：dp[i] += dp[i - num]，其中num是当前元素，且i >= num
// 初始状态：dp[0] = 1 表示和为0的子集有一个（空集）
// 
// 时间复杂度：O(n * target)，其中n是数组长度
// 空间复杂度：O(target)，使用一维DP数组

public class Code27_TargetSum {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 1, 1, 1, 1};
        int target1 = 3;
        System.out.println("测试用例1结果: " + findTargetSumWays(nums1, target1)); // 预期输出: 5
        
        // 测试用例2
        int[] nums2 = {1};
        int target2 = 1;
        System.out.println("测试用例2结果: " + findTargetSumWays(nums2, target2)); // 预期输出: 1
        
        // 测试用例3
        int[] nums3 = {1, 2, 3, 4, 5};
        int target3 = 3;
        System.out.println("测试用例3结果: " + findTargetSumWays(nums3, target3)); // 预期输出: 3
    }
    
    /**
     * 计算可以通过添加'+'或'-'使得表达式结果等于target的不同表达式数目
     * @param nums 整数数组
     * @param target 目标和
     * @return 不同表达式的数目
     */
    public static int findTargetSumWays(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 计算所有元素的和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 检查是否有解的条件
        // 1. sum + target 必须是非负数
        // 2. sum + target 必须是偶数
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        // 计算目标子集和
        int subsetSum = (sum + target) / 2;
        
        // 创建一维DP数组，dp[i]表示和为i的子集数目
        int[] dp = new int[subsetSum + 1];
        
        // 初始状态：和为0的子集有一个（空集）
        dp[0] = 1;
        
        // 对于每个元素，逆序遍历子集和（0-1背包问题）
        for (int num : nums) {
            for (int i = subsetSum; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        
        // 返回结果：和为subsetSum的子集数目
        return dp[subsetSum];
    }
    
    /**
     * 优化版本：处理可能的大数问题（使用long类型）
     */
    public static int findTargetSumWaysOptimized(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        int subsetSum = (sum + target) / 2;
        
        // 使用long类型防止整数溢出
        long[] dp = new long[subsetSum + 1];
        dp[0] = 1;
        
        for (int num : nums) {
            for (int i = subsetSum; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        
        // 转换为int返回
        return (int) dp[subsetSum];
    }
    
    /**
     * 二维DP数组实现
     * dp[i][j]表示前i个元素中和为j的子集数目
     */
    public static int findTargetSumWays2D(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        int subsetSum = (sum + target) / 2;
        int n = nums.length;
        
        // 创建二维DP数组
        int[][] dp = new int[n + 1][subsetSum + 1];
        
        // 初始化：前0个元素中和为0的子集有一个（空集）
        dp[0][0] = 1;
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            for (int j = 0; j <= subsetSum; j++) {
                // 不选当前元素
                dp[i][j] = dp[i - 1][j];
                // 选当前元素（如果可以的话）
                if (j >= num) {
                    dp[i][j] += dp[i - 1][j - num];
                }
            }
        }
        
        return dp[n][subsetSum];
    }
    
    /**
     * 递归+记忆化搜索实现
     */
    public static int findTargetSumWaysDFS(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 计算所有元素的和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 检查是否有解的条件
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        int subsetSum = (sum + target) / 2;
        
        // 使用二维数组作为缓存，memo[i][j]表示前i个元素中和为j的子集数目
        Integer[][] memo = new Integer[nums.length][subsetSum + 1];
        
        // 调用递归辅助函数
        return dfs(nums, 0, 0, subsetSum, memo);
    }
    
    /**
     * 递归辅助函数
     * @param nums 整数数组
     * @param index 当前考虑的元素索引
     * @param currentSum 当前子集和
     * @param targetSum 目标子集和
     * @param memo 缓存数组
     * @return 可以组成目标子集和的子集数目
     */
    private static int dfs(int[] nums, int index, int currentSum, int targetSum, Integer[][] memo) {
        // 基础情况：如果已经考虑完所有元素
        if (index == nums.length) {
            // 如果当前子集和等于目标子集和，返回1，否则返回0
            return currentSum == targetSum ? 1 : 0;
        }
        
        // 检查缓存
        if (memo[index][currentSum] != null) {
            return memo[index][currentSum];
        }
        
        // 计算当前状态的解
        int result = 0;
        
        // 选择不将当前元素加入子集
        result += dfs(nums, index + 1, currentSum, targetSum, memo);
        
        // 选择将当前元素加入子集（如果不会超过目标和）
        if (currentSum + nums[index] <= targetSum) {
            result += dfs(nums, index + 1, currentSum + nums[index], targetSum, memo);
        }
        
        // 缓存结果
        memo[index][currentSum] = result;
        return result;
    }
    
    /**
     * 另一种递归实现方式，直接计算表达式数目
     */
    public static int findTargetSumWaysDFS2(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 使用HashMap作为缓存，键为"index,currentSum"，值为该状态下的表达式数目
        Map<String, Integer> memo = new HashMap<>();
        
        // 调用递归辅助函数
        return dfs2(nums, 0, 0, target, memo);
    }
    
    /**
     * 另一种递归辅助函数实现
     */
    private static int dfs2(int[] nums, int index, int currentSum, int target, Map<String, Integer> memo) {
        // 基础情况：如果已经考虑完所有元素
        if (index == nums.length) {
            // 如果当前和等于目标和，返回1，否则返回0
            return currentSum == target ? 1 : 0;
        }
        
        // 生成缓存键
        String key = index + "," + currentSum;
        
        // 检查缓存
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        // 选择在当前元素前添加'+'
        int add = dfs2(nums, index + 1, currentSum + nums[index], target, memo);
        
        // 选择在当前元素前添加'-'
        int subtract = dfs2(nums, index + 1, currentSum - nums[index], target, memo);
        
        // 计算总表达式数目
        int total = add + subtract;
        
        // 缓存结果
        memo.put(key, total);
        return total;
    }
    
    /**
     * 另一种动态规划方法，使用二维数组记录到达每个和的路径数
     */
    public static int findTargetSumWaysAlternative(int[] nums, int target) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 计算所有元素的和，用于确定可能的和的范围
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 检查是否有解的条件
        if (sum < Math.abs(target)) {
            return 0;
        }
        
        // 创建DP数组，dp[i][j]表示前i个元素能组成和为j的表达式数目
        // 由于和可能为负数，我们需要进行偏移，将和范围从[-sum, sum]映射到[0, 2*sum]
        int offset = sum;
        int[][] dp = new int[nums.length + 1][2 * sum + 1];
        
        // 初始状态：前0个元素能组成和为0的表达式有一个（空表达式）
        dp[0][offset] = 1;
        
        // 填充DP数组
        for (int i = 1; i <= nums.length; i++) {
            int num = nums[i - 1];
            for (int j = 0; j < 2 * sum + 1; j++) {
                // 如果前i-1个元素能组成和为j的表达式
                if (dp[i - 1][j] > 0) {
                    // 添加'+'：和变为j + num
                    dp[i][j + num] += dp[i - 1][j];
                    // 添加'-'：和变为j - num
                    dp[i][j - num] += dp[i - 1][j];
                }
            }
        }
        
        // 返回结果：前n个元素能组成和为target的表达式数目
        // 注意需要加上偏移量
        return dp[nums.length][target + offset];
    }
}
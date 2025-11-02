package class069;

/**
 * 目标和 (Target Sum) - 多维费用背包问题
 * 
 * 题目描述：
 * 给你一个非负整数数组 nums 和一个整数 target。
 * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
 * 
 * 题目来源：LeetCode 494. 目标和
 * 测试链接：https://leetcode.cn/problems/target-sum/
 * 
 * 解题思路：
 * 这是一个典型的多维费用背包问题，可以转化为子集和问题。
 * 假设我们选择一部分数字加上正号，另一部分数字加上负号。
 * 设正数集合的和为 P，负数集合的和为 N，数组总和为 S。
 * 则有：P - N = target，且 P + N = S
 * 联立可得：P = (S + target) / 2
 * 所以问题转化为：在数组中选择一些数字，使其和等于 (S + target) / 2 的方案数。
 * 
 * 算法实现：
 * 1. 动态规划：转化为子集和问题，使用背包DP
 * 2. 记忆化搜索：直接枚举所有可能的符号组合
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n * sum)，其中n是数组长度，sum是数组元素和
 * - 记忆化搜索：O(n * sum)，使用偏移量处理负数
 * 
 * 空间复杂度分析：
 * - 动态规划：O(sum)，一维DP数组
 * - 记忆化搜索：O(n * sum)，二维记忆化数组
 * 
 * 关键技巧：
 * 1. 问题转化：将符号选择问题转化为子集和问题
 * 2. 边界条件：处理(S+target)为奇数的情况
 * 3. 偏移量：记忆化搜索中使用偏移量处理负数索引
 * 
 * 工程化考量：
 * 1. 输入验证：检查target绝对值是否超过sum
 * 2. 边界处理：处理数组为空或和为0的特殊情况
 * 3. 性能优化：动态规划优于记忆化搜索
 * 4. 可读性：清晰的数学推导和注释
 */
public class TargetSum {
    
    /**
     * 方法1：动态规划解法
     * 
     * @param nums 非负整数数组
     * @param target 目标值
     * @return 满足条件的表达式数目
     */
    public static int findTargetSumWays1(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果 target 的绝对值大于 sum，不可能达到目标
        if (Math.abs(target) > sum) {
            return 0;
        }
        
        // 如果 (sum + target) 是奇数，无法整除，无解
        if ((sum + target) % 2 != 0) {
            return 0;
        }
        
        // 转化为子集和问题，目标和为 (sum + target) / 2
        int s = (sum + target) / 2;
        
        // dp[i] 表示和为 i 的方案数
        int[] dp = new int[s + 1];
        dp[0] = 1; // 和为0的方案数为1（什么都不选）
        
        // 遍历每个数字
        for (int num : nums) {
            // 从后往前更新，避免重复使用同一个数字
            for (int j = s; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        
        return dp[s];
    }
    
    /**
     * 方法2：记忆化搜索解法
     * 
     * @param nums 非负整数数组
     * @param target 目标值
     * @return 满足条件的表达式数目
     */
    public static int findTargetSumWays2(int[] nums, int target) {
        int n = nums.length;
        // 使用三维数组进行记忆化搜索
        // dp[i][sum + offset] 表示处理到第i个数字，当前和为sum的方案数
        // offset 用于处理负数索引
        int offset = 1000; // 偏移量，避免负数索引
        int[][] dp = new int[n][2001];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2001; j++) {
                dp[i][j] = -1;
            }
        }
        return dfs(nums, 0, target, dp, offset);
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param nums 数组
     * @param i 当前处理到第几个数字
     * @param target 剩余目标值
     * @param dp 记忆化数组
     * @param offset 偏移量
     * @return 方案数
     */
    private static int dfs(int[] nums, int i, int target, int[][] dp, int offset) {
        // 边界条件：处理完所有数字
        if (i == nums.length) {
            return target == 0 ? 1 : 0;
        }
        
        // 检查是否已经计算过
        if (dp[i][target + offset] != -1) {
            return dp[i][target + offset];
        }
        
        // 两种选择：加法或减法
        int ans = dfs(nums, i + 1, target - nums[i], dp, offset) + 
                  dfs(nums, i + 1, target + nums[i], dp, offset);
        
        // 记忆化存储
        dp[i][target + offset] = ans;
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 1, 1, 1, 1};
        int target1 = 3;
        System.out.println("测试用例1:");
        System.out.println("数组: [1,1,1,1,1], 目标值: 3");
        System.out.println("方法1结果: " + findTargetSumWays1(nums1, target1));
        System.out.println("方法2结果: " + findTargetSumWays2(nums1, target1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1};
        int target2 = 1;
        System.out.println("测试用例2:");
        System.out.println("数组: [1], 目标值: 1");
        System.out.println("方法1结果: " + findTargetSumWays1(nums2, target2));
        System.out.println("方法2结果: " + findTargetSumWays2(nums2, target2));
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {1, 0};
        int target3 = 1;
        System.out.println("测试用例3:");
        System.out.println("数组: [1,0], 目标值: 1");
        System.out.println("方法1结果: " + findTargetSumWays1(nums3, target3));
        System.out.println("方法2结果: " + findTargetSumWays2(nums3, target3));
    }
}
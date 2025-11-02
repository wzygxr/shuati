package class073;

// LeetCode 416. 分割等和子集
// 题目描述：给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
// 
// 解题思路：
// 这是一个0-1背包问题的变种。问题可以转化为：是否存在一个子集，其和等于整个数组和的一半。
// 
// 状态定义：dp[j] 表示是否能组成和为j的子集
// 状态转移方程：dp[j] = dp[j] || dp[j - nums[i]]
// 初始状态：dp[0] = true，表示空子集的和为0是可以组成的
// 
// 时间复杂度：O(n * target)，其中n是数组长度，target是数组和的一半
// 空间复杂度：O(target)，使用一维DP数组

public class Code37_PartitionEqualSubsetSum {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 5, 11, 5};
        System.out.println("测试用例1结果: " + canPartition(nums1)); // 预期输出: true
        
        // 测试用例2
        int[] nums2 = {1, 2, 3, 5};
        System.out.println("测试用例2结果: " + canPartition(nums2)); // 预期输出: false
    }
    
    /**
     * 判断是否可以将数组分割成两个和相等的子集
     * @param nums 非空数组，只包含正整数
     * @return 是否可以分割
     */
    public static boolean canPartition(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和是奇数，不可能分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和为总和的一半
        int target = sum / 2;
        
        // 创建DP数组，dp[j]表示是否能组成和为j的子集
        boolean[] dp = new boolean[target + 1];
        
        // 初始状态：空子集的和为0是可以组成的
        dp[0] = true;
        
        // 遍历每个数字
        for (int num : nums) {
            // 逆序遍历，避免重复使用同一个数字
            for (int j = target; j >= num; j--) {
                // 更新状态：不选当前数字 或 选当前数字（如果可以的话）
                dp[j] = dp[j] || dp[j - num];
            }
        }
        
        return dp[target];
    }
    
    /**
     * 使用二维DP数组的版本（更直观但空间效率较低）
     * @param nums 非空数组，只包含正整数
     * @return 是否可以分割
     */
    public static boolean canPartition2D(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和是奇数，不可能分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和为总和的一半
        int target = sum / 2;
        int n = nums.length;
        
        // 创建二维DP数组，dp[i][j]表示前i个数字是否能组成和为j的子集
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始状态：空子集的和为0是可以组成的
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // 不选第i个数字
                dp[i][j] = dp[i-1][j];
                
                // 选第i个数字（如果可以的话）
                if (j >= nums[i-1]) {
                    dp[i][j] = dp[i][j] || dp[i-1][j - nums[i-1]];
                }
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 优化的一维DP版本，提前处理一些边界情况
     * @param nums 非空数组，只包含正整数
     * @return 是否可以分割
     */
    public static boolean canPartitionOptimized(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        int maxNum = 0;
        for (int num : nums) {
            sum += num;
            maxNum = Math.max(maxNum, num);
        }
        
        // 如果总和是奇数，不可能分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和为总和的一半
        int target = sum / 2;
        
        // 如果最大的数字大于目标和，不可能分割
        if (maxNum > target) {
            return false;
        }
        
        // 创建DP数组
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        
        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }
        
        return dp[target];
    }
    
    /**
     * 使用递归+记忆化搜索实现
     * 这个方法对于较大的输入可能会超时，但展示了递归的思路
     * @param nums 非空数组，只包含正整数
     * @return 是否可以分割
     */
    public static boolean canPartitionRecursive(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和是奇数，不可能分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和为总和的一半
        int target = sum / 2;
        int n = nums.length;
        
        // 创建记忆化缓存
        Boolean[][] memo = new Boolean[n][target + 1];
        
        return dfs(nums, 0, 0, target, memo);
    }
    
    /**
     * 递归辅助函数
     * @param nums 数组
     * @param index 当前处理的索引
     * @param currentSum 当前子集和
     * @param target 目标和
     * @param memo 记忆化缓存
     * @return 是否能组成目标和
     */
    private static boolean dfs(int[] nums, int index, int currentSum, int target, Boolean[][] memo) {
        // 找到目标和
        if (currentSum == target) {
            return true;
        }
        
        // 超过目标和或处理完所有元素
        if (currentSum > target || index == nums.length) {
            return false;
        }
        
        // 检查缓存
        if (memo[index][currentSum] != null) {
            return memo[index][currentSum];
        }
        
        // 递归调用：选当前元素 或 不选当前元素
        boolean result = dfs(nums, index + 1, currentSum + nums[index], target, memo) 
                       || dfs(nums, index + 1, currentSum, target, memo);
        
        // 缓存结果
        memo[index][currentSum] = result;
        return result;
    }
    
    /**
     * 使用位操作优化的版本
     * 对于较大的数组但元素值不大的情况，位操作可以更高效
     * @param nums 非空数组，只包含正整数
     * @return 是否可以分割
     */
    public static boolean canPartitionBitSet(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和是奇数，不可能分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和为总和的一半
        int target = sum / 2;
        
        // 使用位集表示可达的和
        // bitset的第i位为1表示和为i是可达的
        int bitset = 1; // 初始状态，和为0是可达的
        
        for (int num : nums) {
            // 位操作：当前可达的和 | (之前可达的和 + 当前数字)
            bitset |= bitset << num;
        }
        
        // 检查目标和是否可达
        return (bitset & (1 << target)) != 0;
    }
}
package class073;

// LeetCode 416. 分割等和子集
// 题目描述：给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
// 
// 解题思路：
// 这是一个典型的01背包问题的变种。我们可以将问题转化为：
// 1. 计算数组的总和sum
// 2. 如果sum是奇数，直接返回false（无法分成两个和相等的子集）
// 3. 如果sum是偶数，问题转化为：是否存在一个子集，使得其和为sum/2
// 4. 这相当于01背包问题，容量为sum/2，物品价值和重量都是nums[i]，问是否能恰好装满背包
// 
// 状态定义：dp[i] 表示是否可以选择一些数字，使得它们的和恰好为i
// 状态转移方程：dp[i] = dp[i] || dp[i - nums[j]]，其中j遍历所有数字，且i >= nums[j]
// 初始状态：dp[0] = true（表示和为0可以通过不选任何数字来实现）
// 
// 时间复杂度：O(n * target)，其中n是数组长度，target是sum/2
// 空间复杂度：O(target)，使用一维DP数组

public class Code21_PartitionEqualSubsetSum {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 5, 11, 5};
        System.out.println("测试用例1结果: " + canPartition(nums1)); // 预期输出: true (分割为 [1, 5, 5] 和 [11])
        
        // 测试用例2
        int[] nums2 = {1, 2, 3, 5};
        System.out.println("测试用例2结果: " + canPartition(nums2)); // 预期输出: false
        
        // 测试用例3
        int[] nums3 = {1, 2, 5};
        System.out.println("测试用例3结果: " + canPartition(nums3)); // 预期输出: false
        
        // 测试用例4
        int[] nums4 = {2, 2, 3, 5};
        System.out.println("测试用例4结果: " + canPartition(nums4)); // 预期输出: false
        
        // 测试用例5
        int[] nums5 = {1, 2, 3, 4, 5, 6, 7};
        System.out.println("测试用例5结果: " + canPartition(nums5)); // 预期输出: true (分割为 [2, 3, 5, 7] 和 [1, 4, 6])
    }
    
    /**
     * 判断是否可以将数组分割成两个和相等的子集
     * @param nums 非空正整数数组
     * @return 是否可以分割
     */
    public static boolean canPartition(int[] nums) {
        // 参数验证
        if (nums == null || nums.length <= 1) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和是奇数，无法分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和为总和的一半
        int target = sum / 2;
        
        // 创建DP数组，dp[i]表示是否可以选择一些数字，使得它们的和恰好为i
        boolean[] dp = new boolean[target + 1];
        
        // 初始状态：和为0可以通过不选任何数字来实现
        dp[0] = true;
        
        // 遍历每个数字（物品）
        for (int num : nums) {
            // 逆序遍历目标和（容量），防止重复使用同一个数字
            for (int i = target; i >= num; i--) {
                // 状态转移：选择当前数字或不选择当前数字
                // 如果不选择当前数字，dp[i]保持不变
                // 如果选择当前数字，则要看dp[i - num]是否为true
                dp[i] = dp[i] || dp[i - num];
            }
            
            // 提前终止：如果已经找到可以组成target的子集，直接返回true
            if (dp[target]) {
                return true;
            }
        }
        
        return dp[target];
    }
    
    /**
     * 优化版本：添加一些剪枝条件
     */
    public static boolean canPartitionOptimized(int[] nums) {
        // 参数验证
        if (nums == null || nums.length <= 1) {
            return false;
        }
        
        // 计算数组总和，并找出最大值
        int sum = 0;
        int maxNum = 0;
        for (int num : nums) {
            sum += num;
            maxNum = Math.max(maxNum, num);
        }
        
        // 如果总和是奇数，无法分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 如果数组中最大值大于target，那么这个数字必须单独在一个子集，但剩下的数字之和无法等于target
        if (maxNum > target) {
            return false;
        }
        
        // 如果数组中有元素等于target，直接返回true
        for (int num : nums) {
            if (num == target) {
                return true;
            }
        }
        
        // 创建DP数组
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        
        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
                if (dp[target]) {
                    return true;
                }
            }
        }
        
        return dp[target];
    }
    
    /**
     * 二维DP实现，更容易理解
     */
    public static boolean canPartition2D(int[] nums) {
        // 参数验证
        if (nums == null || nums.length <= 1) {
            return false;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        int n = nums.length;
        
        // dp[i][j]表示前i个数字是否可以组成和为j的子集
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始化：前0个数字只能组成和为0的子集
        dp[0][0] = true;
        
        // 遍历每个数字
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            // 遍历每个可能的和
            for (int j = 0; j <= target; j++) {
                // 不选择当前数字
                dp[i][j] = dp[i - 1][j];
                
                // 选择当前数字（如果j >= num）
                if (j >= num) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - num];
                }
            }
            
            // 提前终止
            if (dp[i][target]) {
                return true;
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 递归+记忆化搜索实现
     */
    public static boolean canPartitionDFS(int[] nums) {
        // 参数验证
        if (nums == null || nums.length <= 1) {
            return false;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 使用记忆化搜索，memo[i][j]表示从第i个数字开始，是否可以找到和为j的子集
        Boolean[][] memo = new Boolean[nums.length][target + 1];
        
        return dfs(nums, 0, target, memo);
    }
    
    /**
     * 递归辅助函数
     * @param nums 数组
     * @param index 当前考虑的索引
     * @param target 目标和
     * @param memo 记忆化数组
     * @return 是否可以找到和为target的子集
     */
    private static boolean dfs(int[] nums, int index, int target, Boolean[][] memo) {
        // 基础情况：找到目标和
        if (target == 0) {
            return true;
        }
        
        // 基础情况：超出数组范围或目标和为负
        if (index >= nums.length || target < 0) {
            return false;
        }
        
        // 如果已经计算过，直接返回结果
        if (memo[index][target] != null) {
            return memo[index][target];
        }
        
        // 选择当前数字或不选择当前数字
        boolean result = dfs(nums, index + 1, target - nums[index], memo) || 
                         dfs(nums, index + 1, target, memo);
        
        // 记忆化结果
        memo[index][target] = result;
        return result;
    }
    
    /**
     * 位运算优化版本
     * 使用位图记录所有可能的和
     */
    public static boolean canPartitionBitwise(int[] nums) {
        // 参数验证
        if (nums == null || nums.length <= 1) {
            return false;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 使用位掩码表示所有可能的和
        // bit[i] = 1表示可以组成和为i的子集
        int dp = 1; // 初始状态：可以组成和为0的子集
        
        for (int num : nums) {
            // 对于每个数字，更新可能的和集合
            // dp |= dp << num 表示当前数字可以与之前的每个和相加，产生新的和
            dp |= dp << num;
        }
        
        // 检查是否可以组成和为target的子集
        return (dp & (1 << target)) != 0;
    }
}
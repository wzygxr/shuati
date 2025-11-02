package class073;

import java.util.Arrays;

// LeetCode 416. 分割等和子集
// 题目描述：给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，
// 使得两个子集的元素和相等。
// 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
// 
// 解题思路：
// 这是一个0-1背包问题的应用，问题可以转化为：
// 1. 计算数组的总和 sum
// 2. 如果 sum 是奇数，那么无法将数组分成两个和相等的子集，直接返回 false
// 3. 如果 sum 是偶数，那么问题转化为：是否存在一个子集，使得其和为 sum/2
// 
// 状态定义：dp[i] 表示是否可以从数组中选择一些元素，使得它们的和为 i
// 状态转移方程：dp[i] = dp[i] || dp[i - num]，其中 num 是当前元素，且 i >= num
// 初始状态：dp[0] = true，表示和为0的子集存在（空集）
// 
// 时间复杂度：O(n * target)，其中 n 是数组长度，target 是数组和的一半
// 空间复杂度：O(target)，使用一维DP数组

public class Code28_PartitionEqualSubsetSum {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 5, 11, 5};
        System.out.println("测试用例1结果: " + canPartition(nums1)); // 预期输出: true
        
        // 测试用例2
        int[] nums2 = {1, 2, 3, 5};
        System.out.println("测试用例2结果: " + canPartition(nums2)); // 预期输出: false
        
        // 测试用例3
        int[] nums3 = {1, 2, 5};
        System.out.println("测试用例3结果: " + canPartition(nums3)); // 预期输出: false
    }
    
    /**
     * 判断是否可以将数组分割成两个和相等的子集
     * @param nums 非空正整数数组
     * @return 是否可以分割成两个和相等的子集
     */
    public static boolean canPartition(int[] nums) {
        // 参数验证
        if (nums == null || nums.length < 2) {
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
        
        // 计算目标和：总和的一半
        int target = sum / 2;
        
        // 创建一维DP数组，dp[i]表示是否可以从数组中选择一些元素，使得它们的和为i
        boolean[] dp = new boolean[target + 1];
        
        // 初始状态：和为0的子集存在（空集）
        dp[0] = true;
        
        // 对于每个元素，逆序遍历目标和（0-1背包问题）
        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                // 状态转移：如果dp[i - num]为true，说明可以组成和为i - num的子集，
                // 那么再加上当前元素num，就可以组成和为i的子集
                dp[i] = dp[i] || dp[i - num];
            }
        }
        
        // 返回是否可以组成和为target的子集
        return dp[target];
    }
    
    /**
     * 优化版本：提前剪枝
     */
    public static boolean canPartitionOptimized(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        
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
        
        // 如果最大元素大于目标和，无法分成两个和相等的子集
        if (maxNum > target) {
            return false;
        }
        
        // 排序数组，方便后续剪枝
        Arrays.sort(nums);
        
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        
        for (int num : nums) {
            // 剪枝：如果当前元素已经大于目标和，可以跳过
            if (num > target) {
                continue;
            }
            
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
            
            // 提前结束：如果已经找到解，可以直接返回true
            if (dp[target]) {
                return true;
            }
        }
        
        return dp[target];
    }
    
    /**
     * 二维DP数组实现
     * dp[i][j]表示前i个元素中是否可以选择一些元素，使得它们的和为j
     */
    public static boolean canPartition2D(int[] nums) {
        if (nums == null || nums.length < 2) {
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
        
        // 创建二维DP数组
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始化：前0个元素可以组成和为0的子集
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            for (int j = 1; j <= target; j++) {
                // 不选当前元素
                dp[i][j] = dp[i - 1][j];
                // 选当前元素（如果可以的话）
                if (j >= num) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - num];
                }
            }
            
            // 提前结束：如果已经找到解，可以直接返回true
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
        if (nums == null || nums.length < 2) {
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
        
        // 使用二维数组作为缓存，memo[i][j]表示从第i个元素开始，是否可以组成和为j的子集
        Boolean[][] memo = new Boolean[n][target + 1];
        
        // 调用递归辅助函数
        return dfs(nums, 0, target, memo);
    }
    
    /**
     * 递归辅助函数
     * @param nums 数组
     * @param index 当前考虑的元素索引
     * @param remaining 剩余需要达到的和
     * @param memo 缓存数组
     * @return 是否可以从当前索引开始，组成和为remaining的子集
     */
    private static boolean dfs(int[] nums, int index, int remaining, Boolean[][] memo) {
        // 基础情况：如果剩余和为0，说明找到了一个子集
        if (remaining == 0) {
            return true;
        }
        
        // 基础情况：如果已经考虑完所有元素或者剩余和小于0，返回false
        if (index == nums.length || remaining < 0) {
            return false;
        }
        
        // 检查缓存
        if (memo[index][remaining] != null) {
            return memo[index][remaining];
        }
        
        // 尝试两种选择：选或不选当前元素
        // 1. 选当前元素：剩余和减去当前元素的值，继续考虑下一个元素
        boolean choose = dfs(nums, index + 1, remaining - nums[index], memo);
        
        // 2. 不选当前元素：剩余和不变，继续考虑下一个元素
        boolean notChoose = dfs(nums, index + 1, remaining, memo);
        
        // 缓存结果
        memo[index][remaining] = choose || notChoose;
        return memo[index][remaining];
    }
    
    /**
     * 位运算优化的DP实现
     * 每个二进制位表示是否可以组成对应索引的和
     */
    public static boolean canPartitionBit(int[] nums) {
        if (nums == null || nums.length < 2) {
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
        
        // 使用位集，每个位表示是否可以组成对应的和
        // bits[0]表示和为0，bits[i]表示和为i
        // 初始时，只有和为0的情况是可能的
        int bits = 1; // 0b000...0001，表示和为0是可以的
        
        for (int num : nums) {
            // 位运算：将当前bits左移num位，并与原bits进行或操作
            // 这样，新的bits中的第i位为1当且仅当原来的bits中的第i位为1（不选当前元素）
            // 或者原来的bits中的第i-num位为1（选当前元素）
            bits |= bits << num;
            
            // 检查目标和是否已经可达
            if ((bits & (1 << target)) != 0) {
                return true;
            }
        }
        
        // 检查目标和是否可达
        return (bits & (1 << target)) != 0;
    }
}
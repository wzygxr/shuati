package class080;

// 划分为k个相等的子集
// 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把数组分成 k 个非空子集，
// 其总和都相等。
// 测试链接 : https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
public class Code03_PartitionToKEqualSumSubsets {

    // 使用状态压缩动态规划解决划分子集问题
    // 核心思想：用二进制位表示数组元素的使用状态，通过状态转移判断是否能划分为k个相等子集
    // 时间复杂度: O(n * 2^n)
    // 空间复杂度: O(2^n)
    public static boolean canPartitionKSubsets(int[] nums, int k) {
        // 边界条件检查
        if (k == 1) {
            // 如果只需要划分成1个子集，直接返回true
            return true;
        }

        // 计算数组元素总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }

        // 如果总和不能被k整除，则无法划分为k个相等子集
        if (sum % k != 0) {
            return false;
        }

        // 计算每个子集的目标和
        int target = sum / k;

        // 检查是否有元素大于目标和，如果有则无法划分
        for (int num : nums) {
            if (num > target) {
                return false;
            }
        }

        // dp[mask] 表示使用mask代表的元素集合能否构成若干个完整子集
        // -1: 未计算, 0: 不能构成, 1: 能构成
        int[] dp = new int[1 << nums.length];
        // 初始化为-1，表示未计算
        for (int i = 0; i < dp.length; i++) {
            dp[i] = -1;
        }
        // 空集状态可以构成0个完整子集
        dp[0] = 1;

        // 调用深度优先搜索函数
        return dfs(nums, (1 << nums.length) - 1, target, dp) == 1;
    }

    // 深度优先搜索 + 记忆化
    // nums: 数组元素
    // mask: 当前元素使用状态，二进制位为1表示对应元素已使用
    // target: 每个子集的目标和
    // dp: 记忆化数组
    // 返回值: 1表示能划分，0表示不能划分，-1表示未计算
    private static int dfs(int[] nums, int mask, int target, int[] dp) {
        // 如果已经计算过当前状态，直接返回结果
        if (dp[mask] != -1) {
            return dp[mask];
        }

        // 计算当前已使用的元素总和
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            // 如果第i个元素已被使用，累加其值
            if ((mask & (1 << i)) != 0) {
                sum += nums[i];
            }
        }

        // 如果当前总和能被目标和整除，说明已经构成若干个完整子集
        if (sum % target == 0) {
            // 如果所有元素都已使用且构成了k个完整子集，则成功
            if (mask == 0) {
                return dp[mask] = 1;
            }

            // 尝试添加新的元素来构成下一个子集
            for (int i = 0; i < nums.length; i++) {
                // 如果第i个元素还未使用
                if ((mask & (1 << i)) != 0) {
                    // 递归调用，尝试使用第i个元素
                    if (dfs(nums, mask ^ (1 << i), target, dp) == 1) {
                        return dp[mask] = 1;
                    }
                }
            }
            // 如果所有未使用的元素都无法构成下一个子集，则失败
            return dp[mask] = 0;
        }

        // 如果当前总和不能被目标和整除，继续添加元素直到构成完整子集
        for (int i = 0; i < nums.length; i++) {
            // 如果第i个元素还未使用，且添加后不会超过目标和
            if ((mask & (1 << i)) != 0 && sum % target + nums[i] <= target) {
                // 递归调用，尝试使用第i个元素
                if (dfs(nums, mask ^ (1 << i), target, dp) == 1) {
                    return dp[mask] = 1;
                }
            }
        }

        // 如果所有可行的元素都无法构成k个相等子集，则失败
        return dp[mask] = 0;
    }

}
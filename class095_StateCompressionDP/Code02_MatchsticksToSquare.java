package class080;

// 火柴拼正方形
// 给定一个整数数组 matchsticks ，其中 matchsticks[i] 是第 i 个火柴棒的长度。
// 你要用所有的火柴棍拼成一个正方形。你不能折断任何一根火柴棒，但可以将它们连在一起，
// 而且每根火柴棒必须使用一次。
// 如果你能拼成正方形，则返回 true，否则返回 false。
// 测试链接 : https://leetcode.cn/problems/matchsticks-to-square/
public class Code02_MatchsticksToSquare {

    // 使用状态压缩动态规划解决火柴拼正方形问题
    // 核心思想：用二进制位表示火柴棒的使用状态，通过状态转移判断是否能构成正方形
    // 时间复杂度: O(n * 2^n)
    // 空间复杂度: O(2^n)
    public static boolean makesquare(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length < 4) {
            return false;
        }

        // 计算所有火柴棒的总长度
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }

        // 如果总长度不能被4整除，则无法构成正方形
        if (sum % 4 != 0) {
            return false;
        }

        // 计算正方形每条边的目标长度
        int target = sum / 4;

        // dp[mask] 表示使用mask代表的火柴棒集合能否构成若干条完整的边
        // -1: 未计算, 0: 不能构成, 1: 能构成
        int[] dp = new int[1 << nums.length];
        // 初始化为-1，表示未计算
        for (int i = 0; i < dp.length; i++) {
            dp[i] = -1;
        }
        // 空集状态可以构成0条完整边
        dp[0] = 1;

        // 调用深度优先搜索函数
        return dfs(nums, (1 << nums.length) - 1, target, dp) == 1;
    }

    // 深度优先搜索 + 记忆化
    // nums: 火柴棒长度数组
    // mask: 当前火柴棒使用状态，二进制位为1表示对应火柴棒已使用
    // target: 正方形每条边的目标长度
    // dp: 记忆化数组
    // 返回值: 1表示能构成正方形，0表示不能构成，-1表示未计算
    private static int dfs(int[] nums, int mask, int target, int[] dp) {
        // 如果已经计算过当前状态，直接返回结果
        if (dp[mask] != -1) {
            return dp[mask];
        }

        // 计算当前已使用的火柴棒总长度
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            // 如果第i根火柴棒已被使用，累加其长度
            if ((mask & (1 << i)) != 0) {
                sum += nums[i];
            }
        }

        // 如果当前总长度能被目标长度整除，说明已经构成若干条完整边
        if (sum % target == 0) {
            // 如果所有火柴棒都已使用且构成了4条完整边，则成功
            if (mask == 0) {
                return dp[mask] = 1;
            }

            // 尝试添加新的火柴棒来构成下一条边
            for (int i = 0; i < nums.length; i++) {
                // 如果第i根火柴棒还未使用
                if ((mask & (1 << i)) != 0) {
                    // 递归调用，尝试使用第i根火柴棒
                    if (dfs(nums, mask ^ (1 << i), target, dp) == 1) {
                        return dp[mask] = 1;
                    }
                }
            }
            // 如果所有未使用的火柴棒都无法构成下一条边，则失败
            return dp[mask] = 0;
        }

        // 如果当前总长度不能被目标长度整除，继续添加火柴棒直到构成完整边
        for (int i = 0; i < nums.length; i++) {
            // 如果第i根火柴棒还未使用，且添加后不会超过目标长度
            if ((mask & (1 << i)) != 0 && sum % target + nums[i] <= target) {
                // 递归调用，尝试使用第i根火柴棒
                if (dfs(nums, mask ^ (1 << i), target, dp) == 1) {
                    return dp[mask] = 1;
                }
            }
        }

        // 如果所有可行的火柴棒都无法构成正方形，则失败
        return dp[mask] = 0;
    }

}
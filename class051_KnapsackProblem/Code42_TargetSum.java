package class073;

// LeetCode 494. 目标和
// 题目描述：给你一个整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
// 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
// 链接：https://leetcode.cn/problems/target-sum/
// 
// 解题思路：
// 这是一个0-1背包问题的变种。我们可以将问题转化为：找到一个子集P，使得sum(P) - sum(N) = target，其中N是数组中不在P中的元素。
// 可以证明：sum(P) - sum(N) = target => sum(P) = (sum(nums) + target) / 2
// 因此，问题转化为：在数组nums中，有多少个子集的和等于(sum(nums) + target) / 2。
// 
// 状态定义：dp[j] 表示和为j的子集数目
// 状态转移方程：dp[j] += dp[j - nums[i]]，其中nums[i]是当前元素，且j >= nums[i]
// 初始状态：dp[0] = 1，表示和为0的子集数目为1（空集）
// 
// 时间复杂度：O(n * sum)，其中n是数组的长度，sum是数组元素的和
// 空间复杂度：O(sum)，使用一维DP数组

public class Code42_TargetSum {

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
        System.out.println("测试用例3结果: " + findTargetSumWays(nums3, target3)); // 预期输出: 5
        
        // 测试用例4 - 无法满足的情况
        int[] nums4 = {1, 2, 3};
        int target4 = 7;
        System.out.println("测试用例4结果: " + findTargetSumWays(nums4, target4)); // 预期输出: 0
    }
    
    /**
     * 计算有多少种不同的方法构造运算结果等于target的表达式
     * @param nums 整数数组
     * @param target 目标值
     * @return 不同表达式的数目
     */
    public static int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) {
            return target == 0 ? 1 : 0;
        }
        
        // 计算数组元素的总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 检查是否有解
        // 1. sum < Math.abs(target)：总和小于目标值的绝对值，无解
        // 2. (sum + target) % 2 != 0：sum + target必须是偶数，否则无法平均分成两部分
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        // 计算目标和：sum(P) = (sum + target) / 2
        int targetSum = (sum + target) / 2;
        if (targetSum < 0) {
            return 0; // 目标和为负数，无解
        }
        
        // 创建DP数组，dp[j]表示和为j的子集数目
        int[] dp = new int[targetSum + 1];
        
        // 初始状态：和为0的子集数目为1（空集）
        dp[0] = 1;
        
        // 填充DP数组
        for (int num : nums) {
            // 注意：这里我们从后往前遍历，避免重复使用同一个元素
            for (int j = targetSum; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        
        return dp[targetSum];
    }
    
    /**
     * 使用二维DP数组的实现
     * dp[i][j]表示使用前i个元素，和为j的子集数目
     */
    public static int findTargetSumWays2D(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) {
            return target == 0 ? 1 : 0;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        int targetSum = (sum + target) / 2;
        if (targetSum < 0) {
            return 0;
        }
        
        // 创建二维DP数组
        int[][] dp = new int[n + 1][targetSum + 1];
        
        // 初始状态：使用前0个元素，和为0的子集数目为1（空集）
        dp[0][0] = 1;
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1]; // 当前元素
            for (int j = 0; j <= targetSum; j++) {
                // 不选择当前元素
                dp[i][j] = dp[i - 1][j];
                // 选择当前元素（如果j >= num）
                if (j >= num) {
                    dp[i][j] += dp[i - 1][j - num];
                }
            }
        }
        
        return dp[n][targetSum];
    }
    
    /**
     * 使用回溯法的实现
     * 注意：这种方法对于大数组可能效率不高
     */
    public static int findTargetSumWaysBacktrack(int[] nums, int target) {
        int[] count = new int[1]; // 使用数组来存储结果，以便在递归中修改
        backtrack(nums, target, 0, 0, count);
        return count[0];
    }
    
    /**
     * 回溯辅助方法
     * @param nums 整数数组
     * @param target 目标值
     * @param index 当前处理的索引
     * @param current 当前的和
     * @param count 结果计数器
     */
    private static void backtrack(int[] nums, int target, int index, int current, int[] count) {
        // 已经处理完所有元素
        if (index == nums.length) {
            if (current == target) {
                count[0]++;
            }
            return;
        }
        
        // 尝试加上当前元素
        backtrack(nums, target, index + 1, current + nums[index], count);
        
        // 尝试减去当前元素
        backtrack(nums, target, index + 1, current - nums[index], count);
    }
    
    /**
     * 使用记忆化递归的实现
     */
    public static int findTargetSumWaysMemo(int[] nums, int target) {
        // 创建记忆化缓存，键为(index, currentSum)，值为对应的方法数
        java.util.Map<String, Integer> memo = new java.util.HashMap<>();
        return backtrackMemo(nums, target, 0, 0, memo);
    }
    
    /**
     * 记忆化递归辅助方法
     */
    private static int backtrackMemo(int[] nums, int target, int index, int currentSum, java.util.Map<String, Integer> memo) {
        // 已经处理完所有元素
        if (index == nums.length) {
            return currentSum == target ? 1 : 0;
        }
        
        // 生成缓存键
        String key = index + "," + currentSum;
        
        // 检查是否已经计算过
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        // 计算两种情况的结果之和
        int add = backtrackMemo(nums, target, index + 1, currentSum + nums[index], memo);
        int subtract = backtrackMemo(nums, target, index + 1, currentSum - nums[index], memo);
        
        // 存储结果到缓存
        memo.put(key, add + subtract);
        
        return add + subtract;
    }
    
    /**
     * 优化版本，考虑数组中包含0的情况
     */
    public static int findTargetSumWaysOptimized(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) {
            return target == 0 ? 1 : 0;
        }
        
        int sum = 0;
        int zeroCount = 0;
        
        // 计算总和和0的个数
        for (int num : nums) {
            sum += num;
            if (num == 0) {
                zeroCount++;
            }
        }
        
        // 检查是否有解
        if (sum < Math.abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        int targetSum = (sum + target) / 2;
        if (targetSum < 0) {
            return 0;
        }
        
        // 过滤掉0，单独处理
        int[] nonZeroNums = new int[n - zeroCount];
        int idx = 0;
        for (int num : nums) {
            if (num != 0) {
                nonZeroNums[idx++] = num;
            }
        }
        
        // 创建DP数组
        int[] dp = new int[targetSum + 1];
        dp[0] = 1;
        
        // 填充DP数组
        for (int num : nonZeroNums) {
            for (int j = targetSum; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        
        // 每个0有两种选择（+0或-0），所以总方法数乘以2^zeroCount
        return dp[targetSum] * (int) Math.pow(2, zeroCount);
    }
    
    /**
     * 打印所有可能的表达式
     * 注意：这个方法仅用于教学目的，对于大数组可能效率不高
     */
    public static void printAllExpressions(int[] nums, int target) {
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder currentExpr = new StringBuilder();
        
        // 第一个数特殊处理，不需要前面的符号
        currentExpr.append(nums[0]);
        backtrackExpressions(nums, target, 1, nums[0], currentExpr, result);
        
        // 尝试第一个数为负数的情况
        currentExpr = new StringBuilder();
        currentExpr.append("-").append(nums[0]);
        backtrackExpressions(nums, target, 1, -nums[0], currentExpr, result);
        
        System.out.println("所有可能的表达式:");
        for (String expr : result) {
            System.out.println(expr);
        }
        System.out.println("总共有 " + result.size() + " 种不同的表达式。");
    }
    
    /**
     * 回溯辅助方法，用于生成所有可能的表达式
     */
    private static void backtrackExpressions(int[] nums, int target, int index, int currentSum,
                                           StringBuilder currentExpr, java.util.List<String> result) {
        if (index == nums.length) {
            if (currentSum == target) {
                result.add(currentExpr.toString());
            }
            return;
        }
        
        int num = nums[index];
        int length = currentExpr.length();
        
        // 尝试加上当前元素
        currentExpr.append("+").append(num);
        backtrackExpressions(nums, target, index + 1, currentSum + num, currentExpr, result);
        currentExpr.setLength(length); // 回溯
        
        // 尝试减去当前元素
        currentExpr.append("-").append(num);
        backtrackExpressions(nums, target, index + 1, currentSum - num, currentExpr, result);
        currentExpr.setLength(length); // 回溯
    }
}
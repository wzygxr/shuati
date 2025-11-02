package class074;

// 组合总和 Ⅳ
// 给你一个由不同整数组成的数组 nums ，和一个目标整数 target
// 请你从 nums 中找出并返回总和为 target 的元素组合的个数
// 顺序不同的序列被视作不同的组合
// 测试链接 : https://leetcode.cn/problems/combination-sum-iv/

/*
 * 算法详解：
 * 这是一个完全背包问题的变种，求组合数（考虑顺序）。与标准的完全背包求组合数不同，
 * 本题中顺序不同的序列被视为不同的组合，因此需要调整遍历顺序。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i]表示总和为i的组合数
 * 2. 状态转移：对于每个总和i，枚举所有可能的数字nums[j]
 *    dp[i] += dp[i - nums[j]]  (当i >= nums[j]时)
 * 3. 遍历顺序：外层遍历背包容量，内层遍历物品（与标准完全背包相反）
 * 
 * 时间复杂度分析：
 * 设数组长度为n，目标值为target
 * 1. 动态规划计算：O(n * target)
 * 总时间复杂度：O(n * target)
 * 
 * 空间复杂度分析：
 * 1. DP数组：O(target)
 * 
 * 相关题目扩展：
 * 1. LeetCode 377. 组合总和 Ⅳ（本题）
 * 2. LeetCode 518. 零钱兑换 II（不考虑顺序的组合数）
 * 3. LeetCode 322. 零钱兑换（求最少硬币数）
 * 4. LeetCode 279. 完全平方数（完全背包变种）
 * 5. LeetCode 139. 单词拆分（字符串匹配+背包）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空数组、负数、溢出等边界情况
 * 3. 可配置性：可以将MOD值作为配置参数传入
 * 4. 单元测试：为combinationSum4方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用记忆化搜索
 * 
 * 语言特性差异：
 * 1. Java：使用long类型防止整数溢出
 * 2. 数组操作：Java数组索引从0开始，需要注意边界处理
 * 3. 数值计算：需要注意整数溢出问题
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 提前剪枝：当目标值小于最小数字时直接返回0
 * 2. 排序优化：先对数组排序，可以提前终止内层循环
 * 3. 记忆化搜索：使用DFS+记忆化作为替代方案
 * 
 * 与标准完全背包的区别：
 * 1. 遍历顺序：外层遍历背包容量，内层遍历物品（考虑顺序）
 * 2. 组合定义：顺序不同的序列被视为不同的组合
 * 3. 状态转移：dp[i] += dp[i - nums[j]]
 */

public class Code15_CombinationSumIV {
    
    public static int MOD = 1000000007;  // 防止整数溢出
    
    // 标准DP版本
    public static int combinationSum4(int[] nums, int target) {
        if (nums == null || nums.length == 0) return 0;
        if (target == 0) return 1;
        
        // 创建DP数组
        int[] dp = new int[target + 1];
        dp[0] = 1;  // 总和为0的组合数为1（不选任何数字）
        
        // 外层遍历背包容量，内层遍历物品（考虑顺序）
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                    // 防止整数溢出
                    if (dp[i] >= MOD) dp[i] -= MOD;
                }
            }
        }
        
        return dp[target];
    }
    
    // 优化版本：先排序，可以提前终止内层循环
    public static int combinationSum4Optimized(int[] nums, int target) {
        if (nums == null || nums.length == 0) return 0;
        if (target == 0) return 1;
        
        // 对数组排序
        java.util.Arrays.sort(nums);
        
        // 如果最小的数字都大于target，直接返回0
        if (nums[0] > target) return 0;
        
        int[] dp = new int[target + 1];
        dp[0] = 1;
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i < num) break;  // 提前终止，因为后面的数字更大
                dp[i] += dp[i - num];
                if (dp[i] >= MOD) dp[i] -= MOD;
            }
        }
        
        return dp[target];
    }
    
    // 记忆化搜索版本（DFS + 记忆化）
    public static int combinationSum4Memo(int[] nums, int target) {
        if (nums == null || nums.length == 0) return 0;
        if (target == 0) return 1;
        
        int[] memo = new int[target + 1];
        java.util.Arrays.fill(memo, -1);
        memo[0] = 1;
        
        return dfs(nums, target, memo);
    }
    
    private static int dfs(int[] nums, int target, int[] memo) {
        if (target < 0) return 0;
        if (memo[target] != -1) return memo[target];
        
        int count = 0;
        for (int num : nums) {
            if (target >= num) {
                count += dfs(nums, target - num, memo);
                if (count >= MOD) count -= MOD;
            }
        }
        
        memo[target] = count;
        return count;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        int target1 = 4;
        System.out.println("测试用例1:");
        System.out.println("标准版本: " + combinationSum4(nums1, target1));
        System.out.println("优化版本: " + combinationSum4Optimized(nums1, target1));
        System.out.println("记忆化版本: " + combinationSum4Memo(nums1, target1));
        System.out.println("预期结果: 7");
        System.out.println("解释：可能的组合有：");
        System.out.println("(1, 1, 1, 1), (1, 1, 2), (1, 2, 1), (1, 3), (2, 1, 1), (2, 2), (3, 1)");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {9};
        int target2 = 3;
        System.out.println("测试用例2:");
        System.out.println("标准版本: " + combinationSum4(nums2, target2));
        System.out.println("优化版本: " + combinationSum4Optimized(nums2, target2));
        System.out.println("记忆化版本: " + combinationSum4Memo(nums2, target2));
        System.out.println("预期结果: 0");
        System.out.println();
        
        // 测试用例3：边界情况
        int[] nums3 = {};
        int target3 = 0;
        System.out.println("测试用例3（边界情况）:");
        System.out.println("标准版本: " + combinationSum4(nums3, target3));
        System.out.println("优化版本: " + combinationSum4Optimized(nums3, target3));
        System.out.println("记忆化版本: " + combinationSum4Memo(nums3, target3));
        System.out.println("预期结果: 1");
        System.out.println();
        
        // 测试用例4：较大规模
        int[] nums4 = {1, 2, 4, 8};
        int target4 = 10;
        System.out.println("测试用例4:");
        System.out.println("标准版本: " + combinationSum4(nums4, target4));
        System.out.println("优化版本: " + combinationSum4Optimized(nums4, target4));
        System.out.println("记忆化版本: " + combinationSum4Memo(nums4, target4));
        System.out.println("预期结果: 64");
    }
    
    /*
     * =============================================================================================
     * 补充题目：LeetCode 518. 零钱兑换 II
     * 题目链接：https://leetcode.cn/problems/coin-change-ii/
     * 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
     * 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0。
     * 假设每一种面额的硬币有无限个。顺序不同的序列被视作相同的组合。
     * 
     * 解题思路：
     * 这是一个标准的完全背包求组合数问题。与377题不同，本题不考虑顺序。
     * 
     * 状态定义：dp[i]表示凑成金额i的组合数
     * 状态转移：对于每个硬币coins[j]，更新所有大于等于coins[j]的金额
     *    dp[i] += dp[i - coins[j]]  (当i >= coins[j]时)
     * 遍历顺序：外层遍历物品，内层遍历背包容量（不考虑顺序）
     * 
     * 时间复杂度：O(n * amount)，其中n为硬币种类数
     * 空间复杂度：O(amount)
     * 
     * Java实现：
     * public int change(int amount, int[] coins) {
     *     if (amount == 0) return 1;
     *     if (coins == null || coins.length == 0) return 0;
     *     
     *     int[] dp = new int[amount + 1];
     *     dp[0] = 1;
     *     
     *     // 外层遍历物品，内层遍历背包容量（不考虑顺序）
     *     for (int coin : coins) {
     *         for (int i = coin; i <= amount; i++) {
     *             dp[i] += dp[i - coin];
     *         }
     *     }
     *     
     *     return dp[amount];
     * }
     * 
     * 与377题的关键区别：
     * 1. 遍历顺序：377题外层容量内层物品（考虑顺序），本题外层物品内层容量（不考虑顺序）
     * 2. 组合定义：377题顺序不同视为不同组合，本题顺序不同视为相同组合
     * 3. 应用场景：377题适用于排列问题，本题适用于组合问题
     * 
     * 工程化考量：
     * 1. 边界检查：处理amount为0、coins为空等特殊情况
     * 2. 数值溢出：使用long类型防止整数溢出
     * 3. 性能优化：对于大金额，可以先对硬币排序
     * 
     * 优化思路：
     * 1. 空间压缩：使用一维数组进行优化
     * 2. 剪枝优化：当硬币面额大于剩余金额时跳过
     * 3. 预处理：计算硬币的最大公约数，判断是否有解
     */
}
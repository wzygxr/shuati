package class073;

// LeetCode 322. 零钱兑换
// 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
// 你可以认为每种硬币的数量是无限的。
// 链接：https://leetcode.cn/problems/coin-change/
// 
// 解题思路：
// 这是一个完全背包问题的变形。
// 1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
// 2. 状态转移方程：dp[i] = min(dp[i], dp[i-coin] + 1)，其中 coin 是每种硬币的面额
// 3. 初始状态：dp[0] = 0（凑成金额0不需要硬币），其余初始化为一个较大值
// 4. 遍历顺序：由于硬币可以重复使用，这是完全背包问题，使用正序遍历金额
// 
// 时间复杂度：O(amount * n)，其中 n 是硬币种类数
// 空间复杂度：O(amount)

public class Code14_CoinChange {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] coins1 = {1, 2, 5};
        int amount1 = 11;
        System.out.println("测试用例1结果: " + coinChange(coins1, amount1)); // 预期输出: 3 (11 = 5 + 5 + 1)
        
        // 测试用例2
        int[] coins2 = {2};
        int amount2 = 3;
        System.out.println("测试用例2结果: " + coinChange(coins2, amount2)); // 预期输出: -1
        
        // 测试用例3
        int[] coins3 = {1};
        int amount3 = 0;
        System.out.println("测试用例3结果: " + coinChange(coins3, amount3)); // 预期输出: 0
    }
    
    /**
     * 计算凑成总金额所需的最少硬币个数
     * 
     * 解题思路：
     * 这是一个完全背包问题的变形。
     * 1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
     * 2. 状态转移方程：dp[i] = min(dp[i], dp[i-coin] + 1)，其中 coin 是每种硬币的面额
     * 3. 初始状态：dp[0] = 0（凑成金额0不需要硬币），其余初始化为一个较大值
     * 4. 遍历顺序：由于硬币可以重复使用，这是完全背包问题，使用正序遍历金额
     * 
     * @param coins 不同面额的硬币数组
     * @param amount 目标总金额
     * @return 最少硬币个数，如果无法凑成返回-1
     */
    public static int coinChange(int[] coins, int amount) {
        // 参数验证
        if (coins == null || coins.length == 0) {
            throw new IllegalArgumentException("硬币数组不能为空");
        }
        
        // 边界条件处理
        if (amount < 0) {
            return -1; // 金额不能为负数
        }
        if (amount == 0) {
            return 0; // 金额为0不需要硬币
        }
        
        // 创建dp数组，dp[i]表示凑成金额i所需的最少硬币个数
        // 初始化为amount + 1（因为最多使用amount个面值为1的硬币）
        int[] dp = new int[amount + 1];
        for (int i = 1; i <= amount; i++) {
            dp[i] = amount + 1;
        }
        dp[0] = 0; // 基础情况：凑成金额0需要0个硬币
        
        // 遍历每种硬币（物品）
        for (int coin : coins) {
            // 完全背包问题，正序遍历金额（容量）
            // 这样可以保证每个硬币可以被重复使用
            for (int j = coin; j <= amount; j++) {
                // 状态转移：选择当前硬币或不选当前硬币
                // 如果选择当前硬币，则需要dp[j-coin] + 1个硬币
                // dp[j] = min(不选择当前硬币, 选择当前硬币)
                // 不选择当前硬币：dp[j]（保持原值）
                // 选择当前硬币：dp[j - coin] + 1（前一个状态+1个当前硬币）
                dp[j] = Math.min(dp[j], dp[j - coin] + 1);
            }
        }
        
        // 如果dp[amount]仍为初始值，说明无法凑成该金额
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * 优化版本：提前剪枝和优化
     * 1. 对硬币进行排序，从小到大处理
     * 2. 当发现当前硬币面值大于剩余金额时，可以提前跳过
     * 3. 当找到一个可能的解后，可以尝试进一步优化
     * 
     * @param coins 不同面额的硬币数组
     * @param amount 目标总金额
     * @return 最少硬币个数，如果无法凑成返回-1
     */
    public static int coinChangeOptimized(int[] coins, int amount) {
        // 参数验证
        if (coins == null || coins.length == 0) {
            throw new IllegalArgumentException("硬币数组不能为空");
        }
        
        // 边界条件处理
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // 创建dp数组
        int[] dp = new int[amount + 1];
        for (int i = 1; i <= amount; i++) {
            dp[i] = amount + 1;
        }
        dp[0] = 0;
        
        // 主逻辑与原方法相同
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                dp[j] = Math.min(dp[j], dp[j - coin] + 1);
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * 工程化版本：包含详细的异常处理和日志记录
     * 在实际项目中，可以根据需要添加日志记录
     * 
     * @param coins 不同面额的硬币数组
     * @param amount 目标总金额
     * @return 最少硬币个数，如果无法凑成返回-1
     */
    public static int coinChangeProduction(int[] coins, int amount) {
        try {
            // 输入参数验证
            if (coins == null) {
                throw new NullPointerException("硬币数组不能为null");
            }
            if (coins.length == 0) {
                throw new IllegalArgumentException("硬币数组不能为空");
            }
            
            // 检查硬币面值是否有效
            for (int coin : coins) {
                if (coin <= 0) {
                    throw new IllegalArgumentException("硬币面值必须为正数: " + coin);
                }
            }
            
            // 调用核心算法
            return coinChange(coins, amount);
        } catch (Exception e) {
            // 在实际项目中，这里应该记录异常日志
            System.err.println("计算硬币兑换时发生错误: " + e.getMessage());
            throw e; // 重新抛出异常，让调用者处理
        }
    }
    
    /*
     * 示例:
     * 输入: coins = [1, 2, 5], amount = 11
     * 输出: 3
     * 解释: 11 = 5 + 5 + 1
     *
     * 输入: coins = [2], amount = 3
     * 输出: -1
     *
     * 输入: coins = [1], amount = 0
     * 输出: 0
     *
     * 时间复杂度: O(amount * n)
     *   - 外层循环遍历所有硬币：O(n)
     *   - 内层循环遍历金额：O(amount)
     * 空间复杂度: O(amount)
     *   - 一维DP数组的空间消耗
     */
}
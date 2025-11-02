/*
 * LeetCode 322. Coin Change (硬币找零)
 * 题目描述：给定不同面额的硬币 coins 和一个总金额 amount。
 * 编写一个函数来计算可以凑成总金额所需的最少的硬币个数。
 * 如果没有任何一种硬币组合能组成总金额，返回 -1。
 * 
 * 解题思路：
 * 这是一个经典的动态规划问题：
 * 1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币数
 * 2. 状态转移方程：dp[i] = min(dp[i], dp[i - coin] + 1) for each coin in coins
 * 3. 初始状态：dp[0] = 0，其他初始化为一个大值
 * 
 * 时间复杂度：O(amount * coins.length)
 * 空间复杂度：O(amount)
 * 
 * 工程化考量：
 * 1. 异常处理：处理无效输入（负数、空数组等）
 * 2. 边界条件：amount为0时返回0
 * 3. 性能优化：可以使用滚动数组优化空间复杂度
 * 4. 可扩展性：可以轻松扩展支持不同的硬币面额
 */

// C++实现
int coinChange(int coins[], int coinsSize, int amount) {
    // 异常处理
    if (amount < 0 || coinsSize <= 0) {
        return -1;
    }
    
    // 边界条件
    if (amount == 0) {
        return 0;
    }
    
    // dp[i] 表示凑成金额 i 所需的最少硬币数
    // 使用固定大小数组代替vector
    int dp[10000]; // 假设amount不超过10000
    
    // 初始化为一个大值
    for (int i = 0; i <= amount; i++) {
        dp[i] = amount + 1;
    }
    
    // 初始状态
    dp[0] = 0;
    
    // 状态转移
    for (int i = 1; i <= amount; i++) {
        for (int j = 0; j < coinsSize; j++) {
            if (i >= coins[j]) {
                int prev = dp[i - coins[j]];
                if (prev + 1 < dp[i]) {
                    dp[i] = prev + 1;
                }
            }
        }
    }
    
    // 返回结果
    return dp[amount] > amount ? -1 : dp[amount];
}

// 注意：由于系统环境限制，这里省略了main函数和测试代码
// 在实际环境中，需要包含适当的头文件才能编译运行
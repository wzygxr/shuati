package class073;

// LeetCode 322. 零钱兑换
// 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
// 你可以认为每种硬币的数量是无限的。
// 链接：https://leetcode.cn/problems/coin-change/
// 
// 解题思路：
// 这是一个经典的完全背包问题。我们需要找到凑成总金额amount所需的最少硬币数量，每种硬币可以重复使用。
// 
// 状态定义：dp[j] 表示凑成总金额j所需的最少硬币数量
// 状态转移方程：dp[j] = min(dp[j], dp[j - coin] + 1)，其中coin是当前硬币的面额，且j >= coin
// 初始状态：dp[0] = 0，表示凑成总金额0所需的最少硬币数量为0；对于其他j，初始化为一个较大的值（如amount + 1）
// 
// 时间复杂度：O(amount * n)，其中amount是总金额，n是硬币的种类数
// 空间复杂度：O(amount)，使用一维DP数组

public class Code43_CoinChange {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] coins1 = {1, 2, 5};
        int amount1 = 11;
        System.out.println("测试用例1结果: " + coinChange(coins1, amount1)); // 预期输出: 3
        
        // 测试用例2
        int[] coins2 = {2};
        int amount2 = 3;
        System.out.println("测试用例2结果: " + coinChange(coins2, amount2)); // 预期输出: -1
        
        // 测试用例3
        int[] coins3 = {1};
        int amount3 = 0;
        System.out.println("测试用例3结果: " + coinChange(coins3, amount3)); // 预期输出: 0
        
        // 测试用例4
        int[] coins4 = {186, 419, 83, 408};
        int amount4 = 6249;
        System.out.println("测试用例4结果: " + coinChange(coins4, amount4)); // 预期输出: 20
    }
    
    /**
     * 计算凑成总金额所需的最少硬币数量
     * @param coins 硬币面额数组
     * @param amount 总金额
     * @return 最少硬币数量，如果无法凑出则返回-1
     */
    public static int coinChange(int[] coins, int amount) {
        if (amount < 0 || coins == null || coins.length == 0) {
            return amount == 0 ? 0 : -1;
        }
        
        // 创建DP数组，dp[j]表示凑成总金额j所需的最少硬币数量
        // 初始化为一个较大的值（amount + 1），因为最多需要amount个1元硬币
        int[] dp = new int[amount + 1];
        for (int j = 1; j <= amount; j++) {
            dp[j] = amount + 1;
        }
        
        // 初始状态：凑成总金额0所需的最少硬币数量为0
        dp[0] = 0;
        
        // 填充DP数组
        // 遍历硬币
        for (int coin : coins) {
            // 遍历金额
            for (int j = coin; j <= amount; j++) {
                // 更新凑成总金额j所需的最少硬币数量
                dp[j] = Math.min(dp[j], dp[j - coin] + 1);
            }
        }
        
        // 如果dp[amount]仍然是初始值，说明无法凑出总金额
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * 从金额角度出发的实现
     * 这种实现方式与上面的类似，只是遍历顺序不同
     */
    public static int coinChange2(int[] coins, int amount) {
        if (amount < 0 || coins == null || coins.length == 0) {
            return amount == 0 ? 0 : -1;
        }
        
        int[] dp = new int[amount + 1];
        for (int j = 1; j <= amount; j++) {
            dp[j] = amount + 1;
        }
        dp[0] = 0;
        
        // 遍历金额
        for (int j = 1; j <= amount; j++) {
            // 遍历硬币
            for (int coin : coins) {
                if (coin <= j) {
                    dp[j] = Math.min(dp[j], dp[j - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * 广度优先搜索(BFS)实现
     * 将问题视为图的最短路径问题：从0到amount的最短路径
     */
    public static int coinChangeBFS(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (coins == null || coins.length == 0 || amount < 0) {
            return -1;
        }
        
        // 使用队列进行BFS
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        // 使用数组记录访问过的金额，避免重复计算
        boolean[] visited = new boolean[amount + 1];
        
        // 将0加入队列，表示初始金额
        queue.offer(0);
        visited[0] = true;
        
        int level = 0; // 记录层数，即硬币数量
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            level++;
            
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                
                // 尝试每种硬币
                for (int coin : coins) {
                    int next = current + coin;
                    
                    // 如果达到目标金额，返回当前层数
                    if (next == amount) {
                        return level;
                    }
                    
                    // 如果没有超过目标金额且未访问过，则加入队列
                    if (next < amount && !visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }
        }
        
        // 无法凑出总金额
        return -1;
    }
    
    /**
     * 贪心 + 回溯 实现
     * 注意：这种方法不一定能得到正确的结果，因为贪心策略不一定适用于所有硬币组合
     * 例如，对于 coins = [1, 3, 4], amount = 6，贪心会选择 [4, 1, 1]（3个硬币），但最优解是 [3, 3]（2个硬币）
     */
    public static int coinChangeGreedy(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (coins == null || coins.length == 0 || amount < 0) {
            return -1;
        }
        
        // 按面额降序排序
        java.util.Arrays.sort(coins);
        int result = Integer.MAX_VALUE;
        
        // 回溯搜索
        backtrack(coins, amount, coins.length - 1, 0, result);
        
        return result == Integer.MAX_VALUE ? -1 : result;
    }
    
    /**
     * 回溯辅助方法
     */
    private static void backtrack(int[] coins, int amount, int index, int count, int result) {
        // 已经找到一个解，或者无法继续使用更大的硬币
        if (amount == 0) {
            result = Math.min(result, count);
            return;
        }
        if (index < 0) {
            return;
        }
        
        // 尝试使用当前硬币的最大可能数量
        for (int i = amount / coins[index]; i >= 0 && count + i < result; i--) {
            backtrack(coins, amount - i * coins[index], index - 1, count + i, result);
        }
    }
    
    /**
     * 优化的回溯方法，使用记忆化搜索
     */
    public static int coinChangeMemo(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (coins == null || coins.length == 0 || amount < 0) {
            return -1;
        }
        
        // 按面额降序排序，有助于剪枝
        java.util.Arrays.sort(coins);
        
        // 创建记忆化缓存
        int[] memo = new int[amount + 1];
        return backtrackMemo(coins, amount, memo);
    }
    
    /**
     * 记忆化搜索辅助方法
     */
    private static int backtrackMemo(int[] coins, int amount, int[] memo) {
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }
        
        // 检查是否已经计算过
        if (memo[amount] != 0) {
            return memo[amount];
        }
        
        int minCount = Integer.MAX_VALUE;
        
        // 尝试每种硬币
        for (int coin : coins) {
            int subResult = backtrackMemo(coins, amount - coin, memo);
            if (subResult >= 0 && subResult < minCount) {
                minCount = subResult + 1;
            }
        }
        
        // 记录结果
        memo[amount] = (minCount == Integer.MAX_VALUE) ? -1 : minCount;
        
        return memo[amount];
    }
    
    /**
     * 打印出一种最优的硬币组合
     */
    public static void printOptimalCoins(int[] coins, int amount) {
        if (amount == 0) {
            System.out.println("无需硬币");
            return;
        }
        if (coins == null || coins.length == 0 || amount < 0) {
            System.out.println("无法凑出总金额");
            return;
        }
        
        // 计算最少硬币数量
        int[] dp = new int[amount + 1];
        for (int j = 1; j <= amount; j++) {
            dp[j] = amount + 1;
        }
        dp[0] = 0;
        
        // 额外创建一个数组，用于记录每个金额使用的最后一个硬币
        int[] lastCoin = new int[amount + 1];
        
        // 填充DP数组
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                if (dp[j] > dp[j - coin] + 1) {
                    dp[j] = dp[j - coin] + 1;
                    lastCoin[j] = coin;
                }
            }
        }
        
        if (dp[amount] > amount) {
            System.out.println("无法凑出总金额");
            return;
        }
        
        // 回溯构建最优硬币组合
        java.util.List<Integer> result = new java.util.ArrayList<>();
        int current = amount;
        while (current > 0) {
            int coin = lastCoin[current];
            result.add(coin);
            current -= coin;
        }
        
        // 输出结果
        System.out.print("最优硬币组合: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" + ");
            }
        }
        System.out.println(" = " + amount);
        System.out.println("最少硬币数量: " + dp[amount]);
    }
    
    /**
     * 优化版本，提前过滤掉大于amount的硬币
     */
    public static int coinChangeOptimized(int[] coins, int amount) {
        if (amount < 0 || coins == null || coins.length == 0) {
            return amount == 0 ? 0 : -1;
        }
        
        // 过滤掉大于amount的硬币
        java.util.List<Integer> filteredCoins = new java.util.ArrayList<>();
        for (int coin : coins) {
            if (coin <= amount) {
                filteredCoins.add(coin);
            }
        }
        
        if (filteredCoins.isEmpty()) {
            return amount == 0 ? 0 : -1;
        }
        
        // 转换回数组
        int[] filteredCoinsArray = new int[filteredCoins.size()];
        for (int i = 0; i < filteredCoins.size(); i++) {
            filteredCoinsArray[i] = filteredCoins.get(i);
        }
        
        int[] dp = new int[amount + 1];
        for (int j = 1; j <= amount; j++) {
            dp[j] = amount + 1;
        }
        dp[0] = 0;
        
        for (int coin : filteredCoinsArray) {
            for (int j = coin; j <= amount; j++) {
                dp[j] = Math.min(dp[j], dp[j - coin] + 1);
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
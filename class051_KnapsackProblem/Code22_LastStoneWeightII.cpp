// LeetCode 1049. 最后一块石头的重量 II
// 题目描述：有一堆石头，每块石头的重量都是正整数。
// 每一回合，从中选出任意两块石头，然后将它们一起粉碎。假设石头的重量分别为x和y，且x <= y。
// 那么粉碎的可能结果如下：
// - 如果x == y，那么两块石头都会被完全粉碎；
// - 如果x != y，那么重量为x的石头将会完全粉碎，而重量为y的石头新重量为y-x。
// 最后，最多只会剩下一块石头。返回此石头最小的可能重量。如果没有石头剩下，就返回0。
// 链接：https://leetcode.cn/problems/last-stone-weight-ii/
// 
// 解题思路：
// 这是一个可以转化为01背包问题的变种。我们的目标是将石头分成两堆，使得它们的重量尽可能接近，
// 这样最后剩下的石头重量就会最小（等于两堆重量之差）。
// 
// 具体分析：
// 1. 如果我们能将石头分成两堆，重量分别为sum1和sum2，那么最终剩下的石头重量为|sum1 - sum2|
// 2. 由于sum1 + sum2 = totalSum（石头总重量），所以剩下的重量为|totalSum - 2*sum1|
// 3. 为了最小化这个值，我们需要让sum1尽可能接近totalSum/2
// 4. 这转化为：在石头中选择一些，使得它们的总重量不超过totalSum/2，且尽可能接近totalSum/2
// 5. 这正是一个01背包问题，背包容量为totalSum/2，物品重量为石头重量，目标是最大化能装入的重量
// 
// 状态定义：dp[i] 表示是否可以组成和为i的石头堆
// 状态转移方程：dp[i] = dp[i] || dp[i - stones[j]]，其中j遍历所有石头，且i >= stones[j]
// 初始状态：dp[0] = true（表示和为0可以通过不选任何石头来实现）
// 
// 时间复杂度：O(n * target)，其中n是石头数量，target是总重量的一半
// 空间复杂度：O(target)，使用一维DP数组

/**
 * 计算最后一块石头的最小可能重量
 * 
 * 解题思路：
 * 1. 这道题可以转化为将石头分为两堆，使得两堆重量差最小
 * 2. 假设两堆分别为 A 和 B，A >= B
 * 3. 最终剩下的石头重量就是 A - B
 * 4. 要使 A - B 最小，就要使 B 尽可能接近 sum/2
 * 5. 问题转化为：在不超过 sum/2 的前提下，背包最多能装多少重量的石头
 * 6. 这就是一个标准的01背包问题
 * 
 * @param stones 石头重量数组
 * @param stonesSize 石头数量
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightII(int* stones, int stonesSize) {
    // 参数验证
    if (stones == 0 || stonesSize == 0) {
        return 0;
    }
    if (stonesSize == 1) {
        return stones[0];
    }
    
    // 计算石头总重量
    int totalSum = 0;
    for (int i = 0; i < stonesSize; i++) {
        totalSum += stones[i];
    }
    
    // 目标是找到不超过totalSum/2的最大子集和
    int target = totalSum / 2;
    
    // 创建DP数组，dp[i]表示是否可以组成和为i的石头堆
    // 由于不能使用动态内存分配，我们使用固定大小的数组
    // 假设target最大不超过10000
    bool dp[10001] = {false};
    
    // 初始状态：和为0可以通过不选任何石头来实现
    dp[0] = true;
    
    // 遍历每个石头（物品）
    for (int i = 0; i < stonesSize; i++) {
        int stone = stones[i];
        // 逆序遍历目标和（容量），防止重复使用同一个石头
        for (int j = target; j >= stone; j--) {
            // 状态转移：选择当前石头或不选择当前石头
            dp[j] = dp[j] || dp[j - stone];
        }
    }
    
    // 找到最大的i，使得dp[i]为true
    int maxSum = 0;
    for (int i = target; i >= 0; i--) {
        if (dp[i]) {
            maxSum = i;
            break;
        }
    }
    
    // 最后一块石头的最小可能重量为总重量减去两倍的最大子集和
    return totalSum - 2 * maxSum;
}

/**
 * 优化版本：使用一维DP数组记录可以达到的最大和
 * 
 * 解题思路：
 * 在基础版本的基础上进行优化，通过记录当前可以达到的最大和来提前结束循环
 * 
 * @param stones 石头重量数组
 * @param stonesSize 石头数量
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightIIOptimized(int* stones, int stonesSize) {
    // 参数验证
    if (stones == 0 || stonesSize == 0) {
        return 0;
    }
    if (stonesSize == 1) {
        return stones[0];
    }
    
    // 计算石头总重量
    int totalSum = 0;
    for (int i = 0; i < stonesSize; i++) {
        totalSum += stones[i];
    }
    
    // 目标是找到不超过totalSum/2的最大子集和
    int target = totalSum / 2;
    
    // 创建DP数组，dp[i]表示是否可以组成和为i的石头堆
    bool dp[10001] = {false};
    dp[0] = true;
    
    // 记录当前可以达到的最大和
    int currentMax = 0;
    
    for (int i = 0; i < stonesSize; i++) {
        int stone = stones[i];
        // 逆序遍历，但只遍历到当前可能的最大和+stone
        int limit = (target < (currentMax + stone)) ? target : (currentMax + stone);
        for (int j = limit; j >= stone; j--) {
            if (dp[j - stone]) {
                dp[j] = true;
                // 更新当前可以达到的最大和
                currentMax = (currentMax > j) ? currentMax : j;
                // 如果已经可以达到target，提前结束
                if (currentMax == target) {
                    return totalSum - 2 * target;
                }
            }
        }
    }
    
    return totalSum - 2 * currentMax;
}

/**
 * 二维DP实现，更容易理解
 * 
 * 解题思路：
 * 使用二维DP数组，dp[i][j]表示前i个石头是否可以组成和为j的石头堆
 * 状态转移方程：dp[i][j] = dp[i-1][j] || dp[i-1][j-stones[i-1]]
 * 
 * @param stones 石头重量数组
 * @param stonesSize 石头数量
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightII2D(int* stones, int stonesSize) {
    // 参数验证
    if (stones == 0 || stonesSize == 0) {
        return 0;
    }
    if (stonesSize == 1) {
        return stones[0];
    }
    
    int totalSum = 0;
    for (int i = 0; i < stonesSize; i++) {
        totalSum += stones[i];
    }
    
    int target = totalSum / 2;
    
    // dp[i][j]表示前i个石头是否可以组成和为j的石头堆
    // 由于不能使用动态内存分配，我们使用固定大小的数组
    bool dp[51][10001] = {{false}}; // 假设石头数量不超过50，目标和不超过10000
    
    // 初始化：前0个石头只能组成和为0的石头堆
    dp[0][0] = true;
    
    // 遍历每个石头
    for (int i = 1; i <= stonesSize; i++) {
        int stone = stones[i - 1];
        // 遍历每个可能的和
        for (int j = 0; j <= target; j++) {
            // 不选择当前石头
            dp[i][j] = dp[i - 1][j];
            
            // 选择当前石头（如果j >= stone）
            if (j >= stone) {
                dp[i][j] = dp[i][j] || dp[i - 1][j - stone];
            }
        }
    }
    
    // 找到最大的j，使得dp[stonesSize][j]为true
    int maxSum = 0;
    for (int j = target; j >= 0; j--) {
        if (dp[stonesSize][j]) {
            maxSum = j;
            break;
        }
    }
    
    return totalSum - 2 * maxSum;
}
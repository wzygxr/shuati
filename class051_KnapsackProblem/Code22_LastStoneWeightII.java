package class073;

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

public class Code22_LastStoneWeightII {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] stones1 = {2, 7, 4, 1, 8, 1};
        System.out.println("测试用例1结果: " + lastStoneWeightII(stones1)); // 预期输出: 1
        // 解释: 
        // 组合2和4，得到2，所以数组转化为 [2,7,1,8,1]
        // 组合7和8，得到1，所以数组转化为 [2,1,1,1]
        // 组合2和1，得到1，所以数组转化为 [1,1]
        // 组合1和1，得到0，所以最终只剩下0
        
        // 测试用例2
        int[] stones2 = {31, 26, 33, 21, 40};
        System.out.println("测试用例2结果: " + lastStoneWeightII(stones2)); // 预期输出: 5
        
        // 测试用例3
        int[] stones3 = {1, 2};
        System.out.println("测试用例3结果: " + lastStoneWeightII(stones3)); // 预期输出: 1
        
        // 测试用例4
        int[] stones4 = {1};
        System.out.println("测试用例4结果: " + lastStoneWeightII(stones4)); // 预期输出: 1
    }
    
    /**
     * 计算最后一块石头的最小可能重量
     * @param stones 石头重量数组
     * @return 最后一块石头的最小可能重量
     */
    public static int lastStoneWeightII(int[] stones) {
        // 参数验证
        if (stones == null || stones.length == 0) {
            return 0;
        }
        if (stones.length == 1) {
            return stones[0];
        }
        
        // 计算石头总重量
        int totalSum = 0;
        for (int stone : stones) {
            totalSum += stone;
        }
        
        // 目标是找到不超过totalSum/2的最大子集和
        int target = totalSum / 2;
        
        // 创建DP数组，dp[i]表示是否可以组成和为i的石头堆
        boolean[] dp = new boolean[target + 1];
        
        // 初始状态：和为0可以通过不选任何石头来实现
        dp[0] = true;
        
        // 遍历每个石头（物品）
        for (int stone : stones) {
            // 逆序遍历目标和（容量），防止重复使用同一个石头
            for (int i = target; i >= stone; i--) {
                // 状态转移：选择当前石头或不选择当前石头
                dp[i] = dp[i] || dp[i - stone];
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
     */
    public static int lastStoneWeightIIOptimized(int[] stones) {
        // 参数验证
        if (stones == null || stones.length == 0) {
            return 0;
        }
        if (stones.length == 1) {
            return stones[0];
        }
        
        // 计算石头总重量
        int totalSum = 0;
        for (int stone : stones) {
            totalSum += stone;
        }
        
        // 目标是找到不超过totalSum/2的最大子集和
        int target = totalSum / 2;
        
        // 创建DP数组，dp[i]表示是否可以组成和为i的石头堆
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        
        // 记录当前可以达到的最大和
        int currentMax = 0;
        
        for (int stone : stones) {
            // 逆序遍历
            for (int i = Math.min(target, currentMax + stone); i >= stone; i--) {
                if (dp[i - stone]) {
                    dp[i] = true;
                    // 更新当前可以达到的最大和
                    currentMax = Math.max(currentMax, i);
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
     */
    public static int lastStoneWeightII2D(int[] stones) {
        // 参数验证
        if (stones == null || stones.length == 0) {
            return 0;
        }
        if (stones.length == 1) {
            return stones[0];
        }
        
        int totalSum = 0;
        for (int stone : stones) {
            totalSum += stone;
        }
        
        int target = totalSum / 2;
        int n = stones.length;
        
        // dp[i][j]表示前i个石头是否可以组成和为j的石头堆
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始化：前0个石头只能组成和为0的石头堆
        dp[0][0] = true;
        
        // 遍历每个石头
        for (int i = 1; i <= n; i++) {
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
        
        // 找到最大的j，使得dp[n][j]为true
        int maxSum = 0;
        for (int j = target; j >= 0; j--) {
            if (dp[n][j]) {
                maxSum = j;
                break;
            }
        }
        
        return totalSum - 2 * maxSum;
    }
    
    /**
     * 递归+记忆化搜索实现
     */
    public static int lastStoneWeightIIDFS(int[] stones) {
        // 参数验证
        if (stones == null || stones.length == 0) {
            return 0;
        }
        if (stones.length == 1) {
            return stones[0];
        }
        
        // 计算石头总重量
        int totalSum = 0;
        for (int stone : stones) {
            totalSum += stone;
        }
        
        // 目标是找到不超过totalSum/2的最大子集和
        int target = totalSum / 2;
        
        // 使用记忆化搜索，memo[i][j]表示从第i个石头开始，当前和为j时是否可以继续选择石头
        // 由于只需要知道是否可达，我们可以使用布尔型数组
        Boolean[][] memo = new Boolean[stones.length][target + 1];
        
        // 尝试找到最大的不超过target的子集和
        int maxSum = dfs(stones, 0, 0, target, memo);
        
        return totalSum - 2 * maxSum;
    }
    
    /**
     * 递归辅助函数，返回从index开始，当前和为currentSum时，可以达到的不超过target的最大子集和
     */
    private static int dfs(int[] stones, int index, int currentSum, int target, Boolean[][] memo) {
        // 基础情况：已经处理完所有石头，或者当前和已经达到目标
        if (index == stones.length || currentSum == target) {
            return currentSum;
        }
        
        // 如果已经计算过，直接返回结果
        if (memo[index][currentSum] != null) {
            // 如果返回false，表示从这个状态无法达到更大的和，返回当前和
            return currentSum;
        }
        
        // 不选择当前石头
        int notTake = dfs(stones, index + 1, currentSum, target, memo);
        
        // 选择当前石头（如果当前和加上石头重量不超过目标）
        int take = currentSum;
        if (currentSum + stones[index] <= target) {
            take = dfs(stones, index + 1, currentSum + stones[index], target, memo);
        }
        
        // 记录这个状态可以到达更大的和
        memo[index][currentSum] = (notTake > currentSum || take > currentSum);
        
        // 返回较大的那个结果
        return Math.max(notTake, take);
    }
    
    /**
     * 位运算优化版本
     * 使用位图记录所有可能的子集和
     */
    public static int lastStoneWeightIIBitwise(int[] stones) {
        // 参数验证
        if (stones == null || stones.length == 0) {
            return 0;
        }
        if (stones.length == 1) {
            return stones[0];
        }
        
        // 计算石头总重量
        int totalSum = 0;
        for (int stone : stones) {
            totalSum += stone;
        }
        
        int target = totalSum / 2;
        
        // 使用位掩码表示所有可能的子集和
        // bit[i] = 1表示可以组成和为i的子集
        int dp = 1; // 初始状态：可以组成和为0的子集
        
        for (int stone : stones) {
            // 对于每个石头，更新可能的子集和集合
            dp |= dp << stone;
        }
        
        // 找到最大的i <= target，使得dp的第i位为1
        int maxSum = 0;
        for (int i = target; i >= 0; i--) {
            if ((dp & (1 << i)) != 0) {
                maxSum = i;
                break;
            }
        }
        
        return totalSum - 2 * maxSum;
    }
}
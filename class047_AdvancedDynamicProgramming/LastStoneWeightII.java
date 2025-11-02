package class069;

/**
 * 最后一块石头的重量 II (Last Stone Weight II) - 背包问题变形
 * 
 * 题目描述：
 * 有一堆石头，用整数数组 stones 表示。其中 stones[i] 表示第 i 块石头的重量。
 * 每一回合，从中选出任意两块石头，然后将它们一起粉碎。
 * 假设石头的重量分别为 x 和 y，且 x <= y。粉碎的可能结果如下：
 * 如果 x == y，那么两块石头都会被完全粉碎；
 * 如果 x != y，那么重量为 x 的石头完全粉碎，重量为 y 的石头新重量为 y-x。
 * 最后，最多只会剩下一块石头。返回此石头最小的可能重量。如果没有石头剩下，就返回 0。
 * 
 * 题目来源：LeetCode 1049. 最后一块石头的重量 II
 * 测试链接：https://leetcode.cn/problems/last-stone-weight-ii/
 * 
 * 解题思路：
 * 这道题可以转化为经典的背包问题。我们希望最后剩下的石头重量最小，相当于要将石头分成两堆，使得两堆的重量差最小。
 * 设石头总重量为 sum，其中一堆的重量为 s，则另一堆的重量为 sum - s。
 * 两堆重量差为 |s - (sum - s)| = |2*s - sum|。
 * 要使差值最小，就要使 s 尽可能接近 sum/2。
 * 所以问题转化为：在石头中选择一些，使其总重量尽可能接近 sum/2，但不超过 sum/2。
 * 这就变成了一个 0-1 背包问题，背包容量为 sum/2，物品重量和价值都为 stones[i]。
 * 
 * 算法实现：
 * 1. 动态规划：使用背包DP求解最大可装重量
 * 2. 记忆化搜索：递归枚举所有选择方案
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n * sum)，其中n是数组长度，sum是数组元素和
 * - 记忆化搜索：O(n * sum)，每个状态计算一次
 * 
 * 空间复杂度分析：
 * - 动态规划：O(sum)，一维DP数组
 * - 记忆化搜索：O(n * sum)，二维记忆化数组
 * 
 * 关键技巧：
 * 1. 问题转化：将石头粉碎问题转化为背包问题
 * 2. 数学推导：最小剩余重量 = sum - 2 * max_weight
 * 3. 背包容量：设置为sum/2，不超过一半总重量
 * 
 * 工程化考量：
 * 1. 边界条件：处理数组为空或单元素的情况
 * 2. 性能优化：动态规划优于记忆化搜索
 * 3. 空间优化：使用一维数组滚动更新
 * 4. 可测试性：提供多种规模的测试用例
 */
public class LastStoneWeightII {
    
    /**
     * 动态规划解法
     * 
     * @param stones 石头重量数组
     * @return 最后剩下的石头最小重量
     */
    public static int lastStoneWeightII(int[] stones) {
        // 计算总重量
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }
        
        // 背包容量为 sum/2
        int target = sum / 2;
        
        // dp[i] 表示容量为 i 的背包最多能装的石头重量
        int[] dp = new int[target + 1];
        
        // 遍历每个石头
        for (int stone : stones) {
            // 从后往前更新，避免重复使用同一个石头
            for (int j = target; j >= stone; j--) {
                dp[j] = Math.max(dp[j], dp[j - stone] + stone);
            }
        }
        
        // 两堆石头的重量差
        return sum - 2 * dp[target];
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param stones 石头重量数组
     * @return 最后剩下的石头最小重量
     */
    public static int lastStoneWeightII2(int[] stones) {
        int n = stones.length;
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }
        
        int target = sum / 2;
        // 记忆化数组
        int[][] memo = new int[n][target + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= target; j++) {
                memo[i][j] = -1;
            }
        }
        
        // 计算能装入背包的最大重量
        int maxWeight = dfs(stones, 0, target, memo);
        
        // 两堆石头的重量差
        return sum - 2 * maxWeight;
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param stones 石头数组
     * @param i 当前处理到第几个石头
     * @param capacity 背包剩余容量
     * @param memo 记忆化数组
     * @return 当前状态下能装入背包的最大重量
     */
    private static int dfs(int[] stones, int i, int capacity, int[][] memo) {
        // 边界条件：处理完所有石头或背包容量为0
        if (i == stones.length || capacity == 0) {
            return 0;
        }
        
        // 检查是否已经计算过
        if (memo[i][capacity] != -1) {
            return memo[i][capacity];
        }
        
        // 不选择当前石头
        int ans = dfs(stones, i + 1, capacity, memo);
        
        // 选择当前石头（如果容量足够）
        if (capacity >= stones[i]) {
            ans = Math.max(ans, dfs(stones, i + 1, capacity - stones[i], memo) + stones[i]);
        }
        
        // 记忆化存储
        memo[i][capacity] = ans;
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] stones1 = {2, 7, 4, 1, 8, 1};
        System.out.println("测试用例1:");
        System.out.println("石头重量: [2,7,4,1,8,1]");
        System.out.println("方法1结果: " + lastStoneWeightII(stones1));
        System.out.println("方法2结果: " + lastStoneWeightII2(stones1));
        System.out.println();
        
        // 测试用例2
        int[] stones2 = {31, 26, 33, 21, 40};
        System.out.println("测试用例2:");
        System.out.println("石头重量: [31,26,33,21,40]");
        System.out.println("方法1结果: " + lastStoneWeightII(stones2));
        System.out.println("方法2结果: " + lastStoneWeightII2(stones2));
        System.out.println();
        
        // 测试用例3
        int[] stones3 = {1, 2};
        System.out.println("测试用例3:");
        System.out.println("石头重量: [1,2]");
        System.out.println("方法1结果: " + lastStoneWeightII(stones3));
        System.out.println("方法2结果: " + lastStoneWeightII2(stones3));
    }
}
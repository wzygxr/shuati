package class066.supplementary_problems;

/**
 * AtCoder Educational DP Contest A - Frog 1
 * 
 * 题目来源：AtCoder Educational DP Contest Problem A - Frog 1
 * 题目链接：https://atcoder.jp/contests/dp/tasks/dp_a
 * 
 * 题目描述：
 * 有N个石头排成一排，从左到右编号为1, 2, ..., N。
 * 青蛙从石头1开始，想跳到石头N。
 * 石头i的高度是h[i]。
 * 青蛙可以从石头i跳到石头i+1或石头i+2（如果存在）。
 * 从石头i跳到石头j的代价是|h[i] - h[j]|。
 * 求青蛙从石头1跳到石头N的最小总代价。
 * 
 * 输入格式：
 * 第一行包含一个整数N（2 ≤ N ≤ 10^5）
 * 第二行包含N个整数h[1], h[2], ..., h[N]（1 ≤ h[i] ≤ 10^4）
 * 
 * 输出格式：
 * 输出一个整数，表示最小总代价。
 * 
 * 示例：
 * 输入：
 * 4
 * 10 30 40 20
 * 输出：
 * 30
 * 
 * 解释：
 * 一种最优路径是1→2→4，代价为|10-30| + |30-20| = 20 + 10 = 30
 * 
 * 解题思路：
 * 这是一个经典的动态规划问题。
 * 状态定义：dp[i]表示从石头1跳到石头i的最小代价
 * 状态转移：dp[i] = min(dp[i-1] + |h[i] - h[i-1]|, dp[i-2] + |h[i] - h[i-2]|)
 * 边界条件：dp[1] = 0
 * 
 * 算法复杂度分析：
 * - 时间复杂度：O(N) - 线性扫描
 * - 空间复杂度：O(N) - dp数组存储所有状态
 * 
 * 工程化考量：
 * 1. 边界处理：正确处理N=2的特殊情况
 * 2. 性能优化：使用迭代代替递归
 * 3. 代码质量：清晰的变量命名和详细的注释说明
 * 
 * 相关题目：
 * - AtCoder Educational DP Contest B - Frog 2
 * - LeetCode 70. 爬楼梯
 * - 牛客网 剑指Offer 10-I. 斐波那契数列
 */
public class AtCoder_DP_A_Frog1 {
    
    /**
     * 计算青蛙从石头1跳到石头N的最小总代价
     * 
     * @param heights 石头高度数组
     * @return 最小总代价
     */
    public static int minCost(int[] heights) {
        int n = heights.length;
        if (n <= 1) return 0;
        
        // dp[i]表示从石头1跳到石头i+1的最小代价
        int[] dp = new int[n];
        dp[0] = 0; // 起点不需要代价
        
        // 计算每个石头的最小代价
        for (int i = 1; i < n; i++) {
            // 从前面一个石头跳过来
            dp[i] = dp[i - 1] + Math.abs(heights[i] - heights[i - 1]);
            
            // 如果可以从前面两个石头跳过来，比较两种方案的代价
            if (i > 1) {
                dp[i] = Math.min(dp[i], dp[i - 2] + Math.abs(heights[i] - heights[i - 2]));
            }
        }
        
        return dp[n - 1]; // 返回到达最后一个石头的最小代价
    }
    
    /**
     * 空间优化版本
     * 
     * @param heights 石头高度数组
     * @return 最小总代价
     */
    public static int minCostOptimized(int[] heights) {
        int n = heights.length;
        if (n <= 1) return 0;
        
        // 只需要保存前两个状态
        int prev2 = 0; // dp[i-2]
        int prev1 = Math.abs(heights[1] - heights[0]); // dp[i-1]
        
        // 从第三个石头开始计算
        for (int i = 2; i < n; i++) {
            int current = Math.min(
                prev1 + Math.abs(heights[i] - heights[i - 1]), // 从前一个石头跳过来
                prev2 + Math.abs(heights[i] - heights[i - 2])  // 从前两个石头跳过来
            );
            
            // 更新状态
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    // 测试用例
    public static void main(String[] args) {
        System.out.println("测试AtCoder DP Contest A - Frog 1：");
        
        // 测试用例1
        int[] heights1 = {10, 30, 40, 20};
        System.out.println("石头高度: [10, 30, 40, 20]");
        System.out.println("最小代价（方法1）: " + minCost(heights1));
        System.out.println("最小代价（方法2）: " + minCostOptimized(heights1));
        System.out.println("预期结果: 30\n");
        
        // 测试用例2
        int[] heights2 = {10, 10};
        System.out.println("石头高度: [10, 10]");
        System.out.println("最小代价（方法1）: " + minCost(heights2));
        System.out.println("最小代价（方法2）: " + minCostOptimized(heights2));
        System.out.println("预期结果: 0\n");
        
        // 测试用例3
        int[] heights3 = {30, 10, 60, 10, 60, 50};
        System.out.println("石头高度: [30, 10, 60, 10, 60, 50]");
        System.out.println("最小代价（方法1）: " + minCost(heights3));
        System.out.println("最小代价（方法2）: " + minCostOptimized(heights3));
        System.out.println("预期结果: 40");
    }
}
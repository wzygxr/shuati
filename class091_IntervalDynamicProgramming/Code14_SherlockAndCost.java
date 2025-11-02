package class076;

/**
 * HackerRank - Sherlock and Cost
 * 题目链接：https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 难度：中等
 * 
 * 题目描述：
 * 给定一个数组B，需要构造一个数组A，使得：
 * 1. 1 <= A[i] <= B[i] 对于所有i
 * 2. 最大化 S = Σ|A[i] - A[i-1]| (i从1到n-1)
 * 
 * 解题思路：
 * 这是一个动态规划问题，需要处理每个位置取1或B[i]的情况。
 * 状态定义：dp[i][0]表示第i个位置取1时的最大和，dp[i][1]表示第i个位置取B[i]时的最大和
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n) 可以优化到O(1)
 * 
 * 工程化考量：
 * 1. 空间优化：使用滚动变量代替数组
 * 2. 边界条件处理：单个元素的情况
 * 3. 优化：只需要前一个状态的信息
 * 
 * 相关题目扩展：
 * 1. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 4. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 5. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 7. LintCode 1639. K 倍重复项删除 - https://www.lintcode.com/problem/1639/
 * 8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 */
public class Code14_SherlockAndCost {

    /**
     * 动态规划解法
     * @param B 约束数组
     * @return 最大和
     */
    public static int cost(int[] B) {
        int n = B.length;
        if (n <= 1) return 0;
        
        // dp[i][0]: 第i个位置取1时的最大和
        // dp[i][1]: 第i个位置取B[i]时的最大和
        int[][] dp = new int[n][2];
        
        for (int i = 1; i < n; i++) {
            // 当前取1，前一个取1
            int diff1 = dp[i-1][0] + Math.abs(1 - 1);
            // 当前取1，前一个取B[i-1]
            int diff2 = dp[i-1][1] + Math.abs(1 - B[i-1]);
            dp[i][0] = Math.max(diff1, diff2);
            
            // 当前取B[i]，前一个取1
            int diff3 = dp[i-1][0] + Math.abs(B[i] - 1);
            // 当前取B[i]，前一个取B[i-1]
            int diff4 = dp[i-1][1] + Math.abs(B[i] - B[i-1]);
            dp[i][1] = Math.max(diff3, diff4);
        }
        
        return Math.max(dp[n-1][0], dp[n-1][1]);
    }
    
    /**
     * 空间优化版本
     */
    public static int costOptimized(int[] B) {
        int n = B.length;
        if (n <= 1) return 0;
        
        int prevLow = 0;  // 前一个位置取1时的最大和
        int prevHigh = 0; // 前一个位置取B[i]时的最大和
        
        for (int i = 1; i < n; i++) {
            int currentLow = Math.max(prevLow, prevHigh + Math.abs(1 - B[i-1]));
            int currentHigh = Math.max(prevLow + Math.abs(B[i] - 1), 
                                     prevHigh + Math.abs(B[i] - B[i-1]));
            
            prevLow = currentLow;
            prevHigh = currentHigh;
        }
        
        return Math.max(prevLow, prevHigh);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] B1 = {1, 2, 3};
        System.out.println("测试用例1: B = [1,2,3]");
        System.out.println("预期结果: 2");
        System.out.println("DP解法: " + cost(B1));
        System.out.println("优化版本: " + costOptimized(B1));
        System.out.println();
        
        // 测试用例2
        int[] B2 = {10, 1, 10, 1, 10};
        System.out.println("测试用例2: B = [10,1,10,1,10]");
        System.out.println("预期结果: 36");
        System.out.println("DP解法: " + cost(B2));
        System.out.println("优化版本: " + costOptimized(B2));
        System.out.println();
        
        // 测试用例3
        int[] B3 = {100, 2, 100, 2, 100};
        System.out.println("测试用例3: B = [100,2,100,2,100]");
        System.out.println("DP解法: " + cost(B3));
        System.out.println("优化版本: " + costOptimized(B3));
    }
    
    /**
     * 复杂度分析：
     * 时间复杂度：O(n)，需要遍历数组一次
     * 空间复杂度：基本版本O(n)，优化版本O(1)
     * 
     * 工程化建议：
     * 1. 对于大规模数据，使用优化版本节省空间
     * 2. 注意整数溢出问题
     * 3. 可以添加输入验证
     */
}
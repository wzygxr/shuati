package class069;

/**
 * 爬楼梯 (Climbing Stairs) - 线性动态规划
 * 
 * 题目描述：
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 
 * 题目来源：LeetCode 70. 爬楼梯
 * 测试链接：https://leetcode.cn/problems/climbing-stairs/
 * 
 * 解题思路：
 * 这是一个经典的线性动态规划问题，类似于斐波那契数列。
 * 设 dp[i] 表示到达第 i 阶楼梯的方法数。
 * 状态转移方程：dp[i] = dp[i-1] + dp[i-2]
 * 边界条件：dp[0] = 1, dp[1] = 1
 * 
 * 算法实现：
 * 1. 动态规划：使用数组存储每一步的结果
 * 2. 空间优化：只保存前两个状态值
 * 3. 记忆化搜索：递归计算，使用记忆化避免重复计算
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n)
 * - 空间优化：O(n)
 * - 记忆化搜索：O(n)
 * 
 * 空间复杂度分析：
 * - 动态规划：O(n)
 * - 空间优化：O(1)
 * - 记忆化搜索：O(n)
 * 
 * 关键技巧：
 * 1. 状态转移：当前状态依赖于前两个状态
 * 2. 边界处理：处理 n=0 和 n=1 的特殊情况
 * 3. 空间优化：使用滚动变量降低空间复杂度
 * 
 * 工程化考量：
 * 1. 输入验证：检查 n 的合法性
 * 2. 边界条件：处理特殊情况
 * 3. 性能优化：空间优化版本最优
 * 4. 可读性：清晰的状态定义和转移方程
 */
public class Code09_ClimbingStairs {
    
    /**
     * 动态规划解法
     * 
     * @param n 需要爬的台阶数
     * @return 到达楼顶的不同方法数
     */
    public static int climbStairs1(int n) {
        if (n <= 1) {
            return 1;
        }
        
        // dp[i] 表示到达第 i 阶楼梯的方法数
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        
        // 状态转移
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param n 需要爬的台阶数
     * @return 到达楼顶的不同方法数
     */
    public static int climbStairs2(int n) {
        if (n <= 1) {
            return 1;
        }
        
        // 只需要保存前两个状态
        int prev2 = 1;  // dp[i-2]
        int prev1 = 1;  // dp[i-1]
        int current = 0; // dp[i]
        
        // 状态转移
        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return current;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param n 需要爬的台阶数
     * @return 到达楼顶的不同方法数
     */
    public static int climbStairs3(int n) {
        // 记忆化数组
        int[] memo = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            memo[i] = -1;
        }
        
        return dfs(n, memo);
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param n 当前需要爬的台阶数
     * @param memo 记忆化数组
     * @return 到达楼顶的不同方法数
     */
    private static int dfs(int n, int[] memo) {
        // 边界条件
        if (n <= 1) {
            return 1;
        }
        
        // 检查是否已经计算过
        if (memo[n] != -1) {
            return memo[n];
        }
        
        // 状态转移
        int ans = dfs(n - 1, memo) + dfs(n - 2, memo);
        
        // 记忆化存储
        memo[n] = ans;
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 2;
        System.out.println("测试用例1:");
        System.out.println("台阶数: " + n1);
        System.out.println("方法1结果: " + climbStairs1(n1));
        System.out.println("方法2结果: " + climbStairs2(n1));
        System.out.println("方法3结果: " + climbStairs3(n1));
        System.out.println();
        
        // 测试用例2
        int n2 = 3;
        System.out.println("测试用例2:");
        System.out.println("台阶数: " + n2);
        System.out.println("方法1结果: " + climbStairs1(n2));
        System.out.println("方法2结果: " + climbStairs2(n2));
        System.out.println("方法3结果: " + climbStairs3(n2));
        System.out.println();
        
        // 测试用例3
        int n3 = 5;
        System.out.println("测试用例3:");
        System.out.println("台阶数: " + n3);
        System.out.println("方法1结果: " + climbStairs1(n3));
        System.out.println("方法2结果: " + climbStairs2(n3));
        System.out.println("方法3结果: " + climbStairs3(n3));
    }
}
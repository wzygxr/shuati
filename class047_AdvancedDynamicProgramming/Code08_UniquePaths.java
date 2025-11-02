package class069;

/**
 * 不同路径 (Unique Paths) - 网格路径计数
 * 
 * 题目描述：
 * 一个机器人位于一个 m x n 网格的左上角。
 * 机器人每次只能向下或者向右移动一步。
 * 机器人试图达到网格的右下角。
 * 问总共有多少条不同的路径？
 * 
 * 题目来源：LeetCode 62. 不同路径
 * 测试链接：https://leetcode.cn/problems/unique-paths/
 * 
 * 解题思路：
 * 这是一个经典的网格路径计数问题，机器人只能向右或向下移动。
 * 可以使用动态规划、组合数学或记忆化搜索等多种方法解决。
 * 
 * 算法实现：
 * 1. 基础动态规划：使用二维DP表存储路径数
 * 2. 空间优化：使用一维数组滚动更新
 * 3. 组合数学：使用组合数公式直接计算
 * 4. 记忆化搜索：递归计算路径数，使用记忆化避免重复计算
 * 
 * 时间复杂度分析：
 * - 基础动态规划：O(m*n)，需要填充二维网格
 * - 空间优化：O(m*n)，时间复杂度相同但空间更优
 * - 组合数学：O(min(m,n))，计算组合数
 * - 记忆化搜索：O(m*n)，每个网格计算一次
 * 
 * 空间复杂度分析：
 * - 基础动态规划：O(m*n)，二维DP表
 * - 空间优化：O(min(m,n))，一维数组
 * - 组合数学：O(1)，常数空间
 * - 记忆化搜索：O(m*n)，记忆化数组
 * 
 * 关键技巧：
 * 1. 状态转移：只能从上方或左方到达当前网格
 * 2. 边界处理：第一行和第一列的路径数都为1
 * 3. 组合公式：C(m+n-2, m-1) 或 C(m+n-2, n-1)
 * 
 * 工程化考量：
 * 1. 整数溢出：使用long类型防止组合数计算溢出
 * 2. 边界条件：处理m=1或n=1的特殊情况
 * 3. 性能优化：组合数学方法最优
 * 4. 代码可读性：多种解法对比展示
 */
public class Code08_UniquePaths {
    
    /**
     * 动态规划解法
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 不同路径数
     */
    public static int uniquePaths1(int m, int n) {
        // dp[i][j] 表示从起点到位置(i,j)的不同路径数
        int[][] dp = new int[m][n];
        
        // 初始化边界条件
        // 第一行只能从左方到达
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        // 第一列只能从上方到达
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        
        // 状态转移
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        
        return dp[m - 1][n - 1];
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 不同路径数
     */
    public static int uniquePaths2(int m, int n) {
        // 确保使用较小的维度作为数组长度，进一步优化空间
        if (m < n) {
            int temp = m;
            m = n;
            n = temp;
        }
        
        // 只需要保存一行的状态
        int[] dp = new int[n];
        
        // 初始化第一行
        for (int j = 0; j < n; j++) {
            dp[j] = 1;
        }
        
        // 状态转移
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] = dp[j] + dp[j - 1];
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 数学组合解法
     * 
     * 总共需要走 (m-1) + (n-1) = m+n-2 步
     * 其中需要向下走 m-1 步，向右走 n-1 步
     * 所以答案是 C(m+n-2, m-1) 或 C(m+n-2, n-1)
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 不同路径数
     */
    public static int uniquePaths3(int m, int n) {
        // 计算 C(m+n-2, min(m-1, n-1))
        int totalSteps = m + n - 2;
        int k = Math.min(m - 1, n - 1);
        
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (totalSteps - k + i) / i;
        }
        
        return (int) result;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 不同路径数
     */
    public static int uniquePaths4(int m, int n) {
        // 记忆化数组
        int[][] memo = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                memo[i][j] = -1;
            }
        }
        
        return dfs(m - 1, n - 1, memo);
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param i 当前行位置
     * @param j 当前列位置
     * @param memo 记忆化数组
     * @return 从(0,0)到(i,j)的不同路径数
     */
    private static int dfs(int i, int j, int[][] memo) {
        // 边界条件
        if (i == 0 || j == 0) {
            return 1;
        }
        
        // 检查是否已经计算过
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        // 从上方和左方到达
        int ans = dfs(i - 1, j, memo) + dfs(i, j - 1, memo);
        
        // 记忆化存储
        memo[i][j] = ans;
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int m1 = 3, n1 = 7;
        System.out.println("测试用例1:");
        System.out.println("网格大小: " + m1 + " x " + n1);
        System.out.println("方法1结果: " + uniquePaths1(m1, n1));
        System.out.println("方法2结果: " + uniquePaths2(m1, n1));
        System.out.println("方法3结果: " + uniquePaths3(m1, n1));
        System.out.println("方法4结果: " + uniquePaths4(m1, n1));
        System.out.println();
        
        // 测试用例2
        int m2 = 3, n2 = 2;
        System.out.println("测试用例2:");
        System.out.println("网格大小: " + m2 + " x " + n2);
        System.out.println("方法1结果: " + uniquePaths1(m2, n2));
        System.out.println("方法2结果: " + uniquePaths2(m2, n2));
        System.out.println("方法3结果: " + uniquePaths3(m2, n2));
        System.out.println("方法4结果: " + uniquePaths4(m2, n2));
        System.out.println();
        
        // 测试用例3
        int m3 = 7, n3 = 3;
        System.out.println("测试用例3:");
        System.out.println("网格大小: " + m3 + " x " + n3);
        System.out.println("方法1结果: " + uniquePaths1(m3, n3));
        System.out.println("方法2结果: " + uniquePaths2(m3, n3));
        System.out.println("方法3结果: " + uniquePaths3(m3, n3));
        System.out.println("方法4结果: " + uniquePaths4(m3, n3));
    }
}
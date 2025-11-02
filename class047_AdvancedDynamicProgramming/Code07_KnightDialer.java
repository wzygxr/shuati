package class069;

/**
 * 骑士拨号器 (Knight Dialer) - 计数动态规划
 * 
 * 题目描述：
 * 象棋骑士有一个独特的移动方式，它可以垂直移动两个方格，水平移动一个方格，
 * 或者水平移动两个方格，垂直移动一个方格(两者都形成一个 L 的形状)。
 * 我们有一个象棋骑士和一个电话垫，骑士只能站在一个数字单元格上。
 * 给定一个整数 n，返回我们可以拨多少个长度为 n 的不同电话号码。
 * 
 * 题目来源：LeetCode 935. 骑士拨号器
 * 测试链接：https://leetcode.cn/problems/knight-dialer/
 * 
 * 解题思路：
 * 这是一个计数动态规划问题，需要计算骑士在数字键盘上移动n步能形成的不同号码数量。
 * 骑士的移动遵循特定的规则，每个数字只能移动到特定的相邻数字。
 * 
 * 算法实现：
 * 1. 基础动态规划：使用二维DP表存储状态
 * 2. 空间优化：使用两个一维数组滚动更新
 * 3. 记忆化搜索：递归计算方案数，使用记忆化避免重复计算
 * 
 * 时间复杂度分析：
 * - 基础动态规划：O(N)，需要处理n步移动
 * - 空间优化：O(N)，时间复杂度相同但空间更优
 * - 记忆化搜索：O(N)，每个状态计算一次
 * 
 * 空间复杂度分析：
 * - 基础动态规划：O(N)，存储所有步数的状态
 * - 空间优化：O(1)，常数空间（10个数字）
 * - 记忆化搜索：O(N)，记忆化数组
 * 
 * 关键技巧：
 * 1. 移动规则定义：正确定义骑士从每个数字可以移动到的数字
 * 2. 模运算：结果可能很大，需要取模
 * 3. 空间优化：只需要保存当前步和前一步的状态
 * 
 * 工程化考量：
 * 1. 大数处理：使用long类型和取模运算
 * 2. 边界条件：处理n=0和n=1的特殊情况
 * 3. 性能优化：空间优化降低内存使用
 * 4. 可维护性：清晰的移动规则定义
 */
public class Code07_KnightDialer {
    private static final int MOD = 1000000007;
    
    // 骑士在每个数字上可以跳到的下一个数字
    private static final int[][] moves = {
        {4, 6},        // 0
        {6, 8},        // 1
        {7, 9},        // 2
        {4, 8},        // 3
        {0, 3, 9},     // 4
        {},            // 5 (无法移动)
        {0, 1, 7},     // 6
        {2, 6},        // 7
        {1, 3},        // 8
        {2, 4}         // 9
    };
    
    /**
     * 动态规划解法
     * 
     * @param n 电话号码长度
     * @return 不同电话号码的数量
     */
    public static int knightDialer1(int n) {
        if (n == 1) {
            return 10;
        }
        
        // dp[i] 表示当前在数字i上的方案数
        long[] dp = new long[10];
        // 初始状态：第一步可以站在任意数字上
        for (int i = 0; i < 10; i++) {
            dp[i] = 1;
        }
        
        // 状态转移
        for (int step = 2; step <= n; step++) {
            long[] next = new long[10];
            for (int i = 0; i < 10; i++) {
                for (int nextNum : moves[i]) {
                    next[nextNum] = (next[nextNum] + dp[i]) % MOD;
                }
            }
            dp = next;
        }
        
        // 计算总方案数
        long result = 0;
        for (int i = 0; i < 10; i++) {
            result = (result + dp[i]) % MOD;
        }
        
        return (int) result;
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param n 电话号码长度
     * @return 不同电话号码的数量
     */
    public static int knightDialer2(int n) {
        if (n == 1) {
            return 10;
        }
        
        // dp[i] 表示当前在数字i上的方案数
        long[] dp = new long[10];
        long[] next = new long[10];
        // 初始状态：第一步可以站在任意数字上
        for (int i = 0; i < 10; i++) {
            dp[i] = 1;
        }
        
        // 状态转移
        for (int step = 2; step <= n; step++) {
            // 初始化next数组
            for (int i = 0; i < 10; i++) {
                next[i] = 0;
            }
            
            for (int i = 0; i < 10; i++) {
                for (int nextNum : moves[i]) {
                    next[nextNum] = (next[nextNum] + dp[i]) % MOD;
                }
            }
            
            // 交换dp和next
            long[] temp = dp;
            dp = next;
            next = temp;
        }
        
        // 计算总方案数
        long result = 0;
        for (int i = 0; i < 10; i++) {
            result = (result + dp[i]) % MOD;
        }
        
        return (int) result;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param n 电话号码长度
     * @return 不同电话号码的数量
     */
    public static int knightDialer3(int n) {
        if (n == 1) {
            return 10;
        }
        
        // memo[i][j] 表示在数字i上还能跳j步的方案数
        long[][] memo = new long[10][n + 1];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j <= n; j++) {
                memo[i][j] = -1;
            }
        }
        
        long result = 0;
        // 从每个数字开始
        for (int i = 0; i < 10; i++) {
            result = (result + dfs(i, n - 1, memo)) % MOD;
        }
        
        return (int) result;
    }
    
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param num 当前所在的数字
     * @param steps 剩余步数
     * @param memo 记忆化数组
     * @return 方案数
     */
    private static long dfs(int num, int steps, long[][] memo) {
        // 边界条件
        if (steps == 0) {
            return 1;
        }
        
        // 检查是否已经计算过
        if (memo[num][steps] != -1) {
            return memo[num][steps];
        }
        
        long ans = 0;
        // 尝试跳到下一个数字
        for (int nextNum : moves[num]) {
            ans = (ans + dfs(nextNum, steps - 1, memo)) % MOD;
        }
        
        // 记忆化存储
        memo[num][steps] = ans;
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 1;
        System.out.println("测试用例1:");
        System.out.println("电话号码长度: " + n1);
        System.out.println("方法1结果: " + knightDialer1(n1));
        System.out.println("方法2结果: " + knightDialer2(n1));
        System.out.println("方法3结果: " + knightDialer3(n1));
        System.out.println();
        
        // 测试用例2
        int n2 = 2;
        System.out.println("测试用例2:");
        System.out.println("电话号码长度: " + n2);
        System.out.println("方法1结果: " + knightDialer1(n2));
        System.out.println("方法2结果: " + knightDialer2(n2));
        System.out.println("方法3结果: " + knightDialer3(n2));
        System.out.println();
        
        // 测试用例3
        int n3 = 3;
        System.out.println("测试用例3:");
        System.out.println("电话号码长度: " + n3);
        System.out.println("方法1结果: " + knightDialer1(n3));
        System.out.println("方法2结果: " + knightDialer2(n3));
        System.out.println("方法3结果: " + knightDialer3(n3));
    }
}
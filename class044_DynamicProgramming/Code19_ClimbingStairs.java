// 爬楼梯 (Climbing Stairs)
// 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
// 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
// 测试链接 : https://leetcode.cn/problems/climbing-stairs/



import java.util.Arrays;

/**
 * 爬楼梯问题 - 斐波那契数列的经典应用
 * 时间复杂度分析：
 * - 暴力递归：O(2^n) 指数级，存在大量重复计算
 * - 记忆化搜索：O(n) 每个状态只计算一次
 * - 动态规划：O(n) 线性时间复杂度
 * - 矩阵快速幂：O(log n) 最优解（未实现）
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(n) 递归调用栈深度
 * - 记忆化搜索：O(n) 递归栈 + 记忆化数组
 * - 动态规划：O(n) dp数组存储所有状态
 * - 空间优化：O(1) 只保存必要的前两个状态
 * 
 * 工程化考量：
 * 1. 异常处理：处理n为负数或0的情况
 * 2. 边界测试：n=0,1,2等小数值
 * 3. 性能优化：选择空间优化版本应对大规模数据
 * 4. 可读性：清晰的变量命名和注释
 */
public class Code19_ClimbingStairs {

    // 方法1：暴力递归解法
    // 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(n) - 递归调用栈的深度
    // 问题：存在大量重复计算，n较大时栈溢出
    public static int climbStairs1(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        return climbStairs1(n - 1) + climbStairs1(n - 2);
    }

    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(n) - 每个状态只计算一次
    // 空间复杂度：O(n) - dp数组和递归调用栈
    // 优化：通过缓存避免重复计算，但仍有递归开销
    public static int climbStairs2(int n) {
        if (n <= 0) return 0;
        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return dfs(n, memo);
    }
    
    private static int dfs(int n, int[] memo) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        if (memo[n] != -1) return memo[n];
        
        memo[n] = dfs(n - 1, memo) + dfs(n - 2, memo);
        return memo[n];
    }

    // 方法3：动态规划（自底向上）
    // 时间复杂度：O(n) - 从底向上计算每个状态
    // 空间复杂度：O(n) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    public static int climbStairs3(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(n) - 仍然需要计算所有状态
    // 空间复杂度：O(1) - 只保存必要的前两个状态值
    // 优化：大幅减少空间使用，工程首选
    public static int climbStairs4(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        int prev1 = 1; // dp[i-2]
        int prev2 = 2; // dp[i-1]
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }

    // 方法5：矩阵快速幂（最优解）
    // 时间复杂度：O(log n) - 通过矩阵快速幂加速
    // 空间复杂度：O(1) - 常数空间
    // 核心思路：将递推关系转化为矩阵乘法，使用快速幂算法
    public static int climbStairs5(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        // 递推关系矩阵：[[1,1],[1,0]]
        long[][] base = {{1, 1}, {1, 0}};
        long[][] result = matrixPower(base, n - 2);
        
        // 结果矩阵与初始状态相乘
        return (int)(result[0][0] * 2 + result[0][1] * 1);
    }
    
    // 矩阵快速幂算法
    private static long[][] matrixPower(long[][] base, int power) {
        long[][] result = {{1, 0}, {0, 1}}; // 单位矩阵
        
        while (power > 0) {
            if ((power & 1) == 1) {
                result = matrixMultiply(result, base);
            }
            base = matrixMultiply(base, base);
            power >>= 1;
        }
        
        return result;
    }
    
    // 2x2矩阵乘法
    private static long[][] matrixMultiply(long[][] a, long[][] b) {
        long[][] result = new long[2][2];
        result[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        result[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        result[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        result[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
        return result;
    }

    // 全面的测试用例
    public static void main(String[] args) {
        System.out.println("=== 爬楼梯问题测试 ===");
        
        // 边界测试
        testCase(0, 0, "n=0");
        testCase(1, 1, "n=1");
        testCase(2, 2, "n=2");
        
        // 常规测试
        testCase(3, 3, "n=3");
        testCase(4, 5, "n=4");
        testCase(5, 8, "n=5");
        testCase(10, 89, "n=10");
        
        // 性能对比测试（只测试高效方法）
        System.out.println("\n=== 性能对比测试 ===");
        int n = 40;
        
        long start = System.currentTimeMillis();
        int result3 = climbStairs3(n);
        long end = System.currentTimeMillis();
        System.out.println("动态规划方法: " + result3 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result4 = climbStairs4(n);
        end = System.currentTimeMillis();
        System.out.println("空间优化方法: " + result4 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result5 = climbStairs5(n);
        end = System.currentTimeMillis();
        System.out.println("矩阵快速幂方法: " + result5 + ", 耗时: " + (end - start) + "ms");
        
        // 错误处理测试
        System.out.println("\n=== 错误处理测试 ===");
        try {
            int result = climbStairs4(-1);
            System.out.println("n=-1 结果: " + result);
        } catch (Exception e) {
            System.out.println("n=-1 异常: " + e.getMessage());
        }
    }
    
    private static void testCase(int n, int expected, String description) {
        int result1 = climbStairs1(n);
        int result2 = climbStairs2(n);
        int result3 = climbStairs3(n);
        int result4 = climbStairs4(n);
        int result5 = climbStairs5(n);
        
        boolean allCorrect = (result1 == expected && result2 == expected && 
                            result3 == expected && result4 == expected && result5 == expected);
        
        System.out.println(description + ": " + (allCorrect ? "✓" : "✗"));
        if (!allCorrect) {
            System.out.println("  方法1: " + result1 + " | 方法2: " + result2 + 
                             " | 方法3: " + result3 + " | 方法4: " + result4 + 
                             " | 方法5: " + result5 + " | 预期: " + expected);
        }
    }
    
    /**
     * 算法总结与工程化思考：
     * 
     * 1. 问题本质：斐波那契数列的变种，f(n) = f(n-1) + f(n-2)
     * 
     * 2. 时间复杂度对比：
     *    - 暴力递归：O(2^n) - 不可接受
     *    - 记忆化搜索：O(n) - 可接受
     *    - 动态规划：O(n) - 推荐
     *    - 矩阵快速幂：O(log n) - 最优
     * 
     * 3. 空间复杂度对比：
     *    - 暴力递归：O(n) - 栈深度
     *    - 记忆化搜索：O(n) - 递归栈+缓存
     *    - 动态规划：O(n) - 数组存储
     *    - 空间优化：O(1) - 工程首选
     * 
     * 4. 工程选择依据：
     *    - 小规模数据：任意方法都可
     *    - 大规模数据：空间优化版本或矩阵快速幂
     *    - 内存敏感：空间优化版本
     *    - 性能极致：矩阵快速幂
     * 
     * 5. 调试技巧：
     *    - 打印中间状态验证递推关系
     *    - 边界测试确保正确性
     *    - 性能测试选择最优算法
     */
}
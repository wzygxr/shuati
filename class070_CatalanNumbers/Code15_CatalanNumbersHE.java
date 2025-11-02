package class147;

// Catalan Numbers
// 计算第n项卡特兰数
// 测试链接：https://www.hackerearth.com/problem/algorithm/catalan-numbers-1/

public class Code15_CatalanNumbersHE {
    
    /**
     * 计算第n项卡特兰数
     * 
     * 题目解析：
     * 1. 卡特兰数是组合数学中一个常出现在各种计数问题中的数列
     * 2. 前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...
     * 3. 有多种计算方法，包括递推公式和组合公式
     * 
     * 时间复杂度分析：
     * 1. 使用递推公式：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
     * 2. 双重循环，外层循环n次，内层循环最多n次
     * 3. 总时间复杂度：O(n²)
     * 
     * 空间复杂度分析：
     * 1. 使用了一个长度为n+1的数组存储中间结果
     * 2. 空间复杂度：O(n)
     * 
     * @param n 第n项
     * @return 第n项卡特兰数
     */
    public static long computeCatalan(int n) {
        // 边界条件处理
        if (n <= 1) {
            return 1;
        }
        
        // dp[i] 表示第i项卡特兰数
        long[] dp = new long[n + 1];
        
        // 初始化基本情况
        dp[0] = 1; // 第0项卡特兰数为1
        dp[1] = 1; // 第1项卡特兰数为1
        
        // 动态规划填表
        // 使用递推公式：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
        for (int i = 2; i <= n; i++) {
            // 对于第i项卡特兰数，累加所有可能的乘积
            for (int j = 0; j < i; j++) {
                // dp[j] 是第j项卡特兰数
                // dp[i-1-j] 是第i-1-j项卡特兰数
                // 两者相乘累加到dp[i]中
                dp[i] += dp[j] * dp[i - 1 - j];
            }
        }
        
        return dp[n];
    }
    
    /**
     * 使用卡特兰数的另一种递推公式计算
     * C(0) = 1
     * C(n) = C(n-1) * (4*n-2) / (n+1)
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param n 第n项
     * @return 第n项卡特兰数
     */
    public static long computeCatalanOptimized(int n) {
        if (n <= 1) {
            return 1;
        }
        
        long catalan = 1;
        for (int i = 2; i <= n; i++) {
            catalan = catalan * (4 * i - 2) / (i + 1);
        }
        return catalan;
    }
    
    /**
     * 使用组合公式计算卡特兰数
     * C(n) = C(2n, n) / (n+1)
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param n 第n项
     * @return 第n项卡特兰数
     */
    public static long computeCatalanCombination(int n) {
        if (n <= 1) {
            return 1;
        }
        
        // 计算组合数C(2n, n)
        long result = 1;
        for (int i = 0; i < n; i++) {
            result = result * (2 * n - i) / (i + 1);
        }
        
        // 除以(n+1)
        return result / (n + 1);
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试不同n值下的结果
        System.out.println("卡特兰数计算测试：");
        for (int i = 0; i <= 10; i++) {
            System.out.println("C(" + i + ") = " + computeCatalan(i) + 
                             ", optimized = " + computeCatalanOptimized(i) +
                             ", combination = " + computeCatalanCombination(i));
        }
    }
}
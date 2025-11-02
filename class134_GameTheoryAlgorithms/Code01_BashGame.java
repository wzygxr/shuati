package class095;

/**
 * 巴什博弈（Bash Game）算法实现
 * 问题描述：有n个石子，两个人轮流取石子，每次最多取m个，最少取1个，取走最后一个石子的人获胜
 * 核心定理：当n % (m + 1) != 0时，先手必胜；否则后手必胜
 * 
 * 时间复杂度分析：
 * - 动态规划解法：O(n * m)
 * - 数学解法：O(1)
 * 
 * 空间复杂度分析：
 * - 动态规划解法：O(n * m) 
 * - 数学解法：O(1)
 * 
 * 应用场景：各种取石子游戏、资源分配问题、游戏策略设计
 * 
 * 相关题目链接：
 * 1. LeetCode 292. Nim Game: https://leetcode.com/problems/nim-game/
 * 2. HDU 1846. Brave Game: http://acm.hdu.edu.cn/showproblem.php?pid=1846
 * 3. POJ 2313. Bash Game: http://poj.org/problem?id=2313
 * 4. 牛客网 NC13685. 取石子游戏: https://www.nowcoder.com/practice/f6153503169545229c77481040056a63
 * 5. 洛谷 P4018: https://www.luogu.com.cn/problem/P4018
 */

import java.util.*;

public class Code01_BashGame {
    
    // 最大石子数限制，防止内存溢出
    public static final int MAX_N = 1000;
    public static final int MAX_M = 1000;
    
    /**
     * 动态规划解法 - 用于验证数学定理的正确性
     * 时间复杂度：O(n * m)
     * 空间复杂度：O(n * m)
     * 
     * @param n 石子总数
     * @param m 每次最多取的石子数
     * @return "先手"或"后手"表示获胜方
     * 
     * 算法思路：
     * 1. dp[i]表示有i个石子时当前玩家的胜负情况
     * 2. true表示当前玩家必胜，false表示当前玩家必败
     * 3. 对于每个状态i，尝试所有可能的取法(1到min(m,i))
     * 4. 如果存在一种取法使得对手处于必败状态，则当前玩家必胜
     */
    public static String bashGameDP(int n, int m) {
        // 输入验证
        if (n < 0 || m <= 0) {
            throw new IllegalArgumentException("石子数n不能为负，每次取的石子数m必须大于0");
        }
        
        // 边界情况处理
        if (n == 0) {
            return "后手"; // 没有石子，后手获胜（因为先手无法行动）
        }
        
        // 创建DP表，dp[i]表示有i个石子时当前玩家的胜负情况
        // true表示当前玩家必胜，false表示当前玩家必败
        boolean[] dp = new boolean[n + 1];
        
        // 基础情况：只有0个石子时，当前玩家必败
        dp[0] = false;
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            dp[i] = false; // 初始化为必败
            
            // 尝试所有可能的取法（1到m个石子）
            for (int pick = 1; pick <= m && pick <= i; pick++) {
                // 如果取走pick个石子后，对手处于必败状态，则当前玩家必胜
                if (!dp[i - pick]) {
                    dp[i] = true;
                    break; // 找到一个必胜策略即可
                }
            }
        }
        
        return dp[n] ? "先手" : "后手";
    }
    
    /**
     * 数学解法 - 基于巴什博弈定理的最优解
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * 定理证明：
     * 1. 当n = k*(m+1)时，无论先手取多少石子（1到m个），后手总可以取相应的石子使得剩余石子数仍然是(m+1)的倍数
     * 2. 当n ≠ k*(m+1)时，先手可以一次取走n % (m+1)个石子，使得剩余石子数是(m+1)的倍数，从而将必败局面留给对手
     * 
     * @param n 石子总数
     * @param m 每次最多取的石子数
     * @return "先手"或"后手"表示获胜方
     */
    public static String bashGameMath(int n, int m) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("石子数n不能为负");
        }
        if (m <= 0) {
            throw new IllegalArgumentException("每次取的石子数m必须大于0");
        }
        
        // 边界情况处理
        if (n == 0) {
            return "后手";
        }
        
        // 应用巴什博弈定理
        return (n % (m + 1) != 0) ? "先手" : "后手";
    }
    
    /**
     * 验证两种解法的一致性
     * 通过随机测试验证动态规划和数学解法是否产生相同结果
     */
    public static void validateSolutions() {
        System.out.println("=== 开始验证巴什博弈算法正确性 ===");
        
        int testCount = 1000;
        int maxN = 100;
        int maxM = 10;
        Random random = new Random();
        
        for (int i = 0; i < testCount; i++) {
            int n = random.nextInt(maxN);
            int m = random.nextInt(maxM) + 1; // m至少为1
            
            String dpResult = bashGameDP(n, m);
            String mathResult = bashGameMath(n, m);
            
            if (!dpResult.equals(mathResult)) {
                System.out.printf("❌ 验证失败：n=%d, m=%d, DP=%s, Math=%s%n", 
                    n, m, dpResult, mathResult);
                return;
            }
        }
        
        System.out.println("✅ 所有验证测试通过！两种解法结果一致");
    }
    
    /**
     * 性能测试：比较动态规划和数学解法的效率
     */
    public static void performanceTest() {
        System.out.println("=== 开始性能测试 ===");
        
        int testCount = 10000;
        
        // 测试数学解法
        long startTime = System.nanoTime();
        for (int i = 0; i < testCount; i++) {
            bashGameMath(i % 1000 + 1, (i % 10) + 1);
        }
        long mathTime = System.nanoTime() - startTime;
        
        // 测试动态规划解法（小规模）
        startTime = System.nanoTime();
        for (int i = 0; i < Math.min(testCount, 100); i++) {
            bashGameDP(i % 100 + 1, (i % 10) + 1);
        }
        long dpTime = System.nanoTime() - startTime;
        
        System.out.printf("数学解法：%d次计算，耗时 %.3f 微秒/次%n", 
            testCount, mathTime / 1000.0 / testCount);
        System.out.printf("动态规划：%d次计算，耗时 %.3f 微秒/次%n", 
            Math.min(testCount, 100), dpTime / 1000.0 / Math.min(testCount, 100));
        System.out.printf("数学解法比动态规划快 %.1f 倍%n", 
            (double)dpTime / mathTime * testCount / Math.min(testCount, 100));
    }
    
    /**
     * 实际应用示例演示
     */
    public static void demo() {
        System.out.println("=== 巴什博弈实际应用示例 ===");
        
        // 示例1：经典巴什博弈
        int n1 = 10, m1 = 3;
        System.out.printf("石子数=%d, 每次最多取=%d → %s%n", 
            n1, m1, bashGameMath(n1, m1));
        
        // 示例2：先手必胜情况
        int n2 = 7, m2 = 3;
        System.out.printf("石子数=%d, 每次最多取=%d → %s%n", 
            n2, m2, bashGameMath(n2, m2));
        
        // 示例3：后手必胜情况  
        int n3 = 12, m3 = 3;
        System.out.printf("石子数=%d, 每次最多取=%d → %s%n", 
            n3, m3, bashGameMath(n3, m3));
        
        // 示例4：边界情况测试
        int[] testCases = {
            0, 5,  // n=0
            1, 1,  // n=1, m=1
            6, 5,  // n=6, m=5
            100, 7 // 大规模测试
        };
        
        for (int i = 0; i < testCases.length; i += 2) {
            int n = testCases[i];
            int m = testCases[i + 1];
            System.out.printf("测试用例：n=%d, m=%d → %s%n", 
                n, m, bashGameMath(n, m));
        }
    }
    
    /**
     * 单元测试框架
     */
    public static void runUnitTests() {
        System.out.println("=== 开始单元测试 ===");
        
        // 测试用例：(n, m, 期望结果)
        int[][] testCases = {
            {0, 3, 0},  // 后手胜
            {1, 3, 1},  // 先手胜
            {4, 3, 0},  // 后手胜 (4 % 4 = 0)
            {5, 3, 1},  // 先手胜 (5 % 4 = 1)
            {10, 3, 1}, // 先手胜 (10 % 4 = 2 ≠ 0)
            {7, 3, 1}   // 先手胜 (7 % 4 = 3)
        };
        
        boolean allPassed = true;
        for (int[] testCase : testCases) {
            int n = testCase[0];
            int m = testCase[1];
            int expected = testCase[2];
            
            String result = bashGameMath(n, m);
            boolean passed = (expected == 1 && result.equals("先手")) || 
                            (expected == 0 && result.equals("后手"));
            
            if (!passed) {
                System.out.printf("❌ 测试失败：n=%d, m=%d, 期望=%s, 实际=%s%n", 
                    n, m, expected == 1 ? "先手" : "后手", result);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("✅ 所有单元测试通过！");
        }
    }
    
    /**
     * 异常场景测试
     */
    public static void testEdgeCases() {
        System.out.println("=== 异常场景测试 ===");
        
        try {
            bashGameMath(-1, 3);
            System.out.println("❌ 负数石子数测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ 负数石子数异常处理正确");
        }
        
        try {
            bashGameMath(10, 0);
            System.out.println("❌ 零取石子数测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ 零取石子数异常处理正确");
        }
        
        try {
            bashGameMath(10, -1);
            System.out.println("❌ 负取石子数测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ 负取石子数异常处理正确");
        }
    }
    
    /**
     * 主函数 - 程序入口
     */
    public static void main(String[] args) {
        if (args.length > 0 && "demo".equals(args[0])) {
            demo();
            return;
        }
        
        if (args.length > 0 && "test".equals(args[0])) {
            runUnitTests();
            testEdgeCases();
            validateSolutions();
            performanceTest();
            return;
        }
        
        // 默认执行完整测试套件
        System.out.println("巴什博弈算法实现 - 完整测试套件");
        System.out.println("=============================");
        
        runUnitTests();
        System.out.println();
        
        testEdgeCases();
        System.out.println();
        
        validateSolutions();
        System.out.println();
        
        performanceTest();
        System.out.println();
        
        demo();
        System.out.println();
        
        // 显示各大平台相关题目
        showRelatedProblems();
    }
    
    /**
     * 显示各大算法平台的巴什博弈相关题目
     */
    public static void showRelatedProblems() {
        System.out.println("=== 各大平台巴什博弈相关题目 ===");
        System.out.println("1. LeetCode 292. Nim Game: https://leetcode.com/problems/nim-game/");
        System.out.println("2. 洛谷 P4018: https://www.luogu.com.cn/problem/P4018");
        System.out.println("3. POJ 2313: http://poj.org/problem?id=2313");
        System.out.println("4. 牛客网 NC13685: https://www.nowcoder.com/practice/f6153503169545229c77481040056a63");
        System.out.println("5. HackerRank Game of Stones: https://www.hackerrank.com/challenges/game-of-stones");
        System.out.println("6. CodeChef STONEGAM: https://www.codechef.com/problems/STONEGAM");
        System.out.println("7. Project Euler Problem 301: https://projecteuler.net/problem=301");
        System.out.println("8. HDU 1846: http://acm.hdu.edu.cn/showproblem.php?pid=1846");
    }
}
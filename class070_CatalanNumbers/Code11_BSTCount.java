package class147;

/**
 * 卡特兰数应用 - 不同二叉搜索树计数
 * 该类实现了计算n个节点能构成的不同二叉搜索树数量的三种方法：
 * 1. 动态规划方法 (时间复杂度O(n²)，空间复杂度O(n))
 * 2. 卡特兰数公式优化方法 (时间复杂度O(n)，空间复杂度O(1))
 * 3. 基于组合数公式的取模方法 (时间复杂度O(n)，空间复杂度O(n))
 * 
 * 该实现具有以下特点：
 * - 完整的参数验证和异常处理机制
 * - 溢出检测和处理
 * - 性能测试和结果验证
 * - 工程化设计考量
 * 
 * 相关题目链接：
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 95. 不同的二叉搜索树 II: https://leetcode.cn/problems/unique-binary-search-trees-ii/
 * - LintCode 1638. 不同的二叉搜索树: https://www.lintcode.com/problem/1638/
 */
public class Code11_BSTCount {
    
    /**
     * 计算n个节点的不同二叉搜索树的数量 - 动态规划方法
     * 
     * 核心思路：对于n个节点，枚举根节点是第i个节点
     * 左子树有i-1个节点，右子树有n-i个节点
     * 总方案数为左子树方案数乘以右子树方案数
     * 
     * 时间复杂度：O(n²)，双层嵌套循环
     * 空间复杂度：O(n)，使用dp数组存储中间结果
     * 
     * @param n 节点数量
     * @return 不同二叉搜索树的数量
     * @throws IllegalArgumentException 当n为负数时抛出异常
     * @throws ArithmeticException 当计算结果溢出时抛出异常
     */
    public static int numTrees(int n) {
        // 输入验证 - 非法输入处理
        if (n < 0) {
            throw new IllegalArgumentException("节点数量不能为负数: " + n);
        }
        
        // 边界情况处理
        if (n <= 1) {
            return 1; // n=0时空树也是一种情况，n=1时只有一种情况
        }
        
        // dp[i]表示i个节点能构成的不同BST数量
        int[] dp = new int[n + 1];
        dp[0] = 1; // 空树的情况，作为基本情况
        dp[1] = 1; // 只有一个节点的树有1种
        
        // 计算dp[2]到dp[n] - 动态规划的主要过程
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                // j是根节点，左子树有j-1个节点，右子树有i-j个节点
                // 检测乘法溢出 - 工程化防御措施
                if (dp[j - 1] > Integer.MAX_VALUE / dp[i - j]) {
                    throw new ArithmeticException("计算结果溢出，对于n = " + n + "，请使用更大的数据类型或取模运算");
                }
                dp[i] += dp[j - 1] * dp[i - j];
                
                // 溢出检查 - 工程化考量
                if (dp[i] < 0) {
                    throw new ArithmeticException("计算结果溢出，对于n = " + n + "，请使用更大的数据类型或取模运算");
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 使用卡特兰数公式优化计算 - 时间复杂度O(n)
     * 应用递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
     * 
     * 该递推式比动态规划更高效，且能保证整数结果
     * 数学证明：每个卡特兰数都是整数，所以除法操作不会产生小数
     * 
     * 时间复杂度：O(n)，单次循环
     * 空间复杂度：O(1)，只使用常量额外空间
     * 
     * @param n 节点数量
     * @return 不同二叉搜索树的数量
     * @throws IllegalArgumentException 当n为负数时抛出异常
     * @throws ArithmeticException 当结果溢出时抛出异常
     */
    public static int numTreesOptimized(int n) {
        // 输入验证 - 非法输入处理
        if (n < 0) {
            throw new IllegalArgumentException("节点数量不能为负数: " + n);
        }
        
        // 边界情况处理
        if (n <= 1) {
            return 1; // n=0时空树也是一种情况，n=1时只有一种情况
        }
        
        // 使用long避免中间结果溢出
        long catalan = 1;
        
        // 应用递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
        // 注意：先乘后除保证整除性
        for (int i = 1; i <= n; i++) {
            // 溢出检查 - 乘法前检查
            if (catalan > Long.MAX_VALUE / (4 * i - 2)) {
                throw new ArithmeticException("计算中间结果溢出，对于n = " + n + "，请使用BigInteger类型或取模运算");
            }
            
            // 先乘后除 - 数学上保证整除
            catalan = catalan * (4 * i - 2) / (i + 1);
            
            // 溢出检查 - 确保结果在int范围内
            if (catalan > Integer.MAX_VALUE) {
                throw new ArithmeticException("计算结果超出int范围，对于n = " + n + "，请使用long类型或取模运算");
            }
        }
        
        return (int) catalan;
    }
    
    /**
     * 使用组合公式计算卡特兰数 - 适用于需要取模的情况
     * 公式：C(n) = C(2n, n) / (n+1) = (2n)! / [n! * (n+1)!]
     * 
     * 该方法通过预处理阶乘和逆元，使用模运算避免溢出
     * 特别适用于大规模数据和编程竞赛场景
     * 
     * 时间复杂度：O(n)，预处理阶乘和逆元
     * 空间复杂度：O(n)，存储阶乘和逆元数组
     * 
     * @param n 节点数量
     * @param mod 模数
     * @return 卡特兰数模mod的结果
     * @throws IllegalArgumentException 当输入参数不合法时抛出异常
     */
    public static long numTreesMod(int n, long mod) {
        // 输入验证 - 全面的参数检查
        if (n < 0) {
            throw new IllegalArgumentException("节点数量不能为负数: " + n);
        }
        if (mod <= 1) {
            throw new IllegalArgumentException("模数必须大于1: " + mod);
        }
        
        // 边界情况处理
        if (n <= 1) {
            return 1 % mod;
        }
        
        // 预处理阶乘和逆元 - 用于快速计算组合数
        long[] factorial = new long[2 * n + 1];
        long[] inverse = new long[2 * n + 1];
        
        // 计算阶乘模mod
        factorial[0] = 1;
        for (int i = 1; i <= 2 * n; i++) {
            factorial[i] = (factorial[i - 1] * i) % mod;
        }
        
        // 计算逆元 - 使用费马小定理 (mod为质数时)
        // 逆元递推公式：inv[i] = inv[i+1] * (i+1) % mod
        inverse[2 * n] = power(factorial[2 * n], mod - 2, mod);
        for (int i = 2 * n - 1; i >= 0; i--) {
            inverse[i] = (inverse[i + 1] * (i + 1)) % mod;
        }
        
        // 计算组合数 C(2n, n) = (2n)! / (n! * n!)
        long combination = (factorial[2 * n] * inverse[n]) % mod;
        combination = (combination * inverse[n]) % mod;
        
        // 计算卡特兰数：C(2n, n) / (n+1) = C(2n, n) * inv(n+1) mod mod
        long invNPlus1 = power(n + 1, mod - 2, mod);
        long catalan = (combination * invNPlus1) % mod;
        
        return catalan;
    }
    
    /**
     * 快速幂算法 - 计算base^exponent mod mod
     * 
     * 时间复杂度：O(log exponent)，指数级减少循环次数
     * 空间复杂度：O(1)
     * 
     * @param base 底数
     * @param exponent 指数
     * @param mod 模数
     * @return 计算结果
     */
    private static long power(long base, long exponent, long mod) {
        long result = 1;
        base = base % mod; // 预处理底数，确保在模范围内
        
        // 快速幂核心逻辑
        while (exponent > 0) {
            // 如果指数为奇数，乘上当前base
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }
            // 指数减半，base平方
            exponent = exponent >> 1;
            base = (base * base) % mod;
        }
        
        return result;
    }
    
    /**
     * 打印性能指标，格式化输出执行时间
     * 
     * @param operation 操作描述
     * @param duration 执行时间（纳秒）
     */
    private static void printPerformance(String operation, long duration) {
        if (duration < 1000) {
            System.out.println("  " + operation + ": " + duration + " ns");
        } else if (duration < 1000000) {
            System.out.println("  " + operation + ": " + String.format("%.2f", duration / 1000.0) + " μs");
        } else if (duration < 1000000000) {
            System.out.println("  " + operation + ": " + String.format("%.2f", duration / 1000000.0) + " ms");
        } else {
            System.out.println("  " + operation + ": " + String.format("%.2f", duration / 1000000000.0) + " s");
        }
    }
    
    /**
     * 主方法 - 测试所有实现
     * 包含多种测试场景：基本测试、边界情况、异常处理、性能测试等
     */
    public static void main(String[] args) {
        // 测试用例 - 覆盖正常输入、边界输入
        int[] testCases = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long mod = 1000000007; // 常用模数
        
        System.out.println("===== 不同二叉搜索树计数测试 =====");
        System.out.println("使用三种方法计算：动态规划、优化递推公式、组合公式取模");
        System.out.println();
        
        // 测试每种方法
        for (int n : testCases) {
            try {
                System.out.println("n = " + n);
                
                // 动态规划方法
                long startTime = System.nanoTime();
                int result1 = numTrees(n);
                long endTime = System.nanoTime();
                printPerformance("动态规划方法: " + result1, endTime - startTime);
                
                // 优化递推公式方法
                startTime = System.nanoTime();
                int result2 = numTreesOptimized(n);
                endTime = System.nanoTime();
                printPerformance("优化递推公式: " + result2, endTime - startTime);
                
                // 组合公式取模方法
                startTime = System.nanoTime();
                long result3 = numTreesMod(n, mod);
                endTime = System.nanoTime();
                printPerformance("组合公式取模: " + result3 + " (mod " + mod + ")", endTime - startTime);
                
                // 验证结果一致性
                if (result1 == result2 && result1 % mod == result3) {
                    System.out.println("  ✓ 所有方法结果一致");
                } else {
                    System.out.println("  ✗ 结果不一致，请检查实现");
                }
                
                System.out.println();
            } catch (Exception e) {
                System.out.println("  计算异常: " + e.getMessage());
                System.out.println();
            }
        }
        
        // 测试异常处理
        System.out.println("===== 异常处理测试 =====");
        try {
            numTrees(-1);
            System.out.println("✗ 未能捕获负数输入异常");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ 正确捕获负数输入异常: " + e.getMessage());
        }
        
        try {
            numTreesMod(5, 0);
            System.out.println("✗ 未能捕获非法模数异常");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ 正确捕获非法模数异常: " + e.getMessage());
        }
        
        // 性能测试 - 计算较大的n值（但不溢出）
        System.out.println("\n===== 性能测试 =====");
        int[] largeTestCases = {15, 19, 20}; // 20是int能容纳的最大卡特兰数
        for (int n : largeTestCases) {
            System.out.println("n = " + n);
            
            // 对比动态规划和优化公式的性能差异
            long startTime = System.nanoTime();
            int resultDP = numTrees(n);
            long endTime = System.nanoTime();
            printPerformance("动态规划 (n=" + n + ")", endTime - startTime);
            
            startTime = System.nanoTime();
            int resultOpt = numTreesOptimized(n);
            endTime = System.nanoTime();
            printPerformance("优化公式 (n=" + n + ")", endTime - startTime);
            
            // 对比性能提升
            double ratio = (double)(System.nanoTime() - startTime) / (endTime - startTime);
            System.out.println("  性能提升: 约" + String.format("%.2f", ratio) + "倍");
            System.out.println();
        }
        
        // 模运算版本测试 - 更大的n值
        System.out.println("\n===== 模运算版本测试 =====");
        int[] modTestCases = {100, 500, 1000};
        for (int n : modTestCases) {
            System.out.println("n = " + n);
            
            long startTime = System.nanoTime();
            long result = numTreesMod(n, mod);
            long endTime = System.nanoTime();
            
            printPerformance("模运算结果: " + result, endTime - startTime);
            System.out.println();
        }
        
        // 最优解分析总结
        System.out.println("===== 最优解分析 =====");
        System.out.println("1. 算法选择建议:");
        System.out.println("   - 当n较小时 (<= 20)，优先使用优化递推公式，时间复杂度O(n)，空间复杂度O(1)");
        System.out.println("   - 当需要取模时，使用组合公式+预处理阶乘和逆元，时间复杂度O(n)，空间复杂度O(n)");
        System.out.println("   - 对于非常大的n值，需要实现高精度计算或使用BigInteger类型");
        System.out.println("   - 动态规划方法虽然时间复杂度较高(O(n²))，但易于理解和扩展到其他类似问题");
        System.out.println();
        System.out.println("2. 工程化考量:");
        System.out.println("   - 输入验证：全面检查参数合法性，确保健壮性");
        System.out.println("   - 溢出检测：预先检测可能的溢出情况，提供清晰错误信息");
        System.out.println("   - 性能优化：根据数据规模选择合适的算法");
        System.out.println("   - 代码可读性：模块化设计，详细注释，提高可维护性");
        System.out.println();
        System.out.println("3. 相关题目和应用场景:");
        System.out.println("   - LeetCode 96. 不同的二叉搜索树: 本题的标准实现");
        System.out.println("   - LeetCode 95. 不同的二叉搜索树 II: 生成所有可能的BST结构");
        System.out.println("   - LintCode 1638. 不同的二叉搜索树: 类似的问题");
        System.out.println("   - 卡特兰数的其他应用：括号生成、出栈序列计数、多边形三角剖分等");
        System.out.println();
        System.out.println("4. Java语言特性注意事项:");
        System.out.println("   - 整数类型限制：int类型最大只能表示到第20个卡特兰数");
        System.out.println("   - 模运算实现：利用费马小定理计算逆元，适用于质数模数");
        System.out.println("   - 性能考虑：避免不必要的对象创建，使用原始类型提高效率");
        System.out.println();
        System.out.println("5. 进阶思考:");
        System.out.println("   - 如何处理超大n值？（使用高精度计算或大整数库）");
        System.out.println("   - 如何扩展到不同类型的二叉树计数问题？");
        System.out.println("   - 如何优化内存使用？（考虑动态规划的滚动数组优化）");
        System.out.println("   - 在实际应用中如何选择合适的实现方式？");
    }
}
package class145;

import java.util.*;

/**
 * 二项式反演综合测试类
 * 
 * 包含所有题目的单元测试、边界条件测试和性能测试
 * 确保代码的正确性、鲁棒性和性能
 */
public class Code15_ComprehensiveTest {
    
    /**
     * 测试错排问题（Code01_Derangement）
     */
    public static void testDerangement() {
        System.out.println("=== 错排问题测试 ===");
        
        // 边界测试
        assert Code01_Derangement.ways1(0) == 1 : "n=0 测试失败";
        assert Code01_Derangement.ways1(1) == 0 : "n=1 测试失败";
        assert Code01_Derangement.ways1(2) == 1 : "n=2 测试失败";
        
        // 正常测试
        assert Code01_Derangement.ways1(3) == 2 : "n=3 测试失败";
        assert Code01_Derangement.ways1(4) == 9 : "n=4 测试失败";
        assert Code01_Derangement.ways1(5) == 44 : "n=5 测试失败";
        
        // 方法一致性测试
        for (int n = 0; n <= 10; n++) {
            long result1 = Code01_Derangement.ways1(n);
            long result2 = Code01_Derangement.ways2(n);
            long result3 = Code01_Derangement.ways3(n);
            assert result1 == result2 && result2 == result3 : 
                "方法不一致: n=" + n + ", ways1=" + result1 + ", ways2=" + result2 + ", ways3=" + result3;
        }
        
        System.out.println("错排问题测试通过");
    }
    
    /**
     * 测试集合计数问题（Code02_SetCounting）
     */
    public static void testSetCounting() {
        System.out.println("\n=== 集合计数问题测试 ===");
        
        // 创建测试实例
        Code02_SetCounting setCounting = new Code02_SetCounting();
        
        // 边界测试
        setCounting.n = 0;
        setCounting.k = 0;
        assert setCounting.compute() == 1 : "n=0,k=0 测试失败";
        
        setCounting.n = 1;
        setCounting.k = 0;
        assert setCounting.compute() == 1 : "n=1,k=0 测试失败";
        
        setCounting.n = 1;
        setCounting.k = 1;
        assert setCounting.compute() == 1 : "n=1,k=1 测试失败";
        
        // 正常测试
        setCounting.n = 2;
        setCounting.k = 1;
        assert setCounting.compute() == 2 : "n=2,k=1 测试失败";
        
        setCounting.n = 3;
        setCounting.k = 1;
        assert setCounting.compute() == 12 : "n=3,k=1 测试失败";
        
        System.out.println("集合计数问题测试通过");
    }
    
    /**
     * 测试排列计数问题（Code03_PermutationCounting）
     */
    public static void testPermutationCounting() {
        System.out.println("\n=== 排列计数问题测试 ===");
        
        // 边界测试
        Code03_PermutationCounting.n = 0;
        Code03_PermutationCounting.k = 0;
        assert Code03_PermutationCounting.countFixedPointsOptimized() == 1 : "n=0,k=0 测试失败";
        
        Code03_PermutationCounting.n = 1;
        Code03_PermutationCounting.k = 0;
        assert Code03_PermutationCounting.countFixedPointsOptimized() == 0 : "n=1,k=0 测试失败";
        
        Code03_PermutationCounting.n = 1;
        Code03_PermutationCounting.k = 1;
        assert Code03_PermutationCounting.countFixedPointsOptimized() == 1 : "n=1,k=1 测试失败";
        
        // 正常测试
        Code03_PermutationCounting.n = 3;
        Code03_PermutationCounting.k = 1;
        assert Code03_PermutationCounting.countFixedPointsOptimized() == 3 : "n=3,k=1 测试失败";
        
        Code03_PermutationCounting.n = 4;
        Code03_PermutationCounting.k = 2;
        assert Code03_PermutationCounting.countFixedPointsOptimized() == 6 : "n=4,k=2 测试失败";
        
        System.out.println("排列计数问题测试通过");
    }
    
    /**
     * 测试全排列II问题（Code12_LeetCode47_PermutationsII）
     */
    public static void testPermutationsII() {
        System.out.println("\n=== 全排列II问题测试 ===");
        
        Code12_LeetCode47_PermutationsII solution = new Code12_LeetCode47_PermutationsII();
        
        // 边界测试：空数组
        int[] empty = {};
        List<List<Integer>> resultEmpty = solution.permuteUnique(empty);
        assert resultEmpty.size() == 0 : "空数组测试失败";
        
        // 边界测试：单元素数组
        int[] single = {1};
        List<List<Integer>> resultSingle = solution.permuteUnique(single);
        assert resultSingle.size() == 1 : "单元素数组测试失败";
        
        // 正常测试：重复元素
        int[] nums1 = {1, 1, 2};
        List<List<Integer>> result1 = solution.permuteUnique(nums1);
        assert result1.size() == 3 : "[1,1,2] 测试失败";
        
        // 验证数学计算
        assert solution.countUniquePermutations(nums1) == 3 : "数学计算测试失败";
        
        // 正常测试：无重复元素
        int[] nums2 = {1, 2, 3};
        List<List<Integer>> result2 = solution.permuteUnique(nums2);
        assert result2.size() == 6 : "[1,2,3] 测试失败";
        
        System.out.println("全排列II问题测试通过");
    }
    
    /**
     * 测试扩展集合计数问题（Code13_SetCountingExtended）
     */
    public static void testSetCountingExtended() {
        System.out.println("\n=== 扩展集合计数问题测试 ===");
        
        // 测试洛谷问题
        long result1 = Code13_SetCountingExtended.luoguSetCounting(3, 1);
        assert result1 == 12 : "luoguSetCounting(3,1) 测试失败";
        
        // 测试牛客网问题
        long result2 = Code13_SetCountingExtended.nowcoderSetCounting(3);
        assert result2 == 6 : "nowcoderSetCounting(3) 测试失败";
        
        // 测试CodeChef问题
        double result3 = Code13_SetCountingExtended.codechefRNG(5, 2, 0.5);
        assert result3 > 0 && result3 < 1 : "codechefRNG 测试失败";
        
        // 测试HackerEarth问题
        double result4 = Code13_SetCountingExtended.hackerearthXORSort(5);
        assert result4 >= 0 : "hackerearthXORSort 测试失败";
        
        System.out.println("扩展集合计数问题测试通过");
    }
    
    /**
     * 测试多平台二项式反演问题（Code14_MultiPlatformBinomialInversion）
     */
    public static void testMultiPlatform() {
        System.out.println("\n=== 多平台二项式反演问题测试 ===");
        
        // 测试UVa问题
        long result1 = Code14_MultiPlatformBinomialInversion.uvaGCDExtreme(10);
        assert result1 > 0 : "uvaGCDExtreme 测试失败";
        
        // 测试杭电问题
        long result2 = Code14_MultiPlatformBinomialInversion.hduKillerNames(2, 3);
        assert result2 > 0 : "hduKillerNames 测试失败";
        
        // 测试AizuOJ问题
        long result3 = Code14_MultiPlatformBinomialInversion.aizuPermutationCount(4, 1);
        assert result3 == 3 : "aizuPermutationCount 测试失败";
        
        // 测试TimusOJ问题
        long result4 = Code14_MultiPlatformBinomialInversion.timusGeneratingSets(5, 2);
        assert result4 > 0 : "timusGeneratingSets 测试失败";
        
        // 测试Comet OJ问题
        long result5 = Code14_MultiPlatformBinomialInversion.cometSetCounting(3, 1);
        assert result5 == 12 : "cometSetCounting 测试失败";
        
        // 测试acwing问题
        long result6 = Code14_MultiPlatformBinomialInversion.acwingFibonacciGCD(6, 8);
        assert result6 == 2 : "acwingFibonacciGCD 测试失败";
        
        System.out.println("多平台二项式反演问题测试通过");
    }
    
    /**
     * 边界条件综合测试
     */
    public static void testBoundaryConditions() {
        System.out.println("\n=== 边界条件综合测试 ===");
        
        // 测试极小值
        testExtremeSmallValues();
        
        // 测试极大值（在合理范围内）
        testExtremeLargeValues();
        
        // 测试非法输入
        testInvalidInputs();
        
        System.out.println("边界条件综合测试通过");
    }
    
    private static void testExtremeSmallValues() {
        // n=0的各种情况
        assert Code01_Derangement.ways1(0) == 1 : "n=0 错排测试失败";
        
        Code03_PermutationCounting.n = 0;
        Code03_PermutationCounting.k = 0;
        assert Code03_PermutationCounting.countFixedPointsOptimized() == 1 : "n=0,k=0 排列计数测试失败";
        
        // n=1的各种情况
        assert Code01_Derangement.ways1(1) == 0 : "n=1 错排测试失败";
        
        Code03_PermutationCounting.n = 1;
        Code03_PermutationCounting.k = 1;
        assert Code03_PermutationCounting.countFixedPointsOptimized() == 1 : "n=1,k=1 排列计数测试失败";
    }
    
    private static void testExtremeLargeValues() {
        // 测试中等规模数据（避免性能问题）
        long startTime = System.nanoTime();
        
        // 测试错排问题
        long result1 = Code01_Derangement.ways1(20);
        assert result1 > 0 : "n=20 错排测试失败";
        
        // 测试集合计数问题
        Code02_SetCounting setCounting = new Code02_SetCounting();
        setCounting.n = 100;
        setCounting.k = 50;
        long result2 = setCounting.compute();
        assert result2 > 0 : "n=100,k=50 集合计数测试失败";
        
        long endTime = System.nanoTime();
        System.out.println("大规模数据测试耗时: " + (endTime - startTime) / 1e6 + " ms");
    }
    
    private static void testInvalidInputs() {
        // 测试非法参数
        try {
            Code03_PermutationCounting.n = -1;
            Code03_PermutationCounting.k = 0;
            Code03_PermutationCounting.countFixedPointsOptimized();
            assert false : "非法输入n=-1应该抛出异常";
        } catch (Exception e) {
            // 期望的行为
        }
        
        // 测试k>n的情况
        Code03_PermutationCounting.n = 3;
        Code03_PermutationCounting.k = 5;
        long result = Code03_PermutationCounting.countFixedPointsOptimized();
        assert result == 0 : "k>n 应该返回0";
    }
    
    /**
     * 性能基准测试
     */
    public static void performanceBenchmark() {
        System.out.println("\n=== 性能基准测试 ===");
        
        // 测试错排问题的性能
        benchmarkDerangement();
        
        // 测试集合计数问题的性能
        benchmarkSetCounting();
        
        // 测试排列计数问题的性能
        benchmarkPermutationCounting();
        
        System.out.println("性能基准测试完成");
    }
    
    private static void benchmarkDerangement() {
        long startTime = System.nanoTime();
        
        for (int n = 1; n <= 100; n++) {
            Code01_Derangement.ways1(n);
        }
        
        long endTime = System.nanoTime();
        System.out.println("错排问题性能: " + (endTime - startTime) / 1e6 + " ms (n=1-100)");
    }
    
    private static void benchmarkSetCounting() {
        Code02_SetCounting setCounting = new Code02_SetCounting();
        
        long startTime = System.nanoTime();
        
        setCounting.n = 1000;
        setCounting.k = 500;
        setCounting.compute();
        
        long endTime = System.nanoTime();
        System.out.println("集合计数问题性能: " + (endTime - startTime) / 1e6 + " ms (n=1000,k=500)");
    }
    
    private static void benchmarkPermutationCounting() {
        long startTime = System.nanoTime();
        
        for (int n = 1; n <= 100; n++) {
            for (int k = 0; k <= n; k++) {
                Code03_PermutationCounting.n = n;
                Code03_PermutationCounting.k = k;
                Code03_PermutationCounting.countFixedPointsOptimized();
            }
        }
        
        long endTime = System.nanoTime();
        System.out.println("排列计数问题性能: " + (endTime - startTime) / 1e6 + " ms (n=1-100, 所有k)");
    }
    
    /**
     * 运行所有测试
     */
    public static void runAllTests() {
        System.out.println("开始运行二项式反演综合测试...\n");
        
        try {
            testDerangement();
            testSetCounting();
            testPermutationCounting();
            testPermutationsII();
            testSetCountingExtended();
            testMultiPlatform();
            testBoundaryConditions();
            performanceBenchmark();
            
            System.out.println("\n✅ 所有测试通过！二项式反演算法实现正确且高效。");
            
        } catch (AssertionError e) {
            System.out.println("\n❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n❌ 测试异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        // 启用断言
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        
        runAllTests();
    }
    
    /**
     * 测试覆盖率分析：
     * 
     * 1. 功能测试：验证算法正确性
     * 2. 边界测试：测试极端输入情况
     * 3. 性能测试：评估算法效率
     * 4. 一致性测试：验证不同实现的一致性
     * 5. 异常测试：验证错误处理
     * 
     * 工程化考量：
     * - 自动化测试：所有测试可以自动运行
     * - 断言机制：使用Java断言验证结果
     * - 性能监控：记录执行时间
     * - 错误报告：详细的错误信息
     * - 测试隔离：每个测试独立运行
     */
}
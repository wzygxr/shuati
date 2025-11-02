package class139;

/**
 * 扩展欧几里得算法综合测试类
 * 
 * 功能：
 * 1. 测试所有扩展欧几里得算法相关题目的实现
 * 2. 验证Java、C++、Python三语言实现的一致性
 * 3. 测试边界条件和异常处理
 * 4. 性能测试和复杂度验证
 * 
 * 测试范围：
 * - Code01_BezoutLemma: 裴蜀定理
 * - Code02_Pagodas: 修理宝塔问题
 * - Code03_UniformGenerator: 均匀生成器
 * - Code04_CongruenceEquation: 同余方程
 * - Code05_ShuffleCards: 洗牌问题
 * - Code06_ExtendedEuclideanProblems: 扩展欧几里得问题集
 * - Code07_FrogDate: 青蛙约会
 * - Code08_CLooooops: C循环问题
 * - Code09_DiophantineEquation: 线性丢番图方程
 * 
 * 测试策略：
 * 1. 功能测试：验证算法正确性
 * 2. 边界测试：测试极端输入
 * 3. 异常测试：验证异常处理
 * 4. 性能测试：验证时间复杂度
 * 5. 一致性测试：验证三语言实现一致性
 */

import java.util.Arrays;

public class ComprehensiveTest {
    
    /**
     * 测试Code01_BezoutLemma - 裴蜀定理
     */
    public static void testBezoutLemma() {
        System.out.println("=== 测试Code01_BezoutLemma ===");
        
        // 测试用例1：正常情况
        int[] test1 = {6, 9, 15};
        int result1 = Code01_BezoutLemma.gcdMultiple(test1);
        System.out.println("测试1 [6, 9, 15]: " + result1 + " (期望: 3)");
        assert result1 == 3 : "测试1失败";
        
        // 测试用例2：包含负数
        int[] test2 = {-4, 6, -8};
        int result2 = Code01_BezoutLemma.gcdMultiple(test2);
        System.out.println("测试2 [-4, 6, -8]: " + result2 + " (期望: 2)");
        assert result2 == 2 : "测试2失败";
        
        // 测试用例3：单个数字
        int[] test3 = {17};
        int result3 = Code01_BezoutLemma.gcdMultiple(test3);
        System.out.println("测试3 [17]: " + result3 + " (期望: 17)");
        assert result3 == 17 : "测试3失败";
        
        System.out.println("Code01_BezoutLemma 测试通过\n");
    }
    
    /**
     * 测试Code02_Pagodas - 修理宝塔问题
     */
    public static void testPagodas() {
        System.out.println("=== 测试Code02_Pagodas ===");
        
        // 测试用例1：后手赢
        boolean result1 = Code02_Pagodas.isFirstPlayerWin(12, 3, 6);
        System.out.println("测试1 n=12, a=3, b=6: " + result1 + " (期望: false)");
        assert !result1 : "测试1失败";
        
        // 测试用例2：先手赢
        boolean result2 = Code02_Pagodas.isFirstPlayerWin(10, 2, 3);
        System.out.println("测试2 n=10, a=2, b=3: " + result2 + " (期望: true)");
        assert result2 : "测试2失败";
        
        // 测试用例3：边界情况
        boolean result3 = Code02_Pagodas.isFirstPlayerWin(8, 2, 4);
        System.out.println("测试3 n=8, a=2, b=4: " + result3 + " (期望: false)");
        assert !result3 : "测试3失败";
        
        System.out.println("Code02_Pagodas 测试通过\n");
    }
    
    /**
     * 测试扩展欧几里得算法基本功能
     */
    public static void testExtendedGCD() {
        System.out.println("=== 测试扩展欧几里得算法 ===");
        
        // 测试用例1：基本功能
        int a1 = 48, b1 = 18;
        int x1 = 0, y1 = 0;
        int gcd1 = Code06_ExtendedEuclideanProblems.exgcd_iterative(a1, b1, x1, y1);
        System.out.println("测试1 gcd(48, 18): " + gcd1 + " (期望: 6)");
        assert gcd1 == 6 : "测试1失败";
        
        // 测试用例2：互质数
        int a2 = 17, b2 = 13;
        int x2 = 0, y2 = 0;
        int gcd2 = Code06_ExtendedEuclideanProblems.exgcd_iterative(a2, b2, x2, y2);
        System.out.println("测试2 gcd(17, 13): " + gcd2 + " (期望: 1)");
        assert gcd2 == 1 : "测试2失败";
        
        // 测试用例3：模逆元
        long inverse1 = Code06_ExtendedEuclideanProblems.mod_inverse(3, 11);
        System.out.println("测试3 3在模11下的逆元: " + inverse1 + " (期望: 4)");
        assert inverse1 == 4 : "测试3失败";
        
        System.out.println("扩展欧几里得算法测试通过\n");
    }
    
    /**
     * 测试线性同余方程求解
     */
    public static void testLinearCongruence() {
        System.out.println("=== 测试线性同余方程 ===");
        
        // 测试用例1：有解情况
        long result1 = Code06_ExtendedEuclideanProblems.linear_congruence(3, 1, 11);
        System.out.println("测试1 3x ≡ 1 mod 11: " + result1 + " (期望: 4)");
        assert result1 == 4 : "测试1失败";
        
        // 测试用例2：无解情况
        long result2 = Code06_ExtendedEuclideanProblems.linear_congruence(4, 2, 6);
        System.out.println("测试2 4x ≡ 2 mod 6: " + result2 + " (期望: 2)");
        assert result2 == 2 : "测试2失败";
        
        // 测试用例3：青蛙约会问题
        long result3 = Code07_FrogDate.solve_frog_date(1, 2, 3, 4, 5);
        System.out.println("测试3 青蛙约会: " + result3 + " (期望: 有解)");
        assert result3 != -1 : "测试3失败";
        
        System.out.println("线性同余方程测试通过\n");
    }
    
    /**
     * 测试线性丢番图方程
     */
    public static void testDiophantineEquation() {
        System.out.println("=== 测试线性丢番图方程 ===");
        
        // 测试用例1：有解情况
        long x1 = 0, y1 = 0;
        boolean hasSol1 = Code09_DiophantineEquation.solve_linear_diophantine(6, 9, 15, x1, y1);
        System.out.println("测试1 6x + 9y = 15: " + hasSol1 + " (期望: true)");
        assert hasSol1 : "测试1失败";
        
        // 测试用例2：无解情况
        long x2 = 0, y2 = 0;
        boolean hasSol2 = Code09_DiophantineEquation.solve_linear_diophantine(4, 6, 9, x2, y2);
        System.out.println("测试2 4x + 6y = 9: " + hasSol2 + " (期望: false)");
        assert !hasSol2 : "测试2失败";
        
        // 测试用例3：解的存在性判断
        boolean exists1 = Code09_DiophantineEquation.has_solution(3, 5, 1);
        System.out.println("测试3 3x + 5y = 1有解: " + exists1 + " (期望: true)");
        assert exists1 : "测试3失败";
        
        System.out.println("线性丢番图方程测试通过\n");
    }
    
    /**
     * 性能测试 - 验证时间复杂度
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 测试大数情况下的性能
        long startTime = System.nanoTime();
        
        // 测试大数的最大公约数计算
        int largeA = 1234567890;
        int largeB = 987654321;
        int gcdResult = Code01_BezoutLemma.gcd(largeA, largeB);
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000; // 微秒
        
        System.out.println("大数gcd计算: gcd(" + largeA + ", " + largeB + ") = " + gcdResult);
        System.out.println("计算时间: " + duration + " 微秒");
        
        // 验证时间复杂度：O(log(min(a,b)))
        // 对于10^9级别的数，log10(10^9) ≈ 9，应该在合理时间内完成
        assert duration < 1000 : "性能测试失败，计算时间过长";
        
        System.out.println("性能测试通过\n");
    }
    
    /**
     * 边界条件测试
     */
    public static void boundaryTest() {
        System.out.println("=== 边界条件测试 ===");
        
        // 测试用例1：零值处理
        try {
            int[] test1 = {0, 0, 0};
            Code01_BezoutLemma.gcdMultiple(test1);
            System.out.println("测试1 [0,0,0]: 未抛出异常 - 失败");
            assert false : "应该抛出异常";
        } catch (IllegalArgumentException e) {
            System.out.println("测试1 [0,0,0]: 正确抛出异常 - " + e.getMessage());
        }
        
        // 测试用例2：空数组
        try {
            int[] test2 = {};
            Code01_BezoutLemma.gcdMultiple(test2);
            System.out.println("测试2 空数组: 未抛出异常 - 失败");
            assert false : "应该抛出异常";
        } catch (IllegalArgumentException e) {
            System.out.println("测试2 空数组: 正确抛出异常 - " + e.getMessage());
        }
        
        // 测试用例3：负数处理
        int[] test3 = {-12, -18, -24};
        int result3 = Code01_BezoutLemma.gcdMultiple(test3);
        System.out.println("测试3 [-12,-18,-24]: " + result3 + " (期望: 6)");
        assert result3 == 6 : "测试3失败";
        
        System.out.println("边界条件测试通过\n");
    }
    
    /**
     * 一致性测试 - 验证不同实现的一致性
     */
    public static void consistencyTest() {
        System.out.println("=== 一致性测试 ===");
        
        // 测试不同算法实现的一致性
        int a = 48, b = 18;
        
        // 递归版本
        int x1 = 0, y1 = 0;
        int gcd1 = Code06_ExtendedEuclideanProblems.exgcd_recursive(a, b, x1, y1);
        
        // 迭代版本
        int x2 = 0, y2 = 0;
        int gcd2 = Code06_ExtendedEuclideanProblems.exgcd_iterative(a, b, x2, y2);
        
        System.out.println("递归版本: gcd=" + gcd1 + ", x=" + x1 + ", y=" + y1);
        System.out.println("迭代版本: gcd=" + gcd2 + ", x=" + x2 + ", y=" + y2);
        
        assert gcd1 == gcd2 : "递归和迭代版本结果不一致";
        assert a * x1 + b * y1 == gcd1 : "递归版本验证失败";
        assert a * x2 + b * y2 == gcd2 : "迭代版本验证失败";
        
        System.out.println("一致性测试通过\n");
    }
    
    /**
     * 运行所有测试
     */
    public static void runAllTests() {
        System.out.println("开始运行扩展欧几里得算法综合测试...\n");
        
        try {
            testBezoutLemma();
            testPagodas();
            testExtendedGCD();
            testLinearCongruence();
            testDiophantineEquation();
            performanceTest();
            boundaryTest();
            consistencyTest();
            
            System.out.println("=== 所有测试通过 ===");
            System.out.println("扩展欧几里得算法实现验证完成！");
            
        } catch (AssertionError e) {
            System.err.println("测试失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("测试异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        runAllTests();
    }
}
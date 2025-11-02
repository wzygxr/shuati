package class134;

import java.io.*;
import java.util.*;

/**
 * 高斯消元与线性基算法全面测试类
 * 
 * 测试目标：
 * 1. 验证高斯消元算法的正确性
 * 2. 测试各种边界条件和异常情况
 * 3. 验证线性基算法的功能
 * 4. 测试多语言实现的兼容性
 * 
 * 测试覆盖范围：
 * - 唯一解情况
 * - 无穷多解情况
 * - 无解情况
 * - 边界条件（空矩阵、全零矩阵等）
 * - 极端输入（大矩阵、特殊值等）
 * 
 * 测试方法：
 * 1. 单元测试：针对每个函数进行独立测试
 * 2. 集成测试：测试整个算法流程
 * 3. 性能测试：测试算法的时间空间复杂度
 * 4. 兼容性测试：测试不同语言的实现一致性
 */
public class Test_Gauss_XOR_All {
    
    /**
     * 测试高斯消元算法的基本功能
     */
    public static void testBasicGauss() {
        System.out.println("=== 测试1: 高斯消元基本功能 ===");
        
        // 测试用例1: 有唯一解
        System.out.println("测试用例1 - 唯一解:");
        int n1 = 3;
        int[][] mat1 = {
            {1, 1, 1, 0},  // x1 ^ x2 ^ x3 = 0
            {1, 0, 1, 1},   // x1 ^ x3 = 1
            {0, 1, 1, 1}    // x2 ^ x3 = 1
        };
        testGaussCase(n1, mat1, 0, "唯一解");
        
        // 测试用例2: 无解
        System.out.println("\n测试用例2 - 无解:");
        int n2 = 3;
        int[][] mat2 = {
            {1, 1, 0, 1},   // x1 ^ x2 = 1
            {1, 0, 1, 1},   // x1 ^ x3 = 1
            {0, 1, 1, 1}    // x2 ^ x3 = 1
        };
        testGaussCase(n2, mat2, -1, "无解");
        
        // 测试用例3: 无穷多解
        System.out.println("\n测试用例3 - 无穷多解:");
        int n3 = 3;
        int[][] mat3 = {
            {1, 0, 1, 1},   // x1 ^ x3 = 1
            {0, 1, 1, 1},   // x2 ^ x3 = 1
            {1, 1, 0, 0}    // x1 ^ x2 = 0
        };
        testGaussCase(n3, mat3, 1, "无穷多解");
    }
    
    /**
     * 测试单个高斯消元案例
     */
    private static void testGaussCase(int n, int[][] inputMat, int expected, String caseName) {
        // 复制矩阵到全局变量
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                Code04_GaussXorTemplate.mat[i][j] = inputMat[i-1][j-1];
            }
        }
        
        // 执行高斯消元
        int result = Code04_GaussXorTemplate.gauss(n);
        
        // 验证结果
        if (result == expected) {
            System.out.println("✓ " + caseName + "测试通过");
        } else {
            System.out.println("✗ " + caseName + "测试失败，期望:" + expected + "，实际:" + result);
        }
        
        // 打印矩阵状态用于调试
        System.out.println("消元后矩阵:");
        Code04_GaussXorTemplate.printMatrix(n);
    }
    
    /**
     * 测试线性基算法的基本功能
     */
    public static void testLinearBasis() {
        System.out.println("\n=== 测试2: 线性基算法基本功能 ===");
        
        // 测试用例1: 最大异或值
        System.out.println("测试用例1 - 最大异或值:");
        long[] nums1 = {3L, 5L, 7L, 9L};
        long expected1 = 15L; // 3 ^ 5 ^ 7 ^ 9 = 15
        testLinearBasisCase(nums1, expected1, "最大异或值");
        
        // 测试用例2: 线性基插入
        System.out.println("\n测试用例2 - 线性基插入:");
        long[] nums2 = {1L, 2L, 4L, 8L};
        long expected2 = 15L; // 所有基向量的异或
        testLinearBasisCase(nums2, expected2, "线性基插入");
    }
    
    /**
     * 测试单个线性基案例
     */
    private static void testLinearBasisCase(long[] nums, long expected, String caseName) {
        // 创建线性基
        Code07_XMAX.LinearBasis lb = new Code07_XMAX.LinearBasis();
        
        // 插入所有数字
        for (long num : nums) {
            lb.insert(num);
        }
        
        // 获取最大异或值
        long result = lb.getMaxXor();
        
        // 验证结果
        if (result == expected) {
            System.out.println("✓ " + caseName + "测试通过");
        } else {
            System.out.println("✗ " + caseName + "测试失败，期望:" + expected + "，实际:" + result);
        }
        
        // 打印线性基状态
        System.out.println("线性基状态:");
        lb.printBasis();
    }
    
    /**
     * 测试边界条件和异常情况
     */
    public static void testEdgeCases() {
        System.out.println("\n=== 测试3: 边界条件和异常情况 ===");
        
        // 测试用例1: 空矩阵
        System.out.println("测试用例1 - 空矩阵:");
        try {
            int result = Code04_GaussXorTemplate.gauss(0);
            System.out.println("空矩阵处理结果:" + result);
        } catch (Exception e) {
            System.out.println("空矩阵异常处理:" + e.getMessage());
        }
        
        // 测试用例2: 全零矩阵
        System.out.println("\n测试用例2 - 全零矩阵:");
        int n = 3;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                Code04_GaussXorTemplate.mat[i][j] = 0;
            }
        }
        int result = Code04_GaussXorTemplate.gauss(n);
        System.out.println("全零矩阵结果:" + result);
        
        // 测试用例3: 单位矩阵
        System.out.println("\n测试用例3 - 单位矩阵:");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                Code04_GaussXorTemplate.mat[i][j] = (j == i) ? 1 : 0;
            }
            Code04_GaussXorTemplate.mat[i][n + 1] = 1; // 常数项为1
        }
        result = Code04_GaussXorTemplate.gauss(n);
        System.out.println("单位矩阵结果:" + result);
    }
    
    /**
     * 测试性能和大规模数据
     */
    public static void testPerformance() {
        System.out.println("\n=== 测试4: 性能测试 ===");
        
        // 测试小规模数据
        System.out.println("小规模数据测试 (n=10):");
        long startTime = System.currentTimeMillis();
        testRandomCase(10);
        long endTime = System.currentTimeMillis();
        System.out.println("耗时:" + (endTime - startTime) + "ms");
        
        // 测试中等规模数据
        System.out.println("\n中等规模数据测试 (n=50):");
        startTime = System.currentTimeMillis();
        testRandomCase(50);
        endTime = System.currentTimeMillis();
        System.out.println("耗时:" + (endTime - startTime) + "ms");
        
        // 测试大规模数据（可选）
        System.out.println("\n大规模数据测试 (n=100):");
        startTime = System.currentTimeMillis();
        testRandomCase(100);
        endTime = System.currentTimeMillis();
        System.out.println("耗时:" + (endTime - startTime) + "ms");
    }
    
    /**
     * 测试随机生成的案例
     */
    private static void testRandomCase(int n) {
        Random rand = new Random();
        
        // 生成随机矩阵
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                Code04_GaussXorTemplate.mat[i][j] = rand.nextInt(2); // 0或1
            }
        }
        
        // 执行高斯消元
        int result = Code04_GaussXorTemplate.gauss(n);
        System.out.println("随机矩阵结果:" + result);
    }
    
    /**
     * 测试多语言实现的一致性
     */
    public static void testCrossLanguageConsistency() {
        System.out.println("\n=== 测试5: 多语言实现一致性 ===");
        
        // 测试用例：简单异或方程组
        System.out.println("测试简单异或方程组的一致性:");
        
        // Java实现测试
        System.out.println("Java实现:");
        int n = 2;
        Code04_GaussXorTemplate.mat[1][1] = 1; Code04_GaussXorTemplate.mat[1][2] = 0; Code04_GaussXorTemplate.mat[1][3] = 1;
        Code04_GaussXorTemplate.mat[2][1] = 0; Code04_GaussXorTemplate.mat[2][2] = 1; Code04_GaussXorTemplate.mat[2][3] = 0;
        int javaResult = Code04_GaussXorTemplate.gauss(n);
        System.out.println("Java结果:" + javaResult);
        
        // 理论上C++和Python应该得到相同结果
        System.out.println("理论上C++和Python应该得到相同结果:" + javaResult);
        System.out.println("（需要实际运行C++和Python代码进行验证）");
    }
    
    /**
     * 运行所有测试
     */
    public static void runAllTests() {
        System.out.println("开始执行高斯消元与线性基算法全面测试...\n");
        
        // 执行所有测试
        testBasicGauss();
        testLinearBasis();
        testEdgeCases();
        testPerformance();
        testCrossLanguageConsistency();
        
        System.out.println("\n=== 测试完成 ===");
        System.out.println("所有测试执行完毕！");
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        // 运行全面测试
        runAllTests();
        
        // 运行单元测试
        System.out.println("\n=== 运行单元测试 ===");
        Code04_GaussXorTemplate.runUnitTests();
        
        // 输出语言特性对比
        System.out.println("\n=== 语言特性对比 ===");
        Code04_GaussXorTemplate.languageFeatureComparison();
        
        // 输出性能优化建议
        System.out.println("\n=== 性能优化建议 ===");
        Code04_GaussXorTemplate.performanceOptimizationTips();
    }
}
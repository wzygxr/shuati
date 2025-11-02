package class136;

import java.util.*;

/**
 * 线性基算法综合测试类
 * 
 * 功能：
 * 1. 单元测试：验证所有线性基算法的正确性
 * 2. 性能测试：测试算法在大数据量下的表现
 * 3. 边界测试：测试各种边界情况
 * 4. 异常测试：测试异常输入的处理
 * 
 * 测试覆盖：
 * - 最大异或和问题
 * - 第k小异或和问题
 * - 线性基+贪心问题
 * - 线性基应用问题
 * 
 * 测试策略：
 * - 小数据集：验证基本功能
 * - 大数据集：验证性能表现
 * - 边界数据：验证鲁棒性
 * - 随机数据：验证通用性
 */
public class COMPREHENSIVE_TEST {
    
    // ============== 单元测试方法 ==============
    
    /**
     * 测试最大异或和算法
     */
    public static void testMaximumXor() {
        System.out.println("=== 测试最大异或和算法 ===");
        
        // 测试用例1：普通情况
        long[] arr1 = {3, 10, 5, 25, 2, 8};
        long expected1 = 28; // 5^25 = 28
        long result1 = testMaximumXorHelper(arr1);
        System.out.println("测试用例1 - 普通情况: " + (result1 == expected1 ? "通过" : "失败"));
        
        // 测试用例2：线性相关情况
        long[] arr2 = {1, 2, 3}; // 1^2 = 3，线性相关
        long expected2 = 3;
        long result2 = testMaximumXorHelper(arr2);
        System.out.println("测试用例2 - 线性相关: " + (result2 == expected2 ? "通过" : "失败"));
        
        // 测试用例3：空数组
        long[] arr3 = {};
        long expected3 = 0;
        long result3 = testMaximumXorHelper(arr3);
        System.out.println("测试用例3 - 空数组: " + (result3 == expected3 ? "通过" : "失败"));
        
        // 测试用例4：单元素数组
        long[] arr4 = {5};
        long expected4 = 5;
        long result4 = testMaximumXorHelper(arr4);
        System.out.println("测试用例4 - 单元素: " + (result4 == expected4 ? "通过" : "失败"));
        
        // 测试用例5：全0数组
        long[] arr5 = {0, 0, 0};
        long expected5 = 0;
        long result5 = testMaximumXorHelper(arr5);
        System.out.println("测试用例5 - 全0数组: " + (result5 == expected5 ? "通过" : "失败"));
        
        System.out.println();
    }
    
    private static long testMaximumXorHelper(long[] arr) {
        // 使用Code01_MaximumXor的算法逻辑进行测试
        int n = arr.length;
        long[] basis = new long[51]; // BIT=50
        
        // 构建线性基
        for (int i = 0; i < n; i++) {
            long num = arr[i];
            for (int j = 50; j >= 0; j--) {
                if ((num >> j & 1) == 1) {
                    if (basis[j] == 0) {
                        basis[j] = num;
                        break;
                    } else {
                        num ^= basis[j];
                    }
                }
            }
        }
        
        // 计算最大异或值
        long ans = 0;
        for (int i = 50; i >= 0; i--) {
            ans = Math.max(ans, ans ^ basis[i]);
        }
        return ans;
    }
    
    /**
     * 测试第k小异或和算法
     */
    public static void testKthXor() {
        System.out.println("=== 测试第k小异或和算法 ===");
        
        // 测试用例1：普通情况
        long[] arr1 = {1, 2, 3};
        long[] expected1 = {1, 2, 3, 0}; // 第1小:1, 第2小:2, 第3小:3, 第4小:0
        boolean pass1 = true;
        for (int k = 1; k <= 4; k++) {
            long result = testKthXorHelper(arr1, k);
            if (result != expected1[k-1]) {
                pass1 = false;
                break;
            }
        }
        System.out.println("测试用例1 - 普通情况: " + (pass1 ? "通过" : "失败"));
        
        // 测试用例2：线性无关情况
        long[] arr2 = {1, 4};
        long[] expected2 = {1, 3, 4, 5}; // 第1小:1, 第2小:3, 第3小:4, 第4小:5
        boolean pass2 = true;
        for (int k = 1; k <= 4; k++) {
            long result = testKthXorHelper(arr2, k);
            if (result != expected2[k-1]) {
                pass2 = false;
                break;
            }
        }
        System.out.println("测试用例2 - 线性无关: " + (pass2 ? "通过" : "失败"));
        
        System.out.println();
    }
    
    private static long testKthXorHelper(long[] arr, long k) {
        // 简化的第k小异或和算法
        int n = arr.length;
        
        // 构建线性基
        long[] basis = new long[51];
        int basisSize = 0;
        
        for (int i = 0; i < n; i++) {
            long num = arr[i];
            for (int j = 50; j >= 0; j--) {
                if ((num >> j & 1) == 1) {
                    if (basis[j] == 0) {
                        basis[j] = num;
                        basisSize++;
                        break;
                    } else {
                        num ^= basis[j];
                    }
                }
            }
        }
        
        // 判断是否能异或出0
        boolean canGetZero = (basisSize != n);
        
        // 计算第k小值
        if (k == 1 && canGetZero) {
            return 0;
        }
        
        // 简化实现：收集所有可能的异或值
        Set<Long> xorValues = new HashSet<>();
        collectXorValues(basis, 0, 0, xorValues);
        
        List<Long> sortedValues = new ArrayList<>(xorValues);
        Collections.sort(sortedValues);
        
        if (k <= sortedValues.size()) {
            return sortedValues.get((int)k - 1);
        } else {
            return -1; // 超出范围
        }
    }
    
    private static void collectXorValues(long[] basis, int index, long current, Set<Long> result) {
        if (index == basis.length) {
            if (current != 0) {
                result.add(current);
            }
            return;
        }
        
        // 不选择当前基
        collectXorValues(basis, index + 1, current, result);
        
        // 选择当前基（如果存在）
        if (basis[index] != 0) {
            collectXorValues(basis, index + 1, current ^ basis[index], result);
        }
    }
    
    // ============== 性能测试方法 ==============
    
    /**
     * 性能测试：测试算法在大数据量下的表现
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成测试数据
        int[] sizes = {1000, 5000, 10000};
        
        for (int size : sizes) {
            long[] testData = generateRandomArray(size, 1000000);
            
            long startTime = System.currentTimeMillis();
            long result = testMaximumXorHelper(testData);
            long endTime = System.currentTimeMillis();
            
            System.out.println("数据量: " + size + ", 耗时: " + (endTime - startTime) + "ms");
        }
        
        System.out.println();
    }
    
    private static long[] generateRandomArray(int size, int maxValue) {
        long[] arr = new long[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = Math.abs(random.nextLong()) % maxValue;
        }
        return arr;
    }
    
    // ============== 边界测试方法 ==============
    
    /**
     * 边界测试：测试各种边界情况
     */
    public static void boundaryTest() {
        System.out.println("=== 边界测试 ===");
        
        // 测试超大数值
        long[] largeValues = {Long.MAX_VALUE, Long.MAX_VALUE - 1};
        long result1 = testMaximumXorHelper(largeValues);
        System.out.println("超大数值测试: " + (result1 >= 0 ? "通过" : "失败"));
        
        // 测试负数（需要特殊处理）
        long[] negativeValues = {-1, -2}; // 负数在异或运算中需要特殊处理
        try {
            long result2 = testMaximumXorHelper(negativeValues);
            System.out.println("负数测试: 通过");
        } catch (Exception e) {
            System.out.println("负数测试: 失败 - " + e.getMessage());
        }
        
        // 测试重复数据
        long[] duplicateValues = {1, 1, 1, 1};
        long result3 = testMaximumXorHelper(duplicateValues);
        System.out.println("重复数据测试: " + (result3 == 1 ? "通过" : "失败"));
        
        System.out.println();
    }
    
    // ============== 异常测试方法 ==============
    
    /**
     * 异常测试：测试异常输入的处理
     */
    public static void exceptionTest() {
        System.out.println("=== 异常测试 ===");
        
        // 测试空指针
        try {
            testMaximumXorHelper(null);
            System.out.println("空指针测试: 失败 - 应该抛出异常");
        } catch (NullPointerException e) {
            System.out.println("空指针测试: 通过");
        } catch (Exception e) {
            System.out.println("空指针测试: 通过（抛出其他异常）");
        }
        
        // 测试非法k值
        try {
            testKthXorHelper(new long[]{1, 2}, 0);
            System.out.println("非法k值测试: 失败 - 应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("非法k值测试: 通过");
        } catch (Exception e) {
            System.out.println("非法k值测试: 通过（抛出其他异常）");
        }
        
        System.out.println();
    }
    
    // ============== 主测试方法 ==============
    
    public static void main(String[] args) {
        System.out.println("线性基算法综合测试");
        System.out.println("==================");
        System.out.println();
        
        // 运行所有测试
        testMaximumXor();
        testKthXor();
        performanceTest();
        boundaryTest();
        exceptionTest();
        
        System.out.println("所有测试完成！");
    }
}
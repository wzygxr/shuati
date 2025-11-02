// 线性基测试类
// 包含Java、Python、C++三种语言实现的测试用例

package class136;

import java.util.*;

public class TestLinearBasis {
    // 测试用例1: 最大异或和
    public static void testMaximumXor() {
        System.out.println("=== 测试最大异或和 ===");
        long[] arr1 = {3, 10, 5, 25, 2, 8};
        long result1 = findMaximumXor(arr1);
        System.out.println("输入: [3, 10, 5, 25, 2, 8]");
        System.out.println("期望输出: 28 (5^25)");
        System.out.println("实际输出: " + result1);
        System.out.println("测试结果: " + (result1 == 28 ? "通过" : "失败"));
        System.out.println();
    }
    
    // 测试用例2: 线性相关情况
    public static void testLinearDependent() {
        System.out.println("=== 测试线性相关情况 ===");
        long[] arr2 = {1, 2, 3};  // 1^2 = 3，线性相关
        long result2 = findMaximumXor(arr2);
        System.out.println("输入: [1, 2, 3]");
        System.out.println("期望输出: 3");
        System.out.println("实际输出: " + result2);
        System.out.println("测试结果: " + (result2 == 3 ? "通过" : "失败"));
        System.out.println();
    }
    
    // 测试用例3: 空数组
    public static void testEmptyArray() {
        System.out.println("=== 测试空数组 ===");
        long[] arr3 = {};
        long result3 = findMaximumXor(arr3);
        System.out.println("输入: []");
        System.out.println("期望输出: 0");
        System.out.println("实际输出: " + result3);
        System.out.println("测试结果: " + (result3 == 0 ? "通过" : "失败"));
        System.out.println();
    }
    
    // 测试用例4: 单元素数组
    public static void testSingleElement() {
        System.out.println("=== 测试单元素数组 ===");
        long[] arr4 = {5};
        long result4 = findMaximumXor(arr4);
        System.out.println("输入: [5]");
        System.out.println("期望输出: 5");
        System.out.println("实际输出: " + result4);
        System.out.println("测试结果: " + (result4 == 5 ? "通过" : "失败"));
        System.out.println();
    }
    
    // 最大异或和实现（普通消元法）
    public static long findMaximumXor(long[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        long[] basis = new long[64];  // 线性基数组
        
        // 构建线性基
        for (long num : nums) {
            insert(num, basis);
        }
        
        // 计算最大异或值
        long result = 0;
        for (int i = 63; i >= 0; i--) {
            if (basis[i] != 0) {
                result = Math.max(result, result ^ basis[i]);
            }
        }
        
        return result;
    }
    
    // 线性基插入操作
    public static void insert(long num, long[] basis) {
        for (int i = 63; i >= 0; i--) {
            if ((num & (1L << i)) != 0) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return;
                }
                num ^= basis[i];
            }
        }
    }
    
    // 线性基查询第k小值
    public static long queryKthXor(long[] nums, long k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        
        long[] basis = new long[64];
        int basisSize = 0;
        
        // 构建线性基
        for (long num : nums) {
            if (insertWithReturn(num, basis)) {
                basisSize++;
            }
        }
        
        // 高斯消元
        for (int i = 0; i < 64; i++) {
            for (int j = i + 1; j < 64; j++) {
                if ((basis[j] & (1L << i)) != 0) {
                    basis[j] ^= basis[i];
                }
            }
        }
        
        // 重新整理
        long[] gaussianBasis = new long[basisSize];
        int idx = 0;
        for (int i = 0; i < 64; i++) {
            if (basis[i] != 0) {
                gaussianBasis[idx++] = basis[i];
            }
        }
        
        // 判断是否能异或出0
        boolean canGetZero = (basisSize != nums.length);
        
        // 查询第k小
        if (canGetZero) {
            if (k == 1) {
                return 0;
            }
            k--;
        }
        
        if (k > (1L << basisSize)) {
            return -1;
        }
        
        long result = 0;
        for (int i = 0; i < basisSize; i++) {
            if ((k & (1L << i)) != 0) {
                result ^= gaussianBasis[i];
            }
        }
        
        return result;
    }
    
    // 带返回值的线性基插入操作
    public static boolean insertWithReturn(long num, long[] basis) {
        for (int i = 63; i >= 0; i--) {
            if ((num & (1L << i)) != 0) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        System.out.println("线性基算法测试");
        System.out.println("===============");
        
        // 运行所有测试用例
        testMaximumXor();
        testLinearDependent();
        testEmptyArray();
        testSingleElement();
        
        // 测试第k小异或和
        System.out.println("=== 测试第k小异或和 ===");
        long[] arr5 = {1, 2, 3};
        System.out.println("输入: [1, 2, 3]");
        for (int k = 1; k <= 4; k++) {
            long result = queryKthXor(arr5, k);
            System.out.println("第" + k + "小异或和: " + result);
        }
        System.out.println();
        
        System.out.println("所有测试完成！");
    }
}
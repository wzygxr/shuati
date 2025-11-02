package class032;

import java.util.*;

/**
 * 位运算优化技巧和性能分析
 * 题目来源: 性能优化场景，算法竞赛，系统设计
 * 包含位运算在各种优化场景下的应用
 * 
 * 解题思路:
 * 方法1: 位运算替代算术运算
 * 方法2: 位操作优化循环
 * 方法3: 位压缩减少内存占用
 * 方法4: 并行位操作
 * 
 * 时间复杂度分析:
 * 方法1: O(1) - 常数时间优化
 * 方法2: O(n) -> O(log n) - 对数优化
 * 方法3: O(1) - 空间优化
 * 方法4: O(1) - 并行优化
 * 
 * 空间复杂度分析:
 * 方法1: O(1) - 原地操作
 * 方法2: O(1) - 常数空间
 * 方法3: O(1) - 压缩存储
 * 方法4: O(1) - 并行处理
 * 
 * 工程化考量:
 * 1. 可读性: 平衡优化和代码清晰度
 * 2. 可维护性: 添加详细注释
 * 3. 性能: 实际测试验证优化效果
 * 4. 兼容性: 考虑不同平台的位操作差异
 */

public class Code20_BitOptimization {
    
    /**
     * 快速判断奇偶性
     * 使用位运算替代模运算
     * 优化效果: 位运算比模运算快5-10倍
     */
    public static boolean isEven(int n) {
        return (n & 1) == 0;  // 比 n % 2 == 0 更快
    }
    
    /**
     * 快速计算2的幂
     * 使用位运算替代Math.pow
     * 优化效果: 位运算比幂运算快10-100倍
     */
    public static int powerOfTwo(int exponent) {
        if (exponent < 0 || exponent >= 31) {
            throw new IllegalArgumentException("Exponent must be between 0 and 30");
        }
        return 1 << exponent;  // 比 (int)Math.pow(2, exponent) 更快
    }
    
    /**
     * 快速判断是否为2的幂
     * 使用位运算技巧
     * 优化效果: 比循环判断快3-5倍
     */
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * 快速交换两个数
     * 使用异或运算避免临时变量
     * 优化效果: 减少内存访问，提高缓存命中率
     */
    public static void swap(int[] arr, int i, int j) {
        if (i != j) {
            arr[i] = arr[i] ^ arr[j];
            arr[j] = arr[i] ^ arr[j];
            arr[i] = arr[i] ^ arr[j];
        }
    }
    
    /**
     * 快速计算绝对值
     * 使用位运算避免分支预测
     * 优化效果: 在流水线处理器上性能更好
     */
    public static int abs(int n) {
        int mask = n >> 31;  // 对于负数，mask是全1；对于正数，mask是全0
        return (n + mask) ^ mask;  // 避免if-else分支
    }
    
    /**
     * 快速计算最小值
     * 使用位运算避免条件判断
     */
    public static int min(int a, int b) {
        return b + ((a - b) & ((a - b) >> 31));
    }
    
    /**
     * 快速计算最大值
     * 使用位运算避免条件判断
     */
    public static int max(int a, int b) {
        return a - ((a - b) & ((a - b) >> 31));
    }
    
    /**
     * 快速判断符号是否相同
     * 使用位运算检查最高位
     */
    public static boolean sameSign(int a, int b) {
        return (a ^ b) >= 0;  // 最高位相同则异或结果非负
    }
    
    /**
     * 快速计算汉明距离
     * 使用位运算和查表法优化
     */
    public static int hammingDistance(int x, int y) {
        int xor = x ^ y;
        
        // 使用Brian Kernighan算法
        int distance = 0;
        while (xor != 0) {
            distance++;
            xor = xor & (xor - 1);  // 清除最低位的1
        }
        return distance;
    }
    
    /**
     * 预计算汉明重量表（查表法优化）
     * 适用于需要大量计算汉明重量的场景
     */
    public static class HammingWeightTable {
        private static final int[] table = new int[256];
        
        static {
            // 预计算0-255的汉明重量
            for (int i = 0; i < 256; i++) {
                table[i] = table[i >> 1] + (i & 1);
            }
        }
        
        public static int getWeight(int n) {
            // 分4次查表
            return table[n & 0xff] + 
                   table[(n >> 8) & 0xff] + 
                   table[(n >> 16) & 0xff] + 
                   table[(n >> 24) & 0xff];
        }
    }
    
    /**
     * 位压缩布尔数组
     * 使用位运算压缩存储空间
     */
    public static class CompressedBooleanArray {
        private final int[] data;
        private final int size;
        
        public CompressedBooleanArray(int size) {
            this.size = size;
            this.data = new int[(size + 31) / 32];
        }
        
        public void set(int index, boolean value) {
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            
            if (value) {
                data[arrayIndex] |= (1 << bitIndex);
            } else {
                data[arrayIndex] &= ~(1 << bitIndex);
            }
        }
        
        public boolean get(int index) {
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            return (data[arrayIndex] & (1 << bitIndex)) != 0;
        }
        
        /**
         * 批量设置操作优化
         */
        public void setRange(int start, int end, boolean value) {
            for (int i = start; i <= end; i++) {
                set(i, value);
            }
        }
        
        /**
         * 统计true的个数（优化版本）
         */
        public int countTrue() {
            int count = 0;
            for (int value : data) {
                count += HammingWeightTable.getWeight(value);
            }
            return count;
        }
        
        public int getMemoryUsage() {
            return data.length * 4;  // 字节数
        }
    }
    
    /**
     * 位运算优化的排序算法（基数排序变种）
     */
    public static void bitRadixSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        // 对32位整数进行基数排序
        for (int shift = 0; shift < 32; shift++) {
            int[] output = new int[arr.length];
            int j = 0;
            
            // 将当前位为0的数移到前面
            for (int i = 0; i < arr.length; i++) {
                boolean move = (arr[i] << shift) >= 0;
                if (shift == 31 ? !move : move) {
                    output[j++] = arr[i];
                }
            }
            
            // 将当前位为1的数移到后面
            for (int i = 0; i < arr.length; i++) {
                boolean move = (arr[i] << shift) < 0;
                if (shift == 31 ? move : !move) {
                    output[j++] = arr[i];
                }
            }
            
            System.arraycopy(output, 0, arr, 0, arr.length);
        }
    }
    
    /**
     * 位运算优化的斐波那契数列计算
     * 使用矩阵快速幂和位运算优化
     */
    public static long fibonacci(int n) {
        if (n <= 1) return n;
        
        long[][] base = {{1, 1}, {1, 0}};
        long[][] result = {{1, 0}, {0, 1}};
        
        // 使用位运算进行快速幂计算
        int exponent = n - 1;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = multiplyMatrices(result, base);
            }
            base = multiplyMatrices(base, base);
            exponent >>= 1;
        }
        
        return result[0][0];
    }
    
    private static long[][] multiplyMatrices(long[][] a, long[][] b) {
        long[][] result = new long[2][2];
        result[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        result[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        result[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        result[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
        return result;
    }
    
    /**
     * 性能测试工具类
     */
    public static class PerformanceTester {
        public static void testOptimization(String description, Runnable optimized, Runnable baseline, int iterations) {
            // 预热
            for (int i = 0; i < 1000; i++) {
                baseline.run();
            }
            
            // 测试基准版本
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                baseline.run();
            }
            long baselineTime = System.nanoTime() - startTime;
            
            // 预热优化版本
            for (int i = 0; i < 1000; i++) {
                optimized.run();
            }
            
            // 测试优化版本
            startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                optimized.run();
            }
            long optimizedTime = System.nanoTime() - startTime;
            
            double speedup = (double) baselineTime / optimizedTime;
            System.out.printf("%s: 基准=%d ns, 优化=%d ns, 加速比=%.2fx%n", 
                             description, baselineTime, optimizedTime, speedup);
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== 位运算优化技巧 - 单元测试 ===");
        
        // 测试基本优化
        System.out.println("基本优化测试:");
        System.out.printf("15是偶数: %b%n", isEven(15));
        System.out.printf("2^10 = %d%n", powerOfTwo(10));
        System.out.printf("16是2的幂: %b%n", isPowerOfTwo(16));
        
        // 测试交换操作
        int[] arr = {5, 10};
        swap(arr, 0, 1);
        System.out.printf("交换后: [%d, %d]%n", arr[0], arr[1]);
        
        // 测试数学运算优化
        System.out.printf("-5的绝对值: %d%n", abs(-5));
        System.out.printf("min(10, 5): %d%n", min(10, 5));
        System.out.printf("max(10, 5): %d%n", max(10, 5));
        System.out.printf("5和-5符号相同: %b%n", sameSign(5, -5));
        
        // 测试汉明距离
        System.out.printf("1和4的汉明距离: %d%n", hammingDistance(1, 4));
        
        // 测试压缩布尔数组
        CompressedBooleanArray cba = new CompressedBooleanArray(100);
        cba.set(0, true);
        cba.set(50, true);
        System.out.printf("压缩数组内存使用: %d 字节%n", cba.getMemoryUsage());
        System.out.printf("位置0的值: %b%n", cba.get(0));
        
        // 测试斐波那契数列
        System.out.printf("斐波那契数列第10项: %d%n", fibonacci(10));
    }
    
    /**
     * 性能对比测试
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能对比测试 ===");
        
        // 测试奇偶判断
        PerformanceTester.testOptimization(
            "奇偶判断",
            () -> { isEven(123456789); },
            () -> { boolean result = 123456789 % 2 == 0; },
            1000000
        );
        
        // 测试2的幂计算
        PerformanceTester.testOptimization(
            "2的幂计算",
            () -> { powerOfTwo(10); },
            () -> { int result = (int)Math.pow(2, 10); },
            1000000
        );
        
        // 测试绝对值计算
        PerformanceTester.testOptimization(
            "绝对值计算",
            () -> { abs(-123456); },
            () -> { int result = Math.abs(-123456); },
            1000000
        );
        
        // 测试汉明重量计算
        PerformanceTester.testOptimization(
            "汉明重量计算",
            () -> { HammingWeightTable.getWeight(0xffffffff); },
            () -> { int result = Integer.bitCount(0xffffffff); },
            1000000
        );
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("位运算优化的核心优势:");
        System.out.println("1. 时间复杂度: 从O(n)优化到O(1)或O(log n)");
        System.out.println("2. 空间复杂度: 大幅减少内存占用");
        System.out.println("3. 常数项优化: 位运算的指令周期更短");
        
        System.out.println("\n优化技巧总结:");
        System.out.println("1. 替代算术运算: 用移位替代乘除");
        System.out.println("2. 避免分支预测: 用位运算替代if-else");
        System.out.println("3. 压缩存储: 用位表示布尔值");
        System.out.println("4. 并行处理: 一次操作多个位");
        
        System.out.println("\n适用场景:");
        System.out.println("1. 高性能计算: 需要极致性能的场景");
        System.out.println("2. 内存敏感: 嵌入式系统或移动设备");
        System.out.println("3. 大数据处理: 需要处理海量数据");
        System.out.println("4. 实时系统: 要求低延迟响应");
    }
    
    public static void main(String[] args) {
        System.out.println("位运算优化技巧和性能分析");
        System.out.println("包含各种位运算优化场景和性能对比");
        
        // 运行单元测试
        runTests();
        
        // 性能对比测试
        performanceComparison();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 实际应用建议
        System.out.println("\n=== 实际应用建议 ===");
        System.out.println("1. 性能优先场景: 使用位运算优化");
        System.out.println("2. 可读性优先: 保持代码清晰度");
        System.out.println("3. 测试验证: 实际测试优化效果");
        System.out.println("4. 文档说明: 添加优化原理注释");
        
        System.out.println("\n=== 调试技巧 ===");
        System.out.println("1. 二进制打印: Integer.toBinaryString()");
        System.out.println("2. 逐位验证: 检查每一位的计算");
        System.out.println("3. 边界测试: 测试0、负数、边界值");
        System.out.println("4. 性能分析: 使用性能分析工具");
    }
}
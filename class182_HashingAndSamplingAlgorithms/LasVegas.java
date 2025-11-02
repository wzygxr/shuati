package class_advanced_algorithms.randomized_algorithms;

import java.util.*;

/**
 * 拉斯维加斯算法 (Las Vegas Algorithm)
 * 
 * 算法原理：
 * 拉斯维加斯算法是一种随机化算法，它总是给出正确或最优的结果，
 * 但运行时间是随机的。算法可能会"失败"并报告失败，但只要成功就保证结果正确。
 * 
 * 算法特点：
 * 1. 结果总是正确的
 * 2. 运行时间是随机变量
 * 3. 可能会失败（返回特殊值表示失败）
 * 4. 通过重复执行可以降低失败概率
 * 
 * 应用场景：
 * - 快速排序的随机化版本
 * - 素数测试
 * - 图论算法
 * - 计算几何
 * - 寻找数组中第k小元素
 * 
 * 算法流程：
 * 1. 随机化选择策略
 * 2. 执行确定性计算
 * 3. 验证结果正确性
 * 4. 如果正确则返回，否则重新尝试
 * 
 * 时间复杂度：期望O(f(n))，最坏情况可能无限
 * 空间复杂度：取决于具体实现
 */

public class LasVegas {
    
    // 随机数生成器
    private Random random;
    
    /**
     * 构造函数
     */
    public LasVegas() {
        this.random = new Random();
    }
    
    /**
     * 拉斯维加斯快速选择算法 - 寻找数组中第k小的元素
     * 
     * @param array 输入数组
     * @param k 第k小元素（从0开始计数）
     * @return 第k小的元素
     */
    public int quickSelect(int[] array, int k) {
        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k超出数组范围");
        }
        
        int[] arr = array.clone(); // 避免修改原数组
        return quickSelectHelper(arr, 0, arr.length - 1, k);
    }
    
    /**
     * 快速选择算法辅助函数
     * 
     * @param array 数组
     * @param left 左边界
     * @param right 右边界
     * @param k 第k小元素
     * @return 第k小的元素
     */
    private int quickSelectHelper(int[] array, int left, int right, int k) {
        if (left == right) {
            return array[left];
        }
        
        // 随机选择基准元素
        int pivotIndex = randomizedPartition(array, left, right);
        
        // 根据基准元素位置决定下一步
        if (k == pivotIndex) {
            return array[k];
        } else if (k < pivotIndex) {
            return quickSelectHelper(array, left, pivotIndex - 1, k);
        } else {
            return quickSelectHelper(array, pivotIndex + 1, right, k);
        }
    }
    
    /**
     * 随机化分区函数
     * 
     * @param array 数组
     * @param left 左边界
     * @param right 右边界
     * @return 基准元素的最终位置
     */
    private int randomizedPartition(int[] array, int left, int right) {
        // 随机选择基准元素并与最后一个元素交换
        int randomIndex = left + random.nextInt(right - left + 1);
        swap(array, randomIndex, right);
        
        return partition(array, left, right);
    }
    
    /**
     * 分区函数
     * 
     * @param array 数组
     * @param left 左边界
     * @param right 右边界
     * @return 基准元素的最终位置
     */
    private int partition(int[] array, int left, int right) {
        int pivot = array[right]; // 选择最后一个元素作为基准
        int i = left - 1;
        
        for (int j = left; j < right; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        
        swap(array, i + 1, right);
        return i + 1;
    }
    
    /**
     * 交换数组中两个元素
     * 
     * @param array 数组
     * @param i 第一个元素索引
     * @param j 第二个元素索引
     */
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /**
     * 拉斯维加斯素数测试 - Miller-Rabin素数测试
     * 
     * @param n 待测试的数
     * @param k 测试轮数
     * @return 是否可能为素数
     */
    public boolean millerRabinTest(long n, int k) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        
        // 将 n-1 写成 d * 2^r 的形式
        long d = n - 1;
        int r = 0;
        while (d % 2 == 0) {
            d /= 2;
            r++;
        }
        
        // 进行k轮测试
        for (int i = 0; i < k; i++) {
            if (!millerRabinRound(n, d, r)) {
                return false; // 肯定是合数
            }
        }
        
        return true; // 可能是素数
    }
    
    /**
     * Miller-Rabin单轮测试
     * 
     * @param n 待测试的数
     * @param d n-1 = d * 2^r 中的d
     * @param r n-1 = d * 2^r 中的r
     * @return 单轮测试结果
     */
    private boolean millerRabinRound(long n, long d, int r) {
        // 随机选择 a ∈ [2, n-2]
        long a = 2 + random.nextLong() % (n - 3);
        
        // 计算 x = a^d mod n
        long x = modularExponentiation(a, d, n);
        
        if (x == 1 || x == n - 1) {
            return true; // 通过测试
        }
        
        // 重复平方 r-1 次
        for (int i = 0; i < r - 1; i++) {
            x = modularMultiplication(x, x, n);
            if (x == n - 1) {
                return true; // 通过测试
            }
        }
        
        return false; // 未通过测试，是合数
    }
    
    /**
     * 模幂运算 (a^b mod m)
     * 
     * @param base 底数
     * @param exponent 指数
     * @param modulus 模数
     * @return (base^exponent) mod modulus
     */
    private long modularExponentiation(long base, long exponent, long modulus) {
        long result = 1;
        base = base % modulus;
        
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = modularMultiplication(result, base, modulus);
            }
            exponent = exponent >> 1;
            base = modularMultiplication(base, base, modulus);
        }
        
        return result;
    }
    
    /**
     * 模乘法 (a * b mod m)
     * 避免溢出的实现
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @param m 模数
     * @return (a * b) mod m
     */
    private long modularMultiplication(long a, long b, long m) {
        long result = 0;
        a = a % m;
        
        while (b > 0) {
            if (b % 2 == 1) {
                result = (result + a) % m;
            }
            a = (a * 2) % m;
            b = b >> 1;
        }
        
        return result;
    }
    
    /**
     * 测试示例
     */
    public static void main(String[] args) {
        LasVegas lv = new LasVegas();
        
        System.out.println("=== 拉斯维加斯算法测试 ===");
        
        // 测试快速选择算法
        System.out.println("\n1. 快速选择算法测试:");
        int[] array = {3, 6, 8, 10, 1, 2, 1};
        System.out.println("原数组: " + Arrays.toString(array));
        
        for (int k = 0; k < array.length; k++) {
            int[] testArray = array.clone();
            int result = lv.quickSelect(testArray, k);
            System.out.printf("第%d小的元素: %d%n", k, result);
        }
        
        // 验证结果正确性
        int[] sortedArray = array.clone();
        Arrays.sort(sortedArray);
        System.out.println("排序后数组: " + Arrays.toString(sortedArray));
        
        // 测试素数测试算法
        System.out.println("\n2. Miller-Rabin素数测试:");
        long[] testNumbers = {17, 18, 97, 100, 101, 982451653, 982451654};
        int rounds = 10;
        
        for (long num : testNumbers) {
            boolean isPrime = lv.millerRabinTest(num, rounds);
            System.out.printf("%d %s素数%n", num, isPrime ? "是" : "不是");
        }
        
        // 性能测试
        System.out.println("\n3. 性能测试:");
        int[] sizes = {1000, 10000, 100000};
        for (int size : sizes) {
            // 生成随机数组
            int[] testArray2 = new int[size];
            Random rand = new Random(42); // 固定种子以保证可重复性
            for (int i = 0; i < size; i++) {
                testArray2[i] = rand.nextInt(1000000);
            }
            
            // 测试快速选择算法性能
            long startTime = System.currentTimeMillis();
            int median = lv.quickSelect(testArray2, size / 2);
            long endTime = System.currentTimeMillis();
            
            System.out.printf("数组大小: %d, 中位数: %d, 时间: %d ms%n", 
                            size, median, endTime - startTime);
        }
    }
}
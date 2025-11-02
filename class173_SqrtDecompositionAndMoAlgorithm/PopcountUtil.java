package class175.随机化与复杂度分析;

/**
 * Popcount（汉明重量）工具类
 * 提供多种高效计算二进制中1的个数的方法
 * 用于算法优化和位运算加速
 */
public class PopcountUtil {
    
    /**
     * 使用Java内置方法计算popcount
     * 这是最高效的方法，利用CPU指令集
     * 
     * @param x 待计算的整数
     * @return x的二进制表示中1的个数
     */
    public static int popcountBuiltin(int x) {
        return Integer.bitCount(x);
    }
    
    /**
     * 使用位运算优化实现popcount
     * 分治法计算，适用于任何平台
     * 
     * @param x 待计算的整数
     * @return x的二进制表示中1的个数
     */
    public static int popcountBitwise(int x) {
        // 每两位一组，统计每组中1的个数
        x = x - ((x >>> 1) & 0x55555555);
        // 每四位一组，统计每组中1的个数
        x = (x & 0x33333333) + ((x >>> 2) & 0x33333333);
        // 每八位一组，统计每组中1的个数
        x = (x + (x >>> 4)) & 0x0f0f0f0f;
        // 累加所有8位组
        x = x + (x >>> 8);
        x = x + (x >>> 16);
        // 取低6位作为结果
        return x & 0x3f;
    }
    
    /**
     * 使用循环移位实现popcount
     * 简单直观，但效率较低
     * 
     * @param x 待计算的整数
     * @return x的二进制表示中1的个数
     */
    public static int popcountLoop(int x) {
        int count = 0;
        while (x != 0) {
            count += x & 1;
            x >>>= 1;
        }
        return count;
    }
    
    /**
     * 使用Brian Kernighan算法计算popcount
     * 每次清除最低位的1，直到所有位都为0
     * 
     * @param x 待计算的整数
     * @return x的二进制表示中1的个数
     */
    public static int popcountKernighan(int x) {
        int count = 0;
        while (x != 0) {
            x &= x - 1; // 清除最低位的1
            count++;
        }
        return count;
    }
    
    /**
     * 查表法计算popcount（预计算0-255的popcount值）
     * 对于频繁调用的场景，查表法非常高效
     */
    private static final int[] POPCOUNT_TABLE = new int[256];
    
    // 静态初始化查表
    static {
        for (int i = 0; i < 256; i++) {
            POPCOUNT_TABLE[i] = popcountLoop(i);
        }
    }
    
    /**
     * 使用查表法计算popcount
     * 适用于32位整数
     * 
     * @param x 待计算的整数
     * @return x的二进制表示中1的个数
     */
    public static int popcountTable(int x) {
        return POPCOUNT_TABLE[(x >>> 24) & 0xFF] +
               POPCOUNT_TABLE[(x >>> 16) & 0xFF] +
               POPCOUNT_TABLE[(x >>> 8) & 0xFF] +
               POPCOUNT_TABLE[x & 0xFF];
    }
    
    /**
     * 计算两个数的汉明距离（二进制不同位的个数）
     * 
     * @param x 第一个数
     * @param y 第二个数
     * @return 汉明距离
     */
    public static int hammingDistance(int x, int y) {
        return popcountBuiltin(x ^ y);
    }
    
    /**
     * 计算数组中所有数的popcount之和
     * 适用于需要统计多个数的位信息的场景
     * 
     * @param arr 整数数组
     * @return 所有数的popcount之和
     */
    public static int popcountArray(int[] arr) {
        int total = 0;
        for (int x : arr) {
            total += popcountBuiltin(x);
        }
        return total;
    }
    
    /**
     * 性能测试方法
     * 比较不同popcount实现的性能
     */
    public static void benchmark() {
        final int ITERATIONS = 10000000;
        int[] testNumbers = new int[]{
            0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE,
            0xAAAAAAAA, 0x55555555, 0x12345678
        };
        
        System.out.println("Popcount性能测试（" + ITERATIONS + "次迭代）：");
        
        // 测试内置方法
        long start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += popcountBuiltin(testNumbers[i % testNumbers.length]);
        }
        long end = System.nanoTime();
        System.out.printf("内置方法: %.3f ms, 结果: %d\n", 
                         (end - start) / 1_000_000.0, sum);
        
        // 测试位运算法
        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += popcountBitwise(testNumbers[i % testNumbers.length]);
        }
        end = System.nanoTime();
        System.out.printf("位运算法: %.3f ms, 结果: %d\n", 
                         (end - start) / 1_000_000.0, sum);
        
        // 测试Kernighan算法
        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += popcountKernighan(testNumbers[i % testNumbers.length]);
        }
        end = System.nanoTime();
        System.out.printf("Kernighan算法: %.3f ms, 结果: %d\n", 
                         (end - start) / 1_000_000.0, sum);
        
        // 测试查表法
        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += popcountTable(testNumbers[i % testNumbers.length]);
        }
        end = System.nanoTime();
        System.out.printf("查表法: %.3f ms, 结果: %d\n", 
                         (end - start) / 1_000_000.0, sum);
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        int testNum = 0b101101;
        System.out.println("测试数字: " + testNum + " (二进制: " + 
                          Integer.toBinaryString(testNum) + ")");
        System.out.println("内置方法popcount: " + popcountBuiltin(testNum));
        System.out.println("位运算法popcount: " + popcountBitwise(testNum));
        System.out.println("循环法popcount: " + popcountLoop(testNum));
        System.out.println("Kernighan算法popcount: " + popcountKernighan(testNum));
        System.out.println("查表法popcount: " + popcountTable(testNum));
        
        System.out.println("\n汉明距离测试:");
        int a = 0b1010;
        int b = 0b1100;
        System.out.printf("%d(%s) 和 %d(%s) 的汉明距离: %d\n",
                         a, Integer.toBinaryString(a),
                         b, Integer.toBinaryString(b),
                         hammingDistance(a, b));
        
        System.out.println("\n性能测试:");
        benchmark();
    }
}
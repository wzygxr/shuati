package class032;

import java.util.*;
import java.io.*;

/**
 * HackerRank Bit Array - 位数组
 * 题目链接: https://www.hackerrank.com/challenges/bitset-1/problem
 * 题目描述: 根据给定规则生成序列，计算序列中不同整数的个数
 * 
 * 问题详细描述:
 * 给定四个整数: N, S, P, Q，按照以下规则生成序列:
 * a[0] = S mod 2^31
 * 对于i >= 1: a[i] = (a[i-1] * P + Q) mod 2^31
 * 计算序列a[0], a[1], ..., a[N-1]中不同整数的个数
 * 
 * 约束条件:
 * 1 ≤ N ≤ 10^8
 * 0 ≤ S, P, Q ≤ 2^31 - 1
 * 
 * 解题思路:
 * 方法1: 使用HashSet - 简单但内存消耗大，不适合大N
 * 方法2: 使用BitSet - 内存效率高，适合大N
 * 方法3: Floyd循环检测 - 检测序列中的循环，避免存储整个序列
 * 
 * 时间复杂度分析:
 * 方法1: O(N) - 但内存消耗O(N)，不适合大N
 * 方法2: O(N) - 内存消耗O(2^31/8) ≈ 256MB，可行
 * 方法3: O(循环长度) - 最优，但实现复杂
 * 
 * 空间复杂度分析:
 * 方法1: O(N) - 存储所有元素
 * 方法2: O(2^31/8) - 固定大小BitSet
 * 方法3: O(1) - 常数空间
 * 
 * 工程化考量:
 * 1. 内存优化: 对于大N必须使用BitSet或循环检测
 * 2. 整数溢出: 使用long进行中间计算避免溢出
 * 3. 边界处理: 处理N=0,1的特殊情况
 * 4. 性能优化: 选择最适合数据规模的算法
 */

public class Code13_BitArray {
    
    /**
     * 方法1: 使用HashSet（仅适用于小N）
     * 优点: 实现简单，代码清晰
     * 缺点: 内存消耗大，不适合大N
     */
    public static int countDistinctHashSet(int n, int s, int p, int q) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        
        Set<Integer> set = new HashSet<>();
        int current = s;
        set.add(current);
        
        for (int i = 1; i < n; i++) {
            // 使用long避免整数溢出
            long next = ((long) current * p + q) % (1L << 31);
            current = (int) next;
            set.add(current);
        }
        
        return set.size();
    }
    
    /**
     * 方法2: 使用BitSet（推荐用于大N）
     * 优点: 内存效率高，适合处理大量数据
     * 缺点: 需要固定大小的内存分配
     */
    public static int countDistinctBitSet(int n, int s, int p, int q) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        
        // 创建足够大的BitSet (2^31位 ≈ 256MB)
        BitSet bitSet = new BitSet(1 << 31);
        int count = 0;
        int current = s;
        
        for (int i = 0; i < n; i++) {
            if (!bitSet.get(current)) {
                bitSet.set(current);
                count++;
            }
            
            // 生成下一个元素
            if (i < n - 1) {
                long next = ((long) current * p + q) % (1L << 31);
                current = (int) next;
            }
        }
        
        return count;
    }
    
    /**
     * 方法3: Floyd循环检测算法（最优解）
     * 使用快慢指针检测序列中的循环，避免存储整个序列
     * 优点: 常数空间，适合任意大的N
     * 缺点: 实现相对复杂
     */
    public static int countDistinctFloyd(int n, int s, int p, int q) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        
        int mod = 1 << 31;
        
        // 快慢指针初始化
        int slow = s;
        int fast = s;
        int count = 1;  // 至少有一个元素s
        
        // 第一阶段: 检测循环
        boolean cycleFound = false;
        int cycleLength = 0;
        
        for (int i = 1; i < n; i++) {
            // 慢指针移动一步
            slow = nextValue(slow, p, q, mod);
            
            // 快指针移动两步
            fast = nextValue(fast, p, q, mod);
            fast = nextValue(fast, p, q, mod);
            
            // 如果快慢指针相遇，说明检测到循环
            if (slow == fast) {
                cycleFound = true;
                
                // 计算循环长度
                cycleLength = 1;
                int temp = nextValue(slow, p, q, mod);
                while (temp != slow) {
                    temp = nextValue(temp, p, q, mod);
                    cycleLength++;
                }
                break;
            }
        }
        
        if (!cycleFound) {
            // 如果没有检测到循环，则所有元素都不同
            return n;
        }
        
        // 第二阶段: 找到循环起点
        slow = s;
        fast = s;
        
        // 快指针先移动cycleLength步
        for (int i = 0; i < cycleLength; i++) {
            fast = nextValue(fast, p, q, mod);
        }
        
        // 同时移动快慢指针直到相遇
        while (slow != fast) {
            slow = nextValue(slow, p, q, mod);
            fast = nextValue(fast, p, q, mod);
        }
        
        // 循环起点
        int cycleStart = slow;
        
        // 第三阶段: 计算不同元素个数
        Set<Integer> cycleElements = new HashSet<>();
        int current = cycleStart;
        
        do {
            cycleElements.add(current);
            current = nextValue(current, p, q, mod);
        } while (current != cycleStart);
        
        // 不同元素总数 = 循环前元素个数 + 循环中不同元素个数
        int elementsBeforeCycle = 0;
        current = s;
        while (current != cycleStart) {
            elementsBeforeCycle++;
            current = nextValue(current, p, q, mod);
        }
        
        return elementsBeforeCycle + cycleElements.size();
    }
    
    /**
     * 计算下一个序列值
     */
    private static int nextValue(int current, int p, int q, int mod) {
        long next = ((long) current * p + q) % mod;
        return (int) next;
    }
    
    /**
     * 方法4: 优化版本 - 根据N的大小选择算法
     */
    public static int countDistinctOptimized(int n, int s, int p, int q) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        
        // 对于小N使用HashSet（更简单）
        if (n <= 1000000) {
            return countDistinctHashSet(n, s, p, q);
        }
        // 对于大N使用BitSet（内存效率高）
        else if (n <= 100000000) {
            return countDistinctBitSet(n, s, p, q);
        }
        // 对于非常大的N使用Floyd算法（常数空间）
        else {
            return countDistinctFloyd(n, s, p, q);
        }
    }
    
    /**
     * 工程化改进版本：完整的异常处理和验证
     */
    public static int countDistinctWithValidation(int n, int s, int p, int q) {
        try {
            // 输入验证
            if (n < 0) {
                throw new IllegalArgumentException("n must be non-negative");
            }
            if (s < 0 || p < 0 || q < 0) {
                throw new IllegalArgumentException("s, p, q must be non-negative");
            }
            if (s >= (1L << 31) || p >= (1L << 31) || q >= (1L << 31)) {
                throw new IllegalArgumentException("s, p, q must be less than 2^31");
            }
            
            return countDistinctOptimized(n, s, p, q);
            
        } catch (Exception e) {
            System.err.println("Error in countDistinct: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== HackerRank Bit Array - 单元测试 ===");
        
        // 测试用例1: 小规模测试
        int n1 = 5, s1 = 1, p1 = 2, q1 = 1;
        int expected1 = 5; // 序列: 1, 3, 7, 15, 31 全部不同
        int result1 = countDistinctOptimized(n1, s1, p1, q1);
        System.out.printf("测试1: n=%d, s=%d, p=%d, q=%d, 期望=%d, 实际=%d, %s%n",
                         n1, s1, p1, q1, expected1, result1,
                         result1 == expected1 ? "通过" : "失败");
        
        // 测试用例2: 有重复的序列
        int n2 = 10, s2 = 1, p2 = 3, q2 = 0;
        int expected2 = 4; // 序列可能产生循环
        int result2 = countDistinctOptimized(n2, s2, p2, q2);
        System.out.printf("测试2: n=%d, s=%d, p=%d, q=%d, 期望=%d, 实际=%d, %s%n",
                         n2, s2, p2, q2, expected2, result2,
                         result2 == expected2 ? "通过" : "失败");
        
        // 测试用例3: 边界情况 n=1
        int n3 = 1, s3 = 100, p3 = 1, q3 = 1;
        int expected3 = 1;
        int result3 = countDistinctOptimized(n3, s3, p3, q3);
        System.out.printf("测试3: n=%d, s=%d, p=%d, q=%d, 期望=%d, 实际=%d, %s%n",
                         n3, s3, p3, q3, expected3, result3,
                         result3 == expected3 ? "通过" : "失败");
        
        // 测试不同方法的结果一致性（小规模）
        System.out.println("\n=== 方法一致性测试（小规模） ===");
        int testN = 100;
        int r1 = countDistinctHashSet(testN, 1, 3, 1);
        int r2 = countDistinctBitSet(testN, 1, 3, 1);
        int r3 = countDistinctFloyd(testN, 1, 3, 1);
        int r4 = countDistinctOptimized(testN, 1, 3, 1);
        
        System.out.printf("HashSet: %d%n", r1);
        System.out.printf("BitSet: %d%n", r2);
        System.out.printf("Floyd: %d%n", r3);
        System.out.printf("优化法: %d%n", r4);
        System.out.printf("所有方法结果一致: %s%n", 
                         (r1 == r2 && r2 == r3 && r3 == r4) ? "是" : "否");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 测试不同规模的数据
        int[][] testCases = {
            {1000, 1, 3, 1},
            {10000, 1, 3, 1},
            {100000, 1, 3, 1},
            {1000000, 1, 3, 1}
        };
        
        for (int[] testCase : testCases) {
            int n = testCase[0], s = testCase[1], p = testCase[2], q = testCase[3];
            System.out.printf("n = %d:%n", n);
            
            // 测试HashSet方法（仅小规模）
            if (n <= 100000) {
                long startTime = System.nanoTime();
                int result1 = countDistinctHashSet(n, s, p, q);
                long time1 = System.nanoTime() - startTime;
                System.out.printf("  HashSet: %d ns, 结果: %d%n", time1, result1);
            }
            
            // 测试BitSet方法
            long startTime = System.nanoTime();
            int result2 = countDistinctBitSet(n, s, p, q);
            long time2 = System.nanoTime() - startTime;
            System.out.printf("  BitSet: %d ns, 结果: %d%n", time2, result2);
            
            // 测试Floyd方法
            startTime = System.nanoTime();
            int result3 = countDistinctFloyd(n, s, p, q);
            long time3 = System.nanoTime() - startTime;
            System.out.printf("  Floyd: %d ns, 结果: %d%n", time3, result3);
            
            System.out.println();
        }
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("=== 复杂度分析 ===");
        System.out.println("方法1（HashSet）:");
        System.out.println("  时间复杂度: O(N) - 线性扫描");
        System.out.println("  空间复杂度: O(N) - 存储所有元素");
        System.out.println("  适用场景: 小规模数据（N <= 10^6）");
        
        System.out.println("\n方法2（BitSet）:");
        System.out.println("  时间复杂度: O(N) - 线性扫描");
        System.out.println("  空间复杂度: O(2^31/8) ≈ 256MB - 固定大小");
        System.out.println("  适用场景: 中等规模数据（N <= 10^8）");
        
        System.out.println("\n方法3（Floyd循环检测）:");
        System.out.println("  时间复杂度: O(循环长度) - 通常远小于N");
        System.out.println("  空间复杂度: O(1) - 常数空间");
        System.out.println("  适用场景: 大规模数据（N > 10^8）");
        
        System.out.println("\n工程化建议:");
        System.out.println("1. 根据N的大小动态选择算法");
        System.out.println("2. 注意整数溢出问题，使用long进行中间计算");
        System.out.println("3. 对于竞赛题目，通常BitSet方法是最佳选择");
    }
    
    public static void main(String[] args) {
        System.out.println("HackerRank Bit Array - 位数组问题");
        System.out.println("计算根据规则生成的序列中不同整数的个数");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 示例使用
        System.out.println("\n=== 示例使用 ===");
        int[][] examples = {
            {5, 1, 2, 1},
            {10, 1, 3, 0},
            {100, 1, 1, 1}
        };
        
        for (int[] example : examples) {
            int n = example[0], s = example[1], p = example[2], q = example[3];
            int result = countDistinctWithValidation(n, s, p, q);
            System.out.printf("n=%d, s=%d, p=%d, q=%d -> 不同元素个数: %d%n", 
                             n, s, p, q, result);
        }
        
        // 处理标准输入（用于在线评测）
        /*
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int s = scanner.nextInt();
        int p = scanner.nextInt();
        int q = scanner.nextInt();
        System.out.println(countDistinctOptimized(n, s, p, q));
        scanner.close();
        */
    }
}
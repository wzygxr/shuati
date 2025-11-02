import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 相关题目22: LeetCode 264. 丑数 II
 * 题目链接: https://leetcode.cn/problems/ugly-number-ii/
 * 题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
 * 丑数 就是只包含质因数 2、3 和 5 的正整数。
 * 解题思路1: 使用最小堆生成丑数序列
 * 解题思路2: 使用动态规划，维护三个指针分别指向2、3、5的下一个乘数
 * 时间复杂度: 最小堆O(n log n)，动态规划O(n)
 * 空间复杂度: 最小堆O(n)，动态规划O(n)
 * 是否最优解: 动态规划解法是最优的，时间复杂度为O(n)
 */
public class Code22_UglyNumberII {
    
    /**
     * 使用最小堆生成丑数序列
     * 
     * @param n 第n个丑数
     * @return 第n个丑数
     * @throws IllegalArgumentException 当n参数无效时抛出异常
     */
    public int nthUglyNumberHeap(int n) {
        // 异常处理：检查n是否有效
        if (n <= 0) {
            throw new IllegalArgumentException("n必须是正整数");
        }
        
        // 特殊情况：第1个丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 使用集合来记录已经生成的丑数，避免重复
        Set<Long> seen = new HashSet<>();
        // 创建最小堆
        PriorityQueue<Long> heap = new PriorityQueue<>();
        
        // 质因数列表
        long[] factors = {2, 3, 5};
        
        // 初始化堆和集合
        seen.add(1L);
        heap.offer(1L);
        
        // 用于记录当前找到的丑数
        long currentUgly = 1;
        
        // 循环n次，找到第n个丑数
        for (int i = 0; i < n; i++) {
            // 取出堆顶元素，即当前最小的丑数
            currentUgly = heap.poll();
            
            // 生成新的丑数
            for (long factor : factors) {
                long nextUgly = currentUgly * factor;
                // 如果新丑数未被生成过，则加入堆和集合
                if (!seen.contains(nextUgly)) {
                    seen.add(nextUgly);
                    heap.offer(nextUgly);
                }
            }
        }
        
        // 返回第n个丑数
        return (int) currentUgly;
    }
    
    /**
     * 使用动态规划生成丑数序列
     * 
     * @param n 第n个丑数
     * @return 第n个丑数
     * @throws IllegalArgumentException 当n参数无效时抛出异常
     */
    public int nthUglyNumberDP(int n) {
        // 异常处理：检查n是否有效
        if (n <= 0) {
            throw new IllegalArgumentException("n必须是正整数");
        }
        
        // 特殊情况：第1个丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 创建一个数组来存储前n个丑数
        int[] uglyNumbers = new int[n];
        // 第1个丑数是1
        uglyNumbers[0] = 1;
        
        // 初始化三个指针，分别指向2、3、5的下一个乘数
        int p2 = 0, p3 = 0, p5 = 0;
        
        // 生成前n个丑数
        for (int i = 1; i < n; i++) {
            // 计算下一个可能的丑数
            int nextUgly2 = uglyNumbers[p2] * 2;
            int nextUgly3 = uglyNumbers[p3] * 3;
            int nextUgly5 = uglyNumbers[p5] * 5;
            
            // 取三个可能的丑数中的最小值作为当前丑数
            int minUgly = Math.min(nextUgly2, Math.min(nextUgly3, nextUgly5));
            uglyNumbers[i] = minUgly;
            
            // 更新对应的指针
            if (minUgly == nextUgly2) {
                p2++;
            }
            if (minUgly == nextUgly3) {
                p3++;
            }
            if (minUgly == nextUgly5) {
                p5++;
            }
        }
        
        // 返回第n个丑数
        return uglyNumbers[n - 1];
    }
    
    /**
     * 一种优化的动态规划实现，代码更简洁
     * 
     * @param n 第n个丑数
     * @return 第n个丑数
     */
    public int nthUglyNumberEfficient(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n必须是正整数");
        }
        
        // 初始化结果数组
        int[] res = new int[n];
        res[0] = 1;
        
        // 初始化三个指针
        int i2 = 0, i3 = 0, i5 = 0;
        
        for (int i = 1; i < n; i++) {
            // 计算下一个可能的最小值
            res[i] = Math.min(res[i2] * 2, Math.min(res[i3] * 3, res[i5] * 5));
            
            // 更新指针
            if (res[i] == res[i2] * 2) i2++;
            if (res[i] == res[i3] * 3) i3++;
            if (res[i] == res[i5] * 5) i5++;
        }
        
        return res[n - 1];
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code22_UglyNumberII solution = new Code22_UglyNumberII();
        
        // 测试用例
        int[] testCases = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] expectedResults = {1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24};
        
        System.out.println("=== 测试最小堆实现 ===");
        for (int i = 0; i < testCases.length; i++) {
            int n = testCases[i];
            int result = solution.nthUglyNumberHeap(n);
            int expected = expectedResults[i];
            System.out.println("第" + n + "个丑数 = " + result + ", 期望结果 = " + expected + ", " + 
                              (result == expected ? "✓" : "✗"));
        }
        
        System.out.println("\n=== 测试动态规划实现 ===");
        for (int i = 0; i < testCases.length; i++) {
            int n = testCases[i];
            int result = solution.nthUglyNumberDP(n);
            int expected = expectedResults[i];
            System.out.println("第" + n + "个丑数 = " + result + ", 期望结果 = " + expected + ", " + 
                              (result == expected ? "✓" : "✗"));
        }
        
        System.out.println("\n=== 测试优化的动态规划实现 ===");
        for (int i = 0; i < testCases.length; i++) {
            int n = testCases[i];
            int result = solution.nthUglyNumberEfficient(n);
            int expected = expectedResults[i];
            System.out.println("第" + n + "个丑数 = " + result + ", 期望结果 = " + expected + ", " + 
                              (result == expected ? "✓" : "✗"));
        }
        
        // 测试异常情况
        System.out.println("\n=== 测试异常情况 ===");
        try {
            solution.nthUglyNumberHeap(0);
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        try {
            solution.nthUglyNumberDP(-5);
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 测试大输入
        int n = 1690; // 最大的第1690个丑数在题目约束范围内
        
        long startTime = System.currentTimeMillis();
        int resultHeap = solution.nthUglyNumberHeap(n);
        long heapTime = System.currentTimeMillis() - startTime;
        System.out.println("最小堆实现在n=" + n + "时的结果: " + resultHeap + ", 用时: " + heapTime + "毫秒");
        
        startTime = System.currentTimeMillis();
        int resultDP = solution.nthUglyNumberDP(n);
        long dpTime = System.currentTimeMillis() - startTime;
        System.out.println("动态规划实现在n=" + n + "时的结果: " + resultDP + ", 用时: " + dpTime + "毫秒");
        
        System.out.println("\n性能比较: 动态规划比最小堆快 " + (double)heapTime / dpTime + "倍");
    }
    
    /*
     * 解题思路总结：
     * 1. 最小堆方法：
     *    - 使用最小堆来维护待处理的丑数候选
     *    - 每次取出最小的丑数，然后生成新的丑数
     *    - 使用集合避免重复
     *    - 时间复杂度O(n log n)，空间复杂度O(n)
     * 
     * 2. 动态规划方法（最优解）：
     *    - 维护三个指针，分别指向2、3、5需要乘的下一个位置
     *    - 每次选择三个指针生成的最小值作为下一个丑数
     *    - 更新对应的指针
     *    - 时间复杂度O(n)，空间复杂度O(n)
     * 
     * 3. 工程实现注意事项：
     *    - 使用long类型避免整数溢出
     *    - 正确处理边界条件
     *    - 注意指针更新逻辑，多个指针可能生成相同的数
     */
}
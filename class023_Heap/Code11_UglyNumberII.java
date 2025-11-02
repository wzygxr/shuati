package class027;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 相关题目3: LeetCode 264. 丑数 II
 * 题目链接: https://leetcode.cn/problems/ugly-number-ii/
 * 题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
 * 丑数 就是只包含质因数 2、3 和 5 的正整数。
 * 解题思路: 使用最小堆维护候选丑数，确保每次取出的是当前最小的丑数
 * 时间复杂度: O(n log n)，每次堆操作需要O(log n)时间
 * 空间复杂度: O(n)，堆和集合都需要O(n)空间
 * 是否最优解: 是，另一种更优的动态规划解法可以达到O(n)时间复杂度，但堆解法更直观
 * 
 * 本题属于堆的典型应用场景：需要频繁获取最小值并生成新的候选值
 */
public class Code11_UglyNumberII {
    
    /**
     * 查找第n个丑数
     * @param n 需要查找的丑数的位置（从1开始计数）
     * @return 第n个丑数
     * @throws IllegalArgumentException 当输入参数无效时抛出异常
     */
    public static int nthUglyNumber(int n) {
        // 异常处理：检查n是否为正整数
        if (n <= 0) {
            throw new IllegalArgumentException("n必须是正整数");
        }
        
        // 特殊情况处理
        if (n == 1) {
            return 1; // 第一个丑数是1
        }
        
        // 定义质因数
        int[] factors = {2, 3, 5};
        
        // 使用最小堆维护候选丑数
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        // 使用集合去重
        Set<Long> seen = new HashSet<>();
        
        // 初始化堆和集合，第一个丑数是1
        minHeap.offer(1L);
        seen.add(1L);
        
        long ugly = 0;
        // 执行n次操作，每次取出最小的丑数并生成新的候选丑数
        for (int i = 0; i < n; i++) {
            // 取出当前最小的丑数
            ugly = minHeap.poll();
            
            // 调试信息：打印当前处理的丑数
            // System.out.println("当前丑数: " + ugly + "，是第" + (i + 1) + "个丑数");
            
            // 生成新的候选丑数：将当前丑数分别乘以2、3、5
            for (int factor : factors) {
                long nextUgly = ugly * factor;
                // 如果新生成的数没有出现过，则加入堆和集合
                if (seen.add(nextUgly)) {
                    minHeap.offer(nextUgly);
                    // 调试信息：打印新加入的候选丑数
                    // System.out.println("新加入候选丑数: " + nextUgly);
                }
            }
        }
        
        // 第n次取出的就是第n个丑数
        return (int) ugly;
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本情况
        int n1 = 10;
        System.out.println("示例1输出: " + nthUglyNumber(n1)); // 期望输出: 12
        
        // 测试用例2：边界情况 - n=1
        int n2 = 1;
        System.out.println("示例2输出: " + nthUglyNumber(n2)); // 期望输出: 1
        
        // 测试用例3：较大的n
        int n3 = 1500;
        System.out.println("示例3输出: " + nthUglyNumber(n3)); // 期望输出: 859963392
        
        // 测试用例4：中等大小的n
        int n4 = 1690;
        System.out.println("示例4输出: " + nthUglyNumber(n4)); // 期望输出: 2123366400
        
        // 注意：以下测试用例会抛出异常
        /*
        try {
            nthUglyNumber(0);
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        try {
            nthUglyNumber(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        */
    }
}
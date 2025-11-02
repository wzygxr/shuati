import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 相关题目23: LeetCode 313. 超级丑数
 * 题目链接: https://leetcode.cn/problems/super-ugly-number/
 * 题目描述: 超级丑数 是一个正整数，并满足其所有质因数都出现在质数数组 primes 中。
 * 给你一个整数 n 和一个整数数组 primes，返回第 n 个 超级丑数 。
 * 解题思路1: 使用最小堆生成超级丑数序列
 * 解题思路2: 使用动态规划，为每个质数维护一个指针
 * 时间复杂度: 最小堆O(n log k)，动态规划O(nk)，其中k是primes数组的长度
 * 空间复杂度: 最小堆O(n)，动态规划O(n + k)
 * 是否最优解: 根据具体输入，两种解法各有优劣
 */
public class Code23_SuperUglyNumber {
    
    /**
     * 使用最小堆生成超级丑数序列
     * 
     * @param n 第n个超级丑数
     * @param primes 质因数数组
     * @return 第n个超级丑数
     * @throws IllegalArgumentException 当输入参数无效时抛出异常
     */
    public int nthSuperUglyNumberHeap(int n, int[] primes) {
        // 异常处理：检查n和primes是否有效
        if (n <= 0) {
            throw new IllegalArgumentException("n必须是正整数");
        }
        if (primes == null || primes.length == 0) {
            throw new IllegalArgumentException("primes数组不能为空");
        }
        
        // 特殊情况：第1个超级丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 使用集合来记录已经生成的超级丑数，避免重复
        Set<Long> seen = new HashSet<>();
        // 创建最小堆
        PriorityQueue<Long> heap = new PriorityQueue<>();
        
        // 初始化堆和集合
        seen.add(1L);
        heap.offer(1L);
        
        // 用于记录当前找到的超级丑数
        long currentUgly = 1;
        
        // 循环n次，找到第n个超级丑数
        for (int i = 0; i < n; i++) {
            // 取出堆顶元素，即当前最小的超级丑数
            currentUgly = heap.poll();
            
            // 生成新的超级丑数
            for (int prime : primes) {
                long nextUgly = currentUgly * prime;
                // 如果新超级丑数未被生成过，则加入堆和集合
                if (!seen.contains(nextUgly)) {
                    seen.add(nextUgly);
                    heap.offer(nextUgly);
                }
            }
        }
        
        // 返回第n个超级丑数
        return (int) currentUgly;
    }
    
    /**
     * 使用动态规划生成超级丑数序列
     * 
     * @param n 第n个超级丑数
     * @param primes 质因数数组
     * @return 第n个超级丑数
     * @throws IllegalArgumentException 当输入参数无效时抛出异常
     */
    public int nthSuperUglyNumberDP(int n, int[] primes) {
        // 异常处理：检查n和primes是否有效
        if (n <= 0) {
            throw new IllegalArgumentException("n必须是正整数");
        }
        if (primes == null || primes.length == 0) {
            throw new IllegalArgumentException("primes数组不能为空");
        }
        
        // 特殊情况：第1个超级丑数是1
        if (n == 1) {
            return 1;
        }
        
        // 创建一个数组来存储前n个超级丑数
        int[] superUgly = new int[n];
        // 第1个超级丑数是1
        superUgly[0] = 1;
        
        // 为每个质数维护一个指针
        int[] pointers = new int[primes.length];
        
        // 生成前n个超级丑数
        for (int i = 1; i < n; i++) {
            // 初始化最小值为一个很大的数
            int minUgly = Integer.MAX_VALUE;
            
            // 计算所有可能的下一个超级丑数，并找出最小值
            for (int j = 0; j < primes.length; j++) {
                int candidate = superUgly[pointers[j]] * primes[j];
                if (candidate < minUgly) {
                    minUgly = candidate;
                }
            }
            
            // 当前超级丑数为最小值
            superUgly[i] = minUgly;
            
            // 更新对应的指针
            for (int j = 0; j < primes.length; j++) {
                if (superUgly[pointers[j]] * primes[j] == minUgly) {
                    pointers[j]++;
                }
            }
        }
        
        // 返回第n个超级丑数
        return superUgly[n - 1];
    }
    
    /**
     * 一种优化的动态规划实现，减少一些重复计算
     * 
     * @param n 第n个超级丑数
     * @param primes 质因数数组
     * @return 第n个超级丑数
     */
    public int nthSuperUglyNumberOptimized(int n, int[] primes) {
        // 异常处理
        if (n <= 0) {
            throw new IllegalArgumentException("n必须是正整数");
        }
        if (primes == null || primes.length == 0) {
            throw new IllegalArgumentException("primes数组不能为空");
        }
        
        // 初始化结果数组
        int[] dp = new int[n];
        dp[0] = 1;
        
        // 初始化指针
        int k = primes.length;
        int[] pointers = new int[k];
        
        // 缓存当前每个质数对应的下一个可能的超级丑数
        int[] nextUglies = new int[k];
        for (int i = 0; i < k; i++) {
            nextUglies[i] = primes[i];
        }
        
        for (int i = 1; i < n; i++) {
            // 找到最小的下一个超级丑数
            dp[i] = findMin(nextUglies);
            
            // 更新指针和对应的下一个可能值
            for (int j = 0; j < k; j++) {
                if (dp[i] == nextUglies[j]) {
                    pointers[j]++;
                    nextUglies[j] = dp[pointers[j]] * primes[j];
                }
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 辅助方法：找到数组中的最小值
     * 
     * @param arr 输入数组
     * @return 数组中的最小值
     */
    private int findMin(int[] arr) {
        int min = arr[0];
        for (int num : arr) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code23_SuperUglyNumber solution = new Code23_SuperUglyNumber();
        
        // 测试用例1：基本用例
        System.out.println("\n测试用例1：基本用例");
        int n1 = 12;
        int[] primes1 = {2, 7, 13, 19};
        int expected1 = 32;
        
        int resultHeap1 = solution.nthSuperUglyNumberHeap(n1, primes1);
        int resultDP1 = solution.nthSuperUglyNumberDP(n1, primes1);
        int resultOpt1 = solution.nthSuperUglyNumberOptimized(n1, primes1);
        
        System.out.println("最小堆实现: " + resultHeap1 + ", 期望: " + expected1 + ", " + 
                          (resultHeap1 == expected1 ? "✓" : "✗"));
        System.out.println("动态规划实现: " + resultDP1 + ", 期望: " + expected1 + ", " + 
                          (resultDP1 == expected1 ? "✓" : "✗"));
        System.out.println("优化动态规划实现: " + resultOpt1 + ", 期望: " + expected1 + ", " + 
                          (resultOpt1 == expected1 ? "✓" : "✗"));
        
        // 测试用例2：简单质数数组
        System.out.println("\n测试用例2：简单质数数组");
        int n2 = 10;
        int[] primes2 = {2, 3, 5};
        int expected2 = 12; // 等同于普通丑数的第10个
        
        int resultHeap2 = solution.nthSuperUglyNumberHeap(n2, primes2);
        int resultDP2 = solution.nthSuperUglyNumberDP(n2, primes2);
        int resultOpt2 = solution.nthSuperUglyNumberOptimized(n2, primes2);
        
        System.out.println("最小堆实现: " + resultHeap2 + ", 期望: " + expected2 + ", " + 
                          (resultHeap2 == expected2 ? "✓" : "✗"));
        System.out.println("动态规划实现: " + resultDP2 + ", 期望: " + expected2 + ", " + 
                          (resultDP2 == expected2 ? "✓" : "✗"));
        System.out.println("优化动态规划实现: " + resultOpt2 + ", 期望: " + expected2 + ", " + 
                          (resultOpt2 == expected2 ? "✓" : "✗"));
        
        // 测试用例3：只有一个质数
        System.out.println("\n测试用例3：只有一个质数");
        int n3 = 5;
        int[] primes3 = {2};
        int expected3 = 16; // 2^4
        
        int resultHeap3 = solution.nthSuperUglyNumberHeap(n3, primes3);
        int resultDP3 = solution.nthSuperUglyNumberDP(n3, primes3);
        int resultOpt3 = solution.nthSuperUglyNumberOptimized(n3, primes3);
        
        System.out.println("最小堆实现: " + resultHeap3 + ", 期望: " + expected3 + ", " + 
                          (resultHeap3 == expected3 ? "✓" : "✗"));
        System.out.println("动态规划实现: " + resultDP3 + ", 期望: " + expected3 + ", " + 
                          (resultDP3 == expected3 ? "✓" : "✗"));
        System.out.println("优化动态规划实现: " + resultOpt3 + ", 期望: " + expected3 + ", " + 
                          (resultOpt3 == expected3 ? "✓" : "✗"));
        
        // 测试异常情况
        System.out.println("\n=== 测试异常情况 ===");
        try {
            solution.nthSuperUglyNumberHeap(0, new int[]{2, 3});
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        try {
            solution.nthSuperUglyNumberDP(5, new int[]{});
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 测试中等规模输入
        int n4 = 1000;
        int[] primes4 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
        
        long startTime = System.currentTimeMillis();
        int resultHeap = solution.nthSuperUglyNumberHeap(n4, primes4);
        long heapTime = System.currentTimeMillis() - startTime;
        System.out.println("最小堆实现在n=" + n4 + "时的结果: " + resultHeap + ", 用时: " + heapTime + "毫秒");
        
        startTime = System.currentTimeMillis();
        int resultDP = solution.nthSuperUglyNumberDP(n4, primes4);
        long dpTime = System.currentTimeMillis() - startTime;
        System.out.println("动态规划实现在n=" + n4 + "时的结果: " + resultDP + ", 用时: " + dpTime + "毫秒");
        
        startTime = System.currentTimeMillis();
        int resultOpt = solution.nthSuperUglyNumberOptimized(n4, primes4);
        long optTime = System.currentTimeMillis() - startTime;
        System.out.println("优化动态规划实现在n=" + n4 + "时的结果: " + resultOpt + ", 用时: " + optTime + "毫秒");
        
        System.out.println("\n性能比较:");
        if (dpTime > 0) {
            double ratio = (double)heapTime / dpTime;
            System.out.println("最小堆比动态规划 " + (ratio > 1 ? "慢" : "快") + " " + 
                              Math.abs(ratio - 1) + "倍");
        }
        if (optTime > 0) {
            double ratio = (double)dpTime / optTime;
            System.out.println("原始动态规划比优化动态规划 " + (ratio > 1 ? "慢" : "快") + " " + 
                              Math.abs(ratio - 1) + "倍");
        }
    }
    
    /*
     * 解题思路总结：
     * 1. 最小堆方法：
     *    - 使用最小堆来维护待处理的超级丑数候选
     *    - 每次取出最小的超级丑数，然后生成新的超级丑数
     *    - 使用集合避免重复
     *    - 时间复杂度O(n log k)，空间复杂度O(n)
     *    - 当primes数组长度较大时，这种方法可能会更高效
     * 
     * 2. 动态规划方法：
     *    - 维护primes数组长度个指针，分别指向每个质数需要乘的下一个位置
     *    - 每次选择所有可能的下一个超级丑数中的最小值
     *    - 更新对应的指针
     *    - 时间复杂度O(nk)，空间复杂度O(n + k)
     *    - 当primes数组长度较小时，这种方法通常比堆方法更高效
     * 
     * 3. 优化技巧：
     *    - 对于动态规划，可以缓存每个质数的下一个可能值，避免重复计算
     *    - 注意处理重复元素，特别是当多个质数生成相同的超级丑数时
     *    - 使用辅助方法提高代码可读性
     * 
     * 4. Java实现注意事项：
     *    - 使用long类型避免整数溢出
     *    - 正确处理异常情况
     *    - 使用Integer.MAX_VALUE作为初始最小值
     */
}
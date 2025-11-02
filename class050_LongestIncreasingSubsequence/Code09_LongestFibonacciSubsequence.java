package class072;

import java.util.*;

/**
 * 最长的斐波那契子序列的长度 - LeetCode 873
 * 题目来源：https://leetcode.cn/problems/length-of-longest-fibonacci-subsequence/
 * 难度：中等
 * 题目描述：如果序列 X_1, X_2, ..., X_n 满足下列条件，就说它是斐波那契式的：
 * n >= 3
 * 对于所有 i + 2 <= n，都有 X_i + X_{i+1} = X_{i+2}
 * 给定一个严格递增的正整数数组形成序列 arr ，找到 arr 中最长的斐波那契式的子序列的长度。
 * 如果没有这样的子序列，返回 0。
 * 子序列是指从原数组中删除一些元素（可以删除任何元素，包括零个元素），剩下的元素保持原来的顺序而不改变。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，我们需要找到满足斐波那契关系的最长子序列
 * 2. 使用动态规划+哈希表的方法：dp[i][j] 表示以arr[i]和arr[j]结尾的最长斐波那契子序列的长度
 * 3. 对于每对(i,j)，我们查找arr[j]-arr[i]是否存在于数组中且索引小于i，如果存在则可以形成更长的子序列
 * 
 * 复杂度分析：
 * 时间复杂度：O(n²)，其中n是数组的长度，我们需要填充大小为n×n的dp数组
 * 空间复杂度：O(n²)，用于存储dp数组，以及O(n)用于哈希表存储值到索引的映射
 */
public class Code09_LongestFibonacciSubsequence {

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println("测试用例1：");
        System.out.println("输入数组: " + Arrays.toString(arr1));
        System.out.println("结果: " + lenLongestFibSubseq(arr1) + "，预期: 5");
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {1, 3, 7, 11, 12, 14, 18};
        System.out.println("测试用例2：");
        System.out.println("输入数组: " + Arrays.toString(arr2));
        System.out.println("结果: " + lenLongestFibSubseq(arr2) + "，预期: 3");
        System.out.println();
        
        // 测试用例3：边界情况
        int[] arr3 = {1, 2, 3};
        System.out.println("测试用例3：");
        System.out.println("输入数组: " + Arrays.toString(arr3));
        System.out.println("结果: " + lenLongestFibSubseq(arr3) + "，预期: 3");
        System.out.println();
        
        // 测试用例4：没有斐波那契子序列
        int[] arr4 = {1, 4, 5};
        System.out.println("测试用例4：");
        System.out.println("输入数组: " + Arrays.toString(arr4));
        System.out.println("结果: " + lenLongestFibSubseq(arr4) + "，预期: 0");
        
        // 运行所有解法的对比测试
        runAllSolutionsTest(arr1);
        runAllSolutionsTest(arr2);
        runAllSolutionsTest(arr3);
        runAllSolutionsTest(arr4);
    }
    
    /**
     * 最优解法：动态规划+哈希表
     * @param arr 严格递增的正整数数组
     * @return 最长斐波那契子序列的长度，如果不存在返回0
     */
    public static int lenLongestFibSubseq(int[] arr) {
        // 边界情况处理
        if (arr == null || arr.length < 3) {
            return 0;
        }
        
        int n = arr.length;
        // 创建哈希表，存储值到索引的映射，用于快速查找
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            valueToIndex.put(arr[i], i);
        }
        
        // dp[i][j] 表示以arr[i]和arr[j]结尾的最长斐波那契子序列的长度
        // 初始化为2，表示至少有两个元素
        int[][] dp = new int[n][n];
        int maxLength = 0;
        
        // 初始化dp数组为2
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                dp[i][j] = 2;
            }
        }
        
        // 填充dp数组
        for (int j = 0; j < n; j++) {
            for (int k = j + 1; k < n; k++) {
                // 查找潜在的第一个元素 arr[i] = arr[k] - arr[j]
                int target = arr[k] - arr[j];
                // 确保target存在且严格小于arr[j]（因为数组严格递增）
                if (target < arr[j] && valueToIndex.containsKey(target)) {
                    int i = valueToIndex.get(target);
                    // 更新dp[j][k]
                    dp[j][k] = dp[i][j] + 1;
                    // 更新最大长度
                    maxLength = Math.max(maxLength, dp[j][k]);
                }
            }
        }
        
        // 如果maxLength至少为3，则返回，否则返回0
        return maxLength >= 3 ? maxLength : 0;
    }
    
    /**
     * 另一种动态规划解法，使用不同的遍历顺序
     * @param arr 严格递增的正整数数组
     * @return 最长斐波那契子序列的长度，如果不存在返回0
     */
    public static int lenLongestFibSubseqAlternative(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        
        int n = arr.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(arr[i], i);
        }
        
        int[][] dp = new int[n][n];
        int maxLength = 0;
        
        // 遍历所有可能的三元组 (i,j,k) 其中 i < j < k
        for (int k = 2; k < n; k++) {
            for (int j = 1; j < k; j++) {
                int i = map.getOrDefault(arr[k] - arr[j], -1);
                if (i >= 0 && i < j) {
                    dp[j][k] = dp[i][j] + 1;
                    maxLength = Math.max(maxLength, dp[j][k]);
                }
            }
        }
        
        // 如果存在斐波那契子序列，长度至少为3
        return maxLength > 0 ? maxLength + 2 : 0;
    }
    
    /**
     * 暴力解法（仅供对比，时间复杂度高）
     * @param arr 严格递增的正整数数组
     * @return 最长斐波那契子序列的长度，如果不存在返回0
     */
    public static int lenLongestFibSubseqBruteForce(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        
        int n = arr.length;
        Set<Integer> set = new HashSet<>();
        for (int num : arr) {
            set.add(num);
        }
        
        int maxLength = 0;
        
        // 枚举所有可能的前两个元素
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int a = arr[i];
                int b = arr[j];
                int length = 2;
                int next = a + b;
                
                // 检查是否可以形成更长的斐波那契序列
                while (set.contains(next)) {
                    a = b;
                    b = next;
                    next = a + b;
                    length++;
                }
                
                if (length >= 3) {
                    maxLength = Math.max(maxLength, length);
                }
            }
        }
        
        return maxLength;
    }
    
    /**
     * 运行所有解法的对比测试
     * @param arr 输入数组
     */
    public static void runAllSolutionsTest(int[] arr) {
        System.out.println("\n对比测试：" + Arrays.toString(arr));
        
        // 测试动态规划+哈希表解法
        long startTime = System.nanoTime();
        int result1 = lenLongestFibSubseq(arr);
        long endTime = System.nanoTime();
        System.out.println("动态规划+哈希表解法结果: " + result1);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试另一种动态规划解法
        startTime = System.nanoTime();
        int result2 = lenLongestFibSubseqAlternative(arr);
        endTime = System.nanoTime();
        System.out.println("另一种动态规划解法结果: " + result2);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 对于小型数组，也测试暴力解法
        if (arr.length <= 20) { // 避免大数组导致超时
            startTime = System.nanoTime();
            int result3 = lenLongestFibSubseqBruteForce(arr);
            endTime = System.nanoTime();
            System.out.println("暴力解法结果: " + result3);
            System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        } else {
            System.out.println("数组长度较大，跳过暴力解法测试");
        }
        
        System.out.println("----------------------------------------");
    }
    
    /**
     * 性能测试函数
     * @param n 数组长度
     */
    public static void performanceTest(int n) {
        // 生成严格递增的随机测试数据
        int[] arr = new int[n];
        arr[0] = 1;
        for (int i = 1; i < n; i++) {
            arr[i] = arr[i-1] + (int)(Math.random() * 10 + 1); // 确保严格递增
        }
        
        System.out.println("\n性能测试：数组长度 = " + n);
        
        // 测试动态规划+哈希表解法
        long startTime = System.nanoTime();
        int result1 = lenLongestFibSubseq(arr);
        long endTime = System.nanoTime();
        System.out.println("动态规划+哈希表解法耗时: " + (endTime - startTime) / 1_000_000 + " ms, 结果: " + result1);
    }
}
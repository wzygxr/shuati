package class072;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 最长定差子序列 - LeetCode 1218
 * 题目来源：https://leetcode.cn/problems/longest-arithmetic-subsequence-of-given-difference/
 * 难度：中等
 * 题目描述：给你一个整数数组 arr 和一个整数 difference，请你找出并返回 arr 中最长等差子序列的长度，
 * 该子序列中相邻元素之间的差等于 difference。
 * 子序列 是指在不改变其余元素顺序的情况下，通过删除一些元素或不删除任何元素而从 arr 派生出来的序列。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，但更特殊，因为我们需要的是固定差值的等差数列子序列
 * 2. 可以使用哈希表来优化动态规划的过程
 * 3. 对于每个元素arr[i]，我们需要查找是否存在arr[i] - difference这个元素，如果存在，则当前元素可以接在它后面形成更长的等差数列
 * 
 * 复杂度分析：
 * 时间复杂度：O(n)，其中n是数组的长度，我们只需要遍历一次数组，每次查询和更新哈希表的操作都是O(1)
 * 空间复杂度：O(n)，哈希表最多存储n个元素
 */
public class Code08_LongestArithmeticSubsequence {

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] arr1 = {1, 2, 3, 4};
        int difference1 = 1;
        System.out.println("测试用例1：");
        System.out.println("输入数组: " + Arrays.toString(arr1) + ", difference: " + difference1);
        System.out.println("结果: " + longestSubsequence(arr1, difference1) + "，预期: 4");
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {1, 3, 5, 7};
        int difference2 = 1;
        System.out.println("测试用例2：");
        System.out.println("输入数组: " + Arrays.toString(arr2) + ", difference: " + difference2);
        System.out.println("结果: " + longestSubsequence(arr2, difference2) + "，预期: 1");
        System.out.println();
        
        // 测试用例3
        int[] arr3 = {1, 5, 7, 8, 5, 3, 4, 2, 1};
        int difference3 = -2;
        System.out.println("测试用例3：");
        System.out.println("输入数组: " + Arrays.toString(arr3) + ", difference: " + difference3);
        System.out.println("结果: " + longestSubsequence(arr3, difference3) + "，预期: 4");
        System.out.println();
        
        // 测试用例4：边界情况
        int[] arr4 = {1};
        int difference4 = 0;
        System.out.println("测试用例4：");
        System.out.println("输入数组: " + Arrays.toString(arr4) + ", difference: " + difference4);
        System.out.println("结果: " + longestSubsequence(arr4, difference4) + "，预期: 1");
        System.out.println();
        
        // 测试用例5：负数差值
        int[] arr5 = {3, 0, -3, 4, -4, 7, 6};
        int difference5 = 3;
        System.out.println("测试用例5：");
        System.out.println("输入数组: " + Arrays.toString(arr5) + ", difference: " + difference5);
        System.out.println("结果: " + longestSubsequence(arr5, difference5) + "，预期: 2");
        
        // 运行所有解法的对比测试
        runAllSolutionsTest(arr1, difference1);
        runAllSolutionsTest(arr2, difference2);
        runAllSolutionsTest(arr3, difference3);
        runAllSolutionsTest(arr4, difference4);
        runAllSolutionsTest(arr5, difference5);
    }
    
    /**
     * 最优解法：使用哈希表优化动态规划
     * @param arr 输入数组
     * @param difference 固定差值
     * @return 最长等差子序列的长度
     */
    public static int longestSubsequence(int[] arr, int difference) {
        // 边界情况处理
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 使用哈希表存储每个数字最后出现时的最长子序列长度
        // key: 数字值, value: 以该数字结尾的最长等差子序列长度
        Map<Integer, Integer> dp = new HashMap<>();
        
        int maxLength = 1; // 至少有一个元素
        
        // 遍历数组中的每个元素
        for (int num : arr) {
            // 查找前驱元素：num - difference
            int prev = num - difference;
            // 如果前驱元素存在，则当前元素可以接在它后面形成更长的子序列
            // 否则，当前元素自身形成一个长度为1的子序列
            int currentLength = dp.getOrDefault(prev, 0) + 1;
            
            // 更新当前元素的最长子序列长度
            dp.put(num, currentLength);
            
            // 更新全局最大长度
            maxLength = Math.max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 动态规划解法（未优化，用于对比）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * @param arr 输入数组
     * @param difference 固定差值
     * @return 最长等差子序列的长度
     */
    public static int longestSubsequenceDP(int[] arr, int difference) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        int[] dp = new int[n];
        // 初始化：每个元素自身形成一个长度为1的子序列
        Arrays.fill(dp, 1);
        
        int maxLength = 1;
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[i] - arr[j] == difference) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
    
    /**
     * 使用哈希表优化的进阶解法 - 考虑到可能有重复元素
     * @param arr 输入数组
     * @param difference 固定差值
     * @return 最长等差子序列的长度
     */
    public static int longestSubsequenceOptimized(int[] arr, int difference) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 使用哈希表记录每个数字最后出现时的最长子序列长度
        Map<Integer, Integer> dp = new HashMap<>();
        int maxLength = 1;
        
        for (int num : arr) {
            // 当前数字可以接在num - difference后面
            int prevLength = dp.getOrDefault(num - difference, 0);
            // 对于重复元素，我们总是保留最大的长度
            int currentLength = prevLength + 1;
            
            // 如果当前数字已经在哈希表中，且之前记录的长度更大，则不更新
            // 否则更新为更长的长度
            if (!dp.containsKey(num) || currentLength > dp.get(num)) {
                dp.put(num, currentLength);
            }
            
            maxLength = Math.max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 运行所有解法的对比测试
     * @param arr 输入数组
     * @param difference 固定差值
     */
    public static void runAllSolutionsTest(int[] arr, int difference) {
        System.out.println("\n对比测试：" + Arrays.toString(arr) + "，difference = " + difference);
        
        // 测试哈希表优化解法
        long startTime = System.nanoTime();
        int result1 = longestSubsequence(arr, difference);
        long endTime = System.nanoTime();
        System.out.println("哈希表优化解法结果: " + result1);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试优化进阶解法
        startTime = System.nanoTime();
        int result3 = longestSubsequenceOptimized(arr, difference);
        endTime = System.nanoTime();
        System.out.println("进阶优化解法结果: " + result3);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 对于小型数组，也测试O(n²)的DP解法
        if (arr.length <= 1000) { // 避免大数组导致超时
            startTime = System.nanoTime();
            int result2 = longestSubsequenceDP(arr, difference);
            endTime = System.nanoTime();
            System.out.println("传统DP解法结果: " + result2);
            System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        } else {
            System.out.println("数组长度较大，跳过传统DP解法测试");
        }
        
        System.out.println("----------------------------------------");
    }
    
    /**
     * 性能测试函数
     * @param n 数组长度
     * @param difference 固定差值
     */
    public static void performanceTest(int n, int difference) {
        // 生成随机测试数据
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int)(Math.random() * 10000 - 5000); // 生成-5000到5000之间的随机数
        }
        
        System.out.println("\n性能测试：数组长度 = " + n);
        
        // 测试哈希表优化解法
        long startTime = System.nanoTime();
        int result1 = longestSubsequence(arr, difference);
        long endTime = System.nanoTime();
        System.out.println("哈希表优化解法耗时: " + (endTime - startTime) / 1_000_000 + " ms, 结果: " + result1);
        
        // 对于小型数组，也测试O(n²)的DP解法
        if (n <= 5000) { // 限制数组大小以避免超时
            try {
                startTime = System.nanoTime();
                int result2 = longestSubsequenceDP(arr, difference);
                endTime = System.nanoTime();
                System.out.println("传统DP解法耗时: " + (endTime - startTime) / 1_000_000 + " ms, 结果: " + result2);
            } catch (Exception e) {
                System.out.println("传统DP解法执行超时");
            }
        } else {
            System.out.println("数组长度超过阈值，跳过传统DP解法性能测试");
        }
    }
}
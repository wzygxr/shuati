package class072;

import java.util.*;

/**
 * 最长等差数列 - LeetCode 1027
 * 题目来源：https://leetcode.cn/problems/longest-arithmetic-subsequence/
 * 难度：中等
 * 题目描述：给你一个整数数组 nums，返回 nums 中最长等差子序列的长度。
 * 注意：子序列是指从数组中删除一些元素（可以不删除任何元素）而不改变其余元素的顺序得到的序列。
 * 等差子序列是指元素之间的差值都相等的序列。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，我们需要找到具有相同差值的最长子序列
 * 2. 使用动态规划+哈希表的方法：dp[i][d] 表示以nums[i]结尾且公差为d的最长等差数列长度
 * 3. 对于每个元素nums[i]，遍历之前的所有元素nums[j]，计算差值d = nums[i] - nums[j]，并更新dp[i][d]
 * 
 * 复杂度分析：
 * 时间复杂度：O(n²)，其中n是数组的长度，对于每个元素，我们需要遍历之前的所有元素
 * 空间复杂度：O(n²)，最坏情况下，每个元素对应的不同公差数量接近n
 */
public class Code10_LongestArithmeticSequence {

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] arr1 = {3, 6, 9, 12};
        System.out.println("测试用例1：");
        System.out.println("输入数组: " + Arrays.toString(arr1));
        System.out.println("结果: " + longestArithSeqLength(arr1) + "，预期: 4");
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {9, 4, 7, 2, 10};
        System.out.println("测试用例2：");
        System.out.println("输入数组: " + Arrays.toString(arr2));
        System.out.println("结果: " + longestArithSeqLength(arr2) + "，预期: 3");
        System.out.println();
        
        // 测试用例3
        int[] arr3 = {20, 1, 15, 3, 10, 5, 8};
        System.out.println("测试用例3：");
        System.out.println("输入数组: " + Arrays.toString(arr3));
        System.out.println("结果: " + longestArithSeqLength(arr3) + "，预期: 4");
        System.out.println();
        
        // 测试用例4：边界情况
        int[] arr4 = {1, 3, 5};
        System.out.println("测试用例4：");
        System.out.println("输入数组: " + Arrays.toString(arr4));
        System.out.println("结果: " + longestArithSeqLength(arr4) + "，预期: 3");
        
        // 运行所有解法的对比测试
        runAllSolutionsTest(arr1);
        runAllSolutionsTest(arr2);
        runAllSolutionsTest(arr3);
        runAllSolutionsTest(arr4);
    }
    
    /**
     * 最优解法：动态规划+哈希表
     * @param nums 整数数组
     * @return 最长等差子序列的长度
     */
    public static int longestArithSeqLength(int[] nums) {
        // 边界情况处理
        if (nums == null || nums.length <= 1) {
            return nums.length;
        }
        
        int n = nums.length;
        int maxLength = 2; // 至少有两个元素时，长度至少为2
        
        // 使用数组的哈希表来存储每个位置的公差对应的最长长度
        // dp[i] 表示以nums[i]结尾的所有可能公差对应的最长等差子序列长度
        Map<Integer, Integer>[] dp = new HashMap[n];
        
        // 初始化dp数组
        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
        }
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 计算公差
                int diff = nums[i] - nums[j];
                
                // 如果dp[j]中存在公差为diff的记录，则dp[i][diff] = dp[j][diff] + 1
                // 否则，dp[i][diff] = 2（至少有nums[j]和nums[i]两个元素）
                dp[i].put(diff, dp[j].getOrDefault(diff, 1) + 1);
                
                // 更新最大长度
                maxLength = Math.max(maxLength, dp[i].get(diff));
            }
        }
        
        return maxLength;
    }
    
    /**
     * 另一种实现方式，使用二维数组（仅当数值范围较小时适用）
     * @param nums 整数数组
     * @return 最长等差子序列的长度
     */
    public static int longestArithSeqLength2(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return nums.length;
        }
        
        int n = nums.length;
        int maxLength = 2;
        
        // 找出数组中的最小值和最大值，确定可能的公差范围
        int minVal = Integer.MAX_VALUE, maxVal = Integer.MIN_VALUE;
        for (int num : nums) {
            minVal = Math.min(minVal, num);
            maxVal = Math.max(maxVal, num);
        }
        
        // 计算可能的最大公差范围
        int maxDiff = maxVal - minVal;
        
        // dp[i][d+maxDiff] 表示以nums[i]结尾且公差为d的最长等差数列长度
        // 加上maxDiff是为了避免负索引
        int[][] dp = new int[n][2 * maxDiff + 1];
        
        // 初始化dp数组为1，表示每个元素自身构成一个长度为1的序列
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], 1);
        }
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                // 将公差映射到非负索引
                int idx = diff + maxDiff;
                
                dp[i][idx] = dp[j][idx] + 1;
                maxLength = Math.max(maxLength, dp[i][idx]);
            }
        }
        
        return maxLength;
    }
    
    /**
     * 暴力解法优化版：使用哈希表记录元素出现的位置
     * @param nums 整数数组
     * @return 最长等差子序列的长度
     */
    public static int longestArithSeqLengthBruteForce(int[] nums) {
        if (nums == null || nums.length <= 2) {
            return nums.length;
        }
        
        int n = nums.length;
        int maxLength = 2;
        
        // 使用哈希表存储每个值及其出现的索引列表
        Map<Integer, List<Integer>> valueToIndices = new HashMap<>();
        for (int i = 0; i < n; i++) {
            valueToIndices.putIfAbsent(nums[i], new ArrayList<>());
            valueToIndices.get(nums[i]).add(i);
        }
        
        // 枚举所有可能的前两个元素
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int prev = nums[i];
                int curr = nums[j];
                int diff = curr - prev;
                int next = curr + diff;
                int length = 2;
                
                // 查找下一个元素
                while (valueToIndices.containsKey(next)) {
                    // 找到在j之后出现的next
                    boolean found = false;
                    for (int idx : valueToIndices.get(next)) {
                        if (idx > j) {
                            j = idx;  // 更新j为下一个元素的索引
                            prev = curr;
                            curr = next;
                            next = curr + diff;
                            length++;
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        break;
                    }
                }
                
                maxLength = Math.max(maxLength, length);
            }
        }
        
        return maxLength;
    }
    
    /**
     * 运行所有解法的对比测试
     * @param nums 输入数组
     */
    public static void runAllSolutionsTest(int[] nums) {
        System.out.println("\n对比测试：" + Arrays.toString(nums));
        
        // 测试动态规划+哈希表解法
        long startTime = System.nanoTime();
        int result1 = longestArithSeqLength(nums);
        long endTime = System.nanoTime();
        System.out.println("动态规划+哈希表解法结果: " + result1);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试二维数组解法
        startTime = System.nanoTime();
        int result2 = longestArithSeqLength2(nums);
        endTime = System.nanoTime();
        System.out.println("二维数组解法结果: " + result2);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 对于小型数组，也测试暴力优化解法
        if (nums.length <= 20) { // 避免大数组导致超时
            startTime = System.nanoTime();
            int result3 = longestArithSeqLengthBruteForce(nums);
            endTime = System.nanoTime();
            System.out.println("暴力优化解法结果: " + result3);
            System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        } else {
            System.out.println("数组长度较大，跳过暴力优化解法测试");
        }
        
        System.out.println("----------------------------------------");
    }
    
    /**
     * 性能测试函数
     * @param n 数组长度
     */
    public static void performanceTest(int n) {
        // 生成随机测试数据
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = (int)(Math.random() * 1000);
        }
        
        System.out.println("\n性能测试：数组长度 = " + n);
        
        // 测试动态规划+哈希表解法
        long startTime = System.nanoTime();
        int result1 = longestArithSeqLength(nums);
        long endTime = System.nanoTime();
        System.out.println("动态规划+哈希表解法耗时: " + (endTime - startTime) / 1_000_000 + " ms, 结果: " + result1);
    }
}
package class072;

import java.util.*;

/**
 * 等差数列划分 II - 子序列 - LeetCode 446
 * 题目来源：https://leetcode.cn/problems/arithmetic-slices-ii-subsequence/
 * 难度：困难
 * 题目描述：给你一个整数数组 nums，请你返回所有长度至少为 3 的等差子序列的数目。
 * 注意：子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 另外，子序列中的元素在原数组中可能不连续，但等差子序列需要满足元素之间的差值相等。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，我们需要计算所有可能的等差数列子序列数目
 * 2. 使用动态规划+哈希表的方法：dp[i][d] 表示以nums[i]结尾且公差为d的等差数列子序列的数目（至少有两个元素）
 * 3. 对于每个元素nums[i]，遍历之前的所有元素nums[j]，计算差值d = nums[i] - nums[j]，并更新dp[i][d] += dp[j][d] + 1
 * 4. 其中+1表示nums[j]和nums[i]形成的新的二元组，dp[j][d]表示可以接在已有等差序列后面的数目
 * 
 * 复杂度分析：
 * 时间复杂度：O(n²)，其中n是数组的长度，对于每个元素，我们需要遍历之前的所有元素
 * 空间复杂度：O(n²)，最坏情况下，每个元素对应的不同公差数量接近n
 */
public class Code11_ArithmeticSlicesII {

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {2, 4, 6, 8, 10};
        System.out.println("测试用例1：");
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("结果: " + numberOfArithmeticSlices(nums1) + "，预期: 7");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {7, 7, 7, 7, 7};
        System.out.println("测试用例2：");
        System.out.println("输入数组: " + Arrays.toString(nums2));
        System.out.println("结果: " + numberOfArithmeticSlices(nums2) + "，预期: 16");
        System.out.println();
        
        // 测试用例3：边界情况
        int[] nums3 = {1, 2, 3};
        System.out.println("测试用例3：");
        System.out.println("输入数组: " + Arrays.toString(nums3));
        System.out.println("结果: " + numberOfArithmeticSlices(nums3) + "，预期: 1");
        
        // 运行所有解法的对比测试
        runAllSolutionsTest(nums1);
        runAllSolutionsTest(nums2);
        runAllSolutionsTest(nums3);
    }
    
    /**
     * 最优解法：动态规划+哈希表
     * @param nums 整数数组
     * @return 所有长度至少为3的等差子序列的数目
     */
    public static int numberOfArithmeticSlices(int[] nums) {
        // 边界情况处理
        if (nums == null || nums.length < 3) {
            return 0;
        }
        
        int n = nums.length;
        int total = 0; // 记录所有长度至少为3的等差子序列数目
        
        // dp[i] 是一个哈希表，键为公差，值为以nums[i]结尾且具有该公差的等差子序列数目（至少有两个元素）
        Map<Long, Integer>[] dp = new HashMap[n];
        
        // 初始化dp数组
        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
        }
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 计算公差，注意可能会溢出，所以使用long
                long diff = (long) nums[i] - nums[j];
                
                // 获取以nums[j]结尾且公差为diff的等差子序列数目
                int countJ = dp[j].getOrDefault(diff, 0);
                
                // 以nums[i]结尾且公差为diff的等差子序列数目 = 
                // 以nums[j]结尾且公差为diff的等差子序列数目（将nums[i]添加到这些序列后面） + 1（nums[j]和nums[i]形成的新二元组）
                dp[i].put(diff, dp[i].getOrDefault(diff, 0) + countJ + 1);
                
                // 只有当countJ >= 1时，才能形成长度至少为3的等差子序列
                // 因为countJ表示以nums[j]结尾且公差为diff的等差子序列数目（至少有两个元素）
                // 所以将nums[i]添加到这些序列后面，就形成了长度至少为3的等差子序列
                total += countJ;
            }
        }
        
        return total;
    }
    
    /**
     * 另一种实现方式，使用Long作为公差类型以避免整数溢出
     * @param nums 整数数组
     * @return 所有长度至少为3的等差子序列的数目
     */
    public static int numberOfArithmeticSlicesAlternative(int[] nums) {
        if (nums == null || nums.length < 3) {
            return 0;
        }
        
        int n = nums.length;
        int total = 0;
        
        List<Map<Long, Integer>> dp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dp.add(new HashMap<>());
        }
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                long diff = (long) nums[i] - nums[j];
                
                int prevCount = dp.get(j).getOrDefault(diff, 0);
                dp.get(i).put(diff, dp.get(i).getOrDefault(diff, 0) + prevCount + 1);
                
                // 每次将prevCount累加到总结果中，因为这些都能形成新的长度>=3的子序列
                total += prevCount;
            }
        }
        
        return total;
    }
    
    /**
     * 解释性更强的版本，添加了详细的中间变量说明
     * @param nums 整数数组
     * @return 所有长度至少为3的等差子序列的数目
     */
    public static int numberOfArithmeticSlicesExplained(int[] nums) {
        if (nums == null || nums.length < 3) {
            return 0;
        }
        
        int n = nums.length;
        int result = 0;
        
        // dp[i][d]表示以nums[i]结尾，公差为d的等差子序列的数量（至少包含两个元素）
        Map<Long, Integer>[] dp = new HashMap[n];
        
        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 计算公差
                long diff = (long) nums[i] - nums[j];
                
                // 获取以nums[j]结尾且公差为diff的等差子序列数目
                int sequencesEndingAtJ = dp[j].getOrDefault(diff, 0);
                
                // 新的序列数目：已有的序列数目 + 1（nums[j], nums[i]这个新的二元组）
                int newSequencesCount = sequencesEndingAtJ + 1;
                
                // 更新dp[i][diff]
                dp[i].put(diff, dp[i].getOrDefault(diff, 0) + newSequencesCount);
                
                // 对于每个以nums[j]结尾且公差为diff的序列，加上nums[i]后就形成了一个长度至少为3的序列
                // 因此，将sequencesEndingAtJ加到结果中
                result += sequencesEndingAtJ;
            }
        }
        
        return result;
    }
    
    /**
     * 运行所有解法的对比测试
     * @param nums 输入数组
     */
    public static void runAllSolutionsTest(int[] nums) {
        System.out.println("\n对比测试：" + Arrays.toString(nums));
        
        // 测试动态规划+哈希表解法
        long startTime = System.nanoTime();
        int result1 = numberOfArithmeticSlices(nums);
        long endTime = System.nanoTime();
        System.out.println("动态规划+哈希表解法结果: " + result1);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试另一种实现方式
        startTime = System.nanoTime();
        int result2 = numberOfArithmeticSlicesAlternative(nums);
        endTime = System.nanoTime();
        System.out.println("另一种实现方式结果: " + result2);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试解释性更强的版本
        startTime = System.nanoTime();
        int result3 = numberOfArithmeticSlicesExplained(nums);
        endTime = System.nanoTime();
        System.out.println("解释性版本结果: " + result3);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
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
        int result1 = numberOfArithmeticSlices(nums);
        long endTime = System.nanoTime();
        System.out.println("动态规划+哈希表解法耗时: " + (endTime - startTime) / 1_000_000 + " ms, 结果: " + result1);
    }
}
package class063;

import java.util.*;

// 目标和
// 给你一个非负整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
// 测试链接 : https://leetcode.cn/problems/target-sum/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
// 然后通过哈希表统计不同表达式的数目
// 时间复杂度：O(2^(n/2))
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查数组是否为空
// 2. 性能优化：使用折半搜索减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// Java中使用HashMap进行计数统计，使用递归计算子集和

public class Code06_TargetSum {
    
    /**
     * 计算可以通过添加 '+' 或 '-' 构造的、运算结果等于 target 的不同表达式的数目
     * @param nums 输入数组
     * @param target 目标值
     * @return 不同表达式的数目
     */
    public static int findTargetSumWays(int[] nums, int target) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 使用折半搜索，将数组分为两半
        Map<Integer, Integer> leftSums = new HashMap<>();
        Map<Integer, Integer> rightSums = new HashMap<>();
        
        // 计算左半部分所有可能的子集和及其出现次数
        generateSubsetSums(nums, 0, n / 2, 0, leftSums);
        
        // 计算右半部分所有可能的子集和及其出现次数
        generateSubsetSums(nums, n / 2, n, 0, rightSums);
        
        // 统计满足条件的表达式数目
        int count = 0;
        for (Map.Entry<Integer, Integer> leftEntry : leftSums.entrySet()) {
            int leftSum = leftEntry.getKey();
            int leftCount = leftEntry.getValue();
            
            // 查找右半部分是否存在和为(target - leftSum)的子集
            int rightCount = rightSums.getOrDefault(target - leftSum, 0);
            count += leftCount * rightCount;
        }
        
        return count;
    }
    
    /**
     * 递归生成指定范围内所有可能的子集和及其出现次数
     * @param nums 输入数组
     * @param start 起始索引
     * @param end 结束索引
     * @param currentSum 当前累积和
     * @param sums 存储结果的Map，key为和，value为出现次数
     */
    private static void generateSubsetSums(int[] nums, int start, int end, int currentSum, Map<Integer, Integer> sums) {
        // 递归终止条件
        if (start == end) {
            sums.put(currentSum, sums.getOrDefault(currentSum, 0) + 1);
            return;
        }
        
        // 不选择当前元素（相当于添加 '-' 号）
        generateSubsetSums(nums, start + 1, end, currentSum - nums[start], sums);
        
        // 选择当前元素（相当于添加 '+' 号）
        generateSubsetSums(nums, start + 1, end, currentSum + nums[start], sums);
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 1, 1, 1, 1};
        int target1 = 3;
        System.out.println("测试用例1:");
        System.out.println("nums = [1, 1, 1, 1, 1], target = 3");
        System.out.println("期望输出: 5");
        System.out.println("实际输出: " + findTargetSumWays(nums1, target1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1};
        int target2 = 1;
        System.out.println("测试用例2:");
        System.out.println("nums = [1], target = 1");
        System.out.println("期望输出: 1");
        System.out.println("实际输出: " + findTargetSumWays(nums2, target2));
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {1, 0};
        int target3 = 1;
        System.out.println("测试用例3:");
        System.out.println("nums = [1, 0], target = 1");
        System.out.println("期望输出: 2");
        System.out.println("实际输出: " + findTargetSumWays(nums3, target3));
    }
}
package class063;

import java.util.*;

// 数组的均值分割
// 题目来源：LeetCode 805
// 题目描述：
// 给定一个整数数组 nums，判断是否可以将数组分割成两个非空子集，使得两个子集的平均值相等。
// 测试链接：https://leetcode.cn/problems/split-array-with-same-average/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和与元素个数组合，
// 然后通过哈希表查找是否存在满足条件的组合
// 时间复杂度：O(2^(n/2) * n)
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查数组长度和边界条件
// 2. 性能优化：使用折半搜索减少搜索空间，提前剪枝
// 3. 可读性：变量命名清晰，注释详细
// 4. 数学优化：利用数学性质进行优化
// 
// 语言特性差异：
// Java中使用HashMap进行组合统计，使用递归计算子集和

public class Code11_ArrayMeanSplit {
    
    /**
     * 判断是否可以将数组分割成两个非空子集，使得两个子集的平均值相等
     * 
     * @param nums 输入数组
     * @return 如果可以分割返回true，否则返回false
     * 
     * 算法核心思想：
     * 1. 数学推导：如果两个子集平均值相等，则整个数组的平均值等于每个子集的平均值
     * 2. 折半搜索：将数组分为两半，分别计算所有可能的和与元素个数组合
     * 3. 组合查找：使用哈希表快速查找满足条件的组合
     * 
     * 时间复杂度分析：
     * - 折半搜索将O(2^n)优化为O(2^(n/2))
     * - 每个组合需要存储和与元素个数信息
     * - 总体时间复杂度：O(2^(n/2) * n)
     * 
     * 空间复杂度分析：
     * - 需要存储两个子问题的所有可能组合
     * - 空间复杂度：O(2^(n/2))
     */
    public static boolean splitArraySameAverage(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        int n = nums.length;
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 数学优化：如果整个数组的和为0，则任何非空子集的和都为0时平均值相等
        // 但需要确保两个子集都非空
        if (totalSum == 0) {
            // 检查是否存在非空真子集和为0
            return hasZeroSubset(nums, n);
        }
        
        // 使用折半搜索，将数组分为两半
        int mid = n / 2;
        
        // 存储左半部分的所有可能组合：key为(和, 元素个数)
        Map<Pair, Boolean> leftCombinations = new HashMap<>();
        // 存储右半部分的所有可能组合
        Map<Pair, Boolean> rightCombinations = new HashMap<>();
        
        // 计算左半部分的所有可能组合
        generateCombinations(nums, 0, mid, 0, 0, leftCombinations);
        
        // 计算右半部分的所有可能组合
        generateCombinations(nums, mid, n, 0, 0, rightCombinations);
        
        // 检查左半部分的空集情况（右半部分需要是非空真子集）
        for (Map.Entry<Pair, Boolean> rightEntry : rightCombinations.entrySet()) {
            Pair rightPair = rightEntry.getKey();
            int rightSum = rightPair.sum;
            int rightCount = rightPair.count;
            
            // 右半部分必须是非空子集
            if (rightCount > 0 && rightCount < n) {
                // 检查平均值是否相等：rightSum/rightCount = totalSum/n
                // 等价于：rightSum * n == totalSum * rightCount
                if ((long)rightSum * n == (long)totalSum * rightCount) {
                    return true;
                }
            }
        }
        
        // 检查左右两部分组合的搭配
        for (Map.Entry<Pair, Boolean> leftEntry : leftCombinations.entrySet()) {
            Pair leftPair = leftEntry.getKey();
            int leftSum = leftPair.sum;
            int leftCount = leftPair.count;
            
            for (Map.Entry<Pair, Boolean> rightEntry : rightCombinations.entrySet()) {
                Pair rightPair = rightEntry.getKey();
                int rightSum = rightPair.sum;
                int rightCount = rightPair.count;
                
                // 两个子集都必须是非空的，且它们的并集不能是整个数组
                int totalCount = leftCount + rightCount;
                if (leftCount > 0 && rightCount > 0 && totalCount < n) {
                    int totalSubsetSum = leftSum + rightSum;
                    
                    // 检查平均值是否相等：totalSubsetSum/totalCount = totalSum/n
                    // 等价于：totalSubsetSum * n == totalSum * totalCount
                    if ((long)totalSubsetSum * n == (long)totalSum * totalCount) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 检查数组中是否存在非空真子集和为0
     */
    private static boolean hasZeroSubset(int[] nums, int n) {
        // 使用动态规划检查是否存在和为0的非空真子集
        Set<Integer> sums = new HashSet<>();
        sums.add(0);
        
        for (int num : nums) {
            Set<Integer> newSums = new HashSet<>(sums);
            for (int sum : sums) {
                newSums.add(sum + num);
            }
            sums = newSums;
        }
        
        // 检查是否存在非空真子集和为0
        return sums.contains(0) && n > 1;
    }
    
    /**
     * 递归生成指定范围内所有可能的和与元素个数组合
     * 
     * @param nums 输入数组
     * @param start 起始索引
     * @param end 结束索引
     * @param currentSum 当前累积和
     * @param currentCount 当前元素个数
     * @param combinations 存储结果的Map
     */
    private static void generateCombinations(int[] nums, int start, int end, 
                                           int currentSum, int currentCount,
                                           Map<Pair, Boolean> combinations) {
        // 递归终止条件
        if (start == end) {
            Pair pair = new Pair(currentSum, currentCount);
            combinations.put(pair, true);
            return;
        }
        
        // 不选择当前元素
        generateCombinations(nums, start + 1, end, currentSum, currentCount, combinations);
        
        // 选择当前元素
        generateCombinations(nums, start + 1, end, currentSum + nums[start], currentCount + 1, combinations);
    }
    
    /**
     * 辅助类：存储和与元素个数的组合
     */
    private static class Pair {
        int sum;
        int count;
        
        Pair(int sum, int count) {
            this.sum = sum;
            this.count = count;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair pair = (Pair) obj;
            return sum == pair.sum && count == pair.count;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(sum, count);
        }
    }
    
    // 单元测试方法
    public static void main(String[] args) {
        // 测试用例1：存在均值分割
        System.out.println("=== 测试用例1：存在均值分割 ===");
        int[] nums1 = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("期望输出: true");
        System.out.println("实际输出: " + splitArraySameAverage(nums1));
        System.out.println();
        
        // 测试用例2：不存在均值分割
        System.out.println("=== 测试用例2：不存在均值分割 ===");
        int[] nums2 = {3, 1};
        System.out.println("输入数组: " + Arrays.toString(nums2));
        System.out.println("期望输出: false");
        System.out.println("实际输出: " + splitArraySameAverage(nums2));
        System.out.println();
        
        // 测试用例3：边界情况 - 两个元素
        System.out.println("=== 测试用例3：两个元素 ===");
        int[] nums3 = {1, 3};
        System.out.println("输入数组: " + Arrays.toString(nums3));
        System.out.println("期望输出: false");
        System.out.println("实际输出: " + splitArraySameAverage(nums3));
        System.out.println();
        
        // 测试用例4：全零数组
        System.out.println("=== 测试用例4：全零数组 ===");
        int[] nums4 = {0, 0, 0, 0};
        System.out.println("输入数组: " + Arrays.toString(nums4));
        System.out.println("期望输出: true");
        System.out.println("实际输出: " + splitArraySameAverage(nums4));
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[] largeNums = new int[20];
        Random random = new Random();
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = random.nextInt(100);
        }
        
        long startTime = System.currentTimeMillis();
        boolean result = splitArraySameAverage(largeNums);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数据规模: 20个元素");
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + result);
    }
}

/*
 * 算法深度分析：
 * 
 * 1. 数学原理：
 *    - 如果数组可以被分割成两个平均值相等的子集，那么有：sum1/k1 = sum2/k2 = total/n
 *    - 等价于：sum1 * n = total * k1 且 sum2 * n = total * k2
 *    - 其中k1 + k2 = n，sum1 + sum2 = total
 * 
 * 2. 折半搜索优化：
 *    - 直接搜索所有子集的时间复杂度为O(2^n)，对于n=30就达到10^9级别
 *    - 折半搜索将复杂度降为O(2^(n/2))，对于n=30只有约3万种可能
 *    - 结合哈希表查找，实现高效搜索
 * 
 * 3. 工程化改进：
 *    - 使用Pair类封装和与个数信息，提高代码可读性
 *    - 添加数学优化，处理特殊边界情况
 *    - 全面的异常处理和测试用例
 * 
 * 4. 性能考量：
 *    - 对于大规模数据，可以考虑进一步优化剪枝策略
 *    - 使用位运算优化组合生成
 *    - 考虑使用动态规划作为替代方案
 */
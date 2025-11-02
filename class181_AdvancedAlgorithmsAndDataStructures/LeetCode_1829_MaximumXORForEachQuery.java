package class185.game_of_life_problems;

import java.util.*;

/**
 * LeetCode 1829. 每个查询的最大异或值 (Maximum XOR for Each Query)
 * 题目链接：https://leetcode.com/problems/maximum-xor-for-each-query/
 * 
 * 题目描述：
 * 给定一个有序数组 nums，它由 n 个非负整数组成。同时给定一个整数 maximumBit。
 * 你需要执行以下查询 n 次：
 * 1. 找到非负整数 k < 2^maximumBit，使得 nums[0] XOR nums[1] XOR ... XOR nums[nums.length-1] XOR k 的结果最大化。
 * 2. k 是第 i 个查询的答案。
 * 3. 从当前数组 nums 中删除最后一个元素。
 * 
 * 返回一个数组 answer，其中 answer[i] 是第 i 个查询的答案。
 * 
 * 算法思路：
 * 1. 使用前缀异或和来优化计算
 * 2. 对于每个查询，我们需要最大化 prefixXOR ^ k，其中 k < 2^maximumBit
 * 3. 最大化的策略是让 k = (2^maximumBit - 1) ^ prefixXOR
 * 4. 这样 prefixXOR ^ k = prefixXOR ^ ((2^maximumBit - 1) ^ prefixXOR) = 2^maximumBit - 1
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 位运算优化：使用位运算代替幂运算
 * 2. 边界处理：maximumBit为0的情况
 * 3. 数组操作：从后向前处理避免删除操作
 */
public class LeetCode_1829_MaximumXORForEachQuery {
    
    /**
     * 主解法：使用前缀异或和
     * @param nums 输入数组
     * @param maximumBit 最大位数
     * @return 每个查询的答案数组
     */
    public int[] getMaximumXor(int[] nums, int maximumBit) {
        int n = nums.length;
        int[] answer = new int[n];
        
        // 计算前缀异或和
        int prefixXOR = 0;
        for (int num : nums) {
            prefixXOR ^= num;
        }
        
        // 计算最大值掩码
        int maxValue = (1 << maximumBit) - 1;
        
        // 从后向前处理（模拟删除最后一个元素）
        for (int i = 0; i < n; i++) {
            // 当前前缀异或和需要最大化的值
            // 我们需要找到 k 使得 prefixXOR ^ k 最大
            // 最大值为 maxValue，此时 k = prefixXOR ^ maxValue
            answer[i] = prefixXOR ^ maxValue;
            
            // 更新前缀异或和（删除最后一个元素）
            prefixXOR ^= nums[n - 1 - i];
        }
        
        return answer;
    }
    
    /**
     * 优化版本：使用位运算优化
     * @param nums 输入数组
     * @param maximumBit 最大位数
     * @return 每个查询的答案数组
     */
    public int[] getMaximumXorOptimized(int[] nums, int maximumBit) {
        int n = nums.length;
        int[] answer = new int[n];
        
        // 计算总异或和
        int totalXOR = 0;
        for (int num : nums) {
            totalXOR ^= num;
        }
        
        // 最大值掩码
        int mask = (1 << maximumBit) - 1;
        
        // 从后向前处理
        for (int i = 0; i < n; i++) {
            // 当前需要最大化的异或值
            // 最大值为 mask，对应的 k = totalXOR ^ mask
            answer[i] = totalXOR ^ mask;
            
            // 更新总异或和（移除最后一个元素）
            totalXOR ^= nums[n - 1 - i];
        }
        
        return answer;
    }
    
    /**
     * 暴力解法（用于对比验证）
     * 时间复杂度：O(n * 2^maximumBit)，不适用于大规模数据
     */
    public int[] getMaximumXorBruteForce(int[] nums, int maximumBit) {
        int n = nums.length;
        int[] answer = new int[n];
        
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        
        for (int i = 0; i < n; i++) {
            // 计算当前数组的异或和
            int currentXOR = 0;
            for (int num : list) {
                currentXOR ^= num;
            }
            
            // 暴力搜索最优 k
            int maxVal = -1;
            int bestK = 0;
            int maxK = (1 << maximumBit) - 1;
            
            for (int k = 0; k <= maxK; k++) {
                int val = currentXOR ^ k;
                if (val > maxVal) {
                    maxVal = val;
                    bestK = k;
                }
            }
            
            answer[i] = bestK;
            
            // 删除最后一个元素
            if (!list.isEmpty()) {
                list.remove(list.size() - 1);
            }
        }
        
        return answer;
    }
    
    /**
     * 单元测试
     */
    public static void main(String[] args) {
        LeetCode_1829_MaximumXORForEachQuery solution = new LeetCode_1829_MaximumXORForEachQuery();
        
        // 测试用例1
        System.out.println("=== 测试用例1 ===");
        int[] nums1 = {0, 1, 1, 3};
        int maximumBit1 = 2;
        int[] result1 = solution.getMaximumXor(nums1, maximumBit1);
        System.out.println("输入: nums = " + Arrays.toString(nums1) + ", maximumBit = " + maximumBit1);
        System.out.println("输出: " + Arrays.toString(result1));
        System.out.println("期望: [0, 3, 2, 3]");
        
        // 测试用例2
        System.out.println("\n=== 测试用例2 ===");
        int[] nums2 = {2, 3, 4, 7};
        int maximumBit2 = 3;
        int[] result2 = solution.getMaximumXor(nums2, maximumBit2);
        System.out.println("输入: nums = " + Arrays.toString(nums2) + ", maximumBit = " + maximumBit2);
        System.out.println("输出: " + Arrays.toString(result2));
        System.out.println("期望: [5, 2, 6, 5]");
        
        // 测试用例3：边界情况
        System.out.println("\n=== 测试用例3：边界情况 ===");
        int[] nums3 = {0};
        int maximumBit3 = 1;
        int[] result3 = solution.getMaximumXor(nums3, maximumBit3);
        System.out.println("输入: nums = " + Arrays.toString(nums3) + ", maximumBit = " + maximumBit3);
        System.out.println("输出: " + Arrays.toString(result3));
        System.out.println("期望: [1]");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] largeNums = new int[10000];
        Arrays.fill(largeNums, 1);
        long startTime = System.currentTimeMillis();
        int[] largeResult = solution.getMaximumXor(largeNums, 20);
        long endTime = System.currentTimeMillis();
        System.out.println("处理10000个元素耗时: " + (endTime - startTime) + "ms");
        
        // 验证优化版本
        System.out.println("\n=== 验证优化版本 ===");
        int[] result2Optimized = solution.getMaximumXorOptimized(nums2, maximumBit2);
        System.out.println("优化版本输出: " + Arrays.toString(result2Optimized));
        System.out.println("结果一致: " + Arrays.equals(result2, result2Optimized));
    }
}
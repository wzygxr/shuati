package class049;

import java.util.*;

/**
 * 992. K 个不同整数的子数组
 * 给定一个正整数数组 nums 和一个整数 k，返回 nums 中「好子数组」的数目。
 * 如果某个子数组中不同整数的个数恰好为 k，则称其为「好子数组」。
 * 
 * 解题思路：
 * 使用滑动窗口的变种：恰好K个不同整数的子数组数量 = 最多K个不同整数的子数组数量 - 最多K-1个不同整数的子数组数量
 * 
 * 时间复杂度：O(n)，其中n是数组长度
 * 空间复杂度：O(k)，用于存储不同整数的哈希表
 * 
 * 是否最优解：是
 * 
 * 测试链接：https://leetcode.cn/problems/subarrays-with-k-different-integers/
 */
public class Code19_SubarraysWithKDifferentIntegers {
    
    /**
     * 计算恰好包含K个不同整数的子数组数量
     * 
     * @param nums 输入数组
     * @param k 不同整数的个数
     * @return 恰好包含K个不同整数的子数组数量
     */
    public static int subarraysWithKDistinct(int[] nums, int k) {
        // 恰好K个不同 = 最多K个不同 - 最多K-1个不同
        return atMostKDistinct(nums, k) - atMostKDistinct(nums, k - 1);
    }
    
    /**
     * 计算最多包含K个不同整数的子数组数量
     * 
     * @param nums 输入数组
     * @param k 最多不同整数的个数
     * @return 最多包含K个不同整数的子数组数量
     */
    private static int atMostKDistinct(int[] nums, int k) {
        if (k < 0) {
            return 0;
        }
        
        int n = nums.length;
        int count = 0; // 子数组数量
        int left = 0; // 窗口左边界
        Map<Integer, Integer> freq = new HashMap<>(); // 记录每个数字的出现频率
        
        // 滑动窗口右边界
        for (int right = 0; right < n; right++) {
            // 添加右边界元素
            freq.put(nums[right], freq.getOrDefault(nums[right], 0) + 1);
            
            // 如果不同数字数量超过k，收缩左边界
            while (freq.size() > k) {
                // 移除左边界元素
                freq.put(nums[left], freq.get(nums[left]) - 1);
                if (freq.get(nums[left]) == 0) {
                    freq.remove(nums[left]);
                }
                left++;
            }
            
            // 以right结尾的，满足条件的子数组数量为 right - left + 1
            count += right - left + 1;
        }
        
        return count;
    }
    
    /**
     * 直接解法：使用双指针和哈希表
     * 时间复杂度：O(n)，空间复杂度：O(k)
     */
    public static int subarraysWithKDistinctDirect(int[] nums, int k) {
        int n = nums.length;
        int count = 0;
        
        // 记录每个数字最后一次出现的位置
        Map<Integer, Integer> lastSeen = new HashMap<>();
        int left = 0; // 窗口左边界
        int right = 0; // 窗口右边界
        
        while (right < n) {
            // 更新当前数字的最后出现位置
            lastSeen.put(nums[right], right);
            
            // 如果不同数字数量超过k，移动左边界
            while (lastSeen.size() > k) {
                // 如果左边界数字的最后出现位置就是当前位置，从map中移除
                if (lastSeen.get(nums[left]) == left) {
                    lastSeen.remove(nums[left]);
                }
                left++;
            }
            
            // 如果恰好有k个不同数字，计算以right结尾的子数组数量
            if (lastSeen.size() == k) {
                // 找到最小的位置，使得从该位置到right的子数组恰好有k个不同数字
                int minIndex = right;
                for (int index : lastSeen.values()) {
                    minIndex = Math.min(minIndex, index);
                }
                count += minIndex - left + 1;
            }
            
            right++;
        }
        
        return count;
    }
    
    /**
     * 优化版本：使用数组代替哈希表（当数字范围有限时）
     * 时间复杂度：O(n)，空间复杂度：O(max_value)
     */
    public static int subarraysWithKDistinctOptimized(int[] nums, int k) {
        int n = nums.length;
        if (n == 0 || k == 0) {
            return 0;
        }
        
        // 找到数组中的最大值，用于确定数组大小
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        int[] freq = new int[maxVal + 1]; // 频率数组
        int distinct = 0; // 当前不同数字的数量
        int count = 0;
        int left = 0;
        
        // 使用双指针技巧
        for (int right = 0; right < n; right++) {
            // 添加右边界元素
            if (freq[nums[right]] == 0) {
                distinct++;
            }
            freq[nums[right]]++;
            
            // 收缩左边界，直到不同数字数量不超过k
            while (distinct > k) {
                freq[nums[left]]--;
                if (freq[nums[left]] == 0) {
                    distinct--;
                }
                left++;
            }
            
            // 如果恰好有k个不同数字，计算数量
            if (distinct == k) {
                int tempLeft = left;
                int tempDistinct = distinct;
                int[] tempFreq = freq.clone();
                
                // 计算以right结尾的恰好k个不同的子数组数量
                while (tempDistinct == k) {
                    count++;
                    tempFreq[nums[tempLeft]]--;
                    if (tempFreq[nums[tempLeft]] == 0) {
                        tempDistinct--;
                    }
                    tempLeft++;
                }
            }
        }
        
        return count;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 1, 2, 3};
        int k1 = 2;
        int result1 = subarraysWithKDistinct(nums1, k1);
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("k = " + k1);
        System.out.println("恰好包含" + k1 + "个不同整数的子数组数量: " + result1);
        System.out.println("预期: 7");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1, 2, 1, 3, 4};
        int k2 = 3;
        int result2 = subarraysWithKDistinct(nums2, k2);
        System.out.println("输入数组: " + Arrays.toString(nums2));
        System.out.println("k = " + k2);
        System.out.println("恰好包含" + k2 + "个不同整数的子数组数量: " + result2);
        System.out.println("预期: 3");
        System.out.println();
        
        // 测试用例3：边界情况，k=0
        int[] nums3 = {1, 2, 3};
        int k3 = 0;
        int result3 = subarraysWithKDistinct(nums3, k3);
        System.out.println("输入数组: " + Arrays.toString(nums3));
        System.out.println("k = " + k3);
        System.out.println("恰好包含" + k3 + "个不同整数的子数组数量: " + result3);
        System.out.println("预期: 0");
        System.out.println();
        
        // 测试用例4：k=1
        int[] nums4 = {1, 1, 1, 2, 2, 3};
        int k4 = 1;
        int result4 = subarraysWithKDistinct(nums4, k4);
        System.out.println("输入数组: " + Arrays.toString(nums4));
        System.out.println("k = " + k4);
        System.out.println("恰好包含" + k4 + "个不同整数的子数组数量: " + result4);
        System.out.println("预期: 9");
        System.out.println();
        
        // 测试用例5：k等于数组长度
        int[] nums5 = {1, 2, 3, 4, 5};
        int k5 = 5;
        int result5 = subarraysWithKDistinct(nums5, k5);
        System.out.println("输入数组: " + Arrays.toString(nums5));
        System.out.println("k = " + k5);
        System.out.println("恰好包含" + k5 + "个不同整数的子数组数量: " + result5);
        System.out.println("预期: 1");
    }
}
package class008_AdvancedAlgorithmsAndDataStructures.difference_array_problems;

import java.util.*;

/**
 * LeetCode 995. Minimum Number of K Consecutive Bit Flips
 * 
 * 题目描述：
 * 给定一个二进制数组 nums 和一个整数 k。
 * k位翻转包括选择一个长度为 k 的连续子数组，然后将子数组中的每个 0 更改为 1，
 * 每个 1 更改为 0。返回数组中不存在 0 所需的 k 位翻转的最小次数。
 * 如果不可能，则返回 -1。
 * 
 * 解题思路：
 * 使用差分数组来优化翻转操作的记录。
 * 我们从左到右遍历数组，当遇到 0 时（考虑之前的翻转影响后实际为 0），
 * 就需要进行一次翻转操作。使用差分数组记录翻转操作的影响范围。
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
public class LeetCode_995_MinimumNumberOfKConsecutiveBitFlips {
    
    static class Solution {
        public int minKBitFlips(int[] nums, int k) {
            int n = nums.length;
            int[] diff = new int[n + 1]; // 差分数组
            int flips = 0; // 当前位置受到的翻转次数
            int result = 0; // 总翻转次数
            
            for (int i = 0; i < n; i++) {
                flips += diff[i]; // 更新当前位置的翻转次数
                
                // 计算当前位置的实际值
                // 如果 flips 是偶数，值不变；如果 flips 是奇数，值翻转
                int actualValue = (flips % 2 == 0) ? nums[i] : 1 - nums[i];
                
                // 如果实际值是 0，需要进行翻转
                if (actualValue == 0) {
                    // 检查是否能进行 k 位翻转（不越界）
                    if (i + k > n) {
                        return -1; // 无法完成翻转
                    }
                    
                    // 进行翻转操作
                    result++;
                    flips++; // 当前位置翻转次数加1
                    diff[i + k]--; // 在 i+k 位置减1，表示翻转影响结束
                }
            }
            
            return result;
        }
        
        // 另一种实现方式：使用滑动窗口
        public int minKBitFlips2(int[] nums, int k) {
            int n = nums.length;
            int[] flipped = new int[n]; // 记录每个位置是否被翻转
            int flips = 0; // 当前窗口内的翻转次数
            int result = 0;
            
            for (int i = 0; i < n; i++) {
                // 如果超出了窗口大小，需要移除窗口左边的翻转影响
                if (i >= k) {
                    flips -= flipped[i - k];
                }
                
                // 计算当前位置的实际值
                int actualValue = (flips % 2 == 0) ? nums[i] : 1 - nums[i];
                
                // 如果实际值是 0，需要进行翻转
                if (actualValue == 0) {
                    // 检查是否能进行 k 位翻转（不越界）
                    if (i + k > n) {
                        return -1; // 无法完成翻转
                    }
                    
                    // 进行翻转操作
                    result++;
                    flips++;
                    flipped[i] = 1; // 标记当前位置被翻转
                }
            }
            
            return result;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[] nums1 = {0,1,0};
        int k1 = 1;
        System.out.println("测试用例1:");
        System.out.println("数组: " + Arrays.toString(nums1));
        System.out.println("k = " + k1);
        System.out.println("最少翻转次数: " + solution.minKBitFlips(nums1, k1));
        System.out.println("另一种解法结果: " + solution.minKBitFlips2(nums1, k1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1,1,0};
        int k2 = 2;
        System.out.println("测试用例2:");
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("k = " + k2);
        System.out.println("最少翻转次数: " + solution.minKBitFlips(nums2, k2));
        System.out.println("另一种解法结果: " + solution.minKBitFlips2(nums2, k2));
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {0,0,0,1,0,1,1,0};
        int k3 = 3;
        System.out.println("测试用例3:");
        System.out.println("数组: " + Arrays.toString(nums3));
        System.out.println("k = " + k3);
        System.out.println("最少翻转次数: " + solution.minKBitFlips(nums3, k3));
        System.out.println("另一种解法结果: " + solution.minKBitFlips2(nums3, k3));
        System.out.println();
        
        // 测试用例4：无法完成翻转
        int[] nums4 = {1,1,0};
        int k4 = 4;
        System.out.println("测试用例4:");
        System.out.println("数组: " + Arrays.toString(nums4));
        System.out.println("k = " + k4);
        System.out.println("最少翻转次数: " + solution.minKBitFlips(nums4, k4));
        System.out.println("另一种解法结果: " + solution.minKBitFlips2(nums4, k4));
    }
}
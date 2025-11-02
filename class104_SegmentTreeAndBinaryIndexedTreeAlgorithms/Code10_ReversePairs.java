package class131;

import java.util.*;

/** 
 * LeetCode 493. Reverse Pairs (翻转对)
 * 题目链接: https://leetcode.cn/problems/reverse-pairs/
 * 
 * 题目描述: 
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 解题思路:
 * 使用归并排序的思想，在归并过程中统计翻转对数量
 * 1. 分治处理左右两部分数组
 * 2. 在合并前，统计跨越两部分的翻转对数量
 * 3. 正常执行归并排序过程
 * 
 * 时间复杂度分析:
 * - 整体框架基于归并排序: O(n log n)
 * - 统计翻转对: 每层统计操作为O(n)，共log n层
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n) 归并排序需要额外的数组空间
 */
public class Code10_ReversePairs {
    
    private int count = 0;  // 翻转对计数器
    
    /** 
     * 计算数组中重要翻转对的数量
     * 
     * @param nums 输入数组
     * @return     重要翻转对的数量
     */
    public int reversePairs(int[] nums) {
        count = 0;
        mergeSort(nums, 0, nums.length - 1);
        return count;
    }
    
    /** 
     * 归并排序主函数
     * 递归地将数组分成两部分，分别处理后再合并
     * 
     * @param nums  输入数组
     * @param left  左边界
     * @param right 右边界
     */
    private void mergeSort(int[] nums, int left, int right) {
        // 递归终止条件：子数组只有一个元素或为空
        if (left >= right) {
            return;
        }
        
        // 计算中点，避免溢出
        int mid = left + (right - left) / 2;
        // 递归处理左右两部分
        mergeSort(nums, left, mid);
        mergeSort(nums, mid + 1, right);
        
        // 在合并前，统计跨越两部分的翻转对数量
        countPairs(nums, left, mid, right);
        
        // 合并两个有序数组
        merge(nums, left, mid, right);
    }
    
    /** 
     * 统计跨越两部分的翻转对数量
     * 对于左半部分的每个元素nums[i]，统计右半部分有多少个元素nums[j]满足nums[i] > 2*nums[j]
     * 
     * @param nums  输入数组
     * @param left  左半部分起始位置
     * @param mid   左半部分结束位置
     * @param right 右半部分结束位置
     */
    private void countPairs(int[] nums, int left, int mid, int right) {
        int j = mid + 1;
        // 对于左半部分的每个元素nums[i]
        for (int i = left; i <= mid; i++) {
            // 找到第一个不满足nums[i] > 2*nums[j]的位置
            // 使用long类型避免溢出
            while (j <= right && (long) nums[i] > 2 * (long) nums[j]) {
                j++;
            }
            // j之前的元素都满足条件，即[mid+1, j-1]范围内的元素
            count += j - (mid + 1);
        }
    }
    
    /** 
     * 合并两个有序数组
     * 将nums[left..mid]和nums[mid+1..right]合并成一个有序数组
     * 
     * @param nums  输入数组
     * @param left  左半部分起始位置
     * @param mid   左半部分结束位置
     * @param right 右半部分结束位置
     */
    private void merge(int[] nums, int left, int mid, int right) {
        // 创建临时数组存储合并结果
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        // 合并过程：比较两个子数组的元素，将较小的元素放入临时数组
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        
        // 处理左半部分剩余元素
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        // 处理右半部分剩余元素
        while (j <= right) {
            temp[k++] = nums[j++];
        }
        
        // 将合并结果复制回原数组
        for (int p = 0; p < temp.length; p++) {
            nums[left + p] = temp[p];
        }
    }
    
    /** 
     * 测试方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Code10_ReversePairs solution = new Code10_ReversePairs();
        
        // 测试用例1
        int[] nums1 = {1, 3, 2, 3, 1};
        System.out.println("Input: " + Arrays.toString(nums1));
        System.out.println("Output: " + solution.reversePairs(nums1)); // 应该输出2
        
        // 测试用例2
        int[] nums2 = {2, 4, 3, 5, 1};
        System.out.println("Input: " + Arrays.toString(nums2));
        System.out.println("Output: " + solution.reversePairs(nums2)); // 应该输出3
        
        // 测试用例3
        int[] nums3 = {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647};
        System.out.println("Input: " + Arrays.toString(nums3));
        System.out.println("Output: " + solution.reversePairs(nums3)); // 应该输出0
    }
}
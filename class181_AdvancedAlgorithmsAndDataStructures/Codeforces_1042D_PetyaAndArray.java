package class008_AdvancedAlgorithmsAndDataStructures.closest_pair_problems;

import java.util.*;

/**
 * Codeforces 1042D Petya and Array
 * 
 * 题目描述：
 * 给定一个包含n个整数的数组a和一个整数t，找出有多少个连续子数组的和严格小于t。
 * 
 * 解题思路：
 * 这个问题可以转化为前缀和问题。设prefix[i]表示前i个元素的和，那么子数组a[l..r]的和等于
 * prefix[r] - prefix[l-1]。我们需要找出有多少对(l,r)满足prefix[r] - prefix[l-1] < t，
 * 即prefix[l-1] > prefix[r] - t。
 * 
 * 我们可以使用平面分治算法的思想来解决这个问题，将问题转化为最近点对问题的变种。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
public class Codeforces_1042D_PetyaAndArray {
    
    static class Solution {
        public long countSubarrays(int[] a, int t) {
            int n = a.length;
            long[] prefix = new long[n + 1];
            
            // 计算前缀和
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + a[i];
            }
            
            // 使用归并排序的思想计算满足条件的子数组数量
            return mergeSortAndCount(prefix, 0, n, t);
        }
        
        private long mergeSortAndCount(long[] prefix, int left, int right, int t) {
            if (left >= right) {
                return 0;
            }
            
            int mid = left + (right - left) / 2;
            long count = 0;
            
            // 递归计算左半部分和右半部分的答案
            count += mergeSortAndCount(prefix, left, mid, t);
            count += mergeSortAndCount(prefix, mid + 1, right, t);
            
            // 计算跨越中点的子数组数量
            count += countCrossing(prefix, left, mid, right, t);
            
            // 合并两个有序数组
            merge(prefix, left, mid, right);
            
            return count;
        }
        
        private long countCrossing(long[] prefix, int left, int mid, int right, int t) {
            long count = 0;
            
            // 对于右半部分的每个元素，计算左半部分有多少元素满足条件
            for (int j = mid + 1; j <= right; j++) {
                // 我们需要找到左半部分中满足 prefix[i] > prefix[j] - t 的元素数量
                // 即找到左半部分中大于 prefix[j] - t 的元素数量
                double target = prefix[j] - t;
                count += countGreaterThan(prefix, left, mid, target);
            }
            
            return count;
        }
        
        private int countGreaterThan(long[] arr, int left, int right, double target) {
            // 在有序数组arr[left..right]中找到大于target的元素数量
            // 使用二分查找
            int low = left, high = right + 1;
            
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (arr[mid] > target) {
                    high = mid;
                } else {
                    low = mid + 1;
                }
            }
            
            return right + 1 - low;
        }
        
        private void merge(long[] prefix, int left, int mid, int right) {
            long[] temp = new long[right - left + 1];
            int i = left, j = mid + 1, k = 0;
            
            while (i <= mid && j <= right) {
                if (prefix[i] <= prefix[j]) {
                    temp[k++] = prefix[i++];
                } else {
                    temp[k++] = prefix[j++];
                }
            }
            
            while (i <= mid) {
                temp[k++] = prefix[i++];
            }
            
            while (j <= right) {
                temp[k++] = prefix[j++];
            }
            
            for (i = 0; i < temp.length; i++) {
                prefix[left + i] = temp[i];
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[] a1 = {5, -1, 4, -2, 3};
        int t1 = 6;
        System.out.println("测试用例1:");
        System.out.println("数组: " + Arrays.toString(a1));
        System.out.println("t = " + t1);
        System.out.println("结果: " + solution.countSubarrays(a1, t1));
        System.out.println();
        
        // 测试用例2
        int[] a2 = {-1, 2, -3, 4, -5};
        int t2 = 0;
        System.out.println("测试用例2:");
        System.out.println("数组: " + Arrays.toString(a2));
        System.out.println("t = " + t2);
        System.out.println("结果: " + solution.countSubarrays(a2, t2));
        System.out.println();
        
        // 测试用例3
        int[] a3 = {1, 2, 3, 4, 5};
        int t3 = 10;
        System.out.println("测试用例3:");
        System.out.println("数组: " + Arrays.toString(a3));
        System.out.println("t = " + t3);
        System.out.println("结果: " + solution.countSubarrays(a3, t3));
    }
}
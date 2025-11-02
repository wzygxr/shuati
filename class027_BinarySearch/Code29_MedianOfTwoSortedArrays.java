package class051;

/**
 * LeetCode 4. 寻找两个正序数组的中位数
 * 问题描述：找出两个有序数组的中位数
 * 解法：二分答案 + 二分查找
 * 时间复杂度：O(log(min(m,n)))
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
 * 
 * 解题思路：
 * 1. 确保第一个数组长度不大于第二个数组，简化问题
 * 2. 使用二分搜索在较短的数组中找到合适的分割点
 * 3. 根据分割点确定两个数组的划分，使得左边元素数量等于右边
 * 4. 根据划分计算中位数
 */
public class Code29_MedianOfTwoSortedArrays {
    
    /**
     * 计算两个有序数组的中位数
     * @param nums1 第一个有序数组
     * @param nums2 第二个有序数组
     * @return 中位数
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // 确保nums1是较短的数组
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        
        int m = nums1.length;
        int n = nums2.length;
        int totalLeft = (m + n + 1) / 2; // 左边应有的元素数量
        
        int left = 0;
        int right = m;
        
        while (left <= right) {
            // i表示nums1在分割点左边的元素数量
            int i = left + ((right - left) >> 1);
            // j表示nums2在分割点左边的元素数量
            int j = totalLeft - i;
            
            // nums1左边最大值（如果左边没有元素，设为最小）
            int nums1LeftMax = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            // nums1右边最小值（如果右边没有元素，设为最大）
            int nums1RightMin = (i == m) ? Integer.MAX_VALUE : nums1[i];
            // nums2左边最大值
            int nums2LeftMax = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            // nums2右边最小值
            int nums2RightMin = (j == n) ? Integer.MAX_VALUE : nums2[j];
            
            if (nums1LeftMax <= nums2RightMin && nums2LeftMax <= nums1RightMin) {
                // 找到正确的分割点
                if ((m + n) % 2 == 1) {
                    // 奇数个元素，中位数是左边最大值
                    return Math.max(nums1LeftMax, nums2LeftMax);
                } else {
                    // 偶数个元素，中位数是左边最大值和右边最小值的平均
                    return (Math.max(nums1LeftMax, nums2LeftMax) + 
                           Math.min(nums1RightMin, nums2RightMin)) / 2.0;
                }
            } else if (nums1LeftMax > nums2RightMin) {
                // nums1左边太大，需要减小i
                right = i - 1;
            } else {
                // nums1左边太小，需要增大i
                left = i + 1;
            }
        }
        
        return 0.0;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(log(min(m,n)))
     *   - 二分搜索在较短的数组上进行，搜索次数为O(log(min(m,n)))
     *   - 每次二分操作都是常数时间
     *   - 总时间复杂度为O(log(min(m,n)))
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 边界条件处理：处理空数组的情况
     * 2. 整数溢出：使用位运算避免溢出
     * 3. 分割点验证：需要验证分割点是否满足条件
     * 
     * 测试用例：
     * - 输入：nums1 = [1,3], nums2 = [2]
     * - 输出：2.0
     * - 输入：nums1 = [1,2], nums2 = [3,4]
     * - 输出：2.5
     */
}

/**
 * C++ 实现
 */
/*
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class Solution {
public:
    double findMedianSortedArrays(vector<int>& nums1, vector<int>& nums2) {
        if (nums1.size() > nums2.size()) {
            return findMedianSortedArrays(nums2, nums1);
        }
        
        int m = nums1.size();
        int n = nums2.size();
        int totalLeft = (m + n + 1) / 2;
        
        int left = 0;
        int right = m;
        
        while (left <= right) {
            int i = left + (right - left) / 2;
            int j = totalLeft - i;
            
            int nums1LeftMax = (i == 0) ? INT_MIN : nums1[i - 1];
            int nums1RightMin = (i == m) ? INT_MAX : nums1[i];
            int nums2LeftMax = (j == 0) ? INT_MIN : nums2[j - 1];
            int nums2RightMin = (j == n) ? INT_MAX : nums2[j];
            
            if (nums1LeftMax <= nums2RightMin && nums2LeftMax <= nums1RightMin) {
                if ((m + n) % 2 == 1) {
                    return max(nums1LeftMax, nums2LeftMax);
                } else {
                    return (max(nums1LeftMax, nums2LeftMax) + min(nums1RightMin, nums2RightMin)) / 2.0;
                }
            } else if (nums1LeftMax > nums2RightMin) {
                right = i - 1;
            } else {
                left = i + 1;
            }
        }
        
        return 0.0;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:
        if len(nums1) > len(nums2):
            return self.findMedianSortedArrays(nums2, nums1)
        
        m, n = len(nums1), len(nums2)
        total_left = (m + n + 1) // 2
        
        left, right = 0, m
        
        while left <= right:
            i = left + (right - left) // 2
            j = total_left - i
            
            nums1_left_max = float('-inf') if i == 0 else nums1[i - 1]
            nums1_right_min = float('inf') if i == m else nums1[i]
            nums2_left_max = float('-inf') if j == 0 else nums2[j - 1]
            nums2_right_min = float('inf') if j == n else nums2[j]
            
            if nums1_left_max <= nums2_right_min and nums2_left_max <= nums1_right_min:
                if (m + n) % 2 == 1:
                    return max(nums1_left_max, nums2_left_max)
                else:
                    return (max(nums1_left_max, nums2_left_max) + min(nums1_right_min, nums2_right_min)) / 2.0
            elif nums1_left_max > nums2_right_min:
                right = i - 1
            else:
                left = i + 1
                
        return 0.0
*/
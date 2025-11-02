package class051;

/**
 * LeetCode 35. 搜索插入位置
 * 问题描述：找到目标值在排序数组中的插入位置
 * 解法：二分搜索
 * 时间复杂度：O(log n)
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/search-insert-position/
 * 
 * 解题思路：
 * 1. 使用标准的二分搜索寻找目标值
 * 2. 如果找到目标值，返回其索引
 * 3. 如果没找到，返回应该插入的位置（left指针的位置）
 * 4. 这是二分搜索的经典应用之一
 */
public class Code20_SearchInsertPosition {
    
    /**
     * 搜索目标值的插入位置
     * @param nums 排序数组
     * @param target 目标值
     * @return 目标值的索引或应该插入的位置
     */
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] == target) {
                // 找到目标值，返回索引
                return mid;
            } else if (nums[mid] < target) {
                // 目标值在右侧
                left = mid + 1;
            } else {
                // 目标值在左侧
                right = mid - 1;
            }
        }
        
        // 没找到目标值，返回应该插入的位置
        return left;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(log n)
     *   - 二分搜索每次将搜索范围减半
     *   - 搜索次数为O(log n)
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 标准二分搜索模板：使用经典的二分搜索实现
     * 2. 插入位置：当目标值不存在时，left指针指向应该插入的位置
     * 3. 边界条件：处理空数组和边界情况
     * 
     * 测试用例：
     * - 输入：nums = [1,3,5,6], target = 5
     * - 输出：2
     * - 输入：nums = [1,3,5,6], target = 2
     * - 输出：1
     * - 输入：nums = [1,3,5,6], target = 7
     * - 输出：4
     */
}

/**
 * C++ 实现
 */
/*
#include <vector>
using namespace std;

class Solution {
public:
    int searchInsert(vector<int>& nums, int target) {
        int left = 0;
        int right = nums.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return left;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def searchInsert(self, nums: List[int], target: int) -> int:
        left = 0
        right = len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
                
        return left
*/
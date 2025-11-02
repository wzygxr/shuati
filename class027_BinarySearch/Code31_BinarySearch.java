package class051;

/**
 * AizuOJ ALDS1_4_B - Binary Search
 * 问题描述：二分查找的基本实现
 * 解法：二分搜索
 * 时间复杂度：O(log n)
 * 空间复杂度：O(1)
 * 链接：https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_4_B
 * 
 * 解题思路：
 * 1. 标准的二分查找算法实现
 * 2. 在有序数组中查找目标值
 * 3. 返回目标值的索引，如果不存在返回-1
 */
public class Code31_BinarySearch {
    
    /**
     * 在有序数组中二分查找目标值
     * @param nums 有序数组（升序）
     * @param target 目标值
     * @return 目标值的索引，如果不存在返回-1
     */
    public int binarySearch(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
    
    /**
     * 统计有序数组中目标值的出现次数
     * @param nums 有序数组（升序）
     * @param target 目标值
     * @return 目标值出现的次数
     */
    public int countOccurrences(int[] nums, int target) {
        // 找到第一个等于target的位置
        int first = findFirst(nums, target);
        if (first == -1) {
            return 0;
        }
        
        // 找到最后一个等于target的位置
        int last = findLast(nums, target);
        
        return last - first + 1;
    }
    
    /**
     * 找到第一个等于target的位置
     * @param nums 有序数组
     * @param target 目标值
     * @return 第一个等于target的索引，不存在返回-1
     */
    private int findFirst(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] >= target) {
                if (nums[mid] == target) {
                    result = mid;
                }
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 找到最后一个等于target的位置
     * @param nums 有序数组
     * @param target 目标值
     * @return 最后一个等于target的索引，不存在返回-1
     */
    private int findLast(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] <= target) {
                if (nums[mid] == target) {
                    result = mid;
                }
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
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
     * 1. 边界条件处理：处理空数组和边界情况
     * 2. 整数溢出：使用位运算避免溢出
     * 3. 重复元素处理：提供统计出现次数的方法
     * 
     * 测试用例：
     * - 输入：nums = [1,2,3,4,5], target = 3
     * - 输出：2（索引）
     * - 输入：nums = [1,2,2,2,3], target = 2
     * - 输出：出现3次
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
    int binarySearch(vector<int>& nums, int target) {
        if (nums.empty()) return -1;
        
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
        
        return -1;
    }
    
    int countOccurrences(vector<int>& nums, int target) {
        int first = findFirst(nums, target);
        if (first == -1) return 0;
        
        int last = findLast(nums, target);
        return last - first + 1;
    }
    
private:
    int findFirst(vector<int>& nums, int target) {
        int left = 0;
        int right = nums.size() - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] >= target) {
                if (nums[mid] == target) {
                    result = mid;
                }
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    int findLast(vector<int>& nums, int target) {
        int left = 0;
        int right = nums.size() - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] <= target) {
                if (nums[mid] == target) {
                    result = mid;
                }
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def binary_search(self, nums: List[int], target: int) -> int:
        if not nums:
            return -1
            
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
                
        return -1
    
    def count_occurrences(self, nums: List[int], target: int) -> int:
        first = self.find_first(nums, target)
        if first == -1:
            return 0
            
        last = self.find_last(nums, target)
        return last - first + 1
    
    def find_first(self, nums: List[int], target: int) -> int:
        left, right = 0, len(nums) - 1
        result = -1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] >= target:
                if nums[mid] == target:
                    result = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return result
    
    def find_last(self, nums: List[int], target: int) -> int:
        left, right = 0, len(nums) - 1
        result = -1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] <= target:
                if nums[mid] == target:
                    result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
*/
package class051;

/**
 * LeetCode 287. 寻找重复数
 * 问题描述：找出数组中重复的数（数组长度为n+1，元素值在1到n之间，且只有一个重复数）
 * 解法：二分答案 + 抽屉原理
 * 时间复杂度：O(n * log n)
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/find-the-duplicate-number/
 * 
 * 解题思路：
 * 1. 利用抽屉原理：如果有n+1个物品放进n个抽屉，那么至少有一个抽屉有至少两个物品
 * 2. 对数值范围[1,n]进行二分搜索
 * 3. 对于每个中间值mid，计算数组中小于等于mid的元素个数
 * 4. 如果count > mid，说明[1,mid]范围内有重复数
 */
public class Code28_FindTheDuplicateNumber {
    
    /**
     * 找出数组中重复的数
     * @param nums 输入数组
     * @return 重复的数
     */
    public int findDuplicate(int[] nums) {
        int n = nums.length - 1; // 数组长度为n+1，元素值在1到n之间
        
        // 确定二分搜索的范围
        int left = 1;
        int right = n;
        int result = -1;
        
        // 二分搜索重复数
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 计算数组中小于等于mid的元素个数
            int count = countLessEqual(nums, mid);
            
            // 应用抽屉原理：如果count > mid，说明[1,mid]范围内有重复数
            if (count > mid) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 计算数组中小于等于target的元素个数
     * @param nums 输入数组
     * @param target 目标值
     * @return 小于等于target的元素个数
     */
    private int countLessEqual(int[] nums, int target) {
        int count = 0;
        for (int num : nums) {
            if (num <= target) {
                count++;
            }
        }
        return count;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log n)
     *   - 二分搜索范围是[1, n]，二分次数为O(log n)
     *   - 每次二分需要遍历数组一次，时间复杂度为O(n)
     *   - 总时间复杂度为O(n * log n)
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 抽屉原理应用：这是解决此类问题的关键数学原理
     * 2. 不修改原数组：符合题目要求
     * 3. 边界条件：处理数组长度为1的特殊情况
     * 
     * 测试用例：
     * - 输入：nums = [1,3,4,2,2]
     * - 输出：2
     * - 解释：数组中有5个元素，值在1-4之间，重复数是2
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
    int findDuplicate(vector<int>& nums) {
        int n = nums.size() - 1;
        int left = 1;
        int right = n;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int count = 0;
            
            for (int num : nums) {
                if (num <= mid) {
                    count++;
                }
            }
            
            if (count > mid) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
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
    def findDuplicate(self, nums: List[int]) -> int:
        n = len(nums) - 1
        left = 1
        right = n
        result = -1
        
        while left <= right:
            mid = left + (right - left) // 2
            count = sum(1 for num in nums if num <= mid)
            
            if count > mid:
                result = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return result
*/
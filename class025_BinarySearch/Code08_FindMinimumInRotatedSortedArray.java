import java.util.Arrays;

/**
 * 二分查找算法：旋转排序数组中的最小值查找
 * 
 * 本文件包含旋转排序数组中查找最小值的多种变体实现，包括：
 * 1. 无重复元素的旋转数组最小值查找
 * 2. 有重复元素的旋转数组最小值查找  
 * 3. 在旋转数组中搜索目标值
 * 4. 多语言实现（Java、C++、Python）
 * 
 * 时间复杂度：O(log n) 最优解
 * 空间复杂度：O(1) 原地算法
 * 
 * @author 算法专家
 * @version 1.0
 * @date 2025-10-18
 */
public class Code08_FindMinimumInRotatedSortedArray {
    
    /**
     * LeetCode 153. 寻找旋转排序数组中的最小值 - Find Minimum in Rotated Sorted Array
     * 题目链接：https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/
     * 
     * 解题思路：
     * 1. 旋转排序数组的特点是：数组被分成两个递增区间
     * 2. 最小值一定在第二个递增区间的开始位置
     * 3. 通过比较中间元素和最右元素来判断最小值在哪一侧
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     * 最优解：✅ 是最优解
     */
    public static int findMin(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        // 二分查找
        while (left < right) {
            int mid = left + ((right - left) >> 1); // 防止整数溢出
            
            // 中间元素大于最右元素，说明最小值在右半部分
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } 
            // 中间元素小于最右元素，说明最小值在左半部分（可能是mid）
            else {
                right = mid;
            }
        }
        
        // 循环结束时left == right，指向的就是最小值
        return nums[left];
    }
    
    /**
     * LeetCode 154. 寻找旋转排序数组中的最小值 II - Find Minimum in Rotated Sorted Array II
     * 题目链接：https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array-ii/
     * 
     * 解题思路：
     * 1. 处理有重复元素的情况
     * 2. 当中间元素等于最右元素时，无法判断最小值在哪一侧
     * 3. 此时只能缩小右边界，逐步逼近最小值
     * 
     * 时间复杂度：平均O(log n)，最坏O(n)（当所有元素都相同时）
     * 空间复杂度：O(1)
     * 最优解：✅ 是最优解
     */
    public static int findMinWithDuplicates(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        // 二分查找
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            // 中间元素大于最右元素，说明最小值在右半部分
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } 
            // 中间元素小于最右元素，说明最小值在左半部分（可能是mid）
            else if (nums[mid] < nums[right]) {
                right = mid;
            }
            // 中间元素等于最右元素，无法判断最小值在左边还是右边，只能缩小右边界
            else {
                right--;
            }
        }
        
        // 循环结束时left == right，指向的就是最小值
        return nums[left];
    }
    
    /**
     * LeetCode 33. 搜索旋转排序数组 - Search in Rotated Sorted Array
     * 题目链接：https://leetcode.cn/problems/search-in-rotated-sorted-array/
     * 
     * 解题思路：
     * 1. 先判断mid所在的区间是递增还是递减
     * 2. 根据目标值的位置决定搜索方向
     * 3. 处理旋转数组的特殊情况
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     * 最优解：✅ 是最优解
     */
    public static int search(int[] nums, int target) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 找到目标值
            if (nums[mid] == target) {
                return mid;
            }
            
            // 判断mid所在的区间是递增还是递减
            if (nums[left] <= nums[mid]) {
                // 左半部分是递增区间
                if (nums[left] <= target && target < nums[mid]) {
                    // 目标在左半部分
                    right = mid - 1;
                } else {
                    // 目标在右半部分
                    left = mid + 1;
                }
            } else {
                // 右半部分是递增区间
                if (nums[mid] < target && target <= nums[right]) {
                    // 目标在右半部分
                    left = mid + 1;
                } else {
                    // 目标在左半部分
                    right = mid - 1;
                }
            }
        }
        
        // 未找到目标值
        return -1;
    }
    
    /**
     * LeetCode 81. 搜索旋转排序数组 II - Search in Rotated Sorted Array II
     * 题目链接：https://leetcode.cn/problems/search-in-rotated-sorted-array-ii/
     * 
     * 解题思路：
     * 1. 处理有重复元素的情况
     * 2. 当无法判断区间时，缩小搜索范围
     * 3. 处理边界条件
     * 
     * 时间复杂度：平均O(log n)，最坏O(n)
     * 空间复杂度：O(1)
     * 最优解：✅ 是最优解
     */
    public static boolean searchWithDuplicates(int[] nums, int target) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 找到目标值
            if (nums[mid] == target) {
                return true;
            }
            
            // 处理重复元素导致的难以判断区间的情况
            if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
                // 无法判断目标在哪个区间，缩小范围
                left++;
                right--;
            }
            // 判断mid所在的区间是递增还是递减
            else if (nums[left] <= nums[mid]) {
                // 左半部分是递增区间
                if (nums[left] <= target && target < nums[mid]) {
                    // 目标在左半部分
                    right = mid - 1;
                } else {
                    // 目标在右半部分
                    left = mid + 1;
                }
            } else {
                // 右半部分是递增区间
                if (nums[mid] < target && target <= nums[right]) {
                    // 目标在右半部分
                    left = mid + 1;
                } else {
                    // 目标在左半部分
                    right = mid - 1;
                }
            }
        }
        
        // 未找到目标值
        return false;
    }
    
    /**
     * C++ 实现：寻找旋转排序数组中的最小值
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static String getCppImplementation() {
        return """
#include <vector>
#include <stdexcept>

using namespace std;

/**
 * LeetCode 153. 寻找旋转排序数组中的最小值 - C++实现
 */
class RotatedArrayMin {
public:
    // 无重复元素的最小值查找
    int findMin(vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        
        int left = 0;
        int right = nums.size() - 1;
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return nums[left];
    }
    
    // 有重复元素的最小值查找
    int findMinWithDuplicates(vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        
        int left = 0;
        int right = nums.size() - 1;
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                right--;
            }
        }
        
        return nums[left];
    }
    
    // 在旋转数组中搜索目标值
    int search(vector<int>& nums, int target) {
        if (nums.empty()) return -1;
        
        int left = 0;
        int right = nums.size() - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (nums[mid] == target) return mid;
            
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
};
""";
    }
    
    /**
     * Python 实现：寻找旋转排序数组中的最小值
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static String getPythonImplementation() {
        return """
from typing import List

class RotatedArrayMin:
    \"\"\"
    LeetCode 153. 寻找旋转排序数组中的最小值 - Python实现
    \"\"\"
    
    @staticmethod
    def find_min(nums: List[int]) -> int:
        \"\"\"无重复元素的最小值查找\"\"\"
        if not nums:
            raise ValueError("数组不能为空")
        
        left, right = 0, len(nums) - 1
        
        while left < right:
            mid = left + ((right - left) >> 1)
            
            if nums[mid] > nums[right]:
                left = mid + 1
            else:
                right = mid
                
        return nums[left]
    
    @staticmethod
    def find_min_with_duplicates(nums: List[int]) -> int:
        \"\"\"有重复元素的最小值查找\"\"\"
        if not nums:
            raise ValueError("数组不能为空")
        
        left, right = 0, len(nums) - 1
        
        while left < right:
            mid = left + ((right - left) >> 1)
            
            if nums[mid] > nums[right]:
                left = mid + 1
            elif nums[mid] < nums[right]:
                right = mid
            else:
                right -= 1
                
        return nums[left]
    
    @staticmethod
    def search(nums: List[int], target: int) -> int:
        \"\"\"在旋转数组中搜索目标值\"\"\"
        if not nums:
            return -1
        
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            if nums[mid] == target:
                return mid
                
            if nums[left] <= nums[mid]:
                if nums[left] <= target < nums[mid]:
                    right = mid - 1
                else:
                    left = mid + 1
            else:
                if nums[mid] < target <= nums[right]:
                    left = mid + 1
                else:
                    right = mid - 1
                    
        return -1

# 测试代码
if __name__ == "__main__":
    # 测试用例
    test_cases = [
        ([3, 4, 5, 1, 2], 1),      # 正常旋转数组
        ([2, 2, 2, 0, 1], 0),      # 有重复元素的旋转数组
        ([1], 1),                   # 单个元素
        ([1, 2, 3, 4, 5], 1),      # 未旋转的数组
    ]
    
    for nums, expected in test_cases:
        result = RotatedArrayMin.find_min(nums)
        print(f"数组: {nums}, 最小值: {result}, 期望: {expected}, 正确: {result == expected}")
""";
    }
    
    /**
     * 测试用例：验证算法正确性
     */
    public static void testAllFunctions() {
        System.out.println("========== 旋转排序数组最小值查找测试 ==========\n");
        
        // 测试LeetCode 153
        int[] test1 = {3, 4, 5, 1, 2};
        System.out.println("LeetCode 153 - 无重复元素旋转数组:");
        System.out.println("数组: " + Arrays.toString(test1));
        System.out.println("最小值: " + findMin(test1)); // 应输出1
        System.out.println();
        
        // 测试LeetCode 154
        int[] test2 = {2, 2, 2, 0, 1};
        System.out.println("LeetCode 154 - 有重复元素旋转数组:");
        System.out.println("数组: " + Arrays.toString(test2));
        System.out.println("最小值: " + findMinWithDuplicates(test2)); // 应输出0
        System.out.println();
        
        // 测试LeetCode 33
        int[] test3 = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("LeetCode 33 - 在旋转数组中搜索:");
        System.out.println("数组: " + Arrays.toString(test3));
        System.out.println("搜索目标0的位置: " + search(test3, 0)); // 应输出4
        System.out.println("搜索目标3的位置: " + search(test3, 3)); // 应输出-1
        System.out.println();
        
        // 测试LeetCode 81
        int[] test4 = {2, 5, 6, 0, 0, 1, 2};
        System.out.println("LeetCode 81 - 在有重复元素的旋转数组中搜索:");
        System.out.println("数组: " + Arrays.toString(test4));
        System.out.println("搜索目标0是否存在: " + searchWithDuplicates(test4, 0)); // 应输出true
        System.out.println("搜索目标3是否存在: " + searchWithDuplicates(test4, 3)); // 应输出false
        System.out.println();
        
        // 测试边界条件
        System.out.println("边界条件测试:");
        try {
            int[] empty = {};
            findMin(empty);
        } catch (IllegalArgumentException e) {
            System.out.println("空数组测试通过: " + e.getMessage());
        }
        
        int[] single = {5};
        System.out.println("单元素数组最小值: " + findMin(single)); // 应输出5
        System.out.println();
        
        // 显示多语言实现
        System.out.println("========== C++ 实现代码 ==========");
        System.out.println(getCppImplementation());
        System.out.println();
        
        System.out.println("========== Python 实现代码 ==========");
        System.out.println(getPythonImplementation());
    }
    
    /**
     * 主函数：运行所有测试
     */
    public static void main(String[] args) {
        testAllFunctions();
    }
}
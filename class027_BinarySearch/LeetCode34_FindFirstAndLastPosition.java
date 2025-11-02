/**
 * LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置
 * 
 * 题目描述：
 * 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。
 * 请你找出给定目标值在数组中的开始位置和结束位置。
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 * 必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
 * 
 * 示例：
 * 输入：nums = [5,7,7,8,8,10], target = 8
 * 输出：[3,4]
 * 
 * 输入：nums = [5,7,7,8,8,10], target = 6
 * 输出：[-1,-1]
 * 
 * 输入：nums = [], target = 0
 * 输出：[-1,-1]
 * 
 * 约束条件：
 * - 0 <= nums.length <= 10^5
 * - -10^9 <= nums[i] <= 10^9
 * - nums 是一个非递减数组
 * - -10^9 <= target <= 10^9
 * 
 * 解题思路：
 * 这是二分查找的高级应用。我们需要分别找到目标值的第一个位置和最后一个位置。
 * 1. 查找第一个位置：找到第一个等于目标值的元素
 * 2. 查找最后一个位置：找到最后一个等于目标值的元素
 * 两种查找都可以通过修改二分查找的逻辑来实现。
 * 
 * 时间复杂度：O(log n)，其中 n 是数组的长度。需要执行两次二分查找。
 * 空间复杂度：O(1)，只使用了常数级别的额外空间。
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组、单元素数组、目标值不存在
 * 2. 整数溢出防护：使用 left + (right - left) / 2 而不是 (left + right) / 2
 * 3. 异常输入处理：检查数组是否为 null
 */
public class LeetCode34_FindFirstAndLastPosition {
    
    /**
     * 查找目标值的第一个和最后一个位置
     * 
     * @param nums 非递减顺序排列的整数数组
     * @param target 目标值
     * @return 包含开始位置和结束位置的数组，如果不存在目标值则返回[-1, -1]
     */
    public static int[] searchRange(int[] nums, int target) {
        // 异常处理：检查数组是否为 null
        if (nums == null) {
            return new int[]{-1, -1};
        }
        
        // 查找第一个位置
        int first = findFirst(nums, target);
        
        // 如果第一个位置不存在，说明目标值不存在
        if (first == -1) {
            return new int[]{-1, -1};
        }
        
        // 查找最后一个位置
        int last = findLast(nums, target);
        
        return new int[]{first, last};
    }
    
    /**
     * 查找第一个等于目标值的元素
     * 
     * @param nums 非递减顺序排列的整数数组
     * @param target 目标值
     * @return 第一个等于目标值的元素索引，如果不存在则返回-1
     */
    private static int findFirst(int[] nums, int target) {
        // 异常处理：检查数组是否为空
        if (nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                result = mid;    // 记录找到的位置
                right = mid - 1; // 继续在左半部分查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 查找最后一个等于目标值的元素
     * 
     * @param nums 非递减顺序排列的整数数组
     * @param target 目标值
     * @return 最后一个等于目标值的元素索引，如果不存在则返回-1
     */
    private static int findLast(int[] nums, int target) {
        // 异常处理：检查数组是否为空
        if (nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                result = mid;   // 记录找到的位置
                left = mid + 1; // 继续在右半部分查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 另一种实现方式：使用一次二分查找找到任意一个目标值，然后向两边扩展
     * 注意：这种方法的时间复杂度在最坏情况下是O(n)，不满足题目要求
     * 
     * @param nums 非递减顺序排列的整数数组
     * @param target 目标值
     * @return 包含开始位置和结束位置的数组，如果不存在目标值则返回[-1, -1]
     */
    public static int[] searchRangeAlternative(int[] nums, int target) {
        // 异常处理：检查数组是否为 null
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }
        
        // 使用标准二分查找找到任意一个目标值
        int index = binarySearch(nums, target);
        
        // 如果没有找到目标值
        if (index == -1) {
            return new int[]{-1, -1};
        }
        
        // 向左扩展找到第一个位置
        int left = index;
        while (left > 0 && nums[left - 1] == target) {
            left--;
        }
        
        // 向右扩展找到最后一个位置
        int right = index;
        while (right < nums.length - 1 && nums[right + 1] == target) {
            right++;
        }
        
        return new int[]{left, right};
    }
    
    /**
     * 标准二分查找实现
     * 
     * @param nums 升序排列的整型数组
     * @param target 目标值
     * @return 目标值在数组中的索引，如果不存在则返回-1
     */
    private static int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
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
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {5, 7, 7, 8, 8, 10};
        int target1 = 8;
        int[] result1 = searchRange(nums1, target1);
        System.out.println("测试用例1:");
        System.out.println("数组: [5, 7, 7, 8, 8, 10]");
        System.out.println("目标值: " + target1);
        System.out.println("结果: [" + result1[0] + ", " + result1[1] + "]");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {5, 7, 7, 8, 8, 10};
        int target2 = 6;
        int[] result2 = searchRange(nums2, target2);
        System.out.println("测试用例2:");
        System.out.println("数组: [5, 7, 7, 8, 8, 10]");
        System.out.println("目标值: " + target2);
        System.out.println("结果: [" + result2[0] + ", " + result2[1] + "]");
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {};
        int target3 = 0;
        int[] result3 = searchRange(nums3, target3);
        System.out.println("测试用例3:");
        System.out.println("数组: []");
        System.out.println("目标值: " + target3);
        System.out.println("结果: [" + result3[0] + ", " + result3[1] + "]");
        System.out.println();
        
        // 测试用例4：单元素数组
        int[] nums4 = {1};
        int target4 = 1;
        int[] result4 = searchRange(nums4, target4);
        System.out.println("测试用例4:");
        System.out.println("数组: [1]");
        System.out.println("目标值: " + target4);
        System.out.println("结果: [" + result4[0] + ", " + result4[1] + "]");
        System.out.println();
        
        // 测试替代方法
        System.out.println("替代方法测试:");
        int[] result5 = searchRangeAlternative(nums1, target1);
        System.out.println("替代方法结果: [" + result5[0] + ", " + result5[1] + "]");
    }
}
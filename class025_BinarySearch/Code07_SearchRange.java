import java.util.Arrays;

/**
 * 二分查找算法 - 搜索范围实现
 * 包含多个二分查找相关题目的实现
 * 
 * 时间复杂度分析：
 * - 标准二分查找：O(log n)
 * - 搜索范围：O(log n)
 * - 峰值查找：O(log n)
 * 
 * 空间复杂度：O(1) - 仅使用常数级别的额外空间
 */
public class Code07_SearchRange {
    
    /**
     * LeetCode 34 - 在排序数组中查找元素的第一个和最后一个位置
     * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。
     * 找出给定目标值在数组中的开始位置和结束位置。
     * 如果数组中不存在目标值，返回 [-1, -1]。
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int[] searchRange(int[] nums, int target) {
        int[] result = {-1, -1};
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 查找第一个等于target的位置
        int left = findFirst(nums, target);
        if (left == -1) {
            return result;
        }
        
        // 查找最后一个等于target的位置
        int right = findLast(nums, target);
        result[0] = left;
        result[1] = right;
        return result;
    }
    
    /**
     * 查找第一个等于target的位置
     */
    private static int findFirst(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int first = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                first = mid;
                right = mid - 1; // 继续在左半部分查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return first;
    }
    
    /**
     * 查找最后一个等于target的位置
     */
    private static int findLast(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int last = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                last = mid;
                left = mid + 1; // 继续在右半部分查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return last;
    }
    
    /**
     * LeetCode 852 - 山脉数组的峰顶索引
     * 山脉数组：arr.length >= 3，存在 i (0 < i < arr.length - 1) 使得：
     * arr[0] < arr[1] < ... < arr[i-1] < arr[i]
     * arr[i] > arr[i+1] > ... > arr[arr.length - 1]
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int peakIndexInMountainArray(int[] arr) {
        if (arr == null || arr.length < 3) {
            return -1;
        }
        
        int left = 0;
        int right = arr.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < arr[mid + 1]) {
                // 峰值在右侧
                left = mid + 1;
            } else {
                // 峰值在左侧或当前位置
                right = mid;
            }
        }
        return left;
    }
    
    /**
     * LeetCode 162 - 寻找峰值
     * 峰值元素是指其值严格大于左右相邻值的元素。
     * 数组可能包含多个峰值，返回任何一个峰值的位置即可。
     * 假设 nums[-1] = nums[n] = -∞
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int findPeakElement(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[mid + 1]) {
                // 峰值在左侧
                right = mid;
            } else {
                // 峰值在右侧
                left = mid + 1;
            }
        }
        return left;
    }
    
    /**
     * LeetCode 278 - 第一个错误的版本
     * 假设你有 n 个版本 [1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本。
     * 实现一个函数来查找第一个错误的版本。
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int firstBadVersion(int n) {
        int left = 1;
        int right = n;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (isBadVersion(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
    
    /**
     * 模拟的isBadVersion函数
     * 在实际LeetCode题目中，这个函数由平台提供
     */
    private static boolean isBadVersion(int version) {
        // 假设版本5及之后都是错误的
        return version >= 5;
    }
    
    /**
     * LeetCode 35 - 搜索插入位置
     * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。
     * 如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int searchInsert(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
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
        
        // 如果没有找到，返回应该插入的位置
        return left;
    }
    
    /**
     * 测试函数 - 验证所有算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 二分查找算法测试 ===");
        
        // 测试LeetCode 34 - 搜索范围
        int[] nums1 = {5, 7, 7, 8, 8, 10};
        int target1 = 8;
        int[] result1 = searchRange(nums1, target1);
        System.out.println("LeetCode 34 - 在排序数组中查找元素的第一个和最后一个位置:");
        System.out.println("数组: " + Arrays.toString(nums1) + ", 目标值: " + target1);
        System.out.println("结果: [" + result1[0] + ", " + result1[1] + "]"); // 应输出[3, 4]
        System.out.println();
        
        // 测试LeetCode 852 - 山脉数组的峰顶索引
        int[] mountain = {0, 1, 0};
        System.out.println("LeetCode 852 - 山脉数组的峰顶索引:");
        System.out.println("山脉数组: " + Arrays.toString(mountain));
        System.out.println("峰顶索引: " + peakIndexInMountainArray(mountain)); // 应输出1
        System.out.println();
        
        // 测试LeetCode 162 - 寻找峰值
        int[] nums2 = {1, 2, 3, 1};
        System.out.println("LeetCode 162 - 寻找峰值:");
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("峰值索引: " + findPeakElement(nums2)); // 应输出2
        System.out.println();
        
        // 测试LeetCode 278 - 第一个错误的版本
        System.out.println("LeetCode 278 - 第一个错误的版本:");
        System.out.println("第一个错误版本: " + firstBadVersion(10)); // 应输出5
        System.out.println();
        
        // 测试LeetCode 35 - 搜索插入位置
        int[] nums3 = {1, 3, 5, 6};
        int target3 = 5;
        System.out.println("LeetCode 35 - 搜索插入位置:");
        System.out.println("数组: " + Arrays.toString(nums3) + ", 目标值: " + target3);
        System.out.println("插入位置: " + searchInsert(nums3, target3)); // 应输出2
        System.out.println();
        
        System.out.println("=== 所有测试完成 ===");
    }
}
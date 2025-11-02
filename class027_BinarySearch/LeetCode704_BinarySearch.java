/**
 * LeetCode 704. 二分查找
 * 
 * 题目描述：
 * 给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target ，
 * 写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。
 * 
 * 示例：
 * 输入: nums = [-1,0,3,5,9,12], target = 9
 * 输出: 4
 * 解释: 9 出现在 nums 中并且下标为 4
 * 
 * 输入: nums = [-1,0,3,5,9,12], target = 2
 * 输出: -1
 * 解释: 2 不存在 nums 中因此返回 -1
 * 
 * 约束条件：
 * - 你可以假设 nums 中的所有元素是不重复的。
 * - n 将在 [1, 10000]之间。
 * - nums 的每个元素都将在 [-9999, 9999]之间。
 * 
 * 解题思路：
 * 使用标准的二分查找算法。由于数组是有序的，我们可以每次比较中间元素与目标值，
 * 根据比较结果缩小搜索范围，直到找到目标值或搜索范围为空。
 * 
 * 时间复杂度：O(log n)，其中 n 是数组的长度。每次搜索都会将搜索范围缩小一半。
 * 空间复杂度：O(1)，只使用了常数级别的额外空间。
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 整数溢出防护：使用 left + (right - left) / 2 而不是 (left + right) / 2
 * 3. 异常输入处理：检查数组是否为 null
 */
public class LeetCode704_BinarySearch {
    
    /**
     * 标准二分查找实现
     * 
     * @param nums 升序排列的整型数组
     * @param target 目标值
     * @return 目标值在数组中的索引，如果不存在则返回-1
     */
    public static int search(int[] nums, int target) {
        // 异常处理：检查数组是否为 null 或空
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        // 初始化左右边界
        int left = 0;
        int right = nums.length - 1;
        
        // 循环条件：left <= right
        // 当 left > right 时，搜索范围为空，退出循环
        while (left <= right) {
            // 防止整数溢出的中点计算方式
            // 使用 left + (right - left) / 2 而不是 (left + right) / 2
            int mid = left + (right - left) / 2;
            
            // 找到目标值，直接返回索引
            if (nums[mid] == target) {
                return mid;
            } 
            // 目标值在右半部分
            else if (nums[mid] < target) {
                left = mid + 1;
            } 
            // 目标值在左半部分
            else {
                right = mid - 1;
            }
        }
        
        // 未找到目标值
        return -1;
    }
    
    /**
     * 递归版本的二分查找实现
     * 
     * @param nums 升序排列的整型数组
     * @param target 目标值
     * @return 目标值在数组中的索引，如果不存在则返回-1
     */
    public static int searchRecursive(int[] nums, int target) {
        // 异常处理：检查数组是否为 null 或空
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        // 调用递归辅助函数
        return binarySearchRecursive(nums, target, 0, nums.length - 1);
    }
    
    /**
     * 递归辅助函数
     * 
     * @param nums 升序排列的整型数组
     * @param target 目标值
     * @param left 搜索范围的左边界
     * @param right 搜索范围的右边界
     * @return 目标值在数组中的索引，如果不存在则返回-1
     */
    private static int binarySearchRecursive(int[] nums, int target, int left, int right) {
        // 基本情况：搜索范围为空
        if (left > right) {
            return -1;
        }
        
        // 计算中点
        int mid = left + (right - left) / 2;
        
        // 找到目标值，直接返回索引
        if (nums[mid] == target) {
            return mid;
        } 
        // 目标值在右半部分
        else if (nums[mid] < target) {
            return binarySearchRecursive(nums, target, mid + 1, right);
        } 
        // 目标值在左半部分
        else {
            return binarySearchRecursive(nums, target, left, mid - 1);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {-1, 0, 3, 5, 9, 12};
        int target1 = 9;
        int result1 = search(nums1, target1);
        System.out.println("测试用例1:");
        System.out.println("数组: [-1, 0, 3, 5, 9, 12]");
        System.out.println("目标值: " + target1);
        System.out.println("结果: " + result1);
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {-1, 0, 3, 5, 9, 12};
        int target2 = 2;
        int result2 = search(nums2, target2);
        System.out.println("测试用例2:");
        System.out.println("数组: [-1, 0, 3, 5, 9, 12]");
        System.out.println("目标值: " + target2);
        System.out.println("结果: " + result2);
        System.out.println();
        
        // 测试用例3：单元素数组
        int[] nums3 = {5};
        int target3 = 5;
        int result3 = search(nums3, target3);
        System.out.println("测试用例3:");
        System.out.println("数组: [5]");
        System.out.println("目标值: " + target3);
        System.out.println("结果: " + result3);
        System.out.println();
        
        // 测试用例4：目标值不存在
        int[] nums4 = {2, 5};
        int target4 = 0;
        int result4 = search(nums4, target4);
        System.out.println("测试用例4:");
        System.out.println("数组: [2, 5]");
        System.out.println("目标值: " + target4);
        System.out.println("结果: " + result4);
        System.out.println();
        
        // 测试递归版本
        System.out.println("递归版本测试:");
        int result5 = searchRecursive(nums1, target1);
        System.out.println("递归版本结果: " + result5);
    }
}
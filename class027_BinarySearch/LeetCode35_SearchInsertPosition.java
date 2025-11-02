/**
 * LeetCode 35. 搜索插入位置
 * 
 * 题目描述：
 * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。
 * 如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
 * 必须使用时间复杂度为 O(log n) 的算法。
 * 
 * 示例：
 * 输入: nums = [1,3,5,6], target = 5
 * 输出: 2
 * 
 * 输入: nums = [1,3,5,6], target = 2
 * 输出: 1
 * 
 * 输入: nums = [1,3,5,6], target = 7
 * 输出: 4
 * 
 * 约束条件：
 * - 1 <= nums.length <= 10^4
 * - -10^4 <= nums[i] <= 10^4
 * - nums 为无重复元素的升序排列数组
 * - -10^4 <= target <= 10^4
 * 
 * 解题思路：
 * 这是二分查找的一个变种。我们需要找到第一个大于等于目标值的位置。
 * 如果找到目标值，直接返回其索引；如果没有找到，则返回应该插入的位置。
 * 
 * 时间复杂度：O(log n)，其中 n 是数组的长度。
 * 空间复杂度：O(1)，只使用了常数级别的额外空间。
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组、目标值小于所有元素、目标值大于所有元素
 * 2. 整数溢出防护：使用 left + (right - left) / 2 而不是 (left + right) / 2
 * 3. 异常输入处理：检查数组是否为 null
 */
public class LeetCode35_SearchInsertPosition {
    
    /**
     * 搜索插入位置实现
     * 
     * @param nums 升序排列的整型数组
     * @param target 目标值
     * @return 目标值在数组中的索引，或应该插入的位置
     */
    public static int searchInsert(int[] nums, int target) {
        // 异常处理：检查数组是否为 null 或空
        if (nums == null) {
            return 0;
        }
        
        // 初始化左右边界
        int left = 0;
        int right = nums.length - 1;
        
        // 循环条件：left <= right
        while (left <= right) {
            // 防止整数溢出的中点计算方式
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
        
        // 循环结束时，left 就是应该插入的位置
        return left;
    }
    
    /**
     * 另一种实现方式：查找第一个大于等于目标值的位置
     * 
     * @param nums 升序排列的整型数组
     * @param target 目标值
     * @return 目标值在数组中的索引，或应该插入的位置
     */
    public static int searchInsertAlternative(int[] nums, int target) {
        // 异常处理：检查数组是否为 null
        if (nums == null) {
            return 0;
        }
        
        // 初始化左右边界
        int left = 0;
        int right = nums.length - 1;
        int result = nums.length; // 默认插入到数组末尾
        
        // 查找第一个大于等于目标值的位置
        while (left <= right) {
            // 防止整数溢出的中点计算方式
            int mid = left + (right - left) / 2;
            
            if (nums[mid] >= target) {
                result = mid; // 记录可能的位置
                right = mid - 1; // 继续在左半部分查找
            } else {
                left = mid + 1; // 继续在右半部分查找
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, 5, 6};
        int target1 = 5;
        int result1 = searchInsert(nums1, target1);
        System.out.println("测试用例1:");
        System.out.println("数组: [1, 3, 5, 6]");
        System.out.println("目标值: " + target1);
        System.out.println("结果: " + result1);
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1, 3, 5, 6};
        int target2 = 2;
        int result2 = searchInsert(nums2, target2);
        System.out.println("测试用例2:");
        System.out.println("数组: [1, 3, 5, 6]");
        System.out.println("目标值: " + target2);
        System.out.println("结果: " + result2);
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {1, 3, 5, 6};
        int target3 = 7;
        int result3 = searchInsert(nums3, target3);
        System.out.println("测试用例3:");
        System.out.println("数组: [1, 3, 5, 6]");
        System.out.println("目标值: " + target3);
        System.out.println("结果: " + result3);
        System.out.println();
        
        // 测试用例4：目标值小于所有元素
        int[] nums4 = {1, 3, 5, 6};
        int target4 = 0;
        int result4 = searchInsert(nums4, target4);
        System.out.println("测试用例4:");
        System.out.println("数组: [1, 3, 5, 6]");
        System.out.println("目标值: " + target4);
        System.out.println("结果: " + result4);
        System.out.println();
        
        // 测试替代方法
        System.out.println("替代方法测试:");
        int result5 = searchInsertAlternative(nums2, target2);
        System.out.println("替代方法结果: " + result5);
    }
}
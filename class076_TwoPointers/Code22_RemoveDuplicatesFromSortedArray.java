/**
 * LeetCode 26. 删除有序数组中的重复项 (Remove Duplicates from Sorted Array)
 * 
 * 题目描述:
 * 给你一个升序排列的数组 nums，请你原地删除重复出现的元素，使得每个元素只出现一次，返回删除后数组的新长度。
 * 元素的相对顺序应该保持一致。
 * 由于在某些语言中不能改变数组的长度，所以必须将结果放在数组nums的第一部分。
 * 更规范地说，如果在删除重复项之后有k个元素，那么nums的前k个元素应该保存最终结果。
 * 将最终结果插入nums的前k个位置后返回k。
 * 不要使用额外的空间，你必须在原地修改输入数组并在O(1)额外空间的条件下完成。
 * 
 * 示例1:
 * 输入: nums = [1,1,2]
 * 输出: 2, nums = [1,2,_]
 * 解释: 函数应该返回新的长度 2 ，并且原数组 nums 的前两个元素被修改为 1, 2 。
 * 不需要考虑数组中超出新长度后面的元素。
 * 
 * 示例2:
 * 输入: nums = [0,0,1,1,1,2,2,3,3,4]
 * 输出: 5, nums = [0,1,2,3,4,_,_,_,_,_]
 * 解释: 函数应该返回新的长度 5 ， 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4 。
 * 不需要考虑数组中超出新长度后面的元素。
 * 
 * 提示:
 * 0 <= nums.length <= 3 * 10^4
 * -10^4 <= nums[i] <= 10^4
 * nums 已按升序排列
 * 
 * 题目链接: https://leetcode.com/problems/remove-duplicates-from-sorted-array/
 * 
 * 解题思路:
 * 这道题是一个经典的双指针应用场景。由于数组是已排序的，所有重复的元素都会相邻。
 * 我们可以使用快慢指针来解决这个问题：
 * 1. 慢指针slow指向当前已处理好的不重复元素的最后一个位置
 * 2. 快指针fast遍历整个数组
 * 3. 当nums[fast]不等于nums[slow]时，说明找到了一个新的不重复元素，将slow向前移动一位，然后将nums[fast]赋值给nums[slow]
 * 4. 当nums[fast]等于nums[slow]时，说明遇到了重复元素，fast继续向前移动
 * 5. 遍历结束后，slow+1就是新数组的长度
 * 
 * 时间复杂度: O(n)，其中n是数组的长度。快指针最多遍历数组一次。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 */

import java.util.Arrays;

public class Code22_RemoveDuplicatesFromSortedArray {
    
    /**
     * 解法一: 快慢指针（最优解）
     * 
     * @param nums 升序排列的数组
     * @return 删除重复元素后的数组新长度
     */
    public static int removeDuplicates(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        // 边界情况：数组长度为0或1，直接返回原长度
        if (nums.length <= 1) {
            return nums.length;
        }
        
        int slow = 0; // 慢指针，指向当前已处理好的不重复元素的最后一个位置
        
        // 快指针遍历整个数组
        for (int fast = 1; fast < nums.length; fast++) {
            // 当遇到不同的元素时
            if (nums[fast] != nums[slow]) {
                slow++; // 慢指针向前移动一位
                nums[slow] = nums[fast]; // 将不重复的元素移动到前面
            }
            // 当遇到相同的元素时，fast继续向前移动，slow保持不变
        }
        
        // 新数组的长度是slow + 1
        return slow + 1;
    }
    
    /**
     * 解法二: 快慢指针的另一种写法，包含更多优化
     * 
     * @param nums 升序排列的数组
     * @return 删除重复元素后的数组新长度
     */
    public static int removeDuplicatesOptimized(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        // 边界情况：数组长度为0或1，直接返回原长度
        int n = nums.length;
        if (n <= 1) {
            return n;
        }
        
        int slow = 1; // 这里slow初始化为1，表示第一个元素已经是唯一的
        
        // 从第二个元素开始遍历
        for (int fast = 1; fast < n; fast++) {
            // 当遇到不同的元素时
            if (nums[fast] != nums[fast - 1]) {
                nums[slow] = nums[fast]; // 将不重复的元素移动到前面
                slow++; // 慢指针向前移动一位
            }
        }
        
        return slow;
    }
    
    /**
     * 解法三: 优化版快慢指针，适用于更通用的场景
     * 
     * @param nums 升序排列的数组
     * @return 删除重复元素后的数组新长度
     */
    public static int removeDuplicatesGeneric(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        // 边界情况：数组长度为0，直接返回0
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        
        // 初始化慢指针
        int insertPos = 1;
        
        // 从第二个元素开始遍历
        for (int i = 1; i < n; i++) {
            // 如果当前元素与上一个保留的元素不同，则保留它
            if (nums[i] != nums[insertPos - 1]) {
                nums[insertPos] = nums[i];
                insertPos++;
            }
        }
        
        return insertPos;
    }
    
    /**
     * 打印数组的前k个元素
     * 
     * @param nums 数组
     * @param k 元素个数
     */
    public static void printArray(int[] nums, int k) {
        System.out.print("[");
        for (int i = 0; i < k; i++) {
            System.out.print(nums[i]);
            if (i < k - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        int[] nums1 = {1, 1, 2};
        System.out.println("测试用例1:");
        System.out.println("原始数组: " + Arrays.toString(nums1));
        int length1 = removeDuplicates(nums1);
        System.out.println("新长度: " + length1);
        System.out.print("新数组前" + length1 + "个元素: ");
        printArray(nums1, length1);
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        System.out.println("测试用例2:");
        System.out.println("原始数组: " + Arrays.toString(nums2));
        int length2 = removeDuplicates(nums2);
        System.out.println("新长度: " + length2);
        System.out.print("新数组前" + length2 + "个元素: ");
        printArray(nums2, length2);
        System.out.println();
        
        // 测试用例3 - 边界情况：空数组
        int[] nums3 = {};
        System.out.println("测试用例3（空数组）:");
        System.out.println("原始数组: " + Arrays.toString(nums3));
        int length3 = removeDuplicates(nums3);
        System.out.println("新长度: " + length3);
        System.out.print("新数组前" + length3 + "个元素: ");
        printArray(nums3, length3);
        System.out.println();
        
        // 测试用例4 - 边界情况：只有一个元素的数组
        int[] nums4 = {5};
        System.out.println("测试用例4（单元素数组）:");
        System.out.println("原始数组: " + Arrays.toString(nums4));
        int length4 = removeDuplicates(nums4);
        System.out.println("新长度: " + length4);
        System.out.print("新数组前" + length4 + "个元素: ");
        printArray(nums4, length4);
        System.out.println();
        
        // 测试用例5 - 边界情况：没有重复元素的数组
        int[] nums5 = {1, 2, 3, 4, 5};
        System.out.println("测试用例5（无重复元素数组）:");
        System.out.println("原始数组: " + Arrays.toString(nums5));
        int length5 = removeDuplicates(nums5);
        System.out.println("新长度: " + length5);
        System.out.print("新数组前" + length5 + "个元素: ");
        printArray(nums5, length5);
        System.out.println();
        
        // 测试用例6 - 边界情况：所有元素都相同的数组
        int[] nums6 = {3, 3, 3, 3, 3};
        System.out.println("测试用例6（全相同元素数组）:");
        System.out.println("原始数组: " + Arrays.toString(nums6));
        int length6 = removeDuplicates(nums6);
        System.out.println("新长度: " + length6);
        System.out.print("新数组前" + length6 + "个元素: ");
        printArray(nums6, length6);
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        test();
        
        // 性能测试
        System.out.println("\n性能测试:");
        
        // 创建一个大数组进行性能测试
        int size = 100000;
        int[] largeArray = new int[size];
        // 填充数组，每10个元素重复一次
        for (int i = 0; i < size; i++) {
            largeArray[i] = i / 10;
        }
        
        // 测试解法一的性能
        int[] array1 = Arrays.copyOf(largeArray, size);
        long startTime = System.currentTimeMillis();
        int result1 = removeDuplicates(array1);
        long endTime = System.currentTimeMillis();
        System.out.println("解法一耗时: " + (endTime - startTime) + "ms, 结果长度: " + result1);
        
        // 测试解法二的性能
        int[] array2 = Arrays.copyOf(largeArray, size);
        startTime = System.currentTimeMillis();
        int result2 = removeDuplicatesOptimized(array2);
        endTime = System.currentTimeMillis();
        System.out.println("解法二耗时: " + (endTime - startTime) + "ms, 结果长度: " + result2);
        
        // 测试解法三的性能
        int[] array3 = Arrays.copyOf(largeArray, size);
        startTime = System.currentTimeMillis();
        int result3 = removeDuplicatesGeneric(array3);
        endTime = System.currentTimeMillis();
        System.out.println("解法三耗时: " + (endTime - startTime) + "ms, 结果长度: " + result3);
    }
}
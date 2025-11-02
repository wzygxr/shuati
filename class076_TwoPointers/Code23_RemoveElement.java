package class050;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * LeetCode 27. 移除元素 (Remove Element)
 * 
 * 题目描述:
 * 给你一个数组 nums 和一个值 val，你需要原地移除所有数值等于 val 的元素，并返回移除后数组的新长度。
 * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并原地修改输入数组。
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 * 
 * 示例1:
 * 输入: nums = [3,2,2,3], val = 3
 * 输出: 2, nums = [2,2]
 * 解释: 函数应该返回新的长度 2, 并且 nums 中的前两个元素均为 2。
 * 不需要考虑数组中超出新长度后面的元素。例如，函数返回的新长度为 2 ，
 * 而 nums = [2,2,3,3] 或 nums = [2,2,0,0]，也会被视作正确答案。
 * 
 * 示例2:
 * 输入: nums = [0,1,2,2,3,0,4,2], val = 2
 * 输出: 5, nums = [0,1,4,0,3]
 * 解释: 函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
 * 注意这五个元素可为任意顺序。你不需要考虑数组中超出新长度后面的元素。
 * 
 * 提示:
 * 0 <= nums.length <= 100
 * 0 <= nums[i] <= 50
 * 0 <= val <= 100
 * 
 * 题目链接: https://leetcode.com/problems/remove-element/
 * 
 * 解题思路:
 * 这道题与删除有序数组中的重复项类似，都可以使用双指针技术。
 * 我们可以使用一个慢指针来跟踪应该放置下一个非val元素的位置，
 * 然后用一个快指针来遍历整个数组。
 * 当快指针找到一个不等于val的元素时，我们将该元素放到慢指针指向的位置，然后将慢指针向前移动一位。
 * 
 * 时间复杂度: O(n)，其中n是数组的长度。快指针最多遍历数组一次。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 */

public class Code23_RemoveElement {
    
    /**
     * 解法一: 双指针（最优解）
     * 
     * @param nums 输入数组
     * @param val 要移除的元素值
     * @return 移除后数组的新长度
     * @throws IllegalArgumentException 如果输入数组为null
     */
    public static int removeElement(int[] nums, int val) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        int slow = 0; // 慢指针，指向下一个非val元素应该放置的位置
        
        // 快指针遍历整个数组
        for (int fast = 0; fast < nums.length; fast++) {
            // 如果当前元素不等于val，则将其移到慢指针位置，并将慢指针向前移动
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        
        // 慢指针的值就是新数组的长度
        return slow;
    }
    
    /**
     * 解法二: 优化的双指针（当需要删除的元素很少时）
     * 
     * 当要删除的元素很少时，我们可以将不等于val的元素保留，而将等于val的元素与数组末尾的元素交换，
     * 然后减少数组的长度。这样可以减少不必要的赋值操作。
     * 
     * @param nums 输入数组
     * @param val 要移除的元素值
     * @return 移除后数组的新长度
     */
    public static int removeElementOptimized(int[] nums, int val) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        int left = 0;
        int right = nums.length;
        
        while (left < right) {
            if (nums[left] == val) {
                // 将当前元素与数组末尾元素交换
                nums[left] = nums[right - 1];
                // 减少数组长度
                right--;
            } else {
                // 当前元素不等于val，保留它
                left++;
            }
        }
        
        return right;
    }
    
    /**
     * 解法三: 简洁版双指针
     * 
     * @param nums 输入数组
     * @param val 要移除的元素值
     * @return 移除后数组的新长度
     */
    public static int removeElementConcise(int[] nums, int val) {
        int index = 0;
        
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[index++] = nums[i];
            }
        }
        
        return index;
    }
    
    /**
     * 打印数组的前k个元素
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
     * 打印完整的数组
     */
    public static void printFullArray(int[] nums) {
        System.out.println(Arrays.toString(nums));
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        int[] nums1 = {3, 2, 2, 3};
        int val1 = 3;
        System.out.println("测试用例1:");
        System.out.print("原始数组: ");
        printFullArray(nums1);
        System.out.println("要移除的值: " + val1);
        int length1 = removeElement(nums1, val1);
        System.out.println("新长度: " + length1);
        System.out.print("新数组前" + length1 + "个元素: ");
        printArray(nums1, length1);
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {0, 1, 2, 2, 3, 0, 4, 2};
        int val2 = 2;
        System.out.println("测试用例2:");
        System.out.print("原始数组: ");
        printFullArray(nums2);
        System.out.println("要移除的值: " + val2);
        int length2 = removeElement(nums2, val2);
        System.out.println("新长度: " + length2);
        System.out.print("新数组前" + length2 + "个元素: ");
        printArray(nums2, length2);
        System.out.println();
        
        // 测试用例3 - 边界情况：空数组
        int[] nums3 = {};
        int val3 = 0;
        System.out.println("测试用例3（空数组）:");
        System.out.print("原始数组: []");
        System.out.println("要移除的值: " + val3);
        int length3 = removeElement(nums3, val3);
        System.out.println("新长度: " + length3);
        System.out.print("新数组前" + length3 + "个元素: []");
        System.out.println();
        
        // 测试用例4 - 边界情况：所有元素都等于val
        int[] nums4 = {5, 5, 5, 5};
        int val4 = 5;
        System.out.println("测试用例4（全等于val的数组）:");
        System.out.print("原始数组: ");
        printFullArray(nums4);
        System.out.println("要移除的值: " + val4);
        int length4 = removeElement(nums4, val4);
        System.out.println("新长度: " + length4);
        System.out.print("新数组前" + length4 + "个元素: []");
        System.out.println();
        
        // 测试用例5 - 边界情况：没有元素等于val
        int[] nums5 = {1, 2, 3, 4, 5};
        int val5 = 6;
        System.out.println("测试用例5（无等于val的元素）:");
        System.out.print("原始数组: ");
        printFullArray(nums5);
        System.out.println("要移除的值: " + val5);
        int length5 = removeElement(nums5, val5);
        System.out.println("新长度: " + length5);
        System.out.print("新数组前" + length5 + "个元素: ");
        printArray(nums5, length5);
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建一个大数组进行性能测试
        int size = 1000000;
        int[] largeArray = new int[size];
        
        // 填充数组，其中约20%的元素等于val
        int val = 5;
        for (int i = 0; i < size; i++) {
            largeArray[i] = i % 10 == 0 ? val : i % 10;
        }
        
        // 测试解法一的性能
        int[] array1 = Arrays.copyOf(largeArray, size);
        long startTime = System.nanoTime();
        int result1 = removeElement(array1, val);
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法一耗时: " + duration + "ms, 结果长度: " + result1);
        
        // 测试解法二的性能
        int[] array2 = Arrays.copyOf(largeArray, size);
        startTime = System.nanoTime();
        int result2 = removeElementOptimized(array2, val);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法二耗时: " + duration + "ms, 结果长度: " + result2);
        
        // 测试解法三的性能
        int[] array3 = Arrays.copyOf(largeArray, size);
        startTime = System.nanoTime();
        int result3 = removeElementConcise(array3, val);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法三耗时: " + duration + "ms, 结果长度: " + result3);
        
        // 验证所有解法结果一致
        System.out.println("所有解法结果一致: " + (result1 == result2 && result2 == result3));
    }
    
    public static void main(String[] args) {
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
    }
}
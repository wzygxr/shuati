package class050;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * LeetCode 75. 颜色分类 (Sort Colors)
 * 
 * 题目描述:
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 * 我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
 * 必须在不使用库内置的 sort 函数的情况下解决这个问题。
 * 
 * 示例1:
 * 输入: nums = [2,0,2,1,1,0]
 * 输出: [0,0,1,1,2,2]
 * 
 * 示例2:
 * 输入: nums = [2,0,1]
 * 输出: [0,1,2]
 * 
 * 提示:
 * n == nums.length
 * 1 <= n <= 300
 * nums[i] 为 0、1 或 2
 * 
 * 题目链接: https://leetcode.com/problems/sort-colors/
 * 
 * 解题思路:
 * 这道题是一个经典的「荷兰国旗问题」，可以使用双指针或者三指针技术来解决。
 * 
 * 方法一（三指针法）：
 * 1. 使用三个指针：left（指向0的右边界）、mid（当前处理的元素）、right（指向2的左边界）
 * 2. 初始化left=0，mid=0，right=nums.length-1
 * 3. 当mid<=right时，根据nums[mid]的值进行不同的处理：
 *    - 如果nums[mid]==0，交换nums[left]和nums[mid]，然后left++，mid++
 *    - 如果nums[mid]==1，mid++
 *    - 如果nums[mid]==2，交换nums[mid]和nums[right]，然后right--（注意此时mid不增加，因为交换后的元素还未处理）
 * 
 * 方法二（两次遍历）：
 * 1. 第一次遍历统计0、1、2的个数
 * 2. 第二次遍历根据统计结果填充数组
 * 
 * 最优解是三指针法，时间复杂度O(n)，空间复杂度O(1)，且只需要一次遍历。
 */

public class Code24_SortColors {
    
    /**
     * 解法一: 三指针法（最优解）
     * 
     * @param nums 输入数组，只包含0、1、2三种元素
     * @throws IllegalArgumentException 如果输入数组为null
     */
    public static void sortColors(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        int left = 0;      // 0的右边界
        int mid = 0;       // 当前处理的元素
        int right = nums.length - 1;  // 2的左边界
        
        while (mid <= right) {
            switch (nums[mid]) {
                case 0:
                    // 当前元素是0，放到left指针位置
                    swap(nums, left, mid);
                    left++;
                    mid++;
                    break;
                case 1:
                    // 当前元素是1，已经在正确位置
                    mid++;
                    break;
                case 2:
                    // 当前元素是2，放到right指针位置
                    swap(nums, mid, right);
                    right--;
                    // 注意：此时mid不增加，因为交换后的元素还未处理
                    break;
                default:
                    // 非法输入检查
                    throw new IllegalArgumentException("输入数组包含非法元素，只能包含0、1、2");
            }
        }
    }
    
    /**
     * 解法二: 两次遍历（计数排序思想）
     * 
     * @param nums 输入数组，只包含0、1、2三种元素
     */
    public static void sortColorsTwoPass(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        int count0 = 0, count1 = 0, count2 = 0;
        
        // 第一次遍历：统计0、1、2的个数
        for (int num : nums) {
            switch (num) {
                case 0:
                    count0++;
                    break;
                case 1:
                    count1++;
                    break;
                case 2:
                    count2++;
                    break;
                default:
                    throw new IllegalArgumentException("输入数组包含非法元素，只能包含0、1、2");
            }
        }
        
        // 第二次遍历：根据统计结果填充数组
        int index = 0;
        while (count0 > 0) {
            nums[index++] = 0;
            count0--;
        }
        while (count1 > 0) {
            nums[index++] = 1;
            count1--;
        }
        while (count2 > 0) {
            nums[index++] = 2;
            count2--;
        }
    }
    
    /**
     * 解法三: 双指针优化版
     * 
     * @param nums 输入数组，只包含0、1、2三种元素
     */
    public static void sortColorsTwoPointers(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        // 先将所有的0移到数组前面
        int p0 = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                swap(nums, i, p0);
                p0++;
            }
        }
        
        // 再将所有的1移到0之后
        int p1 = p0;
        for (int i = p0; i < nums.length; i++) {
            if (nums[i] == 1) {
                swap(nums, i, p1);
                p1++;
            }
        }
    }
    
    /**
     * 交换数组中的两个元素
     */
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    /**
     * 打印数组
     */
    public static void printArray(int[] nums) {
        System.out.println(Arrays.toString(nums));
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        int[] nums1 = {2, 0, 2, 1, 1, 0};
        System.out.println("测试用例1:");
        System.out.print("排序前: ");
        printArray(nums1);
        sortColors(nums1);
        System.out.print("排序后: ");
        printArray(nums1);
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {2, 0, 1};
        System.out.println("测试用例2:");
        System.out.print("排序前: ");
        printArray(nums2);
        sortColors(nums2);
        System.out.print("排序后: ");
        printArray(nums2);
        System.out.println();
        
        // 测试用例3 - 边界情况：只有一个元素
        int[] nums3 = {0};
        System.out.println("测试用例3（单元素数组）:");
        System.out.print("排序前: ");
        printArray(nums3);
        sortColors(nums3);
        System.out.print("排序后: ");
        printArray(nums3);
        System.out.println();
        
        // 测试用例4 - 边界情况：已经排序的数组
        int[] nums4 = {0, 0, 1, 1, 2, 2};
        System.out.println("测试用例4（已排序数组）:");
        System.out.print("排序前: ");
        printArray(nums4);
        sortColors(nums4);
        System.out.print("排序后: ");
        printArray(nums4);
        System.out.println();
        
        // 测试用例5 - 边界情况：逆序排列的数组
        int[] nums5 = {2, 2, 1, 1, 0, 0};
        System.out.println("测试用例5（逆序数组）:");
        System.out.print("排序前: ");
        printArray(nums5);
        sortColors(nums5);
        System.out.print("排序后: ");
        printArray(nums5);
        System.out.println();
        
        // 测试用例6 - 边界情况：所有元素都相同
        int[] nums6 = {1, 1, 1, 1};
        System.out.println("测试用例6（全相同元素）:");
        System.out.print("排序前: ");
        printArray(nums6);
        sortColors(nums6);
        System.out.print("排序后: ");
        printArray(nums6);
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建一个大数组进行性能测试
        int size = 1000000;
        int[] largeArray = new int[size];
        
        // 随机填充0、1、2
        for (int i = 0; i < size; i++) {
            largeArray[i] = i % 3;
        }
        
        // 测试解法一的性能
        int[] array1 = Arrays.copyOf(largeArray, size);
        long startTime = System.nanoTime();
        sortColors(array1);
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法一（三指针）耗时: " + duration + "ms");
        
        // 测试解法二的性能
        int[] array2 = Arrays.copyOf(largeArray, size);
        startTime = System.nanoTime();
        sortColorsTwoPass(array2);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法二（两次遍历）耗时: " + duration + "ms");
        
        // 测试解法三的性能
        int[] array3 = Arrays.copyOf(largeArray, size);
        startTime = System.nanoTime();
        sortColorsTwoPointers(array3);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法三（双指针优化）耗时: " + duration + "ms");
        
        // 验证所有解法结果一致
        boolean resultsConsistent = true;
        for (int i = 0; i < size; i++) {
            if (array1[i] != array2[i] || array1[i] != array3[i]) {
                resultsConsistent = false;
                break;
            }
        }
        System.out.println("所有解法结果一致: " + resultsConsistent);
    }
    
    /**
     * 验证排序结果是否正确
     */
    public static boolean isSorted(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
    }
}
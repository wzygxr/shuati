package class050;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * LeetCode 283. 移动零 (Move Zeroes)
 * 
 * 题目描述:
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 * 请注意 ，必须在不复制数组的情况下原地对数组进行操作。
 * 
 * 示例1:
 * 输入: nums = [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 * 
 * 示例2:
 * 输入: nums = [0]
 * 输出: [0]
 * 
 * 提示:
 * 1 <= nums.length <= 10^4
 * -2^31 <= nums[i] <= 2^31 - 1
 * 
 * 题目链接: https://leetcode.com/problems/move-zeroes/
 * 
 * 解题思路:
 * 这道题可以使用双指针的方法来解决：
 * 
 * 方法一（双指针）：
 * 1. 使用两个指针 slow 和 fast，初始都指向0
 * 2. fast指针用于遍历整个数组，当遇到非零元素时，将其移动到 slow 指向的位置，然后 slow++
 * 3. 遍历结束后，将 slow 到数组末尾的所有元素都设为0
 * 
 * 方法二（优化的双指针）：
 * 1. 同样使用两个指针 slow 和 fast，初始都指向0
 * 2. 当 fast 指向非零元素时，如果 slow != fast，交换 nums[slow] 和 nums[fast]，然后 slow++
 * 3. 这种方法避免了不必要的赋值操作，只在需要时进行交换
 * 
 * 最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
 */

public class Code26_MoveZeroes {
    
    /**
     * 解法一: 双指针基础版
     * 
     * @param nums 输入数组
     * @throws IllegalArgumentException 如果输入数组为null
     */
    public static void moveZeroes(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        // 慢指针，指向当前应该放置非零元素的位置
        int slow = 0;
        
        // 快指针遍历整个数组
        for (int fast = 0; fast < nums.length; fast++) {
            // 如果当前元素不为0，将其移到慢指针位置
            if (nums[fast] != 0) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        
        // 将slow之后的所有元素都设为0
        for (int i = slow; i < nums.length; i++) {
            nums[i] = 0;
        }
    }
    
    /**
     * 解法二: 优化的双指针（最优解）
     * 
     * @param nums 输入数组
     */
    public static void moveZeroesOptimized(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        // 慢指针，指向当前应该放置非零元素的位置
        int slow = 0;
        
        // 快指针遍历整个数组
        for (int fast = 0; fast < nums.length; fast++) {
            // 如果当前元素不为0，且slow和fast不同，交换它们
            if (nums[fast] != 0) {
                if (slow != fast) {
                    swap(nums, slow, fast);
                }
                slow++;
            }
        }
    }
    
    /**
     * 解法三: 一次遍历的另一种实现
     * 
     * @param nums 输入数组
     */
    public static void moveZeroesOnePass(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        int lastNonZeroFoundAt = 0;
        
        // 将所有非零元素移到数组前面
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[lastNonZeroFoundAt++] = nums[i];
            }
        }
        
        // 填充剩余位置为0
        for (int i = lastNonZeroFoundAt; i < nums.length; i++) {
            nums[i] = 0;
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
     * 验证结果是否正确
     */
    public static boolean validateResult(int[] original, int[] expected) {
        if (original.length != expected.length) {
            return false;
        }
        
        // 验证所有元素相等
        for (int i = 0; i < original.length; i++) {
            if (original[i] != expected[i]) {
                return false;
            }
        }
        
        // 验证0都在末尾
        boolean zeroStarted = false;
        for (int num : original) {
            if (zeroStarted && num != 0) {
                return false; // 发现0后面有非零元素
            }
            if (num == 0) {
                zeroStarted = true;
            }
        }
        
        return true;
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        int[] nums1 = {0, 1, 0, 3, 12};
        int[] expected1 = {1, 3, 12, 0, 0};
        System.out.println("测试用例1:");
        System.out.print("原数组: ");
        System.out.println(Arrays.toString(nums1));
        moveZeroesOptimized(nums1);
        System.out.print("处理后: ");
        System.out.println(Arrays.toString(nums1));
        System.out.println("验证结果: " + validateResult(nums1, expected1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {0};
        int[] expected2 = {0};
        System.out.println("测试用例2:");
        System.out.print("原数组: ");
        System.out.println(Arrays.toString(nums2));
        moveZeroesOptimized(nums2);
        System.out.print("处理后: ");
        System.out.println(Arrays.toString(nums2));
        System.out.println("验证结果: " + validateResult(nums2, expected2));
        System.out.println();
        
        // 测试用例3 - 边界情况：没有零元素
        int[] nums3 = {1, 2, 3, 4, 5};
        int[] expected3 = {1, 2, 3, 4, 5};
        System.out.println("测试用例3（无零元素）:");
        System.out.print("原数组: ");
        System.out.println(Arrays.toString(nums3));
        moveZeroesOptimized(nums3);
        System.out.print("处理后: ");
        System.out.println(Arrays.toString(nums3));
        System.out.println("验证结果: " + validateResult(nums3, expected3));
        System.out.println();
        
        // 测试用例4 - 边界情况：所有元素都是零
        int[] nums4 = {0, 0, 0, 0, 0};
        int[] expected4 = {0, 0, 0, 0, 0};
        System.out.println("测试用例4（全零元素）:");
        System.out.print("原数组: ");
        System.out.println(Arrays.toString(nums4));
        moveZeroesOptimized(nums4);
        System.out.print("处理后: ");
        System.out.println(Arrays.toString(nums4));
        System.out.println("验证结果: " + validateResult(nums4, expected4));
        System.out.println();
        
        // 测试用例5 - 边界情况：零元素在开头
        int[] nums5 = {0, 0, 1, 2, 3};
        int[] expected5 = {1, 2, 3, 0, 0};
        System.out.println("测试用例5（零在开头）:");
        System.out.print("原数组: ");
        System.out.println(Arrays.toString(nums5));
        moveZeroesOptimized(nums5);
        System.out.print("处理后: ");
        System.out.println(Arrays.toString(nums5));
        System.out.println("验证结果: " + validateResult(nums5, expected5));
        System.out.println();
        
        // 测试用例6 - 边界情况：零元素在末尾
        int[] nums6 = {1, 2, 3, 0, 0};
        int[] expected6 = {1, 2, 3, 0, 0};
        System.out.println("测试用例6（零在末尾）:");
        System.out.print("原数组: ");
        System.out.println(Arrays.toString(nums6));
        moveZeroesOptimized(nums6);
        System.out.print("处理后: ");
        System.out.println(Arrays.toString(nums6));
        System.out.println("验证结果: " + validateResult(nums6, expected6));
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建一个大数组进行性能测试
        int size = 1000000;
        int[] largeArray = new int[size];
        
        // 生成测试数据：交替放置0和1
        for (int i = 0; i < size; i++) {
            largeArray[i] = i % 2;  // 0,1,0,1,...
        }
        
        // 测试解法一的性能
        int[] array1 = Arrays.copyOf(largeArray, size);
        long startTime = System.nanoTime();
        moveZeroes(array1);
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法一（基础双指针）耗时: " + duration + "ms");
        
        // 测试解法二的性能
        int[] array2 = Arrays.copyOf(largeArray, size);
        startTime = System.nanoTime();
        moveZeroesOptimized(array2);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法二（优化双指针）耗时: " + duration + "ms");
        
        // 测试解法三的性能
        int[] array3 = Arrays.copyOf(largeArray, size);
        startTime = System.nanoTime();
        moveZeroesOnePass(array3);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法三（一次遍历）耗时: " + duration + "ms");
        
        // 验证所有解法结果一致
        boolean resultsConsistent = true;
        for (int i = 0; i < size; i++) {
            if (array1[i] != array2[i] || array1[i] != array3[i]) {
                resultsConsistent = false;
                break;
            }
        }
        System.out.println("所有解法结果一致: " + resultsConsistent);
        
        // 验证结果正确性
        boolean isCorrect = true;
        boolean zeroStarted = false;
        for (int num : array1) {
            if (zeroStarted && num != 0) {
                isCorrect = false;
                break;
            }
            if (num == 0) {
                zeroStarted = true;
            }
        }
        System.out.println("结果正确: " + isCorrect);
    }
    
    public static void main(String[] args) {
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
    }
}
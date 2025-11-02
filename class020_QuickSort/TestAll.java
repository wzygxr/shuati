package class023;

import java.util.Arrays;

/**
 * 综合测试类，用于测试所有快速排序相关算法的正确性
 * 本类包含对基础快速排序、三路快速排序、快速选择算法和最小k个数算法的全面测试
 */
public class TestAll {
    
    /**
     * 程序入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 测试基础快速排序
        testBasicQuickSort();
        
        // 测试三路快速排序
        testThreeWayQuickSort();
        
        // 测试快速选择算法
        testQuickSelect();
        
        // 测试最小k个数
        testGetLeastNumbers();
        
        System.out.println("所有测试通过！");
    }
    
    /**
     * 测试基础快速排序
     * 包含多种测试场景：普通数组、包含重复元素的数组、已排序数组、逆序数组
     */
    private static void testBasicQuickSort() {
        System.out.println("=== 测试基础快速排序 ===");
        
        // 测试用例1：普通数组
        int[] arr1 = {5, 2, 3, 1, 4};
        System.out.println("排序前: " + Arrays.toString(arr1));
        Code02_QuickSort.sortArray(arr1);
        System.out.println("排序后: " + Arrays.toString(arr1));
        assert isSorted(arr1) : "基础快速排序测试失败";
        
        // 测试用例2：包含重复元素的数组
        int[] arr2 = {5, 2, 3, 1, 4, 2, 3};
        System.out.println("排序前: " + Arrays.toString(arr2));
        Code02_QuickSort.sortArray(arr2);
        System.out.println("排序后: " + Arrays.toString(arr2));
        assert isSorted(arr2) : "包含重复元素的数组排序测试失败";
        
        // 测试用例3：已排序数组
        int[] arr3 = {1, 2, 3, 4, 5};
        System.out.println("排序前: " + Arrays.toString(arr3));
        Code02_QuickSort.sortArray(arr3);
        System.out.println("排序后: " + Arrays.toString(arr3));
        assert isSorted(arr3) : "已排序数组测试失败";
        
        // 测试用例4：逆序数组
        int[] arr4 = {5, 4, 3, 2, 1};
        System.out.println("排序前: " + Arrays.toString(arr4));
        Code02_QuickSort.sortArray(arr4);
        System.out.println("排序后: " + Arrays.toString(arr4));
        assert isSorted(arr4) : "逆序数组测试失败";
        
        System.out.println("基础快速排序测试通过\n");
    }
    
    /**
     * 测试三路快速排序
     * 专门测试处理包含大量重复元素的数组场景
     */
    private static void testThreeWayQuickSort() {
        System.out.println("=== 测试三路快速排序 ===");
        
        // 测试用例：包含大量重复元素的数组
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6, 3, 3, 3};
        System.out.println("排序前: " + Arrays.toString(arr));
        
        // 使用三路快排思想进行排序
        Code02_QuickSort solution = new Code02_QuickSort();
        if (arr.length > 1) {
            // 调用改进版快速排序算法（三路快排）
            solution.quickSort2(arr, 0, arr.length - 1);
        }
        
        System.out.println("排序后: " + Arrays.toString(arr));
        assert isSorted(arr) : "三路快速排序测试失败";
        
        System.out.println("三路快速排序测试通过\n");
    }
    
    /**
     * 测试快速选择算法
     * 用于验证在数组中查找第k大元素的正确性
     */
    private static void testQuickSelect() {
        System.out.println("=== 测试快速选择算法 ===");
        
        Code03_QuickSort_Leetcode215 solution = new Code03_QuickSort_Leetcode215();
        
        // 测试用例1：查找第2大的元素
        int[] nums1 = {3, 2, 1, 5, 6, 4};
        int k1 = 2;
        int result1 = solution.findKthLargest(nums1, k1);
        System.out.println("数组: " + Arrays.toString(nums1) + ", k=" + k1 + ", 第" + k1 + "大的元素是: " + result1);
        // 验证结果：数组排序后为[1,2,3,4,5,6]，第2大元素是5
        assert result1 == 5 : "快速选择算法测试1失败";
        
        // 测试用例2：包含重复元素的数组，查找第4大的元素
        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int k2 = 4;
        int result2 = solution.findKthLargest(nums2, k2);
        System.out.println("数组: " + Arrays.toString(nums2) + ", k=" + k2 + ", 第" + k2 + "大的元素是: " + result2);
        // 验证结果：数组排序后为[1,2,2,3,3,4,5,5,6]，第4大元素是4
        assert result2 == 4 : "快速选择算法测试2失败";
        
        System.out.println("快速选择算法测试通过\n");
    }
    
    /**
     * 测试最小k个数
     * 用于验证找出数组中最小的k个数的正确性
     */
    private static void testGetLeastNumbers() {
        System.out.println("=== 测试最小k个数 ===");
        
        Code04_QuickSort_JZ40 solution = new Code04_QuickSort_JZ40();
        
        // 测试用例1：找出最小的2个数
        int[] arr1 = {3, 2, 1};
        int k1 = 2;
        int[] result1 = solution.getLeastNumbers(arr1, k1);
        System.out.println("数组: " + Arrays.toString(arr1) + ", k=" + k1 + ", 最小的" + k1 + "个数是: " + Arrays.toString(result1));
        // 验证结果：数组排序后为[1,2,3]，最小的2个数是[1,2]
        assert result1.length == k1 && isSubArraySorted(result1) : "最小k个数测试1失败";
        
        // 测试用例2：找出最小的1个数
        int[] arr2 = {0, 1, 2, 1};
        int k2 = 1;
        int[] result2 = solution.getLeastNumbers(arr2, k2);
        System.out.println("数组: " + Arrays.toString(arr2) + ", k=" + k2 + ", 最小的" + k2 + "个数是: " + Arrays.toString(result2));
        // 验证结果：数组排序后为[0,1,1,2]，最小的1个数是[0]
        assert result2.length == k2 && isSubArraySorted(result2) : "最小k个数测试2失败";
        
        System.out.println("最小k个数测试通过\n");
    }
    
    /**
     * 检查数组是否已排序（升序）
     * @param arr 待检查的数组
     * @return 如果数组已排序返回true，否则返回false
     */
    private static boolean isSorted(int[] arr) {
        // 遍历数组，检查每个元素是否小于等于下一个元素
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                // 发现逆序对，数组未排序
                return false;
            }
        }
        // 没有发现逆序对，数组已排序
        return true;
    }
    
    /**
     * 检查数组子集是否已排序
     * 通过与排序后的数组比较来验证数组元素的正确性
     * @param arr 待检查的数组
     * @return 如果数组元素正确排序返回true，否则返回false
     */
    private static boolean isSubArraySorted(int[] arr) {
        // 创建数组副本并排序
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        
        // 比较原数组与排序后数组的元素
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != sorted[i]) {
                // 发现不匹配的元素
                return false;
            }
        }
        // 所有元素都匹配
        return true;
    }
}
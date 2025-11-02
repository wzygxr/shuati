/**
 * 排序算法变种与优化 - Java版本 (第一部分)
 * 包含快速排序的各种变种实现
 */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AlgorithmVariants_part1 {
    
    /**
     * 快速排序的各种变种
     */
    public static class QuickSortVariants {
        
        /**
         * 基础快速排序
         * 时间复杂度: O(n log n) 平均, O(n²) 最坏
         * 空间复杂度: O(log n) 平均
         */
        public static void quickSortBasic(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            quickSortBasic(nums, 0, nums.length - 1);
        }
        
        private static void quickSortBasic(int[] nums, int left, int right) {
            if (left >= right) return;
            
            int pivotIndex = partitionBasic(nums, left, right);
            quickSortBasic(nums, left, pivotIndex - 1);
            quickSortBasic(nums, pivotIndex + 1, right);
        }
        
        private static int partitionBasic(int[] nums, int left, int right) {
            int pivot = nums[right];
            int i = left;
            
            for (int j = left; j < right; j++) {
                if (nums[j] <= pivot) {
                    swap(nums, i, j);
                    i++;
                }
            }
            swap(nums, i, right);
            return i;
        }
        
        /**
         * 三路快速排序 - 处理大量重复元素
         * 时间复杂度: O(n log n) 平均
         * 空间复杂度: O(log n)
         */
        public static void quickSortThreeWay(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            quickSortThreeWay(nums, 0, nums.length - 1);
        }
        
        private static void quickSortThreeWay(int[] nums, int low, int high) {
            if (low >= high) return;
            
            // 三路划分
            int[] bounds = partitionThreeWay(nums, low, high);
            int lt = bounds[0]; // 小于pivot的右边界
            int gt = bounds[1]; // 大于pivot的左边界
            
            quickSortThreeWay(nums, low, lt - 1);
            quickSortThreeWay(nums, gt + 1, high);
        }
        
        private static int[] partitionThreeWay(int[] nums, int low, int high) {
            int pivot = nums[low];
            int lt = low;     // nums[low..lt-1] < pivot
            int gt = high;    // nums[gt+1..high] > pivot
            int i = low + 1;  // nums[lt..i-1] == pivot
            
            while (i <= gt) {
                if (nums[i] < pivot) {
                    swap(nums, lt++, i++);
                } else if (nums[i] > pivot) {
                    swap(nums, i, gt--);
                } else {
                    i++;
                }
            }
            
            return new int[]{lt, gt};
        }
        
        /**
         * 随机化快速排序 - 避免最坏情况
         * 时间复杂度: O(n log n) 期望
         * 空间复杂度: O(log n)
         */
        public static void quickSortRandomized(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            quickSortRandomized(nums, 0, nums.length - 1);
        }
        
        private static void quickSortRandomized(int[] nums, int left, int right) {
            if (left >= right) return;
            
            // 随机选择基准
            int randomIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
            swap(nums, randomIndex, right);
            
            int pivotIndex = partitionBasic(nums, left, right);
            quickSortRandomized(nums, left, pivotIndex - 1);
            quickSortRandomized(nums, pivotIndex + 1, right);
        }
        
        /**
         * 尾递归优化快速排序 - 减少递归深度
         * 时间复杂度: O(n log n) 平均
         * 空间复杂度: O(log n) 最坏
         */
        public static void quickSortTailRecursive(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            quickSortTailRecursive(nums, 0, nums.length - 1);
        }
        
        private static void quickSortTailRecursive(int[] nums, int left, int right) {
            while (left < right) {
                int pivotIndex = partitionBasic(nums, left, right);
                
                // 递归处理较小的部分，迭代处理较大的部分
                if (pivotIndex - left < right - pivotIndex) {
                    quickSortTailRecursive(nums, left, pivotIndex - 1);
                    left = pivotIndex + 1;
                } else {
                    quickSortTailRecursive(nums, pivotIndex + 1, right);
                    right = pivotIndex - 1;
                }
            }
        }
        
        /**
         * 插入排序优化 - 小数组使用插入排序
         * 时间复杂度: O(n log n) 平均
         * 空间复杂度: O(log n)
         */
        public static void quickSortWithInsertion(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            quickSortWithInsertion(nums, 0, nums.length - 1);
        }
        
        private static void quickSortWithInsertion(int[] nums, int left, int right) {
            // 小数组使用插入排序
            if (right - left + 1 <= 16) {
                insertionSort(nums, left, right);
                return;
            }
            
            int pivotIndex = partitionBasic(nums, left, right);
            quickSortWithInsertion(nums, left, pivotIndex - 1);
            quickSortWithInsertion(nums, pivotIndex + 1, right);
        }
        
        private static void insertionSort(int[] nums, int left, int right) {
            for (int i = left + 1; i <= right; i++) {
                int key = nums[i];
                int j = i - 1;
                while (j >= left && nums[j] > key) {
                    nums[j + 1] = nums[j];
                    j--;
                }
                nums[j + 1] = key;
            }
        }
        
        private static void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        
        /**
         * 测试各种快速排序变种
         */
        public static void testAllVariants() {
            System.out.println("=== 快速排序变种测试 ===");
            
            int[] testData = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            System.out.println("原始数据: " + Arrays.toString(testData));
            
            // 测试基础快速排序
            int[] data1 = testData.clone();
            quickSortBasic(data1);
            System.out.println("基础快速排序: " + Arrays.toString(data1));
            
            // 测试三路快速排序
            int[] data2 = testData.clone();
            quickSortThreeWay(data2);
            System.out.println("三路快速排序: " + Arrays.toString(data2));
            
            // 测试随机化快速排序
            int[] data3 = testData.clone();
            quickSortRandomized(data3);
            System.out.println("随机化快速排序: " + Arrays.toString(data3));
            
            // 测试尾递归优化
            int[] data4 = testData.clone();
            quickSortTailRecursive(data4);
            System.out.println("尾递归优化: " + Arrays.toString(data4));
            
            // 测试插入排序优化
            int[] data5 = testData.clone();
            quickSortWithInsertion(data5);
            System.out.println("插入排序优化: " + Arrays.toString(data5));
        }
    }
    
    /**
     * 归并排序的各种变种
     */
    public static class MergeSortVariants {
        
        /**
         * 递归归并排序
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(n)
         */
        public static void mergeSortRecursive(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            mergeSortRecursive(nums, 0, nums.length - 1);
        }
        
        private static void mergeSortRecursive(int[] nums, int left, int right) {
            if (left >= right) return;
            
            int mid = left + (right - left) / 2;
            mergeSortRecursive(nums, left, mid);
            mergeSortRecursive(nums, mid + 1, right);
            merge(nums, left, mid, right);
        }
        
        /**
         * 迭代归并排序 (自底向上)
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(n)
         */
        public static void mergeSortIterative(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int n = nums.length;
            int[] temp = new int[n];
            
            for (int size = 1; size < n; size *= 2) {
                for (int left = 0; left < n - size; left += 2 * size) {
                    int mid = left + size - 1;
                    int right = Math.min(left + 2 * size - 1, n - 1);
                    merge(nums, left, mid, right);
                }
            }
        }
        
        /**
         * 原地归并排序 (减少空间使用)
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1) 额外空间
         */
        public static void mergeSortInPlace(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            mergeSortInPlace(nums, 0, nums.length - 1);
        }
        
        private static void mergeSortInPlace(int[] nums, int left, int right) {
            if (left >= right) return;
            
            int mid = left + (right - left) / 2;
            mergeSortInPlace(nums, left, mid);
            mergeSortInPlace(nums, mid + 1, right);
            mergeInPlace(nums, left, mid, right);
        }
        
        private static void mergeInPlace(int[] nums, int left, int mid, int right) {
            int i = left, j = mid + 1;
            
            while (i <= mid && j <= right) {
                if (nums[i] <= nums[j]) {
                    i++;
                } else {
                    int value = nums[j];
                    int index = j;
                    
                    // 向右移动元素
                    while (index != i) {
                        nums[index] = nums[index - 1];
                        index--;
                    }
                    nums[i] = value;
                    
                    i++;
                    mid++;
                    j++;
                }
            }
        }
        
        private static void merge(int[] nums, int left, int mid, int right) {
            int[] temp = new int[right - left + 1];
            int i = left, j = mid + 1, k = 0;
            
            while (i <= mid && j <= right) {
                if (nums[i] <= nums[j]) {
                    temp[k++] = nums[i++];
                } else {
                    temp[k++] = nums[j++];
                }
            }
            
            while (i <= mid) temp[k++] = nums[i++];
            while (j <= right) temp[k++] = nums[j++];
            
            System.arraycopy(temp, 0, nums, left, temp.length);
        }
        
        /**
         * 测试各种归并排序变种
         */
        public static void testAllVariants() {
            System.out.println("\n=== 归并排序变种测试 ===");
            
            int[] testData = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            System.out.println("原始数据: " + Arrays.toString(testData));
            
            // 测试递归归并排序
            int[] data1 = testData.clone();
            mergeSortRecursive(data1);
            System.out.println("递归归并排序: " + Arrays.toString(data1));
            
            // 测试迭代归并排序
            int[] data2 = testData.clone();
            mergeSortIterative(data2);
            System.out.println("迭代归并排序: " + Arrays.toString(data2));
            
            // 测试原地归并排序
            int[] data3 = testData.clone();
            mergeSortInPlace(data3);
            System.out.println("原地归并排序: " + Arrays.toString(data3));
        }
    }
    
    // 主函数
    public static void main(String[] args) {
        QuickSortVariants.testAllVariants();
        MergeSortVariants.testAllVariants();
    }
}
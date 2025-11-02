/**
 * 排序算法变种与优化 - Java版本 (第二部分)
 * 包含堆排序、计数排序、基数排序的变种实现
 */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AlgorithmVariants_part2 {
    
    /**
     * 堆排序的各种变种
     */
    public static class HeapSortVariants {
        
        /**
         * 基础堆排序 (最大堆)
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1)
         */
        public static void heapSortBasic(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int n = nums.length;
            
            // 构建最大堆
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(nums, n, i);
            }
            
            // 逐个提取最大元素
            for (int i = n - 1; i > 0; i--) {
                swap(nums, 0, i);
                heapify(nums, i, 0);
            }
        }
        
        /**
         * 最小堆排序 (降序排序)
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1)
         */
        public static void heapSortMinHeap(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int n = nums.length;
            
            // 构建最小堆
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapifyMin(nums, n, i);
            }
            
            // 逐个提取最小元素
            for (int i = n - 1; i > 0; i--) {
                swap(nums, 0, i);
                heapifyMin(nums, i, 0);
            }
        }
        
        /**
         * 原地堆排序 (优化空间使用)
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1)
         */
        public static void heapSortInPlace(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            heapSortBasic(nums); // 基础堆排序已经是原地排序
        }
        
        /**
         * 堆排序优化 - 减少交换次数
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1)
         */
        public static void heapSortOptimized(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int n = nums.length;
            
            // 构建堆的优化版本
            for (int i = (n - 2) / 2; i >= 0; i--) {
                siftDown(nums, i, n);
            }
            
            // 排序优化版本
            for (int i = n - 1; i > 0; i--) {
                swap(nums, 0, i);
                siftDown(nums, 0, i);
            }
        }
        
        private static void heapify(int[] nums, int n, int i) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < n && nums[left] > nums[largest]) {
                largest = left;
            }
            
            if (right < n && nums[right] > nums[largest]) {
                largest = right;
            }
            
            if (largest != i) {
                swap(nums, i, largest);
                heapify(nums, n, largest);
            }
        }
        
        private static void heapifyMin(int[] nums, int n, int i) {
            int smallest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < n && nums[left] < nums[smallest]) {
                smallest = left;
            }
            
            if (right < n && nums[right] < nums[smallest]) {
                smallest = right;
            }
            
            if (smallest != i) {
                swap(nums, i, smallest);
                heapifyMin(nums, n, smallest);
            }
        }
        
        private static void siftDown(int[] nums, int i, int n) {
            int value = nums[i];
            
            while (2 * i + 1 < n) {
                int child = 2 * i + 1;
                
                // 选择较大的子节点
                if (child + 1 < n && nums[child + 1] > nums[child]) {
                    child++;
                }
                
                if (value >= nums[child]) {
                    break;
                }
                
                nums[i] = nums[child];
                i = child;
            }
            
            nums[i] = value;
        }
        
        private static void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        
        /**
         * 测试各种堆排序变种
         */
        public static void testAllVariants() {
            System.out.println("=== 堆排序变种测试 ===");
            
            int[] testData = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            System.out.println("原始数据: " + Arrays.toString(testData));
            
            // 测试基础堆排序
            int[] data1 = testData.clone();
            heapSortBasic(data1);
            System.out.println("基础堆排序: " + Arrays.toString(data1));
            
            // 测试最小堆排序
            int[] data2 = testData.clone();
            heapSortMinHeap(data2);
            System.out.println("最小堆排序: " + Arrays.toString(data2));
            
            // 测试优化堆排序
            int[] data3 = testData.clone();
            heapSortOptimized(data3);
            System.out.println("优化堆排序: " + Arrays.toString(data3));
        }
    }
    
    /**
     * 计数排序的各种变种
     */
    public static class CountingSortVariants {
        
        /**
         * 基础计数排序 (非负整数)
         * 时间复杂度: O(n + k)
         * 空间复杂度: O(k)
         */
        public static void countingSortBasic(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            // 找出最大值
            int max = Arrays.stream(nums).max().getAsInt();
            
            // 创建计数数组
            int[] count = new int[max + 1];
            
            // 统计每个数字出现的次数
            for (int num : nums) {
                count[num]++;
            }
            
            // 重新填充数组
            int index = 0;
            for (int i = 0; i <= max; i++) {
                while (count[i] > 0) {
                    nums[index++] = i;
                    count[i]--;
                }
            }
        }
        
        /**
         * 稳定计数排序 (保持相同元素的相对顺序)
         * 时间复杂度: O(n + k)
         * 空间复杂度: O(n + k)
         */
        public static void countingSortStable(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int max = Arrays.stream(nums).max().getAsInt();
            int min = Arrays.stream(nums).min().getAsInt();
            int range = max - min + 1;
            
            int[] count = new int[range];
            int[] output = new int[nums.length];
            
            // 统计频率
            for (int num : nums) {
                count[num - min]++;
            }
            
            // 计算累积频率
            for (int i = 1; i < range; i++) {
                count[i] += count[i - 1];
            }
            
            // 从后向前遍历，保证稳定性
            for (int i = nums.length - 1; i >= 0; i--) {
                output[count[nums[i] - min] - 1] = nums[i];
                count[nums[i] - min]--;
            }
            
            // 复制回原数组
            System.arraycopy(output, 0, nums, 0, nums.length);
        }
        
        /**
         * 计数排序优化 - 处理负数
         * 时间复杂度: O(n + k)
         * 空间复杂度: O(k)
         */
        public static void countingSortWithNegative(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int max = Arrays.stream(nums).max().getAsInt();
            int min = Arrays.stream(nums).min().getAsInt();
            int range = max - min + 1;
            
            int[] count = new int[range];
            
            // 统计频率
            for (int num : nums) {
                count[num - min]++;
            }
            
            // 重新填充数组
            int index = 0;
            for (int i = 0; i < range; i++) {
                while (count[i] > 0) {
                    nums[index++] = i + min;
                    count[i]--;
                }
            }
        }
        
        /**
         * 测试各种计数排序变种
         */
        public static void testAllVariants() {
            System.out.println("\n=== 计数排序变种测试 ===");
            
            // 测试非负整数
            int[] testData1 = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            System.out.println("原始数据(非负): " + Arrays.toString(testData1));
            
            int[] data1 = testData1.clone();
            countingSortBasic(data1);
            System.out.println("基础计数排序: " + Arrays.toString(data1));
            
            // 测试包含负数
            int[] testData2 = {3, -1, 4, 1, -5, 9, 2, -6, 5, 3, 5};
            System.out.println("原始数据(含负数): " + Arrays.toString(testData2));
            
            int[] data2 = testData2.clone();
            countingSortWithNegative(data2);
            System.out.println("负数计数排序: " + Arrays.toString(data2));
            
            // 测试稳定排序
            int[] testData3 = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            System.out.println("原始数据(稳定性测试): " + Arrays.toString(testData3));
            
            int[] data3 = testData3.clone();
            countingSortStable(data3);
            System.out.println("稳定计数排序: " + Arrays.toString(data3));
        }
    }
    
    /**
     * 基数排序的各种变种
     */
    public static class RadixSortVariants {
        
        /**
         * LSD基数排序 (最低位优先)
         * 时间复杂度: O(d * (n + k))
         * 空间复杂度: O(n + k)
         */
        public static void radixSortLSD(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int max = Arrays.stream(nums).max().getAsInt();
            
            // 按每一位进行计数排序
            for (int exp = 1; max / exp > 0; exp *= 10) {
                countingSortByDigit(nums, exp);
            }
        }
        
        /**
         * MSD基数排序 (最高位优先)
         * 时间复杂度: O(d * (n + k))
         * 空间复杂度: O(n + k)
         */
        public static void radixSortMSD(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            radixSortMSD(nums, 0, nums.length - 1, getMaxDigits(nums));
        }
        
        private static void radixSortMSD(int[] nums, int low, int high, int digit) {
            if (digit <= 0 || low >= high) return;
            
            // 使用计数排序按当前位排序
            int[] count = new int[10];
            int[] output = new int[high - low + 1];
            int exp = (int) Math.pow(10, digit - 1);
            
            // 统计频率
            for (int i = low; i <= high; i++) {
                int digitValue = (nums[i] / exp) % 10;
                count[digitValue]++;
            }
            
            // 计算累积频率
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            
            // 从后向前遍历
            for (int i = high; i >= low; i--) {
                int digitValue = (nums[i] / exp) % 10;
                output[count[digitValue] - 1] = nums[i];
                count[digitValue]--;
            }
            
            // 复制回原数组
            System.arraycopy(output, 0, nums, low, output.length);
            
            // 递归处理每个桶
            int start = low;
            for (int i = 0; i < 10; i++) {
                int bucketSize = (i == 0) ? count[0] : count[i] - count[i - 1];
                if (bucketSize > 1) {
                    radixSortMSD(nums, start, start + bucketSize - 1, digit - 1);
                }
                start += bucketSize;
            }
        }
        
        /**
         * 基数排序优化 - 处理负数
         * 时间复杂度: O(d * (n + k))
         * 空间复杂度: O(n + k)
         */
        public static void radixSortWithNegative(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            // 分离正负数
            List<Integer> positives = new ArrayList<>();
            List<Integer> negatives = new ArrayList<>();
            
            for (int num : nums) {
                if (num >= 0) {
                    positives.add(num);
                } else {
                    negatives.add(-num); // 转换为正数处理
                }
            }
            
            // 分别排序
            int[] posArr = positives.stream().mapToInt(i -> i).toArray();
            int[] negArr = negatives.stream().mapToInt(i -> i).toArray();
            
            if (posArr.length > 0) radixSortLSD(posArr);
            if (negArr.length > 0) radixSortLSD(negArr);
            
            // 合并结果 (负数需要反转)
            int index = 0;
            for (int i = negArr.length - 1; i >= 0; i--) {
                nums[index++] = -negArr[i];
            }
            for (int num : posArr) {
                nums[index++] = num;
            }
        }
        
        private static void countingSortByDigit(int[] nums, int exp) {
            int n = nums.length;
            int[] output = new int[n];
            int[] count = new int[10];
            
            // 统计频率
            for (int i = 0; i < n; i++) {
                int digit = (nums[i] / exp) % 10;
                count[digit]++;
            }
            
            // 计算累积频率
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            
            // 从后向前遍历
            for (int i = n - 1; i >= 0; i--) {
                int digit = (nums[i] / exp) % 10;
                output[count[digit] - 1] = nums[i];
                count[digit]--;
            }
            
            // 复制回原数组
            System.arraycopy(output, 0, nums, 0, n);
        }
        
        private static int getMaxDigits(int[] nums) {
            int max = Arrays.stream(nums).max().getAsInt();
            return (int) Math.log10(max) + 1;
        }
        
        /**
         * 测试各种基数排序变种
         */
        public static void testAllVariants() {
            System.out.println("\n=== 基数排序变种测试 ===");
            
            int[] testData = {170, 45, 75, 90, 2, 802, 24, 66};
            System.out.println("原始数据: " + Arrays.toString(testData));
            
            int[] data1 = testData.clone();
            radixSortLSD(data1);
            System.out.println("LSD基数排序: " + Arrays.toString(data1));
            
            int[] data2 = testData.clone();
            radixSortMSD(data2);
            System.out.println("MSD基数排序: " + Arrays.toString(data2));
            
            // 测试负数处理
            int[] testData2 = {170, -45, 75, -90, 2, 802, -24, 66};
            System.out.println("原始数据(含负数): " + Arrays.toString(testData2));
            
            int[] data3 = testData2.clone();
            radixSortWithNegative(data3);
            System.out.println("负数基数排序: " + Arrays.toString(data3));
        }
    }
    
    // 主函数
    public static void main(String[] args) {
        HeapSortVariants.testAllVariants();
        CountingSortVariants.testAllVariants();
        RadixSortVariants.testAllVariants();
    }
}
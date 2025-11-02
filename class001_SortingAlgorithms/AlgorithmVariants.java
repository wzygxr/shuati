import java.util.*;

/**
 * 排序算法变种和优化版本 - Java版本
 * 包含各种排序算法的变种实现和特殊场景优化
 * 
 * 算法变种列表:
 * 1. 三路快速排序变种 - 针对大量重复元素的优化版本
 * 2. 自底向上的归并排序（迭代版本）- 避免递归调用，节省栈空间
 * 3. 原地归并排序 - 空间复杂度优化版本
 * 4. 计数排序（非比较排序）- 适用于元素范围有限的整数排序
 * 5. 基数排序（非比较排序）- 适用于整数或字符串排序
 * 6. 桶排序 - 适用于均匀分布的浮点数排序
 * 7. 睡眠排序（搞笑算法，实际不可用）- 每个元素启动一个线程，睡眠对应时间后输出
 * 8. 猴子排序（Bogo Sort，随机算法）- 不断随机排列数组，直到有序
 * 9. 鸡尾酒排序（双向冒泡排序） - 冒泡排序的优化版本，双向进行
 * 10. 梳排序（Comb Sort） - 冒泡排序的改进，使用较大的间隔
 */

public class AlgorithmVariants {
    
    /**
     * 1. 三路快速排序变种
     * 针对大量重复元素的优化版本
     * 时间复杂度: O(n log n) 平均
     * 空间复杂度: O(log n)
     */
    public static class ThreeWayQuickSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            threeWayQuickSort(arr, 0, arr.length - 1);
        }
        
        private static void threeWayQuickSort(int[] arr, int low, int high) {
            if (low >= high) return;
            
            // 三路分区
            int[] partition = threeWayPartition(arr, low, high);
            int lt = partition[0];  // 小于区域的右边界
            int gt = partition[1];  // 大于区域的左边界
            
            // 递归排序小于区域和大于区域
            threeWayQuickSort(arr, low, lt - 1);
            threeWayQuickSort(arr, gt + 1, high);
            // 等于区域已经有序，不需要排序
        }
        
        private static int[] threeWayPartition(int[] arr, int low, int high) {
            int pivot = arr[low];  // 选择第一个元素作为基准
            int lt = low;     // 小于区域的右边界
            int gt = high;    // 大于区域的左边界
            int i = low + 1;  // 当前指针
            
            while (i <= gt) {
                if (arr[i] < pivot) {
                    swap(arr, lt++, i++);
                } else if (arr[i] > pivot) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }
            
            return new int[]{lt, gt};
        }
        
        public static void test() {
            System.out.println("=== 三路快速排序测试 ===");
            int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 2. 自底向上的归并排序（迭代版本）
     * 避免递归调用，节省栈空间
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static class IterativeMergeSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            
            int n = arr.length;
            int[] aux = new int[n];
            
            // 从1开始，每次倍增
            for (int size = 1; size < n; size *= 2) {
                for (int left = 0; left < n - size; left += 2 * size) {
                    int mid = left + size - 1;
                    int right = Math.min(left + 2 * size - 1, n - 1);
                    merge(arr, aux, left, mid, right);
                }
            }
        }
        
        private static void merge(int[] arr, int[] aux, int left, int mid, int right) {
            // 复制到辅助数组
            for (int k = left; k <= right; k++) {
                aux[k] = arr[k];
            }
            
            int i = left, j = mid + 1;
            for (int k = left; k <= right; k++) {
                if (i > mid) {
                    arr[k] = aux[j++];
                } else if (j > right) {
                    arr[k] = aux[i++];
                } else if (aux[i] <= aux[j]) {
                    arr[k] = aux[i++];
                } else {
                    arr[k] = aux[j++];
                }
            }
        }
        
        public static void test() {
            System.out.println("\n=== 迭代归并排序测试 ===");
            int[] arr = {64, 34, 25, 12, 22, 11, 90};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 3. 原地归并排序
     * 空间复杂度优化版本，但时间复杂度稍差
     * 时间复杂度: O(n log² n)
     * 空间复杂度: O(1)
     */
    public static class InPlaceMergeSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            inPlaceMergeSort(arr, 0, arr.length - 1);
        }
        
        private static void inPlaceMergeSort(int[] arr, int left, int right) {
            if (left >= right) return;
            
            int mid = left + (right - left) / 2;
            inPlaceMergeSort(arr, left, mid);
            inPlaceMergeSort(arr, mid + 1, right);
            inPlaceMerge(arr, left, mid, right);
        }
        
        private static void inPlaceMerge(int[] arr, int left, int mid, int right) {
            int i = left;
            int j = mid + 1;
            
            while (i <= mid && j <= right) {
                if (arr[i] <= arr[j]) {
                    i++;
                } else {
                    // 将arr[j]插入到arr[i]的位置
                    int value = arr[j];
                    int index = j;
                    
                    // 向右移动元素
                    while (index > i) {
                        arr[index] = arr[index - 1];
                        index--;
                    }
                    arr[i] = value;
                    
                    // 更新指针
                    i++;
                    mid++;
                    j++;
                }
            }
        }
        
        public static void test() {
            System.out.println("\n=== 原地归并排序测试 ===");
            int[] arr = {5, 2, 8, 1, 9, 3};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 4. 计数排序（非比较排序）
     * 适用于元素范围有限的整数排序
     * 时间复杂度: O(n + k)，k为元素范围
     * 空间复杂度: O(k)
     */
    public static class CountingSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            
            // 找到最大值和最小值
            int max = Arrays.stream(arr).max().getAsInt();
            int min = Arrays.stream(arr).min().getAsInt();
            int range = max - min + 1;
            
            // 创建计数数组
            int[] count = new int[range];
            int[] output = new int[arr.length];
            
            // 统计每个元素的出现次数
            for (int num : arr) {
                count[num - min]++;
            }
            
            // 计算累积计数
            for (int i = 1; i < range; i++) {
                count[i] += count[i - 1];
            }
            
            // 构建输出数组（保持稳定性）
            for (int i = arr.length - 1; i >= 0; i--) {
                output[count[arr[i] - min] - 1] = arr[i];
                count[arr[i] - min]--;
            }
            
            // 复制回原数组
            System.arraycopy(output, 0, arr, 0, arr.length);
        }
        
        public static void test() {
            System.out.println("\n=== 计数排序测试 ===");
            int[] arr = {4, 2, 2, 8, 3, 3, 1};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 5. 基数排序（非比较排序）
     * 适用于整数或字符串排序
     * 时间复杂度: O(d * (n + k))，d为最大数字位数
     * 空间复杂度: O(n + k)
     */
    public static class RadixSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            
            // 找到最大值，确定最大位数
            int max = Arrays.stream(arr).max().getAsInt();
            
            // 从最低位到最高位进行计数排序
            for (int exp = 1; max / exp > 0; exp *= 10) {
                countingSortByDigit(arr, exp);
            }
        }
        
        private static void countingSortByDigit(int[] arr, int exp) {
            int n = arr.length;
            int[] output = new int[n];
            int[] count = new int[10];
            
            // 统计当前位的数字出现次数
            for (int num : arr) {
                count[(num / exp) % 10]++;
            }
            
            // 计算累积计数
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            
            // 构建输出数组（从后往前保持稳定性）
            for (int i = n - 1; i >= 0; i--) {
                int digit = (arr[i] / exp) % 10;
                output[count[digit] - 1] = arr[i];
                count[digit]--;
            }
            
            // 复制回原数组
            System.arraycopy(output, 0, arr, 0, n);
        }
        
        public static void test() {
            System.out.println("\n=== 基数排序测试 ===");
            int[] arr = {170, 45, 75, 90, 2, 802, 24, 66};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 6. 桶排序
     * 适用于均匀分布的浮点数排序
     * 时间复杂度: O(n + k) 平均，O(n²) 最坏
     * 空间复杂度: O(n + k)
     */
    public static class BucketSort {
        
        public static void sort(double[] arr) {
            if (arr == null || arr.length <= 1) return;
            
            int n = arr.length;
            
            // 创建桶
            List<Double>[] buckets = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                buckets[i] = new ArrayList<>();
            }
            
            // 将元素分配到桶中
            for (double num : arr) {
                int bucketIndex = (int) (num * n);
                buckets[bucketIndex].add(num);
            }
            
            // 对每个桶进行排序
            for (List<Double> bucket : buckets) {
                Collections.sort(bucket);
            }
            
            // 合并桶
            int index = 0;
            for (List<Double> bucket : buckets) {
                for (double num : bucket) {
                    arr[index++] = num;
                }
            }
        }
        
        public static void test() {
            System.out.println("\n=== 桶排序测试 ===");
            double[] arr = {0.42, 0.32, 0.33, 0.52, 0.37, 0.47, 0.51};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 7. 睡眠排序（搞笑算法，实际不可用）
     * 每个元素启动一个线程，睡眠对应时间后输出
     * 时间复杂度: O(max(arr)) 实际不可用
     */
    public static class SleepSort {
        
        public static void sort(int[] arr) throws InterruptedException {
            if (arr == null || arr.length == 0) return;
            
            List<Integer> result = Collections.synchronizedList(new ArrayList<>());
            List<Thread> threads = new ArrayList<>();
            
            for (int num : arr) {
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(num * 10L); // 睡眠时间与数值成正比
                        result.add(num);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                threads.add(thread);
                thread.start();
            }
            
            // 等待所有线程完成
            for (Thread thread : threads) {
                thread.join();
            }
            
            // 将结果复制回原数组（仅用于演示）
            for (int i = 0; i < arr.length; i++) {
                arr[i] = result.get(i);
            }
        }
        
        public static void test() throws InterruptedException {
            System.out.println("\n=== 睡眠排序测试（仅演示） ===");
            int[] arr = {3, 1, 4, 1, 5};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
            System.out.println("注意：睡眠排序仅用于演示，实际不可用！");
        }
    }
    
    /**
     * 8. 猴子排序（Bogo Sort，随机算法）
     * 不断随机排列数组，直到有序
     * 时间复杂度: O(∞) 平均，O(n!) 最坏
     */
    public static class BogoSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            
            Random random = new Random();
            int attempts = 0;
            int maxAttempts = 1000000; // 防止无限循环
            
            while (!isSorted(arr) && attempts < maxAttempts) {
                shuffle(arr, random);
                attempts++;
            }
            
            if (attempts >= maxAttempts) {
                System.out.println("猴子排序失败，尝试次数: " + attempts);
            } else {
                System.out.println("猴子排序成功，尝试次数: " + attempts);
            }
        }
        
        private static void shuffle(int[] arr, Random random) {
            for (int i = arr.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                swap(arr, i, j);
            }
        }
        
        private static boolean isSorted(int[] arr) {
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] < arr[i - 1]) {
                    return false;
                }
            }
            return true;
        }
        
        public static void test() {
            System.out.println("\n=== 猴子排序测试（仅小数组演示） ===");
            int[] arr = {3, 1, 2};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
            System.out.println("注意：猴子排序仅用于演示，实际不可用！");
        }
    }
    
    /**
     * 9. 鸡尾酒排序（双向冒泡排序）
     * 冒泡排序的优化版本，双向进行
     * 时间复杂度: O(n²)
     * 空间复杂度: O(1)
     */
    public static class CocktailSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            
            boolean swapped = true;
            int start = 0;
            int end = arr.length - 1;
            
            while (swapped) {
                swapped = false;
                
                // 从左到右
                for (int i = start; i < end; i++) {
                    if (arr[i] > arr[i + 1]) {
                        swap(arr, i, i + 1);
                        swapped = true;
                    }
                }
                
                if (!swapped) break;
                
                swapped = false;
                end--;
                
                // 从右到左
                for (int i = end - 1; i >= start; i--) {
                    if (arr[i] > arr[i + 1]) {
                        swap(arr, i, i + 1);
                        swapped = true;
                    }
                }
                
                start++;
            }
        }
        
        public static void test() {
            System.out.println("\n=== 鸡尾酒排序测试 ===");
            int[] arr = {5, 1, 4, 2, 8, 0, 2};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 10. 梳排序（Comb Sort）
     * 冒泡排序的改进，使用较大的间隔
     * 时间复杂度: O(n²) 最坏，O(n log n) 平均
     */
    public static class CombSort {
        
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            
            int n = arr.length;
            int gap = n;
            boolean swapped = true;
            double shrink = 1.3; // 收缩因子
            
            while (gap > 1 || swapped) {
                gap = Math.max(1, (int)(gap / shrink));
                swapped = false;
                
                for (int i = 0; i + gap < n; i++) {
                    if (arr[i] > arr[i + gap]) {
                        swap(arr, i, i + gap);
                        swapped = true;
                    }
                }
            }
        }
        
        public static void test() {
            System.out.println("\n=== 梳排序测试 ===");
            int[] arr = {8, 4, 1, 56, 3, -44, 23, -6, 28, 0};
            System.out.println("原始数组: " + Arrays.toString(arr));
            
            sort(arr);
            System.out.println("排序后: " + Arrays.toString(arr));
        }
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * 运行所有变种算法测试
     */
    public static void runAllTests() {
        ThreeWayQuickSort.test();
        IterativeMergeSort.test();
        InPlaceMergeSort.test();
        CountingSort.test();
        RadixSort.test();
        BucketSort.test();
        CocktailSort.test();
        CombSort.test();
        
        // 注意：以下算法仅用于演示，实际不可用
        try {
            SleepSort.test();
        } catch (InterruptedException e) {
            System.out.println("睡眠排序被中断");
        }
        BogoSort.test();
    }
    
    public static void main(String[] args) {
        runAllTests();
    }
}
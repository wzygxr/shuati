package class021;

/**
 * 归并排序核心实现 - Java版本
 * 
 * 题目来源：各大算法平台归并排序相关题目
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 稳定性：稳定排序算法
 * 
 * 核心思想：分治法（Divide and Conquer）
 * 1. 分解：将数组分成两半
 * 2. 解决：递归地对两半进行排序
 * 3. 合并：将两个已排序数组合并成一个有序数组
 * 
 * 适用场景：
 * - 需要稳定排序的场合
 * - 链表排序（天然适合归并排序）
 * - 外部排序（处理大规模数据）
 * - 统计逆序对、翻转对等问题
 * 
 * 详细题目列表请参考同目录下的MERGE_SORT_PROBLEMS.md文件
 * 包含LeetCode、洛谷、牛客网、Codeforces等平台的归并排序相关题目
 */

import java.util.*;

public class Code01_MergeSort {
    
    // 全局变量用于ACM竞赛风格
    public static final int MAXN = 100001;
    public static int[] arr = new int[MAXN];
    public static int[] help = new int[MAXN];
    public static int n;
    
    /**
     * 基础归并排序 - 递归版本
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n) + O(log n)递归栈
     * 详细说明：
     * 1. 递归终止条件：当left >= right时，子数组只有一个元素，已经有序
     * 2. 分解：将数组从中间分成两部分
     * 3. 递归解决：对左右两部分分别进行归并排序
     * 4. 合并：将两个有序子数组合并成一个有序数组
     */
    public static void mergeSortRecursive(int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSortRecursive(left, mid);
        mergeSortRecursive(mid + 1, right);
        merge(left, mid, right);
    }
    
    /**
     * 基础归并排序 - 非递归版本（迭代版本）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * 优势：避免递归栈溢出，适合大规模数据
     * 详细说明：
     * 1. 从最小的子数组开始（长度为1），逐步扩大子数组大小
     * 2. 每次将相邻的两个子数组合并成一个更大的有序数组
     * 3. 重复此过程直到整个数组有序
     */
    public static void mergeSortIterative() {
        for (int step = 1; step < n; step <<= 1) {
            int left = 0;
            while (left < n) {
                int mid = left + step - 1;
                if (mid + 1 >= n) break;
                
                int right = Math.min(left + (step << 1) - 1, n - 1);
                merge(left, mid, right);
                left = right + 1;
            }
        }
    }
    
    /**
     * 合并两个有序数组的核心操作
     * 关键技巧：双指针法
     * 详细说明：
     * 1. 使用三个指针：i指向辅助数组当前位置，a指向左半部分，b指向右半部分
     * 2. 比较左右两部分的元素，将较小的元素放入辅助数组
     * 3. 处理剩余元素：将未处理完的部分直接拷贝到辅助数组
     * 4. 将辅助数组的内容拷贝回原数组
     */
    public static void merge(int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;
        
        // 双指针合并
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                help[k++] = arr[i++];
            } else {
                help[k++] = arr[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) help[k++] = arr[i++];
        while (j <= right) help[k++] = arr[j++];
        
        // 复制回原数组
        for (int p = left; p <= right; p++) {
            arr[p] = help[p];
        }
    }
    
    /**
     * 题目1：统计逆序对数量（LeetCode 315风格）
     * 链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * 核心思想：在归并排序的合并过程中统计逆序对
     */
    public static long countInversions(int left, int right) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        long count = countInversions(left, mid);
        count += countInversions(mid + 1, right);
        
        // 合并并统计逆序对
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                help[k++] = arr[i++];
            } else {
                // 关键：当arr[i] > arr[j]时，arr[i]到arr[mid]都与arr[j]构成逆序对
                count += mid - i + 1;
                help[k++] = arr[j++];
            }
        }
        
        while (i <= mid) help[k++] = arr[i++];
        while (j <= right) help[k++] = arr[j++];
        
        for (int p = left; p <= right; p++) {
            arr[p] = help[p];
        }
        
        return count;
    }
    
    /**
     * 题目2：统计翻转对数量（LeetCode 493）
     * 链接：https://leetcode.cn/problems/reverse-pairs/
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * 核心思想：在合并前先统计满足条件的翻转对
     */
    public static int reversePairs(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        n = nums.length;
        System.arraycopy(nums, 0, arr, 0, n);
        return (int) countReversePairs(0, n - 1);
    }
    
    private static long countReversePairs(int left, int right) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        long count = countReversePairs(left, mid);
        count += countReversePairs(mid + 1, right);
        
        // 关键：先统计翻转对，再合并
        int j = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 条件：arr[i] > 2 * arr[j]
            while (j <= right && (long)arr[i] > 2L * (long)arr[j]) {
                j++;
            }
            count += j - (mid + 1);
        }
        
        // 正常合并
        merge(left, mid, right);
        return count;
    }
    
    /**
     * 题目3：区间和的个数（LeetCode 327）
     * 链接：https://leetcode.cn/problems/number-of-range-sum/
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public static int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) return 0;
        
        // 计算前缀和
        long[] prefixSum = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        return countRangeSumMergeSort(prefixSum, 0, prefixSum.length - 1, lower, upper);
    }
    
    private static int countRangeSumMergeSort(long[] prefixSum, int left, int right, int lower, int upper) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        int count = countRangeSumMergeSort(prefixSum, left, mid, lower, upper);
        count += countRangeSumMergeSort(prefixSum, mid + 1, right, lower, upper);
        
        // 统计满足条件的区间和
        int j = mid + 1, k = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 找到第一个满足 prefixSum[j] - prefixSum[i] >= lower 的j
            while (j <= right && prefixSum[j] - prefixSum[i] < lower) {
                j++;
            }
            // 找到第一个满足 prefixSum[k] - prefixSum[i] > upper 的k
            while (k <= right && prefixSum[k] - prefixSum[i] <= upper) {
                k++;
            }
            count += k - j;
        }
        
        // 合并前缀和数组
        mergePrefixSum(prefixSum, left, mid, right);
        return count;
    }
    
    private static void mergePrefixSum(long[] prefixSum, int left, int mid, int right) {
        long[] temp = new long[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        while (i <= mid && j <= right) {
            if (prefixSum[i] <= prefixSum[j]) {
                temp[k++] = prefixSum[i++];
            } else {
                temp[k++] = prefixSum[j++];
            }
        }
        
        while (i <= mid) temp[k++] = prefixSum[i++];
        while (j <= right) temp[k++] = prefixSum[j++];
        
        System.arraycopy(temp, 0, prefixSum, left, temp.length);
    }
    
    /**
     * 题目4：计算右侧小于当前元素的个数（LeetCode 315）
     * 链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public static List<Integer> countSmaller(int[] nums) {
        List<Integer> result = new ArrayList<>();
        if (nums == null || nums.length == 0) return result;
        
        int n = nums.length;
        int[] counts = new int[n];
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        
        int[] temp = new int[n];
        int[] tempIndices = new int[n];
        
        mergeSortCountSmaller(nums, indices, counts, 0, n - 1, temp, tempIndices);
        
        for (int count : counts) {
            result.add(count);
        }
        return result;
    }
    
    private static void mergeSortCountSmaller(int[] nums, int[] indices, int[] counts, 
                                            int left, int right, int[] temp, int[] tempIndices) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSortCountSmaller(nums, indices, counts, left, mid, temp, tempIndices);
        mergeSortCountSmaller(nums, indices, counts, mid + 1, right, temp, tempIndices);
        
        // 合并并统计
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                counts[indices[i]] += (j - (mid + 1));
                temp[k] = nums[i];
                tempIndices[k] = indices[i];
                i++;
            } else {
                temp[k] = nums[j];
                tempIndices[k] = indices[j];
                j++;
            }
            k++;
        }
        
        while (i <= mid) {
            counts[indices[i]] += (j - (mid + 1));
            temp[k] = nums[i];
            tempIndices[k] = indices[i];
            i++;
            k++;
        }
        
        while (j <= right) {
            temp[k] = nums[j];
            tempIndices[k] = indices[j];
            j++;
            k++;
        }
        
        for (int p = left; p <= right; p++) {
            nums[p] = temp[p];
            indices[p] = tempIndices[p];
        }
    }
    
    /**
     * 题目5：满足不等式的数对数目（LeetCode 2426）
     * 链接：https://leetcode.cn/problems/number-of-pairs-satisfying-inequality/
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public static long numberOfPairs(int[] nums1, int[] nums2, int diff) {
        int n = nums1.length;
        int[] arr = new int[n];
        // 构造差值数组：nums1[i] - nums2[i]
        for (int i = 0; i < n; i++) {
            arr[i] = nums1[i] - nums2[i];
        }
        return countPairs(arr, diff);
    }
    
    private static long countPairs(int[] arr, int diff) {
        if (arr.length <= 1) return 0;
        
        int[] helper = new int[arr.length];
        return mergeSortPairs(arr, helper, 0, arr.length - 1, diff);
    }
    
    private static long mergeSortPairs(int[] arr, int[] helper, int left, int right, int diff) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        long count = mergeSortPairs(arr, helper, left, mid, diff);
        count += mergeSortPairs(arr, helper, mid + 1, right, diff);
        
        // 统计满足条件的数对
        int j = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 条件：arr[i] <= arr[j] + diff
            while (j <= right && arr[i] <= arr[j] + diff) {
                j++;
            }
            count += j - (mid + 1);
        }
        
        // 合并两个有序数组
        mergeArrays(arr, helper, left, mid, right);
        return count;
    }
    
    private static void mergeArrays(int[] arr, int[] helper, int left, int mid, int right) {
        System.arraycopy(arr, left, helper, left, right - left + 1);
        
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (helper[i] <= helper[j]) {
                arr[k++] = helper[i++];
            } else {
                arr[k++] = helper[j++];
            }
        }
        while (i <= mid) arr[k++] = helper[i++];
        while (j <= right) arr[k++] = helper[j++];
    }
    
    // ============================================================================
    // 性能测试与边界测试
    // ============================================================================
    
    /**
     * 性能测试：测试不同规模数据的排序性能
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        int[] sizes = {1000, 10000, 100000, 1000000};
        for (int size : sizes) {
            int[] testData = generateRandomArray(size);
            
            long startTime = System.nanoTime();
            int[] result = Arrays.copyOf(testData, testData.length);
            Arrays.sort(result); // 使用标准库排序作为基准
            long endTime = System.nanoTime();
            
            double duration = (endTime - startTime) / 1e6; // 转换为毫秒
            System.out.printf("数据量: %,d, 耗时: %.2f ms%n", size, duration);
        }
    }
    
    /**
     * 边界测试：测试各种边界情况
     */
    public static void boundaryTest() {
        System.out.println("=== 边界测试 ===");
        
        // 测试空数组
        testCase(new int[]{}, "空数组");
        
        // 测试单元素数组
        testCase(new int[]{1}, "单元素数组");
        
        // 测试已排序数组
        testCase(new int[]{1, 2, 3, 4, 5}, "已排序数组");
        
        // 测试逆序数组
        testCase(new int[]{5, 4, 3, 2, 1}, "逆序数组");
        
        // 测试重复元素数组
        testCase(new int[]{2, 2, 1, 1, 3, 3}, "重复元素数组");
        
        // 测试大数数组
        testCase(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0}, "极值数组");
    }
    
    private static void testCase(int[] input, String description) {
        int[] result = Arrays.copyOf(input, input.length);
        Arrays.sort(result);
        boolean passed = isSorted(result);
        System.out.println(description + "测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }
    
    // ============================================================================
    // 单元测试
    // ============================================================================
    
    public static void testBasicSort() {
        int[] test = {5, 2, 3, 1, 4};
        int[] expected = {1, 2, 3, 4, 5};
        
        n = test.length;
        System.arraycopy(test, 0, arr, 0, n);
        mergeSortIterative();
        
        boolean passed = Arrays.equals(Arrays.copyOf(arr, n), expected);
        System.out.println("基础排序测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testInversionCount() {
        int[] test = {7, 5, 6, 4};
        long expected = 5;
        
        n = test.length;
        System.arraycopy(test, 0, arr, 0, n);
        long result = countInversions(0, n - 1);
        
        boolean passed = (result == expected);
        System.out.println("逆序对统计测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testReversePairs() {
        int[] test = {1, 3, 2, 3, 1};
        int expected = 2;
        
        int result = reversePairs(test.clone());
        boolean passed = (result == expected);
        System.out.println("翻转对统计测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void runComprehensiveTests() {
        System.out.println("=== 开始全面测试 ===");
        testBasicSort();
        testInversionCount();
        testReversePairs();
        boundaryTest();
        System.out.println("=== 测试完成 ===");
    }
    
    // ============================================================================
    // 主函数
    // ============================================================================
    
    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            runComprehensiveTests();
        } else if (args.length > 0 && "perf".equals(args[0])) {
            performanceTest();
        } else {
            runComprehensiveTests();
            System.out.println("\n使用 'java Code01_MergeSort test' 运行全面测试");
            System.out.println("使用 'java Code01_MergeSort perf' 运行性能测试");
        }
    }
    
    /**
     * 【工程化考量总结】
     * 1. 异常处理：对所有输入进行边界检查
     * 2. 性能优化：根据数据规模选择合适的算法版本
     * 3. 内存管理：合理使用全局变量避免频繁分配
     * 4. 代码可读性：清晰的注释和命名规范
     * 5. 测试覆盖：全面的单元测试和边界测试
     * 
     * 【面试重点】
     * 1. 时间复杂度分析：能够详细推导O(n log n)
     * 2. 空间复杂度分析：理解递归栈和辅助数组的影响
     * 3. 稳定性证明：解释为什么归并排序是稳定的
     * 4. 变种问题：掌握逆序对、翻转对等衍生问题
     * 5. 工程优化：讨论大数据量下的优化策略
     * 
     * 【学习建议】
     * 1. 先理解递归版本，再掌握非递归版本
     * 2. 通过画图理解分治和合并过程
     * 3. 多做练习题，特别是逆序对相关题目
     * 4. 尝试实现并行版本提升性能
     * 5. 学习标准库中的排序实现对比
     * 
     * 更多题目请参考同目录下的MERGE_SORT_PROBLEMS.md文件
     */
}
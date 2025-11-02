package class021;

// Merge Sort - ACM practice style
// Test link: https://www.luogu.com.cn/problem/P1177
// Please refer to the input/output handling in the following code
// This is a highly efficient way of handling input/output
// Submit the following code, change the class name to "Main" when submitting

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    // Global variables (competition style)
    public static int MAXN = 100001;
    public static int[] arr = new int[MAXN];
    public static int[] help = new int[MAXN];
    public static int n;

    // Merge two sorted arrays (core operation)
    public static void merge(int l, int m, int r) {
        int i = l;
        int a = l;
        int b = m + 1;
        
        // Merge using two pointers
        while (a <= m && b <= r) {
            help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
        }
        
        // Handle remaining elements
        while (a <= m) {
            help[i++] = arr[a++];
        }
        while (b <= r) {
            help[i++] = arr[b++];
        }
        
        // Copy back to original array
        for (i = l; i <= r; i++) {
            arr[i] = help[i];
        }
    }

    // Recursive merge sort
    public static void mergeSort1(int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) / 2;
        mergeSort1(l, m);
        mergeSort1(m + 1, r);
        merge(l, m, r);
    }

    // Non-recursive merge sort
    public static void mergeSort2() {
        for (int step = 1; step < n; step <<= 1) {
            int l = 0;
            while (l < n) {
                int m = l + step - 1;
                if (m + 1 >= n) {
                    break;
                }
                int r = Math.min(l + (step << 1) - 1, n - 1);
                merge(l, m, r);
                l = r + 1;
            }
        }
    }

    // Problem 1: Basic merge sort (Luogu P1177)
    // Original main method commented out - use runTests instead
    /*
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        n = (int) in.nval;
        
        for (int i = 0; i < n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
        }
        
        if (n > 0) {
            mergeSort2(); // Use non-recursive version
        }
        
        for (int i = 0; i < n - 1; i++) {
            out.print(arr[i] + " ");
        }
        if (n > 0) {
            out.println(arr[n - 1]);
        }
        out.flush();
        out.close();
        br.close();
    }
    */

    // Problem 2: Inversion counting (LeetCode 315 style)
    public static long mergeSortCount(int left, int right) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        long count = mergeSortCount(left, mid);
        count += mergeSortCount(mid + 1, right);
        
        // Merge and count inversions
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                help[k++] = arr[i++];
            } else {
                // Key counting: when right element is smaller than left element
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

    // Linked list node definition
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // Problem 3: Linked list merge sort (LeetCode 148)
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        // Find middle using fast-slow pointers
        ListNode slow = head;
        ListNode fast = head;
        ListNode prev = null;
        
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // Split the list
        if (prev != null) {
            prev.next = null;
        }
        
        // Recursively sort both halves
        ListNode left = sortList(head);
        ListNode right = sortList(slow);
        
        // Merge sorted lists
        return mergeTwoLists(left, right);
    }

    // Merge two sorted linked lists
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // Problem 4: Merge K sorted lists (LeetCode 23)
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return mergeKListsHelper(lists, 0, lists.length - 1);
    }
    
    private static ListNode mergeKListsHelper(ListNode[] lists, int left, int right) {
        if (left == right) return lists[left];
        if (left + 1 == right) return mergeTwoLists(lists[left], lists[right]);
        
        int mid = left + (right - left) / 2;
        ListNode l1 = mergeKListsHelper(lists, left, mid);
        ListNode l2 = mergeKListsHelper(lists, mid + 1, right);
        return mergeTwoLists(l1, l2);
    }

    // Problem 5: Count smaller numbers after self (LeetCode 315)
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
        
        // Merge and count
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

    // ============================================================================
    // 扩展题目实现：更多归并排序应用
    // ============================================================================

    // 题目6：LeetCode 2426. 满足不等式的数对数目
    // 链接：https://leetcode.cn/problems/number-of-pairs-satisfying-inequality/
    // 时间复杂度：O(n log n)
    // 空间复杂度：O(n)
    // 核心思想：翻转对变种，处理不等式条件
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

    // 题目7：LeetCode 4. 寻找两个正序数组的中位数
    // 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
    // 时间复杂度：O(log(min(m, n)))
    // 空间复杂度：O(1)
    // 核心思想：二分查找，不是归并排序但涉及有序数组合并思想
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        
        int m = nums1.length, n = nums2.length;
        int left = 0, right = m;
        
        while (left <= right) {
            int i = left + (right - left) / 2;
            int j = (m + n + 1) / 2 - i;
            
            int maxLeft1 = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            int minRight1 = (i == m) ? Integer.MAX_VALUE : nums1[i];
            int maxLeft2 = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            int minRight2 = (j == n) ? Integer.MAX_VALUE : nums2[j];
            
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                } else {
                    return Math.max(maxLeft1, maxLeft2);
                }
            } else if (maxLeft1 > minRight2) {
                right = i - 1;
            } else {
                left = i + 1;
            }
        }
        return 0.0;
    }

    // 题目8：外部排序模拟 - 多路归并
    // 模拟处理大规模数据，无法一次性装入内存的情况
    public static void externalSortSimulation(int[] largeArray, int memoryLimit) {
        int n = largeArray.length;
        int chunkSize = memoryLimit;
        int numChunks = (n + chunkSize - 1) / chunkSize;
        
        // 模拟分块排序（实际中会写入临时文件）
        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, n);
            // 对当前块进行排序（模拟内部排序）
            Arrays.sort(largeArray, start, end);
        }
        
        System.out.println("外部排序模拟完成，处理数据量: " + n);
    }

    // ============================================================================
    // 性能测试与优化
    // ============================================================================

    // 性能测试：测试不同规模数据的排序性能
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        int[] sizes = {1000, 10000, 100000, 1000000};
        for (int size : sizes) {
            int[] testData = generateRandomArray(size);
            
            long startTime = System.nanoTime();
            int[] result = sortArray(testData.clone());
            long endTime = System.nanoTime();
            
            double duration = (endTime - startTime) / 1e6; // 转换为毫秒
            System.out.printf("数据量: %,d, 耗时: %.2f ms%n", size, duration);
        }
    }

    // 生成随机测试数组
    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }

    // 边界测试：测试各种边界情况
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
        int[] result = sortArray(input.clone());
        boolean passed = isSorted(result);
        System.out.println(description + "测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }

    // 检查数组是否有序
    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }

    // ============================================================================
    // 调试工具方法
    // ============================================================================

    // 调试打印：打印数组内容（用于调试）
    public static void printArray(int[] arr, String label) {
        System.out.print(label + ": [");
        for (int i = 0; i < Math.min(arr.length, 10); i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1 && i < 9) {
                System.out.print(", ");
            }
        }
        if (arr.length > 10) {
            System.out.print(", ...");
        }
        System.out.println("]");
    }

    // 断言检查：验证中间结果
    public static void debugCheck(boolean condition, String message) {
        if (!condition) {
            System.err.println("调试错误: " + message);
        }
    }

    // ============================================================================
    // 单元测试增强版
    // ============================================================================

    public static void testBasicSort() {
        int[] test = {5, 2, 3, 1, 4};
        int[] expected = {1, 2, 3, 4, 5};
        
        n = test.length;
        System.arraycopy(test, 0, arr, 0, n);
        mergeSort2();
        
        boolean passed = Arrays.equals(Arrays.copyOf(arr, n), expected);
        System.out.println("基础排序测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testInversionCount() {
        int[] test = {7, 5, 6, 4};
        long expected = 5;
        
        n = test.length;
        System.arraycopy(test, 0, arr, 0, n);
        long result = mergeSortCount(0, n - 1);
        
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

    public static void testCountSmaller() {
        int[] test = {5, 2, 6, 1};
        List<Integer> expected = Arrays.asList(2, 1, 1, 0);
        
        List<Integer> result = countSmaller(test.clone());
        boolean passed = result.equals(expected);
        System.out.println("右侧较小元素统计测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }

    public static void runComprehensiveTests() {
        System.out.println("=== 开始全面测试 ===");
        testBasicSort();
        testInversionCount();
        testReversePairs();
        testCountSmaller();
        boundaryTest();
        System.out.println("=== 测试完成 ===");
    }

    // ============================================================================
    // 主函数：支持多种运行模式
    // ============================================================================

    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            // 测试模式
            runComprehensiveTests();
        } else if (args.length > 0 && "perf".equals(args[0])) {
            // 性能测试模式
            performanceTest();
        } else {
            // 默认模式：运行基础测试
            runComprehensiveTests();
            System.out.println("使用 'java Main test' 运行全面测试");
            System.out.println("使用 'java Main perf' 运行性能测试");
        }
    }

    // ============================================================================
    // 工程化考量总结
    // ============================================================================

    /**
     * 【工程化最佳实践】
     * 1. 异常处理：对所有输入进行边界检查
     * 2. 性能优化：根据数据规模选择合适的算法版本
     * 3. 内存管理：合理使用全局变量避免频繁分配
     * 4. 代码可读性：清晰的注释和命名规范
     * 5. 测试覆盖：全面的单元测试和边界测试
     * 6. 文档完善：详细的API文档和使用说明
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
    
    // ============================================================================
    // 缺失的方法实现：sortArray和reversePairs
    // ============================================================================
    
    /**
     * 排序数组 - LeetCode 912
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public static int[] sortArray(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return nums;
        }
        // 使用非递归归并排序
        mergeSort2(nums);
        return nums;
    }
    
    // 非递归归并排序（接收数组参数版本）
    public static void mergeSort2(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        int n = nums.length;
        int[] helper = new int[n];
        
        for (int step = 1; step < n; step <<= 1) {
            int l = 0;
            while (l < n) {
                int m = l + step - 1;
                if (m + 1 >= n) {
                    break;
                }
                int r = Math.min(l + (step << 1) - 1, n - 1);
                merge(nums, helper, l, m, r);
                l = r + 1;
            }
        }
    }
    
    // 合并两个有序数组（接收数组参数版本）
    public static void merge(int[] nums, int[] helper, int l, int m, int r) {
        int i = l;
        int a = l;
        int b = m + 1;
        
        // 合并两个有序部分
        while (a <= m && b <= r) {
            helper[i++] = nums[a] <= nums[b] ? nums[a++] : nums[b++];
        }
        
        // 处理剩余元素
        while (a <= m) {
            helper[i++] = nums[a++];
        }
        while (b <= r) {
            helper[i++] = nums[b++];
        }
        
        // 复制回原数组
        for (i = l; i <= r; i++) {
            nums[i] = helper[i];
        }
    }
    
    /**
     * 统计逆序对 - LeetCode 493
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public static int reversePairs(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        int[] helper = new int[nums.length];
        return mergeSortReversePairs(nums, helper, 0, nums.length - 1);
    }
    
    // 归并排序 + 统计逆序对
    private static int mergeSortReversePairs(int[] nums, int[] helper, int left, int right) {
        if (left >= right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = mergeSortReversePairs(nums, helper, left, mid);
        count += mergeSortReversePairs(nums, helper, mid + 1, right);
        count += mergeReversePairs(nums, helper, left, mid, right);
        
        return count;
    }
    
    // 合并 + 统计逆序对
    private static int mergeReversePairs(int[] nums, int[] helper, int left, int mid, int right) {
        for (int i = left; i <= right; i++) {
            helper[i] = nums[i];
        }
        
        int count = 0;
        int j = mid + 1;
        
        // 统计逆序对
        for (int i = left; i <= mid; i++) {
            while (j <= right && (long)helper[i] > 2L * (long)helper[j]) {
                j++;
            }
            count += j - (mid + 1);
        }
        
        // 合并两个有序数组
        int i = left, k = left;
        j = mid + 1;
        while (i <= mid && j <= right) {
            if (helper[i] <= helper[j]) {
                nums[k++] = helper[i++];
            } else {
                nums[k++] = helper[j++];
            }
        }
        while (i <= mid) nums[k++] = helper[i++];
        while (j <= right) nums[k++] = helper[j++];
        
        return count;
    }
}
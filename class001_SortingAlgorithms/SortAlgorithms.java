import java.util.Arrays;
import java.util.Random;

/**
 * 排序算法完整实现 - Java版本
 * 包含归并排序、快速排序、堆排序的完整实现和详细注释
 * 
 * 时间复杂度分析：
 * - 归并排序: O(n log n) 平均和最坏情况
 * - 快速排序: O(n log n) 平均, O(n²) 最坏
 * - 堆排序: O(n log n) 平均和最坏情况
 * 
 * 空间复杂度分析：
 * - 归并排序: O(n) 需要辅助数组
 * - 快速排序: O(log n) 递归栈空间
 * - 堆排序: O(1) 原地排序
 * 
 * 稳定性分析：
 * - 归并排序: 稳定
 * - 快速排序: 不稳定
 * - 堆排序: 不稳定
 * 
 * 题目相关:
 * - 912. 排序数组: https://leetcode.cn/problems/sort-an-array/
 * - 215. 数组中的第K个最大元素: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * - 75. 颜色分类: https://leetcode.cn/problems/sort-colors/
 * - 56. 合并区间: https://leetcode.cn/problems/merge-intervals/
 * - 148. 排序链表: https://leetcode.cn/problems/sort-list/
 * - 剑指Offer 51. 数组中的逆序对: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 * - ALDS1_2_A: Bubble Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_A
 * - ALDS1_2_B: Selection Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_B
 * - ALDS1_2_C: Stable Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C
 * - ALDS1_2_D: Shell Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_D
 * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
 * - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
 * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
 * - ALDS1_6_C: Quick Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_C
 * - ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
 * - ALDS1_9_A: Complete Binary Tree: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_A
 * - ALDS1_9_B: Maximum Heap: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_B
 * - ALDS1_9_C: Priority Queue: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_C
 * 
 * 工程化考量：
 * - 异常处理：对空数组、单元素数组进行特殊处理
 * - 边界条件：处理各种边界情况，如已排序、逆序、全相同数组
 * - 性能优化：
 *   - 快速排序的小数组优化（长度小于16时使用插入排序）
 *   - 三数取中法选择基准值避免最坏情况
 *   - 归并排序使用全局辅助数组避免频繁创建销毁
 * - 稳定性：归并排序保证稳定性，快速排序和堆排序不稳定
 * - 可读性：清晰的函数命名和详细注释
 * 
 * 算法选择建议：
 * - 数据量小（n < 50）：插入排序
 * - 需要稳定排序：归并排序
 * - 一般情况：快速排序（带优化）
 * - 内存受限：堆排序
 * - 最坏情况要求：堆排序
 */
public class SortAlgorithms {
    
    // 归并排序辅助数组，避免频繁创建销毁
    private static int[] mergeHelper;
    
    /**
     * 归并排序主函数
     * 时间复杂度: O(n log n) - 在所有情况下都是这个复杂度，包括最好、平均和最坏情况
     * 空间复杂度: O(n) - 需要一个与原数组相同大小的辅助数组
     * 稳定性: 稳定 - 相等元素的相对位置在排序后不会改变
     * 
     * 算法原理：
     * 1. 分治法：将数组不断二分直到只有一个元素
     * 2. 合并：将两个有序数组合并成一个有序数组
     * 3. 递归处理：自底向上构建有序数组
     * 
     * 适用场景：
     * - 需要稳定排序
     * - 链表排序
     * - 外部排序（数据量大无法全部加载到内存）
     * 
     * 相关题目：
     * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
     * - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
     * 
     * 工程化考量：
     * - 对于小数组（长度小于16），可考虑使用插入排序优化
     * - 可以使用迭代版本避免递归栈溢出
     * - 可以复用辅助数组避免频繁创建销毁
     * 
     * @param arr 待排序数组
     */
    public static void mergeSort(int[] arr) {
        // 边界条件检查：空数组或单元素数组无需排序
        if (arr == null || arr.length <= 1) {
            return;
        }
        // 初始化辅助数组，避免在递归中频繁创建销毁
        mergeHelper = new int[arr.length];
        // 调用递归实现
        mergeSort(arr, 0, arr.length - 1);
    }
    
    /**
     * 归并排序递归实现
     * 核心思想：分治法，将数组分成两半分别排序，然后合并
     * 
     * @param arr 待排序数组
     * @param left 左边界
     * @param right 右边界
     */
    private static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }
        
        int mid = left + (right - left) / 2;
        
        // 递归排序左半部分
        mergeSort(arr, left, mid);
        // 递归排序右半部分
        mergeSort(arr, mid + 1, right);
        // 合并两个有序数组
        merge(arr, left, mid, right);
    }
    
    /**
     * 合并两个有序数组
     * 关键步骤：双指针合并，保证稳定性
     * 
     * @param arr 原数组
     * @param left 左边界
     * @param mid 中间位置
     * @param right 右边界
     */
    private static void merge(int[] arr, int left, int mid, int right) {
        // 复制数据到辅助数组
        for (int i = left; i <= right; i++) {
            mergeHelper[i] = arr[i];
        }
        
        int i = left;       // 左半部分指针
        int j = mid + 1;    // 右半部分指针
        int k = left;       // 原数组指针
        
        // 合并两个有序数组
        while (i <= mid && j <= right) {
            // 相等时取左边的元素，保证稳定性
            if (mergeHelper[i] <= mergeHelper[j]) {
                arr[k++] = mergeHelper[i++];
            } else {
                arr[k++] = mergeHelper[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            arr[k++] = mergeHelper[i++];
        }
        while (j <= right) {
            arr[k++] = mergeHelper[j++];
        }
    }
    
    /**
     * 快速排序主函数
     * 时间复杂度: 平均O(n log n)，最坏O(n²)
     * 空间复杂度: O(log n) 平均情况下的递归栈空间，最坏O(n)
     * 稳定性: 不稳定 - 元素的相对位置可能改变
     * 
     * 算法原理：
     * 1. 分治法：选取基准值，将数组分成两部分
     * 2. 递归处理：对左右两部分分别进行快速排序
     * 3. 合并：由于是原地排序，不需要合并操作
     * 
     * 适用场景：
     * - 一般情况下的排序
     * - 内存受限时（不需要稳定排序）
     * - 数据量较大时（平均性能较好）
     * 
     * 相关题目：
     * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
     * - ALDS1_6_C: Quick Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_C
     * 
     * 工程化考量：
     * - 小数组优化：当数组长度较小时使用插入排序
     * - 三数取中法选择基准值，避免最坏情况
     * - 随机化基准选择也可避免最坏情况
     * - 可以使用迭代版本避免递归栈溢出
     * 
     * @param arr 待排序数组
     */
    public static void quickSort(int[] arr) {
        // 边界条件检查：空数组或单元素数组无需排序
        if (arr == null || arr.length <= 1) {
            return;
        }
        // 调用递归实现
        quickSort(arr, 0, arr.length - 1);
    }
    
    /**
     * 快速排序递归实现
     * 核心思想：分治法，选取基准值，将数组分成两部分
     * 
     * @param arr 待排序数组
     * @param left 左边界
     * @param right 右边界
     */
    private static void quickSort(int[] arr, int left, int right) {
        // 递归终止条件
        if (left >= right) {
            return;
        }
        
        // 小数组优化：当数组长度较小时使用插入排序
        if (right - left + 1 < 16) {
            insertionSort(arr, left, right);
            return;
        }
        
        // 三数取中法选择基准值，避免最坏情况
        int pivot = medianOfThree(arr, left, right);
        
        // 分区操作
        int[] partition = partition(arr, left, right, pivot);
        int equalLeft = partition[0];
        int equalRight = partition[1];
        
        // 递归排序左右部分
        quickSort(arr, left, equalLeft - 1);
        quickSort(arr, equalRight + 1, right);
    }
    
    /**
     * 三数取中法选择基准值
     * 优化策略：避免快速排序的最坏情况
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @return 基准值
     */
    private static int medianOfThree(int[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        
        // 对左中右三个数排序
        if (arr[left] > arr[mid]) {
            swap(arr, left, mid);
        }
        if (arr[left] > arr[right]) {
            swap(arr, left, right);
        }
        if (arr[mid] > arr[right]) {
            swap(arr, mid, right);
        }
        
        // 将中间值放到right-1位置，作为基准值
        swap(arr, mid, right - 1);
        return arr[right - 1];
    }
    
    /**
     * 快速排序分区操作
     * 荷兰国旗问题变种：将数组分成小于、等于、大于基准值的三部分
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @param pivot 基准值
     * @return 等于区域的左右边界
     */
    private static int[] partition(int[] arr, int left, int right, int pivot) {
        int less = left - 1;        // 小于区域右边界
        int more = right;           // 大于区域左边界
        int i = left;               // 当前指针
        
        while (i < more) {
            if (arr[i] < pivot) {
                swap(arr, ++less, i++);
            } else if (arr[i] > pivot) {
                swap(arr, --more, i);
            } else {
                i++;
            }
        }
        
        // 将基准值放回等于区域
        swap(arr, more, right - 1);
        
        return new int[]{less + 1, more};
    }
    
    /**
     * 插入排序（用于小数组优化）
     * 时间复杂度: O(n²) 但常数项很小
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     */
    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            
            // 将大于key的元素向后移动
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
    /**
     * 堆排序主函数
     * 时间复杂度: O(n log n) - 在所有情况下都是这个复杂度
     * 空间复杂度: O(1) - 原地排序算法
     * 稳定性: 不稳定 - 元素的相对位置可能改变
     * 
     * 算法原理：
     * 1. 构建最大堆：从最后一个非叶子节点开始
     * 2. 逐个提取堆顶元素：将堆顶元素（最大值）与当前末尾元素交换
     * 3. 重新堆化：对剩余元素重新堆化
     * 4. 重复步骤2和3直到堆为空
     * 
     * 适用场景：
     * - 内存受限时（不需要稳定排序）
     * - 最坏情况要求
     * - 数据量较大时（平均性能较好）
     * 
     * 相关题目：
     * - ALDS1_9_A: Complete Binary Tree: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_A
     * - ALDS1_9_B: Maximum Heap: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_B
     * - ALDS1_9_C: Priority Queue: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_C
     * 
     * 工程化考量：
     * - 原地排序，内存使用效率高
     * - 最坏情况时间复杂度有保证
     * - 常数因子相对较大，实际性能可能不如快速排序
     * 
     * @param arr 待排序数组
     */
    public static void heapSort(int[] arr) {
        // 边界条件检查：空数组或单元素数组无需排序
        if (arr == null || arr.length <= 1) {
            return;
        }
    
        int n = arr.length;
        
        // 构建最大堆：从最后一个非叶子节点开始
        // 最后一个非叶子节点的索引是 n/2 - 1
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        
        // 逐个提取堆顶元素
        for (int i = n - 1; i > 0; i--) {
            // 将堆顶元素（最大值）与当前末尾元素交换
            swap(arr, 0, i);
            // 对剩余元素重新堆化，堆大小减1
            heapify(arr, i, 0);
        }
    }
    
    /**
     * 堆化操作：维护最大堆性质
     * 核心思想：下沉操作，确保父节点大于等于子节点
     * 
     * @param arr 数组
     * @param n 堆大小
     * @param i 当前节点索引
     */
    private static void heapify(int[] arr, int n, int i) {
        int largest = i;        // 假设当前节点最大
        int left = 2 * i + 1;   // 左子节点
        int right = 2 * i + 2;  // 右子节点
        
        // 比较左子节点
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        
        // 比较右子节点
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }
        
        // 如果最大值不是当前节点，需要交换并继续堆化
        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }
    
    /**
     * 交换数组元素
     * 基础操作，但要注意边界检查
     * 
     * @param arr 数组
     * @param i 索引1
     * @param j 索引2
     */
    private static void swap(int[] arr, int i, int j) {
        if (i == j) return; // 相同索引不需要交换
        
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * 测试函数：验证排序算法的正确性
     * 包含边界测试、性能测试、稳定性测试
     */
    public static void testSortAlgorithms() {
        System.out.println("=== 排序算法测试开始 ===");
        
        // 测试用例设计
        int[][] testCases = {
            {},                            // 空数组
            {1},                           // 单元素
            {1, 2, 3},                     // 已排序
            {3, 2, 1},                     // 逆序
            {1, 1, 1},                     // 全相同
            {5, 2, 8, 1, 9},              // 普通情况
            {3, 1, 4, 1, 5, 9, 2, 6}      // 重复元素
        };
        
        String[] algorithms = {"归并排序", "快速排序", "堆排序"};
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n测试用例 " + (i + 1) + ": " + Arrays.toString(testCases[i]));
            
            for (int j = 0; j < algorithms.length; j++) {
                int[] arr = testCases[i].clone();
                int[] expected = testCases[i].clone();
                Arrays.sort(expected); // 使用系统排序作为基准
                
                switch (j) {
                    case 0:
                        mergeSort(arr);
                        break;
                    case 1:
                        quickSort(arr);
                        break;
                    case 2:
                        heapSort(arr);
                        break;
                }
                
                boolean correct = Arrays.equals(arr, expected);
                System.out.printf("%s: %s - %s%n", 
                    algorithms[j], 
                    Arrays.toString(arr),
                    correct ? "✓" : "✗"
                );
                
                if (!correct) {
                    System.out.println("预期: " + Arrays.toString(expected));
                }
            }
        }
        
        // 性能测试
        performanceTest();
        
        System.out.println("=== 排序算法测试结束 ===");
    }
    
    /**
     * 性能测试：比较不同排序算法在大数据量下的表现
     */
    private static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        int size = 10000;
        int[] data = generateRandomArray(size);
        
        String[] algorithms = {"归并排序", "快速排序", "堆排序"};
        Runnable[] sorts = {
            () -> mergeSort(data.clone()),
            () -> quickSort(data.clone()),
            () -> heapSort(data.clone())
        };
        
        for (int i = 0; i < algorithms.length; i++) {
            int[] testData = data.clone();
            long startTime = System.nanoTime();
            
            switch (i) {
                case 0: mergeSort(testData); break;
                case 1: quickSort(testData); break;
                case 2: heapSort(testData); break;
            }
            
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1e6; // 转换为毫秒
            
            System.out.printf("%s: %.2f ms%n", algorithms[i], duration);
            
            // 验证排序正确性
            boolean correct = isSorted(testData);
            System.out.printf("  排序正确性: %s%n", correct ? "✓" : "✗");
        }
    }
    
    /**
     * 生成随机测试数组
     * 
     * @param size 数组大小
     * @return 随机数组
     */
    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }
    
    /**
     * 检查数组是否已排序
     * 
     * @param arr 数组
     * @return 是否已排序
     */
    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 主函数：演示排序算法的使用
     */
    public static void main(String[] args) {
        // 基础功能演示
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("原始数组: " + Arrays.toString(arr));
        
        // 测试不同排序算法
        int[] arr1 = arr.clone();
        mergeSort(arr1);
        System.out.println("归并排序: " + Arrays.toString(arr1));
        
        int[] arr2 = arr.clone();
        quickSort(arr2);
        System.out.println("快速排序: " + Arrays.toString(arr2));
        
        int[] arr3 = arr.clone();
        heapSort(arr3);
        System.out.println("堆排序:   " + Arrays.toString(arr3));
        
        // 运行完整测试套件
        testSortAlgorithms();
        
        // 运行扩展题目测试
        System.out.println("\n=== 扩展题目测试 ===");
        testExtendedProblems();
    }
    
    // ========================================
    // 扩展题目实现部分
    // ========================================
    
    /**
     * 题目1: 912. 排序数组
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/sort-an-array/
     * 
     * 题目描述:
     * 给定一个整数数组 nums，将该数组升序排列。
     * 
     * 示例:
     * 输入: nums = [5,2,3,1]
     * 输出: [1,2,3,5]
     * 
     * 约束条件:
     * 1 <= nums.length <= 5 * 10^4
     * -5 * 10^4 <= nums[i] <= 5 * 10^4
     * 
     * 思路:
     * 可以使用快速排序、归并排序或堆排序
     * 本题要求时间复杂度O(nlogn)，三种算法都满足
     * 
     * 时间复杂度: O(n log n) - 所有情况
     * 空间复杂度: O(n) - 归并排序需要辅助数组; O(log n) - 快速排序递归栈; O(1) - 堆排序
     * 
     * 是否最优解: 是。基于比较的排序算法下界是O(n log n)
     * 
     * 相关题目：
     * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
     * 
     * 工程化考量：
     * - 根据数据规模选择合适的排序算法
     * - 可以结合多种算法的优点进行优化
     */
    public static int[] sortArray(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length <= 1) {
            return nums;
        }
        
        // 根据数据规模选择算法
        if (nums.length < 50) {
            // 小数组使用插入排序
            insertionSort(nums, 0, nums.length - 1);
        } else {
            // 大数组使用快速排序(带优化)
            quickSort(nums);
        }
        
        return nums;
    }
    
    /**
     * 题目2: 215. 数组中的第K个最大元素
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
     * 
     * 题目描述:
     * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
     * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     * 
     * 示例:
     * 输入: nums = [3,2,1,5,6,4], k = 2
     * 输出: 5
     * 
     * 思路对比:
     * 方法1: 完全排序后返回第k个 - O(n log n)
     * 方法2: 使用最小堆维护k个最大元素 - O(n log k)
     * 方法3: 快速选择算法 - O(n) 平均，O(n²) 最坏
     * 
     * 时间复杂度: O(n) - 平均情况(快速选择)
     * 空间复杂度: O(1) - 原地操作
     * 
     * 是否最优解: 是。快速选择是找第K大元素的最优算法(平均O(n))
     * 
     * 相关题目：
     * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
     */
    public static int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        // 使用快速选择算法
        // 第k大 = 第(n-k)小(0-based)
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }
    
    /**
     * 快速选择算法
     * 核心思想: 类似快速排序的分区操作，但只递归处理包含目标的一侧
     * 
     * @param nums 数组
     * @param left 左边界
     * @param right 右边界
     * @param k 要找的第k小元素的索引(0-based)
     * @return 第k小的元素值
     */
    private static int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) {
            return nums[left];
        }
        
        // 随机选择pivot，避免最坏情况
        Random rand = new Random();
        int pivotIndex = left + rand.nextInt(right - left + 1);
        swap(nums, pivotIndex, right);
        
        // 分区操作
        int pivot = nums[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (nums[j] < pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        swap(nums, i, right);
        
        // 根据pivot位置决定下一步
        if (i == k) {
            return nums[i];
        } else if (i < k) {
            return quickSelect(nums, i + 1, right, k);
        } else {
            return quickSelect(nums, left, i - 1, k);
        }
    }
    
    /**
     * 题目3: 75. 颜色分类 (荷兰国旗问题)
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/sort-colors/
     * 
     * 题目描述:
     * 给定一个包含红色、白色和蓝色，一共 n 个元素的数组，
     * 原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
     * 此题中，我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
     * 
     * 示例:
     * 输入: nums = [2,0,2,1,1,0]
     * 输出: [0,0,1,1,2,2]
     * 
     * 进阶:
     * - 你可以不使用代码库中的排序函数来解决这道题吗？
     * - 你能想出一个仅使用常数空间的一趟扫描算法吗？
     * 
     * 思路:
     * 使用三指针法(荷兰国旗问题的经典解法):
     * - p0: 指向下一个0应该放置的位置
     * - curr: 当前遍历的位置
     * - p2: 指向下一个2应该放置的位置
     * 
     * 时间复杂度: O(n) - 一趟扫描
     * 空间复杂度: O(1) - 原地操作
     * 
     * 是否最优解: 是。一趟扫描，原地排序是该问题的最优解法
     * 
     * 相关题目：
     * - ALDS1_2_C: Stable Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C
     */
    public static void sortColors(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        
        int p0 = 0;              // 下一个0的位置
        int curr = 0;            // 当前指针
        int p2 = nums.length - 1; // 下一个2的位置
        
        while (curr <= p2) {
            if (nums[curr] == 0) {
                // 遇到0，与p0位置交换，p0和curr都前进
                swap(nums, curr, p0);
                p0++;
                curr++;
            } else if (nums[curr] == 2) {
                // 遇到2，与p2位置交换，p2后退，curr不动(因为交换来的元素还需要判断)
                swap(nums, curr, p2);
                p2--;
            } else {
                // 遇到1，curr前进
                curr++;
            }
        }
    }
    
    /**
     * 题目4: 56. 合并区间
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/merge-intervals/
     * 
     * 题目描述:
     * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]。
     * 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
     * 
     * 示例:
     * 输入: intervals = [[1,3],[2,6],[8,10],[15,18]]
     * 输出: [[1,6],[8,10],[15,18]]
     * 解释: 区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
     * 
     * 思路:
     * 1. 按区间起始位置排序
     * 2. 遍历排序后的区间，判断当前区间与上一个合并区间是否重叠
     * 3. 重叠则合并，不重叠则加入结果
     * 
     * 时间复杂度: O(n log n) - 排序时间
     * 空间复杂度: O(log n) - 排序的递归栈空间
     * 
     * 是否最优解: 是。排序是必需的，整体复杂度已达下界
     * 
     * 相关题目：
     * - ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        // 按起始位置排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        java.util.List<int[]> result = new java.util.ArrayList<>();
        int[] currentInterval = intervals[0];
        result.add(currentInterval);
        
        for (int i = 1; i < intervals.length; i++) {
            int[] interval = intervals[i];
            
            // 判断是否重叠: 当前区间的起始位置 <= 上一个合并区间的结束位置
            if (interval[0] <= currentInterval[1]) {
                // 重叠，合并: 更新结束位置为两者的最大值
                currentInterval[1] = Math.max(currentInterval[1], interval[1]);
            } else {
                // 不重叠，添加新区间
                currentInterval = interval;
                result.add(currentInterval);
            }
        }
        
        return result.toArray(new int[result.size()][]);
    }
    
    /**
     * 题目5: 148. 排序链表
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/sort-list/
     * 
     * 题目描述:
     * 给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。
     * 
     * 进阶: 你可以在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？
     * 
     * 示例:
     * 输入: head = [4,2,1,3]
     * 输出: [1,2,3,4]
     * 
     * 思路:
     * 归并排序特别适合链表:
     * 1. 自顶向下: 递归找中点，分割链表，归并
     * 2. 自底向上: 迭代合并，空间O(1)
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(1) - 自底向上; O(log n) - 自顶向下的递归栈
     * 
     * 是否最优解: 是。满足题目的时间和空间要求
     * 
     * 相关题目：
     * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        // 使用快慢指针找到中点
        ListNode slow = head, fast = head, prev = null;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 断开链表
        prev.next = null;
        
        // 递归排序两半
        ListNode left = sortList(head);
        ListNode right = sortList(slow);
        
        // 合并两个有序链表
        return mergeTwoLists(left, right);
    }
    
    /**
     * 合并两个有序链表
     */
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        
        curr.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
    
    /**
     * 题目6: 剑指Offer 51. 数组中的逆序对
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
     * 
     * 题目描述:
     * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
     * 输入一个数组，求出这个数组中的逆序对的总数。
     * 
     * 示例:
     * 输入: [7,5,6,4]
     * 输出: 5
     * 解释: (7,5), (7,6), (7,4), (5,4), (6,4)
     * 
     * 思路:
     * 利用归并排序的过程统计逆序对:
     * 在合并两个有序数组时，如果左边元素大于右边元素，
     * 则左边该元素及之后的所有元素都与右边元素构成逆序对
     * 
     * 时间复杂度: O(n log n) - 归并排序时间
     * 空间复杂度: O(n) - 辅助数组
     * 
     * 是否最优解: 是。暴力O(n²)，归并排序优化到O(n log n)是最优
     * 
     * 相关题目：
     * - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
     */
    public static int reversePairs(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        int[] temp = new int[nums.length];
        return mergeCountPairs(nums, 0, nums.length - 1, temp);
    }
    
    private static int mergeCountPairs(int[] nums, int left, int right, int[] temp) {
        if (left >= right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = 0;
        
        // 递归统计左半部分和右半部分的逆序对
        count += mergeCountPairs(nums, left, mid, temp);
        count += mergeCountPairs(nums, mid + 1, right, temp);
        
        // 合并时统计跨越左右的逆序对
        count += mergeAndCount(nums, left, mid, right, temp);
        
        return count;
    }
    
    private static int mergeAndCount(int[] nums, int left, int mid, int right, int[] temp) {
        // 复制到临时数组
        for (int i = left; i <= right; i++) {
            temp[i] = nums[i];
        }
        
        int i = left, j = mid + 1, k = left;
        int count = 0;
        
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                nums[k++] = temp[i++];
            } else {
                // temp[i] > temp[j]，形成逆序对
                // 从i到mid的所有元素都大于temp[j]
                count += (mid - i + 1);
                nums[k++] = temp[j++];
            }
        }
        
        while (i <= mid) {
            nums[k++] = temp[i++];
        }
        while (j <= right) {
            nums[k++] = temp[j++];
        }
        
        return count;
    }
    
    /**
     * 测试扩展题目
     */
    private static void testExtendedProblems() {
        // 测试题目1: 排序数组
        System.out.println("\n题目1: 排序数组");
        int[] arr1 = {5, 2, 3, 1};
        System.out.println("输入: " + Arrays.toString(arr1));
        sortArray(arr1);
        System.out.println("输出: " + Arrays.toString(arr1));
        
        // 测试题目2: 第K个最大元素
        System.out.println("\n题目2: 第K个最大元素");
        int[] arr2 = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println("输入: " + Arrays.toString(arr2) + ", k=" + k);
        System.out.println("输出: " + findKthLargest(arr2, k));
        
        // 测试题目3: 颜色分类
        System.out.println("\n题目3: 颜色分类");
        int[] arr3 = {2, 0, 2, 1, 1, 0};
        System.out.println("输入: " + Arrays.toString(arr3));
        sortColors(arr3);
        System.out.println("输出: " + Arrays.toString(arr3));
        
        // 测试题目4: 合并区间
        System.out.println("\n题目4: 合并区间");
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        System.out.println("输入: " + Arrays.deepToString(intervals));
        int[][] merged = merge(intervals);
        System.out.println("输出: " + Arrays.deepToString(merged));
        
        // 测试题目6: 逆序对
        System.out.println("\n题目6: 数组中的逆序对");
        int[] arr6 = {7, 5, 6, 4};
        System.out.println("输入: " + Arrays.toString(arr6));
        System.out.println("逆序对数量: " + reversePairs(arr6.clone()));
    }
}
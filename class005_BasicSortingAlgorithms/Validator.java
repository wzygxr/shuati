import java.util.Arrays;

/**
 * 选择排序、冒泡排序、插入排序的验证与扩展练习
 * 
 * 选择排序(Selection Sort):
 * - 工作原理：每次从未排序的部分中找到最小元素，放到已排序部分的末尾
 * - 时间复杂度：O(n²) - 最好、平均、最坏情况都相同
 * - 空间复杂度：O(1) - 原地排序
 * - 稳定性：不稳定
 * - 适用场景：数据量小且对稳定性无要求
 *
 * 冒泡排序(Bubble Sort):
 * - 工作原理：相邻元素两两比较，如果顺序错误就交换，每轮将最大元素"冒泡"到末尾
 * - 时间复杂度：O(n²) - 最坏和平均情况，O(n) - 最好情况(已排序)
 * - 空间复杂度：O(1) - 原地排序
 * - 稳定性：稳定
 * - 适用场景：数据量小且要求稳定性
 *
 * 插入排序(Insertion Sort):
 * - 工作原理：将未排序元素插入到已排序序列的适当位置
 * - 时间复杂度：O(n²) - 最坏情况，O(n) - 最好情况(已排序)
 * - 空间复杂度：O(1) - 原地排序
 * - 稳定性：稳定
 * - 适用场景：小规模数据或基本有序的数据
 */
public class Validator {

    public static void main(String[] args) {
        System.out.println("=== 基础排序算法验证器启动 ===");
        System.out.println("作者: Algorithm Journey");
        System.out.println("版本: 1.1");
        System.out.println("日期: 2025-10-18");
        System.out.println();
        
        // 第一阶段：基础算法正确性验证
        System.out.println("第一阶段：基础排序算法正确性验证");
        validateBasicAlgorithms();
        
        // 第二阶段：详细测试用例演示
        System.out.println("\n第二阶段：详细测试用例演示");
        testSortAlgorithms();
        
        // 第三阶段：性能测试分析
        System.out.println("\n第三阶段：性能测试分析");
        performanceTest();
        
        // 第四阶段：经典题目解法测试
        System.out.println("\n第四阶段：经典题目解法测试");
        testAdditionalProblems();
        
        // 第五阶段：工程化组件测试
        System.out.println("\n第五阶段：工程化组件测试");
        testEngineeringComponents();
        
        System.out.println("\n=== 所有测试完成 ===");
    }
    
    /**
     * 基础排序算法正确性验证
     * 通过5000次随机测试验证三种排序算法的正确性
     */
    public static void validateBasicAlgorithms() {
        int N = 200;        // 随机数组最大长度
        int V = 1000;       // 随机数组值范围
        int testTimes = 5000; // 测试次数
        
        System.out.println("开始 " + testTimes + " 次随机测试...");
        int errorCount = 0;
        
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N);
            int[] arr = randomArray(n, V);
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            
            selectionSort(arr1);
            bubbleSort(arr2);
            insertionSort(arr3);
            
            if (!sameArray(arr1, arr2) || !sameArray(arr1, arr3)) {
                errorCount++;
            }
            
            // 显示进度
            if ((i + 1) % 1000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试");
            }
        }
        
        System.out.println("测试完成，错误次数: " + errorCount + "/" + testTimes);
        if (errorCount == 0) {
            System.out.println("✓ 所有基础排序算法测试通过");
        } else {
            System.out.println("✗ 发现 " + errorCount + " 个错误");
        }
    }
    
    /**
     * 测试函数：验证三种排序算法的正确性
     */
    public static void testSortAlgorithms() {
        System.out.println("\n=== 选择排序、冒泡排序、插入排序测试 ===");

        int[][] testCases = {
            {},                            // 空数组
            {1},                           // 单元素
            {1, 2, 3},                     // 已排序
            {3, 2, 1},                     // 逆序
            {1, 1, 1},                     // 全相同
            {5, 2, 8, 1, 9},              // 普通情况
            {3, 1, 4, 1, 5, 9, 2, 6}      // 重复元素
        };

        String[] algorithms = {"选择排序", "冒泡排序", "插入排序"};

        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n测试用例 " + (i + 1) + ": " + Arrays.toString(testCases[i]));

            for (int j = 0; j < algorithms.length; j++) {
                int[] arr = testCases[i].clone();
                int[] expected = testCases[i].clone();
                Arrays.sort(expected); // 使用系统排序作为基准

                switch (j) {
                    case 0:
                        selectionSort(arr);
                        break;
                    case 1:
                        bubbleSort(arr);
                        break;
                    case 2:
                        insertionSort(arr);
                        break;
                }

                boolean correct = Arrays.equals(arr, expected);
                System.out.printf("%s: %s - %s%n",
                    algorithms[j],
                    Arrays.toString(arr),
                    correct ? "✓" : "✗"
                );
            }
        }
    }
    
    /**
     * 性能测试：比较三种排序算法在不同数据规模下的表现
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");

        int[] sizes = {100, 500, 1000};
        String[] algorithms = {"选择排序", "冒泡排序", "插入排序"};

        for (int size : sizes) {
            System.out.println("\n数组大小: " + size);
            int[] data = generateRandomArray(size);

            for (int j = 0; j < algorithms.length; j++) {
                int[] testData = data.clone();
                long startTime = System.nanoTime();

                switch (j) {
                    case 0:
                        selectionSort(testData);
                        break;
                    case 1:
                        bubbleSort(testData);
                        break;
                    case 2:
                        insertionSort(testData);
                        break;
                }

                long endTime = System.nanoTime();
                double duration = (endTime - startTime) / 1e6; // 转换为毫秒

                System.out.printf("%s: %.2f ms%n", algorithms[j], duration);
            }
        }
    }
    
    /**
     * 测试新增的题目解法
     */
    public static void testAdditionalProblems() {
        System.out.println("=== 经典题目解法测试 ===");
        
        // 测试LeetCode 75. 颜色分类
        System.out.println("\n1. LeetCode 75. 颜色分类");
        int[] colors = {2, 0, 2, 1, 1, 0};
        System.out.println("输入: " + Arrays.toString(colors));
        sortColors(colors);
        System.out.println("输出: " + Arrays.toString(colors));
        
        // 测试LeetCode 283. 移动零
        System.out.println("\n2. LeetCode 283. 移动零");
        int[] nums = {0, 1, 0, 3, 12};
        System.out.println("输入: " + Arrays.toString(nums));
        moveZeroes(nums);
        System.out.println("输出: " + Arrays.toString(nums));
        
        // 测试LeetCode 215. 数组中的第K个最大元素
        System.out.println("\n3. LeetCode 215. 数组中的第K个最大元素");
        int[] arr = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println("输入: " + Arrays.toString(arr) + ", k = " + k);
        int result = findKthLargest(arr, k);
        System.out.println("输出: " + result);
        
        // 测试牛客网 - 最小的K个数
        System.out.println("\n4. 牛客网 - 最小的K个数");
        int[] numbers = {4, 5, 1, 6, 2, 7, 3, 8};
        int k2 = 4;
        System.out.println("输入: " + Arrays.toString(numbers) + ", k = " + k2);
        int[] smallestK = getLeastNumbers(numbers, k2);
        System.out.println("输出: " + Arrays.toString(smallestK));
    }
    
    /**
     * 工程化组件测试
     */
    public static void testEngineeringComponents() {
        System.out.println("=== 工程化组件测试 ===");
        
        // 测试异常处理
        System.out.println("\n1. 异常处理测试");
        try {
            selectionSort(null);
            System.out.println("空数组处理: ✓");
        } catch (Exception e) {
            System.out.println("空数组处理: ✗ - " + e.getMessage());
        }
        
        // 测试边界条件
        System.out.println("\n2. 边界条件测试");
        int[] empty = {};
        int[] single = {1};
        selectionSort(empty);
        selectionSort(single);
        System.out.println("空数组: " + Arrays.toString(empty));
        System.out.println("单元素: " + Arrays.toString(single));
        
        // 测试性能分析
        System.out.println("\n3. 性能分析测试");
        analyzePerformance();
    }
    
    /**
     * 性能分析测试
     */
    private static void analyzePerformance() {
        System.out.println("性能分析:");
        
        int[] sizes = {100, 500, 1000};
        for (int size : sizes) {
            System.out.println("\n数组大小: " + size);
            
            int[] data = generateRandomArray(size);
            int[] dataCopy = data.clone();
            
            long start = System.nanoTime();
            selectionSort(dataCopy);
            long end = System.nanoTime();
            double time = (end - start) / 1e6;
            
            System.out.printf("选择排序耗时: %.3f ms%n", time);
            System.out.println("排序正确性: " + isSorted(dataCopy));
        }
    }
    
    /**
     * 检查数组是否已排序
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
     * 生成随机测试数组
     * @param size 数组大小
     * @return 随机数组
     */
    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * size * 10);
        }
        return arr;
    }

    /**
     * 得到一个随机数组，长度是n，值在1~v之间
     * @param n 数组长度
     * @param v 数值范围上限
     * @return 随机数组
     */
    public static int[] randomArray(int n, int v) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * v) + 1;
        }
        return arr;
    }

    /**
     * 复制数组
     * @param arr 原数组
     * @return 复制的数组
     */
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int n = arr.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    /**
     * 比较两个数组是否相同
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 是否相同
     */
    public static boolean sameArray(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1 == null || arr2 == null) {
            return false;
        }
        int n = arr1.length;
        if (n != arr2.length) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组中交换i和j位置的数
     * @param arr 数组
     * @param i 位置i
     * @param j 位置j
     */
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 选择排序 - Selection Sort
     * 时间复杂度: O(n²) - 无论什么情况都需要进行n(n-1)/2次比较
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     * 稳定性: 不稳定 - 相等元素的相对位置可能改变
     * 
     * 算法思路：
     * 1. 在未排序序列中找到最小元素
     * 2. 将其与未排序序列的第一个元素交换位置
     * 3. 重复步骤1-2，直到所有元素排序完成
     * 
     * 优点：
     * - 实现简单
     * - 原地排序，空间复杂度低
     * - 交换次数少，最多进行n-1次交换
     * 
     * 缺点：
     * - 时间复杂度高，不适合大数据量
     * - 不稳定
     * - 无法利用数据的有序性优化
     * 
     * 适用场景：
     * - 数据量小的情况
     * - 对内存使用要求严格的场景
     * - 不要求稳定性的场景
     * 
     * @param arr 待排序数组
     */
    public static void selectionSort(int[] arr) {
        // 边界检查：空数组或单元素数组无需排序
        if (arr == null || arr.length < 2) {
            return;
        }
        
        // 外层循环控制排序的轮数，需要进行n-1轮
        // 每轮都会确定一个元素的最终位置（当前未排序部分的最小元素）
        for (int i = 0; i < arr.length - 1; i++) {
            // 假设当前位置i就是未排序部分的最小值位置
            // 从当前位置开始，在未排序部分[i, n-1]中寻找真正的最小值
            int minIndex = i;
            
            // 内层循环在未排序部分[i+1, n-1]中寻找真正的最小值
            // j从i+1开始，因为位置i已经是当前假设的最小值位置
            for (int j = i + 1; j < arr.length; j++) {
                // 如果找到更小的元素，更新最小值索引
                // 这里使用<而不是<=是为了保持算法的不稳定性
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            
            // 如果最小值不在当前位置，则交换
            // 这样可以减少不必要的交换操作（当minIndex == i时不需要交换）
            if (minIndex != i) {
                swap(arr, i, minIndex);
            }
        }
    }

    /**
     * 冒泡排序 - Bubble Sort
     * 时间复杂度: O(n²) - 最坏和平均情况，O(n) - 最好情况(已排序)
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     * 稳定性: 稳定 - 相等元素不会交换位置
     * 
     * 算法思路：
     * 1. 比较相邻的两个元素，如果前面的比后面的大就交换
     * 2. 每一轮都会将当前未排序部分的最大元素"冒泡"到末尾
     * 3. 重复步骤1-2，直到所有元素排序完成
     * 
     * 优点：
     * - 实现简单，容易理解
     * - 稳定排序
     * - 原地排序
     * - 能够检测数组是否已经有序
     * 
     * 缺点：
     * - 时间复杂度高，不适合大数据量
     * - 元素交换次数多
     * 
     * 优化：
     * - 设置标志位，如果某一轮没有发生交换，说明数组已经有序，可以提前结束
     * 
     * 适用场景：
     * - 数据量小的情况
     * - 要求稳定性的场景
     * - 教学演示
     * 
     * @param arr 待排序数组
     */
    public static void bubbleSort(int[] arr) {
        // 边界检查：空数组或单元素数组无需排序
        if (arr == null || arr.length < 2) {
            return;
        }
        
        // 外层循环控制排序的轮数，最多需要进行n-1轮
        // 每轮都会确定一个元素的最终位置（当前未排序部分的最大元素）
        // end表示每轮比较的上界，随着排序的进行逐渐减小
        for (int end = arr.length - 1; end > 0; end--) {
            // 优化标志：记录本轮是否发生交换
            // 如果一轮比较中没有发生任何交换，说明数组已经有序
            boolean swapped = false;
            
            // 内层循环进行相邻元素的比较和交换
            // 每轮比较范围逐渐缩小，因为末尾的元素已经有序
            // i从0开始到end-1，比较arr[i]和arr[i+1]
            for (int i = 0; i < end; i++) {
                // 如果前面的元素比后面的大，则交换
                // 这会将较大的元素逐步向右移动（"冒泡"）
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    swapped = true;
                }
            }
            
            // 如果本轮没有发生交换，说明数组已经有序，可以提前结束
            // 这是冒泡排序的一个重要优化，可以将最好情况的时间复杂度降到O(n)
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * 插入排序 - Insertion Sort
     * 时间复杂度: O(n²) - 最坏情况，O(n) - 最好情况(已排序)
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     * 稳定性: 稳定 - 相等元素不会交换位置
     * 
     * 算法思路：
     * 1. 将数组分为已排序和未排序两部分，初始时已排序部分只有第一个元素
     * 2. 依次取出未排序部分的元素，在已排序部分找到合适的插入位置
     * 3. 将元素插入到正确位置，重复步骤2-3直到所有元素排序完成
     * 
     * 优点：
     * - 实现简单
     * - 稳定排序
     * - 原地排序
     * - 对于小规模或基本有序的数据效率很高
     * - 在线算法：可以在接收数据的同时进行排序
     * 
     * 缺点：
     * - 时间复杂度高，不适合大数据量
     * - 对于逆序数据效率较低
     * 
     * 适用场景：
     * - 小规模数据排序
     * - 基本有序的数据
     * - 在线数据排序
     * - 作为高级排序算法的子过程（如快速排序的小数组优化）
     * 
     * @param arr 待排序数组
     */
    public static void insertionSort(int[] arr) {
        // 边界检查：空数组或单元素数组无需排序
        if (arr == null || arr.length < 2) {
            return;
        }
        
        // 从第二个元素开始，因为第一个元素可以看作已排序
        // i表示当前要插入的元素位置
        for (int i = 1; i < arr.length; i++) {
            // 从当前位置向前比较，找到合适的插入位置
            // 当前元素为arr[i]，需要在arr[0...i-1]中找到插入位置
            // j从i-1开始向前遍历已排序部分
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                // 如果前一个元素大于当前元素，则交换
                // 这实际上是在将当前元素向前移动
                swap(arr, j, j + 1);
            }
        }
    }
    
    /**
     * LeetCode 75. 颜色分类 - 三指针法（最优解）
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 稳定性: 不稳定
     * 
     * 算法思想：
     * 使用三个指针将数组分为三个区域：
     * - [0, p0): 已排序的0区域
     * - [p0, curr): 已排序的1区域
     * - [p2, n-1]: 已排序的2区域
     * - [curr, p2): 待处理的区域
     * 
     * 算法步骤：
     * 1. 初始化p0=0（0的右边界），curr=0（当前处理位置），p2=n-1（2的左边界）
     * 2. 当curr <= p2时循环：
     *    a. 如果nums[curr] == 0，交换nums[curr]和nums[p0]，p0++, curr++
     *    b. 如果nums[curr] == 1，curr++
     *    c. 如果nums[curr] == 2，交换nums[curr]和nums[p2]，p2--（curr不变）
     * 
     * 为什么是最优解：
     * - 相比基础排序算法的O(n²)时间复杂度，三指针法只需要O(n)时间
     * - 空间复杂度为O(1)，不需要额外空间
     * - 只需要一次遍历，效率高
     * - 直接利用了问题特性（只有0、1、2三种元素）
     * 
     * @param nums 待排序数组，元素只能是0、1、2
     */
    public static void sortColors(int[] nums) {
        // 防御性编程：检查输入合法性
        if (nums == null || nums.length < 2) return;
        
        int n = nums.length;
        int p0 = 0;       // 0的右边界（初始为0）
        int curr = 0;     // 当前遍历的位置
        int p2 = n - 1;   // 2的左边界（初始为数组末尾）
        
        // 遍历数组直到curr超过p2
        // 循环条件是curr <= p2，因为p2位置的元素尚未处理
        while (curr <= p2) {
            if (nums[curr] == 0) {
                // 当前元素为0，放到0的区域
                // 交换后，p0位置的元素一定是0，curr位置的元素是原来p0位置的元素（0、1或2）
                // 由于p0 <= curr，p0位置的元素已经被处理过，所以可以安全地递增curr
                swap(nums, curr, p0);
                curr++;
                p0++;
            } else if (nums[curr] == 2) {
                // 当前元素为2，放到2的区域
                // 交换后，p2位置的元素是原来curr位置的元素（未知），所以curr不能递增
                swap(nums, curr, p2);
                p2--;
            } else {
                // 当前元素为1，保持不动，继续处理下一个元素
                // 1的区域自然扩展
                curr++;
            }
        }
    }
    
    /**
     * LeetCode 283. 移动零 - 双指针法（最优解）
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 稳定性: 稳定
     * 
     * 算法思想：
     * 使用两个指针，一个指向当前应该放置非零元素的位置，另一个遍历整个数组
     * 当遇到非零元素时，将其移动到第一个指针指向的位置，然后第一个指针前进
     * 
     * 算法步骤：
     * 1. 初始化一个指针nonZeroIndex=0，表示下一个非零元素应该放置的位置
     * 2. 遍历数组，对于每个元素：
     *    a. 如果元素非零，将其移动到nonZeroIndex位置，然后nonZeroIndex++
     * 3. 遍历结束后，将nonZeroIndex到数组末尾的所有元素设置为0
     * 
     * @param nums 待处理数组
     */
    public static void moveZeroes(int[] nums) {
        // 防御性编程：检查输入合法性
        if (nums == null || nums.length < 2) return;
        
        int nonZeroIndex = 0;
        
        // 第一步：将所有非零元素移动到数组前面
        // 遍历整个数组
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                // 将非零元素移动到nonZeroIndex位置
                nums[nonZeroIndex] = nums[i];
                nonZeroIndex++;
            }
        }
        
        // 第二步：将剩余位置填充为0
        // 将nonZeroIndex到数组末尾的所有位置设置为0
        for (int i = nonZeroIndex; i < nums.length; i++) {
            nums[i] = 0;
        }
    }
    
    /**
     * LeetCode 215. 数组中的第K个最大元素 - 快速选择算法（最优解）
     * 时间复杂度: 平均O(n)，最坏O(n²)
     * 空间复杂度: O(1)
     * 
     * 算法思想：
     * 基于快速排序的分区思想，每次分区后只递归处理包含第k大元素的那一半
     * 这样可以避免对整个数组进行排序
     * 
     * 算法步骤：
     * 1. 选择一个基准元素，将数组分为两部分：大于基准的和小于基准的
     * 2. 如果基准元素的位置正好是第k大的位置，返回该元素
     * 3. 否则，递归处理包含第k大元素的那一半
     * 
     * @param nums 数组
     * @param k 第k大元素（从1开始计数）
     * @return 第k大元素的值
     */
    public static int findKthLargest(int[] nums, int k) {
        // 防御性编程：检查输入合法性
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        // 第k大元素在排序后的数组中的索引是nums.length - k
        // 例如：数组[1,2,3,4,5]中第2大的元素是4，其索引为5-2=3
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }
    
    /**
     * 快速选择算法的核心实现
     * 
     * @param nums 数组
     * @param left 左边界
     * @param right 右边界
     * @param k 目标索引（第k小的元素）
     * @return 第k小的元素值
     */
    private static int quickSelect(int[] nums, int left, int right, int k) {
        // 分区操作，返回基准元素的最终位置
        // pivotIndex是基准元素在数组中的最终位置
        int pivotIndex = partition(nums, left, right);
        
        // 如果基准元素的位置正好是k，返回该元素
        if (k == pivotIndex) {
            return nums[k];
        } else if (k < pivotIndex) {
            // 如果目标索引小于基准位置，递归处理左半部分
            return quickSelect(nums, left, pivotIndex - 1, k);
        } else {
            // 如果目标索引大于基准位置，递归处理右半部分
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }
    
    /**
     * 快速排序的分区操作
     * 
     * @param nums 数组
     * @param left 左边界
     * @param right 右边界
     * @return 基准元素的最终位置
     */
    private static int partition(int[] nums, int left, int right) {
        // 选择最右边的元素作为基准
        // 这是一种简单的选择策略，也可以使用随机选择来避免最坏情况
        int pivot = nums[right];
        // i表示小于基准元素的区域的边界
        // 初始时小于基准的区域为空，所以i = left - 1
        int i = left;
        
        // 遍历[left, right-1]范围内的元素
        for (int j = left; j < right; j++) {
            // 如果当前元素小于基准元素，将其交换到小于区域
            if (nums[j] <= pivot) {
                // 扩展小于基准的区域
                swap(nums, i, j);
                i++;
            }
        }
        
        // 将基准元素放到正确的位置
        // 此时i是基准元素应该放置的位置
        swap(nums, i, right);
        return i;
    }
    
    /**
     * 牛客网 - 最小的K个数
     * 时间复杂度: 平均O(n)，最坏O(n²)
     * 空间复杂度: O(1)
     * 
     * 解题思路：
     * 方法1：排序后取前k个 - 时间复杂度 O(n log n)
     * 方法2：堆（最大堆）- 时间复杂度 O(n log k)
     * 方法3：快速选择算法 - 平均时间复杂度 O(n)
     * 
     * 最优解：快速选择算法
     * 时间复杂度：O(n) - 平均情况
     * 空间复杂度：O(log n) - 递归栈空间
     * 
     * @param arr 输入数组
     * @param k 需要返回的最小元素个数
     * @return 最小的k个数
     */
    public static int[] getLeastNumbers(int[] arr, int k) {
        // 防御性编程
        if (arr == null || arr.length == 0 || k <= 0) {
            return new int[0];
        }
        
        if (k >= arr.length) {
            return arr.clone();
        }
        
        // 使用快速选择找到第k小的元素
        // 由于是找最小的k个数，不需要完全排序
        quickSelectForKSmallest(arr, 0, arr.length - 1, k - 1);
        
        // 前k个元素就是结果
        int[] result = new int[k];
        System.arraycopy(arr, 0, result, 0, k);
        // 对结果进行排序以满足题目要求
        Arrays.sort(result);
        return result;
    }
    
    /**
     * 快速选择算法的变体，用于找到最小的k个数
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @param k 目标索引
     */
    private static void quickSelectForKSmallest(int[] arr, int left, int right, int k) {
        if (left >= right) return;
        
        int pivotIndex = partition(arr, left, right);
        
        if (pivotIndex == k) {
            return;
        } else if (pivotIndex > k) {
            // 如果基准位置大于目标索引，递归处理左半部分
            quickSelectForKSmallest(arr, left, pivotIndex - 1, k);
        } else {
            // 如果基准位置小于目标索引，递归处理右半部分
            quickSelectForKSmallest(arr, pivotIndex + 1, right, k);
        }
    }
}
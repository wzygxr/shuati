package class021;

/**
 * 归并排序进阶应用 - Java版本
 * 
 * 本文件包含归并排序的进阶应用和优化技巧：
 * 1. 原地归并排序优化
 * 2. 自然归并排序
 * 3. TimSort算法原理
 * 4. 归并排序在数据库中的应用
 * 5. 归并排序与机器学习结合
 * 
 * 时间复杂度：O(n log n) 在各种优化下
 * 空间复杂度：根据优化策略有所不同
 * 
 * 详细题目列表请参考同目录下的MERGE_SORT_PROBLEMS.md文件
 * 包含LeetCode、洛谷、牛客网、Codeforces等平台的归并排序相关题目
 */

import java.util.*;

public class Code03_MergeSortAdvanced {
    
    // ============================================================================
    // 原地归并排序优化
    // ============================================================================
    
    /**
     * 题目1：原地归并排序（空间复杂度O(1)）
     * 核心思想：使用插入排序的思想进行原地合并
     * 时间复杂度：O(n²) 最坏情况，但实际性能较好
     * 空间复杂度：O(1)
     * 详细说明：
     * 1. 递归分解数组
     * 2. 原地合并两个有序子数组
     * 3. 通过元素移动实现原地合并
     */
    public static void inPlaceMergeSort(int[] arr, int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        inPlaceMergeSort(arr, left, mid);
        inPlaceMergeSort(arr, mid + 1, right);
        inPlaceMerge(arr, left, mid, right);
    }
    
    /**
     * 原地合并两个有序子数组
     * 使用插入排序的思想，但效率较低
     * 详细说明：
     * 1. 当左侧元素大于右侧元素时，将右侧元素插入到左侧合适位置
     * 2. 通过向右移动元素为插入元素腾出空间
     */
    private static void inPlaceMerge(int[] arr, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                i++;
            } else {
                // 将arr[j]插入到arr[i]前面
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
    
    /**
     * 题目2：块交换原地归并排序
     * 核心思想：使用块交换技术减少元素移动次数
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     * 详细说明：
     * 1. 识别需要交换的块
     * 2. 通过块交换减少元素移动次数
     * 3. 使用数组旋转技术实现块交换
     */
    public static void blockSwapMergeSort(int[] arr, int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        blockSwapMergeSort(arr, left, mid);
        blockSwapMergeSort(arr, mid + 1, right);
        blockSwapMerge(arr, left, mid, right);
    }
    
    /**
     * 块交换合并算法
     * 详细说明：
     * 1. 找到需要交换的连续块
     * 2. 通过块交换实现合并
     */
    private static void blockSwapMerge(int[] arr, int left, int mid, int right) {
        if (arr[mid] <= arr[mid + 1]) return; // 已经有序
        
        int i = left;
        int j = mid + 1;
        
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                i++;
            } else {
                // 找到右侧有序块的起始位置
                int k = j;
                while (k <= right && arr[k] < arr[i]) {
                    k++;
                }
                
                // 交换两个块
                rotate(arr, i, j - 1, k - 1);
                
                // 更新指针
                int shift = k - j;
                i += shift;
                mid += shift;
                j = k;
            }
        }
    }
    
    /**
     * 旋转数组：将arr[left...mid]和arr[mid+1...right]交换位置
     * 详细说明：
     * 1. 反转左半部分
     * 2. 反转右半部分
     * 3. 反转整个数组
     */
    private static void rotate(int[] arr, int left, int mid, int right) {
        reverse(arr, left, mid);
        reverse(arr, mid + 1, right);
        reverse(arr, left, right);
    }
    
    /**
     * 反转数组指定区间
     * 详细说明：
     * 1. 使用双指针从两端向中间交换元素
     */
    private static void reverse(int[] arr, int left, int right) {
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
    
    // ============================================================================
    // 自然归并排序
    // ============================================================================
    
    /**
     * 题目3：自然归并排序
     * 核心思想：利用数组中已有的有序序列（自然有序段）
     * 时间复杂度：O(n log k)，k为自然有序段数量
     * 空间复杂度：O(n)
     * 详细说明：
     * 1. 识别数组中的自然有序段
     * 2. 合并相邻的自然有序段
     * 3. 重复此过程直到整个数组有序
     */
    public static void naturalMergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        int[] temp = new int[arr.length];
        boolean sorted = false;
        
        while (!sorted) {
            sorted = true;
            int left = 0;
            
            while (left < arr.length) {
                // 找到第一个自然有序段的结束位置
                int mid = findRun(arr, left);
                
                if (mid == arr.length - 1) {
                    // 如果已经到末尾，说明整体有序
                    if (left == 0) return;
                    break;
                }
                
                // 找到第二个自然有序段的结束位置
                int right = findRun(arr, mid + 1);
                
                // 合并两个自然有序段
                mergeNatural(arr, temp, left, mid, right);
                left = right + 1;
                sorted = false;
            }
        }
    }
    
    /**
     * 查找自然有序段的结束位置
     * 详细说明：
     * 1. 识别升序或降序序列
     * 2. 对于降序序列进行反转
     */
    private static int findRun(int[] arr, int start) {
        if (start >= arr.length - 1) return arr.length - 1;
        
        int i = start;
        // 升序序列
        if (arr[i] <= arr[i + 1]) {
            while (i < arr.length - 1 && arr[i] <= arr[i + 1]) {
                i++;
            }
        } else {
            // 降序序列，需要反转
            while (i < arr.length - 1 && arr[i] > arr[i + 1]) {
                i++;
            }
            // 反转降序序列为升序
            reverse(arr, start, i);
        }
        
        return i;
    }
    
    /**
     * 合并两个自然有序段
     * 详细说明：
     * 1. 使用辅助数组进行合并
     * 2. 将合并结果拷贝回原数组
     */
    private static void mergeNatural(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;
        
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];
        
        // 复制回原数组
        System.arraycopy(temp, left, arr, left, right - left + 1);
    }
    
    // ============================================================================
    // TimSort算法原理（Java标准库使用的排序算法）
    // ============================================================================
    
    /**
     * 题目4：TimSort算法简化实现
     * TimSort是归并排序和插入排序的混合算法
     * 核心思想：利用自然有序段 + 插入排序优化小数组 + 平衡归并
     * 时间复杂度：O(n log n) 最好情况O(n)
     * 空间复杂度：O(n)
     * 详细说明：
     * 1. 识别自然run（有序段）
     * 2. 对短run进行扩展
     * 3. 使用插入排序优化小数组
     * 4. 平衡归并所有run
     */
    public static void timSort(int[] arr) {
        final int MIN_MERGE = 32;
        
        if (arr.length < MIN_MERGE) {
            // 小数组使用插入排序
            insertionSort(arr, 0, arr.length - 1);
            return;
        }
        
        // 计算最小run长度
        int minRun = minRunLength(arr.length);
        
        // 分割成run并进行归并
        List<int[]> runs = new ArrayList<>();
        int i = 0;
        
        while (i < arr.length) {
            // 找到自然run
            int runEnd = findTimRun(arr, i);
            
            // 如果run太短，扩展到minRun长度
            int runLength = runEnd - i + 1;
            if (runLength < minRun) {
                int force = Math.min(minRun, arr.length - i);
                insertionSort(arr, i, i + force - 1);
                runEnd = i + force - 1;
            }
            
            runs.add(new int[]{i, runEnd});
            i = runEnd + 1;
        }
        
        // 归并所有的run
        mergeAllRuns(arr, runs);
    }
    
    /**
     * 计算最小run长度
     * 详细说明：
     * 1. 基于数组长度计算合适的run长度
     * 2. 保证run长度在合理范围内
     */
    private static int minRunLength(int n) {
        int r = 0;
        while (n >= 64) {
            r |= (n & 1);
            n >>= 1;
        }
        return n + r;
    }
    
    /**
     * 查找TimRun（考虑升序和严格降序）
     * 详细说明：
     * 1. 识别升序或严格降序序列
     * 2. 对于降序序列进行反转
     */
    private static int findTimRun(int[] arr, int start) {
        if (start >= arr.length - 1) return arr.length - 1;
        
        int i = start;
        if (arr[i] <= arr[i + 1]) {
            // 弱升序（允许相等）
            while (i < arr.length - 1 && arr[i] <= arr[i + 1]) {
                i++;
            }
        } else {
            // 严格降序
            while (i < arr.length - 1 && arr[i] > arr[i + 1]) {
                i++;
            }
            reverse(arr, start, i);
        }
        
        return i;
    }
    
    /**
     * 插入排序（用于小数组优化）
     * 详细说明：
     * 1. 对指定区间进行插入排序
     * 2. 适用于小数组或短序列
     */
    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
    /**
     * 归并所有的run
     * 详细说明：
     * 1. 两两合并run
     * 2. 直到只剩一个run
     */
    private static void mergeAllRuns(int[] arr, List<int[]> runs) {
        while (runs.size() > 1) {
            List<int[]> newRuns = new ArrayList<>();
            
            for (int i = 0; i < runs.size(); i += 2) {
                if (i + 1 < runs.size()) {
                    int[] run1 = runs.get(i);
                    int[] run2 = runs.get(i + 1);
                    
                    mergeTimRun(arr, run1[0], run1[1], run2[1]);
                    newRuns.add(new int[]{run1[0], run2[1]});
                } else {
                    newRuns.add(runs.get(i));
                }
            }
            
            runs = newRuns;
        }
    }
    
    /**
     * 合并两个TimRun
     * 详细说明：
     * 1. 使用辅助数组进行合并
     * 2. 将合并结果拷贝回原数组
     */
    private static void mergeTimRun(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];
        
        System.arraycopy(temp, 0, arr, left, temp.length);
    }
    
    // ============================================================================
    // 归并排序在数据库中的应用
    // ============================================================================
    
    /**
     * 题目5：外部排序优化 - 多阶段归并
     * 数据库排序中常用的优化技术
     * 核心思想：减少磁盘I/O次数，优化归并顺序
     * 详细说明：
     * 1. 分阶段进行多路归并
     * 2. 减少磁盘读写次数
     * 3. 优化内存使用
     */
    public static class ExternalSortOptimized {
        private static final int MEMORY_SIZE = 1000; // 模拟内存大小
        private static final int BLOCK_SIZE = 100;   // 模拟磁盘块大小
        
        /**
         * 多阶段归并排序
         * 详细说明：
         * 1. 创建初始有序run
         * 2. 分阶段进行多路归并
         * 3. 减少磁盘I/O次数
         */
        public static void multiPhaseMergeSort(int[] data) {
            // 模拟分块排序（实际中会写入磁盘）
            List<int[]> sortedRuns = createInitialRuns(data);
            
            // 多阶段归并
            while (sortedRuns.size() > 1) {
                sortedRuns = mergePhase(sortedRuns);
            }
            
            // 最终结果
            if (!sortedRuns.isEmpty()) {
                System.arraycopy(sortedRuns.get(0), 0, data, 0, data.length);
            }
        }
        
        /**
         * 创建初始有序run
         * 详细说明：
         * 1. 将数据分块
         * 2. 对每块进行内部排序
         */
        private static List<int[]> createInitialRuns(int[] data) {
            List<int[]> runs = new ArrayList<>();
            int n = data.length;
            
            for (int i = 0; i < n; i += MEMORY_SIZE) {
                int end = Math.min(i + MEMORY_SIZE, n);
                int[] chunk = Arrays.copyOfRange(data, i, end);
                Arrays.sort(chunk);
                runs.add(chunk);
            }
            
            return runs;
        }
        
        /**
         * 多阶段归并
         * 详细说明：
         * 1. 三路归并减少归并次数
         * 2. 优化磁盘I/O
         */
        private static List<int[]> mergePhase(List<int[]> runs) {
            List<int[]> mergedRuns = new ArrayList<>();
            
            for (int i = 0; i < runs.size(); i += 3) {
                if (i + 2 < runs.size()) {
                    // 三路归并
                    int[] merged = threeWayMerge(
                        runs.get(i), runs.get(i + 1), runs.get(i + 2));
                    mergedRuns.add(merged);
                } else if (i + 1 < runs.size()) {
                    // 两路归并
                    int[] merged = mergeTwoArrays(runs.get(i), runs.get(i + 1));
                    mergedRuns.add(merged);
                } else {
                    mergedRuns.add(runs.get(i));
                }
            }
            
            return mergedRuns;
        }
        
        /**
         * 三路归并
         * 详细说明：
         * 1. 同时比较三个数组的元素
         * 2. 选择最小元素放入结果
         */
        private static int[] threeWayMerge(int[] a, int[] b, int[] c) {
            int[] result = new int[a.length + b.length + c.length];
            int i = 0, j = 0, k = 0, idx = 0;
            
            while (i < a.length && j < b.length && k < c.length) {
                if (a[i] <= b[j] && a[i] <= c[k]) {
                    result[idx++] = a[i++];
                } else if (b[j] <= a[i] && b[j] <= c[k]) {
                    result[idx++] = b[j++];
                } else {
                    result[idx++] = c[k++];
                }
            }
            
            // 处理剩余元素
            while (i < a.length && j < b.length) {
                if (a[i] <= b[j]) {
                    result[idx++] = a[i++];
                } else {
                    result[idx++] = b[j++];
                }
            }
            
            while (i < a.length && k < c.length) {
                if (a[i] <= c[k]) {
                    result[idx++] = a[i++];
                } else {
                    result[idx++] = c[k++];
                }
            }
            
            while (j < b.length && k < c.length) {
                if (b[j] <= c[k]) {
                    result[idx++] = b[j++];
                } else {
                    result[idx++] = c[k++];
                }
            }
            
            while (i < a.length) result[idx++] = a[i++];
            while (j < b.length) result[idx++] = b[j++];
            while (k < c.length) result[idx++] = c[k++];
            
            return result;
        }
        
        /**
         * 合并两个数组
         * 详细说明：
         * 1. 双指针合并两个有序数组
         */
        private static int[] mergeTwoArrays(int[] a, int[] b) {
            int[] result = new int[a.length + b.length];
            int i = 0, j = 0, idx = 0;
            
            while (i < a.length && j < b.length) {
                if (a[i] <= b[j]) {
                    result[idx++] = a[i++];
                } else {
                    result[idx++] = b[j++];
                }
            }
            
            while (i < a.length) result[idx++] = a[i++];
            while (j < b.length) result[idx++] = b[j++];
            
            return result;
        }
    }
    
    // ============================================================================
    // 归并排序与机器学习结合
    // ============================================================================
    
    /**
     * 题目6：归并排序在推荐系统中的应用
     * 场景：多路归并用于合并多个推荐源的结果
     * 详细说明：
     * 1. 合并多个推荐源的结果
     * 2. 根据权重调整推荐顺序
     * 3. 去重处理
     */
    public static class RecommendationSystem {
        
        /**
         * 多路归并推荐结果
         * @param recommendations 多个推荐源的结果列表
         * @param weights 各推荐源的权重
         * @return 合并后的推荐结果
         */
        public static List<Integer> mergeRecommendations(
                List<List<Integer>> recommendations, double[] weights) {
            
            // 使用优先队列进行多路归并
            PriorityQueue<RecommendationNode> pq = new PriorityQueue<>(
                (a, b) -> Double.compare(b.score, a.score));
            
            // 初始化优先队列
            for (int i = 0; i < recommendations.size(); i++) {
                List<Integer> recList = recommendations.get(i);
                if (!recList.isEmpty()) {
                    pq.offer(new RecommendationNode(recList.get(0), i, 0, weights[i]));
                }
            }
            
            List<Integer> result = new ArrayList<>();
            Set<Integer> seen = new HashSet<>();
            
            while (!pq.isEmpty() && result.size() < 100) { // 限制结果数量
                RecommendationNode node = pq.poll();
                int item = node.item;
                
                // 去重
                if (!seen.contains(item)) {
                    result.add(item);
                    seen.add(item);
                }
                
                // 添加下一个元素
                int nextIndex = node.index + 1;
                List<Integer> recList = recommendations.get(node.source);
                if (nextIndex < recList.size()) {
                    pq.offer(new RecommendationNode(
                        recList.get(nextIndex), node.source, nextIndex, weights[node.source]));
                }
            }
            
            return result;
        }
        
        /**
         * 推荐结果节点
         */
        private static class RecommendationNode {
            int item;
            int source;
            int index;
            double score;
            
            RecommendationNode(int item, int source, int index, double weight) {
                this.item = item;
                this.source = source;
                this.index = index;
                this.score = weight * (1.0 / (index + 1)); // 衰减权重
            }
        }
    }
    
    // ============================================================================
    // 测试方法
    // ============================================================================
    
    public static void testInPlaceMergeSort() {
        System.out.println("=== 原地归并排序测试 ===");
        
        int[] test = {5, 2, 8, 1, 9, 3, 7, 4, 6, 0};
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        
        inPlaceMergeSort(test, 0, test.length - 1);
        boolean passed = Arrays.equals(test, expected);
        System.out.println("原地归并排序测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testNaturalMergeSort() {
        System.out.println("=== 自然归并排序测试 ===");
        
        int[] test = {1, 2, 3, 5, 4, 6, 8, 7}; // 包含自然有序段
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8};
        
        naturalMergeSort(test);
        boolean passed = Arrays.equals(test, expected);
        System.out.println("自然归并排序测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testTimSort() {
        System.out.println("=== TimSort测试 ===");
        
        int[] test = {5, 2, 8, 1, 9, 3, 7, 4, 6, 0};
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        
        timSort(test);
        boolean passed = Arrays.equals(test, expected);
        System.out.println("TimSort测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testExternalSortOptimized() {
        System.out.println("=== 外部排序优化测试 ===");
        
        int[] test = new int[1000];
        Random random = new Random();
        for (int i = 0; i < test.length; i++) {
            test[i] = random.nextInt(10000);
        }
        
        int[] copy = test.clone();
        ExternalSortOptimized.multiPhaseMergeSort(copy);
        
        boolean passed = isSorted(copy);
        System.out.println("外部排序优化测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testRecommendationSystem() {
        System.out.println("=== 推荐系统测试 ===");
        
        List<List<Integer>> recommendations = new ArrayList<>();
        recommendations.add(Arrays.asList(1, 3, 5, 7, 9));
        recommendations.add(Arrays.asList(2, 4, 6, 8, 10));
        recommendations.add(Arrays.asList(1, 2, 3, 4, 5));
        
        double[] weights = {0.5, 0.3, 0.2};
        
        List<Integer> result = RecommendationSystem.mergeRecommendations(recommendations, weights);
        
        boolean passed = result.size() > 0 && result.size() <= 100;
        System.out.println("推荐系统测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
        System.out.println("推荐结果: " + result.subList(0, Math.min(10, result.size())));
    }
    
    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    public static void runComprehensiveTests() {
        System.out.println("=== 开始进阶应用测试 ===");
        testInPlaceMergeSort();
        testNaturalMergeSort();
        testTimSort();
        testExternalSortOptimized();
        testRecommendationSystem();
        System.out.println("=== 进阶应用测试完成 ===");
    }
    
    // ============================================================================
    // 主函数
    // ============================================================================
    
    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            runComprehensiveTests();
        } else {
            runComprehensiveTests();
            System.out.println("\n使用 'java Code03_MergeSortAdvanced test' 运行全面测试");
        }
    }
    
    /**
     * 【工程化考量总结】
     * 1. 原地优化：减少内存使用，适合资源受限环境
     * 2. 自然归并：利用数据局部有序性提升性能
     * 3. TimSort：工业级排序算法，平衡各种场景
     * 4. 外部排序：处理超大规模数据的技术
     * 5. 机器学习应用：将排序算法应用于推荐系统等场景
     * 
     * 【面试重点】
     * 1. 理解各种优化策略的原理和适用场景
     * 2. 掌握TimSort算法的核心思想
     * 3. 能够分析不同优化策略的时间空间复杂度
     * 4. 了解排序算法在实际系统中的应用
     * 
     * 【学习建议】
     * 1. 从基础归并排序开始，逐步学习各种优化
     * 2. 实践实现不同的优化版本，对比性能差异
     * 3. 研究标准库中的排序实现（如Java的Arrays.sort）
     * 4. 学习排序算法在数据库、推荐系统等领域的应用
     * 5. 关注排序算法的最新研究进展
     * 
     * 更多题目请参考同目录下的MERGE_SORT_PROBLEMS.md文件
     */
}
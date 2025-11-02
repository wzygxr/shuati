/**
 * 排序算法扩展题目 - Java版本
 * 包含LeetCode、牛客网等平台的排序相关题目
 * 每个题目都包含多种解法和详细分析
 * 
 * 题目链接汇总:
 * - 215. 数组中的第K个最大元素: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * - 75. 颜色分类: https://leetcode.cn/problems/sort-colors/
 * - 56. 合并区间: https://leetcode.cn/problems/merge-intervals/
 * - 347. 前K个高频元素: https://leetcode.cn/problems/top-k-frequent-elements/
 * - 164. 最大间距: https://leetcode.cn/problems/maximum-gap/
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
 * 工程化考量:
 * - 异常处理: 对空数组、非法输入进行验证
 * - 边界条件: 处理各种边界情况
 * - 性能优化: 根据数据规模选择最优算法
 * - 内存管理: 合理使用数据结构，避免不必要的内存占用
 * - 可读性: 清晰的命名和详细注释
 * 
 * 算法选择建议:
 * - 第K大元素: 快速选择算法（平均O(n)）
 * - 颜色分类: 三指针法（荷兰国旗问题，O(n)）
 * - 合并区间: 排序+合并（O(n log n)）
 * - 前K个高频元素: 桶排序（O(n)）或最小堆（O(n log k)）
 * - 最大间距: 基数排序（O(n)）
 */
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ExtendedProblems {
    
    /**
     * 题目1: 215. 数组中的第K个最大元素
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
     * 
     * 题目描述:
     * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
     * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     * 
     * 示例:
     * 输入: [3,2,1,5,6,4], k = 2
     * 输出: 5
     * 
     * 解法对比:
     * 1. 快速选择算法: 平均时间复杂度O(n)，最优解
     * 2. 最小堆: 时间复杂度O(n log k)，适合k较小时
     * 3. 排序: 时间复杂度O(n log n)，简单但效率较低
     * 
     * 相关题目：
     * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
     * 
     * 工程化考量:
     * - 输入验证: 检查数组是否为空，k是否合法
     * - 随机化: 快速选择使用随机基准避免最坏情况
     * - 内存优化: 最小堆只维护k个元素
     */
    public static class KthLargestElement {
        
        /**
         * 解法1: 快速选择算法 (最优解)
         * 时间复杂度: O(n) 平均, O(n²) 最坏
         * 空间复杂度: O(1)
         * 
         * 算法原理:
         * 基于快速排序的分区思想，但只处理包含目标的一侧
         * 1. 随机选择基准元素
         * 2. 进行分区操作，确定基准元素的最终位置
         * 3. 根据基准位置与目标位置的关系决定继续处理哪一侧
         * 
         * 优势:
         * - 平均时间复杂度为线性，是最优解
         * - 原地操作，空间复杂度O(1)
         * 
         * 劣势:
         * - 最坏情况时间复杂度O(n²)
         * - 不稳定排序
         * 
         * 相关题目：
         * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
         * 
         * 工程化考量：
         * - 使用ThreadLocalRandom避免多线程竞争
         * - 通过随机选择基准元素避免最坏情况
         * - 原地操作节省内存
         */
        public static int findKthLargestQuickSelect(int[] nums, int k) {
            // 输入验证
            if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
                throw new IllegalArgumentException("Invalid input parameters");
            }
            
            int left = 0, right = nums.length - 1;
            int kSmallest = nums.length - k; // 转换为第k小的索引
            
            // 循环进行分区操作直到找到目标元素
            while (left <= right) {
                // 随机选择基准元素索引
                int pivotIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
                // 分区操作，返回基准元素的最终位置
                int pivotPos = partition(nums, left, right, pivotIndex);
                
                // 根据基准位置与目标位置的关系决定继续处理哪一侧
                if (pivotPos == kSmallest) {
                    return nums[pivotPos];
                } else if (pivotPos < kSmallest) {
                    left = pivotPos + 1;
                } else {
                    right = pivotPos - 1;
                }
            }
            
            return -1;
        }
        
        /**
         * 解法2: 最小堆实现
         * 时间复杂度: O(n log k)
         * 空间复杂度: O(k)
         * 
         * 算法原理:
         * 使用最小堆维护前k个最大的元素
         * 1. 遍历数组，将元素加入堆中
         * 2. 如果堆的大小超过k，移除堆顶元素（最小的元素）
         * 3. 最后堆顶元素即为第k大的元素
         * 
         * 优势:
         * - 时间复杂度为O(n log k)，适合k较小时
         * - 空间复杂度为O(k)
         * 
         * 劣势:
         * - 时间复杂度高于快速选择
         * - 需要额外的空间
         * 
         * 工程化考量：
         * - 使用PriorityQueue实现最小堆
         * - 只维护k个元素，节省内存
         * - 适合流式数据处理
         */
        public static int findKthLargestMinHeap(int[] nums, int k) {
            // 输入验证
            if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
                throw new IllegalArgumentException("Invalid input parameters");
            }
            
            // 创建最小堆
            PriorityQueue<Integer> minHeap = new PriorityQueue<>();
            
            // 遍历数组元素
            for (int num : nums) {
                minHeap.offer(num);
                // 如果堆的大小超过k，移除堆顶元素
                if (minHeap.size() > k) {
                    minHeap.poll(); // 移除最小的元素
                }
            }
            
            // 堆顶元素即为第k大的元素
            return minHeap.peek();
        }
        
        /**
         * 解法3: 排序后直接取
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1) 或 O(n)
         * 
         * 算法原理:
         * 1. 对数组进行排序
         * 2. 返回排序后数组的倒数第k个元素
         * 
         * 优势:
         * - 简单易懂
         * - 适用于所有情况
         * 
         * 劣势:
         * - 时间复杂度较高
         * - 可能需要额外的空间
         * 
         * 工程化考量：
         * - 使用Arrays.sort()进行排序
         * - 代码简单，易于理解和维护
         * - 适合对时间复杂度要求不严格的场景
         */
        public static int findKthLargestSort(int[] nums, int k) {
            // 输入验证
            if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
                throw new IllegalArgumentException("Invalid input parameters");
            }
            
            // 克隆数组避免修改原数组
            int[] sorted = nums.clone();
            // 使用系统排序算法
            Arrays.sort(sorted);
            // 返回倒数第k个元素
            return sorted[sorted.length - k];
        }
        
        /**
         * 分区操作
         * 将数组分为小于基准、等于基准和大于基准三部分
         * 
         * @param nums 数组
         * @param left 左边界
         * @param right 右边界
         * @param pivotIndex 基准元素索引
         * @return 基准元素的最终位置
         */
        private static int partition(int[] nums, int left, int right, int pivotIndex) {
            // 获取基准元素值
            int pivotValue = nums[pivotIndex];
            // 将基准元素移到末尾
            swap(nums, pivotIndex, right);
            
            // 分区操作
            int storeIndex = left;
            for (int i = left; i < right; i++) {
                // 将小于基准的元素移到左侧
                if (nums[i] < pivotValue) {
                    swap(nums, storeIndex, i);
                    storeIndex++;
                }
            }
            // 将基准元素放到正确位置
            swap(nums, storeIndex, right);
            return storeIndex;
        }
        
        /**
         * 交换数组中两个元素
         * @param nums 数组
         * @param i 索引1
         * @param j 索引2
         */
        private static void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        
        public static void test() {
            System.out.println("=== 第K个最大元素测试 ===");
            
            int[] nums = {3, 2, 1, 5, 6, 4};
            int k = 2;
            
            System.out.println("数组: " + Arrays.toString(nums));
            System.out.println("k = " + k);
            
            int result1 = findKthLargestQuickSelect(nums.clone(), k);
            int result2 = findKthLargestMinHeap(nums.clone(), k);
            int result3 = findKthLargestSort(nums.clone(), k);
            
            System.out.println("快速选择结果: " + result1);
            System.out.println("最小堆结果: " + result2);
            System.out.println("排序结果: " + result3);
        }
    }
    
    /**
     * 题目2: 75. 颜色分类 (荷兰国旗问题)
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/sort-colors/
     * 
     * 相关题目：
     * - ALDS1_2_C: Stable Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C
     */
    public static class SortColors {
        
        /**
         * 解法1: 三指针法 (荷兰国旗问题)
         * 时间复杂度: O(n)
         * 空间复杂度: O(1)
         */
        public static void sortColorsThreePointers(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int left = 0;        // 0的右边界
            int right = nums.length - 1; // 2的左边界
            int current = 0;     // 当前指针
            
            while (current <= right) {
                if (nums[current] == 0) {
                    swap(nums, left, current);
                    left++;
                    current++;
                } else if (nums[current] == 2) {
                    swap(nums, current, right);
                    right--;
                    // current不增加，需要检查交换过来的元素
                } else {
                    current++;
                }
            }
        }
        
        /**
         * 解法2: 计数排序
         * 时间复杂度: O(n)
         * 空间复杂度: O(1) 因为只有3种颜色
         */
        public static void sortColorsCounting(int[] nums) {
            if (nums == null || nums.length <= 1) return;
            
            int[] count = new int[3]; // 0,1,2的计数
            
            // 统计每种颜色的数量
            for (int num : nums) {
                count[num]++;
            }
            
            // 重新填充数组
            int index = 0;
            for (int color = 0; color < 3; color++) {
                while (count[color] > 0) {
                    nums[index++] = color;
                    count[color]--;
                }
            }
        }
        
        private static void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        
        public static void test() {
            System.out.println("\n=== 颜色分类测试 ===");
            
            int[] nums = {2, 0, 2, 1, 1, 0};
            System.out.println("原始数组: " + Arrays.toString(nums));
            
            int[] nums1 = nums.clone();
            sortColorsThreePointers(nums1);
            System.out.println("三指针法: " + Arrays.toString(nums1));
            
            int[] nums2 = nums.clone();
            sortColorsCounting(nums2);
            System.out.println("计数排序: " + Arrays.toString(nums2));
        }
    }
    
    /**
     * 题目3: 56. 合并区间
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/merge-intervals/
     * 
     * 相关题目：
     * - ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
     */
    public static class MergeIntervals {
        
        /**
         * 解法: 排序+合并
         * 时间复杂度: O(n log n) 主要来自排序
         * 空间复杂度: O(n) 存储结果
         */
        public static int[][] merge(int[][] intervals) {
            if (intervals == null || intervals.length <= 1) {
                return intervals;
            }
            
            // 按区间起点排序
            Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
            
            List<int[]> result = new ArrayList<>();
            int[] current = intervals[0];
            result.add(current);
            
            for (int[] interval : intervals) {
                int currentEnd = current[1];
                int nextStart = interval[0];
                int nextEnd = interval[1];
                
                if (currentEnd >= nextStart) { // 有重叠
                    current[1] = Math.max(currentEnd, nextEnd); // 合并
                } else { // 无重叠
                    current = interval;
                    result.add(current);
                }
            }
            
            return result.toArray(new int[result.size()][]);
        }
        
        public static void test() {
            System.out.println("\n=== 合并区间测试 ===");
            
            int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
            System.out.println("原始区间: " + Arrays.deepToString(intervals));
            
            int[][] result = merge(intervals);
            System.out.println("合并结果: " + Arrays.deepToString(result));
        }
    }
    
    /**
     * 题目4: 347. 前K个高频元素
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/top-k-frequent-elements/
     */
    public static class TopKFrequentElements {
        
        /**
         * 解法1: 最小堆法 (最优解)
         * 时间复杂度: O(n log k)
         * 空间复杂度: O(n)
         */
        public static int[] topKFrequentMinHeap(int[] nums, int k) {
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                return new int[0];
            }
            
            // 统计频率
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int num : nums) {
                frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
            }
            
            // 最小堆，按频率排序
            PriorityQueue<Map.Entry<Integer, Integer>> minHeap = 
                new PriorityQueue<>((a, b) -> Integer.compare(a.getValue(), b.getValue()));
            
            // 保持堆的大小为k
            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                minHeap.offer(entry);
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }
            
            // 提取结果
            int[] result = new int[k];
            for (int i = k - 1; i >= 0; i--) {
                result[i] = minHeap.poll().getKey();
            }
            
            return result;
        }
        
        /**
         * 解法2: 桶排序
         * 时间复杂度: O(n)
         * 空间复杂度: O(n)
         */
        public static int[] topKFrequentBucketSort(int[] nums, int k) {
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                return new int[0];
            }
            
            // 统计频率
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int num : nums) {
                frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
            }
            
            // 创建桶，索引是频率，值是具有该频率的数字列表
            List<Integer>[] buckets = new List[nums.length + 1];
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = new ArrayList<>();
            }
            
            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                buckets[entry.getValue()].add(entry.getKey());
            }
            
            // 从后向前收集k个元素
            int[] result = new int[k];
            int index = 0;
            for (int i = buckets.length - 1; i > 0 && index < k; i--) {
                for (int num : buckets[i]) {
                    result[index++] = num;
                    if (index == k) break;
                }
            }
            
            return result;
        }
        
        public static void test() {
            System.out.println("\n=== 前K个高频元素测试 ===");
            
            int[] nums = {1, 1, 1, 2, 2, 3};
            int k = 2;
            
            System.out.println("数组: " + Arrays.toString(nums));
            System.out.println("k = " + k);
            
            int[] result1 = topKFrequentMinHeap(nums, k);
            int[] result2 = topKFrequentBucketSort(nums, k);
            
            System.out.println("最小堆结果: " + Arrays.toString(result1));
            System.out.println("桶排序结果: " + Arrays.toString(result2));
        }
    }
    
    /**
     * 题目5: 164. 最大间距
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/maximum-gap/
     * 
     * 相关题目：
     * - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
     */
    public static class MaximumGap {
        
        /**
         * 解法1: 基数排序 (最优解)
         * 时间复杂度: O(n) - 线性时间
         * 空间复杂度: O(n)
         */
        public static int maximumGapRadixSort(int[] nums) {
            if (nums == null || nums.length < 2) {
                return 0;
            }
            
            // 基数排序
            radixSort(nums);
            
            // 计算最大间距
            int maxGap = 0;
            for (int i = 1; i < nums.length; i++) {
                maxGap = Math.max(maxGap, nums[i] - nums[i - 1]);
            }
            
            return maxGap;
        }
        
        /**
         * 解法2: 排序后遍历
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1) 或 O(n)
         */
        public static int maximumGapSort(int[] nums) {
            if (nums == null || nums.length < 2) {
                return 0;
            }
            
            int[] sorted = nums.clone();
            Arrays.sort(sorted);
            
            int maxGap = 0;
            for (int i = 1; i < sorted.length; i++) {
                maxGap = Math.max(maxGap, sorted[i] - sorted[i - 1]);
            }
            
            return maxGap;
        }
        
        private static void radixSort(int[] nums) {
            if (nums.length == 0) return;
            
            // 找出最大值
            int maxVal = Arrays.stream(nums).max().getAsInt();
            
            // 按照个位、十位、百位...进行排序
            for (int exp = 1; maxVal / exp > 0; exp *= 10) {
                countingSortByDigit(nums, exp);
            }
        }
        
        private static void countingSortByDigit(int[] nums, int exp) {
            int n = nums.length;
            int[] output = new int[n];
            int[] count = new int[10];
            
            // 统计每个数字出现的次数
            for (int i = 0; i < n; i++) {
                int digit = (nums[i] / exp) % 10;
                count[digit]++;
            }
            
            // 计算累积计数
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            
            // 从后向前遍历，保证稳定性
            for (int i = n - 1; i >= 0; i--) {
                int digit = (nums[i] / exp) % 10;
                output[count[digit] - 1] = nums[i];
                count[digit]--;
            }
            
            // 复制回原数组
            System.arraycopy(output, 0, nums, 0, n);
        }
        
        public static void test() {
            System.out.println("\n=== 最大间距测试 ===");
            
            int[] nums = {3, 6, 9, 1};
            System.out.println("数组: " + Arrays.toString(nums));
            
            int result1 = maximumGapRadixSort(nums.clone());
            int result2 = maximumGapSort(nums.clone());
            
            System.out.println("基数排序结果: " + result1);
            System.out.println("普通排序结果: " + result2);
        }
    }
    
    /**
     * 综合测试函数
     */
    public static void runAllTests() {
        KthLargestElement.test();
        SortColors.test();
        MergeIntervals.test();
        TopKFrequentElements.test();
        MaximumGap.test();
    }
    
    // 主函数
    public static void main(String[] args) {
        try {
            runAllTests();
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
import java.util.*;

/**
 * 相关题目26: LeetCode 215. 数组中的第K个最大元素
 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 * 解题思路1: 使用最小堆维护前k个最大元素
 * 解题思路2: 使用快速选择算法
 * 时间复杂度: 最小堆O(n log k)，快速选择平均O(n)，最坏O(n²)
 * 空间复杂度: 最小堆O(k)，快速选择O(1)（原地版本）
 * 是否最优解: 快速选择算法在平均情况下是最优解，但堆方法更为稳定
 * 
 * 本题属于堆的应用场景：Top K问题，特别是需要高效获取第k个最大元素
 */
public class Code26_KthLargestElementInAnArray {
    
    static class Solution {
        /**
         * 使用最小堆实现查找数组中的第K个最大元素
         * 
         * @param nums 整数数组
         * @param k 要查找的第k个最大元素的位置
         * @return 数组中第k个最大的元素
         * @throws IllegalArgumentException 当输入参数无效时抛出异常
         */
        public int findKthLargestHeap(int[] nums, int k) {
            // 异常处理：检查nums和k是否有效
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                throw new IllegalArgumentException("输入参数无效");
            }
            
            // 使用最小堆，保持堆的大小为k
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
            
            // 遍历数组中的每个元素
            for (int num : nums) {
                // 如果堆的大小小于k，直接添加
                if (minHeap.size() < k) {
                    minHeap.offer(num);
                }
                // 否则，如果当前元素大于堆顶元素，替换堆顶元素
                else if (num > minHeap.peek()) {
                    minHeap.poll();
                    minHeap.offer(num);
                }
            }
            
            // 堆顶元素就是第k个最大的元素
            return minHeap.peek();
        }
        
        /**
         * 使用排序实现查找数组中的第K个最大元素（简单方法作为对比）
         * 
         * @param nums 整数数组
         * @param k 要查找的第k个最大元素的位置
         * @return 数组中第k个最大的元素
         */
        public int findKthLargestSort(int[] nums, int k) {
            // 异常处理
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                throw new IllegalArgumentException("输入参数无效");
            }
            
            // 排序数组
            Arrays.sort(nums);
            
            // 返回第k个最大的元素（数组是升序排列，所以第k个最大元素的索引是nums.length - k）
            return nums[nums.length - k];
        }
    }
    
    static class QuickSelectSolution {
        private Random random; // 用于随机选择基准元素
        
        public QuickSelectSolution() {
            this.random = new Random();
        }
        
        /**
         * 使用快速选择算法查找数组中的第K个最大元素
         * 
         * @param nums 整数数组
         * @param k 要查找的第k个最大元素的位置
         * @return 数组中第k个最大元素
         */
        public int findKthLargest(int[] nums, int k) {
            // 异常处理
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                throw new IllegalArgumentException("输入参数无效");
            }
            
            // 我们需要找的是第k大的元素，转换为在0-indexed数组中查找第(len(nums)-k)小的元素
            int targetIndex = nums.length - k;
            
            // 调用快速选择函数
            return quickSelect(nums, 0, nums.length - 1, targetIndex);
        }
        
        /**
         * 快速选择的核心实现
         * 
         * @param nums 整数数组
         * @param left 当前子数组的左边界
         * @param right 当前子数组的右边界
         * @param targetIndex 目标索引（0-indexed的第targetIndex小的元素）
         * @return 目标索引处的元素
         */
        private int quickSelect(int[] nums, int left, int right, int targetIndex) {
            // 如果区间只有一个元素，直接返回
            if (left == right) {
                return nums[left];
            }
            
            // 分区并获取基准元素的索引
            int pivotIndex = partition(nums, left, right);
            
            // 根据基准元素的位置决定下一步搜索的区间
            if (pivotIndex == targetIndex) {
                // 找到目标元素
                return nums[pivotIndex];
            } else if (pivotIndex < targetIndex) {
                // 在右半部分继续搜索
                return quickSelect(nums, pivotIndex + 1, right, targetIndex);
            } else {
                // 在左半部分继续搜索
                return quickSelect(nums, left, pivotIndex - 1, targetIndex);
            }
        }
        
        /**
         * 分区函数：选择一个基准元素，将小于基准的元素放在左边，大于基准的元素放在右边
         * 
         * @param nums 整数数组
         * @param left 子数组的左边界
         * @param right 子数组的右边界
         * @return 基准元素的最终位置
         */
        private int partition(int[] nums, int left, int right) {
            // 随机选择一个元素作为基准，避免最坏情况
            int pivotIndex = left + random.nextInt(right - left + 1);
            // 将基准元素交换到末尾
            swap(nums, pivotIndex, right);
            
            // 基准元素的值
            int pivot = nums[right];
            
            // i表示小于基准元素的区域的边界
            int i = left;
            
            // 遍历区间内的元素
            for (int j = left; j < right; j++) {
                // 如果当前元素小于基准元素，将其交换到小于区域
                if (nums[j] <= pivot) {
                    swap(nums, i, j);
                    i++;
                }
            }
            
            // 将基准元素交换到正确的位置
            swap(nums, i, right);
            
            // 返回基准元素的索引
            return i;
        }
        
        /**
         * 交换数组中的两个元素
         * 
         * @param nums 整数数组
         * @param i 第一个元素的索引
         * @param j 第二个元素的索引
         */
        private void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }
    
    static class OptimizedHeapSolution {
        /**
         * 优化的堆实现，使用Java的PriorityQueue
         * 
         * @param nums 整数数组
         * @param k 要查找的第k个最大元素的位置
         * @return 数组中第k个最大元素
         */
        public int findKthLargest(int[] nums, int k) {
            // 异常处理
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                throw new IllegalArgumentException("输入参数无效");
            }
            
            // 创建一个最小堆，大小为k
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
            
            // 添加前k个元素
            for (int i = 0; i < k; i++) {
                minHeap.offer(nums[i]);
            }
            
            // 对于剩余元素，如果大于堆顶，则替换堆顶
            for (int i = k; i < nums.length; i++) {
                if (nums[i] > minHeap.peek()) {
                    minHeap.poll();
                    minHeap.offer(nums[i]);
                }
            }
            
            // 堆顶即为第k个最大元素
            return minHeap.peek();
        }
    }

    /**
     * 测试函数，验证算法在不同输入情况下的正确性
     */
    public static void testFindKthLargest() {
        System.out.println("=== 测试数组中的第K个最大元素算法 ===");
        Solution solution = new Solution();
        QuickSelectSolution quickSelectSolution = new QuickSelectSolution();
        OptimizedHeapSolution optimizedSolution = new OptimizedHeapSolution();
        
        // 测试用例1：基本用例
        System.out.println("\n测试用例1：基本用例");
        int[] nums1 = {3, 2, 1, 5, 6, 4};
        int k1 = 2;
        int expected1 = 5;
        
        int resultHeap1 = solution.findKthLargestHeap(Arrays.copyOf(nums1, nums1.length), k1);
        int resultSort1 = solution.findKthLargestSort(Arrays.copyOf(nums1, nums1.length), k1);
        int resultQuickSelect1 = quickSelectSolution.findKthLargest(Arrays.copyOf(nums1, nums1.length), k1);
        int resultOptimized1 = optimizedSolution.findKthLargest(Arrays.copyOf(nums1, nums1.length), k1);
        
        System.out.println("最小堆实现: " + resultHeap1 + ", 期望: " + expected1 + ", " + 
                          (resultHeap1 == expected1 ? "✓" : "✗"));
        System.out.println("排序实现: " + resultSort1 + ", 期望: " + expected1 + ", " + 
                          (resultSort1 == expected1 ? "✓" : "✗"));
        System.out.println("快速选择实现: " + resultQuickSelect1 + ", 期望: " + expected1 + ", " + 
                          (resultQuickSelect1 == expected1 ? "✓" : "✗"));
        System.out.println("优化堆实现: " + resultOptimized1 + ", 期望: " + expected1 + ", " + 
                          (resultOptimized1 == expected1 ? "✓" : "✗"));
        
        // 测试用例2：有重复元素
        System.out.println("\n测试用例2：有重复元素");
        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int k2 = 4;
        int expected2 = 4;
        
        int resultHeap2 = solution.findKthLargestHeap(Arrays.copyOf(nums2, nums2.length), k2);
        int resultQuickSelect2 = quickSelectSolution.findKthLargest(Arrays.copyOf(nums2, nums2.length), k2);
        
        System.out.println("最小堆实现: " + resultHeap2 + ", 期望: " + expected2 + ", " + 
                          (resultHeap2 == expected2 ? "✓" : "✗"));
        System.out.println("快速选择实现: " + resultQuickSelect2 + ", 期望: " + expected2 + ", " + 
                          (resultQuickSelect2 == expected2 ? "✓" : "✗"));
        
        // 测试用例3：单元素数组
        System.out.println("\n测试用例3：单元素数组");
        int[] nums3 = {1};
        int k3 = 1;
        int expected3 = 1;
        
        int resultHeap3 = solution.findKthLargestHeap(Arrays.copyOf(nums3, nums3.length), k3);
        int resultQuickSelect3 = quickSelectSolution.findKthLargest(Arrays.copyOf(nums3, nums3.length), k3);
        
        System.out.println("最小堆实现: " + resultHeap3 + ", 期望: " + expected3 + ", " + 
                          (resultHeap3 == expected3 ? "✓" : "✗"));
        System.out.println("快速选择实现: " + resultQuickSelect3 + ", 期望: " + expected3 + ", " + 
                          (resultQuickSelect3 == expected3 ? "✓" : "✗"));
        
        // 测试用例4：倒序数组
        System.out.println("\n测试用例4：倒序数组");
        int[] nums4 = {6, 5, 4, 3, 2, 1};
        int k4 = 3;
        int expected4 = 4;
        
        int resultHeap4 = solution.findKthLargestHeap(Arrays.copyOf(nums4, nums4.length), k4);
        int resultQuickSelect4 = quickSelectSolution.findKthLargest(Arrays.copyOf(nums4, nums4.length), k4);
        
        System.out.println("最小堆实现: " + resultHeap4 + ", 期望: " + expected4 + ", " + 
                          (resultHeap4 == expected4 ? "✓" : "✗"));
        System.out.println("快速选择实现: " + resultQuickSelect4 + ", 期望: " + expected4 + ", " + 
                          (resultQuickSelect4 == expected4 ? "✓" : "✗"));
        
        // 测试异常情况
        System.out.println("\n=== 测试异常情况 ===");
        try {
            solution.findKthLargestHeap(new int[0], 1);
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        try {
            quickSelectSolution.findKthLargest(new int[]{1, 2, 3}, 5);
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 测试大规模输入
        int n = 1000000;
        int[] nums5 = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            nums5[i] = random.nextInt(1000001); // 0-1000000的随机数
        }
        int k5 = 500000; // 查找第50万个最大元素
        
        // 最小堆实现
        long startTime = System.currentTimeMillis();
        int resultHeap = solution.findKthLargestHeap(Arrays.copyOf(nums5, nums5.length), k5);
        long heapTime = System.currentTimeMillis() - startTime;
        System.out.println("最小堆实现结果: " + resultHeap + ", 用时: " + heapTime + "毫秒");
        
        // 快速选择实现
        startTime = System.currentTimeMillis();
        int resultQuickSelect = quickSelectSolution.findKthLargest(Arrays.copyOf(nums5, nums5.length), k5);
        long quickSelectTime = System.currentTimeMillis() - startTime;
        System.out.println("快速选择实现结果: " + resultQuickSelect + ", 用时: " + quickSelectTime + "毫秒");
        
        // 优化堆实现
        startTime = System.currentTimeMillis();
        int resultOptimized = optimizedSolution.findKthLargest(Arrays.copyOf(nums5, nums5.length), k5);
        long optimizedTime = System.currentTimeMillis() - startTime;
        System.out.println("优化堆实现结果: " + resultOptimized + ", 用时: " + optimizedTime + "毫秒");
        
        // 排序实现（对于大数组可能较慢）
        if (n <= 100000) { // 对于太大的数组，排序可能会很慢，所以只测试较小的数组
            startTime = System.currentTimeMillis();
            int resultSort = solution.findKthLargestSort(Arrays.copyOf(nums5, nums5.length), k5);
            long sortTime = System.currentTimeMillis() - startTime;
            System.out.println("排序实现结果: " + resultSort + ", 用时: " + sortTime + "毫秒");
        } else {
            System.out.println("排序实现：对于大规模数据，排序实现可能较慢，跳过测试");
        }
        
        // 验证所有方法结果一致
        boolean isConsistent = resultHeap == resultQuickSelect && resultHeap == resultOptimized;
        System.out.println("\n结果一致性检查: " + (isConsistent ? "✓" : "✗"));
        
        // 性能比较
        System.out.println("\n性能比较:");
        System.out.println("最小堆 vs 快速选择: " + 
                          (quickSelectTime < heapTime ? "快速选择更快" : "最小堆更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(heapTime, quickSelectTime) / Math.min(heapTime, quickSelectTime)) + "倍");
        System.out.println("最小堆 vs 优化堆: " + 
                          (optimizedTime < heapTime ? "优化堆更快" : "最小堆更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(heapTime, optimizedTime) / Math.min(heapTime, optimizedTime)) + "倍");
    }

    // 主方法
    public static void main(String[] args) {
        testFindKthLargest();
    }
    
    /*
     * 解题思路总结：
     * 1. 最小堆方法：
     *    - 维护一个大小为k的最小堆
     *    - 遍历数组，保持堆中有k个最大的元素
     *    - 堆顶元素即为第k个最大元素
     *    - 时间复杂度：O(n log k)，其中n是数组长度，k是要找的第k大元素的位置
     *    - 空间复杂度：O(k)
     * 
     * 2. 快速选择算法：
     *    - 基于快速排序的思想，但只需要递归处理一半的区间
     *    - 平均时间复杂度为O(n)，最坏情况为O(n²)（但通过随机选择基准元素可以避免最坏情况）
     *    - 空间复杂度：O(log n)（递归调用栈的空间），原地版本可以达到O(1)
     * 
     * 3. 排序方法：
     *    - 对数组进行排序，然后返回第k-1个索引的元素
     *    - 时间复杂度：O(n log n)
     *    - 空间复杂度：O(1)（原地排序）或O(n)（需要额外空间的排序）
     * 
     * 4. 优化技巧：
     *    - 在Java中，使用PriorityQueue实现最小堆
     *    - 快速选择算法中使用随机选择基准元素可以避免最坏情况
     *    - 对于非常大的k值（接近n），可以考虑找第(n-k+1)小的元素，可能更高效
     * 
     * 5. 应用场景：
     *    - 当需要找到数组中第k个最大元素时
     *    - 这种方法在数据分析、统计等领域有广泛应用
     * 
     * 6. 边界情况处理：
     *    - 空数组
     *    - k为0或大于数组长度
     *    - 单元素数组
     *    - 所有元素都相同的数组
     *    - 已排序或接近排序的数组
     */
}
package class027;

import java.util.*;

/**
 * 相关题目11: LeetCode 347. 前 K 个高频元素
 * 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
 * 题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
 * 解题思路: 使用哈希表统计每个元素的频率，然后使用最小堆筛选出频率最高的k个元素
 * 时间复杂度: O(n log k)，其中n是数组长度，构建哈希表需要O(n)，维护大小为k的堆需要O(n log k)
 * 空间复杂度: O(n)，哈希表需要O(n)空间，堆需要O(k)空间
 * 是否最优解: 是，这是求解前K个高频元素的最优解法之一
 * 
 * 本题属于堆的典型应用场景：需要在一组元素中快速找出前K个最大值（或最小值）
 */
public class Code19_TopKFrequentElements {
    
    /**
     * 使用最小堆求解前K个高频元素
     * @param nums 输入的整数数组
     * @param k 需要返回的高频元素数量
     * @return 出现频率前k高的元素组成的数组
     * @throws IllegalArgumentException 当输入参数无效时抛出异常
     */
    public static int[] topKFrequent(int[] nums, int k) {
        // 异常处理：检查输入数组是否为null或空
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        // 异常处理：检查k是否在有效范围内
        if (k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("k的值必须在1到数组长度之间");
        }
        
        // 特殊情况：如果数组只有一个元素且k=1
        if (nums.length == 1 && k == 1) {
            return nums;
        }
        
        // 1. 使用哈希表统计每个元素的出现频率
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 2. 使用最小堆维护频率最高的k个元素
        // 堆中存储的是Map.Entry<Integer, Integer>，按频率升序排列
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(
            (a, b) -> a.getValue() - b.getValue()
        );
        
        // 遍历哈希表，维护一个大小为k的最小堆
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (minHeap.size() < k) {
                // 如果堆的大小小于k，直接将元素加入堆
                minHeap.offer(entry);
            } else if (entry.getValue() > minHeap.peek().getValue()) {
                // 如果当前元素的频率大于堆顶元素的频率
                // 则移除堆顶元素，加入当前元素
                minHeap.poll();
                minHeap.offer(entry);
            }
            // 否则，不做任何操作
        }
        
        // 3. 从堆中取出k个元素，放入结果数组
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll().getKey();
        }
        
        return result;
    }
    
    /**
     * 使用桶排序求解前K个高频元素（另一种实现方式，时间复杂度更优）
     * @param nums 输入的整数数组
     * @param k 需要返回的高频元素数量
     * @return 出现频率前k高的元素组成的数组
     */
    public static int[] topKFrequentBucketSort(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        // 统计每个元素的频率
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 创建桶，桶的索引表示频率，桶中存储具有该频率的元素
        List<Integer>[] buckets = new List[nums.length + 1];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // 将元素放入对应的桶中
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int num = entry.getKey();
            int frequency = entry.getValue();
            buckets[frequency].add(num);
        }
        
        // 从高频率到低频率遍历桶，收集前k个元素
        int[] result = new int[k];
        int index = 0;
        for (int i = buckets.length - 1; i >= 0 && index < k; i--) {
            for (int num : buckets[i]) {
                result[index++] = num;
                if (index == k) {
                    break;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 打印数组的辅助方法
     */
    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本情况
        int[] nums1 = {1, 1, 1, 2, 2, 3};
        int k1 = 2;
        System.out.print("测试用例1（堆实现）: ");
        int[] result1 = topKFrequent(nums1, k1);
        printArray(result1); // 期望输出: [1, 2]（或[2, 1]，顺序不要求）
        
        System.out.print("测试用例1（桶排序实现）: ");
        int[] result1BucketSort = topKFrequentBucketSort(nums1, k1);
        printArray(result1BucketSort);
        
        // 测试用例2：所有元素都相同
        int[] nums2 = {1};
        int k2 = 1;
        System.out.print("\n测试用例2: ");
        int[] result2 = topKFrequent(nums2, k2);
        printArray(result2); // 期望输出: [1]
        
        // 测试用例3：所有元素频率都不同
        int[] nums3 = {1, 1, 1, 2, 2, 3, 4, 4, 4, 4};
        int k3 = 2;
        System.out.print("\n测试用例3: ");
        int[] result3 = topKFrequent(nums3, k3);
        printArray(result3); // 期望输出: [1, 4] 或 [4, 1]，取决于实现
        
        // 测试用例4：边界情况 - k等于元素种类数
        int[] nums4 = {1, 2, 3, 4};
        int k4 = 4;
        System.out.print("\n测试用例4: ");
        int[] result4 = topKFrequent(nums4, k4);
        printArray(result4); // 期望输出: [1, 2, 3, 4]（顺序不要求）
        
        // 测试异常情况
        try {
            int[] emptyNums = {};
            topKFrequent(emptyNums, 1);
            System.out.println("\n异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("\n异常测试通过: " + e.getMessage());
        }
    }
}
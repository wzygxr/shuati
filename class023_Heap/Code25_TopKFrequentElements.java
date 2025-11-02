import java.util.*;
import java.util.Map.Entry;

/**
 * 相关题目25: LeetCode 347. 前 K 个高频元素
 * 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
 * 题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
 * 解题思路1: 使用最小堆维护前k个高频元素
 * 解题思路2: 使用桶排序，按照频率分组
 * 时间复杂度: 最小堆O(n log k)，桶排序O(n)
 * 空间复杂度: 最小堆O(n)，桶排序O(n)
 * 是否最优解: 桶排序是最优解，时间复杂度为O(n)
 * 
 * 本题属于堆的应用场景：需要高效地获取一组元素中的Top K问题
 */
public class Code25_TopKFrequentElements {
    
    static class Solution {
        /**
         * 使用最小堆实现前K个高频元素
         * 
         * @param nums 整数数组
         * @param k 返回前k个高频元素
         * @return 前k个高频元素的列表
         * @throws IllegalArgumentException 当输入参数无效时抛出异常
         */
        public List<Integer> topKFrequentHeap(int[] nums, int k) {
            // 异常处理：检查nums和k是否有效
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                throw new IllegalArgumentException("输入参数无效");
            }
            
            // 统计每个元素出现的频率
            Map<Integer, Integer> countMap = new HashMap<>();
            for (int num : nums) {
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
            }
            
            // 检查k是否大于不同元素的数量
            if (k > countMap.size()) {
                throw new IllegalArgumentException("k不能大于不同元素的数量");
            }
            
            // 使用最小堆，存储元素（根据频率排序）
            // Java的PriorityQueue是最小堆，我们根据频率进行排序
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(
                (a, b) -> countMap.get(a) - countMap.get(b)
            );
            
            // 遍历所有元素及其频率
            for (int num : countMap.keySet()) {
                // 如果堆的大小小于k，直接添加
                if (minHeap.size() < k) {
                    minHeap.offer(num);
                }
                // 否则，如果当前元素的频率大于堆顶元素的频率，替换堆顶元素
                else if (countMap.get(num) > countMap.get(minHeap.peek())) {
                    minHeap.poll();
                    minHeap.offer(num);
                }
            }
            
            // 从堆中提取元素，转换为列表
            List<Integer> result = new ArrayList<>(minHeap);
            return result;
        }
        
        /**
         * 使用桶排序实现前K个高频元素
         * 
         * @param nums 整数数组
         * @param k 返回前k个高频元素
         * @return 前k个高频元素的列表
         * @throws IllegalArgumentException 当输入参数无效时抛出异常
         */
        public List<Integer> topKFrequentBucket(int[] nums, int k) {
            // 异常处理：检查nums和k是否有效
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                throw new IllegalArgumentException("输入参数无效");
            }
            
            // 统计每个元素出现的频率
            Map<Integer, Integer> countMap = new HashMap<>();
            for (int num : nums) {
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
            }
            
            // 检查k是否大于不同元素的数量
            if (k > countMap.size()) {
                throw new IllegalArgumentException("k不能大于不同元素的数量");
            }
            
            // 创建桶，索引表示频率，值是该频率的元素列表
            // 最大可能的频率是数组长度
            List<Integer>[] bucket = new List[nums.length + 1];
            for (int i = 0; i < bucket.length; i++) {
                bucket[i] = new ArrayList<>();
            }
            
            // 将元素放入对应的桶中
            for (Entry<Integer, Integer> entry : countMap.entrySet()) {
                int num = entry.getKey();
                int freq = entry.getValue();
                bucket[freq].add(num);
            }
            
            // 从后向前遍历桶，收集前k个高频元素
            List<Integer> result = new ArrayList<>();
            for (int i = bucket.length - 1; i > 0 && result.size() < k; i--) {
                if (!bucket[i].isEmpty()) {
                    result.addAll(bucket[i]);
                }
            }
            
            // 返回前k个元素
            return result.subList(0, k);
        }
    }
    
    static class AlternativeApproach {
        /**
         * 使用排序实现前K个高频元素
         * 
         * @param nums 整数数组
         * @param k 返回前k个高频元素
         * @return 前k个高频元素的列表
         */
        public List<Integer> topKFrequentSort(int[] nums, int k) {
            // 异常处理
            if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
                throw new IllegalArgumentException("输入参数无效");
            }
            
            // 统计频率
            Map<Integer, Integer> countMap = new HashMap<>();
            for (int num : nums) {
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
            }
            
            // 检查k是否大于不同元素的数量
            if (k > countMap.size()) {
                throw new IllegalArgumentException("k不能大于不同元素的数量");
            }
            
            // 创建一个列表存储元素和频率的映射
            List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(countMap.entrySet());
            
            // 按照频率排序（降序）
            entryList.sort((a, b) -> b.getValue() - a.getValue());
            
            // 提取前k个元素
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                result.add(entryList.get(i).getKey());
            }
            
            return result;
        }
    }

    /**
     * 测试函数，验证算法在不同输入情况下的正确性
     */
    public static void testTopKFrequentElements() {
        System.out.println("=== 测试前K个高频元素算法 ===");
        Solution solution = new Solution();
        AlternativeApproach alternative = new AlternativeApproach();
        
        // 测试用例1：基本用例
        System.out.println("\n测试用例1：基本用例");
        int[] nums1 = {1, 1, 1, 2, 2, 3};
        int k1 = 2;
        Set<Integer> expected1 = new HashSet<>(Arrays.asList(1, 2));  // 顺序可能不同
        
        List<Integer> resultHeap1 = solution.topKFrequentHeap(nums1, k1);
        List<Integer> resultBucket1 = solution.topKFrequentBucket(nums1, k1);
        List<Integer> resultSort1 = alternative.topKFrequentSort(nums1, k1);
        
        System.out.println("最小堆实现: " + resultHeap1);
        System.out.println("桶排序实现: " + resultBucket1);
        System.out.println("排序实现: " + resultSort1);
        
        // 验证结果（不考虑顺序）
        boolean isHeapCorrect = new HashSet<>(resultHeap1).equals(expected1);
        boolean isBucketCorrect = new HashSet<>(resultBucket1).equals(expected1);
        boolean isSortCorrect = new HashSet<>(resultSort1).equals(expected1);
        
        System.out.println("最小堆实现结果 " + (isHeapCorrect ? "✓" : "✗"));
        System.out.println("桶排序实现结果 " + (isBucketCorrect ? "✓" : "✗"));
        System.out.println("排序实现结果 " + (isSortCorrect ? "✓" : "✗"));
        
        // 测试用例2：所有元素出现频率相同
        System.out.println("\n测试用例2：所有元素出现频率相同");
        int[] nums2 = {1, 2, 3, 4, 5};
        int k2 = 3;
        
        List<Integer> resultHeap2 = solution.topKFrequentHeap(nums2, k2);
        List<Integer> resultBucket2 = solution.topKFrequentBucket(nums2, k2);
        
        System.out.println("最小堆实现: " + resultHeap2);
        System.out.println("桶排序实现: " + resultBucket2);
        System.out.println("结果长度正确: " + (resultHeap2.size() == k2 ? "✓" : "✗"));
        
        // 测试用例3：单个元素
        System.out.println("\n测试用例3：单个元素");
        int[] nums3 = {1};
        int k3 = 1;
        List<Integer> expected3 = Collections.singletonList(1);
        
        List<Integer> resultHeap3 = solution.topKFrequentHeap(nums3, k3);
        List<Integer> resultBucket3 = solution.topKFrequentBucket(nums3, k3);
        
        System.out.println("最小堆实现: " + resultHeap3 + ", 期望: " + expected3 + ", " + 
                          (resultHeap3.equals(expected3) ? "✓" : "✗"));
        System.out.println("桶排序实现: " + resultBucket3 + ", 期望: " + expected3 + ", " + 
                          (resultBucket3.equals(expected3) ? "✓" : "✗"));
        
        // 测试异常情况
        System.out.println("\n=== 测试异常情况 ===");
        try {
            solution.topKFrequentHeap(new int[0], 2);
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        try {
            solution.topKFrequentBucket(new int[]{1, 2, 3}, 5);
            System.out.println("异常测试失败：未抛出预期的异常");
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 测试大规模输入
        int n = 100000;
        int[] nums4 = new int[n];
        for (int i = 0; i < n; i++) {
            nums4[i] = i % 1000;  // 生成大规模数组，每个数字出现约100次
        }
        int k4 = 10;
        
        long startTime = System.currentTimeMillis();
        List<Integer> resultHeap = solution.topKFrequentHeap(nums4, k4);
        long heapTime = System.currentTimeMillis() - startTime;
        System.out.println("最小堆实现结果: " + resultHeap + ", 用时: " + heapTime + "毫秒");
        
        startTime = System.currentTimeMillis();
        List<Integer> resultBucket = solution.topKFrequentBucket(nums4, k4);
        long bucketTime = System.currentTimeMillis() - startTime;
        System.out.println("桶排序实现结果: " + resultBucket + ", 用时: " + bucketTime + "毫秒");
        
        startTime = System.currentTimeMillis();
        List<Integer> resultSort = alternative.topKFrequentSort(nums4, k4);
        long sortTime = System.currentTimeMillis() - startTime;
        System.out.println("排序实现结果: " + resultSort + ", 用时: " + sortTime + "毫秒");
        
        System.out.println("\n性能比较:");
        System.out.println("最小堆 vs 桶排序: " + 
                          (bucketTime < heapTime ? "桶排序更快" : "最小堆更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(heapTime, bucketTime) / Math.min(heapTime, bucketTime)) + "倍");
        System.out.println("最小堆 vs 排序: " + 
                          (sortTime < heapTime ? "排序更快" : "最小堆更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(heapTime, sortTime) / Math.min(heapTime, sortTime)) + "倍");
        System.out.println("桶排序 vs 排序: " + 
                          (sortTime < bucketTime ? "排序更快" : "桶排序更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(bucketTime, sortTime) / Math.min(bucketTime, sortTime)) + "倍");
    }

    // 主方法
    public static void main(String[] args) {
        testTopKFrequentElements();
    }
    
    /*
     * 解题思路总结：
     * 1. 最小堆方法：
     *    - 统计每个元素的频率
     *    - 使用最小堆维护k个最高频率的元素
     *    - 遍历所有元素，保持堆的大小为k
     *    - 时间复杂度：O(n log k)，其中n是数组长度，k是要返回的元素数量
     *    - 空间复杂度：O(n)，需要存储所有元素的频率
     * 
     * 2. 桶排序方法：
     *    - 统计每个元素的频率
     *    - 创建桶，索引表示频率，值是具有该频率的元素列表
     *    - 从高频率到低频率遍历桶，收集元素直到达到k个
     *    - 时间复杂度：O(n)，线性时间
     *    - 空间复杂度：O(n)
     * 
     * 3. 排序方法：
     *    - 统计每个元素的频率
     *    - 按照频率排序
     *    - 取前k个元素
     *    - 时间复杂度：O(n log n)
     *    - 空间复杂度：O(n)
     * 
     * 4. 优化技巧：
     *    - 在Java中使用HashMap快速统计频率
     *    - 使用PriorityQueue实现最小堆
     *    - 桶排序在大多数情况下是最优的，尤其是当k较大时
     * 
     * 5. 应用场景：
     *    - 当需要获取一组元素中出现频率最高的k个元素时
     *    - 这种方法在数据分析、文本处理、推荐系统等领域有广泛应用
     * 
     * 6. 边界情况处理：
     *    - 空数组
     *    - k为0或大于不同元素的数量
     *    - 所有元素频率相同的情况
     *    - 单个元素的情况
     */
}
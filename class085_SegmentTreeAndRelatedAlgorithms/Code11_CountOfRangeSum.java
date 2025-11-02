package class112;

// 327. 区间和的个数 - 线段树实现
// 题目来源：LeetCode 327 https://leetcode.cn/problems/count-of-range-sum/
// 
// 题目描述：
// 给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的区间和的个数。
// 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
// 
// 解题思路：
// 使用线段树配合前缀和与离散化来解决区间和个数问题
// 1. 计算前缀和数组，将区间和问题转换为前缀和差值问题
// 2. 对于前缀和prefixSum[i]，需要找到满足lower <= prefixSum[j] - prefixSum[i] <= upper的j个数
// 3. 转换为prefixSum[i] + lower <= prefixSum[j] <= prefixSum[i] + upper
// 4. 从右向左遍历前缀和数组，使用线段树维护已处理的前缀和信息
// 5. 对于当前前缀和prefixSum[i]，在线段树中查询满足条件的前缀和个数
// 6. 将当前前缀和加入线段树，供后续元素查询使用
// 
// 时间复杂度分析：
// - 计算前缀和：O(n)
// - 离散化：O(n log n)
// - 构建线段树：O(n)
// - 处理每个前缀和：O(log n)
// - 总时间复杂度：O(n log n)
// 空间复杂度：O(n)

import java.util.*;

public class Code11_CountOfRangeSum {
    
    // 线段树节点类
    static class SegmentTree {
        private int[] tree; // 存储线段树节点值
        private int n;      // 离散化后的值域大小
        
        /**
         * 构造函数
         * @param size 线段树维护的区间大小
         * 
         * 时间复杂度: O(n)
         * 空间复杂度: O(n)
         */
        public SegmentTree(int size) {
            this.n = size;
            this.tree = new int[4 * n]; // 线段树通常需要4倍空间
        }
        
        /**
         * 更新线段树中的某个位置的值
         * @param index 要更新的位置索引
         * @param val 要增加的值
         * 
         * 时间复杂度: O(log n)
         */
        public void update(int index, int val) {
            updateHelper(1, 0, n - 1, index, val);
        }
        
        /**
         * 更新辅助函数
         * @param node 当前节点索引
         * @param start 当前节点维护的区间左边界
         * @param end 当前节点维护的区间右边界
         * @param index 要更新的位置索引
         * @param val 要增加的值
         */
        private void updateHelper(int node, int start, int end, int index, int val) {
            if (start == end) {
                // 叶子节点，直接更新
                tree[node] += val;
            } else {
                int mid = start + (end - start) / 2;
                // 根据位置决定更新左子树还是右子树
                if (index <= mid) {
                    // 在左子树中更新
                    updateHelper(2 * node, start, mid, index, val);
                } else {
                    // 在右子树中更新
                    updateHelper(2 * node + 1, mid + 1, end, index, val);
                }
                // 更新当前节点的值（合并子节点）
                tree[node] = tree[2 * node] + tree[2 * node + 1];
            }
        }
        
        /**
         * 查询区间和
         * @param left 查询区间左边界
         * @param right 查询区间右边界
         * @return 区间[left, right]内元素的和
         * 
         * 时间复杂度: O(log n)
         */
        public int query(int left, int right) {
            // 处理边界情况
            if (left < 0) left = 0;
            if (right >= n) right = n - 1;
            if (left > right) return 0;
            
            return queryHelper(1, 0, n - 1, left, right);
        }
        
        /**
         * 查询辅助函数
         * @param node 当前节点索引
         * @param start 当前节点维护的区间左边界
         * @param end 当前节点维护的区间右边界
         * @param left 查询区间左边界
         * @param right 查询区间右边界
         * @return 区间[left, right]内元素的和
         */
        private int queryHelper(int node, int start, int end, int left, int right) {
            // 查询区间与当前节点维护区间无交集，返回0
            if (right < start || end < left) {
                return 0;
            }
            // 当前节点维护区间完全包含在查询区间内，返回节点值
            if (left <= start && end <= right) {
                return tree[node];
            }
            // 部分重叠，递归查询左右子树
            int mid = start + (end - start) / 2;
            return queryHelper(2 * node, start, mid, left, right) + 
                   queryHelper(2 * node + 1, mid + 1, end, left, right);
        }
    }
    
    /**
     * 主函数：计算区间和的个数
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 区间和在[lower, upper]范围内的个数
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int countRangeSum(int[] nums, int lower, int upper) {
        // 处理边界情况
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        // 计算前缀和数组，prefixSum[0] = 0, prefixSum[i] = nums[0] + ... + nums[i-1]
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 离散化处理，收集所有可能需要的值
        Set<Long> uniqueValues = new HashSet<>();
        for (long sum : prefixSum) {
            uniqueValues.add(sum);              // 当前前缀和
            uniqueValues.add(sum - lower);      // 用于查询下界
            uniqueValues.add(sum - upper);      // 用于查询上界
        }
        
        // 将唯一值排序并映射到连续的索引
        List<Long> sortedValues = new ArrayList<>(uniqueValues);
        Collections.sort(sortedValues);
        
        // 创建值到索引的映射
        Map<Long, Integer> valueToIndex = new HashMap<>();
        for (int i = 0; i < sortedValues.size(); i++) {
            valueToIndex.put(sortedValues.get(i), i);
        }
        
        int count = 0;
        // 构建线段树，维护离散化后的值域信息
        SegmentTree segmentTree = new SegmentTree(sortedValues.size());
        
        // 从右到左遍历前缀和，使用线段树查询符合条件的区间和
        for (int i = n; i >= 0; i--) {
            long current = prefixSum[i];
            // 查询满足lower <= prefixSum[j] - prefixSum[i] <= upper的j的数量
            // 即查询prefixSum[j]在[current + lower, current + upper]范围内的数量
            // 转换为查询离散化后的索引范围
            int left = valueToIndex.get(current + lower);
            int right = valueToIndex.get(current + upper);
            count += segmentTree.query(left, right);
            
            // 将当前前缀和添加到线段树中，供后续元素查询使用
            segmentTree.update(valueToIndex.get(current), 1);
        }
        
        return count;
    }
    
    /**
     * 测试代码
     */
    public static void main(String[] args) {
        // 测试用例1: nums = [-2,5,-1], lower = -2, upper = 2
        // 输出: 3
        // 解释：区间和分别为-2, -1, 2，都在[-2,2]范围内
        int[] nums1 = {-2, 5, -1};
        int lower1 = -2;
        int upper1 = 2;
        System.out.println("测试用例1: " + countRangeSum(nums1, lower1, upper1)); // 输出: 3
        
        // 测试用例2: nums = [0], lower = 0, upper = 0
        // 输出: 1
        // 解释：只有一个区间和0，在[0,0]范围内
        int[] nums2 = {0};
        int lower2 = 0;
        int upper2 = 0;
        System.out.println("测试用例2: " + countRangeSum(nums2, lower2, upper2)); // 输出: 1
        
        // 测试用例3: nums = [1,2,3,4,5], lower = 5, upper = 10
        // 输出: 4
        // 解释：有4个区间和在[5,10]范围内
        int[] nums3 = {1, 2, 3, 4, 5};
        int lower3 = 5;
        int upper3 = 10;
        System.out.println("测试用例3: " + countRangeSum(nums3, lower3, upper3)); // 输出: 4
    }
}
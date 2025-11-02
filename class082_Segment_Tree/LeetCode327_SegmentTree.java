package class109;

import java.util.*;

/**
 * LeetCode 327. 区间和的个数
 * 
 * 题目描述：
 * 给你一个整数数组 nums 以及两个整数 lower 和 upper 。
 * 求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的区间和的个数。
 * 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
 * 
 * 示例：
 * 输入：nums = [-2,5,-1], lower = -2, upper = 2
 * 输出：3
 * 解释：存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。
 * 
 * 解题思路：
 * 使用权值线段树来解决这个问题。
 * 1. 计算前缀和数组，将区间和问题转化为前缀和差值问题
 * 2. 离散化：由于前缀和可能很大，需要先进行离散化处理
 * 3. 权值线段树：线段树的每个节点存储某个值域范围内前缀和出现的次数
 * 4. 从左向右遍历前缀和数组，在权值线段树中查询满足条件的前缀和个数，然后将当前前缀和插入线段树
 * 
 * 时间复杂度分析：
 * - 计算前缀和：O(n)
 * - 离散化：O(n log n)
 * - 遍历数组并查询/更新：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - O(n)，线段树需要4*n的空间来存储节点信息
 * 
 * 链接：https://leetcode.cn/problems/count-of-range-sum
 */
public class LeetCode327_SegmentTree {
    
    // 线段树数组，存储区间元素个数
    private int[] tree;
    // 离散化后的数组
    private long[] sorted;
    // 离散化后的数组大小
    private int n;
    
    /**
     * 计算区间和的个数
     * @param nums 输入数组
     * @param lower 下界
     * @param upper 上界
     * @return 区间和的个数
     */
    public int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int len = nums.length;
        // 计算前缀和数组
        long[] prefixSum = new long[len + 1];
        for (int i = 0; i < len; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 离散化处理
        TreeSet<Long> set = new TreeSet<>();
        for (long sum : prefixSum) {
            set.add(sum);
            set.add(sum - lower);
            set.add(sum - upper);
        }
        sorted = new long[set.size()];
        int index = 0;
        for (long num : set) {
            sorted[index++] = num;
        }
        n = sorted.length;
        
        // 线段树数组大小通常为4*n，确保足够容纳所有节点
        tree = new int[n << 2];
        
        int result = 0;
        // 从左向右遍历前缀和数组
        for (int i = 0; i < prefixSum.length; i++) {
            // 查询满足条件 prefixSum[j] - prefixSum[i] 在 [lower, upper] 范围内的j个数
            // 即查询 prefixSum[j] 在 [prefixSum[i] + lower, prefixSum[i] + upper] 范围内的个数
            int leftBound = lowerBound(prefixSum[i] + lower);
            int rightBound = upperBound(prefixSum[i] + upper);
            result += query(0, n - 1, 1, leftBound, rightBound);
            // 将当前前缀和插入线段树
            update(0, n - 1, 1, lowerBound(prefixSum[i]));
        }
        
        return result;
    }
    
    /**
     * 找到目标值在排序数组中的下界（第一个大于等于目标值的位置）
     * @param target 目标值
     * @return 下界位置
     */
    private int lowerBound(long target) {
        int left = 0, right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
    
    /**
     * 找到目标值在排序数组中的上界（第一个大于目标值的位置）
     * @param target 目标值
     * @return 上界位置
     */
    private int upperBound(long target) {
        int left = 0, right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left - 1;
    }
    
    /**
     * 更新线段树中的值（单点更新）
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param index 要更新的位置
     */
    private void update(int start, int end, int node, int index) {
        // 如果index不在当前区间范围内，直接返回
        if (start > index || end < index) {
            return;
        }
        
        // 如果是叶子节点，直接增加计数
        if (start == end) {
            tree[node]++;
            return;
        }
        
        // 递归更新子节点
        int mid = start + (end - start) / 2;
        if (index <= mid) {
            update(start, mid, node * 2, index);
        } else {
            update(mid + 1, end, node * 2 + 1, index);
        }
        
        // 合并左右子树信息
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }
    
    /**
     * 查询线段树中指定区间的元素个数
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间内元素个数
     */
    private int query(int start, int end, int node, int left, int right) {
        // 如果查询区间无效或当前区间与查询区间无重叠，返回0
        if (left > right || start > right || end < left) {
            return 0;
        }
        
        // 如果当前区间完全包含在查询区间内，返回当前节点的值
        if (start >= left && end <= right) {
            return tree[node];
        }
        
        // 递归查询左右子树
        int mid = start + (end - start) / 2;
        int leftCount = query(start, mid, node * 2, left, right);
        int rightCount = query(mid + 1, end, node * 2 + 1, left, right);
        
        return leftCount + rightCount;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        LeetCode327_SegmentTree solution = new LeetCode327_SegmentTree();
        
        int[] nums = {-2, 5, -1};
        int lower = -2, upper = 2;
        System.out.println("输入数组: [-2, 5, -1], lower = -2, upper = 2");
        System.out.println("输出结果: " + solution.countRangeSum(nums, lower, upper)); // 应该输出3
    }
}
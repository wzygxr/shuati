package class109;

import java.util.*;

/**
 * LeetCode 493. 翻转对
 * 
 * 题目描述：
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 示例：
 * 输入: [1,3,2,3,1]
 * 输出: 2
 * 
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 
 * 解题思路：
 * 使用权值线段树来解决这个问题。
 * 1. 离散化：由于数组元素可能很大，需要先进行离散化处理，将元素映射到连续的小范围
 * 2. 权值线段树：线段树的每个节点存储某个值域范围内元素出现的次数
 * 3. 从左向右遍历数组，在权值线段树中查询满足条件的元素个数，然后将当前元素插入线段树
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)
 * - 遍历数组并查询/更新：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - O(n)，线段树需要4*n的空间来存储节点信息
 * 
 * 链接：https://leetcode.cn/problems/reverse-pairs
 */
public class LeetCode493_SegmentTree {
    
    // 线段树数组，存储区间元素个数
    private int[] tree;
    // 离散化后的数组
    private long[] sorted;
    // 离散化后的数组大小
    private int n;
    
    /**
     * 计算重要翻转对的数量
     * @param nums 输入数组
     * @return 重要翻转对的数量
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        // 离散化处理
        TreeSet<Long> set = new TreeSet<>();
        for (int num : nums) {
            set.add((long) num);
            set.add((long) 2 * num);  // 同时加入2*num，用于后续查询
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
        // 从左向右遍历数组
        for (int i = 0; i < nums.length; i++) {
            // 查询满足条件 element > 2*nums[i] 的元素个数
            // 即查询在已经处理的元素中，有多少个元素大于 2*nums[i]
            int count = query(0, n - 1, 1, lowerBound((long) 2 * nums[i] + 1), n - 1);
            result += count;
            // 将当前元素插入线段树
            update(0, n - 1, 1, lowerBound((long) nums[i]));
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
        LeetCode493_SegmentTree solution = new LeetCode493_SegmentTree();
        
        int[] nums1 = {1, 3, 2, 3, 1};
        System.out.println("输入数组: [1, 3, 2, 3, 1]");
        System.out.println("输出结果: " + solution.reversePairs(nums1)); // 应该输出2
        
        int[] nums2 = {2, 4, 3, 5, 1};
        System.out.println("输入数组: [2, 4, 3, 5, 1]");
        System.out.println("输出结果: " + solution.reversePairs(nums2)); // 应该输出3
    }
}
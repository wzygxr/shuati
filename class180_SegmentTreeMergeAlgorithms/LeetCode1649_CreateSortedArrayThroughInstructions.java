/**
 * LeetCode 1649. 通过指令创建有序数组
 * 题目链接: https://leetcode.cn/problems/create-sorted-array-through-instructions/
 * 
 * 题目描述:
 * 给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
 * 一开始你有一个空的数组 nums，你需要从左到右遍历 instructions 中的元素，将它们依次插入 nums 数组中。
 * 每一次插入操作的 代价 是以下两者的较小值：
 * 1. nums 中严格小于 instructions[i] 的数字数目
 * 2. nums 中严格大于 instructions[i] 的数字数目
 * 请你返回将 instructions 中所有元素依次插入 nums 后的 总代价。
 * 
 * 示例:
 * 输入: instructions = [1,5,6,2]
 * 输出: 1
 * 解释: 一开始 nums = []。
 * 插入 1 ，代价为 min(0, 0) = 0，现在 nums = [1]。
 * 插入 5 ，代价为 min(1, 0) = 0，现在 nums = [1,5]。
 * 插入 6 ，代价为 min(2, 0) = 0，现在 nums = [1,5,6]。
 * 插入 2 ，代价为 min(1, 2) = 1，现在 nums = [1,2,5,6]。
 * 总代价为 0 + 0 + 0 + 1 = 1。
 * 
 * 提示:
 * 1 <= instructions.length <= 10^5
 * 1 <= instructions[i] <= 10^5
 * 
 * 解题思路:
 * 这是一个经典的线段树应用问题，需要动态维护数组中每个数字的出现次数。
 * 1. 使用线段树维护值域上的数字出现次数
 * 2. 对于每个新插入的数字，查询小于它的数字个数和大于它的数字个数
 * 3. 取两者较小值作为当前插入的代价
 * 4. 将当前数字插入线段树中
 * 
 * 时间复杂度分析:
 * - 建树: O(max_value)，其中max_value是instructions中的最大值
 * - 每次查询: O(log max_value)
 * - 每次更新: O(log max_value)
 * - 总时间复杂度: O(n * log max_value)
 * 
 * 空间复杂度分析:
 * - 线段树空间: O(4 * max_value)
 * - 总空间复杂度: O(max_value)
 * 
 * 工程化考量:
 * 1. 值域离散化: 由于值域可能很大，需要进行离散化处理
 * 2. 边界处理: 处理空数组和单个元素的情况
 * 3. 性能优化: 使用位运算优化线段树操作
 * 4. 内存优化: 动态开点线段树可以节省空间
 * 
 * 面试要点:
 * 1. 理解线段树在动态统计中的应用
 * 2. 掌握离散化处理技巧
 * 3. 能够分析时间空间复杂度
 * 4. 处理边界情况和极端输入
 */

import java.util.*;

class Solution {
    private static final int MOD = 1000000007;
    
    public int createSortedArray(int[] instructions) {
        // 获取最大值用于确定线段树大小
        int maxVal = 0;
        for (int num : instructions) {
            maxVal = Math.max(maxVal, num);
        }
        
        // 创建线段树，大小为4倍最大值
        SegmentTree segTree = new SegmentTree(maxVal);
        long totalCost = 0;
        
        for (int num : instructions) {
            // 查询小于当前数字的数量
            long lessCount = segTree.query(1, num - 1);
            // 查询大于当前数字的数量
            long greaterCount = segTree.query(num + 1, maxVal);
            
            // 当前插入代价为两者较小值
            long cost = Math.min(lessCount, greaterCount);
            totalCost = (totalCost + cost) % MOD;
            
            // 将当前数字插入线段树
            segTree.update(num, 1);
        }
        
        return (int) totalCost;
    }
    
    /**
     * 线段树类，用于维护值域上的数字出现次数
     */
    class SegmentTree {
        private int n;
        private int[] tree;
        
        public SegmentTree(int size) {
            this.n = size;
            this.tree = new int[4 * n];
        }
        
        /**
         * 更新操作：在位置pos增加val
         */
        public void update(int pos, int val) {
            update(1, 1, n, pos, val);
        }
        
        private void update(int node, int left, int right, int pos, int val) {
            if (left == right) {
                tree[node] += val;
                return;
            }
            
            int mid = left + (right - left) / 2;
            if (pos <= mid) {
                update(node * 2, left, mid, pos, val);
            } else {
                update(node * 2 + 1, mid + 1, right, pos, val);
            }
            
            tree[node] = tree[node * 2] + tree[node * 2 + 1];
        }
        
        /**
         * 查询操作：查询区间[L, R]的和
         */
        public int query(int L, int R) {
            if (L > R) return 0;
            return query(1, 1, n, L, R);
        }
        
        private int query(int node, int left, int right, int L, int R) {
            if (L > right || R < left) {
                return 0;
            }
            
            if (L <= left && right <= R) {
                return tree[node];
            }
            
            int mid = left + (right - left) / 2;
            int leftSum = query(node * 2, left, mid, L, R);
            int rightSum = query(node * 2 + 1, mid + 1, right, L, R);
            
            return leftSum + rightSum;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[] instructions1 = {1, 5, 6, 2};
        int result1 = solution.createSortedArray(instructions1);
        System.out.println("测试用例1: " + Arrays.toString(instructions1) + " -> " + result1);
        
        // 测试用例2
        int[] instructions2 = {1, 2, 3, 6, 5, 4};
        int result2 = solution.createSortedArray(instructions2);
        System.out.println("测试用例2: " + Arrays.toString(instructions2) + " -> " + result2);
        
        // 测试用例3: 单个元素
        int[] instructions3 = {1};
        int result3 = solution.createSortedArray(instructions3);
        System.out.println("测试用例3: " + Arrays.toString(instructions3) + " -> " + result3);
        
        // 测试用例4: 重复元素
        int[] instructions4 = {1, 1, 1, 1};
        int result4 = solution.createSortedArray(instructions4);
        System.out.println("测试用例4: " + Arrays.toString(instructions4) + " -> " + result4);
    }
}
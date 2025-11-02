package class114;

// LeetCode 307. Range Sum Query - Mutable
// 题目描述：
// 给定一个整数数组 nums，实现一个数据结构，支持以下操作：
// 1. update(index, val): 将 nums[index] 的值更新为 val
// 2. sumRange(left, right): 返回 nums[left...right] 的元素和
//
// 解题思路：
// 使用线段树维护区间和，支持单点更新和区间查询
// 每个节点存储区间和，通过递归构建和查询
//
// 时间复杂度分析：
// 1. 建树：O(n)
// 2. 更新：O(log n)
// 3. 查询：O(log n)
// 4. 空间复杂度：O(n)
//
// 是否最优解：是
// 这是解决区间和查询与单点更新问题的最优解法

import java.io.*;
import java.util.*;

public class Code09_RangeMinimumQuery {
    
    static class NumArray {
        private int[] tree;
        private int n;
        
        public NumArray(int[] nums) {
            n = nums.length;
            tree = new int[4 * n];
            buildTree(nums, 0, n - 1, 1);
        }
        
        // 建立线段树
        private void buildTree(int[] nums, int l, int r, int idx) {
            if (l == r) {
                tree[idx] = nums[l];
                return;
            }
            int mid = l + (r - l) / 2;
            buildTree(nums, l, mid, idx * 2);
            buildTree(nums, mid + 1, r, idx * 2 + 1);
            tree[idx] = tree[idx * 2] + tree[idx * 2 + 1];
        }
        
        // 更新操作
        public void update(int index, int val) {
            updateTree(0, n - 1, 1, index, val);
        }
        
        private void updateTree(int l, int r, int idx, int index, int val) {
            if (l == r) {
                tree[idx] = val;
                return;
            }
            int mid = l + (r - l) / 2;
            if (index <= mid) {
                updateTree(l, mid, idx * 2, index, val);
            } else {
                updateTree(mid + 1, r, idx * 2 + 1, index, val);
            }
            tree[idx] = tree[idx * 2] + tree[idx * 2 + 1];
        }
        
        // 区间查询
        public int sumRange(int left, int right) {
            return queryTree(0, n - 1, 1, left, right);
        }
        
        private int queryTree(int l, int r, int idx, int left, int right) {
            if (left <= l && r <= right) {
                return tree[idx];
            }
            int mid = l + (r - l) / 2;
            int sum = 0;
            if (left <= mid) {
                sum += queryTree(l, mid, idx * 2, left, right);
            }
            if (right > mid) {
                sum += queryTree(mid + 1, r, idx * 2 + 1, left, right);
            }
            return sum;
        }
    }
    
    public static void main(String[] args) {
        // 测试用例
        int[] nums = {1, 3, 5};
        NumArray numArray = new NumArray(nums);
        
        System.out.println("初始数组: " + Arrays.toString(nums));
        System.out.println("sumRange(0, 2) = " + numArray.sumRange(0, 2)); // 9
        
        numArray.update(1, 2);
        System.out.println("更新索引1为2后");
        System.out.println("sumRange(0, 2) = " + numArray.sumRange(0, 2)); // 8
        
        // 边界测试
        System.out.println("sumRange(0, 0) = " + numArray.sumRange(0, 0)); // 1
        System.out.println("sumRange(2, 2) = " + numArray.sumRange(2, 2)); // 5
    }
}
package class131;

import java.util.*;

/**
 * AtCoder ABC185F. Range Xor Query
 * 题目链接: https://atcoder.jp/contests/abc185/tasks/abc185_f
 * 题目描述: 给定一个数组，支持两种操作：
 * 1. 更新数组中某个位置的值
 * 2. 查询区间[l,r]内所有元素的异或值
 *
 * 解题思路:
 * 使用线段树实现区间异或查询和单点更新操作
 * 1. 线段树每个节点存储对应区间的异或值
 * 2. 利用异或的性质：a ^ a = 0, a ^ 0 = a
 * 
 * 时间复杂度分析:
 * - 构建线段树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(4n) 线段树需要约4n的空间
 */
public class Code14_RangeXORQuery {
    
    // 线段树节点定义
    static class SegmentTreeNode {
        int start, end;
        SegmentTreeNode left, right;
        int xor; // 区间异或值
        
        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.left = null;
            this.right = null;
            this.xor = 0;
        }
    }
    
    SegmentTreeNode root = null;
    int[] nums;
    
    public Code14_RangeXORQuery(int[] nums) {
        this.nums = nums.clone();
        root = buildTree(nums, 0, nums.length - 1);
    }
    
    // 构建线段树
    private SegmentTreeNode buildTree(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }
        
        SegmentTreeNode ret = new SegmentTreeNode(start, end);
        // 叶子节点
        if (start == end) {
            ret.xor = nums[start];
        } else {
            // 递归构建左右子树
            int mid = start + (end - start) / 2;
            ret.left = buildTree(nums, start, mid);
            ret.right = buildTree(nums, mid + 1, end);
            // 更新当前节点的异或值
            ret.xor = ret.left.xor ^ ret.right.xor;
        }
        return ret;
    }
    
    // 更新指定位置的值
    public void update(int i, int val) {
        update(root, i, val);
    }
    
    // 更新线段树中的值
    private void update(SegmentTreeNode root, int pos, int val) {
        // 叶子节点，直接更新
        if (root.start == root.end) {
            root.xor = val;
            nums[pos] = val;
        } else {
            // 非叶子节点，递归更新
            int mid = root.start + (root.end - root.start) / 2;
            if (pos <= mid) {
                update(root.left, pos, val);
            } else {
                update(root.right, pos, val);
            }
            // 更新当前节点的异或值
            root.xor = root.left.xor ^ root.right.xor;
        }
    }
    
    // 查询区间异或值
    public int xorRange(int i, int j) {
        return xorRange(root, i, j);
    }
    
    // 查询线段树中指定区间的异或值
    private int xorRange(SegmentTreeNode root, int start, int end) {
        // 完全匹配
        if (root.start == start && root.end == end) {
            return root.xor;
        } else {
            int mid = root.start + (root.end - root.start) / 2;
            // 完全在左子树
            if (end <= mid) {
                return xorRange(root.left, start, end);
            } 
            // 完全在右子树
            else if (start >= mid + 1) {
                return xorRange(root.right, start, end);
            }
            // 跨越左右子树
            else {
                return xorRange(root.left, start, mid) ^ xorRange(root.right, mid + 1, end);
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试
        int[] nums = {1, 3, 5, 7, 9};
        Code14_RangeXORQuery solution = new Code14_RangeXORQuery(nums);
        
        System.out.println("初始数组: " + Arrays.toString(nums));
        System.out.println("区间[0,2]的异或值: " + solution.xorRange(0, 2)); // 1^3^5 = 7
        System.out.println("区间[1,3]的异或值: " + solution.xorRange(1, 3)); // 3^5^7 = 1
        
        solution.update(1, 2); // 将索引1的值从3更新为2
        System.out.println("更新索引1的值为2后:");
        System.out.println("区间[0,2]的异或值: " + solution.xorRange(0, 2)); // 1^2^5 = 6
        System.out.println("区间[1,3]的异或值: " + solution.xorRange(1, 3)); // 2^5^7 = 0
    }
}
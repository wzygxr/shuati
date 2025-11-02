package class131;

import java.util.*;

/**
 * SPOJ GSS1. Can you answer these queries I
 * 题目链接: https://www.spoj.com/problems/GSS1/
 * 题目描述: 给定一个数组，查询区间[l,r]内的最大子段和
 *
 * 解题思路:
 * 使用线段树实现区间最大子段和查询
 * 每个节点维护四个值：
 * 1. 区间和(sum)
 * 2. 区间最大子段和(maxSum)
 * 3. 包含左端点的最大子段和(prefixMaxSum)
 * 4. 包含右端点的最大子段和(suffixMaxSum)
 * 
 * 时间复杂度分析:
 * - 构建线段树: O(n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(4n) 线段树需要约4n的空间
 */
public class Code15_MaximumSubarraySum {
    
    // 线段树节点定义
    static class SegmentTreeNode {
        int start, end;
        SegmentTreeNode left, right;
        int sum;           // 区间和
        int maxSum;        // 区间最大子段和
        int prefixMaxSum;  // 包含左端点的最大子段和
        int suffixMaxSum;  // 包含右端点的最大子段和
        
        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.left = null;
            this.right = null;
            this.sum = 0;
            this.maxSum = Integer.MIN_VALUE;
            this.prefixMaxSum = Integer.MIN_VALUE;
            this.suffixMaxSum = Integer.MIN_VALUE;
        }
    }
    
    SegmentTreeNode root = null;
    
    public Code15_MaximumSubarraySum(int[] nums) {
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
            ret.sum = nums[start];
            ret.maxSum = nums[start];
            ret.prefixMaxSum = nums[start];
            ret.suffixMaxSum = nums[start];
        } else {
            // 递归构建左右子树
            int mid = start + (end - start) / 2;
            ret.left = buildTree(nums, start, mid);
            ret.right = buildTree(nums, mid + 1, end);
            // 更新当前节点的值
            ret.sum = ret.left.sum + ret.right.sum;
            ret.prefixMaxSum = Math.max(ret.left.prefixMaxSum, ret.left.sum + ret.right.prefixMaxSum);
            ret.suffixMaxSum = Math.max(ret.right.suffixMaxSum, ret.right.sum + ret.left.suffixMaxSum);
            ret.maxSum = Math.max(Math.max(ret.left.maxSum, ret.right.maxSum), ret.left.suffixMaxSum + ret.right.prefixMaxSum);
        }
        return ret;
    }
    
    // 查询区间最大子段和
    public int maxSubarraySum(int i, int j) {
        return maxSubarraySum(root, i, j).maxSum;
    }
    
    // 查询线段树中指定区间的最大子段和信息
    private SegmentTreeNode maxSubarraySum(SegmentTreeNode root, int start, int end) {
        // 完全匹配
        if (root.start == start && root.end == end) {
            return root;
        } else {
            int mid = root.start + (root.end - root.start) / 2;
            // 完全在左子树
            if (end <= mid) {
                return maxSubarraySum(root.left, start, end);
            } 
            // 完全在右子树
            else if (start >= mid + 1) {
                return maxSubarraySum(root.right, start, end);
            }
            // 跨越左右子树
            else {
                SegmentTreeNode leftResult = maxSubarraySum(root.left, start, mid);
                SegmentTreeNode rightResult = maxSubarraySum(root.right, mid + 1, end);
                
                SegmentTreeNode result = new SegmentTreeNode(start, end);
                result.sum = leftResult.sum + rightResult.sum;
                result.prefixMaxSum = Math.max(leftResult.prefixMaxSum, leftResult.sum + rightResult.prefixMaxSum);
                result.suffixMaxSum = Math.max(rightResult.suffixMaxSum, rightResult.sum + leftResult.suffixMaxSum);
                result.maxSum = Math.max(Math.max(leftResult.maxSum, rightResult.maxSum), leftResult.suffixMaxSum + rightResult.prefixMaxSum);
                return result;
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试
        int[] nums = {1, -2, 3, 4, -5, 6};
        Code15_MaximumSubarraySum solution = new Code15_MaximumSubarraySum(nums);
        
        System.out.println("初始数组: " + Arrays.toString(nums));
        System.out.println("区间[0,5]的最大子段和: " + solution.maxSubarraySum(0, 5)); // 3+4-5+6 = 8
        System.out.println("区间[1,4]的最大子段和: " + solution.maxSubarraySum(1, 4)); // 3+4 = 7
        System.out.println("区间[2,3]的最大子段和: " + solution.maxSubarraySum(2, 3)); // max(3, 4, 3+4) = 7
    }
}
package class112;

// 493. 翻转对 - 动态开点线段树实现
// 题目来源：LeetCode 493 https://leetcode.cn/problems/reverse-pairs/
// 
// 题目描述：
// 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对。
// 你需要返回给定数组中的重要翻转对的数量。
// 
// 解题思路：
// 使用动态开点线段树来解决翻转对问题
// 1. 从右向左遍历数组，这样可以确保每次处理的元素右侧元素都已经处理过
// 2. 使用线段树维护值域信息，记录每个值出现的次数
// 3. 对于当前元素nums[i]，查询值域中大于2*nums[i]的元素个数，即为以i为第一个元素的翻转对数量
// 4. 将当前元素加入线段树，供后续元素查询使用
// 
// 时间复杂度分析：
// - 收集所有值：O(n)
// - 离散化：O(n log n)
// - 构建线段树：O(1)
// - 处理每个元素：O(log n)
// - 总时间复杂度：O(n log n)
// 空间复杂度：O(n)
//
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.util.*;

public class Code10_ReversePairs {
    
    // 线段树节点
    static class SegmentTreeNode {
        int start, end;           // 节点维护的值域范围
        int count;                // 该值域范围内元素的个数
        SegmentTreeNode left, right;  // 左右子节点
        
        SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.count = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    // 线段树实现
    static class SegmentTree {
        SegmentTreeNode root;  // 线段树根节点
        
        SegmentTree(int start, int end) {
            this.root = new SegmentTreeNode(start, end);
        }
        
        /**
         * 更新线段树，将值val的计数加1
         * @param val 要更新的值
         * 
         * 时间复杂度: O(log n)
         */
        public void update(int val) {
            updateHelper(root, val);
        }
        
        /**
         * 更新线段树的辅助函数
         * @param node 当前节点
         * @param val 要更新的值
         */
        private void updateHelper(SegmentTreeNode node, int val) {
            // 到达叶子节点，更新计数
            if (node.start == node.end) {
                node.count++;
                return;
            }
            
            int mid = node.start + (node.end - node.start) / 2;
            // 根据值的大小决定更新左子树还是右子树
            if (val <= mid) {
                // 如果左子节点不存在，则创建
                if (node.left == null) {
                    node.left = new SegmentTreeNode(node.start, mid);
                }
                updateHelper(node.left, val);
            } else {
                // 如果右子节点不存在，则创建
                if (node.right == null) {
                    node.right = new SegmentTreeNode(mid + 1, node.end);
                }
                updateHelper(node.right, val);
            }
            
            // 更新当前节点的计数为左右子节点计数之和
            node.count = (node.left == null ? 0 : node.left.count) + 
                         (node.right == null ? 0 : node.right.count);
        }
        
        /**
         * 查询大于等于某个值的元素个数
         * @param val 查询的值
         * @return 大于等于val的元素个数
         * 
         * 时间复杂度: O(log n)
         */
        public int query(int val) {
            return queryHelper(root, val);
        }
        
        /**
         * 查询大于等于某个值的元素个数的辅助函数
         * @param node 当前节点
         * @param val 查询的值
         * @return 大于等于val的元素个数
         */
        private int queryHelper(SegmentTreeNode node, int val) {
            // 节点为空或查询值大于节点维护的最大值，返回0
            if (node == null || val > node.end) {
                return 0;
            }
            
            // 查询值小于等于节点维护的最小值，返回该节点的计数
            if (val <= node.start) {
                return node.count;
            }
            
            int mid = node.start + (node.end - node.start) / 2;
            // 根据值的大小决定查询左子树还是右子树
            if (val <= mid) {
                // 查询值在左半部分，需要加上右半部分的计数
                return queryHelper(node.left, val) + 
                       (node.right == null ? 0 : node.right.count);
            } else {
                return queryHelper(node.right, val);
            }
        }
    }
    
    /**
     * 主函数：计算翻转对的数量
     * @param nums 输入数组
     * @return 翻转对的数量
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 收集所有可能的值用于离散化（包括nums[i]和2*nums[i]）
        Set<Long> allNumbers = new HashSet<>();
        for (int num : nums) {
            allNumbers.add((long) num);
            allNumbers.add(2L * num);
        }
        
        // 离散化处理
        long[] sorted = new long[allNumbers.size()];
        int index = 0;
        for (long num : allNumbers) {
            sorted[index++] = num;
        }
        Arrays.sort(sorted);
        
        // 构建线段树，值域为离散化后的索引范围
        SegmentTree tree = new SegmentTree(0, sorted.length - 1);
        
        int result = 0;
        // 从右向左遍历数组
        for (int i = nums.length - 1; i >= 0; i--) {
            // 查找2*nums[i]在离散化数组中的位置
            int pos = Arrays.binarySearch(sorted, 2L * nums[i]);
            // 查询大于2*nums[i]的元素个数（即以i为第一个元素的翻转对数量）
            result += tree.query(pos + 1);
            // 查找nums[i]在离散化数组中的位置并更新线段树
            pos = Arrays.binarySearch(sorted, (long) nums[i]);
            tree.update(pos);
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code10_ReversePairs solution = new Code10_ReversePairs();
        
        // 测试用例1: nums = [1,3,2,3,1]
        // 输出: 2
        // 解释：翻转对是(1,4)和(3,4)，即(1>2*1)和(3>2*1)
        int[] nums1 = {1, 3, 2, 3, 1};
        System.out.println(solution.reversePairs(nums1)); // 应该输出 2
        
        // 测试用例2: nums = [2,4,3,5,1]
        // 输出: 3
        // 解释：翻转对是(0,4)、(1,4)和(2,4)，即(2>2*1)、(4>2*1)和(3>2*1)
        int[] nums2 = {2, 4, 3, 5, 1};
        System.out.println(solution.reversePairs(nums2)); // 应该输出 3
    }
}
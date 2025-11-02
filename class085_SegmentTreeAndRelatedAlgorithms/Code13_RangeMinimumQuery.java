package class112;

// 区间最小值查询 - 线段树实现
// 
// 题目描述：
// 实现一个支持区间最小值查询和单点更新的数据结构
// 支持以下操作：
// 1. 构造函数：用整数数组初始化对象
// 2. update：将数组中某个位置的值更新为新值
// 3. minRange：查询数组中某个区间内的最小值
// 
// 解题思路：
// 使用线段树来高效处理区间最小值查询和单点更新操作
// 1. 线段树是一种二叉树结构，每个节点代表一个区间
// 2. 叶子节点代表数组中的单个元素
// 3. 非叶子节点代表其子节点区间的合并结果（这里是区间最小值）
// 
// 时间复杂度分析：
// - 构建线段树：O(n)
// - 单点更新：O(log n)
// - 区间查询：O(log n)
// 空间复杂度：O(n)

import java.util.*;

public class Code13_RangeMinimumQuery {
    
    // 线段树实现
    static class SegmentTree {
        int[] tree;
        int n;
        
        /**
         * 构造函数，用给定数组构建线段树
         * @param nums 原始数组
         */
        public SegmentTree(int[] nums) {
            n = nums.length;
            tree = new int[n * 4]; // 线段树通常需要4倍空间
            buildTree(nums, 0, 0, n - 1);
        }
        
        /**
         * 构建线段树
         * @param nums 原始数组
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * 
         * 时间复杂度: O(n)
         * 空间复杂度: O(n)
         */
        private void buildTree(int[] nums, int node, int start, int end) {
            if (start == end) {
                // 叶子节点，存储数组元素值
                tree[node] = nums[start];
            } else {
                int mid = (start + end) / 2;
                // 递归构建左子树
                buildTree(nums, 2 * node + 1, start, mid);
                // 递归构建右子树
                buildTree(nums, 2 * node + 2, mid + 1, end);
                // 合并左右子树的结果，存储区间最小值
                tree[node] = Math.min(tree[2 * node + 1], tree[2 * node + 2]);
            }
        }
        
        /**
         * 更新数组中某个位置的值
         * @param index 要更新的数组索引
         * @param val 新的值
         * 
         * 时间复杂度: O(log n)
         */
        public void update(int index, int val) {
            updateHelper(0, 0, n - 1, index, val);
        }
        
        /**
         * 更新线段树中某个位置的值的辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param index 要更新的数组索引
         * @param val 新的值
         */
        private void updateHelper(int node, int start, int end, int index, int val) {
            if (start == end) {
                // 找到叶子节点，更新值
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (index <= mid) {
                    // 要更新的索引在左子树中
                    updateHelper(2 * node + 1, start, mid, index, val);
                } else {
                    // 要更新的索引在右子树中
                    updateHelper(2 * node + 2, mid + 1, end, index, val);
                }
                // 更新父节点的值（区间最小值）
                tree[node] = Math.min(tree[2 * node + 1], tree[2 * node + 2]);
            }
        }
        
        /**
         * 查询区间最小值
         * @param left 查询区间左边界
         * @param right 查询区间右边界
         * @return 区间[left, right]内元素的最小值
         * 
         * 时间复杂度: O(log n)
         */
        public int minRange(int left, int right) {
            return minRangeHelper(0, 0, n - 1, left, right);
        }
        
        /**
         * 查询区间最小值的辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param left 查询区间左边界
         * @param right 查询区间右边界
         * @return 区间[left, right]与当前节点区间交集内元素的最小值
         */
        private int minRangeHelper(int node, int start, int end, int left, int right) {
            if (right < start || end < left) {
                // 查询区间与当前区间无交集，返回一个极大值
                return Integer.MAX_VALUE;
            }
            if (left <= start && end <= right) {
                // 当前区间完全包含在查询区间内，直接返回当前节点值
                return tree[node];
            }
            // 部分重叠，递归查询左右子树
            int mid = (start + end) / 2;
            return Math.min(minRangeHelper(2 * node + 1, start, mid, left, right),
                           minRangeHelper(2 * node + 2, mid + 1, end, left, right));
        }
    }
    
    SegmentTree st;
    
    /**
     * 构造函数，用整数数组 nums 初始化对象
     * @param nums 初始数组
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public Code13_RangeMinimumQuery(int[] nums) {
        st = new SegmentTree(nums);
    }
    
    /**
     * 将 nums[index] 的值更新为 val
     * @param index 要更新的数组索引
     * @param val 新的值
     * 
     * 时间复杂度: O(log n)
     */
    public void update(int index, int val) {
        st.update(index, val);
    }
    
    /**
     * 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的最小值
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间最小值
     * 
     * 时间复杂度: O(log n)
     */
    public int minRange(int left, int right) {
        return st.minRange(left, right);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例:
        // nums = [1, 3, 5]
        // minRange(0, 2) => 1
        // update(1, 2)   // nums = [1,2,5]
        // minRange(0, 2) => 1
        
        int[] nums = {1, 3, 5};
        Code13_RangeMinimumQuery numArray = new Code13_RangeMinimumQuery(nums);
        System.out.println(numArray.minRange(0, 2)); // 应该输出 1
        numArray.update(1, 2);   // nums = [1,2,5]
        System.out.println(numArray.minRange(0, 2)); // 应该输出 1
    }
}
package class131;

/** 
 * LeetCode 307. Range Sum Query - Mutable (区域和检索 - 数组可修改)
 * 题目链接: https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * 题目描述: 
 * 给你一个数组 nums ，请你完成两类查询：
 * 1. 更新数组 nums 下标对应的值
 * 2. 求数组 nums 中索引 left 和 right 之间的元素和，包含 left 和 right 两点
 * 
 * 解题思路:
 * 使用线段树实现，支持单点更新和区间查询
 * 线段树每个节点存储对应区间的元素和
 * 
 * 时间复杂度分析:
 * - 构建线段树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(4n) 线段树需要约4n的空间
 */
public class Code07_RangeSumQueryMutable_SegmentTree {
    
    /** 
     * 线段树节点定义
     * 每个节点表示数组的一个区间[start, end]，并存储该区间内所有元素的和
     */
    static class SegmentTreeNode {
        int start, end;           // 节点表示的区间范围
        SegmentTreeNode left, right;  // 左右子节点
        int sum;                  // 区间内元素的和
        
        /** 
         * 构造函数
         * 
         * @param start 区间起始位置
         * @param end   区间结束位置
         */
        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.left = null;
            this.right = null;
            this.sum = 0;
        }
    }
    
    SegmentTreeNode root = null;  // 线段树根节点
    
    /** 
     * 构造函数，根据给定数组构建线段树
     * 
     * @param nums 初始数组
     */
    public Code07_RangeSumQueryMutable_SegmentTree(int[] nums) {
        root = buildTree(nums, 0, nums.length - 1);
    }
    
    /** 
     * 构建线段树
     * 采用递归方式构建，每个节点表示一个区间，叶节点表示单个元素
     * 
     * @param nums  原始数组
     * @param start 区间起始位置
     * @param end   区间结束位置
     * @return      构建好的线段树节点
     */
    private SegmentTreeNode buildTree(int[] nums, int start, int end) {
        // 边界条件：无效区间
        if (start > end) {
            return null;
        }
        
        // 创建当前节点
        SegmentTreeNode ret = new SegmentTreeNode(start, end);
        // 叶子节点：区间只包含一个元素
        if (start == end) {
            ret.sum = nums[start];
        } else {
            // 非叶子节点：递归构建左右子树
            int mid = start + (end - start) / 2;
            ret.left = buildTree(nums, start, mid);
            ret.right = buildTree(nums, mid + 1, end);
            // 更新当前节点的值为左右子树值的和
            ret.sum = ret.left.sum + ret.right.sum;
        }
        return ret;
    }
    
    /** 
     * 更新指定位置的值
     * 
     * @param i   要更新的数组索引
     * @param val 新的值
     */
    void update(int i, int val) {
        update(root, i, val);
    }
    
    /** 
     * 更新线段树中的值
     * 递归查找目标位置并更新，然后向上回溯更新父节点的值
     * 
     * @param root 线段树节点
     * @param pos  要更新的数组位置
     * @param val  新的值
     */
    private void update(SegmentTreeNode root, int pos, int val) {
        // 叶子节点，直接更新
        if (root.start == root.end) {
            root.sum = val;
        } else {
            // 非叶子节点，递归更新
            int mid = root.start + (root.end - root.start) / 2;
            // 根据位置决定更新左子树还是右子树
            if (pos <= mid) {
                update(root.left, pos, val);
            } else {
                update(root.right, pos, val);
            }
            // 更新当前节点的值为左右子树值的和
            root.sum = root.left.sum + root.right.sum;
        }
    }
    
    /** 
     * 查询区间和
     * 
     * @param i 查询区间起始位置
     * @param j 查询区间结束位置
     * @return  区间[i, j]内元素的和
     */
    public int sumRange(int i, int j) {
        return sumRange(root, i, j);
    }
    
    /** 
     * 查询线段树中指定区间的和
     * 根据查询区间与当前节点区间的关系，决定是直接返回、递归查询还是分段查询
     * 
     * @param root  线段树节点
     * @param start 查询区间起始位置
     * @param end   查询区间结束位置
     * @return      区间[start, end]内元素的和
     */
    private int sumRange(SegmentTreeNode root, int start, int end) {
        // 完全匹配：当前节点区间与查询区间完全一致
        if (root.start == start && root.end == end) {
            return root.sum;
        } else {
            int mid = root.start + (root.end - root.start) / 2;
            // 完全在左子树：查询区间完全在左半部分
            if (end <= mid) {
                return sumRange(root.left, start, end);
            } 
            // 完全在右子树：查询区间完全在右半部分
            else if (start >= mid + 1) {
                return sumRange(root.right, start, end);
            }
            // 跨越左右子树：查询区间跨越中点，需要分别查询两部分再合并
            else {
                return sumRange(root.left, start, mid) + sumRange(root.right, mid + 1, end);
            }
        }
    }
    
    /** 
     * 测试方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        int[] nums = {1, 3, 5};
        Code07_RangeSumQueryMutable_SegmentTree numArray = new Code07_RangeSumQueryMutable_SegmentTree(nums);
        
        System.out.println("Initial sum from index 0 to 2: " + numArray.sumRange(0, 2)); // 应该输出9
        
        numArray.update(1, 2); // 将索引1的值从3更新为2
        System.out.println("Sum from index 0 to 2 after update: " + numArray.sumRange(0, 2)); // 应该输出8
    }
}
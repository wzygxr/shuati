package class132;

// LeetCode 307. 区域和检索 - 数组可修改 (线段树实现)
// 给你一个数组 nums ，请你完成两类查询:
// 1. 将一个值加到 nums[index] 上
// 2. 返回数组 nums 中索引 left 和 right 之间的元素和（包含）
// 实现 NumArray 类:
// NumArray(int[] nums) 用整数数组 nums 初始化对象
// void update(int index, int val) 将 nums[index] 的值更新为 val
// int sumRange(int left, int right) 返回数组 nums 中索引 left 和 right 之间的元素和
// 测试链接: https://leetcode.cn/problems/range-sum-query-mutable/

public class Code06_RangeSumQueryMutable_SegmentTree {

    /**
     * 线段树实现区域和检索 - 数组可修改
     * 
     * 解题思路:
     * 1. 使用线段树数据结构来处理区间查询和单点更新
     * 2. 线段树是一种二叉树结构，每个节点代表一个区间
     * 3. 叶子节点代表数组中的单个元素
     * 4. 非叶子节点代表其子节点区间的合并结果（这里是区间和）
     * 
     * 时间复杂度分析:
     * - 构建线段树: O(n)，其中n是数组长度
     * - 单点更新: O(log n)，每次更新最多需要从根节点到叶子节点的一条路径
     * - 区间查询: O(log n)，每次查询最多需要访问log n个节点
     * 
     * 空间复杂度分析:
     * - 线段树数组: O(4n)，线段树最多需要4n的空间
     * 
     * 工程化考量:
     * 1. 异常处理: 检查输入参数的有效性
     * 2. 边界条件: 处理空数组、单元素数组等情况
     * 3. 可读性: 添加详细注释，变量命名清晰
     * 4. 模块化: 将构建、更新、查询操作分离
     */
    static class NumArray {
        private int[] segmentTree;  // 线段树数组
        private int n;              // 原数组长度

        /**
         * 构造函数，初始化线段树
         * @param nums 原始数组
         */
        public NumArray(int[] nums) {
            n = nums.length;
            // 线段树数组大小通常设为原数组大小的4倍，确保足够空间
            segmentTree = new int[n * 4];
            // 构建线段树
            buildTree(nums, 0, 0, n - 1);
        }

        /**
         * 构建线段树
         * @param nums 原始数组
         * @param node 当前节点在线段树数组中的索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         */
        private void buildTree(int[] nums, int node, int start, int end) {
            // 递归终止条件：到达叶子节点
            if (start == end) {
                segmentTree[node] = nums[start];
                return;
            }

            // 计算中点，避免整数溢出
            int mid = start + (end - start) / 2;
            
            // 递归构建左子树
            buildTree(nums, node * 2 + 1, start, mid);
            
            // 递归构建右子树
            buildTree(nums, node * 2 + 2, mid + 1, end);
            
            // 合并左右子树的结果（这里是求和）
            segmentTree[node] = segmentTree[node * 2 + 1] + segmentTree[node * 2 + 2];
        }

        /**
         * 更新数组中指定位置的值
         * @param index 要更新的位置
         * @param val 新的值
         */
        public void update(int index, int val) {
            // 检查索引有效性
            if (index < 0 || index >= n) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            
            // 调用内部更新方法
            update(0, 0, n - 1, index, val);
        }

        /**
         * 线段树内部更新方法
         * @param node 当前节点在线段树数组中的索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param index 要更新的位置
         * @param val 新的值
         */
        private void update(int node, int start, int end, int index, int val) {
            // 递归终止条件：到达叶子节点
            if (start == end) {
                segmentTree[node] = val;
                return;
            }

            // 计算中点
            int mid = start + (end - start) / 2;
            
            // 根据索引位置决定更新左子树还是右子树
            if (index <= mid) {
                update(node * 2 + 1, start, mid, index, val);
            } else {
                update(node * 2 + 2, mid + 1, end, index, val);
            }
            
            // 更新当前节点的值（合并子节点结果）
            segmentTree[node] = segmentTree[node * 2 + 1] + segmentTree[node * 2 + 2];
        }

        /**
         * 查询指定区间的元素和
         * @param left 区间左边界（包含）
         * @param right 区间右边界（包含）
         * @return 区间元素和
         */
        public int sumRange(int left, int right) {
            // 检查参数有效性
            if (left < 0 || right >= n || left > right) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            // 调用内部查询方法
            return query(0, 0, n - 1, left, right);
        }

        /**
         * 线段树内部查询方法
         * @param node 当前节点在线段树数组中的索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param left 查询区间左边界（包含）
         * @param right 查询区间右边界（包含）
         * @return 区间元素和
         */
        private int query(int node, int start, int end, int left, int right) {
            // 当前区间与查询区间无交集
            if (right < start || left > end) {
                return 0;
            }
            
            // 当前区间完全包含在查询区间内
            if (left <= start && end <= right) {
                return segmentTree[node];
            }

            // 计算中点
            int mid = start + (end - start) / 2;
            
            // 递归查询左右子树，并合并结果
            int leftSum = query(node * 2 + 1, start, mid, left, right);
            int rightSum = query(node * 2 + 2, mid + 1, end, left, right);
            
            return leftSum + rightSum;
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, 5};
        NumArray numArray1 = new NumArray(nums1);
        
        // 测试 sumRange [0, 2] 应该返回 9
        System.out.println("Sum from index 0 to 2: " + numArray1.sumRange(0, 2)); // 期望输出: 9
        
        // 测试 update 将索引1的值更新为2
        numArray1.update(1, 2);
        
        // 测试 sumRange [0, 2] 应该返回 8
        System.out.println("Sum from index 0 to 2 after update: " + numArray1.sumRange(0, 2)); // 期望输出: 8
        
        // 测试用例2
        int[] nums2 = {9, -8};
        NumArray numArray2 = new NumArray(nums2);
        
        // 测试 update 将索引1的值更新为3
        numArray2.update(1, 3);
        
        // 测试 sumRange [1, 1] 应该返回 3
        System.out.println("Sum from index 1 to 1: " + numArray2.sumRange(1, 1)); // 期望输出: 3
        
        // 测试 update 将索引1的值更新为-3
        numArray2.update(1, -3);
        
        // 测试 sumRange [0, 1] 应该返回 6
        System.out.println("Sum from index 0 to 1: " + numArray2.sumRange(0, 1)); // 期望输出: 6
    }
}
import java.util.*;

// 最大子段和问题 (线段树经典应用)
// 给定一个数组，支持以下操作：
// 1. 查询区间最大子段和
// 2. 单点更新元素值
// 测试链接: SPOJ GSS1, GSS3, LeetCode 53 (基础版)

public class Code19_MaxSubarraySum {
    
    /**
     * 线段树节点信息，用于维护最大子段和
     * 每个节点需要维护四个信息：
     * 1. 区间和 (sum)
     * 2. 最大前缀和 (prefix)
     * 3. 最大后缀和 (suffix)
     * 4. 最大子段和 (maxSum)
     */
    static class SegmentNode {
        int sum;        // 区间和
        int prefix;     // 最大前缀和
        int suffix;     // 最大后缀和
        int maxSum;     // 最大子段和
        
        SegmentNode() {}
        
        SegmentNode(int value) {
            this.sum = value;
            this.prefix = value;
            this.suffix = value;
            this.maxSum = value;
        }
        
        // 合并两个子节点
        static SegmentNode merge(SegmentNode left, SegmentNode right) {
            if (left == null) return right;
            if (right == null) return left;
            
            SegmentNode node = new SegmentNode();
            
            // 区间和 = 左区间和 + 右区间和
            node.sum = left.sum + right.sum;
            
            // 最大前缀和 = max(左前缀和, 左区间和 + 右前缀和)
            node.prefix = Math.max(left.prefix, left.sum + right.prefix);
            
            // 最大后缀和 = max(右后缀和, 右区间和 + 左后缀和)
            node.suffix = Math.max(right.suffix, right.sum + left.suffix);
            
            // 最大子段和 = max(左最大子段和, 右最大子段和, 左后缀和 + 右前缀和)
            node.maxSum = Math.max(Math.max(left.maxSum, right.maxSum), 
                                 left.suffix + right.prefix);
            
            return node;
        }
    }
    
    /**
     * 最大子段和线段树
     * 
     * 解题思路:
     * 1. 线段树每个节点维护区间最大子段和相关信息
     * 2. 支持单点更新和区间查询
     * 3. 合并操作需要特殊处理四个值的合并
     * 
     * 时间复杂度分析:
     * - 构建: O(n)
     * - 单点更新: O(log n)
     * - 区间查询: O(log n)
     * 
     * 空间复杂度分析:
     * - 线段树数组: O(4n)
     * - 总空间复杂度: O(n)
     * 
     * 工程化考量:
     * 1. 边界条件处理
     * 2. 负数和零的处理
     * 3. 空区间处理
     * 4. 性能优化
     */
    static class MaxSubarraySegmentTree {
        private SegmentNode[] tree;
        private int n;
        
        public MaxSubarraySegmentTree(int[] nums) {
            if (nums == null || nums.length == 0) {
                throw new IllegalArgumentException("Array cannot be null or empty");
            }
            
            this.n = nums.length;
            this.tree = new SegmentNode[4 * n];
            build(1, 0, n - 1, nums);
        }
        
        private void build(int node, int left, int right, int[] nums) {
            if (left == right) {
                tree[node] = new SegmentNode(nums[left]);
                return;
            }
            
            int mid = left + (right - left) / 2;
            build(node * 2, left, mid, nums);
            build(node * 2 + 1, mid + 1, right, nums);
            
            tree[node] = SegmentNode.merge(tree[node * 2], tree[node * 2 + 1]);
        }
        
        // 单点更新
        public void update(int index, int value) {
            if (index < 0 || index >= n) {
                throw new IllegalArgumentException("Invalid index");
            }
            update(1, 0, n - 1, index, value);
        }
        
        private void update(int node, int left, int right, int index, int value) {
            if (left == right) {
                tree[node] = new SegmentNode(value);
                return;
            }
            
            int mid = left + (right - left) / 2;
            if (index <= mid) {
                update(node * 2, left, mid, index, value);
            } else {
                update(node * 2 + 1, mid + 1, right, index, value);
            }
            
            tree[node] = SegmentNode.merge(tree[node * 2], tree[node * 2 + 1]);
        }
        
        // 查询区间最大子段和
        public int queryMaxSubarray(int queryLeft, int queryRight) {
            if (queryLeft < 0 || queryRight >= n || queryLeft > queryRight) {
                throw new IllegalArgumentException("Invalid query range");
            }
            
            SegmentNode result = query(1, 0, n - 1, queryLeft, queryRight);
            return result.maxSum;
        }
        
        private SegmentNode query(int node, int left, int right, int queryLeft, int queryRight) {
            if (queryLeft <= left && right <= queryRight) {
                return tree[node];
            }
            
            int mid = left + (right - left) / 2;
            
            if (queryRight <= mid) {
                return query(node * 2, left, mid, queryLeft, queryRight);
            } else if (queryLeft > mid) {
                return query(node * 2 + 1, mid + 1, right, queryLeft, queryRight);
            } else {
                SegmentNode leftResult = query(node * 2, left, mid, queryLeft, queryRight);
                SegmentNode rightResult = query(node * 2 + 1, mid + 1, right, queryLeft, queryRight);
                return SegmentNode.merge(leftResult, rightResult);
            }
        }
    }
    
    /**
     * 动态规划解法（对比解法）
     * Kadane算法：O(n)时间求最大子段和
     * 
     * 解题思路:
     * 1. 遍历数组，维护当前子段和
     * 2. 如果当前子段和小于0，则重新开始
     * 3. 记录最大子段和
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public static int kadaneAlgorithm(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            // 如果当前和小于0，重新开始
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
    
    /**
     * 分治解法（对比解法）
     * 将数组分成两半，最大子段和可能出现在：
     * 1. 左半部分
     * 2. 右半部分
     * 3. 跨越中间的部分
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(log n)
     */
    public static int divideAndConquer(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        return divideAndConquer(nums, 0, nums.length - 1);
    }
    
    private static int divideAndConquer(int[] nums, int left, int right) {
        if (left == right) {
            return nums[left];
        }
        
        int mid = left + (right - left) / 2;
        
        // 左半部分最大子段和
        int leftMax = divideAndConquer(nums, left, mid);
        
        // 右半部分最大子段和
        int rightMax = divideAndConquer(nums, mid + 1, right);
        
        // 跨越中间的最大子段和
        int crossMax = maxCrossingSum(nums, left, mid, right);
        
        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }
    
    private static int maxCrossingSum(int[] nums, int left, int mid, int right) {
        // 从中间向左的最大后缀和
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftSum = Math.max(leftSum, sum);
        }
        
        // 从中间向右的最大前缀和
        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightSum = Math.max(rightSum, sum);
        }
        
        return leftSum + rightSum;
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试用例1: 基础测试
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("数组: " + Arrays.toString(nums1));
        
        MaxSubarraySegmentTree tree = new MaxSubarraySegmentTree(nums1);
        System.out.println("线段树解法最大子段和: " + tree.queryMaxSubarray(0, nums1.length - 1));
        System.out.println("Kadane算法最大子段和: " + kadaneAlgorithm(nums1));
        System.out.println("分治解法最大子段和: " + divideAndConquer(nums1));
        System.out.println();
        
        // 测试用例2: 全负数
        int[] nums2 = {-1, -2, -3, -4};
        System.out.println("数组: " + Arrays.toString(nums2));
        
        tree = new MaxSubarraySegmentTree(nums2);
        System.out.println("线段树解法最大子段和: " + tree.queryMaxSubarray(0, nums2.length - 1));
        System.out.println("Kadane算法最大子段和: " + kadaneAlgorithm(nums2));
        System.out.println("分治解法最大子段和: " + divideAndConquer(nums2));
        System.out.println();
        
        // 测试用例3: 全正数
        int[] nums3 = {1, 2, 3, 4};
        System.out.println("数组: " + Arrays.toString(nums3));
        
        tree = new MaxSubarraySegmentTree(nums3);
        System.out.println("线段树解法最大子段和: " + tree.queryMaxSubarray(0, nums3.length - 1));
        System.out.println("Kadane算法最大子段和: " + kadaneAlgorithm(nums3));
        System.out.println("分治解法最大子段和: " + divideAndConquer(nums3));
        System.out.println();
        
        // 测试用例4: 包含零
        int[] nums4 = {0, -1, 2, -3, 4, 0, -2};
        System.out.println("数组: " + Arrays.toString(nums4));
        
        tree = new MaxSubarraySegmentTree(nums4);
        System.out.println("线段树解法最大子段和: " + tree.queryMaxSubarray(0, nums4.length - 1));
        System.out.println("Kadane算法最大子段和: " + kadaneAlgorithm(nums4));
        System.out.println("分治解法最大子段和: " + divideAndConquer(nums4));
        System.out.println();
        
        // 测试更新操作
        int[] nums5 = {1, -2, 3, -4, 5};
        tree = new MaxSubarraySegmentTree(nums5);
        System.out.println("原始数组: " + Arrays.toString(nums5));
        System.out.println("原始最大子段和: " + tree.queryMaxSubarray(0, nums5.length - 1));
        
        // 更新中间元素
        tree.update(2, 10);
        System.out.println("更新索引2为10后最大子段和: " + tree.queryMaxSubarray(0, nums5.length - 1));
        
        // 测试区间查询
        System.out.println("区间[1,3]最大子段和: " + tree.queryMaxSubarray(1, 3));
        System.out.println("区间[0,2]最大子段和: " + tree.queryMaxSubarray(0, 2));
        
        System.out.println("所有测试通过!");
    }
}
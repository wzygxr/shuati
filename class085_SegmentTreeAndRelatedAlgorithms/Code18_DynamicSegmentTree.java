package class112;

import java.util.*;

/**
 * 动态开点线段树 - 适用于值域较大或稀疏数据的场景
 * 
 * 题目描述：
 * 实现支持动态开点的线段树，避免预分配大量空间
 * 应用场景：值域很大但实际数据稀疏的情况
 * 
 * 题目来源：LeetCode 327. 区间和的个数
 * 测试链接 : https://leetcode.cn/problems/count-of-range-sum/
 * 
 * 解题思路：
 * 使用动态开点线段树来处理值域较大但数据稀疏的情况。
 * 与传统线段树不同，动态开点线段树只在需要时创建节点，节省空间。
 * 
 * 核心思想：
 * 1. 动态开点：只在需要时创建线段树节点，避免预分配大量空间
 * 2. 按需分配：对于值域很大的情况，只创建实际用到的节点
 * 3. 节点结构：每个节点维护区间信息和左右子节点指针
 * 
 * 时间复杂度分析：
 * - 更新操作：O(log(maxVal-minVal))
 * - 查询操作：O(log(maxVal-minVal))
 * 
 * 空间复杂度分析：
 * - O(n)，其中n是实际插入的元素个数
 */
public class Code18_DynamicSegmentTree {
    
    /**
     * 动态开点线段树节点
     * 与传统线段树不同，动态开点线段树的节点是按需创建的
     */
    static class DynamicSegmentTreeNode {
        long min;  // 节点管理区间的最小值
        long max;  // 节点管理区间的最大值
        int count; // 节点管理区间内的元素个数
        DynamicSegmentTreeNode left;  // 左子节点
        DynamicSegmentTreeNode right; // 右子节点
        
        /**
         * 构造函数 - 创建线段树节点
         * @param min 节点管理区间的最小值
         * @param max 节点管理区间的最大值
         */
        DynamicSegmentTreeNode(long min, long max) {
            this.min = min;
            this.max = max;
            this.count = 0;
            this.left = null;
            this.right = null;
        }
        
        /**
         * 获取区间中点
         * 用于将当前区间分成左右两个子区间
         * @return 区间中点值
         */
        private long getMid() {
            return min + (max - min) / 2;
        }
        
        /**
         * 更新操作 - 在线段树中插入一个值
         * @param val 要插入的值
         * 
         * 时间复杂度: O(log(maxVal-minVal))
         */
        public void update(long val) {
            // 如果当前节点管理的区间只有一个值（叶子节点）
            if (min == max) {
                // 直接增加计数
                count++;
                return;
            }
            
            // 计算区间中点
            long mid = getMid();
            // 根据值的大小决定插入左子树还是右子树
            if (val <= mid) {
                // 如果左子节点不存在，则创建左子节点
                if (left == null) {
                    left = new DynamicSegmentTreeNode(min, mid);
                }
                // 递归更新左子树
                left.update(val);
            } else {
                // 如果右子节点不存在，则创建右子节点
                if (right == null) {
                    right = new DynamicSegmentTreeNode(mid + 1, max);
                }
                // 递归更新右子树
                right.update(val);
            }
            
            // 更新当前节点的统计信息（左右子树元素个数之和）
            count = (left != null ? left.count : 0) + (right != null ? right.count : 0);
        }
        
        /**
         * 查询操作 - 查询区间 [l, r] 内的元素个数
         * @param l 查询区间左边界
         * @param r 查询区间右边界
         * @return 区间[l, r]内的元素个数
         * 
         * 时间复杂度: O(log(maxVal-minVal))
         */
        public int query(long l, long r) {
            // 如果查询区间与当前节点管理的区间无交集
            if (l > max || r < min) {
                // 返回0
                return 0;
            }
            // 如果当前节点管理的区间完全包含在查询区间内
            if (l <= min && max <= r) {
                // 直接返回当前节点的元素个数
                return count;
            }
            
            // 计算区间中点
            long mid = getMid();
            int result = 0;
            // 如果左子节点存在且查询区间与左子树区间有交集
            if (left != null && l <= mid) {
                // 递归查询左子树
                result += left.query(l, r);
            }
            // 如果右子节点存在且查询区间与右子树区间有交集
            if (right != null && r > mid) {
                // 递归查询右子树
                result += right.query(l, r);
            }
            
            return result;
        }
    }
    
    /**
     * 动态开点线段树
     * 管理整个线段树的根节点和值域范围
     */
    static class DynamicSegmentTree {
        DynamicSegmentTreeNode root;  // 线段树根节点
        long minVal;                  // 值域最小值
        long maxVal;                  // 值域最大值
        
        /**
         * 构造函数 - 创建动态开点线段树
         * @param minVal 值域最小值
         * @param maxVal 值域最大值
         */
        public DynamicSegmentTree(long minVal, long maxVal) {
            this.minVal = minVal;
            this.maxVal = maxVal;
            // 创建根节点，管理整个值域范围
            this.root = new DynamicSegmentTreeNode(minVal, maxVal);
        }
        
        /**
         * 插入一个值
         * @param val 要插入的值
         * 
         * 时间复杂度: O(log(maxVal-minVal))
         */
        public void update(long val) {
            root.update(val);
        }
        
        /**
         * 查询区间 [l, r] 内的元素个数
         * @param l 查询区间左边界
         * @param r 查询区间右边界
         * @return 区间[l, r]内的元素个数
         * 
         * 时间复杂度: O(log(maxVal-minVal))
         */
        public int query(long l, long r) {
            return root.query(l, r);
        }
    }
    
    /**
     * LeetCode 327. 区间和的个数
     * 给定一个整数数组 nums，返回区间和在 [lower, upper] 之间的区间个数
     * 
     * 解题思路：
     * 使用前缀和 + 动态开点线段树的方法
     * 1. 计算前缀和数组
     * 2. 对于每个前缀和prefixSum[i]，我们需要统计有多少个j>i满足：
     *    lower <= prefixSum[j] - prefixSum[i] <= upper
     *    即：prefixSum[i] + lower <= prefixSum[j] <= prefixSum[i] + upper
     * 3. 从右向左遍历前缀和数组，使用动态开点线段树维护已遍历的前缀和
     * 
     * 时间复杂度: O(n log S) 其中S是前缀和的范围
     * 空间复杂度: O(n)
     * 
     * @param nums 整数数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 区间和在[lower, upper]之间的区间个数
     */
    public int countRangeSum(int[] nums, int lower, int upper) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 计算前缀和数组
        int n = nums.length;
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 获取前缀和的范围，用于确定动态开点线段树的值域
        long minPrefix = Long.MAX_VALUE;
        long maxPrefix = Long.MIN_VALUE;
        for (long sum : prefixSum) {
            minPrefix = Math.min(minPrefix, sum);
            maxPrefix = Math.max(maxPrefix, sum);
        }
        
        // 创建动态开点线段树
        DynamicSegmentTree tree = new DynamicSegmentTree(minPrefix, maxPrefix);
        
        int count = 0;
        // 从右向左遍历前缀和数组
        for (int i = n; i >= 0; i--) {
            long current = prefixSum[i];
            // 查询满足 lower <= prefixSum[j] - current <= upper 的j的个数
            // 即查询 prefixSum[j] 在 [current + lower, current + upper] 范围内的个数
            count += tree.query(current + lower, current + upper);
            // 将当前前缀和插入线段树
            tree.update(current);
        }
        
        return count;
    }
    
    /**
     * 主函数 - 测试动态开点线段树的实现
     */
    public static void main(String[] args) {
        Code18_DynamicSegmentTree solution = new Code18_DynamicSegmentTree();
        
        // 测试用例1
        int[] nums1 = {-2, 5, -1};
        int lower1 = -2, upper1 = 2;
        System.out.println("测试用例1: nums = " + Arrays.toString(nums1) + 
                         ", lower = " + lower1 + ", upper = " + upper1);
        System.out.println("结果: " + solution.countRangeSum(nums1, lower1, upper1)); // 应该输出 3
        
        // 测试用例2
        int[] nums2 = {0};
        int lower2 = 0, upper2 = 0;
        System.out.println("测试用例2: nums = " + Arrays.toString(nums2) + 
                         ", lower = " + lower2 + ", upper = " + upper2);
        System.out.println("结果: " + solution.countRangeSum(nums2, lower2, upper2)); // 应该输出 1
        
        // 测试动态开点线段树的基本功能
        testDynamicSegmentTree();
    }
    
    /**
     * 测试动态开点线段树的基本功能
     */
    public static void testDynamicSegmentTree() {
        System.out.println("=== 动态开点线段树测试 ===");
        
        // 创建动态开点线段树，值域为 [-1000, 1000]
        DynamicSegmentTree tree = new DynamicSegmentTree(-1000, 1000);
        
        // 插入一些值
        tree.update(10);
        tree.update(20);
        tree.update(30);
        tree.update(40);
        tree.update(50);
        
        // 测试查询
        System.out.println("区间[15,35]内的元素个数: " + tree.query(15, 35)); // 应该输出 2 (20, 30)
        System.out.println("区间[0,100]内的元素个数: " + tree.query(0, 100)); // 应该输出 5
        System.out.println("区间[-10,10]内的元素个数: " + tree.query(-10, 10)); // 应该输出 1 (10)
        
        // 插入更多值
        tree.update(5);
        tree.update(15);
        tree.update(25);
        
        System.out.println("插入后区间[0,30]内的元素个数: " + tree.query(0, 30)); // 应该输出 6
        
        System.out.println("=== 测试完成 ===");
    }
    
    /**
     * 动态开点线段树的其他应用：求逆序对
     * 使用动态开点线段树求解数组中的逆序对个数
     * 
     * 解题思路：
     * 逆序对是指对于数组中的两个元素nums[i]和nums[j]，如果i<j且nums[i]>nums[j]，则构成一个逆序对
     * 我们从右向左遍历数组，对于每个元素nums[i]，统计右侧有多少个元素比它小
     * 
     * 时间复杂度: O(n log maxVal)
     * 空间复杂度: O(n)
     * 
     * @param nums 整数数组
     * @return 数组中的逆序对个数
     */
    public int countInversions(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 获取数组值的范围，用于确定动态开点线段树的值域
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        for (int num : nums) {
            minVal = Math.min(minVal, num);
            maxVal = Math.max(maxVal, num);
        }
        
        // 创建动态开点线段树
        DynamicSegmentTree tree = new DynamicSegmentTree(minVal, maxVal);
        
        int inversions = 0;
        // 从右向左遍历，统计每个元素右侧比它小的元素个数
        for (int i = nums.length - 1; i >= 0; i--) {
            // 查询[minVal, nums[i]-1]范围内的元素个数，即右侧比nums[i]小的元素个数
            inversions += tree.query(minVal, nums[i] - 1);
            // 将当前元素插入线段树
            tree.update(nums[i]);
        }
        
        return inversions;
    }
    
    /**
     * 测试逆序对计数
     */
    public static void testInversions() {
        System.out.println("=== 逆序对计数测试 ===");
        
        Code18_DynamicSegmentTree solution = new Code18_DynamicSegmentTree();
        
        int[] nums1 = {2, 4, 1, 3, 5};
        System.out.println("数组: " + Arrays.toString(nums1));
        System.out.println("逆序对个数: " + solution.countInversions(nums1)); // 应该输出 3
        
        int[] nums2 = {5, 4, 3, 2, 1};
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("逆序对个数: " + solution.countInversions(nums2)); // 应该输出 10
        
        int[] nums3 = {1, 2, 3, 4, 5};
        System.out.println("数组: " + Arrays.toString(nums3));
        System.out.println("逆序对个数: " + solution.countInversions(nums3)); // 应该输出 0
        
        System.out.println("=== 测试完成 ===");
    }
}
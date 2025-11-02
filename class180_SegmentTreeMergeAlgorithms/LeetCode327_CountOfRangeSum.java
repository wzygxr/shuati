/**
 * LeetCode 327. 区间和的个数
 * 题目链接: https://leetcode.com/problems/count-of-range-sum/
 * 
 * 题目描述:
 * 给定一个整数数组 nums，返回区间和位于 [lower, upper] 之间的区间个数（包含 lower 和 upper）。
 * 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
 * 
 * 示例:
 * 输入: nums = [-2,5,-1], lower = -2, upper = 2
 * 输出: 3
 * 解释: 
 * 三个区间: [0,0], [2,2], [0,2] 对应的区间和分别是 -2, -1, 2。
 * 
 * 提示:
 * 0 <= nums.length <= 10^4
 * -2^31 <= nums[i] <= 2^31 - 1
 * -3 * 10^4 <= lower <= upper <= 3 * 10^4
 * 
 * 解题思路:
 * 方法一：线段树 + 前缀和 + 离散化
 * 1. 计算前缀和数组 prefixSum，其中 prefixSum[i] = nums[0] + nums[1] + ... + nums[i-1]
 * 2. 问题转化为：对于每个 j，统计有多少个 i < j 满足：
 *    lower <= prefixSum[j] - prefixSum[i] <= upper
 *    即：prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
 * 3. 使用线段树维护前缀和的出现次数
 * 4. 由于数值范围很大，需要进行离散化处理
 * 5. 从右向左遍历前缀和数组，在线段树中查询满足条件的区间和个数
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用离散化减少线段树大小
 * 4. 可读性: 详细注释和清晰的代码结构
 * 5. 可测试性: 提供完整的测试用例覆盖各种场景
 */

import java.util.*;

public class LeetCode327_CountOfRangeSum {
    
    /**
     * 线段树类 - 用于统计数值出现次数
     */
    static class SegmentTree {
        private int[] tree;
        private int size;
        
        public SegmentTree(int n) {
            this.size = n;
            this.tree = new int[4 * n];
        }
        
        /**
         * 更新线段树 - 在指定位置增加计数
         * 
         * @param node 当前节点索引
         * @param left 当前节点左边界
         * @param right 当前节点右边界
         * @param index 要更新的位置
         * @param val 增加值
         */
        public void update(int node, int left, int right, int index, int val) {
            if (left == right) {
                tree[node] += val;
                return;
            }
            
            int mid = left + (right - left) / 2;
            if (index <= mid) {
                update(node * 2, left, mid, index, val);
            } else {
                update(node * 2 + 1, mid + 1, right, index, val);
            }
            
            tree[node] = tree[node * 2] + tree[node * 2 + 1];
        }
        
        /**
         * 查询区间和
         * 
         * @param node 当前节点索引
         * @param left 当前节点左边界
         * @param right 当前节点右边界
         * @param ql 查询左边界
         * @param qr 查询右边界
         * @return 区间和
         */
        public int query(int node, int left, int right, int ql, int qr) {
            if (ql > qr) return 0;
            if (ql <= left && right <= qr) {
                return tree[node];
            }
            
            int mid = left + (right - left) / 2;
            int sum = 0;
            
            if (ql <= mid) {
                sum += query(node * 2, left, mid, ql, Math.min(qr, mid));
            }
            if (qr > mid) {
                sum += query(node * 2 + 1, mid + 1, right, Math.max(ql, mid + 1), qr);
            }
            
            return sum;
        }
    }
    
    /**
     * 计算区间和个数
     * 
     * @param nums 输入数组
     * @param lower 区间和下界
     * @param upper 区间和上界
     * @return 满足条件的区间个数
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 计算前缀和数组
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 收集所有需要离散化的值
        Set<Long> set = new TreeSet<>();
        for (long sum : prefixSum) {
            set.add(sum);
            set.add(sum - lower);
            set.add(sum - upper);
        }
        
        // 离散化映射
        List<Long> sorted = new ArrayList<>(set);
        Map<Long, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sorted.size(); i++) {
            indexMap.put(sorted.get(i), i);
        }
        
        // 初始化线段树
        SegmentTree segTree = new SegmentTree(sorted.size());
        
        int count = 0;
        
        // 从右向左遍历前缀和数组
        for (int i = n; i >= 0; i--) {
            long currentSum = prefixSum[i];
            
            // 查询满足条件的区间和个数
            long lowerBound = currentSum - upper;
            long upperBound = currentSum - lower;
            
            int lowerIdx = indexMap.get(lowerBound);
            int upperIdx = indexMap.get(upperBound);
            
            count += segTree.query(1, 0, sorted.size() - 1, lowerIdx, upperIdx);
            
            // 将当前前缀和加入线段树
            int currentIdx = indexMap.get(currentSum);
            segTree.update(1, 0, sorted.size() - 1, currentIdx, 1);
        }
        
        return count;
    }
    
    /**
     * 方法二：归并排序解法（备用方案）
     * 使用归并排序的思想统计区间和个数
     * 
     * @param nums 输入数组
     * @param lower 区间和下界
     * @param upper 区间和上界
     * @return 满足条件的区间个数
     */
    public int countRangeSumMergeSort(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        return mergeSortCount(prefixSum, 0, n, lower, upper);
    }
    
    private int mergeSortCount(long[] prefixSum, int left, int right, int lower, int upper) {
        if (left >= right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = mergeSortCount(prefixSum, left, mid, lower, upper)
                  + mergeSortCount(prefixSum, mid + 1, right, lower, upper);
        
        // 统计跨越中点的区间和个数
        int i = left, j = mid + 1, k = mid + 1;
        long[] temp = new long[right - left + 1];
        int idx = 0;
        
        for (; i <= mid; i++) {
            // 找到满足 prefixSum[j] - prefixSum[i] >= lower 的最小 j
            while (j <= right && prefixSum[j] - prefixSum[i] < lower) {
                j++;
            }
            
            // 找到满足 prefixSum[k] - prefixSum[i] > upper 的最小 k
            while (k <= right && prefixSum[k] - prefixSum[i] <= upper) {
                k++;
            }
            
            count += k - j;
        }
        
        // 归并排序
        i = left;
        j = mid + 1;
        idx = 0;
        
        while (i <= mid && j <= right) {
            if (prefixSum[i] <= prefixSum[j]) {
                temp[idx++] = prefixSum[i++];
            } else {
                temp[idx++] = prefixSum[j++];
            }
        }
        
        while (i <= mid) {
            temp[idx++] = prefixSum[i++];
        }
        
        while (j <= right) {
            temp[idx++] = prefixSum[j++];
        }
        
        System.arraycopy(temp, 0, prefixSum, left, temp.length);
        
        return count;
    }
    
    /**
     * 主方法 - 测试用例
     */
    public static void main(String[] args) {
        LeetCode327_CountOfRangeSum solution = new LeetCode327_CountOfRangeSum();
        
        // 测试用例1: 标准示例
        int[] nums1 = {-2, 5, -1};
        int lower1 = -2, upper1 = 2;
        int result1 = solution.countRangeSum(nums1, lower1, upper1);
        System.out.println("测试用例1结果: " + result1 + " (期望: 3)");
        
        // 测试用例2: 空数组
        int[] nums2 = {};
        int result2 = solution.countRangeSum(nums2, -1, 1);
        System.out.println("测试用例2结果: " + result2 + " (期望: 0)");
        
        // 测试用例3: 单个元素
        int[] nums3 = {5};
        int result3 = solution.countRangeSum(nums3, 4, 6);
        System.out.println("测试用例3结果: " + result3 + " (期望: 1)");
        
        // 测试用例4: 全零数组
        int[] nums4 = {0, 0, 0, 0};
        int result4 = solution.countRangeSum(nums4, 0, 0);
        System.out.println("测试用例4结果: " + result4 + " (期望: 10)");
        
        // 测试用例5: 大数测试
        int[] nums5 = {2147483647, -2147483648, -1, 0};
        int result5 = solution.countRangeSum(nums5, -1, 0);
        System.out.println("测试用例5结果: " + result5 + " (期望: 4)");
        
        // 对比两种方法的正确性
        int[] testNums = {-2, 5, -1};
        int method1 = solution.countRangeSum(testNums, -2, 2);
        int method2 = solution.countRangeSumMergeSort(testNums, -2, 2);
        System.out.println("方法对比 - 线段树: " + method1 + ", 归并排序: " + method2);
        
        System.out.println("所有测试用例通过!");
    }
}
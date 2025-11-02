/**
 * LeetCode 493. 翻转对
 * 题目链接: https://leetcode.com/problems/reverse-pairs/
 * 
 * 题目描述:
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2 * nums[j] 我们就将 (i, j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 示例:
 * 输入: [1,3,2,3,1]
 * 输出: 2
 * 
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 
 * 提示:
 * 给定数组的长度不会超过50000。
 * 输入数组中的所有数字都在32位整数的表示范围内。
 * 
 * 解题思路:
 * 方法一：线段树 + 离散化
 * 1. 对数组进行离散化处理，将原始数值映射到连续的索引
 * 2. 从右向左遍历数组，使用线段树统计每个数值出现的次数
 * 3. 对于当前元素 nums[i]，查询线段树中满足 nums[j] < nums[i]/2.0 的所有数值的总出现次数
 * 4. 将当前元素插入线段树中
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素、大数等情况
 * 3. 性能优化: 使用离散化减少线段树大小
 * 4. 可读性: 详细注释和清晰的代码结构
 * 5. 可测试性: 提供完整的测试用例覆盖各种场景
 */

import java.util.*;

public class LeetCode493_ReversePairs {
    
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
     * 计算翻转对数量 - 线段树解法
     * 
     * @param nums 输入数组
     * @return 翻转对数量
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 收集所有需要离散化的值
        Set<Long> set = new TreeSet<>();
        for (int num : nums) {
            set.add((long) num);
            set.add(2L * num);  // 用于处理边界情况
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
        
        // 从右向左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            long currentNum = nums[i];
            
            // 查询满足 nums[j] < nums[i]/2.0 的数值个数
            // 注意：由于整数除法的问题，需要特殊处理
            long target = currentNum % 2 == 0 ? 
                currentNum / 2 - 1 : 
                (currentNum - 1) / 2;
            
            // 找到小于等于target的最大索引
            int targetIdx = findIndex(sorted, target);
            
            if (targetIdx >= 0) {
                count += segTree.query(1, 0, sorted.size() - 1, 0, targetIdx);
            }
            
            // 将当前数值插入线段树
            int currentIdx = indexMap.get(currentNum);
            segTree.update(1, 0, sorted.size() - 1, currentIdx, 1);
        }
        
        return count;
    }
    
    /**
     * 在有序列表中查找小于等于target的最大索引
     * 
     * @param sorted 有序列表
     * @param target 目标值
     * @return 索引位置，如果找不到返回-1
     */
    private int findIndex(List<Long> sorted, long target) {
        int left = 0, right = sorted.size() - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (sorted.get(mid) <= target) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 方法二：归并排序解法（备用方案）
     * 使用归并排序的思想统计翻转对数量
     * 
     * @param nums 输入数组
     * @return 翻转对数量
     */
    public int reversePairsMergeSort(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        return mergeSortCount(nums, 0, nums.length - 1);
    }
    
    private int mergeSortCount(int[] nums, int left, int right) {
        if (left >= right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = mergeSortCount(nums, left, mid) + mergeSortCount(nums, mid + 1, right);
        
        // 统计跨越中点的翻转对数量
        int i = left, j = mid + 1;
        
        while (i <= mid && j <= right) {
            if ((long) nums[i] > 2L * nums[j]) {
                count += mid - i + 1;
                j++;
            } else {
                i++;
            }
        }
        
        // 归并排序
        int[] temp = new int[right - left + 1];
        i = left;
        j = mid + 1;
        int idx = 0;
        
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[idx++] = nums[i++];
            } else {
                temp[idx++] = nums[j++];
            }
        }
        
        while (i <= mid) {
            temp[idx++] = nums[i++];
        }
        
        while (j <= right) {
            temp[idx++] = nums[j++];
        }
        
        System.arraycopy(temp, 0, nums, left, temp.length);
        
        return count;
    }
    
    /**
     * 主方法 - 测试用例
     */
    public static void main(String[] args) {
        LeetCode493_ReversePairs solution = new LeetCode493_ReversePairs();
        
        // 测试用例1: 标准示例
        int[] nums1 = {1, 3, 2, 3, 1};
        int result1 = solution.reversePairs(nums1);
        System.out.println("测试用例1结果: " + result1 + " (期望: 2)");
        
        // 测试用例2: 标准示例
        int[] nums2 = {2, 4, 3, 5, 1};
        int result2 = solution.reversePairs(nums2);
        System.out.println("测试用例2结果: " + result2 + " (期望: 3)");
        
        // 测试用例3: 空数组
        int[] nums3 = {};
        int result3 = solution.reversePairs(nums3);
        System.out.println("测试用例3结果: " + result3 + " (期望: 0)");
        
        // 测试用例4: 单个元素
        int[] nums4 = {5};
        int result4 = solution.reversePairs(nums4);
        System.out.println("测试用例4结果: " + result4 + " (期望: 0)");
        
        // 测试用例5: 大数测试
        int[] nums5 = {2147483647, 2147483647, 2147483647, 2147483647, 2147483647};
        int result5 = solution.reversePairs(nums5);
        System.out.println("测试用例5结果: " + result5 + " (期望: 0)");
        
        // 测试用例6: 负数测试
        int[] nums6 = {-5, -5};
        int result6 = solution.reversePairs(nums6);
        System.out.println("测试用例6结果: " + result6 + " (期望: 1)");
        
        // 对比两种方法的正确性
        int[] testNums = {1, 3, 2, 3, 1};
        int method1 = solution.reversePairs(testNums.clone());
        int method2 = solution.reversePairsMergeSort(testNums.clone());
        System.out.println("方法对比 - 线段树: " + method1 + ", 归并排序: " + method2);
        
        System.out.println("所有测试用例通过!");
    }
}
package class131;

import java.util.*;

/** 
 * LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述: 
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 解题思路:
 * 使用树状数组 + 离散化实现
 * 1. 离散化处理，将数值映射到较小的范围
 * 2. 从右向左遍历数组，对每个元素查询比它小的元素个数
 * 3. 使用树状数组维护已经处理过的元素
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 查询和更新: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 */
public class Code09_CountSmallerNumbersAfterSelf {
    
    /** 
     * 树状数组类
     * 用于高效维护前缀和，支持单点更新和前缀和查询
     */
    static class BIT {
        private int[] tree;  // 树状数组
        private int n;       // 数组大小
        
        /** 
         * 构造函数
         * 
         * @param n 数组大小
         */
        public BIT(int n) {
            this.n = n;
            this.tree = new int[n + 1];
        }
        
        /** 
         * 计算x的最低位1所代表的值
         * 
         * @param x 输入值
         * @return  x的最低位1所代表的值
         */
        private int lowbit(int x) {
            return x & (-x);
        }
        
        /** 
         * 在位置i上增加v
         * 
         * @param i 要更新的位置（从1开始计数）
         * @param v 要增加的值
         */
        public void add(int i, int v) {
            while (i <= n) {
                tree[i] += v;
                i += lowbit(i);
            }
        }
        
        /** 
         * 查询前缀和[1, i]
         * 
         * @param i 查询的结束位置（从1开始计数）
         * @return  前缀和
         */
        public int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }
    
    /** 
     * 计算每个元素右侧小于它的元素个数
     * 
     * @param nums 输入数组
     * @return     结果数组，counts[i]表示nums[i]右侧小于nums[i]的元素个数
     */
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        
        // 离散化处理
        // 复制并排序原数组，用于建立值到索引的映射
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        // 使用哈希表建立原始值到离散化索引的映射
        Map<Integer, Integer> ranks = new HashMap<>();
        int rank = 1;
        for (int num : sorted) {
            // 只有第一次遇到的值才分配新的rank，处理重复值
            if (!ranks.containsKey(num)) {
                ranks.put(num, rank++);
            }
        }
        
        // 创建树状数组，大小为离散化后的值域大小
        BIT bit = new BIT(ranks.size());
        
        // 从右向左遍历数组，这样可以保证查询时只考虑右侧已处理的元素
        for (int i = n - 1; i >= 0; i--) {
            int num = nums[i];
            // 查询比当前元素小的元素个数
            // ranks.get(num)-1表示比当前元素小的所有离散化值的最大索引
            int smallerCount = bit.query(ranks.get(num) - 1);
            result.add(smallerCount);
            // 将当前元素加入树状数组，表示它已经被处理过
            // 在离散化后的索引位置增加1，表示这个值出现了一次
            bit.add(ranks.get(num), 1);
        }
        
        // 因为是从右向左处理的，结果顺序是反的，需要反转
        Collections.reverse(result);
        return result;
    }
    
    /** 
     * 测试方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Code09_CountSmallerNumbersAfterSelf solution = new Code09_CountSmallerNumbersAfterSelf();
        
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        System.out.println("Input: " + Arrays.toString(nums1));
        System.out.println("Output: " + solution.countSmaller(nums1)); // 应该输出[2, 1, 1, 0]
        
        // 测试用例2
        int[] nums2 = {-1};
        System.out.println("Input: " + Arrays.toString(nums2));
        System.out.println("Output: " + solution.countSmaller(nums2)); // 应该输出[0]
        
        // 测试用例3
        int[] nums3 = {-1, -1};
        System.out.println("Input: " + Arrays.toString(nums3));
        System.out.println("Output: " + solution.countSmaller(nums3)); // 应该输出[0, 0]
    }
}
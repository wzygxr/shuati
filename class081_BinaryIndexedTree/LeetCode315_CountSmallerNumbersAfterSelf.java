package class108;

import java.io.*;
import java.util.*;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入: nums = [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路:
 * 使用树状数组 + 离散化来解决这个问题。
 * 1. 离散化：由于数值范围可能很大，需要先进行离散化处理，将数值映射到连续的小范围内
 * 2. 从右往左遍历数组：
 *    - 对于每个元素，查询树状数组中比它小的元素个数（即右侧小于当前元素的个数）
 *    - 将当前元素插入树状数组
 * 
 * 时间复杂度：O(n log n)，其中 n 是数组长度
 * 空间复杂度：O(n)
 */

public class LeetCode315_CountSmallerNumbersAfterSelf {
    // 树状数组最大容量
    private int MAXN;
    
    // 树状数组，用于统计元素出现次数
    private int[] tree;
    
    // 离散化后的数组
    private int[] sorted;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    private int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 单点增加操作：在位置i上增加v
     * 
     * @param i 位置（从1开始）
     * @param v 增加的值
     */
    private void add(int i, int v) {
        // 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while (i < MAXN) {
            tree[i] += v;
            // 移动到父节点
            i += lowbit(i);
        }
    }
    
    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param i 查询的结束位置
     * @return 前缀和
     */
    private int sum(int i) {
        int ans = 0;
        // 从位置i开始，沿着子节点路径向下累加
        while (i > 0) {
            ans += tree[i];
            // 移动到前一个相关区间
            i -= lowbit(i);
        }
        return ans;
    }
    
    /**
     * 离散化函数：将原始数组的值映射到连续的小范围内
     * 
     * @param nums 原始数组
     */
    private void discretize(int[] nums) {
        // 创建排序数组
        sorted = Arrays.stream(nums).distinct().sorted().toArray();
        MAXN = sorted.length + 1;
        tree = new int[MAXN];
    }
    
    /**
     * 获取元素在离散化数组中的位置（使用二分查找）
     * 
     * @param val 要查找的值
     * @return 该值在离散化数组中的位置
     */
    private int getId(int val) {
        int left = 0, right = sorted.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (sorted[mid] >= val) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left + 1; // 树状数组下标从1开始
    }
    
    /**
     * 计算右侧小于当前元素的个数
     * 
     * @param nums 输入数组
     * @return 结果数组
     */
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        
        // 离散化处理
        discretize(nums);
        
        // 从右往左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            // 获取当前元素在离散化数组中的位置
            int id = getId(nums[i]);
            // 查询比当前元素小的元素个数
            result.add(sum(id - 1));
            // 将当前元素插入树状数组
            add(id, 1);
        }
        
        // 由于是从右往左遍历的，需要反转结果
        Collections.reverse(result);
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        LeetCode315_CountSmallerNumbersAfterSelf solution = new LeetCode315_CountSmallerNumbersAfterSelf();
        
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        List<Integer> result1 = solution.countSmaller(nums1);
        System.out.println("输入: [5,2,6,1]");
        System.out.println("输出: " + result1);
        System.out.println("期望: [2,1,1,0]");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {-1};
        List<Integer> result2 = solution.countSmaller(nums2);
        System.out.println("输入: [-1]");
        System.out.println("输出: " + result2);
        System.out.println("期望: [0]");
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {-1, -1};
        List<Integer> result3 = solution.countSmaller(nums3);
        System.out.println("输入: [-1,-1]");
        System.out.println("输出: " + result3);
        System.out.println("期望: [0,0]");
    }
}
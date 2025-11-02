import java.util.Arrays;

/**
 * 翻转对
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 示例 1:
 * 输入: [1,3,2,3,1]
 * 输出: 2
 * 
 * 示例 2:
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 
 * 解题思路：
 * 1. 使用归并排序的思想解决
 * 2. 在归并的过程中统计翻转对的数量
 * 3. 对于左半部分[l...m]和右半部分[m+1...r]：
 *    - 在合并前，先统计翻转对数量：对于左半部分的每个元素nums[i]，统计右半部分有多少个元素nums[j]满足nums[i] > 2*nums[j]
 *    - 然后进行正常的归并排序合并过程
 * 
 * 时间复杂度分析：
 * - 归并排序的时间复杂度为O(n log n)
 * - 每一层递归都会遍历所有元素，共log n层
 * - 所以总时间复杂度为O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外的help数组存储临时数据，空间复杂度为O(n)
 * - 递归调用栈的深度为O(log n)
 * - 所以总空间复杂度为O(n)
 * 
 * 测试链接: https://leetcode.cn/problems/reverse-pairs/
 */
public class TestMain {
    
    /**
     * 计算翻转对数量
     * 
     * @param nums 输入数组
     * @return 翻转对数量
     */
    public static int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        return process(nums, 0, nums.length - 1);
    }
    
    /**
     * 递归处理区间[l,r]内的翻转对
     * 
     * @param nums 数组
     * @param l 区间左边界
     * @param r 区间右边界
     * @return 区间内翻转对数量
     */
    public static int process(int[] nums, int l, int r) {
        if (l == r) {
            return 0;
        }
        int m = l + ((r - l) >> 1);
        return process(nums, l, m) + process(nums, m + 1, r) + merge(nums, l, m, r);
    }
    
    /**
     * 合并两个有序数组，并统计合并过程中产生的翻转对数量
     * 
     * @param nums 数组
     * @param l 左半部分起始位置
     * @param m 左半部分结束位置
     * @param r 右半部分结束位置
     * @return 合并过程中产生的翻转对数量
     */
    public static int merge(int[] nums, int l, int m, int r) {
        // 统计翻转对数量
        int ans = 0;
        // 对于左半部分的每个元素nums[i]，统计右半部分有多少个元素nums[j]满足nums[i] > 2*nums[j]
        for (int i = l; i <= m; i++) {
            // 在右半部分找到第一个不满足nums[i] <= 2*nums[j]的位置
            int j = m + 1;
            while (j <= r && (long) nums[i] <= 2 * (long) nums[j]) {
                j++;
            }
            // 从j到r的所有元素都与nums[i]构成翻转对
            ans += r - j + 1;
        }
        
        // 正常的归并排序合并过程
        int[] help = new int[r - l + 1];
        int i = 0;
        int a = l;
        int b = m + 1;
        while (a <= m && b <= r) {
            help[i++] = nums[a] <= nums[b] ? nums[a++] : nums[b++];
        }
        while (a <= m) {
            help[i++] = nums[a++];
        }
        while (b <= r) {
            help[i++] = nums[b++];
        }
        for (i = 0; i < help.length; i++) {
            nums[l + i] = help[i];
        }
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, 2, 3, 1};
        System.out.println("输入: [1,3,2,3,1]");
        System.out.println("输出: " + reversePairs(nums1));
        System.out.println("期望: 2\n");
        
        // 测试用例2
        int[] nums2 = {2, 4, 3, 5, 1};
        System.out.println("输入: [2,4,3,5,1]");
        System.out.println("输出: " + reversePairs(nums2));
        System.out.println("期望: 3");
    }
}
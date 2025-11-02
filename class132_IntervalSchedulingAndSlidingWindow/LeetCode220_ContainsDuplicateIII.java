package class129;

import java.util.TreeSet;

/**
 * LeetCode 220. 存在重复元素 III
 * 
 * 题目描述：
 * 给你一个整数数组 nums 和两个整数 indexDiff 和 valueDiff 。
 * 找出满足下述条件的下标对 (i, j)：
 * 1. i != j
 * 2. abs(i - j) <= indexDiff
 * 3. abs(nums[i] - nums[j]) <= valueDiff
 * 如果存在，返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 这是一个滑动窗口结合TreeSet（平衡二叉搜索树）的问题。
 * 
 * 核心思想：
 * 1. 使用滑动窗口维护最多k+1个元素（k=indexDiff）
 * 2. 使用TreeSet维护窗口内元素的有序性
 * 3. 对于每个新元素，检查是否存在满足条件的旧元素
 * 
 * 具体步骤：
 * 1. 遍历数组，维护一个大小为k+1的滑动窗口
 * 2. 对于窗口中的每个元素，使用TreeSet的ceiling和floor方法查找值域范围内最近的元素
 * 3. 如果找到满足条件的元素，返回true
 * 4. 当窗口大小超过k+1时，移除最早加入的元素
 * 
 * 时间复杂度：O(n log k)
 * 空间复杂度：O(k)
 * 
 * 相关题目：
 * - LeetCode 219. 存在重复元素 II
 * - LeetCode 287. 寻找重复数
 * - LeetCode 448. 找到所有数组中消失的数字
 */
public class LeetCode220_ContainsDuplicateIII {
    
    /**
     * 判断是否存在满足条件的下标对
     * 
     * @param nums 整数数组
     * @param indexDiff 下标差的最大值
     * @param valueDiff 值差的最大值
     * @return 是否存在满足条件的下标对
     */
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        // 使用TreeSet维护滑动窗口内元素的有序性
        TreeSet<Long> window = new TreeSet<>();
        
        for (int i = 0; i < nums.length; i++) {
            long current = (long) nums[i];
            
            // 在TreeSet中查找是否存在满足条件的元素
            // ceiling(x)返回大于等于x的最小元素
            Long ceiling = window.ceiling(current - valueDiff);
            // floor(x)返回小于等于x的最大元素
            Long floor = window.floor(current + valueDiff);
            
            // 如果找到满足条件的元素，返回true
            if ((ceiling != null && ceiling <= current + valueDiff) || 
                (floor != null && floor >= current - valueDiff)) {
                return true;
            }
            
            // 将当前元素加入窗口
            window.add(current);
            
            // 如果窗口大小超过indexDiff+1，移除最早加入的元素
            if (window.size() > indexDiff + 1) {
                window.remove((long) nums[i - indexDiff - 1]);
            }
        }
        
        return false;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3, 1};
        int indexDiff1 = 3;
        int valueDiff1 = 0;
        System.out.println("测试用例1:");
        System.out.println("输入: nums = [1,2,3,1], indexDiff = " + indexDiff1 + ", valueDiff = " + valueDiff1);
        System.out.println("输出: " + containsNearbyAlmostDuplicate(nums1, indexDiff1, valueDiff1)); // 期望输出: true
        
        // 测试用例2
        int[] nums2 = {1, 5, 9, 1, 5, 9};
        int indexDiff2 = 2;
        int valueDiff2 = 3;
        System.out.println("\n测试用例2:");
        System.out.println("输入: nums = [1,5,9,1,5,9], indexDiff = " + indexDiff2 + ", valueDiff = " + valueDiff2);
        System.out.println("输出: " + containsNearbyAlmostDuplicate(nums2, indexDiff2, valueDiff2)); // 期望输出: false
        
        // 测试用例3
        int[] nums3 = {1, 0, 1, 1};
        int indexDiff3 = 1;
        int valueDiff3 = 2;
        System.out.println("\n测试用例3:");
        System.out.println("输入: nums = [1,0,1,1], indexDiff = " + indexDiff3 + ", valueDiff = " + valueDiff3);
        System.out.println("输出: " + containsNearbyAlmostDuplicate(nums3, indexDiff3, valueDiff3)); // 期望输出: true
    }
}
package class129;

import java.util.*;

/**
 * LeetCode 219. Contains Duplicate II
 * 
 * 题目描述：
 * 给你一个整数数组 nums 和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
 * 满足 nums[i] == nums[j] 且 abs(i - j) <= k。
 * 如果存在，返回 true；否则，返回 false。
 * 
 * 解题思路：
 * 这是一个滑动窗口结合哈希表的问题。
 * 
 * 核心思想：
 * 1. 使用滑动窗口维护最多k+1个元素
 * 2. 使用哈希表维护窗口内元素的存在性
 * 3. 当窗口大小超过k+1时，移除最早加入的元素
 * 
 * 具体步骤：
 * 1. 遍历数组，维护一个大小为k+1的滑动窗口
 * 2. 对于每个元素，检查它是否已在当前窗口中存在
 * 3. 如果存在，返回true
 * 4. 当窗口大小超过k+1时，移除最早加入的元素
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(min(n, k))
 * 
 * 相关题目：
 * - LeetCode 220. 存在重复元素 III（TreeSet滑动窗口）
 * - LeetCode 121. 买卖股票的最佳时机（滑动窗口）
 * - LeetCode 239. 滑动窗口最大值（双端队列）
 */
public class LeetCode219_ContainsDuplicateII {
    
    /**
     * 判断数组中是否存在两个不同的索引满足条件
     * 
     * @param nums 整数数组
     * @param k 索引差的最大值
     * @return 是否存在满足条件的索引对
     */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        // 使用HashSet维护滑动窗口内元素的存在性
        Set<Integer> window = new HashSet<>();
        
        for (int i = 0; i < nums.length; i++) {
            // 如果当前元素已在窗口中存在，返回true
            if (window.contains(nums[i])) {
                return true;
            }
            
            // 将当前元素加入窗口
            window.add(nums[i]);
            
            // 如果窗口大小超过k+1，移除最早加入的元素
            if (window.size() > k) {
                window.remove(nums[i - k]);
            }
        }
        
        return false;
    }
    
    /**
     * 另一种实现方式：使用HashMap记录元素的最新索引
     * 
     * @param nums 整数数组
     * @param k 索引差的最大值
     * @return 是否存在满足条件的索引对
     */
    public static boolean containsNearbyDuplicateV2(int[] nums, int k) {
        // 使用HashMap维护元素及其最新索引
        Map<Integer, Integer> indexMap = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            // 如果元素已存在且索引差满足条件，返回true
            if (indexMap.containsKey(nums[i]) && i - indexMap.get(nums[i]) <= k) {
                return true;
            }
            
            // 更新元素的最新索引
            indexMap.put(nums[i], i);
        }
        
        return false;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3, 1};
        int k1 = 3;
        System.out.println("测试用例1:");
        System.out.println("输入: nums = " + Arrays.toString(nums1) + ", k = " + k1);
        System.out.println("输出 (方法1): " + containsNearbyDuplicate(nums1, k1)); // 期望输出: true
        System.out.println("输出 (方法2): " + containsNearbyDuplicateV2(nums1, k1)); // 期望输出: true
        
        // 测试用例2
        int[] nums2 = {1, 0, 1, 1};
        int k2 = 1;
        System.out.println("\n测试用例2:");
        System.out.println("输入: nums = " + Arrays.toString(nums2) + ", k = " + k2);
        System.out.println("输出 (方法1): " + containsNearbyDuplicate(nums2, k2)); // 期望输出: true
        System.out.println("输出 (方法2): " + containsNearbyDuplicateV2(nums2, k2)); // 期望输出: true
        
        // 测试用例3
        int[] nums3 = {1, 2, 3, 1, 2, 3};
        int k3 = 2;
        System.out.println("\n测试用例3:");
        System.out.println("输入: nums = " + Arrays.toString(nums3) + ", k = " + k3);
        System.out.println("输出 (方法1): " + containsNearbyDuplicate(nums3, k3)); // 期望输出: false
        System.out.println("输出 (方法2): " + containsNearbyDuplicateV2(nums3, k3)); // 期望输出: false
        
        // 测试用例4
        int[] nums4 = {99, 99};
        int k4 = 2;
        System.out.println("\n测试用例4:");
        System.out.println("输入: nums = " + Arrays.toString(nums4) + ", k = " + k4);
        System.out.println("输出 (方法1): " + containsNearbyDuplicate(nums4, k4)); // 期望输出: true
        System.out.println("输出 (方法2): " + containsNearbyDuplicateV2(nums4, k4)); // 期望输出: true
    }
}
package class049;

import java.util.*;

/**
 * 1695. 删除子数组的最大得分
 * 给你一个正整数数组 nums ，请你从中删除一个含有 若干不同元素 的子数组。删除子数组的 得分 就是子数组各元素之 和 。
 * 返回 只删除一个 子数组可获得的 最大得分 。
 * 如果数组为空，返回 0 。
 * 
 * 解题思路：
 * 使用滑动窗口维护一个不含重复元素的子数组
 * 当遇到重复元素时，收缩左边界直到没有重复元素
 * 在滑动过程中记录最大和
 * 
 * 时间复杂度：O(n)，其中n是数组长度
 * 空间复杂度：O(k)，k是不同元素的数量
 * 
 * 是否最优解：是
 * 
 * 测试链接：https://leetcode.cn/problems/maximum-erasure-value/
 */
public class Code21_MaximumErasureValue {
    
    /**
     * 计算删除子数组的最大得分
     * 
     * @param nums 正整数数组
     * @return 最大得分
     */
    public static int maximumUniqueSubarray(int[] nums) {
        int n = nums.length;
        int maxScore = 0; // 最大得分
        int currentSum = 0; // 当前窗口的和
        int left = 0; // 窗口左边界
        Set<Integer> window = new HashSet<>(); // 记录窗口内的元素
        
        // 滑动窗口右边界
        for (int right = 0; right < n; right++) {
            // 如果当前元素已经在窗口中，收缩左边界
            while (window.contains(nums[right])) {
                currentSum -= nums[left];
                window.remove(nums[left]);
                left++;
            }
            
            // 添加当前元素到窗口
            window.add(nums[right]);
            currentSum += nums[right];
            
            // 更新最大得分
            maxScore = Math.max(maxScore, currentSum);
        }
        
        return maxScore;
    }
    
    /**
     * 优化版本：使用哈希表记录元素最后一次出现的位置
     * 时间复杂度：O(n)，空间复杂度：O(k)
     */
    public static int maximumUniqueSubarrayOptimized(int[] nums) {
        int n = nums.length;
        int maxScore = 0;
        int currentSum = 0;
        int left = 0;
        Map<Integer, Integer> lastSeen = new HashMap<>(); // 记录元素最后一次出现的位置
        
        for (int right = 0; right < n; right++) {
            int num = nums[right];
            
            // 如果当前元素已经在窗口中，并且位置在left之后
            if (lastSeen.containsKey(num) && lastSeen.get(num) >= left) {
                // 移动左边界到重复元素的下一个位置
                int duplicateIndex = lastSeen.get(num);
                for (int i = left; i <= duplicateIndex; i++) {
                    currentSum -= nums[i];
                }
                left = duplicateIndex + 1;
            }
            
            // 更新当前元素的位置
            lastSeen.put(num, right);
            currentSum += num;
            
            // 更新最大得分
            maxScore = Math.max(maxScore, currentSum);
        }
        
        return maxScore;
    }
    
    /**
     * 使用前缀和数组优化版本
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    public static int maximumUniqueSubarrayWithPrefixSum(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        
        // 计算前缀和数组
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        int maxScore = 0;
        int left = 0;
        Map<Integer, Integer> lastSeen = new HashMap<>();
        
        for (int right = 0; right < n; right++) {
            int num = nums[right];
            
            // 如果当前元素已经在窗口中，并且位置在left之后
            if (lastSeen.containsKey(num) && lastSeen.get(num) >= left) {
                left = lastSeen.get(num) + 1;
            }
            
            // 更新当前元素的位置
            lastSeen.put(num, right);
            
            // 计算当前窗口的和
            int currentSum = prefixSum[right + 1] - prefixSum[left];
            maxScore = Math.max(maxScore, currentSum);
        }
        
        return maxScore;
    }
    
    /**
     * 使用数组代替哈希表（当数字范围有限时）
     * 时间复杂度：O(n)，空间复杂度：O(max_value)
     */
    public static int maximumUniqueSubarrayWithArray(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        
        // 找到数组中的最大值
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        int maxScore = 0;
        int currentSum = 0;
        int left = 0;
        boolean[] inWindow = new boolean[maxVal + 1]; // 记录元素是否在窗口中
        
        for (int right = 0; right < n; right++) {
            int num = nums[right];
            
            // 如果当前元素已经在窗口中，收缩左边界
            while (inWindow[num]) {
                currentSum -= nums[left];
                inWindow[nums[left]] = false;
                left++;
            }
            
            // 添加当前元素到窗口
            inWindow[num] = true;
            currentSum += num;
            
            // 更新最大得分
            maxScore = Math.max(maxScore, currentSum);
        }
        
        return maxScore;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {4, 2, 4, 5, 6};
        int result1 = maximumUniqueSubarray(nums1);
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("最大得分: " + result1);
        System.out.println("预期: 17");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {5, 2, 1, 2, 5, 2, 1, 2, 5};
        int result2 = maximumUniqueSubarray(nums2);
        System.out.println("输入数组: " + Arrays.toString(nums2));
        System.out.println("最大得分: " + result2);
        System.out.println("预期: 8");
        System.out.println();
        
        // 测试用例3：所有元素都不同
        int[] nums3 = {1, 2, 3, 4, 5};
        int result3 = maximumUniqueSubarray(nums3);
        System.out.println("输入数组: " + Arrays.toString(nums3));
        System.out.println("最大得分: " + result3);
        System.out.println("预期: 15");
        System.out.println();
        
        // 测试用例4：所有元素都相同
        int[] nums4 = {1, 1, 1, 1, 1};
        int result4 = maximumUniqueSubarray(nums4);
        System.out.println("输入数组: " + Arrays.toString(nums4));
        System.out.println("最大得分: " + result4);
        System.out.println("预期: 1");
        System.out.println();
        
        // 测试用例5：边界情况，单个元素
        int[] nums5 = {5};
        int result5 = maximumUniqueSubarray(nums5);
        System.out.println("输入数组: " + Arrays.toString(nums5));
        System.out.println("最大得分: " + result5);
        System.out.println("预期: 5");
        System.out.println();
        
        // 测试用例6：空数组
        int[] nums6 = {};
        int result6 = maximumUniqueSubarray(nums6);
        System.out.println("输入数组: " + Arrays.toString(nums6));
        System.out.println("最大得分: " + result6);
        System.out.println("预期: 0");
    }
}
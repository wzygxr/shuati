package class049;

/**
 * 1493. 删掉一个元素以后全为 1 的最长子数组
 * 给你一个二进制数组 nums ，你需要从中删掉一个元素。
 * 请你在删掉元素的结果数组中，返回最长的且只包含 1 的非空子数组的长度。
 * 如果不存在这样的子数组，请返回 0 。
 * 
 * 解题思路：
 * 使用滑动窗口维护一个最多包含1个0的窗口
 * 当窗口内0的个数超过1时，收缩左边界
 * 最终结果是窗口大小减1（因为要删除一个元素）
 * 
 * 时间复杂度：O(n)，其中n是数组长度
 * 空间复杂度：O(1)
 * 
 * 是否最优解：是
 * 
 * 测试链接：https://leetcode.cn/problems/longest-subarray-of-1s-after-deleting-one-element/
 */
public class Code20_LongestSubarrayOf1sAfterDeletingOneElement {
    
    /**
     * 计算删掉一个元素后全为1的最长子数组长度
     * 
     * @param nums 二进制数组
     * @return 最长子数组长度
     */
    public static int longestSubarray(int[] nums) {
        int n = nums.length;
        int maxLength = 0; // 最大长度
        int left = 0; // 窗口左边界
        int zeroCount = 0; // 窗口内0的个数
        
        // 滑动窗口右边界
        for (int right = 0; right < n; right++) {
            // 如果当前元素是0，增加0的计数
            if (nums[right] == 0) {
                zeroCount++;
            }
            
            // 如果窗口内0的个数超过1，收缩左边界
            while (zeroCount > 1) {
                if (nums[left] == 0) {
                    zeroCount--;
                }
                left++;
            }
            
            // 更新最大长度（窗口大小减1，因为要删除一个元素）
            maxLength = Math.max(maxLength, right - left);
        }
        
        return maxLength;
    }
    
    /**
     * 优化版本：使用更简洁的写法
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    public static int longestSubarrayOptimized(int[] nums) {
        int n = nums.length;
        int maxLength = 0;
        int left = 0;
        int zeroCount = 0;
        
        for (int right = 0; right < n; right++) {
            zeroCount += 1 - nums[right]; // 如果nums[right]是0，则zeroCount加1
            
            while (zeroCount > 1) {
                zeroCount -= 1 - nums[left]; // 如果nums[left]是0，则zeroCount减1
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left);
        }
        
        return maxLength;
    }
    
    /**
     * 另一种思路：计算连续1的段，然后考虑删除中间的一个0来连接两段1
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    public static int longestSubarrayAlternative(int[] nums) {
        int n = nums.length;
        int maxLength = 0;
        int prev = 0; // 前一段连续1的长度
        int curr = 0; // 当前连续1的长度
        boolean hasZero = false; // 是否包含0
        
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                curr++;
            } else {
                hasZero = true;
                // 遇到0时，可以删除这个0来连接prev和curr
                maxLength = Math.max(maxLength, prev + curr);
                prev = curr;
                curr = 0;
            }
        }
        
        // 处理最后一段
        maxLength = Math.max(maxLength, prev + curr);
        
        // 如果整个数组都是1，需要删除一个元素
        if (!hasZero) {
            return n - 1;
        }
        
        return maxLength;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 1, 0, 1};
        int result1 = longestSubarray(nums1);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums1));
        System.out.println("最长子数组长度: " + result1);
        System.out.println("预期: 3");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {0, 1, 1, 1, 0, 1, 1, 0, 1};
        int result2 = longestSubarray(nums2);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums2));
        System.out.println("最长子数组长度: " + result2);
        System.out.println("预期: 5");
        System.out.println();
        
        // 测试用例3：全是1
        int[] nums3 = {1, 1, 1};
        int result3 = longestSubarray(nums3);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums3));
        System.out.println("最长子数组长度: " + result3);
        System.out.println("预期: 2");
        System.out.println();
        
        // 测试用例4：全是0
        int[] nums4 = {0, 0, 0};
        int result4 = longestSubarray(nums4);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums4));
        System.out.println("最长子数组长度: " + result4);
        System.out.println("预期: 0");
        System.out.println();
        
        // 测试用例5：边界情况，单个元素
        int[] nums5 = {1};
        int result5 = longestSubarray(nums5);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums5));
        System.out.println("最长子数组长度: " + result5);
        System.out.println("预期: 0");
        System.out.println();
        
        // 测试用例6：交替的0和1
        int[] nums6 = {1, 0, 1, 0, 1};
        int result6 = longestSubarray(nums6);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums6));
        System.out.println("最长子数组长度: " + result6);
        System.out.println("预期: 2");
    }
}
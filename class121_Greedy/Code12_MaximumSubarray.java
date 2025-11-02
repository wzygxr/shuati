package class090;

// 最大子数组和
// 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
// 子数组是数组中的一个连续部分。
// 测试链接: https://leetcode.cn/problems/maximum-subarray/
public class Code12_MaximumSubarray {

    /**
     * 最大子数组和问题的贪心解法
     * 
     * 解题思路：
     * 1. 使用贪心策略，维护当前子数组的和
     * 2. 遍历数组，对于每个元素：
     *    - 如果当前子数组和为负数，则舍弃，从当前元素重新开始
     *    - 否则将当前元素加入到当前子数组中
     * 3. 在遍历过程中记录最大子数组和
     * 
     * 贪心策略的正确性：
     * 负数前缀会降低总和，因此当当前子数组和为负数时，应该立即舍弃，
     * 从下一个元素重新开始计算子数组和。
     * 
     * 时间复杂度：O(n)，只需要遍历数组一次
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param nums 整数数组
     * @return 具有最大和的连续子数组的最大和
     */
    public static int maxSubArray(int[] nums) {
        // 边界条件处理：如果数组为空，返回0
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // 1. 初始化变量
        int maxSum = nums[0];     // 最大子数组和，初始化为第一个元素
        int currentSum = nums[0]; // 当前子数组和，初始化为第一个元素

        // 2. 从第二个元素开始遍历数组
        for (int i = 1; i < nums.length; i++) {
            // 3. 如果当前子数组和为负数，则舍弃，从当前元素重新开始
            if (currentSum < 0) {
                currentSum = nums[i];
            } 
            // 4. 否则将当前元素加入到当前子数组中
            else {
                currentSum += nums[i];
            }

            // 5. 更新最大子数组和
            maxSum = Math.max(maxSum, currentSum);
        }

        // 6. 返回最大子数组和
        return maxSum;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: nums = [-2,1,-3,4,-1,2,1,-5,4]
        // 输出: 6
        // 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("测试用例1结果: " + maxSubArray(nums1)); // 期望输出: 6

        // 测试用例2
        // 输入: nums = [1]
        // 输出: 1
        int[] nums2 = {1};
        System.out.println("测试用例2结果: " + maxSubArray(nums2)); // 期望输出: 1

        // 测试用例3
        // 输入: nums = [5,4,-1,7,8]
        // 输出: 23
        int[] nums3 = {5, 4, -1, 7, 8};
        System.out.println("测试用例3结果: " + maxSubArray(nums3)); // 期望输出: 23

        // 测试用例4：边界情况
        // 输入: nums = [-1]
        // 输出: -1
        int[] nums4 = {-1};
        System.out.println("测试用例4结果: " + maxSubArray(nums4)); // 期望输出: -1

        // 测试用例5：复杂情况
        // 输入: nums = [-2,-1,-3,-4,-1,-2,-1,-5,-4]
        // 输出: -1
        int[] nums5 = {-2, -1, -3, -4, -1, -2, -1, -5, -4};
        System.out.println("测试用例5结果: " + maxSubArray(nums5)); // 期望输出: -1
    }
}
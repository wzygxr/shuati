// LeetCode 53. 最大子数组和
// 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
// 子数组 是数组中的一个连续部分。
// 测试链接 : https://leetcode.cn/problems/maximum-subarray/


/*
 * 解题思路:
 * 这是经典的Kadane算法问题。
 * 
 * 状态定义:
 * dp[i] 表示以 nums[i] 结尾的最大子数组和
 * 
 * 状态转移:
 * dp[i] = max(nums[i], dp[i-1] + nums[i])
 * 即要么从当前元素重新开始，要么将当前元素加入之前的子数组
 * 
 * 优化:
 * 由于当前状态只与前一个状态有关，可以使用一个变量代替数组
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 */


class Code08_MaximumSubarray {
public:
    static int maxSubArray(int nums[], int n) {
        // dp表示以当前元素结尾的最大子数组和
        int dp = nums[0];
        // maxSum表示全局最大子数组和
        int maxSum = nums[0];
        
        // 从第二个元素开始遍历
        for (int i = 1; i < n; i++) {
            // 要么从当前元素重新开始，要么将当前元素加入之前的子数组
            dp = (nums[i] > dp + nums[i]) ? nums[i] : dp + nums[i];
            // 更新全局最大值
            maxSum = (maxSum > dp) ? maxSum : dp;
        }
        
        return maxSum;
    }
    
    /*
     * 相关题目扩展:
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     */
};
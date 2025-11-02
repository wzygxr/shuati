// 最大子数组和
// 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
// 子数组是数组中的一个连续部分。
// 测试链接 : https://leetcode.cn/problems/maximum-subarray/

/**
 * 最大子数组和（Kadane算法）
 * 
 * 算法思路：
 * 使用贪心策略（Kadane算法）：
 * 1. 维护两个变量：
 *    - maxSoFar：到目前为止找到的最大子数组和
 *    - maxEndingHere：以当前元素结尾的最大子数组和
 * 2. 遍历数组，对于每个元素：
 *    - 更新maxEndingHere = max(nums[i], maxEndingHere + nums[i])
 *    - 更新maxSoFar = max(maxSoFar, maxEndingHere)
 * 3. 返回maxSoFar
 * 
 * 正确性分析：
 * 1. 对于每个位置，我们只需要考虑以该位置结尾的最大子数组和
 * 2. 要么从当前位置重新开始，要么延续之前的子数组
 * 3. 取两者中的较大值
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * @param nums 整数数组
 * @param numsSize 数组长度
 * @return 最大子数组和
 */
int maxSubArray(int nums[], int numsSize) {
    // 初始化变量
    int maxSoFar = nums[0];        // 到目前为止找到的最大子数组和
    int maxEndingHere = nums[0];   // 以当前元素结尾的最大子数组和
    
    // 从第二个元素开始遍历
    for (int i = 1; i < numsSize; i++) {
        // 更新以当前元素结尾的最大子数组和
        // 要么从当前元素重新开始，要么延续之前的子数组
        int currentSum = maxEndingHere + nums[i];
        if (nums[i] > currentSum) {
            maxEndingHere = nums[i];
        } else {
            maxEndingHere = currentSum;
        }
        
        // 更新到目前为止找到的最大子数组和
        if (maxEndingHere > maxSoFar) {
            maxSoFar = maxEndingHere;
        }
    }
    
    // 返回最大子数组和
    return maxSoFar;
}
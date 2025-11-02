// LeetCode 918. 环形子数组的最大和
// 给定一个长度为 n 的环形整数数组 nums ，返回 nums 的非空子数组的最大可能和。
// 环形数组意味着数组的末端将会与开头相连呈环状。
// 测试链接 : https://leetcode.cn/problems/maximum-sum-circular-subarray/


/*
 * 解题思路:
 * 这是最大子数组和问题的环形变种。在环形数组中，最大子数组可能有两种情况：
 * 1. 不跨越数组边界：直接使用Kadane算法求解
 * 2. 跨越数组边界：可以转换为求最小子数组和，然后用总和减去最小子数组和
 * 
 * 对于第二种情况，如果最大子数组跨越了边界，那么中间未被选中的部分就是一个连续的最小子数组。
 * 因此，我们可以计算总和减去最小子数组和，就得到了跨越边界的最大子数组和。
 * 
 * 特殊情况：如果所有元素都是负数，那么最小子数组和等于总和，会导致结果为0，
 * 但实际上子数组不能为空，所以这种情况应该直接返回最大子数组和。
 * 
 * 时间复杂度: O(n) - 需要遍历数组三次（最大子数组和、最小子数组和、总和）
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 */


class Code09_MaximumSumCircularSubarray {
public:
    static int maxSubarraySumCircular(int nums[], int n) {
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        
        // 计算最大子数组和（不跨越边界）
        int maxKadane = kadaneMax(nums, n);
        
        // 计算总和
        int totalSum = 0;
        for (int i = 0; i < n; i++) {
            totalSum += nums[i];
        }
        
        // 计算最小子数组和
        int minKadane = kadaneMin(nums, n);
        
        // 计算跨越边界的最大子数组和
        int maxCircular = totalSum - minKadane;
        
        // 特殊情况：如果所有元素都是负数，maxCircular会是0，但子数组不能为空
        // 所以应该返回不跨越边界的最大子数组和
        if (maxCircular == 0) {
            return maxKadane;
        }
        
        // 返回两种情况的最大值
        return (maxKadane > maxCircular) ? maxKadane : maxCircular;
    }
    
private:
    // Kadane算法求最大子数组和
    static int kadaneMax(int nums[], int n) {
        int dp = nums[0];
        int maxSum = nums[0];
        
        for (int i = 1; i < n; i++) {
            dp = (nums[i] > dp + nums[i]) ? nums[i] : dp + nums[i];
            maxSum = (maxSum > dp) ? maxSum : dp;
        }
        
        return maxSum;
    }
    
    // Kadane算法求最小子数组和
    static int kadaneMin(int nums[], int n) {
        int dp = nums[0];
        int minSum = nums[0];
        
        for (int i = 1; i < n; i++) {
            dp = (nums[i] < dp + nums[i]) ? nums[i] : dp + nums[i];
            minSum = (minSum < dp) ? minSum : dp;
        }
        
        return minSum;
    }
    
    /*
     * 相关题目扩展:
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     */
};
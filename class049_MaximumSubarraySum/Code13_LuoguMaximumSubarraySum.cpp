// 洛谷 P1115 最大子段和
// 给出一个长度为n 的序列a，选出其中连续且非空的一段使得这段和最大。
// 测试链接 : https://www.luogu.com.cn/problem/P1115


/*
 * 解题思路:
 * 这是经典的Kadane算法问题，与LeetCode 53相同。
 * 
 * 状态定义:
 * dp[i] 表示以 a[i] 结尾的最大子数组和
 * 
 * 状态转移:
 * dp[i] = max(a[i], dp[i-1] + a[i])
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


class Code13_LuoguMaximumSubarraySum {
public:
    static long long maxSubArraySum(int a[], int n) {
        // dp表示以当前元素结尾的最大子数组和
        long long dp = a[0];
        // maxSum表示全局最大子数组和
        long long maxSum = a[0];
        
        // 从第二个元素开始遍历
        for (int i = 1; i < n; i++) {
            // 要么从当前元素重新开始，要么将当前元素加入之前的子数组
            dp = (a[i] > dp + a[i]) ? a[i] : dp + a[i];
            // 更新全局最大值
            maxSum = (maxSum > dp) ? maxSum : dp;
        }
        
        return maxSum;
    }
    
    /*
     * 相关题目扩展:
     * 1. 洛谷 P1115 最大子段和 - https://www.luogu.com.cn/problem/P1115
     * 2. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 3. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 4. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 5. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     */
};

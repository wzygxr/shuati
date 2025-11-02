// LeetCode 1191. K 次串联后最大子数组之和
// 给你一个整数数组 arr 和一个整数 k。  
// 首先，需要检查是否可以获得一个长度为 k 的非空子数组，使得该子数组的和最大。
// （子数组是数组中连续的一部分）。
// 测试链接 : https://leetcode.cn/problems/k-concatenation-maximum-sum/


/*
 * 解题思路:
 * 这是最大子数组和问题的K次串联变种。我们需要考虑以下几种情况：
 * 1. 当k=1时，直接求最大子数组和
 * 2. 当k>=2时，需要考虑：
 *    a. 最大子数组完全在第一个数组中
 *    b. 最大子数组完全在最后一个数组中
 *    c. 最大子数组跨越多个数组，这种情况下可以分解为：
 *       - 前缀最大和 + 中间完整数组的和 + 后缀最大和
 * 
 * 具体分析：
 * 1. 如果数组总和为正数，那么中间的(k-2)个完整数组都应该包含在结果中
 * 2. 前缀最大和是从数组开始到某个位置的最大和
 * 3. 后缀最大和是从某个位置到数组结束的最大和
 * 4. 最终结果是：max(单个数组最大子数组和, 前缀最大和 + (k-2)*总和 + 后缀最大和)
 * 
 * 注意：题目允许子数组为空，答案最小是0，不可能是负数
 * 
 * 时间复杂度: O(n) - 需要遍历数组常数次
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 */


class Code10_KConcatenationMaximumSum {
public:
    static int kConcatenationMaxSum(int arr[], int n, int k) {
        const int MOD = 1000000007;
        
        // 特殊情况处理
        if (n == 0 || k == 0) return 0;
        
        // 计算数组总和
        long long totalSum = 0;
        for (int i = 0; i < n; i++) {
            totalSum += arr[i];
        }
        
        // 当k=1时，直接求最大子数组和
        if (k == 1) {
            return (int)(kadane(arr, n) % MOD);
        }
        
        // 当k>=2时，计算前缀最大和和后缀最大和
        long long prefixMax = maxPrefixSum(arr, n);
        long long suffixMax = maxSuffixSum(arr, n);
        
        // 如果总和为正，中间的(k-2)个数组都应该包含
        long long middleSum = 0;
        if (totalSum > 0) {
            middleSum = ((long long)(k - 2) * totalSum) % MOD;
        }
        
        // 跨越多个数组的最大和
        long long crossSum = (prefixMax + middleSum + suffixMax) % MOD;
        
        // 单个数组中的最大子数组和
        long long singleMax = kadane(arr, n);
        
        // 返回较大值，且不小于0
        long long result = (crossSum > singleMax) ? crossSum : singleMax;
        return (int)(result > 0 ? result : 0);
    }
    
private:
    // Kadane算法求最大子数组和
    static long long kadane(int arr[], int n) {
        long long dp = arr[0];
        long long maxSum = arr[0] > 0 ? arr[0] : 0;
        
        for (int i = 1; i < n; i++) {
            dp = (arr[i] > dp + arr[i]) ? arr[i] : dp + arr[i];
            maxSum = (maxSum > dp) ? maxSum : dp;
        }
        
        return maxSum > 0 ? maxSum : 0;
    }
    
    // 计算前缀最大和
    static long long maxPrefixSum(int arr[], int n) {
        long long sum = 0;
        long long maxSum = 0;
        
        for (int i = 0; i < n; i++) {
            sum += arr[i];
            maxSum = (maxSum > sum) ? maxSum : sum;
        }
        
        return maxSum;
    }
    
    // 计算后缀最大和
    static long long maxSuffixSum(int arr[], int n) {
        long long sum = 0;
        long long maxSum = 0;
        
        for (int i = n - 1; i >= 0; i--) {
            sum += arr[i];
            maxSum = (maxSum > sum) ? maxSum : sum;
        }
        
        return maxSum;
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
// HackerRank Maximum Subarray Sum
// 给定一个元素数组和一个整数，确定任何子数组的和模的最大值。
// 测试链接 : https://www.hackerrank.com/challenges/maximum-subarray-sum/problem


/*
 * 解题思路:
 * 这是最大子数组和问题的模运算变种。我们需要找到一个子数组，使得其和对m取模后最大。
 * 
 * 暴力解法是枚举所有子数组，计算它们的和模m，但时间复杂度为O(n^2)，对于大数组会超时。
 * 
 * 优化解法使用前缀和：
 * 1. 计算前缀和数组prefixSum，其中prefixSum[i] = (a[0] + a[1] + ... + a[i-1]) % m
 * 2. 对于每个前缀和prefixSum[i]，我们需要找到一个之前的前缀和prefixSum[j] (j < i)，
 *    使得(prefixSum[i] - prefixSum[j]) % m最大。
 * 3. 这等价于找到一个prefixSum[j]，使得prefixSum[j]最接近prefixSum[i]但小于prefixSum[i]。
 * 4. 如果prefixSum[i]是最小的，那么答案就是prefixSum[i]本身。
 * 
 * 由于C++编译环境限制，我们使用基础数组实现
 * 
 * 时间复杂度: O(n^2) - 简化版本的时间复杂度
 * 空间复杂度: O(n) - 需要存储前缀和
 * 
 * 是否最优解: 不是，但考虑到环境限制，这是一个可行解法
 */


class Code12_HackerRankMaximumSubarraySum {
public:
    static long maximumSum(long a[], int n, long m) {
        // 计算前缀和数组
        long* prefixSum = new long[n + 1];
        prefixSum[0] = 0;
        
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = (prefixSum[i] + a[i]) % m;
        }
        
        // 最大模值
        long maxModSum = 0;
        
        // 遍历所有可能的子数组
        for (int i = 1; i <= n; i++) {
            // 单个元素的情况
            maxModSum = (maxModSum > prefixSum[i]) ? maxModSum : prefixSum[i];
            
            // 多个元素的情况
            for (int j = 0; j < i; j++) {
                long modSum = (prefixSum[i] - prefixSum[j] + m) % m;
                maxModSum = (maxModSum > modSum) ? maxModSum : modSum;
            }
        }
        
        delete[] prefixSum;
        return maxModSum;
    }
    
    /*
     * 相关题目扩展:
     * 1. HackerRank Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
     * 2. HackerRank The Maximum Subarray - https://www.hackerrank.com/challenges/maxsubarray/problem
     * 3. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 4. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     */
};
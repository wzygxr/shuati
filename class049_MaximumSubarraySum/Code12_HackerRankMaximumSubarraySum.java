package class071;

// HackerRank Maximum Subarray Sum
// 给定一个元素数组和一个整数，确定任何子数组的和模的最大值。
// 测试链接 : https://www.hackerrank.com/challenges/maximum-subarray-sum/problem


/*
 * 解题思路:
 * 这是最大子数组和问题的模运算变种。我们需要找到一个子数组，使得其和对m取模后最大。
 * 
 * 暴力解法是枚举所有子数组，计算它们的和模m，但时间复杂度为O(n^2)，对于大数组会超时。
 * 
 * 优化解法使用前缀和和TreeSet：
 * 1. 计算前缀和数组prefixSum，其中prefixSum[i] = (a[0] + a[1] + ... + a[i-1]) % m
 * 2. 对于每个前缀和prefixSum[i]，我们需要找到一个之前的前缀和prefixSum[j] (j < i)，
 *    使得(prefixSum[i] - prefixSum[j]) % m最大。
 * 3. 这等价于找到一个prefixSum[j]，使得prefixSum[j]最接近prefixSum[i]但小于prefixSum[i]。
 * 4. 如果prefixSum[i]是最小的，那么答案就是prefixSum[i]本身。
 * 5. 使用TreeSet来维护之前的前缀和，可以快速找到最接近的值。
 * 
 * 时间复杂度: O(n log n) - 遍历数组需要O(n)，每次在TreeSet中查找需要O(log n)
 * 空间复杂度: O(n) - 需要存储前缀和和TreeSet
 * 
 * 是否最优解: 是，这是该问题的最优解法
 */


import java.util.TreeSet;

public class Code12_HackerRankMaximumSubarraySum {
    public static long maximumSum(long[] a, long m) {
        // 使用TreeSet维护之前的前缀和
        TreeSet<Long> prefixSet = new TreeSet<>();
        // 当前前缀和
        long prefixSum = 0;
        // 最大模值
        long maxModSum = 0;
        
        for (int i = 0; i < a.length; i++) {
            // 更新前缀和
            prefixSum = (prefixSum + a[i]) % m;
            
            // 更新最大模值
            maxModSum = Math.max(maxModSum, prefixSum);
            
            // 在prefixSet中找到第一个大于prefixSum的元素
            Long higher = prefixSet.higher(prefixSum);
            
            // 如果找到了这样的元素
            if (higher != null) {
                // 计算(prefixSum - higher + m) % m
                long modSum = (prefixSum - higher + m) % m;
                maxModSum = Math.max(maxModSum, modSum);
            }
            
            // 将当前前缀和加入集合
            prefixSet.add(prefixSum);
        }
        
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
}
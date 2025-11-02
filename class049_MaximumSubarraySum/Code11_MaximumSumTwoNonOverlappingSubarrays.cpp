// LeetCode 1031. 两个无重叠子数组的最大和
// 给出非负整数数组 A ，返回两个非重叠（连续）子数组中元素的最大和，
// 子数组的长度分别为 L 和 M，其中 L、M 是给定的整数。
// 测试链接 : https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/


/*
 * 解题思路:
 * 这是一个动态规划问题，需要找到两个不重叠的子数组，使得它们的和最大。
 * 
 * 我们可以考虑两种情况：
 * 1. 长度为L的子数组在长度为M的子数组前面
 * 2. 长度为M的子数组在长度为L的子数组前面
 * 
 * 对于每种情况，我们可以使用以下方法：
 * 1. 预处理计算所有长度为L和M的子数组的和
 * 2. 对于每个位置，计算到该位置为止的最大子数组和（前缀最大值）
 * 3. 对于每个位置，计算从该位置开始的最大子数组和（后缀最大值）
 * 4. 枚举分界点，计算两种情况下的最大值
 * 
 * 具体步骤：
 * 1. 计算前缀和数组，便于快速计算子数组和
 * 2. 计算长度为L的子数组和数组Lsums和长度为M的子数组和数组Msums
 * 3. 计算Lsums的前缀最大值和Msums的后缀最大值
 * 4. 计算Msums的前缀最大值和Lsums的后缀最大值
 * 5. 枚举分界点，计算两种情况下的最大值
 * 
 * 时间复杂度: O(n) - 需要遍历数组常数次
 * 空间复杂度: O(n) - 需要额外数组存储子数组和及前缀/后缀最大值
 * 
 * 是否最优解: 是，这是该问题的最优解法
 */


class Code11_MaximumSumTwoNonOverlappingSubarrays {
public:
    static int maxSumTwoNoOverlap(int A[], int n, int L, int M) {
        // 计算前缀和数组
        int* prefixSum = new int[n + 1];
        prefixSum[0] = 0;
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + A[i];
        }
        
        // 计算长度为L的子数组和数组
        int* Lsums = new int[n - L + 1];
        for (int i = 0; i <= n - L; i++) {
            Lsums[i] = prefixSum[i + L] - prefixSum[i];
        }
        
        // 计算长度为M的子数组和数组
        int* Msums = new int[n - M + 1];
        for (int i = 0; i <= n - M; i++) {
            Msums[i] = prefixSum[i + M] - prefixSum[i];
        }
        
        // 情况1: L长度子数组在M长度子数组前面
        int result1 = helper(Lsums, n - L + 1, Msums, n - M + 1, L, M);
        
        // 情况2: M长度子数组在L长度子数组前面
        int result2 = helper(Msums, n - M + 1, Lsums, n - L + 1, M, L);
        
        delete[] prefixSum;
        delete[] Lsums;
        delete[] Msums;
        
        return (result1 > result2) ? result1 : result2;
    }
    
private:
    // 辅助函数，计算一种情况下的最大和
    static int helper(int* firstSums, int firstLen, int* secondSums, int secondLen, int firstL, int secondL) {
        // 计算firstSums的前缀最大值
        int* firstPrefixMax = new int[firstLen];
        firstPrefixMax[0] = firstSums[0];
        for (int i = 1; i < firstLen; i++) {
            firstPrefixMax[i] = (firstPrefixMax[i - 1] > firstSums[i]) ? firstPrefixMax[i - 1] : firstSums[i];
        }
        
        // 计算secondSums的后缀最大值
        int* secondSuffixMax = new int[secondLen];
        secondSuffixMax[secondLen - 1] = secondSums[secondLen - 1];
        for (int i = secondLen - 2; i >= 0; i--) {
            secondSuffixMax[i] = (secondSuffixMax[i + 1] > secondSums[i]) ? secondSuffixMax[i + 1] : secondSums[i];
        }
        
        // 枚举分界点，计算最大和
        int maxSum = 0;
        for (int i = 0; i < firstLen && i + firstL + secondL - 1 < firstLen + secondLen - 1; i++) {
            // 确保不会越界
            int secondIndex = i + firstL;
            if (secondIndex < secondLen) {
                int currentSum = firstPrefixMax[i] + secondSuffixMax[secondIndex];
                maxSum = (maxSum > currentSum) ? maxSum : currentSum;
            }
        }
        
        delete[] firstPrefixMax;
        delete[] secondSuffixMax;
        
        return maxSum;
    }
    
    /*
     * 相关题目扩展:
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1031. 两个无重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     */
};
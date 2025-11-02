# LeetCode 1031. 两个无重叠子数组的最大和
# 给出非负整数数组 A ，返回两个非重叠（连续）子数组中元素的最大和，
# 子数组的长度分别为 L 和 M，其中 L、M 是给定的整数。
# 测试链接 : https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/


"""
解题思路:
这是一个动态规划问题，需要找到两个不重叠的子数组，使得它们的和最大。

我们可以考虑两种情况：
1. 长度为L的子数组在长度为M的子数组前面
2. 长度为M的子数组在长度为L的子数组前面

对于每种情况，我们可以使用以下方法：
1. 预处理计算所有长度为L和M的子数组的和
2. 对于每个位置，计算到该位置为止的最大子数组和（前缀最大值）
3. 对于每个位置，计算从该位置开始的最大子数组和（后缀最大值）
4. 枚举分界点，计算两种情况下的最大值

具体步骤：
1. 计算前缀和数组，便于快速计算子数组和
2. 计算长度为L的子数组和数组Lsums和长度为M的子数组和数组Msums
3. 计算Lsums的前缀最大值和Msums的后缀最大值
4. 计算Msums的前缀最大值和Lsums的后缀最大值
5. 枚举分界点，计算两种情况下的最大值

时间复杂度: O(n) - 需要遍历数组常数次
空间复杂度: O(n) - 需要额外数组存储子数组和及前缀/后缀最大值

是否最优解: 是，这是该问题的最优解法
"""


class Code11_MaximumSumTwoNonOverlappingSubarrays:
    @staticmethod
    def maxSumTwoNoOverlap(A, L, M):
        """
        计算两个无重叠子数组的最大和

        Args:
            A: List[int] - 输入的整数数组
            L: int - 第一个子数组的长度
            M: int - 第二个子数组的长度

        Returns:
            int - 两个无重叠子数组的最大和
        """
        # 计算前缀和数组
        prefix_sum = [0]
        for num in A:
            prefix_sum.append(prefix_sum[-1] + num)
        
        # 计算长度为L的子数组和数组
        Lsums = []
        for i in range(len(A) - L + 1):
            Lsums.append(prefix_sum[i + L] - prefix_sum[i])
        
        # 计算长度为M的子数组和数组
        Msums = []
        for i in range(len(A) - M + 1):
            Msums.append(prefix_sum[i + M] - prefix_sum[i])
        
        # 情况1: L长度子数组在M长度子数组前面
        result1 = Code11_MaximumSumTwoNonOverlappingSubarrays._helper(Lsums, Msums, L, M)
        
        # 情况2: M长度子数组在L长度子数组前面
        result2 = Code11_MaximumSumTwoNonOverlappingSubarrays._helper(Msums, Lsums, M, L)
        
        return max(result1, result2)
    
    @staticmethod
    def _helper(first_sums, second_sums, first_l, second_l):
        """辅助函数，计算一种情况下的最大和"""
        # 计算first_sums的前缀最大值
        first_prefix_max = [0] * len(first_sums)
        first_prefix_max[0] = first_sums[0]
        for i in range(1, len(first_sums)):
            first_prefix_max[i] = max(first_prefix_max[i - 1], first_sums[i])
        
        # 计算second_sums的后缀最大值
        second_suffix_max = [0] * len(second_sums)
        second_suffix_max[-1] = second_sums[-1]
        for i in range(len(second_sums) - 2, -1, -1):
            second_suffix_max[i] = max(second_suffix_max[i + 1], second_sums[i])
        
        # 枚举分界点，计算最大和
        max_sum = 0
        for i in range(len(first_sums)):
            # 确保不会越界
            second_index = i + first_l
            if second_index < len(second_sums):
                current_sum = first_prefix_max[i] + second_suffix_max[second_index]
                max_sum = max(max_sum, current_sum)
        
        return max_sum
    
    '''
    相关题目扩展:
    1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
    2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
    3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
    4. LeetCode 1031. 两个无重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
    5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
    '''


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    A1 = [0, 6, 5, 2, 2, 5, 1, 9, 4]
    L1 = 1
    M1 = 2
    result1 = Code11_MaximumSumTwoNonOverlappingSubarrays.maxSumTwoNoOverlap(A1, L1, M1)
    print(f"输入数组: {A1}, L={L1}, M={M1}")
    print(f"两个无重叠子数组的最大和: {result1}")
    # 预期输出: 20

    # 测试用例2
    A2 = [2, 1, 5, 6, 0, 9, 5, 0, 3, 8]
    L2 = 4
    M2 = 3
    result2 = Code11_MaximumSumTwoNonOverlappingSubarrays.maxSumTwoNoOverlap(A2, L2, M2)
    print(f"输入数组: {A2}, L={L2}, M={M2}")
    print(f"两个无重叠子数组的最大和: {result2}")
    # 预期输出: 31

    # 测试用例3
    A3 = [3, 8, 1, 3, 2]
    L3 = 3
    M3 = 2
    result3 = Code11_MaximumSumTwoNonOverlappingSubarrays.maxSumTwoNoOverlap(A3, L3, M3)
    print(f"输入数组: {A3}, L={L3}, M={M3}")
    print(f"两个无重叠子数组的最大和: {result3}")
    # 预期输出: 12
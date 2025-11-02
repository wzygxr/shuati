# HackerRank Maximum Subarray Sum
# 给定一个元素数组和一个整数，确定任何子数组的和模的最大值。
# 测试链接 : https://www.hackerrank.com/challenges/maximum-subarray-sum/problem


"""
解题思路:
这是最大子数组和问题的模运算变种。我们需要找到一个子数组，使得其和对m取模后最大。

暴力解法是枚举所有子数组，计算它们的和模m，但时间复杂度为O(n^2)，对于大数组会超时。

优化解法使用前缀和和有序集合：
1. 计算前缀和数组prefixSum，其中prefixSum[i] = (a[0] + a[1] + ... + a[i-1]) % m
2. 对于每个前缀和prefixSum[i]，我们需要找到一个之前的前缀和prefixSum[j] (j < i)，
   使得(prefixSum[i] - prefixSum[j]) % m最大。
3. 这等价于找到一个prefixSum[j]，使得prefixSum[j]最接近prefixSum[i]但小于prefixSum[i]。
4. 如果prefixSum[i]是最小的，那么答案就是prefixSum[i]本身。
5. 使用有序集合（如bisect模块）来维护之前的前缀和，可以快速找到最接近的值。

时间复杂度: O(n log n) - 遍历数组需要O(n)，每次在有序集合中查找需要O(log n)
空间复杂度: O(n) - 需要存储前缀和和有序集合

是否最优解: 是，这是该问题的最优解法
"""


import bisect

class Code12_HackerRankMaximumSubarraySum:
    @staticmethod
    def maximumSum(a, m):
        """
        计算最大子数组和模值

        Args:
            a: List[int] - 输入的整数数组
            m: int - 模数

        Returns:
            int - 最大子数组和模值
        """
        # 使用列表维护之前的前缀和（保持有序）
        prefix_list = []
        # 当前前缀和
        prefix_sum = 0
        # 最大模值
        max_mod_sum = 0
        
        for num in a:
            # 更新前缀和
            prefix_sum = (prefix_sum + num) % m
            
            # 更新最大模值
            max_mod_sum = max(max_mod_sum, prefix_sum)
            
            # 在prefix_list中找到第一个大于prefix_sum的位置
            pos = bisect.bisect_right(prefix_list, prefix_sum)
            
            # 如果找到了这样的元素
            if pos < len(prefix_list):
                # 计算(prefix_sum - prefix_list[pos] + m) % m
                mod_sum = (prefix_sum - prefix_list[pos] + m) % m
                max_mod_sum = max(max_mod_sum, mod_sum)
            
            # 将当前前缀和插入到有序列表中
            bisect.insort(prefix_list, prefix_sum)
        
        return max_mod_sum
    
    '''
    相关题目扩展:
    1. HackerRank Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
    2. HackerRank The Maximum Subarray - https://www.hackerrank.com/challenges/maxsubarray/problem
    3. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
    4. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
    5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
    '''


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    a1 = [3, 3, 9, 9, 5]
    m1 = 7
    result1 = Code12_HackerRankMaximumSubarraySum.maximumSum(a1, m1)
    print(f"输入数组: {a1}, m={m1}")
    print(f"最大子数组和模值: {result1}")
    # 预期输出: 6

    # 测试用例2
    a2 = [1, 2, 3]
    m2 = 2
    result2 = Code12_HackerRankMaximumSubarraySum.maximumSum(a2, m2)
    print(f"输入数组: {a2}, m={m2}")
    print(f"最大子数组和模值: {result2}")
    # 预期输出: 1
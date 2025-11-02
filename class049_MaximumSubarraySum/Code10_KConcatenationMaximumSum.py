# LeetCode 1191. K 次串联后最大子数组之和
# 给你一个整数数组 arr 和一个整数 k。  
# 首先，需要检查是否可以获得一个长度为 k 的非空子数组，使得该子数组的和最大。
# （子数组是数组中连续的一部分）。
# 测试链接 : https://leetcode.cn/problems/k-concatenation-maximum-sum/


"""
解题思路:
这是最大子数组和问题的K次串联变种。我们需要考虑以下几种情况：
1. 当k=1时，直接求最大子数组和
2. 当k>=2时，需要考虑：
   a. 最大子数组完全在第一个数组中
   b. 最大子数组完全在最后一个数组中
   c. 最大子数组跨越多个数组，这种情况下可以分解为：
      - 前缀最大和 + 中间完整数组的和 + 后缀最大和

具体分析：
1. 如果数组总和为正数，那么中间的(k-2)个完整数组都应该包含在结果中
2. 前缀最大和是从数组开始到某个位置的最大和
3. 后缀最大和是从某个位置到数组结束的最大和
4. 最终结果是：max(单个数组最大子数组和, 前缀最大和 + (k-2)*总和 + 后缀最大和)

注意：题目允许子数组为空，答案最小是0，不可能是负数

时间复杂度: O(n) - 需要遍历数组常数次
空间复杂度: O(1) - 只需要常数个变量存储状态

是否最优解: 是，这是该问题的最优解法
"""


class Code10_KConcatenationMaximumSum:
    @staticmethod
    def kConcatenationMaxSum(arr, k):
        """
        计算K次串联后最大子数组之和

        Args:
            arr: List[int] - 输入的整数数组
            k: int - 串联次数

        Returns:
            int - K次串联后最大子数组之和
        """
        MOD = 1000000007
        
        # 特殊情况处理
        if not arr or k == 0:
            return 0
        
        # 计算数组总和
        total_sum = sum(arr)
        
        # 当k=1时，直接求最大子数组和
        if k == 1:
            return int(Code10_KConcatenationMaximumSum._kadane(arr) % MOD)
        
        # 当k>=2时，计算前缀最大和和后缀最大和
        prefix_max = Code10_KConcatenationMaximumSum._max_prefix_sum(arr)
        suffix_max = Code10_KConcatenationMaximumSum._max_suffix_sum(arr)
        
        # 如果总和为正，中间的(k-2)个数组都应该包含
        middle_sum = 0
        if total_sum > 0:
            middle_sum = ((k - 2) * total_sum) % MOD
        
        # 跨越多个数组的最大和
        cross_sum = (prefix_max + middle_sum + suffix_max) % MOD
        
        # 单个数组中的最大子数组和
        single_max = Code10_KConcatenationMaximumSum._kadane(arr)
        
        # 返回较大值，且不小于0
        result = max(cross_sum, single_max)
        return int(result if result > 0 else 0)
    
    @staticmethod
    def _kadane(arr):
        """Kadane算法求最大子数组和"""
        dp = arr[0]
        max_sum = max(arr[0], 0)
        
        for i in range(1, len(arr)):
            dp = max(arr[i], dp + arr[i])
            max_sum = max(max_sum, dp)
        
        return max(max_sum, 0)
    
    @staticmethod
    def _max_prefix_sum(arr):
        """计算前缀最大和"""
        sum_val = 0
        max_sum = 0
        
        for num in arr:
            sum_val += num
            max_sum = max(max_sum, sum_val)
        
        return max_sum
    
    @staticmethod
    def _max_suffix_sum(arr):
        """计算后缀最大和"""
        sum_val = 0
        max_sum = 0
        
        for i in range(len(arr) - 1, -1, -1):
            sum_val += arr[i]
            max_sum = max(max_sum, sum_val)
        
        return max_sum
    
    '''
    相关题目扩展:
    1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
    2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
    3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
    4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
    5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
    '''


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    arr1 = [1, 2]
    k1 = 3
    result1 = Code10_KConcatenationMaximumSum.kConcatenationMaxSum(arr1, k1)
    print(f"输入数组: {arr1}, k={k1}")
    print(f"K次串联后最大子数组之和: {result1}")
    # 预期输出: 9 ([1, 2, 1, 2, 1, 2]的最大子数组和)

    # 测试用例2
    arr2 = [1, -2, 1]
    k2 = 5
    result2 = Code10_KConcatenationMaximumSum.kConcatenationMaxSum(arr2, k2)
    print(f"输入数组: {arr2}, k={k2}")
    print(f"K次串联后最大子数组之和: {result2}")
    # 预期输出: 2

    # 测试用例3
    arr3 = [-1, -2]
    k3 = 7
    result3 = Code10_KConcatenationMaximumSum.kConcatenationMaxSum(arr3, k3)
    print(f"输入数组: {arr3}, k={k3}")
    print(f"K次串联后最大子数组之和: {result3}")
    # 预期输出: 0
from typing import List

class Solution:
    """
    LeetCode 2270. 分割数组的方案数 (Number of Ways to Split Array)
    
    题目描述:
    给你一个下标从 0 开始长度为 n 的整数数组 nums。
    如果以下描述为真，那么 nums 在下标 i 处有一个 合法分割：
    1. 前 i + 1 个元素的和 大于等于 剩下的 n - i - 1 个元素的和。
    2. 前 i + 1 个元素的和 大于等于 剩下的 n - i - 1 个元素的和。
    注意，第 i + 1 个元素 包含在 前 i + 1 个元素中。
    请你返回 nums 中的 合法分割 方案数。
    
    示例:
    输入: nums = [10,4,-8,7]
    输出: 2
    解释: 总共有 3 种分割方案：
    - 下标 0 处分割：10 和 4,-8,7 的和，10 >= 4-8+7=3
    - 下标 1 处分割：10,4 和 -8,7 的和，14 >= -8+7=-1
    - 下标 2 处分割：10,4,-8 和 7 的和，6 >= 7
    只有下标 0 和 1 处是合法分割。
    
    提示:
    2 <= nums.length <= 10^5
    -10^5 <= nums[i] <= 10^5
    
    题目链接: https://leetcode.com/problems/number-of-ways-to-split-array/
    
    解题思路:
    使用前缀和技巧来优化区间和的计算。
    1. 计算整个数组的总和 totalSum
    2. 从左到右遍历数组，维护当前前缀和 leftSum
    3. 对于每个位置 i，检查 leftSum >= totalSum - leftSum 是否成立
    4. 统计满足条件的分割点数量
    
    时间复杂度: O(n) - 需要遍历数组两次（一次计算总和，一次遍历检查）
    空间复杂度: O(1) - 只需要常数级别的额外空间
    
    这是最优解，因为需要遍历整个数组来计算前缀和。
    """
    
    def waysToSplitArray(self, nums: List[int]) -> int:
        """
        计算合法分割方案数
        
        Args:
            nums: 输入数组
            
        Returns:
            合法分割方案数
        """
        # 边界情况处理
        if not nums or len(nums) < 2:
            return 0
        
        # 计算整个数组的总和
        total_sum = sum(nums)
        
        # 统计合法分割方案数
        count = 0
        left_sum = 0
        
        # 遍历数组，检查每个可能的分割点
        # 注意：最后一个位置不能分割，因为右边没有元素
        for i in range(len(nums) - 1):
            left_sum += nums[i]
            right_sum = total_sum - left_sum
            
            # 检查分割条件：左边和 >= 右边和
            if left_sum >= right_sum:
                count += 1
        
        return count

def test_ways_to_split_array():
    """
    测试用例
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [10, 4, -8, 7]
    result1 = solution.waysToSplitArray(nums1)
    # 预期输出: 2
    print(f"测试用例1: {result1}")

    # 测试用例2
    nums2 = [2, 3, 1, 0]
    result2 = solution.waysToSplitArray(nums2)
    # 预期输出: 2
    print(f"测试用例2: {result2}")
    
    # 测试用例3
    nums3 = [0, 0]
    result3 = solution.waysToSplitArray(nums3)
    # 预期输出: 1 (左边和0 >= 右边和0)
    print(f"测试用例3: {result3}")
    
    # 测试用例4
    nums4 = [1, 1, 1]
    result4 = solution.waysToSplitArray(nums4)
    # 预期输出: 1 (在位置0分割：1 >= 2? 不成立；在位置1分割：2 >= 1? 成立)
    print(f"测试用例4: {result4}")
    
    # 测试用例5 - 大数测试
    nums5 = [100000, 100000, 100000, 100000]
    result5 = solution.waysToSplitArray(nums5)
    # 预期输出: 3 (每个分割点都满足条件)
    print(f"测试用例5: {result5}")

if __name__ == "__main__":
    test_ways_to_split_array()
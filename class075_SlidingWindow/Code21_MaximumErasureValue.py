from typing import List

class Solution:
    """
    1695. 删除子数组的最大得分
    给你一个正整数数组 nums ，请你从中删除一个含有 若干不同元素 的子数组。删除子数组的 得分 就是子数组各元素之 和 。
    返回 只删除一个 子数组可获得的 最大得分 。
    如果数组为空，返回 0 。
    
    解题思路：
    使用滑动窗口维护一个不含重复元素的子数组
    当遇到重复元素时，收缩左边界直到没有重复元素
    在滑动过程中记录最大和
    
    时间复杂度：O(n)，其中n是数组长度
    空间复杂度：O(k)，k是不同元素的数量
    
    是否最优解：是
    
    测试链接：https://leetcode.cn/problems/maximum-erasure-value/
    """
    
    def maximumUniqueSubarray(self, nums: List[int]) -> int:
        """
        计算删除子数组的最大得分
        
        Args:
            nums: 正整数数组
            
        Returns:
            最大得分
        """
        n = len(nums)
        max_score = 0  # 最大得分
        current_sum = 0  # 当前窗口的和
        left = 0  # 窗口左边界
        window = set()  # 记录窗口内的元素
        
        # 滑动窗口右边界
        for right in range(n):
            # 如果当前元素已经在窗口中，收缩左边界
            while nums[right] in window:
                current_sum -= nums[left]
                window.remove(nums[left])
                left += 1
            
            # 添加当前元素到窗口
            window.add(nums[right])
            current_sum += nums[right]
            
            # 更新最大得分
            max_score = max(max_score, current_sum)
        
        return max_score
    
    def maximumUniqueSubarrayOptimized(self, nums: List[int]) -> int:
        """
        优化版本：使用哈希表记录元素最后一次出现的位置
        时间复杂度：O(n)，空间复杂度：O(k)
        """
        n = len(nums)
        max_score = 0
        current_sum = 0
        left = 0
        last_seen = {}  # 记录元素最后一次出现的位置
        
        for right in range(n):
            num = nums[right]
            
            # 如果当前元素已经在窗口中，并且位置在left之后
            if num in last_seen and last_seen[num] >= left:
                # 移动左边界到重复元素的下一个位置
                duplicate_index = last_seen[num]
                for i in range(left, duplicate_index + 1):
                    current_sum -= nums[i]
                left = duplicate_index + 1
            
            # 更新当前元素的位置
            last_seen[num] = right
            current_sum += num
            
            # 更新最大得分
            max_score = max(max_score, current_sum)
        
        return max_score
    
    def maximumUniqueSubarrayWithPrefixSum(self, nums: List[int]) -> int:
        """
        使用前缀和数组优化版本
        时间复杂度：O(n)，空间复杂度：O(n)
        """
        n = len(nums)
        if n == 0:
            return 0
        
        # 计算前缀和数组
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + nums[i]
        
        max_score = 0
        left = 0
        last_seen = {}
        
        for right in range(n):
            num = nums[right]
            
            # 如果当前元素已经在窗口中，并且位置在left之后
            if num in last_seen and last_seen[num] >= left:
                left = last_seen[num] + 1
            
            # 更新当前元素的位置
            last_seen[num] = right
            
            # 计算当前窗口的和
            current_sum = prefix_sum[right + 1] - prefix_sum[left]
            max_score = max(max_score, current_sum)
        
        return max_score
    
    def maximumUniqueSubarrayWithArray(self, nums: List[int]) -> int:
        """
        使用数组代替哈希表（当数字范围有限时）
        时间复杂度：O(n)，空间复杂度：O(max_value)
        """
        n = len(nums)
        if n == 0:
            return 0
        
        # 找到数组中的最大值
        max_val = max(nums) if nums else 0
        
        max_score = 0
        current_sum = 0
        left = 0
        in_window = [False] * (max_val + 1)  # 记录元素是否在窗口中
        
        for right in range(n):
            num = nums[right]
            
            # 如果当前元素已经在窗口中，收缩左边界
            while in_window[num]:
                current_sum -= nums[left]
                in_window[nums[left]] = False
                left += 1
            
            # 添加当前元素到窗口
            in_window[num] = True
            current_sum += num
            
            # 更新最大得分
            max_score = max(max_score, current_sum)
        
        return max_score


def test_maximum_unique_subarray():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [4, 2, 4, 5, 6]
    result1 = solution.maximumUniqueSubarray(nums1)
    print(f"输入数组: {nums1}")
    print(f"最大得分: {result1}")
    print("预期: 17")
    print()
    
    # 测试用例2
    nums2 = [5, 2, 1, 2, 5, 2, 1, 2, 5]
    result2 = solution.maximumUniqueSubarray(nums2)
    print(f"输入数组: {nums2}")
    print(f"最大得分: {result2}")
    print("预期: 8")
    print()
    
    # 测试用例3：所有元素都不同
    nums3 = [1, 2, 3, 4, 5]
    result3 = solution.maximumUniqueSubarray(nums3)
    print(f"输入数组: {nums3}")
    print(f"最大得分: {result3}")
    print("预期: 15")
    print()
    
    # 测试用例4：所有元素都相同
    nums4 = [1, 1, 1, 1, 1]
    result4 = solution.maximumUniqueSubarray(nums4)
    print(f"输入数组: {nums4}")
    print(f"最大得分: {result4}")
    print("预期: 1")
    print()
    
    # 测试用例5：边界情况，单个元素
    nums5 = [5]
    result5 = solution.maximumUniqueSubarray(nums5)
    print(f"输入数组: {nums5}")
    print(f"最大得分: {result5}")
    print("预期: 5")
    print()
    
    # 测试用例6：空数组
    nums6 = []
    result6 = solution.maximumUniqueSubarray(nums6)
    print(f"输入数组: {nums6}")
    print(f"最大得分: {result6}")
    print("预期: 0")


if __name__ == "__main__":
    test_maximum_unique_subarray()
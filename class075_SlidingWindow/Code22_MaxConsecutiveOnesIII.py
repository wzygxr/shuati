from typing import List

class Solution:
    """
    1004. 最大连续1的个数 III
    给定一个二进制数组 nums 和一个整数 k，如果可以翻转最多 k 个 0 ，则返回 数组中连续 1 的最大个数 。
    
    解题思路：
    使用滑动窗口维护一个最多包含k个0的窗口
    当窗口内0的个数超过k时，收缩左边界
    在滑动过程中记录最大窗口大小
    
    时间复杂度：O(n)，其中n是数组长度
    空间复杂度：O(1)
    
    是否最优解：是
    
    测试链接：https://leetcode.cn/problems/max-consecutive-ones-iii/
    """
    
    def longestOnes(self, nums: List[int], k: int) -> int:
        """
        计算最大连续1的个数（最多翻转k个0）
        
        Args:
            nums: 二进制数组
            k: 最多可以翻转的0的个数
            
        Returns:
            最大连续1的个数
        """
        n = len(nums)
        max_length = 0  # 最大长度
        left = 0         # 窗口左边界
        zero_count = 0   # 窗口内0的个数
        
        # 滑动窗口右边界
        for right in range(n):
            # 如果当前元素是0，增加0的计数
            if nums[right] == 0:
                zero_count += 1
            
            # 如果窗口内0的个数超过k，收缩左边界
            while zero_count > k:
                if nums[left] == 0:
                    zero_count -= 1
                left += 1
            
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
        
        return max_length
    
    def longestOnesOptimized(self, nums: List[int], k: int) -> int:
        """
        优化版本：使用更简洁的写法
        时间复杂度：O(n)，空间复杂度：O(1)
        """
        n = len(nums)
        max_length = 0
        left = 0
        zero_count = 0
        
        for right in range(n):
            zero_count += 1 - nums[right]  # 如果nums[right]是0，则zero_count加1
            
            if zero_count > k:
                zero_count -= 1 - nums[left]  # 如果nums[left]是0，则zero_count减1
                left += 1
            
            max_length = max(max_length, right - left + 1)
        
        return max_length
    
    def longestOnesAlternative(self, nums: List[int], k: int) -> int:
        """
        另一种思路：使用双指针，不显式维护zero_count
        时间复杂度：O(n)，空间复杂度：O(1)
        """
        n = len(nums)
        max_length = 0
        left = 0
        right = 0
        zeros = 0
        
        while right < n:
            # 扩展右边界
            if nums[right] == 0:
                zeros += 1
            right += 1
            
            # 如果0的个数超过k，收缩左边界
            while zeros > k:
                if nums[left] == 0:
                    zeros -= 1
                left += 1
            
            # 更新最大长度
            max_length = max(max_length, right - left)
        
        return max_length
    
    def longestOnesWithPrefixSum(self, nums: List[int], k: int) -> int:
        """
        使用前缀和思想（当k较大时效率更高）
        时间复杂度：O(n)，空间复杂度：O(1)
        """
        n = len(nums)
        max_length = 0
        left = 0
        zero_count = 0
        
        for right in range(n):
            if nums[right] == 0:
                zero_count += 1
            
            # 如果0的个数超过k，移动左边界
            if zero_count > k:
                if nums[left] == 0:
                    zero_count -= 1
                left += 1
            
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
        
        return max_length


def test_longest_ones():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0]
    k1 = 2
    result1 = solution.longestOnes(nums1, k1)
    print(f"输入数组: {nums1}")
    print(f"k = {k1}")
    print(f"最大连续1的个数: {result1}")
    print("预期: 6")
    print()
    
    # 测试用例2
    nums2 = [0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1]
    k2 = 3
    result2 = solution.longestOnes(nums2, k2)
    print(f"输入数组: {nums2}")
    print(f"k = {k2}")
    print(f"最大连续1的个数: {result2}")
    print("预期: 10")
    print()
    
    # 测试用例3：k=0
    nums3 = [1, 1, 0, 1, 1, 1]
    k3 = 0
    result3 = solution.longestOnes(nums3, k3)
    print(f"输入数组: {nums3}")
    print(f"k = {k3}")
    print(f"最大连续1的个数: {result3}")
    print("预期: 3")
    print()
    
    # 测试用例4：k大于0的个数
    nums4 = [0, 0, 0, 0]
    k4 = 2
    result4 = solution.longestOnes(nums4, k4)
    print(f"输入数组: {nums4}")
    print(f"k = {k4}")
    print(f"最大连续1的个数: {result4}")
    print("预期: 2")
    print()
    
    # 测试用例5：全是1
    nums5 = [1, 1, 1, 1, 1]
    k5 = 2
    result5 = solution.longestOnes(nums5, k5)
    print(f"输入数组: {nums5}")
    print(f"k = {k5}")
    print(f"最大连续1的个数: {result5}")
    print("预期: 5")
    print()
    
    # 测试用例6：边界情况，单个元素
    nums6 = [0]
    k6 = 1
    result6 = solution.longestOnes(nums6, k6)
    print(f"输入数组: {nums6}")
    print(f"k = {k6}")
    print(f"最大连续1的个数: {result6}")
    print("预期: 1")


if __name__ == "__main__":
    test_longest_ones()
from typing import List

class Solution:
    """
    1493. 删掉一个元素以后全为 1 的最长子数组
    给你一个二进制数组 nums ，你需要从中删掉一个元素。
    请你在删掉元素的结果数组中，返回最长的且只包含 1 的非空子数组的长度。
    如果不存在这样的子数组，请返回 0 。
    
    解题思路：
    使用滑动窗口维护一个最多包含1个0的窗口
    当窗口内0的个数超过1时，收缩左边界
    最终结果是窗口大小减1（因为要删除一个元素）
    
    时间复杂度：O(n)，其中n是数组长度
    空间复杂度：O(1)
    
    是否最优解：是
    
    测试链接：https://leetcode.cn/problems/longest-subarray-of-1s-after-deleting-one-element/
    """
    
    def longestSubarray(self, nums: List[int]) -> int:
        """
        计算删掉一个元素后全为1的最长子数组长度
        
        Args:
            nums: 二进制数组
            
        Returns:
            最长子数组长度
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
            
            # 如果窗口内0的个数超过1，收缩左边界
            while zero_count > 1:
                if nums[left] == 0:
                    zero_count -= 1
                left += 1
            
            # 更新最大长度（窗口大小减1，因为要删除一个元素）
            max_length = max(max_length, right - left)
        
        return max_length
    
    def longestSubarrayOptimized(self, nums: List[int]) -> int:
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
            
            while zero_count > 1:
                zero_count -= 1 - nums[left]  # 如果nums[left]是0，则zero_count减1
                left += 1
            
            max_length = max(max_length, right - left)
        
        return max_length
    
    def longestSubarrayAlternative(self, nums: List[int]) -> int:
        """
        另一种思路：计算连续1的段，然后考虑删除中间的一个0来连接两段1
        时间复杂度：O(n)，空间复杂度：O(1)
        """
        n = len(nums)
        max_length = 0
        prev = 0  # 前一段连续1的长度
        curr = 0  # 当前连续1的长度
        has_zero = False  # 是否包含0
        
        for i in range(n):
            if nums[i] == 1:
                curr += 1
            else:
                has_zero = True
                # 遇到0时，可以删除这个0来连接prev和curr
                max_length = max(max_length, prev + curr)
                prev = curr
                curr = 0
        
        # 处理最后一段
        max_length = max(max_length, prev + curr)
        
        # 如果整个数组都是1，需要删除一个元素
        if not has_zero:
            return n - 1
        
        return max_length


def test_longest_subarray():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 1, 0, 1]
    result1 = solution.longestSubarray(nums1)
    print(f"输入数组: {nums1}")
    print(f"最长子数组长度: {result1}")
    print("预期: 3")
    print()
    
    # 测试用例2
    nums2 = [0, 1, 1, 1, 0, 1, 1, 0, 1]
    result2 = solution.longestSubarray(nums2)
    print(f"输入数组: {nums2}")
    print(f"最长子数组长度: {result2}")
    print("预期: 5")
    print()
    
    # 测试用例3：全是1
    nums3 = [1, 1, 1]
    result3 = solution.longestSubarray(nums3)
    print(f"输入数组: {nums3}")
    print(f"最长子数组长度: {result3}")
    print("预期: 2")
    print()
    
    # 测试用例4：全是0
    nums4 = [0, 0, 0]
    result4 = solution.longestSubarray(nums4)
    print(f"输入数组: {nums4}")
    print(f"最长子数组长度: {result4}")
    print("预期: 0")
    print()
    
    # 测试用例5：边界情况，单个元素
    nums5 = [1]
    result5 = solution.longestSubarray(nums5)
    print(f"输入数组: {nums5}")
    print(f"最长子数组长度: {result5}")
    print("预期: 0")
    print()
    
    # 测试用例6：交替的0和1
    nums6 = [1, 0, 1, 0, 1]
    result6 = solution.longestSubarray(nums6)
    print(f"输入数组: {nums6}")
    print(f"最长子数组长度: {result6}")
    print("预期: 2")


if __name__ == "__main__":
    test_longest_subarray()
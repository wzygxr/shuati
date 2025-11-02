from typing import List
from collections import defaultdict

class Solution:
    """
    992. K 个不同整数的子数组
    给定一个正整数数组 nums 和一个整数 k，返回 nums 中「好子数组」的数目。
    如果某个子数组中不同整数的个数恰好为 k，则称其为「好子数组」。
    
    解题思路：
    使用滑动窗口的变种：恰好K个不同整数的子数组数量 = 最多K个不同整数的子数组数量 - 最多K-1个不同整数的子数组数量
    
    时间复杂度：O(n)，其中n是数组长度
    空间复杂度：O(k)，用于存储不同整数的哈希表
    
    是否最优解：是
    
    测试链接：https://leetcode.cn/problems/subarrays-with-k-different-integers/
    """
    
    def subarraysWithKDistinct(self, nums: List[int], k: int) -> int:
        """
        计算恰好包含K个不同整数的子数组数量
        
        Args:
            nums: 输入数组
            k: 不同整数的个数
            
        Returns:
            恰好包含K个不同整数的子数组数量
        """
        # 恰好K个不同 = 最多K个不同 - 最多K-1个不同
        return self.at_most_k_distinct(nums, k) - self.at_most_k_distinct(nums, k - 1)
    
    def at_most_k_distinct(self, nums: List[int], k: int) -> int:
        """
        计算最多包含K个不同整数的子数组数量
        
        Args:
            nums: 输入数组
            k: 最多不同整数的个数
            
        Returns:
            最多包含K个不同整数的子数组数量
        """
        if k < 0:
            return 0
        
        n = len(nums)
        count = 0  # 子数组数量
        left = 0   # 窗口左边界
        freq = defaultdict(int)  # 记录每个数字的出现频率
        
        # 滑动窗口右边界
        for right in range(n):
            # 添加右边界元素
            freq[nums[right]] += 1
            
            # 如果不同数字数量超过k，收缩左边界
            while len(freq) > k:
                # 移除左边界元素
                freq[nums[left]] -= 1
                if freq[nums[left]] == 0:
                    del freq[nums[left]]
                left += 1
            
            # 以right结尾的，满足条件的子数组数量为 right - left + 1
            count += right - left + 1
        
        return count


class SolutionDirect:
    """
    直接解法：使用双指针和哈希表
    时间复杂度：O(n)，空间复杂度：O(k)
    """
    
    def subarraysWithKDistinct(self, nums: List[int], k: int) -> int:
        n = len(nums)
        count = 0
        
        # 记录每个数字最后一次出现的位置
        last_seen = {}
        left = 0  # 窗口左边界
        right = 0  # 窗口右边界
        
        while right < n:
            # 更新当前数字的最后出现位置
            last_seen[nums[right]] = right
            
            # 如果不同数字数量超过k，移动左边界
            while len(last_seen) > k:
                # 如果左边界数字的最后出现位置就是当前位置，从map中移除
                if last_seen.get(nums[left]) == left:
                    del last_seen[nums[left]]
                left += 1
            
            # 如果恰好有k个不同数字，计算以right结尾的子数组数量
            if len(last_seen) == k:
                # 找到最小的位置，使得从该位置到right的子数组恰好有k个不同数字
                min_index = right
                for index in last_seen.values():
                    min_index = min(min_index, index)
                count += min_index - left + 1
            
            right += 1
        
        return count


class SolutionOptimized:
    """
    优化版本：使用数组代替哈希表（当数字范围有限时）
    时间复杂度：O(n)，空间复杂度：O(max_value)
    """
    
    def subarraysWithKDistinct(self, nums: List[int], k: int) -> int:
        n = len(nums)
        if n == 0 or k == 0:
            return 0
        
        # 找到数组中的最大值，用于确定数组大小
        max_val = max(nums) if nums else 0
        
        freq = [0] * (max_val + 1)  # 频率数组
        distinct = 0  # 当前不同数字的数量
        count = 0
        left = 0
        
        # 使用双指针技巧
        for right in range(n):
            # 添加右边界元素
            if freq[nums[right]] == 0:
                distinct += 1
            freq[nums[right]] += 1
            
            # 收缩左边界，直到不同数字数量不超过k
            while distinct > k:
                freq[nums[left]] -= 1
                if freq[nums[left]] == 0:
                    distinct -= 1
                left += 1
            
            # 如果恰好有k个不同数字，计算数量
            if distinct == k:
                temp_left = left
                temp_distinct = distinct
                temp_freq = freq.copy()  # 复制频率数组
                
                # 计算以right结尾的恰好k个不同的子数组数量
                while temp_distinct == k:
                    count += 1
                    temp_freq[nums[temp_left]] -= 1
                    if temp_freq[nums[temp_left]] == 0:
                        temp_distinct -= 1
                    temp_left += 1
        
        return count


def test_subarrays_with_k_distinct():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 2, 1, 2, 3]
    k1 = 2
    result1 = solution.subarraysWithKDistinct(nums1, k1)
    print(f"输入数组: {nums1}")
    print(f"k = {k1}")
    print(f"恰好包含{k1}个不同整数的子数组数量: {result1}")
    print("预期: 7")
    print()
    
    # 测试用例2
    nums2 = [1, 2, 1, 3, 4]
    k2 = 3
    result2 = solution.subarraysWithKDistinct(nums2, k2)
    print(f"输入数组: {nums2}")
    print(f"k = {k2}")
    print(f"恰好包含{k2}个不同整数的子数组数量: {result2}")
    print("预期: 3")
    print()
    
    # 测试用例3：边界情况，k=0
    nums3 = [1, 2, 3]
    k3 = 0
    result3 = solution.subarraysWithKDistinct(nums3, k3)
    print(f"输入数组: {nums3}")
    print(f"k = {k3}")
    print(f"恰好包含{k3}个不同整数的子数组数量: {result3}")
    print("预期: 0")
    print()
    
    # 测试用例4：k=1
    nums4 = [1, 1, 1, 2, 2, 3]
    k4 = 1
    result4 = solution.subarraysWithKDistinct(nums4, k4)
    print(f"输入数组: {nums4}")
    print(f"k = {k4}")
    print(f"恰好包含{k4}个不同整数的子数组数量: {result4}")
    print("预期: 9")
    print()
    
    # 测试用例5：k等于数组长度
    nums5 = [1, 2, 3, 4, 5]
    k5 = 5
    result5 = solution.subarraysWithKDistinct(nums5, k5)
    print(f"输入数组: {nums5}")
    print(f"k = {k5}")
    print(f"恰好包含{k5}个不同整数的子数组数量: {result5}")
    print("预期: 1")


if __name__ == "__main__":
    test_subarrays_with_k_distinct()
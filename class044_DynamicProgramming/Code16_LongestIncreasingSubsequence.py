# 最长递增子序列 (Longest Increasing Subsequence, LIS)
# 题目链接: https://leetcode.cn/problems/longest-increasing-subsequence/
# 难度: 中等
# 这是一个经典的动态规划问题，也可以用贪心+二分查找优化
import bisect

class Solution:
    # 方法1: 暴力递归（超时解法，仅作为思路展示）
    # 时间复杂度: O(2^n) - 每个元素有选或不选两种选择
    # 空间复杂度: O(n) - 递归调用栈深度
    def lengthOfLIS1(self, nums):
        if not nums:
            return 0
        return self._process1(nums, 0, float('-inf'))
    
    def _process1(self, nums, index, prev_max):
        # 基本情况: 已经处理完所有元素
        if index == len(nums):
            return 0
        
        # 选择不使用当前元素
        not_take = self._process1(nums, index + 1, prev_max)
        
        # 选择使用当前元素（如果可以）
        take = 0
        if nums[index] > prev_max:
            take = 1 + self._process1(nums, index + 1, nums[index])
        
        # 返回两种选择中的最大值
        return max(not_take, take)
    
    # 方法2: 记忆化搜索（带备忘录的递归）
    # 时间复杂度: O(n^2) - 每个状态只计算一次，共有n^2个状态
    # 空间复杂度: O(n^2) - 备忘录大小
    def lengthOfLIS2(self, nums):
        if not nums:
            return 0
        n = len(nums)
        # 使用字典作为备忘录，键为(index, prev_max的表示)
        memo = {}
        return self._process2(nums, 0, -1, memo)
    
    def _process2(self, nums, index, prev_index, memo):
        if index == len(nums):
            return 0
        
        # 构建备忘录键
        key = (index, prev_index)
        if key in memo:
            return memo[key]
        
        # 不选当前元素
        not_take = self._process2(nums, index + 1, prev_index, memo)
        
        # 选当前元素（如果可以）
        take = 0
        if prev_index == -1 or nums[index] > nums[prev_index]:
            take = 1 + self._process2(nums, index + 1, index, memo)
        
        # 记录结果
        memo[key] = max(not_take, take)
        return memo[key]
    
    # 方法3: 动态规划（自底向上）
    # 时间复杂度: O(n^2) - 双重循环
    # 空间复杂度: O(n) - dp数组大小
    def lengthOfLIS3(self, nums):
        if not nums:
            return 0
        
        n = len(nums)
        # dp[i]表示以nums[i]结尾的最长递增子序列的长度
        dp = [1] * n
        
        max_length = 1
        # 遍历每个元素作为结尾
        for i in range(1, n):
            # 遍历i之前的所有元素
            for j in range(i):
                # 如果前面的元素小于当前元素，可以接在后面形成更长的递增子序列
                if nums[i] > nums[j]:
                    dp[i] = max(dp[i], dp[j] + 1)
            # 更新全局最大值
            max_length = max(max_length, dp[i])
        
        return max_length
    
    # 方法4: 贪心 + 二分查找（最优解）
    # 时间复杂度: O(n log n) - 遍历数组O(n)，每次二分查找O(log n)
    # 空间复杂度: O(n) - tails数组大小
    # 核心思想: 维护一个数组tails，其中tails[i]表示长度为i+1的所有递增子序列的结尾元素的最小值
    def lengthOfLIS4(self, nums):
        if not nums:
            return 0
        
        # tails[i]表示长度为i+1的递增子序列的末尾元素的最小值
        tails = []
        
        for num in nums:
            # 二分查找num在tails数组中的插入位置
            # 使用bisect_left找到第一个大于等于num的位置
            idx = bisect.bisect_left(tails, num)
            
            # 更新tails数组
            if idx == len(tails):
                tails.append(num)
            else:
                tails[idx] = num
        
        # tails数组的长度就是最长递增子序列的长度
        return len(tails)

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 标准测试
    nums1 = [10, 9, 2, 5, 3, 7, 101, 18]
    print("测试用例1结果:")
    print("暴力递归: ", solution.lengthOfLIS1(nums1))  # 预期输出: 4
    print("记忆化搜索: ", solution.lengthOfLIS2(nums1))  # 预期输出: 4
    print("动态规划: ", solution.lengthOfLIS3(nums1))  # 预期输出: 4
    print("贪心+二分: ", solution.lengthOfLIS4(nums1))  # 预期输出: 4
    
    # 测试用例2: 完全递增数组
    nums2 = [1, 2, 3, 4, 5]
    print("\n测试用例2结果:")
    print("贪心+二分: ", solution.lengthOfLIS4(nums2))  # 预期输出: 5
    
    # 测试用例3: 完全递减数组
    nums3 = [5, 4, 3, 2, 1]
    print("\n测试用例3结果:")
    print("贪心+二分: ", solution.lengthOfLIS4(nums3))  # 预期输出: 1
    
    # 测试用例4: 空数组
    nums4 = []
    print("\n测试用例4结果:")
    print("贪心+二分: ", solution.lengthOfLIS4(nums4))  # 预期输出: 0
    
    # 测试用例5: 单元素数组
    nums5 = [1]
    print("\n测试用例5结果:")
    print("贪心+二分: ", solution.lengthOfLIS4(nums5))  # 预期输出: 1
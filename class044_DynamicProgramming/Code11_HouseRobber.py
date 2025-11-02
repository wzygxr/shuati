# 打家劫舍 (House Robber)
# 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
# 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
# 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
# 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，
# 一夜之内能够偷窃到的最高金额。
# 测试链接 : https://leetcode.cn/problems/house-robber/

class Solution:
    # 方法1：暴力递归解法
    # 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    # 空间复杂度：O(n) - 递归调用栈的深度
    # 问题：存在大量重复计算，效率低下
    def rob1(self, nums: list[int]) -> int:
        return self.f1(nums, 0)
    
    # 从index位置开始，能够偷窃到的最高金额
    def f1(self, nums: list[int], index: int) -> int:
        if index >= len(nums):
            return 0
        # 选择1：偷当前房屋，跳过下一个房屋
        rob_current = nums[index] + self.f1(nums, index + 2)
        # 选择2：不偷当前房屋，考虑下一个房屋
        skip_current = self.f1(nums, index + 1)
        # 返回两种选择中的最大值
        return max(rob_current, skip_current)
    
    # 方法2：记忆化搜索（自顶向下动态规划）
    # 时间复杂度：O(n) - 每个状态只计算一次
    # 空间复杂度：O(n) - dp字典和递归调用栈
    # 优化：通过缓存已经计算的结果避免重复计算
    def rob2(self, nums: list[int]) -> int:
        dp = {}
        return self.f2(nums, 0, dp)
    
    # 从index位置开始，能够偷窃到的最高金额
    def f2(self, nums: list[int], index: int, dp: dict) -> int:
        if index >= len(nums):
            return 0
        if index in dp:
            return dp[index]
        # 选择1：偷当前房屋，跳过下一个房屋
        rob_current = nums[index] + self.f2(nums, index + 2, dp)
        # 选择2：不偷当前房屋，考虑下一个房屋
        skip_current = self.f2(nums, index + 1, dp)
        # 返回两种选择中的最大值
        ans = max(rob_current, skip_current)
        dp[index] = ans
        return ans
    
    # 方法3：动态规划（自底向上）
    # 时间复杂度：O(n) - 需要填满整个dp表
    # 空间复杂度：O(n) - dp数组存储所有状态
    # 优化：避免了递归调用的开销
    def rob3(self, nums: list[int]) -> int:
        n = len(nums)
        if n == 0:
            return 0
        if n == 1:
            return nums[0]
        
        # dp[i] 表示考虑前i+1个房屋，能够偷窃到的最高金额
        dp = [0] * n
        dp[0] = nums[0]
        dp[1] = max(nums[0], nums[1])
        
        # 填表过程
        for i in range(2, n):
            # 选择1：偷当前房屋，加上前i-2个房屋的最高金额
            rob_current = nums[i] + dp[i - 2]
            # 选择2：不偷当前房屋，等于前i-1个房屋的最高金额
            skip_current = dp[i - 1]
            dp[i] = max(rob_current, skip_current)
        return dp[n - 1]
    
    # 方法4：空间优化的动态规划
    # 时间复杂度：O(n) - 仍然需要计算所有状态
    # 空间复杂度：O(1) - 只保存必要的状态值
    # 优化：只保存必要的状态，大幅减少空间使用
    def rob4(self, nums: list[int]) -> int:
        n = len(nums)
        if n == 0:
            return 0
        if n == 1:
            return nums[0]
        
        # 只需要保存前两个状态值
        prev2 = nums[0]  # dp[i-2]
        prev1 = max(nums[0], nums[1])  # dp[i-1]
        
        if n == 2:
            return prev1
        
        # 填表过程
        for i in range(2, n):
            # 选择1：偷当前房屋，加上前i-2个房屋的最高金额
            rob_current = nums[i] + prev2
            # 选择2：不偷当前房屋，等于前i-1个房屋的最高金额
            skip_current = prev1
            current = max(rob_current, skip_current)
            
            # 更新状态值
            prev2 = prev1
            prev1 = current
        return prev1

# 测试用例和性能对比
if __name__ == "__main__":
    solution = Solution()
    print("测试打家劫舍实现：")
    
    # 测试用例1
    nums1 = [1, 2, 3, 1]
    print(f"nums = {nums1}")
    print(f"方法3 (动态规划): {solution.rob3(nums1)}")
    print(f"方法4 (空间优化): {solution.rob4(nums1)}")
    
    # 测试用例2
    nums2 = [2, 7, 9, 3, 1]
    print(f"\nnums = {nums2}")
    print(f"方法3 (动态规划): {solution.rob3(nums2)}")
    print(f"方法4 (空间优化): {solution.rob4(nums2)}")
    
    # 测试用例3
    nums3 = [5]
    print(f"\nnums = {nums3}")
    print(f"方法3 (动态规划): {solution.rob3(nums3)}")
    print(f"方法4 (空间优化): {solution.rob4(nums3)}")
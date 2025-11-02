# 跳跃游戏（Jump Game）
# 题目来源：LeetCode 55
# 题目链接：https://leetcode.cn/problems/jump-game/

"""
问题描述：
给定一个非负整数数组 nums，你最初位于数组的第一个位置。
数组中的每个元素代表你在该位置可以跳跃的最大长度。
判断你是否能够到达最后一个位置。

算法思路：
使用贪心策略，维护当前能到达的最远位置。
具体步骤：
1. 遍历数组，对于每个位置，更新能到达的最远位置
2. 如果最远位置超过或等于数组的最后一个位置，返回true
3. 如果在遍历过程中发现当前位置已经无法到达（即当前位置大于能到达的最远位置），返回false

时间复杂度：O(n)，其中n是数组长度，只需遍历数组一次
空间复杂度：O(1)，只使用了常数额外空间

是否最优解：是。贪心策略在此问题中能得到最优解。

适用场景：
1. 路径可达性问题
2. 资源约束下的可达性判断

异常处理：
1. 处理空数组情况
2. 处理数组长度为1的边界情况（已经在终点）

工程化考量：
1. 输入验证：检查数组是否为空
2. 边界条件：处理单元素数组
3. 性能优化：提前返回，一旦确定可以到达终点就立即返回
"""

class Solution:
    def canJump(self, nums):
        """
        判断是否能够到达数组的最后一个位置
        
        Args:
            nums: List[int] - 非负整数数组，每个元素表示在该位置可以跳跃的最大长度
            
        Returns:
            bool - 如果能够到达最后一个位置返回True，否则返回False
        """
        # 边界条件检查
        if not nums:
            return False
        
        # 如果数组只有一个元素，已经在终点
        if len(nums) == 1:
            return True
        
        max_reach = 0  # 当前能到达的最远位置
        
        # 遍历数组中的每个位置
        for i in range(len(nums)):
            # 如果当前位置已经无法到达，直接返回False
            if i > max_reach:
                return False
            
            # 更新能到达的最远位置
            max_reach = max(max_reach, i + nums[i])
            
            # 如果最远位置已经可以到达或超过最后一个位置，直接返回True
            if max_reach >= len(nums) - 1:
                return True
        
        # 理论上不会执行到这里，因为前面已经有检查
        return max_reach >= len(nums) - 1

# 测试函数，验证算法正确性
def test_can_jump():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 能到达终点
    nums1 = [2, 3, 1, 1, 4]
    result1 = solution.canJump(nums1)
    print("测试用例1:")
    print(f"数组: {nums1}")
    print(f"能否到达最后一个位置: {result1}")
    print(f"期望输出: True")
    print()
    
    # 测试用例2: 基本情况 - 不能到达终点
    nums2 = [3, 2, 1, 0, 4]
    result2 = solution.canJump(nums2)
    print("测试用例2:")
    print(f"数组: {nums2}")
    print(f"能否到达最后一个位置: {result2}")
    print(f"期望输出: False")
    print()
    
    # 测试用例3: 边界情况 - 单元素数组
    nums3 = [0]
    result3 = solution.canJump(nums3)
    print("测试用例3:")
    print(f"数组: {nums3}")
    print(f"能否到达最后一个位置: {result3}")
    print(f"期望输出: True")
    print()
    
    # 测试用例4: 边界情况 - 所有元素都是0
    nums4 = [0, 0, 0, 0]
    result4 = solution.canJump(nums4)
    print("测试用例4:")
    print(f"数组: {nums4}")
    print(f"能否到达最后一个位置: {result4}")
    print(f"期望输出: False")
    print()
    
    # 测试用例5: 复杂情况 - 大跳跃
    nums5 = [2, 0, 0]
    result5 = solution.canJump(nums5)
    print("测试用例5:")
    print(f"数组: {nums5}")
    print(f"能否到达最后一个位置: {result5}")
    print(f"期望输出: True")

# 运行测试
if __name__ == "__main__":
    test_can_jump()
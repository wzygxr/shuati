import time
import random
from typing import List

"""
LeetCode 55. 跳跃游戏 (Jump Game)

题目描述:
给定一个非负整数数组 nums，你最初位于数组的第一个位置。
数组中的每个元素代表你在该位置可以跳跃的最大长度。
判断你是否能够到达最后一个位置。

示例1:
输入: nums = [2,3,1,1,4]
输出: true
解释: 可以先跳 1 步，从位置 0 到达位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。

示例2:
输入: nums = [3,2,1,0,4]
输出: false
解释: 无论怎样，总会到达索引为 3 的位置。但该位置的最大跳跃长度是 0 ， 所以永远不能到达最后一个位置。

提示:
1 <= nums.length <= 10^4
0 <= nums[i] <= 10^5

题目链接: https://leetcode.com/problems/jump-game/

解题思路:
这道题可以使用贪心算法来解决。我们的目标是判断是否能够到达最后一个位置。

贪心策略：维护一个变量表示当前能够到达的最远位置。遍历数组，不断更新这个最远位置。
如果在任何时候，当前能够到达的最远位置小于当前遍历到的索引，说明无法到达该位置，也就无法到达最后一个位置。

具体来说，我们维护一个变量 max_reach，表示当前能够到达的最远位置。初始时，max_reach = 0。
遍历数组，对于每个位置 i，如果 i > max_reach，说明无法到达位置 i，返回 False。
否则，更新 max_reach = max(max_reach, i + nums[i])。
如果 max_reach >= len(nums) - 1，说明已经可以到达最后一个位置，返回 True。

时间复杂度: O(n)，其中 n 是数组的长度。我们只需要遍历数组一次。
空间复杂度: O(1)，只使用了常数级别的额外空间。

此外，我们还提供三种其他解法：
1. 动态规划解法（自顶向下）：时间复杂度 O(n^2)，空间复杂度 O(n)
2. 动态规划解法（自底向上）：时间复杂度 O(n^2)，空间复杂度 O(n)
3. 回溯解法：时间复杂度 O(2^n)，空间复杂度 O(n)，但在大规模输入时可能会超时
"""

class Solution:
    def can_jump_greedy(self, nums: List[int]) -> bool:
        """
        解法一: 贪心算法（最优解）
        
        Args:
            nums: 非负整数数组
        
        Returns:
            是否能够到达最后一个位置
        """
        # 参数校验
        if not nums or len(nums) <= 1:
            return True  # 如果数组为空或只有一个元素，已经在终点
        
        max_reach = 0  # 当前能够到达的最远位置
        
        # 遍历数组
        for i in range(len(nums)):
            # 如果当前位置已经无法到达，返回False
            if i > max_reach:
                return False
            
            # 更新能够到达的最远位置
            max_reach = max(max_reach, i + nums[i])
            
            # 如果已经可以到达或超过最后一个位置，可以提前返回True
            if max_reach >= len(nums) - 1:
                return True
        
        # 遍历完整个数组后，判断是否能够到达最后一个位置
        return max_reach >= len(nums) - 1
    
    def can_jump_dynamic_programming_top_down(self, nums: List[int]) -> bool:
        """
        解法二: 动态规划 - 自顶向下
        
        Args:
            nums: 非负整数数组
        
        Returns:
            是否能够到达最后一个位置
        """
        # 参数校验
        if not nums or len(nums) <= 1:
            return True  # 如果数组为空或只有一个元素，已经在终点
        
        n = len(nums)
        # memo[i]表示从位置i是否可以到达最后一个位置
        # 0: 未计算, 1: 可以到达, 2: 无法到达
        memo = [0] * n
        # 最后一个位置可以到达自身
        memo[n - 1] = 1
        
        def can_jump_from_position(pos: int) -> bool:
            # 如果已经计算过，直接返回结果
            if memo[pos] != 0:
                return memo[pos] == 1
            
            # 计算从当前位置可以到达的最远位置
            furthest_jump = min(pos + nums[pos], n - 1)
            
            # 尝试从当前位置跳到所有可能的位置
            for next_pos in range(pos + 1, furthest_jump + 1):
                if can_jump_from_position(next_pos):
                    memo[pos] = 1  # 可以到达
                    return True
            
            memo[pos] = 2  # 无法到达
            return False
        
        return can_jump_from_position(0)
    
    def can_jump_dynamic_programming_bottom_up(self, nums: List[int]) -> bool:
        """
        解法三: 动态规划 - 自底向上
        
        Args:
            nums: 非负整数数组
        
        Returns:
            是否能够到达最后一个位置
        """
        # 参数校验
        if not nums or len(nums) <= 1:
            return True  # 如果数组为空或只有一个元素，已经在终点
        
        n = len(nums)
        # dp[i]表示从位置i是否可以到达最后一个位置
        dp = [False] * n
        # 最后一个位置可以到达自身
        dp[n - 1] = True
        
        # 从后往前遍历
        for i in range(n - 2, -1, -1):
            # 计算从当前位置可以到达的最远位置
            furthest_jump = min(i + nums[i], n - 1)
            
            # 检查从当前位置能否跳到一个可以到达终点的位置
            for j in range(i + 1, furthest_jump + 1):
                if dp[j]:
                    dp[i] = True
                    break  # 一旦找到一个可达的位置，就可以停止检查
        
        # 返回是否可以从起始位置到达终点
        return dp[0]
    
    def can_jump_backtracking(self, nums: List[int]) -> bool:
        """
        解法四: 回溯（暴力解法，在大规模输入时可能会超时）
        
        Args:
            nums: 非负整数数组
        
        Returns:
            是否能够到达最后一个位置
        """
        # 参数校验
        if not nums or len(nums) <= 1:
            return True  # 如果数组为空或只有一个元素，已经在终点
        
        def can_jump_from_position(pos: int) -> bool:
            # 基本情况：已经到达最后一个位置
            if pos == len(nums) - 1:
                return True
            
            # 计算从当前位置可以到达的最远位置
            furthest_jump = min(pos + nums[pos], len(nums) - 1)
            
            # 尝试从当前位置跳到所有可能的位置（优先尝试跳得更远）
            for next_pos in range(furthest_jump, pos, -1):
                if can_jump_from_position(next_pos):
                    return True
            
            return False
        
        return can_jump_from_position(0)
    
    def can_jump(self, nums: List[int]) -> bool:
        """
        LeetCode官方接口的实现（使用贪心算法）
        
        Args:
            nums: 非负整数数组
        
        Returns:
            是否能够到达最后一个位置
        """
        return self.can_jump_greedy(nums)

"""
打印数组
"""
def print_array(arr: List[int]) -> None:
    print(f"[{', '.join(map(str, arr))}]")

"""
性能测试
"""
def performance_test(nums: List[int], solution: Solution) -> None:
    # 测试贪心算法
    start_time = time.time()
    result1 = solution.can_jump_greedy(nums)
    end_time = time.time()
    print(f"贪心算法结果: {result1}")
    print(f"贪心算法耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 测试动态规划自底向上
    start_time = time.time()
    result2 = solution.can_jump_dynamic_programming_bottom_up(nums)
    end_time = time.time()
    print(f"动态规划(自底向上)结果: {result2}")
    print(f"动态规划(自底向上)耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 注意：以下两种方法在大规模数组上可能会超时，所以只在小规模数组上测试
    if len(nums) <= 1000:
        # 测试动态规划自顶向下
        start_time = time.time()
        result3 = solution.can_jump_dynamic_programming_top_down(nums)
        end_time = time.time()
        print(f"动态规划(自顶向下)结果: {result3}")
        print(f"动态规划(自顶向下)耗时: {(end_time - start_time) * 1000:.2f}ms")
        
        # 测试回溯算法
        if len(nums) <= 30:  # 回溯算法在小数据量下才能快速运行
            start_time = time.time()
            result4 = solution.can_jump_backtracking(nums)
            end_time = time.time()
            print(f"回溯算法结果: {result4}")
            print(f"回溯算法耗时: {(end_time - start_time) * 1000:.2f}ms")
        else:
            print("数组过大，跳过回溯算法测试")
    else:
        print("数组过大，跳过动态规划(自顶向下)和回溯算法测试")

"""
生成测试用例
"""
def generate_test_case(n: int, can_reach_end: bool) -> List[int]:
    nums = [0] * n
    
    if can_reach_end:
        # 生成可以到达终点的数组
        for i in range(n - 1):
            # 确保可以到达终点，当前位置的值至少为n-1-i
            min_val = n - 1 - i
            rand_val = random.randint(1, 5)
            nums[i] = max(rand_val, min_val)
    else:
        # 生成无法到达终点的数组
        # 创建一个0，使得无法越过
        zero_position = random.randint(1, n - 2)  # 确保0不在最后一个位置
        nums[zero_position] = 0
        
        # 填充0之前的位置
        for i in range(zero_position):
            # 确保无法越过0
            max_val = zero_position - i
            rand_val = random.randint(1, 5)
            nums[i] = min(rand_val, max_val)
        
        # 填充0之后的位置
        for i in range(zero_position + 1, n):
            nums[i] = random.randint(1, 5)
    
    nums[n - 1] = 0  # 最后一个元素不影响
    return nums

def main():
    solution = Solution()
    
    # 测试用例1
    nums1 = [2, 3, 1, 1, 4]
    print("测试用例1:")
    print("nums = ", end="")
    print_array(nums1)
    print(f"贪心算法结果: {solution.can_jump_greedy(nums1)}")  # 预期输出: True
    print(f"动态规划(自顶向下)结果: {solution.can_jump_dynamic_programming_top_down(nums1)}")  # 预期输出: True
    print(f"动态规划(自底向上)结果: {solution.can_jump_dynamic_programming_bottom_up(nums1)}")  # 预期输出: True
    print(f"回溯算法结果: {solution.can_jump_backtracking(nums1)}")  # 预期输出: True
    print()
    
    # 测试用例2
    nums2 = [3, 2, 1, 0, 4]
    print("测试用例2:")
    print("nums = ", end="")
    print_array(nums2)
    print(f"贪心算法结果: {solution.can_jump_greedy(nums2)}")  # 预期输出: False
    print(f"动态规划(自顶向下)结果: {solution.can_jump_dynamic_programming_top_down(nums2)}")  # 预期输出: False
    print(f"动态规划(自底向上)结果: {solution.can_jump_dynamic_programming_bottom_up(nums2)}")  # 预期输出: False
    print(f"回溯算法结果: {solution.can_jump_backtracking(nums2)}")  # 预期输出: False
    print()
    
    # 测试用例3 - 边界情况：只有一个元素
    nums3 = [0]
    print("测试用例3（单元素数组）:")
    print("nums = ", end="")
    print_array(nums3)
    print(f"贪心算法结果: {solution.can_jump_greedy(nums3)}")  # 预期输出: True
    print(f"动态规划(自顶向下)结果: {solution.can_jump_dynamic_programming_top_down(nums3)}")  # 预期输出: True
    print(f"动态规划(自底向上)结果: {solution.can_jump_dynamic_programming_bottom_up(nums3)}")  # 预期输出: True
    print(f"回溯算法结果: {solution.can_jump_backtracking(nums3)}")  # 预期输出: True
    print()
    
    # 测试用例4 - 边界情况：全是0
    nums4 = [0, 0, 0, 0, 0]
    print("测试用例4（全是0）:")
    print("nums = ", end="")
    print_array(nums4)
    print(f"贪心算法结果: {solution.can_jump_greedy(nums4)}")  # 预期输出: False
    print(f"动态规划(自顶向下)结果: {solution.can_jump_dynamic_programming_top_down(nums4)}")  # 预期输出: False
    print(f"动态规划(自底向上)结果: {solution.can_jump_dynamic_programming_bottom_up(nums4)}")  # 预期输出: False
    print(f"回溯算法结果: {solution.can_jump_backtracking(nums4)}")  # 预期输出: False
    print()
    
    # 测试用例5 - 边界情况：可以一次跳到终点
    nums5 = [10, 0, 0, 0, 0]
    print("测试用例5（可以一次跳到终点）:")
    print("nums = ", end="")
    print_array(nums5)
    print(f"贪心算法结果: {solution.can_jump_greedy(nums5)}")  # 预期输出: True
    print(f"动态规划(自顶向下)结果: {solution.can_jump_dynamic_programming_top_down(nums5)}")  # 预期输出: True
    print(f"动态规划(自底向上)结果: {solution.can_jump_dynamic_programming_bottom_up(nums5)}")  # 预期输出: True
    print(f"回溯算法结果: {solution.can_jump_backtracking(nums5)}")  # 预期输出: True
    print()
    
    # 性能测试 - 小规模数组
    print("小规模数组性能测试（可以到达终点）:")
    small_array1 = generate_test_case(100, True)
    performance_test(small_array1, solution)
    print()
    
    print("小规模数组性能测试（无法到达终点）:")
    small_array2 = generate_test_case(100, False)
    performance_test(small_array2, solution)
    print()
    
    # 性能测试 - 大规模数组 - 只测试贪心算法，因为其他算法在大规模数组上会很慢
    print("大规模数组性能测试（只测试贪心算法）:")
    large_array = generate_test_case(10000, True)
    start_time = time.time()
    result = solution.can_jump_greedy(large_array)
    end_time = time.time()
    print(f"贪心算法结果: {result}")
    print(f"贪心算法耗时: {(end_time - start_time) * 1000:.2f}ms")

if __name__ == "__main__":
    main()
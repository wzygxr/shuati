import time
import random
from typing import List

"""
LeetCode 45. 跳跃游戏 II (Jump Game II)

题目描述:
给定一个非负整数数组 nums，你最初位于数组的第一个位置。
数组中的每个元素代表你在该位置可以跳跃的最大长度。
你的目标是使用最少的跳跃次数到达数组的最后一个位置。
假设你总是可以到达数组的最后一个位置。

示例1:
输入: nums = [2,3,1,1,4]
输出: 2
解释: 跳到最后一个位置的最小跳跃数是 2。
     从下标为 0 的位置跳到下标为 1 的位置，跳 1 步，然后跳 3 步到达数组的最后一个位置。

示例2:
输入: nums = [2,3,0,1,4]
输出: 2

提示:
1 <= nums.length <= 10^4
0 <= nums[i] <= 1000
题目保证可以到达 nums[n-1]

题目链接: https://leetcode.com/problems/jump-game-ii/

解题思路:
这道题可以使用贪心算法来解决。我们的目标是用最少的跳跃次数到达数组的最后一个位置。

贪心策略：在每一步中，我们都选择能够到达的最远位置的下一步。

具体来说，我们维护三个变量：
1. current_end: 当前能够到达的最远边界
2. current_farthest: 在遍历过程中找到的从当前位置可以到达的最远位置
3. jumps: 记录跳跃次数

当我们遍历数组时，每当我们到达current_end，就意味着我们需要进行一次跳跃，此时将jumps加1，并将current_end更新为current_farthest。

时间复杂度: O(n)，其中n是数组的长度。我们只需要遍历数组一次。
空间复杂度: O(1)，只使用了常数级别的额外空间。

此外，我们还提供两种其他解法：
1. 动态规划解法：时间复杂度O(n^2)，空间复杂度O(n)
2. BFS解法：将问题视为图中的最短路径问题，时间复杂度O(n^2)，空间复杂度O(n)
"""

class Solution:
    def jump_greedy(self, nums: List[int]) -> int:
        """
        解法一: 贪心算法（最优解）
        
        Args:
            nums: 非负整数数组
        
        Returns:
            到达最后一个位置的最小跳跃次数
        """
        # 参数校验
        if not nums or len(nums) <= 1:
            return 0  # 如果数组为空或只有一个元素，不需要跳跃
        
        jumps = 0          # 跳跃次数
        current_end = 0    # 当前能到达的最远边界
        current_farthest = 0  # 在遍历过程中找到的最远可达位置
        
        # 遍历数组，但不需要遍历到最后一个元素
        for i in range(len(nums) - 1):
            # 更新从当前位置可达的最远位置
            current_farthest = max(current_farthest, i + nums[i])
            
            # 当到达当前边界时，必须进行一次跳跃
            if i == current_end:
                jumps += 1
                current_end = current_farthest  # 更新边界为新的最远可达位置
                
                # 如果已经可以到达或超过最后一个位置，可以提前结束
                if current_end >= len(nums) - 1:
                    break
        
        return jumps
    
    def jump_dynamic_programming(self, nums: List[int]) -> int:
        """
        解法二: 动态规划
        
        Args:
            nums: 非负整数数组
        
        Returns:
            到达最后一个位置的最小跳跃次数
        """
        # 参数校验
        if not nums or len(nums) <= 1:
            return 0  # 如果数组为空或只有一个元素，不需要跳跃
        
        n = len(nums)
        # dp[i]表示到达位置i所需的最小跳跃次数
        dp = [float('inf')] * n
        dp[0] = 0  # 起始位置不需要跳跃
        
        # 计算每个位置的最小跳跃次数
        for i in range(n):
            # 如果当前位置无法到达，跳过
            if dp[i] == float('inf'):
                continue
            
            # 从当前位置可以跳跃到的所有位置
            max_jump = min(i + nums[i], n - 1)  # 确保不超过数组边界
            for j in range(i + 1, max_jump + 1):
                # 更新到达位置j的最小跳跃次数
                if dp[j] > dp[i] + 1:
                    dp[j] = dp[i] + 1
                    
                    # 如果已经到达最后一个位置，可以提前结束
                    if j == n - 1:
                        return dp[j]
        
        return dp[n - 1]
    
    def jump_bfs(self, nums: List[int]) -> int:
        """
        解法三: BFS
        将问题视为图中的最短路径问题，每个位置是一个节点，从位置i可以到i+1, i+2, ..., i+nums[i]
        
        Args:
            nums: 非负整数数组
        
        Returns:
            到达最后一个位置的最小跳跃次数
        """
        # 参数校验
        if not nums or len(nums) <= 1:
            return 0  # 如果数组为空或只有一个元素，不需要跳跃
        
        n = len(nums)
        visited = [False] * n  # 记录已经访问过的位置
        queue = []  # BFS队列
        
        # 初始化队列，起始位置是0，跳跃次数是0
        queue.append(0)
        visited[0] = True
        jumps = 0
        
        while queue:
            size = len(queue)  # 当前层的节点数
            
            # 处理当前层的所有节点
            for _ in range(size):
                current = queue.pop(0)
                
                # 如果到达最后一个位置，返回跳跃次数
                if current == n - 1:
                    return jumps
                
                # 将从当前位置可以到达的所有位置加入队列
                max_jump = min(current + nums[current], n - 1)
                # 反向遍历，优先考虑跳得更远的位置
                for j in range(max_jump, current, -1):
                    if not visited[j]:
                        visited[j] = True
                        queue.append(j)
                        
                        # 如果下一层已经可以到达最后一个位置，可以提前结束当前层的处理
                        if j == n - 1:
                            return jumps + 1
            
            jumps += 1  # 处理完一层，跳跃次数加1
        
        return -1  # 根据题目描述，一定可以到达最后一个位置，所以不会执行到这里

    def jump(self, nums: List[int]) -> int:
        """
        LeetCode官方接口的实现（使用贪心算法）
        
        Args:
            nums: 非负整数数组
        
        Returns:
            到达最后一个位置的最小跳跃次数
        """
        return self.jump_greedy(nums)

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
    result1 = solution.jump_greedy(nums)
    end_time = time.time()
    print(f"贪心算法结果: {result1}")
    print(f"贪心算法耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 测试动态规划
    start_time = time.time()
    result2 = solution.jump_dynamic_programming(nums)
    end_time = time.time()
    print(f"动态规划结果: {result2}")
    print(f"动态规划耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 测试BFS
    start_time = time.time()
    result3 = solution.jump_bfs(nums)
    end_time = time.time()
    print(f"BFS结果: {result3}")
    print(f"BFS耗时: {(end_time - start_time) * 1000:.2f}ms")

"""
生成测试用例
"""
def generate_test_case(n: int, worst_case: bool) -> List[int]:
    nums = [0] * n
    
    if worst_case:
        # 最坏情况：每次只能跳1步
        for i in range(n):
            nums[i] = 1
    else:
        # 随机情况：生成1到5之间的随机数
        for i in range(n - 1):
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
    print(f"贪心算法结果: {solution.jump_greedy(nums1)}")  # 预期输出: 2
    print(f"动态规划结果: {solution.jump_dynamic_programming(nums1)}")  # 预期输出: 2
    print(f"BFS结果: {solution.jump_bfs(nums1)}")  # 预期输出: 2
    print()
    
    # 测试用例2
    nums2 = [2, 3, 0, 1, 4]
    print("测试用例2:")
    print("nums = ", end="")
    print_array(nums2)
    print(f"贪心算法结果: {solution.jump_greedy(nums2)}")  # 预期输出: 2
    print(f"动态规划结果: {solution.jump_dynamic_programming(nums2)}")  # 预期输出: 2
    print(f"BFS结果: {solution.jump_bfs(nums2)}")  # 预期输出: 2
    print()
    
    # 测试用例3 - 边界情况：只有一个元素
    nums3 = [0]
    print("测试用例3（单元素数组）:")
    print("nums = ", end="")
    print_array(nums3)
    print(f"贪心算法结果: {solution.jump_greedy(nums3)}")  # 预期输出: 0
    print(f"动态规划结果: {solution.jump_dynamic_programming(nums3)}")  # 预期输出: 0
    print(f"BFS结果: {solution.jump_bfs(nums3)}")  # 预期输出: 0
    print()
    
    # 测试用例4 - 边界情况：每次只能跳1步
    nums4 = [1, 1, 1, 1, 1]
    print("测试用例4（每次只能跳1步）:")
    print("nums = ", end="")
    print_array(nums4)
    print(f"贪心算法结果: {solution.jump_greedy(nums4)}")  # 预期输出: 4
    print(f"动态规划结果: {solution.jump_dynamic_programming(nums4)}")  # 预期输出: 4
    print(f"BFS结果: {solution.jump_bfs(nums4)}")  # 预期输出: 4
    print()
    
    # 测试用例5 - 边界情况：可以一次跳到终点
    nums5 = [10, 1, 1, 1, 1]
    print("测试用例5（可以一次跳到终点）:")
    print("nums = ", end="")
    print_array(nums5)
    print(f"贪心算法结果: {solution.jump_greedy(nums5)}")  # 预期输出: 1
    print(f"动态规划结果: {solution.jump_dynamic_programming(nums5)}")  # 预期输出: 1
    print(f"BFS结果: {solution.jump_bfs(nums5)}")  # 预期输出: 1
    print()
    
    # 性能测试 - 小规模数组
    print("小规模数组性能测试:")
    small_array = generate_test_case(100, False)
    performance_test(small_array, solution)
    print()
    
    # 性能测试 - 大规模数组 - 只测试贪心算法，因为其他算法在大规模数组上会很慢
    print("大规模数组性能测试（只测试贪心算法）:")
    large_array = generate_test_case(10000, False)
    start_time = time.time()
    result = solution.jump_greedy(large_array)
    end_time = time.time()
    print(f"贪心算法结果: {result}")
    print(f"贪心算法耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 最坏情况性能测试
    print("\n最坏情况性能测试:")
    worst_case_array = generate_test_case(1000, True)  # 小规模的最坏情况，否则动态规划和BFS会超时
    performance_test(worst_case_array, solution)

if __name__ == "__main__":
    main()
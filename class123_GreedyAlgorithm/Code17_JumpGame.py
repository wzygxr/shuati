# 跳跃游戏
# 给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
# 数组中的每个元素代表你在该位置可以跳跃的最大长度。
# 判断你是否能够到达最后一个下标。
# 测试链接 : https://leetcode.cn/problems/jump-game/


class Solution:
    """
    贪心算法解法

    核心思想：
    1. 维护一个变量maxReach，表示当前能够到达的最远位置
    2. 遍历数组中的每个元素，对于每个位置i，更新maxReach = max(maxReach, i + nums[i])
    3. 如果在遍历过程中，发现当前位置i已经超过了maxReach，说明无法到达该位置，返回false
    4. 如果maxReach大于等于数组最后一个元素的索引，说明可以到达最后一个位置，返回true

    时间复杂度：O(n) - n是数组的长度，只需要遍历一次数组
    空间复杂度：O(1) - 只使用了常量级别的额外空间

    为什么这是最优解？
    1. 贪心策略保证了我们总是关注能够到达的最远位置
    2. 一旦发现无法继续前进，立即返回false，避免不必要的计算
    3. 无法在更优的时间复杂度内解决此问题，因为至少需要检查每个位置是否可达

    工程化考虑：
    1. 边界条件处理：空数组、单元素数组
    2. 特殊情况处理：数组中存在0元素
    3. 算法效率：尽可能提前返回，避免不必要的计算

    算法调试技巧：
    1. 可以打印每一步的maxReach值来观察算法执行过程
    2. 注意处理数组中存在0元素的情况，特别是在最后一个元素之前
    """

    def canJump(self, nums):
        # 边界条件：如果数组为空，返回false
        if not nums:
            return False

        # 边界条件：如果数组只有一个元素，已经在终点，返回true
        if len(nums) == 1:
            return True

        # 初始化能够到达的最远位置
        max_reach = 0
        n = len(nums)

        # 遍历数组中的每个元素
        for i in range(n):
            # 如果当前位置已经超过了能到达的最远位置，无法继续前进
            if i > max_reach:
                return False

            # 更新能够到达的最远位置
            max_reach = max(max_reach, i + nums[i])

            # 如果最远位置已经可以到达或超过最后一个元素的索引，返回true
            if max_reach >= n - 1:
                return True

        # 遍历结束后，如果最远位置仍然小于最后一个元素的索引，返回false
        return max_reach >= n - 1

    # 另一种实现方式，代码更简洁
    def canJump2(self, nums):
        max_reach = 0
        n = len(nums)

        for i in range(n):
            # 如果当前位置无法到达，直接返回false
            if i > max_reach:
                return False
            # 更新最远可达位置
            max_reach = max(max_reach, i + nums[i])
            # 优化：如果已经可以到达最后一个位置，直接返回true
            if max_reach >= n - 1:
                return True

        return True  # 遍历完所有位置，说明可以到达最后一个位置


# 测试函数
def test():
    solution = Solution()

    # 测试用例1: [2,3,1,1,4] -> true
    nums1 = [2, 3, 1, 1, 4]
    print(f"测试用例1: {nums1}")
    print(f"预期结果: True, 实际结果: {solution.canJump(nums1)}")
    print(f"另一种实现结果: {solution.canJump2(nums1)}")
    print()

    # 测试用例2: [3,2,1,0,4] -> false
    nums2 = [3, 2, 1, 0, 4]
    print(f"测试用例2: {nums2}")
    print(f"预期结果: False, 实际结果: {solution.canJump(nums2)}")
    print(f"另一种实现结果: {solution.canJump2(nums2)}")
    print()

    # 测试用例3: [0] -> true
    nums3 = [0]
    print(f"测试用例3: {nums3}")
    print(f"预期结果: True, 实际结果: {solution.canJump(nums3)}")
    print(f"另一种实现结果: {solution.canJump2(nums3)}")
    print()

    # 测试用例4: [1] -> true
    nums4 = [1]
    print(f"测试用例4: {nums4}")
    print(f"预期结果: True, 实际结果: {solution.canJump(nums4)}")
    print(f"另一种实现结果: {solution.canJump2(nums4)}")
    print()

    # 测试用例5: [2,0,0] -> true
    nums5 = [2, 0, 0]
    print(f"测试用例5: {nums5}")
    print(f"预期结果: True, 实际结果: {solution.canJump(nums5)}")
    print(f"另一种实现结果: {solution.canJump2(nums5)}")
    print()

    # 测试用例6: [1,1,1,0] -> true
    nums6 = [1, 1, 1, 0]
    print(f"测试用例6: {nums6}")
    print(f"预期结果: True, 实际结果: {solution.canJump(nums6)}")
    print(f"另一种实现结果: {solution.canJump2(nums6)}")


# 运行测试
if __name__ == "__main__":
    test()
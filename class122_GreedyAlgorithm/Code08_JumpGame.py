# 跳跃游戏
# 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
# 数组中的每个元素代表你在该位置可以跳跃的最大长度。
# 判断你是否能够到达最后一个下标。
# 测试链接 : https://leetcode.cn/problems/jump-game/

def canJump(nums):
    """
    跳跃游戏
    
    算法思路：
    使用贪心策略：
    1. 维护一个变量maxReach表示当前能到达的最远位置
    2. 遍历数组，对于每个位置i：
       - 如果i > maxReach，说明无法到达位置i，直接返回false
       - 否则更新maxReach = max(maxReach, i + nums[i])
    3. 如果遍历完成，说明能到达最后一个位置，返回true
    
    正确性分析：
    1. 如果能到达某个位置，那一定能到达它前面的所有位置
    2. 我们只需要关注能到达的最远位置即可
    3. 如果最远位置超过了最后一个下标，就能到达
    
    时间复杂度：O(n) - 只需要遍历数组一次
    空间复杂度：O(1) - 只使用常数额外空间
    
    :param nums: 非负整数数组，表示每个位置可以跳跃的最大长度
    :return: 是否能到达最后一个下标
    """
    maxReach = 0  # 当前能到达的最远位置
    
    # 遍历数组
    for i in range(len(nums)):
        # 如果当前位置无法到达，直接返回False
        if i > maxReach:
            return False
        
        # 更新能到达的最远位置
        maxReach = max(maxReach, i + nums[i])
        
        # 如果已经能到达最后一个位置，提前返回True
        if maxReach >= len(nums) - 1:
            return True
    
    # 遍历完成，说明能到达最后一个位置
    return True

# 测试用例
if __name__ == "__main__":
    # 测试用例1: nums = [2,3,1,1,4] -> 输出: true
    nums1 = [2, 3, 1, 1, 4]
    print("测试用例1:")
    print("数组:", nums1)
    print("能否到达最后一个下标:", canJump(nums1))  # 期望输出: True
    
    # 测试用例2: nums = [3,2,1,0,4] -> 输出: false
    nums2 = [3, 2, 1, 0, 4]
    print("\n测试用例2:")
    print("数组:", nums2)
    print("能否到达最后一个下标:", canJump(nums2))  # 期望输出: False
    
    # 测试用例3: nums = [0] -> 输出: true
    nums3 = [0]
    print("\n测试用例3:")
    print("数组:", nums3)
    print("能否到达最后一个下标:", canJump(nums3))  # 期望输出: True
    
    # 测试用例4: nums = [1,0,1,0] -> 输出: false
    nums4 = [1, 0, 1, 0]
    print("\n测试用例4:")
    print("数组:", nums4)
    print("能否到达最后一个下标:", canJump(nums4))  # 期望输出: False
"""
LeetCode 996. 正方形数组的数目

给定一个非负整数数组 A，如果该数组任意两个相邻元素的和都可以表示为某个完全平方数，
那么这个数组就称为正方形数组。返回 A 的所有可能的排列中，正方形数组的数目。

算法思路：
使用回溯算法生成所有可能的排列，并在生成过程中检查相邻元素之和是否为完全平方数。
需要注意去重，因为数组中可能有重复元素。

时间复杂度：O(n! * n)
空间复杂度：O(n)
"""

class Solution:
    def __init__(self):
        self.count = 0
    
    def numSquarefulPerms(self, nums):
        """
        返回正方形数组的数目
        :param nums: List[int] 输入数组
        :return: int 正方形数组的数目
        """
        self.count = 0
        nums.sort()  # 排序便于去重
        used = [False] * len(nums)
        self.backtrack(nums, used, -1, 0)
        return self.count
    
    def backtrack(self, nums, used, prev_index, index):
        """
        回溯函数
        :param nums: List[int] 输入数组
        :param used: List[bool] 标记数组元素是否已使用
        :param prev_index: int 前一个元素的索引
        :param index: int 当前处理的位置
        """
        # 终止条件：处理完所有元素
        if index == len(nums):
            self.count += 1
            return
        
        for i in range(len(nums)):
            # 去重：如果当前元素与前一个元素相同，且前一个元素未使用，则跳过
            if used[i] or (i > 0 and nums[i] == nums[i-1] and not used[i-1]):
                continue
            
            # 检查相邻元素之和是否为完全平方数
            if prev_index != -1 and not self.is_perfect_square(nums[prev_index] + nums[i]):
                continue
            
            used[i] = True
            self.backtrack(nums, used, i, index + 1)
            used[i] = False
    
    def is_perfect_square(self, num):
        """
        判断一个数是否为完全平方数
        :param num: int 待判断的数
        :return: bool 是否为完全平方数
        """
        sqrt_num = int(num ** 0.5)
        return sqrt_num * sqrt_num == num

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 17, 8]
    print("Input: [1, 17, 8]")
    print("Output:", solution.numSquarefulPerms(nums1))
    
    # 测试用例2
    nums2 = [2, 2, 2]
    print("\nInput: [2, 2, 2]")
    print("Output:", solution.numSquarefulPerms(nums2))
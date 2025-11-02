from typing import List

class Solution:
    """
    LeetCode 47. 全排列 II
    
    题目描述：
    给定一个可包含重复数字的序列 nums ，按任意顺序返回所有不重复的全排列。
    
    示例：
    输入：nums = [1,1,2]
    输出：[[1,1,2],[1,2,1],[2,1,1]]
    
    输入：nums = [1,2,3]
    输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
    
    提示：
    1 <= nums.length <= 8
    -10 <= nums[i] <= 10
    
    链接：https://leetcode.cn/problems/permutations-ii/
    
    算法思路：
    1. 先对数组进行排序，使相同元素相邻
    2. 使用回溯算法生成所有排列
    3. 使用布尔数组标记已使用的元素
    4. 对于重复元素，确保相同元素的相对顺序，避免生成重复排列
    
    时间复杂度：O(n * n!)，其中n是数组长度，共有n!个排列，每个排列需要O(n)时间复制
    空间复杂度：O(n)，递归栈深度和存储路径的空间
    """
    
    def permuteUnique(self, nums: List[int]) -> List[List[int]]:
        result = []
        nums.sort()  # 先排序，使相同元素相邻
        used = [False] * len(nums)
        self.backtrack(nums, used, [], result)
        return result
    
    def backtrack(self, nums: List[int], used: List[bool], path: List[int], result: List[List[int]]) -> None:
        # 终止条件：排列长度等于数组长度
        if len(path) == len(nums):
            result.append(path[:])
            return
        
        for i in range(len(nums)):
            # 跳过已使用的元素
            if used[i]:
                continue
            
            # 去重关键：如果当前元素与前一个相同，且前一个元素未被使用，则跳过
            # 这样可以确保相同元素的相对顺序，避免生成重复排列
            if i > 0 and nums[i] == nums[i - 1] and not used[i - 1]:
                continue
            
            used[i] = True
            path.append(nums[i])
            self.backtrack(nums, used, path, result)
            path.pop()
            used[i] = False

def test_permutations_ii():
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 1, 2]
    result1 = solution.permuteUnique(nums1)
    print("输入: nums = [1, 1, 2]")
    print("输出:", result1)
    
    # 测试用例2
    nums2 = [1, 2, 3]
    result2 = solution.permuteUnique(nums2)
    print("\n输入: nums = [1, 2, 3]")
    print("输出:", result2)
    
    # 测试用例3
    nums3 = [2, 2, 1, 1]
    result3 = solution.permuteUnique(nums3)
    print("\n输入: nums = [2, 2, 1, 1]")
    print("输出:", result3)

if __name__ == "__main__":
    test_permutations_ii()
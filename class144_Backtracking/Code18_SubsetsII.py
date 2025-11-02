from typing import List

class Solution:
    """
    LeetCode 90. 子集 II
    
    题目描述：
    给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
    解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
    
    示例：
    输入：nums = [1,2,2]
    输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
    
    输入：nums = [0]
    输出：[[],[0]]
    
    提示：
    1 <= nums.length <= 10
    -10 <= nums[i] <= 10
    
    链接：https://leetcode.cn/problems/subsets-ii/
    
    算法思路：
    1. 先对数组进行排序，使相同元素相邻
    2. 使用回溯算法生成所有子集
    3. 在回溯过程中，对于重复元素，只选择第一个出现的，跳过后续相同的元素
    4. 这样可以避免生成重复的子集
    
    时间复杂度：O(n * 2^n)，其中n是数组长度，共有2^n个子集，每个子集需要O(n)时间复制
    空间复杂度：O(n)，递归栈深度和存储路径的空间
    """
    
    def subsetsWithDup(self, nums: List[int]) -> List[List[int]]:
        result = []
        nums.sort()  # 先排序，使相同元素相邻
        self.backtrack(nums, 0, [], result)
        return result
    
    def backtrack(self, nums: List[int], start: int, path: List[int], result: List[List[int]]) -> None:
        # 每一步都添加到结果中
        result.append(path[:])
        
        # 从start开始遍历，避免重复
        for i in range(start, len(nums)):
            # 跳过重复元素：如果当前元素与前一个相同且不是第一个出现的，则跳过
            if i > start and nums[i] == nums[i - 1]:
                continue
            
            path.append(nums[i])  # 选择当前元素
            self.backtrack(nums, i + 1, path, result)  # 递归处理下一个元素
            path.pop()  # 撤销选择

def test_subsets_ii():
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 2, 2]
    result1 = solution.subsetsWithDup(nums1)
    print("输入: nums = [1, 2, 2]")
    print("输出:", result1)
    
    # 测试用例2
    nums2 = [0]
    result2 = solution.subsetsWithDup(nums2)
    print("\n输入: nums = [0]")
    print("输出:", result2)
    
    # 测试用例3
    nums3 = [1, 1, 2, 2]
    result3 = solution.subsetsWithDup(nums3)
    print("\n输入: nums = [1, 1, 2, 2]")
    print("输出:", result3)

if __name__ == "__main__":
    test_subsets_ii()
from typing import List

class Solution:
    """
    子集II
    测试链接：https://leetcode.cn/problems/subsets-ii/
    
    题目描述：
    给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
    解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
    
    解题思路：
    由于数组可能包含重复元素，需要先对数组排序，然后使用回溯法，在递归时跳过重复元素。
    
    时间复杂度：O(n * 2^n) - 最坏情况下需要生成2^n个子集
    空间复杂度：O(n) - 递归深度为n
    """
    
    def subsetsWithDup(self, nums: List[int]) -> List[List[int]]:
        """
        使用回溯法生成所有不重复子集
        
        Args:
            nums: 输入数组（可能包含重复元素）
            
        Returns:
            所有不重复子集的列表
        """
        result = []
        # 先排序，便于跳过重复元素
        nums.sort()
        
        def backtrack(start: int, current: List[int]):
            # 添加当前子集到结果
            result.append(current[:])
            
            for i in range(start, len(nums)):
                # 跳过重复元素，避免生成重复子集
                if i > start and nums[i] == nums[i - 1]:
                    continue
                
                current.append(nums[i])
                backtrack(i + 1, current)
                current.pop()
        
        backtrack(0, [])
        return result
    
    def subsetsWithDupBitmask(self, nums: List[int]) -> List[List[int]]:
        """
        使用位运算的变种方法（处理重复元素）
        这种方法先统计每个数字的出现次数，然后根据出现次数生成子集
        
        Args:
            nums: 输入数组
            
        Returns:
            所有不重复子集的列表
        """
        result = []
        nums.sort()
        n = len(nums)
        total_subsets = 1 << n
        
        for mask in range(total_subsets):
            subset = []
            valid = True
            
            for i in range(n):
                if mask & (1 << i):
                    # 检查是否跳过重复元素
                    if i > 0 and nums[i] == nums[i - 1] and not (mask & (1 << (i - 1))):
                        valid = False
                        break
                    subset.append(nums[i])
            
            if valid:
                result.append(subset)
        
        return result

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    test1 = [1, 2, 2]
    test2 = [0]
    test3 = [1, 1, 2]
    
    result1 = solution.subsetsWithDup(test1)
    result2 = solution.subsetsWithDup(test2)
    result3 = solution.subsetsWithDup(test3)
    
    print(f"Test 1 size: {len(result1)}")
    print(f"Test 2 size: {len(result2)}")
    print(f"Test 3 size: {len(result3)}")
    
    # 打印第一个测试用例的结果
    print("Test 1 subsets:")
    for subset in result1:
        print(subset)
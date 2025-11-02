from typing import List

class Solution:
    """
    子集
    测试链接：https://leetcode.cn/problems/subsets/
    
    题目描述：
    给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
    解集不能包含重复的子集。你可以按任意顺序返回解集。
    
    解题思路：
    使用位运算来生成所有子集。对于长度为n的数组，共有2^n个子集。
    每个子集可以用一个n位的二进制数表示，其中第i位为1表示包含nums[i]，为0表示不包含。
    
    时间复杂度：O(n * 2^n) - 需要生成2^n个子集，每个子集需要O(n)时间构建
    空间复杂度：O(n) - 不考虑输出空间
    """
    
    def subsets(self, nums: List[int]) -> List[List[int]]:
        """
        使用位运算生成所有子集
        
        Args:
            nums: 输入数组
            
        Returns:
            所有子集的列表
        """
        n = len(nums)
        total_subsets = 1 << n  # 2^n个子集
        result = []
        
        # 遍历所有可能的二进制掩码
        for mask in range(total_subsets):
            subset = []
            
            # 检查每个位，如果为1则添加对应元素
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            result.append(subset)
        
        return result
    
    def subsets_backtrack(self, nums: List[int]) -> List[List[int]]:
        """
        回溯法实现（备选方案）
        
        Args:
            nums: 输入数组
            
        Returns:
            所有子集的列表
        """
        result = []
        
        def backtrack(start: int, current: List[int]):
            result.append(current[:])
            
            for i in range(start, len(nums)):
                current.append(nums[i])
                backtrack(i + 1, current)
                current.pop()
        
        backtrack(0, [])
        return result

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    test1 = [1, 2, 3]
    test2 = [0]
    test3 = [1, 2]
    
    result1 = solution.subsets(test1)
    result2 = solution.subsets(test2)
    result3 = solution.subsets(test3)
    
    print(f"Test 1 size: {len(result1)}")
    print(f"Test 2 size: {len(result2)}")
    print(f"Test 3 size: {len(result3)}")
    
    # 打印第一个测试用例的结果
    print("Test 1 subsets:")
    for subset in result1:
        print(subset)
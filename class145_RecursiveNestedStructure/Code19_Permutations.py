# LeetCode 46. Permutations
# 全排列
# 题目来源：https://leetcode.cn/problems/permutations/

from typing import List

"""
问题描述：
给定一个不含重复数字的数组 nums，返回其所有可能的全排列。你可以按任意顺序返回答案。

解题思路：
使用回溯算法，通过递归生成所有可能的排列组合
1. 选择：从当前可用的数字中选择一个加入当前排列
2. 约束：已经选择过的数字不能再次选择
3. 目标：生成长度等于原数组长度的排列

时间复杂度：O(N * N!)，其中N是数组的长度，N!是排列的总数，每个排列需要O(N)的时间复制
空间复杂度：O(N)，递归栈的深度最多为N，另外需要O(N)的空间来标记已使用的数字
"""

class Permutations:
    """求解全排列问题的类，提供两种实现方法"""
    
    def permute(self, nums: List[int]) -> List[List[int]]:
        """
        使用used数组的回溯算法求解全排列
        
        Args:
            nums: 输入的不含重复数字的数组
            
        Returns:
            List[List[int]]: 所有可能的全排列
        """
        result = []
        if not nums:
            return result
        
        # 记录当前的排列
        current_permutation = []
        # 记录数字是否已被使用
        used = [False] * len(nums)
        
        # 开始回溯
        self._backtrack(nums, current_permutation, used, result)
        
        return result
    
    def _backtrack(self, nums: List[int], current_permutation: List[int], 
                   used: List[bool], result: List[List[int]]) -> None:
        """
        回溯函数
        
        Args:
            nums: 输入数组
            current_permutation: 当前正在构建的排列
            used: 标记数组元素是否已被使用
            result: 存储所有全排列的结果集
        """
        # 终止条件：当前排列的长度等于原数组长度，说明找到了一个完整的排列
        if len(current_permutation) == len(nums):
            # 将当前排列的副本加入结果集
            result.append(current_permutation.copy())
            return
        
        # 尝试选择每个未使用的数字
        for i in range(len(nums)):
            # 如果当前数字已经被使用，跳过
            if used[i]:
                continue
            
            # 选择当前数字
            current_permutation.append(nums[i])
            used[i] = True
            
            # 递归到下一层，构建剩余的排列
            self._backtrack(nums, current_permutation, used, result)
            
            # 回溯：撤销选择
            used[i] = False
            current_permutation.pop()
    
    def permute_by_swapping(self, nums: List[int]) -> List[List[int]]:
        """
        不使用used数组的回溯方法（通过交换元素实现）
        这种方法更节省空间，但会修改原数组
        
        Args:
            nums: 输入数组
            
        Returns:
            List[List[int]]: 所有可能的全排列
        """
        result = []
        if not nums:
            return result
        
        # 创建数组副本，避免修改原数组
        nums_copy = nums.copy()
        
        # 开始回溯（通过交换元素实现）
        self._backtrack_by_swapping(nums_copy, 0, result)
        
        return result
    
    def _backtrack_by_swapping(self, nums: List[int], start: int, result: List[List[int]]) -> None:
        """
        通过交换元素实现回溯的辅助函数
        
        Args:
            nums: 当前的数字列表
            start: 开始交换的位置
            result: 存储所有全排列的结果集
        """
        # 终止条件：当start等于nums.size()时，说明已经确定了一个排列
        if start == len(nums):
            result.append(nums.copy())
            return
        
        # 从start位置开始，尝试将每个位置的元素与start位置交换
        for i in range(start, len(nums)):
            # 交换元素
            self._swap(nums, start, i)
            
            # 递归到下一个位置
            self._backtrack_by_swapping(nums, start + 1, result)
            
            # 回溯：撤销交换
            self._swap(nums, start, i)
    
    def _swap(self, nums: List[int], i: int, j: int) -> None:
        """交换列表中的两个元素"""
        nums[i], nums[j] = nums[j], nums[i]

# 测试代码
if __name__ == "__main__":
    solution = Permutations()
    
    # 测试用例1
    nums1 = [1, 2, 3]
    print("测试用例1 - 使用used数组:")
    result1 = solution.permute(nums1)
    for permutation in result1:
        print(permutation)
    
    print("\n测试用例1 - 通过交换元素:")
    result1_by_swapping = solution.permute_by_swapping(nums1)
    for permutation in result1_by_swapping:
        print(permutation)
    
    # 测试用例2
    nums2 = [0, 1]
    print("\n测试用例2 - 使用used数组:")
    result2 = solution.permute(nums2)
    for permutation in result2:
        print(permutation)
    
    # 测试用例3
    nums3 = [1]
    print("\n测试用例3 - 使用used数组:")
    result3 = solution.permute(nums3)
    for permutation in result3:
        print(permutation)

"""
性能分析：

1. 使用used数组的方法：
   - 时间复杂度：O(N * N!)，其中N是数组长度
   - 空间复杂度：O(N)，用于存储used数组、递归栈和当前排列
   - 优点：逻辑清晰，不修改原数组
   - 缺点：需要额外的used数组空间

2. 通过交换元素的方法：
   - 时间复杂度：O(N * N!)，其中N是数组长度
   - 空间复杂度：O(N)，主要是递归栈的空间
   - 优点：节省了used数组的空间，空间效率更高
   - 缺点：如果直接修改原数组，可能会产生副作用

Python语言特性利用：
1. 使用列表推导式和copy()方法简化列表操作
2. 利用Python的动态类型系统，代码更加简洁
3. 使用下划线前缀标记私有方法
4. 利用元组解包实现高效的元素交换

算法优化思路：
1. 剪枝优化：虽然对于标准的全排列问题没有太多剪枝空间，但在实际应用中可以根据具体条件进行剪枝
2. 并行计算：对于大规模数据，可以考虑使用multiprocessing库进行并行计算
3. 迭代实现：可以将递归实现转换为迭代实现，避免深层递归可能导致的栈溢出

工程化考量：
1. 对于包含重复元素的数组，需要修改算法以避免生成重复的排列
2. 对于非常大的排列，可以考虑使用生成器模式进行惰性计算
3. 可以添加缓存机制，在某些应用场景中避免重复计算
4. 在多线程环境中使用时，需要确保线程安全
5. 可以添加日志记录，方便调试和性能监控

回溯算法框架总结：
回溯算法是一种通过探索所有可能的候选解来找出所有解的算法。如果候选解被确认不是一个解（或者至少不是最后一个解），回溯算法会通过在上一步进行一些变化来舍弃该解，即回溯并且尝试另一种可能。

回溯算法通常用于解决以下类型的问题：
1. 排列问题：如本题的全排列
2. 组合问题：如从n个元素中选择k个元素的所有组合
3. 子集问题：找出给定集合的所有子集
4. 棋盘问题：如N皇后、数独等
5. 路径问题：如找出从起点到终点的所有路径

回溯算法的基本框架可以表示为：

def backtrack(路径, 选择列表):
    if 满足结束条件:
        将路径加入结果集
        return
    
    for 选择 in 选择列表:
        做选择
        backtrack(路径, 选择列表)
        撤销选择
"""